/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.testing;

import twitter4j.Trend;

/**
 *
 * @author mistik1
 */
public class TrendTest implements Trend{

	public String getName()
	{
		return "#TESTING";
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getUrl()
	{
		return "http://twitter.com/url";
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getQuery()
	{
		return "Query string here";
		//throw new UnsupportedOperationException("Not supported yet.");
	}

}
