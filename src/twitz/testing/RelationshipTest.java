/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.testing;

import twitter4j.RateLimitStatus;
import twitter4j.Relationship;

/**
 *
 * @author Andrew Williams
 */
public class RelationshipTest implements Relationship
{

	public RelationshipTest()
	{

	}

	public int getSourceUserId()
	{
		return 654321;
	}

	public int getTargetUserId()
	{
		return 12345;
	}

	public boolean isSourceBlockingTarget()
	{
		return false;
	}

	public String getSourceUserScreenName()
	{
		return "A_ScreenName";
	}

	public String getTargetUserScreenName()
	{
		return "Twitz_ras";
	}

	public boolean isSourceFollowingTarget()
	{
		return false;
	}

	public boolean isTargetFollowingSource()
	{
		return true;
	}

	public boolean isSourceFollowedByTarget()
	{
		return true;
	}

	public boolean isTargetFollowedBySource()
	{
		return false;
	}

	public boolean isSourceNotificationsEnabled()
	{
		return false;
	}

	public RateLimitStatus getRateLimitStatus()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
