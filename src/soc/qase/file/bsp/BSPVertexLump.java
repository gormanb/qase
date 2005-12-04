//---------------------------------------------------------------------
// Name:			BSPVertexLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.vecmath.Vector3f;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Represents the Vertex lump of the BSP file. The Vertex Lump is the
 *	area of the file containing the locations of every vertex in the
 *	game environment.
 *	@see BSPEdge */
/*-------------------------------------------------------------------*/
public class BSPVertexLump extends BSPLump
{
	public Vector3f[] vertices = null;

/*-------------------------------------------------------------------*/
/** Constructor. Process Vertext lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the Vertex lump in the byte array
 *	@param len the length of the Vertex lump */
/*-------------------------------------------------------------------*/
	public BSPVertexLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process Vertex lump data. Every 12 bytes corresponds to a different
 *	vertex. This method constructs an array of Vector3fs, representing
 *	the list of vertices in the game environment. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		vertices = new Vector3f[lumpData.length / 12];

		for(int i = 0; i < vertices.length; i++)
			vertices[i] = new Vector3f(Utils.floatValue(lumpData, i * 12), Utils.floatValue(lumpData, i * 12 + 4), Utils.floatValue(lumpData, i * 12 + 8));
	}
}
