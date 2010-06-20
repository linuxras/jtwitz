/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.models;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.DefaultListModel;
import twitter4j.User;
import twitz.testing.UserTest;
import javax.swing.JList;

/**
 * This class extends DefaultListModel because I wanted to make
 * sure that only <em>twitter4j.User</em> were used in the contacts list
 *
 * @author mistik1
 */
public class ContactsListModel extends DefaultListModel {

	private Vector<User> users = new Vector<User>();

	public ContactsListModel() {}

	/**
     * {@inheritDoc}
     */
	@Override
	public int getSize() {
		return users.size();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public User getElementAt(int index) {
		return users.elementAt(index);
    }

    /**
     * {@inheritDoc}
     */
    public void copyInto(User anArray[]) {
		users.copyInto(anArray);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public void trimToSize() {
		users.trimToSize();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public void ensureCapacity(int minCapacity) {
		users.ensureCapacity(minCapacity);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public void setSize(int newSize) {
		int oldSize = users.size();
		users.setSize(newSize);
		if (oldSize > newSize) {
		    fireIntervalRemoved(this, newSize, oldSize-1);
		}
		else if (oldSize < newSize) {
		    fireIntervalAdded(this, oldSize, newSize-1);
		}
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public int capacity() {
		return users.capacity();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public int size() {
		return users.size();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public boolean isEmpty() {
		return users.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public Enumeration<User> elements() {
		return users.elements();
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(User elem) {
		return users.contains(elem);
    }

    /**
     * {@inheritDoc}
     */
    public int indexOf(User elem) {
		return users.indexOf(elem);
    }

    /**
     * {@inheritDoc}
     */
    public int indexOf(User elem, int index) {
		return users.indexOf(elem, index);
	}

    /**
     * {@inheritDoc}
     */
    public int lastIndexOf(User elem) {
		return users.lastIndexOf(elem);
    }

    /**
     * {@inheritDoc}
     */
    public int lastIndexOf(User elem, int index) {
		return users.lastIndexOf(elem, index);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public User elementAt(int index) {
		return users.elementAt(index);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public Object firstElement() {
		return users.firstElement();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public User lastElement() {
		return users.lastElement();
    }

    /**
     * {@inheritDoc}
     */
    public void setElementAt(User obj, int index) {
		users.setElementAt(obj, index);
		fireContentsChanged(this, index, index);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public void removeElementAt(int index) {
		users.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
    }

    /**
     * {@inheritDoc}
     */
    public void insertElementAt(User obj, int index) {
		users.insertElementAt(obj, index);
		fireIntervalAdded(this, index, index);
    }

    /**
     * {@inheritDoc}
     */
    public void addElement(User obj) {
		int index = users.size();
		users.addElement(obj);
		fireIntervalAdded(this, index, index);
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeElement(User obj) {
		int index = indexOf(obj);
		boolean rv = users.removeElement(obj);
		if (index >= 0) {
		    fireIntervalRemoved(this, index, index);
		}
		return rv;
    }


    /**
     * {@inheritDoc}
     */
	@Override
    public void removeAllElements() {
		int index1 = users.size()-1;
		users.removeAllElements();
		if (index1 >= 0) {
			fireIntervalRemoved(this, 0, index1);
		}
    }


    /**
     * {@inheritDoc}
     */
	@Override
	public String toString() {
		return users.toString();
    }


    /**
     * {@inheritDoc}
     */
	@Override
    public User[] toArray() {
		User[] rv = new User[users.size()];
		users.copyInto(rv);
		return rv;
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public User get(int index) {
		return users.get(index);
    }

    /**
     * {@inheritDoc}
     */
    public User set(int index, User element) {
		User rv = users.elementAt(index);
		users.setElementAt(element, index);
		fireContentsChanged(this, index, index);
		return rv;
    }

    /**
     * {@inheritDoc}
     */
    public void add(int index, User element) {
		users.insertElementAt(element, index);
		fireIntervalAdded(this, index, index);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public User remove(int index) {
		User rv = users.elementAt(index);
		users.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
		return rv;
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public void clear() {
		int index = users.size()-1;
		users.clear();
		if (index >= 0) {
		    fireIntervalRemoved(this, 0, index);
		}
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public void removeRange(int fromIndex, int toIndex) {
		if (fromIndex > toIndex) {
			throw new IllegalArgumentException("fromIndex must be <= toIndex");
		}
		for(int i = toIndex; i >= fromIndex; i--) {
			users.removeElementAt(i);
		}
		fireIntervalRemoved(this, fromIndex, toIndex);
    }
}
