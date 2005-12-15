//---------------------------------------------------------------------
// Name:			Config.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.info;

import soc.qase.com.message.*;

/*-------------------------------------------------------------------*/
/**	Wrapper class for game configuration strings. */
/*-------------------------------------------------------------------*/
public class Config
{
	private int configSize = 1569;
	private String[] configStrings = null;

	public static final String[] items = { 
											null, // to make inventory indices equal to this array's indices
											"body", "combat","jacket", "shard", "screen", "shield", // armor
											"v_blast", "v_shotg", "v_shotg2", "v_machn", "v_chain", "v_handgr",
											"v_launch", "v_rocket", "v_hyperb", "v_rail", "v_bfg", // weapon view models
											"shells", "bullets", "cells", "rockets", "slugs", // ammo
											"quaddama", "invulner", "silencer", "breather", "enviro", "a_head",
											"adrenal", "band", "pack", "data_cd", "power", "pyramid", "spinner",
											"pass", "key", "red_key", "c_head", "target", // special items
											"g_shotg", "g_shotg2", "g_machn", "g_chain", "grenades", "g_launch",
											"g_rocket", "g_hyperb", "g_rail", "g_bfg" // weapon ground models (i-33)
										 };

	private static int[] itemsIndex = new int[items.length];

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public Config()
	{
		configStrings = new String[configSize];

		for(int i = 0; i < configSize; i++)
			configStrings[i] = null;
	}

/*-------------------------------------------------------------------*/
/**	Get config string at specified index.
 *	@param index config string index.
 *	@return config string at specified index if available, otherwise
 *	an empty String. */
/*-------------------------------------------------------------------*/
	public String getConfigString(int index)
	{
		if(index < 0 || index > configStrings.length)
			return null;

		return configStrings[index];
	}

/*-------------------------------------------------------------------*/
/**	Set config string at specified index. Records the index at which
 *	the config strings of certain important items were stored.
 *	@param index config string index.
 *	@param configString source. */
/*-------------------------------------------------------------------*/
	public void setConfigString(int index, String configString) 
	{
		if(configString.equals(""))
			configStrings[index] = null;
		else
			configStrings[index] = configString;

		for(int i = 1; i < items.length; i++)
		{
			if(configString.indexOf(items[i]) != -1)
				itemsIndex[i] = index;
		}
	}

/*-------------------------------------------------------------------*/
/**	Resolves the index of an item's in-game model to its associated
 *	inventory index. Called by PlayerGun.
 *	@param modelIndex index of model config string
 *	@return the associated inventory index, or -1 if not found
 *	@see soc.qase.state.PlayerGun#getGunInventoryIndex(int) */
/*-------------------------------------------------------------------*/
	public static int modelIndexToInventoryIndex(int modelIndex)
	{
		for(int i = 1; i < itemsIndex.length; i++)
		{
			if(itemsIndex[i] == modelIndex)
				return (i < 41 ? i : i - 33); // accommodates ground & carried weapons
		}

		return -1;
	}

/*-------------------------------------------------------------------*/
/**	Resolves the string representation of an item's in-game model to
 *	its associated inventory index. Called by Entity.
 *	@param modelString a String representation of the item model
 *	@return the associated inventory index, or -1 if not found
 *	@see soc.qase.state.Entity#getInventoryIndex
 *	@see soc.qase.state.Entity#getModelString */
/*-------------------------------------------------------------------*/
	public static int modelStringToInventoryIndex(String modelString)
	{
		if(modelString == null)
			return -1;

		for(int i = 1; i < items.length; i++)
		{
			if(modelString.indexOf(items[i]) != -1)
				return (i < 41 ? i : i - 33); // accommodates ground & carried weapons
		}

		return -1;
	}

/*-------------------------------------------------------------------*/
/**	Resolves the inventory index of an item to the index of the config
 *	string associated with its in-game model. In the case of weapons,
 *	returns the config string of the model used while the player is
 *	carrying the weapon; that is, this is equivalent of calling
 *	getItemConfigIndex(itemInventoryIndex, false). Called by PlayerGun.
 *	@param itemInventoryIndex index of the item in the inventory
 *	@return the associated config string index, or -1 if not found
 *	@see #getItemConfigIndex(int, boolean)
 *	@see soc.qase.state.PlayerGun#getGunConfigIndex(int) */
/*-------------------------------------------------------------------*/
	public static int getItemConfigIndex(int itemInventoryIndex)
	{
		return getItemConfigIndex(itemInventoryIndex, false);
	}

/*-------------------------------------------------------------------*/
/**	Resolves the inventory index of an item to the index of the config
 *	string associated with its in-game model. In the case of a weapon,
 *	optionally returns either the config string of the model as it
 *	appears while the weapon is waiting to be picked up on the ground,
 *	or as it appears while the player is carrying it.
 *	@param itemInventoryIndex index of the item in the inventory
 *	@param groundWeapon if true and the inventory index refers to a weapon,
 *	returns the config string of the model used while the weapon is on the
 *	ground. Otherwise, returns the config string of the model used while
 *	the player is carrying the gun.
 *	@return the associated config string index, or -1 if not found
 *	@see soc.qase.state.PlayerGun#getGunConfigIndex(int) */
/*-------------------------------------------------------------------*/
	public static int getItemConfigIndex(int itemInventoryIndex, boolean groundWeapon)
	{
		if(groundWeapon && itemInventoryIndex >= 7 && itemInventoryIndex <= 17)
			return itemsIndex[itemInventoryIndex + 33];
		else
			return itemsIndex[itemInventoryIndex];
	}

/*-------------------------------------------------------------------*/
/**	Creates a duplicate of the object by cloning each of its fields.
 *	@return a deep copy of the current object */
/*-------------------------------------------------------------------*/
	public Config deepCopy()
	{
		Config con = new Config();

		for(int i = 0; i < configStrings.length; i++)
		{
			if(configStrings[i] != null)
				con.setConfigString(i, new String(configStrings[i]));
		}

		return con;
	}
}

