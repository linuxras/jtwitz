/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.util;

import java.awt.Rectangle;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.autocomplete.AbstractAutoCompleteAdaptor;
import twitter4j.User;

/**
 *
 * @author Andrew Williams
 */
public class UserComboBoxAdaptor extends AbstractAutoCompleteAdaptor {

	private JComboBox combo;
	private Vector<User> items;
	private User selectedUser;
	
	/**
	 * Default Constructor creates a new UserAutoCompleteAdaptor
	 * @param component an <code>javax.swing.text.JTextComponent</code> to be designed by this adaptor
	 * @param items a <code>java.util.List</code> containing the <code>twitter4j.User</code> objects
	 */
	public UserComboBoxAdaptor(JComboBox component, Vector<User> items)
	{
		this.combo = component;
		this.items = items;
		loadComboBox();
	}

	private void loadComboBox()
	{
		combo.removeAllItems();
		for(User u : items)
		{
			combo.addItem(u);
		}
	}

	/**
	 * Gets the currently selected user from the list
	 * @return a <code>twitter4j.User</code>
	 */
	@Override
	public User getSelectedItem()
	{
		return (User)combo.getSelectedItem();
	}

	/**
	 * Sets the selected User object
	 * @param arg0 a <code>twitter4j.User</code> object
	 * @throws <code>java.lang.IllegalArgumentException</code> is arg0 is not a twitter4j.User
	 */
	@Override
	public void setSelectedItem(Object arg0)
	{
		if(arg0 == getSelectedItem())
			return;
		//the Following is borrowed from ComboBoxAdapter
		Accessible a = combo.getUI().getAccessibleChild(combo, 0);

        if (getItemCount() > 0 && a instanceof ComboPopup)
		{
            JList list = ((ComboPopup) a).getList();
            int lastIndex = list.getModel().getSize() - 1;

            Rectangle rect = list.getCellBounds(lastIndex, lastIndex);

            if (rect == null) {
                throw new IllegalStateException(
                        "attempting to access index " + lastIndex + " for " + combo);
            }

            list.scrollRectToVisible(rect);
        }

        //setting the selected item should scroll it into the visible region
        combo.setSelectedItem(arg0);
	}

	@Override
	public int getItemCount()
	{
		return combo.getItemCount();
	}

	@Override
	public User getItem(int arg0)
	{
		return (User)combo.getItemAt(arg0);
	}

	@Override
	public JTextComponent getTextComponent()
	{
		return (JTextComponent)combo.getEditor().getEditorComponent();
	}

}
