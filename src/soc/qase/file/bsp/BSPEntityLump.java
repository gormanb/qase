//---------------------------------------------------------------------
// Name:			BSPEntityLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import java.util.Vector;
import java.util.StringTokenizer;

/*-------------------------------------------------------------------*/
/** Represents the Edge lump of the BSP file. The Edge Lump is the
 *	area of the file containing details of geometric edges between
 *	vertices in the map.
 *	@see BSPEdge */
/*-------------------------------------------------------------------*/
public class BSPEntityLump extends BSPLump
{
	private String entityData = null;
	private BSPModel[] models = null;

	public BSPEntity[] entities = null;

/*-------------------------------------------------------------------*/
/** Constructor. Process Edge lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the Edge lump in the byte array
 *	@param len the length of the Edge lump */
/*-------------------------------------------------------------------*/
	public BSPEntityLump(byte[] inData, int off, int len, BSPModel[] bspModels)
	{
		entityData = new String(inData, off, len);
		models = bspModels;
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process Edge lump data. Every 4 bytes corresponds to a different
 *	edge. This method sequentially creates BSPEdge objects from raw
 *	byte data. */
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
