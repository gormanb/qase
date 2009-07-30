//---------------------------------------------------------------------
// Name:			ServerDownload.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Download a file (sound, model etc.) from the server. The client, who
 *	initiates the transfer, cannot rearrange packets which arrive in the
 *	wrong order; therefore, all download network packets are reliable
 *	packets and they need the usual acknowledgement. */
/*-------------------------------------------------------------------*/
public class ServerDownload extends Message
{
	private int size = 0;
	private int percent = 0;
	private byte[] data = null;

/*-------------------------------------------------------------------*/
/** Constructor. Parses the data and extracts message details.
 *	@param data the source data */
/*-------------------------------------------------------------------*/
	public ServerDownload(byte[] data, int off)
	{
		size = (int)Utils.shortValue(data, off);
		percent = (int)data[off + 2];

		if(size < 0)
			return;

		this.data = new byte[size];

		for(int i = 0; i < size; i++)
			this.data[i] = data[off + 3 + i];

		setLength(size + 3);
	}

	public int getSize()
	{
		return size;
	}

	public int getPercent()
	{
		return percent;
	}

	public byte[] getData()
	{
		return data;
	}
}

