//--------------------------------------------------
// Name:			BasicBot.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.bot;

import soc.qase.com.*;
import soc.qase.info.*;
import soc.qase.state.*;
import soc.qase.file.bsp.*;
import soc.qase.file.pak.*;
import soc.qase.tools.Utils;
import soc.qase.ai.waypoint.*;
import soc.qase.tools.vecmath.*;

import java.io.*;
import java.util.Vector;

import java.util.Observable;

/*-------------------------------------------------------------------*/
/**	An abstract class which provides all the base functionality needed
 *	by the game agent. All that is required is to provide a means of
 *	handling server updates, as in the ObserverBot and PollingBot classes.
 *	BasicBot also provides tailored, embedded access to the BSPParser and
 *	WaypointMap classes - methods in this class simply relay calls to the
 *	the BSPParser or Waypoint object as appropriate, with certain parameters
 *	pre-defined to the most useful values from the perspective of game
 *	agents (e.g. the bounding box used to trace through the level is set to
 *	the size of the agent's in-game character's bounding box). Users can
 *	also obtain a pointer to the underlying object, thereby allowing full
 *	access to all their facilities.
 *	@see ObserverBot
 *	@see PollingBot */
/*-------------------------------------------------------------------*/
public abstract class BasicBot extends Thread implements Bot
{
	private User user = null;
	protected Proxy proxy = null;

	private Angles angles = new Angles(0, 0, 0);
	private Angles delta_Angles = new Angles(0, 0, 0);

	private Velocity velocity = new Velocity(0, 0, 0);
	private Action action = new Action(false, false, false);

	private boolean connected = false;
	private boolean threadSafe = false;
	private boolean traceFromView = false;

	protected boolean ctfTeamAssigned = false;

	private boolean mapNotFound = false;
	private static String q2HomeDir = null;

	protected WaypointMap wpMap = null;
	private BSPParser bsp = new BSPParser();

	private float sphereRadius = 18.0f;
	private boolean globalAngles = true;
	private static final Vector3f BOUNDING_MAX = new Vector3f(9, 25, 9);
	private static final Vector3f BOUNDING_MIN = new Vector3f(-9, -25, -9);

/*-------------------------------------------------------------------*/
/**	Default constructor. Creates the agent using default parameters. */
/*-------------------------------------------------------------------*/
	public BasicBot()
	{
		user = new User("QASE_Bot", "female/athena", 65535, 1, 90, User.HAND_RIGHT, "");
		commonSetup(false, false);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name and skin (appearance)
 *	for the agent.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance */
/*-------------------------------------------------------------------*/
	public BasicBot(String botName, String botSkin)
	{
		user = new User((botName == null ? "QASE_BasicBot" : botName), (botSkin == null ? "female/athena" : botSkin), 65535, 1, 90, User.HAND_RIGHT, "");
		commonSetup(false, false);
	}

/*-------------------------------------------------------------------*/
/**	Constructor allowing the user to specify a name, skin, and whether
 *	it should manually track its inventory.
 *	@param botName name of the character during game session
 *	@param botSkin specifies the character's in-game appearance
 *	@param trackInv if true, the agent will manually track its inventory */
/*-------------------------------------------------------------------*/
	public BasicBot(String botName, String botSkin, boolean trackInv)
	{
		user = new User((botName == null ? "QASE_BasicBot" : botName), (botSkin == null ? "female/athena" : botSkin), 65535, 1, 90, User.HAND_RIGHT, "");
		commonSetup(false, trackInv);
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
	public BasicBot(String botName, String botSkin, boolean highThreadSafety, boolean trackInv)
	{
		user = new User((botName == null ? "QASE_BasicBot" : botName), (botSkin == null ? "female/athena" : botSkin), 65535, 1, 90, User.HAND_RIGHT, "");
		commonSetup(highThreadSafety, trackInv);
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
	public BasicBot(String botName, String botSkin, String password, boolean highThreadSafety, boolean trackInv)
	{
		user = new User((botName == null ? "QASE_BasicBot" : botName), (botSkin == null ? "female/athena" : botSkin), 65535, 1, 90, User.HAND_RIGHT, password);
		commonSetup(highThreadSafety, trackInv);
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
	public BasicBot(String botName, String botSkin, int recvRate, int msgLevel, int fov, int hand, String password, boolean highThreadSafety, boolean trackInv)
	{
		user = new User((botName == null ? "QASE_BasicBot" : botName), (botSkin == null ? "female/athena" : botSkin), (recvRate < 0 ? 65535 : recvRate), (msgLevel < 0 ? 1 : msgLevel), (fov < 0 ? 90 : fov), (hand < 0 ? User.HAND_RIGHT : hand), (password == null ? "" : password));
		commonSetup(highThreadSafety, trackInv);
	}

	private void commonSetup(boolean highThreadSafety, boolean trackInv)
	{
		threadSafe = highThreadSafety;
		proxy = new Proxy(user, highThreadSafety, trackInv);
	}

// all abstract methods to be supplied by derived classes
// custom bots should synchronize on World if high thread safety is enabled
/*-------------------------------------------------------------------*/
/**	Disconnect from the server. To be implemented by derived classes.*/
/*-------------------------------------------------------------------*/
	public abstract void disconnect();
/*-------------------------------------------------------------------*/
/**	Connect to a game server. To be implemented by derived classes.
 *	@param host a String representation of the host machine's IP address
 *	@param port the port on which the game server is running */
/*-------------------------------------------------------------------*/
	public abstract boolean connect(String host, int port);

/*-------------------------------------------------------------------*/
/**	Connect to a CTF game server. To be implemented by derived classes.
 *	@param host a String representation of the host machine's IP address
 *	@param port the port on which the game server is running
 *	@param ctfTeam the team to join; one of the CTF constants found in
 *	soc.qase.info.Server */
/*-------------------------------------------------------------------*/
	public abstract boolean connect(String host, int port, int ctfTeam);

/*-------------------------------------------------------------------*/
/**	Switch the agent to a specified team during a CTF match (assuming in-game
 *	team switching is enabled).
 *	@param ctfTeam the team to join; one of the CTF constants found in
 *	soc.qase.info.Server */
/*-------------------------------------------------------------------*/
	protected void setCTFTeam(int ctfTeam)
	{
		ctfTeamAssigned = true;
		sendConsoleCommand("team " + Server.CTF_STRINGS[(Math.abs(ctfTeam) < 2 ? Math.abs(ctfTeam) : (int)Math.round(Math.random()))]);
	}

/*-------------------------------------------------------------------*/
/**	Resolve the CTF team number of the local agent, if the current server
 *	is running the CTF mod.
 *	@return the team number of the local agent; 0 = RED, 1 = BLUE */
/*-------------------------------------------------------------------*/
	protected int getCTFTeamNumber()
	{
		return (proxy == null ? Integer.MIN_VALUE : proxy.getCTFTeamNumber());
	}

/*-------------------------------------------------------------------*/
/**	Resolve the CTF team name of the local agent, if the current server
 *	is running the CTF mod.
 *	@return the team name of the local agent; either RED, BLUE or null
 *	if the agent is not currently on a team. */
/*-------------------------------------------------------------------*/
	protected String getCTFTeamString()
	{
		return (proxy == null ? null : proxy.getCTFTeamString());
	}

/*-------------------------------------------------------------------*/
/**	Determined whether the given player Entity is on the same CTF team
 *	as the local agent.
 *	@param otherPlayer the Entity representing the player to be checked
 *	@return true if otherPlayer is on the same CTF team as the local agent,
 *	false otherwise */
/*-------------------------------------------------------------------*/
	protected boolean isOnSameCTFTeam(Entity otherPlayer)
	{
		return (getCTFTeamNumber() >= 0 && getCTFTeamNumber() == otherPlayer.getCTFTeamNumber());
	}

/*-------------------------------------------------------------------*/
/**	The core AI routine. To be implemented by derived classes.
 *	@param w a World object representing the current gamestate */
/*-------------------------------------------------------------------*/
	public abstract void runAI(World w);

/*-------------------------------------------------------------------*/
/**	Returns the Player object associated with this bot, which can then
 *	be further queried for information.
 *	@return the Player object containing full details of the bot's
 *	current state, or null if no such object exists */
/*-------------------------------------------------------------------*/
	protected Player getPlayer()
	{
		return ((proxy == null || proxy.getWorld() == null) ? null : proxy.getWorld().getPlayer());
	}

/*-------------------------------------------------------------------*/
/**	Check whether the agent is currently alive and active in the game.
 *	@return true if the agent is active and alive, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isBotAlive()
	{
		return proxy != null && proxy.inGame() && proxy.getWorld().getPlayer().isAlive();
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently jumping.
 *	@return true if jumping, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isJumping()
	{
		return proxy != null && proxy.inGame() && proxy.getWorld().getPlayer().isJumping();
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently ducking.
 *	@return true if ducking, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isDucked()
	{
		return proxy != null && proxy.inGame() && proxy.getWorld().getPlayer().isDucked();
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently in water.
 *	@return true if in water, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isUnderWater()
	{
		return proxy != null && proxy.inGame() && proxy.getWorld().getPlayer().isUnderWater();
	}

/*-------------------------------------------------------------------*/
/**	Get the agent's current health.
 *	@return the agent's health value */
/*-------------------------------------------------------------------*/
	public int getHealth()
	{
		return proxy.getWorld().getPlayer().getPlayerStatus().getStatus(PlayerStatus.HEALTH);
	}

/*-------------------------------------------------------------------*/
/**	Get the amount of ammo the agent has for the current weapon.
 *	@return the current ammo */
/*-------------------------------------------------------------------*/
	public int getAmmo()
	{
		return proxy.getWorld().getPlayer().getPlayerStatus().getStatus(PlayerStatus.AMMO);
	}

/*-------------------------------------------------------------------*/
/**	Get the amount of armor currently held by the agent.
 *	@return the current armor value */
/*-------------------------------------------------------------------*/
	public int getArmor()
	{
		return proxy.getWorld().getPlayer().getPlayerStatus().getStatus(PlayerStatus.ARMOR);
	}

/*-------------------------------------------------------------------*/
/**	Flag the agent as being connected to or disconnected from the game server.
 *	@param value true if the agent is connected, false otherwise */
/*-------------------------------------------------------------------*/
	protected void setConnected(boolean value)
	{
		connected = value;
	}

/*-------------------------------------------------------------------*/
/**	Check whether the agent is connected to the server.
 *	@return value true if the agent is connected, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isConnected()
	{
		return connected;
	}

/*-------------------------------------------------------------------*/
/**	Set the agent to operate in low or high thread safety mode. In
 *	high thread safety mode, the gamestate object is locked while
 *	QASE is reading from it, preventing errors which may occur due to
 *	updates arriving in the middle of the AI cycle. Since BasicBot does
 *	not implement a server-handling mechanism, it is the responsibility
 *	of derived classes to correctly observe this contract (as is the case
 *	with ObserverBot and PollingBot). Note that, because the Proxy object
 *	is implicitly locked while updating all registered ObserverBots, high
 *	thread safety is not required except in cases where external threads
 *	may intervene in mid-AI cycle.
 *	@param highThreadSafety true to enable high safety mode, false to disable
 *	@see ObserverBot
 *	@see PollingBot*/
/*-------------------------------------------------------------------*/
	public void setHighThreadSafety(boolean highThreadSafety)
	{
		threadSafe = highThreadSafety;
		proxy.setHighThreadSafety(highThreadSafety);
	}

/*-------------------------------------------------------------------*/
/**	Check whether the agent is operating in high thread-safety mode.
 *	@return true if the agent is in high safety mode, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean getHighThreadSafety()
	{
		return threadSafe;
	}

/*-------------------------------------------------------------------*/
/**	Specifies whether the agent should automatically request a full
 *	listing of the agent's inventory on each frame. This can be used in
 *	place of manual inventory tracking - it ensures greater accuracy, at
 *	the cost of increasing the amount of network traffic per update.
 *	@param refresh turn auto inventory refresh on/off */
/*-------------------------------------------------------------------*/
	public void setAutoInventoryRefresh(boolean refresh)
	{
		proxy.setAutoInventoryRefresh(refresh);
	}

/*-------------------------------------------------------------------*/
/**	Get the specified angle, as defined by the constants in the
 *	Angles class.
 *	@param angleType the angle to return
 *	@see soc.qase.state.Angles */
/*-------------------------------------------------------------------*/
	public float getAngle(int angleType)
	{
		return angles.get(angleType);
	}

/*-------------------------------------------------------------------*/
/**	Set the agent's angles in each plane.
 *	@param yaw the new yaw angle
 *	@param pitch the new pitch angle
 *	@param roll the new roll angle */
/*-------------------------------------------------------------------*/
	protected void setAngle(float yaw, float pitch, float roll)
	{
		angles.set(yaw, pitch, roll);
	}

/*-------------------------------------------------------------------*/
/**	Set the agent's angles in the given plane, as defined by the constants
 *	in the Angles class.
 *	@param angleType the angle to change
 *	@param value the new angle to set
 *	@see soc.qase.state.Angles */
/*-------------------------------------------------------------------*/
	protected void setAngle(int angleType, float value)
	{
		angles.set(angleType, value);
	}

/*-------------------------------------------------------------------*/
/**	Get the current velocity in the given direction, as specified by
 *	the constants in the Velocity class.
 *	@param velocityType the direction (forward/right) whose magnitude
 *	is required
 *	@return the magnitude of the agent's velocity in the given direction
 *	@see soc.qase.state.Velocity */
/*-------------------------------------------------------------------*/
	public int getVelocity(int velocityType)
	{
		return velocity.get(velocityType);
	}

/*-------------------------------------------------------------------*/
/**	Get the current walk state of the bot. This will be one of WALK_STOPPED,
 *	WALK_NORMAL or WALK_RUN, as specified in the PlayerMove class.
 *	@return the agent's current walk state
 *	@see soc.qase.state.PlayerMove */
/*-------------------------------------------------------------------*/
	public int getWalkState()
	{
		return getPlayer().getWalkState();
	}

/*-------------------------------------------------------------------*/
/**	Set the agent's velocity in each direction.
 *	@param forward the new forward velocity
 *	@param right the new right velocity
 *	@param up the new vertical velocity */
/*-------------------------------------------------------------------*/
	protected void setVelocity(int forward, int right, int up)
	{
		velocity.set(forward, right, up);
	}

/*-------------------------------------------------------------------*/
/**	Set the agent's velocity in a given direction, as given by the
 *	constants in the Velocity class.
 *	@param velocityType the direction whose velocity is to be set
 *	@param value the magnitude of the new velocity
 *	@see soc.qase.state.Velocity */
/*-------------------------------------------------------------------*/
	protected void setVelocity(int velocityType, int value)
	{
		velocity.set(velocityType, value);
	}

/*-------------------------------------------------------------------*/
/**	Get the agent's current action settings, as specified by the constants
 *	in the Action class.
 *	@param actionType the action to return
 *	@return true if the given action is active, false otherwise
 *	@see soc.qase.state.Action */
/*-------------------------------------------------------------------*/
	public boolean getAction(int actionType)
	{
		return action.get(actionType);
	}

/*-------------------------------------------------------------------*/
/**	Set the agent's current actions.
 *	@param attack the action represents an attack
 *	@param use the action represents usage of current item
 *	@param any the action represents any button press
 *	@see soc.qase.state.Action */
/*-------------------------------------------------------------------*/
	protected void setAction(boolean attack, boolean use, boolean any)
	{
		action.set(attack, use, any);
	}

/*-------------------------------------------------------------------*/
/**	Set one of the agent's actions, as specified by the constants in
 *	the Action class.
 *	@param actionType the action to return
 *	@param value true if the given action should be active, false otherwise 
 *	@see soc.qase.state.Action */
/*-------------------------------------------------------------------*/
	protected void setAction(int actionType, boolean value)
	{
		action.set(actionType, value);
	}

/*-------------------------------------------------------------------*/
/**	Set the agent's posture to POSTURE_DUCKED, POSTURE_STAND or
 *	POSTURE_JUMP; these constants are found in the PlayerMove class.
 *	@param postureState specifies the bot's posture (crouch/stand/jump)
 *	@see soc.qase.state.PlayerMove */
/*-------------------------------------------------------------------*/
	protected void setPosture(int postureState)
	{
		velocity.setUp(postureState * 300);
	}

/*-------------------------------------------------------------------*/
/**	Specify whether the bot should jump. A value of 'true' causes the
 *	bot to jump, while 'false' returns it to a normal standing posture.
 *	@param jump indicates whether or not the agent should jump */
/*-------------------------------------------------------------------*/
	protected void setJump(boolean jump)
	{
		velocity.setUp(jump ? 300 : 0);
	}

/*-------------------------------------------------------------------*/
/**	Specify whether the bot should crouch. A value of 'true' causes the
 *	bot to crouch, while 'false' returns it to a normal standing posture.
 *	@param crouch indicates whether or not the agent should crouch */
/*-------------------------------------------------------------------*/
	protected void setCrouch(boolean crouch)
	{
		velocity.setUp(crouch ? -300 : 0);
	}

/*-------------------------------------------------------------------*/
/**	Reactivate the agent in the game world upon its death. */
/*-------------------------------------------------------------------*/
	protected void respawn()
	{
		angles.setYaw(0);
		angles.setRoll(0);
		angles.setPitch(0);

		velocity.setForward(0);
		velocity.setRight(0);

		action.setAttack(true);

		while(!isBotAlive())
		{
			proxy.sendMovement(angles, velocity, action);

			try
			{
				Thread.sleep(10);
			}
			catch(InterruptedException ie)
			{	}
		}

		pacify();
	}

/*-------------------------------------------------------------------*/
/**	Stop all agent activities. */
/*-------------------------------------------------------------------*/
	protected void pacify()
	{
		action.setAny(false);
		action.setUse(false);
		action.setAttack(false);

		velocity.setRight(0);
		velocity.setForward(0);

		angles.setYaw(0);
		angles.setRoll(0);
		angles.setPitch(0);

		proxy.sendMovement(angles, velocity, action);
	}

	private float[] botAngles = new float[2];
	private Vector2f perp = new Vector2f(0, 0);
	private Vector2f moveDir2f = new Vector2f(0, 0), aimDir2f = new Vector2f(0, 0);

/*-------------------------------------------------------------------*/
/**	Convenience method to facilitate the separation of movement and
 *	aiming, and allow both to be specified in global co-ordinates. Also
 *	allows the programmer to specify the bot's 'posture' - that is, whether
 *	it is standing, crouching or jumping. The postureState parameter should
 *	be one of the POSTURE_CROUCH, POSTURE_NORMAL or POSTURE_JUMP constants
 *	from the PlayerMove class.
 *	@param moveDir the direction in which to move
 *	@param aimDir the direction in which to aim
 *	@param vel the agent's total velocity
 *	@param postureState indicates the bot's posture (crouch/stand/jump)
 *	@see soc.qase.state.PlayerMove */
/*-------------------------------------------------------------------*/
	protected void setBotMovement(Vector3f moveDir, Vector3f aimDir, float vel, int postureState)
	{
		if(moveDir == null && aimDir == null)
			return;
		else if(moveDir == null)
			moveDir = new Vector3f(aimDir);
		else if(aimDir == null)
			aimDir = new Vector3f(moveDir);

		moveDir2f.set(moveDir.x, moveDir.y);
		aimDir2f.set(aimDir.x, aimDir.y);

		if(vel < 0)
			vel = (int)moveDir2f.length();

		moveDir2f.normalize();
		aimDir2f.normalize();

		moveDir2f.scale(vel);
		perp.set(aimDir2f.y, -aimDir2f.x);

		velocity.setForward((int)Math.round(aimDir2f.x * moveDir2f.x + aimDir2f.y * moveDir2f.y));
		velocity.setRight((int)Math.round(perp.x * moveDir2f.x + perp.y * moveDir2f.y));
		setPosture(postureState);

		botAngles = Utils.calcAngles(aimDir);

		angles.setYaw(botAngles[0]);
		angles.setPitch(botAngles[1]);
	}

/*-------------------------------------------------------------------*/
/**	Convenience method to facilitate the separation of movement and
 *	aiming, and allow both to be specified in global co-ordinates. Also
 *	allows the programmer to specify the bot's 'posture' - that is, whether
 *	it is standing, crouching or jumping. The postureState parameter should
 *	be one of the POSTURE_CROUCH, POSTURE_NORMAL or POSTURE_JUMP constants
 *	from the PlayerMove class.<br>
 *	<br>
 *	Instead of providing an explicit velocity as above, the programmer
 *	specifies the current walk state (WALK_STOPPED, WALK_NORMAL, WALK_RUN)
 *	using the constants defined in the PlayerMove class. The method computes
 *	the correct velocity based on whether the agent is on land or submerged,
 *	and then passes the call to setBotMovement(Vector3f, Vector3f, float).
 *	To account for the possibility of the programmer passing
 *	an explicit velocity as an int rather than float, the call will be
 *	passed directly to the above method if walkState exceeds the range
 *	of the constant values (i.e. 0, 1, 2).
 *	@param moveDir the direction in which to move
 *	@param aimDir the direction in which to aim
 *	@param walkState the discrete movement speed to use
 *	@param postureState indicates the bot's posture (crouch/stand/jump)
 *	@see soc.qase.state.PlayerMove */
/*-------------------------------------------------------------------*/
	protected void setBotMovement(Vector3f moveDir, Vector3f aimDir, int walkState, int postureState)
	{
		float vel = 0f; // assume stopped

		if(walkState == 0 || walkState > 2)
			vel = (float)walkState;
		else if(proxy.getWorld().getPlayer().isUnderWater()) // in water
			vel = (walkState == 1 ? 110f : 300f);
		else // moving, on land
			vel = (walkState == 1 ? 200f : 300f);

		setBotMovement(moveDir, aimDir, vel, postureState);
	}

/*-------------------------------------------------------------------*/
/**	Instruct the agent to use a global or local co-ordinate system. In
 *	Quake 2, the agent operates using a set of local axes that are rotated
 *	relative to the global co-ordinate system depending on the player's
 *	orientation when spawning. Generally, programmers are advised to work
 *	in global co-ordinates (the default setting), but may occasionally need
 *	or want to work using the local axes instead.
 *	@param use true if the agent should use global co-ordinates, false
 *	if it should use local */
/*-------------------------------------------------------------------*/
	protected void useGlobalAngles(boolean use)
	{
		globalAngles = use;
	}

/*-------------------------------------------------------------------*/
/**	Check whether the agent is using a global or local co-ordinate system.
 *	@return true if the agent is using global co-ordinates, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean getUseGlobalAngles()
	{
		return globalAngles;
	}

/*-------------------------------------------------------------------*/
/**	Activate the agent's hand grenades. */
/*-------------------------------------------------------------------*/
	protected void activateGrenades()
	{
		useItem(PlayerGun.GRENADES);
	}

/*-------------------------------------------------------------------*/
/**	Change the active weapon. For use with the constants stored in the
 *	PlayerGun class.
 *	@param gun the index of the gun to activate
 *	@see soc.qase.state.PlayerGun */
/*-------------------------------------------------------------------*/
	protected void changeWeapon(int gun)
	{
		changeWeaponByInventoryIndex(gun);
	}

/*-------------------------------------------------------------------*/
/**	Change the current weapon by specifying a number from 0-9.
 *	@param gun the keyboard index of the weapon to activate */
/*-------------------------------------------------------------------*/
	protected void changeWeaponByKeyboardIndex(int gun)
	{
		useItem((gun >= 6 ? gun + 7 : (gun > 0 ? gun + 6 : 17)));
	}

/*-------------------------------------------------------------------*/
/**	Change the current weapon by specifying an inventory index.
 *	Typically uses the constants from the Inventory class.
 *	@param gun the inventory index of the weapon to activate
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	protected void changeWeaponByInventoryIndex(int gun)
	{
		useItem(gun);
	}

/*-------------------------------------------------------------------*/
/**	Change the current weapon by specifying the index of the associated
 *	weapon model in the Config table.
 *	@param gun the Config index of the weapon to activate
 *	@see soc.qase.info.Config */
/*-------------------------------------------------------------------*/
	protected void changeWeaponByGunModelIndex(int gun)
	{
		useItem(PlayerGun.getGunInventoryIndex(gun));
	}

/*-------------------------------------------------------------------*/
/**	Use the specified item. Items are specified by inventory index,
 *	using the constants from the Inventory class.
 *	@param item the keyboard index of the weapon to activate
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	protected void useItem(int item)
	{
		proxy.useItem(item);
	}

/*-------------------------------------------------------------------*/
/**	Request a full copy of the agent's current inventory. Used when
 *	auto inventory refresh is enabled.*/
/*-------------------------------------------------------------------*/
	protected void refreshInventory()
	{
		proxy.refreshInventory();
	}

/*-------------------------------------------------------------------*/
/**	Send a non-blocking console message to connected host.
 *	@param command message to send */
/*-------------------------------------------------------------------*/
	protected void sendConsoleCommand(String command)
	{
		proxy.sendConsoleCommand(command);
	}

/*-------------------------------------------------------------------*/
/**	Find the nearest enemy in the game world.
 *	@return nearest enemy */
/*-------------------------------------------------------------------*/
	protected Entity getNearestEnemy()
	{
		if(proxy == null || (visWorld = proxy.getWorld()) == null)
			return null;

		opp = visWorld.getOpponents();
		tempOrigin = visWorld.getPlayer().getPlayerMove().getOrigin();
		pos.set(tempOrigin.getX(), tempOrigin.getY(), tempOrigin.getZ());

		Entity nearestEnemy = null;
		float distance = Float.MAX_VALUE;

		for(int i = 0; i < opp.size(); i++)
		{
			tempOrigin = ((Entity)opp.elementAt(i)).getOrigin();
			enemyPos.set(tempOrigin.getX(), tempOrigin.getY(), tempOrigin.getZ());
			enemyPos.sub(pos);

			if(enemyPos.length() < distance && enemyPos.length() > 0)
			{
				distance = enemyPos.length();
				nearestEnemy = (Entity)opp.elementAt(i);
			}
		}

		return nearestEnemy;
	}

/*-------------------------------------------------------------------*/
/**	Set the agent's WaypointMap to be the specified WaypointMap.
 *	@param wp the WaypointMap that the agent should use */
/*-------------------------------------------------------------------*/
	protected void setWaypointMap(WaypointMap wp)
	{
		wpMap = wp;
	}

/*-------------------------------------------------------------------*/
/**	Return the agent's WaypointMap, thereby allowing full access to its
 *	facilities.
 *	@return the agent's WaypointMap */
/*-------------------------------------------------------------------*/
	protected WaypointMap getWaypointMap()
	{
		return wpMap;
	}

/*-------------------------------------------------------------------*/
/**	Load a WaypointMap object from a file created using the saveMap method.
 *	The resulting WaypointMap is set as the agent's navigation map, to be
 *	used when any waypoint-related BasicBot methods are invoked. A reference
 *	to the object is also returned for convenience.
 *	@param filename the path and name of the saved waypoint file
 *	@return the WaypointMap which was loaded from file, or null if the loading
 *	process failed
 *	@see soc.qase.ai.waypoint.WaypointMap#saveMap(String) */
/*-------------------------------------------------------------------*/
	protected WaypointMap loadWaypointMap(String filename)
	{
		return (wpMap = WaypointMap.loadMap(filename));
	}

/*-------------------------------------------------------------------*/
/**	Generate a WaypointMap by analysing a recorded DM2 demo. The resulting
 *	WaypointMap is set as the agent's navigation map, to be used when
 *	any waypoint-related BasicBot methods are invoked. A reference to the
 *	object is also returned for convenience.
 *	@param dm2Filename filename of the DM2 demo to analyse
 *	@param numWaypoints the number of nodes to generate for the WaypointMap
 *	@param keepItemPositions if true, records the positions at which
 *	items were collected in the demo, and holds these locations constant
 *	through the clustering process
 *	@return the resulting WaypointMap
 *	@see soc.qase.ai.waypoint.WaypointMapGenerator#generate(String, int, boolean) */
/*-------------------------------------------------------------------*/
	protected WaypointMap generateWaypointMap(String dm2Filename, int numWaypoints, boolean keepItemPositions)
	{
		return (wpMap = WaypointMapGenerator.generate(dm2Filename, numWaypoints, keepItemPositions));
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified position at which an item
 *	of the the given type resides. The item type is specified by inventory
 *	index; see the Inventory class for a list of inventory constants
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param itemInventoryIndex the inventory index corresponding to the
 *	item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	protected Waypoint findClosestItem(Origin currentPos, int itemInventoryIndex)
	{
		if(wpMap == null)
			return null;

		return wpMap.findClosestItem(currentPos, itemInventoryIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified position at which an item
 *	of the the given type resides. The item type is specified by inventory
 *	index; see the Inventory class for a list of inventory constants
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param itemInventoryIndex the inventory index corresponding to the
 *	item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	protected Waypoint findClosestItem(Vector3f currentPos, int itemInventoryIndex)
	{
		if(wpMap == null)
			return null;

		return wpMap.findClosestItem(currentPos, itemInventoryIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified position at which an item
 *	of the the specified type resides. The category, type and subtype
 *	are typically passed using the constants found in the Entity class.
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param cat the category of item to search for
 *	@param type the type of item to search for
 *	@param subType the subtype of item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	protected Waypoint findClosestItem(Origin currentPos, String cat, String type, String subType)
	{
		if(wpMap == null)
			return null;

		return wpMap.findClosestItem(currentPos, cat, type, subType);
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified position at which an item
 *	of the the specified type resides. The category, type and subtype
 *	are typically passed using the constants found in the Entity class.
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param cat the category of item to search for
 *	@param type the type of item to search for
 *	@param subType the subtype of item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	protected Waypoint findClosestItem(Vector3f currentPos, String cat, String type, String subType)
	{
		if(wpMap == null)
			return null;

		return wpMap.findClosestItem(currentPos, cat, type, subType);
	}

/*-------------------------------------------------------------------*/
/**	Find the shortest path between the closest Waypoints to the specified
 *	locations. This uses the previously-generated cost and predecessor
 *	matrices.
 *	@param from the starting position, usually the agent's current location
 *	@param to the position to which we need a path
 *	@return a Waypoint array indicating the shortest path between the
 *	two Waypoints closest to the start and end positions */
/*-------------------------------------------------------------------*/
	protected Waypoint[] findShortestPath(Origin from, Origin to)
	{
		if(wpMap == null)
			return null;

		return wpMap.findShortestPath(from, to);
	}

/*-------------------------------------------------------------------*/
/**	Find the shortest path between the closest Waypoints to the specified
 *	locations. This uses the previously-generated cost and predecessor
 *	matrices.
 *	@param from the starting position, usually the agent's current location
 *	@param to the position to which we need a path
 *	@return a Waypoint array indicating the shortest path between the
 *	two Waypoints closest to the start and end positions */
/*-------------------------------------------------------------------*/
	protected Waypoint[] findShortestPath(Vector3f from, Vector3f to)
	{
		if(wpMap == null)
			return null;

		return wpMap.findShortestPath(from, to);
	}

/*-------------------------------------------------------------------*/
/**	Get the path through the waypoint graph from the current position to
 *	the closest Waypoint at which an item of the the given type resides.
 *	The item type is specified by inventory index; see the Inventory class
 *	for a list of inventory constants
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param itemInventoryIndex the inventory index corresponding to the
 *	item to search for
 *	@return a Waypoint array indicating the shortest path
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	protected Waypoint[] findShortestPathToItem(Origin currentPos, int itemInventoryIndex)
	{
		if(wpMap == null)
			return null;

		return wpMap.findShortestPathToItem(currentPos, itemInventoryIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the path through the waypoint graph from the current position to
 *	the closest Waypoint at which an item of the the given type resides.
 *	The item type is specified by inventory index; see the Inventory class
 *	for a list of inventory constants
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param itemInventoryIndex the inventory index corresponding to the
 *	item to search for
 *	@return a Waypoint array indicating the shortest path
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	protected Waypoint[] findShortestPathToItem(Vector3f currentPos, int itemInventoryIndex)
	{
		if(wpMap == null)
			return null;

		return wpMap.findShortestPathToItem(currentPos, itemInventoryIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the path through the waypoint graph from the current position to
 *	the closest Waypoint at which an item of the the given type resides.
 *	The category, type and subtype are typically passed using the
 *	constants found in the Entity class.
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param cat the category of item to search for
 *	@param type the type of item to search for
 *	@param subType the subtype of item to search for
 *	@return a Waypoint array indicating the shortest path
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	protected Waypoint[] findShortestPathToItem(Origin currentPos, String cat, String type, String subType)
	{
		if(wpMap == null)
			return null;

		return wpMap.findShortestPathToItem(currentPos, cat, type, subType);
	}

/*-------------------------------------------------------------------*/
/**	Get the path through the waypoint graph from the current position to
 *	the closest Waypoint at which an item of the the given type resides.
 *	The category, type and subtype are typically passed using the
 *	constants found in the Entity class.
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param cat the category of item to search for
 *	@param type the type of item to search for
 *	@param subType the subtype of item to search for
 *	@return a Waypoint array indicating the shortest path
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	protected Waypoint[] findShortestPathToItem(Vector3f currentPos, String cat, String type, String subType)
	{
		if(wpMap == null)
			return null;

		return wpMap.findShortestPathToItem(currentPos, cat, type, subType);
	}

/*-------------------------------------------------------------------*/
/**	Return the BSP parser object, thereby allowing full access to its
 *	facilities.
 *	@return the agent's BSP parser */
/*-------------------------------------------------------------------*/
	protected BSPParser getBSPParser()
	{
		if(isBotAlive() && (bsp.isMapLoaded() || readMap()))
			return bsp;
		else
			return null;
	}

/*-------------------------------------------------------------------*/
/**	Indicate whether visibility-checking functions should simply start
 *	from the actual location of the player entity, or from its point-of-view
 *	(i.e. the "camera" position). Defaults to FALSE.
 *	@param useVO if true, checks visibility of other points in the game
 *	environment from the bot's POV; if false, from the bot's location in
 *	the game world (i.e. the centre of the agent model) */
/*-------------------------------------------------------------------*/
	protected void useViewOffset(boolean useVO)
	{
		traceFromView = useVO;
	}

/*-------------------------------------------------------------------*/
/**	Check whether a particular entity is visible from the player's
 *	current position.
 *	@param e the entity whose visibility will be checked
 *	@return true if visible, false otherwise */
/*-------------------------------------------------------------------*/
	protected boolean isVisible(Entity e)
	{
		return (e != null && isVisible(e.getOrigin()));
	}

/*-------------------------------------------------------------------*/
/**	Check whether a particular point in the environment is visible from
 *	the player's current position.
 *	@param o the point whose visibility will be checked
 *	@return true if visible, false otherwise */
/*-------------------------------------------------------------------*/
	protected boolean isVisible(Origin o)
	{
		return (o != null && isVisible(new Vector3f(o)));
	}

/*-------------------------------------------------------------------*/
/**	Check whether a particular point in the environment is visible from
 *	the player's current position.
 *	@param v the point whose visibility will be checked
 *	@return true if visible, false otherwise */
/*-------------------------------------------------------------------*/
	protected boolean isVisible(Vector3f v)
	{
		if(v == null || proxy == null || (visWorld = proxy.getWorld()) == null) return false;

		pos.set(visWorld.getPlayer().getPlayerMove().getOrigin());
		if(traceFromView) pos.add(visWorld.getPlayer().getPlayerView().getViewOffset());

		return bsp.isVisible(pos, v);
	}

/*-------------------------------------------------------------------*/
/**	Check whether the nearest enemy in the game is currently visible.
 *	@return true if visible, false otherwise */
/*-------------------------------------------------------------------*/
	protected boolean isNearestEnemyVisible()
	{
		return isNearestEnemyVisible(false);
	}

	private Entity nearEnemy = null;
	private Origin tempOrigin = null;
	private Vector3f dir = new Vector3f(0, 0, 0);

/*-------------------------------------------------------------------*/
/**	Check whether the nearest enemy in the game is currently visible.
 *	@param withinFOV if true, constrain visibility check to agent's
 *	line-of-sight
 *	@return true if visible, false otherwise */
/*-------------------------------------------------------------------*/
	protected boolean isNearestEnemyVisible(boolean withinFOV)
	{
		nearEnemy = getNearestEnemy();

		if(nearEnemy != null)
		{
			enemyPos.set(nearEnemy.getOrigin());
			dir.sub(enemyPos, pos);

			if(traceFromView) pos.add(visWorld.getPlayer().getPlayerView().getViewOffset());
			return (withinFOV ? Utils.calcAngles(dir)[0] <= visWorld.getPlayer().getPlayerView().getFOV() / 2.0 && bsp.isVisible(pos, enemyPos) : bsp.isVisible(pos, enemyPos));
		}
		else
			return false;
	}

	private Vector opp = null;
	private World visWorld = null;
	private Vector3f pos = new Vector3f(0, 0, 0);
	private Vector3f enemyPos = new Vector3f(0, 0, 0);

/*-------------------------------------------------------------------*/
/**	Projects a bounding-box through the game world in a given direction
 *	from the agent's current position, and returns the location of the
 *	first collision with solid geometry. Finds and loads the current game
 *	map transparently when called, using BSPParser.
 *	@param dir direction to sweep bounding box
 *	@param maxDist maximum distance across which to sweep
 *	@return the location of the first collision
 *	@see soc.qase.file.bsp.BSPParser */
/*-------------------------------------------------------------------*/
	protected Vector3f getObstacleLocation(Vector3f dir, float maxDist)
	{
		return getObstacleLocation(dir, BSPParser.TRACE_BOX, BSPBrush.CONTENTS_SOLID, maxDist);
	}

/*-------------------------------------------------------------------*/
/**	Get the radius of the sphere used to trace through the level. By
 *	default this is 18.0 units, or the same width as the player's
 *	bounding box.
 *	@return the sphere radius */
/*-------------------------------------------------------------------*/
	protected float getTraceSphereRadius()
	{
		return sphereRadius;
	}

/*-------------------------------------------------------------------*/
/**	Set the radius of the sphere used to trace through the level. This
 *	is permitted to be variable to account for different projectile sizes.
 *	@param radius the new sphere radius */
/*-------------------------------------------------------------------*/
	protected void setTraceSphereRadius(float radius)
	{
		sphereRadius = radius;
	}

/*-------------------------------------------------------------------*/
/**	Projects a bounding-box, sphere or line through the game world in
 *	a given direction from the agent's current position, and returns
 *	the location of the first collision with geometry matching the given
 *	type (specified using the constants in BSPBrush). Finds and loads
 *	the current game map transparently when called, using BSPParser.
 *	@param dir direction to sweep bounding box
 *	@param traceType the type of trace, specified using the constants
 *	found in BSPParser
 *	@param brushType the type of brush to check against, specified by
 *	the constants found in BSPBrush. Allows the agent to check for
 *	different types of terrain
 *	@param maxDist maximum distance across which to sweep
 *	@return the location of the first collision
 *	@see soc.qase.file.bsp.BSPParser
 *	@see soc.qase.file.bsp.BSPBrush*/
/*-------------------------------------------------------------------*/
	protected Vector3f getObstacleLocation(Vector3f dir, int traceType, int brushType, float maxDist)
	{
		if(!isBotAlive() || (!bsp.isMapLoaded() && !readMap()))
			return null;

		pos.set(proxy.getWorld().getPlayer().getPlayerMove().getOrigin());
		if(traceFromView) pos.add(visWorld.getPlayer().getPlayerView().getViewOffset());

		bsp.setBrushType(brushType);

		if(traceType == BSPParser.TRACE_LINE)
			return bsp.getObstacleLocation(pos, dir, maxDist);
		else if(traceType == BSPParser.TRACE_SPHERE)
			return bsp.getObstacleLocation(pos, dir, sphereRadius, maxDist);
		else if(traceType == BSPParser.TRACE_BOX)
			return bsp.getObstacleLocation(pos, dir, BOUNDING_MIN, BOUNDING_MAX, maxDist);
		else
			return null;
	}

/*-------------------------------------------------------------------*/
/**	Projects a bounding-box through the game world in a given direction
 *	from the agent's current position, and returns the distance to the
 *	nearest solid geometry. Finds and loads the current game map
 *	transparently when called, using BSPParser.
 *	@param dir direction to sweep bounding box
 *	@param maxDist maximum distance across which to sweep
 *	@return the location of the first collision
 *	@see soc.qase.file.bsp.BSPParser */
/*-------------------------------------------------------------------*/
	protected float getObstacleDistance(Vector3f dir, float maxDist)
	{
		return getObstacleDistance(dir, BSPParser.TRACE_BOX, BSPBrush.CONTENTS_SOLID, maxDist);
	}

/*-------------------------------------------------------------------*/
/**	Projects a bounding-box, sphere or line through the game world in
 *	a given direction from the agent's current position, and returns
 *	the distance to the nearest geometry matching the given type
 *	(specified using the constants in BSPBrush). Finds and loads the
 *	current game map transparently when called, using BSPParser.
 *	@param dir direction to sweep bounding box
 *	@param traceType the type of trace, specified using the constants
 *	found in BSPParser
 *	@param brushType the type of brush to check against, specified by
 *	the constants found in BSPBrush. Allows the agent to check for
 *	different types of terrain
 *	@param maxDist maximum distance across which to sweep
 *	@return the location of the first collision
 *	@see soc.qase.file.bsp.BSPParser
 *	@see soc.qase.file.bsp.BSPBrush*/
/*-------------------------------------------------------------------*/
	protected float getObstacleDistance(Vector3f dir, int traceType, int brushType, float maxDist)
	{
		if(!isBotAlive() || (!bsp.isMapLoaded() && !readMap()))
			return Float.NaN;

		pos.set(proxy.getWorld().getPlayer().getPlayerMove().getOrigin());
		if(traceFromView) pos.add(visWorld.getPlayer().getPlayerView().getViewOffset());

		bsp.setBrushType(brushType);

		if(traceType == BSPParser.TRACE_LINE)
			return bsp.getObstacleDistance(pos, dir, maxDist);
		else if(traceType == BSPParser.TRACE_SPHERE)
			return bsp.getObstacleDistance(pos, dir, sphereRadius, maxDist);
		else if(traceType == BSPParser.TRACE_BOX)
			return bsp.getObstacleDistance(pos, dir, BOUNDING_MIN, BOUNDING_MAX, maxDist);
		else
			return Float.NaN;
	}

/*-------------------------------------------------------------------*/
/**	Set the Quake 2 home directory. Used when locating the local BSP files
 *	containing the game geometry. Two alternatives to calling this method
 *	exist; the user can pass the folder location to the JVM as a variable
 *	called QUAKE2 using the -D switch (eg 'java -DQUAKE2="C:/quake2"'), or
 *	- the preferred approach - an environment variable called 'QUAKE2' can
 *	be declared which points to the home folder.
 *	@param q2hd the location of the Quake 2 home folder */
/*-------------------------------------------------------------------*/
	public static void setQuake2HomeDirectory(String q2hd)
	{
		q2HomeDir = new String(q2hd);
	}

/*-------------------------------------------------------------------*/
/**	Return the Quake 2 home folder location.
 *	@return the location of the Quake 2 home folder */
/*-------------------------------------------------------------------*/
	public static String getQuake2HomeDirectory()
	{
		return new String(q2HomeDir);
	}

/*-------------------------------------------------------------------*/
/**	Read a local geometry file into memory. The path to the BSP file can
 *	begin with the alias 'Q2HOME' (eg 'Q2HOME/maps/bsp1.bsp') which will
 *	automatically be resolved to the actual Quake 2 home folder using
 *	findQuake2HomeDirectory. If the correct BSP file is know before the
 *	agent connects to the server, this method can be used to pre-load it.
 *	@param filename the location of the BSP file
 *	@return true if the file was successfully read, false otherwise
 *	@see #setQuake2HomeDirectory
 *	@see #findQuake2HomeDirectory */
/*-------------------------------------------------------------------*/
	protected boolean readMap(String filename)
	{
		if(filename.substring(0, 6).equalsIgnoreCase("Q2HOME"))
		{
			if(q2HomeDir == null)
				findQuake2HomeDirectory();

			filename = q2HomeDir + filename.substring(6);
		}

		return bsp.load(filename);
	}

/*-------------------------------------------------------------------*/
/**	Read the current game map into memory. This will automatically
 *	deduce the name of the map, and will then search all possible
 *	locations in decreasing order of likelihood, including within PAK
 *	archives. Uses PAKParser and BSPParser.
 *	@return true if the map was successfully found and loaded, false
 *	otherwise
 *	@see soc.qase.file.pak.BSPParser
 *	@see soc.qase.file.pak.PAKParser */
/*-------------------------------------------------------------------*/
	private boolean readMap()
	{
		if(!isBotAlive() || mapNotFound)
			return false;

		String pathAndFileName = null;
		String gameDir = proxy.getServer().getGameDirectory();
		String mapName = proxy.getServer().getMapName();

		if(gameDir == null || gameDir.length() == 0)
			gameDir = "baseq2";

		if(q2HomeDir == null || q2HomeDir.length() == 0)
			findQuake2HomeDirectory();

		// try to load BSP assuming filename == mapName
		if(!bsp.load(q2HomeDir + "/" + gameDir + "/maps/" + mapName + ".bsp"))
		{
			String pakBSPFilename = null;
			String pakDir = q2HomeDir + "/" + gameDir + "/";

			// search PAKs assuming filename == mapName
			for(int i = 0; i < 10; i++)
			{
				pakBSPFilename = PAKParser.findFileFromPAK(pakDir + "pak" + i + ".pak", mapName + ".bsp");

				if(pakBSPFilename != null)
				{
					bsp.load(pakDir + "pak" + i + ".pak#" + pakBSPFilename);
					break;
				}
			}
		}

		// search in BSP files for map name
		if(!bsp.isMapLoaded())
		{
			File bspDir = new File(q2HomeDir + "/" + gameDir + "/maps");
			String[] fileList = bspDir.list();

			for(int i = 0; i < fileList.length; i++)
			{
				if(fileList[i].toLowerCase().indexOf(".bsp") != -1 && BSPParser.isMapNameInFile(q2HomeDir + "/" + gameDir + "/maps/" + fileList[i], mapName))
				{
					bsp.load(q2HomeDir + "/" + gameDir + "/maps/" + fileList[i]);
					break;
				}
			}
		}

		// search in PAK files for map name
		if(!bsp.isMapLoaded())
		{
			String foundFile = null;
			String pakDir = q2HomeDir + "/" + gameDir + "/";

			for(int i = 0; i < 10; i++)
			{
				foundFile = PAKParser.findBSPFileFromPAK(pakDir + "pak" + i + ".pak", mapName);

				if(foundFile != null)
				{
					bsp.load(pakDir + "pak" + i + ".pak#" + foundFile);
					break;
				}
			}
		}

		mapNotFound = !bsp.isMapLoaded();
		return bsp.isMapLoaded();
	}

	private String findQuake2HomeDirectory()
	{
		q2HomeDir = System.getProperty("QUAKE2");

/*		// System.getenv not valid on most J2SE platforms
		if(q2Dir == null || q2Dir.length() == 0)
		{
			try
			{
				q2Dir = System.getenv("QUAKE2");
			}
			catch(Exception e)
			{	}
		}
*/

		if(q2HomeDir == null || q2HomeDir.length() == 0)
			q2HomeDir = Utils.parseEnvironmentVariables("QUAKE2");

		if(q2HomeDir == null || q2HomeDir.length() == 0)
			q2HomeDir = "c:/quake2";

		return new String(q2HomeDir);
	}

/*-------------------------------------------------------------------*/
/**	Check if the proxy is recording the current game.
 *	@return true if the proxy is recording the current game,
 *	otherwise false. */
/*-------------------------------------------------------------------*/
	public boolean isRecording()
	{
		return proxy.isRecording();
	}

	protected void sendMovement()
	{
		if(globalAngles)
		{
			delta_Angles = proxy.getWorld().getPlayer().getPlayerMove().getDeltaAngles();
			angles.setYaw(angles.getYaw() - delta_Angles.getYaw());
			angles.setPitch(angles.getPitch() - delta_Angles.getPitch());
			angles.setRoll(-delta_Angles.getRoll());
		}

		proxy.sendMovement(angles, velocity, action);
	}
}
