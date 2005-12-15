//---------------------------------------------------------------------
// Name:			ServerBad.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;

/*-------------------------------------------------------------------*/
/**	Internal message indicating misparsed packets. Not an actual game
 *	message. */
/*-------------------------------------------------------------------*/
public class ServerBad extends Message
{
/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data the data contained within this message */
/*-------------------------------------------------------------------*/
	public ServerBad(byte[] data, int off)
	{
		setLength(data.length - 1);
	}
}

