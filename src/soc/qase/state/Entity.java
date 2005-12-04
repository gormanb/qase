//---------------------------------------------------------------------
// Name:			Effects.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.state;

import java.util.*;
import soc.qase.com.message.*;
import soc.qase.info.*;

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
 *	category corresponds to player, it has no type or sub-type, instead
 *	the getName() method can be called to investigate the name of the
 *	currently investigated entity (diverse agent). */
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

	public static final String	ANY = null;
	public static final String	CAT_ITEMS = "items", CAT_WEAPONS = "weapons", CAT_PLAYERS = "players", CAT_OBJECTS = "objects",
								TYPE_KEYS = "keys", TYPE_ARMOR = "armor", TYPE_HEALTH = "healing", TYPE_AMMO = "ammo", TYPE_MEGAHEALTH = "mega_h",
								SUBTYPE_MEDIUM = "medium", SUBTYPE_LARGE = "large", SUBTYPE_STIMPACK = "stimpack";

	public static final String	TYPE_BLASTER = Config.items[Inventory.BLASTER], TYPE_SHOTGUN = Config.items[Inventory.SHOTGUN],
								TYPE_SUPERSHOTGUN = Config.items[Inventory.SUPER_SHOTGUN], TYPE_MACHINEGUN = Config.items[Inventory.MACHINEGUN],
								TYPE_CHAINGUN = Config.items[Inventory.CHAINGUN], TYPE_GRENADES = Config.items[Inventory.GRENADES],
								TYPE_GRENADELAUNCHER = Config.items[Inventory.GRENADE_LAUNCHER], TYPE_ROCKETLAUNCHER = Config.items[Inventory.ROCKET_LAUNCHER],
								TYPE_HYPERBLASTER = Config.items[Inventory.HYPERBLASTER], TYPE_RAILGUN = Config.items[Inventory.HYPERBLASTER],
								TYPE_BFG = Config.items[Inventory.BFG10K];

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
	
/*-------------------------------------------------------------------*/
/**	Calculate entity family. */
/*-------------------------------------------------------------------*/
	public void confirmFamily()
	{
		if(modelString != null)
			return;

		StringTokenizer st = null;
		String currentString = null;
		int players = 0;

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
				players = (new Integer(config.getConfigString(30))).intValue();

				if(entityNumber < players)
				{
					st = new StringTokenizer(modelString, "\\");
					category = new String("player");
					name = st.nextToken();

					if(st.countTokens() > 1)
						skin = st.nextToken();
				}
			}
		}
	}

/*-------------------------------------------------------------------*/
/**	Get model string.
 *	@return model string. */
/*-------------------------------------------------------------------*/
	public String getModelString()
	{
		if(modelString != null && modelString.length() > 0)
			return new String(modelString);

		String result = null;

		int index = 0;
		int players = 0;

		try
		{
			index = getModel().getIndex(0);
			if(index == 0) index = getModel().getIndex(1);
			if(index == 0) index = getModel().getIndex(2);
			if(index == 0) index = getModel().getIndex(3);

			players = (new Integer(config.getConfigString(30))).intValue();

			if(entityNumber > players)
				result = config.getConfigString(32 + index);
			else
				result = config.getConfigString(1312 + entityNumber - 1);
		}
		catch(Exception e)
		{	}

		return result;
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity name. If entity category corresponds to 'player'
 *	this method will return the name of the entity.
 *	@return name. */
/*-------------------------------------------------------------------*/
	public String getName()
	{
		if(name == null)
			confirmFamily();

		return (name == null ? "" : new String(name));
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity skin. If entity category corresponds to 'player'
 *	this method will return the skin of the entity.
 *	@return skin. */
/*-------------------------------------------------------------------*/
	public String getSkin()
	{
		if(skin == null)
			confirmFamily();

		return (skin == null ? "" : new String(skin));
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity category string. The category can assume one of the
 *	following values: "items", "weapons", "objects", or "player"
 *	@return category string. */
/*-------------------------------------------------------------------*/
	public String getCategory()
	{
		if(category == null)
			confirmFamily();

		return (category == null ? "" : new String(category));
	}
	
/*-------------------------------------------------------------------*/
/**	Get entity type string.
 *	@return entity type string. */
/*-------------------------------------------------------------------*/
	public String getType()
	{
		if(type == null)
			confirmFamily();

		return (type == null ? "" : new String(type));
	}

/*-------------------------------------------------------------------*/
/**	Get entity subtype string.
 *	@return entity subtype string. */
/*-------------------------------------------------------------------*/
	public String getSubType()
	{
		if(subType == null)
			confirmFamily();

		return (subType == null ? "" : new String(subType));
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
		if(model == null) model = new Model();
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
		if(effects == null) effects = new Effects();
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
		if(origin == null) origin = new Origin();
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
		if(angles == null) angles = new Angles();
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
		if(oldOrigin == null) oldOrigin = new Origin();
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
		if(sound == null) sound = new Sound();
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
		if(events == null) events = new Events();
		return events;
	}

/*-------------------------------------------------------------------*/
/**	Set entity events.
 *	@param events entity events. */
/*-------------------------------------------------------------------*/
	public void setEvents(Events events)
	{
		this.events = events;
		respawned = events.checkEvent(Events.ITEM_RESPAWN);
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
/**	Get entity solid.
 *	@return entity solid. */
/*-------------------------------------------------------------------*/
	public Solid getSolid()
	{
		if(solid == null) solid = new Solid();
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
		if(entity != null)
		{
			if(model == null) model = new Model();
			if(effects == null) effects = new Effects();
			if(origin == null) origin = new Origin();
			if(angles == null) angles = new Angles();
			if(oldOrigin == null) oldOrigin = new Origin();
			if(sound == null) sound = new Sound();
			if(events == null) events = new Events();
			if(solid == null) solid = new Solid();
			entityNumber = entity.getNumber();
			model.merge(entity.getModel());
			effects.merge(entity.getEffects());
			origin.merge(entity.getOrigin());
			angles.merge(entity.getAngles());
			oldOrigin.merge(entity.getOldOrigin());
			sound.merge(entity.getSound());
			events.merge(entity.getEvents());
			solid.merge(entity.getSolid());
			inventoryIndex = entity.getInventoryIndex();
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
		ent.setModel((model == null ? null : model.deepCopy()));
		ent.setEffects((effects == null ? null : effects.deepCopy()));
		ent.setOrigin((origin == null ? null : origin.deepCopy()));
		ent.setAngles((angles == null ? null : angles.deepCopy()));
		ent.setOldOrigin((oldOrigin == null ? null : oldOrigin.deepCopy()));
		ent.setSound((sound == null ? null : sound.deepCopy()));
		ent.setEvents((events == null ? null : events.deepCopy()));
		ent.setSolid((solid == null ? null : solid.deepCopy()));
		ent.inventoryIndex = inventoryIndex;
//		ent.setConfig((config == null ? null : config.deepCopy()));

		return ent;
	}
}
