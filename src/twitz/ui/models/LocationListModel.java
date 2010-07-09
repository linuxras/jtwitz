/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.models;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import twitter4j.Location;

/**
 *
 * @author mistik1
 */
public class LocationListModel extends AbstractListModel {

	Vector<Location> locations = new Vector<Location>();

	public LocationListModel() {

	}

	public int getSize()
	{
		return locations.size();
	}

	public Location getElementAt(int index)
	{
		return locations.elementAt(index);
	}

	public Location getLocationAt(int index)
	{
		return locations.elementAt(index);
	}

	public void addLocation(Location l) {
		int index = getSize();
		locations.addElement(l);
		fireIntervalAdded(this, index, index);
	}

	public void insertLocation(Location l, int index)
	{
		if(index < getSize())
		{
			locations.insertElementAt(l, index);
			fireIntervalAdded(this, index, index);
		}
	}
	
	public void removeLocation(Location l)
	{
		int index = locations.indexOf(l);
		if(index != -1)
		{
			removeLocation(index);
		}
	}

	public void removeLocation(int index)
	{
		if(index < getSize()) {
			locations.removeElementAt(index);
			fireIntervalRemoved(this, index, index);
		}

	}

	public void clear()
	{
		int index = locations.size();
		locations.clear();
		if(index > 0)
			fireIntervalRemoved(this, 0, index-1);
	}

	public int indexOf(Location l)
	{
		return locations.indexOf(l);
	}

	public Location firstElement()
	{
		return locations.firstElement();
	}

	public Location lastElement()
	{
		return locations.lastElement();
	}

	public Vector<Location> getDataVector()
	{
		return locations;
	}

	public void setDataVector(Vector<Location> data)
	{
		int index = getSize();
		if(data != null)
		{
			fireIntervalRemoved(this, 0, index-1);
			this.locations = data;
			fireIntervalAdded(this, 0, getSize()-1);
		}
	}

	public int size()
	{
		return locations.size();
	}
	
	public boolean isEmpty()
	{
		return locations.isEmpty();
	}

	public Enumeration<Location> elements()
	{
		return locations.elements();
	}

	@Override
	public String toString()
	{
		return locations.toString();
	}

	public Location[] toArray()
	{
		Location[] rv = new Location[getSize()];
		locations.copyInto(rv);
		return rv;
	}
}
