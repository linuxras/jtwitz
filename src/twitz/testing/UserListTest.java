/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.testing;

import java.net.URI;
import twitter4j.RateLimitStatus;
import twitter4j.User;
import twitter4j.UserList;

/**
 *
 * @author mistik1
 */
public class UserListTest implements UserList{

	private int id = -1;
	private String name = "Test List";
	private String fullname = "Full List Name";
	private String slug = "this is the test list slug";
	private String description = "A test list for development";
	private int subCount = 0;
	private int memberCount = 1;
	private boolean pub = true;
	private User user = new UserTest();

	public UserListTest() {

	}

	public UserListTest(int id, String name, String fullName,
			String slug, String desc, int subCount,
			int memberCount, boolean pub, User user)
	{
		this.id = id;
		this.name = name;
		this.fullname = fullName;
		this.slug = slug;
		this.description = desc;
		this.subCount = subCount;
		this.memberCount = memberCount;
		this.pub = pub;
		this.user = user;
	}

	public int getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	public String getFullName()
	{
		return this.fullname;
	}

	public String getSlug()
	{
		return this.slug;
	}

	public String getDescription()
	{
		return this.description;
	}

	public int getSubscriberCount()
	{
		return this.subCount;
	}

	public int getMemberCount()
	{
		return this.memberCount;
	}

	public URI getURI()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean isPublic()
	{
		return this.pub;
	}

	public User getUser()
	{
		return this.user;
	}

	public int compareTo(UserList o)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public RateLimitStatus getRateLimitStatus()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
