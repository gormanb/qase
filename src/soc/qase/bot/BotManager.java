//--------------------------------------------------
// Name:			BotManager.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot;

import soc.qase.bot.sample.SampleObserverBot;
import soc.qase.bot.sample.SamplePollingBot;

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
		BasicBot[] botArray = new BasicBot[5];

 		for(int i = 0; i < 5; i++)
		{
			if(i % 2 == 1)
				botArray[i] = new SampleObserverBot("ObserverBot_0" + i, null);
			else
				botArray[i] = new SamplePollingBot("PollingBot_0" + i, null);

			botArray[i].connect("127.0.0.1", -1);
		}

		try { Thread.sleep(90000); } catch(InterruptedException e) { };

		for(int i = 0; i < 5; i++)
			botArray[i].disconnect();
	}
}
