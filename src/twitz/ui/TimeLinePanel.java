/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TimeLinePanel.java
 *
 * Created on Jul 1, 2010, 2:33:21 PM
 */

package twitz.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import javax.accessibility.Accessible;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.plaf.basic.ComboPopup;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.User;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventModel;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;
import twitz.util.*;

/**
 *
 * @author mistik1
 */
public class TimeLinePanel extends javax.swing.JPanel implements TwitzEventModel, ActionListener, MouseListener{

    /** Creates new form TimeLinePanel */
    public TimeLinePanel() {
		 resourceMap = twitz.TwitzApp.getContext().getResourceMap(TimeLinePanel.class);
		 actionMap = twitz.TwitzApp.getContext().getActionMap(TimeLinePanel.class, this);
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

        pagingToolbar = new javax.swing.JToolBar();
        btnTimelinePrev = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnTimelineNext = new javax.swing.JButton();
        timelineToolbar = new javax.swing.JToolBar();
        cmbTimelineType = new javax.swing.JComboBox();
        txtTimelineUser = new javax.swing.JComboBox(UserStore.getInstance().getRegisteredUsers());
        btnRefresh = new javax.swing.JButton();
        statusScrollPane = new javax.swing.JScrollPane();
        statusList = new twitz.ui.StatusList();

        pagingToolbar.setFloatable(false);
        pagingToolbar.setRollover(true);
        pagingToolbar.setName("pagingToolbar"); // NOI18N
        pagingToolbar.setPreferredSize(new java.awt.Dimension(128, 22));

        btnTimelinePrev.setText(resourceMap.getString("btnTimelinePrev.text")); // NOI18N
        btnTimelinePrev.setToolTipText(resourceMap.getString("btnTimelinePrev.toolTipText")); // NOI18N
        btnTimelinePrev.setFocusable(false);
        btnTimelinePrev.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnTimelinePrev.setName("btnTimelinePrev"); // NOI18N
        btnTimelinePrev.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pagingToolbar.add(btnTimelinePrev);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jSeparator4.setPreferredSize(new java.awt.Dimension(1000, 10));
        pagingToolbar.add(jSeparator4);

        btnTimelineNext.setText(resourceMap.getString("btnTimelineNext.text")); // NOI18N
        btnTimelineNext.setToolTipText(resourceMap.getString("btnTimelineNext.toolTipText")); // NOI18N
        btnTimelineNext.setFocusable(false);
        btnTimelineNext.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnTimelineNext.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnTimelineNext.setName("btnTimelineNext"); // NOI18N
        btnTimelineNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pagingToolbar.add(btnTimelineNext);

        setName("TimeLinePanel"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        timelineToolbar.setFloatable(false);
        timelineToolbar.setRollover(true);
        timelineToolbar.setMinimumSize(new java.awt.Dimension(4, 24));
        timelineToolbar.setName("timelineToolbar"); // NOI18N
        timelineToolbar.setPreferredSize(new java.awt.Dimension(124, 24));

        cmbTimelineType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Home", "User", "Public", "Retweeted by me", "Retweeted to me", "Retweets of me", "Mentions" }));
        cmbTimelineType.setToolTipText(resourceMap.getString("cmbTimelineType.toolTipText")); // NOI18N
        cmbTimelineType.setMinimumSize(new java.awt.Dimension(100, 24));
        cmbTimelineType.setName("cmbTimelineType"); // NOI18N
        timelineToolbar.add(cmbTimelineType);

        txtTimelineUser.setToolTipText(resourceMap.getString("txtTimelineUser.toolTipText")); // NOI18N
        txtTimelineUser.setEnabled(false);
        txtTimelineUser.setName("txtTimelineUser"); // NOI18N
        timelineToolbar.add(txtTimelineUser);

        btnRefresh.setAction(actionMap.get("doSearch")); // NOI18N
        btnRefresh.setIcon(resourceMap.getIcon("btnRefresh.icon")); // NOI18N
        btnRefresh.setToolTipText(resourceMap.getString("btnRefresh.toolTipText")); // NOI18N
        btnRefresh.setText(resourceMap.getString("btnRefresh.text")); // NOI18N
        btnRefresh.setFocusable(false);
        btnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnRefresh.setName("btnRefresh"); // NOI18N
        btnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        timelineToolbar.add(btnRefresh);

        add(timelineToolbar, java.awt.BorderLayout.NORTH);

        statusScrollPane.setName("statusScrollPane"); // NOI18N

        statusList.setModel(new twitz.ui.models.StatusListModel());
        statusList.setName("statusList"); // NOI18N
        statusScrollPane.setViewportView(statusList);

        add(statusScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
//}}}
	private void initDefaults()//{{{
	{
		statusPanel = new twitz.ui.StatusPanel(true);
		add(statusPanel, java.awt.BorderLayout.CENTER);
		statusPanel.getStatusList().addMouseListener(this);
		FocusListener fl = new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent e)
			{
				txtTimelineUser.removeAllItems();
				Vector<User> v = UserStore.getInstance().getRegisteredUsers();
				for(User u : v)
				{
					txtTimelineUser.addItem(u);
				}
				Accessible a = txtTimelineUser.getUI().getAccessibleChild(txtTimelineUser, 0);

        		if (a instanceof ComboPopup)
				{
    		        javax.swing.JList list = ((ComboPopup) a).getList();
					list.doLayout();
    		    }
				//ListComboBoxModel lcb = new ListComboBoxModel(UserStore.getInstance().getRegisteredUsersAsList());
				//txtTimelineUser.setModel(lcb);
				//AutoCompleteDecorator.decorate(txtTimelineUser, UserStore.getInstance().getRegisteredUsersAsList(), false, new UserToStringConverter()); 
			}
		};
		//ActionListener
		Timer comboUpdate = new Timer(delay, this);
		comboUpdate.setInitialDelay(initialDelay);
		comboUpdate.setActionCommand("COMBO_TIMER");
		comboUpdate.start();

		//txtTimelineUser.addFocusListener(fl);
		txtTimelineUser.setEnabled(false);
		txtTimelineUser.setEditable(true);
		txtTimelineUser.setRenderer(new twitz.ui.renderers.UserComboRenderer());
		AutoCompleteDecorator.decorate(txtTimelineUser, new UserToStringConverter());
		cmbTimelineType.addItemListener(new ItemListener(){

			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					String item = (String)e.getItem();
					txtTimelineUser.setEnabled("User".equals(item));
				}
			}
		});
		//btnRefresh.setAction(actionMap.get("requestTimeLine"));
		//UserComboBoxAdaptor uca = new UserComboBoxAdaptor(txtTimelineUser, UserStore.getInstance().getRegisteredUsers());
		//AutoCompleteDecorator.decorate(list, textField);txtTimelineUser
		//twitz.TwitzMainView.fixJScrollPaneBarsSize(statusScrollPane);
	}//}}}

	@Action
	public void doSearch()
	{
		String user = null;
		int type = cmbTimelineType.getSelectedIndex();
		Map map = Collections.synchronizedMap(new TreeMap());
		ArrayList args = new ArrayList();
		switch(type)
		{
			case 0: //Home timeline
				map = Collections.synchronizedMap(new TreeMap());
				map.put("async", true);
				map.put("caller", this);
				fireTwitzEvent(new TwitzEvent(this, TwitzEventType.HOME_TIMELINE, new java.util.Date().getTime(), map));
				break;
			case 1: //User
				if(txtTimelineUser.getSelectedItem() instanceof User)
				{
					user = ((User)txtTimelineUser.getSelectedItem()).getScreenName();
				}
				else if(txtTimelineUser.getSelectedItem() instanceof String)
				{
					user = (String)txtTimelineUser.getSelectedItem();
				}
				if(user != null && !user.equals(""))
				{
					map = Collections.synchronizedMap(new TreeMap());
					map.put("async", true);
					map.put("caller", this);
					args = new ArrayList();
					args.add(user);
					map.put("arguments", args);
					fireTwitzEvent(new TwitzEvent(this, TwitzEventType.USER_TIMELINE, new java.util.Date().getTime(), map));
				}
				break;
			case 2: //Public
				map = Collections.synchronizedMap(new TreeMap());
				map.put("async", true);
				map.put("caller", this);
				fireTwitzEvent(new TwitzEvent(this, TwitzEventType.PUBLIC_TIMELINE, new java.util.Date().getTime(), map));
				break;
			case 3: //Retweet by me
				map = Collections.synchronizedMap(new TreeMap());
				map.put("async", true);
				map.put("caller", this);
				fireTwitzEvent(new TwitzEvent(this, TwitzEventType.RETWEETED_BY_ME, new java.util.Date().getTime(), map));
				break;
			case 4: //Retweets to me
				map = Collections.synchronizedMap(new TreeMap());
				map.put("async", true);
				map.put("caller", this);
				fireTwitzEvent(new TwitzEvent(this, TwitzEventType.RETWEETED_TO_ME, new java.util.Date().getTime(), map));
				break;
			case 5: //Retweet of
				map = Collections.synchronizedMap(new TreeMap());
				map.put("async", true);
				map.put("caller", this);
				fireTwitzEvent(new TwitzEvent(this, TwitzEventType.RETWEETS_OF_ME, new java.util.Date().getTime(), map));
				break;
			case 6: //Mentions
				map = Collections.synchronizedMap(new TreeMap());
				map.put("async", true);
				map.put("caller", this);
				fireTwitzEvent(new TwitzEvent(this, TwitzEventType.MENTIONS, new java.util.Date().getTime(), map));
				break;
		}
	}

	public void setStatusList(StatusList list)//{{{
	{
		StatusList old = this.statusList;
		if(list != null)
		{
			statusPanel.setStatusList(list);
			this.firePropertyChange("TimeLinePanelStatusListChanged", old, list);
		}
	}//}}}

	public StatusList getStatusList()
	{
		return statusPanel.getStatusList();
	}

	public void setNextCursor(long cursor)
	{
		this.nextCursor = cursor;
	}

	public long getNextCursor()
	{
		return this.nextCursor;
	}

	public void setPrevCursor(long cursor)
	{
		this.prevCursor = cursor;
	}

	public long getPrevCursor()
	{
		return this.prevCursor;
	}

	public void updateStatus(ResponseList<Status> statuses)
	{
		statusPanel.updateStatus(statuses);
	}
	
	//TwitzEventModel
	public void addTwitzListener(TwitzListener o) {
		statusPanel.addTwitzListener(o);
	}

	public void removeTwitzListener(TwitzListener o) {
		statusPanel.removeTwitzListener(o);
	}

	public void fireTwitzEvent(TwitzEvent e) {
		statusPanel.fireTwitzEvent(e);
	}

	//ActionListener
	public void actionPerformed(ActionEvent e) {//{{{
		String cmd = e.getActionCommand();
		if("COMBO_TIMER".equals(cmd))
		{
			if(txtTimelineUser.isPopupVisible())
				return; //Dont update if combo is currently in use
			final Object obj = txtTimelineUser.getSelectedItem();

			SwingWorker<DefaultComboBoxModel, Object> worker = new SwingWorker<DefaultComboBoxModel, Object>(){

				@Override
				protected DefaultComboBoxModel doInBackground() throws Exception
				{
					DefaultComboBoxModel cmod = new DefaultComboBoxModel(DBM.getRegisteredUsers());
					return cmod;
				}

				@Override
				public void done()
				{
					if(logdebug)
						logger.info("Updating timeline ComboBox");
					try
					{
						txtTimelineUser.setModel(get());
					}
					catch (InterruptedException ex)
					{
						logger.error(ex.getLocalizedMessage(), ex);
					}
					catch (ExecutionException ex)
					{
						logger.error(ex.getLocalizedMessage(), ex);
					}
					if(obj != null)
						txtTimelineUser.setSelectedItem(obj);
				}
			};
			worker.execute();
		}
		else
		{
			Map map = Collections.synchronizedMap(new TreeMap());
			map.put("caller", this);
			map.put("async", true);
			Status[] selections = getStatusList().getSelectedValues();
			//User[] selections = new User[select.length]; //getContactsList().getSelectedValues();
			map.put("selections", selections);
			fireTwitzEvent(new TwitzEvent(this, TwitzEventType.valueOf(cmd), new java.util.Date().getTime(), map));
		}
	}//}}}
	
	//MouseListener
	public void mouseClicked(MouseEvent e) {//{{{
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			java.awt.Point p = e.getPoint();
			if (e.getSource() instanceof StatusList)
			{
				StatusList clist = (StatusList) e.getSource();
				//p = javax.swing.SwingUtilities.convertPoint(this, p, clist);
				int index = clist.locationToIndex(p);
				if (index != -1)
				{ //Show menu only if list is not empty
					if(clist.getSelectedIndex() == -1)
						clist.setSelectedIndex(index);
					//Make the caller this panel as we can add the selected list to the panel
					//that  will make the action listener of the menu items this panel as well
					twitz.TwitzMainView.getInstance().getActionsMenu(this).show(this, p.x, p.y);
				}

			}
		}
		else if(e.getButton() == MouseEvent.BUTTON1) {
			if(e.getSource() instanceof StatusList) {
				StatusList clist = (StatusList)e.getSource();
				
			}
		}
	}//}}}
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e)	{ }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnTimelineNext;
    private javax.swing.JButton btnTimelinePrev;
    private javax.swing.JComboBox cmbTimelineType;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar pagingToolbar;
    private twitz.ui.StatusList statusList;
    private javax.swing.JScrollPane statusScrollPane;
    private javax.swing.JToolBar timelineToolbar;
    private javax.swing.JComboBox txtTimelineUser;
    // End of variables declaration//GEN-END:variables

	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private twitz.ui.StatusPanel statusPanel;
    private org.jdesktop.application.ResourceMap resourceMap;
    private javax.swing.ActionMap actionMap;
	private static final DBManager DBM = DBManager.getInstance();
	private static final Logger logger = Logger.getLogger(TimeLinePanel.class.getName());
	private static final boolean logdebug = logger.isDebugEnabled();
	private long nextCursor = -1;
	private long prevCursor = -1;
	private int delay = 300000;
	private int initialDelay = 10000;
}
