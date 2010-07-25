package twitz.ui.renderers;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import org.jdesktop.application.ResourceMap;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.renderers.*;
import twitter4j.Tweet;
import twitter4j.util.TimeSpanUtil;
import twitz.TwitzApp;
import twitz.TwitzMainView;

public class TweetListRenderer extends SubstanceDefaultListCellRenderer {

	org.jdesktop.application.ResourceMap resourceMap = twitz.TwitzApp.getContext().getResourceMap(twitz.TwitzMainView.class);

	public TweetListRenderer()
	{
		super();
		this.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, 1.0);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus)
	{
		super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
		Tweet s = (Tweet)value;
		//User u = s.getUser();
		//Place p = s.getPlace();
		Date d = s.getCreatedAt();

		setFont(new Font("Arial", Font.BOLD, 10)); //TODO: put this in the resourceMap so its not hard coded
//		URL imgURI = null;
//		try
//		{
//			imgURI = new URL(s.getProfileImageUrl());
//		}
//		catch (MalformedURLException ex)
//		{
//			//ignore
//		}
//
//		ImageIcon icon = null;
//		if(imgURI != null)
//		{
//			icon = new ImageIcon(imgURI);
//			int status = icon.getImageLoadStatus();
//			Image img = null;
//			if (status == MediaTracker.ERRORED)
//			{
//				ResourceMap res = TwitzApp.getContext().getResourceMap(TwitzMainView.class);
//				icon = res.getImageIcon("icon.comments");
//				img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
//			}
//			else
//			{
//				img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
//			}
//			icon = new ImageIcon(img);
//		}
//		else
//		{
//			Image img = null;
//			ResourceMap res = TwitzApp.getContext().getResourceMap(TwitzMainView.class);
//			icon = res.getImageIcon("icon.comments");
//			img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
//			icon = new ImageIcon(img);
//		}
		//getScaledInstance(int width, int height, int hints)
		setVerticalAlignment(TOP);

		StringBuilder buf = new StringBuilder("<p>");
		if(s.getToUser() != null && !s.getToUser().equals("")) {
			buf.append("<strong><font color=\"blue\">@");
			buf.append(s.getToUser());
			buf.append(" - </font></strong>");
		}
		buf.append(s.getText());
		buf.append("</p>");
		StringBuilder tbuf = new StringBuilder();
		tbuf.append("<p><center><strong>");
		tbuf.append(s.getFromUser()).append("</strong><br/><em>");
		tbuf.append(TimeSpanUtil.toTimeSpanString(d));
		tbuf.append("</em></center></p>");

		//setIcon(icon);
		setToolTipText("<html>"+tableWrap(s, tbuf.toString(), 200));
		//setToolTipText("<html>"+tableWrap(buf.toString(), 250));
		int width = list.getWidth()-50;
		//System.out.println("List width: "+width);
		if(list.getParent() != null)
		{
			width = list.getParent().getWidth()-85;
			//System.out.println("Parent found using width: "+width);
		}
		setText("<html>"+tableWrap(s, buf.toString(), width));

		setVerticalAlignment(TOP);
		setHorizontalAlignment(CENTER);
		Border margin = new EmptyBorder(5,10,5,10);

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

	private String tableWrap(Tweet s, String source, int width) {
		String resourcesDir = resourceMap.getResourcesDir();
		String filename = resourcesDir + resourceMap.getString("icon.picture_empty");
		//filename = resourceMap.getResourcesDir() + resourceMap.getString("icon.comment");
		URL altImg = resourceMap.getClassLoader().getResource(filename);

		URL img = null;
		try
		{
			img = new URL(s.getProfileImageUrl());
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(TweetListRenderer.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
		}
		
		img = twitz.TwitzApp.verifyImage(img);
		
		StringBuilder table = new StringBuilder("<table border=0 width=");
		table.append(width).append("><tr><td align='left'>")
				.append("<img border=0 width=32 height=32 src='")
				.append(img.toString()).append("'>")
				.append("</td><td>")
				.append(source)
				.append("</td></tr></table>");
		return table.toString();
	}

}
