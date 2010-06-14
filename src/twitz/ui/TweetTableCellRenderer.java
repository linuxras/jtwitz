/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.application.ResourceMap;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

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
		if(value != null)
			setToolTipText(value.toString());
		//System.out.println("Value in table cell is: "+value);
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

	protected void setValue(Object value) {
		setText((value == null) ? "" : value.toString());
    }
}
