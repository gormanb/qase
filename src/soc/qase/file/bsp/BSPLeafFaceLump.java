//---------------------------------------------------------------------
// Name:			BSPLeafFaceLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Represents the LeafFace lump of the BSP file. This stores a lookup
 *	table, associating Leaf objects with their corresponding Faces.
 *	@see BSPLeaf
 *	@see BSPFace */
/*-------------------------------------------------------------------*/
public class BSPLeafFaceLump extends BSPLump
{
	public int[] leafFaceTable = null;

/*-------------------------------------------------------------------*/
/** Constructor. Process LeafFace lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the LeafFace lump in the byte array
 *	@param len the length of the LeafFace lump */
/*-------------------------------------------------------------------*/
	public BSPLeafFaceLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process LeafFace lump data. Every 2 bytes corresponds to a different
 *	entry in the table. This method populates the LeafFace table from raw
 *	byte data. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		leafFaceTable = new int[lumpData.length / 2];

		for(int i = 0; i < leafFaceTable.length; i++)
			leafFaceTable[i] = Utils.unsignedShortValue(lumpData, i * 2);
	}
}
