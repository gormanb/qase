//---------------------------------------------------------------------
// Name:			ServerMessageHandler.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import soc.qase.com.packet.*;
import soc.qase.state.*;
import soc.qase.info.*;

import java.util.Observable;

/*-------------------------------------------------------------------*/
/**	Abstract class used as base for all classes which read and process
 *	server messages (ie Proxy and DM2Parser).
 *	@see soc.qase.com.Proxy
 *	@see soc.qase.file.dm2.DM2Parser */
/*-------------------------------------------------------------------*/
public abstract class ServerMessageHandler extends Observable
{
	protected World world = null;
	protected Server server = null;

	protected boolean verbose = false;
	protected boolean trackInventory = false;

/*-------------------------------------------------------------------*/
/**	Process an incoming ServerPacket, identifying the type of message
 *	it contains and reacting accordingly. See the following methods
 *	for a list of all valid message types. */
/*-------------------------------------------------------------------*/
	protected void processServerPacket(ServerPacket packet)
	{
		ClientPacket clientPacket = null;
		Message message = null;

		message = packet.getMessage();

		try
		{
			if(message instanceof ServerBad && verbose) System.out.println("Received: ServerBad");
			else if(message instanceof ServerPlayerMuzzleFlash) processServerPlayerMuzzleFlash((ServerPlayerMuzzleFlash)message);
			else if(message instanceof ServerMonsterMuzzleFlash) processServerMonsterMuzzleFlash((ServerMonsterMuzzleFlash)message);
			else if(message instanceof ServerTemporaryEntity) processServerTemporaryEntity((ServerTemporaryEntity)message);
			else if(message instanceof ServerLayout) processServerLayout((ServerLayout)message);
			else if(message instanceof ServerInventory) processServerInventory((ServerInventory)message);
			else if(message instanceof ServerNop && verbose) System.out.println("Received: ServerNop");
			else if(message instanceof ServerDisconnect) processServerDisconnect((ServerDisconnect)message);
			else if(message instanceof ServerReconnect) processServerReconnect((ServerReconnect)message);
			else if(message instanceof ServerSound) processServerSound((ServerSound)message);
			else if(message instanceof ServerPrint) processServerPrint((ServerPrint)message);
			else if(message instanceof ServerStuffText) processServerStuffText((ServerStuffText)message);
			else if(message instanceof ServerData) processServerData((ServerData)message);
			else if(message instanceof ServerConfigString) processServerConfigString((ServerConfigString)message);
			else if(message instanceof ServerSpawnBaseline) processServerSpawnBaseline((ServerSpawnBaseline)message);
			else if(message instanceof ServerCenterPrint && verbose) System.out.println("Received: ServerCenterPrint");
			else if(message instanceof ServerDownload && verbose) System.out.println("Received: ServerDownload");
			else if(message instanceof ServerPlayerInfo) processServerPlayerInfo((ServerPlayerInfo)message);
			else if(message instanceof ServerPacketEntities) processServerPacketEntities((ServerPacketEntities)message);
			else if(message instanceof ServerDeltaPacketEntities && verbose) System.out.println("Received: ServerDeltaPacketEntities");
			else if(message instanceof ServerFrame) processServerFrame((ServerFrame)message);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerPlayerMuzzleFlash(ServerPlayerMuzzleFlash message)
	{
		if(verbose)
			System.out.println("Processing: ServerPlayerMuzzleFlash");
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerMonsterMuzzleFlash(ServerMonsterMuzzleFlash message)
	{
		if(verbose)
			System.out.println("Processing: ServerMonsterMuzzleFlash");
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerTemporaryEntity(ServerTemporaryEntity message)
	{
		if(verbose)
			System.out.println("Processing: ServerTemporaryEntity");

		world.setTemporaryEntity(message.getTemporaryEntity());
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerLayout(ServerLayout message)
	{
		if(verbose)
			System.out.println("Processing: ServerLayout");

		world.setLayout(message.getLayout());
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerInventory(ServerInventory message)
	{
		if(verbose)
			System.out.println("Processing: ServerInventory");

		world.setInventory(message.getInventory());
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerDisconnect(ServerDisconnect message)
	{
		if(verbose)
			System.out.println("Processing: ServerDisconnect");
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerReconnect(ServerReconnect message)
	{
		if(verbose)
			System.out.println("Processing: ServerReconnect");
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerSound(ServerSound message)
	{
		if(verbose)
			System.out.println("Processing: ServerSound");

		world.processSound(message.getSound());
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerPrint(ServerPrint message)
	{
		if(verbose)
			System.out.println("Processing: ServerPrint");

		world.setMessage(message.getPrintString());
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerStuffText(ServerStuffText message)
	{
		if(verbose)
			System.out.println("Processing: ServerStuffText");
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerData(ServerData message)
	{
		if(verbose)
			System.out.println("Processing: ServerData");

		server = new Server(message);

		if(world != null)
			world.setServerData(server);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerConfigString(ServerConfigString message)
	{
		if(verbose)
			System.out.println("Processing: ServerConfigString - [" + message.getIndex() + " " + message.getConfigString() + "]");

		world.getConfig().setConfigString(message.getIndex(), message.getConfigString());
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerSpawnBaseline(ServerSpawnBaseline message)
	{
		if(verbose)
			System.out.println("Processing: ServerSpawnBaseline");

		world.setEntity(message.getEntity(), true);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerPlayerInfo(ServerPlayerInfo message)
	{
		if(verbose)
			System.out.println("Processing: ServerPlayerInfo");

		world.setPlayer(message.getPlayer());
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerPacketEntities(ServerPacketEntities message)
	{
		if(verbose)
			System.out.println("Processing: ServerPacketEntities");		

		world.setEntities(message.getEntities());		
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	protected void processServerFrame(ServerFrame message)
	{
		if(verbose)
			System.out.println("Processing: ServerFrame");

		world.setFrame(message);
	}

/*-------------------------------------------------------------------*/
/**	Specify whether the ServerMessageHandler should print notification
 *	of its operations to the standard output. Useful for debugging.
 *	@param verb true if the class should print a log to stdout */
/*-------------------------------------------------------------------*/
	public synchronized void setVerbose(boolean verb)
	{
		verbose = verb;
	}

/*-------------------------------------------------------------------*/
/**	Check whether the ServerMessageHandler prints notification of its
 *	operations to the standard output.
 *	@return true if the class prints a log to stdout, false otherwise */
/*-------------------------------------------------------------------*/
	public synchronized boolean getVerbose()
	{
		return verbose;
	}
}
