//---------------------------------------------------------------------
// Name:			ServerData.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Message wrapper used when signalling server data from host
 *	to client. */
/*-------------------------------------------------------------------*/
public class ServerData extends Message
{
	private int server_version = 0;
	private int level_key = 0;
	private int is_demo = 0;
	private int client_entity = 0;
	private String game_directory = null;
	private String map_name = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source. */
/*-------------------------------------------------------------------*/
	public ServerData(byte[] data, int off)
	{
		int str1_length = 0;
		int str2_length = 0;

		server_version = Utils.intValue(data, off);
		level_key = Utils.intValue(data, off + 4);
		is_demo = (int)data[off + 8];

		str1_length = Utils.stringLength(data, off + 9);
		game_directory = Utils.stringValue(data, off + 9, str1_length);

		client_entity = Utils.shortValue(data, off + 10 + str1_length);
		str2_length = Utils.stringLength(data, off + 12 + str1_length);
		map_name = Utils.stringValue(data, off + 12 + str1_length, str2_length);

		setLength(11 + str1_length + str2_length + 2);
	}

/*-------------------------------------------------------------------*/
/**	Get server version.
 *	@return server version. */
/*-------------------------------------------------------------------*/
	public int getServerVersion()
	{
		return server_version;
	}

/*-------------------------------------------------------------------*/
/**	Get level key.
 *	@return level key. */
/*-------------------------------------------------------------------*/
	public int getLevelKey()
	{
		return level_key;
	}

/*-------------------------------------------------------------------*/
/**	Get isDemo.
 *	@return 1 if demo, otherwise 0. */
/*-------------------------------------------------------------------*/
	public int getIsDemo()
	{
		return is_demo;
	}

/*-------------------------------------------------------------------*/
/**	Get game directory.
 *	@return game directory. */
/*-------------------------------------------------------------------*/
	public String getGameDirectory()
	{
		return game_directory;
	}

/*-------------------------------------------------------------------*/
/**	Get client entity number.
 *	@return client entity number. */
/*-------------------------------------------------------------------*/
	public int getClientEntity()
	{
		return client_entity;
	}

/*-------------------------------------------------------------------*/
/**	Get map name.
 *	@return map name. */
/*-------------------------------------------------------------------*/
	public String getMapName()
	{
		return map_name;
	}

/*-------------------------------------------------------------------*/
/**	Get String representation of this object.
 *	@return String representation of this object. */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		String result = null;
		
		result = new String();
		result += "Server version: " + server_version + "\n";	
		result += "Level key: " + level_key + "\n";
		result += "Is demo: " + is_demo + "\n";
		result += "Client entity: " + client_entity + "\n";
		result += "Game directory: " + game_directory + "\n";
		result += "Map name: " + map_name;
		return result;
	}
}


