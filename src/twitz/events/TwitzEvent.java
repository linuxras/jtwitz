/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.events;

import java.util.Map;

/**
 *
 * @author Andrew Williams
 */
public class TwitzEvent extends java.util.EventObject{

	private long timestamp = -1;
	private int type = -1;
	private TwitzEventType etype;
	private Map storage;

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
	public static final int UPDATE_FRIENDS_TWEETS_LIST = 10;

	public static enum EventType {
		LOGIN,
		UPDATE,
		ADD_FRIEND,
		MSG_SENT,
		MSG_RECIEVED,
		DELETE_FRIEND,
		ADD_BLOCK,
		REMOVE_BLOCK,
		REPORT_SPAM,
		UPDATE_FRIENDS_TWEETS_LIST
	};
	//public static final int
	//public static enum TYPES{LOGIN, UPDATE, ADD_FRIEND, MSG_SENT, MSG_RECIEVED};

	/**
	 * TwitzEvent object all twitter related events in the Twitz Application should fire one of these
	 * @param source - The object that caused this event.
	 * @param t - The twitter4j.Twitter instance used when even was generated.
	 * @param e - Event Type .
	 * @param ts - Timestamp of the moment this event occurred.
	 * @param orders A Map or array of Maps containing caller information and other info of what to do.
	 */
	public TwitzEvent(Object source, TwitzEventType e, long ts, Map... orders) {
		super(source);
		this.etype = e;
		this.timestamp = ts;
		if(orders.length > 0)
			this.storage = orders[0];
	}

	/**
	 *
	 * @return the timestamp of the event
	 */
	public long getTimeStamp() {
		return this.timestamp;
	}

	/**
	 * @return the event type Enum.
	 */
	public TwitzEventType getEventType() {
		return this.etype;
	}

	/**
	 * @return the event Map or null if none was given by the event creator.
	 */
	public Map getEventMap() {
		return storage;
	}
}
