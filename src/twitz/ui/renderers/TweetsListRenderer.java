/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.renderers;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author mistik1
 */
public class TweetsListRenderer extends JLabel implements ListCellRenderer {

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		String str = value.toString();
		String[] s = str.split(":", 2);
		String user = "<p><b>"+s[1]+"</p></b>";
		setText(user+": "+s[2]);
		return this;
		//throw new UnsupportedOperationException("Not supported yet.");
	}

}
