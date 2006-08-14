//--------------------------------------------------
// Name:			SampleMatPollingBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot.matlab.sample;

import soc.qase.com.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.tools.vecmath.*;

import java.util.Vector;
import soc.qase.bot.matlab.MatLabPollingBot;

/*-------------------------------------------------------------------*/
/**	A ready-made example of a class written to interface with MatLab.
 *	Designed for use with the accompanying MatLab sample script, the
 *	agent will simply run directly towarwds the closest available item
 *	in the game environment, collect it, and move on. */
/*-------------------------------------------------------------------*/
public final class SampleMatLabPollingBot extends MatLabPollingBot
{
	private World world = null;
	private Player player = null;
	private Vector entities = null;

	private Vector3f pos = null;
	private Vector3f dir = null;

	private Entity tempEntity = null;
	private Entity nearestEntity = null;
	private float entDist = Float.MAX_VALUE;

	private Origin tempOrigin = null;
	private Vector3f entPos = new Vector3f(0, 0, 0);
	private Vector3f entDir = new Vector3f(0, 0, 0);

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name and skin (appearance)
 *	for the agent.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance */
/*-------------------------------------------------------------------*/
	public SampleMatLabPollingBot(String botName, String botSkin)
	{
		super((botName == null ? "SampleMatLabPollingBot" : botName), botSkin, 2);
		initBot();
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, and whether
 *	the agent should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public SampleMatLabPollingBot(String botName, String botSkin, boolean trackInv)
	{
		super((botName == null ? "SampleMatLabPollingBot" : botName), botSkin, 2, trackInv);
		initBot();
	}

	private void initBot()
	{
		pos = new Vector3f(0, 0, 0);
		dir = new Vector3f(0, 0, 0);
	}

/*-------------------------------------------------------------------*/
/**	Populate the MatLab parameter array with the relevant values.
 *	@param w the current gamestate
 *	@param mlParams an Object array to be populated with data for MatLab
 *	(typically a series of float arrays) */
/*-------------------------------------------------------------------*/
	protected void preMatLab(World w, Object[] mlParams)
	{
		world = w;

		tempEntity = null;
		nearestEntity = null;
		entDist = Float.MAX_VALUE;

		tempOrigin = null;
		entPos.set(0, 0, 0);
		entDir.set(0, 0, 0);

		player = world.getPlayer();
		entities = world.getItems();

		setAction(Action.ATTACK, false);

		for(int i = 0; i < entities.size(); i++)
		{
			tempEntity = (Entity)entities.elementAt(i);

			tempOrigin = tempEntity.getOrigin();
			entPos.set(tempOrigin.getX(), tempOrigin.getY(), 0);
			
			tempOrigin = player.getPlayerMove().getOrigin();
			pos.set(tempOrigin.getX(), tempOrigin.getY(), 0);

			entDir.sub(entPos, pos);

			if((nearestEntity == null || entDir.length() < entDist) && entDir.length() > 0)
			{
				nearestEntity = tempEntity;
				entDist = entDir.length();
			}
		}

		if(nearestEntity != null)
		{
			tempOrigin = nearestEntity.getOrigin();
			entPos.set(tempOrigin.getX(), tempOrigin.getY(), 0);

			tempOrigin = player.getPlayerMove().getOrigin();
			pos.set(tempOrigin.getX(), tempOrigin.getY(), 0);
		}

	/* -------PARAMATERS FOR MATLAB------- */
		matLabParams[0] = pos.toArray();
		matLabParams[1] = entPos.toArray();
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
