/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.util;

import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.tmatesoft.sqljet.core.SqlJetException;
import twitter4j.User;

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
		try
		{
			DBM.registerUser(user);
		}
		catch (Exception ex)
		{
			logger.error("Error while registering user", ex);
		}
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
