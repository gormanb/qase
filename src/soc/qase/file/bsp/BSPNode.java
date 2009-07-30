//---------------------------------------------------------------------
// Name:			BSPNode.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.Utils;
import soc.qase.tools.vecmath.Vector3f;

/*-------------------------------------------------------------------*/
/** Wrapper class for Nodes in the BSP file. The BSP tree and its
 *	constituent nodes are used primarily as a spatial subdivision scheme,
 *	dividing the world into convex regions called leafs.
 *	@see BSPNodeLump */
/*-------------------------------------------------------------------*/
public class BSPNode
{
	public long plane;
	public int[] children = null;
	public int frontChild, backChild;
	public Vector3f bboxMin, bboxMax;
	public int firstFace, numFaces;

/*-------------------------------------------------------------------*/
/**	Constructor. Builds a BSPNode from raw byte data.
 *	@param nodeData byte array containing node lump data
 *	@param offset location of the node in the byte array */
/*-------------------------------------------------------------------*/
	public BSPNode(byte[] nodeData, int offset)
	{
		children = new int[2];

		plane = Utils.unsignedIntValue(nodeData, offset);
		children[0] = frontChild = Utils.intValue(nodeData, offset + 4);
		children[1] = backChild = Utils.intValue(nodeData, offset + 8);
		bboxMin = new Vector3f(Utils.shortValue(nodeData, offset + 12), Utils.shortValue(nodeData, offset + 14), Utils.shortValue(nodeData, offset + 16));
		bboxMax = new Vector3f(Utils.shortValue(nodeData, offset + 18), Utils.shortValue(nodeData, offset + 20), Utils.shortValue(nodeData, offset + 22));
		firstFace = Utils.unsignedShortValue(nodeData, offset + 24);
		numFaces = Utils.unsignedShortValue(nodeData, offset + 26);
	}
}
