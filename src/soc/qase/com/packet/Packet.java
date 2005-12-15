//---------------------------------------------------------------------
// Name:			ServerReconnect.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.com.packet;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;
import soc.qase.com.message.*;

/*-------------------------------------------------------------------*/
/**	The Packet class is used when sending messages from client to
 *	host. Specifies a generic wrapper for client-host messages. */
/*-------------------------------------------------------------------*/
public class Packet
{
	private Sequence firstSequence = null;
	private Sequence secondSequence = null;
	private Message message = null;
	private int length = 0;	

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public Packet()
	{	}

/*-------------------------------------------------------------------*/
/**	Get first packet sequence number.
 *	@return first packet sequence number */
/*-------------------------------------------------------------------*/
	public Sequence getFirstSequence()
	{
		return firstSequence;
	}

/*-------------------------------------------------------------------*/
/**	Set first sequence number.
 *	@param sequence first sequence number */
/*-------------------------------------------------------------------*/
	public void setFirstSequence(Sequence sequence)
	{
		firstSequence = sequence;
	}

/*-------------------------------------------------------------------*/
/**	Get second packet sequence number.
 *	@return second packet sequence number */
/*-------------------------------------------------------------------*/
	public Sequence getSecondSequence()
	{
		return secondSequence;
	}

/*-------------------------------------------------------------------*/
/**	Set second sequence number.
 *	@param sequence second sequence number */
/*-------------------------------------------------------------------*/
	public void setSecondSequence(Sequence sequence)
	{
		secondSequence = sequence;
	}

/*-------------------------------------------------------------------*/
/**	Get packet message.
 *	@return packet message */
/*-------------------------------------------------------------------*/
	public Message getMessage()
	{
		return message;
	}

/*-------------------------------------------------------------------*/
/**	Set packet message.
 *	@param message packet message */
/*-------------------------------------------------------------------*/
	public void setMessage(Message message)
	{
		this.message = message;
	}

/*-------------------------------------------------------------------*/
/**	Get packet length.
 *	@return packet length */
/*-------------------------------------------------------------------*/
	public int getLength()
	{
		return length;
	}

/*-------------------------------------------------------------------*/
/**	Set packet length.
 *	@param length packet length */
/*-------------------------------------------------------------------*/
	public void setLength(int length)
	{
		this.length = length;
	}

/*-------------------------------------------------------------------*/
/**	Get packet bytes.
 *	@return byte array */
/*-------------------------------------------------------------------*/
	public byte[] getBytes()
	{
		byte[] result = new byte[(getMessage() instanceof ClientMove ? 56 : 0) + (getSecondSequence() == null ? 4 : 8)];

		getFirstSequence().getBytes(result, 0);

		if(getSecondSequence() != null)
			getSecondSequence().getBytes(result, 4);

		if(getMessage() != null && getMessage() instanceof ClientMove)
			Utils.copyArray(getMessage().getBytes(), result, 0, (getSecondSequence() == null ? 4 : 8), 56);
		else if(getMessage() != null)
			result = Utils.concatBytes(result, getMessage().getBytes());

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Get String representation of this object.
 *	@return String representation of this object */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		String result = "" + getFirstSequence().toString() + " ";

		if(getSecondSequence() != null)
			result += new String(getSecondSequence().toString() + " ");

		if(getMessage() != null)
			result += new String(getMessage().toString() + " ");

		return result;
	}
}
