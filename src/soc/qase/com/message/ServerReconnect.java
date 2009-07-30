//---------------------------------------------------------------------
// Name:			ServerReconnect.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

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


