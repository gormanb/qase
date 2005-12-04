//---------------------------------------------------------------------
// Name:			BSPBrushSideLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

/*-------------------------------------------------------------------*/
/** Represents the BrushSide Lump of the BSP file. The BrushSide Lump
 *	is the area of the file containing details of each brush's bounding
 *	surfaces (Brush Sides).
 *	@see BSPBrushSide */
/*-------------------------------------------------------------------*/
public class BSPBrushSideLump extends BSPLump
{
	public BSPBrushSide[] brushSides = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Process BrushSide lump data.
 *	@param inData byte array containing BSP file data
 *	@param off location of the BrushSide lump in the byte array
 *	@param len length of the BrushSide lump */
/*-------------------------------------------------------------------*/
	public BSPBrushSideLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/**	Process BrushSide lump data. Each 4 bytes corresponds to a different
 *	BrushSide. This method sequentially creates BSPBrushSide objects from
 *	the raw data. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		brushSides = new BSPBrushSide[lumpData.length / 4];

		for(int i = 0; i < brushSides.length; i++)
			brushSides[i] = new BSPBrushSide(lumpData, i * 4);
	}
}
