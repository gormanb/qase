//---------------------------------------------------------------------
// Name:			Inventory.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.state;

import java.util.Arrays;

import soc.qase.info.Config;

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
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public Inventory()
	{
		inventoryCount = new int[256];
		setCount(7, 1);
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Accepts a Config object for use in later string-based
 *	item searches.
 *	@param con the Config item to be used */
/*-------------------------------------------------------------------*/
	public Inventory(Config con)
	{
		inventoryCount = new int[256];
		setCount(7, 1);
		config = con;
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
		if(itemIndex > 0 && itemIndex <= 255)
			inventoryCount[itemIndex] = count;
	}

/*-------------------------------------------------------------------*/
/**	Reset the inventory counts to initial values. Typically used when
 *	a player dies. */
/*-------------------------------------------------------------------*/
	public void resetCount()
	{
		Arrays.fill(inventoryCount, 0);
		setCount(7, 1);
	}

/*-------------------------------------------------------------------*/
/**	Get count for specified inventory item.
 *	@param itemIndex item index. Should be one of the constants defined
 *	in the Inventory class.
 *	@return item index count. */
/*-------------------------------------------------------------------*/
	public int getCount(int itemIndex)
	{
		return (itemIndex > 0 && itemIndex <= 255 ? inventoryCount[itemIndex] : 0);
	}

/*-------------------------------------------------------------------*/
/**	Get count for specified range of inventory items.
 *	@param startIndex item index at which to start. Should be one of
 *	the constants defined in the Inventory class.
 *	@param endIndex item index at which to end, inclusive. Should be
 *	one of the constants defined in the Inventory class.
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
/**	Get count for specified inventory item. Note that this string-matching
 *	method is somewhat slower that the index methods above.
 *	@param item plain english string of the item as seen in-game, e.g.
 *	rocket launcher, hyperblaster, chaingun.
 *	@return item count. */
/*-------------------------------------------------------------------*/
	public int getCount(String item)
	{
		for(int i = 0; item != null && i < 256; i++)
		{
			if(item.equalsIgnoreCase(config.getItemString(i)))
				return inventoryCount[i];
		}

		return 0;
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
/**	Returns the full English name of the specified item.
 *	@param itemIndex index of the item in the inventory
 *	@return the full name of the specified inventory item.
 */
/*-------------------------------------------------------------------*/
	public String getItemString(int itemIndex)
	{
		return (itemIndex > 0 && itemIndex <= 255 ? config.getItemString(itemIndex) : null);
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
