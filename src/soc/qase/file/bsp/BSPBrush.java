//---------------------------------------------------------------------
// Name:			BSPBrush.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Wrapper class for Brushes in the BSP file. Brushes are convex
 *	polyhedra, defined by their surrounding surfaces, used to describe
 *	solid spaces within the environment.
 *	@see BSPBrushLump */
/*-------------------------------------------------------------------*/
public class BSPBrush
{
	public static final int
	CONTENTS_SOLID = 1, CONTENTS_WINDOW = 2, CONTENTS_AUX = 4,
	CONTENTS_LAVA = 8, CONTENTS_SLIME = 16, CONTENTS_WATER = 32,
	CONTENTS_MIST = 64, LAST_VISIBLE_CONTENTS = 64;

	public int contents;
	public long firstSide, numSides;

/*-------------------------------------------------------------------*/
/**	Constructor. Builds a BSPBrush from raw byte data.
 *	@param brushData byte array containing brush lump data
 *	@param offset location of the brush data in the byte array */
/*-------------------------------------------------------------------*/
	public BSPBrush(byte[] brushData, int offset)
	{
		firstSide = Utils.unsignedIntValue(brushData, offset);
		numSides = Utils.unsignedIntValue(brushData, offset + 4);
		contents = Utils.intValue(brushData, offset + 8);
	}
}
