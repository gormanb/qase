//---------------------------------------------------------------------
// Name:			Sound.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

/*-------------------------------------------------------------------*/
/**	Wrapper class for sound attributes. */
/*-------------------------------------------------------------------*/
public class Sound
{
	private int index = -1;
	private float volume = -1;
	private int attenuation = -1;
	private float timeOffset = -1;
	private int entityNumber = -1;
	private int soundChannel = -1;
	private Origin origin = null;
	private int loop = -1;

/*-------------------------------------------------------------------*/
/**	Constructor. */
/*-------------------------------------------------------------------*/
	public Sound()
	{	}
	
/*-------------------------------------------------------------------*/
/**	Parameterised constructor.
 *	@param loop indicates whether the sound should play on a continual loop. */
/*-------------------------------------------------------------------*/
	public Sound(int loop)
	{
		this.loop = loop;
	}
	
/*-------------------------------------------------------------------*/
/**	Get sound index.
 *	@return sound index. */
/*-------------------------------------------------------------------*/
	public int getIndex()
	{
		return index;
	}

/*-------------------------------------------------------------------*/
/**	Get sound index in the config string table.
 *	@return sound config index */
/*-------------------------------------------------------------------*/
	public int getConfigIndex()
	{
		return index + 288;
	}

/*-------------------------------------------------------------------*/
/**	Set sound index.
 *	@param index sound index. */
/*-------------------------------------------------------------------*/
	public void setIndex(int index)
	{
		this.index = index;
	}

/*-------------------------------------------------------------------*/
/**	Get sound volume.
 *	@return sound volume. */
/*-------------------------------------------------------------------*/
	public float getVolume()
	{
		return volume;
	}

/*-------------------------------------------------------------------*/
/**	Set sound volume.
 *	@param volume sound volume. */
/*-------------------------------------------------------------------*/
	public void setVolume(float volume)
	{
		this.volume = volume;
	}

/*-------------------------------------------------------------------*/
/**	Get sound attenuation. The value can range from between 0 and 3
 *	@return sound attenuation. */
/*-------------------------------------------------------------------*/
	public int getAttenuation()
	{
		return attenuation;
	}

/*-------------------------------------------------------------------*/
/**	Get sound attenuation string. The string can assume one of four
 *	possible values: "none", "normal", "idle", or "static".
 *	@return sound attenuation string. */
/*-------------------------------------------------------------------*/
	public String getAttenuationString()
	{
		String result = null;

		result = new String();
		if(attenuation == 0) result += "none";
		else if(attenuation == 1) result += "normal";
		else if(attenuation == 2) result += "idle";
		else if(attenuation == 3) result += "static";
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Set sound attenuation.
 *	@param attenuation sound attenuation. */
/*-------------------------------------------------------------------*/
	public void setAttenuation(int attenuation)
	{
		this.attenuation = attenuation;
	}

/*-------------------------------------------------------------------*/
/**	Get sound time offset.
 *	@return sound time offset. */
/*-------------------------------------------------------------------*/
	public float getTimeOffset()
	{
		return timeOffset;
	}

/*-------------------------------------------------------------------*/
/**	Set sound time offset.
 *	@param timeOffset sound time offset. */
/*-------------------------------------------------------------------*/
	public void setTimeOffset(float timeOffset)
	{
		this.timeOffset = timeOffset;
	}

/*-------------------------------------------------------------------*/
/**	Get sound loop value.
 *	@return sound loop value. */
/*-------------------------------------------------------------------*/
	public int getLoop()
	{
		return loop;
	}

/*-------------------------------------------------------------------*/
/**	Set sound loop value.
 *	@param loop sound loop value. */
/*-------------------------------------------------------------------*/
	public void setLoop(int loop)
	{
		this.loop = loop;
	}

/*-------------------------------------------------------------------*/
/**	Get sound entity number.
 *	@return sound entity number. */
/*-------------------------------------------------------------------*/
	public int getEntityNumber()
	{
		return entityNumber;
	}

/*-------------------------------------------------------------------*/
/**	Set sound entity number.
 *	@param entityNumber sound entity number. */
/*-------------------------------------------------------------------*/
	public void setEntityNumber(int entityNumber)
	{
		this.entityNumber = entityNumber;
	}

/*-------------------------------------------------------------------*/
/**	Get sound channel. Can assume a value between 0 and 4.
 *	@return sound channel. */
/*-------------------------------------------------------------------*/
	public int getSoundChannel()
	{
		return soundChannel;
	}

/*-------------------------------------------------------------------*/
/**	Get sound channel value as string. Can assume one of five
 *	possible strings: "auto", "weapon", "voice", "item", or "body".
 *	@return sound channel value as string. */
/*-------------------------------------------------------------------*/
	public String getSoundChannelString()
	{
		String result = null;

		result = new String();
		if(soundChannel == 0) result += "auto";
		else if(soundChannel == 1) result += "weapon";
		else if(soundChannel == 2) result += "voice";
		else if(soundChannel == 3) result += "item";
		else if(soundChannel == 4) result += "body";
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Set sound channel.
 *	@param soundChannel sound channel. */
/*-------------------------------------------------------------------*/
	public void setSoundChannel(int soundChannel)
	{
		this.soundChannel = soundChannel;
	}

/*-------------------------------------------------------------------*/
/**	Get sound origin.
 *	@return sound origin. */
/*-------------------------------------------------------------------*/
	public Origin getOrigin()
	{
		return origin;
	}

/*-------------------------------------------------------------------*/
/**	Set sound origin.
 *	@param origin sound origin. */
/*-------------------------------------------------------------------*/
	public void setOrigin(Origin origin)
	{
		this.origin = origin;
	}

/*-------------------------------------------------------------------*/
/**	Merge sound properties.
 *	@param sound source. */
/*-------------------------------------------------------------------*/
	public void merge(Sound sound)
	{
		if(sound != null) {
			if(index == -1) index = sound.getIndex();
			if(volume == -1) volume = sound.getVolume();
			if(attenuation == -1) attenuation = sound.getAttenuation();
			if(timeOffset == -1) timeOffset = sound.getTimeOffset();
			if(entityNumber == -1) entityNumber = sound.getEntityNumber();
			if(soundChannel == -1) soundChannel = sound.getSoundChannel();
			if(origin == null) origin = sound.origin; else origin.merge(sound.origin);
			if(loop == -1) loop = sound.getLoop();
		}
	}

/*-------------------------------------------------------------------*/
/**	Creates a duplicate of the object by cloning each of its fields.
 *	@return a deep copy of the current object */
/*-------------------------------------------------------------------*/
	public Sound deepCopy()
	{
		Sound snd = new Sound();

		snd.setIndex(index);
		snd.setVolume(volume);
		snd.setAttenuation(attenuation);
		snd.setTimeOffset(timeOffset);
		snd.setEntityNumber(entityNumber);
		snd.setSoundChannel(soundChannel);
		snd.setOrigin((origin == null ? null : origin.deepCopy()));
		snd.setLoop(loop);

		return snd;
	}
}
