//---------------------------------------------------------------------
// Name:			Connectionless.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

/*-------------------------------------------------------------------*/
/**	Special messages sent outside the normal flow of network traffic,
 *	such as at session-setup or to interrogate and maintain the state
 *	of a server. */
/*-------------------------------------------------------------------*/
public class Connectionless extends Message
{
/*-------------------------------------------------------------------*/
/**	Constructor.
 *	@param data message to send from client to host. */
/*-------------------------------------------------------------------*/
	public Connectionless(String data)
	{
		setData(data.getBytes());
	}

/*-------------------------------------------------------------------*/
/**	Constructor.
 *	@param data message to send from client to host. */
/*-------------------------------------------------------------------*/
	public Connectionless(byte[] data)
	{
		setData(data);
	}
}

