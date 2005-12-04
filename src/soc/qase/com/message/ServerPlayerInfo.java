//---------------------------------------------------------------------
// Name:			ServerPlayerInfo.java
// Author:			Bernard.Gorman@computing.dcu.ie
//					Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.state.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Message wrapper used when signalling player information from host
 *	to client. */
/*-------------------------------------------------------------------*/
public class ServerPlayerInfo extends Message
{
	private Player player = null;
	private int offset = 0;
	private int bitmask = 0;
	private byte[] data = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source. */
/*-------------------------------------------------------------------*/
	public ServerPlayerInfo(byte[] data)
	{
		int value = 1;

		this.data = data;
		player = new Player();
		bitmask = processBitmask();

		processMove();
		processAngles();
		processGun();
		processView();
		processStatus();

		setLength(offset);
	}

/*-------------------------------------------------------------------*/
/**	Get player information.
 *	@return player. */
/*-------------------------------------------------------------------*/
	public Player getPlayer()
	{
		return player;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private int processBitmask()
	{
		int result = 0;

		result = Utils.shortValue(data, offset);
		offset = offset + 2;
		return result;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void processMove()
	{
		int x = 0;
		int y = 0;
		int z = 0;
		int value = 0;

		if((bitmask & 0x0001) != 0) {
			player.setMoveType((int)data[offset]);
			offset = offset + 1;
		}
		if((bitmask & 0x0002) != 0) {
			x = Utils.shortValue(data, offset);
			y = Utils.shortValue(data, offset + 2);
			z = Utils.shortValue(data, offset + 4);
			offset = offset + 6;
			player.setOrigin(new Origin((int)(0.125 * x), (int)(0.125 * y), (int)(0.125 * z)));
		}
		if((bitmask & 0x0004) != 0) {
			x = Utils.shortValue(data, offset);
			y = Utils.shortValue(data, offset + 2);
			z = Utils.shortValue(data, offset + 4);
			offset = offset + 6;
			player.setVelocity(new Velocity((int)(0.125 * x), (int)(0.125 * y), (int)(0.125 * z)));
		}
		if((bitmask & 0x0008) != 0) {
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			player.setMoveTime(value);
			offset = offset + 1;
		}
		if((bitmask & 0x0010) != 0) {
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			player.setMoveFlags(value);
			offset = offset + 1;
		}
		if((bitmask & 0x0020) != 0) {
			player.setGravity(Utils.shortValue(data, offset));
			offset = offset + 2;
		}
	}
	
/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void processAngles()
	{
		int x = 0;
		int y = 0;
		int z = 0;
		
		if((bitmask & 0x0040) != 0) {
			x = Utils.shortValue(data, offset);
			y = Utils.shortValue(data, offset + 2);
			z = Utils.shortValue(data, offset + 4);
			offset = offset + 6;
			player.setDeltaAngles(new Angles((float)(180.0 / 32768.0 * x), (float)(180.0 / 32768.0 * y), (float)(180.0 / 32768.0 * z)));
		}
		if((bitmask & 0x0080) != 0) {
			x = (int)data[offset];
			y = (int)data[offset + 1];
			z = (int)data[offset + 2];
			if(x < 0) x = x + 256;
			if(y < 0) y = y + 256;
			if(z < 0) z = z + 256;
			offset = offset + 3;
			player.setViewOffset(new Origin(x, y, z));
		}
		if((bitmask & 0x0100) != 0) {
			x = Utils.shortValue(data, offset);
			y = Utils.shortValue(data, offset + 2);
			z = Utils.shortValue(data, offset + 4);
			offset = offset + 6;
			player.setViewAngles(new Angles((float)(180.0 / 32768.0 * x), (float)(180.0 / 32768.0 * y), (float)(180.0 / 32768.0 * z)));
		}
		if((bitmask & 0x0200) != 0) {
			x = (int)data[offset];
			y = (int)data[offset + 1];
			z = (int)data[offset + 2];
			if(x < 0) x = x + 256;
			if(y < 0) y = y + 256;
			if(z < 0) z = z + 256;
			offset = offset + 3;
			player.setKickAngles(new Angles(x, y, z));
		}
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void processGun()
	{
		int x = 0;
		int y = 0;
		int z = 0;
		int value = 0;

		if((bitmask & 0x1000) != 0) {
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			player.setGunIndex(value);
			offset = offset + 1;
		}
		if((bitmask & 0x2000) != 0) {
			player.setGunFrame((int)data[offset]);
			offset = offset + 1;
			x = (int)data[offset];
			y = (int)data[offset + 1];
			z = (int)data[offset + 2];
			if(x < 0) x = x + 256;
			if(y < 0) y = y + 256;
			if(z < 0) z = z + 256;
			offset = offset + 3;
			player.setGunOffset(new Origin(x, y, z));
			x = (int)data[offset];
			y = (int)data[offset + 1];
			z = (int)data[offset + 2];
			if(x < 0) x = x + 256;
			if(y < 0) y = y + 256;
			if(z < 0) z = z + 256;
			offset = offset + 3;
			player.setGunAngles(new Angles(x, y, z));
		}
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void processView()
	{
		int value = 0;

		if((bitmask & 0x0400) != 0) {
			float[] blend = new float[4];
			blend[0] = (float)((int)data[offset] / 255);
			blend[1] = (float)((int)data[offset + 1] / 255);
			blend[2] = (float)((int)data[offset + 2] / 255);
			blend[3] = (float)((int)data[offset + 3] / 255);
			offset = offset + 4;
			player.setBlend(blend);
		}
		if((bitmask & 0x0800) != 0) {
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			player.setFOV(value);
			offset = offset + 1;
		}
		if((bitmask & 0x4000) != 0) {
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			player.setRender(value);
			offset = offset + 1;
		}
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void processStatus()
	{
		int value = 1;

		bitmask = Utils.intValue(data, offset);
		offset = offset + 4;
		player.setStatus(bitmask, data, offset);
		for(int i = 0; i < 32; i++) {
			if((value & bitmask) != 0) {
				offset = offset + 2;
			}
			value = value * 2;
		}
	}
}


