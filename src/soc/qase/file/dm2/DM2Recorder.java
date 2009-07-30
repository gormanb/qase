//---------------------------------------------------------------------
// Name:			DM2Recorder.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.dm2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import soc.qase.com.message.Message;
import soc.qase.com.message.ServerData;
import soc.qase.com.message.ServerDownload;
import soc.qase.com.message.ServerReconnect;
import soc.qase.com.message.ServerStuffText;
import soc.qase.com.packet.ServerPacket;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	The DM2Recorder allows the agent to automatically record a demo of
 *	itself during play. Improves upon Quake 2's standard recording
 *	facilities by allowing demos spanning multiple maps to be recorded
 *	in playable format. The incoming network stream is sampled, edited
 *	as necessary, and saved to file when the agent disconnects from
 *	the server or as an intermediate step whenever the map is changed. */
/*-------------------------------------------------------------------*/
public class DM2Recorder
{
	protected File dm2File = null;
	protected String filename = null;

	protected byte[] inData = null;
	protected byte[] blockLength = null;

	protected BufferedInputStream bufIn = null;
	protected BufferedOutputStream bufOut = null;

	protected ByteArrayOutputStream store = null;
	protected ByteArrayOutputStream storeHeader = null;

	protected boolean recording = false;

/*-------------------------------------------------------------------*/
/**	Default constructor. Prepares the DM2Recorder to start saving the
 *	session. */
/*-------------------------------------------------------------------*/
	public DM2Recorder()
	{
		initRecorder();
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Instantiates the DM2Recorder and creates the specified
 *	file.
 *	@param fName the filename under which to save the recording */
/*-------------------------------------------------------------------*/
	public DM2Recorder(String fName)
	{
		initRecorder();
		startRecording(fName);
	}

	private void initRecorder()
	{
		blockLength = new byte[4];
		store = new ByteArrayOutputStream(500 * 1024);
		storeHeader = new ByteArrayOutputStream(500 * 1024);
	}

/*-------------------------------------------------------------------*/
/**	Start recording to the specified file. Called by Proxy if the user
 *	stipulates that the session should  be saved while connecting to
 *	the game server.
 *	@param fName the filename under which to save the recording
 *	@return true if the file was successfully created, false otherwise
 *	@see soc.qase.com.Proxy#connect(String, int, String) */
/*-------------------------------------------------------------------*/
	public synchronized boolean startRecording(String fName)
	{
		if(recording)
			stopRecording();

		filename = fName;

		try
		{	bufOut = new BufferedOutputStream(new FileOutputStream(filename));	}
		catch(Exception e)
		{	return false;	}

		return (recording = true);
	}

/*-------------------------------------------------------------------*/
/**	Add a block of data to the recording. Called by Proxy to record a
 *	block of normal in-game data, as distinct from header data.
 *	@param block the block of data to be added
 *	@see #addHeader(byte[])
 *	@see soc.qase.com.Proxy#processIncomingDataPacket(byte[]) */
/*-------------------------------------------------------------------*/
	public synchronized void addData(byte[] block)
	{
		if(!recording)
			return;

		block = filterData(block);
		Utils.intToByteArray(block.length, blockLength, 0);

		try
		{
			store.write(blockLength);
			store.write(block);
		}
		catch(IOException ioe)
		{	}
	}

/*-------------------------------------------------------------------*/
/**	Add a block of header data to the recording. Called by Proxy to
 *	record the header data received upon entering a new map, as distinct
 *	from normal in-game data.
 *	@param block the block of header data to be added
 *	@see #addData(byte[])
 *	@see soc.qase.com.Proxy#processIncomingDataPacket(byte[]) */
/*-------------------------------------------------------------------*/
	public synchronized void addHeader(byte[] block)
	{
		block = filterData(block);
		Utils.intToByteArray(block.length, blockLength, 0);

		try
		{
			storeHeader.write(blockLength);
			storeHeader.write(block);
		}
		catch(IOException ioe)
		{	}
	}

	private byte[] filterData(byte[] incomingData)
	{
		byte[] temp = new byte[0];
		byte[] result = new byte[0];
		byte[] curData = new byte[0];

		if(incomingData != null)
		{
			int dataIndex = 8;

			while(dataIndex != incomingData.length)
			{
				ServerPacket sPacket = new ServerPacket(incomingData, dataIndex);
				Message msg = sPacket.getMessage();

				if(msg instanceof ServerStuffText)
				{
					String text = Utils.stringValue(incomingData, dataIndex + 1, Utils.stringLength(incomingData, dataIndex + 1));

					if(text.indexOf("precache") != -1)
						curData = new byte[]{11, 'p', 'r', 'e', 'c', 'a', 'c', 'h', 'e', '\n', '\0'};
					else if(text.indexOf("cmd configstring") != -1 || text.indexOf("cmd baseline") != -1 || text.indexOf("record") != -1)
						curData = null;
					else
						curData = Utils.extractBytes(incomingData, dataIndex, sPacket.getLength());
				}
				else if(msg instanceof ServerData)
				{
					curData = Utils.extractBytes(incomingData, dataIndex, sPacket.getLength());
					Utils.intToByteArray(65578, curData, 5);
					curData[9] = 1;
				}
				else if(msg instanceof ServerReconnect || msg instanceof ServerDownload)
					curData = null;
				else
					curData = Utils.extractBytes(incomingData, dataIndex, sPacket.getLength());

				if(curData != null)
					result = Utils.concatBytes(result, curData);

				dataIndex += sPacket.getLength();
			}
		}

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Signal to the DM2Recorder that the agent has entered a new map. The
 *	data recorded thus far is dumped to file, in preparation for the
 *	data to be received from the new map. Called by the Proxy upon
 *	receipt of a ServerReconnect message.
 *	@see soc.qase.com.Proxy#run */
/*-------------------------------------------------------------------*/
	public synchronized void newMap()
	{
		writeCurrentData();
	}

/*-------------------------------------------------------------------*/
/**	Stop recording and close the specified file. Called by Proxy when the
 *	agent disconnects from the game server.
 *	@return true if the file was successfully closed, false otherwise
 *	@see soc.qase.com.Proxy#disconnect(boolean) */
/*-------------------------------------------------------------------*/
	public synchronized boolean stopRecording()
	{
		if(!recording)
			return false;

		try
		{
			Utils.intToByteArray(-1, blockLength, 0);
			store.write(blockLength);

			writeCurrentData();

			bufOut.close();
		}
		catch(IOException ioe)
		{
			return false;
		}

		recording = false;
		return true;
	}

	private boolean writeCurrentData()
	{
		if(store.size() < 5) // no game data was recorded, only an endblock
		{
			store.reset();
			storeHeader.reset();

			return true;
		}

		try
		{	storeHeader.writeTo(bufOut);
			store.writeTo(bufOut);

			storeHeader.reset();
			store.reset();

			bufOut.flush();
		}
		catch(Exception e)
		{
			return false;
		}

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Check whether the DM2Recorder is currently recording to file.
 *	@return true if the DM2Recorder is recording, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isRecording()
	{
		return recording;
	}
}
