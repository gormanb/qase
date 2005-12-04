//---------------------------------------------------------------------
// Name:			Inventory.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.state;

import soc.qase.state.*;
import soc.qase.info.*;
import soc.qase.com.message.*;

/*-------------------------------------------------------------------*/
/**	Wrapper class for inventory attributes. */
/*-------------------------------------------------------------------*/
public class Inventory
{
	private Config config = null;
	private int[] inventoryCount = null;

/*-------------------------------------------------------------------*/
/*	Standard inventory values (from v3.12 on). These are defined at
 *	compile-time and can be changed at will by game modifications.
/*-------------------------------------------------------------------*/
	public static final int
	BODY_ARMOR = 1, COMBAT_ARMOR = 2, JACKET_ARMOR = 3, ARMOR_SHARD = 4,
	POWER_SCREEN = 5, POWER_SHIELD = 6, BLASTER = 7, SHOTGUN = 8,
	SUPER_SHOTGUN = 9, MACHINEGUN = 10, CHAINGUN = 11, GRENADES = 12,
	GRENADE_LAUNCHER = 13, ROCKET_LAUNCHER = 14, HYPERBLASTER = 15,
	RAILGUN = 16, BFG10K = 17, SHELLS = 18, BULLETS = 19, CELLS = 20,
	ROCKETS = 21, SLUGS = 22, QUAD_DAMAGE = 23, INVULNERABILITY = 24,
	SILENCER = 25, REBREATHER = 26, ENVIRONMENT_SUIT = 27, ANCIENT_HEAD = 28,
	ADRENALINE = 29, BANDOLIER = 30, AMMO_PACK = 31, DATA_CD = 32, POWER_CUBE = 33,
	PYRAMID_KEY = 34, DATA_SPINNER = 35, SECURITY_PASS = 36, BLUE_KEY = 37,
	RED_KEY = 38, COMMANDERS_HEAD = 39, AIRSTRIKE_MARKER = 40, HEALTH = 41;

/*-------------------------------------------------------------------*/
/**	Constructor. */
/*-------------------------------------------------------------------*/
	public Inventory()
	{
		inventoryCount = new int[256];
	}

/*-------------------------------------------------------------------*/
/**	Set current config.
 *	@param config config information. */
/*-------------------------------------------------------------------*/
	public void setConfig(Config config)
	{
		this.config = config;
	}

/*-------------------------------------------------------------------*/
/**	Set item count for specified index.
 *	@param itemIndex item index.
 *	@param count item index count. */
/*-------------------------------------------------------------------*/
	public void setCount(int itemIndex, int count)
	{
		if(itemIndex < 0) return;
		if(itemIndex > 255) return;
		inventoryCount[itemIndex] = count;
	}

/*-------------------------------------------------------------------*/
/**	Get count for specified index.
 *	@param itemIndex item index.
 *	@return item index count. */
/*-------------------------------------------------------------------*/
	public int getCount(int itemIndex)
	{
		int result = 0;

		if(itemIndex < 0) return result;
		if(itemIndex > 255) return result;
		result = inventoryCount[itemIndex];
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Get count for specified range of indices.
 *	@param startIndex item index at which to start
 *	@param endIndex item index at which to end, inclusive
 *	@return an array giving a count of each item in the range */
/*-------------------------------------------------------------------*/
	public int[] getCount(int startIndex, int endIndex)
	{
		if(startIndex > endIndex || startIndex < 0 || endIndex < 0)
			return null;
		
		int[] result = new int[endIndex - startIndex + 1];

		for(int i = 0; i < result.length; i++)
			result[i] = getCount(startIndex + i);

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Get count for specified item.
 *	@param item item.
 *	@return item count. */
/*-------------------------------------------------------------------*/
	public int getCount(String item)
	{
		int result = 0;
		
		for(int i = 0; i < 256; i++) {
			if(config.getConfigString(1056 + i).equalsIgnoreCase(item)) {
				result = inventoryCount[i];
				break;
			}
		}
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Return a copy of the entire inventory array. Useful when the
 *	programmer wants to ensure that processes performed on the copy do
 *	not permeate to the original array.
 *	@return inventory array copy */
/*-------------------------------------------------------------------*/
	public int[] getInventoryArray()
	{
		return (int[])inventoryCount.clone();
	}

/*-------------------------------------------------------------------*/
/**	Return a reference to the actual inventory array.
 *	@return inventory array */
/*-------------------------------------------------------------------*/
	public int[] getInventoryArrayReference()
	{
		return inventoryCount;
	}

/*-------------------------------------------------------------------*/
/**	Get item string for specified index.
 *	@param itemIndex item index.
 *	@return item string. */
/*-------------------------------------------------------------------*/
	public String getItemString(int itemIndex)
	{
		String result = null;

		result = new String();
		if(itemIndex < 0) return result;
		if(itemIndex > 255) return result;
		if(config == null) return result;
		result = config.getConfigString(1056 + itemIndex);
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Return a string representation of the inventory.
 *	@return a string representation of the inventory */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		String retString = "";

		for(int i = 0; i < inventoryCount.length; i++)
			retString += inventoryCount[i] + ", ";

		return retString;
	}
}
