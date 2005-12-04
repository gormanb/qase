//---------------------------------------------------------------------
// Name:			BSPModelLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

/*-------------------------------------------------------------------*/
/** Wrapper class for the Model lump in the BSP file. The first model
 *	correponds to the base portion of the map while the remaining models
 *	correspond to movable portions of, such as doors, platforms, and
 *	buttons. Each model has a list of faces and list of brushes; these
 *	are especially important for the movable parts of the map, which
 *	(unlike the base portion of the map) do not have BSP trees associated
 *	with them.
 *	@see BSPModel */
/*-------------------------------------------------------------------*/
public class BSPModelLump extends BSPLump
{
	public BSPModel[] models = null;

/*-------------------------------------------------------------------*/
/** Constructor. Process Model lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the Model lump in the byte array
 *	@param len the length of the Model lump */
/*-------------------------------------------------------------------*/
	public BSPModelLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process Model lump data. Every 48 bytes corresponds to a different
 *	model. This method sequentially creates BSPModel objects from raw
 *	byte data. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		models = new BSPModel[lumpData.length / 48];

		for(int i = 0; i < models.length; i++)
			models[i] = new BSPModel(lumpData, i * 48);
	}
}
