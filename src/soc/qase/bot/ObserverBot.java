//--------------------------------------------------
// Name:			ObserverBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot;

import soc.qase.com.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.tools.vecmath.*;

import java.util.Vector;
import java.util.Observer;
import java.util.Observable;

/*-------------------------------------------------------------------*/
/**	ObserverBot, along with PollingBot, occupies the highest level of
 *	the bot hierarchy. Each of these is a near fully-realised agent,
 *	providing a means of detecting changes to the gamestate (implemented
 *	as indicated by their names), as well as a single point of insertion
 *	- the programmer need only supply the AI routine in the runAI method
 *	defined by the Bot interface. Each has its own advantages; the
 *	ObserverBot allows several different objects to be attached to a
 *	single Proxy, whereas the multithreaded PollingBot offers slightly
 *	more efficient performance. */
/*-------------------------------------------------------------------*/
public abstract class ObserverBot extends BasicBot implements Observer
{
	protected boolean pacifyNeeded = false;
	protected boolean respawnNeeded = false;

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name and skin (appearance)
 *	for the agent.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance */
/*-------------------------------------------------------------------*/
	public ObserverBot(String botName, String botSkin)
	{
		super((botName == null ? "ObserverBot" : botName), botSkin);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, and whether
 *	the agent should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public ObserverBot(String botName, String botSkin, boolean trackInv)
	{
		super((botName == null ? "ObserverBot" : botName), botSkin, trackInv);
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
	public ObserverBot(String botName, String botSkin, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "ObserverBot" : botName), botSkin, highThreadSafety, trackInv);
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
	public ObserverBot(String botName, String botSkin, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "ObserverBot" : botName), botSkin, password, highThreadSafety, trackInv);
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
	public ObserverBot(String botName, String botSkin, int recvRate, int msgLevel, int fov, int hand, String password, boolean highThreadSafety, boolean trackInv)
	{
		super((botName == null ? "ObserverBot" : botName), botSkin, recvRate, msgLevel, fov, hand, password, highThreadSafety, trackInv);
	}

/*-------------------------------------------------------------------*/
/**	Connect the agent to a game server.
 *	@param host a String representation of the server's IP address
 *	@param port the port on which the game server is running */
/*-------------------------------------------------------------------*/
	public boolean connect(String host, int port)
	{
		return connect(host, port, Integer.MIN_VALUE, null);
	}

/*-------------------------------------------------------------------*/
/**	Connect to a CTF game server.
 *	@param host a String representation of the host machine's IP address
 *	@param port the port on which the game server is running
 *	@param ctfTeam the team to join; one of the CTF constants found in
 *	soc.qase.info.Server */
/*-------------------------------------------------------------------*/
	public boolean connect(String host, int port, int ctfTeam)
	{
		return connect(host, port, ctfTeam, null);
	}

/*-------------------------------------------------------------------*/
/**	Connect the agent to a game server, and start recording the game
 *	session to DM2 file.
 *	@param host a String representation of the server's IP address
 *	@param port the port on which the game server is running
 *	@param recordDM2File the filename under which to start recording
 *	the game session */
/*-------------------------------------------------------------------*/
	public boolean connect(String host, int port, String recordDM2File)
	{
		return connect(host, port, Integer.MIN_VALUE, recordDM2File);
	}

/*-------------------------------------------------------------------*/
/**	Connect the agent to a CTF game server, and start recording to a
 *	DM2 file.
 *	@param host a String representation of the server's IP address
 *	@param port the port on which the game server is running
 *	@param recordDM2File the filename under which to start recording
 *	the game session
 *	@param ctfTeam the team to join; one of the CTF constants found in
 *	soc.qase.info.Server */
/*-------------------------------------------------------------------*/
	public boolean connect(String host, int port, int ctfTeam, String recordDM2File)
	{
		if(!proxy.connect(host, (port <= 0 ? 27910 : port), recordDM2File))
			return false;

		if(ctfTeam != Integer.MIN_VALUE)
			setCTFTeam(Math.abs(ctfTeam) < 2 ? Math.abs(ctfTeam) : (int)Math.round(Math.random()));

		proxy.addObserver(this);

		setConnected(true);
		this.start();

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Disconnect from the game server, and de-register from the Proxy. */
/*-------------------------------------------------------------------*/
	public void disconnect()
	{
		proxy.deleteObserver(this);
		setConnected(false);
		proxy.disconnect();
	}

/*-------------------------------------------------------------------*/
/**	Required by the Observer pattern. Checks whether the agent is alive
 *	or needs to be respawned, then calls the runAI method.
 *	@param o the object which generated this Observable event - in our
 *	case, the Proxy
 *	@param a an argument object passed by the Observable; here, this is
 *	the World gamestate object */
/*-------------------------------------------------------------------*/
	public void update(Observable o, Object a)
	{
		if(!isBotAlive())
		{
			if(getAction(Action.ATTACK))
				pacifyNeeded = true;
			else
				respawnNeeded = true;
		}
		else if(a != null && !respawnNeeded)
		{
			if(!ctfTeamAssigned && proxy.isCTFServer())
				setCTFTeam(Server.CTF_RANDOM);

			if(getHighThreadSafety())
			{
				synchronized(a)
				{	runAI((World)a);	}
			}
			else
				runAI((World)a);

			sendMovement();
		}
	}

/*-------------------------------------------------------------------*/
/**	The core AI routine. To be implemented by derived classes.
 *	@param w a World object representing the current gamestate */
/*-------------------------------------------------------------------*/
	public abstract void runAI(World w);	// to be supplied by derived class

/*-------------------------------------------------------------------*/
/**	A thread which runs parallel to the Observer pattern updates, checking
 *	to determine whether or not the agent need to respawn. */
/*-------------------------------------------------------------------*/
	public void run()
	{
		while(isConnected())
		{
			if(respawnNeeded)
			{
				respawn();
				respawnNeeded = false;
			}
			else if(pacifyNeeded)
			{
				pacify();
				pacifyNeeded = false;
			}

			Thread.yield();
		}
	}
}
