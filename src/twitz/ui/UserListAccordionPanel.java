/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu.Separator;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import twitter4j.PagableResponseList;
import twitter4j.User;
import twitter4j.UserList;
import twitz.TwitzMainView;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;
import twitz.util.SettingsManager;
import twitz.util.TwitzSessionManager;

/**
 *
 * @author mistik1
 */
public class UserListAccordionPanel extends JLayeredPane implements ActionListener, MouseListener {

	private JPanel top = new JPanel(new GridLayout(1, 1));
	private JPanel bottom = new JPanel(new GridLayout(1, 1));
	private JPanel display = new JPanel(new BorderLayout());
	private Map<String, UserListBox> panels = new LinkedHashMap<String, UserListBox>();

	private JToolBar toolbar = new JToolBar();
	private javax.swing.JButton btnPrev = new javax.swing.JButton();
    private Separator jSeparator3 = new Separator();
	private Separator separatorRight = new Separator();
    private javax.swing.JButton btnNext = new javax.swing.JButton();
	private javax.swing.JButton btnAddList = new javax.swing.JButton();
	private javax.swing.JButton btnDeleteList = new javax.swing.JButton();
	private javax.swing.JButton btnReload = new javax.swing.JButton();
    private javax.swing.JScrollPane userListPane = new javax.swing.JScrollPane();
    private org.jdesktop.application.ResourceMap resourceMap = twitz.TwitzApp.getContext().getResourceMap(twitz.ui.UserListBox.class);
	private javax.swing.ActionMap actionMap = twitz.TwitzApp.getContext().getActionMap(UserListAccordionPanel.class, this);
	
	private Map<String, UserList> userlists = new LinkedHashMap<String, UserList>();
	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private TwitzSessionManager session = TwitzSessionManager.getInstance();
	public static final String SESSION_PROPERTY = "sessionName";
	private String sessionName;
	private SettingsManager config;
	private TwitzMainView view;

	private long nextPage = -1;
	private long prevPage = -1;
	private long currentPage = -1;
	private boolean firstrun = true;
	private int visibleList = 0;
	private UserListBox currentPanel = null;
	
	public UserListAccordionPanel()
	{
		this.setLayout(new BorderLayout());
		
		initDefaults();
	}

	private void initDefaults()
	{
		//display.setLayout(new BorderLayout());
		display.add(top, BorderLayout.NORTH);
		display.add(bottom, BorderLayout.SOUTH);
		setupToolBar();
		add(toolbar, BorderLayout.NORTH);

		btnNext.setEnabled(false);
		btnPrev.setEnabled(false);

        userListPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        userListPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        userListPane.setAutoscrolls(true);
        userListPane.setName("userListPane"); // NOI18N
        userListPane.setViewportView(display);
		javax.swing.JScrollBar bar = userListPane.getVerticalScrollBar();
		bar.setUnitIncrement(50);
		TwitzMainView.fixJScrollPaneBarsSize(userListPane);
		add(userListPane, BorderLayout.CENTER);
	}

	private void render() {
		// Compute how many panels we are going to have where
		int totalPanels = this.panels.size();
		int topPanels = this.visibleList + 1;
		int bottomPanels = totalPanels - topPanels;

		// Get an iterator to walk through out panels
		Iterator<String> iter = this.panels.keySet().iterator();

		// Render the top panels: remove all components, reset the GridLayout to
		// hold to correct number of panels, add the panels, and "validate" it to
		// cause it to re-layout its components
		this.top.removeAll();
		GridLayout topLayout = (GridLayout) this.top.getLayout();
		topLayout.setRows(topPanels);
		UserListBox listPanel = null;
		for (int i = 0; i < topPanels; i++) {
			String barName = (String) iter.next();
			listPanel = (UserListBox) this.panels.get(barName);
			this.top.add(listPanel.getToolBar());
		}
		this.top.validate();

		// Render the center component: remove the current component (if there
		// is one) and then put the visible component in the center of this
		// panel
		if (this.currentPanel != null) {
			this.display.remove(this.currentPanel);
		}
		this.currentPanel = listPanel;//.getComponent();
		this.display.add(currentPanel, BorderLayout.CENTER);

		// Render the bottom panels: remove all components, reset the GridLayout
		// to
		// hold to correct number of panels, add the panels, and "validate" it to
		// cause it to re-layout its components
		this.bottom.removeAll();
		GridLayout bottomLayout = (GridLayout) this.bottom.getLayout();
		bottomLayout.setRows(bottomPanels);
		for (int i = 0; i < bottomPanels; i++) {
			String barName = (String) iter.next();
			listPanel = (UserListBox) this.panels.get(barName);
			this.bottom.add(listPanel.getToolBar());
		}
		this.bottom.validate();

		// Validate all of our components: cause this container to re-layout its
		// subcomponents
		validate();
	}

	//Actions
	@Action
	public void getNext()//{{{
	{
		this.currentPage = nextPage;
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
		this.currentPage = prevPage;
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", this);
		ArrayList args = new ArrayList();
		args.add(config.getString("twitter_id"));//screenName
		args.add(prevPage);
		map.put("arguments", args);
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.USER_LISTS, new java.util.Date().getTime(), map));
	}//}}}

	@Action
	public void addNewList()
	{
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", this);
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.CREATE_USER_LIST, new java.util.Date().getTime(), map));
	}

	@Action
	public void deleteList()
	{
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", this);
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.DELETE_USER_LIST, new java.util.Date().getTime(), map));
	}

	@Action
	public void reload()
	{
		update(true);
	}

	private void setupToolBar()//{{{
	{
        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.setName("listToolbar"); // NOI18N
        toolbar.setPreferredSize(new java.awt.Dimension(100, 18));

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

		btnReload.setAction(actionMap.get("reload"));
		btnReload.setText(resourceMap.getString("btnReload.text"));
		btnReload.setIcon(resourceMap.getIcon("btnReload.icon"));
		btnReload.setToolTipText(resourceMap.getString("btnReload.toolTipText"));
		btnReload.setMargin(new java.awt.Insets(2, 2, 2, 2));
		toolbar.add(btnReload);

		btnAddList.setAction(actionMap.get("addNewList"));
		btnAddList.setText(resourceMap.getString("btnAddList.text"));
		btnAddList.setIcon(resourceMap.getIcon("btnAddList.icon"));
		btnAddList.setToolTipText(resourceMap.getString("btnAddList.toolTipText"));
		btnAddList.setMargin(new java.awt.Insets(2, 2, 2, 2));
		toolbar.add(btnAddList);

		btnDeleteList.setAction(actionMap.get("deleteList"));
		btnDeleteList.setText(resourceMap.getString("btnDeleteList.text"));
		btnDeleteList.setIcon(resourceMap.getIcon("btnDeleteList.icon"));
		btnDeleteList.setToolTipText(resourceMap.getString("btnDeleteList.toolTipText"));
		btnDeleteList.setMargin(new java.awt.Insets(2, 2, 2, 2));
		toolbar.add(btnDeleteList);

		separatorRight.setMaximumSize(new java.awt.Dimension(1000, 10));
		separatorRight.setName("separatorRight"); // NOI18N
		toolbar.add(separatorRight);

        btnNext.setIcon(resourceMap.getIcon("btnNext.icon")); // NOI18N
        btnNext.setToolTipText(resourceMap.getString("btnNext.toolTipText")); // NOI18N
        btnNext.setFocusable(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setText(resourceMap.getString("btnNext.label")); // NOI18N
        btnNext.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnNext.setName("btnNext"); // NOI18N
        btnNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		toolbar.add(btnNext);
	}//}}}

	public void setSessionName(String name)
	{
		String old = this.sessionName;
		this.sessionName = name;
		config = session.getSettingsManagerForSession(sessionName);
		view = session.getTwitMainViewForSession(sessionName);
		firePropertyChange(SESSION_PROPERTY, old, name);
	}

	public String getSessionName()
	{
		return this.sessionName;
	}

	public boolean removeUserList(String listname) {//{{{
		boolean rv = true;
		UserListBox panel = panels.get(listname);
		if(panel != null) {
			//getLayout().removeLayoutComponent(panel);
			panels.remove(panel);
			UserList list = userlists.get(listname);
			userlists.remove(list);
			render();
		}
		else
			rv = false;
		return rv;
	}//}}}

	public void removeAllUserList() {//{{{
//		Set<String> set = userlists.keySet();
//		Iterator iter = set.iterator();
//		while(iter.hasNext()) {
//			UserList list = userlists.get(iter.next());
//			//getLayout().removeLayoutComponent(panels.get(list.getName()));
//		}
		panels.clear();
		userlists.clear();
		render();
	}//}}}

	/**
	 * @deprecated This method is used only for development testing Do NOT use in production
	 * @param users
	 * @param listName
	 * @return
	 */
	public UserListBox addUserList(User[] users, String listName) {//{{{
		UserListBox panel = new UserListBox(this);
		panel.setSessionName(sessionName);
		panel.addUser(users);
		panel.setTitle(listName);
		//Lets make sure that the TwitzMainView is a TwitzEventListener for this and all panels
		panel.addTwitzListener(view);
//		panel.addPropertyChangeListener("collapsed", this);
		panels.put(listName, panel);
		//panel.setCollapsed(true);
		return panel;

	}//}}}

	/**
	 * @deprecated This method is used only for development testing Do NOT use in production
	 * @param listName
	 * @return
	 */
	public UserListBox addUserList(String listName) {//{{{
		UserListBox panel = new UserListBox(this);
		panel.setSessionName(sessionName);
		panel.setTitle(listName);
		//Lets make sure that the TwitzMainView is a TwitzEventListener for this and all panels
		panel.addTwitzListener(view);
//		panel.addPropertyChangeListener("collapsed", this);
		panels.put(listName, panel);
		//panel.setCollapsed(true);
		return panel;
	}//}}}

	public UserListBox addUserList(UserList list)//{{{
	{
		UserListBox panel = null;
		try
		{
			panel = new UserListBox(this);
			panel.setSessionName(sessionName);
			panel.setTitle(list.getName());
			//Lets make sure that the TwitzMainView is a TwitzEventListener for this and all panels
			//We add it first here because setUserList causes a TwitzEvent to be fired to get the list users
			panel.addTwitzListener(view);
//			panel.addPropertyChangeListener("collapsed", this);

			panel.setUserList(list);
			userlists.put(list.getName(), list);
			panels.put(list.getName(), panel);
			render();
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

	public Map<String, UserList> getUserLists()
	{
		return userlists;
	}

	public void update(boolean force)
	{
		logger.debug("update() run force = "+force);
		if (firstrun && view.isConnected() || force)
		{
			//Load userlists view
			Map map = Collections.synchronizedMap(new TreeMap());
			map.put("async", true);
			map.put("caller", this);
			ArrayList args = new ArrayList();
			args.add(config.getString("twitter_id"));//screenName
			args.add(currentPage);
			map.put("arguments", args);
			fireTwitzEvent(new TwitzEvent(this, TwitzEventType.USER_LISTS, new java.util.Date().getTime(), map));
			firstrun = false;
		}
	}

	public int getVisibleList()
	{
		
		return this.visibleList;
	}

	public void setVisibleList(int list)
	{
		if(list >= 0 && list < panels.size())
		{
			this.visibleList = list;
			render();
		}
	}

	public void setVisibleList(String listname)
	{
		if(listname != null && panels.containsKey(listname))
		{
			Set<String> set = panels.keySet();
			Iterator<String> iter = set.iterator();
			int position = -1, count = 0;
			while(iter.hasNext())
			{
				String name = iter.next();
				if(listname.equals(name))
				{
					position = count;
					break;
				}
			}
			setVisibleList(position);
		}
	}

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

	public void actionPerformed(ActionEvent e)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void mouseClicked(MouseEvent e)
	{
		int current = 0;
		for (Iterator<String> it = this.panels.keySet().iterator(); it.hasNext();)
		{
			String name = it.next();
			javax.swing.JComponent comp = (JComponent) e.getSource();
			String cname = comp.getName();
			logger.debug("Box name = "+name+" Component name = "+cname);
			if(name.equals(comp.getName()))
			{
				this.setVisibleList(current);
				UserListBox box = panels.get(name);
				box.requestListStatus();
				break;
			}
			current++;
		}
	}

	public void mousePressed(MouseEvent e)
	{
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void mouseReleased(MouseEvent e)
	{
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void mouseEntered(MouseEvent e)
	{
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void mouseExited(MouseEvent e)
	{
		//throw new UnsupportedOperationException("Not supported yet.");
	}
}
