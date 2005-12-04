//---------------------------------------------------------------------
// Name:			BSPLeafLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

/*-------------------------------------------------------------------*/
/** Represents the Leaf lump of the BSP file. The Leaf Lump is the
 *	area of the file containing details of the BSP tree's leaves, which
 *	themselves contains a cluster index for determining potential
 *	visibility, a list of faces for rendering, and a list of brushes
 *	for collision detection.
 *	@see BSPLeaf */
/*-------------------------------------------------------------------*/
public class BSPLeafLump extends BSPLump
{
	public BSPLeaf[] leaves = null;

/*-------------------------------------------------------------------*/
/** Constructor. Process Leaf lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the Leaf lump in the byte array
 *	@param len the length of the Leaf lump */
/*-------------------------------------------------------------------*/
	public BSPLeafLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process Edge lump data. Every 28 bytes corresponds to a different
 *	leaf. This method sequentially creates BSPLeaf objects from raw
 *	byte data. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		leaves = new BSPLeaf[lumpData.length / 28];

		for(int i = 0; i < leaves.length; i++)
			leaves[i] = new BSPLeaf(lumpData, i * 28);
	}
}
