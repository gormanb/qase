//---------------------------------------------------------------------
// Name:			MatLabPollingBot.java
// JDK Version:		Java 1.4
// Author:			bernard.gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.bot.matlab;

import soc.qase.com.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.tools.vecmath.*;

import java.util.Vector;
import java.util.Observable;
import soc.qase.bot.PollingBot;

/*-------------------------------------------------------------------*/
/**	MatLabPollingBot provides a framework to allow QASE to interact
 *	with MatLab. MatLab must first be used to create and connect the bot;
 *	thereafter, on each update the bot will pre-process the data to
 *	be used by MatLab (in method preMatLab), hand control over to MatLab
 *	(processMatLab), and will post-process the results which MatLab
 *	returns via the setMatLabResults method. Although this is the
 *	supported paradigm - the abstract class implements methods to
 *	facilitate each of the steps outlined above - the programmer can
 *	actually adopt any approach to the task of integrating his bot with
 *	MatLab, since MatLab scripts can access the Java API in the same way
 *	as any Java object. For an example of MatLabPollingBot in action,
 *	see SampleMatLabPollingBot and the accompanying MatLab script.*/
/*-------------------------------------------------------------------*/
public abstract class MatLabPollingBot extends PollingBot implements MatLabBot
{
	protected boolean inMatLab = false;
	protected Object[] matLabParams = null;
	protected Object[] matLabResults = null;

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name and skin (appearance)
 *	for the agent, as well as the number of parameters to be passed to
 *	MatLab on each update.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param mlParams number of parameters to be passed to MatLab on each update */
/*-------------------------------------------------------------------*/
	public MatLabPollingBot(String botName, String botSkin, int mlParams)
	{
		super((botName == null ? "MatLabPollingBot" : botName), botSkin);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, number of
 *	parameters to be passed to MatLab, and whether the agent should
 *	manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param mlParams number of parameters to be passed to MatLab on each update
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabPollingBot(String botName, String botSkin, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabPollingBot" : botName), botSkin, trackInv);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, whether the
 *	agent should operate in high thread safety mode, the number of
 *	parameters to be passed to MatLab, and whether the agent should
 *	manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param highThreadSafety if true, enables high thread safety mode
 *	@param mlParams number of parameters to be passed to MatLab on each update
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabPollingBot(String botName, String botSkin, boolean highThreadSafety, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabPollingBot" : botName), botSkin, highThreadSafety, trackInv);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, server password,
 *	whether the agent should operate in high thread safety mode, the number of
 *	parameters to be passed to MatLab, and whether the agent should manually
 *	track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param password the password of the server, if necessary
 *	@param highThreadSafety if true, enables high thread safety mode
 *	@param mlParams number of parameters to be passed to MatLab on each update
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabPollingBot(String botName, String botSkin, String password, boolean highThreadSafety, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabPollingBot" : botName), botSkin, password, highThreadSafety, trackInv);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, connection
 *	receive rate, type of messages received from server, field of view, 
 *	which hand the agent should hold its gun in, server password,
 *	whether the agent should operate in high thread safety mode, the number of
 *	parameters to be passed to MatLab, and whether the agent should manually
 *	track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param recvRate rate at which the client communicates with server
 *	@param msgLevel specifies which server messages to register interest in
 *	@param fov specifies the agent's field of vision
 *	@param hand specifies the hand in which the agent hold its gun
 *	@param password the password of the server, if necessary
 *	@param highThreadSafety if true, enables high thread safety mode
 *	@param mlParams number of parameters to be passed to MatLab on each update
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabPollingBot(String botName, String botSkin, int recvRate, int msgLevel, int fov, int hand, String password, boolean highThreadSafety, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabPollingBot" : botName), botSkin, recvRate, msgLevel, fov, hand, password, highThreadSafety, trackInv);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	The main loop of the AI cycle. Controls the preprocessing of data,
 *	the handing over of control to MatLab, and the postprocessing of
 *	MatLab's results. Implemented here as final, to prevent subclasses
 *	from overriding.
 *	@param w the current gamestate */
/*-------------------------------------------------------------------*/
	public final void runAI(World w)
	{
		preMatLab(w, matLabParams);	// pre-MatLab processing
		processMatLab();			// hand control to MatLab
		postMatLab(matLabResults);	// post-MatLab processing
	}

/*-------------------------------------------------------------------*/
/**	Populate the MatLab parameter array with the relevant values.
 *	@param w the current gamestate
 *	@param mlParams an Object array to be populated with data for MatLab
 *	(typically a series of float arrays) */
/*-------------------------------------------------------------------*/
	protected abstract void preMatLab(World w, Object[] mlParams);

/*-------------------------------------------------------------------*/
/**	Hand control to MatLab and wait for it to relinquish.*/
/*-------------------------------------------------------------------*/
	private void processMatLab()
	{
		inMatLab = true;

		while(inMatLab)
			Thread.yield();
	}

/*-------------------------------------------------------------------*/
/**	Implement the results of MatLab's operations. To be supplied by
 *	derived classes.
 *	@param mlResults the data returned by MatLab (typically a set of
 *	float arrays) */
/*-------------------------------------------------------------------*/
	protected abstract void postMatLab(Object[] mlResults);

/*-------------------------------------------------------------------*/
/**	Called by MatLab to determine whether or not it should start
 *	running its AI routine.
 *	@return true if the agent is waiting for MatLab to perform its
 *	operations */
/*-------------------------------------------------------------------*/
	public final boolean waitingForMatLab()
	{
		return inMatLab;
	}

/*-------------------------------------------------------------------*/
/**	Called by MatLab to relinquish control back to QASE upon completion
 *	of its AI processing. */
/*-------------------------------------------------------------------*/
	public final void releaseFromMatLab()
	{
		inMatLab = false;
	}

/*-------------------------------------------------------------------*/
/**	Called by MatLab to obtain an array of the objects upon which it
 *	is expected to act.
 *	@return an array of Objects, containing the data to be processed by
 *	MatLab (typically a series of float arrays) */
/*-------------------------------------------------------------------*/
	public final Object[] getMatLabParams()
	{
		return matLabParams;
	}

/*-------------------------------------------------------------------*/
/**	Called by MatLab to provide the bot with the results of its
 *	computations.
 *	@param results an Object array containing the results of MatLab's
 *	computations (typically consisting of a series of float arrays) */
/*-------------------------------------------------------------------*/
	public final void setMatLabResults(Object[] results)
	{
		matLabResults = results;
	}

/*-------------------------------------------------------------------*/
/* Accessor/mutator methods which allow MatLab to read and affect the bot's state.*/
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
/**	Set hand grenades as the active weapon. For use with the constants
 *	stored in the PlayerGun class.
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
