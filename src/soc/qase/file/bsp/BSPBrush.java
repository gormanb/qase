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

/*-------------------------------------------------------------------*/
/**	Checks whether the contents - that is, the type of in-game object
 *	which the brush represents, be it lava, water, solid, mist, etc - of
 *	this brush matches any of the specified bitwise combination of contents.
 *	@param brushOr bitwise OR of one or more CONTENTS constants from
 *	the BSPBrush class
 *	@return true if the brush contains ANY of the the specified contents,
 *	false otherwise */
/*-------------------------------------------------------------------*/
	public boolean checkContents(int brushOr)
	{
		return (contents & brushOr) != 0;
	}

/*-------------------------------------------------------------------*/
/**	Checks whether the contents - that is, the type of in-game object
 *	which the brush represents, be it lava, water, solid, mist, etc - of
 *	this brush exactly matches all of the specified bitwise combination
 *	of contents.
 *	@param brushOr bitwise OR of one or more CONTENTS constants from
 *	the BSPBrush class
 *	@return true if the brush contains ALL of the the specified contents,
 *	false otherwise */
/*-------------------------------------------------------------------*/
	public boolean checkStrictContents(int brushOr)
	{
		return (contents & brushOr) == brushOr;
	}
}
