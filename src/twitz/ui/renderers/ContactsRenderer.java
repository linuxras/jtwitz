package twitz.ui.renderers;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import twitter4j.User;
import twitz.testing.*;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.renderers.*;

public class ContactsRenderer extends SubstanceDefaultListCellRenderer {
	
	public ContactsRenderer()
	{
		super();
		this.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, 1.0);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus)
	{
		super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
		UserTest u = (UserTest)value;
		java.net.URL imgURI = u.getProfileImageURL();
		javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgURI);
		Image img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);
		StringBuffer buf = new StringBuffer("<html><center><b>");
		buf.append(u.getScreenName()+"</b></center>From: ");
		buf.append(u.getLocation()+"<br/>");
		buf.append("Joined: "+u.getCreatedAt().toString()+"");
		setText("<html><b>"+u.getScreenName()+"</b>");
		setIcon(icon);
		setToolTipText(buf.toString());
		setVerticalAlignment(TOP);
		return this;
	}
}
