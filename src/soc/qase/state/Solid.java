//---------------------------------------------------------------------
// Name:			Solid.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

/*-------------------------------------------------------------------*/
/**	Wrapper class for solid attributes. */
/*-------------------------------------------------------------------*/
public class Solid
{
	private int solid = -1;

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public Solid()
	{	}
	
/*-------------------------------------------------------------------*/
/**	Set solid value.
 *	@param solid solid value. */
/*-------------------------------------------------------------------*/
	public void setSolid(int solid)
	{
		this.solid = solid;
	}

/*-------------------------------------------------------------------*/
/**	Get solid value.
 *	@return solid value. */
/*-------------------------------------------------------------------*/
	public int getSolid()
	{
		return solid;
	}

/*-------------------------------------------------------------------*/
/**	Merge Solid properties from an existing Solid object into the
 *	current Solid object. Used when assimilating cumulative updates
 *	from the Quake 2 server into the gamestate.
 *	@param solid source Solid whose attributes should be merged
 *	into the current Solid
 *	@see soc.qase.state.World#setEntity(Entity, boolean) */
/*-------------------------------------------------------------------*/
	public void merge(Solid solid)
	{
		if(solid != null)
		{
			if(this.solid == -1)
				this.solid = solid.getSolid();
		}
	}

/*-------------------------------------------------------------------*/
/**	Creates a duplicate of the object by cloning each of its fields.
 *	@return a deep copy of the current object */
/*-------------------------------------------------------------------*/
	public Solid deepCopy()
	{
		Solid sol = new Solid();

		sol.setSolid(solid);

		return sol;
	}
}
