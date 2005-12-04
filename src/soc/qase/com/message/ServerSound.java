//---------------------------------------------------------------------
// Name:			ServerSound.java
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
/**	Message wrapper used when signalling sound information from host
 *	to client. */
/*-------------------------------------------------------------------*/
public class ServerSound extends Message
{
	private Sound sound = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source */
/*-------------------------------------------------------------------*/
	public ServerSound(byte[] data)
	{
		int x = 0;
		int y = 0;
		int z = 0;
		int bitmask = 0;
		int offset = 0;

		sound = new Sound();
		bitmask = (int)data[offset++];
		sound.setIndex((int)data[offset++]);

		if((bitmask & 0x00000001) != 0)
			sound.setVolume((float)((int)data[offset++]) / 255);

		if((bitmask & 0x00000002) != 0)
			sound.setAttenuation(((int)data[offset++]) >> 6);
		else
			sound.setAttenuation(1);

		if((bitmask & 0x00000010) != 0)
			sound.setTimeOffset((float)((int)data[offset++]) / 1000);
		else
			sound.setTimeOffset(0);

		if((bitmask & 0x00000008) != 0)
		{
			int entityChannel = Utils.shortValue(data, offset);

			int entityNumber = (entityChannel >> 3);
			int soundChannel = entityChannel & 0x07;

			sound.setEntityNumber(entityNumber);
			sound.setSoundChannel(soundChannel);

			offset += 2;
		}

		if((bitmask & 0x00000004) != 0)
		{
			x = Utils.shortValue(data, offset);
			y = Utils.shortValue(data, offset + 2);
			z = Utils.shortValue(data, offset + 4);

			sound.setOrigin(new Origin(x, y, z));
			offset += 6;
		}

		setLength(offset);
	}

/*-------------------------------------------------------------------*/
/**	Get the Sound object which was constructed from the received data.
 *	@return the final Sound object */
/*-------------------------------------------------------------------*/
	public Sound getSound()
	{
		return sound;
	}
}


