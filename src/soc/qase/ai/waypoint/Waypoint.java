//--------------------------------------------------
// Name:			Waypoint.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.ai.waypoint;

import java.io.Serializable;
import java.util.Vector;

import soc.qase.state.Origin;
import soc.qase.tools.vecmath.Vector3f;

/*-------------------------------------------------------------------*/
/**	Represents a single node in the topological map. Consists of a
 *	Vector3f indicating the node's position, and a vector of all other
 *	Waypoints to which this node is connected. Note that Quake 2 uses
 *	a co-ordinate system in which the Z dimension is the vertical plane
 *	from the perspective of the agent.*/
/*-------------------------------------------------------------------*/
public class Waypoint implements Serializable
{
	private Vector3f pos = null;
	private boolean locked = false;
	private Vector edges = new Vector();

/*-------------------------------------------------------------------*/
/**	Constructor. For convenience, both Origin and Vector3f forms are
 *	supplied.
 *	@param location an Origin object indicating the position of this node */
/*-------------------------------------------------------------------*/
	public Waypoint(Origin location)
	{
		pos = new Vector3f(location);
	}

/*-------------------------------------------------------------------*/
/**	Constructor. For convenience, both Origin and Vector3f forms are
 *	supplied.
 *	@param location a Vector3f object indicating the position of this node */
/*-------------------------------------------------------------------*/
	public Waypoint(Vector3f location)
	{
		pos = new Vector3f(location);
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Allows a node to be specified directly in 3D co-ordinates.
/*-------------------------------------------------------------------*/
	public Waypoint(float x, float y, float z)
	{
		pos = new Vector3f(x, y, z);
	}

/*-------------------------------------------------------------------*/
/**	Add an edge between this node and another.
 *	@param node the node to which this node should be connected
 *	@return true if the node is unlocked and the edge was added,
 *	false otherwise */
/*-------------------------------------------------------------------*/
	public boolean addEdge(Waypoint node)
	{
		if(locked)
			return false;

		if(edges.indexOf(node) == -1)
			edges.add(node);

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Remove an edge between this node and another, if such an edge exists.
 *	@param node the node which forms the far endpoint of the edge to be
 *	removed
 *	@return true if the node is unlocked and the edge was removed,
 *	false otherwise */
/*-------------------------------------------------------------------*/
	public boolean removeEdge(Waypoint node)
	{
		return !locked && edges.remove(node);
	}

/*-------------------------------------------------------------------*/
/**	Remove an edge between this node and another.
 *	@param index the index in this node's edge array of the edge to remove
 *	@return true if the node is unlocked and the edge was removed,
 *	false otherwise */
/*-------------------------------------------------------------------*/
	public boolean removeEdge(int index)
	{
		return !locked && edges.remove(index) != null;
	}

/*-------------------------------------------------------------------*/
/**	Obtain the position of this waypoint.
 *	@return a Vector3f indicating the position of this waypoint */
/*-------------------------------------------------------------------*/
	public Vector3f getPosition()
	{
		return new Vector3f(pos);
	}

/*-------------------------------------------------------------------*/
/**	Obtain the set of nodes to which this node is connected.
 *	@return an array of Waypoints indicating the set of nodes to which
 *	this node is directly connected */
/*-------------------------------------------------------------------*/
	public Waypoint[] getEdges()
	{
		return (Waypoint[])edges.toArray(new Waypoint[0]);
	}

/*-------------------------------------------------------------------*/
/**	Set the position of this waypoint. For convenience, both Origin and
 *	Vector3f forms are supplied.
 *	@param location the new location of this waypoint
 *	@return true if the node is unlocked and the position was changed,
 *	false otherwise */
/*-------------------------------------------------------------------*/
	public boolean setPosition(Origin location)
	{
		if(locked)
			return false;

		pos.set(location);

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Set the position of this waypoint. For convenience, both Origin and
 *	Vector3f forms are supplied.
 *	@param location the new location of this waypoint
 *	@return true if the node is unlocked and the position was changed,
 *	false otherwise */
/*-------------------------------------------------------------------*/
	public boolean setPosition(Vector3f location)
	{
		if(locked)
			return false;

		pos.set(location);

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Set the position of this waypoint directly, in 3D co-ordinates.
 *	@param x the X co-ordinate of this node
 *	@param y the X co-ordinate of this node
 *	@param z the X co-ordinate of this node
 *	@return true if the node is unlocked and the position was changed,
 *	false otherwise */
/*-------------------------------------------------------------------*/
	public boolean setPosition(float x, float y, float z)
	{
		if(locked)
			return false;

		pos.set(x, y, z);

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Lock the node, preventing further alteration of its position or edge
 *	array. Waypoints should not be locked individually - the associated
 *	method of the WaypointMap class will simultaneously lock all nodes,
 *	thereby ensuring consistency. Used by WaypointMapGenerator when
 *	automatically building a topological map, before the path and cost
 *	matrices are constructed.
 *	@see WaypointMap#lockMap
 *	@see WaypointMapGenerator */
/*-------------------------------------------------------------------*/
	public void lockNode()
	{
		locked = true;
	}

/*-------------------------------------------------------------------*/
/**	Unlocks the node, allowing alteration of its position and edge array.
 *	Waypoints should not be unlocked individually - the associated method
 *	of the WaypointMap class will simultaneously unlock all nodes, thereby
 *	ensuring consistency.
 *	@see WaypointMap#unlockMap
/*-------------------------------------------------------------------*/
	public void unlockNode()
	{
		locked = false;
	}

/*-------------------------------------------------------------------*/
/**	Check whether this node is locked.
 *	@return true if the node is locked, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isLocked()
	{
		return locked;
	}
}
