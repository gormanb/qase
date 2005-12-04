//---------------------------------------------------------------------
// Name:			PAKHeader.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.pak;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Wrapper class for the header of the PAK archive. Contains the
 *	location in the archive of the directory lising, its length, and
 *	the total number of directories.
 *	@see PAKDirectoryTree */
/*-------------------------------------------------------------------*/
public class PAKHeader
{
	public int numDirectories;
	public int directoryOffset;
	public int directoryLength;

	private int identity;
	private static final int PAK_HEADER_ID = (('K'<<24)+('C'<<16)+('A'<<8)+'P');

/*-------------------------------------------------------------------*/
/**	Extract the directory listing information from the PAK archive's
 *	header.
 *	@param headerData the PAK archive's raw header data */
/*-------------------------------------------------------------------*/
	public PAKHeader(byte[] headerData)
	{
		identity = Utils.intValue(headerData, 0);
		directoryOffset = Utils.intValue(headerData, 4);
		directoryLength = Utils.intValue(headerData, 8);

		numDirectories = directoryLength / 64;
	}

/*-------------------------------------------------------------------*/
/**	Validate that the file is a real PAK archive.
 *	@return true if the file is a PAK archive, false otherwise. */
/*-------------------------------------------------------------------*/
	public boolean validatePAK()
	{
		return identity == PAK_HEADER_ID;
	}
}

