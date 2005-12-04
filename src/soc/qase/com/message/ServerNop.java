//---------------------------------------------------------------------
// Name:			ServerMonsterMuzzleFlash.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.message;

import java.io.*;
import java.net.*;
import java.lang.*;

/*-------------------------------------------------------------------*/
/**	Do nothing. */
/*-------------------------------------------------------------------*/
public class ServerNop extends Message
{

/*-------------------------------------------------------------------*/
/**	Default constructor. As the message instructs, this does nothing.*/
/*-------------------------------------------------------------------*/
	public ServerNop()
	{
		setLength(0);
	}
}

