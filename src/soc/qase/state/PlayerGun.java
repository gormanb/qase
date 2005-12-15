//---------------------------------------------------------------------
// Name:			PlayerGun.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.state;

import soc.qase.info.Config;

/*-------------------------------------------------------------------*/
/**	Wrapper class for player gun attributes. Also encapsulates several
 *	methods allowing the programmer to resolve model indices to
 *	inventory indices and vice versa, associate guns with ammo, determine
 *	the maximum ammo that can be carried for each type, find how much
 *	each ammo pickup is worth, and various other statistics. */
/*-------------------------------------------------------------------*/
public class PlayerGun
{
	private int index = -1;
	private int frame = -1;
	private Origin offset = null;
	private Angles angles = null;
	private boolean isFiring = false;

	private static final int[][] weaponsIndex = getWeaponsArray();
	public static final int BLASTER = 7, SHOTGUN = 8, SUPER_SHOTGUN = 9,
	MACHINEGUN = 10, CHAINGUN = 11, GRENADES = 12, GRENADE_LAUNCHER = 13,
	ROCKET_LAUNCHER = 14, HYPERBLASTER = 15, RAILGUN = 16, BFG10K = 17,
	SHELLS = 18, BULLETS = 19, CELLS = 20, ROCKETS = 21, SLUGS = 22;

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public PlayerGun()
	{	}

	public static int[][] getWeaponsArray()
	{
		int[][] weaponsIndex = new int[11][3];

		// 0 = associated ammo inventory index, 1 = ammo per pickup, 2 = max ammo
		weaponsIndex[0][0] = -1;	weaponsIndex[0][1] = -1;	weaponsIndex[0][2] = -1;
		weaponsIndex[1][0] = 18;	weaponsIndex[1][1] = 10;	weaponsIndex[1][2] = 100;
		weaponsIndex[2][0] = 18;	weaponsIndex[2][1] = 10;	weaponsIndex[2][2] = 100;
		weaponsIndex[3][0] = 19;	weaponsIndex[3][1] = 50;	weaponsIndex[3][2] = 200;
		weaponsIndex[4][0] = 19;	weaponsIndex[4][1] = 50;	weaponsIndex[4][2] = 200;
		weaponsIndex[5][0] = 12;	weaponsIndex[5][1] = 5;		weaponsIndex[5][2] = 50;
		weaponsIndex[6][0] = 12;	weaponsIndex[6][1] = 5;		weaponsIndex[6][2] = 50;
		weaponsIndex[7][0] = 21;	weaponsIndex[7][1] = 5;		weaponsIndex[7][2] = 50;
		weaponsIndex[8][0] = 20;	weaponsIndex[8][1] = 50;	weaponsIndex[8][2] = 200;
		weaponsIndex[9][0] = 22;	weaponsIndex[9][1] = 10;	weaponsIndex[9][2] = 50;
		weaponsIndex[10][0] = 20;	weaponsIndex[10][1] = 50;	weaponsIndex[10][2] = 200;

		return weaponsIndex;
	}

/*-------------------------------------------------------------------*/
/**	Resolves the inventory index of a gun to the index of the config
 *	string associated with its in-game model. Uses Config.
 *	@param inventoryGunIndex index of the gun in the inventory
 *	@return the associated inventory index, or -1 if not found
 *	@see soc.qase.info.Config#getItemConfigIndex(int) */
/*-------------------------------------------------------------------*/
	public static int getGunConfigIndex(int inventoryGunIndex)
	{
		return Config.getItemConfigIndex(inventoryGunIndex);
	}

/*-------------------------------------------------------------------*/
/**	Resolves the model index of a gun (ie the 'index' field above) to
 *	the index of the weapon in the inventory. Uses Config.
 *	@param gunIndex model index of the gun
 *	@return the associated inventory index, or -1 if not found
 *	@see soc.qase.info.Config#modelIndexToInventoryIndex(int) */
/*-------------------------------------------------------------------*/
	public static int getGunInventoryIndex(int gunIndex)
	{
		return Config.modelIndexToInventoryIndex(gunIndex + 32); // model indices start at Config[31]
	}

/*-------------------------------------------------------------------*/
/**	Resolves the inventory index of a particular type of ammunition to
 *	the inventory indices of all weapons which use that ammo.
 *	@param ammoInventoryIndex index of the ammo in the inventory
 *	@return the inventory indices of all weapons which use that ammo
/*-------------------------------------------------------------------*/
	public static int[] getGunInventoryIndicesByAmmo(int ammoInventoryIndex)
	{
		int[] indices = new int[2];

		for(int i = 0; i < 11; i++)
		{
			if(weaponsIndex[i][0] == ammoInventoryIndex)
			{
				if(indices[0] > 0)
					indices[1] = i + 7;
				else
					indices[0] = i + 7;
			}
		}

		return indices;
	}

/*-------------------------------------------------------------------*/
/**	Resolves the inventory index of a particular type of ammunition to
 *	the inventory indices of all weapons which use that ammo.
 *	@param inventoryGunIndex index of the gun in the inventory
 *	@return the inventory indices of the associated ammo
/*-------------------------------------------------------------------*/
	public static int getAmmoInventoryIndexByGun(int inventoryGunIndex)
	{
		return weaponsIndex[inventoryGunIndex - 7][0];
	}

/*-------------------------------------------------------------------*/
/**	Determines the maximum amount of ammo that the agent can carry for
 *	a given gun.
 *	@param inventoryGunIndex index of the gun in the inventory
 *	@return the maximum amount of the associated ammo the player can
 *	carry */
/*-------------------------------------------------------------------*/
	public static int getMaxAmmoByGun(int inventoryGunIndex)
	{
		return weaponsIndex[inventoryGunIndex - 7][2];
	}

/*-------------------------------------------------------------------*/
/**	Determines the amount of ammo that the agent obtains upon collecting
 *	an ammo pickup for the given weapon.
 *	@param inventoryGunIndex index of the gun in the inventory
 *	@return the amount of the associated ammo obtained for each pickup
/*-------------------------------------------------------------------*/
	public static int getAmmoPerPickupByGun(int inventoryGunIndex)
	{
		return weaponsIndex[inventoryGunIndex - 7][1];
	}

/*-------------------------------------------------------------------*/
/**	Determines the maximum amount of a given ammo that the agent can
 *	carry.
 *	@param inventoryAmmoIndex index of the ammo in the inventory
 *	@return the maximum amount of that ammo the player can carry */
/*-------------------------------------------------------------------*/
	public static int getMaxAmmo(int inventoryAmmoIndex)
	{
		for(int i = 0; i < 11; i++)
		{
			if(weaponsIndex[i][0] == inventoryAmmoIndex)
				return	weaponsIndex[i][2];
		}

		return -1;
	}

/*-------------------------------------------------------------------*/
/**	Determines the amount of ammo that the agent obtains upon collecting
 *	an pickup for a given ammo type.
 *	@param inventoryAmmoIndex index of the ammo in the inventory
 *	@return the amount of the given ammo obtained for each pickup
/*-------------------------------------------------------------------*/
	public static int getAmmoPerPickup(int inventoryAmmoIndex)
	{
		for(int i = 0; i < 11; i++)
		{
			if(weaponsIndex[i][0] == inventoryAmmoIndex)
				return	weaponsIndex[i][1];
		}

		return -1;
	}

/*-------------------------------------------------------------------*/
/**	Get player gun index.
 *	@return player gun index. */
/*-------------------------------------------------------------------*/
	public int getIndex()
	{
		return index;
	}
	
/*-------------------------------------------------------------------*/
/**	Set player gun index.
 *	@param index player gun index. */
/*-------------------------------------------------------------------*/
	public void setIndex(int index)
	{
		this.index = index;
	}
	
/*-------------------------------------------------------------------*/
/**	Get player gun frame.
 *	@return player gun frame. */
/*-------------------------------------------------------------------*/
	public int getFrame()
	{
		return frame;
	}
	
/*-------------------------------------------------------------------*/
/**	Set player gun frame.
 *	@param frame player gun frame. */
/*-------------------------------------------------------------------*/
	public void setFrame(int frame)
	{
		this.frame = frame;
	}

/*-------------------------------------------------------------------*/
/**	Setermine whether the player/agent is firing his weapon.
 *	@return true if the player is firing, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isFiring()
	{
		return isFiring;
	}

/*-------------------------------------------------------------------*/
/**	Get player gun offset.
 *	@return player gun offset. */
/*-------------------------------------------------------------------*/
	public Origin getOffset()
	{
		if(offset == null) offset = new Origin();
		return offset;
	}
	
/*-------------------------------------------------------------------*/
/**	Set player gun offset.
 *	@param offset player gun offset. */
/*-------------------------------------------------------------------*/
	public void setOffset(Origin offset)
	{
		this.offset = offset;
	}
	
/*-------------------------------------------------------------------*/
/**	Get player gun angles.
 *	@return player gun angles. */
/*-------------------------------------------------------------------*/
	public Angles getAngles()
	{
		if(angles == null) angles = new Angles();
		return angles;
	}
	
/*-------------------------------------------------------------------*/
/**	Set player gun angles.
 *	@param angles player gun angles. */
/*-------------------------------------------------------------------*/
	public void setAngles(Angles angles)
	{
		this.angles = angles;
	}

/*-------------------------------------------------------------------*/
/**	Merge PlayerGun properties from an existing PlayerGun object into the
 *	current PlayerGun object. Used when assimilating cumulative updates
 *	from the Quake 2 server into the gamestate.
 *	@param playerGun source PlayerGun whose attributes should be merged
 *	into the current PlayerGun
 *	@param ammoReduced used to determine whether the gun is being fired
 *	@see soc.qase.state.World#setPlayer(Player) */
/*-------------------------------------------------------------------*/
	public void merge(PlayerGun playerGun, boolean ammoReduced)
	{
		if(playerGun == null)
			return;

		if(index == -1) index = playerGun.getIndex();
		if(frame == -1) frame = playerGun.getFrame();
		if(offset == null) offset = playerGun.offset; else offset.merge(playerGun.offset);
		if(angles == null) angles = playerGun.angles; else angles.merge(playerGun.angles);

		this.isFiring = (ammoReduced && (index == -1 || playerGun.getIndex() == index)); // ammo changed, gun did not
	}
}
