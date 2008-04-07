//--------------------------------------------------
// Name:			WaypointItem.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.ai.waypoint;

import java.io.Serializable;
import soc.qase.state.Entity;

/*-------------------------------------------------------------------*/
/**	Stores information about the item located at a particular node in
 *	the Waypoint map. Generally instantiated and used by the markItemNodes
 *	methods in the WaypointMap class.
 *	@see Waypoint
 *	@see WaypointMap */
/*-------------------------------------------------------------------*/
public class WaypointItem implements Serializable
{
	private Waypoint node = null;
	private int inventoryIndex = -1;
	private String iCategory = null, iType = null, iSubType = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Stores the index of a node in the parent WaypointMap's
 *	node array, and an Entity object representing the item stored at
 *	that location.
 *	@param wp the relevant node in the parent map
 *	@param item an Entity object corresponding to the item located at
 *	this node */
/*-------------------------------------------------------------------*/
	public WaypointItem(Waypoint wp, Entity item)
	{
		node = wp;
		inventoryIndex = item.getInventoryIndex();

		iType = item.getType();
		iSubType = item.getSubType();
		iCategory = item.getCategory();
	}

/*-------------------------------------------------------------------*/
/**	Obtain the index of the node associated with this item.
 *	@return the index of the node in the parent map's node array */
/*-------------------------------------------------------------------*/
	public Waypoint getNode()
	{
		return node;
	}

/*-------------------------------------------------------------------*/
/**	Get the category of the item stored at this node. See the Entity
 *	class for a list of category constants.
 *	@return a String indicating the category of this item
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	public String getCategory()
	{
		return iCategory;
	}

/*-------------------------------------------------------------------*/
/**	Get the type of the item stored at this node. See the Entity
 *	class for a list of type constants.
 *	@return a String indicating the type of this item
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	public String getType()
	{
		return iType;
	}

/*-------------------------------------------------------------------*/
/**	Get the subtype of the item stored at this node. See the Entity
 *	class for a list of type constants.
 *	@return a String indicating the subtype of this item
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	public String getSubType()
	{
		return iSubType;
	}

/*-------------------------------------------------------------------*/
/**	Get the inventory index associated with this item. See the Inventory
 *	class for a list of inventory constants.
 *	@return an integer indicating the inventory index corresponding to this item
 *	@see soc.qase.state.Inventory */
/*-------------------------------------------------------------------*/
	public int getItemInventoryIndex()
	{
		return inventoryIndex;
	}

/*-------------------------------------------------------------------*/
/**	Check whether the item at this node is of the specified type.
 *	Typically, the string constants listed in the Entity class are used
 *	here. Note that null strings act as wildcards, matching any entry.
 *	@param cat the category against which to compare the item
 *	@param type the type against which to compare the item
 *	@param subType the subtype against which to compare the item
 *	@return true if the item matches the filters, false otherwise
 *	@see soc.qase.state.Entity */
/*-------------------------------------------------------------------*/
	public boolean isEntityType(String cat, String type, String subType)
	{
		if((cat == null || (iCategory != null && iCategory.equalsIgnoreCase(cat)))
		&& (type == null || (iType != null && iType.equalsIgnoreCase(type)))
		&& (subType == null || (iSubType != null && iSubType.equalsIgnoreCase(subType))))
		{
			return true;
		}

		return false;
	}
}
