
# QASE: Quake 2 Agent Simulation Environment

Bernard Gorman <bernard.gorman@gmail.com>

# About

QASE is a Java-based framework designed to provide all the functionality
necessary to create game agents in Quake 2. Powerful enough to facilitate
high-end games research, it is also suitable for hobbyist projects and
undergrad courses geared towards classic AI.

# Changelog

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
