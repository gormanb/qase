//--------------------------------------------------
// Name:			WaypointMapGenerator.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.ai.waypoint;

import java.util.Vector;

import soc.qase.ai.kmeans.KMeansCalc;
import soc.qase.ai.kmeans.KMeansData;
import soc.qase.file.dm2.DM2Parser;
import soc.qase.state.Entity;
import soc.qase.state.World;
import soc.qase.tools.vecmath.Vector3f;

/*-------------------------------------------------------------------*/
/**	The WaypointMapGenerator class allows a WaypointMap to be automatically
 *	generated from a DM2 recording, using techniques explained in our paper.
 *	The set of all player positions are first read from the file, and are
 *	then clustered to produce a set of n Waypoints. Optionally, the locations
 *	at which items were collected in the demo are used to hold certain cluster
 *	centroids constant during this process, ensuring that nodes will be allocated
 *	to the positions at which items reside in the map. Edges are drawn between
 *	pairs of waypoints wherever the player was observed to move directly from
 *	one to the other. Finally, the Waypoints at which items reside are marked as
 *	such. All the user need do to create a full navigation map, therefore, is
 *	record himself running around the given level, collecting any necessary
 *	items, and supply the DM2 file to the WaypointMapGenerator class.
 *	@see soc.qase.file.dm2.DM2Parser
 *	@see soc.qase.ai.kmeans.KMeansCalc
 *	@see Waypoint
 *	@see WaypointItem
 *	@see WaypointMap */
/*-------------------------------------------------------------------*/
public class WaypointMapGenerator
{
/*-------------------------------------------------------------------*/
/**	Generate and return a WaypointMap by analysing a recorded DM2 demo.
 *	This method also records the positions at which items were collected
 *	in the demo, and holds these locations constant through the clustering
 *	process.
 *	@param dm2File filename of the DM2 demo to analyse
 *	@param fNumNodes the number of nodes to generate for the WaypointMap.
 *	If this number is less than 1, it is treated as a percentage of the
 *	total number of observed player positions. If it is 1 or greater, it
 *	is treated as an absolute number of nodes to generate.
 *	@return the resulting WaypointMap */
/*-------------------------------------------------------------------*/
	public static WaypointMap generate(String dm2File, float fNumNodes)
	{
		int numNodes = 0;

		World world = null;
		WaypointMap wpMap = new WaypointMap();
		DM2Parser dm2p = new DM2Parser(dm2File);

		Vector isAlive = new Vector();
		Vector playerPos = new Vector();
		Vector waypointItems = new Vector();
		Vector pickupPos = new Vector();

		Vector3f currentPos = null, lastPos = null, itemPos = null;

		while((world = dm2p.getNextWorld()) != null)
		{
			currentPos = new Vector3f(world.getPlayer().getPlayerMove().getOrigin());

			if(lastPos == null || !currentPos.equals(lastPos))
			{
				playerPos.add(currentPos);
				isAlive.add(new Boolean(world.getPlayer().isAlive()));

				if(world.getPickupEntityIndex() != -1 && !duplicateItemNode(pickupPos, (itemPos = new Vector3f(world.getPickupEntity().getOrigin()))))
				{
					pickupPos.add(itemPos);
					waypointItems.add(world.getPickupEntity());
				}
			}

			lastPos = currentPos;
		}

		Vector3f[] playerPos3f = (Vector3f[])playerPos.toArray(new Vector3f[0]);
		Vector3f[] pickupPos3f = (Vector3f[])pickupPos.toArray(new Vector3f[0]);

		fNumNodes = Math.abs(fNumNodes);
		numNodes = Math.min(Math.round((fNumNodes < 1 ? fNumNodes * playerPos3f.length : fNumNodes)), playerPos3f.length);

		KMeansData kmResults = KMeansCalc.doKMeans(numNodes, playerPos3f, pickupPos3f);

		wpMap.addNode(kmResults.centroids);

		for(int i = 1; i < kmResults.clusterID.length; i++)
		{
			if(kmResults.clusterID[i] != kmResults.clusterID[i-1] && ((Boolean)isAlive.elementAt(i-1)).booleanValue())
				wpMap.addEdge(kmResults.clusterID[i-1], kmResults.clusterID[i], false);
		}

		wpMap.markItemNodes(kmResults.anchorClusterID, (Entity[])waypointItems.toArray(new Entity[0]), true);

		wpMap.lockMap();
		return wpMap;
	}

	private static boolean duplicateItemNode(Vector itemNodes, Vector3f itemPos)
	{
		for(int i = 0; i < itemNodes.size(); i++)
		{
			if(itemPos.equals((Vector3f)itemNodes.elementAt(i)))
				return true;
		}

		return false;
	}
}
