/*
 * TwitzView.java
 */

package twitz;

import java.awt.Color;
import javax.swing.event.DocumentEvent;
import twitz.dialogs.TwitzAboutBox;
import java.awt.Component;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
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
import java.util.Vector;
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
import javax.swing.ListModel;
import javax.swing.MenuElement;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import twitter4j.*;
import twitz.dialogs.MessageDialog;
import twitz.events.TwitzListener;
import twitz.twitter.TwitterConstants;
import twitz.twitter.TwitterManager;
import twitz.ui.TweetTableCellRenderer;

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
		resource = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(twitz.twitter.TwitterManager.class);
        initComponents();
		this.getFrame().setVisible(false);
		this.getFrame().setSize(400, 300);
		btnTweet.setEnabled(false);

		searchHeaders.add("User");
		searchHeaders.add("Results");

		//look & feel setup
		//Default to full view
		fullTwitz();

		this.getFrame().addWindowFocusListener(new WindowFocusListener() {

			public void windowGainedFocus(WindowEvent e)
			{
				txtTweet.requestFocus();
				//firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
			}

			public void windowLostFocus(WindowEvent e)
			{
				//firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
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
        advSearchBar = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        txtSinceDate = new org.jdesktop.swingx.JXDatePicker();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        txtUntilDate = new org.jdesktop.swingx.JXDatePicker();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jLabel3 = new javax.swing.JLabel();
        cmbRpp = new javax.swing.JComboBox();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        lblPage = new javax.swing.JLabel();
        actionPanel = new javax.swing.JPanel();
        chkCOT = new javax.swing.JCheckBox();
        btnTweet = new javax.swing.JButton();
        btnMini = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtTweet = new javax.swing.JTextArea();
        lblChars = new javax.swing.JLabel();
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
        buttonGroup1 = new javax.swing.ButtonGroup();

        mainPanel.setComponentPopupMenu(contextMenu);
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mainPanelKeyReleased(evt);
            }
        });

        tabPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabPane.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        tabPane.setInheritsPopupMenu(true);
        tabPane.setMinimumSize(new java.awt.Dimension(300, 37));
        tabPane.setName("tabPane"); // NOI18N
        tabPane.setNextFocusableComponent(txtTweet);
        tabPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tabPaneKeyReleased(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(TwitzView.class);
        recentPane.setBackground(resourceMap.getColor("recentPane.background")); // NOI18N
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
        recentList.setIntercellSpacing(new java.awt.Dimension(3, 3));
        recentList.setName("recentList"); // NOI18N
        recentList.setRowHeight(50);
        recentList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        recentList.setShowHorizontalLines(false);
        recentList.setShowVerticalLines(false);
        recentPane.setViewportView(recentList);

        tabPane.addTab(resourceMap.getString("recentPane.TabConstraints.tabTitle"), resourceMap.getIcon("recentPane.TabConstraints.tabIcon"), recentPane, resourceMap.getString("recentPane.TabConstraints.tabToolTip")); // NOI18N

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

        tabPane.addTab(resourceMap.getString("friendsPane.TabConstraints.tabTitle"), resourceMap.getIcon("friendsPane.TabConstraints.tabIcon"), friendsPane, resourceMap.getString("friendsPane.TabConstraints.tabToolTip")); // NOI18N

        blockedPane.setName("blockedPane"); // NOI18N

        blockedList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        blockedList.setName("blockedList"); // NOI18N
        blockedPane.setViewportView(blockedList);

        tabPane.addTab(resourceMap.getString("blockedPane.TabConstraints.tabTitle"), resourceMap.getIcon("blockedPane.TabConstraints.tabIcon"), blockedPane, resourceMap.getString("blockedPane.TabConstraints.tabToolTip")); // NOI18N

        followingPane.setName("followingPane"); // NOI18N

        followingList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        followingList.setName("followingList"); // NOI18N
        followingPane.setViewportView(followingList);

        tabPane.addTab(resourceMap.getString("followingPane.TabConstraints.tabTitle"), resourceMap.getIcon("followingPane.TabConstraints.tabIcon"), followingPane, resourceMap.getString("followingPane.TabConstraints.tabToolTip")); // NOI18N

        followersPane.setName("followersPane"); // NOI18N

        followersList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        followersList.setName("followersList"); // NOI18N
        followersPane.setViewportView(followersList);

        tabPane.addTab(resourceMap.getString("followersPane.TabConstraints.tabTitle"), resourceMap.getIcon("followersPane.TabConstraints.tabIcon"), followersPane, resourceMap.getString("followersPane.TabConstraints.tabToolTip")); // NOI18N

        searchPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        searchPanel.setName("searchPanel"); // NOI18N

        searchBar.setFloatable(false);
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
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        searchBar.add(txtSearch);

        btnSearch.setIcon(resourceMap.getIcon("btnSearch.icon")); // NOI18N
        btnSearch.setText(resourceMap.getString("btnSearch.text")); // NOI18N
        btnSearch.setEnabled(false);
        btnSearch.setFocusable(false);
        btnSearch.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSearch.setName("btnSearch"); // NOI18N
        searchBar.add(btnSearch);

        searchPane.setName("searchPane"); // NOI18N

        tblSearch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "User", "Results"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
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

        advSearchBar.setFloatable(false);
        advSearchBar.setRollover(true);
        advSearchBar.setMaximumSize(new java.awt.Dimension(32600, 28));
        advSearchBar.setMinimumSize(new java.awt.Dimension(300, 28));
        advSearchBar.setName("advSearchBar"); // NOI18N

        jLabel1.setLabelFor(txtSinceDate);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        advSearchBar.add(jLabel1);

        txtSinceDate.setToolTipText(resourceMap.getString("txtSinceDate.toolTipText")); // NOI18N
        txtSinceDate.setFormats("yyyy-MM-dd");
        txtSinceDate.setName("txtSinceDate"); // NOI18N
        txtSinceDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSinceDateActionPerformed(evt);
            }
        });
        advSearchBar.add(txtSinceDate);

        jSeparator2.setName("jSeparator2"); // NOI18N
        advSearchBar.add(jSeparator2);

        jLabel2.setLabelFor(txtUntilDate);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        advSearchBar.add(jLabel2);

        txtUntilDate.setToolTipText(resourceMap.getString("txtUntilDate.toolTipText")); // NOI18N
        txtUntilDate.setFormats("yyyy-MM-dd");
        txtUntilDate.setName("txtUntilDate"); // NOI18N
        txtUntilDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUntilDateActionPerformed(evt);
            }
        });
        advSearchBar.add(txtUntilDate);

        jSeparator3.setName("jSeparator3"); // NOI18N
        advSearchBar.add(jSeparator3);

        jLabel3.setLabelFor(cmbRpp);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        advSearchBar.add(jLabel3);

        cmbRpp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "70", "80", "90", "100" }));
        cmbRpp.setToolTipText(resourceMap.getString("cmbRpp.toolTipText")); // NOI18N
        cmbRpp.setMaximumSize(new java.awt.Dimension(52, 24));
        cmbRpp.setName("cmbRpp"); // NOI18N
        advSearchBar.add(cmbRpp);

        btnNext.setIcon(resourceMap.getIcon("btnNext.icon")); // NOI18N
        btnNext.setText(resourceMap.getString("btnNext.text")); // NOI18N
        btnNext.setToolTipText(resourceMap.getString("btnNext.toolTipText")); // NOI18N
        btnNext.setFocusable(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNext.setName("btnNext"); // NOI18N

        btnPrev.setIcon(resourceMap.getIcon("btnPrev.icon")); // NOI18N
        btnPrev.setText(resourceMap.getString("btnPrev.text")); // NOI18N
        btnPrev.setFocusable(false);
        btnPrev.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPrev.setName("btnPrev"); // NOI18N

        lblPage.setText(resourceMap.getString("lblPage.text")); // NOI18N
        lblPage.setName("lblPage"); // NOI18N

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(searchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
            .addComponent(advSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPrev)
                .addGap(98, 98, 98)
                .addComponent(lblPage, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                .addComponent(btnNext)
                .addContainerGap())
            .addComponent(searchPane, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(advSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchPane, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPage, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tabPane.addTab(resourceMap.getString("searchPanel.TabConstraints.tabTitle"), resourceMap.getIcon("searchPanel.tabIcon"), searchPanel, resourceMap.getString("searchPanel.TabConstraints.tabToolTip")); // NOI18N

        actionPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        actionPanel.setMinimumSize(new java.awt.Dimension(100, 50));
        actionPanel.setName("actionPanel"); // NOI18N

        chkCOT.setText(resourceMap.getString("chkCOT.text")); // NOI18N
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

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBar(null);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        txtTweet.setColumns(20);
        txtTweet.setRows(5);
        txtTweet.setWrapStyleWord(true);
        txtTweet.setName("txtTweet"); // NOI18N
        txtTweet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTweetKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txtTweet);

        lblChars.setFont(resourceMap.getFont("lblChars.font")); // NOI18N
        lblChars.setForeground(resourceMap.getColor("lblChars.foreground")); // NOI18N
        lblChars.setText(resourceMap.getString("lblChars.text")); // NOI18N
        lblChars.setName("lblChars"); // NOI18N

        javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.setHorizontalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, actionPanelLayout.createSequentialGroup()
                .addComponent(lblChars, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
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
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(lblChars, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(chkCOT, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTweet, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnMini, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(actionPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 385, Short.MAX_VALUE)
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

	private void txtSinceDateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtSinceDateActionPerformed
	{//GEN-HEADEREND:event_txtSinceDateActionPerformed
		// TODO add your handling code here:
}//GEN-LAST:event_txtSinceDateActionPerformed

	private void txtUntilDateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtUntilDateActionPerformed
	{//GEN-HEADEREND:event_txtUntilDateActionPerformed
		// TODO add your handling code here:
}//GEN-LAST:event_txtUntilDateActionPerformed

	private void txtSearchKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSearchKeyReleased
	{//GEN-HEADEREND:event_txtSearchKeyReleased
		switch(evt.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				btnSearch.doClick();
				break;
			default:
				if(!txtSearch.getText().equals(""))
					btnSearch.setEnabled(true);
				else
					btnSearch.setEnabled(false);
		}
	}//GEN-LAST:event_txtSearchKeyReleased

	private void txtTweetKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTweetKeyReleased
	{//GEN-HEADEREND:event_txtTweetKeyReleased
		keyTyped(evt);
	}//GEN-LAST:event_txtTweetKeyReleased

	/**
	 * This method is called on startup in the constructor.
	 * It is used to configure the GUI with the default from the config file.
	 */
	private void setupDefaults() {
		DefaultTableColumnModel cModel = (DefaultTableColumnModel) recentList.getColumnModel();
		cModel.getColumn(0).setMaxWidth(1);
		cModel.getColumn(1).setMaxWidth(1);
		//recentList.setDefaultRenderer(String.class, new TweetTableCellRenderer());

		String tcc = "<html><P><IMG SRC=\"http://localhost/~mistik1/me_3.jpg\" BORDER=0>This is a tweet I hope its nice. Lets add some more text and see how it looks</P>";
		recentList.setValueAt(tcc, 0, 3);
		
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
		tabPane.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e)
			{
				if(tabPane.getSelectedComponent().equals(searchPanel)) {
					actionPanel.setVisible(false);
				}
				else {
					actionPanel.setVisible(true);
				}
				//firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
			}
		});
		//Setup the ActionListener for the search buttin
		btnSearch.setActionCommand(TwitterConstants.SEARCH+"");
		btnSearch.addActionListener(this);
		btnTweet.setActionCommand(TwitterConstants.UPDATE_STATUS+"");
		btnTweet.addActionListener(this);
	}

	@Action
	private void keyTyped(java.awt.event.KeyEvent evt) {
		switch(evt.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				//sendAsyncTweet();
				if(evt.isShiftDown())
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
			default:
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
		}
	}

	@Action
	public void hideSearchTab()
	{
		tabPane.remove(searchPanel);
		menuItemSearch.setSelected(false);
	}
	
	@Action
	public void doSearch() {
		DefaultTableCellRenderer ren = new DefaultTableCellRenderer();

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
			prefs = new PreferencesDialog(mainFrame, true, mainApp);
			prefs.setLocationRelativeTo(mainFrame);
		}
		prefs.setVisible(true);
	}

	public void toggleTabs(java.awt.event.ActionEvent evt) {
		if (evt.getSource() instanceof javax.swing.JCheckBoxMenuItem)
		{
			javax.swing.JCheckBoxMenuItem item = (javax.swing.JCheckBoxMenuItem) evt.getSource();
			int index = tabPane.getTabCount();
			if (evt.getActionCommand().equals("Friends"))
			{
				config.setProperty("tab.friends", item.isSelected() + "");
				if (item.isSelected())
				{
					tabPane.insertTab(getResourceMap().getString("friendsPane.TabConstraints.tabTitle"), getResourceMap().getIcon("friendsPane.TabConstraints.tabIcon"), friendsPane, getResourceMap().getString("friendsPane.TabConstraints.tabTooltip"), 1);
					tabPane.setSelectedComponent(friendsPane);
				}
				else
				{
					tabPane.remove(friendsPane);
				}
			}
			else if (evt.getActionCommand().equals("Blocked"))
			{
				config.setProperty("tab.blocked", item.isSelected() + "");
				if (item.isSelected())
				{
					tabPane.insertTab(getResourceMap().getString("blockedPane.TabConstraints.tabTitle"), getResourceMap().getIcon("blockedPane.TabConstraints.tabIcon"), blockedPane, getResourceMap().getString("blockedPane.TabConstraints.tabTooltip"), index >= 2 ? 2 : index); // NOI18N
					tabPane.setSelectedComponent(blockedPane);
				}
				else
				{
					tabPane.remove(blockedPane);
				}
			}
			else if (evt.getActionCommand().equals("Following"))
			{
				config.setProperty("tab.following", item.isSelected() + "");
				if (item.isSelected())
				{
					tabPane.insertTab(getResourceMap().getString("followingPane.TabConstraints.tabTitle"), getResourceMap().getIcon("followingPane.TabConstraints.tabIcon"), followingPane, getResourceMap().getString("followingPane.TabConstraints.tabTooltip"), index >= 3 ? 3 : index); // NOI18N
					tabPane.setSelectedComponent(followingPane);
				}
				else
				{
					tabPane.remove(followingPane);
				}
			}
			else if (evt.getActionCommand().equals("Followers"))
			{
				config.setProperty("tab.followers", item.isSelected() + "");
				if (item.isSelected())
				{
					tabPane.insertTab(getResourceMap().getString("followersPane.TabConstraints.tabTitle"), getResourceMap().getIcon("followersPane.TabConstraints.tabIcon"), followersPane, getResourceMap().getString("followersPane.TabConstraints.tabTooltip"), index >= 4 ? 4 : index); // NOI18N
					tabPane.setSelectedComponent(followersPane);
				}
				else
				{
					tabPane.remove(followersPane);
				}
			}
			else if (evt.getActionCommand().equals("Search"))
			{
				config.setProperty("tab.search", item.isSelected() + "");
				if (item.isSelected())
				{
					tabPane.addTab(getResourceMap().getString("searchPanel.TabConstraints.tabTitle"), getResourceMap().getIcon("searchPanel.tabIcon"), searchPanel, getResourceMap().getString("searchPanel.TabConstraints.tabTooltip")); // NOI18N
					tabPane.setSelectedComponent(searchPanel);
				}
				else
				{
					tabPane.remove(searchPanel);
				}
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
					msg.setLocationRelativeTo(TwitzApp.getApplication().getMainFrame());
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
		this.getFrame().setSize(this.getFrame().getWidth(), 150);
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
		this.getFrame().setSize(this.getFrame().getWidth(), 350);
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

	public String getScreenNameFromActiveTab() {
		String rv = "";
		if(tabPane.getSelectedComponent().equals(recentPane)) {
			int row = recentList.getSelectedRow();
			if(row != -1)
				rv = (String)recentList.getValueAt(row, 2);
		}
		else if(tabPane.getSelectedComponent().equals(friendsPane)) {
			if(friendsNameList.getSelectedIndex() != -1)
				rv = (String)friendsNameList.getSelectedValue();
		}
		else if(tabPane.getSelectedComponent().equals(blockedPane)) {
			if(blockedList.getSelectedIndex() != -1)
				rv = (String)blockedList.getSelectedValue();
		}
		else if(tabPane.getSelectedComponent().equals(followingPane)) {
			if(followingList.getSelectedIndex() != -1)
				rv = (String)followingList.getSelectedValue();
		}
		else if(tabPane.getSelectedComponent().equals(followersPane)) {
			if(followersList.getSelectedIndex() != -1)
				rv = (String)followersList.getSelectedValue();
		}
		else if(tabPane.getSelectedComponent().equals(searchPanel)) {
			int row = tblSearch.getSelectedRow();
			if(row != -1)
				rv = (String)tblSearch.getValueAt(row, 2);
		}
		return rv;
	}

	public Vector<String> getScreenNamesFromActiveTab() {
		Vector<String> rv = new Vector<String>();
		String ua = "";
		String ub = "";
		if(tabPane.getSelectedComponent().equals(recentPane)) {
			int row[] = recentList.getSelectedRows();
			if(row.length >= 2) {
				rv.add((String)recentList.getValueAt(row[0], 2));
				rv.add((String)recentList.getValueAt(row[1], 2));
				return rv;
			}
		}
		else if(tabPane.getSelectedComponent().equals(friendsPane)) {
			Object row[] = friendsNameList.getSelectedValues();
			if(row.length >= 2) {
				rv.add((String)row[0]);
				rv.add((String)row[1]);
				return rv;
			}
		}
		else if(tabPane.getSelectedComponent().equals(blockedPane)) {
			Object row[] = blockedList.getSelectedValues();
			if(row.length >= 2) {
				rv.add((String)row[0]);
				rv.add((String)row[1]);
				return rv;
			}
		}
		else if(tabPane.getSelectedComponent().equals(followingPane)) {
			Object row[] = followingList.getSelectedValues();
			if(row.length >= 2) {
				rv.add((String)row[0]);
				rv.add((String)row[1]);
				return rv;
			}
		}
		else if(tabPane.getSelectedComponent().equals(followersPane)) {
			Object row[] = followersList.getSelectedValues();
			if(row.length >= 2) {
				rv.add((String)row[0]);
				rv.add((String)row[1]);
				return rv;
			}
		}
		else if(tabPane.getSelectedComponent().equals(searchPanel)) {
			int row[] = tblSearch.getSelectedRows();
			if(row.length >= 2) {
				rv.add((String)tblSearch.getValueAt(row[0], 2));
				rv.add((String)tblSearch.getValueAt(row[1], 2));
				return rv;
			}
		}
		return rv;
	}

	//TwitterListener abstract methods
	// <editor-fold defaultstate="collapsed" desc="Abstract Methods">

	//TwitzListener
	/**
	 * Event Processor for all events that are related to twitter activity in the #link TwitterManager
	 * A TwitzEvent will will have and type.
	 * @param t
	 */
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
		//System.out.println(evt.getSource().toString());
		String screenName = null;
		int action = -1;
		try
		{
			action = Integer.parseInt(evt.getActionCommand());
		}
		catch(NumberFormatException ignore) {}
		if(action != -1) {
			switch(action) {
				case TwitterConstants.SEARCH:
					Query query = new Query();
					query.setQuery(txtSearch.getText());
					if(!txtSinceDate.getEditor().getText().equals(""))
						query.setSince(txtSinceDate.getEditor().getText());
					if(!txtUntilDate.getEditor().getText().equals(""))
						query.setUntil(txtUntilDate.getEditor().getText());
					query.setRpp(Integer.parseInt((String)cmbRpp.getSelectedItem()));
					aTwitter.search(query);
					break;
				case TwitterConstants.TRENDS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.CURRENT_TRENDS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.DAILY_TRENDS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.WEEKLY_TRENDS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.PUBLIC_TIMELINE:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.HOME_TIMELINE:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.FRIENDS_TIMELINE:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.USER_TIMELINE:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.MENTIONS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.RETWEETED_BY_ME:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.RETWEETED_TO_ME:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.RETWEETS_OF_ME:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.SHOW_STATUS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.UPDATE_STATUS:
					String tweet = txtTweet.getText();
					btnTweet.setEnabled(false);
					txtTweet.setEnabled(false);
					aTwitter.updateStatus(tweet);
					break;
				case TwitterConstants.DESTROY_STATUS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.RETWEET_STATUS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.RETWEETS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.SHOW_USER:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.LOOKUP_USERS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.SEARCH_USERS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.SUGGESTED_USER_CATEGORIES:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.USER_SUGGESTIONS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.FRIENDS_STATUSES:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.FOLLOWERS_STATUSES:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.CREATE_USER_LIST:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.UPDATE_USER_LIST:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.USER_LISTS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.SHOW_USER_LIST:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.DESTROY_USER_LIST:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.USER_LIST_STATUSES:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.USER_LIST_MEMBERSHIPS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.USER_LIST_SUBSCRIPTIONS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.LIST_MEMBERS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.ADD_LIST_MEMBER:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.DELETE_LIST_MEMBER:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.CHECK_LIST_MEMBERSHIP:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.LIST_SUBSCRIBERS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.SUBSCRIBE_LIST:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.UNSUBSCRIBE_LIST:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.CHECK_LIST_SUBSCRIPTION:
					break;
				case TwitterConstants.DIRECT_MESSAGES:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.SENT_DIRECT_MESSAGES:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.SEND_DIRECT_MESSAGE:
					System.out.println("Send Direct Meessage Clicked");
					screenName = getScreenNameFromActiveTab();
					String text = JOptionPane.showInputDialog("Compose Message for "+screenName);
					if(text != null)
						aTwitter.sendDirectMessage(screenName, text);
					break;
				case TwitterConstants.DESTROY_DIRECT_MESSAGES:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.CREATE_FRIENDSHIP:
					screenName = getScreenNameFromActiveTab();
					if(!screenName.equals(""))
						aTwitter.createFriendship(screenName);
					System.out.println("Create Friendship clicked");
					break;
				case TwitterConstants.DESTROY_FRIENDSHIP:
					screenName = getScreenNameFromActiveTab();
					if(!screenName.equals(""))
						aTwitter.destroyFriendship(screenName);
					break;
				case TwitterConstants.EXISTS_FRIENDSHIP:
					names = getScreenNamesFromActiveTab();
					if(names.size() >= 2)
					{
						aTwitter.existsFriendship(names.get(0), names.get(1));
					}
					break;
				case TwitterConstants.SHOW_FRIENDSHIP:
					names = getScreenNamesFromActiveTab();
					if(names.size() >= 2) {
						aTwitter.showFriendship(names.get(0), names.get(1));
					}
					break;
				case TwitterConstants.INCOMING_FRIENDSHIPS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.OUTGOING_FRIENDSHIPS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.FRIENDS_IDS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.FOLLOWERS_IDS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.RATE_LIMIT_STATUS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.UPDATE_DELIVERY_DEVICE:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.UPDATE_PROFILE_COLORS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.UPDATE_PROFILE_IMAGE:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.UPDATE_PROFILE_BACKGROUND_IMAGE:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.UPDATE_PROFILE:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.FAVORITES:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.CREATE_FAVORITE:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.DESTROY_FAVORITE:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.ENABLE_NOTIFICATION:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.DISABLE_NOTIFICATION:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.CREATE_BLOCK:
					System.out.println("Create Block clicked");
					screenName = getScreenNameFromActiveTab();
					if(!screenName.equals(""))
						aTwitter.createBlock(screenName);
					break;
				case TwitterConstants.DESTROY_BLOCK:
					screenName = getScreenNameFromActiveTab();
					if(!screenName.equals(""))
						aTwitter.destroyBlock(screenName);
					break;
				case TwitterConstants.EXISTS_BLOCK:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.BLOCKING_USERS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.BLOCKING_USERS_IDS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.REPORT_SPAM:
					System.out.println("Report SPAM clicked");
					//get the username from the active tab and selected row
					screenName = getScreenNameFromActiveTab();
					if(!screenName.equals("")) {
						try
						{
							aTwitter.reportSpam(screenName);
						}
						catch (TwitterException ex)
						{
							onException(ex, TwitterMethod.REPORT_SPAM);
						}
					}
					break;
				case TwitterConstants.AVAILABLE_TRENDS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.LOCATION_TRENDS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.NEAR_BY_PLACES:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
//					screenName = getScreenNameFromActiveTab();
//					User usr = null;
//					try
//					{
//						usr = tm.getTwitterInstance().showUser(screenName);
//					}
//					catch (TwitterException ex)
//					{
//						displayError(ex, "Error", "Error while looking up GEO data", NEAR_BY_PLACES);
//					}
//					if(usr != null) {
//						//GeoQuery q1 = new GeoQuery();
//						//aTwitter.getNearbyPlaces(query);
//					}
					break;
				case TwitterConstants.REVERSE_GEO_CODE:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.GEO_DETAILS:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
				case TwitterConstants.TEST:
					firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
					break;
			} //End switch()
		} //End if(action != -1)
	}

	// TwitterListener
	public void searched(QueryResult queryResult)
	{
		Vector results = new Vector();
		List<Tweet> l = queryResult.getTweets();
		for(Tweet t : l) {
			//results.add(t);
			Vector v = new Vector();
			v.add(t.getFromUser());
			v.add(t.getText());
			results.add(v);
		}
		DefaultTableModel m = (DefaultTableModel) tblSearch.getModel();
		m.setDataVector(results, searchHeaders);
		lblPage.setText(queryResult.getPage()+"");
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void gotTrends(Trends trends)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotCurrentTrends(Trends trends)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotDailyTrends(List<Trends> trendsList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotWeeklyTrends(List<Trends> trendsList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotPublicTimeline(ResponseList<Status> statuses)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotHomeTimeline(ResponseList<Status> statuses)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotFriendsTimeline(ResponseList<Status> statuses)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotUserTimeline(ResponseList<Status> statuses)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotMentions(ResponseList<Status> statuses)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotRetweetedByMe(ResponseList<Status> statuses)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotRetweetedToMe(ResponseList<Status> statuses)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotRetweetsOfMe(ResponseList<Status> statuses)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotShowStatus(Status status)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void updatedStatus(Status status)
	{
		txtTweet.setText("");
		btnTweet.setEnabled(false);

		if (chkCOT.isSelected())
		{
			mainApp.toggleWindowView("down");
		}
		updateTweetsList(status);
		//firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void destroyedStatus(Status destroyedStatus)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void retweetedStatus(Status retweetedStatus)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotRetweets(ResponseList<Status> retweets)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotUserDetail(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void lookedupUsers(ResponseList<User> users)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void searchedUser(ResponseList<User> userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotSuggestedUserCategories(ResponseList<Category> category)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotUserSuggestions(ResponseList<User> users)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotFriendsStatuses(PagableResponseList<User> users)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotFollowersStatuses(PagableResponseList<User> users)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void createdUserList(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void updatedUserList(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotUserLists(PagableResponseList<UserList> userLists)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotShowUserList(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void destroyedUserList(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotUserListStatuses(ResponseList<Status> statuses)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotUserListMemberships(PagableResponseList<UserList> userLists)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotUserListSubscriptions(PagableResponseList<UserList> userLists)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotUserListMembers(PagableResponseList<User> users)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void addedUserListMember(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void deletedUserListMember(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void checkedUserListMembership(User users)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotUserListSubscribers(PagableResponseList<User> users)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void subscribedUserList(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void unsubscribedUserList(UserList userList)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void checkedUserListSubscription(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotDirectMessages(ResponseList<DirectMessage> messages)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotSentDirectMessages(ResponseList<DirectMessage> messages)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void sentDirectMessage(DirectMessage message)
	{
		String str[] = {"Twitz Message", getResourceMap().getString("DIRECT_MESSAGE_SENT.TEXT", message.getRecipientScreenName()), "2"};
		this.firePropertyChange("POPUP", new Object(), str);
		//firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void destroyedDirectMessage(DirectMessage message)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void createdFriendship(User user)
	{
		String[] str = {"Twitz Message", getResourceMap().getString("FRIENDSHIP_CREATED.TEXT",user.getScreenName()),"2"};
		this.firePropertyChange("POPUP", new Object(), str);
	}

	public void destroyedFriendship(User user)
	{
		String[] str = {"Twitz Message", getResourceMap().getString("FRIENDSHIP_DELETED.TEXT",user.getScreenName()),"2"};
		this.firePropertyChange("POPUP", new Object(), str);
	}

	public void gotExistsFriendship(boolean exists)
	{
		if(exists) {
			String[] str = {"Twitz Message", getResourceMap().getString("FRIENDSHIP_EXISTS.TEXT",names.get(0),names.get(1)),"2"};
			this.firePropertyChange("POPUP", new Object(), str);
		}
		else {
			String[] str = {"Twitz Message", getResourceMap().getString("FRIENDSHIP_NOT_EXISTS.TEXT",names.get(0),names.get(1)),"2"};
			this.firePropertyChange("POPUP", new Object(), str);
		}
		//firePropertyChange("POPUP", new Object(), {"Twitz Message", "Not supported yet","2"});
	}

	public void gotShowFriendship(Relationship relationship)
	{
		//TODO create model for displaying relationships
		//relationship.//.getSourceUserScreenName();
		//firePropertyChange("POPUP", new Object(), {"Twitz Message", "Not supported yet","2"});
	}

	public void gotIncomingFriendships(IDs ids)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotOutgoingFriendships(IDs ids)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotFriendsIDs(IDs ids)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotFollowersIDs(IDs ids)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotRateLimitStatus(RateLimitStatus rateLimitStatus)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void updatedDeliveryDevice(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void updatedProfileColors(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void updatedProfileImage(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void updatedProfileBackgroundImage(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void updatedProfile(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotFavorites(ResponseList<Status> statuses)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void createdFavorite(Status status)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void destroyedFavorite(Status status)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void enabledNotification(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void disabledNotification(User user)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void createdBlock(User user)
	{
		DefaultListModel m = (DefaultListModel)blockedList.getModel();
		m.addElement(user.getScreenName());
		//firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void destroyedBlock(User user)
	{
		DefaultListModel m = (DefaultListModel)blockedList.getModel();
		int i = m.indexOf(user.getScreenName());
		if(i != -1)
			m.removeElementAt(i);
		//firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotExistsBlock(boolean blockExists)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotBlockingUsers(ResponseList<User> blockingUsers)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotBlockingUsersIDs(IDs blockingUsersIDs)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void reportedSpam(User reportedSpammer)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotAvailableTrends(ResponseList<Location> locations)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotLocationTrends(Trends trends)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotNearByPlaces(ResponseList<Place> places)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotReverseGeoCode(ResponseList<Place> places)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void gotGeoDetails(Place place)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void tested(boolean test)
	{
		firePropertyChange("POPUP", new Object(), new String[]{"Twitz Message", "Not supported yet","2"});
	}

	public void onException(TwitterException te, TwitterMethod method)
	{
		if(method != null && method.equals(TwitterMethod.UPDATE_STATUS))
		{
			btnTweet.setEnabled(true);
			txtTweet.setEnabled(true);
		}
			//sendTweetClicked().cancel(true);
		System.out.println(method);
		if(method != null)
			displayError(te, "Twitter Error", "Error Occurred while attempting: \n\t"+tm.getResourceMap().getString(method.name()), method);
		else
			displayError(te, "Twitter Error", "Unexpected fatal error\nCannot complete operation\n"+te.getCause().getMessage(), null);
	}
	//END abstract methods
	//</editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Generated Variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JMenu actionsMenu;
    private javax.swing.JToolBar advSearchBar;
    private javax.swing.JList blockedList;
    private javax.swing.JScrollPane blockedPane;
    private javax.swing.JButton btnExitSearch;
    private javax.swing.JButton btnMini;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTweet;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkCOT;
    private javax.swing.JComboBox cmbRpp;
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JLabel lblChars;
    private javax.swing.JLabel lblPage;
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
    private org.jdesktop.swingx.JXDatePicker txtSinceDate;
    private javax.swing.JTextArea txtTweet;
    private org.jdesktop.swingx.JXDatePicker txtUntilDate;
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
	private ResourceMap resource;// = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(twitz.twitter.TwitterManager.class);

	private Logger logger = Logger.getLogger(TwitzView.class.getName());

	private Vector searchHeaders = new Vector();
	private Vector<String> names = new Vector<String>();

}
