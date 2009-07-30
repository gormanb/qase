//---------------------------------------------------------------------
// Name:			ConnectionlessPacket.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.packet;

import soc.qase.com.message.Connectionless;

/*-------------------------------------------------------------------*/
/**	Packet wrapper used when sending connectionless messages to
 *	host. */
/*-------------------------------------------------------------------*/
public class ConnectionlessPacket extends Packet
{
/*-------------------------------------------------------------------*/
/**	Constructor. Builds a connectionless packet from the given message.
 *	@param message source message */
/*-------------------------------------------------------------------*/
	public ConnectionlessPacket(Connectionless message)
	{
		Sequence firstSequence = null;

		firstSequence = new Sequence(0x7fffffff, true);
		setFirstSequence(firstSequence);
		setMessage(message);
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Builds a connectionless packet from the given data.
 *	@param data source data */
/*-------------------------------------------------------------------*/
	public ConnectionlessPacket(byte[] data)
	{
		Sequence firstSequence = null;

		firstSequence = new Sequence(0x7fffffff, true);
		setFirstSequence(firstSequence);
		setMessage(new Connectionless(data));
	}
}

