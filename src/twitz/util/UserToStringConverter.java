/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.util;

import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;
import twitter4j.User;

/**
 *
 * @author mistik1
 */
public class UserToStringConverter extends ObjectToStringConverter {

	public UserToStringConverter() {}

	@Override
	public String[] getPossibleStringsForItem(Object item) {
		if(item == null) return new String[0];
		//System.out.println(item);
		if(item instanceof User)
		{
			//throw new IllegalArgumentException("item must a twitter4j.User");
			User u = (User)item;
			return new String[]{u.getScreenName(), u.getName()};
		}
		return new String[]{"Not Found"};
	}

	@Override
	public String getPreferredStringForItem(Object item)
	{
		return item == null ? "" : getPossibleStringsForItem(item)[0];
	}

}
