/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.events;

/**
 *
 * @author mistik1
 */
public interface TwitzEventModel {

	public void addTwitzListener(TwitzListener o);;
	public void removeTwitzListener(TwitzListener o);

}
