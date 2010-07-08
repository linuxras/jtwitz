package twitz.events;

import java.awt.Component;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import javax.swing.SwingWorker;
import twitter4j.*;
import org.apache.log4j.Logger;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventType;
import twitz.twitter.TwitterManager;


public class TwitzEventHandler extends SwingWorker<String, Object> {

	private TwitzEvent mainEvent;
	private TwitterManager tm;
	private TwitterMethod errorMethod;
	private TwitterException errorT;
	private boolean error = false;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private boolean logdebug = logger.isDebugEnabled();
	private Vector<String> names = new Vector<String>();

	public TwitzEventHandler(TwitzEvent event, TwitterManager tmanager)
	{
		this.mainEvent = event;
		this.tm = tmanager;
	}

	public String doInBackground() //{{{
	{
		String rv = "";
		//int type = t.getEventType();
		String screenName = null;
		Component caller = null;
		Map eventMap = null;
		boolean async = true;
		ArrayList args = null;

		if(mainEvent.getEventMap() != null) {
			eventMap = mainEvent.getEventMap();
			if(eventMap.containsKey("caller"))
				caller = (Component) eventMap.get("caller");
			if(eventMap.containsKey("async"))
				async = (Boolean)eventMap.get("async");
			if(eventMap.containsKey("arguments"))
				args = (ArrayList)eventMap.get("arguments");
		}
				//run action performed
		TwitzEventType type = mainEvent.getEventType();
		switch(type) {
			case UPDATE_FRIENDS_TWEETS_LIST:
				//TODO Replace this test code with more checks
				//StatusListModel mod = friendsTweets.getModel();
				//mod.clear();
				//for(int i=0; i<10; i++)
				//	mod.addStatus(new StatusTest());
				break;
			case SEARCH:
				logger.debug("Search run");
				if(args != null && args.size() >= 1) {
					Query query = (Query)args.get(0);
					tm.getAsyncTwitterInstance().search(query);
				}
				break;
			case TRENDS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "TRENDS: Not supported yet", "2"
						});
				break;
			case CURRENT_TRENDS:
				//
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "CURRENT_TRENDS: Not supported yet", "2"
						});
				break;
			case DAILY_TRENDS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "DAILY_TRENDS: Not supported yet", "2"
						});
				break;
			case WEEKLY_TRENDS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "WEEKLY_TRENDS: Not supported yet", "2"
						});
				break;
			case PUBLIC_TIMELINE:
				tm.getAsyncTwitterInstance().getPublicTimeline();
				break;
			case HOME_TIMELINE:
				tm.getAsyncTwitterInstance().getHomeTimeline();
				break;
			case FRIENDS_TIMELINE:
				tm.getAsyncTwitterInstance().getFriendsTimeline();
				break;
			case USER_TIMELINE:
				if(args != null) {
					String sn = (String)args.get(0);
					tm.getAsyncTwitterInstance().getUserTimeline(sn);
				}
				break;
			case MENTIONS:
				tm.getAsyncTwitterInstance().getMentions();
				break;
			case RETWEETED_BY_ME:
				tm.getAsyncTwitterInstance().getRetweetedByMe();
				break;
			case RETWEETED_TO_ME:
				tm.getAsyncTwitterInstance().getRetweetedToMe();
				break;
			case RETWEETS_OF_ME:
				tm.getAsyncTwitterInstance().getRetweetsOfMe();
				break;
			case SHOW_STATUS:
				//screenName = getScreenNameFromActiveTab();
				//tm.getAsyncTwitterInstance().setStatus(screenName);
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "SHOW_STATUS: Not supported yet", "2"
						});
				break;
			case UPDATE_STATUS:
				if(logdebug)
						logger.debug("Updating status");
				//String tweet = txtTweet.getText();
				//btnTweet.setEnabled(false);
				//txtTweet.setEnabled(false);
				if(args != null && args.size() == 1)
				{
					String tweet = (String)args.get(0);
					if(logdebug)
						logger.debug("Updating status");
					tm.getAsyncTwitterInstance().updateStatus(tweet);
				}
				break;
			case DESTROY_STATUS:
				if(args != null) {
					long st = (Long)args.get(0);
					tm.getAsyncTwitterInstance().destroyStatus(st);
				}
				break;
			case RETWEET_STATUS:
				if(args != null) {
					long st = (Long)args.get(0);
					tm.getAsyncTwitterInstance().retweetStatus(st);
				}
				break;
			case RETWEETS:
				if(args != null) {
					long st = (Long)args.get(0);
					tm.getAsyncTwitterInstance().getRetweets(st);
				}
				break;
			case SHOW_USER:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "SHOW_USER: Not supported yet", "2"
						});
				break;
			case LOOKUP_USERS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "LOOKUP_USERS: Not supported yet", "2"
						});
				break;
			case SEARCH_USERS:
				if(args != null && args.size() == 2)
				{
					String q = (String)args.get(0);
					int pg = (Integer)args.get(1);
					tm.getAsyncTwitterInstance().searchUsers(q, pg);
				}
				break;
			case SUGGESTED_USER_CATEGORIES:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "SUGGESTED_USER_CATEGORIES: Not supported yet", "2"
						});
				break;
			case USER_SUGGESTIONS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "USER_SUGGESTIONS: Not supported yet", "2"
						});
				break;
			case FRIENDS_STATUSES:
				tm.getAsyncTwitterInstance().getFriendsStatuses();
				break;
			case FOLLOWERS_STATUSES:
				tm.getAsyncTwitterInstance().getFollowersStatuses();
				break;
			case CREATE_USER_LIST:
				//createUserList(java.lang.String listName, boolean isPublicList, java.lang.String description) 
				if(args != null) {
					try
					{
						String ln = (String)args.get(0);
						boolean pub = (Boolean)args.get(1);
						String desc = (String)args.get(2);
						tm.getAsyncTwitterInstance().createUserList(ln, pub, desc);
					}
					catch(NullPointerException npe){
						logger.error(npe);
					}
				}
				break;
			case UPDATE_USER_LIST:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UPDATE_USER_LIST: Not supported yet", "2"
						});
				break;
			case USER_LISTS:
				if(args != null && args.size() != 0)
				{
					tm.getAsyncTwitterInstance().getUserLists((String)args.get(0), (Long)args.get(1));;
				}
				break;
			case SHOW_USER_LIST:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "SHOW_USER_LIST: Not supported yet", "2"
						});
				break;
			case DESTROY_USER_LIST:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "DESTROY_USER_LIST: Not supported yet", "2"
						});
				break;
			case USER_LIST_STATUSES:
				if(args != null && args.size() == 3)
				{
					tm.getAsyncTwitterInstance().getUserListStatuses((String)args.get(0),
							(Integer)args.get(1), (Paging)args.get(2));
				}
				break;
			case USER_LIST_MEMBERSHIPS:
				
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "USER_LIST_MEMBERSHIPS: Not supported yet", "2"
						});
				break;
			case USER_LIST_SUBSCRIPTIONS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "USER_LIST_SUBSCRIPTIONS: Not supported yet", "2"
						});
				break;
			case LIST_MEMBERS:
				if(args != null && args.size() == 3)
				{
					String owner = (String)args.get(0);
					int lid = (Integer)args.get(1);
					long page = (Long)args.get(2);
					//que.add(caller); done already on the other side
					//getUserListMembers(java.lang.String listOwnerScreenName, int listId, long cursor) 
					tm.getAsyncTwitterInstance().getUserListMembers(owner, lid, page);
				}
				break;
			case ADD_LIST_MEMBER:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "ADD_LIST_MEMBER: Not supported yet", "2"
						});
				break;
			case DELETE_LIST_MEMBER:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "DELETE_LIST_MEMBER: Not supported yet", "2"
						});
				break;
			case CHECK_LIST_MEMBERSHIP:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "CHECK_LIST_MEMBERSHIP: Not supported yet", "2"
						});
				break;
			case LIST_SUBSCRIBERS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "LIST_SUBSCRIBERS: Not supported yet", "2"
						});
				break;
			case SUBSCRIBE_LIST:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "SUBSCRIBE_LIST: Not supported yet", "2"
						});
				break;
			case UNSUBSCRIBE_LIST:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UNSUBSCRIBE_LIST: Not supported yet", "2"
						});
				break;
			case CHECK_LIST_SUBSCRIPTION:
				break;
			case DIRECT_MESSAGES:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "DIRECT_MESSAGES: Not supported yet", "2"
						});
				break;
			case SENT_DIRECT_MESSAGES:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "SENT_DIRECT_MESSAGES: Not supported yet", "2"
						});
				break;
			case SEND_DIRECT_MESSAGE:
				if(args != null && args.size() == 2) {
					screenName = (String)args.get(0);
					String text = (String)args.get(1);
					if (text != null)
					{
						tm.getAsyncTwitterInstance().sendDirectMessage(screenName, text);
					}
				}
				break;
			case DESTROY_DIRECT_MESSAGES:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "DESTROY_DIRECT_MESSAGES: Not supported yet", "2"
						});
				break;
			case CREATE_FRIENDSHIP:
				if(eventMap != null) {
					screenName = getScreenNameFromMap(eventMap.get("selections"));
				}
				//screenName = getScreenNameFromActiveTab();
				if (!screenName.equals(""))
				{
					tm.getAsyncTwitterInstance().createFriendship(screenName);
				}
				logger.debug("Create Friendship clicked");
				break;
			case DESTROY_FRIENDSHIP:
				//screenName = getScreenNameFromActiveTab();
				if(eventMap != null) {
					screenName = getScreenNameFromMap(eventMap.get("selections"));
				}
				if (!screenName.equals(""))
				{
					tm.getAsyncTwitterInstance().destroyFriendship(screenName);
				}
				break;
			case EXISTS_FRIENDSHIP:
				if(eventMap != null) {
					names = getScreenNamesFromMap(eventMap.get("selections"));
				}
				if (names.size() >= 2)
				{
					tm.getAsyncTwitterInstance().existsFriendship(names.get(0), names.get(1));
				}
				else
				{
					rv = "You must select more than one User to use this feature"; //TODO: needs I18N
				}
				break;
			case SHOW_FRIENDSHIP:
				if(eventMap != null) {
					names = getScreenNamesFromMap(eventMap.get("selections"));
				}
				//names = getScreenNamesFromActiveTab();
				if (names.size() >= 2)
				{
					tm.getAsyncTwitterInstance().showFriendship(names.get(0), names.get(1));
				}
				break;
			case INCOMING_FRIENDSHIPS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "INCOMING_FRIENDSHIPS: Not supported yet", "2"
						});
				break;
			case OUTGOING_FRIENDSHIPS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "OUTGOING_FRIENDSHIPS: Not supported yet", "2"
						});
				break;
			case FRIENDS_IDS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "FRIENDS_IDS: Not supported yet", "2"
						});
				break;
			case FOLLOWERS_IDS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "FOLLOWERS_IDS: Not supported yet", "2"
						});
				break;
			case RATE_LIMIT_STATUS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "RATE_LIMIT_STATUS: Not supported yet", "2"
						});
				break;
			case UPDATE_DELIVERY_DEVICE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UPDATE_DELIVERY_DEVICE: Not supported yet", "2"
						});
				break;
			case UPDATE_PROFILE_COLORS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UPDATE_PROFILE_COLORS: Not supported yet", "2"
						});
				break;
			case UPDATE_PROFILE_IMAGE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UPDATE_PROFILE_IMAGE: Not supported yet", "2"
						});
				break;
			case UPDATE_PROFILE_BACKGROUND_IMAGE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UPDATE_PROFILE_BACKGROUND_IMAGE: Not supported yet", "2"
						});
				break;
			case UPDATE_PROFILE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UPDATE_PROFILE: Not supported yet", "2"
						});
				break;
			case FAVORITES:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "FAVORITES: Not supported yet", "2"
						});
				break;
			case CREATE_FAVORITE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "CREATE_FAVORITE: Not supported yet", "2"
						});
				break;
			case DESTROY_FAVORITE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "DESTROY_FAVORITE: Not supported yet", "2"
						});
				break;
			case ENABLE_NOTIFICATION:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "ENABLE_NOTIFICATION: Not supported yet", "2"
						});
				break;
			case DISABLE_NOTIFICATION:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "DISABLE_NOTIFICATION: Not supported yet", "2"
						});
				break;
			case CREATE_BLOCK:
				logger.info("Create Block clicked");
				//screenName = getScreenNameFromActiveTab();
				if(eventMap != null) {
					screenName = getScreenNameFromMap(eventMap.get("selections"));
				}
				if (!screenName.equals(""))
				{
					tm.getAsyncTwitterInstance().createBlock(screenName);
				}
				break;
			case DESTROY_BLOCK:
				//screenName = getScreenNameFromActiveTab();
				if(eventMap != null) {
					screenName = getScreenNameFromMap(eventMap.get("selections"));
				}
				if (!screenName.equals(""))
				{
					tm.getAsyncTwitterInstance().destroyBlock(screenName);
				}
				break;
			case EXISTS_BLOCK:
				if(args != null) {
					String bu = (String)args.get(0);
					tm.getAsyncTwitterInstance().existsBlock(bu);
				}
				break;
			case BLOCKING_USERS:
				tm.getAsyncTwitterInstance().getBlockingUsers();
				break;
			case BLOCKING_USERS_IDS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "BLOCKING_USERS_IDS: Not supported yet", "2"
						});
				break;
			case REPORT_SPAM:
				logger.info("Report SPAM clicked");
				//get the username from the active tab and selected row
				//screenName = getScreenNameFromActiveTab();
				if(eventMap != null) {
					screenName = getScreenNameFromMap(eventMap.get("selections"));
				}
				if (!screenName.equals(""))
				{
					try
					{
						tm.getAsyncTwitterInstance().reportSpam(screenName);
					}
					catch (TwitterException ex)
					{
						//FIXME: this needs to be reported right away
						error = true;
						errorT = ex;
						errorMethod = TwitterMethod.REPORT_SPAM;
						//onException(ex, TwitterMethod.REPORT_SPAM);
					}
				}
				break;
			case AVAILABLE_TRENDS:
				tm.getAsyncTwitterInstance().getAvailableTrends();
				break;
			case LOCATION_TRENDS:
				if(args != null && args.size() != 0)
				{
					tm.getAsyncTwitterInstance().getLocationTrends((Integer)args.get(0));
				}
				break;
			case NEAR_BY_PLACES:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "NEAR_BY_PLACES: Not supported yet", "2"
						});
				break;
			case REVERSE_GEO_CODE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "REVERSE_GEO_CODE: Not supported yet", "2"
						});
				break;
			case GEO_DETAILS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "GEO_DETAILS: Not supported yet", "2"
						});
				break;
			case TEST:
				tm.getAsyncTwitterInstance().test();
				break;
		}
		return rv;
	}//}}}

	public void done()
	{
		if(error)
		{
			twitz.TwitzMainView.getInstance().onException(errorT, errorMethod);
		}
	}

	private String getScreenNameFromMap(Object obj) {//{{{
		String screenName = "";
		User u = null;
		if (isUserArray(obj))
		{
			if(logdebug)
				logger.debug("Found User array");
			User[] users = (User[]) obj;
			screenName = users[0].getScreenName();
		}
		else if (isStatusArray(obj))
		{
			if(logdebug)
				logger.debug("Found Status array");
			Status[] stat = (Status[]) obj;
			u = stat[0].getUser();
			screenName = u.getScreenName();
		}
		else if (isUser(obj))
		{
			u = (User) obj;
			screenName = u.getScreenName();
		}
		else if (isStatus(obj))
		{
			Status stat = (Status) obj;
			u = stat.getUser();
			screenName = u.getScreenName();
		}
		return screenName;
	}//}}}

	private Vector<String> getScreenNamesFromMap(Object obj) {//{{{
		String screenName = "";
		Vector<String> rv = new Vector<String>();
		User u = null;
		if (isUserArray(obj))
		{
			User[] users = (User[]) obj;
			if(users.length >= 2) {
				rv.addElement(users[0].getScreenName());
				rv.addElement(users[1].getScreenName());
			}
		}
		else if (isStatusArray(obj))
		{
			Status[] stat = (Status[]) obj;
			if(stat.length >= 2) {
				u = stat[0].getUser();
				rv.addElement(u.getScreenName());
				u = stat[1].getUser();
				rv.addElement(u.getScreenName());
			}
		}

		return rv;
	}//}}}


	private boolean isUser(Object obj) {
		return (obj instanceof User);
	}

	private boolean isUserArray(Object obj) {
		return (obj instanceof User[]);
	}

	private boolean isStatus(Object obj) {
		return (obj instanceof Status);
	}

	private boolean isStatusArray(Object obj) {
		return (obj instanceof Status[]);
	}

}
