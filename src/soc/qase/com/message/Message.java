//---------------------------------------------------------------------
// Name:			ClientUserInfo.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	The message class implements all general functionality of
 *	messages sent between client/host and host/client. */
/*-------------------------------------------------------------------*/
public class Message
{
	private int type = -1;
	private int clientID = -1;
	private byte[] data = null;
	private int length = 0;

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public Message()
	{	}

/*-------------------------------------------------------------------*/
/**	Get message type.
 *	@return message type. */
/*-------------------------------------------------------------------*/
	public int getType()
	{
		return type;
	}

/*-------------------------------------------------------------------*/
/**	Set message type.
 *	@param type message type. */
/*-------------------------------------------------------------------*/
	public void setType(int type)
	{
		this.type = type;
	}

/*-------------------------------------------------------------------*/
/**	Get client identifier.
 *	@return client identifier. */
/*-------------------------------------------------------------------*/
	public int getClientID()
	{
		return clientID;
	}

/*-------------------------------------------------------------------*/
/**	Set client identifier.
 *	@param clientID client identifier. */
/*-------------------------------------------------------------------*/
	public void setClientID(int clientID)
	{
		this.clientID = clientID;
	}

/*-------------------------------------------------------------------*/
/**	Get message data as a byte array.
 *	@return message data. */
/*-------------------------------------------------------------------*/
	public byte[] getData()
	{
		return data;
	}

/*-------------------------------------------------------------------*/
/**	Set message data.
 *	@param data message data. */
/*-------------------------------------------------------------------*/
	public void setData(byte[] data)
	{
		this.data = data;
	}

/*-------------------------------------------------------------------*/
/**	Get message length.
 *	@return message length. */
/*-------------------------------------------------------------------*/
	public int getLength()
	{
		return length;
	}

/*-------------------------------------------------------------------*/
/**	Set message length.
 *	@param length message length. */
/*-------------------------------------------------------------------*/
	public void setLength(int length)
	{
		this.length = length;
	}

/*-------------------------------------------------------------------*/
/**	Get message bytes.
 *	@return byte array. */
/*-------------------------------------------------------------------*/
	public byte[] getBytes()
	{
		int id = 0;
		byte[] type = null;
		byte[] result = null;

		if(getType() == -1)
		{
			if(getData() != null)
				result = getData();
		}
		else
		{
			if(getClientID() != -1)
			{
				id = getClientID();
				result = new byte[2];
			
				for(int i = 0; i < 2; i++)
				{
					result[i] = (byte)(id % 256);
					id = id / 256;
				}
			}

			if(getType() != -1)
			{
				type = new byte[1];
				type[0] = (byte)getType();
				result = Utils.concatBytes(result, type);
			}
		}

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Get String representation of message.
 *	@return String representation of message. */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		String result = null;

		if(getType() == -1)
		{
			if(getData() != null)
				result = new String(getData());
		}
		else
		{
			result = new String("" + getType());

			if(getClientID() != -1)
				result += " " + getClientID();

			if(getData() != null)
				result += " " + new String(getData());
		}

		return result;
	}
}

