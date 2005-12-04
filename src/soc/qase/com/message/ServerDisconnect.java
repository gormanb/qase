//---------------------------------------------------------------------
// Name:			ServerDisconnect.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;

/*-------------------------------------------------------------------*/
/**	Notifies the client that it should disconnect from the server. */
/*-------------------------------------------------------------------*/
public class ServerDisconnect extends Message
{
/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public ServerDisconnect()
	{
		setLength(0);
	}
}

