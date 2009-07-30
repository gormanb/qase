//---------------------------------------------------------------------
// Name:			Effects.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

/*-------------------------------------------------------------------*/
/**	Wrapper class for effect visualization. */
/*-------------------------------------------------------------------*/
public class Effects
{
	private int effects = -1;
	private int renderEffects = -1;

	public static final int MZ_BLASTER = 0, MZ_MACHINEGUN = 1, MZ_SHOTGUN = 2, MZ_CHAINGUN1 = 3,
	MZ_CHAINGUN2 = 4, MZ_CHAINGUN3 = 5, MZ_RAILGUN = 6, MZ_ROCKET = 7, MZ_GRENADE = 8, MZ_LOGIN = 9,
	MZ_LOGOUT = 10, MZ_RESPAWN = 11, MZ_BFG = 12, MZ_SSHOTGUN = 13, MZ_HYPERBLASTER = 14,
	MZ_ITEMRESPAWN = 15, MZ_IONRIPPER = 16, MZ_BLUEHYPERBLASTER = 17, MZ_PHALANX = 18, MZ_SILENCED = 128,
	MZ_ETF_RIFLE = 30, MZ_UNUSED = 31, MZ_SHOTGUN2 = 32, MZ_HEATBEAM = 33, MZ_BLASTER2 = 34,
	MZ_TRACKER = 35, MZ_NUKE1 = 36, MZ_NUKE2 = 37, MZ_NUKE4 = 38, MZ_NUKE8 = 39;

	public static final int MZ2_TANK_BLASTER_1 = 1, MZ2_TANK_BLASTER_2 = 2, MZ2_TANK_BLASTER_3 = 3,
	MZ2_TANK_MACHINEGUN_1 = 4, MZ2_TANK_MACHINEGUN_2 = 5, MZ2_TANK_MACHINEGUN_3 = 6, MZ2_TANK_MACHINEGUN_4 = 7,
	MZ2_TANK_MACHINEGUN_5 = 8, MZ2_TANK_MACHINEGUN_6 = 9, MZ2_TANK_MACHINEGUN_7 = 10, MZ2_TANK_MACHINEGUN_8 = 11,
	MZ2_TANK_MACHINEGUN_9 = 12, MZ2_TANK_MACHINEGUN_10 = 13, MZ2_TANK_MACHINEGUN_11 = 14, MZ2_TANK_MACHINEGUN_12 = 15,
	MZ2_TANK_MACHINEGUN_13 = 16, MZ2_TANK_MACHINEGUN_14 = 17, MZ2_TANK_MACHINEGUN_15 = 18, MZ2_TANK_MACHINEGUN_16 = 19,
	MZ2_TANK_MACHINEGUN_17 = 20, MZ2_TANK_MACHINEGUN_18 = 21, MZ2_TANK_MACHINEGUN_19 = 22, MZ2_TANK_ROCKET_1 = 23,
	MZ2_TANK_ROCKET_2 = 24, MZ2_TANK_ROCKET_3 = 25 , MZ2_INFANTRY_MACHINEGUN_1 = 26, MZ2_INFANTRY_MACHINEGUN_2 = 27,
	MZ2_INFANTRY_MACHINEGUN_3 = 28, MZ2_INFANTRY_MACHINEGUN_4 = 29, MZ2_INFANTRY_MACHINEGUN_5 = 30, MZ2_INFANTRY_MACHINEGUN_6 = 31,
	MZ2_INFANTRY_MACHINEGUN_7 = 32, MZ2_INFANTRY_MACHINEGUN_8 = 33, MZ2_INFANTRY_MACHINEGUN_9 = 34, MZ2_INFANTRY_MACHINEGUN_10 = 35,
	MZ2_INFANTRY_MACHINEGUN_11 = 36, MZ2_INFANTRY_MACHINEGUN_12 = 37, MZ2_INFANTRY_MACHINEGUN_13 = 38 , MZ2_SOLDIER_BLASTER_1 = 39,
	MZ2_SOLDIER_BLASTER_2 = 40, MZ2_SOLDIER_SHOTGUN_1 = 41, MZ2_SOLDIER_SHOTGUN_2 = 42, MZ2_SOLDIER_MACHINEGUN_1 = 43,
	MZ2_SOLDIER_MACHINEGUN_2 = 44 , MZ2_GUNNER_MACHINEGUN_1 = 45, MZ2_GUNNER_MACHINEGUN_2 = 46, MZ2_GUNNER_MACHINEGUN_3 = 47,
	MZ2_GUNNER_MACHINEGUN_4 = 48, MZ2_GUNNER_MACHINEGUN_5 = 49, MZ2_GUNNER_MACHINEGUN_6 = 50, MZ2_GUNNER_MACHINEGUN_7 = 51,
	MZ2_GUNNER_MACHINEGUN_8 = 52, MZ2_GUNNER_GRENADE_1 = 53, MZ2_GUNNER_GRENADE_2 = 54, MZ2_GUNNER_GRENADE_3 = 55,
	MZ2_GUNNER_GRENADE_4 = 56 , MZ2_CHICK_ROCKET_1 = 57 , MZ2_FLYER_BLASTER_1 = 58, MZ2_FLYER_BLASTER_2 = 59 , MZ2_MEDIC_BLASTER_1 = 60 ,
	MZ2_GLADIATOR_RAILGUN_1 = 61 , MZ2_HOVER_BLASTER_1 = 62 , MZ2_ACTOR_MACHINEGUN_1 = 63 , MZ2_SUPERTANK_MACHINEGUN_1 = 64,
	MZ2_SUPERTANK_MACHINEGUN_2 = 65, MZ2_SUPERTANK_MACHINEGUN_3 = 66, MZ2_SUPERTANK_MACHINEGUN_4 = 67, MZ2_SUPERTANK_MACHINEGUN_5 = 68,
	MZ2_SUPERTANK_MACHINEGUN_6 = 69, MZ2_SUPERTANK_ROCKET_1 = 70, MZ2_SUPERTANK_ROCKET_2 = 71, MZ2_SUPERTANK_ROCKET_3 = 72,
	MZ2_BOSS2_MACHINEGUN_L1 = 73, MZ2_BOSS2_MACHINEGUN_L2 = 74, MZ2_BOSS2_MACHINEGUN_L3 = 75, MZ2_BOSS2_MACHINEGUN_L4 = 76,
	MZ2_BOSS2_MACHINEGUN_L5 = 77, MZ2_BOSS2_ROCKET_1 = 78, MZ2_BOSS2_ROCKET_2 = 79, MZ2_BOSS2_ROCKET_3 = 80, MZ2_BOSS2_ROCKET_4 = 81,
	MZ2_FLOAT_BLASTER_1 = 82 , MZ2_SOLDIER_BLASTER_3 = 83, MZ2_SOLDIER_SHOTGUN_3 = 84, MZ2_SOLDIER_MACHINEGUN_3 = 85, MZ2_SOLDIER_BLASTER_4 = 86,
	MZ2_SOLDIER_SHOTGUN_4 = 87, MZ2_SOLDIER_MACHINEGUN_4 = 88, MZ2_SOLDIER_BLASTER_5 = 89, MZ2_SOLDIER_SHOTGUN_5 = 90, MZ2_SOLDIER_MACHINEGUN_5 = 91,
	MZ2_SOLDIER_BLASTER_6 = 92, MZ2_SOLDIER_SHOTGUN_6 = 93, MZ2_SOLDIER_MACHINEGUN_6 = 94, MZ2_SOLDIER_BLASTER_7 = 95, MZ2_SOLDIER_SHOTGUN_7 = 96,
	MZ2_SOLDIER_MACHINEGUN_7 = 97, MZ2_SOLDIER_BLASTER_8 = 98, MZ2_SOLDIER_SHOTGUN_8 = 99, MZ2_SOLDIER_MACHINEGUN_8 = 100 , MZ2_MAKRON_BFG  = 101,
	MZ2_MAKRON_BLASTER_1 = 102, MZ2_MAKRON_BLASTER_2 = 103, MZ2_MAKRON_BLASTER_3 = 104, MZ2_MAKRON_BLASTER_4 = 105, MZ2_MAKRON_BLASTER_5 = 106,
	MZ2_MAKRON_BLASTER_6 = 107, MZ2_MAKRON_BLASTER_7 = 108, MZ2_MAKRON_BLASTER_8 = 109, MZ2_MAKRON_BLASTER_9 = 110, MZ2_MAKRON_BLASTER_10 = 111,
	MZ2_MAKRON_BLASTER_11 = 112, MZ2_MAKRON_BLASTER_12 = 113, MZ2_MAKRON_BLASTER_13 = 114, MZ2_MAKRON_BLASTER_14 = 115, MZ2_MAKRON_BLASTER_15 = 116,
	MZ2_MAKRON_BLASTER_16 = 117, MZ2_MAKRON_BLASTER_17 = 118, MZ2_MAKRON_RAILGUN_1 = 119, MZ2_JORG_MACHINEGUN_L1 = 120, MZ2_JORG_MACHINEGUN_L2 = 121,
	MZ2_JORG_MACHINEGUN_L3 = 122, MZ2_JORG_MACHINEGUN_L4 = 123, MZ2_JORG_MACHINEGUN_L5 = 124, MZ2_JORG_MACHINEGUN_L6 = 125, MZ2_JORG_MACHINEGUN_R1 = 126,
	MZ2_JORG_MACHINEGUN_R2 = 127, MZ2_JORG_MACHINEGUN_R3 = 128, MZ2_JORG_MACHINEGUN_R4 = 129, MZ2_JORG_MACHINEGUN_R5 = 130, MZ2_JORG_MACHINEGUN_R6 = 131,
	MZ2_JORG_BFG_1  = 132, MZ2_BOSS2_MACHINEGUN_R1 = 133, MZ2_BOSS2_MACHINEGUN_R2 = 134, MZ2_BOSS2_MACHINEGUN_R3 = 135, MZ2_BOSS2_MACHINEGUN_R4 = 136,
	MZ2_BOSS2_MACHINEGUN_R5 = 137, MZ2_CARRIER_MACHINEGUN_L1 = 138, MZ2_CARRIER_MACHINEGUN_R1 = 139, MZ2_CARRIER_GRENADE = 140, MZ2_TURRET_MACHINEGUN = 141,
	MZ2_TURRET_ROCKET = 142, MZ2_TURRET_BLASTER = 143, MZ2_STALKER_BLASTER = 144, MZ2_DAEDALUS_BLASTER = 145, MZ2_MEDIC_BLASTER_2 = 146, MZ2_CARRIER_RAILGUN = 147,
	MZ2_WIDOW_DISRUPTOR = 148, MZ2_WIDOW_BLASTER = 149, MZ2_WIDOW_RAIL  = 150, MZ2_WIDOW_PLASMABEAM = 151, MZ2_CARRIER_MACHINEGUN_L2 = 152,
	MZ2_CARRIER_MACHINEGUN_R2 = 153, MZ2_WIDOW_RAIL_LEFT = 154, MZ2_WIDOW_RAIL_RIGHT = 155, MZ2_WIDOW_BLASTER_SWEEP1 = 156, MZ2_WIDOW_BLASTER_SWEEP2 = 157,
	MZ2_WIDOW_BLASTER_SWEEP3 = 158, MZ2_WIDOW_BLASTER_SWEEP4 = 159, MZ2_WIDOW_BLASTER_SWEEP5 = 160, MZ2_WIDOW_BLASTER_SWEEP6 = 161, MZ2_WIDOW_BLASTER_SWEEP7 = 162,
	MZ2_WIDOW_BLASTER_SWEEP8 = 163, MZ2_WIDOW_BLASTER_SWEEP9 = 164, MZ2_WIDOW_BLASTER_100 = 165, MZ2_WIDOW_BLASTER_90 = 166, MZ2_WIDOW_BLASTER_80 = 167,
	MZ2_WIDOW_BLASTER_70 = 168, MZ2_WIDOW_BLASTER_60 = 169, MZ2_WIDOW_BLASTER_50 = 170, MZ2_WIDOW_BLASTER_40 = 171, MZ2_WIDOW_BLASTER_30 = 172,
	MZ2_WIDOW_BLASTER_20 = 173, MZ2_WIDOW_BLASTER_10 = 174, MZ2_WIDOW_BLASTER_0 = 175, MZ2_WIDOW_BLASTER_10L = 176, MZ2_WIDOW_BLASTER_20L = 177,
	MZ2_WIDOW_BLASTER_30L = 178, MZ2_WIDOW_BLASTER_40L = 179, MZ2_WIDOW_BLASTER_50L = 180, MZ2_WIDOW_BLASTER_60L = 181, MZ2_WIDOW_BLASTER_70L = 182,
	MZ2_WIDOW_RUN_1 = 183, MZ2_WIDOW_RUN_2  = 184, MZ2_WIDOW_RUN_3  = 185, MZ2_WIDOW_RUN_4  = 186, MZ2_WIDOW_RUN_5  = 187, MZ2_WIDOW_RUN_6  = 188,
	MZ2_WIDOW_RUN_7 = 189, MZ2_WIDOW_RUN_8  = 190, MZ2_CARRIER_ROCKET_1 = 191, MZ2_CARRIER_ROCKET_2 = 192, MZ2_CARRIER_ROCKET_3 = 193, MZ2_CARRIER_ROCKET_4 = 194,
	MZ2_WIDOW2_BEAMER_1 = 195, MZ2_WIDOW2_BEAMER_2 = 196, MZ2_WIDOW2_BEAMER_3 = 197, MZ2_WIDOW2_BEAMER_4 = 198, MZ2_WIDOW2_BEAMER_5 = 199,
	MZ2_WIDOW2_BEAM_SWEEP_1 = 200, MZ2_WIDOW2_BEAM_SWEEP_2 = 201, MZ2_WIDOW2_BEAM_SWEEP_3 = 202, MZ2_WIDOW2_BEAM_SWEEP_4 = 203, MZ2_WIDOW2_BEAM_SWEEP_5 = 204,
	MZ2_WIDOW2_BEAM_SWEEP_6 = 205, MZ2_WIDOW2_BEAM_SWEEP_7 = 206, MZ2_WIDOW2_BEAM_SWEEP_8 = 207, MZ2_WIDOW2_BEAM_SWEEP_9 = 208, MZ2_WIDOW2_BEAM_SWEEP_10 = 209,
	MZ2_WIDOW2_BEAM_SWEEP_11 = 210;

	public static final int EF_ROTATE = 0x00000001, EF_GIB = 0x00000002, EF_BLASTER = 0x00000008,
	EF_ROCKET = 0x00000010, EF_GRENADE = 0x00000020, EF_HYPERBLASTER = 0x00000040, EF_BFG = 0x00000080,
	EF_COLOR_SHELL = 0x00000100,  EF_POWERSCREEN = 0x00000200, EF_ANIM01 = 0x00000400, EF_ANIM23 = 0x00000800,
	EF_ANIM_ALL = 0x00001000, EF_ANIM_ALLFAST = 0x00002000, EF_FLIES = 0x00004000, EF_QUAD = 0x00008000,
	EF_PENT = 0x00010000, EF_TELEPORTER = 0x00020000,  EF_FLAG1 = 0x00040000, EF_FLAG2 = 0x00080000,
	EF_IONRIPPER = 0x00100000,  EF_GREENGIB = 0x00200000, EF_BLUEHYPERBLASTER = 0x00400000,
	EF_SPINNINGLIGHTS = 0x00800000,  EF_PLASMA = 0x01000000, EF_TRAP = 0x02000000,  EF_TRACKER = 0x04000000,
	EF_DOUBLE = 0x08000000, EF_SPHERETRANS = 0x10000000,  EF_TAGTRAIL = 0x20000000,  EF_HALF_DAMAGE = 0x40000000,
	EF_TRACKERTRAIL = 0x80000000;

	public static final int RF_MINLIGHT = 1, RF_VIEWERMODEL = 2, RF_WEAPONMODEL = 4, RF_FULLBRIGHT = 8,
	RF_DEPTHHACK = 16, RF_TRANSLUCENT = 32, RF_FRAMELERP = 64,  RF_BEAM = 128, RF_CUSTOMSKIN = 256,
	RF_GLOW = 512,  RF_SHELL_RED = 1024, RF_SHELL_GREEN = 2048,  RF_SHELL_BLUE = 4096;

/*-------------------------------------------------------------------*/
/**	Default constructor. */
/*-------------------------------------------------------------------*/
	public Effects()
	{	}
	
/*-------------------------------------------------------------------*/
/**	Parameterised constructor.
 *	@param effects OR'd integer value indicating the visual effects currently
 *	in operation
 *	@param renderEffects lighting effects and similar */
/*-------------------------------------------------------------------*/
	public Effects(int effects, int renderEffects)
	{
		this.effects = effects;
		this.renderEffects = renderEffects;
	}
	
/*-------------------------------------------------------------------*/
/**	Set effects bitmask.
 *	@param effects effects bitmask. */
/*-------------------------------------------------------------------*/
	public void setEffects(int effects)
	{
		this.effects = effects;
	}

/*-------------------------------------------------------------------*/
/**	Get effects bitmask.
 *	@return effects bitmask. */
/*-------------------------------------------------------------------*/
	public int getEffects()
	{
		return effects;
	}

/*-------------------------------------------------------------------*/
/**	Compare the effects bitmask against a given bitmask to determine
 *	if the specified effects are active. Typically, the sub-bitmask
 *	is constructed using the Effects (EF) constants listed above.
 *	@param eff the bitmask against which to compare the effects bitmask
 *	@return true if the given bitmask fits the effects bitmask */
/*-------------------------------------------------------------------*/
	public boolean checkEffects(int eff)
	{
		return effects >= 0 && ((effects & eff) != 0);
	}

/*-------------------------------------------------------------------*/
/**	Get concatenated string denoting current effects bitmask. E.g.
 *	bitmask = 0x00018000, effectsString = "quad:pent:".
 *	@return effects string. */
/*-------------------------------------------------------------------*/
	public String getEffectsString()
	{
		String result;

		result = new String();
		if((effects & 0x00000001) != 0) result += "rotate:";
		if((effects & 0x00000002) != 0) result += "gib:";
		if((effects & 0x00000004) != 0) result += "???:";
		if((effects & 0x00000008) != 0) result += "blaster:";
		if((effects & 0x00000010) != 0) result += "rocket:";
		if((effects & 0x00000020) != 0) result += "grenade:";
		if((effects & 0x00000040) != 0) result += "hyperBlaster:";
		if((effects & 0x00000080) != 0) result += "bfg:";
		if((effects & 0x00000100) != 0) result += "colorShell:";
		if((effects & 0x00000200) != 0) result += "powerScreen:";
		if((effects & 0x00000400) != 0) result += "animation01:";
		if((effects & 0x00000800) != 0) result += "animation23:";
		if((effects & 0x00001000) != 0) result += "animationAll:";
		if((effects & 0x00002000) != 0) result += "animationAllFast:";
		if((effects & 0x00004000) != 0) result += "flies:";
		if((effects & 0x00008000) != 0) result += "quad:";
		if((effects & 0x00010000) != 0) result += "pent:";
		if((effects & 0x00020000) != 0) result += "teleporter:";
		if((effects & 0x00040000) != 0) result += "flag1:";
		if((effects & 0x00080000) != 0) result += "flag2:";
		if((effects & 0x00100000) != 0) result += "boomer:";
		if((effects & 0x00200000) != 0) result += "greenGib:";
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Set render effects bitmask.
 *	@param renderEffects render effects bitmask. */
/*-------------------------------------------------------------------*/
	public void setRenderEffects(int renderEffects)
	{
		this.renderEffects = renderEffects;
	}

/*-------------------------------------------------------------------*/
/**	Get render effects bitmask.
 *	@return render effects bitmask. */
/*-------------------------------------------------------------------*/
	public int getRenderEffects()
	{
		return renderEffects;
	}

/*-------------------------------------------------------------------*/
/**	Compare the render effects bitmask against a given bitmask to determine
 *	if the specified render effects are active. Typically, the sub-bitmask
 *	is constructed using the Render Effects (RF) constants listed above.
 *	@param reff the bitmask against which to compare the render effects
 *	bitmask
 *	@return true if the given bitmask fits the render effects bitmask */
/*-------------------------------------------------------------------*/
	public boolean checkRenderEffects(int reff)
	{
		return renderEffects >= 0 && ((renderEffects & reff) != 0);
	}

/*-------------------------------------------------------------------*/
/**	Get concatenated string denoting current render effects
 *	bitmask. E.g. bitmask = 0x00001200, effectsString = 
 *	"shellBlue:glow:".
 *	@return render effects string. */
/*-------------------------------------------------------------------*/
	public String getRenderEffectsString()
	{
		String result;

		result = new String();
		if((renderEffects & 0x00000001) != 0) result += "minLight:";
		if((renderEffects & 0x00000002) != 0) result += "viewerModel:";
		if((renderEffects & 0x00000004) != 0) result += "weaponModel:";
		if((renderEffects & 0x00000008) != 0) result += "fullBright:";
		if((renderEffects & 0x00000010) != 0) result += "depthHack:";
		if((renderEffects & 0x00000020) != 0) result += "translucent:";
		if((renderEffects & 0x00000040) != 0) result += "frameLerp:";
		if((renderEffects & 0x00000080) != 0) result += "beam:";
		if((renderEffects & 0x00000100) != 0) result += "customSkins:";
		if((renderEffects & 0x00000200) != 0) result += "glow:";
		if((renderEffects & 0x00000400) != 0) result += "shellRed:";
		if((renderEffects & 0x00000800) != 0) result += "shellGreen:";
		if((renderEffects & 0x00001000) != 0) result += "shellBlue:";
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Merge Effects properties from an existing Effects object into the
 *	current Effects object. Used when assimilating cumulative updates
 *	from the Quake 2 server into the gamestate.
 *	@param effects source Effects whose attributes should be merged
 *	into the current Effects
 *	@see soc.qase.state.World#setEntity(Entity, boolean) */
/*-------------------------------------------------------------------*/
	public void merge(Effects effects)
	{
		if(effects != null) {
			if(this.effects == -1) this.effects = effects.getEffects();
			if(this.renderEffects == -1) this.renderEffects = effects.getRenderEffects();
		}
	}

/*-------------------------------------------------------------------*/
/**	Creates a duplicate of the object by cloning each of its fields.
 *	@return a deep copy of the current object */
/*-------------------------------------------------------------------*/
	public Effects deepCopy()
	{
		Effects eff = new Effects();

		eff.setEffects(effects);
		eff.setRenderEffects(renderEffects);

		return eff;
	}
}
