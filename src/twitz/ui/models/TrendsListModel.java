/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.models;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.AbstractListModel;
import twitter4j.Trend;

/**
 *
 * @author mistik1
 */
public class TrendsListModel extends AbstractListModel {

	Vector<Trend> trends = new Vector<Trend>();

	public TrendsListModel() {

	}

	public int getSize()
	{
		return trends.size();
	}

	public Trend getElementAt(int index)
	{
		return trends.elementAt(index);
	}

	public void addTrend(Trend l) {
		int index = getSize();
		trends.addElement(l);
		fireIntervalAdded(this, index, index);
	}

	public void insertTrend(Trend l, int index)
	{
		if(index < getSize())
		{
			trends.insertElementAt(l, index);
			fireIntervalAdded(this, index, index);
		}
	}
	
	public void removeTrend(Trend l)
	{
		int index = trends.indexOf(l);
		if(index != -1)
		{
			removeTrend(index);
		}
	}

	public void removeTrend(int index)
	{
		if(index < getSize()) {
			trends.removeElementAt(index);
			fireIntervalRemoved(this, index, index);
		}

	}

	public void clear()
	{
		int index = trends.size();
		trends.clear();
		if(index > 0)
			fireIntervalRemoved(this, 0, index-1);
	}

	public int indexOf(Trend l)
	{
		return trends.indexOf(l);
	}

	public Trend firstElement()
	{
		return trends.firstElement();
	}

	public Trend lastElement()
	{
		return trends.lastElement();
	}

	public Vector<Trend> getDataVector()
	{
		return trends;
	}

	public void setDataVector(Vector<Trend> data)
	{
		int index = getSize();
		if(data != null)
		{
			fireIntervalRemoved(this, 0, index-1);
			this.trends = data;
			fireIntervalAdded(this, 0, getSize()-1);
		}
	}

	public int size()
	{
		return trends.size();
	}
	
	public boolean isEmpty()
	{
		return trends.isEmpty();
	}

	public Enumeration<Trend> elements()
	{
		return trends.elements();
	}

	@Override
	public String toString()
	{
		return trends.toString();
	}

	public Trend[] toArray()
	{
		Trend[] rv = new Trend[getSize()];
		trends.copyInto(rv);
		return rv;
	}
}
