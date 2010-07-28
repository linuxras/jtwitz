/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.models;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.AbstractListModel;
import twitter4j.Tweet;

/**
 *
 * @author Andrew Williams
 */
public class TweetListModel extends AbstractListModel {

	Vector<Tweet> trends = new Vector<Tweet>();

	public TweetListModel() {

	}

	public int getSize()
	{
		return trends.size();
	}

	public Tweet getElementAt(int index)
	{
		return trends.elementAt(index);
	}

	public void addTweet(Tweet l) {
		int index = getSize();
		trends.addElement(l);
		fireIntervalAdded(this, index, index);
	}

	public void insertTweet(Tweet l, int index)
	{
		if(index < getSize())
		{
			trends.insertElementAt(l, index);
			fireIntervalAdded(this, index, index);
		}
	}
	
	public void removeTweet(Tweet l)
	{
		int index = trends.indexOf(l);
		if(index != -1)
		{
			removeTweet(index);
		}
	}

	public void removeTweet(int index)
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

	public int indexOf(Tweet l)
	{
		return trends.indexOf(l);
	}

	public Tweet firstElement()
	{
		return trends.firstElement();
	}

	public Tweet lastElement()
	{
		return trends.lastElement();
	}

	public Vector<Tweet> getDataVector()
	{
		return trends;
	}

	public void setDataVector(Vector<Tweet> data)
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

	public Enumeration<Tweet> elements()
	{
		return trends.elements();
	}

	@Override
	public String toString()
	{
		return trends.toString();
	}

	public Tweet[] toArray()
	{
		Tweet[] rv = new Tweet[getSize()];
		trends.copyInto(rv);
		return rv;
	}
}
