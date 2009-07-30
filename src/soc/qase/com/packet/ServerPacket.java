//---------------------------------------------------------------------
// Name:			ServerPacket.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.packet;

import soc.qase.com.message.Message;
import soc.qase.com.message.ServerBad;
import soc.qase.com.message.ServerCenterPrint;
import soc.qase.com.message.ServerConfigString;
import soc.qase.com.message.ServerData;
import soc.qase.com.message.ServerDeltaPacketEntities;
import soc.qase.com.message.ServerDisconnect;
import soc.qase.com.message.ServerDownload;
import soc.qase.com.message.ServerFrame;
import soc.qase.com.message.ServerInventory;
import soc.qase.com.message.ServerLayout;
import soc.qase.com.message.ServerMonsterMuzzleFlash;
import soc.qase.com.message.ServerNop;
import soc.qase.com.message.ServerPacketEntities;
import soc.qase.com.message.ServerPlayerInfo;
import soc.qase.com.message.ServerPlayerMuzzleFlash;
import soc.qase.com.message.ServerPrint;
import soc.qase.com.message.ServerReconnect;
import soc.qase.com.message.ServerSound;
import soc.qase.com.message.ServerSpawnBaseline;
import soc.qase.com.message.ServerStuffText;
import soc.qase.com.message.ServerTemporaryEntity;

/*-------------------------------------------------------------------*/
/**	Packet wrapper used when receiving host messages. */
/*-------------------------------------------------------------------*/
public class ServerPacket extends Packet
{
/*-------------------------------------------------------------------*/
/**	Constructor. Detect data type and create appropriate Message object.
 *	@param data source message data */
/*-------------------------------------------------------------------*/
	public ServerPacket(byte[] data, int off)
	{
		int messageType = 0;
		Message message = null;

		messageType = (int)data[off];

		if(messageType == 0) message = new ServerBad(data, off + 1);
		else if(messageType == 1) message = new ServerPlayerMuzzleFlash(data, off + 1);
		else if(messageType == 2) message = new ServerMonsterMuzzleFlash(data, off + 1);
		else if(messageType == 3) message = new ServerTemporaryEntity(data, off + 1);
		else if(messageType == 4) message = new ServerLayout(data, off + 1);
		else if(messageType == 5) message = new ServerInventory(data, off + 1);
		else if(messageType == 6) message = new ServerNop();
		else if(messageType == 7) message = new ServerDisconnect();
		else if(messageType == 8) message = new ServerReconnect();
		else if(messageType == 9) message = new ServerSound(data, off + 1);
		else if(messageType == 10) message = new ServerPrint(data, off + 1);
		else if(messageType == 11) message = new ServerStuffText(data, off + 1);
		else if(messageType == 12) message = new ServerData(data, off + 1);
		else if(messageType == 13) message = new ServerConfigString(data, off + 1);
		else if(messageType == 14) message = new ServerSpawnBaseline(data, off + 1);
		else if(messageType == 15) message = new ServerCenterPrint(data, off + 1);
		else if(messageType == 16) message = new ServerDownload(data, off + 1);
		else if(messageType == 17) message = new ServerPlayerInfo(data, off + 1);
		else if(messageType == 18) message = new ServerPacketEntities(data, off + 1);
		else if(messageType == 19) message = new ServerDeltaPacketEntities(data, off + 1);
		else if(messageType == 20) message = new ServerFrame(data, off + 1);
		else if(messageType > 20)
		{
			messageType = 0;
			message = new ServerBad(data, off + 1);
		}

		setMessage(message);
		setLength(1 + message.getLength());
	}
}

