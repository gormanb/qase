//---------------------------------------------------------------------
// Name:			ServerLayout.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.info.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Message wrapper used when signalling HUD information from host
 *	to client. */
/*-------------------------------------------------------------------*/
public class ServerLayout extends Message
{
	private Layout layout = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source. */
/*-------------------------------------------------------------------*/
	public ServerLayout(byte[] data, int off)
	{
		int length = 0;
		String displayInfo = null;

		length = Utils.stringLength(data, off);
		displayInfo = Utils.stringValue(data, off, length);

		layout = new Layout(displayInfo);

		setLength(length + 1);
	}

/*-------------------------------------------------------------------*/
/**	Get layout.
 *	@return layout. */
/*-------------------------------------------------------------------*/
	public Layout getLayout()
	{
		return layout;
	}
}

