//---------------------------------------------------------------------
// Name:			CommunicationHandler.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import soc.qase.com.message.Connectionless;
import soc.qase.com.packet.ClientPacket;
import soc.qase.com.packet.ConnectionlessPacket;
import soc.qase.com.packet.Packet;
import soc.qase.com.packet.Sequence;

/*-------------------------------------------------------------------*/
/**	The CommunicationHandler class is a wrapper class for low-level
 *	communication between a Quake2 client and a Quake2 server. The
 *	class takes care of the physical connection, but it also
 *	implements message sending methods and current ping 
 *	information. */
/*-------------------------------------------------------------------*/
public class CommunicationHandler
{
	// connection information
	private DatagramSocket socket = null;
	private boolean connected = false;
	private String host = null;
	private int clientID = 0;
	private int port = 0;
	private boolean reliableLock = false;
	private boolean unreliableLock = false;
	private boolean reliableReceived = false;
	private boolean serverBit = false;
	private boolean clientBit = false;
	private byte[] currentData = null;

	// game information
	private long currentTime = 0;
	private int ping = 0;

	// sequence information
	private Sequence lastReliableSequence = null;
	private Sequence clientSequence = null;
	private Sequence serverSequence = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Sets the client ID of the current agent.
 *	@param clientID Client identifier used throughout a complete
 *	client-server connection (included in all messages sent). */
/*-------------------------------------------------------------------*/
	public CommunicationHandler(int clientID)
	{
		this.clientID = clientID;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void init()
	{
		ping = 0;
		currentTime = 0;
		clientSequence = new Sequence(0);
		serverSequence = new Sequence(0);
		lastReliableSequence = new Sequence(0);
	}

/*-------------------------------------------------------------------*/
/**	Check if a CommunicationHandler object is connected to a host
 *	or not.
 *	@return true if connected, otherwise false. */
/*-------------------------------------------------------------------*/
	public boolean isConnected()
	{
		return connected;
	}

/*-------------------------------------------------------------------*/
/**	Connect to specified host and port.
 *	@param host hostname.
 *	@param port portnumber.
 *	@return a boolean denoting if the connect call was successful
 *	or not. */
/*-------------------------------------------------------------------*/
	public boolean connect(String host, int port)
	{
		boolean result = false;

		try
		{
			socket = new DatagramSocket();
			socket.connect(InetAddress.getByName(host), port);

			connected = true;
			init();

			result = connected;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Disconnect from current host. */
/*-------------------------------------------------------------------*/
	public void disconnect()
	{
		if(isConnected())
		{
			connected = false;
			socket.close();
		}
	}

/*-------------------------------------------------------------------*/
/**	Send a connectionless message.
 *	@param command message to send to host. */
/*-------------------------------------------------------------------*/
	public void sendConnectionless(String command)
	{
		DatagramPacket outgoingPacket = null;
		ConnectionlessPacket packet = null;
		Connectionless message = null;
		
		try
		{
			message = new Connectionless(command);
			packet = new ConnectionlessPacket(message);

			byte[] outData = packet.getBytes();
			outgoingPacket = new DatagramPacket(outData, outData.length);

			socket.send(outgoingPacket);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

/*-------------------------------------------------------------------*/
/**	Send an unreliable ClientPacket message. This method call will
 *	return immediately after successfully sending the message.
 *	@param packet message to send to host. */
/*-------------------------------------------------------------------*/
	public void sendUnreliable(ClientPacket packet)
	{
		sendData(packet, false);
	}

/*-------------------------------------------------------------------*/
/**	Send a reliable ClientPacket message. This method call will not
 *	return until a reliable reply has been received by this object
 *	(blocking).
 *	@param packet message to send to host. */
/*-------------------------------------------------------------------*/
	public void sendReliable(ClientPacket packet)
	{
		sendData(packet, true);
	}

	private byte[] incomingBuffer = new byte[2048];
	private DatagramPacket incomingPacket = new DatagramPacket(incomingBuffer, incomingBuffer.length);

/*-------------------------------------------------------------------*/
/**	Receive data from connected server. This method call will not
 *	return until a reply has been received by this object (blocking).
 *	@return data received from host. */
/*-------------------------------------------------------------------*/
	public byte[] receiveData()
	{
		long previousTime = 0;
		byte[] incomingData = null;
		
		try
		{
			if(reliableReceived)
			{
				reliableReceived = false;
				return currentData;
			}

			socket.receive(incomingPacket);

			previousTime = currentTime;
			currentTime = System.currentTimeMillis();

			ping = (int)(currentTime - previousTime);
			incomingData = new byte[incomingPacket.getLength()];

			for(int i = 0; i < incomingPacket.getLength(); i++)
				incomingData[i] = incomingPacket.getData()[i];

			incomingData = processIncomingPacket(incomingData);
		}
		catch(Exception e)
		{	}

		return incomingData;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void sendData(Packet packet, boolean reliable)
	{
		DatagramPacket outgoingPacket = null;
		
		try
		{
			if(reliable)
			{
				clientSequence = clientSequence.getNext();
				clientSequence.setReliable(true);
				serverSequence.setReliable(serverBit);

				((ClientPacket)packet).setFirstSequence(clientSequence);
				((ClientPacket)packet).setSecondSequence(serverSequence);

				byte[] outData = packet.getBytes();
				outgoingPacket = new DatagramPacket(outData, outData.length);

				for(int i = 0; reliableReceived != true; i++)
				{
					if((i % 10) == 0)
						socket.send(outgoingPacket);

					Thread.sleep(50);
					currentData = receiveData();
				}
			}
			else
			{
				clientSequence = clientSequence.getNext();
				clientSequence.setReliable(false);
				serverSequence.setReliable(serverBit);

				((ClientPacket)packet).setFirstSequence(clientSequence);
				((ClientPacket)packet).setSecondSequence(serverSequence);

				byte[] outData = packet.getBytes();
				outgoingPacket = new DatagramPacket(outData, outData.length);

				socket.send(outgoingPacket);
			}
		}
		catch(Exception e)
		{
			// e.printStackTrace(); // always "Socket closed" - ignore
		}
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private byte[] processIncomingPacket(byte[] data)
	{
		ClientPacket currentPacket;
		Sequence sequenceOne = null;
		Sequence sequenceTwo = null;
		int length = 0;

		sequenceOne = new Sequence(data);

		if(sequenceOne.intValue() == 0x7fffffff && sequenceOne.isReliable())
			return data;
		else
		{
			serverSequence = sequenceOne;
			sequenceTwo = new Sequence(data, 4);

			if(serverSequence.isReliable())
			{
				if(serverSequence.intValue() > lastReliableSequence.intValue())
				{
					lastReliableSequence = new Sequence(serverSequence.intValue());
					serverBit = serverBit ^ true;
				}
			}

			if(sequenceTwo.isReliable() != clientBit)
			{
				clientBit = sequenceTwo.isReliable();
				reliableReceived = true;				
			}
		}

		return data;
	}

/*-------------------------------------------------------------------*/
/**	Get current ping.
 *	@return current ping. */
/*-------------------------------------------------------------------*/
	public int getPing()
	{
		return ping;
	}

/*-------------------------------------------------------------------*/
/**	Get the client sequence included in the next message sent to
 *	the connected host.
 *	@return next client sequence. */
/*-------------------------------------------------------------------*/
	public Sequence getNextSequence()
	{
		return clientSequence.getNext();
	}
}
