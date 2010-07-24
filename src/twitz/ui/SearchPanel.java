/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SearchPanel.java
 *
 * Created on Jul 3, 2010, 9:49:11 PM
 */

package twitz.ui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Tweet;
import twitter4j.User;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventModel;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;
import twitz.ui.models.ContactsListModel;
import twitz.ui.models.TweetListModel;
import twitz.util.TwitzSessionManager;
import twitz.util.UserStore;

/**
 *
 * @author mistik1
 */
public class SearchPanel extends javax.swing.JPanel implements TwitzEventModel {

    /** Creates new form SearchPanel */
    public SearchPanel(String session) {
		setSessionName(session);
		actionMap = twitz.TwitzApp.getContext().getActionMap(SearchPanel.class, this);
		resourceMap = twitz.TwitzApp.getContext().getResourceMap(SearchPanel.class);
        initComponents();
		initDefaults();
    }

	private void initDefaults()
	{
		twitz.TwitzMainView.fixJScrollPaneBarsSize(searchPane);
		this.cmbSearchType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				String item = (String)e.getItem();
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					setSearchType(cmbSearchType.getSelectedIndex());
				}
			}
		});
		//the page label gets squashed by the push spacer since JLabel
		//auto truncates to make up for size. wish it would in a toolbar too
		lblPage.setMinimumSize(new java.awt.Dimension(50,20));
		cmbRpp.setSelectedIndex(3); //Default to 20 result per page
		//Set a default search type;
		setSearchType(cmbSearchType.getSelectedIndex());
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked") //{{{
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contactsList = new twitz.ui.ContactsList();
        statusList = new twitz.ui.StatusList(sessionName);
        tweetList = new twitz.ui.TweetList();
        searchPanel = new javax.swing.JPanel();
        searchBar = new javax.swing.JToolBar();
        cmbSearchType = new javax.swing.JComboBox();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        searchPane = new javax.swing.JScrollPane();
        advSearchBar = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        txtSinceDate = new org.jdesktop.swingx.JXDatePicker();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        txtUntilDate = new org.jdesktop.swingx.JXDatePicker();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jLabel3 = new javax.swing.JLabel();
        cmbRpp = new javax.swing.JComboBox();
        pagingToolBar = new javax.swing.JToolBar();
        btnPrev = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        lblPage = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnNext = new javax.swing.JButton();

        contactsList.setName("contactsList"); // NOI18N

        statusList.setCellRenderer(new twitz.ui.renderers.StatusListPanelRenderer());
        statusList.setName("statusList"); // NOI18N

        tweetList.setName("tweetList"); // NOI18N

        setName("SearchPanel"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        searchPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        searchPanel.setName("searchPanel"); // NOI18N

        searchBar.setFloatable(false);
        searchBar.setRollover(true);
        searchBar.setName("searchBar"); // NOI18N

        cmbSearchType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tweet", "User" })); 
        cmbSearchType.setToolTipText(resourceMap.getString("cmbSearchType.toolTipText")); // NOI18N
        cmbSearchType.setName("cmbSearchType"); // NOI18N
        searchBar.add(cmbSearchType);

        txtSearch.setName("txtSearch"); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        searchBar.add(txtSearch);

        btnSearch.setAction(actionMap.get("doSearch")); // NOI18N
        btnSearch.setIcon(resourceMap.getIcon("btnSearch.icon")); // NOI18N
        btnSearch.setText(resourceMap.getString("btnSearch.text")); // NOI18N
        btnSearch.setEnabled(false);
        btnSearch.setFocusable(false);
        btnSearch.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSearch.setName("btnSearch"); // NOI18N
        searchBar.add(btnSearch);

        searchPane.setName("searchPane"); // NOI18N

        advSearchBar.setFloatable(false);
        advSearchBar.setRollover(true);
        advSearchBar.setMaximumSize(new java.awt.Dimension(32600, 28));
        advSearchBar.setMinimumSize(new java.awt.Dimension(300, 28));
        advSearchBar.setName("advSearchBar"); // NOI18N

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

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        advSearchBar.add(jLabel3);

        cmbRpp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "70", "80", "90", "100" }));
        cmbRpp.setSelectedItem(20);
        cmbRpp.setToolTipText(resourceMap.getString("cmbRpp.toolTipText")); // NOI18N
        cmbRpp.setMaximumSize(new java.awt.Dimension(52, 24));
        cmbRpp.setName("cmbRpp"); // NOI18N
        advSearchBar.add(cmbRpp);

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(searchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
            .addComponent(advSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
            .addComponent(searchPane, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(advSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchPane, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
        );

        add(searchPanel, java.awt.BorderLayout.CENTER);

        pagingToolBar.setFloatable(false);
        pagingToolBar.setRollover(true);
        pagingToolBar.setName("pagingToolBar"); // NOI18N
        pagingToolBar.setPreferredSize(new java.awt.Dimension(173, 22));

        btnPrev.setAction(actionMap.get("setPrevious")); // NOI18N
        btnPrev.setIcon(resourceMap.getIcon("btnPrev.icon")); // NOI18N
        btnPrev.setText(resourceMap.getString("btnPrev.text")); // NOI18N
        btnPrev.setEnabled(false);
        btnPrev.setFocusable(false);
        btnPrev.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPrev.setName("btnPrev"); // NOI18N
        pagingToolBar.add(btnPrev);

        jSeparator1.setMaximumSize(new java.awt.Dimension(1000, 10));
        jSeparator1.setName("jSeparator1"); // NOI18N
        pagingToolBar.add(jSeparator1);

        lblPage.setText(resourceMap.getString("lblPage.text")); // NOI18N
        lblPage.setName("lblPage"); // NOI18N
        pagingToolBar.add(lblPage);

        jSeparator4.setMaximumSize(new java.awt.Dimension(1000, 10));
        jSeparator4.setName("jSeparator4"); // NOI18N
        pagingToolBar.add(jSeparator4);

        btnNext.setAction(actionMap.get("getNext")); // NOI18N
        btnNext.setIcon(resourceMap.getIcon("btnNext.icon")); // NOI18N
        btnNext.setText(resourceMap.getString("btnNext.text")); // NOI18N
        btnNext.setToolTipText(resourceMap.getString("btnNext.toolTipText")); // NOI18N
        btnNext.setEnabled(false);
        btnNext.setFocusable(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnNext.setName("btnNext"); // NOI18N
        btnNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pagingToolBar.add(btnNext);

        add(pagingToolBar, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
	//}}}

	private void txtSearchKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSearchKeyReleased
	{//GEN-HEADEREND:event_txtSearchKeyReleased
		switch(evt.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
				btnSearch.doClick();
				break;
			default:
				if(!txtSearch.getText().equals(""))
				{
					btnSearch.setEnabled(true);
				}
				else
				{
					btnSearch.setEnabled(false);
					setCurrentPage(1);
				}
		}
	}//GEN-LAST:event_txtSearchKeyReleased

	private void txtSinceDateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtSinceDateActionPerformed
	{//GEN-HEADEREND:event_txtSinceDateActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_txtSinceDateActionPerformed

	private void txtUntilDateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtUntilDateActionPerformed
	{//GEN-HEADEREND:event_txtUntilDateActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_txtUntilDateActionPerformed

	// <editor-fold defaultstate="collapsed" desc="Generated Variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar advSearchBar;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox cmbRpp;
    private javax.swing.JComboBox cmbSearchType;
    private twitz.ui.ContactsList contactsList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JLabel lblPage;
    private javax.swing.JToolBar pagingToolBar;
    private javax.swing.JToolBar searchBar;
    private javax.swing.JScrollPane searchPane;
    private javax.swing.JPanel searchPanel;
    private twitz.ui.StatusList statusList;
    private twitz.ui.TweetList tweetList;
    private javax.swing.JTextField txtSearch;
    private org.jdesktop.swingx.JXDatePicker txtSinceDate;
    private org.jdesktop.swingx.JXDatePicker txtUntilDate;
    // End of variables declaration//GEN-END:variables
	// </editor-fold>

	private static final int TWEET = 0;
	private static final int USER = 1;

	private int searchType = TWEET;
	private int currentPage = 1;

	private javax.swing.ActionMap actionMap;
	private org.jdesktop.application.ResourceMap resourceMap;
	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private String lastSearch = "";
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private final UserStore store = UserStore.getInstance();
	private String sessionName;
	public static final String SESSION_PROPERTY = "sessionName";
	twitz.TwitzMainView view;

	@Action
	public void doSearch()//{{{
	{
		btnSearch.setEnabled(true);
		Map map = null;
		ArrayList args = null;
		switch(this.searchType)
		{
			case TWEET:
				Query query = new Query();
				String searchText = txtSearch.getText();
				if(!searchText.equals(lastSearch))
				{
					setCurrentPage(1);
				}
				lastSearch = searchText;
				query.setQuery(searchText);
				if (!txtSinceDate.getEditor().getText().equals(""))
				{
					query.setSince(txtSinceDate.getEditor().getText());
				}
				if (!txtUntilDate.getEditor().getText().equals(""))
				{
					query.setUntil(txtUntilDate.getEditor().getText());
				}
				query.setRpp(Integer.parseInt((String) cmbRpp.getSelectedItem()));
				query.setPage(currentPage);
				map = Collections.synchronizedMap(new TreeMap());
				map.put("async", true);
				map.put("caller", this);
				args = new ArrayList();
				args.add(query);
				map.put("arguments", args);
				fireTwitzEvent(new TwitzEvent(this, TwitzEventType.SEARCH, new java.util.Date().getTime(), map));
				break;
			case USER:
				map = Collections.synchronizedMap(new TreeMap());
				map.put("async", true);
				map.put("caller", this);
				args = new ArrayList();
				args.add(txtSearch.getText());
				args.add(currentPage);
				map.put("arguments", args);
				fireTwitzEvent(new TwitzEvent(this, TwitzEventType.SEARCH_USERS, new java.util.Date().getTime(), map));
				break;
		}
	}//}}}
	
	@Action
	public void getNext()//{{{
	{
		if(currentPage < 1500) {
			currentPage = currentPage+1;
			doSearch();
		}
	}//}}}

	@Action
	public void setPrevious() //{{{
	{
		if(currentPage > 1) {
			currentPage = currentPage-1;
			doSearch();
		}
	}//}}}

	public final void setSessionName(String name)
	{
		String old = this.sessionName;
		this.sessionName = name;
		view = TwitzSessionManager.getInstance().getTwitMainViewForSession(sessionName);
		//config = TwitzSessionManager.getInstance().getSettingsManagerForSession(sessionName);
		//firePropertyChange(SESSION_PROPERTY, old, name);
	}

	public String getSessionName()
	{
		return this.sessionName;
	}

	public void setCurrentPage(int page)
	{
		this.currentPage = page;
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	/**
	 * This sets the current search type being performed and updates
	 * the viewport with the currect renderer.
	 * @param type The type on search can be one of {@link #TWEET}</code> or {@link #USER}
	 */
	public void setSearchType(int type)//{{{
	{
		this.searchType = type;
		switch(searchType)
		{
			case TWEET:
				searchPane.setViewportView(tweetList);
				cmbSearchType.setSelectedIndex(TWEET);
				break;
			case USER:
				searchPane.setViewportView(contactsList);
				cmbSearchType.setSelectedIndex(USER);
				break;
		}
	}//}}}

	public void setSearchText(String text) 
	{
		txtSearch.setText(text);
	}

	public int getSearchType()
	{
		return this.searchType;
	}

	public Component getCurrentComponent()
	{
		return (this.searchType == TWEET) ? tweetList : contactsList;
	}

	public void updateTweetsList(final QueryResult results)//{{{
	{
		if(results != null)
		{
			lblPage.setText("Page "+currentPage);
			lblPage.setToolTipText("Page "+currentPage);
			searchPane.setViewportView(tweetList);
			btnPrev.setEnabled((currentPage > 1));
			btnNext.setEnabled((currentPage < 1500));
			final List<Tweet> tweets = results.getTweets();
			SwingWorker<TweetListModel, Object> worker = new SwingWorker<TweetListModel, Object>()
			{
				public TweetListModel doInBackground()
				{
					int total = tweets.size(), count = 1;
					firePropertyChange("started", null, "Loading search results");
					double completedIn = results.getCompletedIn();
					//do something with tweets.
					TweetListModel tlm = new TweetListModel();
					//tlm.clear();
					for(Tweet t : tweets)
					{
						tlm.addTweet(t);
						firePropertyChange("message", null, String.format("Loading %d of %d results. Please wait...", count, total));
						count++;
					}
					return tlm;
				}

				@Override
				public void done()
				{
					try
					{
						tweetList.setModel(get());
					}
					catch(Exception e)
					{
						logger.error("Error while loading search results", e);//TODO needs I18N
					}
					firePropertyChange("done", null, null);
				}
			};
			worker.addPropertyChangeListener(view.getStatusListener());
			worker.execute();
		}
	}//}}}

	private boolean isUser(Object o)
	{
		return (o instanceof User);
	}

	public void updateUsersList(final ResponseList results)//{{{
	{
		if(results != null)
		{
			lblPage.setText("Page "+currentPage);
			searchPane.setViewportView(contactsList);
			btnPrev.setEnabled((currentPage > 1));
			btnNext.setEnabled((currentPage < 1500));
			SwingWorker<ContactsListModel, Object> worker = new SwingWorker<ContactsListModel, Object>()
			{

				public ContactsListModel doInBackground()
				{
					int total = results.size(), count = 1;
					firePropertyChange("started", null, "Loading search results");
					ContactsListModel clm = new ContactsListModel();
					for(Object o : results)
					{
						if(isUser(o))
						{
							User u = (User)o;
							clm.addElement(u);
							store.registerUser(u);
							firePropertyChange("message", null, String.format("Loading %d of %d results. Please wait...", count, total));
							count++;
						}
					}
					return clm;
				}

				@Override
				public void done()
				{
					try
					{
						contactsList.setModel(get());
					}
					catch(Exception e)
					{
						logger.error("Error while loading search results", e);//TODO needs I18N
					}
					firePropertyChange("done", null, null);
				}
			};
			worker.addPropertyChangeListener(view.getStatusListener());
			worker.execute();
		}
	}//}}}

	public void addTwitzListener(TwitzListener o)
	{
		dtem.addTwitzListener(o);
	}

	public void removeTwitzListener(TwitzListener o)
	{
		dtem.removeTwitzListener(o);
	}

	public void fireTwitzEvent(TwitzEvent evt)
	{
		dtem.fireTwitzEvent(evt);
	}
}
