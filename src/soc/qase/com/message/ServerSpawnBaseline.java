//---------------------------------------------------------------------
// Name:			ServerSpawnBaseline.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.state.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Message wrapper used when signalling spawn baseline information
 *	from host to client. */
/*-------------------------------------------------------------------*/
public class ServerSpawnBaseline extends Message
{
	private Entity entity = null;
	private int offset = 0;
	private int bitmask = 0;
	private byte[] data = null;

	private static float PI = (float)3.1415926535;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source */
/*-------------------------------------------------------------------*/
	public ServerSpawnBaseline(byte[] data, int off)
	{
		offset = off;
		this.data = data;
		entity = new Entity();

		bitmask = processBitmask();

		entity.setNumber(processNumber());
		entity.setActive(processActive());
		entity.setModel(processModel());
		entity.setEffects(processEffects());
		entity.setOrigin(processOrigin());
		entity.setAngles(processAngles());
		entity.setOldOrigin(processOldOrigin());
		entity.setSound(processSound());
		entity.setEvents(processEvents());
		entity.setSolid(processSolid());

		setLength(offset - off);
	}

/*-------------------------------------------------------------------*/
/**	Get entity information.
 *	@return entity. */
/*-------------------------------------------------------------------*/
	public Entity getEntity()
	{
		return entity;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private int processBitmask()
	{
		int result = 0;

		result = Utils.intValue(data, offset);
		offset = offset + 4;
		return result;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private int processNumber()
	{
		int result = 0;

		if((bitmask & 0x00000100) != 0) {
			result = Utils.shortValue(data, offset);
			offset = offset + 2;
		}
		else {
			result = (int)data[offset];
			if(result < 0) result = result + 256;
			offset = offset + 1;
		}
		return result;
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
	private Model processModel()
	{
		Model result = null;
		int value = 0;

		result = new Model();

		// process model index
		if((bitmask & 0x00000800) != 0) {
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			result.setIndex(0, value);
			offset = offset + 1;
		}
		else result.setIndex(0, 0);
		if((bitmask & 0x00100000) != 0) {
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			result.setIndex(1, value);
			offset = offset + 1;
		}
		else result.setIndex(1, 0);
		if((bitmask & 0x00200000) != 0) {
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			result.setIndex(2, value);
			offset = offset + 1;
		}
		else result.setIndex(2, 0);
		if((bitmask & 0x00400000) != 0) {
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			result.setIndex(3, value);
			offset = offset + 1;
		}
		else result.setIndex(3, 0);

		// process model frame
		result.setFrame(0);
		if((bitmask & 0x00000010) != 0) {
			value = (int)data[offset];
			if(value < 0) value = value + 256;
			result.setFrame(value);
			offset = offset + 1;
		}
		if((bitmask & 0x00020000) != 0) {
			result.setFrame((int)(Utils.shortValue(data, offset)));
			offset = offset + 2;
		}

		// process model skin
		if((bitmask & 0x00010000) != 0) {
			if((bitmask & 0x02000000) != 0) {
				result.setSkin((int)(Utils.intValue(data, offset)));
				offset = offset + 4;
			}
			else {
				value = (int)data[offset];
				if(value < 0) value = value + 256;
				result.setSkin(value);
				offset = offset + 1;
			}
		}
		else if((bitmask & 0x02000000) != 0) {
			result.setSkin((int)(Utils.shortValue(data, offset)));
			offset = offset + 2;
		}
		return result;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Effects processEffects()
	{
		Effects result = null;
		int effect = 0;
		
		result = new Effects();
		
		// process entity effects
		if((bitmask & 0x00004000) != 0) {
			if((bitmask & 0x00080000) != 0) {
				result.setEffects((int)(Utils.intValue(data, offset)));
				offset = offset + 4;
			}
			else {
				effect = (int)data[offset];
				if(effect < 0) effect = effect + 256;
				result.setEffects(effect);
				offset = offset + 1;
			}
		}
		else if((bitmask & 0x00080000) != 0) {
			result.setEffects((int)(Utils.shortValue(data, offset)));
			offset = offset + 2;
		}		

		// process render effects
		if((bitmask & 0x00001000) != 0) {
			if((bitmask & 0x00040000) != 0) {
				result.setRenderEffects((int)(Utils.intValue(data, offset)));
				offset = offset + 4;
			}
			else {
				effect = (int)data[offset];
				if(effect < 0) effect = effect + 256;
				result.setRenderEffects(effect);
				offset = offset + 1;
			}
		}
		else if((bitmask & 0x00040000) != 0) {
			result.setRenderEffects((int)(Utils.shortValue(data, offset)));
			offset = offset + 2;
		}
		else result.setRenderEffects(0);
		return result;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Origin processOrigin()
	{
		Origin result = null;
		int x = 0;
		int y = 0;
		int z = 0;

		result = new Origin();

		// process origin 
		if((bitmask & 0x00000001) != 0) {
			x = (int)(Utils.shortValue(data, offset));
			x = (int)(0.125 * x);
			result.setX(x);
			offset = offset + 2;
		}
		else result.setX(0);
		if((bitmask & 0x00000002) != 0) {
			y = (int)(Utils.shortValue(data, offset));
			y = (int)(0.125 * y);
			result.setY(y);
			offset = offset + 2;
		}
		else result.setY(0);
		if((bitmask & 0x00000200) != 0) {
			z = (int)(Utils.shortValue(data, offset));
			z = (int)(0.125 * z);
			result.setZ(z);
			offset = offset + 2;
		}
		else result.setZ(0);
		return result;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Angles processAngles()
	{
		Angles result = null;
		int angle = 0;

		result = new Angles();

		// process angles 
		if((bitmask & 0x00000400) != 0) {
			angle = (int)data[offset];
			if(angle < 0) angle = angle + 256;
			result.setPitch((int)(PI / 128.0 * (float)angle));
			offset = offset + 1;
		}
		else result.setPitch(0);
		if((bitmask & 0x00000004) != 0) {
			angle = (int)data[offset];
			if(angle < 0) angle = angle + 256;
			result.setYaw((int)(PI / 128.0 * (float)angle));
			offset = offset + 1;
		}
		else result.setYaw(0);
		if((bitmask & 0x00000008) != 0) {
			angle = (int)data[offset];
			if(angle < 0) angle = angle + 256;
			result.setRoll((int)(PI / 128.0 * (float)angle));
			offset = offset + 1;
		}
		else result.setRoll(0);
		return result;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Origin processOldOrigin()
	{
		Origin result = null;
		int x = 0;
		int y = 0;
		int z = 0;

		result = new Origin();

		// process old origin 
		if((bitmask & 0x01000000) != 0) {
			x = (int)(Utils.shortValue(data, offset));
			x = (int)(0.125 * x);
			result.setX(x);
			offset = offset + 2;
			y = (int)(Utils.shortValue(data, offset));
			y = (int)(0.125 * y);
			result.setY(y);
			offset = offset + 2;
			z = (int)(Utils.shortValue(data, offset));
			z = (int)(0.125 * z);
			result.setZ(z);
			offset = offset + 2;
		}
		return result;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Sound processSound()
	{
		Sound result = null;
		int sound = 0;

		result = new Sound();

		// process sound 
		if((bitmask & 0x04000000) != 0) {
			sound = (int)data[offset];
			if(sound < 0) sound = sound + 256;
			result.setLoop(sound);
			offset = offset + 1;
		}
		return result;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Events processEvents()
	{
		Events result = null;
		int event = 0;

		result = new Events();

		// process events 
		if((bitmask & 0x00000020) != 0) {
			event = (int)data[offset];
			if(event < 0) event = event + 256;
			result.setEvents(event);
			offset = offset + 1;
		}
		return result;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Solid processSolid()
	{
		Solid result = null;

		result = new Solid();

		// process solid 
		if((bitmask & 0x08000000) != 0) {
			result.setSolid((int)(Utils.shortValue(data, offset)));
			offset = offset + 2;
		}
		return result;
	}

}


