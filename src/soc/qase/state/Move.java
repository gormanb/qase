//---------------------------------------------------------------------
// Name:			Move.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

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
/**	Get byte array representation of object. Places the representation
 *	at the appropriate offset in the argument array. Called by the Move
 *	class when compiling a byte representation of the client's movement
 *	for transmission to the server.
 *	@return byte array. */
/*-------------------------------------------------------------------*/
	public byte[] getBytes(byte[] moveBytes, int offset)
	{
		if(moveBytes == null)
		{
			offset = 0;
			moveBytes = new byte[16];
		}

		moveBytes[offset + 0] = (byte)0x7f; // mask
		moveBytes[offset + 14] = (byte)(deltaTime % 256); // time
		moveBytes[offset + 15] = (byte)0x88; // light

		angles.getBytes(moveBytes, offset);
		velocity.getBytes(moveBytes, offset);
		action.getBytes(moveBytes, offset);

		return moveBytes;
	}
}
