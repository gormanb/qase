//---------------------------------------------------------------------
// Name:			BSPLeaf.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.Utils;
import soc.qase.tools.vecmath.Vector3f;

/*-------------------------------------------------------------------*/
/** Wrapper class for Leaf objects in the BSP file. The leaf objects
 *	store the leaves of the map's BSP tree. Each leaf is a convex region
 *	that contains, among other things, a cluster index (for determining
 *	the other leafs potentially visible from within the leaf), a list of
 *	faces (for rendering), and a list of brushes (for collision detection). .
 *	@see BSPLeafLump
 *	@see BSPFace
 *	@see BSPBrush */
/*-------------------------------------------------------------------*/
public class BSPLeaf
{
	public long brushOr;
	public int cluster, area;
	public Vector3f bboxMin, bboxMax;
	public int firstLeafFace, numLeafFaces;
	public int firstLeafBrush, numLeafBrushes;

/*-------------------------------------------------------------------*/
/**	Constructor. Builds a BSPLeaf from raw byte data.
 *	@param leafData byte array containing leaf lump data
 *	@param offset location of the leaf in the byte array */
/*-------------------------------------------------------------------*/
	public BSPLeaf(byte[] leafData, int offset)
	{
		brushOr = Utils.unsignedIntValue(leafData, offset);
		cluster = Utils.unsignedShortValue(leafData, offset + 4);
		area = Utils.unsignedShortValue(leafData, offset + 6);
		bboxMin = new Vector3f(Utils.shortValue(leafData, offset + 8), Utils.shortValue(leafData, offset + 10), Utils.shortValue(leafData, offset + 12));
		bboxMax = new Vector3f(Utils.shortValue(leafData, offset + 14), Utils.shortValue(leafData, offset + 16), Utils.shortValue(leafData, offset + 18));
		firstLeafFace = Utils.unsignedShortValue(leafData, offset + 20);
		numLeafFaces = Utils.unsignedShortValue(leafData, offset + 22);
		firstLeafBrush = Utils.unsignedShortValue(leafData, offset + 24);
		numLeafBrushes = Utils.unsignedShortValue(leafData, offset + 26);		
	}
}
