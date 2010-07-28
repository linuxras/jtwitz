/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.renderers;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.jdesktop.application.ResourceMap;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultListCellRenderer;
import twitter4j.Location;

/**
 *
 * @author Andrew Williams
 */
public class LocationListRenderer extends SubstanceDefaultListCellRenderer {

	private ResourceMap resource;
	private boolean countryMode = true;

	public LocationListRenderer(boolean countryMode)
	{
		super();
		this.countryMode = countryMode;
		resource = twitz.TwitzApp.getContext().getResourceMap(twitz.TwitzMainView.class);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		Component rv = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		setHorizontalTextPosition(LEFT);
		setOpaque(true);
		if(isSelected) {
			//set an icon
			setIcon(resource.getIcon("icon.tick"));
		}
		String text = "";
		String tip = "";
		/* This code will need to accept a twitter4j.Location as value and process it
		 * from there.
		 */
		if(value instanceof Location)
		{//do Location processing
			Location local = (Location)value;
			if(countryMode)
			{
				text = local.getCountryName();
				tip = local.getCountryName();
			}
			else {
				text = local.getPlaceName();
				tip = local.getPlaceName();
			}
		}
		else
		{ //Internal testing mode
			text = (String)value;
			tip = (String)value;
		}

		setText(text);
		setToolTipText(tip);
		
		return rv;
		//throw new UnsupportedOperationException("Not supported yet.");
	}

}
