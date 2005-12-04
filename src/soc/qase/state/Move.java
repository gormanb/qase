//---------------------------------------------------------------------
// Name:			Move.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Wrapper class for move attributes. */
/*-------------------------------------------------------------------*/
public class Move
{
	private Angles angles = null;
	private Velocity velocity = null;
	private Action action = null;
	private int deltaTime;

/*-------------------------------------------------------------------*/
/**	Constructor.
 *	@param angles move angles.
 *	@param velocity move velocity.
 *	@param action move action.
 *	@param deltaTime time between this move, and previous move in
 *	milliseconds. */
/*-------------------------------------------------------------------*/
	public Move(Angles angles, Velocity velocity, Action action, int deltaTime)
	{
		this.angles = angles;
		this.velocity = velocity;
		this.action = action;
		this.deltaTime = deltaTime;
	}
	
/*-------------------------------------------------------------------*/
/**	Get byte array representation of this object.
 *	@return byte array. */
/*-------------------------------------------------------------------*/
	public byte[] getBytes()
	{
		byte[] result = null;
		byte[] mask = null;
		byte[] time = null;
		byte[] light = null;

		mask = new byte[1];
		time = new byte[1];
		light = new byte[1];
		mask[0] = (byte)0x7f;
		time[0] = (byte)(deltaTime % 256);
		light[0] = (byte)0x88;
		result = Utils.concatBytes(mask, angles.getBytes());
		result = Utils.concatBytes(result, velocity.getBytes());
		result = Utils.concatBytes(result, action.getBytes());
		result = Utils.concatBytes(result, time);
		result = Utils.concatBytes(result, light);
		return result;
	}
}
