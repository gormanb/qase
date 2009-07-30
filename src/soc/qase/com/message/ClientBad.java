//---------------------------------------------------------------------
// Name:			ClientBad.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

/*-------------------------------------------------------------------*/
/**	Internal message indicating misparsed packets. Not an actual game
 *	message. */
/*-------------------------------------------------------------------*/
public class ClientBad extends Message
{

/*-------------------------------------------------------------------*/
/**	Constructor.
 *	@param clientID current client identifier */
/*-------------------------------------------------------------------*/
	public ClientBad(int clientID)
	{
		setType(0);
		setClientID(clientID);
	}
}

