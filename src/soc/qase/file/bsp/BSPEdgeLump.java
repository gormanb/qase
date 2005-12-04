//---------------------------------------------------------------------
// Name:			BSPEdgeLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

/*-------------------------------------------------------------------*/
/** Represents the Edge lump of the BSP file. The Edge Lump is the
 *	area of the file containing details of geometric edges between
 *	vertices in the map.
 *	@see BSPEdge */
/*-------------------------------------------------------------------*/
public class BSPEdgeLump extends BSPLump
{
	public BSPEdge[] edges = null;

/*-------------------------------------------------------------------*/
/** Constructor. Process Edge lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the Edge lump in the byte array
 *	@param len the length of the Edge lump */
/*-------------------------------------------------------------------*/
	public BSPEdgeLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process Edge lump data. Every 4 bytes corresponds to a different
 *	edge. This method sequentially creates BSPEdge objects from raw
 *	byte data. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		edges = new BSPEdge[lumpData.length / 4];

		for(int i = 0; i < edges.length; i++)
			edges[i] = new BSPEdge(lumpData, i * 4);
	}
}
