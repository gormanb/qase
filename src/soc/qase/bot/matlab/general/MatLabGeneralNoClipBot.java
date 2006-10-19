//--------------------------------------------------
// Name:			MatLabGeneralNoClipBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot.matlab.general;

import soc.qase.com.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.tools.vecmath.*;

import soc.qase.bot.matlab.MatLabNoClipBot;

/*-------------------------------------------------------------------*/
/**	The MatGeneralBot family provides a framework to allow MatLab to
 *	directly control the QASE bot from within a script. MatLab first
 *	instantiates and connects the bot, and is notified of gamestate
 *	changes as before. However, it does NOT receive any input from the
 *	bot - the script must obtain the gamestate, examine it, and then
 *	set the agent's next action using the standard bot mutator methods.
 *	MatLabGeneralBots are concrete, ready-to-use classes with the
 *	necessary overriding of the pre- and postMatLab methods already
 *	written. MatLabNoClipBot additionally allows the agent to 'clip' to a
 *	specific point on the map, as long as the '+set cheats 1' option
 *	is enabled on the Quake 2 server. For an example of MatLabGeneralBots
 *	in action, see the accompanying MatLab script. See also the QASE User
 *	Guide, Sections 5.5 and 6.2.*/
/*-------------------------------------------------------------------*/
public final class MatLabGeneralNoClipBot extends MatLabNoClipBot
{
/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name and skin (appearance)
 *	for the agent. Setting either to null will substitute default values.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance */
/*-------------------------------------------------------------------*/
	public MatLabGeneralNoClipBot(String botName, String botSkin)
	{
		super((botName == null ? "MatLabGeneralNoClipBot" : botName), botSkin, 0);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin and initial
 *	starting point for the agent. The bot will 'noclip' through the
 *	environment to this point upon entering the game.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param initialPosition the position to which the agent should move
 *	before entering the game as an active participant */
/*-------------------------------------------------------------------*/
	public MatLabGeneralNoClipBot(String botName, String botSkin, Vector3f initialPosition)
	{
		super((botName == null ? "MatLabGeneralNoClipBot" : botName), botSkin, initialPosition, 0);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin and whether the
 *	agent should manually track its inventory as it collects and uses items.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabGeneralNoClipBot(String botName, String botSkin, boolean trackInv)
	{
		super((botName == null ? "MatLabGeneralNoClipBot" : botName), botSkin, 0, trackInv);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, initial
 *	starting point, and whether it should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param initialPosition the position to which the agent should move
 *	before entering the game as an active participant
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabGeneralNoClipBot(String botName, String botSkin, Vector3f initialPosition, boolean trackInv)
	{
		super((botName == null ? "MatLabGeneralNoClipBot" : botName), botSkin, initialPosition, 0, trackInv);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, whether the
 *	agent should operate in high thread safety mode, and whether it
 *	should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param highThreadSafety if true, enables high thread safety mode
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabGeneralNoClipBot(String botName, String botSkin, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "MatLabGeneralNoClipBot" : botName), botSkin, highThreadSafety, 0, trackInv);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, server password,
 *	whether the agent should operate in high thread safety mode, and whether
 *	it should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param password the password of the server, if necessary
 *	@param highThreadSafety if true, enables high thread safety mode
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabGeneralNoClipBot(String botName, String botSkin, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "MatLabGeneralNoClipBot" : botName), botSkin, password, highThreadSafety, 0, trackInv);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, connection
 *	receive rate, type of messages received from server, field of view, 
 *	which hand the agent should hold its gun in, server password,
 *	whether the agent should operate in high thread safety mode, and whether
 *	it should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param recvRate rate at which the client communicates with server
 *	@param msgLevel specifies which server messages to register interest in
 *	@param fov specifies the agent's field of vision
 *	@param hand specifies the hand in which the agent hold its gun
 *	@param password the password of the server, if necessary
 *	@param highThreadSafety if true, enables high thread safety mode
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabGeneralNoClipBot(String botName, String botSkin, int recvRate, int msgLevel, int fov, int hand, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "MatLabGeneralNoClipBot" : botName), botSkin, recvRate, msgLevel, fov, hand, password, highThreadSafety, 0, trackInv);
	}

/*-------------------------------------------------------------------*/
/**	Pre-process data for MatLab. Here, it does nothing; MatLab controls
 *	the AI cycle directly.
 *	@param mlParams an Object array containing data for processing by
 *	MatLab (typically a collection of float arrays) */
/*-------------------------------------------------------------------*/
	protected void preMatLab(World w, Object[] mlParams)	// apply the results which MatLab provided
	{
		/* do nothing - MatLab should directly access the bot using setAngles, etc. */
	}

/*-------------------------------------------------------------------*/
/**	Post-process and apply the results of MatLab's operations. Here,
 *	it does nothing; MatLab controls the AI cycle directly.
 *	@param mlResults an Object array containing the data resulting from
 *	MatLab's computations (typically a collection of float arrays) */
/*-------------------------------------------------------------------*/
	protected void postMatLab(Object[] mlResults)	// apply the results which MatLab provided
	{
		/* do nothing - MatLab should directly access the bot using setAngles, etc. */
	}

/*-------------------------------------------------------------------*/
/* Accessor/mutator methods: allow MatLab to get/set the bot's state.*/
/*-------------------------------------------------------------------*/

/*-------------------------------------------------------------------*/
/**	Get the current gamestate.
 *	@return a World object representing the current gamestate */
/*-------------------------------------------------------------------*/
	public World getWorld()
	{
		return proxy.getWorld();
	}

/*-------------------------------------------------------------------*/
/**	Set the bot's angles.
 *	@param yaw the agent's new yaw angle
 *	@param pitch the agent's new pitch angle
 *	@param roll the agent's new roll angle */
/*-------------------------------------------------------------------*/
	public void setAngle(float yaw, float pitch, float roll)
	{
		super.setAngle(yaw, pitch, roll);
	}

/*-------------------------------------------------------------------*/
/**	Set the bot's actions.
 *	@param attack the action represents an attack
 *	@param use the action represents usage of current item
 *	@param any the action represents any button press */
/*-------------------------------------------------------------------*/
	public void setAction(boolean attack, boolean use, boolean any)
	{
		super.setAction(attack, use, any);
	}

/*-------------------------------------------------------------------*/
/**	Set the bot's forward and right velocities.
 *	@param forward the agent's new forward velocity
 *	@param right the agent's velocity towards the right
 *	@param up the agent's upwards velocity (jump/crouch) */
/*-------------------------------------------------------------------*/
	public void setVelocity(int forward, int right, int up)
	{
		super.setVelocity(forward, right, up);
	}

/*-------------------------------------------------------------------*/
/**	Convenience method to facilitate the separation of movement and
 *	firing, and allow both to be specified in global co-ordinates. Also allows the
 *	programmer to specify the bot's 'posture' - that is, whether it is
 *	standing, crouching or jumping. The postureState parameter should be one
 *	of the POSTURE_CROUCH, POSTURE_NORMAL or POSTURE_JUMP constants from the
 *	PlayerMove class.
 *	@param moveDir the direction in which to move
 *	@param fireDir the direction in which to aim
 *	@param vel the agent's total velocity
 *	@param postureState the bot's current posture, as defined in PlayerMove constants
 *	@see soc.qase.state.PlayerMove */
/*-------------------------------------------------------------------*/
	public void setBotMovement(Vector3f moveDir, Vector3f fireDir, float vel, int postureState)
	{
		super.setBotMovement(moveDir, fireDir, vel, postureState);
	}

/*-------------------------------------------------------------------*/
/**	Convenience method to facilitate the separation of movement and
 *	firing, and allow both to be specified in global co-ordinates.
 *	Instead of providing an explicit velocity as above, the programmer
 *	specifies the current walk state (WALK_STOPPED, WALK_NORMAL, WALK_RUN)
 *	using the constants defined in the PlayerMove class. To account for the
 *	possibility of the programmer passing an explicit velocity as an int
 *	rather than float, the call will be passed directly to the above method
 *	if walkState exceeds the range of the constant values (i.e. 0, 1, 2).
 *	This method also allows the programmer to specify the bot's 'posture' -
 *	that is, whether it is standing, crouching or jumping. The postureState
 *	parameter should be one of the POSTURE_CROUCH, POSTURE_NORMAL or POSTURE_JUMP
 *	constants from the PlayerMove class.
 *	@param moveDir the direction in which to move
 *	@param fireDir the direction in which to aim
 *	@param walkState the agent's walk state, as defined in PlayerMove constants
 *	@param postureState the agent's current posture, as defined in PlayerMove constants
 *	@see soc.qase.state.PlayerMove */
/*-------------------------------------------------------------------*/
	public void setBotMovement(Vector3f moveDir, Vector3f fireDir, int walkState, int postureState)
	{
		super.setBotMovement(moveDir, fireDir, walkState, postureState);
	}

/*-------------------------------------------------------------------*/
/**	Send a non-blocking console message to connected host.
 *	@param command message to send */
/*-------------------------------------------------------------------*/
	public void sendConsoleCommand(String command)
	{
		super.sendConsoleCommand(command);
	}

/*-------------------------------------------------------------------*/
/**	Set hand grenades as the active weapon. */
/*-------------------------------------------------------------------*/
	public void activateGrenades()
	{
		super.activateGrenades();
	}

/*-------------------------------------------------------------------*/
/**	Change the active weapon. For use with the constants stored in the
 *	PlayerGun class.
 *	@param gun the index of the gun to activate
 *	@see soc.qase.state.PlayerGun */
/*-------------------------------------------------------------------*/
	public void changeWeapon(int gun)
	{
		super.changeWeaponByInventoryIndex(gun);
	}

/*-------------------------------------------------------------------*/
/**	Change the current weapon by specifying a number from 0-9.
 *	@param gun the keyboard index of the weapon to activate */
/*-------------------------------------------------------------------*/
	public void changeWeaponByKeyboardIndex(int gun)
	{
		super.changeWeaponByGunModelIndex(gun);
	}

/*-------------------------------------------------------------------*/
/**	Change the current weapon by specifying an inventory index.
 *	Typically uses the constants from the Inventory class.
 *	@param gun the inventory index of the weapon to activate
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	public void changeWeaponByInventoryIndex(int gun)
	{
		super.changeWeaponByInventoryIndex(gun);
	}

/*-------------------------------------------------------------------*/
/**	Change the current weapon by specifying the index of the associated
 *	weapon model in the Config table.
 *	@param gun the Config index of the weapon to activate
 *	@see soc.qase.info.Config */
/*-------------------------------------------------------------------*/
	public void changeWeaponByGunModelIndex(int gun)
	{
		super.changeWeaponByGunModelIndex(gun);
	}

/*-------------------------------------------------------------------*/
/**	Use the specified item. Items are specified by inventory index,
 *	using the constants from the Inventory class.
 *	@param item the keyboard index of the weapon to activate
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	public void useItem(int item)
	{
		super.useItem(item);
	}
}
