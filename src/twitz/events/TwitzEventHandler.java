package twitz.events;

import java.awt.Component;
import java.util.Map;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import twitter4j.*;
import org.apache.log4j.Logger;
import twitz.TwitzMainView;
import twitz.twitter.TwitterManager;
import twitz.util.TwitzSessionManager;


public class TwitzEventHandler extends SwingWorker<String, Object> {

	private TwitzEvent mainEvent;
	private TwitterManager tm;
	private TwitterMethod errorMethod;
	private TwitterException errorT;
	private boolean error = false;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private boolean logdebug = logger.isDebugEnabled();
	private Vector<String> names = new Vector<String>();
    private org.jdesktop.application.ResourceMap resourceMap = twitz.TwitzApp.getContext().getResourceMap(twitz.TwitzMainView.class);
	private final String sessionName;
	private final TwitzMainView view;
	//private PropertyChangeSupport pcs = new EDTPropertyChangeSupport(this);

	public TwitzEventHandler(TwitzEvent event, String session)
	{
		this.mainEvent = event;
		this.sessionName = session;
		this.view = TwitzSessionManager.getInstance().getTwitMainViewForSession(sessionName);
		this.tm = view.getTwitterManager();
	}

	public void exec()
	{
		firePropertyChange("started", null, null);
		firePropertyChange("message", null, String.format("Performing task - %s", resourceMap.getString(mainEvent.getEventType().name())));
		execute();
	}
	
	public String doInBackground() //{{{
	{
		String rv = "";
		int userId = -1;
		int listId = -1;
		String screenName = null;
		Component caller = null;
		Map eventMap = null;
		boolean async = true;
		ArrayList args = null;
		Paging pager = null;
		long cursor = -10;

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
							"Twitz Message", "TRENDS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case CURRENT_TRENDS:
				//
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "CURRENT_TRENDS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case DAILY_TRENDS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "DAILY_TRENDS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case WEEKLY_TRENDS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "WEEKLY_TRENDS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
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
							"Twitz Message", "SHOW_STATUS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case UPDATE_STATUS:
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
							"Twitz Message", "SHOW_USER: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case LOOKUP_USERS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "LOOKUP_USERS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
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
							"Twitz Message", "SUGGESTED_USER_CATEGORIES: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case USER_SUGGESTIONS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "USER_SUGGESTIONS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case FRIENDS_STATUSES:
				if(args != null && args.size() == 2) //pager
				{
					if(args.get(0) instanceof String)
						screenName = (String)args.get(0);
					if(args.get(0) instanceof Integer)
						userId = (Integer)args.get(0);
					if(args.get(1) instanceof Long)
						cursor = (Long)args.get(1);
					if(screenName != null && cursor != -10)
						tm.getAsyncTwitterInstance().getFriendsStatuses(screenName, cursor);
					else if(userId != -1 && cursor != -10)
						tm.getAsyncTwitterInstance().getFriendsStatuses(userId, cursor);
				}
				else if(args != null && args.size() == 1) //no pager
				{
					if(args.get(0) instanceof String)
						screenName = (String)args.get(0);
					if(args.get(0) instanceof Integer)
						userId = (Integer)args.get(0);
					
					if(screenName != null)
						tm.getAsyncTwitterInstance().getFriendsStatuses(screenName);
					else if(userId != -1)
						tm.getAsyncTwitterInstance().getFriendsStatuses(userId);
				}
				else
				{
					tm.getAsyncTwitterInstance().getFriendsStatuses();
				}
				break;
			case FOLLOWERS_STATUSES:
				if(args != null && args.size() == 2) //pager
				{
					if(args.get(0) instanceof String)
						screenName = (String)args.get(0);
					if(args.get(0) instanceof Integer)
						userId = (Integer)args.get(0);
					if(args.get(1) instanceof Long)
						cursor = (Long)args.get(1);
					if(screenName != null && cursor != -10)
						tm.getAsyncTwitterInstance().getFollowersStatuses(screenName, cursor);
					else if(userId != -1 && cursor != -10)
						tm.getAsyncTwitterInstance().getFollowersStatuses(userId, cursor);
				}
				else if(args != null && args.size() == 1) //no pager
				{
					if(args.get(0) instanceof String)
						screenName = (String)args.get(0);
					if(args.get(0) instanceof Integer)
						userId = (Integer)args.get(0);
					
					if(screenName != null)
						tm.getAsyncTwitterInstance().getFollowersStatuses(screenName);
					else if(userId != -1)
						tm.getAsyncTwitterInstance().getFollowersStatuses(userId);
				}
				else
				{
					tm.getAsyncTwitterInstance().getFollowersStatuses();
				}
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
							"Twitz Message", "UPDATE_USER_LIST: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case USER_LISTS:
				if(args != null && !args.isEmpty())
				{
					tm.getAsyncTwitterInstance().getUserLists((String)args.get(0), (Long)args.get(1));;
				}
				break;
			case SHOW_USER_LIST:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "SHOW_USER_LIST: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case DESTROY_USER_LIST:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "DESTROY_USER_LIST: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
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
							"Twitz Message", "USER_LIST_MEMBERSHIPS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case USER_LIST_SUBSCRIPTIONS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "USER_LIST_SUBSCRIPTIONS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
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
				if(args != null && args.size() == 2)
				{
					listId = (Integer)args.get(0);
					userId = (Integer)args.get(1);
					if(screenName != null && !screenName.equals(""))
					{
						tm.getAsyncTwitterInstance().addUserListMember(listId, userId);
					}
				}
				break;
			case DELETE_LIST_MEMBER:
				if(args != null && args.size() == 2)
				{
					listId = (Integer)args.get(0);
					userId = (Integer)args.get(1);
					if(screenName != null && !screenName.equals(""))
					{
						tm.getAsyncTwitterInstance().deleteUserListMember(listId, userId);
					}
				}
				break;
			case CHECK_LIST_MEMBERSHIP:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "CHECK_LIST_MEMBERSHIP: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case LIST_SUBSCRIBERS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "LIST_SUBSCRIBERS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case SUBSCRIBE_LIST:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "SUBSCRIBE_LIST: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case UNSUBSCRIBE_LIST:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UNSUBSCRIBE_LIST: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case CHECK_LIST_SUBSCRIPTION:
				break;
			case DIRECT_MESSAGES:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "DIRECT_MESSAGES: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case SENT_DIRECT_MESSAGES:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "SENT_DIRECT_MESSAGES: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
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
							"Twitz Message", "DESTROY_DIRECT_MESSAGES: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
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
					rv = resourceMap.getString("SELECT_MORE_USERS_ERROR.TEXT"); 
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
							"Twitz Message", "INCOMING_FRIENDSHIPS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case OUTGOING_FRIENDSHIPS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "OUTGOING_FRIENDSHIPS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case FRIENDS_IDS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "FRIENDS_IDS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case FOLLOWERS_IDS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "FOLLOWERS_IDS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case RATE_LIMIT_STATUS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "RATE_LIMIT_STATUS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case UPDATE_DELIVERY_DEVICE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UPDATE_DELIVERY_DEVICE: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case UPDATE_PROFILE_COLORS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UPDATE_PROFILE_COLORS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case UPDATE_PROFILE_IMAGE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UPDATE_PROFILE_IMAGE: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case UPDATE_PROFILE_BACKGROUND_IMAGE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UPDATE_PROFILE_BACKGROUND_IMAGE: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case UPDATE_PROFILE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "UPDATE_PROFILE: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case FAVORITES:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "FAVORITES: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case CREATE_FAVORITE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "CREATE_FAVORITE: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case DESTROY_FAVORITE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "DESTROY_FAVORITE: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case ENABLE_NOTIFICATION:
				if(args != null && args.size() == 1)
				{
					screenName = (String)args.get(0);
					if(screenName != null && !screenName.equals(""))
					{
						tm.getAsyncTwitterInstance().enableNotification(screenName);
					}
				}
				break;
			case DISABLE_NOTIFICATION:
				if(args != null && args.size() == 1)
				{
					screenName = (String)args.get(0);
					if(screenName != null && !screenName.equals(""))
					{
						tm.getAsyncTwitterInstance().disableNotification(screenName);
					}
				}
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
							"Twitz Message", "BLOCKING_USERS_IDS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
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
				if(args != null && !args.isEmpty())
				{
					tm.getAsyncTwitterInstance().getLocationTrends((Integer)args.get(0));
				}
				break;
			case NEAR_BY_PLACES:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "NEAR_BY_PLACES: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case REVERSE_GEO_CODE:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "REVERSE_GEO_CODE: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case GEO_DETAILS:
				firePropertyChange("POPUP", new Object(), new String[]
						{
							"Twitz Message", "GEO_DETAILS: "+resourceMap.getString("NOT_SUPPORTED.TEXT"), "2"
						});
				break;
			case TEST:
				tm.getAsyncTwitterInstance().test();
				break;
		}
		return rv;
	}//}}}

	@Override
	public void done()
	{
		if(error)
		{
			view.onException(errorT, errorMethod);
			return;
		}
		String value = "";
		try
		{
			value = get();
		}
		catch(Exception ignore){/*ignore this for now*/}
		if(!value.equals(""))
			JOptionPane.showMessageDialog(null, value, resourceMap.getString("ERROR_TITLE.TEXT"), JOptionPane.ERROR_MESSAGE);
		firePropertyChange("message", null, String.format("Completed task - %s",resourceMap.getString(mainEvent.getEventType().name())));
		firePropertyChange("done", null, null);
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
