/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.events;

/**
 *
 * @author Andrew Williams
 */
public interface TwitzListener extends java.util.EventListener{

	public	void eventOccurred(TwitzEvent t);

}
