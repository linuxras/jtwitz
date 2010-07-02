package twitz.ui.renderers;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.jdesktop.application.ResourceMap;
import twitter4j.User;
import twitz.testing.*;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.renderers.*;
import twitz.TwitzApp;
import twitz.TwitzMainView;

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
		User u = (User)value;
		java.net.URL imgURI = u.getProfileImageURL();
		javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgURI);
		int status = icon.getImageLoadStatus();
		Image img = null;
		if(status == MediaTracker.ERRORED) {
			ResourceMap res = TwitzApp.getContext().getResourceMap(TwitzMainView.class);
			icon = res.getImageIcon("icon.user_gray");
			img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
		}
		else {
			img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
		}
		//Image img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);
		StringBuffer buf = new StringBuffer("<html><center><b>");
		buf.append(u.getScreenName()+"</b></center>From: ");
		buf.append(u.getLocation()+"<br/>");
		buf.append("Joined: "+u.getCreatedAt().toString()+"");
		setText("<html><b>"+u.getScreenName()+"</b>");
		setIcon(icon);
		setToolTipText(buf.toString());
		setVerticalAlignment(TOP);
		setFont(new Font("Arial", Font.BOLD, 10));
		return this;
	}
}
