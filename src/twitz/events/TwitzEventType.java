/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.events;

/**
 *
 * @author mistik
 */
public enum TwitzEventType {
	SEARCH,
	TRENDS,
	CURRENT_TRENDS,
	DAILY_TRENDS,
	WEEKLY_TRENDS,
	PUBLIC_TIMELINE,
	HOME_TIMELINE,
	FRIENDS_TIMELINE,
	USER_TIMELINE,
	MENTIONS,
	RETWEETED_BY_ME,
	RETWEETED_TO_ME,
	RETWEETS_OF_ME,
	SHOW_STATUS,
	UPDATE_STATUS,
	DESTROY_STATUS,
	RETWEET_STATUS,
	RETWEETS,
	SHOW_USER,
	LOOKUP_USERS,
	SEARCH_USERS,
	SUGGESTED_USER_CATEGORIES,
	USER_SUGGESTIONS,
	FRIENDS_STATUSES,
	FOLLOWERS_STATUSES,
	CREATE_USER_LIST,
	UPDATE_USER_LIST,
	USER_LISTS,
	SHOW_USER_LIST,
	DELETE_USER_LIST,
	USER_LIST_STATUSES,
	USER_LIST_MEMBERSHIPS,
	USER_LIST_SUBSCRIPTIONS,
	LIST_MEMBERS,
	ADD_LIST_MEMBER,
	DELETE_LIST_MEMBER,
	CHECK_LIST_MEMBERSHIP,
	LIST_SUBSCRIBERS,
	SUBSCRIBE_LIST,
	UNSUBSCRIBE_LIST,
	CHECK_LIST_SUBSCRIPTION,
	DIRECT_MESSAGES,
	SENT_DIRECT_MESSAGES,
	SEND_DIRECT_MESSAGE,
	DESTROY_DIRECT_MESSAGES,
	CREATE_FRIENDSHIP,
	DESTROY_FRIENDSHIP,
	EXISTS_FRIENDSHIP,
	SHOW_FRIENDSHIP,
	INCOMING_FRIENDSHIPS,
	OUTGOING_FRIENDSHIPS,
	FRIENDS_IDS,
	FOLLOWERS_IDS,
	RATE_LIMIT_STATUS,
	UPDATE_DELIVERY_DEVICE,
	UPDATE_PROFILE_COLORS,
	UPDATE_PROFILE_IMAGE,
	UPDATE_PROFILE_BACKGROUND_IMAGE,
	UPDATE_PROFILE,
	FAVORITES,
	CREATE_FAVORITE,
	DESTROY_FAVORITE,
	ENABLE_NOTIFICATION,
	DISABLE_NOTIFICATION,
	CREATE_BLOCK,
	DESTROY_BLOCK,
	EXISTS_BLOCK,
	BLOCKING_USERS,
	BLOCKING_USERS_IDS,
	REPORT_SPAM,
	AVAILABLE_TRENDS,
	LOCATION_TRENDS,
	NEAR_BY_PLACES,
	REVERSE_GEO_CODE,
	GEO_DETAILS,
	TEST,
	UPDATE_FRIENDS_TWEETS_LIST,
	LOGIN,
	TREND_SEARCH,
	DB_RETURN_EVENT,
	DB_ERROR_EVENT
}
