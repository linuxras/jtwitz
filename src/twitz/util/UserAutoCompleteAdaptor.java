/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.util;

import java.util.Vector;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.autocomplete.AbstractAutoCompleteAdaptor;
import twitter4j.User;

/**
 *
 * @author mistik1
 */
public class UserAutoCompleteAdaptor extends AbstractAutoCompleteAdaptor {

	private JTextComponent textfield;
	private Vector<User> items;
	private User selectedUser;
	
	/**
	 * Default Constructor creates a new UserAutoCompleteAdaptor
	 * @param component an <code>javax.swing.text.JTextComponent</code> to be designed by this adaptor
	 * @param items a <code>java.util.List</code> containing the <code>twitter4j.User</code> objects
	 */
	public UserAutoCompleteAdaptor(JTextComponent component, Vector<User> items)
	{
		this.textfield = component;
		this.items = items;
	}

	/**
	 * Gets the currently selected user from the list
	 * @return a <code>twitter4j.User</code>
	 */
	@Override
	public User getSelectedItem()
	{
		return selectedUser;
	}

	/**
	 * Sets the selected User object
	 * @param arg0 a <code>twitter4j.User</code> object
	 * @throws <code>java.lang.IllegalArgumentException</code> is arg0 is not a twitter4j.User
	 */
	@Override
	public void setSelectedItem(Object arg0)
	{
		if(!(arg0 instanceof User))
			throw new IllegalArgumentException("arg0 must be a twitter4j.User");
		selectedUser = (User)arg0;
	}

	@Override
	public int getItemCount()
	{
		return items.size();
	}

	@Override
	public User getItem(int arg0)
	{
		return items.get(arg0);
	}

	@Override
	public JTextComponent getTextComponent()
	{
		return textfield;
	}

}
