//---------------------------------------------------------------------
// Name:			PlayerStatus.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

import soc.qase.info.*;
import soc.qase.com.message.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Wrapper class for player status attributes. This information
 *	includes a set of counters, each one associated with a key. */
/*-------------------------------------------------------------------*/
public class PlayerStatus
{
	private Config config = null;
	private boolean updated = false;

	private int[] stats = null;

	public static final int
	HEALTH_ICON = 0, HEALTH = 1, AMMO_ICON = 2, AMMO = 3, ARMOR_ICON = 4,
	ARMOR = 5, SELECTED_ICON = 6, PICKUP_ICON = 7, PICKUP_STRING = 8,
	TIMER_ICON = 9, TIMER = 10, HELPICON = 11, SELECTED_ITEM = 12,
	LAYOUTS = 13, FRAGS = 14, FLASHES = 15, CHASE = 16, SPECTATOR = 17;

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public PlayerStatus()
	{
		stats = new int[32];

		for(int i = 0; i < stats.length; i++)
			stats[i] = -1;

		stats[SELECTED_ITEM] = 7;
	}
	
/*-------------------------------------------------------------------*/
/**	Set player status config.
 *	@param config player status config. */
/*-------------------------------------------------------------------*/
	public void setConfig(Config config)
	{
		this.config = config;
	}

/*-------------------------------------------------------------------*/
/**	Set player status.
 *	@param bitmask player status bitmask.
 *	@param status player status data.
 *	@param index player status data index. */
/*-------------------------------------------------------------------*/
	public void setStatus(int bitmask, byte[] status, int index)
	{
		int value = 1;
		int offset = 0;

		offset = index;

		for(int i = 0; i < 32; i++)
		{
			if((bitmask & value) != 0)
			{
				stats[i] = Utils.shortValue(status, offset);
				offset = offset + 2;
			}

			value = value * 2;
		}
	}
	
/*-------------------------------------------------------------------*/
/**	Get player status of specified key.
 *	@param key player status key.
 *	@return player status of specified key. */
/*-------------------------------------------------------------------*/
	public int getStatus(int key)
	{
		if(key >= 0 && key < 32)
			return stats[key];
		else
			return -1;
	}

/*-------------------------------------------------------------------*/
/**	Get string representation of recent pickup.
 *	@return The configstring corresponding to the recent pickup, or
 *	null if no such pickup exists. */
/*-------------------------------------------------------------------*/
	public String getPickupString()
	{
		if(stats[PICKUP_STRING] > 0)
			return config.getConfigString(stats[PICKUP_STRING]);
		else
			return null;
	}

/*-------------------------------------------------------------------*/
/**	Merge PlayerStatus properties from an existing PlayerStatus object into the
 *	current PlayerStatus object. Used when assimilating cumulative updates
 *	from the Quake 2 server into the gamestate.
 *	@param playerStatus source PlayerStatus whose attributes should be merged
 *	into the current PlayerStatus
 *	@see soc.qase.state.World#setPlayer(Player) */
/*-------------------------------------------------------------------*/
	public void merge(PlayerStatus playerStatus)
	{
		if(stats[PICKUP_STRING] > 0)
			updated = true;
		else if(stats[PICKUP_STRING] <= 0)
			updated = false;

		for(int i = 0; i < stats.length; i++)
		{
			if(stats[i] == -1)
				stats[i] = playerStatus.getStatus(i);
		}
	}
}
