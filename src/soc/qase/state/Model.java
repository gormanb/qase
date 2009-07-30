//---------------------------------------------------------------------
// Name:			Model.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

/*-------------------------------------------------------------------*/
/**	Wrapper class for model attributes. */
/*-------------------------------------------------------------------*/
public class Model
{
	private int modelIndex00 = -1;
	private int modelIndex01 = -1;
	private int modelIndex02 = -1;
	private int modelIndex03 = -1;

	private int modelFrame = -1;
	private long modelSkin = -1;

/*-------------------------------------------------------------------*/
/**	Constructor. */
/*-------------------------------------------------------------------*/
	public Model()
	{	}

/*-------------------------------------------------------------------*/
/**	Parameterised constructor.
 *	@param modelValues an array containing the indices within the
 *	config array of model strings 0-3
 *	@param mFrame the model frame number
 *	@param mSkin the index to the model skin */
/*-------------------------------------------------------------------*/
	public Model(int[] modelValues, int mFrame, long mSkin)
	{
		setSkin(mSkin);
		setFrame(mFrame);

		for(int i = 0; i < 4; i++)
			setIndex(i, modelValues[i]);
	}

/*-------------------------------------------------------------------*/
/**	Set model skin. In the case of Entity objects of category "players",
 *	this value indicates both the model skin (bytes 1-2) and the skin of
 *	the currently-equipped weapon (bytes 2-3). getSkin returns the former,
 *	getWeaponNumber returns the latter, and getFullSkin returns the
 *	unprocessed long value containing both.
 *	@param mSkin model skin. */
/*-------------------------------------------------------------------*/
	public void setSkin(long mSkin)
	{
		modelSkin = mSkin;
	}

/*-------------------------------------------------------------------*/
/**	Get index of player skin.
 *	@return index of skin in the playerskin subsection of the Config
 *	table. */
/*-------------------------------------------------------------------*/
	public int getSkin()
	{
		return (modelSkin == -1 ? -1 : (int)(modelSkin & 0xFF));
	}

/*-------------------------------------------------------------------*/
/**	Returns the index of the skin which defines the appearance of the
 *	weapon currently being wielded by a player; this method is only
 *	applicable to Model objects contained within Entities of category
 *	"players". The value returned by this method is is the index of the
 *	active weapon's entry in the weaponskin subsection of the Config
 *	table, the start of which is defined by the SECTION_WEAPON_SKINS
 *	constant in the Config class. Calling the method
 *	config.getConfigString(SECTION_WEAPON_SKINS,model.getWeaponNumber())
 *	or simply config.getWeaponSkinString(model.getWeaponSkin) will return
 *	the weapon skin string, i.e. its MD2 filename.
 *
 *	Adding 6 to the weapon skin index will give the inventory index which
 *	corresponds to this weapon. The getWeaponInventoryIndex method of the
 *	Entity class can be used to get the inventory index directly. Calling
 *	config.getItemName(entity.getWeaponInventoryIndex()) or
 *	inventory.getItemName(entity.getWeaponInventoryIndex()) will return
 *	the full English-language name of the weapon.
 *
 *	@return index of the weapon in the SECTION_WEAPON_SKINS subsection
 *	of the Config table, or -1 if no such skin exists. */
/*-------------------------------------------------------------------*/
	public int getWeaponSkin()
	{
		return (modelSkin == -1 ? -1 : (int)(modelSkin >> 8));
	}

/*-------------------------------------------------------------------*/
/**	Returns a long value indicating the Config indices of both the
 *	player's skin (bytes 1-2) and the skin of the active weapon (bytes
 *	2-3). Only applicable to Entity objects of category "players".
 *	@return value indicating indices of player skin and weapon skin. */
/*-------------------------------------------------------------------*/
	public long getFullSkin()
	{
		return modelSkin;
	}

/*-------------------------------------------------------------------*/
/**	Set model frame.
 *	@param mFrame model frame. */
/*-------------------------------------------------------------------*/
	public void setFrame(int mFrame)
	{
		modelFrame = mFrame;
	}

/*-------------------------------------------------------------------*/
/**	Get model frame.
 *	@return model frame. */
/*-------------------------------------------------------------------*/
	public int getFrame()
	{
		return modelFrame;
	}

/*-------------------------------------------------------------------*/
/**	Set modex index value.
 *	@param modelIndex model index.
 *	@param modelValue model value. */
/*-------------------------------------------------------------------*/
	public void setIndex(int modelIndex, int modelValue)
	{
		if(modelIndex == 0) modelIndex00 = modelValue;
		if(modelIndex == 1) modelIndex01 = modelValue;
		if(modelIndex == 2) modelIndex02 = modelValue;
		if(modelIndex == 3) modelIndex03 = modelValue;
	}

/*-------------------------------------------------------------------*/
/**	Get model index value.
 *	@param modelIndex model index.
 *	@return model index value. */
/*-------------------------------------------------------------------*/
	public int getIndex(int modelIndex)
	{
		int result = 0;
		
		if(modelIndex == 0) result = modelIndex00;
		else if(modelIndex == 1) result = modelIndex01;
		else if(modelIndex == 2) result = modelIndex02;
		else if(modelIndex == 3) result = modelIndex03;
		else result = 0;
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Merge Model properties from an existing Model object into the
 *	current Model object. Used when assimilating cumulative updates
 *	from the Quake 2 server into the gamestate.
 *	@param model source Model whose attributes should be merged
 *	into the current Model
 *	@see soc.qase.state.World#setEntity(Entity, boolean) */
/*-------------------------------------------------------------------*/
	public void merge(Model model)
	{
		if(model != null)
		{
			setIndex(0, model.getIndex(0));
			setIndex(1, model.getIndex(1));
			setIndex(2, model.getIndex(2));
			setIndex(3, model.getIndex(3));
			if(getFrame() == -1) setFrame(model.getFrame());
			if(getFullSkin() == -1) setSkin(model.getFullSkin());
		}
	}

/*-------------------------------------------------------------------*/
/**	Creates a duplicate of the object by cloning each of its fields.
 *	@return a deep copy of the current object */
/*-------------------------------------------------------------------*/
	public Model deepCopy()
	{
		Model mod = new Model();

		mod.setIndex(0, modelIndex00);
		mod.setIndex(1, modelIndex01);
		mod.setIndex(2, modelIndex02);
		mod.setIndex(3, modelIndex03);

		mod.setFrame(modelFrame);
		mod.setSkin(modelSkin);

		return mod;
	}
}
