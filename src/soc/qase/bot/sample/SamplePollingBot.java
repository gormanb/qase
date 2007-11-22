//--------------------------------------------------
// Name:			SamplePollingBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot.sample;

import soc.qase.com.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.tools.vecmath.*;

import soc.qase.bot.PollingBot;

/*-------------------------------------------------------------------*/
/**	A ready-made example of a standalone QASE agent. When connected,
 *	the bot will simply run directly towarwds the closest available item
 *	in the game environment, collect it, and move on. */
/*-------------------------------------------------------------------*/
public final class SamplePollingBot extends PollingBot
{
/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name and skin (appearance)
 *	for the agent.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance */
/*-------------------------------------------------------------------*/
	public SamplePollingBot(String botName, String botSkin)
	{
		super((botName == null ? "SamplePollingBot" : botName), botSkin);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, and whether
 *	the agent should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public SamplePollingBot(String botName, String botSkin, boolean trackInv)
	{
		super((botName == null ? "SamplePollingBot" : botName), botSkin, trackInv);
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
	public SamplePollingBot(String botName, String botSkin, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "SamplePollingBot" : botName), botSkin, highThreadSafety, trackInv);
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
	public SamplePollingBot(String botName, String botSkin, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "SamplePollingBot" : botName), botSkin, password, highThreadSafety, trackInv);
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
	public SamplePollingBot(String botName, String botSkin, int recvRate, int msgLevel, int fov, int hand, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "SamplePollingBot" : botName), botSkin, recvRate, msgLevel, fov, hand, password, highThreadSafety, trackInv);
	}

	private Vector3f pos = new Vector3f(0, 0, 0);
	private Vector3f itemPos = new Vector3f(0, 0, 0);
	private Vector3f itemDir = new Vector3f(0, 0, 0);

/*-------------------------------------------------------------------*/
/**	The agent's main AI cycle.
 *	@param world a World object representing the current gamestate */
/*-------------------------------------------------------------------*/
	public void runAI(World world)
	{
		setAction(Action.ATTACK, false);

		// get the nearest item of any kind
		Entity nearestItem = getNearestItem(null, null);

		if(nearestItem != null)
		{
			pos.set(getPosition());
			itemPos.set(nearestItem.getOrigin());

			itemDir.sub(itemPos, pos);
			setBotMovement(itemDir, null, 200, PlayerMove.POSTURE_NORMAL);
		}
	}
}
