//---------------------------------------------------------------------
// Name:			ServerReconnect.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;

/*-------------------------------------------------------------------*/
/**	Indicates that the client should reconnect to the server. */
/*-------------------------------------------------------------------*/
public class ServerReconnect extends Message
{

/*-------------------------------------------------------------------*/
/** Deafult constructor. */
/*-------------------------------------------------------------------*/
	public ServerReconnect()
	{
		setLength(0);
	}
}


