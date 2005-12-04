//---------------------------------------------------------------------
// Name:			BSPBrushLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

/*-------------------------------------------------------------------*/
/** Represents the Brush Lump of the BSP file. The Brush Lump is the
 *	area of the file containing specifications of the various brushes
 *	present in the game environment.
 *	@see BSPBrush */
/*-------------------------------------------------------------------*/
public class BSPBrushLump extends BSPLump
{
	public BSPBrush[] brushes = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Process Brush Lump data.
 *	@param inData byte array containing BSP file data
 *	@param off location of the brush data in the byte array
 *	@param len length of the Brush lump */
/*-------------------------------------------------------------------*/
	public BSPBrushLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/**	Process the lump data. Each 12 bytes corresponds to a different
 *	brush. This method sequentially creates BSPBrush objects from the
 *	raw data. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		brushes = new BSPBrush[lumpData.length / 12];

		for(int i = 0; i < brushes.length; i++)
			brushes[i] = new BSPBrush(lumpData, i * 12);
	}
}
