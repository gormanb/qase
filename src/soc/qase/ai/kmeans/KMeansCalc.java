//--------------------------------------------------
// Name:			KMeansCalc.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.ai.kmeans;

import java.util.Arrays;

import soc.qase.tools.Utils;
import soc.qase.tools.vecmath.Vector3f;

/*-------------------------------------------------------------------*/
/**	The KMeansCalc class is designed to give students an insight into
 *	some basic principles of clustering techniques, and how they can be
 *	used. It is also heavily employed by various methods of the WaypointMap
 *	and WaypointMapGenerator classes.
 *	@see KMeansData
 *	@see soc.qase.ai.waypoint.WaypointMap
 *	@see soc.qase.ai.waypoint.WaypointMapGenerator */
/*-------------------------------------------------------------------*/
public class KMeansCalc
{
	private int[] anchorIndices = null;

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public KMeansCalc()
	{	}

/*-------------------------------------------------------------------*/
/**	Compute K centroids for a given dataset.
 *	@param numCentroids the number of clusters in which to represent the
 *	dataset
 *	@param data the data to be clustered
 *	@return a KMeansData object, contatining both the cluster centroids
 *	and a table indicating which cluster each data sample belongs to */
/*-------------------------------------------------------------------*/
	public KMeansData compute(int numCentroids, Vector3f[] data)
	{
		return compute(numCentroids, data, null);
	}

/*-------------------------------------------------------------------*/
/**	Compute K centroids for a given dataset, while holding a predefined
 *	set of 'anchor' centroids constant. This is typically used to fix
 *	the locations of items in the game world, and arrange an appropriate
 *	clustering around them.
 *	@param numCentroids the number of clusters in which to represent the
 *	dataset
 *	@param data the data to be clustered
 *	@param anchors the anchor centroids; these indicate important points
 *	in the dataset that should be held constant during clustering
 *	@return a KMeansData object, contatining both the cluster centroids
 *	and a table indicating which cluster each data sample belongs to */
/*-------------------------------------------------------------------*/
	public KMeansData compute(int numCentroids, Vector3f[] data, Vector3f[] anchors)
	{
		Vector3f[] centroids = getInitialCentroids(numCentroids, data);

		if(anchors != null)
			insertAnchorCentroids(centroids, anchors);

		Vector3f[] finalCentroids = doIteration(data, centroids, anchors != null);
		int[] clusterID = getClusterAllocations(data, finalCentroids, null);

		return new KMeansData(finalCentroids, clusterID, (anchors != null ? anchorIndices : null));
	}

/*-------------------------------------------------------------------*/
/**	A convenience method which automatically instantiates an anonymous
 *	KMeansCalc object, performs the clustering, and returns the result.
 *	@param numCentroids the number of clusters in which to represent the
 *	dataset
 *	@param data the data to be clustered
 *	@return a KMeansData object, contatining both the cluster centroids
 *	and a table indicating which cluster each data sample belongs to */
/*-------------------------------------------------------------------*/
	public static KMeansData doKMeans(int numCentroids, Vector3f[] data)
	{
		KMeansCalc kmCalc = new KMeansCalc();
		return kmCalc.compute(numCentroids, data, null);
	}

/*-------------------------------------------------------------------*/
/**	A convenience method which automatically instantiates an anonymous
 *	KMeansCalc object, performs the clustering, and returns the result.
 *	@param numCentroids the number of clusters in which to represent the
 *	dataset
 *	@param data the data to be clustered
 *	@param anchors the anchor centroids; these indicate important points
 *	in the dataset that should be held constant during clustering
 *	@return a KMeansData object, contatining both the cluster centroids
 *	and a table indicating which cluster each data sample belongs to */
/*-------------------------------------------------------------------*/
	public static KMeansData doKMeans(int numCentroids, Vector3f[] data, Vector3f[] anchors)
	{
		KMeansCalc kmCalc = new KMeansCalc();
		return kmCalc.compute(numCentroids, data, anchors);
	}

/*-------------------------------------------------------------------*/
/**	The core of the KMeanCalc class, this method actually performs the
 *	clustering.
 *	@param data the data to be clustered
 *	@param centroids the locations of the initial cluster centroids
 *	@param withAnchors indicates whether or not anchors are present in
 *	the set of initial centroids
 *	@return an array of Vector3f objects indicating the final locations
 *	of the centroids at the end of the clustering process */
/*-------------------------------------------------------------------*/
	private Vector3f[] doIteration(Vector3f[] data, Vector3f[] centroids, boolean withAnchors)
	{
		int[] lastClusterNums = new int[data.length], curClusterNums = new int[data.length];

		do
		{
			Utils.copyArray(curClusterNums, lastClusterNums);
			getClusterAllocations(data, centroids, curClusterNums);

			for(int i = 0; i < centroids.length; i++)
			{
				float numPoints = 0.0f;
				float xsum = 0.0f, ysum = 0.0f, zsum = 0.0f;

				if(!withAnchors || !isAnchorIndex(i))
				{
					for(int j = 0; j < curClusterNums.length; j++)
					{
						if(curClusterNums[j] == i)
						{
							xsum += data[j].x;
							ysum += data[j].y;
							zsum += data[j].z;
							numPoints += 1.0f;
						}
					}

					if(numPoints > 0)
					{
						xsum /= numPoints;
						ysum /= numPoints;
						zsum /= numPoints;

						centroids[i].set(xsum, ysum, zsum);
					}
				}
			}
		}
		while(!Arrays.equals(lastClusterNums, curClusterNums));

		return centroids;
	}

/*-------------------------------------------------------------------*/
/**	Select K initial centroids. Typically, this chooses K temporally
 *	equidistant samples from the full dataset, assuming that the data
 *	is a demo of a player moving around the game world.
 *	@param numCentroids the number of initial centroids to choose
 *	@param data the full dataset
 *	@return an array of Vector3f objects indicating the locations
 *	of the initial centroids before the clustering process */
/*-------------------------------------------------------------------*/
	private Vector3f[] getInitialCentroids(int numCentroids, Vector3f[] data)
	{
		int skip = data.length / numCentroids;
		Vector3f[] initialCentroids = new Vector3f[numCentroids];

		for(int i = 0; i < initialCentroids.length; i++)
			initialCentroids[i] = data[i * skip];

		return initialCentroids;
	}

/*-------------------------------------------------------------------*/
/**	Inserts the predefined anchor centroids into the set of initial clusters,
 *	by substituting each anchor for the closest-matching centroid.
 *	@param centroids the set of initial centroids
 *	@param anchors the set of predefined anchors
/*-------------------------------------------------------------------*/
	private void insertAnchorCentroids(Vector3f[] centroids, Vector3f[] anchors)
	{
		anchorIndices = new int[anchors.length];

		for(int i = 0; i < anchorIndices.length; i++)
			anchorIndices[i] = -1;

		for(int i = 0; i < anchors.length; i++)
		{
			float curDist = 0;
			float minDist = Float.MAX_VALUE;

			for(int j = 0; j < centroids.length; j++)
			{
				curDist = anchors[i].distance(centroids[j]);
	
				if(curDist < minDist && !isAnchorIndex(j))
				{
					minDist = curDist;
					anchorIndices[i] = j;
				}
			}
		}

		for(int i = 0; i < anchorIndices.length; i++)
			centroids[anchorIndices[i]] = anchors[i];
	}

/*-------------------------------------------------------------------*/
/**	Checks whether a given centroid is an anchor.
 *	@param num the index of the centroid
 *	@return true if the specified centroid is an anchor, false otherwise */
/*-------------------------------------------------------------------*/
	private boolean isAnchorIndex(int num)
	{
		for(int i = 0; i < anchorIndices.length; i++)
		{
			if(anchorIndices[i] == num)
				return true;
		}

		return false;
	}

/*-------------------------------------------------------------------*/
/**	Finds the closest centroid to a given point.
 *	@param centroids the locations of the cluster centroids
 *	@param point the point from which to measure the distance
 *	@return the index in the centroid array of the centroid closest
 *	to the specified point*/
/*-------------------------------------------------------------------*/
	private int findClosestCentroid(Vector3f[] centroids, Vector3f point)
	{
		int index = 0;
		float curDist = 0;
		float minDist = Float.MAX_VALUE;

		for(int i = 0; i < centroids.length; i++)
		{
			curDist = centroids[i].distance(point);

			if(curDist < minDist)
			{
				index = i;
				minDist = curDist;
			}
		}

		return index;
	}

/*-------------------------------------------------------------------*/
/**	Find the list of cluster allocations - that is, which cluster each
 *	sample in the dataset falls into.
 *	@param data the dataset
 *	@param centroids the locations of the cluster centroids
 *	@param curClusterNums the current list of allocations; this will
 *	change on each iteration of the clustering loop
 *	@return an array of integers indicating the cluster to which each
 *	data sample belongs */
/*-------------------------------------------------------------------*/
	private int[] getClusterAllocations(Vector3f[] data, Vector3f[] centroids, int[] curClusterNums)
	{
		if(curClusterNums == null)
			curClusterNums = new int[data.length];

		for(int i = 0; i < data.length; i++)
			curClusterNums[i] = findClosestCentroid(centroids, data[i]);

		return curClusterNums;
	}
}
