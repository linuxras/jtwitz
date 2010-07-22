/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.jdesktop.application.ResourceMap;
import org.tmatesoft.sqljet.core.SqlJetException;
import twitz.TwitzApp;
import twitz.TwitzMainView;

/**
 *
 * @author mistik1
 */
public class TwitzSessionManager {

	//this needs to be moved to where it can be checked for SecurityException
	//String configFile = System.getProperty("user.home")+"/.Twitz/user_prefs.ini";
	String configFile = "user_prefs.ini";
	File config;
	static Logger logger;
	private static TwitzSessionManager instance;
	private static DBManager DBM;
	private String currentSession = "Default";
	private boolean autosave = true;
	private Vector<SettingsManager> vsessions = new Vector<SettingsManager>();
	protected Map<String, SettingsManager> sessions = Collections.synchronizedMap(new TreeMap<String, SettingsManager>());
	private Map<String, TwitzMainView> views = Collections.synchronizedMap(new TreeMap<String, TwitzMainView>());
	private final TwitzApp mainApp;


	public static enum OS {
		WINDOWS,
		OSX,
		UNIX
	};

	private TwitzSessionManager(TwitzApp app) {//{{{
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
		System.out.println("creating new view for session: "+sessionName+" =========================");
		rv = new TwitzMainView(mainApp, sessionName);
		views.put(sessionName, rv);
		return rv;
	}

	public synchronized void addTwitzMainView(String sessionName, TwitzMainView v)
	{
		if(!views.containsKey(sessionName))
		{
			views.put(sessionName, v);
			//sessions.put(sessionName, null);
		}
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
}
