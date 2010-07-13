/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.util;

import java.util.List;
import java.util.Vector;
import twitter4j.User;

/**
 *
 * @author mistik1
 */
public class UserStore {

	private static Vector<User> users = new Vector<User>();
	private static UserStore instance;

	private UserStore()
	{
	}

	public static UserStore getInstance()
	{
		if(instance == null)
		{
			instance = new UserStore();
		}
		return instance;
	}

	public static void registerUser(User user)
	{
		if(!users.contains(user))
			users.addElement(user);
	}

	public static Vector<User> getRegisteredUsers()
	{
		//return (Vector<User>)users.clone();
		return users;
	}

	public static List<User> getRegisteredUsersAsList()
	{
		Vector<User> v = (Vector<User>) users.clone();
		return v.subList(0, users.size());
	}
}
