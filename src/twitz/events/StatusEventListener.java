/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.events;

/**
 *
 * @author mistik1
 */
public interface StatusEventListener extends java.util.EventListener{

	public	void statusUpdated(StatusEvent s);

}
