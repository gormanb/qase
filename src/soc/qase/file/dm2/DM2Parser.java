//---------------------------------------------------------------------
// Name:			DM2Parser.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.dm2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

import soc.qase.com.message.ServerData;
import soc.qase.com.message.ServerMessageHandler;
import soc.qase.com.message.ServerReconnect;
import soc.qase.com.packet.ServerPacket;
import soc.qase.state.World;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Quake 2's inbuilt client, used by human players to connect
 *	to the game server, facilitates the recording of matches from
 *	the perspective of each individual player. These demo or
 *	DM2 files contain an edited copy of the network packet
 *	stream received by the client during the game session,
 *	capturing the player's actions and the state of all entities at
 *	each discrete time step. The DM2Parser allows QASE to read and
 *	analyse such recordings; it treats the demo file as a virtual server,
 *	'connecting' to it and reading blocks of data in exactly the
 *	same manner as it receives network packets during an online
 *	session. A copy of the gamestate is returned for each recorded
 *	frame, and the programmer may query it to retrieve whatever
 *	information (s)he requires. */
/*-------------------------------------------------------------------*/
public class DM2Parser extends ServerMessageHandler
{
	private byte[] blockLength = null;
	private byte[] incomingData = null;

	private File dm2File = null;
	private String fName = null;
	private RandomAccessFile bufIn = null;

	private int mapNumber = -1;
	private int worldNumber = -1;

	private int fileOffset = 0;
	private byte[] fileContents = null;

	private boolean EOF = false;
	private boolean fileOpen = false;

/*-------------------------------------------------------------------*/
/**	Default constructor. Prepares the DM2Parser for file loading. */
/*-------------------------------------------------------------------*/
	public DM2Parser()
	{
		world = new World(true);
		blockLength = new byte[4];		
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Instantiates the DM2Parser and loads the specified file. */
/*-------------------------------------------------------------------*/
	public DM2Parser(String filename)
	{
		world = new World(true);
		blockLength = new byte[4];

		if(filename != null)
			open(filename);
	}

/*-------------------------------------------------------------------*/
/**	Load the specified DM2 file from disk.
 *	@return true if the file was successfully opened, false otherwise.*/
/*-------------------------------------------------------------------*/
	public synchronized boolean open(String filename)
	{
		if(fileOpen)
			close();

		try
		{
			fName = filename;
			dm2File = new File(fName);

			bufIn = new RandomAccessFile(dm2File, "r");
			fileOpen = true;
		}
		catch(IOException ioe)
		{	}

		return fileOpen;
	}

/*-------------------------------------------------------------------*/
/**	Close the open DM2 file when finished reading from it. */
/*-------------------------------------------------------------------*/
	public synchronized void close()
	{
		if(!fileOpen)
			return;

		EOF = false;
		fName = null;
		fileOpen = false;

		fileOffset = 0;
		fileContents = null;

		bufIn = null;
		dm2File = null;
	}

/*-------------------------------------------------------------------*/
/**	Reopen the DM2 file and reset the file pointer to the first block. */
/*-------------------------------------------------------------------*/
	public synchronized void reset()
	{
		EOF = false;

		server = null;
		mapNumber = -1;
		worldNumber = -1;
		world = new World(true);

		try
		{	bufIn.seek(0);	}
		catch(IOException ioe)
		{	}
	}

	private synchronized boolean processNextBlock()
	{
		if(!fileOpen || EOF)
			return false;

		try
		{
			bufIn.read(blockLength);

			if(Utils.intValue(blockLength, 0) == -1)
			{
				EOF = true;
				return false;
			}
	
			incomingData = new byte[Utils.intValue(blockLength, 0)];
			bufIn.read(incomingData);

			int dataIndex = 0;

			while(dataIndex != incomingData.length)
			{
				ServerPacket packet = new ServerPacket(incomingData, dataIndex);
				processServerPacket(packet);

				dataIndex += packet.getLength();
			}
		}
		catch(IOException ioe)
		{
			return false;
		}

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Obtain the gamestate for the next frame in the demo.
 *	@return a World object representing the gamestate at the next frame */
/*-------------------------------------------------------------------*/
	public synchronized World getNextWorld()
	{
		int currentFrame = (world == null ? 0 : world.getFrame());

		do
		{
			if(!processNextBlock())
				return null;
		}
		while(world.getFrame() == currentFrame);

		worldNumber++;

		return world;
	}

/*-------------------------------------------------------------------*/
/**	Skip to a particular gamestate (world) in the demo. Note that this
 * 	is NOT the frame number, which may start at any number depending
 * 	on how much time elapsed between the time the server was started
 * 	and the time the client connected. Note also that the worldNum
 * 	is zero-indexed. In short, passing b will return the (b+1)th
 * 	gamestate in the demo.
 *	@param worldNum the gamestate to which the parser should skip
 *	@return the specified gamestate, or null if no such world exists */
/*-------------------------------------------------------------------*/
	public synchronized World goToWorld(int worldNum)
	{
		return goToWorld(-1, worldNum);
	}

/*-------------------------------------------------------------------*/
/**	Skip to a particular gamestate (world) in a particular map of a
 * 	multi-map demo. Note that this is NOT the frame number, which may
 * 	start at any number depending on how much time elapsed between the
 * 	time the server was started and the time the client connected. Note
 * 	also that mapNum and worldNum are zero-indexed. In short, passing
 * 	(m, b) will return the (b+1)th gamestate of the (m+1)th map in the
 * 	demo. Passing -1 for mapNum will ignore the map number, rendering the
 *	method identical to goToWorld(int).
 *	@param mapNum the map within the demo to which the parser should skip
 *	@param worldNum the gamestate within the map to which the parser should skip
 *	@return the specified gamestate, or null if no such world exists */
/*-------------------------------------------------------------------*/
	public synchronized World goToWorld(int mapNum, int worldNum)
	{
		if(!fileOpen)
			return null;
		else if(mapNum < 0 && worldNum < 0)
		{
			reset();
			return null;
		}

// --------------------------------------------------------------
		// save the current state of various params for quick-reset
		// if skipping to the specified gamestate fails
		int oldMapNum = mapNumber;
		int oldWorldNum = worldNumber;
		boolean tempVerbose = verbose;

// --------------------------------------------------------------

		reset();
		verbose = false;

		do	// attempt to skip to the specified gamestate
		{
			world = getNextWorld();
		}
		while(world != null && (mapNumber != mapNum || worldNumber != worldNum));

// --------------------------------------------------------------

		verbose = tempVerbose;

		// if the user tried to skip to a non-existent
		// index, reset to the previous position
		if(world == null)
		{
			goToWorld(oldMapNum, oldWorldNum);
			return null;
		}

		return world;
	}

/*-------------------------------------------------------------------*/
/**	Gets information about the maps contained within the current DM2,
 *	then moves the file point back to its previous position.
 *	@return an array of integer values; [0] indicates the number of
 *	maps in the DM2, while subsequent elements indicate the number
 *	of gamestates in the corresponding map. */
/*-------------------------------------------------------------------*/
	public synchronized int[] getMapWorldInfo()
	{
		if(!fileOpen)
			return null;

// --------------------------------------------------------------
		// save the current state of various params for quick-reset
		int oldMapNum = mapNumber;
		int oldWorldNum = worldNumber;
		boolean tempVerbose = verbose;

// --------------------------------------------------------------

		reset();
		verbose = false;

		int curWorldNum = -1;

		// Vector for temporary storage of map info
		Vector mbInfo = new Vector();

		do	// collect map info
		{
			world = getNextWorld();

			// worldNumber resets on each map change; if it has,
			// then add the current count to the list and continue
			if(world == null || worldNumber < curWorldNum)
				mbInfo.add(new Integer(curWorldNum+1));

			curWorldNum = worldNumber;
		}
		while(world != null && !isEOF());

// --------------------------------------------------------------

		// create the int array to be returned and populate it
		int[] mapWorldInfo = new int[mapNumber+2];

		// [0] indicates the number of maps
		mapWorldInfo[0] = mapNumber + 1;

		// [1.. n] indicates the number of gamestates in each map
		for(int i = 0; i < mbInfo.size(); i++)
			mapWorldInfo[i+1] = ((Integer)mbInfo.elementAt(i)).intValue();

// --------------------------------------------------------------

		// reset to the position before the method was called
		verbose = tempVerbose;
		goToWorld(oldMapNum, oldWorldNum);

		return mapWorldInfo;
	}

/*-------------------------------------------------------------------*/
/**	Gets the index of the current map within the DM2.
 *	@return the number of the current map within the DM2 (zero-indexed). */
/*-------------------------------------------------------------------*/
	public synchronized int getMapNumber()
	{
		return mapNumber;
	}

/*-------------------------------------------------------------------*/
/**	Gets the index of the current world within the current map.
 *	@return the number of the current world (zero-indexed). */
/*-------------------------------------------------------------------*/
	public synchronized int getWorldNumber()
	{
		return worldNumber;
	}

/*-------------------------------------------------------------------*/
/**	Check whether the DM2Parser has a file open.
 *	@return true if a file is currently open, false otherwise */
/*-------------------------------------------------------------------*/
	public synchronized boolean isFileOpen()
	{
		return fileOpen;
	}

/*-------------------------------------------------------------------*/
/**	Check whether the DM2Parser has reached the end of the file.
 *	@return true if end of file has been reached, false otherwise. */
/*-------------------------------------------------------------------*/
	public synchronized boolean isEOF()
	{
		return EOF;
	}

/*-------------------------------------------------------------------*/
/**	Overrides the default method in ServerMessageHandler. When notified
 *	of a server restart, the DM2Parser creates new World and Server
 *	objects to handle the new map.
 *	@param message the ServerReconnect message */
/*-------------------------------------------------------------------*/
	protected void processServerReconnect(ServerReconnect message)
	{
		if(verbose)
			System.out.println("Processing: ServerReconnect");

		server = null;
		world = new World(true);
	}

/*-------------------------------------------------------------------*/
/**	Overrides the default method in ServerMessageHandler. When entering
 *	a new map, the client receives a ServerData message containing
 *	information at the level. DM2Parser acts by recording this data and
 *	incrementing the map number (used by the goToWorld methods).
 *	@param message the ServerData message */
/*-------------------------------------------------------------------*/
	protected void processServerData(ServerData message)
	{
		super.processServerData(message);
		worldNumber = -1;
		mapNumber++;		
	}
}
