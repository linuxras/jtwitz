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
import java.lang.String;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mistik1
 */
public class SettingsManager extends Properties{

	String configFile = System.getenv("HOME")+"/.twitz/user_prefs.ini";
	Logger logger = Logger.getLogger(SettingsManager.class.getName());
	private static SettingsManager instance;

	private SettingsManager() {
		if(!loadSettings())
		{
			setDefaults();
		}
	}

	/**
	 * Use this Constructor to build a general use SettingsManager that can be used to
	 * manage any java .properties file
	 * @param cfg - config file to manage
	 */
	public SettingsManager(String cfg) {
		this.configFile = cfg;
		loadSettings();
	}

	/**
	 * Default way to get a SettingsManager instance
	 * @return SettingsManager singleton
	 */
	public static synchronized SettingsManager getInstance() {
		if(instance == null)
			instance = new SettingsManager();
		return instance;
	}

	private void setDefaults() {
		setProperty("twitter.id", "changeme");
		setProperty("twitter.id.cfgdesc", "Twitter ID");
		setProperty("twitter.id.cfgtype", "String");
		setProperty("twitter.password", "changeme");
		setProperty("twitter.password.cfgdesc", "Twitter Password");
		setProperty("twitter.password.cfgtype", "Password");
		setProperty("minimize.startup", "false");
		setProperty("minimize.startup.cfgdesc", "Minimize on Startup");
		setProperty("minimize.startup.cfgtype", "Boolean");
		setProperty("twitter.picture", "");
		setProperty("twitter.picture.cfgdesc", "Twitter Profile Image");
		setProperty("twitter.picture.cfgtype", "File");
		setProperty("twitz.undecorated", "false");
		setProperty("twitz.undecorated.cfgdesc", "Start Undecorated");
		setProperty("twitz.undecorated.cfgtype", "Boolean");
		//This is a null entry AKA an internally managed property
		//This keeps the mini/full state of the program between restarts
		setProperty("minimode", "true");
		setProperty("minimode.cfgtype", "NULL");
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
		setProperty("tab.search", "false");
		setProperty("tab.search.cfgdesc", "Enable Search Tab");
		setProperty("tab.search.cfgtype", "Boolean");
		saveSettings();
	}

	public Enumeration<Object> getKeys() {
		return keys();
	}

	private boolean loadSettings() {
		try
		{
			load(new FileInputStream(configFile));
		}
		catch (IOException ex)
		{
			logger.log(Level.SEVERE, ex.getMessage());
			return false;
		}
		return true;
	}

	private void saveSettings() {
		try
		{
			store(new FileOutputStream(configFile), "DO NOT EDIT MANUALLY");
		}
		catch (FileNotFoundException ex)
		{
			//Logger.getLogger(JAlarmView.class.getName()).log(Level.SEVERE, null, ex);
			if(createSettingsFile(configFile))
				saveSettings();
		}
		catch (IOException ex)
		{
			logger.log(Level.SEVERE, ex.getMessage());
		}
	}

	private void checkDirs() {
		File f = new File(configFile);
		String d = f.getParent();
		f = new File(d);
		if(!f.exists())
			f.mkdir();
	}

	private boolean createSettingsFile(String file) {
		checkDirs();
		boolean rc = false;
		try
		{
			File settingsFile = new File(file);
			rc = settingsFile.createNewFile();
		}
		catch(IOException e)
		{
			logger.log(Level.SEVERE, e.getMessage());
			rc = false;
		}
		return rc;
	}

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
	public String getString(String param, Object... args) {
		if(args.length == 0) {
			return getProperty(param);
		}
		else {
			String str = getProperty(param);
			return (str == null) ? null : String.format(str, args);
		}
	}

	/**
	 *
	 * @param param The key to search the configuration for.
	 * @param defaultParam If <tt>param</tt> is null this returned.
	 * @param args Arguments to be passed to the formatter.
	 * @return the String value of the resource named <tt>param</tt> or <tt>defaultParam</tt>
	 * @see #getString(java.lang.String, java.lang.Object...)
	 * param is not available.
	 */
	public String getString(String param, String defaultParam, Object... args) {
		if(args.length == 0) {
			return getProperty(param, defaultParam);
		}
		else {
			String str = getProperty(param, defaultParam);
			return (str == null) ? null : String.format(str, args);
		}
	}
	public String[] getStringArray(String param, String separator) {
		return getProperty(param).split(separator);
	}
	public int getInteger(String param) {
		logger.log(Level.CONFIG, param);
		logger.log(Level.CONFIG, getProperty(param));
		try
		{
			return Integer.parseInt(getProperty(param));
		}
		catch(NumberFormatException ex) {
			return -1;
		}
	}
	public int getInteger(String param, int defaultParam) {
		try
		{
			return Integer.parseInt(getProperty(param));
		}
		catch (NumberFormatException ex)
		{
			return defaultParam;
		}
	}
	public long getLong(String param) {
		try
		{
			return Long.parseLong(getProperty(param));
		}
		catch(NumberFormatException e)
		{
			return -1;
		}
	}
	public long getLong(String param, long defaultParam) {
		try
		{
			return Long.parseLong(getProperty(param));
		}
		catch(NumberFormatException e)
		{
			return defaultParam;
		}
	}

	public boolean getBoolean(String param) {
		return Boolean.parseBoolean(getProperty(param));
	}

	public boolean getBoolean(String param, boolean defaultParam) {
		if(getProperty(param) != null && !getProperty(param).equals(""))
			return Boolean.parseBoolean(getProperty(param));
		else
			return defaultParam;
	}


	@Override
	public Object setProperty(String property, String value) {
		Object rv = super.setProperty(property, value);
		saveSettings();
		return rv;
	}
	public void setProperties(Properties list) {
		Enumeration e = list.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String)e.nextElement();
			setProperty(key, list.getProperty(key));
		}
		saveSettings();
	}
}
