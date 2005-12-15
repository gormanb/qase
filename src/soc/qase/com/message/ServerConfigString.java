//---------------------------------------------------------------------
// Name:			ServerConfigString.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Message wrapper used when signalling config string from host
 *	to client. */
/*-------------------------------------------------------------------*/
public class ServerConfigString extends Message
{
	private int index = 0;
	private String configString = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source. */
/*-------------------------------------------------------------------*/
	public ServerConfigString(byte[] data, int off)
	{
		int length = 0;

		index = Utils.shortValue(data, off);
		length = Utils.stringLength(data, off + 2);
		configString = Utils.stringValue(data, off + 2, length);

		setLength(2 + length + 1);
}

/*-------------------------------------------------------------------*/
/**	Get config string index.
 *	@return string index. */
/*-------------------------------------------------------------------*/
	public int getIndex()
	{
		return index;
	}

/*-------------------------------------------------------------------*/
/**	Get config string.
 *	@return config string. */
/*-------------------------------------------------------------------*/
	public String getConfigString()
	{
		return configString;
	}

/*-------------------------------------------------------------------*/
/**	Get String representation of this object.
 *	@return String representation of this object. */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		return configString;
	}
}

