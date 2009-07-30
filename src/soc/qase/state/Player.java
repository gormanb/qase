//---------------------------------------------------------------------
// Name:			Player.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

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

	public long drownTTL = Long.MIN_VALUE;
	public boolean playerIsDrowning = false;

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
/**	Get the player's current position.
 *	@return the current position */
/*-------------------------------------------------------------------*/
	public Origin getPosition()
	{
		return getPlayerMove().getOrigin();
	}

/*-------------------------------------------------------------------*/
/**	Get the player's current orientation.
 *	@return the current orientation */
/*-------------------------------------------------------------------*/
	public Angles getOrientation()
	{
		return getPlayerView().getViewAngles();
	}

/*-------------------------------------------------------------------*/
/**	Get the player's current health.
 *	@return the player's health value */
/*-------------------------------------------------------------------*/
	public int getHealth()
	{
		return getPlayerStatus().getStatus(PlayerStatus.HEALTH);
	}

/*-------------------------------------------------------------------*/
/**	Get the amount of armor currently held by the player.
 *	@return the current armor value */
/*-------------------------------------------------------------------*/
	public int getArmor()
	{
		return getPlayerStatus().getStatus(PlayerStatus.ARMOR);
	}

/*-------------------------------------------------------------------*/
/**	Get the inventory index of the current weapon.
 *	@return the index of the current weapon, as indicated by the
 *	constants in the Inventory class */
/*-------------------------------------------------------------------*/
	public int getWeaponIndex()
	{
		return getPlayerGun().getInventoryIndex();
	}

/*-------------------------------------------------------------------*/
/**	Get the amount of ammo the agent has for the current weapon.
 *	@return the current ammo, or Integer.MIN_VALUE if the agent
 *	is not currently connected */
/*-------------------------------------------------------------------*/
	public int getAmmo()
	{
		return getPlayerStatus().getStatus(PlayerStatus.AMMO);
	}

/*-------------------------------------------------------------------*/
/**	Determines whether the player is currently firing his gun.
 *	@return true if the player is firing, false otherwise. */
/*-------------------------------------------------------------------*/
	public boolean isFiring()
	{
		return getPlayerGun().isFiring();
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently alive.
 *	@return true if alive, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isAlive()
	{
		return getHealth() > 0;
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
/**	Check whether agent is currently crouching.
 *	@return true if crouching, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isCrouching()
	{
		return getPlayerMove().checkFlags(PlayerMove.FLAG_CROUCH);
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently stopped.
 *	@return true if stopped, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isStopped()
	{
		return getPlayerMove().getWalkState() == PlayerMove.WALK_STOPPED;
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently walking.
 *	@return true if walking, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isWalking()
	{
		return getPlayerMove().getWalkState() == PlayerMove.WALK_NORMAL;
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently running.
 *	@return true if running, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isRunning()
	{
		return getPlayerMove().getWalkState() == PlayerMove.WALK_RUN;
	}

/*-------------------------------------------------------------------*/
/**	Get the agent's current posture, as one of the POSTURE constants
 *	in the PlayerMove class (POSTURE_CROUCH = -1, POSTURE_NORMAL = 0,
 *	POSTURE_JUMP = 1;).
 *	@return one of the POSTURE constants in PlayerMove */
/*-------------------------------------------------------------------*/
	public int getPosture()
	{
		if(isCrouching())
			return PlayerMove.POSTURE_CROUCH;
		else if(isJumping())
			return PlayerMove.POSTURE_JUMP;
		else
			return PlayerMove.POSTURE_NORMAL;
	}

/*-------------------------------------------------------------------*/
/**	Get the agent's current walking state (stopped, normal or run).
 *	@return one of the WALK constants in PlayerMove */
/*-------------------------------------------------------------------*/
	public int getWalkState()
	{
		return getPlayerMove().getWalkState();
	}

/*-------------------------------------------------------------------*/
/**	Check whether agent is currently in water.
 *	@return true if in water, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isUnderWater()
	{
		// return getPlayerMove().checkFlags(PlayerMove.FLAG_TIME_WATER);
		return playerView.checkRender(PlayerView.RDF_UNDERWATER);
	}

/*-------------------------------------------------------------------*/
/**	Determines the amount of time remaining until the player begins
 *	drowning, or Long.MIN_VALUE if the player is not underwater. If
 *	the player has a Rebreather or Environment Suit active - meaning
 *	that they can breathe underwater - the value returned will indicate
 *	the total amount of time until drowning, taking the active item
 *	into account.
 *	@return the time remaining until the player starts to drown in
 *	milliseconds, or Long.MIN_VALUE if the player is not underwater */
/*-------------------------------------------------------------------*/
	public long timeUntilDrowning()
	{
		return drownTTL;
	}

/*-------------------------------------------------------------------*/
/**	Checks whether the agent is currently drowning.
 *	@return true if the player is drowning, false otherwise. */
/*-------------------------------------------------------------------*/
	public boolean isDrowning()
	{
		return playerIsDrowning;
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

		if(playerView == null) playerView = player.playerView; else playerView.merge(player.playerView);
		if(playerMove == null) playerMove = player.playerMove; else playerMove.merge(player.playerMove, isUnderWater());
		if(playerGun == null) playerGun = player.playerGun;

		if(playerStatus == null)
		{
			playerStatus = player.playerStatus;
			playerGun.merge(player.playerGun, false);
		}
		else
		{
			playerGun.merge(player.playerGun, playerStatus.getStatus(PlayerStatus.AMMO) != Integer.MIN_VALUE && playerStatus.getStatus(PlayerStatus.AMMO) < player.getPlayerStatus().getStatus(PlayerStatus.AMMO));
			playerStatus.merge(player.getPlayerStatus());
		}
	}
}
