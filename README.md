
# QASE: Quake 2 Agent Simulation Environment

Bernard Gorman <bernard.gorman@gmail.com>

# About

QASE is a Java-based framework designed to provide all the functionality
necessary to create game agents in Quake 2. Powerful enough to facilitate
high-end games research, it is also suitable for hobbyist projects and
undergrad courses geared towards classic AI.

# Changelog

r2.4.8 (30-03-08)
-
**Notes:**

A release that introduces some performance and
functionality improvements, which were requested by
various correspondents. Please note that the interfaces
of two methods in particular (BasicBot.getPlayer and
WaypointMapGenerator.generate) have changed, as outlined
below.

**Changes:**

* New method getPlayerInfo will return the User object
  associated with the current bot, which contains
  information relating to the agent's in-game name, skin,
  FoV, etc.

* Similarly, getServerInfo returns the Server object associated
  with the current game session. This contains information such
  as the server version, map name, and whether the server
  is running CTF or a simple deathmatch.

* The getPlayer method in the BasicBot class has been
  renamed getPlayerState, to properly distinguish it from the
  new getPlayerInfo method.

* New gamestate processing has been added, enabling
  QASE to determine when an opposing player dies. The method
  "hasDied" in the Entity class returns true if the Entity is
  a player who died during the current game frame. Note that
  this serves to flag an instantaneous event - it will register
  as true only for the frame at which the player died, not for
  the entire period between the player's death and respawn.

* A method, getOpponentByName, has been added to the
  World class. This allows the programmer to obtain the Entity
  object associated with a particular player by supplying that
  player's in-game name as a String.

* The interface and functionality of the "generate"
  method in WaypointMapGenerator has changed. The programmer
  need no longer specify whether or not the positions of
  items should be recorded as part of the WaypointMap; this
  is done automatically, since it results in a better
  topological representation of the environment, and because
  the user can simply opt not to use the item information if
  he so desires.

* Furthermore, the number of nodes to be used in the waypoint
  map is now specified as a float value rather than an integer.
  If the argument supplied is greater than 1, it is taken to
  represent the actual number of nodes to use. If, however,
  the value is a decimal less than 1.0, it is treated as
  specifying the number of nodes as a fraction of the total
  number of observed player positions in the demo file.
  
> For instance, the following call:

>>    WaypointMapGenerator.generate("test.dm2", 50)

> will generate a map consisting of 50 waypoints, whereas

>>    WaypointMapGenerator.generate("test.dm2", 0.3)

> will generate a map containing 30% of the total number of
  player positions found in the demo.

* Finally, additional error checks have been added to the
  WaypointMap generation process, to account for more obscure
  scenarios.

* It has been observed that QASE clients do not connect
  to Jake2 servers, Jake2 being a Java port of the Quake2 game
  engine. Initial investigations suggest that Jake2 uses non-
  standard indices for the Config table, which stores data about
  the various entities, models and media used in the course of
  a game session. The Jake2 project has been defunct for some
  time, which makes obtaining the relevant information somewhat
  difficult; I'll see what I can find out, and will incorporate
  any necessary changes into the next release.

r2.4.5 (22-11-07)
-
**Notes:**

A release which makes an important change to the way in which
the default BasicBot constructors work, as well as changes to
the BasicBot and WaypointMap interfaces to maintain consistency
and more accurately describe certain methods. Further convenience
functions have also been added, which facilitate easier querying
of the Inventory and WaypointMap objects from within BasicBot.

**Changes:**

* By default, BasicBot constructors will now enable QASE's
  local inventory-tracking mechanism rather than requiring the
  programmer to explicitly enable it. QASE provides two approaches
  to tracking the agent's inventory, which Quake 2 maintains on the
  server side during game sessions and which is therefore not
  always available to the client; local tracking and automatic
  refreshing. Local tracking involves detecting each item pickup
  and weapon discharge on the client side, building up an inventory
  representation from each frame to the next. Automatic refreshing,
  which can be enabled by calling setAutoInventoryRefresh(true),
  requests an inventory listing from the server on each update.
  Programmers should choose ONE of these two approaches - that is,
  either allow local inventory tracking to be enabled, or disable
  it and call setAutoInventoryRefresh.

* Changed the findClosest and findShortestPath methods in
  BasicBot such that the programmer need no longer specify a
  starting position; the agent's current location is used.

* findClosestItem(cat,type,subtype) renamed findClosestEntity
  in both BasicBot and WaypointMap, to reflect the fact that the methods
  can search for entities of categories other than Item. The methods
  findShortestPathToItem(cat,type,subtype) in both classes have been
  renamed findShortestPathToEntity for the same reason.

* Separate findClosestEntity, findClosestEnemy, findClosestItem,
  findClosestWeapon, findClosestObject, findShortestPathToEntity,
  findShortestPathToEnemy, findShortestPathToItem, findShortestPathToWeapon,
  and findShortestPathToObject methods in BasicBot.

* Added getNearestEntity, getNearestItem, getNearestWeapon
  and getNearestObject methods to BasicBot.

* Added a distance method in the Origin class.

* Updated the Sample bots to use the new methods mentioned above.

r2.4.2 (16-11-07)
-
**Notes:**

A(nother) release which fixes a small recently-discovered bug,
and adds some extra convenience functions.

**Changes:**

* Fixed a bug which caused the model for the Railgun while
  lying on the ground to be confused with that of the Hyperblaster.
  Thanks to Johan Hagelbock for spotting it!

* Added new passthrough methods to the BasicBot class
  (namely getInventoryItemCount and hasItem), allowing the inventory
  to be queried directly rather than requiring the programmer to
  obtain and access the Inventory object.

* Added getPosition, getOrientation and getWeaponIndex
  convenience methods to the BasicBot class, allowing the programmer
  to obtain these values directly rather than via the Player
  object.

* Changed the names of the FLAG\_DUCKED and POSTURE\_DUCKED
  constants in the PlayerMove class, as well as the isDucked() method
  in the Player class, to FLAG\_CROUCH, POSTURE\_CROUCH and isCrouching().
  This is to conform to naming conventions elsewhere.

* Updated the JavaDoc with better descriptions of the Inventory
  access methods, and info on the new BasicBot functions.

r2.4.1 (02-07-07)
-
**Notes:**

A release which fixes a small recently-discovered bug, and 
adds some extra convenience functions.

**Changes:**

* Fixed a bug which caused bots not to auto-respawn if
  they died while firing. Thanks to Modesto Castrillon and his
  students for spotting it!

* QASE now allows the user to determine whether a weapon
  is actively firing on the current frame, or whether it is
  "cooling down" (i.e. the period after a weapon discharge during
  which another round cannot be fired). Previously, cooldowns
  were not distinguished from active discharges.

* New setAttack and setUse convenience methods in
  BasicBot, allowing these common actions to be set individually
  rather than simultaneously via the setActions methods.

* Updated the JavaDoc with information for the isOnLift
method in BasicBot.

r2.4.0 (11-05-07)
-
**Notes:**

Another significant update. This release allows the Entity
blocks of each map's BSP file to be parsed; these blocks
describe environmental effects such as doors, buttons,
lifts, teleporters, etc. QASE also performs some additional
processing to allow this data to be queried from high-level
contexts - for instance, a new method in BasicBot lets the player
easily determine whether he is currently riding on a lift,
while the links between teleporter locations are inferred by
the library and presented to the user as edges in a graph.

**Changes:**

* Full parsing support for BSP Entities, including methods
in BasicBot and other classes which allow the Entity data to be
easily queried.

r2.3.0 (03-05-07)
-
**Notes:**

A significant update. This release incorporates full support
for Threewave's team-based Capture-the-Flag mod, including
new connect methods in BasicBot, team-switching functions, and
the ability to easily determine whether a particular player is
on your team or the opposing.

**Changes:**

* Added support for the Threewave CTF mod.

* Calling getBSPParser in BasicBot will now transparently
  find and load the map, rather than returning null if no map has
  yet been loaded.

* New BSP-related functions in BasicBot, including a
  general-purpose visibility check; this should reduce the need
  to delve into the BSPParser class itself in most cases.

* Visibility methods can now start from either the agent's
  actual in-game position, or from the offset position of the
  viewing platform (i.e. the game "camera").

* The demonstration agents in BotManager will now connect
  for 90 seconds and then automatically disconnect themselves.

* Some noticeable performance improvements.

r2.2.0 (19-10-06)
-
**Notes:**

A release which primarily addresses a recently-discovered bug,
and which also implements a transparent (but conceptually important)
architectural change in the API.

**Changes:**

* Fixed a bug which caused MatLabGeneralPollingBot
  to freeze. Thanks to Du Xiaoqin for spotting it! This involved
  updates to both QASE and the MatLab integration scripts, bringing
  them in line with changes to the API.

* Moved all mutator and accessor methods from MatLabBots
  to MatLabGeneralBots, since allowing direct access to the
  gamestate from within Hybrid agents broke paradigm.

* Removed some of the MatLabGeneralPollingBot constructors,
  since they were redundant for this bot category.

* Added a basic test map in the QASE API archive, which
  provides a useful testbed while learning how to use the API and
  works well with the sample bots included.

* Updated some sections of the Javadoc, as well as the
  User's Guide.

r2.1.9 (14/08/06)
-
**Notes:**

Another interim release, since time constraints have
prevented the incorporation of the Quake 3 protocol as
of yet. This version includes some minor fixes and slight
changes to methods in the BasicBot class, making the
process of setting the agent's movement more efficient
and intuitive.

**Changes:**

* Fixed a bug which could, under certain
  circumstances, cause an exception to be thrown if the
  player's health fell to exactly -1.

* Fixed a bug which could occasionally cause the
  player's inventory not to be reset upon death when manual
  inventory tracking is active.

* New methods have been added to the BasicBot class which
  allow the programmer to more easily produce 'posture'
  actions such as crouching, standing or jumping; see
  setJump, setCrouch and setPosture.

* An additional parameter has been added to the two
  setBotMovement methods of the BasicBot class, allowing
  the programmer to specify one of the postures mentioned
  above. Since the purpose of these methods is to
  provide an intuitive single-call means of setting all
  aspects of the agent's motion, the absence of this
  parameter was an oversight. Please note that any agents
  which call setBotMovement will need to be updated to
  add the new argument.

* When the getOpponents method of the World object is
  called, the entity object corresponding to the local
  player will now be automatically removed.

* New non-static methods have been added to the PlayerGun
  class, allowing the direct resolution of the current
  gun's model index and associated ammo to the relevant
  inventory indices. This removes the need to make two
  successive static method calls in order to ascertain
  these indices.

* Some typographical errors in the Javadoc pages have
  been corrected.

r2.1.5 (19/02/2006)
-
**Notes:**

An interim release designed to address a bug which
sometimes caused use of the Shotgun weapon to throw an 
exception. No significant changes otherwise. The next 
major release will probably see the incorporation of an 
integrated Quake3 client; however, given that I am working 
on several other projects at present, this probably won't 
be completed for some time.

**Changes:**

* Fixed a bug which sometimes caused selecting
the Shotgun as the agent's current weapon (or reading 
frames from a DM2 in which the human used the Shotgun)
to throw an exception.

* Minor alterations and additions to existing methods,
  provision of some new convenience functions.

* Facilitates reading and application of discrete walk
  states (stop, normal, run) in addition to explicit
  velocities. Accounts for changes in speed due to
  submersion in water/other fluids.

r2.1.0 (15/12/2005)
-
**Notes:**

Updates some core elements of the QASE engine which could
not be completed in time for the initial release.

**Changes:**
* Substantial rewrite of the gamestate-handling routines.
  Agents now leave a far smaller footprint in memory and are 
  generally more resource-friendly.

* Additional utility functions for byte array manipulation

* Separate representations of weapon models while on the
  ground and in the player's hands

r2.0.0 (04/12/2005)
-
* Initial public release. See the User Guide and other docs for a full list
of features.
