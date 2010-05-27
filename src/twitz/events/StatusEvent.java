/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.events;

/**
 *
 * @author mistik1
 */
public class StatusEvent extends java.util.EventObject{

	twitter4j.Status stat = null;
	long timestamp = -1;

	public StatusEvent(Object source, twitter4j.Status status, long ts) {
		super(source);
		this.stat = status;
		this.timestamp = ts;
	}

	public twitter4j.Status getStatus() {
		return this.stat;
	}

	public long getTimeStamp() {
		return this.timestamp;
	}
}
