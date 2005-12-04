//--------------------------------------------------
// Name:			BasicBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot;

import soc.qase.bot.sample.*;
import soc.qase.file.dm2.*;
import soc.qase.state.*;

/*-------------------------------------------------------------------*/
/**	An executable class, designed to demonstrate QASE. Connects three
 *	SamplePollingBots and two SampleObserverBots to a game server on the
 *	local machine.
 *	@see SampleObserverBot
 *	@see SamplePollingBot */
/*-------------------------------------------------------------------*/
public class BotManager extends Thread
{
/*-------------------------------------------------------------------*/
/**	Instantiate the agents and connect them to the game server. */
/*-------------------------------------------------------------------*/
	public static void main(String[] args)
	{
 		for(int i = 0; i < 5; i++)
		{
			if(i % 2 == 1)
				(new SampleObserverBot("ObserverBot_0" + i, null)).connect("127.0.0.1", -1);
			else
				(new SamplePollingBot("PollingBot_0" + i, null)).connect("127.0.0.1", -1);
		}
	}
}
