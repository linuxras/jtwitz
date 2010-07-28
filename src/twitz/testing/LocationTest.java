/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.testing;

import twitter4j.Location;

/**
 *
 * @author Andrew Williams
 */
public class LocationTest implements Location {

	String countryName = "Worldwide";
	int woeid = 1;
	String countryCode = "";
	String placeName = "";
	int placeCode = 1;
	String name = countryName;
	String url = "http://twitter.com";

	public LocationTest() {

	}

	public LocationTest(String countryName, String countryCode,
			String placeName, int placeCode, int woeid)
	{
		this.countryName = countryName;
		this.countryCode = countryCode;
		this.placeName = placeName;
		this.placeCode = placeCode;
		this.woeid = woeid;
	}

	public int getWoeid()
	{
		return woeid;
	}

	public String getCountryName()
	{
		return countryName;
	}

	public String getCountryCode()
	{
		return countryCode;
	}

	public String getPlaceName()
	{
		return placeName;
	}

	public int getPlaceCode()
	{
		return placeCode;
	}

	public String getName()
	{
		return name;
	}

	public String getURL()
	{
		return url;
	}

}
