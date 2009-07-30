//---------------------------------------------------------------------
// Name:			Entity.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.state;

import java.util.StringTokenizer;

import soc.qase.info.Config;
import soc.qase.info.Server;

/*-------------------------------------------------------------------*/
/**	Wrapper class for entity attributes (players, items, weapons,
 *	objects). The Entity class is used as a wrapper class for a set of
 *	information concerning an entity's state as a tangible object in the
 *	simulator environment. Each frame of execution, instances of this
 *	type can be extracted from a Proxy object, while the agent is part
 *	of an ongoing simulation. All entities belong to one of the following
 *	categories: objects, items, weapons, or player. If the entity belongs
 *	to one of the first three categories, it can be further investigated
 *	by consequent calls to getType() and getSubType(). However, if the
 *	category corresponds to player, it has no type or sub-type; instead
 *	the getName() method can be called to investigate the name of the
 *	currently investigated entity. */
/*-------------------------------------------------------------------*/
public class Entity
{
	private int entityNumber = 0;
	private boolean active = false;
	private Model model = null;
	private Effects effects = null;
	private Origin origin = null;
	private Angles angles = null;
	private Origin oldOrigin = null;
	private Sound sound = null;
	private Events events = null;
	private Solid solid = null;
	private Config config = null;
	private String category = null;
	private String modelString = null;
	private String type = null;
	private String subType = null;
	private String name = null;
	private String skin = null;
	private int inventoryIndex = -1;
	private boolean respawned = false;
	public boolean playerDied = false;
	public boolean playerJumped = false;

	public static final String	ANY = null;
	public static final String	CAT_ITEMS = "items", CAT_WEAPONS = "weapons", CAT_PLAYERS = "players", CAT_OBJECTS = "objects", CAT_MISC = "misc",
								TYPE_KEYS = "keys", TYPE_ARMOR = "armor", TYPE_HEALTH = "healing", TYPE_AMMO = "ammo", TYPE_MEGAHEALTH = "mega_h",
								SUBTYPE_MEDIUM = "medium", SUBTYPE_LARGE = "large", SUBTYPE_STIMPACK = "stimpack";

	// note: 33 added to standard index to differentiate between models of weapon while on the ground versus while in the player's hand
	public static final String	TYPE_SHOTGUN = Config.items[Inventory.SHOTGUN + 33], TYPE_SUPERSHOTGUN = Config.items[Inventory.SUPER_SHOTGUN + 33],
								TYPE_MACHINEGUN = Config.items[Inventory.MACHINEGUN + 33], TYPE_CHAINGUN = Config.items[Inventory.CHAINGUN + 33],
								TYPE_GRENADES = Config.items[Inventory.GRENADES + 33], TYPE_GRENADELAUNCHER = Config.items[Inventory.GRENADE_LAUNCHER + 33],
								TYPE_ROCKETLAUNCHER = Config.items[Inventory.ROCKET_LAUNCHER + 33], TYPE_HYPERBLASTER = Config.items[Inventory.HYPERBLASTER + 33],
								TYPE_RAILGUN = Config.items[Inventory.RAILGUN + 33], TYPE_BFG = Config.items[Inventory.BFG10K + 33];

	public static final String	TYPE_INVULNERABILITY = Config.items[Inventory.INVULNERABILITY], TYPE_AMMOPACK = Config.items[Inventory.AMMO_PACK],
								TYPE_QUAD = Config.items[Inventory.QUAD_DAMAGE], TYPE_SILENCER = Config.items[Inventory.SILENCER], TYPE_ADRENALINE = Config.items[Inventory.ADRENALINE],
								TYPE_BANDOLIER = Config.items[Inventory.BANDOLIER], TYPE_REBREATHER = Config.items[Inventory.REBREATHER], TYPE_ENVIRONMENTSUIT = Config.items[Inventory.ENVIRONMENT_SUIT],
								SUBTYPE_COMBATARMOR = Config.items[Inventory.COMBAT_ARMOR], SUBTYPE_ARMORSHARD = Config.items[Inventory.ARMOR_SHARD], SUBTYPE_BODYARMOR = Config.items[Inventory.BODY_ARMOR],
								SUBTYPE_JACKETARMOR = Config.items[Inventory.JACKET_ARMOR];

	public static final String	TYPE_ANCIENTHEAD = Config.items[Inventory.ANCIENT_HEAD], SUBTYPE_DATACD = Config.items[Inventory.DATA_CD], SUBTYPE_BLUEKEY = Config.items[Inventory.BLUE_KEY],
								SUBTYPE_REDKEY = Config.items[Inventory.RED_KEY], SUBTYPE_POWERCUBE = Config.items[Inventory.POWER_CUBE], SUBTYPE_PYRAMIDKEY = Config.items[Inventory.PYRAMID_KEY],
								SUBTYPE_DATASPINNER = Config.items[Inventory.DATA_SPINNER], SUBTYPE_SECURITYPASS = Config.items[Inventory.SECURITY_PASS], SUBTYPE_COMMANDERSHEAD = Config.items[Inventory.COMMANDERS_HEAD],
								SUBTYPE_SHELLS = Config.items[Inventory.SHELLS], SUBTYPE_BULLETS = Config.items[Inventory.BULLETS], SUBTYPE_CELLS = Config.items[Inventory.CELLS], SUBTYPE_ROCKETS = Config.items[Inventory.ROCKETS],
								SUBTYPE_SLUGS = Config.items[Inventory.SLUGS], SUBTYPE_POWERSCREEN = Config.items[Inventory.POWER_SCREEN], SUBTYPE_POWERSHIELD = Config.items[Inventory.POWER_SHIELD];

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public Entity()
	{	}

	private static String[] enumerateItemStrings()
	{
		String[] itemStrings = new String[51];

		itemStrings[0] = CAT_ITEMS; itemStrings[1] = CAT_WEAPONS; itemStrings[2] = CAT_PLAYERS;
		itemStrings[3] = CAT_OBJECTS; itemStrings[4] = TYPE_KEYS; itemStrings[5] = TYPE_ARMOR;
		itemStrings[6] = TYPE_HEALTH; itemStrings[7] = TYPE_AMMO; itemStrings[8] = TYPE_MEGAHEALTH;
		itemStrings[9] = SUBTYPE_MEDIUM; itemStrings[10] = SUBTYPE_LARGE; itemStrings[11] = SUBTYPE_STIMPACK;

		int index = 12;

		for(int i = 1 ; i < Config.items.length; i++)
		{
			if(i < 7 || i > 17)
				itemStrings[index++] = Config.items[i];
		}

		return itemStrings;
	}

/*-------------------------------------------------------------------*/
/**	Calculate entity family. */
/*-------------------------------------------------------------------*/
	public void confirmFamily()
	{
		StringTokenizer st = null;
		String currentString = null;
		int players = 0;

		if(modelString == null)
			modelString = getModelString();

		if(modelString != null)
		{
			st = new StringTokenizer(modelString, "/");
			currentString = st.nextToken();

			if(currentString.equals("models"))
			{
				currentString = st.nextToken();

				if(currentString.equals("items") || currentString.equals("weapons") || currentString.equals("objects"))
				{
					category = currentString;
					type = st.nextToken();

					if(st.hasMoreTokens())
					{
						currentString = st.nextToken();
						if(currentString.equals("tris.md2"));
						else subType = currentString;
					}
				}
			}
			else
			{
				players = config.getMaxClients();

				if(entityNumber <= players)
				{
					st = new StringTokenizer(modelString, "\\");
					category = CAT_PLAYERS;
					name = st.nextToken();

					if(st.hasMoreTokens())
						skin = st.nextToken();
				}
				else
				{
					category = CAT_MISC;
					type = modelString;
					subType = modelString;
				}
			}
		}
	}

/*-------------------------------------------------------------------*/
/**	Get model string.
 *	@return model string. */
/*-------------------------------------------------------------------*/
	private String getModelString()
	{
		int index = 0;
		int players = 0;

		index = model.getIndex(0);
		if(index == 0) index = model.getIndex(1);
		if(index == 0) index = model.getIndex(2);
		if(index == 0) index = model.getIndex(3);

		players = Integer.parseInt(config.getConfigString(30));

		if(entityNumber > players)
			modelString = config.getConfigString(Config.SECTION_MODELS + index);
		else
			modelString = config.getConfigString(Config.SECTION_PLAYER_SKINS + entityNumber - 1);

		return modelString;
	}

/*-------------------------------------------------------------------*/
/**	Resolve the CTF team number of the local agent, if the current server
 *	is running the CTF mod.
 *	@return the team number of the local agent; 0 = RED, 1 = BLUE */
/*-------------------------------------------------------------------*/
	public int getCTFTeamNumber()
	{
		if(!getCategory().equals("players")) return Integer.MIN_VALUE;

		if(getSkin().indexOf("/ctf_r") > 0) return 0;
		if(getSkin().indexOf("/ctf_b") > 0) return 1;

		return Integer.MIN_VALUE;
	}

/*-------------------------------------------------------------------*/
/**	Resolve the CTF team name of the local agent, if the current server
 *	is running the CTF mod.
 *	@return the team name of the local agent; either RED, BLUE or null
 *	if the agent is not currently on a team. */
/*-------------------------------------------------------------------*/
	public String getCTFTeamString()
	{
		int ctfTeamIndex = getCTFTeamNumber();
		
		if(ctfTeamIndex >= 0)
			return Server.CTF_STRINGS[ctfTeamIndex];
		else
			return null;
	}

/*-------------------------------------------------------------------*/
/**	Get entity category string. The category can assume one of the
 *	following values: "items", "weapons", "objects", or "players"
 *	@return category string. */
/*-------------------------------------------------------------------*/
	public String getCategory()
	{
		if(category == null)
			confirmFamily();

		return (category == null ? "" : category);
	}

/*-------------------------------------------------------------------*/
/**	Get entity type string.
 *	@return entity type string. */
/*-------------------------------------------------------------------*/
	public String getType()
	{
		return (type == null ? (getCategory().equals("players") || type == null ? "" : type) : type);
	}

/*-------------------------------------------------------------------*/
/**	Get entity subtype string.
 *	@return entity subtype string. */
/*-------------------------------------------------------------------*/
	public String getSubType()
	{
		return (subType == null ? (getCategory().equals("players") || subType == null ? "" : subType) : subType);
	}

/*-------------------------------------------------------------------*/
/**	Convenience method. Indicates whether this Entity represents a player.
 *	@return true if this Entity represents a player, false otherwise. */
/*-------------------------------------------------------------------*/
	public boolean isPlayerEntity()
	{
		return CAT_PLAYERS.equalsIgnoreCase(getCategory());
	}

/*-------------------------------------------------------------------*/
/**	Convenience method. Indicates whether this Entity represents an item.
 *	@return true if this Entity represents an item, false otherwise. */
/*-------------------------------------------------------------------*/
	public boolean isItemEntity()
	{
		return CAT_ITEMS.equalsIgnoreCase(getCategory());
	}

/*-------------------------------------------------------------------*/
/**	Convenience method. Indicates whether this Entity represents a weapon.
 *	@return true if this Entity represents a weapon, false otherwise. */
/*-------------------------------------------------------------------*/
	public boolean isWeaponEntity()
	{
		return CAT_WEAPONS.equalsIgnoreCase(getCategory());
	}

/*-------------------------------------------------------------------*/
/**	Convenience method. Indicates whether this Entity represents an object.
 *	@return true if this Entity represents an object, false otherwise. */
/*-------------------------------------------------------------------*/
	public boolean isObjectEntity()
	{
		return CAT_OBJECTS.equalsIgnoreCase(getCategory());
	}

/*-------------------------------------------------------------------*/
/**	Get entity name. If entity category corresponds to 'players'
 *	this method will return the name of the entity.
 *	@return name. */
/*-------------------------------------------------------------------*/
	public String getName()
	{
		return (name != null || getCategory().equals("players") ? name : "");
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity skin. If entity category corresponds to 'players'
 *	this method will return the skin of the entity.
 *	@return skin. */
/*-------------------------------------------------------------------*/
	public String getSkin()
	{
		return (skin != null || getCategory().equals("players") ? skin : "");
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity number.
 *	@return entity number. */
/*-------------------------------------------------------------------*/
	public int getNumber()
	{
		return entityNumber;
	}

/*-------------------------------------------------------------------*/
/**	Get inventory number corresponding to the current entity type. Uses
 *	Config.
 *	@return entity number
 *	@see soc.qase.info.Config#modelStringToInventoryIndex(java.lang.String) */
/*-------------------------------------------------------------------*/
	public int getInventoryIndex()
	{
		if(getType().equals("healing"))
			return 41;
		else if(inventoryIndex == -1)
			inventoryIndex = Config.modelStringToInventoryIndex(getModelString());

		return inventoryIndex;
	}

/*-------------------------------------------------------------------*/
/**	Get the amount of time, in frames (ie tenths of seconds), that an
 *	item will be inactive for once collected.
 *	@return respawn time interval */
/*-------------------------------------------------------------------*/
	public int getRespawnTime()
	{
		if(getModelString().indexOf("invul") != -1)
			return 3000;
		else if(getModelString().indexOf("band") != -1)
			return Integer.MAX_VALUE;
		else if(getCategory().equals("weapon") || getType().equals("ammo") || getType().equals("healing"))
			return 300;
		else if(getType().equals("armor"))
			return 200;
		else
			return 600;
	}

/*-------------------------------------------------------------------*/
/**	Set entity number.
 *	@param entityNumber entity number. */
/*-------------------------------------------------------------------*/
	public void setNumber(int entityNumber)
	{
		this.entityNumber = entityNumber;
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity activity.
 *	@return entity activity. */
/*-------------------------------------------------------------------*/
	public boolean getActive()
	{
		return active;
	}
	
/*-------------------------------------------------------------------*/
/**	Set entity activity.
 *	@param active entity activity. */
/*-------------------------------------------------------------------*/
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity model.
 *	@return entity model. */
/*-------------------------------------------------------------------*/
	public Model getModel()
	{
		return model;
	}
	
/*-------------------------------------------------------------------*/
/**	Set entity model.
 *	@param model entity model. */
/*-------------------------------------------------------------------*/
	public void setModel(Model model)
	{
		this.model = model;
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity effects.
 *	@return entity effects. */
/*-------------------------------------------------------------------*/
	public Effects getEffects()
	{
		return effects;
	}
	
/*-------------------------------------------------------------------*/
/**	Set entity effects.
 *	@param effects entity effects. */
/*-------------------------------------------------------------------*/
	public void setEffects(Effects effects)
	{
		this.effects = effects;
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity origin.
 *	@return entity origin. */
/*-------------------------------------------------------------------*/
	public Origin getOrigin()
	{
		return origin;
	}
	
/*-------------------------------------------------------------------*/
/**	Set entity origin.
 *	@param origin entity origin. */
/*-------------------------------------------------------------------*/
	public void setOrigin(Origin origin)
	{
		this.origin = origin;
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity angles.
 *	@return entity angles. */
/*-------------------------------------------------------------------*/
	public Angles getAngles()
	{
		return angles;
	}
	
/*-------------------------------------------------------------------*/
/**	Set entity angles.
 *	@param angles entity angles. */
/*-------------------------------------------------------------------*/
	public void setAngles(Angles angles)
	{
		this.angles = angles;
	}
	
/*-------------------------------------------------------------------*/
/**	Get old entity origin.
 *	@return old entity orgin. */
/*-------------------------------------------------------------------*/
	public Origin getOldOrigin()
	{
		return oldOrigin;
	}
	
/*-------------------------------------------------------------------*/
/**	Set old entity origin.
 *	@param origin entity origin. */
/*-------------------------------------------------------------------*/
	public void setOldOrigin(Origin origin)
	{
		this.oldOrigin = oldOrigin;
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity sound.
 *	@return entity sound. */
/*-------------------------------------------------------------------*/
	public Sound getSound()
	{
		return sound;
	}
	
/*-------------------------------------------------------------------*/
/**	Set entity sound.
 *	@param sound entity sound. */
/*-------------------------------------------------------------------*/
	public void setSound(Sound sound)
	{
		this.sound = sound;
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity events.
 *	@return entity events. */
/*-------------------------------------------------------------------*/
	public Events getEvents()
	{
		return events;
	}

/*-------------------------------------------------------------------*/
/**	Set entity events.
 *	@param events entity events. */
/*-------------------------------------------------------------------*/
	public void setEvents(Events events)
	{
		this.events = events;
		respawned = (events == null ? false : events.checkEvent(Events.ITEM_RESPAWN));
	}

/*-------------------------------------------------------------------*/
/**	Check whether this entity is an item that has just respawned.
 *	@return true if this is an entity that has just respawned, false
 *	otherwise */
/*-------------------------------------------------------------------*/
	public boolean isRespawned()
	{
		return respawned;
	}

/*-------------------------------------------------------------------*/
/**	Check whether this entity died during the current frame. This
 *	naturally applies only to entities of category "players". Note
 *	that this value serves to flag an instantaneous event - it will
 *	register as true only for the frame at which the player died,
 *	NOT for the entire period between the player's death and respawn.
 *	@return true if this is a player entity which died during the
 *	current frame, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean hasDied()
	{
		return playerDied;
	}

/*-------------------------------------------------------------------*/
/**	If this Entity represents a Player, this method will indicate whether
 *	or not the player is currently crouching. Useful when determining
 *	how an agent should aim at an opponent.
 *	@return true if the player represented by this Entity is crouching,
 *	false if this Entity is not a player or is not crouching. */
/*-------------------------------------------------------------------*/
	public boolean isCrouching()
	{
		return (isPlayerEntity() && solid != null && solid.getSolid() == 4194);
	}

/*-------------------------------------------------------------------*/
/**	Check whether this entity jumped during the current frame. This
 *	naturally applies only to entities of category "players". Note
 *	that this value serves to flag an instantaneous event - it will
 *	register as true only for the frame at which the player jumped,
 *	NOT for the entire period between the player's takeoff and landing.
 *	@return true if this Entity represents a player who jumped during
 *	the current frame, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isJumping()
	{
		return playerJumped;
	}

/*-------------------------------------------------------------------*/
/**	Get the inventory index of the current active weapon. This naturally
 *	applies only to entities of category "players". The value returned
 *	by this function will match one of the constants defined in the
 *	PlayerGun class; calling config.getItemName(entity.getWeaponInventoryIndex())
 *	or inventory.getItemName(entity.getWeaponInventoryIndex()) will
 *	return the full English-language name of the weapon.
 *	@return the inventory index of the active weapon, if this Entity
 *	represents a Player and a valid gun is equipped; -1 otherwise. */
/*-------------------------------------------------------------------*/
	public int getWeaponInventoryIndex()
	{
		return (isPlayerEntity() && model != null  && model.getFullSkin() != -1 ? model.getWeaponSkin() + 6 : -1);
	}

/*-------------------------------------------------------------------*/
/**	Get entity solid.
 *	@return entity solid. */
/*-------------------------------------------------------------------*/
	public Solid getSolid()
	{
		return solid;
	}
	
/*-------------------------------------------------------------------*/
/**	Set entity solid.
 *	@param solid entity solid. */
/*-------------------------------------------------------------------*/
	public void setSolid(Solid solid)
	{
		this.solid = solid;
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity config.
 *	@param config entity configuration */
/*-------------------------------------------------------------------*/
	public void setConfig(Config config)
	{
		this.config = config;
	}

/*-------------------------------------------------------------------*/
/**	Merge Entity properties from an existing Entity object into the
 *	current Entity object. Used when assimilating cumulative updates
 *	from the Quake 2 server into the gamestate.
 *	@param entity source Entity whose attributes should be merged
 *	into the current Entity
 *	@see soc.qase.state.World#setEntity(Entity, boolean) */
/*-------------------------------------------------------------------*/
	public void merge(Entity entity)
	{
		if(model == null || entity == null) model = (entity == null ? (model == null ? new Model() : model) : entity.model); else model.merge(entity.model);
		if(effects == null || entity == null) effects = (entity == null ? (effects == null ? new Effects() : effects) : entity.effects); else effects.merge(entity.effects);
		if(origin == null || entity == null) origin = (entity == null ? (origin == null ? new Origin() : origin) : entity.origin); else origin.merge(entity.origin);
		if(angles == null || entity == null) angles = (entity == null ? (angles == null ? new Angles() : angles) : entity.angles); else angles.merge(entity.angles);
		if(oldOrigin == null || entity == null) oldOrigin = (entity == null ? (oldOrigin == null ? new Origin() : oldOrigin) : entity.oldOrigin); else oldOrigin.merge(entity.oldOrigin);
		if(sound == null || entity == null) sound = (entity == null ? (sound == null ? new Sound() : sound) : entity.sound); else sound.merge(entity.sound);
		if(events == null || entity == null) events = (entity == null ? (events == null ? new Events() : events) : entity.events); else events.merge(entity.events);
		if(solid == null || entity == null) solid = (entity == null ? (solid == null ? new Solid() : solid) : entity.solid); else solid.merge(entity.solid);

		entityNumber = (entityNumber == 0 && entity != null ? entity.entityNumber : entityNumber);
		modelString = (entity == null || entity.modelString == null ? getModelString() : entity.modelString);

		if(entity != null)
		{
			name = entity.name;
			skin = entity.skin;

			type = entity.type;
			subType = entity.subType;
			category = entity.category;
		}
	}

/*-------------------------------------------------------------------*/
/**	Creates a duplicate of the object by cloning each of its fields.
 *	@return a deep copy of the current object */
/*-------------------------------------------------------------------*/
	public Entity deepCopy()
	{
		Entity ent = new Entity();

		ent.setNumber(entityNumber);
		ent.setActive(active);
		ent.setModel(model);
		ent.setEffects((effects == null ? null : effects.deepCopy()));
		ent.setOrigin((origin == null ? null : origin.deepCopy()));
		ent.setAngles((angles == null ? null : angles.deepCopy()));
		ent.setOldOrigin((oldOrigin == null ? null : oldOrigin.deepCopy()));
		ent.setSound((sound == null ? null : sound.deepCopy()));
		ent.setEvents((events == null ? null : events.deepCopy()));
		ent.setSolid((solid == null ? null : solid.deepCopy()));
		ent.inventoryIndex = inventoryIndex;

		ent.category = this.category;
		ent.modelString = this.modelString;
		ent.type = this.type;
		ent.subType = this.subType;
		ent.name = this.name;
		ent.skin = this.skin;

//		ent.setConfig((config == null ? null : config.deepCopy()));

		return ent;
	}

/*-------------------------------------------------------------------*/
/**	Constructs and returns a string representation of the Entity object.
 *	@return a string representation of the Entity object */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		return ("Entity " + getNumber() + ":\t" + getCategory() + " / " + getType() + " / " + getSubType() + " / " + getName() + " / " + getSkin());
	}
}
