//---------------------------------------------------------------------
// Name:			ServerFrame.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.state.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Message wrapper used to store the inventory list received from
 *	the server. */
/*-------------------------------------------------------------------*/
public class ServerInventory extends Message
{
	private Inventory inventory = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source. */
/*-------------------------------------------------------------------*/
	public ServerInventory(byte[] data, int off)
	{
		inventory = new Inventory();

		for(int i = 0; i < 256; i++)
			inventory.setCount(i, Utils.shortValue(data, off + (i * 2)));

		setLength(512);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	public Inventory getInventory()
	{
		return inventory;
	}
}

