//--------------------------------------------------
// Name:			WaypointMap.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.ai.waypoint;

import java.util.Vector;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import soc.qase.state.Origin;
import soc.qase.state.Entity;
import soc.qase.tools.vecmath.Vector3f;

/*-------------------------------------------------------------------*/
/**	Controls and co-ordinates the high-level creation of a waypoint map.
 *	To ensure consistency, all nodes, edges and item nodes should be
 *	added using the methods of this class. These methods are used by
 *	the WaypointMapGenerator class to create a topological map from a
 *	DM2 recording. The class also provides methods to save the WaypointMap
 *	object to file and reload it at a later time, thereby allowing maps
 *	to be reused without having to be regenerated each time. /*	
 *	@see Waypoint
 *	@see WaypointItem
 *	@see WaypointMapGenerator */
/*-------------------------------------------------------------------*/
public class WaypointMap implements Serializable
{
	private Vector nodes = null;
	private Vector itemNodes = null;

	private int[] itemNodeTypes = null;
	private int[] itemNodeIndices = null;
	private Waypoint[] itemNodeWaypoints = null;

	private boolean locked = false;

	private boolean[][] edgeMatrix = null;
	private float[][] waypointMatrix = null;

	private int[][] predMatrix = null;
	private float[][] costMatrix = null;

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public WaypointMap()
	{
		nodes = new Vector();
		itemNodes = new Vector();
	}

/*-------------------------------------------------------------------*/
/**	Get the index in the map's node array of a given waypoint. Compares
 *	Waypoint objects by reference - to compare by position, use
 *	findClosestWaypoint.
 *	@param node the node whose index is required
 *	@return the index in the map's array of the specified node */
/*-------------------------------------------------------------------*/
	public int indexOf(Waypoint node)
	{
		return nodes.indexOf(node);
	}

/*-------------------------------------------------------------------*/
/**	Add a node to the waypoint map. For convenience, this method is
 *	overloaded to accept Waypoint, Vector3f and array paramters.
 *	@param node the new node to be added
 *	@return true if the WaypointMap is unlocked and the node was
 *	successfully added, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean addNode(Waypoint node)
	{
		if(locked)
			return false;

		nodes.add(node);
		nullifyMatrices();

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Add a node to the waypoint map. For convenience, this method is
 *	overloaded to accept Waypoint, Vector3f, Origin and array paramters.
 *	@param node the new node to be added
 *	@return true if the WaypointMap is unlocked and the node was
 *	successfully added, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean addNode(Vector3f node)
	{
		if(locked)
			return false;

		nodes.add(new Waypoint(node));
		nullifyMatrices();

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Add a node to the waypoint map. For convenience, this method is
 *	overloaded to accept Waypoint, Vector3f, Origin and array paramters.
 *	@param node the new node to be added
 *	@return true if the WaypointMap is unlocked and the node was
 *	successfully added, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean addNode(Origin node)
	{
		return addNode(new Vector3f(node));
	}

/*-------------------------------------------------------------------*/
/**	Add a set of nodes to the waypoint map. For convenience, this
 *	method is overloaded to accept Waypoint, Vector3f, Origin and array
 *	paramters.
 *	@param newNodes the new nodes to be added
 *	@return true if the WaypointMap is unlocked and the node was
 *	successfully added, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean addNode(Origin[] newNodes)
	{
		if(locked)
			return false;

		for(int i = 0; i < newNodes.length; i++)
			nodes.add(new Waypoint(newNodes[i]));

		nullifyMatrices();

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Add a set of nodes to the waypoint map. For convenience, this
 *	method is overloaded to accept Waypoint, Vector3f, Origin and array
 *	paramters.
 *	@param newNodes the new nodes to be added
 *	@return true if the WaypointMap is unlocked and the node was
 *	successfully added, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean addNode(Vector3f[] newNodes)
	{
		if(locked)
			return false;

		for(int i = 0; i < newNodes.length; i++)
			nodes.add(new Waypoint(newNodes[i]));

		nullifyMatrices();

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Add a set of nodes to the waypoint map. For convenience, this
 *	method is overloaded to accept Waypoint, Vector3f, Origin and array
 *	paramters.
 *	@param newNodes the new nodes to be added
 *	@return true if the WaypointMap is unlocked and the node was
 *	successfully added, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean addNode(Waypoint[] newNodes)
	{
		if(locked)
			return false;

		for(int i = 0; i < newNodes.length; i++)
			nodes.add(newNodes[i]);

		nullifyMatrices();

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Remove a node from the waypoint map. Compares nodes by reference.
 *	Also removes all edges of which the deleted node was an endpoint,
 *	and - if appropriate - the record of an item at that point.
 *	@param node the node to be removed, if present
 *	@return true if the WaypointMap is unlocked and the node was
 *	successfully removed, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean deleteNode(Waypoint node)
	{
		if(locked)
			return false;

		for(int i = 0; i < nodes.size(); i++)
		{
			((Waypoint)nodes.elementAt(i)).removeEdge(node);
		}

		WaypointItem wpi = null;

		for(int i = itemNodes.size() - 1; i >= 0; i--)
		{
			wpi = (WaypointItem)itemNodes.elementAt(i);

			if(node == wpi.getNode())
				itemNodes.removeElementAt(i);
		}

		nodes.remove(node);
		nullifyMatrices();

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Remove a node from the waypoint map.  Compares nodes by reference.
 *	Also removes all edges of which the deleted node was an endpoint,
 *	and - if appropriate - the record of an item at that point.
 *	@param index the index of the node to be removed
 *	@return true if the WaypointMap is unlocked and the node was
 *	successfully removed, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean deleteNode(int index)
	{
		if(locked)
			return false;

		deleteNode((Waypoint)nodes.elementAt(index));

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Mark certain nodes on the waypoint map as containing items. Does
 *	not remove any existing marked nodes.
 *	@param iNodes the nodes at which items are located
 *	@param entityAtNodes the correspoding item entities
 *	@return true if the WaypointMap is unlocked and the nodes were
 *	marked, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean markItemNodes(Waypoint[] iNodes, Entity[] entityAtNodes)
	{
		return markItemNodes(iNodes, entityAtNodes, false);
	}

/*-------------------------------------------------------------------*/
/**	Mark certain nodes on the waypoint map as containing items. Does
 *	not remove any existing marked nodes.
 *	@param indices the indices of the nodes at which items are located
 *	@param entityAtNodes the correspoding item entities
 *	@return true if the WaypointMap is unlocked and the nodes were
 *	marked, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean markItemNodes(int[] indices, Entity[] entityAtNodes)
	{
		return markItemNodes(indices, entityAtNodes, false);
	}

/*-------------------------------------------------------------------*/
/**	Mark certain nodes on the waypoint map as containing items.
 *	@param iNodes the nodes at which items are located
 *	@param entityAtNodes the correspoding item entities
 *	@param clearPreviousMarks if true, remove all existing references
 *	to items at nodes; the current list is taken as being exhaustive
 *	@return true if the WaypointMap is unlocked and the nodes were
 *	marked, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean markItemNodes(Waypoint[] iNodes, Entity[] entityAtNodes, boolean clearPreviousMarks)
	{
		if(locked)
			return false;

		if(clearPreviousMarks)
			itemNodes.clear();

		for(int i = 0; i < iNodes.length; i++)
			itemNodes.add(new WaypointItem(iNodes[i], entityAtNodes[i]));

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Mark certain nodes on the waypoint map as containing items.
 *	@param indices the indices of the nodes at which items are located
 *	@param entityAtNodes the correspoding item entities
 *	@param clearPreviousMarks if true, remove all existing references
 *	to items at nodes; the current list is taken as being exhaustive
 *	@return true if the WaypointMap is unlocked and the nodes were
 *	marked, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean markItemNodes(int[] indices, Entity[] entityAtNodes, boolean clearPreviousMarks)
	{
		if(locked)
			return false;

		Waypoint[] iNodes = new Waypoint[indices.length];

		for(int i = 0; i < iNodes.length; i++)
			iNodes[i] = (Waypoint)nodes.elementAt(indices[i]);

		return markItemNodes(iNodes, entityAtNodes, clearPreviousMarks);
	}

/*-------------------------------------------------------------------*/
/**	Mark certain nodes on the waypoint map as containing items. Other
 *	overloadings of this method construct WaypointItem objects given
 *	source information; this allows the addition of pre-built
 *	WaypointItem objects.
 *	@param waypointItems the pre-built list of items and nodes
 *	@return true if the WaypointMap is unlocked and the nodes were
 *	marked, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean markItemNodes(WaypointItem[] waypointItems)
	{
		if(locked)
			return false;

		itemNodes.clear();

		for(int i = 0; i < waypointItems.length; i++)
			itemNodes.add(waypointItems[i]);

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Add an edge between two nodes.
 *	@param from the node at which the edge begins
 *	@param to the node at which the edge ends
 *	@param bidirectional if true, adds an edge from start to end node
 *	and vice versa
 *	@return true if the WaypointMap is unlocked and the addition of
 *	the edge was successful, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean addEdge(Waypoint from, Waypoint to, boolean bidirectional)
	{
		if(locked)
			return false;

		try
		{
			from.addEdge(to);
			nullifyMatrices();

			if(bidirectional)
				to.addEdge(from);
		}
		catch(Exception e)
		{
			return false;
		}

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Add an edge between two nodes.
 *	@param fromIndex the index of the node at which the edge begins
 *	@param toIndex the index  of the node at which the edge ends
 *	@param bidirectional if true, adds an edge from start to end node
 *	and vice versa
 *	@return true if the WaypointMap is unlocked and the addition of
 *	the edge was successful, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean addEdge(int fromIndex, int toIndex, boolean bidirectional)
	{
		if(locked)
			return false;

		try
		{
			nullifyMatrices();
			return addEdge((Waypoint)nodes.elementAt(fromIndex), (Waypoint)nodes.elementAt(toIndex), bidirectional);
		}
		catch(Exception e)
		{
			return false;
		}
	}

/*-------------------------------------------------------------------*/
/**	Save the current WaypointMap object to file. Serializes the object
 *	and saves it to disk at the specified location; this allows waypoint
 *	maps to be re-used without having to be regenerated.
 *	@param filename and file name under which to save the map
 *	@return true if the file was successfully saved, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean saveMap(String filename)
	{
		try
		{
			(new ObjectOutputStream(new FileOutputStream(filename))).writeObject(this);
		}
		catch(Exception e)
		{
			return false;
		}

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Load and return a WaypointMap stored at the spcified location.
 *	@param filename the path and filename of the saved WaypointMap
 *	@return the WaypointMap if successfully deserialized, null otherwise */
/*-------------------------------------------------------------------*/
	public static WaypointMap loadMap(String filename)
	{
		try
		{
			return (WaypointMap)(new ObjectInputStream(new FileInputStream(filename))).readObject();
		}
		catch(Exception e)
		{
			return null;
		}
	}

/*-------------------------------------------------------------------*/
/**	Nullify the WaypointMap's location, edge, path and cost matrices,
 *	to preserve consistency. This method is called whenever an operation
 *	is performed which alters the structure of the graph. The various
 *	traversal methods are thereby notified that they must recalculate
 *	the costs from each node to every other, and recompute the shortest
 *	paths between them.
 *	@see #generateWaypointMatrix
 *	@see #getWaypointMatrix
 *	@see #generateEdgeMatrix
 *	@see #getEdgeMatrix
 *	@see #generateCostAndPathMatrices */
/*-------------------------------------------------------------------*/
	private void nullifyMatrices()
	{
		edgeMatrix = null;
		waypointMatrix = null;

		itemNodeIndices = null;
		itemNodeWaypoints = null;

		costMatrix = null;
		predMatrix = null;
	}

/*-------------------------------------------------------------------*/
/**	Get a node from the node array.
 *	@param index the index of the node in the map's node array
 *	@return the Waypoint at the specified index */
/*-------------------------------------------------------------------*/
	public Waypoint getNode(int index)
	{
		return (Waypoint)nodes.elementAt(index);
	}

/*-------------------------------------------------------------------*/
/**	Get the full list of all nodes.
 *	@return an array of Waypoints containing all the nodes in the graph */
/*-------------------------------------------------------------------*/
	public Waypoint[] getAllNodes()
	{
		Waypoint[] nodeArray = new Waypoint[nodes.size()];
		nodes.toArray(nodeArray);

		return nodeArray;
	}

/*-------------------------------------------------------------------*/
/**	Get the list of all waypoint locations in the form of an n-by-3
 *	matrix of floats, where n is the number of waypoints. This is
 *	particularly convenient for reading the raw WaypointMap data into MatLab.
 *	@return an n-by-3 matrix of floats, representing the locations of
 *	each node in the topological map
 *	@see #generateWaypointMatrix */
/*-------------------------------------------------------------------*/
	public float[][] getWaypointMatrix()
	{
		if(waypointMatrix != null)
			return (float[][])waypointMatrix.clone();

		generateWaypointMatrix();

		return (float[][])waypointMatrix.clone();
	}

/*-------------------------------------------------------------------*/
/**	Get the indices of the nodes at which items are present. This method
 *	is particularly useful for reading the node item data into MatLab
 *	for visualisation. Note that MatLab indexing starts at 1 rather than
 *	0, which means that the indices must be incremented if used within
 *	the MatLab environment; for convenience, this method will optionally
 *	perform this action if the boolean argument is set to true.
 *	@param increment increment all indices, for use in MatLab
 *	@return a list of the indices of nodes at which items are present */
/*-------------------------------------------------------------------*/
	public int[] getItemNodeIndices(boolean increment)
	{
		if(itemNodeIndices == null)
			generateItemInfo();

		int[] dupeArray = (int[])itemNodeIndices.clone();

		if(increment)
		{
			for(int i = 0; i < dupeArray.length; i++)
				dupeArray[i]++;
		}

		return dupeArray;
	}

/*-------------------------------------------------------------------*/
/**	Get the Waypoint nodes at which items are present. This is particularly
 *	convenient for use with subsequent path-finding methods.
 *	@return a Waypoint array of the nodes at which items are present */
/*-------------------------------------------------------------------*/
	public Waypoint[] getItemNodeWaypoints()
	{
		if(itemNodeWaypoints == null)
			generateItemInfo();

		return (Waypoint[])itemNodeWaypoints.clone();
	}

/*-------------------------------------------------------------------*/
/**	Get the item types, in the form of their inventory indices, at each
 *	item node. This is particularly useful for reading item type data
 *	into MatLab.
 *	@return an array containing the inventory indices of the items
 *	at each item node */
/*-------------------------------------------------------------------*/
	public int[] getItemNodeTypes()
	{
		if(itemNodeTypes == null)
			generateItemInfo();

		return (int[])itemNodeTypes.clone();
	}

/*-------------------------------------------------------------------*/
/**	Generate and store lists of the item nodes, associated indices, and
 *	entity types. */
/*-------------------------------------------------------------------*/
	private void generateItemInfo()
	{
		itemNodeTypes = new int[itemNodes.size()];
		itemNodeIndices = new int[itemNodes.size()];
		itemNodeWaypoints = new Waypoint[itemNodes.size()];

		WaypointItem wpi = null;

		for(int i = 0; i < itemNodeIndices.length; i++)
		{
			wpi = (WaypointItem)itemNodes.elementAt(i);

			itemNodeWaypoints[i] = wpi.getNode();
			itemNodeIndices[i] = indexOf(wpi.getNode());
			itemNodeTypes[i] = wpi.getItemInventoryIndex();
		}
	}

/*-------------------------------------------------------------------*/
/**	Get an n-by-n matrix indicating the shortest distance between each
 *	pair of points.
 *	@return an n-by-n matrix of floats, representing the shortest
 *	distance between each pair of points
 *	@see #generateCostAndPathMatrices */
/*-------------------------------------------------------------------*/
	public float[][] getCostMatrix()
	{
		if(costMatrix != null)
			return (float[][])costMatrix.clone();

		generateCostAndPathMatrices();

		return (float[][])costMatrix.clone();
	}

/*-------------------------------------------------------------------*/
/**	Get an n-by-n matrix indicating the predecessor of each node in the
 *	shortest path between each pair of nodes. The path can be reconstructed
 *	by reading this sequence in reverse order.
 *	@return an n-by-n predecessor matrix
 *	@see #generateCostAndPathMatrices */
/*-------------------------------------------------------------------*/
	public int[][] getPredecessorMatrix()
	{
		if(predMatrix != null)
			return (int[][])predMatrix.clone();

		generateCostAndPathMatrices();

		return (int[][])predMatrix.clone();
	}

/*-------------------------------------------------------------------*/
/**	Generate and store the Waypoint matrix, if it does not already exist. */
/*-------------------------------------------------------------------*/
	private void generateWaypointMatrix()
	{
		waypointMatrix = new float[nodes.size()][3];

		for(int i = 0; i < nodes.size(); i++)
			waypointMatrix[i] = getNode(i).getPosition().toArray();
	}

/*-------------------------------------------------------------------*/
/**	Get the list of all edges between nodes, in the form of an n-by-n
 *	matrix of booleans, where n is the number of nodes. This is
 *	particularly convenient for reading the raw edge data into MatLab.
 *	@return an n-by-n boolean matrix indicating the presence or absence
 *	of an edge between each pair of nodes */
/*-------------------------------------------------------------------*/
	public boolean[][] getEdgeMatrix()
	{
		if(edgeMatrix != null)
			return (boolean[][])edgeMatrix.clone();

		generateEdgeMatrix();

		return (boolean[][])edgeMatrix.clone();
	}

/*-------------------------------------------------------------------*/
/**	Generate and store the edge matrix, if it does not already exist. */
/*-------------------------------------------------------------------*/
	private void generateEdgeMatrix()
	{
		Waypoint curNode = null;
		Waypoint[] curEdges = null;
		edgeMatrix = new boolean[nodes.size()][nodes.size()];

		for(int i = 0; i < nodes.size(); i++)
		{
			curNode = (Waypoint)nodes.elementAt(i);
			curEdges = curNode.getEdges();

			for(int j = 0; j < curEdges.length; j++)
				edgeMatrix[i][nodes.indexOf(curEdges[j])] = true;
		}
	}

/*-------------------------------------------------------------------*/
/**	Get the closest waypoint to a given location. For convenience,
 *	this method is overloaded to accept both Origin or Vector3f parameters.
 *	@param location the location from which to measure waypoint distances
 *	@return the Waypoint closest to the given position */
/*-------------------------------------------------------------------*/
	public Waypoint findClosestWaypoint(Origin location)
	{
		return findClosestWaypoint(new Vector3f(location));
	}

/*-------------------------------------------------------------------*/
/**	Get the closest waypoint to a given location. For convenience,
 *	this method is overloaded to accept both Origin or Vector3f parameters.
 *	@param location the location from which to measure waypoint distances
 *	@return the Waypoint closest to the given position */
/*-------------------------------------------------------------------*/
	public Waypoint findClosestWaypoint(Vector3f location)
	{
		float curDist = 0.0f;
		Waypoint closest = null;
		float minDist = Float.MAX_VALUE;

		for(int i = 0; i < nodes.size(); i++)
		{
			curDist = location.distance(((Waypoint)nodes.elementAt(i)).getPosition());

			if(curDist < minDist)
			{
				minDist = curDist;
				closest = (Waypoint)nodes.elementAt(i);
			}
		}

		return closest;
	}

/*-------------------------------------------------------------------*/
/**	Lock the WaypointMap, to prohibit further alteration of its
 *	constituent nodes. This method also locks all individual nodes,
 *	preventing addition or removal of nodes, edges and item locations. */
/*-------------------------------------------------------------------*/
	public void lockMap()
	{
		if(!locked)
		{
			locked = true;

			for(int i = 0; i < nodes.size(); i++)
				((Waypoint)nodes.elementAt(i)).lockNode();

			generateMatrices();
		}
	}

/*-------------------------------------------------------------------*/
/**	Unlock the WaypointMap, permitting alteration of constituent elements. */
/*-------------------------------------------------------------------*/
	public void unlockMap()
	{
		locked = false;

		for(int i = 0; i < nodes.size(); i++)
			((Waypoint)nodes.elementAt(i)).unlockNode();
	}

/*-------------------------------------------------------------------*/
/**	Check whether or not the WaypointMap is locked.
 *	@return true if locked, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isLocked()
	{
		return locked;
	}

/*-------------------------------------------------------------------*/
/**	Generate the waypoint, edge, cost and path matrices. Called whenever
 *	any of the graph-traversal methods finds that the matrices have been
 *	nullified - that is, when any changes are made to the structure of
 *	the graph.
 *	@see #nullifyMatrices
 *	@see #generateEdgeMatrix
 *	@see #generateWaypointMatrix
 *	@see #generateCostAndPathMatrices */
/*-------------------------------------------------------------------*/
	private void generateMatrices()
	{
		generateItemInfo();
		generateEdgeMatrix();
		generateWaypointMatrix();
		generateCostAndPathMatrices();
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified Waypoint at which an item
 *	of the the given type resides. The item type is specified by inventory
 *	index; see the Inventory class for a list of inventory constants
 *	@param currentPos the waypoint from which to seach
 *	@param itemInventoryIndex the inventory index corresponding to the
 *	item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	public Waypoint findClosestItem(Waypoint currentPos, int itemInventoryIndex)
	{
		return findClosestItem(indexOf(currentPos), itemInventoryIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified position at which an item
 *	of the the given type resides. The item type is specified by inventory
 *	index; see the Inventory class for a list of inventory constants
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param itemInventoryIndex the inventory index corresponding to the
 *	item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	public Waypoint findClosestItem(Origin currentPos, int itemInventoryIndex)
	{
		return findClosestItem(indexOf(findClosestWaypoint(currentPos)), itemInventoryIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified position at which an item
 *	of the the given type resides. The item type is specified by inventory
 *	index; see the Inventory class for a list of inventory constants
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param itemInventoryIndex the inventory index corresponding to the
 *	item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	public Waypoint findClosestItem(Vector3f currentPos, int itemInventoryIndex)
	{
		return findClosestItem(indexOf(findClosestWaypoint(currentPos)), itemInventoryIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified Waypoint at which an item
 *	of the the specified type resides. The category, type and subtype
 *	are typically passed using the constants found in the Entity class.
 *	@param currentPos the Waypoint from which to search
 *	@param cat the category of item to search for
 *	@param type the type of item to search for
 *	@param subType the subtype of item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	public Waypoint findClosestItem(Waypoint currentPos, String cat, String type, String subType)
	{
		return findClosestItem(indexOf(currentPos), cat, type, subType);
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified position at which an item
 *	of the the specified type resides. The category, type and subtype
 *	are typically passed using the constants found in the Entity class.
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param cat the category of item to search for
 *	@param type the type of item to search for
 *	@param subType the subtype of item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	public Waypoint findClosestItem(Origin currentPos, String cat, String type, String subType)
	{
		return findClosestItem(indexOf(findClosestWaypoint(currentPos)), cat, type, subType);
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified position at which an item
 *	of the the specified type resides. The category, type and subtype
 *	are typically passed using the constants found in the Entity class.
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param cat the category of item to search for
 *	@param type the type of item to search for
 *	@param subType the subtype of item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	public Waypoint findClosestItem(Vector3f currentPos, String cat, String type, String subType)
	{
		return findClosestItem(indexOf(findClosestWaypoint(currentPos)), cat, type, subType);
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified position at which an item
 *	of the the given type resides. The item type is specified by inventory
 *	index; see the Inventory class for a list of inventory constants
 *	@param fromIndex the index of the node at which to start searching
 *	@param itemInventoryIndex the inventory index corresponding to the
 *	item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	public Waypoint findClosestItem(int fromIndex, int itemInventoryIndex)
	{
		int toIndex = -1;
		WaypointItem wpItem = null;
		float minDist = Float.MAX_VALUE, curDist = 0.0f;

		if(costMatrix == null)
			generateCostAndPathMatrices();

		for(int i = 0; i < itemNodes.size(); i++)
		{
			wpItem = (WaypointItem)itemNodes.elementAt(i);
			curDist = costMatrix[fromIndex][indexOf(wpItem.getNode())];

			if(wpItem.getItemInventoryIndex() == itemInventoryIndex && curDist < minDist)
			{
				minDist = curDist;
				toIndex = indexOf(wpItem.getNode());
			}
		}

		return (Waypoint)nodes.elementAt(toIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the closest Waypoint to the specified position at which an item
 *	of the the specified type resides. The category, type and subtype
 *	are typically passed using the constants found in the Entity class.
 *	@param fromIndex the index of the node at which to start searching
 *	@param cat the category of item to search for
 *	@param type the type of item to search for
 *	@param subType the subtype of item to search for
 *	@return the closest Waypoint at which a matching item exists
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	public Waypoint findClosestItem(int fromIndex, String cat, String type, String subType)
	{
		int toIndex = -1;
		WaypointItem wpItem = null;
		float minDist = Float.MAX_VALUE, curDist = 0.0f;

		if(costMatrix == null)
			generateCostAndPathMatrices();

		for(int i = 0; i < itemNodes.size(); i++)
		{
			wpItem = (WaypointItem)itemNodes.elementAt(i);
			curDist = costMatrix[fromIndex][indexOf(wpItem.getNode())];

			if(wpItem.isEntityType(cat, type, subType) && curDist < minDist)
			{
				minDist = curDist;
				toIndex = indexOf(wpItem.getNode());
			}
		}

		return (Waypoint)nodes.elementAt(toIndex);
	}

/*-------------------------------------------------------------------*/
/**	Find the shortest path between two Waypoints. This uses the previously-
 *	generated cost and predecessor matrices.
 *	@param from the starting Waypoint
 *	@param to the ending Waypoint
 *	@return a Waypoint array indicating the shortest path */
/*-------------------------------------------------------------------*/
	public Waypoint[] findShortestPath(Waypoint from, Waypoint to)
	{
		return findShortestPath(nodes.indexOf(from), nodes.indexOf(to));
	}

/*-------------------------------------------------------------------*/
/**	Find the shortest path between the closest Waypoints to the specified
 *	locations. This uses the previously-generated cost and predecessor
 *	matrices.
 *	@param from the starting position, usually the agent's current location
 *	@param to the position to which we need a path
 *	@return a Waypoint array indicating the shortest path between the
 *	two Waypoints closest to the start and end positions */
/*-------------------------------------------------------------------*/
	public Waypoint[] findShortestPath(Origin from, Origin to)
	{
		return findShortestPath(nodes.indexOf(findClosestWaypoint(from)), nodes.indexOf(findClosestWaypoint(to)));
	}

/*-------------------------------------------------------------------*/
/**	Find the shortest path between the closest Waypoints to the specified
 *	locations. This uses the previously-generated cost and predecessor
 *	matrices.
 *	@param from the starting position, usually the agent's current location
 *	@param to the position to which we need a path
 *	@return a Waypoint array indicating the shortest path between the
 *	two Waypoints closest to the start and end positions */
/*-------------------------------------------------------------------*/
	public Waypoint[] findShortestPath(Vector3f from, Vector3f to)
	{
		return findShortestPath(nodes.indexOf(findClosestWaypoint(from)), nodes.indexOf(findClosestWaypoint(to)));
	}

/*-------------------------------------------------------------------*/
/**	Find the shortest path between two Waypoints. This uses the previously-
 *	generated cost and predecessor matrices.
 *	@param fromIndex the index of the starting Waypoint
 *	@param toIndex the index of the ending Waypoint
 *	@return a Waypoint array indicating the shortest path */
/*-------------------------------------------------------------------*/
	public Waypoint[] findShortestPath(int fromIndex, int toIndex)
	{
		if(costMatrix == null || predMatrix == null)
			generateCostAndPathMatrices();

		int curPred = toIndex;
		Vector predPath = new Vector();

		try
		{	predPath.add(nodes.elementAt(toIndex));

			while((curPred = predMatrix[fromIndex][curPred]) != fromIndex) // backtrack through predecessors
				predPath.insertElementAt(nodes.elementAt(curPred), 0);
	
			predPath.insertElementAt(nodes.elementAt(fromIndex), 0);
	
			return (Waypoint[])predPath.toArray(new Waypoint[0]);
		}
		catch(Exception e)
		{
			return null;
		}
	}

/*-------------------------------------------------------------------*/
/**	Get the path through the waypoint graph from the current Waypoint to
 *	the closest Waypoint at which an item of the the given type resides.
 *	The item type is specified by inventory index; see the Inventory class
 *	for a list of inventory constants
 *	@param currentPos the Waypoint from which to search
 *	@param itemInventoryIndex the inventory index corresponding to the
 *	item to search for
 *	@return a Waypoint array indicating the shortest path
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	public Waypoint[] findShortestPathToItem(Waypoint currentPos, int itemInventoryIndex)
	{
		int fromIndex = indexOf(currentPos);
		int toIndex = indexOf(findClosestItem(fromIndex, itemInventoryIndex));

		if(toIndex == -1)
			return null;

		return findShortestPath(fromIndex, toIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the path through the waypoint graph from the current position to
 *	the closest Waypoint at which an item of the the given type resides.
 *	The item type is specified by inventory index; see the Inventory class
 *	for a list of inventory constants
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param itemInventoryIndex the inventory index corresponding to the
 *	item to search for
 *	@return a Waypoint array indicating the shortest path
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	public Waypoint[] findShortestPathToItem(Origin currentPos, int itemInventoryIndex)
	{
		int fromIndex = indexOf(findClosestWaypoint(currentPos));
		int toIndex = indexOf(findClosestItem(fromIndex, itemInventoryIndex));

		if(toIndex == -1)
			return null;

		return findShortestPath(fromIndex, toIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the path through the waypoint graph from the current position to
 *	the closest Waypoint at which an item of the the given type resides.
 *	The item type is specified by inventory index; see the Inventory class
 *	for a list of inventory constants
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param itemInventoryIndex the inventory index corresponding to the
 *	item to search for
 *	@return a Waypoint array indicating the shortest path
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	public Waypoint[] findShortestPathToItem(Vector3f currentPos, int itemInventoryIndex)
	{
		int fromIndex = indexOf(findClosestWaypoint(currentPos));
		int toIndex = indexOf(findClosestItem(fromIndex, itemInventoryIndex));

		if(toIndex == -1)
			return null;

		return findShortestPath(fromIndex, toIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the path through the waypoint graph from the current Waypoint to
 *	the closest Waypoint at which an item of the the given type resides.
 *	The category, type and subtype are typically passed using the
 *	constants found in the Entity class.
 *	@param currentPos the Waypoint from which to search
 *	@param cat the category of item to search for
 *	@param type the type of item to search for
 *	@param subType the subtype of item to search for
 *	@return a Waypoint array indicating the shortest path
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	public Waypoint[] findShortestPathToItem(Waypoint currentPos, String cat, String type, String subType)
	{
		int fromIndex = indexOf(currentPos);
		int toIndex = indexOf(findClosestItem(fromIndex, cat, type, subType));

		if(fromIndex == -1 || toIndex == -1)
			return null;

		return findShortestPath(fromIndex, toIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the path through the waypoint graph from the current position to
 *	the closest Waypoint at which an item of the the given type resides.
 *	The category, type and subtype are typically passed using the
 *	constants found in the Entity class.
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param cat the category of item to search for
 *	@param type the type of item to search for
 *	@param subType the subtype of item to search for
 *	@return a Waypoint array indicating the shortest path
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	public Waypoint[] findShortestPathToItem(Origin currentPos, String cat, String type, String subType)
	{
		int fromIndex = indexOf(findClosestWaypoint(currentPos));
		int toIndex = indexOf(findClosestItem(fromIndex, cat, type, subType));

		if(toIndex == -1)
			return null;

		return findShortestPath(fromIndex, toIndex);
	}

/*-------------------------------------------------------------------*/
/**	Get the path through the waypoint graph from the current position to
 *	the closest Waypoint at which an item of the the given type resides.
 *	The category, type and subtype are typically passed using the
 *	constants found in the Entity class.
 *	@param currentPos the position from which to search (generally the
 *	agent's current location)
 *	@param cat the category of item to search for
 *	@param type the type of item to search for
 *	@param subType the subtype of item to search for
 *	@return a Waypoint array indicating the shortest path
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	public Waypoint[] findShortestPathToItem(Vector3f currentPos, String cat, String type, String subType)
	{
		int fromIndex = indexOf(findClosestWaypoint(currentPos));
		int toIndex = indexOf(findClosestItem(fromIndex, cat, type, subType));

		if(toIndex == -1)
			return null;

		return findShortestPath(fromIndex, toIndex);
	}

/*-------------------------------------------------------------------*/
/**	Generate the cost matrix (distances between each pair of nodes)
 *	and predecessor/path matrix (which node precedes the current node
 *	in the shortest path between each pair of nodes) by applying Floyd's
 *	algorithm to the adjacency matrix (distances between each directly-
 *	connected pair of nodes). Thereafter, the traversal methods can
 *	simply consult these matrices to find the shortest paths between
 *	any two arbitrary nodes.
 *	@see #generateWaypointMatrix
 *	@see #getWaypointMatrix
 *	@see #generateEdgeMatrix
 *	@see #getEdgeMatrix */
/*-------------------------------------------------------------------*/
	private void generateCostAndPathMatrices()
	{
		float dist = 0.0f;

		if(edgeMatrix == null)
			generateEdgeMatrix();

		costMatrix = new float[nodes.size()][nodes.size()];
		predMatrix = new int[nodes.size()][nodes.size()];

		for(int i = 0; i < nodes.size(); i++)
		{
			for(int j = 0; j < nodes.size(); j++)
			{
				if(edgeMatrix[i][j])
				{
					dist = (((Waypoint)nodes.elementAt(i)).getPosition().distance(((Waypoint)nodes.elementAt(j)).getPosition()));

					predMatrix[i][j] = i;
					costMatrix[i][j] = dist;
				}
				else if(i != j)
				{
					predMatrix[i][j] = -1;
					costMatrix[i][j] = Float.POSITIVE_INFINITY;
				}
			}
		}

		for(int c = 0; c < nodes.size(); c++) // Floyd's Algorithm
		{
			for(int a = 0; a < nodes.size(); a++)
			{
				for(int b = 0; b < nodes.size(); b++)
				{
					if(!Float.isInfinite(costMatrix[a][c]) && !Float.isInfinite(costMatrix[c][b]))
					{
						if(Float.isInfinite(costMatrix[a][b]) || costMatrix[a][c] + costMatrix[c][b] < costMatrix[a][b])
						{
							predMatrix[a][b] = predMatrix[c][b];
							costMatrix[a][b] = costMatrix[a][c] + costMatrix[c][b];
						}
					}
				}
			}
		}
	}
}
