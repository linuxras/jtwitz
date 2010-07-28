/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.events;

import java.util.Vector;

/**
 *
 * @author Andrew Williams
 */
public class DefaultTwitzEventModel implements TwitzEventModel {

	private final Vector<TwitzListener> listeners = new Vector<TwitzListener>();

	public void addTwitzListener(TwitzListener o)
	{
		listeners.add(o);
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void removeTwitzListener(TwitzListener o)
	{
		listeners.remove(o);
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void fireTwitzEvent(TwitzEvent e) {
		for(TwitzListener l : listeners) {
			l.eventOccurred(e);
		}
	}

}
