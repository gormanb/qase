//---------------------------------------------------------------------
// Name:			BSPModel.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import soc.qase.tools.vecmath.Vector3f;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/** Wrapper class for Models in the BSP file. The first model correponds to the
 *	base portion of the map while the remaining models correspond to movable
 *	portions thereof, such as doors, platforms, and buttons. Each model has a list
 *	of faces and list of brushes; these are especially important for the movable
 *	parts of the map, which (unlike the base portion) do not have BSP trees
 *	associated with them.
 *	@see BSPModelLump */
/*-------------------------------------------------------------------*/
public class BSPModel
{
	public Vector3f bboxMin, bboxMax;
	public Vector3f origin;
	public long headNode;
	public long firstFace, numFaces;

/*-------------------------------------------------------------------*/
/**	Constructor. Builds a BSPModel from raw byte data.
 *	@param modelData byte array containing model lump data
 *	@param offset location of the model in the byte array */
/*-------------------------------------------------------------------*/
	public BSPModel(byte[] modelData, int offset)
	{
		bboxMin = new Vector3f(Utils.floatValue(modelData, offset), Utils.floatValue(modelData, offset + 4), Utils.floatValue(modelData, offset + 8));
		bboxMax = new Vector3f(Utils.floatValue(modelData, offset + 12), Utils.floatValue(modelData, offset + 16), Utils.floatValue(modelData, offset + 20));
		origin = new Vector3f(Utils.floatValue(modelData, offset + 24), Utils.floatValue(modelData, offset + 28), Utils.floatValue(modelData, offset + 32));
		headNode = Utils.unsignedIntValue(modelData, offset + 36);
		firstFace = Utils.unsignedIntValue(modelData, offset + 40);
		numFaces = Utils.unsignedIntValue(modelData, offset + 44);
	}
}
