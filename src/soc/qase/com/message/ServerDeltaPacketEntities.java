//---------------------------------------------------------------------
// Name:			ServerDeltaPacketEntities.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;

/*-------------------------------------------------------------------*/
/**	Entity updates. May come after a ServerPacketEntities block. It isn't
 *	really necessary, since ServerPacketEntities handles all the delta
 *	encoding itself. */
/*-------------------------------------------------------------------*/
public class ServerDeltaPacketEntities extends Message
{
/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data the source data */
/*-------------------------------------------------------------------*/
	public ServerDeltaPacketEntities(byte[] data, int off)
	{
		setLength(data.length - 1);
	}
}

