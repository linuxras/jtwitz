/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import org.apache.log4j.Logger;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import twitter4j.User;
import twitter4j.UserList;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventModel;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;
import twitz.ui.models.ContactsListModel;
import twitz.ui.renderers.ContactsRenderer;
import twitz.util.*;

/**
 *
 * @author mistik1
 */
public class ContactsList extends JList implements ActionListener, TwitzEventModel{

	private final ContactsListModel model = new ContactsListModel();
	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private static Logger logger = Logger.getLogger(ContactsList.class.getName());
	private boolean logdebug = logger.isDebugEnabled();
	private UserStore store = UserStore.getInstance();
	private boolean inUserList = false;
	/**
	 * This is the Userlist object, this is only valid if inUserList is <em>true</em>
	 */
	private UserList userlist = null;

	public ContactsList() {
		this(new ContactsListModel());
	}
	public ContactsList(ContactsListModel model) {
		super(model);
		this.setCellRenderer(new ContactsRenderer());
	}

	public User addUser(User u) {
		ContactsListModel dm = (ContactsListModel)getModel();
		dm.addElement(u);
		store.registerUser(u);
		return u;
	}

	public User[] addUser(User[] u) {
		ContactsListModel dm = (ContactsListModel)getModel();
		for(User usr : u) {
			dm.addElement(usr);
			store.registerUser(usr);
		}
		return u;
	}

	public Vector<User> addUser(Vector<User> u) {
		ContactsListModel dm = (ContactsListModel)getModel();
		for(User usr : u) {
			dm.addElement(u);
			store.registerUser(usr);
		}
		return u;
	}

	public User removeUser(User u) {//{{{
		ContactsListModel dm = (ContactsListModel)getModel();
		if(dm.contains(u)) {
			dm.removeElement(u);
			return u;
		}
		else
			return null;
	}//}}}

	public User removeUser(int u) {//{{{
		ContactsListModel dm = (ContactsListModel)getModel();
		if(u < dm.getSize()) {
			User usr = dm.elementAt(u);
			return usr;
		}
		else
			return null;
	}//}}}

	public void setSelectedValue(User aUser, boolean scrollToUser) {//{{{
        if(aUser == null)
            setSelectedIndex(-1);
        else if(!aUser.equals(getSelectedValue())) {
            int i,c;
            ContactsListModel dm = (ContactsListModel)getModel();
            for(i=0,c=dm.getSize();i<c;i++) {
                if(aUser.equals(dm.getElementAt(i))){
                    setSelectedIndex(i);
                    if(scrollToUser)
                        ensureIndexIsVisible(i);
                    //repaint();
                    return;
                }
			}
            setSelectedIndex(-1);
        }
        //repaint();
    }//}}}

	@Override
	public User getSelectedValue() {//{{{
        int i = getMinSelectionIndex();
        if (i == -1) {
			return null;
		}
		else {
			ContactsListModel dm = (ContactsListModel)getModel();
			return dm.getElementAt(i);
		}
    }//}}}

	@Override
	public User[] getSelectedValues() {//{{{
        ListSelectionModel sm = getSelectionModel();
        ContactsListModel dm = (ContactsListModel) getModel();

        int iMin = sm.getMinSelectionIndex();
        int iMax = sm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return new User[0];
        }

        User[] rvTmp = new User[1+ (iMax - iMin)];
        int n = 0;
        for(int i = iMin; i <= iMax; i++) {
            if (sm.isSelectedIndex(i)) {
                rvTmp[n++] = dm.getElementAt(i);
            }
        }
        User[] rv = new User[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        return rv;
    }//}}}

	@Override
	public ContactsListModel getModel() {//{{{
		if(super.getModel() instanceof ContactsListModel) {
			return (ContactsListModel)super.getModel();
		}
		return new ContactsListModel();
	}//}}}

	//TwitzEventModel
	public void addTwitzListener(TwitzListener o) {
		dtem.addTwitzListener(o);
	}

	public void removeTwitzListener(TwitzListener o) {
		dtem.removeTwitzListener(o);
	}

	public void fireTwitzEvent(TwitzEvent e) {
		dtem.fireTwitzEvent(e);
	}

	public void actionPerformed(ActionEvent e) {
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("caller", this);
		map.put("async", true);
		User[] selections = getSelectedValues();
		map.put("selections", selections);
		if(logdebug)
			logger.debug("Firing TwitzEvent");
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.valueOf(e.getActionCommand()), new java.util.Date().getTime(), map));
	}

	public boolean isUserList() {
		return inUserList;
	}

	public void setInUserList(boolean val) {
		inUserList = val;
	}

	public void setUserList(UserList list)
	{
		if(list != null)
		{
			this.userlist = list;
			setInUserList(true);
		}
		else
			setInUserList(false);
	}

	public UserList getUserList()
	{
		return this.userlist;
	}
}
