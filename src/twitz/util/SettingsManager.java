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
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.ResourceMap;
import twitz.TwitzApp;

/**
 *
 * @author mistik1
 */
public class SettingsManager extends Properties{

	//this needs to be moved to where it can be checked for SecurityException
	//String configFile = System.getProperty("user.home")+"/.Twitz/user_prefs.ini";
	String configFile = "user_prefs.ini";
	File config;
	Logger logger = Logger.getLogger(SettingsManager.class.getName());
	private static SettingsManager instance;

	public static enum OS {
		WINDOWS,
		OSX,
		UNIX
	};

	private SettingsManager() {//{{{
		File f = getConfigDirectory();
		config = new File(f, configFile);
		if(!loadSettings())
		{
			setDefaults();
		}
	}//}}}

	/**
	 * Use this Constructor to build a general use SettingsManager that can be used to
	 * manage any java .properties file
	 * @param cfg - config file to manage
	 */
	public SettingsManager(String cfg) {//{{{
		this.configFile = cfg;
		config = new File(configFile);
		loadSettings();
	}//}}}

	/**
	 * Default way to get a SettingsManager instance
	 * @return SettingsManager singleton
	 */
	public static synchronized SettingsManager getInstance() {//{{{
		if(instance == null)
			instance = new SettingsManager();
		return instance;
	}//}}}

	private void setDefaults() {//{{{
		setProperty("twitter.id", "changeme");
		setProperty("twitter.id.cfgdesc", "Twitter ID");
		setProperty("twitter.id.cfgtype", "String");
		setProperty("twitter.password", "changeme");
		setProperty("twitter.password.cfgdesc", "Twitter Password");
		setProperty("twitter.password.cfgtype", "Password");
		setProperty("twitter.picture", "");
		setProperty("twitter.picture.cfgdesc", "Twitter Profile Image");
		setProperty("twitter.picture.cfgtype", "File");
		//Proxy settings
		setProperty("twitter.use_proxy", "false");
		setProperty("twitter.use_proxy.cfgdesc", "Twitter Proxy?");
		setProperty("twitter.use_proxy.cfgtype", "Boolean");
		//Port (Must have a value if use_proxy is enalbed)
		setProperty("twitter.proxy_port", "");
		setProperty("twitter.proxy_port.cfgdesc", "Twitter Proxy Port");
		setProperty("twitter.proxy_port.cfgtype", "String");
		//Host (Must have a value if use_proxy is enalbed)
		setProperty("twitter.proxy_host", "");
		setProperty("twitter.proxy_host.cfgdesc", "Twitter Proxy Host");
		setProperty("twitter.proxy_host.cfgtype", "Password");
		//User (Can be blank)
		setProperty("twitter.proxy_user", "");
		setProperty("twitter.proxy_user.cfgdesc", "Twitter Proxy User");
		setProperty("twitter.proxy_user.cfgtype", "String");
		//Password (Can be blank)
		setProperty("twitter.proxy_password", "");
		setProperty("twitter.proxy_password.cfgdesc", "Twitter Proxy Password");
		setProperty("twitter.proxy_password.cfgtype", "Password");
		//Minimize settings
		setProperty("minimize.startup", "false");
		setProperty("minimize.startup.cfgdesc", "Minimize on Startup");
		setProperty("minimize.startup.cfgtype", "Boolean");
		//Decoration settings
		setProperty("twitz.undecorated", "false");
		setProperty("twitz.undecorated.cfgdesc", "Start Undecorated");
		setProperty("twitz.undecorated.cfgtype", "Boolean");
		setProperty("twitz.skin", "MistAqua");
		setProperty("twitz.skin.cfgdesc", "Twitz Theme");
		setProperty("twitz.skin.cfgtype", "Theme");
		//This is a null entry AKA an internally managed property
		//This keeps the mini/full state of the program between restarts
		setProperty("minimode", "false");
		setProperty("minimode.cfgtype", "NULL");
		//Internal Size settings
		setProperty("twitz.last.height", "640");
		setProperty("twitz.last.height.cfgtype", "NULL");
		//Tab positions
		setProperty("tab.position", "north");
		setProperty("tab.position.cfgtype", "NULL");
		//Friends Tab settings
		setProperty("tab.friends", "true");
		setProperty("tab.friends.cfgdesc", "Enable Friends Tab");
		setProperty("tab.friends.cfgtype", "Boolean");
		//Blocked Tab settings
		setProperty("tab.blocked", "true");
		setProperty("tab.blocked.cfgdesc", "Enable Blocked Tab");
		setProperty("tab.blocked.cfgtype", "Boolean");
		//Following tab settings
		setProperty("tab.following", "true");
		setProperty("tab.following.cfgdesc", "Enable Following Tab");
		setProperty("tab.following.cfgtype", "Boolean");
		//Followers tab settings
		setProperty("tab.followers", "true");
		setProperty("tab.followers.cfgdesc", "Enable Followers Tab");
		setProperty("tab.followers.cfgtype", "Boolean");
		//Search tab settings
		setProperty("tab.search", "true");
		setProperty("tab.search.cfgdesc", "Enable Search Tab");
		setProperty("tab.search.cfgtype", "Boolean");
		saveSettings();
	}//}}}

	private boolean loadSettings() {//{{{
		try
		{
			loadFromXML(new FileInputStream(config));
		}
		catch (IOException ex)
		{
			logger.log(Level.SEVERE, ex.getMessage());
			return false;
		}
		return true;
	}//}}}

	private boolean saveSettings() {//{{{
		boolean rv = true;
		try
		{
			storeToXML(new FileOutputStream(config), "DO NOT EDIT MANUALLY");
		}
		catch (FileNotFoundException ex)
		{
			//Logger.getLogger(JAlarmView.class.getName()).log(Level.SEVERE, null, ex);
			rv = false;
			if(createSettingsFile())
				saveSettings();
		}
		catch (IOException ex)
		{
			rv = false;
			logger.log(Level.SEVERE, ex.getMessage());
		}
		return rv;
	}//}}}

	private void checkDirs() {//{{{
		String d = config.getParent();
		File f = new File(d);
		if(!f.exists())
			f.mkdirs();
	}//}}}

	private boolean createSettingsFile() {//{{{
		checkDirs();
		boolean rc = false;
		try
		{
			rc = config.createNewFile();
		}
		catch(IOException e)
		{
			logger.log(Level.SEVERE, e.getMessage());
			rc = false;
		}
		return rc;
	}//}}}

	/**
	 * This method uses privilaged  access to calculate the current operating system
	 * @return An Enum representing the OS
	 */
	private static OS getOS() {//{{{
		PrivilegedAction<String> doOSLookup = new PrivilegedAction<String>() {
			public String run() {
				return System.getProperty("user.home");
			}
		};

		//I'n a linux guy i'll default to UNIX
		OS rv = OS.UNIX;
		//Do the lookup
		String os = AccessController.doPrivileged(doOSLookup);
        if (os != null) {
            if (os.toLowerCase().startsWith("mac os x")) {
                rv = OS.OSX;
            } else if (os.contains("Windows")) {
                rv = OS.WINDOWS;
            }
        }
		return rv;
	}//}}}

	/**
	 * This method is borrowed from org.jdesktop.application.LocalStorage
	 * @return
	 */
	public static File getConfigDirectory() {//{{{
		ResourceMap resource = TwitzApp.getContext().getResourceMap();
		String appId = resource.getString("Application.id");
		String vendorId = resource.getString("Application.venderId");
		if(appId == null) {
			appId = TwitzApp.getContext().getApplicationClass().getSimpleName();
		}
		if (vendorId == null)
		{
			vendorId = "UnknownApplicationVendor";
		}

		File directory = null;
		String userHome = null;
		try
		{
			userHome = System.getProperty("user.home");
		}
		catch (SecurityException ignore)
		{
		}
		if (userHome != null)
		{
			OS osId = getOS();
			if (osId == OS.WINDOWS)
			{
				File appDataDir = null;
				try
				{
					String appDataEV = System.getenv("APPDATA");
					if ((appDataEV != null) && (appDataEV.length() > 0))
					{
						appDataDir = new File(appDataEV);
					}
				}
				catch (SecurityException ignore)
				{
				}
				if ((appDataDir != null) && appDataDir.isDirectory())
				{
					// ${APPDATA}\{vendorId}\${applicationId}
					String path = vendorId + "\\" + appId + "\\";
					directory = new File(appDataDir, path);
				}
				else
				{
					// ${userHome}\Application Data\${vendorId}\${applicationId}
					String path = "Application Data\\" + vendorId + "\\" + appId + "\\";
					directory = new File(userHome, path);
				}
			}
			else if (osId == OS.OSX)
			{
				// ${userHome}/Library/Application Support/${applicationId}
				String path = "Library/Application Support/" + appId + "/";
				directory = new File(userHome, path);
			}
			else
			{
				// ${userHome}/.${applicationId}/
				String path = "." + appId + "/";
				directory = new File(userHome, path);
			}
		}
		return directory;
	}//}}}

	public Enumeration<Object> getKeys() {//{{{
		return keys();
	}//}}}

	/**
	 * @param param The key to search the configuration for.
	 * @param args Arguments to be passed to the formatter.
     *
	 * If no arguments are specified, return the String value
     * of the resource named <tt>param</tt>.
     * If arguments are provided, then the type of the resource
     * named <tt>param</tt> is assumed to be
     * {@link String#format(String, Object...) format} string,
     * which is applied to the arguments if it's non null.
     * For example, given the following resources
     * <pre>
     * hello = Hello %s
     * </pre>
     * then the value of <tt>getString("hello", "World")</tt> would
     * be <tt>"Hello World"</tt>.
     *
     * @return the String value of the resource named <tt>param</tt>
     * @see String#format(String, Object...)
     */
	public String getString(String param, Object... args) {//{{{
		if(args.length == 0) {
			return getProperty(param);
		}
		else {
			String str = getProperty(param);
			return (str == null) ? null : String.format(str, args);
		}
	}//}}}

	/**
	 *
	 * @param param The key to search the configuration for.
	 * @param defaultParam If <tt>param</tt> is null this returned.
	 * @param args Arguments to be passed to the formatter.
	 * @return the String value of the resource named <tt>param</tt> or <tt>defaultParam</tt>
	 * @see #getString(java.lang.String, java.lang.Object...)
	 */
	public String getString(String param, String defaultParam, Object... args) {//{{{
		if(args.length == 0) {
			return getProperty(param, defaultParam);
		}
		else {
			String str = getProperty(param, defaultParam);
			return (str == null) ? null : String.format(str, args);
		}
	}//}}}
	public String[] getStringArray(String param, String separator) {//{{{
		return getProperty(param).split(separator);
	}//}}}
	public int getInteger(String param) {//{{{
		logger.log(Level.CONFIG, param);
		logger.log(Level.CONFIG, getProperty(param));
		try
		{
			return Integer.parseInt(getProperty(param));
		}
		catch(NumberFormatException ex) {
			return -1;
		}
	}//}}}
	public int getInteger(String param, int defaultParam) {//{{{
		try
		{
			return Integer.parseInt(getProperty(param));
		}
		catch (NumberFormatException ex)
		{
			return defaultParam;
		}
	}//}}}
	public long getLong(String param) {//{{{
		try
		{
			return Long.parseLong(getProperty(param));
		}
		catch(NumberFormatException e)
		{
			return -1;
		}
	}//}}}
	public long getLong(String param, long defaultParam) {//{{{
		try
		{
			return Long.parseLong(getProperty(param));
		}
		catch(NumberFormatException e)
		{
			return defaultParam;
		}
	}//}}}

	public boolean getBoolean(String param) {//{{{
		return Boolean.parseBoolean(getProperty(param));
	}//}}}

	public boolean getBoolean(String param, boolean defaultParam) {//{{{
		if(getProperty(param) != null && !getProperty(param).equals(""))
			return Boolean.parseBoolean(getProperty(param));
		else
			return defaultParam;
	}//}}}


	@Override
	public Object setProperty(String property, String value) {//{{{
		Object rv = super.setProperty(property, value);
		saveSettings();
		return rv;
	}//}}}
	public void setProperties(Properties list) {//{{{
		Enumeration e = list.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String)e.nextElement();
			setProperty(key, list.getProperty(key));
		}
		saveSettings();
	}//}}}
}
