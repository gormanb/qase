//--------------------------------------------------
// Name:			KMeansData.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.ai.kmeans;

import soc.qase.tools.vecmath.Vector3f;

/*-------------------------------------------------------------------*/
/**	A wrapper class used to hold the results of the clustering process.
 *	@see KMeansCalc */
/*-------------------------------------------------------------------*/
public class KMeansData
{
	/** The cluster to which each sample in the dataset is allocated */
	public int[] clusterID = null;
	/** The set of cluster centroids */
	public Vector3f[] centroids = null;
	/** The indices of those centroids which acted as anchors during clustering */
	public int[] anchorClusterID = null;

/*-------------------------------------------------------------------*/
/**	Populates the KMeansData object.
 *	@param centroidPos the locations of the centroids
 *	@param cID the cluster allocations
 *	@param aCID the anchor indices */
/*-------------------------------------------------------------------*/
	public KMeansData(Vector3f[] centroidPos, int[] cID, int[] aCID)
	{
		clusterID = cID;
		centroids = centroidPos;
		
		if(aCID != null)
		{
			anchorClusterID = new int[aCID.length];

			for(int i = 0; i < anchorClusterID.length; i++)
				anchorClusterID[i] = aCID[i];
		}
	}
}
