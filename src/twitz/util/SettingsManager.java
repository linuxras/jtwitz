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
public class SettingsManager {

	private Properties settings;
	String configFile = System.getenv("HOME")+"/.twitz/user_prefs.ini";
	Logger logger = Logger.getLogger(SettingsManager.class.getName());
	private static SettingsManager instance;

	private SettingsManager() {
		settings = new Properties();
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
		settings = new Properties();
		loadSettings();
	}

	public static synchronized SettingsManager getInstance() {
		if(instance == null)
			instance = new SettingsManager();
		return instance;
	}

	private void setDefaults() {
		settings.setProperty("twitter.id", "changeme");
		settings.setProperty("twitter.id.cfgdesc", "Twitter ID");
		settings.setProperty("twitter.id.cfgtype", "String");
		settings.setProperty("twitter.password", "changeme");
		settings.setProperty("twitter.password.cfgdesc", "Twitter Password");
		settings.setProperty("twitter.password.cfgtype", "Password");
		settings.setProperty("minimize.startup", "true");
		settings.setProperty("minimize.startup.cfgdesc", "Minimize on Startup");
		settings.setProperty("minimize.startup.cfgtype", "Boolean");
		settings.setProperty("twitter.picture", "");
		settings.setProperty("twitter.picture.cfgdesc", "Twitter Profile Image");
		settings.setProperty("twitter.picture.cfgtype", "File");
		//This is a null entry AKA an internally managed property
		//This keeps the mini/full state of the program between restarts
		settings.setProperty("minimode", "true");
		settings.setProperty("minimode.cfgtype", "NULL");
		//Friends Tab settings
		settings.setProperty("tab.friends", "true");
		settings.setProperty("tab.friends.cfgdesc", "Enable Friends Tab");
		settings.setProperty("tab.friends.cfgtype", "Boolean");
		//Blocked Tab settings
		settings.setProperty("tab.blocked", "true");
		settings.setProperty("tab.blocked.cfgdesc", "Enable Blocked Tab");
		settings.setProperty("tab.blocked.cfgtype", "Boolean");
		//Following tab settings
		settings.setProperty("tab.following", "true");
		settings.setProperty("tab.following.cfgdesc", "Enable Following Tab");
		settings.setProperty("tab.following.cfgtype", "Boolean");
		//Followers tab settings
		settings.setProperty("tab.followers", "true");
		settings.setProperty("tab.followers.cfgdesc", "Enable Followers Tab");
		settings.setProperty("tab.followers.cfgtype", "Boolean");
		saveSettings();
	}

	public Enumeration<Object> getKeys() {
		return settings.keys();
	}

	private boolean loadSettings() {
		try
		{
			settings.load(new FileInputStream(configFile));
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
			settings.store(new FileOutputStream(configFile), "DO NOT EDIT MANUALLY");
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

	public String getProperty(String param) {
		return settings.getProperty(param);
	}
	public String getString(String param) {
		return settings.getProperty(param);
	}
	public String getString(String param, String defaultParam) {
		return settings.getProperty(param, defaultParam);
	}
	public String[] getStringArray(String param, String separator) {
		return settings.getProperty(param).split(separator);
	}
	public int getInteger(String param) {
		logger.log(Level.CONFIG, param);
		logger.log(Level.CONFIG, settings.getProperty(param));
		return Integer.parseInt(settings.getProperty(param));
	}
	public int getInteger(String param, int defaultParam) {
		return Integer.parseInt(settings.getProperty(param,defaultParam+""));
	}
	public long getLong(String param) {
		return Long.parseLong(settings.getProperty(param));
	}
	public long getLong(String param, long defaultParam) {
		return Long.parseLong(settings.getProperty(param, defaultParam+""));
	}
	public boolean getBoolean(String param) {
		return Boolean.parseBoolean(settings.getProperty(param));
	}
	public boolean getBoolean(String param, boolean defaultParam) {
		return Boolean.parseBoolean(settings.getProperty(param, defaultParam+""));
	}


	public void setProperty(String property, String value) {
		settings.setProperty(property, value);
		saveSettings();
	}
	public void setProperty(Properties list) {
		Enumeration e = list.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String)e.nextElement();
			settings.setProperty(key, list.getProperty(key));
		}
		saveSettings();
	}
}
