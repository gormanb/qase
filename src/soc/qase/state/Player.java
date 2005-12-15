//---------------------------------------------------------------------
// Name:			Player.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

import soc.qase.com.message.*;
import soc.qase.info.*;

/*-------------------------------------------------------------------*/
/**	The Player class is used as a wrapper class for a set of information
 *	concerning an agent's state as a player in the simulated environment.
 *	Each frame of execution instances of this type can be extracted from
 *	the proxy object, while the agent is part of an ongoing simulation.
 *	It is possible to extract information concerning an agent's movement
 *	state, as well as other agent status information (e.g. health, items
 *	carried, etc.). */
/*-------------------------------------------------------------------*/
public class Player
{
	private PlayerMove playerMove = null;
 	private PlayerGun playerGun = null;
	private PlayerView playerView = null;
	private PlayerStatus playerStatus = null;

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public Player()
	{	}
	
/*-------------------------------------------------------------------*/
/**	Set player move type.
 *	@param playerMoveType player move type. */
/*-------------------------------------------------------------------*/
	public void setMoveType(int playerMoveType)
	{
		if(playerMove == null)
			playerMove = new PlayerMove();

		playerMove.setType(playerMoveType);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player origin.
 *	@param origin player origin. */
/*-------------------------------------------------------------------*/
	public void setOrigin(Origin origin)
	{
		if(playerMove == null)
			playerMove = new PlayerMove();

		playerMove.setOrigin(origin);
	}
	
/*-------------------------------------------------------------------*/
/**	Set velocity origin.
 *	@param velocity player velocity. */
/*-------------------------------------------------------------------*/
	public void setVelocity(Velocity velocity)
	{
		if(playerMove == null)
			playerMove = new PlayerMove();

		playerMove.setVelocity(velocity);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player move time.
 *	@param playerMoveTime player move time. */
/*-------------------------------------------------------------------*/
	public void setMoveTime(int playerMoveTime)
	{
		if(playerMove == null)
			playerMove = new PlayerMove();

		playerMove.setTime(playerMoveTime);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player move flags.
 *	@param playerMoveFlags player move flags. */
/*-------------------------------------------------------------------*/
	public void setMoveFlags(int playerMoveFlags)
	{
		if(playerMove == null)
			playerMove = new PlayerMove();

		playerMove.setFlags(playerMoveFlags);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player gravity.
 *	@param gravity player gravity. */
/*-------------------------------------------------------------------*/
	public void setGravity(int gravity)
	{
		if(playerMove == null)
			playerMove = new PlayerMove();

		playerMove.setGravity(gravity);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player delta angles.
 *	@param deltaAngles player delta angles. */
/*-------------------------------------------------------------------*/
	public void setDeltaAngles(Angles deltaAngles)
	{
		if(playerMove == null)
			playerMove = new PlayerMove();

		playerMove.setDeltaAngles(deltaAngles);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player view offset.
 *	@param viewOffset player view offset. */
/*-------------------------------------------------------------------*/
	public void setViewOffset(Origin viewOffset)
	{
		if(playerView == null)
			playerView = new PlayerView();

		playerView.setViewOffset(viewOffset);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player view angles.
 *	@param viewAngles player view angles. */
/*-------------------------------------------------------------------*/
	public void setViewAngles(Angles viewAngles)
	{
		if(playerView == null)
			playerView = new PlayerView();

		playerView.setViewAngles(viewAngles);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player kick angles.
 *	@param kickAngles player kick angles. */
/*-------------------------------------------------------------------*/
	public void setKickAngles(Angles kickAngles)
	{
		if(playerView == null)
			playerView = new PlayerView();

		playerView.setKickAngles(kickAngles);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player gun index.
 *	@param gunIndex player gun index. */
/*-------------------------------------------------------------------*/
	public void setGunIndex(int gunIndex)
	{
		if(playerGun == null)
			playerGun = new PlayerGun();

		playerGun.setIndex(gunIndex);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player gun frame.
 *	@param gunFrame player gun frame. */
/*-------------------------------------------------------------------*/
	public void setGunFrame(int gunFrame)
	{
		if(playerGun == null)
			playerGun = new PlayerGun();

		playerGun.setFrame(gunFrame);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player gun offset.
 *	@param gunOffset player gun offset. */
/*-------------------------------------------------------------------*/
	public void setGunOffset(Origin gunOffset)
	{
		if(playerGun == null)
			playerGun = new PlayerGun();

		playerGun.setOffset(gunOffset);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player gun angles.
 *	@param gunAngles player gun angles. */
/*-------------------------------------------------------------------*/
	public void setGunAngles(Angles gunAngles)
	{
		if(playerGun == null)
			playerGun = new PlayerGun();

		playerGun.setAngles(gunAngles);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player blend.
 *	@param blend player blend. */
/*-------------------------------------------------------------------*/
	public void setBlend(float[] blend)
	{
		if(playerView == null)
			playerView = new PlayerView();

		playerView.setBlend(blend);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player field of view.
 *	@param fov player field of view. */
/*-------------------------------------------------------------------*/
	public void setFOV(int fov)
	{
		if(playerView == null)
			playerView = new PlayerView();

		playerView.setFOV(fov);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player render.
 *	@param render player render. */
/*-------------------------------------------------------------------*/
	public void setRender(int render)
	{
		if(playerView == null)
			playerView = new PlayerView();

		playerView.setRender(render);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player status.
 *	@param bitmask status bitmask.
 *	@param data status data.
 *	@param offset status data offset. */
/*-------------------------------------------------------------------*/
	public void setStatus(int bitmask, byte[] data, int offset)
	{
		if(playerStatus == null)
			playerStatus = new PlayerStatus();

		playerStatus.setStatus(bitmask, data, offset);
	}
	
/*-------------------------------------------------------------------*/
/**	Set player status.
 *	@param playerStatus player status. */
/*-------------------------------------------------------------------*/
	public void setPlayerStatus(PlayerStatus playerStatus)
	{
		this.playerStatus = playerStatus;
	}
	
/*-------------------------------------------------------------------*/
/**	Get player move.
 *	@return player move infomation. */
/*-------------------------------------------------------------------*/
	public PlayerMove getPlayerMove()
	{
		if(playerMove == null)
			playerMove = new PlayerMove();

		return playerMove;
	}
	
/*-------------------------------------------------------------------*/
/**	Get player view.
 *	@return player view infomation. */
/*-------------------------------------------------------------------*/
	public PlayerView getPlayerView()
	{
		if(playerView == null)
			playerView = new PlayerView();

		return playerView;
	}
	
/*-------------------------------------------------------------------*/
/**	Get player gun.
 *	@return player gun infomation. */
/*-------------------------------------------------------------------*/
	public PlayerGun getPlayerGun()
	{
		if(playerGun == null)
			playerGun = new PlayerGun();

		return playerGun;
	}
	
/*-------------------------------------------------------------------*/
/**	Get player status.
 *	@return player status infomation. */
/*-------------------------------------------------------------------*/
	public PlayerStatus getPlayerStatus()
	{
		if(playerStatus == null)
			playerStatus = new PlayerStatus();

		return playerStatus;
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently alive.
 *	@return true if alive, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isAlive()
	{
		return getPlayerStatus().getStatus(PlayerStatus.HEALTH) > 0;
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently jumping.
 *	@return true if jumping, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isJumping()
	{
		return getPlayerMove().checkFlags(PlayerMove.FLAG_JUMP);
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently ducking.
 *	@return true if ducking, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isDucked()
	{
		return getPlayerMove().checkFlags(PlayerMove.FLAG_DUCKED);
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently in water.
 *	@return true if in water, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isInWater()
	{
		return getPlayerMove().checkFlags(PlayerMove.FLAG_TIME_WATER);
	}

/*-------------------------------------------------------------------*/
/**	Merge player properties from an existing Player object into the
 *	current Player object. Used when assimilating cumulative updates
 *	from the Quake 2 server into the gamestate.
 *	@param player source Player whose attributes should be merged
 *	into the current Player
 *	@see soc.qase.state.World#setPlayer(Player) */
/*-------------------------------------------------------------------*/
	public void merge(Player player)
	{
		if(player == null)
			return;

		if(playerMove == null) playerMove = player.playerMove; else playerMove.merge(player.playerMove);
		if(playerView == null) playerView = player.playerView; else playerView.merge(player.playerView);
		if(playerGun == null) playerGun = player.playerGun;

		if(playerStatus == null)
		{
			playerStatus = player.playerStatus;
			playerGun.merge(player.playerGun, false);
		}
		else
		{
			playerGun.merge(player.playerGun, playerStatus.getStatus(PlayerStatus.AMMO) != -1 && playerStatus.getStatus(PlayerStatus.AMMO) < player.getPlayerStatus().getStatus(PlayerStatus.AMMO));
			playerStatus.merge(player.getPlayerStatus());
		}
	}
}
