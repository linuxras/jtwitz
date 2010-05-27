/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz;

import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionListener;

/**
 *
 * @author mistik1
 */
public class TwitzPopup extends PopupMenu {

	private ActionListener listener;
	private Font smallFont = null;
	private int sf = 10; //Small font point size

	public TwitzPopup(ActionListener l) {
		super("Twitz tweets");
		listener = l;
	}

	//Override addNotify so we can do things like make a default font size
	@Override
	public void addNotify() {
		super.addNotify();
		Font cFont = getFont();
		smallFont = new Font(cFont.getName(), cFont.getStyle(), sf);
		setFont(smallFont);
	}

	public void updateEnteries(MenuItem[] items) {

        removeAll();

        for (MenuItem mi: items) {
            if (mi == null)
                break;
            add(mi);
            mi.setFont(smallFont);
            mi.addActionListener(listener);
        }

        setLabel("New Tweet on: " + (new java.util.Date()).toString());
    }

}
