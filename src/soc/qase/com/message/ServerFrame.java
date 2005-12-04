//---------------------------------------------------------------------
// Name:			ServerFrame.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Message wrapper used when signalling frame information from host
 *	to client. */
/*-------------------------------------------------------------------*/
public class ServerFrame extends Message
{
	private int frame = 0;
	private int count = 0;
	private int deltaFrame = 0;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source. */
/*-------------------------------------------------------------------*/
	public ServerFrame(byte[] data)
	{
		int offset = 0;
		int entityNumber = 0;

		frame = Utils.intValue(data, 0);
		deltaFrame = Utils.intValue(data, 4);

		count = (int)data[9];

		if(count < 0)
			count = count + 256;

		setLength(count + 10);
	}

/*-------------------------------------------------------------------*/
/**	Get frame number.
 *	@return frame number. */
/*-------------------------------------------------------------------*/
	public int getFrame()
	{
		return frame;
	}

/*-------------------------------------------------------------------*/
/**	Get delta frame number.
 *	@return delta frame number. */
/*-------------------------------------------------------------------*/
	public int getDeltaFrame()
	{
		return deltaFrame;
	}
}
