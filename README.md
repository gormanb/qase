
# QASE: Quake 2 Agent Simulation Environment

Bernard Gorman <bernard.gorman@gmail.com>

# About

QASE is a Java-based framework designed to provide all the functionality
necessary to create game agents in Quake 2. Powerful enough to facilitate
high-end games research, it is also suitable for hobbyist projects and
undergrad courses geared towards classic AI.

# Changelog

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
