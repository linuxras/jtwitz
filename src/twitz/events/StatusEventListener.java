/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.events;

/**
 *
 * @author Andrew Williams
 */
public interface StatusEventListener extends java.util.EventListener{

	public	void statusUpdated(StatusEvent s);

}
