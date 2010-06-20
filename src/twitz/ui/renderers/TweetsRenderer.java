package twitz.ui.renderers;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.net.URL;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.jdesktop.application.ResourceMap;
import twitter4j.User;
import twitz.testing.*;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.renderers.*;
import twitter4j.Place;
import twitter4j.Status;
import twitz.TwitzApp;
import twitz.TwitzMainView;

public class TweetsRenderer extends SubstanceDefaultListCellRenderer {
	
	public TweetsRenderer()
	{
		super();
		this.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, 1.0);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus)
	{
		super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
		Status s = (Status)value;
		User u = s.getUser();
		Place p = s.getPlace();
		Date d = s.getCreatedAt();

		setFont(new Font("Arial", Font.BOLD, 10)); //TODO: put this in the resourceMap so its not hard coded
		URL imgURI = u.getProfileImageURL();
		ImageIcon icon = new ImageIcon(imgURI);
		int status = icon.getImageLoadStatus();
		Image img = null;
		if(status == MediaTracker.ERRORED) {
			ResourceMap res = TwitzApp.getContext().getResourceMap(TwitzMainView.class);
			icon = res.getImageIcon("icon.comments");
			img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
		}
		else {
			img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
		}
		icon = new ImageIcon(img);
		//getScaledInstance(int width, int height, int hints)
		setVerticalAlignment(TOP);

		StringBuffer buf = new StringBuffer("<p>");
		if(!s.getInReplyToScreenName().equals("")) {
			buf.append("<strong><font color=\"blue\">@");
			buf.append(s.getInReplyToScreenName());
			buf.append(" - </font></strong>");
		}
		buf.append(s.getText());
		buf.append("<hr><center><strong>");
		buf.append(u.getScreenName()+"</strong><br/><em>");
		buf.append(d.toString());
		buf.append("<br/>"+p.getName()+"</em></center></p>");

		setIcon(icon);
		//setToolTipText("<html>"+tableWrap(buf.toString(), 250));
		setText("<html>"+tableWrap(buf.toString(), list.getWidth()-50));

		setVerticalAlignment(TOP);
		return this;
	}

	private String tableWrap(String source, int width) {
		StringBuffer table = new StringBuffer("<table border=0 width=");
		table.append(width+"");
		table.append("><tr><td>");
		table.append(source);
		table.append("</td></tr></table>");
		return table.toString();
	}

}
