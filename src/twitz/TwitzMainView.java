/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TwitzMainView.java
 *
 * Created on Jun 12, 2010, 1:40:58 PM
 */

package twitz;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.MenuElement;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.MenuEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.TaskMonitor;
import twitter4j.*;
import twitz.ui.dialogs.MessageDialog;
import twitz.ui.dialogs.PreferencesDialog;
import twitz.ui.dialogs.TwitzAboutBox;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEventModel;
import twitz.twitter.TwitterManager;
import twitz.testing.*;
import twitz.util.SettingsManager;
import twitz.util.UserStore;
import twitz.ui.BlockedPanel;
import twitz.ui.ContactsList;
import twitz.ui.FollowersPanel;
import twitz.ui.FriendsPanel;
import twitz.ui.StatusList;
import twitz.ui.TrendsPanel;
import twitz.ui.UserListPanel;
import twitz.ui.models.ContactsListModel;
import twitz.ui.models.StatusTableModel;
import twitz.ui.models.StatusListModel;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.SubstanceConstants.ScrollPaneButtonPolicyKind;
import org.pushingpixels.substance.api.tabbed.*;
import org.pushingpixels.substance.api.SubstanceConstants.TabCloseKind;


import twitz.ui.SearchPanel;
import twitz.ui.StatusPanel;
import twitz.ui.TimeLinePanel;
import twitz.ui.TweetBox;
import twitz.ui.UserListMainPanel;
import twitz.ui.renderers.StatusListRenderer;
import twitz.ui.renderers.StatusListPanelRenderer;
import twitz.util.TwitzSessionManager;

/**
 *
 * @author mistik1
 */
public class TwitzMainView extends javax.swing.JInternalFrame implements ActionListener, TwitterListener, TwitzListener, TwitzEventModel {
	private final String sessionName;
	private ResourceMap resource;

    /** Creates new form TwitzMainView */
    public TwitzMainView(TwitzApp app, String session) {//{{{
		this.sessionName = session;
		TwitzSessionManager.getInstance().addTwitzMainView(sessionName, this);
		config = TwitzSessionManager.getInstance().getSettingsManagerForSession(sessionName);
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXxxx");
		startMode = config.getBoolean("minimode");
		mainApp = (TwitzApp)app;
        actionMap = TwitzApp.getContext().getActionMap(TwitzMainView.class, this);
        resourceMap = TwitzApp.getContext().getResourceMap(TwitzMainView.class);
        initComponents();
		System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");

		init();
		System.out.println("000000000000000000000000000000000000000000000000000000000000");
		updateLayout();
		System.out.println("22222222222222222222222222222222222222222222222222222222222222");
		//Add tab position menu
		editMenu.add(createTabPositionMenu());

		//btnTweet.setEnabled(false);

		//searchHeaders.add("User");
		//searchHeaders.add("Results");
		//Configure the GUI from the defaults in the config file
		//setupDefaults();

    }//}}}

//	/**
//	 * This is the method to get a singleton instance of <code>TwitzMainView</code>
//	 * This is called by <code>TwitzApp</code> when initializing Twitz
//	 * @param app The TwitzApp or subclass caller.
//	 */
//	public synchronized static TwitzMainView getInstance(TwitzApp app) {
//		if(instance == null) {
//			instance = new TwitzMainView(app);
//		}
//		return instance;
//	}
//
//	/**
//	 * This is just a convenience method to access the singleton instance
//	 * by other parts of the application.
//	 * @throws IllegalStateException if the application has not yet been initialized
//	 */
//	public synchronized static TwitzMainView getInstance() {
//		if(mainApp == null || instance == null)
//			throw new IllegalStateException("Application has not been properly initialized");
//		return instance;
//	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */ 
    @SuppressWarnings("unchecked") //{{{
    private void initComponents() {//GEN-BEGIN:initComponents

        contextMenu = new javax.swing.JPopupMenu();
        prefsItem = new javax.swing.JMenuItem();
        miniItem = new javax.swing.JMenuItem();
        helpItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        exitItem = new javax.swing.JMenuItem();
        actionPanel = new javax.swing.JPanel();
        chkCOT = new javax.swing.JCheckBox();
        btnTweet = new javax.swing.JButton();
        btnMini = new javax.swing.JButton();
        lblChars = new javax.swing.JLabel();
        txtTweet = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        prefsMenuItem = new javax.swing.JMenuItem();
        menuTabs = new javax.swing.JMenu();
        menuItemFriends = new javax.swing.JCheckBoxMenuItem();
        menuItemBlocked = new javax.swing.JCheckBoxMenuItem();
        menuItemFollowing = new javax.swing.JCheckBoxMenuItem();
        menuItemFollowers = new javax.swing.JCheckBoxMenuItem();
        menuItemSearch = new javax.swing.JCheckBoxMenuItem();
        actionsMenu = createActionsMenu();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        logsMenuItem = new javax.swing.JMenuItem();
        blockedPane = new javax.swing.JScrollPane();
        followingPane = new javax.swing.JScrollPane();
        followingList = new javax.swing.JList();
        followersPane = new javax.swing.JScrollPane();
        followersList = new javax.swing.JList();
        recentList = new javax.swing.JTable();
        mainPanel = new javax.swing.JPanel();
        tabPane = new javax.swing.JTabbedPane();
        recentPane = new javax.swing.JPanel();
        timelineTrendsPane = new javax.swing.JSplitPane();
        friendsPanel = new javax.swing.JSplitPane();
        friendsPane = new javax.swing.JTabbedPane();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        contextMenu.setName("contextMenu"); // NOI18N

        
        prefsItem.setAction(actionMap.get("showPrefsBox")); // NOI18N
        
        prefsItem.setIcon(resourceMap.getIcon("prefsItem.icon")); // NOI18N
        prefsItem.setText(resourceMap.getString("prefsItem.text")); // NOI18N
        prefsItem.setName("prefsItem"); // NOI18N
        contextMenu.add(prefsItem);

        miniItem.setAction(actionMap.get("showMiniMode")); // NOI18N
        miniItem.setIcon(resourceMap.getIcon("miniItem.icon")); // NOI18N
        miniItem.setText(resourceMap.getString("miniItem.text")); // NOI18N
        miniItem.setName("miniItem"); // NOI18N
        contextMenu.add(miniItem);

        helpItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        helpItem.setIcon(resourceMap.getIcon("helpItem.icon")); // NOI18N
        helpItem.setText(resourceMap.getString("helpItem.text")); // NOI18N
        helpItem.setName("helpItem"); // NOI18N
        contextMenu.add(helpItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        contextMenu.add(jSeparator1);

        exitItem.setAction(actionMap.get("quit")); // NOI18N
        exitItem.setIcon(resourceMap.getIcon("exitItem.icon")); // NOI18N
        exitItem.setName("exitItem"); // NOI18N
        contextMenu.add(exitItem);

        actionPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        actionPanel.setMinimumSize(new java.awt.Dimension(100, 50));
        actionPanel.setName("actionPanel"); // NOI18N

        chkCOT.setToolTipText(resourceMap.getString("chkCOT.toolTipText")); // NOI18N
        chkCOT.setName("chkCOT"); // NOI18N

        btnTweet.setText(resourceMap.getString("btnTweet.text")); // NOI18N
        btnTweet.setToolTipText(resourceMap.getString("btnTweet.toolTipText")); // NOI18N
        btnTweet.setName("btnTweet"); // NOI18N

        btnMini.setAction(actionMap.get("showMiniMode")); // NOI18N
        btnMini.setIcon(resourceMap.getIcon("btnMini.icon")); // NOI18N
        btnMini.setText(resourceMap.getString("btnMini.text")); // NOI18N
        btnMini.setToolTipText(resourceMap.getString("btnMini.toolTipText")); // NOI18N
        btnMini.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnMini.setName("btnMini"); // NOI18N

        lblChars.setFont(resourceMap.getFont("lblChars.font")); // NOI18N
        lblChars.setForeground(resourceMap.getColor("lblChars.foreground")); // NOI18N
        lblChars.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblChars.setText(resourceMap.getString("lblChars.text")); // NOI18N
        lblChars.setName("lblChars"); // NOI18N

        txtTweet.setText(resourceMap.getString("txtTweet.text")); // NOI18N
        txtTweet.setName("txtTweet"); // NOI18N
        txtTweet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTweetKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTweetKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.setHorizontalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, actionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblChars, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTweet, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkCOT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTweet)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMini)
                .addContainerGap())
        );
        actionPanelLayout.setVerticalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, actionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chkCOT)
                    .addComponent(btnTweet)
                    .addComponent(btnMini)
                    .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblChars, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTweet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setIcon(resourceMap.getIcon("exitMenuItem.icon")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
        editMenu.setName("editMenu"); // NOI18N

        prefsMenuItem.setAction(actionMap.get("showPrefsBox")); // NOI18N
        prefsMenuItem.setIcon(resourceMap.getIcon("prefsMenuItem.icon")); // NOI18N
        prefsMenuItem.setText(resourceMap.getString("prefsMenuItem.text")); // NOI18N
        prefsMenuItem.setName("prefsMenuItem"); // NOI18N
        editMenu.add(prefsMenuItem);

        menuTabs.setIcon(resourceMap.getIcon("menuTabs.icon")); // NOI18N
        menuTabs.setText(resourceMap.getString("menuTabs.text")); // NOI18N
        menuTabs.setName("menuTabs"); // NOI18N

        menuItemFriends.setSelected(true);
        menuItemFriends.setText(resourceMap.getString("menuItemFriends.text")); // NOI18N
        menuItemFriends.setToolTipText(resourceMap.getString("menuItemFriends.toolTipText")); // NOI18N
        menuItemFriends.setIcon(resourceMap.getIcon("menuItemFriends.icon")); // NOI18N
        menuItemFriends.setName("menuItemFriends"); // NOI18N
        menuItemFriends.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemFriendsActionPerformed(evt);
            }
        });
        menuTabs.add(menuItemFriends);

        menuItemBlocked.setSelected(true);
        menuItemBlocked.setText(resourceMap.getString("menuItemBlocked.text")); // NOI18N
        menuItemBlocked.setToolTipText(resourceMap.getString("menuItemBlocked.toolTipText")); // NOI18N
        menuItemBlocked.setIcon(resourceMap.getIcon("menuItemBlocked.icon")); // NOI18N
        menuItemBlocked.setName("menuItemBlocked"); // NOI18N
        menuItemBlocked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemBlockedActionPerformed(evt);
            }
        });
        menuTabs.add(menuItemBlocked);

        menuItemFollowing.setSelected(true);
        menuItemFollowing.setText(resourceMap.getString("menuItemFollowing.text")); // NOI18N
        menuItemFollowing.setToolTipText(resourceMap.getString("menuItemFollowing.toolTipText")); // NOI18N
        menuItemFollowing.setIcon(resourceMap.getIcon("menuItemFollowing.icon")); // NOI18N
        menuItemFollowing.setName("menuItemFollowing"); // NOI18N
        menuItemFollowing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemFollowingActionPerformed(evt);
            }
        });
        menuTabs.add(menuItemFollowing);

        menuItemFollowers.setSelected(true);
        menuItemFollowers.setText(resourceMap.getString("menuItemFollowers.text")); // NOI18N
        menuItemFollowers.setToolTipText(resourceMap.getString("menuItemFollowers.toolTipText")); // NOI18N
        menuItemFollowers.setIcon(resourceMap.getIcon("menuItemFollowers.icon")); // NOI18N
        menuItemFollowers.setName("menuItemFollowers"); // NOI18N
        menuItemFollowers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemFollowersActionPerformed(evt);
            }
        });
        menuTabs.add(menuItemFollowers);

        menuItemSearch.setSelected(true);
        menuItemSearch.setText(resourceMap.getString("menuItemSearch.text")); // NOI18N
        menuItemSearch.setIcon(resourceMap.getIcon("menuItemSearch.icon")); // NOI18N
        menuItemSearch.setName("menuItemSearch"); // NOI18N
        menuItemSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSearchActionPerformed(evt);
            }
        });
        menuTabs.add(menuItemSearch);

        editMenu.add(menuTabs);

        menuBar.add(editMenu);

        actionsMenu.setText(resourceMap.getString("actionsMenu.text")); // NOI18N
        actionsMenu.setName("actionsMenu"); // NOI18N
        menuBar.add(actionsMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setIcon(resourceMap.getIcon("aboutMenuItem.icon")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        logsMenuItem.setAction(actionMap.get("viewHTMLLog")); // NOI18N
        logsMenuItem.setIcon(resourceMap.getIcon("icon.bug")); // NOI18N
        logsMenuItem.setText(resourceMap.getString("logsMenuItem.text")); // NOI18N
        logsMenuItem.setName("logsMenuItem"); // NOI18N
        helpMenu.add(logsMenuItem);

        menuBar.add(helpMenu);

        blockedPane.setName("blockedPane"); // NOI18N

        followingPane.setName("followingPane"); // NOI18N

        followingList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        followingList.setName("followingList"); // NOI18N
        followingPane.setViewportView(followingList);

        followersPane.setName("followersPane"); // NOI18N

        followersList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        followersList.setName("followersList"); // NOI18N
        followersPane.setViewportView(followersList);

        recentList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "I", "", "", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        recentList.setFillsViewportHeight(true);
        recentList.setIntercellSpacing(new java.awt.Dimension(3, 3));
        recentList.setName("recentList"); // NOI18N
        recentList.setRowHeight(50);
        recentList.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        recentList.setShowHorizontalLines(false);
        recentList.setShowVerticalLines(false);

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setName("TwitzMainView"); // NOI18N
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        mainPanel.setName("mainPanel"); // NOI18N

        tabPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabPane.setInheritsPopupMenu(true);
        tabPane.setMinimumSize(new java.awt.Dimension(200, 37));
        tabPane.setName("tabPane"); // NOI18N
        tabPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tabPaneKeyReleased(evt);
            }
        });

        recentPane.setName("recentPane"); // NOI18N
        recentPane.setLayout(new java.awt.BorderLayout());

        timelineTrendsPane.setDividerLocation(160);
        timelineTrendsPane.setDividerSize(8);
        timelineTrendsPane.setName("timelineTrendsPane"); // NOI18N
        recentPane.add(timelineTrendsPane, java.awt.BorderLayout.CENTER);

        tabPane.addTab(resourceMap.getString("recentPane.TabConstraints.tabTitle"), resourceMap.getIcon("icon.comments"), recentPane, resourceMap.getString("recentPane.TabConstraints.tabToolTip")); // NOI18N

        friendsPanel.setDividerLocation(180);
        friendsPanel.setDividerSize(4);
        friendsPanel.setName("friendsPanel"); // NOI18N

        friendsPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        friendsPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        friendsPane.setName("friendsPane"); // NOI18N
        friendsPanel.setLeftComponent(friendsPane);

        tabPane.addTab(resourceMap.getString("friendsPanel.TabConstraints.tabTitle"), resourceMap.getIcon("friendsPanel.TabConstraints.tabIcon"), friendsPanel, resourceMap.getString("friendsPanel.TabConstraints.tabToolTip")); // NOI18N

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 331, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                    .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                .addGap(93, 93, 93))
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                    .addContainerGap(586, Short.MAX_VALUE)
                    .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }//GEN-END:initComponents
	//}}}

	private void menuItemFriendsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuItemFriendsActionPerformed
	{//GEN-HEADEREND:event_menuItemFriendsActionPerformed
		toggleTabs(evt);
	}//GEN-LAST:event_menuItemFriendsActionPerformed

	private void menuItemBlockedActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuItemBlockedActionPerformed
	{//GEN-HEADEREND:event_menuItemBlockedActionPerformed
		toggleTabs(evt);
	}//GEN-LAST:event_menuItemBlockedActionPerformed

	private void menuItemFollowingActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuItemFollowingActionPerformed
	{//GEN-HEADEREND:event_menuItemFollowingActionPerformed
		toggleTabs(evt);
	}//GEN-LAST:event_menuItemFollowingActionPerformed

	private void menuItemFollowersActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuItemFollowersActionPerformed
	{//GEN-HEADEREND:event_menuItemFollowersActionPerformed
		toggleTabs(evt);
	}//GEN-LAST:event_menuItemFollowersActionPerformed

	private void menuItemSearchActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuItemSearchActionPerformed
	{//GEN-HEADEREND:event_menuItemSearchActionPerformed
		toggleTabs(evt);
	}//GEN-LAST:event_menuItemSearchActionPerformed

	private void tabPaneKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tabPaneKeyReleased
	{//GEN-HEADEREND:event_tabPaneKeyReleased
		keyTyped(evt);
	}//GEN-LAST:event_tabPaneKeyReleased

	private void txtTweetKeyReleased(java.awt.event.KeyEvent evt)                                  
	{                                         
		keyReleased(evt);
	}                                    

	private void txtTweetKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTweetKeyTyped
	{//GEN-HEADEREND:event_txtTweetKeyTyped
		keyTyped(evt);
	}//GEN-LAST:event_txtTweetKeyTyped

	private void updateLayout()//{{{
	{
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                    .addComponent(tweetBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tweetBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
	}//}}}

	public void fixTables() {
		//tblSearch.setRowHeight(50);
	}

	public javax.swing.JTextField getTweetField()
	{
		return tweetBox.getTweetField(); //this.txtTweet;
	}

	/**
	 * This is a utility method that will set the scrollbars of any
	 * <em>JScrollPane</em> you pass to it 8 pixels thickness. 
	 * @param pane The <em>JScrollPane</em> you want to fix
	 */
	public static void fixJScrollPaneBarsSize(javax.swing.JScrollPane pane)//{{{
	{
		if(pane != null)
		{
			//Make the scrollbar very thin
			JScrollBar vbar = pane.getVerticalScrollBar();
			vbar.setPreferredSize(new java.awt.Dimension(8, 0));

			JScrollBar hbar = pane.getHorizontalScrollBar();
			hbar.setPreferredSize(new java.awt.Dimension(0, 8));

			pane.putClientProperty(SubstanceLookAndFeel.SCROLL_PANE_BUTTONS_POLICY,
					ScrollPaneButtonPolicyKind.MULTIPLE_BOTH);
		}
	}//}}}

	private void init() {//{{{
		//Make the scrollbar very thin in the user list tab
		fixJScrollPaneBarsSize(followersPane);
		fixJScrollPaneBarsSize(blockedPane);
		fixJScrollPaneBarsSize(followingPane);
		
		System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCcc");
		blockedList = new ContactsList();
		tweetBox = new TweetBox();
		tweetBox.setSessionName(sessionName);
		System.out.println("ttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
		blocked = new BlockedPanel();
		blocked.setSessionName(sessionName);
		System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
		followers = new FollowersPanel();
		followers.setSessionName(sessionName);
		System.out.println("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
		friends = new FriendsPanel();
		friends.setSessionName(sessionName);
		System.out.println("friends----------------------------------------------");
		userListMainPanel1 = new twitz.ui.UserListMainPanel();
		userListMainPanel1.setName("userListMainPanel1"); // NOI18N
		userListMainPanel1.setSessionName(sessionName);
		System.out.println("userlistmainpanel=============================================");
		friendsStatusPanel = new StatusPanel();
		friendsStatusPanel.setSessionName(sessionName);
		this.friendsPanel.setRightComponent(friendsStatusPanel);

		friendsTweets = new StatusList();
		friendsTweets.setSessionName(sessionName);
		trendPanel = new TrendsPanel();
		trendPanel.setSessionName(sessionName);
		timelinePanel = new TimeLinePanel();
		timelinePanel.setSessionName(sessionName);
		searchPanel = new SearchPanel();
		searchPanel.setSessionName(sessionName);
		timelineTrendsPane.setLeftComponent(trendPanel);
		timelineTrendsPane.setRightComponent(timelinePanel);

		//Add friends list tothe friendsPane tabbed panel
		friendsPane.insertTab(resourceMap.getString("friends.TabConstraints.tabTitle"), resourceMap.getIcon("friends.TabConstraints.tabIcon"), friends, resourceMap.getString("friends.TabConstraints.tabToolTip"),0); // NOI18N
        friendsPane.addTab(resourceMap.getString("userListMainPanel1.TabConstraints.tabTitle"), resourceMap.getIcon("userListMainPanel1.TabConstraints.tabIcon"), userListMainPanel1, resourceMap.getString("userListMainPanel1.TabConstraints.tabToolTip")); // NOI18N
        //friendsPanel.setLeftComponent(friendsPane);
		//friendsPane.addTab(resourceMap.getString("following.TabConstraints.tabTitle"), resourceMap.getIcon("following.TabConstraints.tabIcon"), following, resourceMap.getString("following.TabConstraints.tabToolTip")); // NOI18N
		friendsPane.addTab(resourceMap.getString("followers.TabConstraints.tabTitle"), resourceMap.getIcon("followers.TabConstraints.tabIcon"), followers, resourceMap.getString("followers.TabConstraints.tabToolTip")); // NOI18N
		friendsPane.addTab(resourceMap.getString("blocked.TabConstraints.tabTitle"), resourceMap.getIcon("blocked.TabConstraints.tabIcon"), blocked, resourceMap.getString("blocked.TabConstraints.tabToolTip")); // NOI18N

		//Make these tabs render with a close button
		searchPanel.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, Boolean.TRUE);
		followersPane.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, Boolean.TRUE);
		followingPane.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, Boolean.TRUE);
		blockedPane.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, Boolean.TRUE);
		friendsPanel.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, Boolean.TRUE);
		
		TabCloseCallback closeCallback = new TabCloseCallback() {//{{{
			public TabCloseKind onAreaClick(JTabbedPane tabbedPane, int tabIndex, MouseEvent mouseEvent) 
			{
				if (mouseEvent.getButton() != MouseEvent.BUTTON3)
					return TabCloseKind.NONE;
			//	if (mouseEvent.isShiftDown()) {
			//		return TabCloseKind.ALL;
			//	}
				//
				return TabCloseKind.NONE;
			}
			
			public TabCloseKind onCloseButtonClick(JTabbedPane tabbedPane, int tabIndex, MouseEvent mouseEvent) 
			{
			//	if (mouseEvent.isAltDown()) {
			//		return TabCloseKind.ALL_BUT_THIS;
			//	}
			//	if (mouseEvent.isShiftDown()) {
			//		return TabCloseKind.ALL;
			//	}
				String value = tabbedPane.getComponentAt(tabIndex).getName();
				if(value.equals("searchPanel")) {
					config.setProperty("tab_search", false + "");
					menuItemSearch.setSelected(false);
				}
				else if(value.equals("blockedPane")) {
					config.setProperty("tab_blocked", false + "");
					menuItemBlocked.setSelected(false);
				}
				else if(value.equals("friendsPanel")) {
					config.setProperty("tab_friends", false + "");
					menuItemFriends.setSelected(false);
				}
				else if(value.equals("followingPane")) {
					config.setProperty("tab_following", false + "");
					menuItemFollowing.setSelected(false);
				}
				else if(value.equals("followersPane")) {
					config.setProperty("tab_followers", false + "");
					menuItemFollowers.setSelected(false);
				}

				return TabCloseKind.THIS;
			}
			
			public String getAreaTooltip(JTabbedPane tabbedPane, int tabIndex) {
				//String tip = tabbedPane.getToolTipTextAt(tabIndex);
				//System.out.println(tip);
				return getRealTabTooltip(tabbedPane, tabIndex);
			}
				 
			public String getCloseButtonTooltip(JTabbedPane tabbedPane, int tabIndex) 
			{
				StringBuilder result = new StringBuilder();
				result.append("<html><body>");
				result.append("Mouse click closes <b>").append(tabbedPane.getTitleAt(tabIndex)).append("</b> tab");
				//result.append("<br><b>Alt</b>-Mouse click closes all tabs but <b>"
				//	+ tabbedPane.getTitleAt(tabIndex) + "</b> tab");
				//result.append("<br><b>Shift</b>-Mouse click closes all tabs");
				result.append("</body></html>");
				return result.toString();
			}

			public String getRealTabTooltip(JTabbedPane tabbedPane, int tabIndex) {
				String value = tabbedPane.getComponentAt(tabIndex).getName();
				String rv = null;
				if(value.equals("searchPanel")) {
					rv = getResourceMap().getString("searchPanel.TabConstraints.tabToolTip");
				}
				else if(value.equals("blockedPane")) {
					rv = getResourceMap().getString("blockedPane.TabConstraints.tabToolTip");
				}
				else if(value.equals("friendsPanel")) {
					rv = getResourceMap().getString("friendsPanel.TabConstraints.tabToolTip");
				}
				else if(value.equals("followingPane")) {
					rv = getResourceMap().getString("followingPane.TabConstraints.tabToolTip");
				}
				else if(value.equals("followersPane")) {
					rv = getResourceMap().getString("followersPane.TabConstraints.tabToolTip");
				}
				return rv;
			}
		};//}}}

		tabPane.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_CALLBACK, closeCallback);
		
		//initTwitter();
	
		//fixTables();
	
        // status bar initialization - message timeout, idle icon and busy animation, etc
   		final Timer messageTimer;//{{{
   		final Timer busyIconTimer;
   		final Icon idleIcon;
   		final Icon[] busyIcons = new Icon[15];
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

		statusListener = new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        };
		
        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(TwitzApp.getContext());
        taskMonitor.addPropertyChangeListener(statusListener);//}}}

		//chkCOT.setVisible(false);

		//Set the tab location from config
		setTabPlacement();


		//if(startMode) //We closed in minimode
		//	miniTwitz(true);

		//Disable tabs that were removed by user
		if(!config.getBoolean("tab_friends")) {
			tabPane.remove(friendsPanel);
			menuItemFriends.setSelected(false);
		}
		if(!config.getBoolean("tab_blocked")) {
			tabPane.remove(blockedPane);
			menuItemBlocked.setSelected(false);
		}
		if(!config.getBoolean("tab_following")) {
			tabPane.remove(followingPane);
			menuItemFollowing.setSelected(false);
		}
		if(!config.getBoolean("tab_followers")) {
			tabPane.remove(followersPane);
			menuItemFollowers.setSelected(false);
		}
		if(!config.getBoolean("tab_search")) {
			tabPane.remove(searchPanel);
			menuItemSearch.setSelected(false);
		}
		else {
			tabPane.addTab(getResourceMap().getString("searchPanel.TabConstraints.tabTitle"), getResourceMap().getIcon("searchPanel.tabIcon"), searchPanel, getResourceMap().getString("searchPanel.TabConstraints.tabTooltip")); // NOI18N
		}
		
		tabPane.addChangeListener(new ChangeListener() {//{{{

			public void stateChanged(ChangeEvent e)
			{
				if(tabPane.getSelectedComponent().equals(searchPanel)) {
					tweetBox.setVisible(false);
				}
				else {
					tweetBox.setVisible(true);
				}
			}
		});//}}}
		
		tweetBox.setButtonEnabled(false);
		//Dont allow the tweets list to steal focus
		friendsTweets.setFocusable(false);
		friendsTweets.addTwitzListener(this);
		friends.addTwitzListener(this);
		followers.addTwitzListener(this);
		blocked.addTwitzListener(this);
		searchPanel.addTwitzListener(this);
		trendPanel.addTwitzListener(this);
		userListMainPanel1.addTwitzListener(this);
		timelinePanel.addTwitzListener(this);
		tweetBox.addTwitzListener(this);

		blockedList.addTwitzListener(this);
		//Disable the menuitems no longer in use
		menuItemBlocked.setEnabled(false);
		menuItemFollowers.setEnabled(false);
		menuItemFollowing.setEnabled(false);
	}//}}}

	public void initTwitter() {//{{{
		//Initialize twitter
		twitterManager = new TwitterManager(sessionName);//.getInstance();
		twitterManager.addTwitzListener(this);
		resource = TwitzApp.getContext().getResourceMap(twitz.twitter.TwitterManager.class);
		javax.swing.SwingWorker worker = new javax.swing.SwingWorker() //{{{
		{
			boolean online = false;
			boolean error = false;
			TwitterException tec;

			public Void doInBackground()
			{
				try
				{
					online = twitterManager.getTwitterInstance().test();
				}
				catch(TwitterException te){
					logger.error(te);
					online = false;
				}
				try
				{
					twitterManager.getTwitterInstance().verifyCredentials();
				}
				catch(TwitterException te)
				{
					if(te.getStatusCode() == 401)
					{
						//Authentication incorrect
						tec = te;
						online = false;
					}
				}
				return null;
			}

			@Override
			public void done()
			{
				if(error)
				{
					connected = online;
					displayError(tec, "Login Error", "Incorrect username or password", null, true);
				}
				else
				{
					connected = online;
					
					if(online)
					{
						loadAllPanels();
					}
					else if(DEVMODE)
					{
						addSampleData();
					}
				}
			}
		}; //}}}
		worker.execute();
	}//}}}

	public void setTwitterManager(TwitterManager t)
	{
		if(t != null)
		{
			TwitterManager old = twitterManager;
			this.twitterManager = t;
			firePropertyChange("twitterManager", old, t);
		}
	}

	public TwitterManager getTwitterManager()
	{
		return twitterManager;
	}

	public String getSessionName()
	{
		return this.sessionName;
	}
	
	public void addSampleFriends()
	{
		if(DEVMODE)
		{
			StatusListModel mod = (StatusListModel) friendsStatusPanel.getStatusList().getModel();
			mod.clear();
			for (int i = 0; i < 10; i++)
			{
				mod.addStatus(new StatusTest(i));
			}
		}

	}

	public User[] getSampleListUsers(int which)//{{{
	{
		User[] rv = new User[0];

		User[] ouser = new User[]{
			new UserTest("Python"),
			new UserTest("Ladybug"),
			new UserTest("perry")
		};

		User[] puser = new User[]{
			new UserTest("cansport"),
			new UserTest("CNN News"),
			new UserTest("Abc News"),
			new UserTest("Nbc Online"),
			new UserTest("Black Power gen"),
			new UserTest("Facts of Life"),
			new UserTest("black_rino")
		};
		switch(which)
		{
			case 0:
				rv = ouser;
			break;
			case 1:
				rv = puser;
			break;
		}
		return rv;
	}//}}}

	private void addSampleData() {//{{{
		StatusListModel dm = timelinePanel.getStatusList().getModel();
		//TODO: Test code to be removed in production
		for (int i=0; i < 10; i++) {
			dm.addStatus(new StatusTest(i));
		}

		trendPanel.addTrend(new TrendTest());
		trendPanel.addTrend(new TrendTest());
		trendPanel.addTrend(new TrendTest());
//		timelinePanel.getStatusList().setFixedCellHeight(120);

		User[] ouser = new User[]{
			new UserTest("Python"),
			new UserTest("Ladybug"),
			new UserTest("perry")
		};
		

		User[] puser = new User[]{
			new UserTest("cansport"),
			new UserTest("CNN News"),
			new UserTest("Abc News"),
			new UserTest("Nbc Online"),
			new UserTest("Black Power gen"),
			new UserTest("Facts of Life"),
			new UserTest("black_rino")
		};
//		UserListTest(int id, String name, String fullName,
//			String slug, String desc, int subCount,
//			int memberCount, boolean pub, User user)
		UserListTest ult = new UserListTest(1, "List 1", "List 1 Testing",
				"this is a slug", "this describes the list", 0, 7, true, new UserTest());
		UserListTest ult1 = new UserListTest(2, "List 2", "List 1 Testing",
				"this is a slug", "this describes the list", 0, 7, true, new UserTest());
		UserListTest ult2 = new UserListTest(3, "News Networks", "List 1 Testing",
				"this is a slug", "this describes the list", 0, 7, true, new UserTest());
		UserListTest ult3 = new UserListTest(4, "List 4", "List 1 Testing",
				"this is a slug", "this describes the list", 0, 7, true, new UserTest());
		UserListTest ult4 = new UserListTest(5, "Mizer Ball", "List 1 Testing",
				"this is a slug", "this describes the list", 0, 7, true, new UserTest());
		UserListTest ult5 = new UserListTest(6, "List 6", "List 1 Testing",
				"this is a slug", "this describes the list", 0, 7, true, new UserTest());
		UserListPanel pnl = userListMainPanel1.addUserList(ult);
		pnl.addUser(ouser);
		UserListPanel pnl1 = userListMainPanel1.addUserList(ult1);
		pnl1.addUser(puser);
		UserListPanel pnl2 = userListMainPanel1.addUserList(ult2);
		pnl2.addUser(puser);
		UserListPanel pnl3 = userListMainPanel1.addUserList(ult3);
		pnl3.addUser(puser);
		UserListPanel pnl4 = userListMainPanel1.addUserList(ult4);
		pnl4.addUser(puser);
		UserListPanel pnl5 = userListMainPanel1.addUserList(ult5);
		pnl5.addUser(puser);

		friends.addUser(ouser);
		blocked.addUser(ouser);
		followers.addUser(puser);
//		userListMainPanel1.addUserList(ouser, "Panel 1");
//		userListMainPanel1.addUserList(puser, "Panel 2");
//		userListMainPanel1.addUserList(puser, "Mizer Ball");
//		userListMainPanel1.addUserList(puser, "News Networks");
//		userListMainPanel1.addUserList(puser, "Panel 5");

	}//}}}

	@Action
	public void loadAllPanels()//{{{
	{
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", timelinePanel);
		//Update the timeline view.
		eventOccurred(new TwitzEvent(this, TwitzEventType.HOME_TIMELINE, new java.util.Date().getTime(), map));
		//Update trends view
		map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", trendPanel);
		ArrayList args = new ArrayList();
		args.add(1);
		map.put("arguments", args);
		eventOccurred(new TwitzEvent(this, TwitzEventType.LOCATION_TRENDS, new java.util.Date().getTime(), map));
		//Load userlists view
		map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", userListMainPanel1);
		args = new ArrayList();
		args.add(config.getString("twitter_id"));//screenName
		args.add(-1L);
		map.put("arguments", args);
		eventOccurred(new TwitzEvent(this, TwitzEventType.USER_LISTS, new java.util.Date().getTime(), map));
		//Load blocked users
		map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", blocked);
		eventOccurred(new TwitzEvent(this, TwitzEventType.BLOCKING_USERS, new java.util.Date().getTime(), map));
		//Load followers list
		map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", followers);
		args = new ArrayList();
		args.add(config.getString("twitter_id"));//screenName
		args.add(-1L);
		map.put("arguments", args);
		eventOccurred(new TwitzEvent(this, TwitzEventType.FOLLOWERS_STATUSES, new java.util.Date().getTime(), map));
		//Load friends list
		map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", friends);
		args = new ArrayList();
		args.add(config.getString("twitter_id"));//screenName
		args.add(-1L);
		map.put("arguments", args);
		eventOccurred(new TwitzEvent(this, TwitzEventType.FRIENDS_STATUSES, new java.util.Date().getTime(), map));
	}//}}}

	@Action
	private void keyReleased(java.awt.event.KeyEvent evt) {//{{{
		int c = txtTweet.getDocument().getLength();
		lblChars.setText((140 - c)+"");
		if((c > 0) && (c < 141)) {
			btnTweet.setEnabled(true);
			lblChars.setForeground(getResourceMap().getColor("lblChars.foreground"));
		}
		else if(c > 140) {
			lblChars.setForeground(Color.RED);
			btnTweet.setEnabled(false);
		}
		else
		{
			btnTweet.setEnabled(false);
		}
	}//}}}

	@Action
	private void keyTyped(java.awt.event.KeyEvent evt) {//{{{
		switch(evt.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				//sendAsyncTweet();
				btnTweet.doClick();
				//sendTweetClicked().execute();
				break;
			case KeyEvent.VK_M:
				if(evt.isControlDown())
					showMiniMode();
				break;
			case KeyEvent.VK_ESCAPE:
				mainApp.toggleWindowView("down");
				break;
		//	default:
		//		int c = txtTweet.getDocument().getLength();
		//		lblChars.setText((140 - c)+"");
		//		if((c > 0) && (c < 141)) {
		//			btnTweet.setEnabled(true);
		//			lblChars.setForeground(getResourceMap().getColor("lblChars.foreground"));
		//		}
		//		else if(c > 140) {
		//			lblChars.setForeground(Color.RED);
		//			btnTweet.setEnabled(false);
		//		}
		//		else
		//		{
		//			btnTweet.setEnabled(false);
		//		}
		}
	}//}}}

	private JMenu createTabPositionMenu() {//{{{
		ButtonGroup g = new ButtonGroup();
		JMenu rv = new JMenu(getResourceMap().getString("menuTabPosition.text"));
	//	rv.setActionCommand();
	//	rv.addActionListener(this);
		rv.setIcon(getResourceMap().getIcon("icon.tab_go"));

//		Top
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(getResourceMap().getString("menuItemTabUp.text"));
		item.setActionCommand("tabs_up");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.arrow_up"));
		item.setSelected(config.getString("tab_position").equals("north"));
		rv.add(item);
		g.add(item);

//		Bottom
		item = new JCheckBoxMenuItem(getResourceMap().getString("menuItemTabDown.text"));
		item.setActionCommand("tabs_down");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.arrow_down"));
		item.setSelected(config.getString("tab_position").equals("south"));
		rv.add(item);
		g.add(item);

//		Right
		item = new JCheckBoxMenuItem(getResourceMap().getString("menuItemTabRight.text"));
		item.setActionCommand("tabs_right");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.arrow_right"));
		item.setSelected(config.getString("tab_position").equals("east"));
		rv.add(item);
		g.add(item);

//		Left
		item = new JCheckBoxMenuItem(getResourceMap().getString("menuItemTabLeft.text"));
		item.setActionCommand("tabs_left");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.arrow_left"));
		item.setSelected(config.getString("tab_position").equals("west"));
		rv.add(item);
		g.add(item);

		return rv;
	}//}}}

	private JMenu createActionsMenu() {//{{{
		JMenu menu = new JMenu();
		JMenu users = new JMenu(resourceMap.getString("ACTIONS_MENU.TEXT"));
		//java.awt.Component selected = tabPane.getSelectedComponent();
		JPopupMenu actions = getActionsMenu(timelinePanel);
		MenuElement[] aelems = actions.getSubElements();
		for (MenuElement e : aelems) {
			if(e instanceof JMenuItem) {
				users.add((JMenuItem)e);
			}
			else if(e instanceof JMenu) {
				users.add((JMenu)e);
			}
		}

		menu.addMenuListener(new MenuListener() {//{{{

			public void menuSelected(MenuEvent e)
			{
				//System.out.println("Menu Selected");
				java.awt.Component o = tabPane.getSelectedComponent();
				Component c = actionsMenu.getMenuComponent(0);
				if(o.getName().equals("recentPane")) {
					if(recentList.getSelectedRow() == -1)
						c.setEnabled(false);
					else
						c.setEnabled(true);
				}
				else if(o.getName().equals("friendsPanel")) {
					if(friends.getSelectedIndex() == -1)
						c.setEnabled(false);
					else
						c.setEnabled(true);
				}
				else if(o.getName().equals("followingPane")) {
					if(followingList.getSelectedIndex() == -1)
						c.setEnabled(false);
					else
						c.setEnabled(true);
				}
				else if(o.getName().equals("followersPane")) {
					if(followersList.getSelectedIndex() == -1)
						c.setEnabled(false);
					else
						c.setEnabled(true);
				}
				else if(o.getName().equals("blockedPane")) {
					if(blockedList.getSelectedIndex() == -1)
						c.setEnabled(false);
					else
						c.setEnabled(true);
				}
			}

			public void menuDeselected(MenuEvent e)
			{
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void menuCanceled(MenuEvent e)
			{
				//throw new UnsupportedOperationException("Not supported yet.");
			}
		});//}}}
		menu.add(users);
		//Setup menus for all other twitter actions
		return menu;
	}//}}}

	/**
	 * Method builds the Action menu that is displayed application wide
	 * when a user is clicked on.
	 * @return A JPopupMenu
	 */
	public JPopupMenu getActionsMenu(Component caller) {//{{{
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

		ActionListener actions = null;

		boolean selected = true;
		if(caller != null)
		{
			if (caller instanceof JTable)
			{
				JTable t = (JTable) caller;
				if (t.getSelectedRow() == -1)
				{
					selected = false;
				}
			}
			else if (caller instanceof ContactsList)
			{
				ContactsList l = (ContactsList) caller;
				cl = (ContactsList) caller;
				if (l.getSelectedIndex() == -1)
				{
					selected = false;
				}
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
				actions = ulp;
			}
			else if (caller instanceof StatusList)
			{
				tl = (StatusList) caller;
				if (tl.getSelectedIndex() == -1)
				{
					selected = false;
				}
				actions = tl;
			}
			else if(caller instanceof StatusPanel)
			{
				sp = (StatusPanel)caller;
				if(sp.getStatusList().getSelectedIndex() == -1)
				{
					selected = false;
				}
				actions = sp;
			}
			else if(caller instanceof TimeLinePanel)
			{
				tlp = (TimeLinePanel)caller;
				if(tlp.getStatusList().getSelectedIndex() == -1)
				{
					selected = false;
				}
				actions = tlp;
			}
			else if(caller instanceof FriendsPanel)
			{
				fp = (FriendsPanel)caller;
				if(fp.getContactsList().getSelectedIndex() == -1)
				{
					selected = false;
				}
				actions = fp;
			}
			else if(caller instanceof BlockedPanel)
			{
				bp = (BlockedPanel)caller;
				if(bp.getContactsList().getSelectedIndex() == -1)
				{
					selected = false;
				}
				actions = bp;
			}
			else if(caller instanceof FollowersPanel)
			{
				flp = (FollowersPanel)caller;
				if(flp.getContactsList().getSelectedIndex() == -1)
				{
					selected = false;
				}
				actions = flp;
			}
		}
		JPopupMenu menu = new JPopupMenu(getResourceMap().getString("ACTIONS"));
		menu.setFocusable(false);
		menu.setLabel(getResourceMap().getString("ACTIONS"));

		JMenuItem item = new JMenuItem(getResourceMap().getString("REPORT_SPAM"));
		//item.setActionCommand(TwitterConstants.REPORT_SPAM+"");
		item.setActionCommand("REPORT_SPAM");
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setIcon(getResourceMap().getIcon("icon.bomb"));
		item.setEnabled(selected);
		item.setFocusable(false);
		menu.add(item);
		menu.addSeparator();

		JMenu sub = new JMenu(getResourceMap().getString("BLOCKING_USERS"));
		sub.setIcon(getResourceMap().getIcon("icon.stop"));
		sub.setFocusable(false);
		item = new JMenuItem(getResourceMap().getString("CREATE_BLOCK"));
		item.setActionCommand("CREATE_BLOCK");
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setIcon(getResourceMap().getIcon("icon.stop"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("DESTROY_BLOCK"));
		item.setActionCommand("DESTROY_BLOCK");
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setIcon(getResourceMap().getIcon("icon.stop"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);
		menu.add(sub);

		sub = new JMenu(getResourceMap().getString("FRIENDSHIPS"));
		sub.setIcon(getResourceMap().getIcon("icon.user"));
		sub.setFocusable(false);
		item = new JMenuItem(getResourceMap().getString("CREATE_FRIENDSHIP"));
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setActionCommand("CREATE_FRIENDSHIP");
		item.setIcon(getResourceMap().getIcon("icon.user_add"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("DESTROY_FRIENDSHIP"));
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setActionCommand("DESTROY_FRIENDSHIP");
		item.setIcon(getResourceMap().getIcon("icon.user_delete"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("EXISTS_FRIENDSHIP"));
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setActionCommand("EXISTS_FRIENDSHIP");
		item.setIcon(getResourceMap().getIcon("icon.user_go"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("SHOW_FRIENDSHIP"));
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setActionCommand("SHOW_FRIENDSHIP");
		item.setIcon(getResourceMap().getIcon("icon.user_gray"));
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
		item.setActionCommand("SEND_DIRECT_MESSAGE");
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setIcon(getResourceMap().getIcon("icon.user_comment"));
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

		sub = new JMenu(getResourceMap().getString("LOCATION"));
		sub.setIcon(getResourceMap().getIcon("icon.map"));
		sub.setFocusable(false);
		item = new JMenuItem(getResourceMap().getString("NEAR_BY_PLACES"));
		item.setActionCommand("NEAR_BY_PLACES");
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setIcon(getResourceMap().getIcon("icon.map"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("GEO_DETAILS"));
		item.setActionCommand("GEO_DETAILS");
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.addActionListener(actions);
		item.setIcon(getResourceMap().getIcon("icon.map_edit"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);
		menu.add(sub);

//ADD_LIST_MEMBER
//DELETE_LIST_MEMBER
//CHECK_LIST_MEMBERSHIP
		sub = new JMenu(getResourceMap().getString("USER_LISTS"));
		sub.setIcon(getResourceMap().getIcon("icon.group"));
		sub.setFocusable(false);
		item = new JMenuItem(getResourceMap().getString("ADD_LIST_MEMBER"));
		item.setActionCommand("ADD_LIST_MEMBER");
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.addActionListener(actions);
		item.setIcon(getResourceMap().getIcon("icon.group_add"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("DELETE_LIST_MEMBER"));
		item.setActionCommand("DELETE_LIST_MEMBER");
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setIcon(getResourceMap().getIcon("icon.group_delete"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);

		item = new JMenuItem(getResourceMap().getString("CHECK_LIST_MEMBERSHIP"));
		item.setActionCommand("CHECK_LIST_MEMBERSHIP");
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setIcon(getResourceMap().getIcon("icon.group_gear"));
		item.setEnabled(selected);
		item.setFocusable(false);
		sub.add(item);
		menu.add(sub);

	//USER_TIMELINE
		item = new JMenuItem(getResourceMap().getString("USER_TIMELINE"));
		item.setActionCommand("USER_TIMELINE");
		if(actions != null) {
			item.addActionListener(actions);
		}
		item.setIcon(getResourceMap().getIcon("icon.timeline_marker"));
		item.setEnabled(selected);
		item.setFocusable(false);
		menu.add(item);

		//menu.setEnabled(selected);

		return menu;
	}//}}}
	//END private methods

	@Action
    public void showAboutBox() {//{{{
        if (aboutBox == null) {
            //JFrame mainFrame = TwitzApp.getApplication().getMainFrame();
            aboutBox = new TwitzAboutBox(getMainFrame());
            aboutBox.setLocationRelativeTo(getMainFrame());
        }
        //TwitzApp.getApplication().show(aboutBox);
		aboutBox.setVisible(true);
    }//}}}

	@Action
	public void showPrefsBox() {//{{{
		if(prefs == null) {
			//JFrame mainFrame = TwitzApp.getApplication().getMainFrame();
			prefs = new PreferencesDialog(getMainFrame(), true, mainApp);
			prefs.addPropertyChangeListener(mainApp);
			prefs.setLocationRelativeTo(getMainFrame());
		}
		prefs.setVisible(true);
	}//}}}

	public JMenuBar getMenuBar() {
		return this.menuBar;
	}

	public void toggleTabs(java.awt.event.ActionEvent evt) {//{{{
		if (evt.getSource() instanceof javax.swing.JCheckBoxMenuItem)
		{
			javax.swing.JCheckBoxMenuItem item = (javax.swing.JCheckBoxMenuItem) evt.getSource();
			int index = tabPane.getTabCount();
			if (evt.getActionCommand().equals("Friends"))
			{
				config.setProperty("tab_friends", item.isSelected() + "");
				if (item.isSelected())
				{
					tabPane.insertTab(getResourceMap().getString("friendsPanel.TabConstraints.tabTitle"), getResourceMap().getIcon("friendsPanel.TabConstraints.tabIcon"), friendsPanel, getResourceMap().getString("friendsPanel.TabConstraints.tabTooltip"), 1);
					tabPane.setSelectedComponent(friendsPanel);
					//setTabComponent();
				}
				else
				{
					tabPane.remove(friendsPanel);
				}
			}
			else if (evt.getActionCommand().equals("Blocked"))
			{
				config.setProperty("tab_blocked", item.isSelected() + "");
				if (item.isSelected())
				{
					tabPane.insertTab(getResourceMap().getString("blockedPane.TabConstraints.tabTitle"), getResourceMap().getIcon("blockedPane.TabConstraints.tabIcon"), blockedPane, getResourceMap().getString("blockedPane.TabConstraints.tabTooltip"), index >= 2 ? 2 : index); // NOI18N
					tabPane.setSelectedComponent(blockedPane);
					//setTabComponent();
				}
				else
				{
					tabPane.remove(blockedPane);
				}
			}
			else if (evt.getActionCommand().equals("Following"))
			{
				config.setProperty("tab_following", item.isSelected() + "");
				if (item.isSelected())
				{
					tabPane.insertTab(getResourceMap().getString("followingPane.TabConstraints.tabTitle"), getResourceMap().getIcon("followingPane.TabConstraints.tabIcon"), followingPane, getResourceMap().getString("followingPane.TabConstraints.tabTooltip"), index >= 3 ? 3 : index); // NOI18N
					tabPane.setSelectedComponent(followingPane);
					//setTabComponent();
				}
				else
				{
					tabPane.remove(followingPane);
				}
			}
			else if (evt.getActionCommand().equals("Followers"))
			{
				config.setProperty("tab_followers", item.isSelected() + "");
				if (item.isSelected())
				{
					tabPane.insertTab(getResourceMap().getString("followersPane.TabConstraints.tabTitle"), getResourceMap().getIcon("followersPane.TabConstraints.tabIcon"), followersPane, getResourceMap().getString("followersPane.TabConstraints.tabTooltip"), index >= 4 ? 4 : index); // NOI18N
					tabPane.setSelectedComponent(followersPane);
					//setTabComponent();
				}
				else
				{
					tabPane.remove(followersPane);
				}
			}
			else if (evt.getActionCommand().equals("Search"))
			{
				config.setProperty("tab_search", item.isSelected() + "");
				if (item.isSelected())
				{
					tabPane.addTab(getResourceMap().getString("searchPanel.TabConstraints.tabTitle"), getResourceMap().getIcon("searchPanel.tabIcon"), searchPanel, getResourceMap().getString("searchPanel.TabConstraints.tabTooltip")); // NOI18N
					tabPane.setSelectedComponent(searchPanel);
					//setTabComponent();
				}
				else
				{
					tabPane.remove(searchPanel);
				}
			}
		}
	}//}}}

	private void setTabPlacement() {//{{{
		//JTabbedPane.TOP JTabbedPane.BOTTOM JTabbedPane.LEFT JTabbedPane.RIGHT
		if(config.getString("tab_position").equals("north")) {
			tabPane.setTabPlacement(JTabbedPane.TOP);
		}
		else if(config.getString("tab_position").equals("south")) {
			tabPane.setTabPlacement(JTabbedPane.BOTTOM);
		}
		else if(config.getString("tab_position").equals("east")) {
			tabPane.setTabPlacement(JTabbedPane.RIGHT);
		}
		else if(config.getString("tab_position").equals("west")) {
			tabPane.setTabPlacement(JTabbedPane.LEFT);
		}
	}//}}}

	public static ResourceMap getResourceMap() {
		return TwitzApp.getContext().getResourceMap(TwitzMainView.class);
	}

	public JFrame getMainFrame() {
		return mainApp.getMainFrame();
	}

	public TwitzApp getMainApp()
	{
		return this.mainApp;
	}
	
	/**
	 * This method is used to display visual error dialogs
	 *
	 * @param t Any Throwable, this is used to gain the stacktrace
	 * @param title The title of the error dialog
	 * @param message The error message to be displayed
	 * @param method The TwitterMethod object that caused this error. This can be null
	 */
	public void displayError(final Throwable t, final String title, final Object message, final twitter4j.TwitterMethod method, final boolean showprefs) {//{{{
		Runnable doRun = new Runnable() {
			public void run() {
				Object[] options = {"Ok", "Stack Trace"};
				int rv = JOptionPane.showOptionDialog(getMainFrame(), message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, getResourceMap().getIcon("error.icon"), options, options[0]);
				switch (rv)
				{
					case 0:
						if(showprefs && (prefs != null && !prefs.isVisible()))
							showPrefsBox();
						break;
					case 1:
						//show the full stack trace here
						MessageDialog msg = new MessageDialog(getMainFrame(), false);
						msg.setLocationRelativeTo(getMainFrame());
						java.io.StringWriter w = new java.io.StringWriter();
						java.io.PrintWriter p = new java.io.PrintWriter(w);
						t.printStackTrace(p);
						p.flush();
						msg.setMessage(w.toString());
						msg.setVisible(true);
						if(showprefs && (prefs != null && !prefs.isVisible()))
							showPrefsBox();
						break;
				}
			}
		};
		SwingUtilities.invokeLater(doRun);
	}//}}}

	@Action
	public void showMiniMode() {//{{{
		if(minimode) {
			fullTwitz();
		}
		else {
			miniTwitz(false);
		}
	}//}}}

	public boolean isMiniMode()//{{{
	{
		return minimode;
	}//}}}

	/**
	 * Display only a miniature version of Twitz
	 */
	public void miniTwitz(boolean startup) {//{{{
		java.awt.Rectangle rec = null;
		if(startup)
			oldHeight = config.getInteger("twitz_last_height");
		else
		{
			oldHeight = getMainFrame().getHeight();
			rec = getBounds();
		}
		if(!startup)
			config.setProperty("twitz_last_height", oldHeight+"");
		tabPane.setVisible(false);
		//getMainFrame().getStatusBar().setVisible(false);
		menuBar.setVisible(false);
		setSize(getWidth(), config.getBoolean("twitz_undecorated") ? 90 : 110);
		mainApp.fixIFrameLocation(this);
		if(rec != null)
		{
			int bottom = rec.y + rec.height;
			java.awt.Rectangle newrec = getBounds();
			int top = bottom - newrec.height;
			newrec.setLocation(newrec.x, top);
			setLocation(newrec.getLocation());
		}
		tweetBox.setMiniIcon(getResourceMap().getIcon("btnMini.up.icon"));
		//btnMini.setIcon(getResourceMap().getIcon("btnMini.up.icon"));
		minimode = true;
		config.setProperty("minimode", minimode+""); //Update the config file so it starts in the same mode
	}//}}}

	/**
	 * Display the full Twitz window
	 */
	public void fullTwitz() {//{{{
		tabPane.setVisible(true);
		//getMainFrame().getStatusBar().setVisible(true);
		menuBar.setVisible(true);
		setSize(getWidth(), config.getInteger("twitz_last_height"));
		mainApp.fixIFrameLocation(this);
		//btnMini.setIcon(getResourceMap().getIcon("btnMini.icon"));
		tweetBox.setMiniIcon(getResourceMap().getIcon("btnMini.icon"));
		minimode = false;
		config.setProperty("minimode", minimode+""); //Update the config file so it starts in the same mode
	}//}}}

	public boolean isConnected() {
		return connected;
	}

	private boolean isUser(Object obj) {
		return (obj instanceof User);
	}

	private boolean isUserArray(Object obj) {
		return (obj instanceof User[]);
	}

	private boolean isStatus(Object obj) {
		return (obj instanceof Status);
	}

	private boolean isStatusArray(Object obj) {
		return (obj instanceof Status[]);
	}

	private String getScreenNameFromMap(Object obj) {//{{{
		String screenName = "";
		User u = null;
		if (isUserArray(obj))
		{
			if(logdebug)
				logger.debug("Found User array");
			User[] users = (User[]) obj;
			screenName = users[0].getScreenName();
		}
		else if (isStatusArray(obj))
		{
			if(logdebug)
				logger.debug("Found Status array");
			Status[] stat = (Status[]) obj;
			u = stat[0].getUser();
			screenName = u.getScreenName();
		}
		else if (isUser(obj))
		{
			u = (User) obj;
			screenName = u.getScreenName();
		}
		else if (isStatus(obj))
		{
			Status stat = (Status) obj;
			u = stat.getUser();
			screenName = u.getScreenName();
		}
		return screenName;
	}//}}}

	private Vector<String> getScreenNamesFromMap(Object obj) {//{{{
		String screenName = "";
		Vector<String> rv = new Vector<String>();
		User u = null;
		if (isUserArray(obj))
		{
			User[] users = (User[]) obj;
			if(users.length >= 2) {
				rv.addElement(users[0].getScreenName());
				rv.addElement(users[1].getScreenName());
			}
		}
		else if (isStatusArray(obj))
		{
			Status[] stat = (Status[]) obj;
			if(stat.length >= 2) {
				u = stat[0].getUser();
				rv.addElement(u.getScreenName());
				u = stat[1].getUser();
				rv.addElement(u.getScreenName());
			}
		}

		return rv;
	}//}}}

	//TwitterListener abstract methods
	// <editor-fold defaultstate="collapsed" desc="Abstract Methods">

	//TwitzListener
	/**
	 * Event Processor for all events that are related to twitter activity in the {@link twitz.twitter.TwitterManager}
	 * A TwitzEvent will will have and type.
	 * @param t
	 */
	public void eventOccurred(TwitzEvent t)//{{{
	{
		//int type = t.getEventType();
		String screenName = null;
		Component caller = null;
		Map eventMap = null;
		boolean async = true;
		ArrayList args = null;

		if(t.getEventMap() != null) {
			eventMap = t.getEventMap();
			if(eventMap.containsKey("caller"))
				caller = (Component) eventMap.get("caller");
			if(eventMap.containsKey("async"))
				async = (Boolean)eventMap.get("async");
			if(eventMap.containsKey("arguments"))
				args = (ArrayList)eventMap.get("arguments");
		}
				//run action performed
		TwitzEventType type = t.getEventType();
		switch(type)
		{
			case TREND_SEARCH:
				if(args != null && args.size() == 1)
				{
					if(tabPane.indexOfComponent(searchPanel) == -1)
					{
						toggleTabs(new java.awt.event.ActionEvent(this, 2344, "Search"));
					}
					else
					{
						tabPane.setSelectedComponent(searchPanel);
					}
					searchPanel.setSearchText((String)args.get(0));
					searchPanel.setCurrentPage(1);
					searchPanel.doSearch();
				}
			return;
			case LIST_MEMBERS: //do this here to avoid threading issues with the ArrayDeque
				if(args != null && args.size() == 3)
				{
					que.add(caller); 
				}
			break;
			case SEND_DIRECT_MESSAGE:
				logger.info("Send Direct Meessage Clicked");
				User u = null;
				if(eventMap != null) {
					screenName = getScreenNameFromMap(eventMap.get("selections"));
				}
				if(!screenName.equals("")) {
					String text = JOptionPane.showInputDialog("Compose Message for " + screenName);
					if (text != null)
					{
						args = new ArrayList();
						args.add(screenName);
						args.add(text);
						Map m = t.getEventMap();
						m.put("arguments", args);
					}
				}
			break;
			case EXISTS_FRIENDSHIP:
				if(eventMap != null) {
					names = getScreenNamesFromMap(eventMap.get("selections"));
				}
			break;
			case HOME_TIMELINE:
			case FRIENDS_TIMELINE:
			case USER_TIMELINE:
			case MENTIONS:
			case RETWEETED_BY_ME:
			case RETWEETED_TO_ME:
			case RETWEETS_OF_ME:
				if(caller != null)
					timelineQue.add(caller);
			break;
		}
		twitz.events.TwitzEventHandler handler = new twitz.events.TwitzEventHandler(t, sessionName);
		handler.addPropertyChangeListener(mainApp.getTrayIcon());
		handler.addPropertyChangeListener(statusListener);
		handler.exec();
//		(new twitz.events.TwitzEventHandler(t, twitterManager)).execute();
	} //}}}

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
	public void actionPerformed(ActionEvent evt) {//{{{
		//System.out.println(evt.getSource().toString());
		String screenName = null;
		String command = evt.getActionCommand();
		if(command.equals("tabs_up")) {
			config.setProperty("tab_position", "north");
			tabPane.setTabPlacement(JTabbedPane.TOP);
			//JTabbedPane.TOP JTabbedPane.BOTTOM JTabbedPane.LEFT JTabbedPane.RIGHT
		}
		else if(command.equals("tabs_down")) {
			config.setProperty("tab_position", "south");
			tabPane.setTabPlacement(JTabbedPane.BOTTOM);
		}
		else if(command.equals("tabs_right")) {
			config.setProperty("tab_position", "east");
			tabPane.setTabPlacement(JTabbedPane.RIGHT);
		}
		else if(command.equals("tabs_left")) {
			config.setProperty("tab_position", "west");
			tabPane.setTabPlacement(JTabbedPane.LEFT);
		}
		else {
			Map map = Collections.synchronizedMap(new TreeMap());
			map.put("caller", evt.getSource());
			map.put("async", true);
			User[] selections = new User[4];//getContactsList().getSelectedValues();
			map.put("selections", selections);
			logger.debug("Got action to perform"); 
			eventOccurred(new TwitzEvent(this, TwitzEventType.valueOf(evt.getActionCommand()),
						new java.util.Date().getTime()));
		}
	}//}}}

	// TwitterListener//{{{
	public void searched(QueryResult queryResult)//{{{
	{
		searchPanel.updateTweetsList(queryResult);
	}//}}}

	public void gotTrends(Trends trends)//{{{
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotTrends(Trends trends) Not supported yet","2"});
	}//}}}

	public void gotCurrentTrends(Trends trends)//{{{
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotCurrentTrends(Trends trends) Not supported yet","2"});
	}//}}}

	public void gotDailyTrends(List<Trends> trendsList)//{{{
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotDailyTrends(List<Trends> trendsList) Not supported yet","2"});
	}//}}}

	public void gotWeeklyTrends(List<Trends> trendsList)//{{{
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotWeeklyTrends(List<Trends> trendsList) Not supported yet","2"});
	}//}}}

	public void gotPublicTimeline(ResponseList<Status> statuses)//{{{
	{
		Object o = timelineQue.peek();
		if(o instanceof TimeLinePanel)
		{
			TimeLinePanel panel = (TimeLinePanel)timelineQue.poll();
			panel.updateStatus(statuses);
		}
		else
		{
			friendsStatusPanel.updateStatus(statuses);
		}
	}//}}}

	public void gotHomeTimeline(ResponseList<Status> statuses)//{{{
	{
		Object o = timelineQue.peek();
		if(o instanceof TimeLinePanel)
		{
			TimeLinePanel panel = (TimeLinePanel)timelineQue.poll();
			panel.updateStatus(statuses);
		}
		else
		{
			friendsStatusPanel.updateStatus(statuses);
		}
		//timelinePanel.updateStatus(statuses);
	}//}}}

	public void gotFriendsTimeline(ResponseList<Status> statuses)//{{{
	{
		Object o = timelineQue.peek();
		if(o instanceof TimeLinePanel)
		{
			TimeLinePanel panel = (TimeLinePanel)timelineQue.poll();
			panel.updateStatus(statuses);
		}
		else
		{
			friendsStatusPanel.updateStatus(statuses);
		}
		//timelinePanel.updateStatus(statuses);
	}//}}}

	public void gotUserTimeline(ResponseList<Status> statuses)//{{{
	{
		Object o = timelineQue.peek();
		if(o instanceof TimeLinePanel)
		{
			TimeLinePanel panel = (TimeLinePanel)timelineQue.poll();
			panel.updateStatus(statuses);
		}
		else
		{
			friendsStatusPanel.updateStatus(statuses);
		}
		//timelinePanel.updateStatus(statuses);
	}//}}}

	public void gotMentions(ResponseList<Status> statuses)//{{{
	{
		Object o = timelineQue.peek();
		if(o instanceof TimeLinePanel)
		{
			TimeLinePanel panel = (TimeLinePanel)timelineQue.poll();
			panel.updateStatus(statuses);
		}
		else
		{
			friendsStatusPanel.updateStatus(statuses);
		}
		//timelinePanel.updateStatus(statuses);
	}//}}}

	public void gotRetweetedByMe(ResponseList<Status> statuses)//{{{
	{
		Object o = timelineQue.peek();
		if(o instanceof TimeLinePanel)
		{
			TimeLinePanel panel = (TimeLinePanel)timelineQue.poll();
			panel.updateStatus(statuses);
		}
		else
		{
			friendsStatusPanel.updateStatus(statuses);
		}
		//timelinePanel.updateStatus(statuses);
	}//}}}

	public void gotRetweetedToMe(ResponseList<Status> statuses)//{{{
	{
		Object o = timelineQue.peek();
		if(o instanceof TimeLinePanel)
		{
			TimeLinePanel panel = (TimeLinePanel)timelineQue.poll();
			panel.updateStatus(statuses);
		}
		else
		{
			friendsStatusPanel.updateStatus(statuses);
		}
		//timelinePanel.updateStatus(statuses);
	}//}}}

	public void gotRetweetsOfMe(ResponseList statuses)//{{{
	{
		Object o = timelineQue.peek();
		if(o instanceof TimeLinePanel)
		{
			TimeLinePanel panel = (TimeLinePanel)timelineQue.poll();
			panel.updateStatus(statuses);
		}
		else
		{
			friendsStatusPanel.updateStatus(statuses);
		}
		//timelinePanel.updateStatus(statuses);
	}//}}}

	public void gotShowStatus(Status status)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotShowStatus(Status status) Not supported yet","2"});
	}

	public void updatedStatus(Status status)//{{{
	{
		//txtTweet.setText("");
		tweetBox.clearTweetText();
		tweetBox.setButtonEnabled(false);
		//btnTweet.setEnabled(false);

		//I need to just reload the timeline list here instead of adding this status
		//that us more expensive but is the best way since I wont know before hand 
		//which timeline is showing when the user tweets. just call getHomeTimeline 
		//or getUserTimeline. the callback for those will update the list
		User u = status.getUser();
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", this);
		ArrayList args = new ArrayList();
		args.add(u.getScreenName());
		map.put("arguments", args);
		TwitzEvent te = new TwitzEvent(this, TwitzEventType.USER_TIMELINE, new Date().getTime(), map);
//		(new twitz.events.TwitzEventHandler(te, sessionName)).execute();
		twitz.events.TwitzEventHandler handler = new twitz.events.TwitzEventHandler(te, sessionName);
		handler.addPropertyChangeListener(mainApp.getTrayIcon());
		handler.addPropertyChangeListener(statusListener);
		handler.exec();
	}//}}}

	public void destroyedStatus(Status destroyedStatus)//{{{
	{
		//See explanation in above method
		User u = destroyedStatus.getUser();
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", this);
		ArrayList args = new ArrayList();
		args.add(u.getScreenName());
		map.put("arguments", args);
		TwitzEvent te = new TwitzEvent(this, TwitzEventType.USER_TIMELINE, new Date().getTime(), map);
		//(new twitz.events.TwitzEventHandler(te, sessionName)).execute();
		twitz.events.TwitzEventHandler handler = new twitz.events.TwitzEventHandler(te, sessionName);
		handler.addPropertyChangeListener(mainApp.getTrayIcon());
		handler.addPropertyChangeListener(statusListener);
		handler.exec();
	}//}}}

	public void retweetedStatus(Status retweetedStatus)//{{{
	{
		//See explanation in above method
		User u = retweetedStatus.getUser();
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", this);
		ArrayList args = new ArrayList();
		args.add(u.getScreenName());
		map.put("arguments", args);
		TwitzEvent te = new TwitzEvent(this, TwitzEventType.USER_TIMELINE, new Date().getTime(), map);
//		(new twitz.events.TwitzEventHandler(te, sessionName)).execute();
		twitz.events.TwitzEventHandler handler = new twitz.events.TwitzEventHandler(te, sessionName);
		handler.addPropertyChangeListener(mainApp.getTrayIcon());
		handler.addPropertyChangeListener(statusListener);
		handler.exec();
	}//}}}

	//ResponseList<Status> retweets
	public void gotRetweets(ResponseList retweets)//{{{
	{
		Object o = timelineQue.peek();
		if(o instanceof TimeLinePanel)
		{
			TimeLinePanel panel = (TimeLinePanel)timelineQue.poll();
			panel.updateStatus(retweets);
		}
		else
		{
			friendsStatusPanel.updateStatus(retweets);
		}
//		StatusListModel tlm = timelinePanel.getStatusList().getModel();
//		tlm.clear();
//		for(Object s : retweets) {
//			if(s instanceof Status)
//			{
//				tlm.addStatus((Status)s);
//			}
//		}
	}//}}}

	public void gotUserDetail(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotUserDetail(User user) Not supported yet","2"});
	}

	//ResponseList<User> users
	public void lookedupUsers(ResponseList users)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "lookedupUsers(ResponseList<User> users) Not supported yet","2"});
	}

	//ResponseList<User> userList
	public void searchedUser(ResponseList userList)
	{
		searchPanel.updateUsersList(userList);
	}

	//ResponseList<Category> category
	public void gotSuggestedUserCategories(ResponseList category)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotSuggestedUserCategories() Not supported yet","2"});
	}

	//ResponseList<User> users
	public void gotUserSuggestions(ResponseList users)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotUserSuggestions() Not supported yet","2"});
	}

	//PagableResponseList<User> users
	public void gotFriendsStatuses(PagableResponseList users)//{{{
	{
		//Now we update the friendsList
		friends.updateList(users);
	}//}}}

	//PagableResponseList<User> users
	public void gotFollowersStatuses(PagableResponseList users)//{{{
	{
		followers.updateList(users);
	}//}}}

	public void createdUserList(UserList userList)
	{
		this.userListMainPanel1.addUserList(userList);
		//firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void updatedUserList(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "updatedUserList(UserList userList) Not supported yet","2"});
	}

	//PagableResponseList<UserList> userLists
	public void gotUserLists(PagableResponseList<UserList> userLists) //{{{
	{
		userListMainPanel1.addUserList(userLists);
		//firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotUserLists(PagableResponseList<UserList> userLists) Not supported yet","2"});
	} //}}}

	public void gotShowUserList(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotShowUserList(UserList userList) Not supported yet","2"});
	}

	public void destroyedUserList(UserList userList) //{{{
	{
		this.userListMainPanel1.removeUserList(userList.getName());
	} //}}}

	public void gotUserListStatuses(ResponseList<Status> statuses) //{{{
	{
		this.friendsStatusPanel.updateStatus(statuses);
	} //}}}

	public void gotUserListMemberships(PagableResponseList<UserList> userLists)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotUserListMemberships() Not supported yet","2"});
	}

	public void gotUserListSubscriptions(PagableResponseList<UserList> userLists)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "gotUserListSubscriptions() Not supported yet","2"});
	}

	public void gotUserListMembers(PagableResponseList<User> users) //{{{
	{
		Object o = que.peek();
		if(o instanceof UserListPanel)
		{
			UserListPanel ulp = (UserListPanel) que.poll();
			ulp.updateList(users);
		}
	} //}}}

	public void addedUserListMember(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "addedUserListMember(UserList userList) Not supported yet","2"});
	}

	public void deletedUserListMember(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "deletedUserListMember() Not supported yet","2"});
	}

	public void checkedUserListMembership(User users)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "checkedUserListMembership() Not supported yet","2"});
	}

	public void gotUserListSubscribers(PagableResponseList<User> users)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void subscribedUserList(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void unsubscribedUserList(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void checkedUserListSubscription(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void gotDirectMessages(ResponseList<DirectMessage> messages)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void gotSentDirectMessages(ResponseList<DirectMessage> messages)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void sentDirectMessage(DirectMessage message)//{{{
	{
		String str[] = {"Twitz Message", getResourceMap().getString("DIRECT_MESSAGE_SENT.TEXT", message.getRecipientScreenName()), "2"};
		firePropertyChange("POPUP", new Object(), str);
	}//}}}

	public void destroyedDirectMessage(DirectMessage message)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void createdFriendship(User user)//{{{
	{
		//Load friends list
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", friends);
		ArrayList args = new ArrayList();
		args.add(config.getString("twitter_id"));//screenName
		args.add(-1L);
		map.put("arguments", args);
		eventOccurred(new TwitzEvent(this, TwitzEventType.FRIENDS_STATUSES, new java.util.Date().getTime(), map));
		String[] str = {"Twitz Message", getResourceMap().getString("FRIENDSHIP_CREATED.TEXT",user.getScreenName()),"2"};
		firePropertyChange("POPUP", new Object(), str);
	}//}}}

	public void destroyedFriendship(User user)//{{{
	{
		//Load friends list
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("async", true);
		map.put("caller", friends);
		ArrayList args = new ArrayList();
		args.add(config.getString("twitter_id"));//screenName
		args.add(-1L);
		map.put("arguments", args);
		eventOccurred(new TwitzEvent(this, TwitzEventType.FRIENDS_STATUSES, new java.util.Date().getTime(), map));
		String[] str = {"Twitz Message", getResourceMap().getString("FRIENDSHIP_DELETED.TEXT",user.getScreenName()),"2"};
		firePropertyChange("POPUP", new Object(), str);
	}//}}}

	public void gotExistsFriendship(boolean exists)//{{{
	{
		if(exists) {
			String[] str = {"Twitz Message", getResourceMap().getString("FRIENDSHIP_EXISTS.TEXT",names.get(0),names.get(1)),"2"};
			firePropertyChange("POPUP", new Object(), str);
		}
		else {
			String[] str = {"Twitz Message", getResourceMap().getString("FRIENDSHIP_NOT_EXISTS.TEXT",names.get(0),names.get(1)),"2"};
			firePropertyChange("POPUP", new Object(), str);
		}
	}//}}}

	public void gotShowFriendship(Relationship relationship)
	{
		//TODO create model for displaying relationships
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void gotIncomingFriendships(IDs ids)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void gotOutgoingFriendships(IDs ids)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void gotFriendsIDs(IDs ids)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void gotFollowersIDs(IDs ids)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void gotRateLimitStatus(RateLimitStatus rateLimitStatus)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void updatedDeliveryDevice(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void updatedProfileColors(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void updatedProfileImage(User user) //{{{
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Completed updating profile image","2"});
	}//}}}

	public void updatedProfileBackgroundImage(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void updatedProfile(User user) //{{{
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Profile update completed","2"});
	}//}}}

	public void gotFavorites(ResponseList<Status> statuses)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void createdFavorite(Status status)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void destroyedFavorite(Status status)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void enabledNotification(User user) //{{{
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOTIFICATION_ADDED.TEXT", user.getScreenName()),"2"});
	} //}}}

	public void disabledNotification(User user) //{{{
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOTIFICATION_REMOVED.TEXT", user.getScreenName()),"2"});
	} //}}}

	public void createdBlock(User user)//{{{
	{
		TwitzEvent te = new TwitzEvent(this, TwitzEventType.BLOCKING_USERS, new Date().getTime());
//		(new twitz.events.TwitzEventHandler(te, sessionName)).execute();
		twitz.events.TwitzEventHandler handler = new twitz.events.TwitzEventHandler(te, sessionName);
		handler.addPropertyChangeListener(mainApp.getTrayIcon());
		handler.addPropertyChangeListener(statusListener);
		handler.exec();
	}//}}}

	public void destroyedBlock(User user)//{{{
	{
		TwitzEvent te = new TwitzEvent(this, TwitzEventType.BLOCKING_USERS, new Date().getTime());
//		(new twitz.events.TwitzEventHandler(te, sessionName)).execute();
		twitz.events.TwitzEventHandler handler = new twitz.events.TwitzEventHandler(te, sessionName);
		handler.addPropertyChangeListener(mainApp.getTrayIcon());
		handler.addPropertyChangeListener(statusListener);
		handler.exec();
	}//}}}

	public void gotExistsBlock(boolean blockExists)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void gotBlockingUsers(ResponseList<User> blockingUsers)//{{{
	{
		blocked.updateList(blockingUsers);
	}//}}}

	public void gotBlockingUsersIDs(IDs blockingUsersIDs)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void reportedSpam(User reportedSpammer)
	{
		//TODO: flag this user for exclusion filter from further viewing if twitter.com does not do it.
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void gotAvailableTrends(ResponseList<Location> locations) //{{{
	{
		trendPanel.setLocations(locations);
	} //}}}

	public void gotLocationTrends(Trends trends) //{{{
	{
		trendPanel.setTrends(trends);
	} //}}}

	public void gotNearByPlaces(ResponseList<Place> places)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void gotReverseGeoCode(ResponseList<Place> places)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void gotGeoDetails(Place place)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", resourceMap.getString("NOT_SUPPORTED.TEXT"),"2"});
	}

	public void tested(boolean test)//{{{
	{
		connected = test;
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Connection to twitter.com has "+ (test ? "succeeded" : "failed!!!"),"2"});
		//Fire off all the startup tasks that populate the GUI
	}//}}}

	public void onException(TwitterException te, TwitterMethod method)//{{{
	{
		if(logdebug)
		{
			logger.debug(method);
		}
		logger.error(te);
		if(method != null && method.equals(TwitterMethod.UPDATE_STATUS))
		{
			//tweetBox.clearTweetText();
			tweetBox.setButtonEnabled(true);
			tweetBox.setTweetEnabled(true);
			//btnTweet.setEnabled(true);
			//txtTweet.setEnabled(true);
		}
		if(te.getStatusCode() == 401)
		{
			//Incorrect login or password, do something and return
			displayError(te, resourceMap.getString("ERROR_TITLE.TEXT"), resourceMap.getString("PASSWORD_ERROR.TEXT"), method, true);
			//showPrefsBox();
			return;
		}
		if(te.getStatusCode() == 404) 
		{
			if(method != null && method.equals(TwitterMethod.CHECK_LIST_MEMBERSHIP))
			{
				displayError(te, resourceMap.getString("USER_NOT_USERLIST_MEMBER.TITLE.TEXT"),
						resourceMap.getString("USER_NOT_USERLIST_MEMBER.TEXT"), method, false);
				return;
			}
			if(method != null)
			{
				displayError(te, resourceMap.getString("ERROR_TITLE.TEXT"), 
						resourceMap.getString("RESOURCE_NOT_FOUND_ERROR.TEXT"), method, false);
			}
			else
			{
				displayError(te, resourceMap.getString("ERROR_TITLE.TEXT"),
						resourceMap.getString("RESOURCE_NOT_FOUND_ERROR.TEXT"), null, false);
			}
			return;
		}

		
		if(method != null)
		{
			displayError(te, resourceMap.getString("ERROR_TITLE.TEXT"), 
					resourceMap.getString("DEFAULT_ERROR_MESSAGE.TEXT",twitterManager.getResourceMap().getString(method.name())),
					method, false);
		}
		else
		{
			displayError(te, resourceMap.getString("ERROR_TITLE.TEXT"), 
					resourceMap.getString("DEFAULT_NO_METHOD_MESSAGE.TEXT",te.getCause().getMessage()), null, false);
		}
	}//}}}
	//}}}
	//END abstract methods
	//</editor-fold>

	@Action
	public void viewHTMLLog()//{{{
	{
		MessageDialog msg = new MessageDialog(getMainFrame(), false);
		msg.setContentType("text/html");
		try
		{
			URI spec = SettingsManager.getConfigDirectory().toURI();
			String s = spec.toString()+"/logs/index.html";
			URL path = new URL(s);
			msg.setMessage(path);
			msg.setResizable(true);
			msg.setTitle(resourceMap.getString("LOG_WINDOW_TITLE.TEXT"));
			msg.setSize(640, 480);
			msg.setVisible(true);
			//InputStream is = this.getClass().getResourceAsStream(path);
		}
		catch (IOException ex)
		{
			logger.error(ex.getMessage(), ex);
		}
		//InputStream is = this.getClass().getResourceAsStream(path);
	}//}}}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JMenu actionsMenu;
    private javax.swing.JScrollPane blockedPane;
    private javax.swing.JButton btnMini;
    private javax.swing.JButton btnTweet;
    private javax.swing.JCheckBox chkCOT;
    private javax.swing.JPopupMenu contextMenu;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JList followersList;
    private javax.swing.JScrollPane followersPane;
    private javax.swing.JList followingList;
    private javax.swing.JScrollPane followingPane;
    private javax.swing.JTabbedPane friendsPane;
    private javax.swing.JSplitPane friendsPanel;
    private javax.swing.JMenuItem helpItem;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblChars;
    private javax.swing.JMenuItem logsMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JCheckBoxMenuItem menuItemBlocked;
    private javax.swing.JCheckBoxMenuItem menuItemFollowers;
    private javax.swing.JCheckBoxMenuItem menuItemFollowing;
    private javax.swing.JCheckBoxMenuItem menuItemFriends;
    private javax.swing.JCheckBoxMenuItem menuItemSearch;
    private javax.swing.JMenu menuTabs;
    private javax.swing.JMenuItem miniItem;
    private javax.swing.JMenuItem prefsItem;
    private javax.swing.JMenuItem prefsMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTable recentList;
    private javax.swing.JPanel recentPane;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JSplitPane timelineTrendsPane;
    private javax.swing.JTextField txtTweet;
    // End of variables declaration//GEN-END:variables

	
	private static SettingsManager config;
	int busyIconIndex = 0;
	private int oldHeight = 400;

	private UserListMainPanel userListMainPanel1;
	private ContactsList blockedList;
	private FriendsPanel friends;
	private FollowersPanel followers;
	//private ContactsList following = new ContactsList();
	private BlockedPanel blocked;
	private StatusList friendsTweets;
	private StatusPanel friendsStatusPanel;
	private TrendsPanel trendPanel;
	private TimeLinePanel timelinePanel;
	private SearchPanel searchPanel;
	private TweetBox tweetBox;

    private JDialog aboutBox;
	private PreferencesDialog prefs;
	private twitz.twitter.TwitterManager twitterManager;
	
	private static TwitzApp mainApp;
	
    private org.jdesktop.application.ResourceMap resourceMap;
    private javax.swing.ActionMap actionMap;

	private Logger logger = Logger.getLogger(TwitzMainView.class.getName());
	
	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private Vector<String> names = new Vector<String>();

	private boolean connected = false;
	private boolean minimode = false;
	private boolean logdebug = logger.isDebugEnabled();
	private boolean startMode = false;

	/**
	 * This que is used to track which part of the application is requesting an action
	 */
	private ArrayDeque que = new ArrayDeque();
	private ArrayDeque timelineQue = new ArrayDeque();
	private java.beans.PropertyChangeListener statusListener;
	//A Map to store all the statuses in the recentList table
	private Map<Long, Status> recentMap = new TreeMap<Long, Status>();
	public static final String DEVMODE_TEXT = System.getProperty("twitz_mode");
	private static final boolean DEVMODE = ("devmode".equals(DEVMODE_TEXT));
	private static TwitzMainView instance;

}
