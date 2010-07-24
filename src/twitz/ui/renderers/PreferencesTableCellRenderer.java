/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.renderers;

import java.awt.Component;
import javax.swing.JTable;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;
import twitz.util.SettingsManager;
import twitz.util.TwitzSessionManager;

/**
 *
 * @author mistik1
 */
public class PreferencesTableCellRenderer extends SubstanceDefaultTableCellRenderer {

	SettingsManager config = TwitzSessionManager.getInstance().getSettingsManagerForSession("Default");
	
	public PreferencesTableCellRenderer() {}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, 
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component rv = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		//int col = table.convertColumnIndexToModel(column);
		//int realrow = table.convertRowIndexToModel(row);
		if(column == 2) {
			String key = (String) table.getValueAt(row, 0);
			if(config.getString(key+".cfgtype").equalsIgnoreCase("Password"))
			{
				StringBuilder b = new StringBuilder();
				char[] pc = ((String)value).toCharArray();
				for(int i=0;i<pc.length; i++)
					b.append("***");
				setText(b.toString());
			}
		}
		return rv;
	}
}
