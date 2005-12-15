//---------------------------------------------------------------------
// Name:			ServerMonsterMuzzleFlash.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Monster muzzle flashes and effects. Muzzle effect indices can be
 *	compared against the MZ2 constants in the Effects class.
 *	@see soc.qase.state.Effects */
/*-------------------------------------------------------------------*/
public class ServerMonsterMuzzleFlash extends Message
{
	private int monsterEntity = 0;
	private int effect = 0;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the data and extracts message details.
 *	@param data message source. */
/*-------------------------------------------------------------------*/
	public ServerMonsterMuzzleFlash(byte[] data, int off)
	{
		monsterEntity = Utils.shortValue(data, off);
		effect = (int)data[off + 2];
		setLength(3);
	}
}

