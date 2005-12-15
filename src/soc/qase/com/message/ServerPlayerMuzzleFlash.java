//---------------------------------------------------------------------
// Name:			ServerPlayerInfo.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Player muzzle flashes and effects. Muzzle effect indices can be
 *	compared against the MZ constants in the Effects class.
 *	@see soc.qase.state.Effects
/*-------------------------------------------------------------------*/
public class ServerPlayerMuzzleFlash extends Message
{
	private int playerEntity = 0;
	private int effect = 0;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source. */
/*-------------------------------------------------------------------*/
	public ServerPlayerMuzzleFlash(byte[] data, int off)
	{
		playerEntity = Utils.shortValue(data, off);
		effect = (int)data[off + 2];
		setLength(3);
	}

/*-------------------------------------------------------------------*/
/**	Get the index of the player entity who caused the muzzle flash.
 *	@return the index of the source player entity
/*-------------------------------------------------------------------*/
	public int getEntity()
	{
		return playerEntity;
	}

/*-------------------------------------------------------------------*/
/**	Get the index of the effect. Can be compared against the constants
 *	in the Effects class.
 *	@return an integer indicating the observed effect
 *	@see soc.qase.state.Effects
/*-------------------------------------------------------------------*/
	public int getEffect()
	{
		return effect;
	}

/*-------------------------------------------------------------------*/
/**	Get a String representation of the player/muzzle effect.
 *	@return a String description of the effect
/*-------------------------------------------------------------------*/
	public String getEffectString()
	{
		String result = null;

		result = new String();
		if(playerEntity == 0) result += "blaster";
		if(playerEntity == 1) result += "machinegun";
		if(playerEntity == 2) result += "shotgun";
		if(playerEntity == 3) result += "chaingun1";
		if(playerEntity == 4) result += "chaingun2";
		if(playerEntity == 5) result += "chaingun3";
		if(playerEntity == 6) result += "railgun";
		if(playerEntity == 7) result += "rocket";
		if(playerEntity == 8) result += "grenade";
		if(playerEntity == 9) result += "login";
		if(playerEntity == 10) result += "logout";
		if(playerEntity == 11) result += "respawn";
		if(playerEntity == 12) result += "bfg";
		if(playerEntity == 13) result += "superShotgun";
		if(playerEntity == 14) result += "hyperBlaster";
		if(playerEntity == 15) result += "itemRespawn";
		if(playerEntity == 16) result += "boomergun";
		if(playerEntity == 128) result += "silenced";
		return result;
	}
}


