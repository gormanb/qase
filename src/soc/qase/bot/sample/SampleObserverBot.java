//--------------------------------------------------
// Name:			SampleObserverBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot.sample;

import soc.qase.com.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.tools.vecmath.*;

import java.util.Vector;
import soc.qase.bot.ObserverBot;

/*-------------------------------------------------------------------*/
/**	A ready-made example of a standalone QASE agent. When connected,
 *	the bot will simply run directly towarwds the closest available item
 *	in the game environment, collect it, and move on. */
/*-------------------------------------------------------------------*/
public final class SampleObserverBot extends ObserverBot
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
	public SampleObserverBot(String botName, String botSkin)
	{
		super((botName == null ? "SampleObserverBot" : botName), botSkin);
		initBot();
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, and whether
 *	the agent should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public SampleObserverBot(String botName, String botSkin, boolean trackInv)
	{
		super((botName == null ? "SampleObserverBot" : botName), botSkin, trackInv);
		initBot();
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
	public SampleObserverBot(String botName, String botSkin, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "SampleObserverBot" : botName), botSkin, highThreadSafety, trackInv);
		initBot();
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
	public SampleObserverBot(String botName, String botSkin, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "SampleObserverBot" : botName), botSkin, password, highThreadSafety, trackInv);
		initBot();
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
	public SampleObserverBot(String botName, String botSkin, int recvRate, int msgLevel, int fov, int hand, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "SampleObserverBot" : botName), botSkin, recvRate, msgLevel, fov, hand, password, highThreadSafety, trackInv);
		initBot();
	}

	private void initBot()
	{
		pos = new Vector3f(0, 0, 0);
		dir = new Vector3f(0, 0, 0);
	}

/*-------------------------------------------------------------------*/
/**	The agent's core AI routine.
 *	@param w a World object representing the current gamestate */
/*-------------------------------------------------------------------*/
	public void runAI(World w)
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

			entDir.sub(entPos, pos);
			entDir.normalize();

			setBotMovement(entDir, null, 200, PlayerMove.POSTURE_NORMAL);
		}
	}
}
