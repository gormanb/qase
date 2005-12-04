//---------------------------------------------------------------------
// Name:			BSPBrushSide.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Represents a Brush Side. BrushSides are descriptions of a brush's
 *	bounding surfaces, which in turn define its volume.
 *	@see BSPBrushSideLump */
/*-------------------------------------------------------------------*/
public class BSPBrushSide
{
	public int planeNum;
	public short texInfo;

/*-------------------------------------------------------------------*/
/**	Constructor. Builds a BSPBrushSide from raw byte data.
 *	@param brushSideData byte array containing BrushSide lump data
 *	@param offset location of the brushside in the byte array */
/*-------------------------------------------------------------------*/
	public BSPBrushSide(byte[] brushSideData, int offset)
	{
		planeNum = Utils.unsignedShortValue(brushSideData, offset);
		texInfo = Utils.shortValue(brushSideData, offset + 2);
	}
}
