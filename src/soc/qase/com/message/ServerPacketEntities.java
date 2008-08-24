//---------------------------------------------------------------------
// Name:			ServerPacketEntities.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import soc.qase.state.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Message wrapper used when signalling delta packet information
 *	from host to client. */
/*-------------------------------------------------------------------*/
public class ServerPacketEntities extends Message
{
	private Vector entities = null;
	private Entity entity = null;
	private int offset = 0;
	private long bitmask = 0;
	private byte[] data = null;

	private static float PI = (float)3.1415926535;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source. */
/*-------------------------------------------------------------------*/
	public ServerPacketEntities(byte[] data, int off)
	{
		offset = off;
		int number = 0;

		this.data = data;
		entities = new Vector();

		try
		{
			while(true)
			{
				entity = new Entity();
				bitmask = processBitmask();
				entity.setNumber(processNumber());

				if(entity.getNumber() == 0)
					break;

				entity.setActive(processActive());
				entity.setModel(processModel());
				entity.setEffects(processEffects());
				entity.setOrigin(processOrigin());
				entity.setAngles(processAngles());
				entity.setOldOrigin(processOldOrigin());
				entity.setSound(processSound());
				entity.setEvents(processEvents());
				entity.setSolid(processSolid());

				if(entity.getNumber() < 1024 && entity.getNumber() > 0)
					entities.addElement(entity);
			}
		}
		catch(Exception e)
		{
			offset = data.length;
		}

		setLength(offset - off);
	}

/*-------------------------------------------------------------------*/
/**	Get entities information.
 *	@return entities. */
/*-------------------------------------------------------------------*/
	public Vector getEntities()
	{
		return entities;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private long processBitmask()
	{
		long mask = 0;

		mask=(0xFF & ((int)data[offset++]));
		if((mask&0x00000080) != 0) mask|=((0xFF & ((int)data[offset++]))<<8);
		if((mask&0x00008000) != 0) mask|=((0xFF & ((int)data[offset++]))<<16);
		if((mask&0x00800000) != 0) mask|=((0xFF & ((int)data[offset++]))<<24);

		return mask & 0xFFFFFFFFL;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private boolean processActive()
	{
		boolean result = true;

		if((bitmask & 0x00000040) != 0) result = false;
		return result;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private int processNumber()
	{
		int result = 0;

		if((bitmask & 0x00000100) != 0)
		{
			result = Utils.shortValue(data, offset);
			offset = offset + 2;
		}
		else
		{
			result = (int)data[offset];
			if(result < 0) result = result + 256;
			offset = offset + 1;
		}

		return result;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Model processModel()
	{
		int value = 0;

		int mFrame = -1; long mSkin = -1;
		int[] mIndexVal = new int[]{ -1, -1, -1, -1};

		int prevOff = offset;

		// process model index
		if((bitmask & 0x00000800) != 0)
		{
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			mIndexVal[0] = value;
			offset = offset + 1;
		}
		//else result.setIndex(0, 0);
		if((bitmask & 0x00100000) != 0)
		{	
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			mIndexVal[1] = value;
			offset = offset + 1;
		}
		//else result.setIndex(1, 0);
		if((bitmask & 0x00200000) != 0)
		{
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			mIndexVal[2] = value;
			offset = offset + 1;
		}
		//else result.setIndex(2, 0);
		if((bitmask & 0x00400000) != 0)
		{
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			mIndexVal[3] = value;
			offset = offset + 1;
		}
		//else result.setIndex(3, 0);

		// process model frame
		if((bitmask & 0x00000010) != 0)
		{
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			mFrame = value;
			offset = offset + 1;
		}

		if((bitmask & 0x00020000) != 0)
		{
			mFrame = (int)(Utils.shortValue(data, offset));
			offset = offset + 2;
		}

		// process model skin
		if((bitmask & 0x00010000) != 0)
		{
			if((bitmask & 0x02000000) != 0)
			{
				mSkin = Utils.unsignedIntValue(data, offset);
				offset = offset + 4;
			}
			else
			{
				value = (int)data[offset];
				if(value < 0) value = value + 256;
				mSkin = value;
				offset = offset + 1;
			}
		}
		else if((bitmask & 0x02000000) != 0)
		{
			mSkin = Utils.shortValue(data, offset);
			offset = offset + 2;
		}

		return (offset > prevOff ? new Model(mIndexVal, mFrame, mSkin) : null);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Effects processEffects()
	{
		int effect = -1;

		int nEffect = -1;
		int rEffect = -1;

		int prevOff = offset;
		
		// process entity effects
		if((bitmask & 0x00004000) != 0)
		{
			if((bitmask & 0x00080000) != 0)
			{
				nEffect = (int)(Utils.intValue(data, offset));
				offset = offset + 4;
			}
			else
			{
				effect = (int)data[offset];
				if(effect < 0) effect = effect + 256;
				nEffect = effect;
				offset = offset + 1;
			}
		}
		else if((bitmask & 0x00080000) != 0)
		{
			nEffect = (int)(Utils.shortValue(data, offset));
			offset = offset + 2;
		}		

		// process render effects
		if((bitmask & 0x00001000) != 0)
		{
			if((bitmask & 0x00040000) != 0)
			{
				rEffect = (int)(Utils.intValue(data, offset));
				offset = offset + 4;
			}
			else
			{
				effect = (int)data[offset];
				if(effect < 0) effect = effect + 256;
				rEffect = effect;
				offset = offset + 1;
			}
		}
		else if((bitmask & 0x00040000) != 0)
		{
			rEffect = (int)(Utils.shortValue(data, offset));
			offset = offset + 2;
		}

		return (offset > prevOff ? new Effects(nEffect, rEffect) : null);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Origin processOrigin()
	{
		int x = -1;
		int y = -1;
		int z = -1;

		int prevOff = offset;

		// process origin 
		if((bitmask & 0x00000001) != 0)
		{
			x = (int)(Utils.shortValue(data, offset));
			x = (int)(0.125 * x);
			offset = offset + 2;
		}
		//else result.setX(0);
		if((bitmask & 0x00000002) != 0)
		{
			y = (int)(Utils.shortValue(data, offset));
			y = (int)(0.125 * y);
			offset = offset + 2;
		}
		//else result.setY(0);
		if((bitmask & 0x00000200) != 0)
		{
			z = (int)(Utils.shortValue(data, offset));
			z = (int)(0.125 * z);
			offset = offset + 2;
		}
		//else result.setZ(0);

		return (offset > prevOff ? new Origin(x, y, z) : null);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Angles processAngles()
	{
		int angle = -1;

		int yaw = -1;
		int roll = -1;
		int pitch = -1;

		int prevOff = offset;

		// process angles 
		if((bitmask & 0x00000004) != 0)
		{
			angle = data[offset];
			if(angle < 0) angle = angle + 256;
			pitch = (int)(PI / 128.0 * (float)angle);
			offset = offset + 1;
		}
//		else result.setPitch(0);
		if((bitmask & 0x00000400) != 0)
		{
			angle = data[offset];
			if(angle < 0) angle = angle + 256;
			yaw = (int)(PI / 128.0 * (float)angle);
			offset = offset + 1;
		}
//		else result.setYaw(0);
		if((bitmask & 0x00000008) != 0)
		{
			angle = data[offset];
			if(angle < 0) angle = angle + 256;
			roll = (int)(PI / 128.0 * (float)angle);
			offset = offset + 1;
		}
//		else result.setRoll(0);
		return (offset > prevOff ? new Angles(pitch, yaw, roll) : null);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Origin processOldOrigin()
	{
		int x = -1;
		int y = -1;
		int z = -1;

		int prevOff = offset;

		// process old origin 
		if((bitmask & 0x01000000) != 0)
		{
			x = (int)(Utils.shortValue(data, offset));
			x = (int)(0.125 * x);

			offset = offset + 2;
			y = (int)(Utils.shortValue(data, offset));
			y = (int)(0.125 * y);

			offset = offset + 2;
			z = (int)(Utils.shortValue(data, offset));
			z = (int)(0.125 * z);

			offset = offset + 2;
		}

		return (offset > prevOff ? new Origin(x, y, z) : null);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Sound processSound()
	{
		int sound = -1;
		int prevOff = offset;

		// process sound 
		if((bitmask & 0x04000000) != 0)
		{
			sound = (int)data[offset];
			if(sound < 0) sound = sound + 256;
			offset = offset + 1;
		}

		return (offset > prevOff ? new Sound(sound) : null);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Events processEvents()
	{
		int event = -1;
		int prevOff = offset;

		// process events 
		if((bitmask & 0x00000020) != 0)
		{
			event = (int)data[offset];
			if(event < 0) event = event + 256;
			offset = offset + 1;
		}

		return (offset > prevOff ? new Events(event) : null);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Solid processSolid()
	{
		int solid = -1;
		int prevOff = offset;

		// process solid 
		if((bitmask & 0x08000000) != 0)
		{
			solid = (int)(Utils.shortValue(data, offset));
			offset = offset + 2;
		}

		return (offset > prevOff ? new Solid(solid) : null);
	}
}

