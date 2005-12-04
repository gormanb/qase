//---------------------------------------------------------------------
// Name:			Model.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.state;

import soc.qase.com.message.*;

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
	private int modelSkin = -1;

/*-------------------------------------------------------------------*/
/**	Constructor. */
/*-------------------------------------------------------------------*/
	public Model()
	{	}
	
/*-------------------------------------------------------------------*/
/**	Set model skin.
 *	@param modelSkin model skin. */
/*-------------------------------------------------------------------*/
	public void setSkin(int modelSkin)
	{
		this.modelSkin = modelSkin;
	}

/*-------------------------------------------------------------------*/
/**	Get model skin.
 *	@return model skin. */
/*-------------------------------------------------------------------*/
	public int getSkin()
	{
		return modelSkin;
	}

/*-------------------------------------------------------------------*/
/**	Set model frame.
 *	@param modelFrame model frame. */
/*-------------------------------------------------------------------*/
	public void setFrame(int modelFrame)
	{
		this.modelFrame = modelFrame;
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
		if(model != null) {
			setIndex(0, model.getIndex(0));
			setIndex(1, model.getIndex(1));
			setIndex(2, model.getIndex(2));
			setIndex(3, model.getIndex(3));
			if(getFrame() == -1) setFrame(model.getFrame());
			if(getSkin() == -1) setSkin(model.getSkin());
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
