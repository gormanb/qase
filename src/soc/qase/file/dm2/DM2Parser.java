//---------------------------------------------------------------------
// Name:			DM2Parser.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.dm2;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.com.packet.*;
import soc.qase.tools.Utils;
import soc.qase.com.message.*;

/*-------------------------------------------------------------------*/
/**	Quake 2’s inbuilt client, used by human players to connect
 *	to the game server, facilitates the recording of matches from
 *	the perspective of each individual player. These demo or
 *	DM2 files contain an edited copy of the network packet
 *	stream received by the client during the game session,
 *	capturing the player’s actions and the state of all entities at
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
	private int fileOffset = 0;
	private byte[] fileContents = null;

	private boolean EOF = false;
	private boolean fileOpen = false;

	private Packet packet = null;
	private int incomingDataIndex = 0;
	private int incomingDataLength = 0;

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
			fName = new String(filename);
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
	
			incomingDataIndex = 0;
			incomingDataLength = incomingData.length;
	
			while(incomingDataIndex != incomingDataLength)
			{
				incomingData = Utils.removeBytes(incomingData, incomingDataIndex);
				incomingDataLength = incomingData.length;
				packet = new ServerPacket(incomingData);
				processServerPacket((ServerPacket)packet);
				incomingDataIndex = packet.getLength();
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

		return world;
	}

/*-------------------------------------------------------------------*/
/**	Skip to a particular block in the demo. Note that this is NOT the frame
 *	number, which may start at any number depending on how much time
 *	elapsed between the time the server was started and the time the
 *	client connected. Note also that the blockNum is zero-indexed. In
 *	short, passing b will return the (b+1)th gamestate in the demo.
 *	@param blockNum the block to which the parser should skip
 *	@return the gamestate at the specified block */
/*-------------------------------------------------------------------*/
	public synchronized World goToBlock(int blockNum)
	{
		return goToBlock(-1, blockNum);
	}

/*-------------------------------------------------------------------*/
/**	Skip to a particular block in a particular map of a multi-map demo.
 *	Note that this is NOT the frame number, which may start at any
 *	number depending on how much time elapsed between the time the
 *	server was started and the time the client connected. Note also
 *	that mapNum and blockNum are zero-indexed. In short, passing (m, b)
 *	will return the (b+1)th gamestate of the (m+1)th map in the demo.
 *	Passing -1 for mapNum will ignore the map number, rendering the
 *	method identical to goToBlock(int).
 *	@param mapNum the map within the demo to which the parser should skip
 *	@param blockNum the block within the map to which the parser should skip
 *	@return the gamestate at the specified block */
/*-------------------------------------------------------------------*/
	public synchronized World goToBlock(int mapNum, int blockNum)
	{
		if(!fileOpen)
			return null;

		int curBlockNum = -1;
		long filePosition = 0;
		World oldWorld = world;
		boolean tempVerbose = verbose;

		try
		{	filePosition = bufIn.getFilePointer();	}
		catch(IOException ioe)
		{	}

		reset();
		verbose = false;

		do
		{
			curBlockNum++;
			world = getNextWorld();
		}
		while(curBlockNum != blockNum && mapNumber != mapNum && world != null);

		verbose = tempVerbose;
		
		if(world == null)
		{
			world = oldWorld;

			try
			{	bufIn.seek(filePosition);	}
			catch(IOException ioe)
			{	}

			return null;
		}
		
		return world;
	}

/*-------------------------------------------------------------------*/
/**	Check whether the DM2Parser has a file open.
 *	@return true if a file is currently open, false otherwise */
/*-------------------------------------------------------------------*/
	public synchronized boolean isFileOpen()
	{
		return fileOpen;
	}

	private synchronized boolean isEOF()
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
 *	incrementing the map number (used by the goToBlock methods).
 *	@param message the ServerData message */
/*-------------------------------------------------------------------*/
	protected void processServerData(ServerData message)
	{
		super.processServerData(message);
		mapNumber++;		
	}
}
