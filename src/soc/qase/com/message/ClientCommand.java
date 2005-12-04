//---------------------------------------------------------------------
// Name:			ClientCommand.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Message wrapper used when signalling console command from
 *	client to host. */
/*-------------------------------------------------------------------*/
public class ClientCommand extends Message
{
/*-------------------------------------------------------------------*/
/**	Constructor.
 *	@param clientID current client identifier
 *	@param command message to send to host */
/*-------------------------------------------------------------------*/
	public ClientCommand(int clientID, String command)
	{
		setType(4);
		setClientID(clientID);
		setData((command + "\0").getBytes());
	}

/*-------------------------------------------------------------------*/
/**	Get message bytes.
 *	@return byte array */
/*-------------------------------------------------------------------*/
	public byte[] getBytes()
	{
		byte[] result = super.getBytes();

		if(getData() != null)
			result = Utils.concatBytes(result, getData());

		return result;
	}
}


