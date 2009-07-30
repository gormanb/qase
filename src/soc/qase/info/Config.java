//---------------------------------------------------------------------
// Name:			Config.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.info;

/*-------------------------------------------------------------------*/
/**	Wrapper class for game configuration strings. */
/*-------------------------------------------------------------------*/
public class Config
{
	private int configSize = 2048;
	private String[] configStrings = null;

	// indices of subsections within the Config table
	public static final int	SECTION_STATUSBAR = 5, SECTION_MAX_CLIENTS = 30, SECTION_MODELS = 32,
							SECTION_WEAPON_SKINS = 34, SECTION_SOUNDS = 288, SECTION_IMAGES = 544,
							SECTION_LIGHT_STYLES = 800, SECTION_ITEM_NAMES = 1056, SECTION_PLAYER_SKINS = 1312;

	public static final String[] items = { 
											null, // to make inventory indices equal to this array's indices
											"/body/", "/combat/","/jacket/", "/shard/", "/screen/", "/shield/", // armor
											"/v_blast/", "/v_shotg/", "/v_shotg2/", "/v_machn/", "/v_chain/", "/v_handgr/",
											"/v_launch/", "/v_rocket/", "/v_hyperb/", "/v_rail/", "/v_bfg/", // weapon view models
											"/shells/", "/bullets/", "/cells/", "/rockets/", "/slugs/", // ammo
											"/quaddama/", "/invulner/", "/silencer/", "/breather/", "/enviro/", "/a_head/",
											"/adrenal/", "/band/", "/pack/", "/data_cd/", "/power/", "/pyramid/", "/spinner/",
											"/pass/", "/key/", "/red_key/", "/c_head/", "/target/", // special items
											"/g_shotg/", "/g_shotg2/", "/g_machn/", "/g_chain/", // weapon ground models @ 41 (i-33)
											"/grenades/", "/g_launch/", "/g_rocket/", "/g_hyperb/", "/g_rail/", "/g_bfg/",
										 	"#w_blaster.md2", "#w_shotgun.md2", "#w_sshotgun.md2", // weapon skin models @ 51 (i-44)
										 	"#w_machinegun.md2", "#w_chaingun.md2", "#a_grenades.md2", "#w_glauncher.md2",
										 	"#w_rlauncher.md2", "#w_hyperblaster.md2", "#w_railgun.md2", "#w_bfg.md2"
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
 *	@return config string at specified index if available, null otherwise.
 */
/*-------------------------------------------------------------------*/
	public String getConfigString(int index)
	{
		if(index < 0 || index > configStrings.length)
			return null;

		return configStrings[index];
	}

/*-------------------------------------------------------------------*/
/**	The Config table is logically divided into independent subsections.
 *	Game objects which refer to the table generally specify an offset
 *	from the known starting index of the contextually-relevant subsection,
 *	rather than from the beginning of the table. This method allows the
 *	user to query a particular portion of the table in a manner more suited
 *	to the indices encountered while processing the data received during
 *	a game session. The passed sectionIndex should be one of the SECTION
 *	constants defined in the Config class.
 *	@param sectionIndex global index of the start of a particular subsection
 *	within the Config table. Should be one of the SECTION constants found
 *	in the Config class.
 *	@param subOffset offset from the section start point.
 *	@return config string at specified subsection offset if available,
 *	otherwise null. */
/*-------------------------------------------------------------------*/
	public String getConfigString(int sectionIndex, int subOffset)
	{
		return getConfigString(sectionIndex + subOffset);
	}

/*-------------------------------------------------------------------*/
/**	Set config string at specified index. Records the index at which
 *	the config strings of certain important items were stored.
 *	@param index config string index.
 *	@param configString source. */
/*-------------------------------------------------------------------*/
	public void setConfigString(int index, String configString) 
	{
		if(configString == null || configString.equals(""))
			configStrings[index] = null;
		else
		{
			configStrings[index] = configString;

			for(int i = 1; i < items.length; i++)
			{
				if(configString.indexOf(items[i]) != -1)
					itemsIndex[i] = index;
			}
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
				return (i < 41 ? i : (i < 51 ? i - 33 : i - 44)); // accommodates ground/carried weapon models & weapon skins
		}

		return -1;
	}

/*-------------------------------------------------------------------*/
/**	Resolves the string representation of an item's in-game model to
 *	its associated inventory index. Called by Entity. The method will
 *	work for the model strings of weapons currently carried by the player
 *	(of the form "/v_shotg/"), weapon models on the ground before
 *	collection (of the form "g_shotg"), and weapon skin strings (of
 *	the form "#w_shotgun.md2").
 *	@param modelString a String representation of the item model
 *	@return the associated inventory index, or -1 if not found
 *	@see soc.qase.state.Entity#getInventoryIndex
 *	@see soc.qase.state.Entity#getModelString
 *	@see soc.qase.info.Config#getWeaponSkinString */
/*-------------------------------------------------------------------*/
	public static int modelStringToInventoryIndex(String modelString)
	{
		if(modelString == null)
			return -1;

		for(int i = 1; i < items.length; i++)
		{
			if(modelString.indexOf(items[i]) != -1)
				return (i < 41 ? i : (i < 51 ? i - 33 : i - 44)); // accommodates ground/carried weapon models & weapon skins
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
/**	Returns the full English name of the specified inventory item.
 *	@param itemInventoryIndex index of the item in the inventory
 *	@return the full name of the specified inventory item. */
/*-------------------------------------------------------------------*/
	public String getItemString(int itemInventoryIndex)
	{
		return getConfigString(SECTION_ITEM_NAMES, itemInventoryIndex);
	}

/*-------------------------------------------------------------------*/
/**	Returns the Config string associated with a particular element of
 *	the status bar. Each of these consists of a script specifying what
 *	should be shown on-screen.
 *	@param index the statusbar entry to return
 *	@return the definition of the statusbar element. */
/*-------------------------------------------------------------------*/
	public String getStatusBarString(int index)
	{
		return getConfigString(SECTION_STATUSBAR, index);
	}

/*-------------------------------------------------------------------*/
/**	Get the maximum number of clients allowed in the current match.
 *	@return maximum number of clients allowed to connect to the server. */
/*-------------------------------------------------------------------*/
	public int getMaxClients()
	{
		return Integer.parseInt(getConfigString(SECTION_MAX_CLIENTS));
	}

/*-------------------------------------------------------------------*/
/**	Returns the string representation of the model at the specified index.
 *	@param index the index of the model string to be returned
 *	@return the specified model string. */
/*-------------------------------------------------------------------*/
	public String getModelString(int index)
	{
		return getConfigString(SECTION_MODELS, index);
	}

/*-------------------------------------------------------------------*/
/**	Returns a player skin - that is, a string signifying the model
 *	used by an in-game avatar.
 *	@param index the index of the player skin to be returned, as given
 *	by model.getSkin().
 *	@return the specified playerskin string. */
/*-------------------------------------------------------------------*/
	public String getPlayerSkinString(int index)
	{
		return getConfigString(SECTION_PLAYER_SKINS, index);
	}

/*-------------------------------------------------------------------*/
/**	Returns the weapon skin - that is, the md2 filename which defines
 *	the weapon's appearance - of the specified weapon.
 *	@param index the index of the weapon skin to be returned, as given
 *	by model.getWeaponSkin().
 *	@return the specified weaponskin string. */
/*-------------------------------------------------------------------*/
	public String getWeaponSkinString(int index)
	{
		return getConfigString(SECTION_WEAPON_SKINS, index);
	}

/*-------------------------------------------------------------------*/
/**	Returns filename associated with a particular sound.
 *	@param index the index of the sound whose Config string (i.e. filename)
 *	is to be returned.
 *	@return the specified sound string. */
/*-------------------------------------------------------------------*/
	public String getSoundString(int index)
	{
		return getConfigString(SECTION_SOUNDS, index);
	}

/*-------------------------------------------------------------------*/
/**	Returns the string identifier of a particular image. Images in this
 *	context refer to the small icons used to signify different weapons
 *	on the status bar, the portraits of the characters on the
 *	scoreboard, etc.
 *	@param index the index of the image string to be returned.
 *	@return the specified image string. */
/*-------------------------------------------------------------------*/
	public String getImageString(int index)
	{
		return getConfigString(SECTION_IMAGES, index);
	}

/*-------------------------------------------------------------------*/
/**	Returns the specified lighting style string.
 *	@param index lighting style string to be returned.
 *	@return the specified lighting string. */
/*-------------------------------------------------------------------*/
	public String getLightStyleString(int index)
	{
		return getConfigString(SECTION_LIGHT_STYLES, index);
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
				con.setConfigString(i, configStrings[i]);
		}

		return con;
	}
}

