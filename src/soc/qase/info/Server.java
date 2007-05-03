//---------------------------------------------------------------------
// Name:			Server.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.info;

import soc.qase.com.message.*;

/*-------------------------------------------------------------------*/
/**	Server information wrapper. */
/*-------------------------------------------------------------------*/
public class Server
{
	private int serverVersion = 0;
	private int levelKey = 0;
	private int isDemo = 0;
	private int clientEntity = 0;
	private String gameDirectory = null;
	private String mapName = null;

	public static final int CTF_RED = 0;
	public static final int CTF_BLUE = 1;
	public static final int CTF_RANDOM = 2;
	public static final String[] CTF_STRINGS = {"RED", "BLUE"};

/*-------------------------------------------------------------------*/
/**	Constructor.
 *	@param data server data source, consisting of version, level key,
 *	demo, client entity, game directoy and map name information */
/*-------------------------------------------------------------------*/
	public Server(ServerData data)
	{
		serverVersion = data.getServerVersion();
		levelKey = data.getLevelKey();
		isDemo = data.getIsDemo();
		clientEntity = data.getClientEntity();
		gameDirectory = data.getGameDirectory();
		mapName = data.getMapName();
	}
	
/*-------------------------------------------------------------------*/
/**	Get server version.
 *	@return server version. */
/*-------------------------------------------------------------------*/
	public int getServerVersion() 
	{
		return serverVersion;
	}

/*-------------------------------------------------------------------*/
/**	Get level key.
 *	@return level key. */
/*-------------------------------------------------------------------*/
	public int getLevelKey() 
	{
		return levelKey;
	}

/*-------------------------------------------------------------------*/
/**	Get isDemo.
 *	@return 1 if demo, otherwise 0. */
/*-------------------------------------------------------------------*/
	public boolean isDemo()
	{
		return (isDemo == 1);
	}

/*-------------------------------------------------------------------*/
/**	Get client entity number.
 *	@return client entity number. */
/*-------------------------------------------------------------------*/
	public int getClientEntity() 
	{
		return clientEntity;
	}

/*-------------------------------------------------------------------*/
/**	Get game directory.
 *	@return game directory. */
/*-------------------------------------------------------------------*/
	public String getGameDirectory() 
	{
		return gameDirectory;
	}

/*-------------------------------------------------------------------*/
/**	Determine whether the current server is running CTF.
 *	@return true if the server is running CTF, false otherwise. */
/*-------------------------------------------------------------------*/
	public boolean isCTFServer() 
	{
		return gameDirectory.equals("ctf");
	}

/*-------------------------------------------------------------------*/
/**	Get map name.
 *	@return map name. */
/*-------------------------------------------------------------------*/
	public String getMapName() 
	{
		return mapName;
	}

/*-------------------------------------------------------------------*/
/**	Get String representation of this object.
 *	@return String representation of this object. */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		String result = null;

		result = new String("\\");
		result += "serverVersion" + "\\" + serverVersion + "\\";
		result += "levelKey" + "\\" + levelKey + "\\";
		result += "isDemo" + "\\" + isDemo + "\\";
		result += "clientEntity" + "\\" + clientEntity + "\\";
		result += "gameDirectory" + "\\" + gameDirectory + "\\";
		result += "mapName" + "\\" + mapName;
		return result;
	}
}
