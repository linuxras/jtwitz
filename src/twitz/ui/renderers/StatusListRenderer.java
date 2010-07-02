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
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import org.jdesktop.application.ResourceMap;
import twitter4j.User;
import twitz.testing.*;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.renderers.*;
import twitter4j.Place;
import twitter4j.Status;
import twitz.TwitzApp;
import twitz.TwitzMainView;

public class StatusListRenderer extends SubstanceDefaultListCellRenderer {
	
	public StatusListRenderer()
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
		buf.append("</p>");
		StringBuffer tbuf = new StringBuffer();
		tbuf.append("<p><center><strong>");
		tbuf.append(u.getScreenName()+"</strong><br/><em>");
		tbuf.append(d.toString());
		tbuf.append("<br/>"+p.getName()+"</em></center></p>");

		setIcon(icon);
		setToolTipText("<html>"+tableWrap(tbuf.toString(), 200));
		//setToolTipText("<html>"+tableWrap(buf.toString(), 250));
		int width = list.getWidth()-50;
		//System.out.println("List width: "+width);
		if(list.getParent() != null)
		{
			width = list.getParent().getWidth()-85;
			//System.out.println("Parent found using width: "+width);
		}
		setText("<html>"+tableWrap(buf.toString(), width));

		setVerticalAlignment(TOP);
		setHorizontalAlignment(CENTER);
		Border margin = new EmptyBorder(10,10,10,10);

		Border inraised = new SoftBevelBorder(SoftBevelBorder.RAISED);
		Border inlowered = new SoftBevelBorder(SoftBevelBorder.LOWERED);
		Border raised = new CompoundBorder(margin, inraised);
		Border lowered = new CompoundBorder(margin, inlowered);
		if(isSelected) {
			setBorder(lowered);
		}
		else
		{
			setBorder(raised);
		}
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
