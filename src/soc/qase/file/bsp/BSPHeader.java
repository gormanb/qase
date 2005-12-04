//---------------------------------------------------------------------
// Name:			BSPHeader.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Wrapper class for the Header section of the BSP file. The Header
 *	contains the full directory listing of the file - that is, the
 *	location of each lump, along with their lengths. */
/*-------------------------------------------------------------------*/
public class BSPHeader
{
	protected static final int BSP_HEADER_ID = (('P'<<24)+('S'<<16)+('B'<<8)+'I');

	protected long identity, version;
	protected int[] offsets, lengths;

	protected byte[] headerData = null;

	public static final int	ENTITIES = 0, PLANES = 1, VERTICES = 2,
							VISIBILITY = 3, NODES = 4, TEXTURE_INFORMATION = 5,
							FACES = 6, LIGHTMAPS = 7, LEAVES = 8, LEAF_FACE_TABLE = 9,
							LEAF_BRUSH_TABLE = 10, EDGES = 11, FACE_EDGE_TABLE = 12,
							MODELS = 13, BRUSHES = 14, BRUSH_SIDES = 15, POP = 16,
							AREAS = 17, AREA_PORTALS = 18;

/*-------------------------------------------------------------------*/
/** Constructor. Process the header data to obtain the directory listing.
 *	@param hData the BSP header data */
/*-------------------------------------------------------------------*/
	public BSPHeader(byte[] hData)
	{
		offsets = new int[19];
		lengths = new int[19];

		headerData = hData;

		loadHeader();
	}

/*-------------------------------------------------------------------*/
/** Performs the actual extraction and storage of the directory listing. */
/*-------------------------------------------------------------------*/
	protected void loadHeader()
	{
		int dataOffset = 0;

		identity = Utils.intValue(headerData, dataOffset);
		dataOffset += 4;

		version = Utils.intValue(headerData, dataOffset);
		dataOffset += 4;

		for(int i = 0; i < offsets.length; i++)
		{
			offsets[i] = Utils.intValue(headerData, dataOffset) - headerData.length;
			dataOffset += 4;

			lengths[i] = Utils.intValue(headerData, dataOffset);
			dataOffset += 4;
		}
	}

/*-------------------------------------------------------------------*/
/** Get the offset associated with a particular lump. Lump indices are
 *	specified using the constants found in BSPHeader (see above).
 *	@param index the index of the desired lump
 *	@return an integer indicating the position in the file where the
 *	given lump begins */
/*-------------------------------------------------------------------*/
	public int getOffset(int index)
	{
		return offsets[index];
	}

/*-------------------------------------------------------------------*/
/** Get the length of a particular lump. Lump indices are specified using
 *	the constants found in BSPHeader (see above).
 *	@param index the index of the desired lump
 *	@return an integer indicating the length of the given lump */
/*-------------------------------------------------------------------*/
	public int getLength(int index)
	{
		return lengths[index];
	}

/*-------------------------------------------------------------------*/
/** Get the offset and length of a particular lump. Lump indices are
 *	specified using the constants found in BSPHeader (see above).
 *	@param index the index of the desired lump
 *	@return an integer array indicating the position and length of the
 *	given lump */
/*-------------------------------------------------------------------*/
	public int[] getOffsetAndLength(int index)
	{
		int[] offsetLength = new int[2];

		offsetLength[0] = offsets[index];
		offsetLength[1] = lengths[index];

		return offsetLength;
	}

/*-------------------------------------------------------------------*/
/** Verifies that the current file is a valid BSP.
 *	@return true if BSP validation succeeds, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean validateBSP()
	{
		return identity == BSP_HEADER_ID;
	}
}
