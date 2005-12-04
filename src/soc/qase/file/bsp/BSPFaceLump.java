//---------------------------------------------------------------------
// Name:			BSPFaceLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

/*-------------------------------------------------------------------*/
/** Represents the Face lump of the BSP file. The Face Lump is the
 *	area of the file containing details of the various faces (surfaces)
 *	present in the game world.
 *	@see BSPFace */
/*-------------------------------------------------------------------*/
public class BSPFaceLump extends BSPLump
{
	public BSPFace[] faces = null;

/*-------------------------------------------------------------------*/
/** Constructor. Process Face lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the Face lump in the byte array
 *	@param len the length of the Face lump */
/*-------------------------------------------------------------------*/
	public BSPFaceLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process Face lump data. Every 20 bytes corresponds to a different
 *	face. This method sequentially creates BSPFace objects from raw
 *	byte data. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		faces = new BSPFace[lumpData.length / 20];

		for(int i = 0; i < faces.length; i++)
			faces[i] = new BSPFace(lumpData, i * 20);
	}
}
