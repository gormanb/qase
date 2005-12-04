//---------------------------------------------------------------------
// Name:			ClientUserInfo.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;

/*-------------------------------------------------------------------*/
/**	A wrapper for a command string, decoded by the game server console. */
/*-------------------------------------------------------------------*/
public class ClientUserInfo extends Message
{
/*-------------------------------------------------------------------*/
/**	Constructor.
 *	@param clientID current client identifier
 *	@param command message to send to host */
/*-------------------------------------------------------------------*/
	public ClientUserInfo(int clientID, String command)
	{
		setType(3);
		setClientID(clientID);
		setData((command + "\0").getBytes());
	}
}

