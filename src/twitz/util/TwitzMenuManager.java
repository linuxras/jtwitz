/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.util;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import org.jdesktop.application.ResourceMap;
import twitz.TwitzApp;
import twitz.TwitzMainView;
import twitz.ui.BlockedPanel;
import twitz.ui.ContactsList;
import twitz.ui.FollowersPanel;
import twitz.ui.FriendsPanel;
import twitz.ui.StatusList;
import twitz.ui.StatusPanel;
import twitz.ui.TimeLinePanel;
import twitz.ui.UserListPanel;

/**
 *
 * @author Andrew Williams
 */
public class TwitzMenuManager implements PropertyChangeListener{

	public static boolean REPORT_SPAM = true;
	public static boolean CREATE_BLOCK = true;
	public static boolean DESTROY_BLOCK = true;
	public static boolean CREATE_FRIENDSHIP = true;
	public static boolean DESTROY_FRIENDSHIP = true;
	public static boolean EXISTS_FRIENDSHIP = true;
	public static boolean SHOW_FRIENDSHIP = true;
	public static boolean SEND_DIRECT_MESSAGE = true;
	public static boolean SEND_DIRECT_MESSAGES = true;
	public static boolean NEAR_BY_PLACES = true;
	public static boolean GEO_DETAILS = true;
	public static boolean ADD_LIST_MEMBER = true;
	public static boolean DELETE_LIST_MEMBER = true;
	public static boolean CHECK_LIST_MEMBERSHIP = true;
	public static boolean USER_TIMELINE = true;


	public static ResourceMap getResourceMap() {
		return TwitzApp.getContext().getResourceMap(TwitzMainView.class);
	}

	/**
	 * Method builds the Action menu that is displayed application wide
	 * when a user is clicked on.
	 * @return A JPopupMenu
	 */
	public static synchronized JPopupMenu getActionsMenu(Component caller)
	{//{{{
//RETWEETED_BY_ME
//RETWEETED_TO_ME
//RETWEETS_OF_ME
//SHOW_STATUS
//SHOW_USER
		ContactsList cl = null;
		StatusList tl = null;
		UserListPanel ulp = null;
		StatusPanel sp = null;
		TimeLinePanel tlp = null;
		FriendsPanel fp = null;
		BlockedPanel bp = null;
		FollowersPanel flp = null;

		//ActionListener actions = null;
		//javax.swing.JComponent actions = null;
		javax.swing.ActionMap aMap = null;

		boolean selected = true;
		boolean blocked = false;
		if(caller != null)
		{
			if (caller instanceof JTable)
			{
				JTable t = (JTable) caller;
				if (t.getSelectedRow() == -1)
				{
					selected = false;
				}
				aMap = TwitzApp.getContext().getActionMap(t.getClass(), t);
			}
			else if (caller instanceof ContactsList)
			{
				ContactsList l = (ContactsList) caller;
				cl = (ContactsList) caller;
				if (l.getSelectedIndex() == -1)
				{
					selected = false;
				}
				aMap = TwitzApp.getContext().getActionMap(cl.getClass(), cl);
			}
			else if (caller instanceof UserListPanel)
			{
				//caller = (UserListPanel)caller;
				ContactsList l = ((UserListPanel) caller).getContactsList();
				ulp = (UserListPanel) caller;
				if (l.getSelectedIndex() == -1)
				{
					selected = false;
				}
				aMap = TwitzApp.getContext().getActionMap(ulp.getClass(), ulp);
				//actions = ulp;
			}
			else if (caller instanceof StatusList)
			{
				tl = (StatusList) caller;
				if (tl.getSelectedIndex() == -1)
				{
					selected = false;
				}
				aMap = TwitzApp.getContext().getActionMap(tl.getClass(), tl);
				//actions = tl;
			}
			else if(caller instanceof StatusPanel)
			{
				sp = (StatusPanel)caller;
				if(sp.getStatusList().getSelectedIndex() == -1)
				{
					selected = false;
				}
				aMap = TwitzApp.getContext().getActionMap(sp.getClass(), sp);
				//actions = sp;
			}
			else if(caller instanceof TimeLinePanel)
			{
				tlp = (TimeLinePanel)caller;
				if(tlp.getStatusList().getSelectedIndex() == -1)
				{
					selected = false;
				}
				aMap = TwitzApp.getContext().getActionMap(tlp.getClass(), tlp);
				//actions = tlp;
			}
			else if(caller instanceof FriendsPanel)
			{
				fp = (FriendsPanel)caller;
				if(fp.getContactsList().getSelectedIndex() == -1)
				{
					selected = false;
				}
				aMap = TwitzApp.getContext().getActionMap(fp.getClass(), fp);
				//actions = fp;
			}
			else if(caller instanceof BlockedPanel)
			{
				bp = (BlockedPanel)caller;
				if(bp.getContactsList().getSelectedIndex() == -1)
				{
					selected = false;
				}
				selected = false;
				blocked = true;
				aMap = TwitzApp.getContext().getActionMap(bp.getClass(), bp);
				//actions = bp;
			}
			else if(caller instanceof FollowersPanel)
			{
				flp = (FollowersPanel)caller;
				if(flp.getContactsList().getSelectedIndex() == -1)
				{
					selected = false;
				}
				aMap = TwitzApp.getContext().getActionMap(flp.getClass(), flp);
				//actions = flp;
			}
		}
		JPopupMenu menu = new JPopupMenu(getResourceMap().getString("ACTIONS"));
		menu.setFocusable(false);
		menu.setLabel(getResourceMap().getString("ACTIONS"));

		JMenuItem item = new JMenuItem(getResourceMap().getString("REPORT_SPAM"));
		//TwitzApp.getContext().getActionMap(TwitzDesktopFrame.class, this);
		//item.setActionCommand(TwitterConstants.REPORT_SPAM+"");
		if(aMap != null) {
			//logger.debug("REPORT_SPAM");
			item.setAction(aMap.get("menuAction"));
		}
		item.setActionCommand("REPORT_SPAM");
		item.setIcon(getResourceMap().getIcon("icon.bomb"));
		item.setText(getResourceMap().getString("REPORT_SPAM"));
		if(blocked)
			item.setEnabled(blocked);
		else
			item.setEnabled(selected);
		item.setFocusable(false);
		menu.add(item);
		menu.addSeparator();

		JMenu sub = new JMenu(getResourceMap().getString("BLOCKING_USERS"));
		sub.setIcon(getResourceMap().getIcon("icon.stop"));
		sub.setFocusable(false);
		item = new JMenuItem(getResourceMap().getString("CREATE_BLOCK"));
		if(aMap != null) {
			item.setAction(aMap.get("menuAction"));
			//item.addActionListener(actions);
		}
		item.setActionCommand("CREATE_BLOCK");
		item.setIcon(getResourceMap().getIcon("icon.stop"));
		item.setText(getResourceMap().getString("CREATE_BLOCK"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("DESTROY_BLOCK"));
		if(aMap != null) {
			item.setAction(aMap.get("menuAction"));
			//item.addActionListener(actions);
		}
		item.setActionCommand("DESTROY_BLOCK");
		item.setText(getResourceMap().getString("DESTROY_BLOCK"));
		item.setIcon(getResourceMap().getIcon("icon.stop"));
		if(blocked)
			item.setEnabled(blocked);
		else
			item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);
		menu.add(sub);

		sub = new JMenu(getResourceMap().getString("FRIENDSHIPS"));
		sub.setIcon(getResourceMap().getIcon("icon.user"));
		sub.setFocusable(false);
		item = new JMenuItem(getResourceMap().getString("CREATE_FRIENDSHIP"));
		if(aMap != null) {
			item.setAction(aMap.get("menuAction"));
			//item.addActionListener(actions);
		}
		item.setActionCommand("CREATE_FRIENDSHIP");
		item.setIcon(getResourceMap().getIcon("icon.user_add"));
		item.setText(getResourceMap().getString("CREATE_FRIENDSHIP"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("DESTROY_FRIENDSHIP"));
		if(aMap != null) {
			item.setAction(aMap.get("menuAction"));
			//item.addActionListener(actions);
		}
		item.setActionCommand("DESTROY_FRIENDSHIP");
		item.setIcon(getResourceMap().getIcon("icon.user_delete"));
		item.setText(getResourceMap().getString("DESTROY_FRIENDSHIP"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("EXISTS_FRIENDSHIP"));
		if(aMap != null) {
			item.setAction(aMap.get("menuAction"));
			//item.addActionListener(actions);
		}
		item.setActionCommand("EXISTS_FRIENDSHIP");
		item.setIcon(getResourceMap().getIcon("icon.user_go"));
		item.setText(getResourceMap().getString("EXISTS_FRIENDSHIP"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("SHOW_FRIENDSHIP"));
		if(aMap != null) {
			item.setAction(aMap.get("menuAction"));
			//item.addActionListener(actions);
		}
		item.setActionCommand("SHOW_FRIENDSHIP");
		item.setIcon(getResourceMap().getIcon("icon.user_gray"));
		item.setText(getResourceMap().getString("SHOW_FRIENDSHIP"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);
		menu.add(sub);

//SENT_DIRECT_MESSAGES
//SEND_DIRECT_MESSAGE
		sub = new JMenu(getResourceMap().getString("DIRECT_MESSAGES"));
		sub.setIcon(getResourceMap().getIcon("icon.user_comment"));
		sub.setFocusable(false);
		item = new JMenuItem(getResourceMap().getString("SEND_DIRECT_MESSAGE"));
		if(aMap != null) {
			item.setAction(aMap.get("menuAction"));
			//item.addActionListener(actions);
		}
		item.setActionCommand("SEND_DIRECT_MESSAGE");
		item.setIcon(getResourceMap().getIcon("icon.user_comment"));
		item.setText(getResourceMap().getString("SEND_DIRECT_MESSAGE"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

//		item = new JMenuItem(getResourceMap().getString("SEND_DIRECT_MESSAGES"));
//		item.setActionCommand(TwitterConstants.SEND_DIRECT_MESSAGES+"");
//		if(cl != null) { 			item.addActionListener(cl); 		} else if(ulp != null) { 			item.addActionListener(ulp); 		} 		else if(tl != null) { 			item.addActionListener(tl); 		}
//		item.setIcon(getResourceMap().getIcon("icon.user_comment"));
//		item.setEnabled(selected);
//		sub.add(item);
		menu.add(sub);
//NEAR_BY_PLACES
//GEO_DETAILS

//		sub = new JMenu(getResourceMap().getString("LOCATION"));
//		sub.setIcon(getResourceMap().getIcon("icon.map"));
//		sub.setFocusable(false);
//		item = new JMenuItem(getResourceMap().getString("NEAR_BY_PLACES"));
//		if(aMap != null) {
//			item.setAction(aMap.get("menuAction"));
//			//item.addActionListener(actions);
//		}
//		item.setActionCommand("NEAR_BY_PLACES");
//		item.setIcon(getResourceMap().getIcon("icon.map"));
//		item.setText(getResourceMap().getString("NEAR_BY_PLACES"));
//		item.setEnabled(selected);
//		item.setFocusable(false);
//		sub.add(item);
//
//		item = new JMenuItem(getResourceMap().getString("GEO_DETAILS"));
//		if(aMap != null) {
//			item.setAction(aMap.get("menuAction"));
//			//item.addActionListener(actions);
//		}
//		item.setActionCommand("GEO_DETAILS");
//		//item.addActionListener(actions);
//		item.setIcon(getResourceMap().getIcon("icon.map_edit"));
//		item.setText(getResourceMap().getString("GEO_DETAILS"));
//		item.setEnabled(selected);
//		item.setFocusable(false);
//		sub.add(item);
//		menu.add(sub);

//ADD_LIST_MEMBER
//DELETE_LIST_MEMBER
//CHECK_LIST_MEMBERSHIP
		sub = new JMenu(getResourceMap().getString("USER_LISTS"));
		sub.setIcon(getResourceMap().getIcon("icon.group"));
		sub.setFocusable(false);
		item = new JMenuItem(getResourceMap().getString("ADD_LIST_MEMBER"));
		if(aMap != null) {
			item.setAction(aMap.get("menuAction"));
			//item.addActionListener(actions);
		}
		item.setActionCommand("ADD_LIST_MEMBER");
		//item.addActionListener(actions);
		item.setIcon(getResourceMap().getIcon("icon.group_add"));
		item.setText(getResourceMap().getString("ADD_LIST_MEMBER"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("DELETE_LIST_MEMBER"));
		if(aMap != null) {
			item.setAction(aMap.get("menuAction"));
			//item.addActionListener(actions);
		}
		item.setActionCommand("DELETE_LIST_MEMBER");
		item.setIcon(getResourceMap().getIcon("icon.group_delete"));
		item.setText(getResourceMap().getString("DELETE_LIST_MEMBER"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("CHECK_LIST_MEMBERSHIP"));
		if(aMap != null) {
			item.setAction(aMap.get("menuAction"));
			//item.addActionListener(actions);
		}
		item.setActionCommand("CHECK_LIST_MEMBERSHIP");
		item.setIcon(getResourceMap().getIcon("icon.group_gear"));
		item.setText(getResourceMap().getString("CHECK_LIST_MEMBERSHIP"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);
		menu.add(sub);

	//USER_TIMELINE
		item = new JMenuItem(getResourceMap().getString("USER_TIMELINE"));
		if(aMap != null) {
			item.setAction(aMap.get("menuAction"));
			//item.addActionListener(actions);
		}
		item.setActionCommand("USER_TIMELINE");
		item.setIcon(getResourceMap().getIcon("icon.timeline_marker"));
		item.setText(getResourceMap().getString("USER_TIMELINE"));
		item.setEnabled(selected);
		item.setFocusable(false);
		menu.add(item);

		//menu.setEnabled(selected);

		return menu;
	}//}}}


	private void setREPORT_SPAM(boolean state){ REPORT_SPAM=state; }
	private void setCREATE_BLOCK(boolean state){ CREATE_BLOCK=state; }
	private void setDESTROY_BLOCK(boolean state) { DESTROY_BLOCK=state; }
	private void setCREATE_FRIENDSHIP(boolean state) {CREATE_FRIENDSHIP=state;}
	private void setDESTROY_FRIENDSHIP(boolean state) {DESTROY_FRIENDSHIP=state;}
	private void setEXISTS_FRIENDSHIP(boolean state) {EXISTS_FRIENDSHIP=state;}
	private void setSHOW_FRIENDSHIP(boolean state) {SHOW_FRIENDSHIP=state;}
	private void setSEND_DIRECT_MESSAGE(boolean state) {SEND_DIRECT_MESSAGE=state;}
	private void setSEND_DIRECT_MESSAGES(boolean state) {SEND_DIRECT_MESSAGES=state;}
	private void setNEAR_BY_PLACES(boolean state) {NEAR_BY_PLACES=state;}
	private void setGEO_DETAILS(boolean state) {GEO_DETAILS=state;}
	private void setADD_LIST_MEMBER(boolean state) {ADD_LIST_MEMBER=state;}
	private void setDELETE_LIST_MEMBER(boolean state) {DELETE_LIST_MEMBER=state;}
	private void setCHECK_LIST_MEMBERSHIP(boolean state) {CHECK_LIST_MEMBERSHIP=state;}
	private void setUSER_TIMELINE(boolean state) {USER_TIMELINE=state;}

	public void propertyChange(PropertyChangeEvent evt)
	{
		String p = evt.getPropertyName();
		if(p.equals("REPORT_SPAM_MENU")) {
			this.setREPORT_SPAM(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("CREATE_BLOCK_MENU")) {
			this.setCREATE_BLOCK(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("DESTROY_BLOCK_MENU")) {
			this.setDESTROY_BLOCK(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("CREATE_FRIENDSHIP_MENU")) {
			this.setCREATE_FRIENDSHIP(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("DESTROY_FRIENDSHIP_MENU")) {
			this.setDESTROY_FRIENDSHIP(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("EXISTS_FRIENDSHIP_MENU")) {
			this.setEXISTS_FRIENDSHIP(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("SHOW_FRIENDSHIP_MENU")) {
			this.setSHOW_FRIENDSHIP(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("SEND_DIRECT_MESSAGE_MENU")) {
			this.setSEND_DIRECT_MESSAGE(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("SEND_DIRECT_MESSAGES_MENU")) {
			this.setSEND_DIRECT_MESSAGES(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("NEAR_BY_PLACES_MENU")) {
			this.setNEAR_BY_PLACES(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("GEO_DETAILS_MENU")) {
			this.setGEO_DETAILS(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("ADD_LIST_MEMBER_MENU")) {
			this.setADD_LIST_MEMBER(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("DELETE_LIST_MEMBER_MENU")) {
			this.setDELETE_LIST_MEMBER(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("CHECK_LIST_MEMBERSHIP_MENU")) {
			this.setCHECK_LIST_MEMBERSHIP(Boolean.parseBoolean((String)evt.getNewValue()));
		}
		else if(p.equals("USER_TIMELINE_MENU")) {
			this.setUSER_TIMELINE(Boolean.parseBoolean((String)evt.getNewValue()));
		}
	}

}
