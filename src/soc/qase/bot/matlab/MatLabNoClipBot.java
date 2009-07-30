//---------------------------------------------------------------------
// Name:			MatLabNoClipBot.java
// JDK Version:		Java 1.4
// Author:			bernard.gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.bot.matlab;

import soc.qase.bot.NoClipBot;
import soc.qase.state.World;
import soc.qase.tools.vecmath.Vector3f;

/*-------------------------------------------------------------------*/
/**	MatLabNoClipBot provides a framework to allow QASE to interact
 *	with MatLab. MatLab must first be used to create and connect the bot;
 *	thereafter, on each update the bot will pre-process the data to
 *	be used by MatLab (in method preMatLab), hand control over to MatLab
 *	(processMatLab), and will post-process the results which MatLab
 *	returns via the setMatLabResults method. Due to its significantly
 *	better performance, this paradigm is preferred over the direct access
 *	provided by the MatLabGeneralBot family. MatLabNoClipBot additionally
 *	allows the agent to 'clip' to a specific point on the map, as long as
 *	the '+set cheats 1' option is enabled on the Quake 2 server. See also
 *	the QASE User Guide, Sections 5.5 and 6.2. */
/*-------------------------------------------------------------------*/
public abstract class MatLabNoClipBot extends NoClipBot implements MatLabBot
{
	protected boolean inMatLab = false;
	protected Object[] matLabParams = null;
	protected Object[] matLabResults = null;

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name and skin (appearance)
 *	for the agent, as well as the number of parameters to be passed to MatLab.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param mlParams number of parameters to be passed to MatLab on each update */
/*-------------------------------------------------------------------*/
	public MatLabNoClipBot(String botName, String botSkin, int mlParams)
	{
		super((botName == null ? "MatLabNoClipBot" : botName), botSkin);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, the position
 *	to which the agent should move before entering the game as an active
 *	participant, and the number of parameters to be passed to MatLab.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param initialPosition the position to which the agent should move
 *	before entering the game as an active participant 
 *	@param mlParams number of parameters to be passed to MatLab on each update */
/*-------------------------------------------------------------------*/
	public MatLabNoClipBot(String botName, String botSkin, Vector3f initialPosition, int mlParams)
	{
		super((botName == null ? "MatLabNoClipBot" : botName), botSkin, initialPosition);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, the number
 *	of parameters to be passed to MatLab, and whether the agent should
 *	manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param mlParams number of parameters to be passed to MatLab on each update
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabNoClipBot(String botName, String botSkin, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabNoClipBot" : botName), botSkin, trackInv);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, the position
 *	to which the agent should move before entering the game as an active
 *	participant, the number of parameters to be passed to MatLab, and
 *	whether the agent should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param initialPosition the position to which the agent should move
 *	before entering the game as an active participant 
 *	@param mlParams number of parameters to be passed to MatLab on each update
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabNoClipBot(String botName, String botSkin, Vector3f initialPosition, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabNoClipBot" : botName), botSkin, initialPosition, trackInv);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, initial
 *	starting position, whether the agent should operate in high thread
 *	safety mode, the number of parameters to be passed to MatLab, and
 *	whether the agent should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param initialPosition the position to which the agent should move
 *	before entering the game as an active participant 
 *	@param highThreadSafety if true, enables high thread safety mode
 *	@param mlParams number of parameters to be passed to MatLab on each update
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabNoClipBot(String botName, String botSkin, Vector3f initialPosition, boolean highThreadSafety, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabNoClipBot" : botName), botSkin, initialPosition, highThreadSafety, trackInv);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, initial
 *	starting position, server password, whether the agent should operate
 *	in high thread safety mode, the number of parameters to be passed to
 *	MatLab, and whether the agent should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param initialPosition the position to which the agent should move
 *	before entering the game as an active participant 
 *	@param password the password of the server, if necessary
 *	@param highThreadSafety if true, enables high thread safety mode
 *	@param mlParams number of parameters to be passed to MatLab on each update
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabNoClipBot(String botName, String botSkin, Vector3f initialPosition, String password, boolean highThreadSafety, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabNoClipBot" : botName), botSkin, initialPosition, password, highThreadSafety, trackInv);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, initial
 *	starting position, connection receive rate, type of messages received
 *	from server, field of view, which hand the agent should hold its gun
 *	in, server password, whether the agent should operate in high thread
 *	safety mode, the number of parameters to be passed to MatLab, and
 *	whether the agent should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param initialPosition the position to which the agent should move
 *	before entering the game as an active participant 
 *	@param recvRate rate at which the client communicates with server
 *	@param msgLevel specifies which server messages to register interest in
 *	@param fov specifies the agent's field of vision
 *	@param hand specifies the hand in which the agent hold its gun
 *	@param password the password of the server, if necessary
 *	@param highThreadSafety if true, enables high thread safety mode
 *	@param mlParams number of parameters to be passed to MatLab on each update
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public MatLabNoClipBot(String botName, String botSkin, Vector3f initialPosition, int recvRate, int msgLevel, int fov, int hand, String password, boolean highThreadSafety, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabNoClipBot" : botName), botSkin, initialPosition, recvRate, msgLevel, fov, hand, password, highThreadSafety, trackInv);
		matLabParams = new Object[mlParams];
	}

/*-------------------------------------------------------------------*/
/**	The main loop of the AI cycle. Controls the preprocessing of data,
 *	the handing over of control to MatLab, and the postprocessing of
 *	MatLab's results. Implemented here as final, to prevent subclasses
 *	from overriding.
 *	@param world the current gamestate */
/*-------------------------------------------------------------------*/
	public final void runAI(World world)
	{
		preMatLab(world, matLabParams);	// pre-MatLab processing
		processMatLab();			// hand control to MatLab
		postMatLab(matLabResults);	// post-MatLab processing
	}

/*-------------------------------------------------------------------*/
/**	Populate the MatLab parameter array with the relevant values.
 *	@param world the current gamestate
 *	@param mlParams an Object array to be populated with data for MatLab
 *	(typically a series of float arrays) */
/*-------------------------------------------------------------------*/
	protected abstract void preMatLab(World world, Object[] mlParams);

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
}
