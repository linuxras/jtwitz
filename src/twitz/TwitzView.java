/*
 * TwitzView.java
 */

package twitz;

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
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import javax.swing.DefaultListModel;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import twitter4j.TwitterException;
import twitz.dialogs.MessageDialog;
import twitz.events.TwitzListener;
import twitz.twitter.TwitterManager;

/**
 * The application's main frame.
 */
public class TwitzView extends FrameView implements TwitzListener{

    public TwitzView(SingleFrameApplication app) {
        super(app);
		mainApp = (TwitzApp)app;
		//tray = t;
		//config = c;
		tm = TwitterManager.getInstance();
		tm.addTwitzListener(this);
        initComponents();
		this.getFrame().setVisible(false);
		this.getFrame().setSize(400, 300);
		btnTweet.setEnabled(false);

		//look & feel setup
		//Default to full view
		boolean m = config.getBoolean("minimode");
		fullTwitz();
		if(m) {
			miniTwitz();
		}
		else
		{
			fullTwitz();
		}

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
	}

	/**
	 * This method is called on startup in the constructor.
	 * It is used to configure the GUI with the default from the config file.
	 */
	private void setupDefaults() {
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
			message("startMessage", 2);
			twitter4j.User u = null;
			try
			{
				u = tm.getTwitterInstance().verifyCredentials();
			}
			catch (TwitterException ex)
			{
				errors++;
				failed(ex);
			}
			if (u != null)
			{
				try
				{
					tm.sendTweet(tweet);
				}
				catch (TwitterException ex)
				{
					errors++;
					failed(ex);
				}
				catch (IllegalStateException ex)
				{
					errors++;
					failed(ex);
				}
				message("finishMessage", 2, errors);
			}
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

		@Override protected void failed(Throwable t) {
			logger.log(Level.SEVERE, t.getMessage());
			if(t instanceof TwitterException) {
				TwitterException te = (TwitterException)t;
				if(te.isCausedByNetworkIssue()) {
					displayError(t, "Network Error", "Unable to reach: "+te.getMessage());
					//errorDialog.showMessageDialog(getFrame(), "Unable to reach " + te.getMessage(), "Network Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(te.resourceNotFound()) {
					//errorDialog.showMessageDialog(getFrame(), "Unable to locate resource: " + te.getMessage(), "Resource Alocation Error", JOptionPane.ERROR_MESSAGE);
					displayError(t, "Resource Allocation Error", "Unable to locate resource: "+te.getMessage());
				}
			}
			message("finishMessage", 2, errors);
		}
    }

	private void displayError(Throwable t, String title, Object message) {
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
	public void eventOccurred(TwitzEvent t)
	{
		int type = t.getEventType();
		switch(type) {
			case TwitzEvent.UPDATE:
				twitter4j.Status s = (twitter4j.Status) t.getSource();
				twitter4j.User u = s.getUser();
				String me = u.getScreenName();

				DefaultListModel model = (DefaultListModel) tweetsList.getModel();
				model.addElement(me+": "+s.getText());
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
        tweetsList = new javax.swing.JList();
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

        tweetsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tweetsList.setCellRenderer(new TweetsListRenderer());
        tweetsList.setName("tweetsList"); // NOI18N
        recentPane.setViewportView(tweetsList);

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

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getActionMap(TwitzView.class, this);
        btnTweet.setAction(actionMap.get("sendTweetClicked")); // NOI18N
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

        editMenu.add(menuTabs);

        menuBar.add(editMenu);

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

	@Action
	private void keyTyped(java.awt.event.KeyEvent evt) {
		switch(evt.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				sendTweetClicked().execute();
				//txtTweet.setText("");
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

	// <editor-fold defaultstate="collapsed" desc="Generated Variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JList blockedList;
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
    private javax.swing.JList friendsList;
    private javax.swing.JList friendsNameList;
    private javax.swing.JSplitPane friendsPane;
    private javax.swing.JMenuItem helpItem;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JCheckBoxMenuItem menuItemBlocked;
    private javax.swing.JCheckBoxMenuItem menuItemFollowers;
    private javax.swing.JCheckBoxMenuItem menuItemFollowing;
    private javax.swing.JCheckBoxMenuItem menuItemFriends;
    private javax.swing.JMenu menuTabs;
    private javax.swing.JMenuItem miniItem;
    private javax.swing.JScrollPane nameListPane;
    private javax.swing.JMenuItem prefsItem;
    private javax.swing.JMenuItem prefsMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JScrollPane recentPane;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JList tweetsList;
    private javax.swing.JScrollPane tweetsPane;
    private javax.swing.JTextField txtTweet;
    // End of variables declaration//GEN-END:variables
	//</editor-fold>

	private SettingsManager config = SettingsManager.getInstance();
	//private TwitzTrayIcon tray;
	//private TwitterManager tm; // = new TwitterManager();
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
	private PreferencesDialog prefs;
	//private TwitzViewMini mini;
	private twitz.twitter.TwitterManager tm;
	//private JOptionPane errorDialog = new JOptionPane();
	private boolean minimode = false;
	private TwitzApp mainApp;

	private Logger logger = Logger.getLogger(TwitzView.class.getName());
}
