//---------------------------------------------------------------------
// Name:			BSPLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

/*-------------------------------------------------------------------*/
/** An abstract class which acts as a generic wrapper for lump objects.
 *	A 'lump' within a BSP file is a section of the file which stores
 *	information about a particular aspect of the environment's sctructure;
 *	for instance, the Node, Leaf, and Face lumps. */
/*-------------------------------------------------------------------*/
public abstract class BSPLump
{
	protected byte[] lumpData = null;

/*-------------------------------------------------------------------*/
/** Default constructor. */
/*-------------------------------------------------------------------*/
	public BSPLump()
	{	}

/*-------------------------------------------------------------------*/
/** Constructor. Process lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the lump in the byte array
 *	@param len the length of the lump */
/*-------------------------------------------------------------------*/
	public BSPLump(byte[] inData, int off, int len)
	{
		lumpData = new byte[len];

		for(int i = 0; i < len; i++)
			lumpData[i] = inData[off + i];
	}

/*-------------------------------------------------------------------*/
/** Perform specialised processing on the lump data. */
/*-------------------------------------------------------------------*/
	protected abstract void processLumpData();
}
