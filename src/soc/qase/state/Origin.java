//---------------------------------------------------------------------
// Name:			Origin.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

import soc.qase.tools.vecmath.*;

/*-------------------------------------------------------------------*/
/**	Wrapper class for origin attributes. The class implements functionality
 *	related to Quake2 entity state information, i.e. the position / coordinates
 *	of an entity currently part of a simulated environment. The coordinates
 *	are expressed in a value ranging from -32727 to 32728. */
/*-------------------------------------------------------------------*/
public class Origin
{
	private int x = -1;
	private int y = -1;
	private int z = -1;

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public Origin()
	{	}
	
/*-------------------------------------------------------------------*/
/**	Basic constructor.
 *	@param x X coordinate.
 *	@param y Y coordinate.
 *	@param z Z coordinate. */
/*-------------------------------------------------------------------*/
	public Origin(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
/*-------------------------------------------------------------------*/
/**	Constructor. Builds an Origin equivalent to a given Vector3f by
 *	rounding the float co-ordinates of the latter.
 *	@param v3f the Vector3f to duplicate */
/*-------------------------------------------------------------------*/
	public Origin(Vector3f v3f)
	{
		this.x = Math.round(v3f.x);
		this.y = Math.round(v3f.y);
		this.z = Math.round(v3f.z);
	}
	
/*-------------------------------------------------------------------*/
/**	Constructor. Builds an Origin equivalent to a given Vector2f, ie
 *	sets (x, y, z) to (v2f.x, v2f.y, 0), by rounding the float co-ordinates
 *	of the latter
 *	@param v2f the Vector2f to duplicate */
/*-------------------------------------------------------------------*/
	public Origin(Vector2f v2f)
	{
		this.x = Math.round(v2f.x);
		this.y = Math.round(v2f.y);
		this.z = 0;
	}
	
/*-------------------------------------------------------------------*/
/**	Get X coordinate.
 *	@return x coordinate. */
/*-------------------------------------------------------------------*/
	public int getX()
	{
		return x;
	}

/*-------------------------------------------------------------------*/
/**	Get Y coordinate.
 *	@return y coordinate. */
/*-------------------------------------------------------------------*/
	public int getY()
	{
		return y;
	}

/*-------------------------------------------------------------------*/
/**	Get Z coordinate.
 *	@return z coordinate. */
/*-------------------------------------------------------------------*/
	public int getZ()
	{
		return z;
	}

/*-------------------------------------------------------------------*/
/**	Set X coordinate.
 *	@param x X coordinate. */
/*-------------------------------------------------------------------*/
	public void setX(int x)
	{
		this.x = x;
	}

/*-------------------------------------------------------------------*/
/**	Set Y coordinate.
 *	@param y Y coordinate. */
/*-------------------------------------------------------------------*/
	public void setY(int y)
	{
		this.y = y;
	}

/*-------------------------------------------------------------------*/
/**	Set Z coordinate.
 *	@param z Z coordinate. */
/*-------------------------------------------------------------------*/
	public void setZ(int z)
	{
		this.z = z;
	}

/*-------------------------------------------------------------------*/
/**	Compare two Origin objects for equality.
 *	@param o the Origin object against which this Origin is to
 *	be compared
 *	@return true if the Origin objects denote the same spatial
 *	location, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean equals(Origin o)
	{
		return o.x == x && o.y == y && o.z == z;
	}

/*-------------------------------------------------------------------*/
/**	Merge Origin properties from an existing Origin object into the
 *	current Origin object. Used when assimilating cumulative updates
 *	from the Quake 2 server into the gamestate.
 *	@param origin source Origin whose attributes should be merged
 *	into the current Origin
 *	@see soc.qase.state.World#setEntity(Entity, boolean) */
/*-------------------------------------------------------------------*/
	public void merge(Origin origin)
	{
		if(origin != null) {
			if(x == -1) x = origin.getX();
			if(y == -1) y = origin.getY();
			if(z == -1) z = origin.getZ();
		}
	}

/*-------------------------------------------------------------------*/
/**	Creates a duplicate of the object by cloning each of its fields.
 *	@return a deep copy of the current object */
/*-------------------------------------------------------------------*/
	public Origin deepCopy()
	{
		return new Origin(x, y, z);
	}

/*-------------------------------------------------------------------*/
/**	Return a string representation of the Origin.
 *	@return a string representation of the Origin */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
