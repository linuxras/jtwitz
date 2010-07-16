/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.util;

import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmatesoft.sqljet.core.SqlJetException;
import twitter4j.User;

/**
 *
 * @author mistik1
 */
public class UserStore {

	private static Vector<User> users = new Vector<User>();
	private static UserStore instance;
	private static final DBManager DBM = DBManager.getInstance();

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

	public void registerUser(User user)
	{
		try
		{
			DBM.registerUser(user);
			//if(!users.contains(user))
			//	users.addElement(user);
		}
		catch (SqlJetException ex)
		{
			Logger.getLogger(UserStore.class.getName()).log(Level.SEVERE, null, ex);
		}
		//if(!users.contains(user))
		//	users.addElement(user);
	}

	public Vector<User> getRegisteredUsers()
	{
		Vector<User> v = new Vector<User>();
		try
		{
			//return (Vector<User>)users.clone();
			v = DBM.getRegisteredUsers();
		}
		catch (SqlJetException ex)
		{
			Logger.getLogger(UserStore.class.getName()).log(Level.SEVERE, null, ex);
		}
		return v;
	}

	public List<User> getRegisteredUsersAsList()
	{
		try
		{
			return DBM.getRegisteredUsersAsList();
		}
		catch (SqlJetException ex)
		{
			Logger.getLogger(UserStore.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
}
