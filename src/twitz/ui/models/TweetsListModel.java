/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.models;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.AbstractListModel;
import twitter4j.Status;

/**
 *
 * @author mistik1
 */
public class TweetsListModel extends AbstractListModel {

	Vector<Status> status = new Vector<Status>();

	public TweetsListModel() {

	}

	public int getSize()
	{
		return status.size();
	}

	public Status getElementAt(int index)
	{
		return status.elementAt(index);
	}

	public void addStatus(Status l) {
		int index = getSize();
		status.addElement(l);
		fireIntervalAdded(this, index, index);
	}

	public void insertStatus(Status l, int index)
	{
		if(index < getSize())
		{
			status.insertElementAt(l, index);
			fireIntervalAdded(this, index, index);
		}
	}
	
	public void removeStatus(Status l)
	{
		int index = status.indexOf(l);
		if(index != -1)
		{
			removeStatus(index);
		}
	}

	public void removeStatus(int index)
	{
		if(index < getSize()) {
			status.removeElementAt(index);
			fireIntervalRemoved(this, index, index);
		}

	}

	public void clear()
	{
		int index = status.size();
		status.clear();
		if(index > 0)
			fireIntervalRemoved(this, 0, index-1);
	}

	public int indexOf(Status l)
	{
		return status.indexOf(l);
	}

	public Status firstElement()
	{
		return status.firstElement();
	}

	public Status lastElement()
	{
		return status.lastElement();
	}

	public Vector<Status> getDataVector()
	{
		return status;
	}

	public void setDataVector(Vector<Status> data)
	{
		int index = getSize();
		if(data != null)
		{
			fireIntervalRemoved(this, 0, index-1);
			this.status = data;
			fireIntervalAdded(this, 0, getSize()-1);
		}
	}

	public int size()
	{
		return status.size();
	}
	
	public boolean isEmpty()
	{
		return status.isEmpty();
	}

	public Enumeration<Status> elements()
	{
		return status.elements();
	}

	@Override
	public String toString()
	{
		return status.toString();
	}

	public Status[] toArray()
	{
		Status[] rv = new Status[getSize()];
		status.copyInto(rv);
		return rv;
	}
}
