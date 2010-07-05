/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui;

import java.awt.Component;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import twitter4j.User;
import twitter4j.UserList;
import twitz.TwitzMainView;


/**
 *
 * @author mistik1
 */
public class UserListMainPanel extends JPanel {
	javax.swing.GroupLayout layout;
	ParallelGroup vgroup;
	SequentialGroup hgroup;
	Map<String, UserListPanel> panels = Collections.synchronizedMap(new TreeMap<String, UserListPanel>());
	Map<String,UserList> userlists = Collections.synchronizedMap(new TreeMap<String, UserList>());
	
	public UserListMainPanel()
	{
		super();
		initLayout();
	}

	@Override
	public GroupLayout getLayout() {
		return layout;
	}

	/**
	 * This method is overridden and currently does nothing.
	 * All layout is handled internally and cannot be changed externally.
	 * If you want to modify the layout use <code>getLayout</code> to get
	 * the internal layout and add setting to that.
	 * @param layout
	 */
	@Override
	public void setLayout(java.awt.LayoutManager layout) {
		return;
	}

	private void initLayout() {//{{{
		layout = new javax.swing.GroupLayout(this);

        super.setLayout(layout);
		layout.setHonorsVisibility(true);
		//layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);

        ParallelGroup pg = layout.createParallelGroup(Alignment.LEADING);
        SequentialGroup sg = layout.createSequentialGroup();
		pg = pg.addGroup(sg);
		//add components to this one.
        vgroup = layout.createParallelGroup(Alignment.LEADING);
		sg.addGroup(vgroup);
        layout.setHorizontalGroup(pg);

		//Add out components to this one.
		hgroup = layout.createSequentialGroup();
		ParallelGroup pg1 = layout.createParallelGroup(Alignment.LEADING)
				.addGroup(hgroup);
		layout.setVerticalGroup(pg1);
	}//}}}

	public void addPanel(Component comp) {

//      vgroup.addComponent(comp, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
//		hgroup.addComponent(comp, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);

		vgroup.addComponent(comp, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE);
		hgroup.addComponent(comp, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
//		if(!userlists.isEmpty()){
//			Set<String> set = userlists.keySet();
//			Iterator iter = set.iterator();
//			UserListPanel[] components = new UserListPanel[set.size()+1];
//			int count = 0;
//			while(iter.hasNext()) {
//				components[count] = userlists.get(iter.next());
//				count++;
//			}
//			components[count] = comp;
//			layout.linkSize(SwingConstants.HORIZONTAL, components[components.length-1], comp);
//		}
	}

	public boolean removeUserList(String listname) {
		boolean rv = true;
		UserListPanel panel = panels.get(listname);
		if(panel != null) {
			getLayout().removeLayoutComponent(panel);
			panels.remove(panel);
			UserList list = userlists.get(listname);
			userlists.remove(list);
		}
		else
			rv = false;
		return rv;
	}

	public void removeAllUserList() {
		Set<String> set = userlists.keySet();
		Iterator iter = set.iterator();
		while(iter.hasNext()) {
			UserList list = userlists.get(iter.next());
			getLayout().removeLayoutComponent(panels.get(list.getName()));
		}
		panels.clear();
		userlists.clear();
	}

	/**
	 * @deprecated This method is used only for development testing Do NOT use in production
	 * @param users
	 * @param listName
	 * @return
	 */
	public UserListPanel addUserList(User[] users, String listName) {
		UserListPanel panel = new UserListPanel();
		panel.addUser(users);
		panel.setTitle(listName);
		//Lets make sure that the TwitzMainView is a TwitzEventListener for this and all panels
		panel.addTwitzListener(TwitzMainView.getInstance());
		addPanel(panel);
		panels.put(listName, panel);
		return panel;
		
	}

	/**
	 * @deprecated This method is used only for development testing Do NOT use in production
	 * @param listName
	 * @return
	 */
	public UserListPanel addUserList(String listName) {
		UserListPanel panel = new UserListPanel();
		panel.setTitle(listName);
		//Lets make sure that the TwitzMainView is a TwitzEventListener for this and all panels
		panel.addTwitzListener(TwitzMainView.getInstance());
		addPanel(panel);
		panels.put(listName, panel);
		return panel;
	}

	public UserListPanel addUserList(UserList list)
	{
		UserListPanel panel = new UserListPanel();
		panel.setTitle(list.getName());
		//Lets make sure that the TwitzMainView is a TwitzEventListener for this and all panels
		//We add it first here because setUserList causes a TwitzEvent to be fired to get the list users
		panel.addTwitzListener(TwitzMainView.getInstance());
		
		panel.setUserList(list);
		
		addPanel(panel);
		userlists.put(list.getName(), list);
		panels.put(list.getName(), panel);
		return panel;
	}
}
