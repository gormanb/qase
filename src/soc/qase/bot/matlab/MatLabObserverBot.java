//---------------------------------------------------------------------
// Name:			MatLabObserverBot.java
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
import soc.qase.bot.ObserverBot;

/*-------------------------------------------------------------------*/
/**	MatLabObserverBot provides a framework to allow QASE to interact
 *	with MatLab. MatLab must first be used to create and connect the bot;
 *	thereafter, on each update the bot will pre-process the data to
 *	be used by MatLab (in method preMatLab), hand control over to MatLab
 *	(processMatLab), and will post-process the results which MatLab
 *	returns via the setMatLabResults method. Due to its significantly
 *	better performance, this paradigm is preferred over the direct access
 *	provided by the MatLabGeneralBot family. For an example of MatLabObserverBot
 *	in action, see SampleMatLabObserverBot and the accompanying MatLab script.
 *	See also the QASE User Guide, Sections 5.5 and 6.2.*/
/*-------------------------------------------------------------------*/
public abstract class MatLabObserverBot extends ObserverBot implements MatLabBot
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
	public MatLabObserverBot(String botName, String botSkin, int mlParams)
	{
		super((botName == null ? "MatLabObserverBot" : botName), botSkin);
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
	public MatLabObserverBot(String botName, String botSkin, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabObserverBot" : botName), botSkin, trackInv);
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
	public MatLabObserverBot(String botName, String botSkin, boolean highThreadSafety, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabObserverBot" : botName), botSkin, highThreadSafety, trackInv);
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
	public MatLabObserverBot(String botName, String botSkin, String password, boolean highThreadSafety, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabObserverBot" : botName), botSkin, password, highThreadSafety, trackInv);
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
	public MatLabObserverBot(String botName, String botSkin, int recvRate, int msgLevel, int fov, int hand, String password, boolean highThreadSafety, int mlParams, boolean trackInv)
	{
		super((botName == null ? "MatLabObserverBot" : botName), botSkin, recvRate, msgLevel, fov, hand, password, highThreadSafety, trackInv);
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
}
