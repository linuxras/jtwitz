/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.models;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import twitter4j.User;
import twitz.testing.UserTest;
import javax.swing.JList;

/**
 * This class extends DefaultListModel because I wanted to make
 * sure that only <em>twitter4j.User</em> were used in the contacts list
 *
 * @author Andrew Williams
 */
public class ContactsListModel extends DefaultListModel {

	private Vector<User> users = new Vector<User>();

	public ContactsListModel() {}

	@Override
	public int getSize() {
		return users.size();
    }

	@Override
    public User getElementAt(int index) {
		return users.elementAt(index);
    }

    public void copyInto(User anArray[]) {
		users.copyInto(anArray);
    }

	@Override
    public void trimToSize() {
		users.trimToSize();
    }

	@Override
    public void ensureCapacity(int minCapacity) {
		users.ensureCapacity(minCapacity);
    }

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

	@Override
    public int capacity() {
		return users.capacity();
    }

	@Override
    public int size() {
		return users.size();
    }

	@Override
    public boolean isEmpty() {
		return users.isEmpty();
    }

	@Override
    public Enumeration<User> elements() {
		return users.elements();
    }

    public boolean contains(User elem) {
		return users.contains(elem);
    }

    public int indexOf(User elem) {
		return users.indexOf(elem);
    }

    public int indexOf(User elem, int index) {
		return users.indexOf(elem, index);
	}

    public int lastIndexOf(User elem) {
		return users.lastIndexOf(elem);
    }

    public int lastIndexOf(User elem, int index) {
		return users.lastIndexOf(elem, index);
    }

	@Override
    public User elementAt(int index) {
		return users.elementAt(index);
    }

	@Override
    public Object firstElement() {
		return users.firstElement();
    }

	@Override
    public User lastElement() {
		return users.lastElement();
    }

    public void setElementAt(User obj, int index) {
		users.setElementAt(obj, index);
		fireContentsChanged(this, index, index);
    }

	@Override
    public void removeElementAt(int index) {
		users.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
    }

    public void insertElementAt(User obj, int index) {
		users.insertElementAt(obj, index);
		fireIntervalAdded(this, index, index);
    }

    public void addElement(User obj) {
		int index = users.size();
		users.addElement(obj);
		fireIntervalAdded(this, index, index);
    }

    public boolean removeElement(User obj) {
		int index = indexOf(obj);
		boolean rv = users.removeElement(obj);
		if (index >= 0) {
		    fireIntervalRemoved(this, index, index);
		}
		return rv;
    }


	@Override
    public void removeAllElements() {
		int index1 = users.size()-1;
		users.removeAllElements();
		if (index1 >= 0) {
			fireIntervalRemoved(this, 0, index1);
		}
    }


	@Override
	public String toString() {
		return users.toString();
    }


	@Override
    public User[] toArray() {
		User[] rv = new User[users.size()];
		users.copyInto(rv);
		return rv;
    }

	@Override
    public User get(int index) {
		return users.get(index);
    }

    public User set(int index, User element) {
		User rv = users.elementAt(index);
		users.setElementAt(element, index);
		fireContentsChanged(this, index, index);
		return rv;
    }

    public void add(int index, User element) {
		users.insertElementAt(element, index);
		fireIntervalAdded(this, index, index);
    }

	@Override
    public User remove(int index) {
		User rv = users.elementAt(index);
		users.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
		return rv;
    }

	@Override
    public void clear() {
		int index = users.size()-1;
		users.clear();
		if (index >= 0) {
		    fireIntervalRemoved(this, 0, index);
		}
    }

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
