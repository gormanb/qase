//---------------------------------------------------------------------
// Name:			ClientNop.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;

/*-------------------------------------------------------------------*/
/**	No operation. Contains no data. */
/*-------------------------------------------------------------------*/
public class ClientNop extends Message
{
/*-------------------------------------------------------------------*/
/** Constructor.
 *	@param clientID current client identifier */
/*-------------------------------------------------------------------*/
	public ClientNop(int clientID)
	{
		setType(1);
		setClientID(clientID);
	}
}

