//---------------------------------------------------------------------
// Name:			Vector3f.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

/*-------------------------------------------------------------------*/
/**	Wrapper class for event attributes. */
/*-------------------------------------------------------------------*/
public class Events
{
	private int events = 0;

	public static int NONE = 0, ITEM_RESPAWN = 1, FOOTSTEP = 2,
					  FALLSHORT = 3, FALL = 4, FALLFAR = 5, PLAYER_TELEPORT = 6;

/*-------------------------------------------------------------------*/
/** Default constructor. */
/*-------------------------------------------------------------------*/
	public Events()
	{	}
	
/*-------------------------------------------------------------------*/
/**	Set events bitmask.
 *	@param events events bitmask. */
/*-------------------------------------------------------------------*/
	public void setEvents(int events)
	{
		this.events = events;
	}

/*-------------------------------------------------------------------*/
/**	Get events bitmask.
 *	@return events bitmask. */
/*-------------------------------------------------------------------*/
	public int getEvents()
	{
		return events;
	}

/*-------------------------------------------------------------------*/
/**	Check whether the specified event occurred.
 *	@param eventType the event to check for
 *	@return true if the given event occurred */
/*-------------------------------------------------------------------*/
	public boolean checkEvent(int eventType)
	{
		return (events == eventType);
	}

/*-------------------------------------------------------------------*/
/**	Merge Events properties from an existing Events object into the
 *	current Events object. Used when assimilating cumulative updates
 *	from the Quake 2 server into the gamestate.
 *	@param events source Events whose attributes should be merged
 *	into the current Events
 *	@see soc.qase.state.World#setEntity(Entity, boolean) */
/*-------------------------------------------------------------------*/
	public void merge(Events events)
	{
		if(events != null)
		{
			if(this.events == 0)
				this.events = events.getEvents();
		}
	}

/*-------------------------------------------------------------------*/
/**	Creates a duplicate of the object by cloning each of its fields.
 *	@return a deep copy of the current object */
/*-------------------------------------------------------------------*/
	public Events deepCopy()
	{
		Events ev = new Events();

		ev.setEvents(events);

		return ev;
	}
}
