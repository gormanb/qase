//---------------------------------------------------------------------
// Name:			BSPEntity.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import java.util.Hashtable;

import soc.qase.state.*;
import soc.qase.tools.Utils;
import soc.qase.tools.vecmath.Vector3f;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;

/*-------------------------------------------------------------------*/
/** Wrapper class for Entities in the BSP file; that is, doors, buttons,
 *	lifts, monsters, lights, weapons, and any similar environmental
 *	features. The data is parsed and the most important attributes of
 *	each entity are extracted to class variables; any additional
 *	attributes can be retrived by calling the getAttribute method. */
/*-------------------------------------------------------------------*/
public class BSPEntity
{
	public int lip = -1;
	public int dmg = -1;
	public int wait = -1;
	public int style = -1;
	public int speed = -1;
	public int accel = -1;
	public int height = -1;
	public int sounds = -1;
	public float delay = -1;

	public int brightness = -1;
	public int spawnflags = -1;

	public Origin origin = null;
	public Angles angles = null;

/*-------------------------------------------------------------------*/
/** In cases where the entity possesses an in-game model, this holds a
 *	reference to that model. */
/*-------------------------------------------------------------------*/
	public BSPModel model = null;
/*-------------------------------------------------------------------*/
/** Links teleporters to their destinations, and vice-versa. */
/*-------------------------------------------------------------------*/
	public BSPEntity teleportEdge = null;

/*-------------------------------------------------------------------*/
/** Links teleporters to destinations, doors to depression depth,
 *	conveyor belts to their paths, etc. */
/*-------------------------------------------------------------------*/
	public String target = null;
/*-------------------------------------------------------------------*/
/** Links teleport destinations to the source teleporter, doors to
 *	depression depth, conveyor belts to their paths, etc. */
/*-------------------------------------------------------------------*/
	public String targetName = null;

/*-------------------------------------------------------------------*/
/** Entity title. */
/*-------------------------------------------------------------------*/
	public String className = null;

	public boolean isItem = false;
	public boolean isModel = false;
	public boolean isWeapon = false;
	public boolean isMonster = false;
	public boolean isMiscObject = false;
	public boolean isWorldSpawn = false;

	public boolean isLift = false;
	public boolean isDoor = false;
	public boolean isSecretDoor = false;

	public boolean isButton = false;
	public boolean isWalkoverButton = false;

/*-------------------------------------------------------------------*/
/** Refers to a conveyor belt or train. */
/*-------------------------------------------------------------------*/
	public boolean isConveyor = false;
	public boolean isIllusion = false;

/*-------------------------------------------------------------------*/
/** Refers to teleporters that are only active in DeathMatch mode. */
/*-------------------------------------------------------------------*/
	public boolean isDMTeleport = false;
/*-------------------------------------------------------------------*/
/** Refers to teleporters that are active in single-player mode, and
 *	may also be active in DeathMatch mode. */
/*-------------------------------------------------------------------*/
	public boolean isNormalTeleport = false;

	public boolean isPathCorner = false;
	public boolean isTeleportDestination = false;

/*-------------------------------------------------------------------*/
/** Refers to player spawn points in DeathMatch mode. */
/*-------------------------------------------------------------------*/
	public boolean isDMStart = false;
/*-------------------------------------------------------------------*/
/** Refers to player spawn points in non-DeathMatch mode, and may also
 *	be used in DeathMatch mode. */
/*-------------------------------------------------------------------*/
	public boolean isPlayerStart = false;

/*-------------------------------------------------------------------*/
/** Internal entity ID number. Matches one of the integer constants
 *	defined in BSPEntity. */
/*-------------------------------------------------------------------*/
	public int entityType = -1;

	public static final int BSP_ITEM = 0, BSP_WEAPON = 1, BSP_MONSTER = 2, BSP_DOOR = 3, BSP_LIFT = 4,
							BSP_CONVEYOR = 5, BSP_BUTTON = 6, BSP_ILLUSION = 7, BSP_NORMAL_TELEPORT = 8,
							BSP_DM_TELEPORT = 9, BSP_SECRET_DOOR = 10, BSP_CORNER = 11, BSP_WALKOVER = 12,
							BSP_DESTINATION = 13, BSP_PLAYER_START = 14, BSP_DM_START = 15,
							BSP_MISC_OBJECT = 16, BSP_MODEL = 17, BSP_WORLDSPAWN = 18;

	private Hashtable hash = new Hashtable();

/*-------------------------------------------------------------------*/
/**	Constructor. Builds a BSPEntity from raw byte data.
 *	@param entData byte array containing entity lump data
 *	@param bspModels the array of BSP Model definitions, which is indexed
 *	by certain Entities */
/*-------------------------------------------------------------------*/
	public BSPEntity(String entData, BSPModel[] bspModels)
	{
		String key = null, value = null;
		StringTokenizer st = new StringTokenizer(entData, "\"");

		while(st.hasMoreTokens())
		{
			try
			{	st.nextToken();
				key = st.nextToken();

				st.nextToken();
				value = st.nextToken();
			}
			catch(NoSuchElementException nste)
			{
				break;
			}

			if(key.equals("origin"))
			{
				StringTokenizer st2 = new StringTokenizer(value, " ");
				origin = new Origin(Integer.parseInt(st2.nextToken()), Integer.parseInt(st2.nextToken()), Integer.parseInt(st2.nextToken()));
			}
			else if(key.equals("angle"))
				angles = new Angles(0, Integer.parseInt(value), 0);
			else if(key.equals("light"))
				brightness = Integer.parseInt(value);
			else if(key.equals("spawnflags"))
				spawnflags = Integer.parseInt(value);
			else if(key.equals("target"))
				target = value;
			else if(key.equals("targetname"))
				targetName = value;
			else if(key.equals("model"))
			{
				isModel = true;
				model = bspModels[Integer.parseInt(value.substring(1))];
			}
			else if(key.equals("speed"))
				speed = Integer.parseInt(value);
			else if(key.equals("wait"))
				wait = Integer.parseInt(value);
			else if(key.equals("lip"))
				lip = Integer.parseInt(value);
			else if(key.equals("style"))
				style = Integer.parseInt(value);
			else if(key.equals("dmg"))
				dmg = Integer.parseInt(value);
			else if(key.equals("height"))
				height = Integer.parseInt(value);
			else if(key.equals("accel"))
				accel = Integer.parseInt(value);
			else if(key.equals("sounds"))
				sounds = Integer.parseInt(value);
			else if(key.equals("delay"))
				delay = Float.parseFloat(value);
			else if(key.equals("classname"))
			{	className = value;

				if(value.equals("worldspawn"))
				{
					isWorldSpawn = true;
					entityType = BSP_WORLDSPAWN;
				}
				else if(value.equals("info_player_start"))
				{
					isPlayerStart = true;
					entityType = BSP_PLAYER_START;
				}
				else if(value.equals("info_player_deathmatch"))
				{
					isDMStart = true;
					entityType = BSP_DM_START;
				}
				else if(value.indexOf("weapon_") >= 0)
				{
					isWeapon = true;
					entityType = BSP_WEAPON;
				}
				else if(value.indexOf("monster_") >= 0)
				{
					isMonster = true;
					entityType = BSP_MONSTER;
				}
				else if(value.indexOf("item_") >= 0)
				{
					isItem = true;
					entityType = BSP_ITEM;
				}
				else if(value.indexOf("misc_") >= 0)
				{
					isMiscObject = true;
					entityType = BSP_MISC_OBJECT;
				}
				else if(value.equals("info_teleport_destination"))
				{
					isTeleportDestination = true;
					entityType = BSP_DESTINATION;
				}
				else if(value.equals("path_corner"))
				{
					isPathCorner = true;
					entityType = BSP_CORNER;
				}
				else if(value.equals("trigger_teleport"))
				{
					isNormalTeleport = true;
					entityType = BSP_NORMAL_TELEPORT;
				}
				else if(value.equals("trigger_multiple"))
				{
					isWalkoverButton = true;
					entityType = BSP_WALKOVER;
				}
				else if(value.indexOf("func_") >= 0)
				{
					isModel = true;

					if(value.equals("func_door_secret"))
					{
						isSecretDoor = true;
						entityType = BSP_SECRET_DOOR;
					}
					else if(value.equals("func_door"))
					{
						isDoor = true;
						entityType = BSP_DOOR;
					}
					else if(value.equals("func_button"))
					{
						isButton = true;
						entityType = BSP_BUTTON;
					}
					else if(value.equals("func_plat"))
					{
						isLift = true;
						entityType = BSP_LIFT;
					}
					else if(value.equals("func_train"))
					{
						isConveyor = true;
						entityType = BSP_CONVEYOR;
					}
					else if(value.equals("func_dm_only"))
					{
						isDMTeleport = true;
						entityType = BSP_DM_TELEPORT;
					}
					else if(value.equals("func_illusionary"))
					{
						isIllusion = true;
						entityType = BSP_ILLUSION;
					}
				}
			}

			hash.put(key, value);
		}
	}

/*-------------------------------------------------------------------*/
/**	Returns the bounding box of the model associated with this entity,
 *	where applicable.
 *	@return an array of 2 Vector3f objects; the first contains the min
 *	xyz of the model's bounding box, the second contains the man xyz */
/*-------------------------------------------------------------------*/
	public Vector3f[] getBoundingBox()
	{
		if(!isModel || model == null)
			return null;
		else
			return new Vector3f[]{	model.bboxMin, model.bboxMax	};
	}

/*-------------------------------------------------------------------*/
/**	Given an attribute key, returns the value of that key from the
 *	BSPEntity's attribute hashtable.
 *	@param key the key whose attribute should be returned
 *	@return the value of the specified attribute */
/*-------------------------------------------------------------------*/
	public String getAttribute(String key)
	{	return (String)hash.get(key);	}
}
