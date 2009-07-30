//---------------------------------------------------------------------
// Name:			Layout.java
// Author:			Martin.Fredriksson@bth.se
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.info;

/*-------------------------------------------------------------------*/
/**	HUD layout wrapper class. */
/*-------------------------------------------------------------------*/
public class Layout
{
	private String layoutInfo = null;

/*-------------------------------------------------------------------*/
/**	Constructor.
 *	@param layoutInfo HUD layout information. */
/*-------------------------------------------------------------------*/
	public Layout(String layoutInfo)
	{
		this.layoutInfo = layoutInfo;
	}
	
/*-------------------------------------------------------------------*/
/**	Get HUD layout information.
 *	@return String representation of HUD layout information. */
/*-------------------------------------------------------------------*/
	public String getLayout()
	{
		return layoutInfo;
	}
}

