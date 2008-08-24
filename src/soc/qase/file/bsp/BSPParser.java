//---------------------------------------------------------------------
// Name:			BSPParser.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.file.bsp;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;
import java.util.StringTokenizer;

import soc.qase.tools.Utils;
import soc.qase.file.pak.PAKParser;
import soc.qase.tools.vecmath.Vector3f;

/*-------------------------------------------------------------------*/
/**	A class which enables QASE to parse BSP files, which are used by
 *	Quake 2 to store the geometry of its various maps. By constructing
 *	an internal representation of the geometry from this data, the
 *	BSPParser can perform collision detection on the environment,	
 *	detecting and reporting the location of or distance to the nearest
 *	solid obstacle in a given direction from a specified starting point. */
/*-------------------------------------------------------------------*/
public class BSPParser
{
	private byte[] lumpData = null;
	private byte[] headerData = null;
	private byte[] pakBSPFileData = null;

	private String fName = null;
	private File bspFile = null;

	private BufferedInputStream bufIn = null;
	private ByteArrayInputStream byteIn = null;

	private boolean mapRead = false;
	private boolean fileOpen = false;
	private boolean inPAKFile = false;

	private BSPLump[] bspLumps = null;
	private BSPHeader bspHeader = null;

	public BSPPlaneLump planeLump = null;
	public BSPVertexLump vertexLump = null;
	public BSPNodeLump nodeLump = null;
	public BSPLeafLump leafLump = null;
	public BSPLeafFaceLump leafFaceLump = null;
	public BSPFaceEdgeLump faceEdgeLump = null;
	public BSPEdgeLump edgeLump = null;
	public BSPModelLump modelLump = null;
	public BSPBrushLump brushLump = null;
	public BSPBrushSideLump brushSideLump = null;
	public BSPLeafBrushLump leafBrushLump = null;

	public BSPEntityLump entitiesLump = null;

	public static final float EPSILON = 0.03125f;
	public static final int TRACE_LINE = 0, TRACE_SPHERE = 1, TRACE_BOX = 2;

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public BSPParser()
	{	}

/*-------------------------------------------------------------------*/
/**	Constructor. Allows a BSP file to be specified.
 *	@param filename the filename of the BSP file from which to read */
/*-------------------------------------------------------------------*/
	public BSPParser(String filename)
	{
		load(filename);
	}

/*-------------------------------------------------------------------*/
/**	Load the specified BSP file.
 *	@param filename the filename of the BSP file from which to read.
 *	If the file is within a PAK archive, it can be loaded by using the
 *	hash character to combine the path to the PAK file and the path to
 *	the BSP file within it, as in C:/archive1.pak#maps/mymap.bsp
 *	@return true if the load was successful, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean load(String filename)
	{
		if(mapRead) // if the BSPParser already contains BSP data, reset it and load new data
			reset();

		// # in filename means PAK archive
		if(filename.indexOf('#') != -1)
		{
			StringTokenizer st = new StringTokenizer(filename, "#");
			pakBSPFileData = PAKParser.getFileFromPAK(st.nextToken(), st.nextToken());

			if(pakBSPFileData != null)
			{
				byteIn = new ByteArrayInputStream(pakBSPFileData);
				bufIn = new BufferedInputStream(byteIn);

				fName = filename;
				fileOpen = inPAKFile = true;
			}			
		}
		else
		{
			try
			{
				bspFile = new File(filename);
				bufIn = new BufferedInputStream(new FileInputStream(bspFile));

				fName = filename;
				fileOpen = true;
			}
			catch(IOException ioe)
			{	}
		}

		// parse the data and build BSP structures
		mapRead = (fileOpen && readBSPData());

		// close file handles and reset internal variables
		try { bufIn.close(); byteIn.close(); } catch(Exception ioe){}

		bufIn = null;
		byteIn = null;
		bspFile = null;

		fileOpen = inPAKFile = false;
		headerData = lumpData = pakBSPFileData = null;

		if(!mapRead) // if map reading was unsuccessful, reset all class variables
			reset();

		return mapRead;
	}

/*-------------------------------------------------------------------*/
/**	Check whether the specified BSP file has been loaded.
 *	@return true if the map is loaded, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isMapLoaded()
	{
		return mapRead;
	}

/*-------------------------------------------------------------------*/
/**	Read the BSP data from the open file.
 *	@return true if the data was successfully parsed, false otherwise*/
/*-------------------------------------------------------------------*/
	private boolean readBSPData()
	{
		return readBSPHeader() && readBSPLumps();
	}

/*-------------------------------------------------------------------*/
/**	Load the specified BSP file's header, which contains the full
 *	directory listing of all lumps.
 *	@return true if the header is read successfully, false otherwise */
/*-------------------------------------------------------------------*/
	private boolean readBSPHeader()
	{
		headerData = new byte[4 * 40];	// 40 integer values
		
		try
		{
			bufIn.read(headerData);
			bspHeader = new BSPHeader(headerData);
		}
		catch(IOException ioe)
		{
			return false;
		}

		return bspHeader.validateBSP();
	}

/*-------------------------------------------------------------------*/
/**	Read and store the various lumps from the BSP file.
 *	@return true if the lumps are read correctly, false otherwise */
/*-------------------------------------------------------------------*/
	private boolean readBSPLumps()
	{
		if(inPAKFile)
			lumpData = new byte[pakBSPFileData.length - headerData.length];
		else
			lumpData = new byte[(int)bspFile.length() - headerData.length];

		try
		{
			bufIn.read(lumpData);
		}
		catch(IOException ioe)
		{
			return false;
		}

		bspLumps = new BSPLump[19];

		bspLumps[BSPHeader.PLANES] = planeLump = new BSPPlaneLump(lumpData, bspHeader.getOffset(BSPHeader.PLANES), bspHeader.getLength(BSPHeader.PLANES));
		bspLumps[BSPHeader.VERTICES] = vertexLump = new BSPVertexLump(lumpData, bspHeader.getOffset(BSPHeader.VERTICES), bspHeader.getLength(BSPHeader.VERTICES));
		bspLumps[BSPHeader.NODES] = nodeLump = new BSPNodeLump(lumpData, bspHeader.getOffset(BSPHeader.NODES), bspHeader.getLength(BSPHeader.NODES));
		bspLumps[BSPHeader.LEAVES] = leafLump = new BSPLeafLump(lumpData, bspHeader.getOffset(BSPHeader.LEAVES), bspHeader.getLength(BSPHeader.LEAVES));
		bspLumps[BSPHeader.LEAF_FACE_TABLE] = leafFaceLump = new BSPLeafFaceLump(lumpData, bspHeader.getOffset(BSPHeader.LEAF_FACE_TABLE), bspHeader.getLength(BSPHeader.LEAF_FACE_TABLE));
		bspLumps[BSPHeader.FACE_EDGE_TABLE] = faceEdgeLump = new BSPFaceEdgeLump(lumpData, bspHeader.getOffset(BSPHeader.FACE_EDGE_TABLE), bspHeader.getLength(BSPHeader.FACE_EDGE_TABLE));
		bspLumps[BSPHeader.EDGES] = edgeLump = new BSPEdgeLump(lumpData, bspHeader.getOffset(BSPHeader.EDGES), bspHeader.getLength(BSPHeader.EDGES));
		bspLumps[BSPHeader.MODELS] = modelLump = new BSPModelLump(lumpData, bspHeader.getOffset(BSPHeader.MODELS), bspHeader.getLength(BSPHeader.MODELS));
		bspLumps[BSPHeader.BRUSHES] = brushLump = new BSPBrushLump(lumpData, bspHeader.getOffset(BSPHeader.BRUSHES), bspHeader.getLength(BSPHeader.BRUSHES));
		bspLumps[BSPHeader.BRUSH_SIDES] = brushSideLump = new BSPBrushSideLump(lumpData, bspHeader.getOffset(BSPHeader.BRUSH_SIDES), bspHeader.getLength(BSPHeader.BRUSH_SIDES));
		bspLumps[BSPHeader.LEAF_BRUSH_TABLE] = leafBrushLump = new BSPLeafBrushLump(lumpData, bspHeader.getOffset(BSPHeader.LEAF_BRUSH_TABLE), bspHeader.getLength(BSPHeader.LEAF_BRUSH_TABLE));

		bspLumps[BSPHeader.ENTITIES] = entitiesLump = new BSPEntityLump(lumpData, bspHeader.getOffset(BSPHeader.ENTITIES), bspHeader.getLength(BSPHeader.ENTITIES), modelLump.models);

		return true;
	}

/*-------------------------------------------------------------------*/
/**	A convenience method, which determines whether a given BSP file
 *	contains the specified map name. Generally called by the readMap
 *	method of BasicBot, when attempting to locate and load the map
 *	being used in the current game session.
 *	@param pathAndFileName the full path to the BSP file
 *	@param mapName the name of the map to look for in the file
 *	@return true if the map name was found, false otherwise
 *	@see soc.qase.bot.BasicBot#readMap */
/*-------------------------------------------------------------------*/
	public static boolean isMapNameInFile(String pathAndFileName, String mapName)
	{
		File bspMapCheck = new File(pathAndFileName);

		if(bspMapCheck.exists())
		{
			byte[] entityBlockOffset = new byte[4];
			byte[] searchBytes = mapName.getBytes();
			String currentLine = null, entityBlock = "";

			try
			{
				RandomAccessFile mapReader = new RandomAccessFile(bspMapCheck, "r");

				mapReader.seek(8); // skip ID & version info
				mapReader.read(entityBlockOffset);
				mapReader.seek(Utils.intValue(entityBlockOffset, 0));

				while((currentLine = mapReader.readLine()) != null)
				{
					entityBlock += currentLine;

					if(currentLine.indexOf('}') != -1 && entityBlock.indexOf("worldspawn") != -1)
						break;
				}

				mapReader.close();
			}
			catch(IOException ioe)
			{	}

			return (entityBlock.indexOf(mapName) > -1);
		}

		return false;
	}

/*-------------------------------------------------------------------*/
/**	Returns the name of the current map (not necessarily the same as
 *	the BSP file name), or null if the map does not have a name or no
 *	map is loaded.
 *	@return the name of the currently-loaded map, or null if no such
 *	map/name. */
/*-------------------------------------------------------------------*/
	public String getMapName()
	{
		if(!mapRead)
			return null;

		for(int i = 0; i < entitiesLump.entities.length; i++)
		{
			if(entitiesLump.entities[i].isWorldSpawn)
				return entitiesLump.entities[i].getAttribute("message");
		}

		return null;
	}

/*-------------------------------------------------------------------*/
/**	Returns the path and name of the .bsp file associated with the BSPParser.
 *	@return the name of the current .bsp file. */
/*-------------------------------------------------------------------*/
	public String getFileName()
	{
		return fName;
	}

/*-------------------------------------------------------------------*/
/**	Return a specified lump object.
 *	@param lumpNum the index of the lump (using constants listed above)
 *	@return the BSPLump object at that index */
/*-------------------------------------------------------------------*/
	public BSPLump getLump(int lumpNum)
	{
		return bspLumps[lumpNum];
	}

/*-------------------------------------------------------------------*/
/** Returns all Item entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getItems(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_ITEM);	}

/*-------------------------------------------------------------------*/
/** Returns all entities which possess in-game models.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getModels(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_MODEL);	}

/*-------------------------------------------------------------------*/
/** Returns all Weapon entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getWeapons(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_WEAPON);	}

/*-------------------------------------------------------------------*/
/** Returns all Monster entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getMonsters(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_MONSTER);	}

/*-------------------------------------------------------------------*/
/** Returns all Door entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getDoors(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_DOOR);	}

/*-------------------------------------------------------------------*/
/** Returns all Lift entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getLifts(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_LIFT);	}

/*-------------------------------------------------------------------*/
/** Returns all Button entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getButtons(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_BUTTON);	}

/*-------------------------------------------------------------------*/
/** Returns all Illusory entities (i.e. can be seen but not interactive).
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getIllusion(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_ILLUSION);	}

/*-------------------------------------------------------------------*/
/** Returns all Conveyor belt and train entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getConveyors(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_CONVEYOR);	}

/*-------------------------------------------------------------------*/
/** Returns all Teleporter entities, both single-player and DeathMatch.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getTeleports(Vector vect)
	{	return getDMTeleports(getNormalTeleports(vect));	}

/*-------------------------------------------------------------------*/
/** Returns all DeathMatch teleporter entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getDMTeleports(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_DM_TELEPORT);	}

/*-------------------------------------------------------------------*/
/** Returns all single-player teleporter entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getNormalTeleports(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_NORMAL_TELEPORT);	}

/*-------------------------------------------------------------------*/
/** Returns all secret door entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getSecretDoors(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_SECRET_DOOR);	}

/*-------------------------------------------------------------------*/
/** Returns all path corner entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getPathCorners(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_CORNER);	}

/*-------------------------------------------------------------------*/
/** Returns all walkover-button entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getWalkovers(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_WALKOVER);	}

/*-------------------------------------------------------------------*/
/** Returns all Teleport destination entities.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getTeleportDestinations(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_DESTINATION);	}

/*-------------------------------------------------------------------*/
/** Returns all miscellaneous object entities (exploding barrels, etc).
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getMiscObjects(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_MISC_OBJECT);	}

/*-------------------------------------------------------------------*/
/** Returns all spawn points, regardless of single or multi-player.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getStartPositions(Vector vect)
	{	return getDMStartPositions(getPlayerStartPositions(vect));	}

/*-------------------------------------------------------------------*/
/** Returns all single-player spawn points.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getPlayerStartPositions(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_PLAYER_START);	}

/*-------------------------------------------------------------------*/
/** Returns all DeathMatch spawn points.
 *	@param vect the Vector into which the entities will be added
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getDMStartPositions(Vector vect)
	{	return getEntityType(vect, BSPEntity.BSP_DM_START);	}

/*-------------------------------------------------------------------*/
/** Returns all entities of the specified type. The supplied entity ID
 *	should match one of the integer constants found in BSPEntity.
 *	@param vect the Vector into which the entities will be added
 *	@param entID the type of entity to find and return; should be one
 *	of the integer constants from BSPEntity
 *	@return a reference to the newly-populated vect for convenience */
/*-------------------------------------------------------------------*/
	public Vector getEntityType(Vector vect, int entID)
	{
		for(int i = 0; i < entitiesLump.entities.length; i++)
		{
			if((entID == BSPEntity.BSP_MODEL ? entitiesLump.entities[i].isModel : entitiesLump.entities[i].entityType == entID))
				vect.add(entitiesLump.entities[i]);
		}

		return vect;
	}

/*-------------------------------------------------------------------*/
/**	Perform a line trace from one point to another to determine if the
 *	endpoint is visible from the start point.
 *	@return true if nothing obscures the view, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean isVisible(Vector3f start, Vector3f end)
	{
		return (traceLine(start, end)).equals(end);
	}

/*-------------------------------------------------------------------*/
/**	Projects a line through the game world in a given direction from a
 *	given start point, and returns the location of the first collision
 *	with solid geometry.
 *	@param start start point for line trace
 *	@param dir direction in which to trace
 *	@param maxDist maximum distance across which to sweep
 *	@return the location of the first collision */
/*-------------------------------------------------------------------*/
	public Vector3f getObstacleLocation(Vector3f start, Vector3f dir, float maxDist)
	{
		return traceLine(start, getEndpoint(start, dir, maxDist));
	}

/*-------------------------------------------------------------------*/
/**	Projects a sphere through the game world in a given direction from a
 *	given start point, and returns the location of the first collision
 *	with solid geometry.
 *	@param start start point for trace
 *	@param dir direction in which to trace
 *	@param sphereRadius the radius of the sphere to be traced
 *	@param maxDist maximum distance across which to sweep
 *	@return the location of the first collision */
/*-------------------------------------------------------------------*/
	public Vector3f getObstacleLocation(Vector3f start, Vector3f dir, float sphereRadius, float maxDist)
	{
		return traceSphere(start, getEndpoint(start, dir, maxDist), sphereRadius);
	}

/*-------------------------------------------------------------------*/
/**	Projects a bounding box through the game world in a given direction
 *	from a given start point, and returns the location of the first
 *	collision with solid geometry.
 *	@param start start point for trace
 *	@param dir direction in which to trace
 *	@param boundingBoxMins the lower reference point of the bounding box
 *	@param boundingBoxMaxs the upper reference point of the bounding box
 *	@param maxDist maximum distance across which to sweep
 *	@return the location of the first collision */
/*-------------------------------------------------------------------*/
	public Vector3f getObstacleLocation(Vector3f start, Vector3f dir, Vector3f boundingBoxMins, Vector3f boundingBoxMaxs, float maxDist)
	{
		return traceBox(start, getEndpoint(start, dir, maxDist), boundingBoxMins, boundingBoxMaxs);
	}

/*-------------------------------------------------------------------*/
/**	Projects a line through the game world in a given direction from a
 *	given start point, and returns the distance to the first collision
 *	with solid geometry.
 *	@param start start point for line trace
 *	@param dir direction in which to trace
 *	@param maxDist maximum distance across which to sweep
 *	@return the distance to the first collision */
/*-------------------------------------------------------------------*/
	public float getObstacleDistance(Vector3f start, Vector3f dir, float maxDist)
	{
		if(!mapRead)
			return Float.NaN;

		Vector3f end = getObstacleLocation(start, dir, maxDist);

		end.sub(start);
		return end.length();
	}

/*-------------------------------------------------------------------*/
/**	Projects a sphere through the game world in a given direction from a
 *	given start point, and returns the distance to the first collision
 *	with solid geometry.
 *	@param start start point for trace
 *	@param dir direction in which to trace
 *	@param sphereRadius the radius of the sphere to be traced
 *	@param maxDist maximum distance across which to sweep
 *	@return the distance to the first collision */
/*-------------------------------------------------------------------*/
	public float getObstacleDistance(Vector3f start, Vector3f dir, float sphereRadius, float maxDist)
	{
		if(!mapRead)
			return Float.NaN;

		Vector3f end = getObstacleLocation(start, dir, sphereRadius, maxDist);

		end.sub(start);
		return end.length();
	}

/*-------------------------------------------------------------------*/
/**	Projects a bounding box through the game world in a given direction
 *	from a given start point, and returns the location of the first
 *	collision with solid geometry.
 *	@param start start point for trace
 *	@param dir direction in which to trace
 *	@param boundingBoxMins the lower reference point of the bounding box
 *	@param boundingBoxMaxs the upper reference point of the bounding box
 *	@param maxDist maximum distance across which to sweep
 *	@return the location of the first collision */
/*-------------------------------------------------------------------*/
	public float getObstacleDistance(Vector3f start, Vector3f dir, Vector3f boundingBoxMins, Vector3f boundingBoxMaxs, float maxDist)
	{
		if(!mapRead)
			return Float.NaN;

		Vector3f end = getObstacleLocation(start, dir, boundingBoxMins, boundingBoxMaxs, maxDist);

		end.sub(start);
		return end.length();
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Vector3f getEndpoint(Vector3f start, Vector3f dir, float maxDist)
	{
		Vector3f end = new Vector3f(dir);

		end.normalize();
		end.scale(maxDist);
		end.add(start);

		return end;
	}

	private int TRACE_TYPE;
	private int BRUSH_BITS = BSPBrush.CONTENTS_SOLID;

	private float traceRadius;
	private float outputFraction;

	private boolean outputAllSolid;
	private boolean outputStartsOut;

	private Vector3f outputEnd = new Vector3f(0, 0, 0);
	private Vector3f inputStart = new Vector3f(0, 0, 0);
	private Vector3f inputEnd = new Vector3f(0, 0, 0);

	private Vector3f traceMins = null;
	private Vector3f traceMaxs = null;
	private Vector3f traceExtents = new Vector3f(0, 0, 0);

/*-------------------------------------------------------------------*/
/**	Set the type of brush to check for when sweeping, specified by
 *	the constants found in BSPBrush. Allows the agent to check for
 *	different types of terrain.
 *	@param brushType the type of brush to check against 
 *	@see BSPBrush */
/*-------------------------------------------------------------------*/
	public void setBrushType(int brushType)
	{
		BRUSH_BITS = brushType;
	}

/*-------------------------------------------------------------------*/
/**	Trace a line between two points, and return the point at which a
 *	collision first occurs.
 *	@param start the start point of the trace
 *	@param end the endpoint of the trace
 *	@return a Vector3f indicating the location at which the first
 *	collision occurred */
/*-------------------------------------------------------------------*/
	public Vector3f traceLine(Vector3f start, Vector3f end)
	{
		TRACE_TYPE = TRACE_LINE;
		return trace(start, end);
	}

/*-------------------------------------------------------------------*/
/**	Trace a sphere between two points, and return the point at which a
 *	collision first occurs.
 *	@param start the start point of the trace
 *	@param end the endpoint of the trace
 *	@param radius the radius of the sphere to sweep
 *	@return a Vector3f indicating the location at which the first
 *	collision occurred */
/*-------------------------------------------------------------------*/
	public Vector3f traceSphere(Vector3f start, Vector3f end, float radius)
	{
		traceRadius = radius;
		TRACE_TYPE = TRACE_SPHERE;
		return trace(start, end);
	}

/*-------------------------------------------------------------------*/
/**	Trace a bounding box between two points, and return the point at
 *	which a collision first occurs.
 *	@param start the start point of the trace
 *	@param end the endpoint of the trace
 *	@param mins the lower reference point of the bounding box
 *	@param maxs the upper reference point of the bounding box
 *	@return a Vector3f indicating the location at which the first
 *	collision occurred */
/*-------------------------------------------------------------------*/
	public Vector3f traceBox(Vector3f start, Vector3f end, Vector3f mins, Vector3f maxs)
	{
		if(mins.length() == 0 && maxs.length() == 0)
			return traceLine(start, end);

		TRACE_TYPE = TRACE_BOX;

		traceMins = new Vector3f(mins);
		traceMaxs = new Vector3f(maxs);

		for(int i = 0; i < 3; i++)
			traceExtents.set(i, Math.max(-traceMins.get(i), traceMaxs.get(i)));	

		return trace(start, end);
	}

	private Vector3f trace(Vector3f start, Vector3f end)
	{
		if(!mapRead)
			return null;

		outputStartsOut = true;
		outputAllSolid = false;
		outputFraction = 1.0f;

		inputEnd.set(end);
		inputStart.set(start);

		// traverse the BSP tree
		checkNode( 0, 0.0f, 1.0f, inputStart, inputEnd );
	
		if (outputFraction == 1.0f)
		{	// nothing blocked the trace
			outputEnd = inputEnd;
		}
		else
		{	// collided with something 
			for (int i = 0; i < 3; i++)
			{
				outputEnd.set(i, inputStart.get(i) + outputFraction * (inputEnd.get(i) - inputStart.get(i)));
			}
		}

		return new Vector3f(outputEnd);
	}

	private void checkNode( int nodeIndex, float startFraction, float endFraction, Vector3f start, Vector3f end )
	{
		if (nodeIndex < 0)
		{	// this is a leaf
			BSPLeaf leaf = leafLump.leaves[-(nodeIndex + 1)];

			for (int i = 0; i < leaf.numLeafBrushes; i++)
			{
				BSPBrush brush = brushLump.brushes[leafBrushLump.leafBrushTable[leaf.firstLeafBrush + i]];

				if (brush.numSides > 0 && brush.checkContents(BRUSH_BITS))
					checkBrush(brush);
			}

			// don't have to do anything else for leaves
			return;
		}
	
		// this is a node

		BSPNode node = nodeLump.nodes[nodeIndex];
		BSPPlane plane = planeLump.planes[(int)node.plane];

		float offset = 0;
		float startDistance = start.dot(plane.normal) - plane.distance;
		float endDistance = end.dot(plane.normal) - plane.distance;

		if(TRACE_TYPE == TRACE_LINE)
			offset = 0;
		else if(TRACE_TYPE == TRACE_SPHERE)
			offset = traceRadius;
		else if(TRACE_TYPE == TRACE_BOX)
		{
			for(int i = 0; i < 3; i++)
				offset += Math.abs(traceExtents.get(i) * plane.normal.get(i));
		}

		if (startDistance >= offset && endDistance >= offset)
		{	// both points are in front of the plane
			// so check the front child
			checkNode(node.frontChild, startFraction, endFraction, start, end );
		}
		else if (startDistance < -offset && endDistance < -offset)
		{	// both points are behind the plane
			// so check the back child
			checkNode(node.backChild, startFraction, endFraction, start, end );
		}
		else
		{	// the line spans the splitting plane
			int side;
			float fraction1, fraction2, middleFraction;
			Vector3f middle = new Vector3f(0, 0, 0);

			// split the segment into two
			if (startDistance < endDistance)
			{
				side = 1; // back
				float inverseDistance = 1.0f / (startDistance - endDistance);
				fraction1 = (startDistance - offset + EPSILON) * inverseDistance;
				fraction2 = (startDistance + offset + EPSILON) * inverseDistance;
			}
			else if (endDistance < startDistance)
			{
				side = 0; // front
				float inverseDistance = 1.0f / (startDistance - endDistance);
				fraction1 = (startDistance + offset + EPSILON) * inverseDistance;
				fraction2 = (startDistance - offset - EPSILON) * inverseDistance;
			}
			else
			{
				side = 0; // front
				fraction1 = 1.0f;
				fraction2 = 0.0f;
			}
	
			// make sure the numbers are valid
			if (fraction1 < 0.0f) fraction1 = 0.0f;
			else if (fraction1 > 1.0f) fraction1 = 1.0f;
			if (fraction2 < 0.0f) fraction2 = 0.0f;
			else if (fraction2 > 1.0f) fraction2 = 1.0f;
	
			// calculate the middle point for the first side
			middleFraction = startFraction + (endFraction - startFraction) * fraction1;

			for (int i = 0; i < 3; i++)
				middle.set(i, start.get(i) + fraction1 * (end.get(i) - start.get(i)));

			// check the first side
			checkNode(node.children[side], startFraction, middleFraction, start, middle );
	
			// calculate the middle point for the second side
			middleFraction = startFraction + (endFraction - startFraction) * fraction2;

			for (int i = 0; i < 3; i++)
				middle.set(i, start.get(i) + fraction2 * (end.get(i) - start.get(i)));

			// check the second side
			checkNode(node.children[(side + 1) % 2], middleFraction, endFraction, middle, end );
		}
	}

	private void checkBrush(BSPBrush brush)
	{
		float startFraction = -1.0f;
		float endFraction = 1.0f;
		boolean startsOut = false;
		boolean endsOut = false;
	
		for (int i = 0; i < brush.numSides; i++)
		{
			BSPBrushSide brushSide = brushSideLump.brushSides[(int)brush.firstSide + i];
			BSPPlane plane = planeLump.planes[brushSide.planeNum];

			float startDistance = 0, endDistance = 0;

			if(TRACE_TYPE == TRACE_LINE)
			{
				startDistance = inputStart.dot(plane.normal) - plane.distance;
				endDistance = inputEnd.dot(plane.normal) - plane.distance;
			}
			if(TRACE_TYPE == TRACE_SPHERE)
			{
				startDistance = inputStart.dot(plane.normal) - (plane.distance + traceRadius);
				endDistance = inputEnd.dot(plane.normal) - (plane.distance + traceRadius);
			}
			else if(TRACE_TYPE == TRACE_BOX)
			{
				for(int j = 0; j < 3; j++)
				{
					if(plane.normal.get(j) < 0)
					{
						startDistance += ((inputStart.get(j) + traceMaxs.get(j)) * plane.normal.get(j));	
						endDistance += ((inputEnd.get(j) + traceMaxs.get(j)) * plane.normal.get(j));	
					}
					else
					{
						startDistance += ((inputStart.get(j) + traceMins.get(j)) * plane.normal.get(j));	
						endDistance += ((inputEnd.get(j) + traceMins.get(j)) * plane.normal.get(j));	
					}
				}

				startDistance -= plane.distance;
				endDistance -= plane.distance;
			}

			if (startDistance > 0)
				startsOut = true;
			if (endDistance > 0)
				endsOut = true;
	
			// make sure the trace isn't completely on one side of the brush
			if (startDistance > 0 && endDistance > 0)
			{   // both are in front of the plane, its outside of this brush
				return;
			}

			if (startDistance <= 0 && endDistance <= 0)
			{   // both are behind this plane, it will get clipped by another one
				continue;
			}
	
			if (startDistance > endDistance)
			{   // line is entering into the brush
				float fraction = (startDistance - EPSILON) / (startDistance - endDistance);
				if (fraction > startFraction)
					startFraction = fraction;
			}
			else
			{   // line is leaving the brush
				float fraction = (startDistance + EPSILON) / (startDistance - endDistance);
				if (fraction < endFraction)
					endFraction = fraction;
			}
		}
	
		if (!startsOut)
		{
			outputStartsOut = false;

			if(!endsOut)
				outputAllSolid = true;

			return;
		}
	
		if (startFraction < endFraction)
		{
			if (startFraction > -1 && startFraction < outputFraction)
			{
				if (startFraction < 0)
					startFraction = 0;
				outputFraction = startFraction;
			}
		}
	}

/*-------------------------------------------------------------------*/
/**	Resets all contents of the BSPParser object. Called if a new BSP
 *	file is loaded while a previous file is active, or by the user
 *	for any arbitraty purpose. */
/*-------------------------------------------------------------------*/
	public void reset()
	{
		// reset BSP structures
		lumpData = null;
		headerData = null;
		pakBSPFileData = null;

		fName = null;
		bspFile = null;

		bufIn = null;
		byteIn = null;

		mapRead = false;
		fileOpen = false;
		inPAKFile = false;

		bspLumps = null;
		bspHeader = null;

		planeLump = null;
		vertexLump = null;
		nodeLump = null;
		leafLump = null;
		leafFaceLump = null;
		faceEdgeLump = null;
		edgeLump = null;
		modelLump = null;
		brushLump = null;
		brushSideLump = null;
		leafBrushLump = null;

		entitiesLump = null;

		// reset private trace variables
		TRACE_TYPE = 0;
		BRUSH_BITS = BSPBrush.CONTENTS_SOLID;

		traceRadius = 0.0f;
		outputFraction = 0.0f;

		outputAllSolid = false;
		outputStartsOut = false;

		outputEnd = new Vector3f(0, 0, 0);
		inputStart = new Vector3f(0, 0, 0);
		inputEnd = new Vector3f(0, 0, 0);

		traceMins = null;
		traceMaxs = null;
		traceExtents = new Vector3f(0, 0, 0);
	}
}
