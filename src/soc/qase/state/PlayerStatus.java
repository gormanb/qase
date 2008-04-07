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
	LAYOUTS = 13, FRAGS = 14, FLASHES = 15, CHASE = 16, SPECTATOR = 17,
	CONFIG_ICON_OFFSET = 544;

	public static final String
	ICON_REBREATHER = "p_rebreather", ICON_ENVIRONMENT_SUIT = "p_envirosuit", ICON_POWER_SHIELD = "i_powershield",
	ICON_QUAD_DAMAGE = "p_quad", ICON_COMBAT_ARMOR = "i_combatarmor", ICON_JACKET_ARMOR = "i_jacketarmor",
	ICON_HEALTH = "i_health", ICON_BLASTER = "w_blaster", ICON_CELLS = "a_cells", ICON_RAILGUN = "w_railgun",
	ICON_SLUGS = "a_slugs", ICON_ROCKET_LAUNCHER = "w_rlauncher", ICON_ROCKETS = "a_rockets", ICON_BULLETS = "a_bullets",
	ICON_CHAINGUN = "w_chaingun", ICON_BODY_ARMOR = "i_bodyarmor", ICON_ADRENALINE = "p_adrenaline",
	ICON_SHELLS = "a_shells", ICON_GRENADES = "a_grenades", ICON_HYPERBLASTER = "w_hyperblaster", ICON_SHOTGUN = "w_shotgun",
	ICON_MACHINEGUN = "w_machinegun", ICON_SUPER_SHOTGUN = "w_sshotgun", ICON_GRENADE_LAUNCHER = "w_glauncher",
	ICON_PACK = "i_pack", ICON_BFG = "w_bfg", ICON_INVULNERABILITY = "p_invulnerability", ICON_SILENCER = "p_silencer";

/*-------------------------------------------------------------------*/
/**	Default constructor. Keys are initialised to Integer.MIN_VALUE,
 *	since certain values (eg Health) can legitimately take the usual
 *	default value of -1 */
/*-------------------------------------------------------------------*/
	public PlayerStatus()
	{
		stats = new int[32];

		for(int i = 0; i < stats.length; i++)
			stats[i] = Integer.MIN_VALUE;

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
			return Integer.MIN_VALUE;
	}

/*-------------------------------------------------------------------*/
/**	Get the Config string associated with a particular status icon, i.e
 *	HEALTH_ICON, AMMO_ICON, TIMER_ICON, etc. The returned string should
 *	match one of the ICON string constants above.
 *	@param key player status icon key
 *	@return icon's associated Config string */
/*-------------------------------------------------------------------*/
	public String getIconConfigString(int key)
	{
		return (stats[key] >= 0 ? config.getConfigString(CONFIG_ICON_OFFSET + stats[key]) : null);
	}

/*-------------------------------------------------------------------*/
/**	Checks whether or not a particular timed buff (invulnerability,
 *	quad damage, environment suit, etc) is currently active on the
 *	player; if so, returns the time remaining until the buff expires.
 *	If null is passed, then the time remaining for any active buff is
 *	returned. If no such buff is active, Integer.MIN_VALUE is returned.
 *	The string passed to this method should be one of the appropriate
 *	ICON_XYZ constants from the PlayerStatus class, e.g. ICON_INVULNERABILITY,
 *	ICON_ENVIRONMENT_SUIT, ICON_QUAD_DAMAGE; this is because  the buff
 *	is checked by examining the contents of the TIMER_ICON field in
 *	the player's status array. Note that the time is a floored integer
 *	representation of the real value - that is, a 30-second buff will
 *	begin at 29 and end with 0 being returned for the final second it
 *	is active.
 *	@param buffIcon icon string of a particular timed buff, or null to check for
 *	any buff
 *	@return the time remaining on the active buff, in seconds; or
 *	Integer.MIN_VALUE if no such buff is found */
/*-------------------------------------------------------------------*/
	public int checkTimedBuff(String buffIcon)
	{
		return (buffIcon == null || buffIcon.equals(getIconConfigString(TIMER_ICON)) ? getStatus(TIMER) : Integer.MIN_VALUE);
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
			if(stats[i] == Integer.MIN_VALUE)
				stats[i] = playerStatus.getStatus(i);
		}
	}
}
