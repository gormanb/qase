//--------------------------------------------------
// Name:			SampleNoClipBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot.sample;

import soc.qase.com.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.tools.vecmath.*;

import soc.qase.bot.NoClipBot;

/*-------------------------------------------------------------------*/
/**	A ready-made example of a standalone QASE agent, designed to demonstrate
 *	NoClipBot's functionality. NoClipBots are intended to provide a means of
 *	moving QASE agents directly to a particular part of the game map, for
 *	cases where AI testing requires it. When connected, the bot will first
 *	"noclip" - that is, move through solid objects - directly to the location
 *	specified in the constructor, and will then run towarwds the closest
 *	available item, collect it, and move on. Note that in order for NoClipBots
 *	to be used, the 'set cheats 1' option must be enabled on the Quake 2 server. */
/*-------------------------------------------------------------------*/
public final class SampleNoClipBot extends NoClipBot
{
	private Vector3f pos = new Vector3f(0, 0, 0);
	private Vector3f itemPos = new Vector3f(0, 0, 0);
	private Vector3f itemDir = new Vector3f(0, 0, 0);

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, and initial
 *	starting position.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param initialPosition the position to which the agent should move
 *	before entering the game as an active participant */
/*-------------------------------------------------------------------*/
	public SampleNoClipBot(String botName, String botSkin, Vector3f initialPosition)
	{
		super(botName, botSkin, initialPosition);
	}

/*-------------------------------------------------------------------*/
/**	The agent's core AI routine.
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
