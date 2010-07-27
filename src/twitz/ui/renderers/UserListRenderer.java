/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.renderers;

import java.awt.Component;
import javax.swing.JList;
import org.jdesktop.application.ResourceMap;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultListCellRenderer;
import twitter4j.Trend;
import twitter4j.UserList;

/**
 *
 * @author mistik1
 */
public class UserListRenderer extends SubstanceDefaultListCellRenderer {

	private ResourceMap resource;

	public UserListRenderer()
	{
		super();
		resource = twitz.TwitzApp.getContext().getResourceMap(twitz.TwitzMainView.class);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		Component rv = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		setHorizontalTextPosition(RIGHT);
		setOpaque(true);
		if(isSelected) {
			//set an icon
			setIcon(resource.getIcon("icon.tick"));
		}
		String text = "";
		String tip = "";
		/* This code will need to accept a twitter4j.Trend as value and process it
		 * from there.
		 */
		if(value instanceof UserList)
		{//do Trend processing
			UserList local = (UserList)value;
			
			text = local.getName();
			tip = local.getName();
		}
		else
		{ //Internal testing mode
			text = (String)value;
			tip = (String)value;
		}

		setText(text);
		setToolTipText(tip);
		
		return rv;
	}

}
