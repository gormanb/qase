//---------------------------------------------------------------------
// Name:			ServerPrint.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Message wrapper used when signalling print information from host
 *	to client. */
/*-------------------------------------------------------------------*/
public class ServerPrint extends Message
{
	private int printLevel = 0;
	private String printString = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source. */
/*-------------------------------------------------------------------*/
	public ServerPrint(byte[] data)
	{
		int length = 0;

		printLevel = (int)data[0];
		length = Utils.stringLength(data, 1);

		printString = Utils.stringValue(data, 1, length - 1);		

		setLength(1 + length + 1);
	}

/*-------------------------------------------------------------------*/
/**	Get priority level. 0 = pickup, 1 = death, 2 = critical, 3 = chat
 *	@return print priority level. */
/*-------------------------------------------------------------------*/
	public int getPrintLevel()
	{
		return printLevel;
	}

/*-------------------------------------------------------------------*/
/**	Get print string.
 *	@return print string. */
/*-------------------------------------------------------------------*/
	public String getPrintString()
	{
		return printString;
	}

/*-------------------------------------------------------------------*/
/**	Get String representation of this object.
 *	@return String representation of this object. */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		return printString;
	}
}

