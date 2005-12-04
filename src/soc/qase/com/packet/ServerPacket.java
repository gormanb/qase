//---------------------------------------------------------------------
// Name:			ServerReconnect.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.packet;

import java.io.*;
import java.net.*;
import java.lang.*;
import soc.qase.tools.Utils;
import soc.qase.com.message.*;

/*-------------------------------------------------------------------*/
/**	Packet wrapper used when receiving host messages. */
/*-------------------------------------------------------------------*/
public class ServerPacket extends Packet
{
/*-------------------------------------------------------------------*/
/**	Constructor. Detect data type and create appropriate Message object.
 *	@param data source message data */
/*-------------------------------------------------------------------*/
	public ServerPacket(byte[] data)
	{
		byte[] type = null;
		byte[] messageData = null;

		int messageType = 0;
		Message message = null;

		type = Utils.extractBytes(data, 0, 1);
		messageData = Utils.extractBytes(data, 1, data.length - 1);

		messageType = (int)type[0];

		if(messageType == 0) message = new ServerBad(messageData);
		else if(messageType == 1) message = new ServerPlayerMuzzleFlash(messageData);
		else if(messageType == 2) message = new ServerMonsterMuzzleFlash(messageData);
		else if(messageType == 3) message = new ServerTemporaryEntity(messageData);
		else if(messageType == 4) message = new ServerLayout(messageData);
		else if(messageType == 5) message = new ServerInventory(messageData);
		else if(messageType == 6) message = new ServerNop();
		else if(messageType == 7) message = new ServerDisconnect();
		else if(messageType == 8) message = new ServerReconnect();
		else if(messageType == 9) message = new ServerSound(messageData);
		else if(messageType == 10) message = new ServerPrint(messageData);
		else if(messageType == 11) message = new ServerStuffText(messageData);
		else if(messageType == 12) message = new ServerData(messageData);
		else if(messageType == 13) message = new ServerConfigString(messageData);
		else if(messageType == 14) message = new ServerSpawnBaseline(messageData);
		else if(messageType == 15) message = new ServerCenterPrint(messageData);
		else if(messageType == 16) message = new ServerDownload(messageData);
		else if(messageType == 17) message = new ServerPlayerInfo(messageData);
		else if(messageType == 18) message = new ServerPacketEntities(messageData);
		else if(messageType == 19) message = new ServerDeltaPacketEntities(messageData);
		else if(messageType == 20) message = new ServerFrame(messageData);
		else if(messageType > 20)
		{
			messageType = 0;
			message = new ServerBad(messageData);
		}

		setMessage(message);
		setLength(1 + message.getLength());
	}
}

