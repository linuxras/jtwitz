package twitz.ui.renderers;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Collections;
import java.util.Iterator;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import org.jdesktop.application.ResourceMap;
import twitz.testing.*;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.renderers.*;
import twitter4j.Place;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.util.TimeSpanUtil;
import twitz.TwitzApp;
import twitz.TwitzMainView;
import twitz.ui.dialogs.StatusPopupPanel;
import twitz.ui.StatusList;
import twitz.util.ListHotSpot;

public class StatusListRenderer extends SubstanceDefaultListCellRenderer {
	
	org.jdesktop.application.ResourceMap resourceMap = twitz.TwitzApp.getContext().getResourceMap(twitz.TwitzMainView.class);
	private String statusTitle = "Status";
	private Border border = BorderFactory.createTitledBorder(new SoftBevelBorder(BevelBorder.RAISED), statusTitle, TitledBorder.LEADING, TitledBorder.BELOW_TOP); // NOI18N
	private int selection = -1;
	private StatusList source = null;
	private boolean selected = false;
	private boolean focused = false;
	private Status status = null;
	private String spotTip = "";

	public StatusListRenderer()
	{
		super();
		this.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, 1.0);
	}
	
	private void updateBorder(boolean selected)//{{{
	{
		border = BorderFactory.createTitledBorder(new SoftBevelBorder(selected ? BevelBorder.LOWERED : BevelBorder.RAISED), statusTitle, TitledBorder.LEADING, TitledBorder.BELOW_TOP);
		setBorder(border);
	}//}}}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) //{{{
	{
		super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
		Status s = (Status)value;
		User u = s.getUser();
		Place p = s.getPlace();
		Date d = s.getCreatedAt();
		selection = index;
		source = (StatusList)list;
		focused = hasFocus;
		selected = isSelected;

		setFont(new Font("Arial", Font.BOLD, 10)); //TODO: put this in the resourceMap so its not hard coded
		URL imgURI = u.getProfileImageURL();
		ImageIcon icon = new ImageIcon(imgURI);
		int status = icon.getImageLoadStatus();
		Image img = null;
		if(status == MediaTracker.ERRORED) {
			//ResourceMap res = TwitzApp.getContext().getResourceMap(TwitzMainView.class);
			icon = resourceMap.getImageIcon("icon.comments");
			img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
		}
		else {
			img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
		}
		icon = new ImageIcon(img);
		//getScaledInstance(int width, int height, int hints)

	//	StringBuffer tbuf = new StringBuffer();
	//	tbuf.append("<p><center><strong>");
	//	tbuf.append(u.getScreenName()+"</strong><br/><em>");
	//	tbuf.append(TimeSpanUtil.toTimeSpanString(d));
	//	if(p != null)
	//	{
	//		tbuf.append("<br/>"+p.getName()+"</em></center></p>");
	//	}
	//	else
	//	{
	//		tbuf.append("</em></center></p>");
	//	}

		setIcon(icon);
	//	setToolTipText("<html>"+tableWrap(tbuf.toString(), 200));
	//	//setToolTipText("<html>"+tableWrap(buf.toString(), 250));
		int width = list.getWidth()-50;
		//System.out.println("List width: "+width);
		if(list.getParent() != null)
		{
			width = list.getParent().getWidth()-85;
			//System.out.println("Parent found using width: "+width);
		}
		
		statusTitle = s.getUser().getScreenName();
		setText("<html>"+tableWrap(s, width));
		setVerticalAlignment(TOP);
		setHorizontalAlignment(CENTER);
		updateBorder(isSelected);
		return this;
	} //}}}

	private String tableWrap(String source, int width) //{{{
	{
		StringBuffer table = new StringBuffer("<table border=0 width=");
		table.append(width+"");
		table.append("><tr><td>");
		table.append(source);
		table.append("</td></tr></table>");
		return table.toString();
	} //}}}

	private String tableWrap(Status stat, int width) {//{{{
		User u = stat.getUser();
		Place p = stat.getPlace();
		Date d = stat.getCreatedAt();

		String filename = resourceMap.getResourcesDir() + resourceMap.getString(stat.isRetweet() ? "icon.arrow_rotate_clockwise" : "icon.arrow_rotate_clockwise_off" );
		//logger.debug(filename);
		URL retweet = resourceMap.getClassLoader().getResource(filename);

		filename = resourceMap.getResourcesDir() + resourceMap.getString(stat.isFavorited() ? "icon.heart" : "icon.heart_off");
		URL fav = resourceMap.getClassLoader().getResource(filename);

		filename = resourceMap.getResourcesDir() + resourceMap.getString("icon.comment_edit");
		URL action = resourceMap.getClassLoader().getResource(filename);

		StringBuffer table = new StringBuffer("<table border=0 width=");
		table.append(width+"");
		table.append("><tr><td>");
		table.append(pretify(stat, stat.getText()));
		table.append("</td></tr></table>");
		StringBuffer tb = new StringBuffer("<table border=0 width=");
		tb.append(width+"");
		tb.append("><tr><td align='left'>");
		tb.append(TimeSpanUtil.toTimeSpanString(d));
		tb.append("</td><td align='right' width='16'>");
		tb.append("<img border=0 src='"+retweet.toString()+"'>");
		tb.append("</td><td align='right' width='16'>");
		tb.append("<img border=0 src='"+fav.toString()+"'>");
		tb.append("</td><td align='right' width='16'>");
		tb.append("<img border=0 src='"+action.toString()+"'>");
		tb.append("</td></tr></table>");

		table.append(tb);
		return table.toString();
	}//}}}

	private String pretify(Status stat, String text)//{{{
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

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public Status getStatus()
	{
		return this.status;
	}

	@Override
	public String getToolTipText(java.awt.event.MouseEvent e) //{{{
	{
		if(isActionSpot(e))
		{
			source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			return resourceMap.getString(spotTip); 
		}
		source.setCursor(Cursor.getDefaultCursor());
		return super.getToolTipText();
	}//}}}

	private boolean isActionSpot(MouseEvent e)//{{{
	{
		boolean rv = false;
		final java.awt.Point loc = e.getPoint();
		if(source != null)
		{
			Vector<ListHotSpot> hotzone = source.getHotSpot();
			for(ListHotSpot spot : hotzone)
			{
				if(spot.isActionSpot(e, selection, source))
				{
					spotTip = "tooltip."+spot.getName();
					rv = true;
					break;
				}
			}
		}
		return rv;
	}//}}}
}
