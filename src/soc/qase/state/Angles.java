//---------------------------------------------------------------------
// Name:			Angles.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	The Angles class handles X, Y, and Z angles. It is used when a
 *	QASE agent is trying to change its self state in a simulated
 *	environment. The class is implemented as a wrapper of functionality
 *	related to Quake2 client control, i.e. the desired angle of the agent
 *	itself in respect of the surrounding environments coordinate system.
 *	The pitch value indicates rotation around the horizontal axis (i.e.
 *	looking up or down. The yaw value indicates rotation around the vertical
 *	axis. The roll  value indicates rotation around the depth axis, and is
 *	generally not used. All method parameters are represented as float
 *	values ranging between 0 and 360. */
/*-------------------------------------------------------------------*/
public class Angles
{
	private float pitch = -1;
	private float yaw = -1;
	private float roll = -1;

	public static final int PITCH = 0, YAW = 1, ROLL = 2;

/*-------------------------------------------------------------------*/
/** Default coonstructor. */
/*-------------------------------------------------------------------*/
	public Angles()
	{	}

/*-------------------------------------------------------------------*/
/**	Constructor. Build Angles object from angle data.
 *	@param pitch rotation around the X axis.
 *	@param yaw rotation around the Y axis.
 *	@param roll rotation around the Z axis. */
/*-------------------------------------------------------------------*/
	public Angles(float pitch, float yaw, float roll)
	{
		setPitch(pitch);
		setYaw(yaw);
		setRoll(roll);
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Build Angles object from angle data.
 *	@param fAngles the values to which the angles should be set;
 *	fAngles[0] = yaw, [1] = pitch, [2] = roll */
/*-------------------------------------------------------------------*/
	public Angles(float[] fAngles)
	{
		set(fAngles);
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Build Angles object from existing object.
 *	@param angles source object */
/*-------------------------------------------------------------------*/
	public Angles(Angles angles)
	{
		setPitch(angles.getPitch());
		setYaw(angles.getYaw());
		setRoll(angles.getRoll());
	}

/*-------------------------------------------------------------------*/
/**	Get a specific angle.
 *	@param angleType the angle to return, specified using the constants
 *	in the Angles class (see above)
 *	@return the desired angle */
/*-------------------------------------------------------------------*/
	public float get(int angleType)
	{
		if(angleType == YAW)
			return yaw;
		else if(angleType == PITCH)
			return pitch;
		else if(angleType == ROLL)
			return roll;

		return Float.NaN;
	}

/*-------------------------------------------------------------------*/
/**	Get a float array containing the yaw, pitch and roll angles.
 *	@return a 3-element array where [0] = yaw, [1] = pitch, [2] = roll */
/*-------------------------------------------------------------------*/
	public float[] getYPR()
	{
		return new float[]{ yaw, pitch, roll };
	}

/*-------------------------------------------------------------------*/
/**	Get current pitch value.
 *	@return current pitch value. */
/*-------------------------------------------------------------------*/
	public float getPitch()
	{
		return pitch;
	}

/*-------------------------------------------------------------------*/
/**	Get current yaw value.
 *	@return current yaw value. */
/*-------------------------------------------------------------------*/
	public float getYaw()
	{
		return yaw;
	}

/*-------------------------------------------------------------------*/
/**	Get current roll value.
 *	@return current roll value. */
/*-------------------------------------------------------------------*/
	public float getRoll()
	{
		return roll;
	}

/*-------------------------------------------------------------------*/
/**	Get byte array representation of object. Places the representation
 *	at the appropriate offset in the argument array. Called by the Move
 *	class when compiling a byte representation of the client's movement
 *	for transmission to the server.
 *	@see Move#getBytes */
/*-------------------------------------------------------------------*/
	public void getBytes(byte[] moveBytes, int offset)
	{
		Utils.shortToByteArray((short)Math.round(pitch * (float)(65535.0f / 360.0f)), moveBytes, offset + 1);
		Utils.shortToByteArray((short)Math.round(yaw * (float)(65535.0f / 360.0f)), moveBytes, offset + 3);
		Utils.shortToByteArray((short)Math.round(roll * (float)(65535.0f / 360.0f)), moveBytes, offset + 5);
	}

/*-------------------------------------------------------------------*/
/**	Set the current angles to new values.
 *	@param yaw the new yaw value
 *	@param pitch the new pitch value
 *	@param roll the new roll value */
/*-------------------------------------------------------------------*/
	public void set(float yaw, float pitch, float roll)
	{
		this.yaw = yaw;
		this.roll = roll;
		this.pitch = pitch;
	}

/*-------------------------------------------------------------------*/
/**	Set the current angles to the values specified in the passed array.
 *	@param fAngles the new values to which the angles should be set;
 *	fAngles[0] = yaw, [1] = pitch, [2] = roll */
/*-------------------------------------------------------------------*/
	public void set(float[] fAngles)
	{
		this.yaw = fAngles[0];
		this.pitch = fAngles[1];
		this.roll = fAngles[2];
	}

/*-------------------------------------------------------------------*/
/**	Set a specific angle to the given value.
 *	@param angleType the angle to change, specified using the constants
 *	in the Angles class (see above)
 *	@param value the new value for the given angle */
/*-------------------------------------------------------------------*/
	public void set(int angleType, float value)
	{
		if(angleType == YAW)
			yaw = value;
		else if(angleType == PITCH)
			pitch = value;
		else if(angleType == ROLL)
			roll = value;
	}

/*-------------------------------------------------------------------*/
/**	Set current pitch value.
 *	@param pitch pitch value. */
/*-------------------------------------------------------------------*/
	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}

/*-------------------------------------------------------------------*/
/**	Set current yaw value.
 *	@param yaw yaw value. */
/*-------------------------------------------------------------------*/
	public void setYaw(float yaw)
	{
		this.yaw = yaw;
	}

/*-------------------------------------------------------------------*/
/**	Set current roll value.
 *	@param roll roll value. */
/*-------------------------------------------------------------------*/
	public void setRoll(float roll)
	{
		this.roll = roll;
	}

/*-------------------------------------------------------------------*/
/**	Merge angle parameters.
 *	@param angles source. */
/*-------------------------------------------------------------------*/
	public void merge(Angles angles)
	{
		if(angles != null) {
			if(pitch == -1) pitch = angles.getPitch();
			if(yaw == -1) yaw = angles.getYaw();
			if(roll == -1) roll = angles.getRoll();
		}
	}

/*-------------------------------------------------------------------*/
/**	Creates a duplicate of the object by cloning each of its fields.
 *	@return a deep copy of the current object */
/*-------------------------------------------------------------------*/
	public Angles deepCopy()
	{
		return new Angles(this);
	}
}
