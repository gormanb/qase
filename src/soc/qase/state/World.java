//---------------------------------------------------------------------
// Name:			World.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.state;

import java.util.*;
import soc.qase.info.*;
import soc.qase.com.message.*;
import soc.qase.tools.vecmath.Vector3f;

/*-------------------------------------------------------------------*/
/**	The World class is used as a wrapper class for the complete
 *	environment state of a simulation. Each frame of execution, instances
 *	of this type can be extracted from the Proxy object, while the agent
 *	is part of an ongoing simulation. The World class is responsible for
 *	maintaining the consistency of the gamestate, for correctly tracking
 *	past frames, and for merging updates into the existing gamestate
 *	representation according to Quake 2's cumulative update protocols. */
/*-------------------------------------------------------------------*/
public class World
{
	private int currentFrame = 0;
	private int previousFrame = 0;
	private int currentDeltaFrame = 0;

	private int mergeState = 16;
	private int currentState = 16;

	private Layout layout = null;
	private Config config = null;

	private Player players[] = null;
	private Entity entities[][] = null;

	private Vector messages = null;
	private Vector tempEntities = new Vector();

	private Inventory inventory = null;
	private boolean inventoryUpdated = false;

	private int pickupEntityNum = -1;
	private int playerEntityNum = -1;
	private boolean trackInventory = false;

	private int[] respawnTimes = null;
	private boolean[] respawnedEntities = null;
	private boolean[] deactivatedEntities = null;

/*-------------------------------------------------------------------*/
/**	Default constructor. Sets up the required structures with default
 *	attributes. */
/*-------------------------------------------------------------------*/
	public World()
	{
		commonSetup();
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Allows the programmer to specify whether QASE should
 *	manually track the inventory as the agent collects items in the game. */
/*-------------------------------------------------------------------*/
	public World(boolean trackInv)
	{
		commonSetup();
		trackInventory = trackInv;
	}

	private void commonSetup()
	{
		config = new Config();
		entities = new Entity[17][1024];

		respawnTimes = new int[1024];
		respawnedEntities = new boolean[1024];
		deactivatedEntities = new boolean[1024];

		players = new Player[17];
		inventory = new Inventory(config);
	}

/*-------------------------------------------------------------------*/
/**	Accept and store the data received from the server at the beginning
 *	of each game session, and when a new map is entered.
 *	@param server a Server object containing the server information */
/*-------------------------------------------------------------------*/
	public void setServerData(Server server)
	{
		playerEntityNum = server.getClientEntity();
	}

/*-------------------------------------------------------------------*/
/**	Add message to the internal mailbox.
 *	@param message message to add. */
/*-------------------------------------------------------------------*/
	public synchronized void setMessage(String message)
	{
		if(messages == null) messages = new Vector();
		messages.addElement(message);
	}

/*-------------------------------------------------------------------*/
/**	Get messages currently in the internal mailbox. After a call
 *	to this method the internal mailbox will be reset.
 *	@return messages currently in the internal mailbox. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getMessages()
	{
		Vector result = messages;
		messages = null;
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Add a temporary entity. */
/*-------------------------------------------------------------------*/
	public synchronized void setTemporaryEntity(TemporaryEntity tempEnt)
	{
		tempEntities.addElement(tempEnt);
	}

/*-------------------------------------------------------------------*/
/**	Get the list of temporary entities for the current frame. Temporary
 *	entities are spawned instantaneously at a single frame; the list is
 *	cleared on each new update.
 *	@return the current list of temporary entities */
/*-------------------------------------------------------------------*/
	public synchronized Vector getTemporaryEntities()
	{
		return new Vector(tempEntities);
	}

/*-------------------------------------------------------------------*/
/**	Set current frame information.
 *	@param frame current frame information. */
/*-------------------------------------------------------------------*/
	public synchronized void setFrame(ServerFrame frame)
	{
		previousFrame = currentFrame;
		currentFrame = frame.getFrame();
		currentDeltaFrame = frame.getDeltaFrame();

		if((currentFrame - previousFrame) > 12)
			currentState = 0;
		else
			currentState = (currentState + currentFrame - previousFrame) % 16;

		if(currentDeltaFrame == -1)
			mergeState = 16;
		else if((currentFrame - previousFrame) <= 12)
			mergeState = (currentState + currentDeltaFrame - currentFrame + 16) % 16;

		for(int i = 0; i < 1024; i++)
		{
			if(entities[mergeState][i] != null)
				entities[currentState][i] = entities[mergeState][i];
		}

		pickupEntityNum = -1;
		tempEntities.clear();
		Arrays.fill(respawnedEntities, false);
		Arrays.fill(deactivatedEntities, false);
	}

/*-------------------------------------------------------------------*/
/**	Get current frame number.
 *	@return current frame number. */
/*-------------------------------------------------------------------*/
	public synchronized int getFrame()
	{
		return currentFrame;
	}

/*-------------------------------------------------------------------*/
/**	Merge new player information into the current Player state.
 *	@param player player information. */
/*-------------------------------------------------------------------*/
	public synchronized void setPlayer(Player player)
	{
		players[currentState] = player;
		players[currentState].getPlayerStatus().setConfig(config);
		players[currentState].merge(players[mergeState]);
//		players[mergeState] = players[currentState];

		for(int i = 0; i < players.length; i++)
		{
			if(players[i] != null)
				players[i].setPlayerStatus(players[currentState].getPlayerStatus());
		}

		if(trackInventory && isPlayerActive())
			updateInventoryAmmo();
		else if(trackInventory && !players[currentState].isAlive())
			inventory.resetCount();
	}

/*-------------------------------------------------------------------*/
/*	Get player information.
 *	@return current player information. */
/*-------------------------------------------------------------------*/
	public synchronized Player getPlayer()
	{
		return players[currentState];
	}

	public synchronized boolean isPlayerActive()
	{
		return getPlayer() != null && getPlayer().isAlive();
	}

/*-------------------------------------------------------------------*/
/**	Set entity information.
 *	@param entities Vector of entities to add
/*-------------------------------------------------------------------*/
	public synchronized void setEntities(Vector entities)
	{
		for(int i = 0; i < entities.size(); i++)
			setEntity((Entity)entities.elementAt(i), false);

		for(int i = 0; i < respawnTimes.length; i++)
		{
			if(respawnTimes[i] > 0 && respawnTimes[i] != Integer.MAX_VALUE)
			{
				respawnTimes[i]--;
				respawnedEntities[i] = (respawnTimes[i] == 0);
			}
		}
	}

/*-------------------------------------------------------------------*/
/**	Set entity information, merging updated data into the current
 *	gamestate representation.
 *	@param entity entity data to merge
 *	@param baseline true if the entity is a baseline ('master' entity
 *	into which subsequent updates are merged), false otherwise */
/*-------------------------------------------------------------------*/
	public synchronized void setEntity(Entity entity, boolean baseline)
	{
		if(baseline)
			entities[mergeState][entity.getNumber()] = entity;
		else
		{
			respawnedEntities[entity.getNumber()] = entity.isRespawned();
			deactivatedEntities[entity.getNumber()] = (entities[mergeState][entity.getNumber()] != null && entities[mergeState][entity.getNumber()].getActive() && !entity.getActive());

			if(entity.isRespawned())
				respawnTimes[entity.getNumber()] = 0;

			entity.setConfig(config);
			entities[currentState][entity.getNumber()] = entity;
			entities[currentState][entity.getNumber()].merge(entities[mergeState][entity.getNumber()]);
		}
	}

/*-------------------------------------------------------------------*/
/**	Check whether the current entity has been collected by the agent,
 *	if it is an item.
 *	@param entityNum the entity number
 *	@return true if the item has been collected, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isCollected(int entityNum)
	{
		return respawnTimes[entityNum] != 0;
	}

/*-------------------------------------------------------------------*/
/**	Get a list of the entities which the agent has collected
 *	@return an integer array containing the entity numbers of the
 *	items which the agent has collected */
/*-------------------------------------------------------------------*/
	public int[] getCollectedEntityNumbers()
	{
		int count = 0;

		for(int i = 0; i < respawnTimes.length; i++)
		{
			if(respawnTimes[i] > 0)
				count++;
		}

		int[] entNums = new int[count];

		count = 0;

		for(int i = 0; i < respawnTimes.length; i++)
		{
			if(respawnTimes[i] > 0)
				entNums[count++] = i;
		}

		return entNums;
	}

/*-------------------------------------------------------------------*/
/**	Get the entities which the agent has collected.
 *	@return an Entity array containing the items which the agent
 *	has collected */
/*-------------------------------------------------------------------*/
	public Entity[] getCollectedEntities()
	{
		return getEntities(getCollectedEntityNumbers());
	}

/*-------------------------------------------------------------------*/
/**	Get the entity numbers of the items which have respawned on this frame
 *	@return an integer array containing the entity numbers of the
 *	respawned items */
/*-------------------------------------------------------------------*/
	public int[] getRespawnedEntityNumbers()
	{
		int count = 0;

		for(int i = 0; i < respawnedEntities.length; i++)
		{
			if(respawnedEntities[i])
				count++;
		}

		int[] entNums = new int[count];

		count = 0;

		for(int i = 0; i < respawnedEntities.length; i++)
		{
			if(respawnedEntities[i])
				entNums[count++] = i;
		}

		return entNums;
	}

/*-------------------------------------------------------------------*/
/**	Get the item entities which have respawned on this frame
 *	@return an Entity array containing the respawned item entities */
/*-------------------------------------------------------------------*/
	public Entity[] getRespawnedEntities()
	{
		return getEntities(getRespawnedEntityNumbers());
	}

/*-------------------------------------------------------------------*/
/**	Get the amount of time remaining, in frames (or tenths of seconds),
 *	before the given entity respawns
 *	@param entityNum the entity number
 *	@return the number of frames remaining before the item respawns */
/*-------------------------------------------------------------------*/
	public int getRespawnTimeRemaining(int entityNum)
	{
		return respawnTimes[entityNum];
	}

/*-------------------------------------------------------------------*/
/**	Get the list of entity numbers of entities which were marked as
 *	inactive on this frame, due either to being collected or moving
 *	outside the maximum range at which the server notifies the agent
 *	of the entity's status.
 *	@return an array of entity numbers indicating the deactivated entities */
/*-------------------------------------------------------------------*/
	protected int[] getDeactivatedEntityNumbers()
	{
		int count = 0;

		for(int i = 0; i < deactivatedEntities.length; i++)
		{
			if(deactivatedEntities[i])
				count++;
		}

		int[] entNums = new int[count];

		count = 0;

		for(int i = 0; i < deactivatedEntities.length; i++)
		{
			if(deactivatedEntities[i])
				entNums[count++] = i;
		}

		return entNums;
	}

/*-------------------------------------------------------------------*/
/**	Get the collection of entities which deactivated on this frame,
 *	due either to being collected or moving outside the maximum range
 *	at which the server notifies the agent of the entity's status
 *	@return an array of deactivated entities */
/*-------------------------------------------------------------------*/
	protected Entity[] getDeactivatedEntities()
	{
		return getEntities(getDeactivatedEntityNumbers());
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private synchronized void updateInventoryAmmo()
	{
		PlayerGun gun = players[currentState].getPlayerGun();
		PlayerStatus status = players[currentState].getPlayerStatus();
		int ammoIndex = gun.getAmmoInventoryIndexByGun(gun.getGunInventoryIndex(gun.getIndex()));

		if(ammoIndex != -1)
			inventory.setCount(ammoIndex, status.getStatus(PlayerStatus.AMMO));
	}

/*-------------------------------------------------------------------*/
/**	Process the sounds received by the agent. Since Quake 2 does not
 *	send explicit notification when an item is picked up, this is used
 *	to determine whether or not the agent has just collected an item,
 *	and if so to deduce the entity type, number and respawn interval.
 *	Similarly, this method is used to determine whether a particular
 *	player has died during the current game frame.
 *	@param sound the Sound message received from the server */
/*-------------------------------------------------------------------*/
	public void processSound(Sound sound)
	{
		int pkup = players[currentState].getPlayerStatus().getStatus(PlayerStatus.PICKUP_STRING) - 1056;

		if(sound.getEntityNumber() == playerEntityNum + 1 && pkup > 0 && config.getConfigString(sound.getConfigIndex()).indexOf("pkup") >= 0)
		{
			if(trackInventory && isPlayerActive())
			{	PlayerGun gun = players[currentState].getPlayerGun();
	
				if(pkup >= 8 && pkup <= 17)
				{
					if(pkup != 12)
						inventory.setCount(pkup, inventory.getCount(pkup) + 1);
	
					inventory.setCount(gun.getAmmoInventoryIndexByGun(pkup), Math.min(gun.getMaxAmmoByGun(pkup), inventory.getCount(gun.getAmmoInventoryIndexByGun(pkup)) + gun.getAmmoPerPickupByGun(pkup)));
				}
				else if(pkup >= 18 && pkup <= 22)
					inventory.setCount(pkup, Math.min(gun.getMaxAmmo(pkup), inventory.getCount(pkup) + gun.getAmmoPerPickup(pkup)));
				else
					inventory.setCount(pkup, inventory.getCount(pkup) + 1);

				updateInventoryAmmo();
				inventoryUpdated = true;
			}

			Entity[] deactEnts = getDeactivatedEntities();

			if(deactEnts != null)
			{
				Vector3f entOrigin = new Vector3f();
				Vector3f playerOrigin = new Vector3f(getPlayer().getPlayerMove().getOrigin());

				int index = -1;
				float curDist = 0;
				float minDist = Float.MAX_VALUE;

				for(int i = 0; i < deactEnts.length; i++)
				{
					deactEnts[i].setConfig(config);
					entOrigin.set(deactEnts[i].getOrigin());

					if(deactEnts[i].getNumber() != playerEntityNum + 1 && deactEnts[i].getNumber() > 0 && (curDist = playerOrigin.distance(entOrigin)) < minDist && deactEnts[i].getInventoryIndex() == pkup)
					{
						minDist = curDist;
						index = deactEnts[i].getNumber();
					}
				}

				pickupEntityNum = index;

				if(pickupEntityNum > 0)
					respawnTimes[index] = getEntity(index).getRespawnTime();
			}
		}
		else if(config.getConfigString(sound.getConfigIndex()).indexOf("death") >= 0)
			getEntity(sound.getEntityNumber()).playerDied = true;
	}

/*-------------------------------------------------------------------*/
/**	Get the entity number of the item picked up on the current frame.
 *	@return the entity number of the item picked up, or -1 if none */
/*-------------------------------------------------------------------*/
	public int getPickupEntityIndex()
	{
		return pickupEntityNum;
	}

/*-------------------------------------------------------------------*/
/**	Get the item entity picked up on the current frame.
 *	@return the item entity picked up, or null if none */
/*-------------------------------------------------------------------*/
	public Entity getPickupEntity()
	{
		return getEntity(pickupEntityNum);
	}

/*-------------------------------------------------------------------*/
/**	Record the effect of using an item on the inventory (ie decrement
 *	associated item by 1, if it isn't a weapon).
 *	@param itemIndex the number of the item used */
/*-------------------------------------------------------------------*/
	public void processUsedItem(int itemIndex)
	{
		if(trackInventory && itemIndex > 22 && itemIndex != 30 && itemIndex != 31 && isPlayerActive())
			inventory.setCount(itemIndex, Math.max(0, inventory.getCount(itemIndex) - 1));
	}

/*-------------------------------------------------------------------*/
/**	Set the player's current inventory values. */
/*-------------------------------------------------------------------*/
	public synchronized void setInventory(Inventory invent)
	{
		inventoryUpdated = Arrays.equals(inventory.getInventoryArrayReference(), invent.getInventoryArrayReference());
		inventory = invent;
	}

/*-------------------------------------------------------------------*/
/**	Check whether the inventory has changed (i.e. items picked up or
 *	weapons discharged, etc) since the last time the getInventory
 *	method was called.
 *	@return true if the inventory has changed, false utherwise */
/*-------------------------------------------------------------------*/
	public boolean isInventoryUpdated() // since last getInventory
	{
		return inventoryUpdated;
	}

/*-------------------------------------------------------------------*/
/**	Get the player's current inventory values.
 *	@return Inventory object containing number of each item held */
/*-------------------------------------------------------------------*/
	public synchronized Inventory getInventory()
	{
		inventoryUpdated = false;
		return inventory;
	}

/*-------------------------------------------------------------------*/
/**	Get entity collection information.
 *	@param entityNum index number of entity to return */
/*-------------------------------------------------------------------*/
	public Entity getEntity(int entityNum)
	{
		if(entityNum < 0)
			return null;
		else if(entities[currentState][entityNum] != null)
			entities[currentState][entityNum].setConfig(config);

		return entities[currentState][entityNum];
	}

/*-------------------------------------------------------------------*/
/**	Get a selection of entities based on entity number.
 *	@param entityNums indices of entities to return
 *	@return list of entities as defined by the entity number list */
/*-------------------------------------------------------------------*/
	public Entity[] getEntities(int[] entityNums)
	{
		Entity[] ents = new Entity[entityNums.length];

		for(int i = 0; i < entityNums.length; i++)
		{
			ents[i] = (entityNums[i] >= 0 ? entities[currentState][entityNums[i]] : null);
			
			if(ents[i] != null)
				ents[i].setConfig(config);
		}

		return ents;
	}

/*-------------------------------------------------------------------*/
/**	Get entity collection information based on specified category, type and subtype.
 *	Designed to be used with the Entity.CONSTANT values
 *	@return entity collection information matching the given criteria. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getEntities(String cat, String type, String subType, boolean onlyActive)
	{
		Vector result = null;
		Entity currentEntity = null;

		result = new Vector();

		for(int i = 0; i < 1024; i++)
		{
			currentEntity = entities[currentState][i];

			if(currentEntity != null)
			{
				currentEntity.setConfig(config);

				if((cat == null || currentEntity.getCategory().equalsIgnoreCase(cat))
				&& (type == null || currentEntity.getType().equalsIgnoreCase(type))
				&& (subType == null || currentEntity.getSubType().equalsIgnoreCase(subType))
				&& (currentEntity.getActive() || !onlyActive))
				{
					result.addElement(currentEntity);
				}
			}
		}

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Get entity information.
 *	@param onlyActive if true, returns only those entities which are
 *	currently marked as active; if false, returns all entities
 *	regardless of whether they are marked active or inactive.
 *	@return entity list. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getEntities(boolean onlyActive)
	{
		return getEntities(null, null, null, onlyActive);
	}

/*-------------------------------------------------------------------*/
/**	Get entity information. Returns only entities which are currently active.
 *	@return entity list. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getEntities()
	{
		return getEntities(true);
	}

/*-------------------------------------------------------------------*/
/**	Get entity information on opposing players.
 *	@param onlyActive specifies whether inactive entities
 *	should be returned
 *	@return entity collection containing only opposing player
 *	entities. The entity corresponding to the local player is
 *	automatically removed. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getOpponents(boolean onlyActive)
	{
		Vector playerEnts = getEntities(Entity.CAT_PLAYERS, null, null, onlyActive);

		for(int i = 0; i < playerEnts.size(); i++)
		{
			if(((Entity)playerEnts.elementAt(i)).getNumber() == playerEntityNum + 1)
			{
				playerEnts.removeElementAt(i);
				break;
			}
		}

		return playerEnts;
	}

/*-------------------------------------------------------------------*/
/**	Get entity information on opposing players. Returns only entities
 *	which are currently active.
 *	@return entity collection containing only opposing player
 *	entities. The entity corresponding to the local player is
 *	automatically removed. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getOpponents()
	{
		return getOpponents(true);
	}

/*-------------------------------------------------------------------*/
/**	Get entity information on a particular opposing player by supplying
 *	that opponent's name.
 *	@return the Entity corresponding to the specified opponent. */
/*-------------------------------------------------------------------*/
	public synchronized Entity getOpponentByName(String oppName)
	{
		if(oppName == null)
			return null;

		Vector opps = getOpponents(false);

		for(int i = 0; i < opps.size(); i++)
		{
			if(((Entity)opps.elementAt(i)).getName().equalsIgnoreCase(oppName))
				return (Entity)opps.elementAt(i);
		}

		return null;
	}

/*-------------------------------------------------------------------*/
/**	Get item information.
 *	@return entity collection containing only item entities. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getItems(boolean onlyActive)
	{
		return getEntities(Entity.CAT_ITEMS, null, null, onlyActive);
	}

/*-------------------------------------------------------------------*/
/**	Get item information. Returns only entities which are currently active.
 *	@return entity collection containing only item entities. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getItems()
	{
		return getItems(true);
	}

/*-------------------------------------------------------------------*/
/**	Get weapon collection information.
 *	@return entity collection containing only weapon entities. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getWeapons(boolean onlyActive)
	{
		return getEntities(Entity.CAT_WEAPONS, null, null, onlyActive);
	}

/*-------------------------------------------------------------------*/
/**	Get weapon collection information. Returns only entities which are
 *	currently active.
 *	@return entity collection containing only weapon entities. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getWeapons()
	{
		return getWeapons(true);
	}

/*-------------------------------------------------------------------*/
/**	Get object collection information.
 *	@return entity collection containing only object entities. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getObjects(boolean onlyActive)
	{
		return getEntities(Entity.CAT_OBJECTS, null, null, onlyActive);
	}

/*-------------------------------------------------------------------*/
/**	Get object collection information. Returns only entities which are
 *	currently active.
 *	@return entity collection containing only object entities. */
/*-------------------------------------------------------------------*/
	public synchronized Vector getObjects()
	{
		return getObjects(true);
	}

/*-------------------------------------------------------------------*/
/**	Set client HUD information.
 *	@param layout client HUD information. */
/*-------------------------------------------------------------------*/
	public synchronized void setLayout(Layout layout)
	{
		this.layout = layout;
	}
	
/*-------------------------------------------------------------------*/
/**	Get client HUD information.
 *	@return client HUD information. */
/*-------------------------------------------------------------------*/
	public synchronized Layout getLayout()
	{
		return layout;
	}
	
/*-------------------------------------------------------------------*/
/**	Set client config information.
 *	@param config client config information. */
/*-------------------------------------------------------------------*/
	public synchronized void setConfig(Config config)
	{
		this.config = config;
	}
	
/*-------------------------------------------------------------------*/
/**	Get client config information.
 *	@return client config information. */
/*-------------------------------------------------------------------*/
	public synchronized Config getConfig()
	{
		return config;
	}
	
}
