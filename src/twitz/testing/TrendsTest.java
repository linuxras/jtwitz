/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.testing;

import java.util.Date;
import twitter4j.Location;
import twitter4j.Trend;
import twitter4j.Trends;

/**
 *
 * @author Andrew Williams
 */
public class TrendsTest implements Trends{

	public Trend[] getTrends()
	{
		return new Trend[]{new TrendTest(), new TrendTest(), new TrendTest()};
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public Location getLocation()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Date getAsOf()
	{
		return new Date();
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public Date getTrendAt()
	{
		return new Date();
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public int compareTo(Trends o)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
