/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.testing;

import twitter4j.Trend;

/**
 *
 * @author Andrew Williams
 */
public class TrendTest implements Trend{

	public String getName()
	{
		return "#TESTING TRENDS";
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getUrl()
	{
		return "http://twitter.com/url";
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getQuery()
	{
		return "TESTING+TRENDS";
		//throw new UnsupportedOperationException("Not supported yet.");
	}

}
