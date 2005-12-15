//---------------------------------------------------------------------
// Name:			Action.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

/*-------------------------------------------------------------------*/
/**	Handles action parameters used in movement messages sent between
 *	client and a host. The Action class is used when a QASE agent is
 *	trying to change its self state in a simulated environment. It is
 *	implemented as a wrapper of functionality related to Quake2 client
 *	control, i.e. firing weapons, pressing keyboard keys, etc. */
/*-------------------------------------------------------------------*/
public class Action
{
	private boolean attack = false;
	private boolean use = false;
	private boolean any = false;

	public static final int ATTACK = 0, USE = 1, ANY = 2;

/*-------------------------------------------------------------------*/
/**	Constructor.
 *	@param attack true if the action represents an attack, 
 *	otherwise false.
 *	@param use true if the action represents usage of current item, 
 *	otherwise false.
 *	@param any true if the action represents any button press,
 *	otherwise false. */
/*-------------------------------------------------------------------*/
	public Action(boolean attack, boolean use, boolean any)
	{
		setAttack(attack);
		setUse(use);
		setAny(any);
	}
	
/*-------------------------------------------------------------------*/
/**	Constructor. Duplcates the actions of a source Action object.
 *	@param action source object whose actions are to be copied  */
/*-------------------------------------------------------------------*/
	public Action(Action action)
	{
		setAttack(action.getAttack());
		setUse(action.getUse());
		setAny(action.getAny());
	}

/*-------------------------------------------------------------------*/
/**	Check the status of a particular action type. Specified using the
 *	Action constants (see above).
 *	@param actionType the action to be checked
 *	@return true if the action setting is active, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean get(int actionType)
	{
		if(actionType == ATTACK)
			return attack;
		else if(actionType == USE)
			return use;
		else if(actionType == ANY)
			return any;

		return false;
	}

/*-------------------------------------------------------------------*/
/**	Get current attack value.
 *	@return current attack value. */
/*-------------------------------------------------------------------*/
	public boolean getAttack()
	{
		return attack;
	}

/*-------------------------------------------------------------------*/
/**	Get current use value.
 *	@return current use value. */
/*-------------------------------------------------------------------*/
	public boolean getUse()
	{
		return use;
	}

/*-------------------------------------------------------------------*/
/**	Get current any value.
 *	@return current any value. */
/*-------------------------------------------------------------------*/
	public boolean getAny()
	{
		return any;
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
		int value = 0;

		if(attack) value = value | 0x01;
		if(use) value = value | 0x02;
		if(any) value = value | 0x80;

		moveBytes[offset + 13] = (byte)value;
	}

/*-------------------------------------------------------------------*/
/**	Specify the current action settings.
 *	@param attack the action represents an attack
 *	@param use the action represents usage of current item
 *	@param any the action represents any button press
 *	@see soc.qase.state.Action */
/*-------------------------------------------------------------------*/
	public void set(boolean attack, boolean use, boolean any)
	{
		this.use = use;
		this.any = any;
		this.attack = attack;
	}

/*-------------------------------------------------------------------*/
/**	Set one of the agent's actions, as specified by the constants in
 *	the Action class.
 *	@param actionType the action to return
 *	@param value true if the given action should be active, false otherwise 
 *	@see soc.qase.state.Action */
/*-------------------------------------------------------------------*/
	public void set(int actionType, boolean value)
	{
		if(actionType == ATTACK)
			attack = value;
		else if(actionType == USE)
			use = value;
		else if(actionType == ANY)
			any = value;
	}

/*-------------------------------------------------------------------*/
/**	Set current attack value.
 *	@param attack attack value. */
/*-------------------------------------------------------------------*/
	public void setAttack(boolean attack)
	{
		this.attack = attack;
	}

/*-------------------------------------------------------------------*/
/**	Set current use value.
 *	@param use use value. */
/*-------------------------------------------------------------------*/
	public void setUse(boolean use)
	{
		this.use = use;
	}

/*-------------------------------------------------------------------*/
/**	Set current any value.
 *	@param any any value. */
/*-------------------------------------------------------------------*/
	public void setAny(boolean any)
	{
		this.any = any;
	}
}
