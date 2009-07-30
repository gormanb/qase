//---------------------------------------------------------------------
// Name:			BSPEntityLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import java.util.StringTokenizer;
import java.util.Vector;

/*-------------------------------------------------------------------*/
/** Represents the Entity lump of the BSP file. The entities lump stores
 *	game-related map information, including information about the map name,
 *	weapons, health, armor, triggers, spawn points, lights, and models to
 *	be placed in the map. */
/*-------------------------------------------------------------------*/
public class BSPEntityLump extends BSPLump
{
	private String entityData = null;
	private BSPModel[] models = null;

	public BSPEntity[] entities = null;

/*-------------------------------------------------------------------*/
/** Constructor. Initialises the Entity lump attributes and process
 *	lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the Entity lump in the byte array
 *	@param len the length of the Entity lump
 *	@param bspModels an array of BSP Model objects, with which some
 *	entries in the Entities lump will be associated */
/*-------------------------------------------------------------------*/
	public BSPEntityLump(byte[] inData, int off, int len, BSPModel[] bspModels)
	{
		entityData = new String(inData, off, len);
		models = bspModels;
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process Entity lump data. The raw Entity lump data consists of a
 *	single long string. Individual Entity objects are extracted by
 *	tokenizing this string. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		Vector eVector = new Vector();
		StringTokenizer st = new StringTokenizer(entityData,"}");

		while(st.hasMoreTokens())
			eVector.addElement(new BSPEntity(st.nextToken(), models));

		entities = new BSPEntity[eVector.size()];
		eVector.toArray(entities);

		linkTeleports();
	}

/*-------------------------------------------------------------------*/
/** Links Teleport objects to their associated teleport destination,
 *	and vice-versa. */
/*-------------------------------------------------------------------*/
	private void linkTeleports()
	{
		for(int i = 0; i < entities.length; i++)
		{
			if(entities[i].isNormalTeleport || entities[i].isDMTeleport)
			{
				for(int j = 0; j < entities.length; j++)
				{
					if(entities[j].isTeleportDestination && entities[j].targetName.equals(entities[i].target))
					{
						entities[i].teleportEdge = entities[j];
						entities[j].teleportEdge = entities[i];
					}
				}
			}
		}
	}
}
