/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.twitter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.jdesktop.application.ResourceMap;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Category;
import twitter4j.DirectMessage;
import twitter4j.IDs;
import twitter4j.Location;
import twitter4j.PagableResponseList;
import twitter4j.Place;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.User;
import twitter4j.UserList;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.util.SettingsManager;

/**
 *
 * @author mistik1
 */
public class TwitterManager extends DefaultTwitzEventModel/*implements TwitterListener*/ {

	SettingsManager config = SettingsManager.getInstance();
	private Twitter twitter = new TwitterFactory().getInstance();
	private twitz.TwitzView view;
	private AsyncTwitter atwitter;
	private Logger logger = Logger.getLogger(TwitterManager.class.getName());
	private static TwitterManager instance;
	private ResourceMap resource;

	private TwitterManager(twitz.TwitzView v) {
		this.view = v;
		this.atwitter = new AsyncTwitterFactory(view).getInstance(config.getString("twitter.id"),config.getString("twitter.password"));
		resource = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(twitz.twitter.TwitterManager.class);
	}

	/**
	 * Get a TwitterManager instance
	 * @param v The TwitzView using this manager
	 * @return The Singleton TwitterManager Object
	 */
	public synchronized static TwitterManager getInstance(twitz.TwitzView v) {
		if(instance == null)
			instance = new TwitterManager(v);
		return instance;
	}

	public void setPassword(String pass) {
		config.setProperty("twitter.password", pass);
		//twitter = new TwitterFactory().getInstance(config.getString("twitter.id"), pass);
	}

	public void setUserId(int id) {
		config.setProperty("twitter.id", id+"");
		//twitter = new TwitterFactory().getInstance(config.getString("twitter.id"),config.getString("twitter.password"));
	}

	public void sendDirectMessage(String rec, String msg) {
		try {
			DirectMessage message = twitter.sendDirectMessage(rec, msg);
		}
		catch(TwitterException te) {
			logger.log(Level.SEVERE, te.getMessage());
			JOptionPane.showMessageDialog(null, te.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
		}
	}

	public List<DirectMessage> getDirectMessages() {
		List<DirectMessage> messages = null;
		try
		{
			messages = twitter.getDirectMessages();
		}
		catch (TwitterException ex)
		{
			logger.log(Level.SEVERE, ex.getMessage());
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
		}
		return messages;
	}

	public void sendTweet(String msg) throws TwitterException, IllegalStateException {
		Status stat = null;
//		try
//		{
			stat = twitter.updateStatus(msg);
//		}
//		catch (Exception ex)
//		{
//			logger.log(Level.SEVERE, ex.getMessage());
//			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
//		}
		if(stat != null) {
			//TODO Update pertinent list with this new data. I would like to fire an event here
			this.fireTwitzEvent(new TwitzEvent(stat, twitter, TwitzEvent.UPDATE, new java.util.Date().getTime()));
		}
	}

	public void login() {
		twitter = new TwitterFactory().getInstance(config.getString("twitter.id"),config.getString("twitter.password"));
		atwitter = new AsyncTwitterFactory(view).getInstance(config.getString("twitter.id"),config.getString("twitter.password"));
	}

	public ResourceMap getResourceMap() {
		return resource;
	}
	
	public Twitter getTwitterInstance() {
		return twitter;
	}

	public AsyncTwitter getAsyncTwitterInstance() {
		return atwitter;
	}
	
	// <editor-fold defaultstate="collapsed" desc="TwitterListener Abstract Methods">
	/*//TwitterListener abstract methods
	public void searched(QueryResult queryResult)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotTrends(Trends trends)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotCurrentTrends(Trends trends)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotDailyTrends(List<Trends> trendsList)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotWeeklyTrends(List<Trends> trendsList)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotPublicTimeline(ResponseList<Status> statuses)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotHomeTimeline(ResponseList<Status> statuses)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFriendsTimeline(ResponseList<Status> statuses)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserTimeline(ResponseList<Status> statuses)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotMentions(ResponseList<Status> statuses)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotRetweetedByMe(ResponseList<Status> statuses)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotRetweetedToMe(ResponseList<Status> statuses)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotRetweetsOfMe(ResponseList<Status> statuses)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotShowStatus(Status status)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedStatus(Status status)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedStatus(Status destroyedStatus)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void retweetedStatus(Status retweetedStatus)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotRetweets(ResponseList<Status> retweets)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserDetail(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void lookedupUsers(ResponseList<User> users)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void searchedUser(ResponseList<User> userList)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotSuggestedUserCategories(ResponseList<Category> category)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserSuggestions(ResponseList<User> users)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFriendsStatuses(PagableResponseList<User> users)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFollowersStatuses(PagableResponseList<User> users)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void createdUserList(UserList userList)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedUserList(UserList userList)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserLists(PagableResponseList<UserList> userLists)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotShowUserList(UserList userList)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedUserList(UserList userList)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserListStatuses(ResponseList<Status> statuses)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserListMemberships(PagableResponseList<UserList> userLists)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserListSubscriptions(PagableResponseList<UserList> userLists)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserListMembers(PagableResponseList<User> users)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void addedUserListMember(UserList userList)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void deletedUserListMember(UserList userList)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void checkedUserListMembership(User users)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserListSubscribers(PagableResponseList<User> users)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void subscribedUserList(UserList userList)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void unsubscribedUserList(UserList userList)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void checkedUserListSubscription(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotDirectMessages(ResponseList<DirectMessage> messages)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotSentDirectMessages(ResponseList<DirectMessage> messages)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void sentDirectMessage(DirectMessage message)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedDirectMessage(DirectMessage message)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void createdFriendship(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedFriendship(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotExistsFriendship(boolean exists)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotShowFriendship(Relationship relationship)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotIncomingFriendships(IDs ids)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotOutgoingFriendships(IDs ids)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFriendsIDs(IDs ids)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFollowersIDs(IDs ids)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotRateLimitStatus(RateLimitStatus rateLimitStatus)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedDeliveryDevice(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedProfileColors(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedProfileImage(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedProfileBackgroundImage(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedProfile(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFavorites(ResponseList<Status> statuses)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void createdFavorite(Status status)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedFavorite(Status status)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void enabledNotification(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void disabledNotification(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void createdBlock(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedBlock(User user)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotExistsBlock(boolean blockExists)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotBlockingUsers(ResponseList<User> blockingUsers)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotBlockingUsersIDs(IDs blockingUsersIDs)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void reportedSpam(User reportedSpammer)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotAvailableTrends(ResponseList<Location> locations)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotLocationTrends(Trends trends)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotNearByPlaces(ResponseList<Place> places)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotReverseGeoCode(ResponseList<Place> places)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotGeoDetails(Place place)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void tested(boolean test)
	{
	throw new UnsupportedOperationException("Not supported yet.");
	}

	public void onException(TwitterException te, TwitterMethod method)
	{
	if(method.equals(TwitterMethod.UPDATE_STATUS))
	view.sendTweetClicked().cancel(true);
	view.displayError(te, "Twitter Error", "Error Occurred while attempting: \n\t"+resource.getString(method.name()), method);
	}
	//END abstract methods*/
	//</editor-fold>

}
