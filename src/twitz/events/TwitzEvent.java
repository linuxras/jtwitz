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
	public static final int LOGIN = 1;
	public static final int UPDATE = 2;
	public static final int ADD_FRIEND = 3;
	public static final int MSG_SENT = 4;
	public static final int MSG_RECIEVED = 5;
	public static final int DELETE_FRIEND = 6;
	public static final int ADD_BLOCK = 7;
	public static final int REMOVE_BLOCK = 8;
	public static final int REPORT_SPAM = 9;
	//public static final int
	//public static enum TYPES{LOGIN, UPDATE, ADD_FRIEND, MSG_SENT, MSG_RECIEVED};

	/**
	 * TwitzEvent object all twitter related events in the Twitz Application should fire one of these
	 * @param source - The object that caused this event.
	 * @param t - The twitter4j.Twitter instance used when even was generated.
	 * @param e - Event Type #see Constants for support types .
	 * @param ts - Timestamp of the moment this event occurred.
	 */
	public TwitzEvent(Object source, twitter4j.Twitter t, int e, long ts) {
		super(source);
		this.stat = t;
		this.type = e;
		this.timestamp = ts;
	}

	/**
	 * Returns the twitter4j.Twitter object
	 * @return
	 */
	public twitter4j.Twitter getTwiter() {
		return this.stat;
	}

	/**
	 * Returns the timestamp of the event
	 * @return
	 */
	public long getTimeStamp() {
		return this.timestamp;
	}

	/**
	 * Returns the event type.
	 * @return
	 */
	public int getEventType() {
		return this.type;
	}
}
