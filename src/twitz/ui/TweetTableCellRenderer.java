/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import sun.swing.DefaultLookup;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author mistik1
 */
public class TweetTableCellRenderer extends JLabel implements TableCellRenderer {

	private static final Border NO_FOCUS_BORDER = new EmptyBorder(1,1,1,1);
	protected static Border defaultBorder = NO_FOCUS_BORDER;

	private Color foregroundColor;
	private Color backgroundColor;

	private ResourceMap resource;

	/**
	 * Default constructor
	 */
	public TweetTableCellRenderer() {
		super();
		setOpaque(true);
		setBorder(getNonFocusBorder());
		setName("Table.cellRenderer");
		//setContentType("text/html");
		resource = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(TweetTableCellRenderer.class);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Color fg = null;
		Color bg = null;
		Color alternateColor = resource.getColor("Table.alternateRowColor");
		//System.out.println(alternateColor);

		JTable.DropLocation dropLocation = table.getDropLocation();
		if (dropLocation != null && !dropLocation.isInsertRow() && !dropLocation.isInsertColumn() && dropLocation.getRow() == row && dropLocation.getColumn() == column)
		{

			fg = resource.getColor("Table.dropCellForeground");
			bg = resource.getColor("Table.dropCellBackground");

			isSelected = true;
		}

//		if (isSelected)
//		{
//			super.setForeground(fg == null ? table.getSelectionForeground() : fg);
//			super.setBackground(bg == null ? table.getSelectionBackground() : bg);
//		}
//		else
//		{
			Color background = backgroundColor != null ? backgroundColor : resource.getColor("Table.backgroundColor");
//			if (background == null || background instanceof javax.swing.plaf.UIResource)
//			{
				//System.out.println("Row: "+row+" Math: "+row % 2);
				if (alternateColor != null && row % 2 == 0)
				{
				//	System.out.println("this is alternate row");
					background = alternateColor;
				}
//			}
			super.setForeground(foregroundColor != null ? foregroundColor : resource.getColor("Table.foregroundColor"));
			super.setBackground(background);
//		}

		setFont(table.getFont());

		if (hasFocus)
		{
			Border border = null;
			if (isSelected)
			{
				border = DefaultLookup.getBorder(this, ui, "Table.focusSelectedCellHighlightBorder");
			}
			if (border == null)
			{
				border = DefaultLookup.getBorder(this, ui, "Table.focusCellHighlightBorder");
			}
			setBorder(border);

			if (!isSelected && table.isCellEditable(row, column))
			{
				Color col;
				col = resource.getColor("Table.focusCellForeground");
				if (col != null)
				{
					super.setForeground(col);
				}
				col = resource.getColor("Table.focusCellBackground");
				if (col != null)
				{
					super.setBackground(col);
				}
			}
		}
		else
		{
			setBorder(getNonFocusBorder());
		}

		setValue(value);

		return this;
	}

	private Border getNonFocusBorder() {
		Border b = DefaultLookup.getBorder(this, ui, "Table.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            if (b != null)
				return b;
            return NO_FOCUS_BORDER;
        } else if (b != null) {
            if (defaultBorder == null || defaultBorder == NO_FOCUS_BORDER) {
                return b;
            }
        }
		return defaultBorder;
	}

	public void setForeground(Color c) {
		super.setForeground(c);
		foregroundColor = c;
	}

	public void setBackground(Color c) {
		super.setBackground(c);
		backgroundColor = c;
	}
	
	public void updateUI() {
		super.updateUI();
		setForeground(null);
		setBackground(null);
	}

	/*
     * The following methods are overridden as a performance measure as recommended
	 * by DefaultTableCellRenderer.
     */
	
	public boolean isOpaque() { 
		Color b = getBackground();
		Component p = getParent(); 
		if (p != null) { 
			p = p.getParent(); 
		}
         
		boolean match = (b != null) && (p != null) && b.equals(p.getBackground()) && p.isOpaque();
		return !match && super.isOpaque(); 
    }

	public void invalidate() {}

	public void revalidate() {}

	public void repaint(long tm, int x, int y, int width, int height) {}

	public void repaint(Rectangle r) {}

	public void repaint() {}

	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) { }

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		if (propertyName.equals("text") || propertyName.equals("labelFor") || propertyName.equals("displayedMnemonic") || ((propertyName.equals("font") || propertyName.equals("foreground")) && oldValue != newValue && getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey) != null))
		{
			super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

	protected void setValue(Object value) {
		setText((value == null) ? "" : value.toString());
    }
}
