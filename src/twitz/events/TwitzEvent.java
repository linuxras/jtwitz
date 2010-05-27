/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.events;

/**
 *
 * @author mistik1
 */
public class TwitzEvent extends java.util.EventObject{

	private twitter4j.Twitter stat = null;
	private long timestamp = -1;
	private int type = -1;

	//Constants to define change type
	public static final int TWITZ_LOGIN = 1;
	public static final int TWITZ_UPDATE = 2;
	public static final int TWITZ_ADD_FRIEND = 3;

	public TwitzEvent(Object source, twitter4j.Twitter t, int e, long ts) {
		super(source);
		this.stat = t;
		this.type = e;
		this.timestamp = ts;
	}

	public twitter4j.Twitter getTwiter() {
		return this.stat;
	}

	public long getTimeStamp() {
		return this.timestamp;
	}

	public int getEventType() {
		return this.type;
	}
}
