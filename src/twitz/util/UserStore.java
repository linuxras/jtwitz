/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.util;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.tmatesoft.sqljet.core.SqlJetException;
import twitter4j.User;
import twitz.events.TwitzEvent;
import twitz.events.TwitzListener;
import twitz.util.DBManager.DBQuery;
import twitz.util.DBManager.DBTask;

/**
 *
 * @author mistik1
 */
public class UserStore {

	private static UserStore instance;
	private static final DBManager DBM = DBManager.getInstance();
	private static final Logger logger = Logger.getLogger(UserStore.class.getName());

	private UserStore()
	{
	}

	public static synchronized UserStore getInstance()
	{
		if(instance == null)
		{
			instance = new UserStore();
		}
		return instance;
	}

	public synchronized void registerUser(User user)
	{
		TwitzListener tl = new TwitzListener() {

			public void eventOccurred(TwitzEvent t)
			{
				Map map = t.getEventMap();
				switch(t.getEventType())
				{
					case DB_ERROR_EVENT:
						Exception e = (Exception) map.get("error");
						logger.error("Error while registering user "+e.getLocalizedMessage());
						break;
					case DB_RETURN_EVENT: //We dont care that it added a user
						break;
				}
			}
		};
		DBM.runQuery(DBTask.registerUser, tl, true, user);
//		try
//		{
//			DBM.registerUser(user);
//		}
//		catch (Exception ex)
//		{
//			logger.error("Error while registering user", ex);
//		}
	}

	public synchronized Vector<User> getRegisteredUsers()
	{
		Vector<User> v = new Vector<User>();
		try
		{
			v = DBM.getRegisteredUsers();
		}
		catch (Exception ex)
		{
			logger.error("Error while fetching user from database", ex);
		}
		return v;
	}

	public synchronized List<User> getRegisteredUsersAsList()
	{
		try
		{
			return DBM.getRegisteredUsersAsList();
		}
		catch (Exception ex)
		{
			logger.error("Error while fetching user from database", ex);
			return null;
		}
	}
}
