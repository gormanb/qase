//---------------------------------------------------------------------
// Name:			PAKDirectoryTree.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.pak;

import java.util.Set;
import java.util.Vector;
import java.util.HashMap;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Wrapper class for the directory tree of the PAK archive. Contains
 *	a list of all directories in the archive, the list of files in said
 *	directories, their locations, and the length of each. */
/*-------------------------------------------------------------------*/
public class PAKDirectoryTree
{
	private HashMap directory = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Parses the directory listing, building a map of all
 *	folders and their associated files.
 *	@param dirData the raw directory listing */
/*-------------------------------------------------------------------*/
	public PAKDirectoryTree(byte[] dirData)
	{
		directory = new HashMap(dirData.length / 64);

		for(int i = 0; i < dirData.length / 64; i++)
		{
			String filename = Utils.stringValue(dirData, i * 64, Utils.stringLength(dirData, i * 64));

			int[] fileLocation = new int[2];
			fileLocation[0] = Utils.intValue(dirData, i * 64 + 56);
			fileLocation[1] = Utils.intValue(dirData, i * 64 + 60);

			directory.put(filename.toLowerCase(), fileLocation);
		}
	}

/*-------------------------------------------------------------------*/
/**	Get the location of a specific file in the archive.
 *	@param pathAndFilename the path and name of the file in the archive
 *	@return an integer array specifying the location and length of the
 *	file within the arvhive */
/*-------------------------------------------------------------------*/
	public int[] getFileLocation(String pathAndFilename)
	{
		int[] returnInts = null;
		Object val = directory.get(pathAndFilename.toLowerCase());

		if(val != null)
		{
			returnInts = new int[2];
			int[] fileLocation = (int[])val;

			returnInts[0] = fileLocation[0];
			returnInts[1] = fileLocation[1];
		}

		return returnInts;
	}

/*-------------------------------------------------------------------*/
/**	Find the full path of a file in the archive based on the name.
 *	@param filename the name of the file to find
 *	@return the full path to the file, including the name */
/*-------------------------------------------------------------------*/
	public String findFile(String filename)
	{
		Set dirKeys = directory.keySet();
		String[] paths = new String[dirKeys.size()];

		dirKeys.toArray(paths);
		filename = filename.toLowerCase();

		for(int i = 0; i < paths.length; i++)
		{
			if(paths[i].indexOf(filename) != -1)
				return paths[i];
		}

		return null;
	}

/*-------------------------------------------------------------------*/
/**	Find all files in the archive from a partial filename. This method
 *	does not perform full wildcard matching; however, searches for files
 *	with a particular extension (e.g. '*.bsp' or simply '.bsp') are
 *	permitted. Searches are performed on the full path and filename
 *	from the directory listing.
 *	@param partialFilename the partial filename to search for
 *	@return a vector containing all matching filenames */
/*-------------------------------------------------------------------*/
	public Vector findAllFiles(String partialFilename)
	{
		Set dirKeys = directory.keySet();
		String[] paths = new String[dirKeys.size()];

		Vector matches = new Vector();

		dirKeys.toArray(paths);
		partialFilename = partialFilename.toLowerCase();

		if(partialFilename.charAt(0) == '*')
			partialFilename = partialFilename.substring(1);

		for(int i = 0; i < paths.length; i++)
		{
			if(paths[i].indexOf(partialFilename) != -1)
				matches.addElement(paths[i]);
		}

		return matches;
	}
}
