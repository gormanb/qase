//---------------------------------------------------------------------
// Name:			BSPPlaneLump.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

/*-------------------------------------------------------------------*/
/** Represents the Plane lump of the BSP file. The Plane Lump is the
 *	area of the file containing details of the splitting planes which
 *	are used to successively subdivide the environment.
 *	@see BSPPlane */
/*-------------------------------------------------------------------*/
public class BSPPlaneLump extends BSPLump
{
	public BSPPlane[] planes = null;

/*-------------------------------------------------------------------*/
/** Constructor. Process Plane lump data.
 *	@param inData the BSP file data
 *	@param off the offset of the Plane lump in the byte array
 *	@param len the length of the Plane lump */
/*-------------------------------------------------------------------*/
	public BSPPlaneLump(byte[] inData, int off, int len)
	{
		super(inData, off, len);
		processLumpData();
	}

/*-------------------------------------------------------------------*/
/** Process Plane lump data. Every 20 bytes corresponds to a different
 *	plane. This method sequentially creates BSPPlane objects from raw
 *	byte data. */
/*-------------------------------------------------------------------*/
	protected void processLumpData()
	{
		planes = new BSPPlane[lumpData.length / 20];

		for(int i = 0; i < planes.length; i++)
			planes[i] = new BSPPlane(lumpData, i * 20);
	}
}
