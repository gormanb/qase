//---------------------------------------------------------------------
// Name:			User.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.info;

/*-------------------------------------------------------------------*/
/**	User information wrapper. The User class is employed when an agent
 *	connects to the simulator environment using a Proxy object, supplying
 *	the environment with basic information required by a Quake2 server. */
/*-------------------------------------------------------------------*/
public class User
{
	private String name = null;
	private String skin = null;
	private int rate = 0;
	private int message = 0;
	private int fov = 0;
	private int hand = 0;
	private String password = null;

	public static final int HAND_RIGHT = 0, HAND_LEFT = 1, HAND_CENTER = 2;

/*-------------------------------------------------------------------*/
/**	Constructor. Records the user's specified options.
 *	@param name the name by which the agent will be known by other
 *	agents interacting with the environment.
 *	@param skin the rendering model to be used by an agent; it defines
 *	in what way another agent, making use of environment visualization,
 *	will perceive the agent.
 *	@param rate defines the data receive rate of a Quake2 client and is
 *	expressed in terms of possible number of bytes per second; the value
 *	can range between 0 and 65535 bps
 *	@param message user message level; defines the type of messages in
 *	which the agent registers an interest
 *	@param fov describes the current size of a Quake2 client's view space,
 *	in degrees; as with all angles in the QASE API, this value can range
 *	between 0 and 360
 *	@param hand agents interacting with the physical environment simulated
 *	by a Quake2 server are able to hold certain weapons in their hands;
 *	this hand parameter defines the default hand in which to hold a weapon,
 *	and should be one of the HAND constants listed above
 *	@param password the Quake2 server component features a limited support
 *	for security, and as such may optionally define a password to be used
 *	by connecting parties */
/*-------------------------------------------------------------------*/
	public User(String name, String skin, int rate, int message, int fov, int hand, String password)
	{
		setName(name);
		setSkin(skin);
		setRate(rate);
		setMessage(message);
		setFov(fov);
		setHand(hand);
		setPassword(password);
	}
	
/*-------------------------------------------------------------------*/
/**	Set user name.
 *	@param name user name. */
/*-------------------------------------------------------------------*/
	public void setName(String name)
	{
		this.name = name;
	}

/*-------------------------------------------------------------------*/
/**	Set user skin.
 *	@param skin user skin. */
/*-------------------------------------------------------------------*/
	public void setSkin(String skin)
	{
		this.skin = skin;
	}


/*-------------------------------------------------------------------*/
/**	Set data receive rate.
 *	@param rate data receive rate. */
/*-------------------------------------------------------------------*/
	public void setRate(int rate)
	{
		this.rate = rate;
	}

/*-------------------------------------------------------------------*/
/**	Set user message level. Defines what kind of messages the client
 *	is interested in.
 *	@param message user message level. */
/*-------------------------------------------------------------------*/
	public void setMessage(int message)
	{
		this.message = message;
	}

/*-------------------------------------------------------------------*/
/**	Set user field of view.
 *	@param fov user field of view. */
/*-------------------------------------------------------------------*/
	public void setFov(int fov)
	{
		this.fov = fov;
	}

/*-------------------------------------------------------------------*/
/**	Set user hand. 0 for right, 1 for left, 2 for centre (invisible)
 *	@param hand user hand. */
/*-------------------------------------------------------------------*/
	public void setHand(int hand)
	{
		this.hand = hand;
	}

/*-------------------------------------------------------------------*/
/**	Set host password.
 *	@param password host password. */
/*-------------------------------------------------------------------*/
	public void setPassword(String password)
	{
		this.password = password;
	}

/*-------------------------------------------------------------------*/
/**	Get user name.
 *	@return user name. */
/*-------------------------------------------------------------------*/
	public String getName() 
	{
		return name;
	}

/*-------------------------------------------------------------------*/
/**	Get user skin.
 *	@return user skin. */
/*-------------------------------------------------------------------*/
	public String getSkin() 
	{
		return skin;
	}

/*-------------------------------------------------------------------*/
/**	Get data receive rate.
 *	@return data receive rate. */
/*-------------------------------------------------------------------*/
	public int getRate() 
	{
		return rate;
	}

/*-------------------------------------------------------------------*/
/**	Get user message level.
 *	@return user message level. */
/*-------------------------------------------------------------------*/
	public int getMessage() 
	{
		return message;
	}

/*-------------------------------------------------------------------*/
/**	Get user field of view.
 *	@return user field of view. */
/*-------------------------------------------------------------------*/
	public int getFov() 
	{
		return fov;
	}

/*-------------------------------------------------------------------*/
/**	Get user hand.
 *	@return user hand. */
/*-------------------------------------------------------------------*/
	public int getHand() 
	{
		return hand;
	}

/*-------------------------------------------------------------------*/
/**	Get host password.
 *	@return host password. */
/*-------------------------------------------------------------------*/
	public String getPassword() 
	{
		return password;
	}

/*-------------------------------------------------------------------*/
/**	Get String representation of this object.
 *	@return String representation of this object. */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		String result = null;

		result = new String("\\");
		result += "rate" + "\\" + rate + "\\";
		result += "msg" + "\\" + message + "\\";
		result += "fov" + "\\" + fov + "\\";
		result += "skin" + "\\" + skin + "\\";
		result += "name" + "\\" + name + "\\";
		result += "hand" + "\\" + hand;
		return result;
	}
}
