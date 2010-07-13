/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BlockedPanel.java
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
import twitter4j.ResponseList;
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
public class BlockedPanel extends javax.swing.JPanel implements MouseListener, TwitzEventModel, ActionListener
{
    /** Creates new form BlockedPanel */
    public BlockedPanel() {
		resourceMap = TwitzApp.getContext().getResourceMap(BlockedPanel.class);
		actionMap = TwitzApp.getContext().getActionMap(BlockedPanel.class, this);
        initComponents();
		initDefaults();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//{{{

        jSeparator1 = new javax.swing.JToolBar.Separator();
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

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setName("BlockedPanel"); // NOI18N
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
        //toolbar.add(btnUserAdd);

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
    } //}}}

	//Private methods
	//
	private void requestTimelineForUser(User user)//{{{
	{
		if(TwitzMainView.getInstance().isConnected())
		{
			if(user != null)
			{
				Map map = Collections.synchronizedMap(new TreeMap());
				map.put("async", true);
				map.put("caller", this);
				ArrayList args = new ArrayList();
				args.add(user.getScreenName());
				Paging pager = new Paging();
				pager.setPage(1); //FIXME: need to properly support the paging with a next/prev 
				//args.add(pager);
				map.put("arguments", args);
				fireTwitzEvent(new TwitzEvent(this, TwitzEventType.USER_TIMELINE, new java.util.Date().getTime(), map));
			}
		}
		else
		{
			TwitzMainView.getInstance().addSampleFriends();
		}
	}//}}}

	private void initDefaults() {//{{{
		ListSelectionListener lsl = new ListSelectionListener(){//{{{

			public void valueChanged(ListSelectionEvent e)
			{
		//		btnUserDelete.setEnabled(!((ContactsList)e.getSource()).isSelectionEmpty());
				if(contactsList1.getSelectedIndex() != -1)
				{
					User u = contactsList1.getSelectedValue();
					requestTimelineForUser(u);
				}
			}
		};//}}}
		contactsList1.addListSelectionListener(lsl);
		final ContactsListModel mod = contactsList1.getModel();
	//	mod.addListDataListener(new ListDataListener() {//{{{

	//		public void intervalAdded(ListDataEvent e)
	//		{
	//			setPreferredSize(calcSize(e));
	//			revalidate();
	//		}

	//		public void intervalRemoved(ListDataEvent e)
	//		{
	//			setPreferredSize(calcSize(e));
	//			revalidate();
	//		}

	//		public void contentsChanged(ListDataEvent e)
	//		{
	//			//System.out.println("Inside contentsChanged");
	//		}

	//		private Dimension calcSize(ListDataEvent e) {
	//			int height = 50;
	//			int count = mod.getSize();
	//			int newHeight = (count*height);
	//			Dimension dim = null;
	//			if(e.getType() == ListDataEvent.INTERVAL_ADDED) {
	//				dim = new Dimension(50, newHeight < MAX_HEIGHT ? newHeight : MAX_HEIGHT);
	//			}
	//			else if(e.getType() == ListDataEvent.INTERVAL_REMOVED) {
	//				dim = new Dimension(50, newHeight >= MIN_HEIGHT ? newHeight : MIN_HEIGHT);
	//			}
	//			//logger.log(Level.INFO, "New Dimension for panel: "+dim.toString());
	//			return dim;
	//		}
	//	});//}}}
	//	MouseListener toolbarListener = new MouseAdapter() {//{{{
	//		@Override
	//		public void mouseClicked(MouseEvent evt) {
//	//			requestFocusInWindow();
	//			if(evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
	//				toggleCollapsed();
	//			}
	//			if(!isFocusOwner())
	//				requestFocusInWindow();
	//		}
	//	};//}}}
	//	toolbar.addMouseListener(toolbarListener);
	//	listName.addMouseListener(toolbarListener);
		contactsList1.addMouseListener(this);
		//addTwitzListener(TwitzMainView.getInstance());
		setFocusable(true);
		addMouseListener(this);
		//addFocusListener(this);
		//Store the default border set by the user.
		defaultBorder = getBorder();
		selectedBorder = BorderFactory.createLoweredBevelBorder();
		jSeparator2.setPreferredSize(new Dimension(1000,20));
		//listName.setMaximumSize(new Dimension(40,listName.getPreferredSize().height));
		listName.setFont(new Font("Arial", Font.BOLD, 10));

		twitz.TwitzMainView.fixJScrollPaneBarsSize(listPane);

		btnPrev.setEnabled(false);
		btnNext.setEnabled(false);
	}//}}}

	@Action
	public void addListUser() {
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("caller", this);
		//firePropertyChange("createFriendshop", map, null);
	}

	@Action
	public void deleteListUser() {//{{{
		if(contactsList1.getSelectedIndex() != -1)
		{
			Map map = Collections.synchronizedMap(new TreeMap());
			map.put("async", true);
			map.put("caller", this);
			ArrayList args = new ArrayList();
			User toBeDeleted = contactsList1.getSelectedValue();
			args.add(toBeDeleted.getScreenName());
			map.put("arguments", args);
			fireTwitzEvent(new TwitzEvent(this, TwitzEventType.DESTROY_BLOCK, new java.util.Date().getTime(), map));
			firePropertyChange("destroyBlock", toBeDeleted, null);
		}
	}//}}}

//	@Action
//	public void getNext()//{{{
//	{
//		Map map = Collections.synchronizedMap(new TreeMap());
//		map.put("caller", this);
//		map.put("async", true);
//		User[] selections = getContactsList().getSelectedValues();
//		map.put("selections", selections);
//		ArrayList args = new ArrayList();
//		args.add(config.getString("twitter.id"));
//		args.add(nextPage);
//		map.put("arguments", args);
//		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.FRIENDS_STATUSES, new java.util.Date().getTime(), map));
//	}//}}}

//`	@Action
//`	public void getPrevious()//{{{
//`	{
//`		Map map = Collections.synchronizedMap(new TreeMap());
//`		map.put("caller", this);
//`		map.put("async", true);
//`		ArrayList args = new ArrayList();
//`		args.add(config.getString("twitter.id"));
//`		args.add(prevPage);
//`		map.put("arguments", args);
//`		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.FRIENDS_STATUSES, new java.util.Date().getTime(), map));
//`	}//}}}

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
	
	public void addUser(Vector<User> user) {
		contactsList1.addUser(user);
	}

	public void updateList(ResponseList<User> users)//{{{
	{
		ContactsListModel clm = contactsList1.getModel();
		clm.clear();
		for(User u : users)
			clm.addElement(u);
		
	//	prevPage = users.getPreviousCursor();
	//	nextPage = users.getNextCursor();
	//	btnPrev.setEnabled(users.hasPrevious());
	//	btnNext.setEnabled(users.hasNext());
	}//}}}

	public int getSelectedIndex()
	{
		return contactsList1.getSelectedIndex();
	}

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
			}
		}
	}//}}}
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e)	{ }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }

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
	private twitz.util.SettingsManager config = twitz.util.SettingsManager.getInstance();

	boolean collapsed = false;
	int boxHeight = 350;
	//final Logger logger = Logger.getLogger(this.getClass().getName());
	org.jdesktop.application.ResourceMap resourceMap;
	javax.swing.ActionMap actionMap;
	final static Logger logger = Logger.getLogger(BlockedPanel.class.getName());
	boolean logdebug = logger.isDebugEnabled();
	boolean loginfo = logger.isInfoEnabled();

	private long prevPage = -1;
	private long nextPage = -1;

	public static final int MAX_HEIGHT = 300;
	public static final int MIN_HEIGHT = 50;

}
