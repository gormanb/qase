//---------------------------------------------------------------------
// Name:			ServerCenterPrint.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	A message containing text to be printed in the centre of the screen. */
/*-------------------------------------------------------------------*/
public class ServerCenterPrint extends Message
{
	private String text_message = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source */
/*-------------------------------------------------------------------*/
	public ServerCenterPrint(byte[] data)
	{
		int strLength = Utils.stringLength(data, 0);
		text_message = Utils.stringValue(data, 0, strLength);

		setLength(strLength + 1);
	}

/*-------------------------------------------------------------------*/
/**	Get the text to be printed on screen.
 *	@return the text received from the server */
/*-------------------------------------------------------------------*/
	public String getText()
	{
		return text_message;
	}
}

