//--------------------------------------------------
// Name:			NoClipBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot;

import soc.qase.com.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.tools.vecmath.*;

import java.util.Vector;
import java.util.Observable;

/*-------------------------------------------------------------------*/
/**	MatLabNoClipBot is an extension of ObserverBot, and as such also
 *	occupies a place on the highest level of the bot hierarchy. Each of
 *	these is a near fully-realised agent, providing a means of detecting
 *	changes to the gamestate (implemented as indicated by their names),
 *	as well as a single point of insertion - the programmer need only
 *	supply the AI routine in the runAI method defined by the Bot interface.
 *	Each has its own advantages; the ObserverBot allows several different
 *	objects to be attached to a single Proxy, whereas the multithreaded
 *	PollingBot offers slightly more efficient performance. Beyond this,
 *	MatLabNoClipBot additionally allows the agent to 'clip' to a specific
 *	point on the map before entering the game as an active participant,
 *	as long as the '+set cheats 1' option is enabled on the Quake 2 server.*/
/*-------------------------------------------------------------------*/
public abstract class NoClipBot extends ObserverBot
{
	private Vector3f initPos = null;
	protected boolean initPosReached = true, isNoClip = false;

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name and skin (appearance)
 *	for the agent.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance */
/*-------------------------------------------------------------------*/
	public NoClipBot(String botName, String botSkin)
	{
		super((botName == null ? "NoClipBot" : botName), botSkin);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, and initial
 *	starting position.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param initialPosition the position to which the agent should move
 *	before entering the game as an active participant */
/*-------------------------------------------------------------------*/
	public NoClipBot(String botName, String botSkin, Vector3f initialPosition)
	{
		super((botName == null ? "MatLabNoClipBot" : botName), botSkin);
		clipToPosition(initialPosition);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, and whether
 *	the agent should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public NoClipBot(String botName, String botSkin, boolean trackInv)
	{
		super((botName == null ? "NoClipBot" : botName), botSkin, trackInv);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, initial
 *	starting position, and whether the agent should manually track its
 *	inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param initialPosition the position to which the agent should move
 *	before entering the game as an active participant 
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public NoClipBot(String botName, String botSkin, Vector3f initialPosition, boolean trackInv)
	{
		super((botName == null ? "MatLabNoClipBot" : botName), botSkin, trackInv);
		clipToPosition(initialPosition);
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
	public NoClipBot(String botName, String botSkin, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "NoClipBot" : botName), botSkin, highThreadSafety, trackInv);
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
	public NoClipBot(String botName, String botSkin, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "NoClipBot" : botName), botSkin, password, highThreadSafety, trackInv);
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
	public NoClipBot(String botName, String botSkin, int recvRate, int msgLevel, int fov, int hand, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "NoClipBot" : botName), botSkin, recvRate, msgLevel, fov, hand, password, highThreadSafety, trackInv);
	}

/*-------------------------------------------------------------------*/
/**	Required by the Observer pattern. Checks whether the agent is alive
 *	and has reached the specified initial position, then calls the runAI
 *	method.
 *	@param o the object which generated this Observable event - in our
 *	case, the Proxy
 *	@param a an argument object passed by the Observable; here, this is
 *	the World gamestate object */
/*-------------------------------------------------------------------*/
	public void update(Observable o, Object a)
	{
		if(!isBotAlive())
			respawnNeeded = true;
		else if(a != null && !respawnNeeded)
		{
			if(checkNoClip((World)a))
			{
				if(getHighThreadSafety())
				{
					synchronized(a)
					{	runAI((World)a);	}
				}
				else
					runAI((World)a);
			}

			sendMovement();
		}
	}

/*-------------------------------------------------------------------*/
/**	The core AI routine. To be implemented by derived classes.
 *	@param w a World object representing the current gamestate */
/*-------------------------------------------------------------------*/
	public abstract void runAI(World w);	// to be supplied by derived class

/*-------------------------------------------------------------------*/
/**	Causes the agent to clip from its present location to the specified
 *	position. Assumes that the server has been started with the '+set cheats 
 *	1' option.
 *	@param clipPos the position to which the agent should clip */
/*-------------------------------------------------------------------*/
	protected void clipToPosition(Vector3f clipPos)
	{
		initPos = (clipPos == null ? null : new Vector3f(clipPos));
		initPosReached = (initPos == null);
	}

	private boolean checkNoClip(World w)
	{
		if(!initPosReached)
		{
			if(!isNoClip)
			{
				pacify();
				sendConsoleCommand("noclip");
				isNoClip = true;
			}
			else
			{
				Vector3f pos = new Vector3f(w.getPlayer().getPlayerMove().getOrigin());
				pos.sub(initPos, pos);

				if(pos.length() < 2)
				{
					pacify();
					initPosReached = true;
					sendConsoleCommand("noclip");
				}
				else
					setBotMovement(pos, null, (pos.length() > 50 ? 400 : 10), PlayerMove.POSTURE_NORMAL);
			}
		}
		else if(isNoClip)
		{
			Vector v = w.getMessages();

			for(int i = 0; i < v.size(); i++)
			{
				if(((String)v.elementAt(i)).equalsIgnoreCase("noclip OFF"))
				{
					isNoClip = false;
					break;
				}
			}
		}

		return initPosReached && !isNoClip;
	}
}
