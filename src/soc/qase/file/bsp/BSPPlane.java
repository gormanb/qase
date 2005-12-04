//---------------------------------------------------------------------
// Name:			BSPPlane.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.vecmath.Vector3f;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Wrapper class for Planes in the BSP file. These are generic geometric
 *	planes used to recursively split the environment; they are in turn
 *	referenced by nodes, faces and brush sides.
 *	@see BSPPlaneLump
 *	@see BSPNode
 *	@see BSPBrushSide */
/*-------------------------------------------------------------------*/
public class BSPPlane
{
	public long type;
	public float distance;
	public Vector3f normal = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Builds a BSPPlane from raw byte data.
 *	@param planeData byte array containing plane lump data
 *	@param offset location of the plane in the byte array */
/*-------------------------------------------------------------------*/
	public BSPPlane(byte[] planeData, int offset)
	{
		normal = new Vector3f(Utils.floatValue(planeData, offset), Utils.floatValue(planeData, offset + 4), Utils.floatValue(planeData, offset + 8));
		distance = Utils.floatValue(planeData, offset + 12);
		type = Utils.unsignedIntValue(planeData, offset + 16);
	}
}

