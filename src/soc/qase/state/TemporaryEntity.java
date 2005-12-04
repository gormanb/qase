//---------------------------------------------------------------------
// Name:			TemporaryEntity.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.state;

import java.util.*;
import soc.qase.info.*;
import soc.qase.com.message.*;
import soc.qase.tools.vecmath.*;

/*-------------------------------------------------------------------*/
/**	Represents a temporary entity - a projectile, blood, weapon trails,
 *	etc. Temporary entity events are for things that happen at a location
 *	seperate from existing entity. Temporary entity messages are explicitly
 *	constructed and broadcast. */
/*-------------------------------------------------------------------*/
public class TemporaryEntity
{
	private int entityType = -1, entityCategory = -1;

	private long plat2Flags = -1, wait = -1;
	private long flashEntity = -1, style = -1;
	private long entity = -1, destEntity = -1;
	private long nextID = -1, count = -1, type = -1;

	private Vector3f moveDir = null;
	private Origin origin = null, destOrigin = null, start = null;

	private Origin traceEndPos = null, pos1 = null;
	private Origin pos2 = null, pos3 = null, pos4 = null;

	public static final int TE_GUNSHOT = 0, TE_BLOOD = 1, TE_BLASTER = 2,
	TE_SHOTGUN = 4, TE_SPARKS = 9, TE_SCREEN_SPARKS = 12, TE_SHIELD_SPARKS = 13,
	TE_BULLET_SPARKS = 14, TE_GREENBLOOD_NEW = 26, TE_GREENBLOOD_OLD = 27,
	TE_BLASTER2 = 30, TE_MOREBLOOD = 42, TE_HEATBEAM_SPARKS = 43,
	TE_HEATBEAM_STEAM = 44, TE_FLECHETTE = 55, TE_RAILTRAIL = 3,
	TE_BUBBLETRAIL = 11, TE_BFG_LASER = 23, TE_PLASMATRAIL = 26,
	TE_BLUEHYPERBLASTER = 27, TE_DEBUGTRAIL = 34, TE_BUBBLETRAIL2 = 41,
	TE_ELECTRIC_SPARKS = 46, TE_SPLASH = 10, TE_LASER_SPARKS = 15,
	TE_PARASITE_ATTACK = 16, TE_MEDIC_CABLE_ATTACK = 19, TE_GRAPPLE_CABLE = 24,
	TE_WELDING_SPARKS = 25, TE_TUNNEL_SPARKS = 29, TE_RAILTRAIL2 = 31,
	TE_FLAME = 32, TE_LIGHTNING = 33, TE_FLASHLIGHT = 36, TE_FORCEWALL = 37,
	TE_HEATBEAM = 38, TE_MONSTER_HEATBEAM = 39, TE_STEAM = 40, TE_WIDOWBEAMOUT = 50;

	public static final int POINT_ENTITY = 1, IMPACT_ENTITY = 2, LINE_ENTITY = 3,
							SPECIAL_ENTITY = 4, BAD_ENTITY = 5;

	public static final int[] POINT_INDICES = { 5, 6, 7, 8, 17, 18, 20, 21, 22, 28, 35, 45, 47, 48, 49, 51, 52, 53, 54 },
							   IMPACT_INDICES = { 0, 1, 2, 4, 9, 12, 13, 14, 26, 30, 42, 43, 44, 55 },
							   LINE_INDICES = { 3, 11, 23, 27, 34, 41, 46 },
							   SPECIAL_INDICES = { 10, 15, 16, 19, 24, 25, 29, 32, 33, 36, 37, 38, 39, 40, 50 };

	public static final double[][] VERTEXNORMS = {  {-0.525731, 0.000000, 0.850651}, {-0.442863, 0.238856, 0.864188}, {-0.295242, 0.000000, 0.955423}, {-0.309017, 0.500000, 0.809017}, {-0.162460, 0.262866, 0.951056}, {0.000000, 0.000000, 1.000000}, 
	{0.000000, 0.850651, 0.525731}, {-0.147621, 0.716567, 0.681718}, {0.147621, 0.716567, 0.681718}, {0.000000, 0.525731, 0.850651}, {0.309017, 0.500000, 0.809017}, {0.525731, 0.000000, 0.850651}, 
	{0.295242, 0.000000, 0.955423}, {0.442863, 0.238856, 0.864188}, {0.162460, 0.262866, 0.951056}, {-0.681718, 0.147621, 0.716567}, {-0.809017, 0.309017, 0.500000}, {-0.587785, 0.425325, 0.688191}, 
	{-0.850651, 0.525731, 0.000000}, {-0.864188, 0.442863, 0.238856}, {-0.716567, 0.681718, 0.147621}, {-0.688191, 0.587785, 0.425325}, {-0.500000, 0.809017, 0.309017}, {-0.238856, 0.864188, 0.442863}, 
	{-0.425325, 0.688191, 0.587785}, {-0.716567, 0.681718, -0.147621}, {-0.500000, 0.809017, -0.309017}, {-0.525731, 0.850651, 0.000000}, {0.000000, 0.850651, -0.525731}, {-0.238856, 0.864188, -0.442863}, 
	{0.000000, 0.955423, -0.295242}, {-0.262866, 0.951056, -0.162460}, {0.000000, 1.000000, 0.000000}, {0.000000, 0.955423, 0.295242}, {-0.262866, 0.951056, 0.162460}, {0.238856, 0.864188, 0.442863}, 
	{0.262866, 0.951056, 0.162460}, {0.500000, 0.809017, 0.309017}, {0.238856, 0.864188, -0.442863}, {0.262866, 0.951056, -0.162460}, {0.500000, 0.809017, -0.309017}, {0.850651, 0.525731, 0.000000}, 
	{0.716567, 0.681718, 0.147621}, {0.716567, 0.681718, -0.147621}, {0.525731, 0.850651, 0.000000}, {0.425325, 0.688191, 0.587785}, {0.864188, 0.442863, 0.238856}, {0.688191, 0.587785, 0.425325}, 
	{0.809017, 0.309017, 0.500000}, {0.681718, 0.147621, 0.716567}, {0.587785, 0.425325, 0.688191}, {0.955423, 0.295242, 0.000000}, {1.000000, 0.000000, 0.000000}, {0.951056, 0.162460, 0.262866}, 
	{0.850651, -0.525731, 0.000000}, {0.955423, -0.295242, 0.000000}, {0.864188, -0.442863, 0.238856}, {0.951056, -0.162460, 0.262866}, {0.809017, -0.309017, 0.500000}, {0.681718, -0.147621, 0.716567}, 
	{0.850651, 0.000000, 0.525731}, {0.864188, 0.442863, -0.238856}, {0.809017, 0.309017, -0.500000}, {0.951056, 0.162460, -0.262866}, {0.525731, 0.000000, -0.850651}, {0.681718, 0.147621, -0.716567}, 
	{0.681718, -0.147621, -0.716567}, {0.850651, 0.000000, -0.525731}, {0.809017, -0.309017, -0.500000}, {0.864188, -0.442863, -0.238856}, {0.951056, -0.162460, -0.262866}, {0.147621, 0.716567, -0.681718}, 
	{0.309017, 0.500000, -0.809017}, {0.425325, 0.688191, -0.587785}, {0.442863, 0.238856, -0.864188}, {0.587785, 0.425325, -0.688191}, {0.688191, 0.587785, -0.425325}, {-0.147621, 0.716567, -0.681718}, 
	{-0.309017, 0.500000, -0.809017}, {0.000000, 0.525731, -0.850651}, {-0.525731, 0.000000, -0.850651}, {-0.442863, 0.238856, -0.864188}, {-0.295242, 0.000000, -0.955423}, {-0.162460, 0.262866, -0.951056}, 
	{0.000000, 0.000000, -1.000000}, {0.295242, 0.000000, -0.955423}, {0.162460, 0.262866, -0.951056}, {-0.442863, -0.238856, -0.864188}, {-0.309017, -0.500000, -0.809017}, {-0.162460, -0.262866, -0.951056}, 
	{0.000000, -0.850651, -0.525731}, {-0.147621, -0.716567, -0.681718}, {0.147621, -0.716567, -0.681718}, {0.000000, -0.525731, -0.850651}, {0.309017, -0.500000, -0.809017}, {0.442863, -0.238856, -0.864188}, 
	{0.162460, -0.262866, -0.951056}, {0.238856, -0.864188, -0.442863}, {0.500000, -0.809017, -0.309017}, {0.425325, -0.688191, -0.587785}, {0.716567, -0.681718, -0.147621}, {0.688191, -0.587785, -0.425325}, 
	{0.587785, -0.425325, -0.688191}, {0.000000, -0.955423, -0.295242}, {0.000000, -1.000000, 0.000000}, {0.262866, -0.951056, -0.162460}, {0.000000, -0.850651, 0.525731}, {0.000000, -0.955423, 0.295242},
	{0.238856, -0.864188, 0.442863}, {0.262866, -0.951056, 0.162460}, {0.500000, -0.809017, 0.309017}, {0.716567, -0.681718, 0.147621}, {0.525731, -0.850651, 0.000000}, {-0.238856, -0.864188, -0.442863},
	{-0.500000, -0.809017, -0.309017}, {-0.262866, -0.951056, -0.162460}, {-0.850651, -0.525731, 0.000000}, {-0.716567, -0.681718, -0.147621}, {-0.716567, -0.681718, 0.147621}, {-0.525731, -0.850651, 0.000000},
	{-0.500000, -0.809017, 0.309017}, {-0.238856, -0.864188, 0.442863}, {-0.262866, -0.951056, 0.162460}, {-0.864188, -0.442863, 0.238856}, {-0.809017, -0.309017, 0.500000}, {-0.688191, -0.587785, 0.425325},
	{-0.681718, -0.147621, 0.716567}, {-0.442863, -0.238856, 0.864188}, {-0.587785, -0.425325, 0.688191}, {-0.309017, -0.500000, 0.809017}, {-0.147621, -0.716567, 0.681718}, {-0.425325, -0.688191, 0.587785},
	{-0.162460, -0.262866, 0.951056}, {0.442863, -0.238856, 0.864188}, {0.162460, -0.262866, 0.951056}, {0.309017, -0.500000, 0.809017}, {0.147621, -0.716567, 0.681718}, {0.000000, -0.525731, 0.850651},
	{0.425325, -0.688191, 0.587785}, {0.587785, -0.425325, 0.688191}, {0.688191, -0.587785, 0.425325}, {-0.955423, 0.295242, 0.000000}, {-0.951056, 0.162460, 0.262866}, {-1.000000, 0.000000, 0.000000},
	{-0.850651, 0.000000, 0.525731}, {-0.955423, -0.295242, 0.000000}, {-0.951056, -0.162460, 0.262866}, {-0.864188, 0.442863, -0.238856}, {-0.951056, 0.162460, -0.262866}, {-0.809017, 0.309017, -0.500000},
	{-0.864188, -0.442863, -0.238856}, {-0.951056, -0.162460, -0.262866}, {-0.809017, -0.309017, -0.500000}, {-0.681718, 0.147621, -0.716567}, {-0.681718, -0.147621, -0.716567}, {-0.850651, 0.000000, -0.525731},
	{-0.688191, 0.587785, -0.425325}, {-0.587785, 0.425325, -0.688191}, {-0.425325, 0.688191, -0.587785}, {-0.425325, -0.688191, -0.587785}, {-0.587785, -0.425325, -0.688191}, {-0.688191, -0.587785, -0.425325} };

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public TemporaryEntity()
	{	}

/*-------------------------------------------------------------------*/
/**	Set entity type. One of the TE constants listed above.
 *	@param type the entity type */
/*-------------------------------------------------------------------*/
	public void setType(int type)
	{
		entityType = type;
	}

/*-------------------------------------------------------------------*/
/**	Set entity category. Point, impact, line, special or bad, as defined
 *	by the constants above.
 *	@param cat the entity category */
/*-------------------------------------------------------------------*/
	public void setCategory(int cat)
	{
		entityCategory = cat;
	}

/*-------------------------------------------------------------------*/
/**	Set the entity which spawned the temporary entity.
 *	@param e the spawning entity */
/*-------------------------------------------------------------------*/
	public void setSpawnEntity(long e)
	{
		entity = e;
	}

/*-------------------------------------------------------------------*/
/**	Set entity at which an effect ends.
 *	@param de the destination entity */
/*-------------------------------------------------------------------*/
	public void setDestinationEntity(long de)
	{
		destEntity = de;
	}

/*-------------------------------------------------------------------*/
/**	Set temporary entity's origin.
 *	@param o the entity origin */
/*-------------------------------------------------------------------*/
	public void setOrigin(Origin o)
	{
		origin = o;
	}

/*-------------------------------------------------------------------*/
/**	Set the temporary entity's destination.
 *	@param dor the destination origin */
/*-------------------------------------------------------------------*/
	public void setDestinationOrigin(Origin dor)
	{
		destOrigin = dor;
	}

/*-------------------------------------------------------------------*/
/**	Set the start of the TE_FLAME entity
 *	@param so the entity start origin */
/*-------------------------------------------------------------------*/
	public void setStart(Origin so)
	{
		start = so;
	}

/*-------------------------------------------------------------------*/
/**	Set the direction in which the temporary entity is moving.
 *	@param mDir the direction of movement */
/*-------------------------------------------------------------------*/
	public void setMoveDir(Vector3f mDir)
	{
		moveDir = mDir;
	}

/*-------------------------------------------------------------------*/
/**	Set additional positions for larger entites.
 *	@param p1 extra position 1
 *	@param p2 extra position 2
 *	@param p3 extra position 3
 *	@param p4 extra position 4 */
/*-------------------------------------------------------------------*/
	public void setExtraPositions(Origin p1, Origin p2, Origin p3, Origin p4)
	{
		pos1 = p1;
		pos2 = p2;
		pos3 = p3;
		pos4 = p4;
	}

/*-------------------------------------------------------------------*/
/**	Set the endpoint for entities of the LINE_ENTITY category.
 *	@param tep the endpoint of the line trace */
/*-------------------------------------------------------------------*/
	public void setTraceEndPos(Origin tep)
	{
		traceEndPos = tep;
	}

/*-------------------------------------------------------------------*/
/**	Set flags used for TE_STEAM entity.
 *	@param p2f the flag bitmask */
/*-------------------------------------------------------------------*/
	public void setPlat2Flags(long p2f)
	{
		plat2Flags = p2f;
	}

/*-------------------------------------------------------------------*/
/**	Set timeout value for the TE_STEAM entity, in milliseconds
 *	@param w the entity timeout */
/*-------------------------------------------------------------------*/
	public void setWait(long w)
	{
		wait = w;
	}

/*-------------------------------------------------------------------*/
/**	Set entity which spawned the TE_FLASHLIGHT entity
 *	@param fe the flashlight-spawning entity */
/*-------------------------------------------------------------------*/
	public void setFlashEntity(long fe)
	{
		flashEntity = fe;
	}

/*-------------------------------------------------------------------*/
/**	Set additional style value for TE_SPLASH entity.
 *	@param s the style value */
/*-------------------------------------------------------------------*/
	public void setStyle(long s)
	{
		style = s;
	}

/*-------------------------------------------------------------------*/
/**	Set next ID, a special value to chain multiple TE_STEAM effects.
 *	@param nID the next ID */
/*-------------------------------------------------------------------*/
	public void setNextID(long nID)
	{
		nextID = nID;
	}

/*-------------------------------------------------------------------*/
/**	Set count for multi-particle temporary entities.
 *	@param c the multi-particle temporary entity count */
/*-------------------------------------------------------------------*/
	public void setCount(long c)
	{
		count = c;
	}

/*-------------------------------------------------------------------*/
/**	Set additional type value for TE_WINDOWBEAMOUT multi-particle entity.
 *	@param t the additional type value */
/*-------------------------------------------------------------------*/
	public void setExtraType(long t)
	{
		type = t;
	}

/*-------------------------------------------------------------------*/
/**	Get entity type. One of the TE constants listed above.
 *	@return the entity type */
/*-------------------------------------------------------------------*/
	public int getType()
	{
		return entityType;
	}

/*-------------------------------------------------------------------*/
/**	Get entity category. Point, impact, line, special or bad, as defined
 *	by the constants above.
 *	@return the entity category */
/*-------------------------------------------------------------------*/
	public int getCategory()
	{
		return entityCategory;
	}

/*-------------------------------------------------------------------*/
/**	Get the entity which spawned the temporary entity.
 *	@return the spawning entity */
/*-------------------------------------------------------------------*/
	public long getSpawnEntity()
	{
		return entity;
	}

/*-------------------------------------------------------------------*/
/**	Get entity at which an effect ends.
 *	@return the destination entity */
/*-------------------------------------------------------------------*/
	public long getDestinationEntity()
	{
		return destEntity;
	}

/*-------------------------------------------------------------------*/
/**	Get temporary entity's origin.
 *	@return the entity origin */
/*-------------------------------------------------------------------*/
	public Origin getOrigin()
	{
		return origin;
	}

/*-------------------------------------------------------------------*/
/**	Get the temporary entity's destination.
 *	@return the destination origin */
/*-------------------------------------------------------------------*/
	public Origin getDestinationOrigin()
	{
		return destOrigin;
	}

/*-------------------------------------------------------------------*/
/**	Get the start of the TE_FLAME entity
 *	@return the entity start origin */
/*-------------------------------------------------------------------*/
	public Origin getStart()
	{
		return start;
	}

/*-------------------------------------------------------------------*/
/**	Get the direction in which the temporary entity is moving.
 *	@return the direction of movement */
/*-------------------------------------------------------------------*/
	public Vector3f getMoveDir()
	{
		return moveDir;
	}

/*-------------------------------------------------------------------*/
/**	Get additional positions for larger entites.
 *	@return array containing the extra position Origins */
/*-------------------------------------------------------------------*/
	public Origin[] getExtraPositions()
	{
		return new Origin[]{ pos1, pos2, pos3, pos4 };
	}

/*-------------------------------------------------------------------*/
/**	Get the endpoint for entities of the LINE_ENTITY category.
 *	@return the endpoint of the line trace */
/*-------------------------------------------------------------------*/
	public Origin getTraceEndPos()
	{
		return traceEndPos;
	}

/*-------------------------------------------------------------------*/
/**	Get flags used for TE_STEAM entity.
 *	@return the flag bitmask */
/*-------------------------------------------------------------------*/
	public long getPlat2Flags()
	{
		return plat2Flags;
	}

/*-------------------------------------------------------------------*/
/**	Get timeout value for the TE_STEAM entity, in milliseconds
 *	@return the entity timeout */
/*-------------------------------------------------------------------*/
	public long getWait()
	{
		return wait;
	}

/*-------------------------------------------------------------------*/
/**	Get entity which spawned the TE_FLASHLIGHT entity
 *	@return the flashlight-spawning entity */
/*-------------------------------------------------------------------*/
	public long getFlashEntity()
	{
		return flashEntity;
	}

/*-------------------------------------------------------------------*/
/**	Get additional style value for TE_SPLASH entity.
 *	@return the style value */
/*-------------------------------------------------------------------*/
	public long getStyle()
	{
		return style;
	}

/*-------------------------------------------------------------------*/
/**	Get next ID, a special value to chain multiple TE_STEAM effects.
 *	@return the next ID */
/*-------------------------------------------------------------------*/
	public long getNextID()
	{
		return nextID;
	}

/*-------------------------------------------------------------------*/
/**	Get count for multi-particle temporary entities.
 *	@return the multi-particle temporary entity count */
/*-------------------------------------------------------------------*/
	public long getCount()
	{
		return count;
	}

/*-------------------------------------------------------------------*/
/**	Get additional type value for TE_WINDOWBEAMOUT multi-particle entity.
 *	@return the additional type value */
/*-------------------------------------------------------------------*/
	public long getExtraType()
	{
		return type;
	}
}

