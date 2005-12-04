//---------------------------------------------------------------------
// Name:			BSPEdge.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Represents an edge between vertices in the geometry of the level.
 *	@see BSPEdgeLump */
/*-------------------------------------------------------------------*/
public class BSPEdge
{
	public int vertex1, vertex2;

/*-------------------------------------------------------------------*/
/**	Constructor. Builds a BSPEdge from raw byte data.
 *	@param edgeData byte array containing edge lump data
 *	@param offset location of the edge in the byte array */
/*-------------------------------------------------------------------*/
	public BSPEdge(byte[] edgeData, int offset)
	{
		vertex1 = Utils.unsignedShortValue(edgeData, offset);
		vertex2 = Utils.unsignedShortValue(edgeData, offset + 2);
	}
}
