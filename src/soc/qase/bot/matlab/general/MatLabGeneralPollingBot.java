//--------------------------------------------------
// Name:			MatLabGeneralPollingBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot.matlab.general;

import soc.qase.com.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.tools.vecmath.*;

import soc.qase.bot.matlab.MatLabPollingBot;

/*-------------------------------------------------------------------*/
/**	The MatGeneralBot family provides a framework to allow MatLab to
 *	directly control the QASE bot from within a script. MatLab first
 *	instantiates and connects the bot, and is notified of gamestate
 *	changes as before. However, it does NOT receive any input from the
 *	bot - the script must obtain the gamestate, examine it, and then
 *	set the agent's next action using the standard bot mutator methods.
 *	Although the other MatLabBots support this functionality as well,
 *	MatLabGeneralBots are concrete, ready-to-use classes with the
 *	necessary overriding of the pre- and postMatLab methods already
 *	written. For an example of MatLabGeneralBots in action, see the
 *	accompanying MatLab script.*/
/*-------------------------------------------------------------------*/
public final class MatLabGeneralPollingBot extends MatLabPollingBot
{
/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name and skin (appearance)
 *	for the agent.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance */
/*-------------------------------------------------------------------*/
	public MatLabGeneralPollingBot(String botName, String botSkin)
	{
		super((botName == null ? "MatLabGeneralPollingBot" : botName), botSkin, 0);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, and whether
 *	the agent should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabGeneralPollingBot(String botName, String botSkin, boolean trackInv)
	{
		super((botName == null ? "MatLabGeneralPollingBot" : botName), botSkin, 0, trackInv);
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
	public MatLabGeneralPollingBot(String botName, String botSkin, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "MatLabGeneralPollingBot" : botName), botSkin, highThreadSafety, 0, trackInv);
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
	public MatLabGeneralPollingBot(String botName, String botSkin, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "MatLabGeneralPollingBot" : botName), botSkin, password, highThreadSafety, 0, trackInv);
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
	public MatLabGeneralPollingBot(String botName, String botSkin, int recvRate, int msgLevel, int fov, int hand, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "MatLabGeneralPollingBot" : botName), botSkin, recvRate, msgLevel, fov, hand, password, highThreadSafety, 0, trackInv);
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
}
