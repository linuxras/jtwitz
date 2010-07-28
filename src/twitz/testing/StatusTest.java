/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.testing;

import java.util.Date;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.User;


/**
 *
 * @author Andrew Williams
 */
public class StatusTest implements Status{

	private Date created = new Date();
	private String[] texts = {
		"this is my test tweet, if this works then we know that something is going on right. It has just enough charactors for a full #twitter message.",
		"@monkeybrain - this is my test tweet, if this works then we know that something is going on right. It has just enough charactors for a full #twitter message.",
		"this is my test tweet, if this works then we know that something is going on right. It has just enough charactors for a full #twitter message.",
		"@twitz_ras - this is my test tweet", 
		"this is my test tweet, if this works then we know that something is going on right. It has just enough charactors for a full #twitter message.",
		"@fembot23552 - if this works then we know that something is going on right.",
		"this is my test tweet, if this works then we know that something is going on right. It has just enough charactors for a full #twitter message.",
		"It has just enough charactors for a full #twitter message.",
		"This works then we know that something is going on right.",
		"this is my test tweet, if this works then we know that something is going on right. It has just enough charactors for a full #twitter message.",
	};
	private int text = 0;

	public StatusTest(){}
	
	public StatusTest(int sample){this.text = sample;}
	public Date getCreatedAt()
	{
		return created;
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public long getId()
	{
		long r = 1234567890234525339L;
		return r;
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getText()
	{
		return texts[text];
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getSource()
	{
		return "jtwitz";
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean isTruncated()
	{
		return false;
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public long getInReplyToStatusId()
	{
		return 1234545323L;
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public int getInReplyToUserId()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getInReplyToScreenName()
	{
		return "Powder-Puff";
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public GeoLocation getGeoLocation()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Place getPlace()
	{
		return null;
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean isFavorited()
	{
		return true;
	}

	public User getUser()
	{
		return new UserTest();
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean isRetweet()
	{
		return false;
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public Status getRetweetedStatus()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String[] getContributors()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public int compareTo(Status o)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public RateLimitStatus getRateLimitStatus()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
