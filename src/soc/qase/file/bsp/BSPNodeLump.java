//---------------------------------------------------------------------
// Name:			BSPNodeLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

/*-------------------------------------------------------------------*/
/** Represents the Node lump of the BSP file. The node lump stores all
 *	of the nodes in the map's BSP tree. The BSP tree is used primarily
 *	as a spatial subdivision scheme, dividing the world into convex
 *	regions called leafs. The first node in the lump is the tree's root
 *	node..
 *	@see BSPNode */
/*-------------------------------------------------------------------*/
public class BSPNodeLump extends BSPLump
{
	public BSPNode[] nodes = null;

/*-------------------------------------------------------------------*/
/** Constructor. Process Node lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the Node lump in the byte array
 *	@param len the length of the Node lump */
/*-------------------------------------------------------------------*/
	public BSPNodeLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process Node lump data. Every 28 bytes corresponds to a different
 *	node. This method sequentially creates BSPNode objects from raw
 *	byte data. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		nodes = new BSPNode[lumpData.length / 28];

		for(int i = 0; i < nodes.length; i++)
			nodes[i] = new BSPNode(lumpData, i * 28);
	}
}
