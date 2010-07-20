/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * UserListPanel.java
 *
 * Created on Jun 17, 2010, 12:08:22 PM
 */

package twitz.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.swingx.JXCollapsiblePane;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserList;
import twitz.TwitzApp;
import twitz.TwitzMainView;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;
import twitz.ui.models.ContactsListModel;
import twitz.ui.renderers.ContactsRenderer;


/**
 *
 * @author mistik1
 */
public class UserListPanel extends javax.swing.JPanel implements MouseListener, FocusListener, 
		TwitzEventModel, ActionListener
{
    /** Creates new form UserListPanel */
	@SuppressWarnings("LeakingThisInConstructor")
    public UserListPanel() {
		resourceMap = TwitzApp.getContext().getResourceMap(UserListPanel.class);
		actionMap = TwitzApp.getContext().getActionMap(UserListPanel.class, this);
        initComponents();
		initDefaults();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")//{{{
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnCollapse = new javax.swing.JButton();
        toolbar = new javax.swing.JToolBar();
        listName = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnUserAdd = new javax.swing.JButton();
        btnUserDelete = new javax.swing.JButton();
        listPane = new javax.swing.JScrollPane();
        contactsList1 = new twitz.ui.ContactsList();
        pagingToolBar = new javax.swing.JToolBar();
        btnPrev = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnNext = new javax.swing.JButton();

        jSeparator1.setName("jSeparator1"); // NOI18N

        btnCollapse.setAction(actionMap.get("toggleCollapsed")); // NOI18N
        btnCollapse.setIcon(resourceMap.getIcon("btnCollapse.icon")); // NOI18N
        btnCollapse.setText(resourceMap.getString("btnCollapse.text")); // NOI18N
        btnCollapse.setToolTipText(resourceMap.getString("btnCollapse.toolTipText")); // NOI18N
        btnCollapse.setFocusable(false);
        btnCollapse.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnCollapse.setName("btnCollapse"); // NOI18N
        btnCollapse.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setName("UserListPanel"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.setName("toolbar"); // NOI18N
        toolbar.setPreferredSize(new java.awt.Dimension(100, 22));

        listName.setText(resourceMap.getString("listName.text")); // NOI18N
        listName.setToolTipText(listName.getText());
        listName.setName("listName"); // NOI18N
        toolbar.add(listName);

        jSeparator2.setName("jSeparator2"); // NOI18N
        toolbar.add(jSeparator2);

        btnUserAdd.setAction(actionMap.get("addListUser")); // NOI18N
        btnUserAdd.setIcon(resourceMap.getIcon("btnUserAdd.icon")); // NOI18N
        btnUserAdd.setText(resourceMap.getString("btnUserAdd.text")); // NOI18N
        btnUserAdd.setToolTipText(resourceMap.getString("btnUserAdd.toolTipText")); // NOI18N
        btnUserAdd.setFocusable(false);
        btnUserAdd.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnUserAdd.setIconTextGap(0);
        btnUserAdd.setName("btnUserAdd"); // NOI18N
        toolbar.add(btnUserAdd);

        btnUserDelete.setAction(actionMap.get("deleteListUser")); // NOI18N
        btnUserDelete.setIcon(resourceMap.getIcon("btnUserDelete.icon")); // NOI18N
        btnUserDelete.setToolTipText(resourceMap.getString("btnUserDelete.toolTipText")); // NOI18N
        btnUserDelete.setFocusable(false);
        btnUserDelete.setHideActionText(true);
        btnUserDelete.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnUserDelete.setIconTextGap(0);
        btnUserDelete.setName("btnUserDelete"); // NOI18N
        toolbar.add(btnUserDelete);

        add(toolbar, java.awt.BorderLayout.NORTH);

        listPane.setName("listPane"); // NOI18N

        contactsList1.setModel(new ContactsListModel());
        contactsList1.setCellRenderer(new ContactsRenderer());
        contactsList1.setInUserList(true);
        contactsList1.setName("contactsList1"); // NOI18N
        listPane.setViewportView(contactsList1);

        add(listPane, java.awt.BorderLayout.CENTER);

        pagingToolBar.setFloatable(false);
        pagingToolBar.setRollover(true);
        pagingToolBar.setName("pagingToolBar"); // NOI18N
        pagingToolBar.setPreferredSize(new java.awt.Dimension(100, 15));

        btnPrev.setIcon(resourceMap.getIcon("btnPrev.icon")); // NOI18N
        btnPrev.setText(resourceMap.getString("btnPrev.text")); // NOI18N
        btnPrev.setToolTipText(resourceMap.getString("btnPrev.toolTipText")); // NOI18N
        btnPrev.setFocusable(false);
        btnPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrev.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnPrev.setName("btnPrev"); // NOI18N
        btnPrev.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pagingToolBar.add(btnPrev);

        jSeparator3.setMaximumSize(new java.awt.Dimension(1000, 10));
        jSeparator3.setName("jSeparator3"); // NOI18N
        pagingToolBar.add(jSeparator3);

        btnNext.setIcon(resourceMap.getIcon("btnNext.icon")); // NOI18N
        btnNext.setToolTipText(resourceMap.getString("btnNext.toolTipText")); // NOI18N
        btnNext.setFocusable(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setLabel(resourceMap.getString("btnNext.label")); // NOI18N
        btnNext.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnNext.setName("btnNext"); // NOI18N
        btnNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pagingToolBar.add(btnNext);

        add(pagingToolBar, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
	//}}}

	//Private methods
	//
	private void requestListStatus()//{{{
	{
		if(TwitzMainView.getInstance().isConnected())
		{
			Map map = Collections.synchronizedMap(new TreeMap());
			map.put("async", true);
			map.put("caller", this);
			ArrayList args = new ArrayList();
			args.add(list.getUser().getScreenName());
			args.add(list.getId());
			Paging pager = new Paging();
			pager.setPage(1); //FIXME: need to properly support the paging with a next/prev 
			args.add(pager);
			map.put("arguments", args);
			fireTwitzEvent(new TwitzEvent(this, TwitzEventType.USER_LIST_STATUSES, new java.util.Date().getTime(), map));
		}
	}//}}}

	private void initDefaults() {//{{{
		ListSelectionListener lsl = new ListSelectionListener(){//{{{

			public void valueChanged(ListSelectionEvent e)
			{
				btnUserDelete.setEnabled(!((ContactsList)e.getSource()).isSelectionEmpty());
			}
		};//}}}
		contactsList1.addListSelectionListener(lsl);
		final ContactsListModel mod = contactsList1.getModel();
		mod.addListDataListener(new ListDataListener() {//{{{

			public void intervalAdded(ListDataEvent e)
			{
				setPreferredSize(calcSize(e));
				revalidate();
			}

			public void intervalRemoved(ListDataEvent e)
			{
				setPreferredSize(calcSize(e));
				revalidate();
			}

			public void contentsChanged(ListDataEvent e)
			{
				//System.out.println("Inside contentsChanged");
			}

			private Dimension calcSize(ListDataEvent e) {
				int height = 50;
				int count = mod.getSize();
				int newHeight = (count*height);
				Dimension dim = null;
				if(e.getType() == ListDataEvent.INTERVAL_ADDED) {
					dim = new Dimension(50, newHeight < MAX_HEIGHT ? newHeight : MAX_HEIGHT);
				}
				else if(e.getType() == ListDataEvent.INTERVAL_REMOVED) {
					dim = new Dimension(50, newHeight >= MIN_HEIGHT ? newHeight : MIN_HEIGHT);
				}
				//logger.log(Level.INFO, "New Dimension for panel: "+dim.toString());
				return dim;
			}
		});//}}}
		MouseListener toolbarListener = new MouseAdapter() {//{{{
			@Override
			public void mouseClicked(MouseEvent evt) {
//				requestFocusInWindow();
				if(evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
					toggleCollapsed();
				}
				if(!isFocusOwner())
					requestFocusInWindow();
			}
		};//}}}
		toolbar.addMouseListener(toolbarListener);
		listName.addMouseListener(toolbarListener);
		contactsList1.addMouseListener(this);
		//addTwitzListener(TwitzMainView.getInstance());
		setFocusable(true);
		addMouseListener(this);
		addFocusListener(this);
		//Store the default border set by the user.
		defaultBorder = getBorder();
		selectedBorder = BorderFactory.createLoweredBevelBorder();
        contactsList1.setFocusable(false);
		jSeparator2.setPreferredSize(new Dimension(1000,20));
		listName.setMaximumSize(new Dimension(40,listName.getPreferredSize().height));
		listName.setFont(new Font("Arial", Font.BOLD, 10));
		MouseListener ml = new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				toggleCollapsed();
			}
		};
		listName.addMouseListener(ml);
		jSeparator2.addMouseListener(ml);

		twitz.TwitzMainView.fixJScrollPaneBarsSize(listPane);

		btnPrev.setEnabled(false);
		btnNext.setEnabled(false);
		//setCollapsed(true);
	//	toggle = collapsiblePane.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION);
	//	toggle.putValue(JXCollapsiblePane.COLLAPSE_ICON, resourceMap.getIcon("btnCollapse.down.icon"));
	//	toggle.putValue(JXCollapsiblePane.EXPAND_ICON, resourceMap.getIcon("btnCollapse.icon"));
	//	btnCollapse.setAction(toggle);
	//	btnCollapse.setText("");
	}//}}}

	@Action
	public void toggleCollapsed()
	{
		setCollapsed(!collapsed);
		if(!isFocusOwner())
			requestFocusInWindow();
	}

	@Action
	public void addListUser() {
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("listname", list.getName());
		firePropertyChange("addListUser", map, null);
	}

	@Action
	public void deleteListUser() {//{{{
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", this);
		ArrayList args = new ArrayList();
		User toBeDeleted = contactsList1.getSelectedValue();
		args.add(list.getId());
		args.add(toBeDeleted.getId());
		map.put("arguments", args);
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.DELETE_LIST_MEMBER, new java.util.Date().getTime(), map));
		firePropertyChange("deleteListUser", map, null);
	}//}}}

	@Action
	public void getNext()//{{{
	{
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("caller", this);
		map.put("async", true);
		User[] selections = getContactsList().getSelectedValues();
		map.put("selections", selections);
		ArrayList args = new ArrayList();
		args.add(list.getUser().getScreenName());
		args.add(list.getId());
		args.add(nextPage);
		map.put("arguments", args);
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.LIST_MEMBERS, new java.util.Date().getTime(), map));
	}//}}}

	@Action
	public void getPrevious()//{{{
	{
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("caller", this);
		map.put("async", true);
		ArrayList args = new ArrayList();
		args.add(list.getUser().getScreenName());
		args.add(list.getId());
		args.add(prevPage);
		map.put("arguments", args);
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.LIST_MEMBERS, new java.util.Date().getTime(), map));
	}//}}}

	public void setCollapsed(boolean c) {//{{{
		boolean old = collapsed;
		if(collapsed && c) {
			return;
		}
		else if(collapsed && !c) {
			listPane.setVisible(true);
			contactsList1.setVisible(true);
			contactsList1.revalidate();
			setPreferredSize(new Dimension(50, boxHeight));
			btnCollapse.setIcon(resourceMap.getIcon("btnCollapse.icon"));
			collapsed = false;
			requestListStatus();
			setBorder(selectedBorder);
			firePropertyChange("collapsed", old, collapsed);
		}
		else if(!collapsed && c) {
			boxHeight = getHeight();
			listPane.setVisible(false);
			contactsList1.setVisible(false);
			setPreferredSize(new Dimension(50, 24));
			btnCollapse.setIcon(resourceMap.getIcon("btnCollapse.down.icon"));
			collapsed = true;
			setBorder(defaultBorder);
			firePropertyChange("collapsed", old, collapsed);
		}
		//parent.collapsePanels(list.getName(), collapsed);
		this.revalidate();
		repaint();
	}//}}}

	public boolean isCollapsed() {
		return collapsed;
	}

	public ContactsList getContactsList() {
		return contactsList1;
	}

	public void setTitle(String title) {//{{{
		if (title.length() > 9)
		{
			listName.setText(title.substring(0, 9));
		}
		else
		{
			listName.setText(title);
		}
		listName.setToolTipText(title);
	}//}}}

	public String getTitle() {
		return listName.getText();
	}

	public void addUser(User[] users) {
		contactsList1.addUser(users);
	}
	
	public void addUser(User user) {
		contactsList1.addUser(user);
	}
	
	public void addUser(@SuppressWarnings("UseOfObsoleteCollectionType") Vector<User> user) {
		contactsList1.addUser(user);
	}

	public void setUserList(UserList userList)//{{{
	{
		if(userList != null)
		{
			this.list = userList;
			if(twitz.TwitzMainView.getInstance().isConnected())//if this is false we are in offline testing mode
			{
				Map map = Collections.synchronizedMap(new TreeMap());
				map.put("async", true);
				map.put("caller", this);
				ArrayList args = new ArrayList();
				args.add(list.getUser().getScreenName());
				args.add(list.getId());
				args.add(-1);
				map.put("arguments", args);
				fireTwitzEvent(new TwitzEvent(this, TwitzEventType.LIST_MEMBERS, new java.util.Date().getTime(), map));
			}
		}
	}//}}}

	public UserList getUserList()
	{
		return list;
	}

	public boolean isUser(Object u)
	{
		return (u instanceof User);
	}

	public void updateList(final PagableResponseList users)//{{{
	{
		if(users != null)
		{
			SwingWorker<ContactsListModel, Object> worker = new SwingWorker<ContactsListModel, Object>()
			{
				//PagableResponseList<User> res = users;

				public ContactsListModel doInBackground()
				{
					ContactsListModel clm = new ContactsListModel();
					//clm.clear();
					for(Object o  : users)
					{
						if(isUser(o))
							clm.addElement((User)o);
					}
					return clm;
				}

				@Override
				public void done()
				{
					try
					{
						contactsList1.setModel(get());
					}
					catch(Exception e)
					{
						logger.error("Error while populating UserList", e); //TODO needs I18N
					}
				}
			};
			worker.execute();
		
			prevPage = users.getPreviousCursor();
			nextPage = users.getNextCursor();
			btnPrev.setEnabled(users.hasPrevious());
			btnNext.setEnabled(users.hasNext());
		}
	}//}}}

	//MouseListener
	public void mouseClicked(MouseEvent e) {//{{{
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			if(!isFocusOwner())
				requestFocusInWindow();
			java.awt.Point p = e.getPoint();
			if (e.getSource() instanceof ContactsList)
			{
				ContactsList clist = (ContactsList) e.getSource();
				int index = clist.locationToIndex(p);
				if (index != -1)
				{ //Show menu only if list is not empty
					if(clist.getSelectedIndex() == -1)
						clist.setSelectedIndex(index);
					//Make the caller this panel as we can add the selected list to the panel
					//that  will make the action listener of the menu items this panel as well
					TwitzMainView.getInstance().getActionsMenu(this).show(this, p.x, p.y);
				}

			}
		}
		else if(e.getButton() == MouseEvent.BUTTON1) {
			if(e.getSource() instanceof ContactsList) {
				ContactsList clist = (ContactsList)e.getSource();
				//TODO: This should fire an event to get userlist statuses and not user status
				//twitter4j.Twitter t = TwitterManager.getInstance(TwitzMainView.getInstance()).getTwitterInstance();
				
			}
			if(!isFocusOwner())
				requestFocusInWindow();
		}
	}//}}}
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e)	{ }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }

	//FocusListener
	public void focusGained(FocusEvent e) {//{{{
		//if(logdebug)
		//	logger.debug("Panel selected: "+getTitle());
		setCollapsed(false);
		if(!e.isTemporary() && getInActive())
		{
			//requestListStatus();
		//	setBorder(selectedBorder);
			setInActive(false);
		}
	}//}}}

	public void focusLost(FocusEvent e) {//{{{
		//if(logdebug)
		//	logger.debug("Panel unselected: "+getTitle());
		if(e.getOppositeComponent() instanceof UserListPanel && !e.isTemporary())
		{
		//	setBorder(defaultBorder);
			setInActive(true);
		}
	}//}}}

	public void setInActive(boolean bool)
	{
		inactive = bool;
	}
	
	public boolean getInActive()
	{
		return inactive;
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

	//ActionListener
	public void actionPerformed(ActionEvent e) {//{{{
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("caller", this);
		map.put("async", true);
		User[] selections = getContactsList().getSelectedValues();
		map.put("selections", selections);
		if(logdebug)
			logger.debug("Firing TwitzEvent");
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.valueOf(e.getActionCommand()), new java.util.Date().getTime(), map));
	}//}}}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCollapse;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnUserAdd;
    private javax.swing.JButton btnUserDelete;
    private twitz.ui.ContactsList contactsList1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JLabel listName;
    private javax.swing.JScrollPane listPane;
    private javax.swing.JToolBar pagingToolBar;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables

	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private Border defaultBorder, selectedBorder;
	private javax.swing.Action toggle;
	private JPanel contentPanel = new JPanel(new BorderLayout());

	boolean collapsed = false;
	int boxHeight = 350;
	//final Logger logger = Logger.getLogger(this.getClass().getName());
	org.jdesktop.application.ResourceMap resourceMap;
	javax.swing.ActionMap actionMap;
	private UserList list;
	final static Logger logger = Logger.getLogger(UserListPanel.class.getName());
	boolean logdebug = logger.isDebugEnabled();
	boolean loginfo = logger.isInfoEnabled();
	private boolean inactive = true;

	private long prevPage = -1;
	private long nextPage = -1;

	public static final int MAX_HEIGHT = 300;
	public static final int MIN_HEIGHT = 50;

}
