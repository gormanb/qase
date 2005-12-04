//---------------------------------------------------------------------
// Name:			BSPFace.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Wrapper class for Faces in the BSP file. Faces store information
 *	used to render the surfaces of geometry in the game world.
 *	@see BSPFaceLump */
/*-------------------------------------------------------------------*/
public class BSPFace
{
	public int plane, planeSide;
	public long firstEdge;
	public int numEdges;
	public int textureInfo;
	public byte[] lightMapStyles = null;
	public long lightMapOffset;

/*-------------------------------------------------------------------*/
/**	Constructor. Builds a BSPFace from raw byte data.
 *	@param faceData byte array containing face lump data
 *	@param offset location of the face in the byte array */
/*-------------------------------------------------------------------*/
	public BSPFace(byte[] faceData, int offset)
	{
		lightMapStyles = new byte[4];

		plane = Utils.unsignedShortValue(faceData, offset);
		planeSide = Utils.unsignedShortValue(faceData, offset + 2);
		firstEdge = Utils.unsignedIntValue(faceData, offset + 4);
		numEdges = Utils.unsignedShortValue(faceData, offset + 8);
		textureInfo = Utils.unsignedShortValue(faceData, offset + 10);

		for(int i = 0; i < lightMapStyles.length; i++)
			lightMapStyles[i] = faceData[offset + 12 + i];

		lightMapOffset = Utils.unsignedIntValue(faceData, offset + 16);
	}
}
