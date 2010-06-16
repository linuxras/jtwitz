/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.renderers;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.util.Date;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.application.ResourceMap;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.Place;

/**
 *
 * @author mistik1
 */
public class TweetTableCellRenderer extends SubstanceDefaultTableCellRenderer{

	private ResourceMap resource;

	/**
	 * Default constructor
	 */
	public TweetTableCellRenderer() {
		super();
		//setOpaque(true);
		setName("Table.cellRenderer");
		//setContentType("text/html");
		resource = twitz.TwitzApp.getContext().getResourceMap(TweetTableCellRenderer.class);
		this.setVerticalTextPosition(TOP);
		this.setVerticalAlignment(TOP);
		//setSize(this.getWidth(), 50);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		//if(value != null)
		//	setToolTipText(value.toString());
		//System.out.println("Value in table cell is: "+value);
		Status s = (Status)value;
		User u = s.getUser();
		Place p = s.getPlace();
		Date d = s.getCreatedAt();
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		//
		//setFont(new Font("SansSerif", Font.PLAIN, 10));
		setFont(new Font("Arial", Font.BOLD, 10)); //TODO: put this in the resourceMap so its not hard coded
		URL imgURI = u.getProfileImageURL();
		ImageIcon icon = new ImageIcon(imgURI);
		Image img = icon.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);
		//getScaledInstance(int width, int height, int hints)
		setVerticalAlignment(TOP);
		
		StringBuffer buf = new StringBuffer("<p>");
		if(!s.getInReplyToScreenName().equals("")) {
			buf.append("<strong>@");
			buf.append(s.getInReplyToScreenName());
			buf.append(" - </strong>");
		}
		buf.append(s.getText());
		buf.append("<hr><center><strong>");
		buf.append(u.getScreenName()+"</strong><em> at ");
		buf.append(d.toString());
		buf.append("<br> from "+p.getName()+"</em></center></p>");

		setIcon(icon);
		setToolTipText("<html>"+processToolTip(buf.toString()));
		setText("<html>"+buf.toString());
		setSize(300,300);

		return this;
	}

	private String processToolTip(String source) {
		StringBuffer table = new StringBuffer("<table border=0 width=300><tr><td>");
		table.append(source);
		table.append("</td></tr></table>");
		return table.toString();
	}
//	protected void setValue(Object value) {
//		setText((value == null) ? "" : value.toString());
//    }
}
