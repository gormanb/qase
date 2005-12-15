//---------------------------------------------------------------------
// Name:			PlayerView.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

/*-------------------------------------------------------------------*/
/**	Wrapper class for player view attributes. */
/*-------------------------------------------------------------------*/
public class PlayerView
{
	private Origin viewOffset = null;
	private Angles viewAngles = null;
	private Angles kickAngles = null;
	private float[] blend = null;
	private int fov = -1;
	private int render = -1;

/*-------------------------------------------------------------------*/
/**	Constructor. */
/*-------------------------------------------------------------------*/
	public PlayerView()
	{	}
	
/*-------------------------------------------------------------------*/
/**	Get player view offset.
 *	@return player view offset. */
/*-------------------------------------------------------------------*/
	public Origin getViewOffset()
	{
		if(viewOffset == null)
			viewOffset = new Origin();

		return viewOffset;
	}

/*-------------------------------------------------------------------*/
/**	Set player view offset.
 *	@param viewOffset player view offset. */
/*-------------------------------------------------------------------*/
	public void setViewOffset(Origin viewOffset)
	{
		this.viewOffset = viewOffset;
	}
	
/*-------------------------------------------------------------------*/
/**	Get player view angles.
 *	@return player view angles. */
/*-------------------------------------------------------------------*/
	public Angles getViewAngles()
	{
		if(viewAngles == null)
			viewAngles = new Angles();

		return viewAngles;
	}

/*-------------------------------------------------------------------*/
/**	Set player view angles.
 *	@param viewAngles player view angles. */
/*-------------------------------------------------------------------*/
	public void setViewAngles(Angles viewAngles)
	{
		this.viewAngles = viewAngles;
	}
	
/*-------------------------------------------------------------------*/
/**	Get player view kick angles.
 *	@return player view kick angles. */
/*-------------------------------------------------------------------*/
	public Angles getKickAngles()
	{
		if(kickAngles == null)
			kickAngles = new Angles();

		return kickAngles;
	}

/*-------------------------------------------------------------------*/
/**	Set player view kick angles.
 *	@param kickAngles player view kick angles. */
/*-------------------------------------------------------------------*/
	public void setKickAngles(Angles kickAngles)
	{
		this.kickAngles = kickAngles;
	}
	
/*-------------------------------------------------------------------*/
/**	Get player view blend.
 *	@return player view blend values. */
/*-------------------------------------------------------------------*/
	public float[] getBlend()
	{
		if(blend == null)
			blend = new float[4];

		return blend;
	}

/*-------------------------------------------------------------------*/
/**	Set player view blend.
 *	@param blend player view blend values. */
/*-------------------------------------------------------------------*/
	public void setBlend(float[] blend)
	{
		this.blend = blend;
	}
	
/*-------------------------------------------------------------------*/
/**	Get player field of view.
 *	@return player field of view. */
/*-------------------------------------------------------------------*/
	public int getFOV()
	{
		return fov;
	}

/*-------------------------------------------------------------------*/
/**	Set player field of view.
 *	@param fov player's field of view */
/*-------------------------------------------------------------------*/
	public void setFOV(int fov)
	{
		this.fov = fov;
	}
	
/*-------------------------------------------------------------------*/
/**	Get player view render value. Can assume two values: 1 or 2. 
 *	@return player view render value. */
/*-------------------------------------------------------------------*/
	public int getRender()
	{
		return render;
	}

/*-------------------------------------------------------------------*/
/**	Get player view render value as string. Can assume two values: 
 *	"underwater" or "noWorldModel".
 *	@return player view render value as string. */
/*-------------------------------------------------------------------*/
	public String getRenderString()
	{
		String result = "";

		if(render == 1) result += "underwater";
		if(render == 2) result += "noWorldModel";

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Set player view render value.
 *	@param render player view render value. */
/*-------------------------------------------------------------------*/
	public void setRender(int render)
	{
		this.render = render;
	}
	
/*-------------------------------------------------------------------*/
/**	Merge player properties from an existing PlayerView object into the
 *	current PlayerView object. Used when assimilating cumulative updates
 *	from the Quake 2 server into the gamestate.
 *	@param playerView source PlayerView whose attributes should be merged
 *	into the current PlayerView
 *	@see soc.qase.state.World#setPlayer(Player) */
/*-------------------------------------------------------------------*/
	public void merge(PlayerView playerView)
	{
		if(playerView == null)
			return;

		if(blend == null) blend = playerView.getBlend();
		if(fov == -1) fov = playerView.getFOV();
		if(render == -1) render = playerView.getRender();

		if(viewOffset == null) viewOffset = playerView.viewOffset; else viewOffset.merge(playerView.viewOffset);
		if(viewAngles == null) viewAngles = playerView.viewAngles; else viewAngles.merge(playerView.viewAngles);
		if(kickAngles == null) kickAngles = playerView.kickAngles; else kickAngles.merge(playerView.kickAngles);
	}
}
