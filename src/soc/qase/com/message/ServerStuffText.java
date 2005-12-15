//---------------------------------------------------------------------
// Name:			ServerStuffText.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Message wrapper used when signalling stuff text information from
 *	host to client. */
/*-------------------------------------------------------------------*/
public class ServerStuffText extends Message
{
	private String stuffString = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source */
/*-------------------------------------------------------------------*/
	public ServerStuffText(byte[] data, int off)
	{
		int stringLength = Utils.stringLength(data, off);
		stuffString = new String(Utils.stringValue(data, off, stringLength - 1));
		setLength(stringLength + 1);
	}

/*-------------------------------------------------------------------*/
/**	Get stuff string.
 *	@return stuff string. */
/*-------------------------------------------------------------------*/
	public String getStuffString()
	{
		return stuffString;
	}

/*-------------------------------------------------------------------*/
/**	Get String representation of this object.
 *	@return String representation of this object. */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		return stuffString;
	}
}

