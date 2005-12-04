//---------------------------------------------------------------------
// Name:			BSPLeafBrushLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Represents the LeafBrush lump of the BSP file. This stores a lookup
 *	table, associating Leaf objects with their corresponding Brushes.
 *	@see BSPLeaf
 *	@see BSPBrush */
/*-------------------------------------------------------------------*/
public class BSPLeafBrushLump extends BSPLump
{
	public int[] leafBrushTable = null;

/*-------------------------------------------------------------------*/
/** Constructor. Process LeafBrush lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the LeafBrush lump in the byte array
 *	@param len the length of the LeafBrush lump */
/*-------------------------------------------------------------------*/
	public BSPLeafBrushLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process LeafBrush lump data. Every 2 bytes corresponds to a different
 *	entry in the table. This method populates the LeafBrush table from raw
 *	byte data. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		leafBrushTable = new int[lumpData.length / 2];

		for(int i = 0; i < leafBrushTable.length; i++)
			leafBrushTable[i] = Utils.unsignedShortValue(lumpData, i * 2);
	}
}
