package twitz.ui.renderers;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.jdesktop.application.ResourceMap;
import twitter4j.Status;
import twitter4j.User;
import twitz.testing.*;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.renderers.*;
import twitz.TwitzApp;
import twitz.TwitzMainView;

public class UserComboRenderer extends SubstanceDefaultListCellRenderer {

	org.jdesktop.application.ResourceMap resourceMap = twitz.TwitzApp.getContext().getResourceMap(twitz.TwitzMainView.class);

	public UserComboRenderer()
	{
		super();
		this.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, 1.0);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus)
	{
		super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
		if(value instanceof User)
		{
		User u = (User)value;
//		java.net.URL imgURI = u.getProfileImageURL();
//		javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgURI);
//		int status = icon.getImageLoadStatus();
//		Image img = null;
//		if(status == MediaTracker.ERRORED) {
//			ResourceMap res = TwitzApp.getContext().getResourceMap(TwitzMainView.class);
//			icon = res.getImageIcon("icon.user_gray");
//			img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
//		}
//		else {
//			img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
//		}
//		//Image img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
//		icon = new ImageIcon(img);
		Status s = u.getStatus();
		int width = list.getWidth()-50;
		//System.out.println("List width: "+width);
		if(list.getParent() != null)
		{
			width = list.getParent().getWidth()-10;//85;
			//System.out.println("Parent found using width: "+width);
		}
		setText("<html>"+tableWrap(u, width));
//		setIcon(icon);
		setToolTipText("<html>"+tableWrap(u, width));
		setVerticalAlignment(TOP);
		setFont(new Font("Arial", Font.BOLD, 10));
		}
		else
		{
			setText(value.toString());
		}
		return this;
	}

	private String tableWrap(User u, Status s, int width) //{{{
	{
		StringBuilder table = new StringBuilder("<table border=0 width=");
		table.append(width);
		table.append("><tr align='center' ><td><b>");
		table.append(u.getScreenName());
		table.append("</b></td></tr>");
		if(s != null && s.getText() != null)
		{
			table.append("<tr><td>");
			table.append(pretify(s.getText()));
			table.append("</td></tr>");
		}
		if(u.getLocation() != null)
		{
			table.append("<tr><td>From: ").append(u.getLocation()).append("</td></tr>");
		}
		table.append("</table>");
		return table.toString();
	} //}}}

	private String tableWrap(User u, int width)
	{
		URL img = twitz.TwitzApp.verifyImage(u.getProfileImageURL());

		StringBuilder tb = new StringBuilder();
		tb.append("<table width=").append(width).append(">")
				.append("<tr><td align='left'>")
				.append("<img border=0 width=32 height=32 src='")
				.append(img.toString()).append("'></td><td>")
				.append("<b>").append(u.getScreenName()).append("</b>")
				.append("</td></tr></table>");
		return tb.toString();
	}

	private String pretify(String text)//{{{
	{
		ArrayList<String> list = new ArrayList<String>();
		String rv = "";
		String left = "<strong><font color=\"blue\">";
		String right = "</font></strong>";
		Pattern p = Pattern.compile("([#@]\\w+?)\\s");
		Matcher m = p.matcher(text);
		while(m.find())
		{
			list.add(m.group());
		}
		String t = text;
		for(String s:list)
		{
			t = t.replaceAll(s, left+s+right);
		}
		rv = t;
		return rv;
	} //}}}
}
