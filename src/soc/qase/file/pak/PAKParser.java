//---------------------------------------------------------------------
// Name:			PAKParser.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.pak;

import java.io.*;
import java.util.Vector;
import java.util.Arrays;
import soc.qase.file.bsp.*;
import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Allows QASE to parse PAK files, and extract their contents. Quake 2
 *	PAKs are archives containing game resources, arranged in full directory
 *	trees and stored in a single consolidated file for convenience. The
 *	PAKParser reads a PAK file, constructs a representation of its
 *	internals, and allows the programmer to search for and retrieve
 *	the various files stored therein. */
/*-------------------------------------------------------------------*/
public class PAKParser
{
	private String fName = null;
	private boolean fileOpen = false;
	private boolean pakRead = false;

	private PAKHeader pakHeader = null;
	private PAKDirectoryTree pakDirectory = null;

	private File pakFile = null;
	private RandomAccessFile bufIn = null;

	private byte[] headerData = null;

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public PAKParser()
	{	}

/*-------------------------------------------------------------------*/
/**	Constructor. Loads the specified PAK file.
 *	@param filename the filename (including path) of the PAK archive */
/*-------------------------------------------------------------------*/
	public PAKParser(String filename)
	{
		load(filename);
	}

/*-------------------------------------------------------------------*/
/**	Loads the specified PAK file.
 *	@param filename the filename (including path) of the PAK archive
 *	@return true if the file was successfully opened, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean load(String filename)
	{
		if(fileOpen)
			close();

		try
		{
			pakFile = new File(filename);
			bufIn = new RandomAccessFile(pakFile, "r");
			fName = new String(filename);

			fileOpen = true;
		}
		catch(IOException ioe)
		{	}

		pakRead = (fileOpen && readPAKData());

		return pakRead;
	}

	private boolean readPAKData()
	{
		return (readPAKHeader() && readPAKDirectory());
	}

	private boolean readPAKHeader()
	{
		headerData = new byte[12];

		try
		{
			bufIn.read(headerData);
			pakHeader = new PAKHeader(headerData);
		}
		catch(IOException ioe)
		{
			return false;
		}

		return pakHeader.validatePAK();
	}

	private boolean readPAKDirectory()
	{
		byte[] dirData = new byte[pakHeader.directoryLength];

		try
		{	bufIn.seek(pakHeader.directoryOffset);
			bufIn.read(dirData);
		}
		catch(IOException ioe)
		{
			return false;
		}

		pakDirectory = new PAKDirectoryTree(dirData);
		return true;
	}

/*-------------------------------------------------------------------*/
/**	Obtains a byte array containing the data from a specific file
 *	extracted from within the PAK archive.
 *	@param pathAndFilename the path and name of the file to retrieve
 *	@return a byte array containing the file data */
/*-------------------------------------------------------------------*/
	public byte[] getFile(String pathAndFilename)
	{
		if(!fileOpen)
			return null;

		if(pathAndFilename.charAt(0) == '/' || pathAndFilename.charAt(0) == '\\')
			pathAndFilename = pathAndFilename.substring(1);

		int[] fileLocation = pakDirectory.getFileLocation(pathAndFilename);

		if(fileLocation == null)
			return null;

		byte[] fileData = new byte[fileLocation[1]];

		try
		{
			bufIn.seek(fileLocation[0]);
			bufIn.read(fileData);
		}
		catch(IOException ioe)
		{
			return null;
		}

		return fileData;
	}

/*-------------------------------------------------------------------*/
/**	Find the full path of a file in the archive based on the name.
 *	@param fileName the name of the file to find
 *	@return the full path to the file, including the name */
/*-------------------------------------------------------------------*/
	public String findFile(String fileName)
	{
		return pakDirectory.findFile(fileName);
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
		return pakDirectory.findAllFiles(partialFilename);
	}

/*-------------------------------------------------------------------*/
/**	Since most PAK operations will involve searching for the BSP file
 *	of the current map (ie the geometry data), this convenience method
 *	searches for and returns the full path and name of a BSP file
 *	within the archive with the same map name as specified. Generally,
 *	this will not match the filename; the method has to manually parse
 *	all .bsp files found in the archive.
 *	@param mapName the name of the map to search for
 *	@return the full path to the associated BSP file */
/*-------------------------------------------------------------------*/
	public String findBSPFile(String mapName)
	{
		String currentFile = null;
		String currentLine = null, entityBlock = "";

		int[] currentFileLocation = null;
		byte[] entityBlockOffset = new byte[4];

		Vector allFiles = findAllFiles(".bsp");

		try
		{
			for(int i = 0; i < allFiles.size(); i++)
			{
				currentFile = (String)allFiles.elementAt(i);
				currentFileLocation = pakDirectory.getFileLocation(currentFile);

				bufIn.seek(currentFileLocation[0] + 8);
				bufIn.read(entityBlockOffset);

				bufIn.seek(currentFileLocation[0] + Utils.intValue(entityBlockOffset, 0));

				while((currentLine = bufIn.readLine()) != null)
				{
					entityBlock += currentLine;

					if(currentLine.indexOf('}') != -1 && entityBlock.indexOf("worldspawn") != -1)
					{
						if(entityBlock.indexOf(mapName) != -1)
							return currentFile;
						else
							break;
					}
				}
			}
		}
		catch(IOException ioe)
		{	}

		return null;
	}

/*-------------------------------------------------------------------*/
/**	Convenience method which instantiates an anonymous PAKParser and
 *	returns the contents of a known file.
 *	@param pakPathAndName the fully-qualified filename of the PAK archive
 *	@param filePathAndName the path and name of the file within the PAK
 *	@return the file contents */
/*-------------------------------------------------------------------*/
	public static byte[] getFileFromPAK(String pakPathAndName, String filePathAndName)
	{
		PAKParser pakParser = new PAKParser();

		if(pakParser.load(pakPathAndName))
			return pakParser.getFile(filePathAndName);
		else
			return null;
	}

/*-------------------------------------------------------------------*/
/**	Convenience method which instantiates an anonymous PAKParser and
 *	searches for a file whose name is known, but not its path in the archive.
 *	@param pakPathAndName the fully-qualified filename of the PAK archive
 *	@param fileName the filename to search for
 *	@return the full path and name of the file in the archive */
/*-------------------------------------------------------------------*/
	public static String findFileFromPAK(String pakPathAndName, String fileName)
	{
		PAKParser pakParser = new PAKParser();

		if(pakParser.load(pakPathAndName))
			return pakParser.findFile(fileName);
		else
			return null;
	}

/*-------------------------------------------------------------------*/
/**	Convenience method which instantiates an anonymous PAKParser, searches
 *	for a file whose name is known but not its path in the archive, and
 *	returns that file's contents.
 *	@param pakPathAndName the fully-qualified filename of the PAK archive
 *	@param fileName the filename to search for
 *	@return the file contents */
/*-------------------------------------------------------------------*/
	public static byte[] findAndGetFileFromPAK(String pakPathAndName, String fileName)
	{
		String resolvedFilename = null;
		PAKParser pakParser = new PAKParser();

		if(pakParser.load(pakPathAndName) && (resolvedFilename = pakParser.findFile(fileName)) != null)
			return pakParser.getFile(resolvedFilename);
		else
			return null;
	}

/*-------------------------------------------------------------------*/
/**	Convenience method which instantiates an anonymous PAKParser, searches
 *	for files on the basis of a partial filename, and returns a vector
 *	containing the full path and name of all matching files.
 *	@param pakPathAndName the fully-qualified filename of the PAK archive
 *	@param partialFileName the filename to search for
 *	@return a vector containing the path and name of all matching files */
/*-------------------------------------------------------------------*/
	public static Vector findAllFilesFromPAK(String pakPathAndName, String partialFileName)
	{
		PAKParser pakParser = new PAKParser();

		if(pakParser.load(pakPathAndName))
			return pakParser.findAllFiles(partialFileName);
		else
			return null;
	}

/*-------------------------------------------------------------------*/
/**	Since most PAK operations will involve searching for the BSP file
 *	of the current map (ie the geometry data), this convenience method
 *	instantiates an anonymous PAKParser, searches for and returns the
 *	full path and name of a BSP file within the archive with the same
 *	map name as specified. Generally, this will not match the filename;
 *	the method has to manually parse all .bsp files found in the archive.
 *	Called by BasicBot and derivatives to automatically find and load
 *	geometry data based only on the map name of the current game session.
 *	@param pakPathAndName the fully-qualified filename of the PAK archive
 *	@param mapName the name of the map to search for
 *	@return the full path to the associated BSP file
 *	@see soc.qase.bot.BasicBot#readMap */
/*-------------------------------------------------------------------*/
	public static String findBSPFileFromPAK(String pakPathAndName, String mapName)
	{
		PAKParser pakParser = new PAKParser();

		if(pakParser.load(pakPathAndName))
			return pakParser.findBSPFile(mapName);

		return null;
	}

/*-------------------------------------------------------------------*/
/**	Since most PAK operations will involve searching for the BSP file
 *	of the current map (ie the geometry data), this convenience method
 *	instantiates an anonymous PAKParser, searches for and the full path
 *	and name of a BSP file within the archive with the same map name as
 *	specified, and returns the contents of that file. Generally, this
 *	will not match the filename; the method has to manually parse all
 *	.bsp files found in the archive.
 *	@param pakPathAndName the fully-qualified filename of the PAK archive
 *	@param mapName the name of the map to search for
 *	@return the contents of the associated BSP file */
/*-------------------------------------------------------------------*/
	public static byte[] findAndGetBSPFileFromPAK(String pakPathAndName, String mapName)
	{
		String resolvedFilename = null;
		PAKParser pakParser = new PAKParser();

		if(pakParser.load(pakPathAndName) && (resolvedFilename = pakParser.findBSPFile(mapName)) != null)
			return pakParser.getFile(resolvedFilename);
		else
			return null;
	}

/*-------------------------------------------------------------------*/
/**	Close an open PAK file and reset the PAKParser object. */
/*-------------------------------------------------------------------*/
	public void close()
	{
		if(!fileOpen)
			return;

		try
		{
			bufIn.close();
		}
		catch(IOException ioe)
		{
			return;
		}

		fName = null;
		bufIn = null;
		pakFile = null;
		pakHeader = null;
		pakDirectory = null;
		fileOpen = pakRead = false;
	}
}

