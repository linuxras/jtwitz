/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StatusPanel.java
 *
 * Created on Jul 1, 2010, 11:54:05 AM
 */

package twitz.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.UserList;
import twitz.TwitzMainView;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventModel;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;
import twitz.ui.dialogs.StatusPopupPanel;
import twitz.ui.models.StatusListModel;
import twitz.util.*;

/**
 *
 * @author Andrew Williams
 */
public class StatusPanel extends javax.swing.JLayeredPane implements TwitzEventModel,
		PropertyChangeListener,/* ActionListener,*/ MouseListener, java.io.Serializable
	{
	
	public StatusPanel()
	{
		this(false);
	}

	public StatusPanel(String session)
	{
		this(false, session);
	}
	
	public StatusPanel(boolean timeline)
	{
		this(timeline, "Default");
	}

    /** Creates new form StatusPanel */
    public StatusPanel(boolean timeline, String session) {
		super();
		setSessionName(session);
		this.inTimeline = timeline;
		resourceMap = twitz.TwitzApp.getContext().getResourceMap(StatusPanel.class);
        initComponents();
		initDefaults();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        statusScrollPane = new javax.swing.JScrollPane();
        statusList = new twitz.ui.StatusList(sessionName);

        setName("StatusPanel"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        statusScrollPane.setName("statusScrollPane"); // NOI18N

        statusList.setName("statusList"); // NOI18N
        statusScrollPane.setViewportView(statusList);

        add(statusScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

	private void initDefaults()
	{
		twitz.TwitzMainView.fixJScrollPaneBarsSize(statusScrollPane);
	//	statusScrollPane.setViewportView(status);
	//	status.setFillsViewportHeight(true);
	//	statusList.addHotSpot("Actions", new Rectangle(45, 25, 20, 20));
	//	statusList.addHotSpot("Favorite", new Rectangle(65, 25, 20, 20));
	//	statusList.addHotSpot("Retweet", new Rectangle(85, 25, 20, 20));
	//	statusList.addPropertyChangeListener(this);
		org.jdesktop.application.ResourceMap res = twitz.TwitzApp.getContext().getResourceMap(TwitzMainView.class);
		String resourcesDir = res.getResourcesDir();

		String filename = resourcesDir + res.getString("Retweet.icon");
		//logger.debug(filename);
		URL reet = res.getClassLoader().getResource(filename);
		filename = resourcesDir + res.getString("Retweet.off.icon" );
		URL reetOff = res.getClassLoader().getResource(filename);


		filename = resourcesDir + res.getString("Favorite.icon");
		URL favUrl = res.getClassLoader().getResource(filename);
		filename = resourcesDir + res.getString("Favorite.off.icon");
		URL favOff = res.getClassLoader().getResource(filename);

		filename = resourcesDir + res.getString("icon.bin");
		URL delUrl = res.getClassLoader().getResource(filename);
		//filename = resourcesDir + res.getString("icon.bin");
		//URL delOff = res.getClassLoader().getResource(filename);

		filename = resourcesDir + res.getString("icon.comment_edit");
		URL actionUrl = res.getClassLoader().getResource(filename);

		ListHotSpot action = new ListHotSpot("Actions", 
				ListHotSpot.Direction.RIGHT_TO_LEFT,
				new Rectangle(28, 25, 20, 20),
				res.getString("tooltip.Actions"));
		action.setIcon(actionUrl);
		action.addPropertyChangeListener(this);
		ListHotSpot fav = new ListHotSpot("Favorite", 
				ListHotSpot.Direction.RIGHT_TO_LEFT,
				new Rectangle(48, 25, 20, 20),
				res.getString("tooltip.Favorite"));
		fav.setIcon(favUrl);
		fav.setDisabledIcon(favOff);
		fav.addPropertyChangeListener(this);
		ListHotSpot retweet = new ListHotSpot("Retweet", 
				ListHotSpot.Direction.RIGHT_TO_LEFT,
				new Rectangle(68, 25, 20, 20),
				res.getString("tooltip.Retweet"));
		retweet.setIcon(reet);
		retweet.setDisabledIcon(reetOff);
		//ListHotSpot retweet = new ListHotSpot("Retweet", ListHotSpot.Direction.LEFT_TO_RIGHT, new Rectangle(85, 25, 20, 20), "");
		retweet.addPropertyChangeListener(this);
		ListHotSpot delete = new ListHotSpot("Delete_Status",
				ListHotSpot.Direction.RIGHT_TO_LEFT,
				new Rectangle(88, 25, 20, 20),
				res.getString("tooltip.Delete_Status"));
		delete.setIcon(delUrl);
		delete.setDisabledIcon(delUrl);
		delete.addPropertyChangeListener(this);
		statusList.addHotSpot(action);
		statusList.addHotSpot(fav);
		statusList.addHotSpot(retweet);
		statusList.addHotSpot(delete);
		if(!inTimeline)
			statusList.addMouseListener(this);
	}

	public final void setSessionName(String name)
	{
		String old = this.sessionName;
		this.sessionName = name;
		//config = TwitzSessionManager.getInstance().getSettingsManagerForSession(sessionName);
		view = TwitzSessionManager.getInstance().getTwitzMainViewForSession(sessionName);
		//firePropertyChange(SESSION_PROPERTY, old, name);
	}

	public String getSessionName()
	{
		return this.sessionName;
	}

	public void setUserList(UserList list)
	{
		UserList old = this.userlist;
		if(list != null)
		{
			this.userlist = list;
			setInUserList(true);
			this.firePropertyChange("StatusPanelUserListChanged", old, this.userlist);
		}
		else
		{
			this.userlist = null;
			setInUserList(false);
			this.firePropertyChange("StatusPanelUserListChanged", old, this.userlist);
		}
	}

	public UserList getUserList()
	{
		return this.userlist;
	}

	private void setInUserList(boolean val)
	{
		this.isUserList = val;
	}

	public boolean isInUserList()
	{
		return this.isUserList;
	}
	
	public void setInTimeline(boolean val)
	{
		this.inTimeline = val;
	}

	public boolean isInTimeline()
	{
		return this.inTimeline;
	}

	public void setStatusList(StatusList list)
	{
		StatusList old = this.statusList;
		if(list != null)
		{
			this.statusList = list;
			this.firePropertyChange("StatusPanelStatusListChanged", old, this.statusList);
		}
	}

	public void setStatusTable(JTable list)
	{
		JTable old = this.status;
		if(list != null)
		{
			this.status = list;
			this.firePropertyChange("StatusPanelStatusListChanged", old, this.status);
		}
	}

	public JTable getStatusTable()
	{
		return status;
	}

	public StatusList getStatusList()
	{
		return this.statusList;
	}

	private boolean isStatus(Object o)
	{
		return (o instanceof Status);
	}

	public void updateStatus(final ResponseList statuses)
	{
		if(statuses != null)
		{
			StatusUpdater worker = new StatusUpdater(this, statuses);
			worker.addPropertyChangeListener(view.getStatusListener());
			worker.start();
		}
	}

	class StatusUpdater extends SwingWorker<StatusListModel, ResponseList>
	{
		final javax.swing.JComponent frame;
		int total = -1;
		int count = 1;
		private final ResponseList statuses;
		private TwitzBusyPane busyPane;
		private final StatusUpdater me;

		public StatusUpdater(final javax.swing.JComponent frame, ResponseList list)
		{
			this.statuses = list;
			this.frame = frame;
			me = this;
			Runnable runnable = new Runnable()
			{

				public void run()
				{
					busyPane = new TwitzBusyPane(frame, me);
				}

			};
			if(SwingUtilities.isEventDispatchThread())
			{
				runnable.run();
			}
			else
			{
				try
				{
					SwingUtilities.invokeAndWait(runnable);
				}
				catch (InterruptedException ex)
				{
					logger.error(ex.getLocalizedMessage());
				}
				catch (InvocationTargetException ex)
				{
					logger.error(ex.getLocalizedMessage());
				}
			}
		}

		public void start()
		{
			firePropertyChange("started", null, "processing status list");
			busyPane.block();
		}

		@Override
		public StatusListModel doInBackground()
		{
			StatusListModel model = new StatusListModel();
			total = statuses.size();
			//firePropertyChange("started", null, "processing status list");
			for (Object o : statuses)
			{
				if (isStatus(o))
				{
					firePropertyChange("message", null, String.format("Processing %d of %d records. Please wait...", count, total));
					Status s = (Status) o;
					//publish(s);
					store.registerUser(s.getUser());
					model.addStatus(s);
					count++;
				}
			}
			return model;
			//return null;//I wont be using get() to process anything
		}

//				@Override
//				protected void process(List<Status> part)
//				{
//					for(Status s: part)
//					{
//
//						store.registerUser(s.getUser());
//						model.addStatus(s);
//						count++;
//					}
//				}
		@Override
		protected void done()
		{
			busyPane.unblock();

			try
			{
				getStatusList().setModel(get());
			}
			catch (InterruptedException ex)
			{
				logger.error(ex.getLocalizedMessage());
			}
			catch (ExecutionException ex)
			{
				logger.error(ex.getLocalizedMessage());
			}
			firePropertyChange("done", null, null);

		}

	}

		@Override
	public void setEnabled(boolean enabled)
	{
		statusList.setEnabled(enabled);
		super.setEnabled(enabled);
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
	@Action
	public void menuAction(ActionEvent e) {//{{{
		if (e.getSource() instanceof JMenuItem)
		{
			String cmd = e.getActionCommand();
			logger.debug("StatusPanel---------------=========================== "+cmd);
			if(cmd.equals("USER_TIMELINE"))
			{
				TimeLinePanel panel = view.getTimeLine();
				view.switchTab(0);
				Status s = getStatusList().getSelectedValue();
				panel.timeLineSearch(s.getUser());
				return;
			}
			
			Map map = Collections.synchronizedMap(new TreeMap());
			map.put("caller", this);
			map.put("async", true);
			Status[] selections = getStatusList().getSelectedValues();
			//User[] selections = new User[select.length]; //getContactsList().getSelectedValues();
			map.put("selections", selections);
			fireTwitzEvent(new TwitzEvent(this, TwitzEventType.valueOf(e.getActionCommand()), new java.util.Date().getTime(), map));
		}
	}//}}}
	
	public void propertyChange(PropertyChangeEvent evt)//{{{
	{
		Status lstat = statusList.getSelectedValue();
		if("Actions".equals(evt.getPropertyName()))
		{
			//Status lstat = statusList.getSelectedValue();
			int selection = statusList.getSelectedIndex();
			MouseEvent e = (MouseEvent)evt.getNewValue();
			StatusPopupPanel spp = new StatusPopupPanel(sessionName, inTimeline ? view.getTimeLine() : this);
			//spp.setSessionName(sessionName);
			spp.configureBox(statusList, lstat, selection);
			spp.popupBox(e.getXOnScreen(), e.getYOnScreen());
		}
		else if("Retweet".equals(evt.getPropertyName()))
		{
			//Status lstat = statusList.getSelectedValue();
			Map map = Collections.synchronizedMap(new TreeMap());
			map.put("async", true);
			map.put("caller", inTimeline ? view.getTimeLine() : this);
			ArrayList args = new ArrayList();
			args.add(lstat.getId());
			map.put("arguments", args);
			TwitzEvent te = new TwitzEvent(this, TwitzEventType.RETWEET_STATUS, new Date().getTime(), map);
			fireTwitzEvent(te);
		}
		else if("Delete_Status".equals(evt.getPropertyName()))
		{
			int ans = JOptionPane.showConfirmDialog(view, "Are sure you want to delete status?", "Delete Status", JOptionPane.YES_NO_OPTION);
			if (ans == JOptionPane.OK_OPTION)
			{
				Map map = Collections.synchronizedMap(new TreeMap());
				map.put("async", true);
				map.put("caller", inTimeline ? view.getTimeLine() : this);
				ArrayList args = new ArrayList();
				args.add(lstat.getId());
				map.put("arguments", args);
				TwitzEvent te = new TwitzEvent(this, TwitzEventType.DESTROY_STATUS, new Date().getTime(), map);
				fireTwitzEvent(te);
			}
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
				int index = clist.locationToIndex(p);
				if (index != -1)
				{ //Show menu only if list is not empty
					if(clist.getSelectedIndex() == -1)
						clist.setSelectedIndex(index);
					//Make the caller this panel as we can add the selected list to the panel
					//that  will make the action listener of the menu items this panel as well
					//view.getActionsMenu(this).show(this, p.x, p.y);
					java.awt.Point pe = SwingUtilities.convertPoint(clist, p, this);
					view.getActionsMenu(this).show(this, pe.x, pe.y);
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
    private twitz.ui.StatusList statusList;
    private javax.swing.JScrollPane statusScrollPane;
    // End of variables declaration//GEN-END:variables

//	private twitz.ui.StatusTable status = new twitz.ui.StatusTable();
	private javax.swing.JTable status = null;
	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private Logger logger = Logger.getLogger(StatusPanel.class);
    private org.jdesktop.application.ResourceMap resourceMap;
	private boolean isUserList = false;
	private boolean inTimeline = false;
	private UserList userlist = null;
	private UserStore store = UserStore.getInstance();
	public static final String SESSION_PROPERTY = "sessionName";
	private String sessionName;
	private TwitzMainView view;
}
