//---------------------------------------------------------------------
// Name:			ClientPacket.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.packet;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.com.message.*;

/*-------------------------------------------------------------------*/
/**	Packet wrapper used when sending client messages to host.
 *	@author Martin Fredriksson
 *	@author Bernard Gorman */
/*-------------------------------------------------------------------*/
public class ClientPacket extends Packet
{
/*-------------------------------------------------------------------*/
/**	Constructor. Builds a client packet from the given message.
 *	@param message source message */
/*-------------------------------------------------------------------*/
	public ClientPacket(Message message)
	{
		setMessage(message);
	}
}
