/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui;

import java.awt.Component;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JToolBar.Separator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.PagableResponseList;
import org.jdesktop.application.Action;
import org.apache.log4j.Logger;
import twitz.TwitzMainView;
import twitz.events.*;
import twitz.util.SettingsManager;
import twitz.util.TwitzSessionManager;


/**
 *
 * @author mistik1
 */
public class UserListMainPanel extends JPanel implements TwitzEventModel, PropertyChangeListener {

	private BorderLayout blayout = new BorderLayout();
	/**
	 * The panel used to contain the UserListPanel used to display userlists
	 */
	private JPanel listPanel = new JPanel();
	private JToolBar toolbar = new JToolBar();
	private javax.swing.JButton btnPrev = new javax.swing.JButton();
    private Separator jSeparator3 = new Separator();
    private javax.swing.JButton btnNext = new javax.swing.JButton();
    private javax.swing.JScrollPane userListPane = new javax.swing.JScrollPane();
    private org.jdesktop.application.ResourceMap resourceMap = twitz.TwitzApp.getContext().getResourceMap(twitz.ui.UserListPanel.class);
	private javax.swing.ActionMap actionMap = twitz.TwitzApp.getContext().getActionMap(UserListMainPanel.class, this);
	private SettingsManager config = SettingsManager.getInstance();
	private javax.swing.GroupLayout layout;
	private ParallelGroup vgroup;
	private SequentialGroup hgroup;
	private Map<String, UserListPanel> panels = Collections.synchronizedMap(new TreeMap<String, UserListPanel>());
	private Map<String,UserList> userlists = Collections.synchronizedMap(new TreeMap<String, UserList>());
	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private Logger logger = Logger.getLogger(this.getClass().getName());
	public static final String SESSION_PROPERTY = "sessionName";
	private String sessionName;
	private TwitzMainView view;

	private long nextPage = -1;
	private long prevPage = -1;
	
	public UserListMainPanel()
	{
		super();
		initLayout();
	}

	public void setSessionName(String name)
	{
		String old = this.sessionName;
		this.sessionName = name;
		//config = TwitzSessionManager.getInstance().getSettingsManagerForSession(sessionName);
		view = TwitzSessionManager.getInstance().getTwitMainViewForSession(sessionName);
		//firePropertyChange(SESSION_PROPERTY, old, name);
	}

	public String getSessionName()
	{
		return this.sessionName;
	}

	@Override
	public BorderLayout getLayout() {
		return blayout;
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

	public void collapsePanels(UserListPanel ulp, boolean collapsed)
	{
		if(!collapsed)
		{
			String listname = ulp.getUserList().getName(); //TODO Null check (should nevel be null as a userlistpanel should not exist with a userlist)
			java.util.Set<String> set = panels.keySet();
			java.util.Iterator<String> iter = set.iterator();
			while(iter.hasNext())
			{
				String key = iter.next();
				UserListPanel p = panels.get(key);
				if(key.equals(listname))
				{
					//p.setCollapsed(false);
					continue;
				}
				p.setCollapsed(true);
			}
		}
	}

	private void initLayout() {//{{{
		layout = new javax.swing.GroupLayout(listPanel);

        super.setLayout(blayout);
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
		//Add the listPanel to our layout
		listPanel.setLayout(layout);
		add(userListPane, BorderLayout.CENTER);
		setupToolBar();
		add(toolbar, BorderLayout.NORTH);
		btnNext.setEnabled(false);
		btnPrev.setEnabled(false);

        userListPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        userListPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        userListPane.setAutoscrolls(true);
        userListPane.setName("userListPane"); // NOI18N
        userListPane.setViewportView(listPanel);
		javax.swing.JScrollBar bar = userListPane.getVerticalScrollBar();
		bar.setUnitIncrement(50);
		TwitzMainView.fixJScrollPaneBarsSize(userListPane);

	}//}}}

	private void setupToolBar()//{{{
	{
        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.setName("listToolbar"); // NOI18N
        toolbar.setPreferredSize(new java.awt.Dimension(100, 15));

        btnPrev.setIcon(resourceMap.getIcon("btnPrev.icon")); // NOI18N
        btnPrev.setText(resourceMap.getString("btnPrev.text")); // NOI18N
        btnPrev.setToolTipText(resourceMap.getString("btnPrev.toolTipText")); // NOI18N
        btnPrev.setFocusable(false);
        btnPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrev.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnPrev.setName("btnPrev"); // NOI18N
        btnPrev.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar.add(btnPrev);

        jSeparator3.setMaximumSize(new java.awt.Dimension(1000, 10));
        jSeparator3.setName("jSeparator3"); // NOI18N
        toolbar.add(jSeparator3);

        btnNext.setIcon(resourceMap.getIcon("btnNext.icon")); // NOI18N
        btnNext.setToolTipText(resourceMap.getString("btnNext.toolTipText")); // NOI18N
        btnNext.setFocusable(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setLabel(resourceMap.getString("btnNext.label")); // NOI18N
        btnNext.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnNext.setName("btnNext"); // NOI18N
        btnNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		toolbar.add(btnNext);
	}//}}}

	@Action
	public void getNext()//{{{
	{
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", this);
		ArrayList args = new ArrayList();
		args.add(config.getString("twitter_id"));//screenName
		args.add(nextPage);
		map.put("arguments", args);
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.USER_LISTS, new java.util.Date().getTime(), map));
	}//}}}

	@Action
	public void getPrevious()//{{{
	{
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", this);
		ArrayList args = new ArrayList();
		args.add(config.getString("twitter_id"));//screenName
		args.add(prevPage);
		map.put("arguments", args);
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.USER_LISTS, new java.util.Date().getTime(), map));
	}//}}}

	public void addPanel(Component comp) //{{{
	{
		vgroup.addComponent(comp, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE);
		hgroup.addComponent(comp, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
	}//}}}

	public boolean removeUserList(String listname) {//{{{
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
	}//}}}

	public void removeAllUserList() {//{{{
		Set<String> set = userlists.keySet();
		Iterator iter = set.iterator();
		while(iter.hasNext()) {
			UserList list = userlists.get(iter.next());
			getLayout().removeLayoutComponent(panels.get(list.getName()));
		}
		panels.clear();
		userlists.clear();
	}//}}}

	/**
	 * @deprecated This method is used only for development testing Do NOT use in production
	 * @param users
	 * @param listName
	 * @return
	 */
	public UserListPanel addUserList(User[] users, String listName) {//{{{
		UserListPanel panel = new UserListPanel();
		panel.setSessionName(sessionName);
		panel.addUser(users);
		panel.setTitle(listName);
		//Lets make sure that the TwitzMainView is a TwitzEventListener for this and all panels
		panel.addTwitzListener(view);
		panel.addPropertyChangeListener("collapsed", this);
		addPanel(panel);
		panels.put(listName, panel);
		//panel.setCollapsed(true);
		return panel;
		
	}//}}}

	/**
	 * @deprecated This method is used only for development testing Do NOT use in production
	 * @param listName
	 * @return
	 */
	public UserListPanel addUserList(String listName) {//{{{
		UserListPanel panel = new UserListPanel();
		panel.setSessionName(sessionName);
		panel.setTitle(listName);
		//Lets make sure that the TwitzMainView is a TwitzEventListener for this and all panels
		panel.addTwitzListener(view);
		panel.addPropertyChangeListener("collapsed", this);
		addPanel(panel);
		panels.put(listName, panel);
		//panel.setCollapsed(true);
		return panel;
	}//}}}

	public UserListPanel addUserList(UserList list)//{{{
	{
		UserListPanel panel = null;
		try
		{
			panel = new UserListPanel();
			panel.setSessionName(sessionName);
			panel.setTitle(list.getName());
			//Lets make sure that the TwitzMainView is a TwitzEventListener for this and all panels
			//We add it first here because setUserList causes a TwitzEvent to be fired to get the list users
			panel.addTwitzListener(view);
			panel.addPropertyChangeListener("collapsed", this);
			
			panel.setUserList(list);
			
			addPanel(panel);
			userlists.put(list.getName(), list);
			panels.put(list.getName(), panel);
		}
		catch(Exception e)
		{
			logger.error("Error while adding UserListPanel", e);
		}
		//panel.setCollapsed(true);
		return panel;
	}//}}}

	public boolean  isUserList(Object o)
	{
		return (o instanceof UserList);
	}

	public void addUserList(final PagableResponseList userLists)//{{{
	{
		if(userLists != null)
		{
			//Clear out the current lists
			try
			{
				SwingUtilities.invokeAndWait( new Runnable(){public void run() {removeAllUserList();}});
			}
			catch(Exception ie)
			{
				logger.error(ie);
			}

			SwingWorker<List<UserList>, UserList> worker = new SwingWorker<List<UserList>, UserList>()
			{
				int total = -1, count = 1;
				@Override
				public List<UserList> doInBackground()
				{
					total = userLists.size();
					firePropertyChange("started", null, "processing UserLists");
					for(Object o : userLists)
					{
						if(isUserList(o))
						{
							publish((UserList)o);
						}
					}
					return null;
				}

				@Override
				protected void done()
				{
					btnNext.setEnabled(userLists.hasNext());
					btnPrev.setEnabled(userLists.hasPrevious());
				}

				@Override
				protected void process(List<UserList> part)
				{
					for(UserList u : part)
					{
						firePropertyChange("message", null, String.format("Processing %d of %d UserLists. Please wait...", count, total));
						addUserList(u);
						count++;
					}
				}
			};
			worker.addPropertyChangeListener(view.getStatusListener());
			worker.execute();
			nextPage = userLists.getNextCursor();
			prevPage = userLists.getPreviousCursor();
		}
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

	public void propertyChange(PropertyChangeEvent evt)
	{
		if("collapsed".equals(evt.getPropertyName()))
		{
			boolean collapsed = (Boolean)evt.getNewValue();
			UserListPanel p = (UserListPanel)evt.getSource();
			this.collapsePanels(p, collapsed);
		}
	}

}
