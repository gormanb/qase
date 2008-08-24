
# QASE: Quake 2 Agent Simulation Environment

Bernard Gorman <bernard.gorman@gmail.com>

# About

QASE is a Java-based framework designed to provide all the functionality
necessary to create game agents in Quake 2. Powerful enough to facilitate
high-end games research, it is also suitable for hobbyist projects and
undergrad courses geared towards classic AI.

# Changelog

r2.5.9 (24-08-08)
-
**Notes:**

A release which introduces some significant new functionality,
and fixes a bug which had been introduced in the previous
version. NOTE THAT SOME METHOD NAMES HAVE ALSO BEEN CHANGED
TO MAINTAIN API-WIDE CONSISTENCY; a search-and-replace will need
to be performed on agents which call these methods, as listed below.

**Changes:**

* Fixed a bug, introduced in the previous version, which
  could sometimes cause a NullPointerException to be thrown,
  particularly when using the DM2Parser class. Also fixed a minor
  bug which could cause a single Player entity to be misidentified,
  if the current number of active players is exactly the same as
  the maximum allowed number of players.

* Entity objects of category "players" can now be
  queried using the new methods isCrouching, isJumping and
  getWeaponInventoryIndex, to obtain additional information
  about opposing players. QASE tracks these by examining and
  interpreting incoming sound events, weapon skin changes, and
  - in the case of crouching - the size of the volume box
  occupied by the player. Thanks to Modesto Santana and his
  students for suggesting that this functionality be added.

* The Entity class now contains additional methods -
  isPlayerEntity, isItemEntity, isObjectEntity, isWeaponEntity -
  which can be used to more easily ascertain the class to which
  the Entity belongs.

* The Config table is logically partitioned into
  separate subsections; QASE now provides constants in the Config
  class which index the beginning of each of these sections, so
  that context-sensitive indices encountered in other gamestate
  objects can be used to directly retrieve the information to which
  they refer. A new overloading of getConfigString allows the user
  to specify any such subsection and an index within it.

* An extension of the above, new Config methods
  getStatusBarString, getMaxClients, getModelString,
  getPlayerSkinString, getWeaponSkinString, getSoundString,
  getImageString and getLightStyleString have been added for
  easy access to each subsection of the Config table.

* The Model class now has two new methods, getSkin and
  getWeaponSkin, which in the case of Player models returns the
  indices of the entity in the PlayerSkin and WeaponSkin Config
  subsections, respectively. These methods are used by the
  aforementioned Entity.getWeaponInventoryIndex method, and can
  be used to index the relevant subsections of the Config table
  as necessary.

* The BSPParser class now has two additional methods,
  getMapName and getFileName, which return the relevant
  information about the current BSP file. The BSPBrush class
  also has two new methods, checkContents and checkStrictContents,
  which return booleans based on the correlation between the
  passed content bitmask and the actual brush content bitmask.

* The User class now contains KEY_WEAPON constants,
  indicating the default keyboard number button associated with
  each of the game's weapons. These can be used from within
  BasicBot or its derivatives to change the active weapon.

* The Origin class now has a new method, getXYZ, which
  returns the 3D co-ordinate represented by the object in the
  form of a float array.

* Slightly improved overall API performance.

* The BasicBot methods getNearestEnemy, findClosestEnemy,
  and findShortestPathToEnemy have been renamed getNearestOpponent,
  findClosestOpponent and findShortestPathToOpponent, to conform to
  naming conventions established in the World class.

* The getStandState method in the Player class has been
  renamed getPosture, to conform to the terminology used elsewhere
  in the API (e.g. in the BasicBot class).

* The getIndex method of the PlayerGun class has been
  renamed getModelIndex, to differentiate it from the other
  index-returning methods in the class.

* The getItemName method in the Config and Inventory classes
  has been renamed getItemString, to conform to naming conventions
  used in all other Config methods.

r2.5.2 (07-04-08)
-
**Notes:**

A release which fixes a recently-discovered bug, as well
as adding some significant new functionality which has
been requested by QASE users.

**Changes:**

* Fixed a bug which could prevent the BSPParser
  embedded within BasicBot from correctly switching to the
  new BSP map file when the game server changes map. Also
  updated certain methods to correctly check whether the
  current map file has been loaded and, if not, to find
  and load it automatically.

* A new "mapHasChanged" method in ObserverBot and
  PollingBot (and of course their derivatives) allows the
  user to determine whether the server has changed the
  active game map since the last time the method was called.

* New "getEnvironmentFeatureLocations" method in BasicBot
  allows the user to obtain a list of BSPLeaf objects specifying
  the location of various phenomena in the game environment - lava,
  water, slime, and so forth. Convenience methods have also been
  added for easier access to this data (getLavaLocations,
  getWaterLocations, getSlimeLocations, getMistLocations,
  getWindowLocations).

* New methods "timeUntilDrowning" and "isDrowning"
  have been added to the Player class; the former returns
  the time remaining until the agent begins to drown in
  milliseconds, while the latter returns a boolean indicating
  whether the agent is currently drowning. If an item which
  allows the bot to breathe underwater is active, then the
  time remaining on the buff is factored into the value
  returned by timeUntilDrowning. Convenience methods of the
  same name have also been added to BasicBot.

* A new method "checkTimedBuff" has been added to
  the PlayerStatus class, allowing the user to determine if
  a time-limited buff (invulnerability, quad damage, etc) is
  active on the bot, and if so to obtain the time remaining
  until said buff expires. Again, a shortcut method to this
  functionality has been added to BasicBot.

* The PlayerStatus class has been augmented with
  new constants specifying the Config strings associated with
  each status bar icon; these are used in conjunction with
  the checkTimedBuff method described above. A new method
  "getIconConfigString" has also been added to PlayerStatus.

* SampleNoClipBot has been added, allowing the user
  to better examine the functionality of the NoClipBots. These
  allow the agent to "clip" through the environment to a specific
  location, as long as the game server has the "set cheats 1"
  option enabled. Passing a Vector3f object as an argument in
  a NoClipBot's constructor causes the agent to move to the
  given location before the first execution of the AI cycle;
  thereafter, the agent can be instructed to move to any arbitrary
  point by calling "clipToPosition". This is often highly useful
  when designing AI models, since it allows the programmer to
  move the agent to the most appropriate part of the map for
  testing purposes.

* Unused constructors have been removed from the
  SampleBots for simplicity and clarity. In addition, several
  constructors in classes derived from NoClipBot have been
  updated to include a Vector3f specifying the initial
  position to which the agent should move, in keeping with
  the intent of the class.

* A number of methods which were previously marked
  public in BasicBot have been changed to protected, in order
  to maintain consistency and design intent.

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
