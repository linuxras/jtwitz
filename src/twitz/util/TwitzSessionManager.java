/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import org.apache.log4j.Logger;
import twitz.TwitzApp;
import twitz.TwitzMainView;

/**
 *
 * @author mistik1
 */
public class TwitzSessionManager implements java.io.Serializable
{
	public static final String LOADED_PROPERTY = "sessionsLoaded";
	public static final String ADDED_PROPERTY = "sessionsAdded";
	static Logger logger;
	private static TwitzSessionManager instance;
	private static DBManager DBM;
	private String currentSession = "Default";
	private boolean autosave = true;
	private Vector<TwitzMainView> autoLoad = new Vector<TwitzMainView>();
	protected Map<String, SettingsManager> sessions = Collections.synchronizedMap(new TreeMap<String, SettingsManager>());
	private Map<String, TwitzMainView> views = Collections.synchronizedMap(new TreeMap<String, TwitzMainView>());
	private final TwitzApp mainApp;
	private TwitzMainView defaultSession;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);


	public static enum OS {
		WINDOWS,
		OSX,
		UNIX
	};

	private TwitzSessionManager(TwitzApp app) {//{{{
		//super(this);
		this.mainApp = app;
		DBM = DBManager.getInstance();
		logger  = Logger.getLogger(TwitzSessionManager.class.getName());
		//loadSettingsFromDb();
	}//}}}

	/**
	 * Default way to get a SettingsManager instance
	 * @return SettingsManager singleton
	 */
	public static synchronized TwitzSessionManager getInstance(TwitzApp app) {//{{{
		if(instance == null)
			instance = new TwitzSessionManager(app);
		return instance;
	}//}}}

	public static synchronized TwitzSessionManager getInstance()
	{
		if(instance == null)
			throw new IllegalStateException("Application has not be propery initialized");
		return instance;
	}

	public synchronized SettingsManager getSettingsManagerForSession(String sessionName)
	{
		SettingsManager rv = null;
		if(sessions.containsKey(sessionName))
			return sessions.get(sessionName);
		rv = new SettingsManager(sessionName);
		sessions.put(sessionName, rv);
		return rv;
	}

	public synchronized TwitzMainView getTwitMainViewForSession(String sessionName)
	{
		TwitzMainView rv = null;
		if(views.containsKey(sessionName))
			return views.get(sessionName);
		logger.debug("creating new view for session: "+sessionName+" =========================");
		rv = new TwitzMainView(mainApp, sessionName);
		firePropertyChange(ADDED_PROPERTY, new Object(), rv);
		views.put(sessionName, rv);
		return rv;
	}

	public synchronized void addTwitzMainView(String sessionName, TwitzMainView v)
	{
		//if(!views.containsKey(sessionName))
		//{
			views.put(sessionName, v);
			//sessions.put(sessionName, null);
		//}
	}

//	public void addNewSession(String sname)
//	{
//		boolean hasview = views.containsKey(sname);
//		logger.debug("Checking if we should add: "+sname+" = "+hasview);
//		logger.debug(views.keySet());
//		if(!hasview)
//		{
//			logger.debug(sname+" Added");
//			TwitzMainView v = getTwitMainViewForSession(sname);
//			//firePropertyChange(ADDED_PROPERTY, new Object(), v);
//		}
//	}

	public void loadAvailableSessions()
	{
		Vector<Map<String, Object>> sess = null;
		try
		{
			sess = DBM.lookupSessions();
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage());
		}
		if(sess != null)
		{
			for(int i=0, max = sess.size(); i<max; i++)
			{
				Map<String, Object> map = sess.get(i);
				String name = (String) map.get(DBManager.SESSION_NAME);
				boolean def = (Boolean)map.get(DBManager.SESSION_DEFAULT);
				boolean autoload = (Boolean)map.get(DBManager.SESSION_AUTOLOAD);
				TwitzMainView view = new TwitzMainView(mainApp, name);
				view.setTitle(name);
				view.setMaximizable(true);
				view.setName(name.replaceAll(" ", "_").replaceAll("\\W", ""));
				//System.out.println(view.getName()+" views size(): "+views.size());
				if(name.equals("Default"))
				{
					defaultSession = view;
					view.setClosable(false);
				}
				else
				{
					if(def)
						autoLoad.add(view);
				}
				//addTwitzMainView(name, view);
			}
		}
		firePropertyChange(LOADED_PROPERTY, null, views);
	}

	public TwitzMainView getDefaultSession()
	{
		return this.defaultSession;
	}

	public Vector<TwitzMainView> getAutoLoadingSessions()
	{
		return autoLoad;
	}

	public Map<String, TwitzMainView> getSessions()
	{
		return views;
	}

	private void setDefaults() {//{{{
//		setProperty("twitter.id", "changeme");
//		setProperty("twitter.id.cfgdesc", "Twitter ID");
//		setProperty("twitter.id.cfgtype", "String");
//		setProperty("twitter.password", "changeme");
//		setProperty("twitter.password.cfgdesc", "Twitter Password");
//		setProperty("twitter.password.cfgtype", "Password");
//		setProperty("twitter.picture", "");
//		setProperty("twitter.picture.cfgdesc", "Twitter Profile Image");
//		setProperty("twitter.picture.cfgtype", "File");
//		//Proxy settings
//		setProperty("twitter.use_proxy", "false");
//		setProperty("twitter.use_proxy.cfgdesc", "Twitter Proxy?");
//		setProperty("twitter.use_proxy.cfgtype", "Boolean");
//		//Port (Must have a value if use_proxy is enalbed)
//		setProperty("twitter.proxy_port", "");
//		setProperty("twitter.proxy_port.cfgdesc", "Twitter Proxy Port");
//		setProperty("twitter.proxy_port.cfgtype", "String");
//		//Host (Must have a value if use_proxy is enalbed)
//		setProperty("twitter.proxy_host", "");
//		setProperty("twitter.proxy_host.cfgdesc", "Twitter Proxy Host");
//		setProperty("twitter.proxy_host.cfgtype", "Password");
//		//User (Can be blank)
//		setProperty("twitter.proxy_user", "");
//		setProperty("twitter.proxy_user.cfgdesc", "Twitter Proxy User");
//		setProperty("twitter.proxy_user.cfgtype", "String");
//		//Password (Can be blank)
//		setProperty("twitter.proxy_password", "");
//		setProperty("twitter.proxy_password.cfgdesc", "Twitter Proxy Password");
//		setProperty("twitter.proxy_password.cfgtype", "Password");
//		//Minimize settings
//		setProperty("minimize.startup", "false");
//		setProperty("minimize.startup.cfgdesc", "Minimize on Startup");
//		setProperty("minimize.startup.cfgtype", "Boolean");
//		//Decoration settings
//		setProperty("twitz.undecorated", "false");
//		setProperty("twitz.undecorated.cfgdesc", "Start Undecorated");
//		setProperty("twitz.undecorated.cfgtype", "Boolean");
//		setProperty("twitz.skin", "MistAqua");
//		setProperty("twitz.skin.cfgdesc", "Twitz Theme");
//		setProperty("twitz.skin.cfgtype", "Theme");
//		//This is a null entry AKA an internally managed property
//		//This keeps the mini/full state of the program between restarts
//		setProperty("minimode", "false");
//		setProperty("minimode.cfgtype", "NULL");
//		//Internal Size settings
//		setProperty("twitz.last.height", "640");
//		setProperty("twitz.last.height.cfgtype", "NULL");
//		//Tab positions
//		setProperty("tab.position", "north");
//		setProperty("tab.position.cfgtype", "NULL");
//		//Friends Tab settings
//		setProperty("tab.friends", "true");
//		setProperty("tab.friends.cfgdesc", "Enable Friends Tab");
//		setProperty("tab.friends.cfgtype", "Boolean");
//		//Blocked Tab settings
//		setProperty("tab.blocked", "false");
//		setProperty("tab.blocked.cfgdesc", "Enable Blocked Tab");
//		setProperty("tab.blocked.cfgtype", "NULL");//"Boolean");
//		//Following tab settings
//		setProperty("tab.following", "false");
//		setProperty("tab.following.cfgdesc", "Enable Following Tab");
//		setProperty("tab.following.cfgtype", "NULL");//"Boolean");
//		//Followers tab settings
//		setProperty("tab.followers", "false");
//		setProperty("tab.followers.cfgdesc", "Enable Followers Tab");
//		setProperty("tab.followers.cfgtype", "NULL");//"Boolean");
//		//Search tab settings
//		setProperty("tab.search", "true");
//		setProperty("tab.search.cfgdesc", "Enable Search Tab");
//		setProperty("tab.search.cfgtype", "Boolean");
//		saveSettings();
	}//}}}

	public void setCurrentSession(String session)
	{
		this.currentSession = session;
	}

	public String getCurrentSession()
	{
		return this.currentSession;
	}

	public void firePropertyChange(String name, Object old, Object newVal)
	{
		pcs.firePropertyChange(name, old, newVal);
	}

	public void firePropertyChange(PropertyChangeEvent e)
	{
		pcs.firePropertyChange(e);
	}

	public void firePropertyChange(String name, boolean oldVal, boolean newVal)
	{
		pcs.firePropertyChange(name, oldVal, newVal);
	}

	public void firePropertyChange(String name, int oldVal, int newVal)
	{
		pcs.firePropertyChange(name, oldVal, newVal);
	}

	public void addPropertyChangeListener(PropertyChangeListener l)
	{
		pcs.addPropertyChangeListener(l);
	}

	public void addPropertyChangeListener(String name, PropertyChangeListener l)
	{
		pcs.addPropertyChangeListener(name, l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		pcs.removePropertyChangeListener(l);
	}

	public void removePropertyChangeListener(String name, PropertyChangeListener l)
	{
		pcs.removePropertyChangeListener(name, l);
	}

	public boolean hasListeners(String name)
	{
		return pcs.hasListeners(name);
	}

	public PropertyChangeListener[] getPropertyChangeListeners()
	{
		return pcs.getPropertyChangeListeners();
	}

	public PropertyChangeListener[] getPropertyChangeListeners(String name)
	{
		return pcs.getPropertyChangeListeners(name);
	}

}
