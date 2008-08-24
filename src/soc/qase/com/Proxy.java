//---------------------------------------------------------------------
// Name:			Proxy.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.tools.Utils;
import soc.qase.com.packet.*;
import soc.qase.com.message.*;

import soc.qase.file.dm2.DM2Recorder;

/*-------------------------------------------------------------------*/
/**	The Proxy class is a wrapper class for high-level communication
 *	between a Quake2 client and a Quake2 server. The class takes care
 *	of the actual connection and game-handling requirements of the API.
 *	It is used by an agent to connect to the simulator environment, i.e.
 *	it corresponds to a QASE agent interface implementation. Furthermore,
 *	it is used to receive information concerning itself and visual entities,
 *	and to perform actions in the environment. */
/*-------------------------------------------------------------------*/
public class Proxy extends ServerMessageHandler implements Runnable
{
	private int port = 0;
	private String host = null;

	private int clientID = 0;
	private boolean connected = false;

	private Thread recvThread = null;
	private boolean threadSafe = false;
	private CommunicationHandler communicator = null;

	// information wrapper
	private User user = null;

	// state wrappers
	private Angles angles = null;
	private Velocity velocity = null;
	private Action action = null;
	private Move currentMove = null;
	private Move previousMove = null;
	private Move lastMove = null;

	// game information
	private boolean inGame = false;
	private DM2Recorder dm2Recorder = null;

	private int currentCTFTeam = Integer.MIN_VALUE;

	// connection information
	private boolean reconnect = false;
	private boolean sentChallenge = false;
	private boolean sentConnect = false;
	private boolean recvThreadTerminated = true;
	private boolean autoInventoryRefresh = false;

	private static Vector allocatedCIDs = new Vector();
	private static Random numGen = new Random(System.currentTimeMillis());

/*-------------------------------------------------------------------*/
/**	Default constructor. Allocates a unique client ID and instantiates
 *	the DM2Recorder, in case it is needed later. */
/*-------------------------------------------------------------------*/
	public Proxy()
	{
		allocateCID();
		dm2Recorder = new DM2Recorder();
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the specification of user and thread safety details.
 *	@param user user information to be used when connecting to a
 *	host. 
 *	@param highThreadSafety specifies whether the proxy should operate
 *	in high thread safety mode. If true, the proxy will lock itself from
 *	accessor and mutator calls during the processing of each block of
 *	incoming data. */
/*-------------------------------------------------------------------*/
	public Proxy(User user, boolean highThreadSafety)
	{
		allocateCID();
		this.user = user;
		threadSafe = highThreadSafety;
		dm2Recorder = new DM2Recorder();
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the specification of user, thread safet and
 *	inventory-tracking details.
 *	@param user user information to be used when connecting to a
 *	host. 
 *	@param highThreadSafety specifies whether the proxy should operate
 *	in high thread safety mode. If true, the proxy will lock itself from
 *	accessor and mutator calls during the processing of each block of
 *	incoming data.
 *	@param trackInv specifies whether the Proxy should manually track
 *	the player's inventory as he collects and uses items.*/
/*-------------------------------------------------------------------*/
	public Proxy(User user, boolean highThreadSafety, boolean trackInv)
	{
		allocateCID();
		this.user = user;
		trackInventory = trackInv;
		threadSafe = highThreadSafety;
		dm2Recorder = new DM2Recorder();
	}

/*-------------------------------------------------------------------*/
/**	Determine and allocate a new, unused client ID number for the new
 *	connection.*/
/*-------------------------------------------------------------------*/
	private void allocateCID()
	{
		synchronized(allocatedCIDs)
		{
			Integer[] allocatedCIDArray = null;

			if(allocatedCIDs.size() > 0)
			{
				allocatedCIDArray = new Integer[allocatedCIDs.size()];
				allocatedCIDs.toArray(allocatedCIDArray);
				Arrays.sort(allocatedCIDArray);
			}

			do
			{
				clientID = numGen.nextInt(65535) + 1;
			}
			while(allocatedCIDs.size() != 0 && Arrays.binarySearch(allocatedCIDArray, new Integer(clientID)) >= 0);
	
			allocatedCIDs.add(new Integer(clientID));
		}
	}

/*-------------------------------------------------------------------*/
/**	Connect to specified host.
 *	@param host hostname of server
 *	@param port portnumber of server; -1 for default (27920)
 *	@return true if the connect call was successful, otherwise
 *	false. */
/*-------------------------------------------------------------------*/
	public synchronized boolean connect(String host, int port)
	{
		boolean result = false;

		try
		{
			if(connected)
				disconnect();

			if(recvThread != null)
			{
				while(!recvThreadTerminated)
					Thread.sleep(10);
			}

			communicator = new CommunicationHandler(clientID);

			this.host = host;
			this.port = port;

			if(communicator.connect(host, port))
			{
				connected = true;
				init();
				result = connected;
			}
		}
		catch(Exception e)
		{	}

		waitForSpawn();

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Connect to specified host.
 *	@param host hostname of server
 *	@param port portnumber of server; -1 for default (27920)
 *	@param recordDM2File the filename to which the game session should
 *	be recorded, or null if none
 *	@return true if the connect call was successful, otherwise
 *	false. */
/*-------------------------------------------------------------------*/
	public synchronized boolean connect(String host, int port, String recordDM2File)
	{
		if(recordDM2File != null) dm2Recorder.startRecording(recordDM2File);
		return connect(host, port);
	}

/*-------------------------------------------------------------------*/
/**	Disconnect from current host. Equivalent to disconnect(true).*/
/*-------------------------------------------------------------------*/
	public synchronized void disconnect()
	{
		disconnect(true);
	}

/*-------------------------------------------------------------------*/
/**	Disconnect from current host.
 *	@param stopRecording specifies whether the DM2Recorder should stop
 *	recording the current game session; used by QASE when recording
 *	multi-map demos*/
/*-------------------------------------------------------------------*/
	public synchronized void disconnect(boolean stopRecording)
	{
		ClientPacket packet = null;
		ClientCommand message = null;

		if(dm2Recorder.isRecording() && stopRecording)
			dm2Recorder.stopRecording();

		if(connected)
		{
			inGame = false;
			message = new ClientCommand(clientID, "disconnect");
			packet = new ClientPacket(message);
			communicator.sendUnreliable(packet);
			communicator.disconnect();
			connected = false;
		}
	}

/*-------------------------------------------------------------------*/
/**	Suspends the thread until such time as the agent is spawned into the
 *	game environment. */
/*-------------------------------------------------------------------*/
	protected void waitForSpawn()
	{
		while(!(inGame() && getWorld().getPlayer().isAlive()))
			Thread.yield();
	}

/*-------------------------------------------------------------------*/
/**	Resolve the CTF team number of the local agent, if the current server
 *	is running the CTF mod.
 *	@return the team number of the local agent; 0 = RED, 1 = BLUE */
/*-------------------------------------------------------------------*/
	public int getCTFTeamNumber()
	{
		return currentCTFTeam;
	}

/*-------------------------------------------------------------------*/
/**	Resolve the CTF team name of the local agent, if the current server
 *	is running the CTF mod.
 *	@return the team name of the local agent; either RED, BLUE or null
 *	if the agent is not currently on a team. */
/*-------------------------------------------------------------------*/
	public String getCTFTeamString()
	{
		return (currentCTFTeam >= 0 ? Server.CTF_STRINGS[currentCTFTeam] : null);
	}

/*-------------------------------------------------------------------*/
/**	Check whether the proxy is operating in high thread safety mode.
 *	@return true if the proxy is operating in high thread safety mode,
 *	false otherwise */
/*-------------------------------------------------------------------*/

	public synchronized boolean getHighThreadSafety()
	{
		return threadSafe;
	}

/*-------------------------------------------------------------------*/
/**	Set the thread safety level of the proxy.
 *	@param highThreadSafety true if the proxy should switch to high
 *	thread safety mode, false if it should operate under normal thread safety */
/*-------------------------------------------------------------------*/

	public synchronized void setHighThreadSafety(boolean highThreadSafety)
	{
		threadSafe = highThreadSafety;
	}

/*-------------------------------------------------------------------*/
/**	Check if the proxy is currently engaged in a game.
 *	@return true if the proxy is currently engaged in a game,
 *	otherwise false */
/*-------------------------------------------------------------------*/
	public synchronized boolean inGame()
	{
		return inGame;
	}

/*-------------------------------------------------------------------*/
/**	Check if the proxy is recording the current game.
 *	@return true if the proxy is recording the current game,
 *	otherwise false */
/*-------------------------------------------------------------------*/
	public boolean isRecording()
	{
		return dm2Recorder.isRecording();
	}

/*-------------------------------------------------------------------*/
/**	Determine whether the current server is running CTF.
 *	@return true if the server is running CTF, false otherwise. */
/*-------------------------------------------------------------------*/
	public boolean isCTFServer() 
	{
		return server != null && server.isCTFServer();
	}

/*-------------------------------------------------------------------*/
/**	Get current state of an ongoing game.
 *	@return a world object representing the current gamestate */
/*-------------------------------------------------------------------*/
	public synchronized World getWorld()
	{
		if(!inGame)
			return null;

		return world;
	}

/*-------------------------------------------------------------------*/
/**	Obtain the current Server object, containing information about the
 *	server and game session.
 *	@return the current server object
 *	@see Server */
/*-------------------------------------------------------------------*/
	public synchronized Server getServer()
	{
		if(!inGame)
			return null;

		return server;
	}

/*-------------------------------------------------------------------*/
/**	Send client movement information to a connected host. This method
 *	does not actually send the information, but rather stores the move
 *	details to be transmitted at the appropriate point in the main
 *	Proxy thread.
 *	@param angles desired movement angles
 *	@param velocity desired movement velocity
 *	@param action desired movement action (attack, use, any)
 *	@see Angles
 *	@see Velocity
 *	@see Action*/
/*-------------------------------------------------------------------*/
	public void sendMovement(Angles angles, Velocity velocity, Action action)
	{
		this.angles = angles;
		this.velocity = velocity;
		this.action = action;
	}

/*-------------------------------------------------------------------*/
/**	Initialise the agent in preparation for a game session. */
/*-------------------------------------------------------------------*/
	private void init()
	{
		// movement wrappers
		angles = new Angles(0, 0, 0);
		velocity = new Velocity(0, 0, 0);
		action = new Action(false, false, false);
		currentMove = new Move(angles, velocity, action, 0);
		previousMove = new Move(angles, velocity, action, 0);
		lastMove = new Move(angles, velocity, action, 0);

		// game information
		world = null;
		server = null;
		inGame = false;

		// connection information
		sentChallenge = false;
		sentConnect = false;
		reconnect = false;

		// start proxy
		recvThread = new Thread(this);
		recvThread.start();
		sentChallenge = true;
		communicator.sendConnectionless("getchallenge");
	}

/*-------------------------------------------------------------------*/
/**	Specifies whether the Proxy should automatically request a full
 *	listing of the agent's inventory on each frame. This can be used in
 *	place of manual inventory tracking - it ensures greater accuracy, at
 *	the cost of increasing the amount of network traffic per update.
 *	@param refresh turn auto inventory refresh on/off */
/*-------------------------------------------------------------------*/
	public void setAutoInventoryRefresh(boolean refresh)
	{
		autoInventoryRefresh = refresh;

		if(inGame() && refresh)
			refreshInventory();
	}

/*-------------------------------------------------------------------*/
/**	Request a full copy of the agent's current inventory. Used when
 *	auto inventory refresh is enabled.*/
/*-------------------------------------------------------------------*/
	public void refreshInventory()
	{
		sendConsoleCommand("inven");
		sendConsoleCommand("inven");
	}

/*-------------------------------------------------------------------*/
/**	Use the specified item, if the agent is currently in possession of it.
 *	Called when changing weapons.
 *	@param item the index of the item to use.*/
/*-------------------------------------------------------------------*/
	public void useItem(int item)
	{
		sendConsoleCommand("use " + world.getConfig().getConfigString(Config.SECTION_ITEM_NAMES + item));
		world.processUsedItem(item);
	}

/*-------------------------------------------------------------------*/
/**	Send console message to connected host. This is a blocking call
 *	and it will not return until the proxy receives a reliable
 *	answer from the connected host.
 *	@param command message to send */
/*-------------------------------------------------------------------*/
	public void sendCommand(String command)
	{
		communicator.sendReliable(buildCommandPacket(command));
	}

/*-------------------------------------------------------------------*/
/**	Send a non-blocking console message to connected host.
 *	@param command message to send */
/*-------------------------------------------------------------------*/
	public void sendConsoleCommand(String command)
	{
		communicator.sendUnreliable(buildCommandPacket(command));
	}

/*-------------------------------------------------------------------*/
/**	Constructs a new packet instructing the server to execute a command.
 *	Called by sendCommand and sendConsoleCommand.
 *	@param command the command to send to the server */
/*-------------------------------------------------------------------*/
	protected ClientPacket buildCommandPacket(String command)
	{
		return new ClientPacket(new ClientCommand(clientID, command));
	}

/*-------------------------------------------------------------------*/
/**	Send a 'begin' message to the server, indicating the agent's intent
 *	to enter a game session.*/
/*-------------------------------------------------------------------*/
	private void sendBegin()
	{
		String command = null;
		ClientPacket packet = null;
		ClientCommand message = null;

		message = new ClientCommand(clientID, "begin " + server.getLevelKey());
		packet = new ClientPacket(message);
		communicator.sendReliable(packet);
	}

/*-------------------------------------------------------------------*/
/**	Send client movement information to a connected host. This method
 *	actually performs the transmission of the client's desired move.
 *	@see #sendMovement(Angles, Velocity, Action) */
/*-------------------------------------------------------------------*/
	private void sendMove()
	{
		ClientPacket packet = null;
		ClientMove message = null;
		
		lastMove = previousMove;
		previousMove = currentMove;
		currentMove = new Move(angles, velocity, action, communicator.getPing());
		message = new ClientMove(clientID, world.getFrame(), currentMove, previousMove, lastMove, communicator.getNextSequence());
		packet = new ClientPacket(message);
		communicator.sendUnreliable(packet);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void processConnectionlessPacket(ConnectionlessPacket packet)
	{
		String challengeNumber = null;
		String connectResult = null;

		if(sentChallenge)
		{
			sentChallenge = false;
			challengeNumber = packet.getMessage().toString().substring(10);

			sentConnect = true;
			communicator.sendConnectionless("connect 34 " + clientID + " " + challengeNumber + " \"" + user.toString() + "\"");
		}
		else if(sentConnect)
		{
			sentConnect = false;
			connectResult = packet.getMessage().toString();

			if(connectResult.equals("client_connect"))
			{
				world = new World(trackInventory);
				sendCommand("new");
			}
		}
	}

/*-------------------------------------------------------------------*/
/**	Processes the ServerDisconnect message by disconnecting from the
 *	current game session.
 *	@param message the ServerDisconnect message for processing
 */
/*-------------------------------------------------------------------*/
	protected void processServerDisconnect(ServerDisconnect message)
	{
		if(verbose)
			System.out.println("Processing: ServerDisconnect");

		disconnect();
	}

/*-------------------------------------------------------------------*/
/**	Processes the ServerReconnect message by disconnecting from the
 *	current game session and then reconnecting.
 *	@param message the ServerReconnect message for processing
 */
/*-------------------------------------------------------------------*/
	protected void processServerReconnect(ServerReconnect message)
	{
		if(verbose)
			System.out.println("Processing: ServerReconnect");

		disconnect(false);
		reconnect = true;
	}

/*-------------------------------------------------------------------*/
/**	Processes the ServerStuffText message by parsing the command
 *	string and acting accordingly.
 *	@param message the ServerStuffText message for processing
 */
/*-------------------------------------------------------------------*/
	protected void processServerStuffText(ServerStuffText message)
	{
		if(verbose)
			System.out.println("Processing: ServerStuffText");

		StringTokenizer st = null;
		String currentToken = null;

		st = new StringTokenizer(message.getStuffString());
		currentToken = st.nextToken();

		if(currentToken.equals("cmd"))
			sendCommand(message.getStuffString().substring(4));
		else if(currentToken.equals("precache"))
		{
			sendBegin();

			if(autoInventoryRefresh)
				refreshInventory();
		}
	}

/*-------------------------------------------------------------------*/
/**	Processes the ServerPacketEntities message by extracting and
 *	storing the entity data, and marking the agent as being active
 *	in the game world.
 *	@param message the ServerPacketEntities message for processing
 */
/*-------------------------------------------------------------------*/
	protected void processServerPacketEntities(ServerPacketEntities message)
	{
		super.processServerPacketEntities(message);
		inGame = true;
	}

/*-------------------------------------------------------------------*/
/**	Processes the ServerInventory message by extracting and storing
 *	the inventory data.
 *	@param message the ServerInventory message for processing
 */
/*-------------------------------------------------------------------*/
	protected void processServerInventory(ServerInventory message)
	{
		super.processServerInventory(message);

		if(autoInventoryRefresh)
			refreshInventory();
	}

/*-------------------------------------------------------------------*/
/**	Processes the ServerPrint message by extracting and storing
 *	the message data. Also checks to see whether the local agent has
 *	switched teams in a CTF game.
 *	@param message the ServerPrint message for processing
 */
/*-------------------------------------------------------------------*/
	protected void processServerPrint(ServerPrint message)
	{
		super.processServerPrint(message);

		for(int i = 0; i < 2; i++)
		{
			if(message.getPrintString().equals(user.getName() + " joined the " + Server.CTF_STRINGS[i] + " team."))
			{
				currentCTFTeam = i;
				break;
			}
		}
	}

/*-------------------------------------------------------------------*/
/**	The main loop of the Proxy thread. Controls server synchronisation,
 *	data processing, map changes, etc. */
/*-------------------------------------------------------------------*/
	public void run()
	{
		int lastFrameNum = 0;

		try
		{
			byte[] incomingData = null;
			recvThreadTerminated = false;
		
			while(connected)
			{
				if(world != null)
					lastFrameNum = world.getFrame();

				incomingData = communicator.receiveData();

				if(threadSafe && inGame)
				{
					synchronized(world)
					{
						processIncomingDataPacket(incomingData);
					}
				}
				else
					processIncomingDataPacket(incomingData);

				if(connected && world != null && lastFrameNum != world.getFrame() && countObservers() > 0)
				{
					setChanged();
					notifyObservers(world);
				}

				if(inGame)
					sendMove();

				Thread.yield();
			}

			if(communicator.isConnected())
				communicator.disconnect();

			recvThreadTerminated = true;
		}
		catch(Exception e)
		{	}

		if(reconnect)
		{
			try
			{	Thread.sleep(8000 + (int)(Math.round(Math.random() * 10000)));	}	// pause to allow server to restart
			catch(InterruptedException ie)
			{	}

			reconnect = false;
			dm2Recorder.newMap();

			final boolean ctf = isCTFServer();

			server = null;
			world = new World(trackInventory);

			(new Thread()
				{
					public void run()
					{
						connect(host, port);

						if(ctf)
						{
							waitForSpawn();
							sendConsoleCommand("team " + Server.CTF_STRINGS[(currentCTFTeam >= 0 ? currentCTFTeam : (int)Math.round(Math.random()))]);
						}
					}
				}
			).start();
		}
	}

/*-------------------------------------------------------------------*/
/**	Process incoming data. Abstracted from the core thread loop so that
 *	the Proxy object may be optionally locked before this method is called.
 *	This thread also passes the network stream to the DM2Recorder, if
 *	active, for saving to file.
 *	@see #setHighThreadSafety(boolean)
 *	@see soc.qase.file.dm2.DM2Recorder */
/*-------------------------------------------------------------------*/
	private void processIncomingDataPacket(byte[] incomingData)
	{
		Packet packet = null;
		Sequence sequenceOne = new Sequence(incomingData);

		if(sequenceOne.intValue() == 0x7fffffff && sequenceOne.isReliable())
		{
			packet = new ConnectionlessPacket(incomingData);
			processConnectionlessPacket((ConnectionlessPacket)packet);
		}
		else
		{
			if(inGame && dm2Recorder.isRecording())
				dm2Recorder.addData(incomingData);
			else if(!inGame && dm2Recorder.isRecording())
				dm2Recorder.addHeader(incomingData);

			if(incomingData != null)
			{
				int dataIndex = 8;

				while(dataIndex != incomingData.length)
				{
					packet = new ServerPacket(incomingData, dataIndex);
					processServerPacket((ServerPacket)packet);

					dataIndex += packet.getLength();
				}
			}
		}
	}
}

