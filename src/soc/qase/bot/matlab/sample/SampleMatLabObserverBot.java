//--------------------------------------------------
// Name:			SampleMatLabObserverBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot.matlab.sample;

import soc.qase.com.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.tools.vecmath.*;

import soc.qase.bot.matlab.MatLabObserverBot;

/*-------------------------------------------------------------------*/
/**	A ready-made example of a class written to interface with MatLab.
 *	Designed for use with the accompanying MatLab sample script, the
 *	agent will simply run directly towarwds the closest available item
 *	in the game environment, collect it, and move on. */
/*-------------------------------------------------------------------*/
public final class SampleMatLabObserverBot extends MatLabObserverBot
{
/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name and skin (appearance)
 *	for the agent.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance */
/*-------------------------------------------------------------------*/
	public SampleMatLabObserverBot(String botName, String botSkin)
	{
		super((botName == null ? "SampleMatLabObserverBot" : botName), botSkin, 2);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, and whether
 *	the agent should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public SampleMatLabObserverBot(String botName, String botSkin, boolean trackInv)
	{
		super((botName == null ? "SampleMatLabObserverBot" : botName), botSkin, 2, trackInv);
	}

	private Vector3f pos = new Vector3f(0, 0, 0);
	private Vector3f itemPos = new Vector3f(0, 0, 0);

/*-------------------------------------------------------------------*/
/**	Populate the MatLab parameter array with the relevant values.
 *	@param world the current gamestate
 *	@param mlParams an Object array to be populated with data for MatLab
 *	(typically a series of float arrays) */
/*-------------------------------------------------------------------*/
	protected void preMatLab(World world, Object[] mlParams)
	{
		setAction(Action.ATTACK, false);

		// get the nearest item of any kind
		Entity nearestItem = getNearestItem(null, null);

		if(nearestItem != null)
		{
			pos.set(getPosition());
			itemPos.set(nearestItem.getOrigin());
		}

	/* -------PARAMATERS FOR MATLAB------- */
		mlParams[0] = pos.toArray();
		mlParams[1] = itemPos.toArray();
	/* ----------------------------------- */

	}

/*-------------------------------------------------------------------*/
/**	Implement the results of MatLab's operations.
 *	@param mlResults the data returned by MatLab */
/*-------------------------------------------------------------------*/
	protected void postMatLab(Object[] mlResults)	// apply the results which MatLab provided
	{
		setBotMovement(new Vector3f((float[])mlResults[0]), null, 200, PlayerMove.POSTURE_NORMAL);
	}
}
