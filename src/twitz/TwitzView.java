/*
 * TwitzView.java
 */

package twitz;

import java.awt.Component;
import javax.swing.ListSelectionModel;
import javax.swing.event.MenuEvent;
import twitz.dialogs.PreferencesDialog;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;
import org.jdesktop.application.Task;
import twitz.events.TwitzEvent;
import twitz.util.SettingsManager;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowFocusListener;
import java.util.List;
import java.util.logging.Level;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.MenuElement;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import twitter4j.*;
import twitz.dialogs.MessageDialog;
import twitz.events.TwitzListener;
import twitz.twitter.TwitterConstants;
import twitz.twitter.TwitterManager;

/**
 * The application's main frame.
 */
public class TwitzView extends FrameView implements TwitzListener, TwitterListener, ActionListener {

    public TwitzView(SingleFrameApplication app) {
        super(app);
		mainApp = (TwitzApp)app;
		tm = TwitterManager.getInstance(this);
		tm.addTwitzListener(this);
		//Initialize twitter
		aTwitter = tm.getAsyncTwitterInstance();
        initComponents();
		this.getFrame().setVisible(false);
		this.getFrame().setSize(400, 300);
		btnTweet.setEnabled(false);

		//look & feel setup
		//Default to full view
		fullTwitz();

		this.getFrame().addWindowFocusListener(new WindowFocusListener() {

			public void windowGainedFocus(WindowEvent e)
			{
				txtTweet.requestFocus();
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void windowLostFocus(WindowEvent e)
			{
				//throw new UnsupportedOperationException("Not supported yet.");
			}
		});

		//Configure the GUI from the defaults in the config file
		setupDefaults();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
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

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
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
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        tabPane = new javax.swing.JTabbedPane();
        recentPane = new javax.swing.JScrollPane();
        recentList = new javax.swing.JTable();
        friendsPane = new javax.swing.JSplitPane();
        nameListPane = new javax.swing.JScrollPane();
        friendsNameList = new javax.swing.JList();
        tweetsPane = new javax.swing.JScrollPane();
        friendsList = new javax.swing.JList();
        blockedPane = new javax.swing.JScrollPane();
        blockedList = new javax.swing.JList();
        followingPane = new javax.swing.JScrollPane();
        followingList = new javax.swing.JList();
        followersPane = new javax.swing.JScrollPane();
        followersList = new javax.swing.JList();
        searchPanel = new javax.swing.JPanel();
        searchBar = new javax.swing.JToolBar();
        btnExitSearch = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        searchPane = new javax.swing.JScrollPane();
        tblSearch = new javax.swing.JTable();
        actionPanel = new javax.swing.JPanel();
        txtTweet = new javax.swing.JTextField();
        chkCOT = new javax.swing.JCheckBox();
        btnTweet = new javax.swing.JButton();
        btnMini = new javax.swing.JButton();
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
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        contextMenu = new javax.swing.JPopupMenu();
        prefsItem = new javax.swing.JMenuItem();
        miniItem = new javax.swing.JMenuItem();
        helpItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        exitItem = new javax.swing.JMenuItem();

        mainPanel.setComponentPopupMenu(contextMenu);
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mainPanelKeyReleased(evt);
            }
        });

        tabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPane.setInheritsPopupMenu(true);
        tabPane.setName("tabPane"); // NOI18N
        tabPane.setNextFocusableComponent(txtTweet);
        tabPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tabPaneKeyReleased(evt);
            }
        });

        recentPane.setName("recentPane"); // NOI18N

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
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        recentList.setFillsViewportHeight(true);
        recentList.setName("recentList"); // NOI18N
        recentList.setRowHeight(32);
        recentList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        recentList.setShowHorizontalLines(false);
        recentList.setShowVerticalLines(false);
        recentPane.setViewportView(recentList);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(TwitzView.class);
        tabPane.addTab(resourceMap.getString("recentPane.TabConstraints.tabTitle"), resourceMap.getIcon("recentPane.TabConstraints.tabIcon"), recentPane); // NOI18N

        friendsPane.setDividerLocation(100);
        friendsPane.setDividerSize(6);
        friendsPane.setName("friendsPane"); // NOI18N

        nameListPane.setName("nameListPane"); // NOI18N

        friendsNameList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        friendsNameList.setName("friendsNameList"); // NOI18N
        nameListPane.setViewportView(friendsNameList);

        friendsPane.setLeftComponent(nameListPane);

        tweetsPane.setName("tweetsPane"); // NOI18N

        friendsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        friendsList.setName("friendsList"); // NOI18N
        tweetsPane.setViewportView(friendsList);

        friendsPane.setRightComponent(tweetsPane);

        tabPane.addTab(resourceMap.getString("friendsPane.TabConstraints.tabTitle"), resourceMap.getIcon("friendsPane.TabConstraints.tabIcon"), friendsPane); // NOI18N

        blockedPane.setName("blockedPane"); // NOI18N

        blockedList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        blockedList.setName("blockedList"); // NOI18N
        blockedPane.setViewportView(blockedList);

        tabPane.addTab(resourceMap.getString("blockedPane.TabConstraints.tabTitle"), resourceMap.getIcon("blockedPane.TabConstraints.tabIcon"), blockedPane); // NOI18N

        followingPane.setName("followingPane"); // NOI18N

        followingList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        followingList.setName("followingList"); // NOI18N
        followingPane.setViewportView(followingList);

        tabPane.addTab(resourceMap.getString("followingPane.TabConstraints.tabTitle"), resourceMap.getIcon("followingPane.TabConstraints.tabIcon"), followingPane); // NOI18N

        followersPane.setName("followersPane"); // NOI18N

        followersList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        followersList.setName("followersList"); // NOI18N
        followersPane.setViewportView(followersList);

        tabPane.addTab(resourceMap.getString("followersPane.TabConstraints.tabTitle"), resourceMap.getIcon("followersPane.TabConstraints.tabIcon"), followersPane); // NOI18N

        searchPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        searchPanel.setName("searchPanel"); // NOI18N

        searchBar.setRollover(true);
        searchBar.setName("searchBar"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getActionMap(TwitzView.class, this);
        btnExitSearch.setAction(actionMap.get("hideSearchTab")); // NOI18N
        btnExitSearch.setIcon(resourceMap.getIcon("btnExitSearch.icon")); // NOI18N
        btnExitSearch.setText(resourceMap.getString("btnExitSearch.text")); // NOI18N
        btnExitSearch.setToolTipText(resourceMap.getString("btnExitSearch.toolTipText")); // NOI18N
        btnExitSearch.setFocusable(false);
        btnExitSearch.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExitSearch.setName("btnExitSearch"); // NOI18N
        btnExitSearch.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        searchBar.add(btnExitSearch);

        txtSearch.setText(resourceMap.getString("txtSearch.text")); // NOI18N
        txtSearch.setName("txtSearch"); // NOI18N
        searchBar.add(txtSearch);

        btnSearch.setIcon(resourceMap.getIcon("btnSearch.icon")); // NOI18N
        btnSearch.setText(resourceMap.getString("btnSearch.text")); // NOI18N
        btnSearch.setFocusable(false);
        btnSearch.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSearch.setName("btnSearch"); // NOI18N
        searchBar.add(btnSearch);

        searchPane.setName("searchPane"); // NOI18N

        tblSearch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Results"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSearch.setFillsViewportHeight(true);
        tblSearch.setName("tblSearch"); // NOI18N
        tblSearch.setRowHeight(50);
        searchPane.setViewportView(tblSearch);

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(searchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
            .addComponent(searchPane, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchPane, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
        );

        tabPane.addTab(resourceMap.getString("searchPanel.TabConstraints.tabTitle"), resourceMap.getIcon("searchPanel.tabIcon"), searchPanel); // NOI18N

        actionPanel.setMinimumSize(new java.awt.Dimension(100, 50));
        actionPanel.setName("actionPanel"); // NOI18N

        txtTweet.setText(resourceMap.getString("txtTweet.text")); // NOI18N
        txtTweet.setName("txtTweet"); // NOI18N
        txtTweet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTweetKeyReleased(evt);
            }
        });

        chkCOT.setText(resourceMap.getString("chkCOT.text")); // NOI18N
        chkCOT.setToolTipText(resourceMap.getString("chkCOT.toolTipText")); // NOI18N
        chkCOT.setName("chkCOT"); // NOI18N

        btnTweet.setAction(actionMap.get("sendAsyncTweet")); // NOI18N
        btnTweet.setText(resourceMap.getString("btnTweet.text")); // NOI18N
        btnTweet.setToolTipText(resourceMap.getString("btnTweet.toolTipText")); // NOI18N
        btnTweet.setName("btnTweet"); // NOI18N

        btnMini.setAction(actionMap.get("showMiniMode")); // NOI18N
        btnMini.setIcon(resourceMap.getIcon("btnMini.icon")); // NOI18N
        btnMini.setText(resourceMap.getString("btnMini.text")); // NOI18N
        btnMini.setToolTipText(resourceMap.getString("btnMini.toolTipText")); // NOI18N
        btnMini.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnMini.setName("btnMini"); // NOI18N

        javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.setHorizontalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, actionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTweet, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTweet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkCOT)
                    .addComponent(btnTweet)
                    .addComponent(btnMini))
                .addContainerGap())
        );

        txtTweet.requestFocusInWindow();

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(actionPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        menuBar.add(helpMenu);

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
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 259, Short.MAX_VALUE)
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

        contextMenu.setName("contextMenu"); // NOI18N

        prefsItem.setAction(actionMap.get("showPrefsBox")); // NOI18N
        prefsItem.setText(resourceMap.getString("prefsItem.text")); // NOI18N
        prefsItem.setName("prefsItem"); // NOI18N
        contextMenu.add(prefsItem);

        miniItem.setAction(actionMap.get("showMiniMode")); // NOI18N
        miniItem.setText(resourceMap.getString("miniItem.text")); // NOI18N
        miniItem.setName("miniItem"); // NOI18N
        contextMenu.add(miniItem);

        helpItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        helpItem.setText(resourceMap.getString("helpItem.text")); // NOI18N
        helpItem.setName("helpItem"); // NOI18N
        contextMenu.add(helpItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        contextMenu.add(jSeparator1);

        exitItem.setAction(actionMap.get("quit")); // NOI18N
        exitItem.setText(resourceMap.getString("exitItem.text")); // NOI18N
        exitItem.setName("exitItem"); // NOI18N
        contextMenu.add(exitItem);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents


	//private methods
	private void txtTweetKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTweetKeyReleased
	{//GEN-HEADEREND:event_txtTweetKeyReleased
		keyTyped(evt);
	}//GEN-LAST:event_txtTweetKeyReleased

	private void tabPaneKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tabPaneKeyReleased
	{//GEN-HEADEREND:event_tabPaneKeyReleased
		keyTyped(evt);
	}//GEN-LAST:event_tabPaneKeyReleased

	private void mainPanelKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_mainPanelKeyReleased
	{//GEN-HEADEREND:event_mainPanelKeyReleased
		keyTyped(evt);
	}//GEN-LAST:event_mainPanelKeyReleased

	private void menuItemFollowersActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuItemFollowersActionPerformed
	{//GEN-HEADEREND:event_menuItemFollowersActionPerformed
		toggleTabs(evt);
	}//GEN-LAST:event_menuItemFollowersActionPerformed

	private void menuItemBlockedActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuItemBlockedActionPerformed
	{//GEN-HEADEREND:event_menuItemBlockedActionPerformed
		toggleTabs(evt);
	}//GEN-LAST:event_menuItemBlockedActionPerformed

	private void menuItemFollowingActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuItemFollowingActionPerformed
	{//GEN-HEADEREND:event_menuItemFollowingActionPerformed
		toggleTabs(evt);
	}//GEN-LAST:event_menuItemFollowingActionPerformed

	private void menuItemFriendsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuItemFriendsActionPerformed
	{//GEN-HEADEREND:event_menuItemFriendsActionPerformed
		toggleTabs(evt);
	}//GEN-LAST:event_menuItemFriendsActionPerformed

	private void menuItemSearchActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuItemSearchActionPerformed
	{//GEN-HEADEREND:event_menuItemSearchActionPerformed
		toggleTabs(evt);
	}//GEN-LAST:event_menuItemSearchActionPerformed

	/**
	 * This method is called on startup in the constructor.
	 * It is used to configure the GUI with the default from the config file.
	 */
	private void setupDefaults() {
		DefaultTableColumnModel cModel = (DefaultTableColumnModel) recentList.getColumnModel();
		cModel.getColumn(0).setMaxWidth(1);
		cModel.getColumn(1).setMaxWidth(1);
		
		if(startMode) //We closed in minimode
			miniTwitz();
		//Disable tabs that were removed by user
		if(!config.getBoolean("tab.friends")) {
			tabPane.remove(friendsPane);
			menuItemFriends.setSelected(false);
		}
		if(!config.getBoolean("tab.blocked")) {
			tabPane.remove(blockedPane);
			menuItemBlocked.setSelected(false);
		}
		if(!config.getBoolean("tab.following")) {
			tabPane.remove(followingPane);
			menuItemFollowing.setSelected(false);
		}
		if(!config.getBoolean("tab.followers")) {
			tabPane.remove(followersPane);
			menuItemFollowers.setSelected(false);
		}
		if(!config.getBoolean("tab.search")) {
			tabPane.remove(searchPanel);
			menuItemSearch.setSelected(false);
		}
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					java.awt.Point p = e.getPoint();
					if (e.getSource() instanceof JList)
					{
						JList list = (JList) e.getSource();
						int index = list.locationToIndex(p);
						if (index != -1)
						{ //Show menu only if list is not empty
							list.setSelectedIndex(index);
							getActionsMenu(list).show(list, p.x, p.y);
						}

					}
					else if (e.getSource() instanceof javax.swing.JTable)
					{
						javax.swing.JTable table = (javax.swing.JTable) e.getSource();
						int index = table.rowAtPoint(p);
						if(index != -1)
						{
							ListSelectionModel lsm = table.getSelectionModel();
							lsm.setLeadSelectionIndex(index);
							getActionsMenu(table).show(table, p.x, p.y);
						}
					}
				}
			}
		};
		friendsNameList.addMouseListener(mouseListener);
		//friendsNameList.setComponentPopupMenu(getActionsMenu());
		friendsList.addMouseListener(mouseListener);
		//friendsList.setComponentPopupMenu(getActionsMenu());
		recentList.addMouseListener(mouseListener);
		//tweetsList.addMouseListener(mouseListener);
		//tweetsList.setComponentPopupMenu(getActionsMenu());
	}

	@Action
	private void keyTyped(java.awt.event.KeyEvent evt) {
		switch(evt.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				sendAsyncTweet();
				//sendTweetClicked().execute();
				break;
			case KeyEvent.VK_M:
				if(evt.isControlDown())
					showMiniMode();
				break;
			case KeyEvent.VK_ESCAPE:
				mainApp.toggleWindowView("down");
				break;
			default:
				if(!txtTweet.getText().equals(""))
					btnTweet.setEnabled(true);
				else
					btnTweet.setEnabled(false);
		}
	}

	@Action
	public void hideSearchTab()
	{
		tabPane.remove(searchPanel);
		menuItemSearch.setSelected(false);
	}
	
	private JPopupMenu getTweetsMenu() {
		JPopupMenu menu = new JPopupMenu();
		return menu;
	}

	private JMenu createActionsMenu() {
		JMenu menu = new JMenu();
		JMenu users = new JMenu("User Actions");
		java.awt.Component selected = tabPane.getSelectedComponent();
		JPopupMenu actions = getActionsMenu(selected);
		MenuElement[] aelems = actions.getSubElements();
		for (MenuElement e : aelems) {
			if(e instanceof JMenuItem) {
				users.add((JMenuItem)e);
			}
			else if(e instanceof JMenu) {
				users.add((JMenu)e);
			}
		}

		menu.addMenuListener(new MenuListener() {

			public void menuSelected(MenuEvent e)
			{
				System.out.println("Menu Selected");
				java.awt.Component o = tabPane.getSelectedComponent();
				Component c = actionsMenu.getMenuComponent(0);
				if(o.getName().equals("recentPane")) {
					if(recentList.getSelectedRow() == -1)
						c.setEnabled(false);
					else
						c.setEnabled(true);
				}
				else if(o.getName().equals("friendsPane")) {
					if(friendsList.getSelectedIndex() == -1)
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
		});
		menu.add(users);
		//Setup menus for all other twitter actions
		return menu;
	}

	/**
	 * Method builds the Action menu that is displayed application wide
	 * when a user is clicked on.
	 * @return A JPopupMenu
	 */
	private JPopupMenu getActionsMenu(Object caller) {
//RETWEETED_BY_ME
//RETWEETED_TO_ME
//RETWEETS_OF_ME
//SHOW_STATUS
//SHOW_USER
		boolean selected = true;
		if(caller instanceof JTable) {
			JTable t = (JTable)caller;
			if(t.getSelectedRow() == -1)
				selected = false;
		}
		else if(caller instanceof JList) {
			JList l = (JList)caller;
			if(l.getSelectedIndex() == -1)
				selected = false;
		}
		JPopupMenu menu = new JPopupMenu(resource.getString("ACTIONS"));
		menu.setLabel(resource.getString("ACTIONS"));

		JMenuItem item = new JMenuItem(resource.getString("REPORT_SPAM"));
		item.setActionCommand(TwitterConstants.REPORT_SPAM+"");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.bomb"));
		item.setEnabled(selected);
		menu.add(item);
		menu.addSeparator();

		JMenu sub = new JMenu(resource.getString("BLOCKING_USERS"));
		sub.setIcon(getResourceMap().getIcon("icon.stop"));
		item = new JMenuItem(resource.getString("CREATE_BLOCK"));
		item.setActionCommand(TwitterConstants.CREATE_BLOCK+"");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.stop"));
		item.setEnabled(selected);
		sub.add(item);

		item = new JMenuItem(resource.getString("DESTROY_BLOCK"));
		item.setActionCommand(TwitterConstants.DESTROY_BLOCK+"");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.stop"));
		item.setEnabled(selected);
		sub.add(item);
		menu.add(sub);

		sub = new JMenu(resource.getString("FRIENDSHIPS"));
		sub.setIcon(getResourceMap().getIcon("icon.user"));
		item = new JMenuItem(resource.getString("CREATE_FRIENDSHIP"));
		item.addActionListener(this);
		item.setActionCommand(TwitterConstants.CREATE_FRIENDSHIP+"");
		item.setIcon(getResourceMap().getIcon("icon.user_add"));
		item.setEnabled(selected);
		sub.add(item);

		item = new JMenuItem(resource.getString("DESTROY_FRIENDSHIP"));
		item.addActionListener(this);
		item.setActionCommand(TwitterConstants.DESTROY_FRIENDSHIP+"");
		item.setIcon(getResourceMap().getIcon("icon.user_delete"));
		item.setEnabled(selected);
		sub.add(item);

		item = new JMenuItem(resource.getString("EXISTS_FRIENDSHIP"));
		item.addActionListener(this);
		item.setActionCommand(TwitterConstants.EXISTS_FRIENDSHIP+"");
		item.setIcon(getResourceMap().getIcon("icon.user_go"));
		item.setEnabled(selected);
		sub.add(item);

		item = new JMenuItem(resource.getString("SHOW_FRIENDSHIP"));
		item.addActionListener(this);
		item.setActionCommand(TwitterConstants.SHOW_FRIENDSHIP+"");
		item.setIcon(getResourceMap().getIcon("icon.user_gray"));
		item.setEnabled(selected);
		sub.add(item);
		menu.add(sub);

//SENT_DIRECT_MESSAGES
//SEND_DIRECT_MESSAGE
		sub = new JMenu(resource.getString("DIRECT_MESSAGES"));
		sub.setIcon(getResourceMap().getIcon("icon.user_comment"));
		item = new JMenuItem(resource.getString("SEND_DIRECT_MESSAGE"));
		item.setActionCommand(TwitterConstants.SEND_DIRECT_MESSAGE+"");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.user_comment"));
		item.setEnabled(selected);
		sub.add(item);

//		item = new JMenuItem(resource.getString("SEND_DIRECT_MESSAGES"));
//		item.setActionCommand(TwitterConstants.SEND_DIRECT_MESSAGES+"");
//		item.addActionListener(this);
//		item.setIcon(getResourceMap().getIcon("icon.user_comment"));
//		item.setEnabled(selected);
//		sub.add(item);
		menu.add(sub);
//NEAR_BY_PLACES
//GEO_DETAILS

		sub = new JMenu(resource.getString("LOCATION"));
		sub.setIcon(getResourceMap().getIcon("icon.map"));
		item = new JMenuItem(resource.getString("NEAR_BY_PLACES"));
		item.setActionCommand(TwitterConstants.NEAR_BY_PLACES+"");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.map"));
		item.setEnabled(selected);
		sub.add(item);

		item = new JMenuItem(resource.getString("GEO_DETAILS"));
		item.setActionCommand(TwitterConstants.GEO_DETAILS+"");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.map_edit"));
		item.setEnabled(selected);
		sub.add(item);
		menu.add(sub);

//ADD_LIST_MEMBER
//DELETE_LIST_MEMBER
//CHECK_LIST_MEMBERSHIP
		sub = new JMenu(resource.getString("USER_LISTS"));
		sub.setIcon(getResourceMap().getIcon("icon.group"));
		item = new JMenuItem(resource.getString("ADD_LIST_MEMBER"));
		item.setActionCommand(TwitterConstants.ADD_LIST_MEMBER+"");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.group_add"));
		item.setEnabled(selected);
		sub.add(item);

		item = new JMenuItem(resource.getString("DELETE_LIST_MEMBER"));
		item.setActionCommand(TwitterConstants.DELETE_LIST_MEMBER+"");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.group_delete"));
		item.setEnabled(selected);
		sub.add(item);

		item = new JMenuItem(resource.getString("CHECK_LIST_MEMBERSHIP"));
		item.setActionCommand(TwitterConstants.CHECK_LIST_MEMBERSHIP+"");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.group_gear"));
		item.setEnabled(selected);
		sub.add(item);
		menu.add(sub);

	//USER_TIMELINE
		item = new JMenuItem(resource.getString("USER_TIMELINE"));
		item.setActionCommand(TwitterConstants.USER_TIMELINE+"");
		item.addActionListener(this);
		item.setIcon(getResourceMap().getIcon("icon.timeline_marker"));
		item.setEnabled(selected);
		menu.add(item);

		//menu.setEnabled(selected);

		return menu;
	}
	//END private methods

	@Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = TwitzApp.getApplication().getMainFrame();
            aboutBox = new TwitzAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        //TwitzApp.getApplication().show(aboutBox);
		aboutBox.setVisible(true);
    }

	@Action
	public void showPrefsBox() {
		if(prefs == null) {
			JFrame mainFrame = TwitzApp.getApplication().getMainFrame();
			prefs = new PreferencesDialog(mainFrame, true);
		}
		prefs.setVisible(true);
	}

	public void toggleTabs(java.awt.event.ActionEvent evt) {
		javax.swing.JCheckBoxMenuItem item = (javax.swing.JCheckBoxMenuItem) evt.getSource();
		int index = tabPane.getTabCount();
		if (evt.getActionCommand().equals("Friends"))
		{
			config.setProperty("tab.friends", item.isSelected()+"");
			if (item.isSelected())
			{
				tabPane.insertTab(getResourceMap().getString("friendsPane.TabConstraints.tabTitle"), getResourceMap().getIcon("friendsPane.TabConstraints.tabIcon"), friendsPane, null, 1);
			}
			else
			{
				tabPane.remove(friendsPane);
			}
		}
		else if (evt.getActionCommand().equals("Blocked"))
		{
			config.setProperty("tab.blocked", item.isSelected()+"");
			if (item.isSelected())
			{
				tabPane.insertTab(getResourceMap().getString("blockedPane.TabConstraints.tabTitle"), getResourceMap().getIcon("blockedPane.TabConstraints.tabIcon"), blockedPane, null, index >= 2 ? 2 : index); // NOI18N
			}
			else
			{
				tabPane.remove(blockedPane);
			}
		}
		else if (evt.getActionCommand().equals("Following"))
		{
			config.setProperty("tab.following", item.isSelected()+"");
			if (item.isSelected())
			{
				tabPane.insertTab(getResourceMap().getString("followingPane.TabConstraints.tabTitle"), getResourceMap().getIcon("followingPane.TabConstraints.tabIcon"), followingPane, null, index >= 3 ? 3 : index); // NOI18N
			}
			else
			{
				tabPane.remove(followingPane);
			}
		}
		else if (evt.getActionCommand().equals("Followers"))
		{
			config.setProperty("tab.followers", item.isSelected()+"");
			if (item.isSelected())
			{
				tabPane.insertTab(getResourceMap().getString("followersPane.TabConstraints.tabTitle"), getResourceMap().getIcon("followersPane.TabConstraints.tabIcon"), followersPane, null, index >= 4 ? 4 : index); // NOI18N
			}
			else
			{
				tabPane.remove(followersPane);
			}
		}
		else if(evt.getActionCommand().equals("Search"))
		{
			config.setProperty("tab.search", item.isSelected()+"");
			if(item.isSelected()) {
				tabPane.addTab(getResourceMap().getString("searchPanel.TabConstraints.tabTitle"), getResourceMap().getIcon("searchPanel.tabIcon"), searchPanel); // NOI18N
			}
			else
			{
				tabPane.remove(searchPanel);
			}
		}
	}

	@Action public void sendAsyncTweet() {
		//aTwitter.updateStatus(txtTweet.getText());
		sendTweetClicked().execute();
	}

	@Action
	@SuppressWarnings("static-access")
	public Task sendTweetClicked()
	{
		return new SendTweetClickedTask(getApplication());
	}

    private class SendTweetClickedTask extends org.jdesktop.application.Task<Object, Void> {
        String tweet;
		int errors = 0;
		SendTweetClickedTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to SendTweetClickedTask fields, here.
            super(app);
			tweet = txtTweet.getText();
			
        }
        @Override protected Object doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
			message("startMessage", 1);
			//Lets disable the Tweet button and the textfield to prevent button mashing while we wait
			btnTweet.setEnabled(false);
			txtTweet.setEnabled(false);

			aTwitter.updateStatus(tweet);
			//Go into a endless loop until we hear back from the backend
			boolean active = true;
			while(!this.isCancelled() && !this.isDone()) {
				//Run till canceled or finished
			}
//			twitter4j.User u = null;
//			try
//			{
//				u = tm.getTwitterInstance().verifyCredentials();
//			}
//			catch (TwitterException ex)
//			{
//				errors++;
//				failed(ex);
//			}
//			if (u != null)
//			{
//				try
//				{
//					tm.sendTweet(tweet);
//				}
//				catch (TwitterException ex)
//				{
//					errors++;
//					failed(ex);
//				}
//				catch (IllegalStateException ex)
//				{
//					errors++;
//					failed(ex);
//				}
//				message("finishMessage", 2, errors);
//			}
            return null;  // return your result
        }
        @Override protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
			if(errors < 1)
			{
				txtTweet.setText("");
				btnTweet.setEnabled(false);

				if (chkCOT.isSelected())
				{
					mainApp.toggleWindowView("down");
				}
			}
        }
		@Override protected void cancelled() {
			btnTweet.setEnabled(true);
			txtTweet.setEnabled(true);
		}
		@Override protected void failed(Throwable t) {
			logger.log(Level.SEVERE, t.getMessage());
			if(t instanceof TwitterException) {
				TwitterException te = (TwitterException)t;
				if(te.isCausedByNetworkIssue()) {
					displayError(t, "Network Error", "Unable to reach: "+te.getMessage(), null);
					//errorDialog.showMessageDialog(getFrame(), "Unable to reach " + te.getMessage(), "Network Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(te.resourceNotFound()) {
					//errorDialog.showMessageDialog(getFrame(), "Unable to locate resource: " + te.getMessage(), "Resource Alocation Error", JOptionPane.ERROR_MESSAGE);
					displayError(t, "Resource Allocation Error", "Unable to locate resource: "+te.getMessage(), null);
				}
			}
			message("finishMessage", 2, errors);
		}
    }

	/**
	 *
	 * @param t Any Throwable, this is used to gain the stacktrace
	 * @param title The title of the error dialog
	 * @param message The error message to be displayed
	 * @param method The TwitterMethod object that caused this error. This can be null
	 */
	public void displayError(Throwable t, String title, Object message, twitter4j.TwitterMethod method) {
		Object[] options = {"Ok", "Stack Trace"};
		int rv = JOptionPane.showOptionDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, getResourceMap().getIcon("error.icon"), options, options[0]);
		switch (rv)
		{
			case 0:
				break;
			case 1:
				//show the full stack trace here
				MessageDialog msg = new MessageDialog(getFrame(), false);
				java.io.StringWriter w = new java.io.StringWriter();
				java.io.PrintWriter p = new java.io.PrintWriter(w);
				t.printStackTrace(p);
				p.flush();
				msg.setMessage(w.toString());
				msg.setVisible(true);
				break;
		}
	}

	@Action
	public void showMiniMode() {
		if(minimode) {
			fullTwitz();
		}
		else {
			miniTwitz();
		}
	}

	/**
	 * Display only a miniature version of Twitz
	 */
	public void miniTwitz() {
		tabPane.setVisible(false);
		//this.getStatusBar().setVisible(false);
		this.getMenuBar().setVisible(false);
		this.getFrame().setSize(this.getFrame().getWidth(), 100);
		fixLocation();
		btnMini.setIcon(getResourceMap().getIcon("btnMini.up.icon"));
		minimode = true;
		config.setProperty("minimode", minimode+""); //Update the config file so it starts in the same mode
	}

	/**
	 * Display the full Twitz window
	 */
	public void fullTwitz() {
		tabPane.setVisible(true);
		//this.getStatusBar().setVisible(true);
		this.getMenuBar().setVisible(true);
		this.getFrame().setSize(this.getFrame().getWidth(), 300);
		fixLocation();
		btnMini.setIcon(getResourceMap().getIcon("btnMini.icon"));
		minimode = false;
		config.setProperty("minimode", minimode+""); //Update the config file so it starts in the same mode
	}

	/**
	 * Resets the Tweet button, clears the tweet text and closes the window is the close
	 * on send is checked
	 */
	public void resetTweetAndHide() {
		txtTweet.setText("");
		btnTweet.setEnabled(false);

		if (chkCOT.isSelected())
		{
			mainApp.toggleWindowView("down");
		}
	}

	private void fixLocation() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle frame = this.getFrame().getBounds();
		Rectangle desktop = ge.getMaximumWindowBounds();
		//System.out.println("Widht of desktop: " + desk.toString());
		//int x = (desk.width - frame.width);
		if(!desktop.contains(frame.x, frame.y, frame.width, frame.height)) {
			int x = (desktop.width - frame.width);
			int y = (desktop.height - frame.height) - 32;
			//System.out.println("X: " + x + " Y: " + y);
			this.getFrame().setLocation(x, y);
		}
	}

	/**
	 * Event Processor for all events that are related to twitter activity in the #link TwitterManager
	 * A TwitzEvent will will have and type.
	 * @param t
	 */
	

	public void updateTweetsList(twitter4j.Status s ) {
		twitter4j.User u = s.getUser();
		String user = u.getScreenName();
		java.net.URL pUrl = u.getProfileImageURL();
		//TODO I need to incoporate the Profile image here
		java.util.Vector vData = new java.util.Vector();
		vData.add(s.getId());
		vData.add(s.isRetweet());
		vData.add(u.getScreenName());
		vData.add(s.getText());
		DefaultTableModel model = (DefaultTableModel) recentList.getModel();
		model.addRow(vData);
		//model.addElement(user+": "+s.getText());
	}



	//TwitterListener abstract methods
	// <editor-fold defaultstate="collapsed" desc="Abstract Methods">

	//TwitzListener
	public void eventOccurred(TwitzEvent t)
	{
		int type = t.getEventType();
		switch(type) {
			case TwitzEvent.UPDATE:
				twitter4j.Status s = (twitter4j.Status) t.getSource();
				updateTweetsList(s);
				break;
			case TwitzEvent.LOGIN:
				//We need to do any post login logic here such as updating of any changed profile data et al.
				break;
			case TwitzEvent.ADD_FRIEND:
				break;
			case TwitzEvent.DELETE_FRIEND:
				break;
			case TwitzEvent.MSG_SENT:
				break;
			case TwitzEvent.MSG_RECIEVED:
				break;
			case TwitzEvent.ADD_BLOCK:
				break;
			case TwitzEvent.REMOVE_BLOCK:
				break;
			case TwitzEvent.REPORT_SPAM:
				break;
		}
	}

	//ActionListener
	public void actionPerformed(ActionEvent evt) {
		System.out.println(evt.getSource().toString());
		JList list = null;
		if(evt.getSource() instanceof javax.swing.JMenuItem) {
			JMenuItem item = (JMenuItem)evt.getSource();
			if(item.getParent() instanceof javax.swing.JPopupMenu) {
				JPopupMenu m = (JPopupMenu)item.getParent();
				if(m.getInvoker() instanceof JList)
					list = (JList)m.getInvoker();
			}
		}
		int action = -1;
		try
		{
			action = Integer.parseInt(evt.getActionCommand());
		}
		catch(NumberFormatException ignore) {}
		if(action != -1) {
			switch(action) {
				case TwitterConstants.SEARCH:
					break;
				case TwitterConstants.TRENDS:
					break;
				case TwitterConstants.CURRENT_TRENDS:
					break;
				case TwitterConstants.DAILY_TRENDS:
					break;
				case TwitterConstants.WEEKLY_TRENDS:
					break;
				case TwitterConstants.PUBLIC_TIMELINE:
					break;
				case TwitterConstants.HOME_TIMELINE:
					break;
				case TwitterConstants.FRIENDS_TIMELINE:
					break;
				case TwitterConstants.USER_TIMELINE:
					break;
				case TwitterConstants.MENTIONS:
					break;
				case TwitterConstants.RETWEETED_BY_ME:
					break;
				case TwitterConstants.RETWEETED_TO_ME:
					break;
				case TwitterConstants.RETWEETS_OF_ME:
					break;
				case TwitterConstants.SHOW_STATUS:
					break;
				case TwitterConstants.UPDATE_STATUS:
					break;
				case TwitterConstants.DESTROY_STATUS:
					break;
				case TwitterConstants.RETWEET_STATUS:
					break;
				case TwitterConstants.RETWEETS:
					break;
				case TwitterConstants.SHOW_USER:
					break;
				case TwitterConstants.LOOKUP_USERS:
					break;
				case TwitterConstants.SEARCH_USERS:
					break;
				case TwitterConstants.SUGGESTED_USER_CATEGORIES:
					break;
				case TwitterConstants.USER_SUGGESTIONS:
					break;
				case TwitterConstants.FRIENDS_STATUSES:
					break;
				case TwitterConstants.FOLLOWERS_STATUSES:
					break;
				case TwitterConstants.CREATE_USER_LIST:
					break;
				case TwitterConstants.UPDATE_USER_LIST:
					break;
				case TwitterConstants.USER_LISTS:
					break;
				case TwitterConstants.SHOW_USER_LIST:
					break;
				case TwitterConstants.DESTROY_USER_LIST:
					break;
				case TwitterConstants.USER_LIST_STATUSES:
					break;
				case TwitterConstants.USER_LIST_MEMBERSHIPS:
					break;
				case TwitterConstants.USER_LIST_SUBSCRIPTIONS:
					break;
				case TwitterConstants.LIST_MEMBERS:
					break;
				case TwitterConstants.ADD_LIST_MEMBER:
					break;
				case TwitterConstants.DELETE_LIST_MEMBER:
					break;
				case TwitterConstants.CHECK_LIST_MEMBERSHIP:
					break;
				case TwitterConstants.LIST_SUBSCRIBERS:
					break;
				case TwitterConstants.SUBSCRIBE_LIST:
					break;
				case TwitterConstants.UNSUBSCRIBE_LIST:
					break;
				case TwitterConstants.CHECK_LIST_SUBSCRIPTION:
					break;
				case TwitterConstants.DIRECT_MESSAGES:
					break;
				case TwitterConstants.SENT_DIRECT_MESSAGES:
					break;
				case TwitterConstants.SEND_DIRECT_MESSAGE:
					System.out.println("Send Direct Meessage Clicked");
					if(list != null) {
						int s = list.getSelectedIndex();
						javax.swing.ListModel model = list.getModel();
						String value = (String)model.getElementAt(s);
					}
					break;
				case TwitterConstants.DESTROY_DIRECT_MESSAGES:
					break;
				case TwitterConstants.CREATE_FRIENDSHIP:
					System.out.println("Create Friendship clicked");
					break;
				case TwitterConstants.DESTROY_FRIENDSHIP:
					break;
				case TwitterConstants.EXISTS_FRIENDSHIP:
					break;
				case TwitterConstants.SHOW_FRIENDSHIP:
					break;
				case TwitterConstants.INCOMING_FRIENDSHIPS:
					break;
				case TwitterConstants.OUTGOING_FRIENDSHIPS:
					break;
				case TwitterConstants.FRIENDS_IDS:
					break;
				case TwitterConstants.FOLLOWERS_IDS:
					break;
				case TwitterConstants.RATE_LIMIT_STATUS:
					break;
				case TwitterConstants.UPDATE_DELIVERY_DEVICE:
					break;
				case TwitterConstants.UPDATE_PROFILE_COLORS:
					break;
				case TwitterConstants.UPDATE_PROFILE_IMAGE:
					break;
				case TwitterConstants.UPDATE_PROFILE_BACKGROUND_IMAGE:
					break;
				case TwitterConstants.UPDATE_PROFILE:
					break;
				case TwitterConstants.FAVORITES:
					break;
				case TwitterConstants.CREATE_FAVORITE:
					break;
				case TwitterConstants.DESTROY_FAVORITE:
					break;
				case TwitterConstants.ENABLE_NOTIFICATION:
					break;
				case TwitterConstants.DISABLE_NOTIFICATION:
					break;
				case TwitterConstants.CREATE_BLOCK:
					System.out.println("Create Block clicked");
					break;
				case TwitterConstants.DESTROY_BLOCK:
					break;
				case TwitterConstants.EXISTS_BLOCK:
					break;
				case TwitterConstants.BLOCKING_USERS:
					break;
				case TwitterConstants.BLOCKING_USERS_IDS:
					break;
				case TwitterConstants.REPORT_SPAM:
					System.out.println("Report SPAM clicked");
					break;
				case TwitterConstants.AVAILABLE_TRENDS:
					break;
				case TwitterConstants.LOCATION_TRENDS:
					break;
				case TwitterConstants.NEAR_BY_PLACES:
					break;
				case TwitterConstants.REVERSE_GEO_CODE:
					break;
				case TwitterConstants.GEO_DETAILS:
					break;
				case TwitterConstants.TEST:
					break;
			} //End switch()
		} //End if(action != -1)
	}

	// TwitterListener
	public void searched(QueryResult queryResult)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotTrends(Trends trends)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotCurrentTrends(Trends trends)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotDailyTrends(List<Trends> trendsList)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotWeeklyTrends(List<Trends> trendsList)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotPublicTimeline(ResponseList<Status> statuses)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotHomeTimeline(ResponseList<Status> statuses)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFriendsTimeline(ResponseList<Status> statuses)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserTimeline(ResponseList<Status> statuses)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotMentions(ResponseList<Status> statuses)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotRetweetedByMe(ResponseList<Status> statuses)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotRetweetedToMe(ResponseList<Status> statuses)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotRetweetsOfMe(ResponseList<Status> statuses)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotShowStatus(Status status)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedStatus(Status status)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedStatus(Status destroyedStatus)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void retweetedStatus(Status retweetedStatus)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotRetweets(ResponseList<Status> retweets)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserDetail(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void lookedupUsers(ResponseList<User> users)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void searchedUser(ResponseList<User> userList)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotSuggestedUserCategories(ResponseList<Category> category)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserSuggestions(ResponseList<User> users)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFriendsStatuses(PagableResponseList<User> users)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFollowersStatuses(PagableResponseList<User> users)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void createdUserList(UserList userList)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedUserList(UserList userList)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserLists(PagableResponseList<UserList> userLists)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotShowUserList(UserList userList)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedUserList(UserList userList)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserListStatuses(ResponseList<Status> statuses)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserListMemberships(PagableResponseList<UserList> userLists)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserListSubscriptions(PagableResponseList<UserList> userLists)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserListMembers(PagableResponseList<User> users)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void addedUserListMember(UserList userList)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void deletedUserListMember(UserList userList)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void checkedUserListMembership(User users)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotUserListSubscribers(PagableResponseList<User> users)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void subscribedUserList(UserList userList)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void unsubscribedUserList(UserList userList)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void checkedUserListSubscription(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotDirectMessages(ResponseList<DirectMessage> messages)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotSentDirectMessages(ResponseList<DirectMessage> messages)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void sentDirectMessage(DirectMessage message)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedDirectMessage(DirectMessage message)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void createdFriendship(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedFriendship(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotExistsFriendship(boolean exists)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotShowFriendship(Relationship relationship)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotIncomingFriendships(IDs ids)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotOutgoingFriendships(IDs ids)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFriendsIDs(IDs ids)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFollowersIDs(IDs ids)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotRateLimitStatus(RateLimitStatus rateLimitStatus)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedDeliveryDevice(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedProfileColors(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedProfileImage(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedProfileBackgroundImage(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void updatedProfile(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotFavorites(ResponseList<Status> statuses)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void createdFavorite(Status status)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedFavorite(Status status)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void enabledNotification(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void disabledNotification(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void createdBlock(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroyedBlock(User user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotExistsBlock(boolean blockExists)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotBlockingUsers(ResponseList<User> blockingUsers)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotBlockingUsersIDs(IDs blockingUsersIDs)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void reportedSpam(User reportedSpammer)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotAvailableTrends(ResponseList<Location> locations)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotLocationTrends(Trends trends)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotNearByPlaces(ResponseList<Place> places)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotReverseGeoCode(ResponseList<Place> places)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotGeoDetails(Place place)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void tested(boolean test)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void onException(TwitterException te, TwitterMethod method)
	{
		if(method.equals(TwitterMethod.UPDATE_STATUS))
			sendTweetClicked().cancel(true);
		displayError(te, "Twitter Error", "Error Occurred while attempting: \n\t"+tm.getResourceMap().getString(method.name()), method);
	}
	//END abstract methods
	//</editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Generated Variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JMenu actionsMenu;
    private javax.swing.JList blockedList;
    private javax.swing.JScrollPane blockedPane;
    private javax.swing.JButton btnExitSearch;
    private javax.swing.JButton btnMini;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTweet;
    private javax.swing.JCheckBox chkCOT;
    private javax.swing.JPopupMenu contextMenu;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JList followersList;
    private javax.swing.JScrollPane followersPane;
    private javax.swing.JList followingList;
    private javax.swing.JScrollPane followingPane;
    private javax.swing.JList friendsList;
    private javax.swing.JList friendsNameList;
    private javax.swing.JSplitPane friendsPane;
    private javax.swing.JMenuItem helpItem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JCheckBoxMenuItem menuItemBlocked;
    private javax.swing.JCheckBoxMenuItem menuItemFollowers;
    private javax.swing.JCheckBoxMenuItem menuItemFollowing;
    private javax.swing.JCheckBoxMenuItem menuItemFriends;
    private javax.swing.JCheckBoxMenuItem menuItemSearch;
    private javax.swing.JMenu menuTabs;
    private javax.swing.JMenuItem miniItem;
    private javax.swing.JScrollPane nameListPane;
    private javax.swing.JMenuItem prefsItem;
    private javax.swing.JMenuItem prefsMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTable recentList;
    private javax.swing.JScrollPane recentPane;
    private javax.swing.JToolBar searchBar;
    private javax.swing.JScrollPane searchPane;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblSearch;
    private javax.swing.JScrollPane tweetsPane;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTweet;
    // End of variables declaration//GEN-END:variables
	//</editor-fold>

	private SettingsManager config = SettingsManager.getInstance();
	private boolean startMode = config.getBoolean("minimode");
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
	private PreferencesDialog prefs;
	private twitz.twitter.TwitterManager tm;
	private boolean minimode = false;
	private TwitzApp mainApp;
	private twitter4j.AsyncTwitter aTwitter;
	private ResourceMap resource = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(twitz.twitter.TwitterManager.class);

	private Logger logger = Logger.getLogger(TwitzView.class.getName());
}
