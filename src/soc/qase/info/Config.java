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

	// weapons index: 0 = PlayerGun.index
	public static final String[] items = { 	null, "body", "combat","jacket", "shard", "screen", "shield", "v_blast",
											"v_shotg", "v_shotg2", "v_machn", "v_chain", "v_handgr", "v_launch",
											"v_rocket", "v_hyperb", "v_rail", "v_bfg", "shells", "bullets",
											"cells", "rockets", "slugs", "quaddama", "invulner", "silencer",
											"breather", "enviro", "a_head", "adrenal", "band", "pack",
											"data_cd", "power", "pyramid", "spinner", "pass", "key", "red_key",
											"c_head", "target" };

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
		if(index < 0) return new String();
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
				return i;
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
		else if(modelString.indexOf("grenades") != -1)
			return 12;

		String mString = "";
		int g_index = modelString.indexOf("g_");

		if(g_index != -1)
		{
			mString = modelString.substring(0, g_index) + "v_" + modelString.substring(g_index + 2);
			modelString = mString;
		}

		for(int i = 1; i < items.length; i++)
		{
			if(modelString.indexOf(items[i]) != -1)
				return i;
		}

		return -1;
	}

/*-------------------------------------------------------------------*/
/**	Resolves the inventory index of an item to the index of the config
 *	string associated with its in-game model. Called by PlayerGun.
 *	@param itemInventoryIndex index of the item in the inventory
 *	@return the associated config string index, or -1 if not found
 *	@see soc.qase.state.PlayerGun#getGunConfigIndex(int) */
/*-------------------------------------------------------------------*/
	public static int getItemConfigIndex(int itemInventoryIndex)
	{
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

