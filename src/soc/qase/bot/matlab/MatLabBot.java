//--------------------------------------------------
// Name:			MatLabBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot.matlab;

import soc.qase.bot.Bot;

/*-------------------------------------------------------------------*/
/**	An interface which extends the fundamental Bot interface, requiring
 *	that MatLabBots provide methods which allow functions written in ML
 *	to communicate with QASE. */
/*-------------------------------------------------------------------*/
public interface MatLabBot extends Bot
{
/*-------------------------------------------------------------------*/
/**	Called by MatLab to determine whether or not it should start
 *	running its AI routine.
 *	@return true if the agent is waiting for MatLab to perform its
 *	operations */
/*-------------------------------------------------------------------*/
	public boolean waitingForMatLab();

/*-------------------------------------------------------------------*/
/**	Called by MatLab to obtain an array of the objects upon which it
 *	is expected to act.
 *	@return an array of Objects, containing the data to be processed by
 *	MatLab (typically a series of float arrays) */
/*-------------------------------------------------------------------*/
	public Object[] getMatLabParams();

/*-------------------------------------------------------------------*/
/**	Called by MatLab to provide the bot with the results of its
 *	computations.
 *	@param results an Object array containing the results of MatLab's
 *	computations (typically consisting of a series of float arrays) */
/*-------------------------------------------------------------------*/
	public void setMatLabResults(Object[] results);

/*-------------------------------------------------------------------*/
/**	Called by MatLab to relinquish control back to QASE upon completion
 *	of its AI processing. */
/*-------------------------------------------------------------------*/
	public void releaseFromMatLab();
}
