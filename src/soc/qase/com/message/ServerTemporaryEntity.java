//---------------------------------------------------------------------
// Name:			ServerTemporaryEntity.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.Arrays;
import soc.qase.tools.Utils;
import soc.qase.state.Origin;
import soc.qase.tools.vecmath.*;
import soc.qase.state.TemporaryEntity;

/*-------------------------------------------------------------------*/
/**	Wrapper for temporary entity messages - a projectile, blood, weapon trails,
 *	etc. Temporary entity events are for things that happen at a location
 *	seperate from existing entity. Temporary entity messages are explicitly
 *	constructed and broadcast. */
/*-------------------------------------------------------------------*/
public class ServerTemporaryEntity extends Message
{
	private TemporaryEntity tempEnt = null;
	private int entityType = -1, entityCategory = -1;

	private int offset = 0;
	private byte[] data = null;
	private int serverVersion = 34;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source */
/*-------------------------------------------------------------------*/
	public ServerTemporaryEntity(byte[] data, int off)
	{
		offset = off;
		this.data = data;
		this.serverVersion = serverVersion;

		tempEnt = new TemporaryEntity();

		entityType = processEntityType();
		entityCategory = getEntityCategory();

		tempEnt.setType(entityType);
		tempEnt.setCategory(entityCategory);

		if(entityCategory == TemporaryEntity.POINT_ENTITY)
			processPointEntity();
		else if(entityCategory == TemporaryEntity.IMPACT_ENTITY)
			processImpactEntity();
		else if(entityCategory == TemporaryEntity.LINE_ENTITY)
			processLineEntity();
		else if(entityCategory == TemporaryEntity.SPECIAL_ENTITY)
			processSpecialEntity();
		else if(entityCategory == TemporaryEntity.BAD_ENTITY)
			processBadEntity();

		setLength(offset - off);
	}

	public TemporaryEntity getTemporaryEntity()
	{
		return tempEnt;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private int processEntityType()
	{
		return data[offset++];
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private int getEntityCategory()
	{
		if(entityType == TemporaryEntity.TE_GREENBLOOD_NEW)
		{
			if(serverVersion >= 32)
				return TemporaryEntity.IMPACT_ENTITY;
			else
				return TemporaryEntity.LINE_ENTITY;
		}

		if(entityType == TemporaryEntity.TE_BLUEHYPERBLASTER)
		{
			if(serverVersion >= 32)
				return TemporaryEntity.LINE_ENTITY;
			else
				return TemporaryEntity.IMPACT_ENTITY;
		}

		if(Arrays.binarySearch(TemporaryEntity.POINT_INDICES, entityType) >= 0)
			return TemporaryEntity.POINT_ENTITY;
		else if(Arrays.binarySearch(TemporaryEntity.IMPACT_INDICES, entityType) >= 0)
			return TemporaryEntity.IMPACT_ENTITY;
		else if(Arrays.binarySearch(TemporaryEntity.LINE_INDICES, entityType) >= 0)
			return TemporaryEntity.LINE_ENTITY;
		else if(Arrays.binarySearch(TemporaryEntity.SPECIAL_INDICES, entityType) >= 0)
			return TemporaryEntity.SPECIAL_ENTITY;
		else
			return TemporaryEntity.BAD_ENTITY;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void processPointEntity()
	{
		tempEnt.setOrigin(readPosition());
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void processImpactEntity()
	{
		tempEnt.setOrigin(readPosition());
		tempEnt.setMoveDir(readDirection());
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void processLineEntity()
	{
		tempEnt.setOrigin(readPosition());
		tempEnt.setTraceEndPos(readPosition());
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void processSpecialEntity()
	{
		if(entityType == TemporaryEntity.TE_SPLASH || entityType == TemporaryEntity.TE_LASER_SPARKS || entityType == TemporaryEntity.TE_WELDING_SPARKS || entityType == TemporaryEntity.TE_TUNNEL_SPARKS)
		{
			tempEnt.setCount(data[offset++]);
			tempEnt.setOrigin(readPosition());
			tempEnt.setMoveDir(readDirection());
			tempEnt.setStyle(data[offset++]);
		}
		else if(entityType == TemporaryEntity.TE_PARASITE_ATTACK || entityType == TemporaryEntity.TE_MEDIC_CABLE_ATTACK || entityType == TemporaryEntity.TE_HEATBEAM || entityType == TemporaryEntity.TE_MONSTER_HEATBEAM)
		{
			tempEnt.setSpawnEntity(Utils.shortValue(data, offset));
			offset += 2;

			tempEnt.setOrigin(readPosition());
			tempEnt.setTraceEndPos(readPosition());
		}
		else if(entityType == TemporaryEntity.TE_GRAPPLE_CABLE)
		{
			tempEnt.setSpawnEntity(Utils.shortValue(data, offset));
			offset += 2;

			tempEnt.setOrigin(readPosition());
			tempEnt.setTraceEndPos(readPosition());
			tempEnt.setExtraPositions(readPosition(), null, null, null);
		}
		else if(entityType == TemporaryEntity.TE_FLAME)
		{
			tempEnt.setSpawnEntity(Utils.shortValue(data, offset));
			offset += 2;

			tempEnt.setCount(Utils.shortValue(data, offset));
			offset += 2;

			tempEnt.setStart(readPosition());
			tempEnt.setOrigin(readPosition());
			tempEnt.setExtraPositions(readPosition(), readPosition(), readPosition(), readPosition());
		}
		else if(entityType == TemporaryEntity.TE_LIGHTNING)
		{
			tempEnt.setDestinationEntity(Utils.shortValue(data, offset));
			offset += 2;

			tempEnt.setSpawnEntity(Utils.shortValue(data, offset));
			offset += 2;

			tempEnt.setDestinationOrigin(readPosition());
			tempEnt.setOrigin(readPosition());
		}
		else if(entityType == TemporaryEntity.TE_FLASHLIGHT)
		{
			tempEnt.setOrigin(readPosition());
			tempEnt.setFlashEntity(Utils.shortValue(data, offset));
			offset += 2;
		}
		else if(entityType == TemporaryEntity.TE_FORCEWALL)
		{
			tempEnt.setOrigin(readPosition());
			tempEnt.setTraceEndPos(readPosition());
			tempEnt.setStyle(Utils.shortValue(data, offset));
			offset += 2;
		}
		else if(entityType == TemporaryEntity.TE_STEAM)
		{
			tempEnt.setNextID(Utils.shortValue(data, offset));
			offset += 2;

			tempEnt.setCount(data[offset++]);
			tempEnt.setOrigin(readPosition());
			tempEnt.setMoveDir(readDirection());
			tempEnt.setStyle(data[offset++]);
			tempEnt.setPlat2Flags(Utils.shortValue(data, offset));
			offset += 2;

			if(tempEnt.getNextID() != -1)
			{
				tempEnt.setWait(Utils.longValue(data, offset));
				offset += 8;
			}
		}
		else if(entityType == TemporaryEntity.TE_WIDOWBEAMOUT)
		{
			tempEnt.setExtraType(Utils.shortValue(data, offset));
			offset += 2;

			tempEnt.setOrigin(readPosition());
		}
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void processBadEntity()
	{
		System.out.println("Bad Temporary Entity Received.");
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Origin readPosition()
	{
		short x = Utils.shortValue(data, offset);
		short y = Utils.shortValue(data, offset + 2);
		short z = Utils.shortValue(data, offset + 4);

		offset += 6;
		return new Origin((int)(0.125 * x), (int)(0.125 * y), (int)(0.125 * z));
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Vector3f readDirection()
	{
		int code = data[offset++] & 0xFF;
		return new Vector3f(TemporaryEntity.VERTEXNORMS[code][0], TemporaryEntity.VERTEXNORMS[code][1], TemporaryEntity.VERTEXNORMS[code][2]);
	}
}
