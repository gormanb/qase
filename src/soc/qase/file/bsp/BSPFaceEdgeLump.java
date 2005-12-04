//---------------------------------------------------------------------
// Name:			BSPFaceEdgeLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Represents the FaceEdge lump of the BSP file. The FaceEdge Lump is
 *	a lookup table, used to associate each face with its counterparts in
 *	the edge matrix.
 *	@see BSPFaceLump
 *	@see BSPFace
 *	@see BSPEdgeLump
 *	@see BSPEdge */
/*-------------------------------------------------------------------*/
public class BSPFaceEdgeLump extends BSPLump
{
	public long[] faceEdgeTable = null;

/*-------------------------------------------------------------------*/
/** Constructor. Process FaceEdge lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the Edge lump in the byte array
 *	@param len the length of the Edge lump */
/*-------------------------------------------------------------------*/
	public BSPFaceEdgeLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process FaceEdge lump data. Every 4 bytes corresponds to a different
 *	entry in the face-edge table. This method populates the table from
 *	raw byte data. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		faceEdgeTable = new long[lumpData.length / 4];

		for(int i = 0; i < faceEdgeTable.length; i++)
			faceEdgeTable[i] = Utils.unsignedIntValue(lumpData, i * 4);
	}
}
