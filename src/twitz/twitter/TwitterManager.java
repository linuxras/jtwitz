/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.twitter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.jdesktop.application.ResourceMap;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationFactory;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventType;
import twitz.util.DBManager;
import twitz.util.SettingsManager;
import twitz.util.TwitzSessionManager;

/**
 *
 * @author Andrew Williams
 */
public class TwitterManager extends DefaultTwitzEventModel {

	SettingsManager config;// = SettingsManager.getInstance();
	private Twitter twitter;
	private Twitter defaultTwitter;
	private static twitz.TwitzMainView view;
	private AsyncTwitter atwitter;
	private Logger logger = Logger.getLogger(TwitterManager.class.getName());
	private static TwitterManager instance;
	private ResourceMap resource;
	private ConfigurationBuilder builder;
	private Configuration baseConfig;
	private String sessionName;
	public static final String SESSION_PROPERTY = "sessionName";
	private PropertyChangeSupport pcs = new EDTPropertyChangeSupport(this);
	private String consumerKey = "";
	private String consumerSecret = ""; //I have to get these from twitter.com

	@SuppressWarnings("static-access")
	public TwitterManager(String session) {
		this.sessionName = session;
		this.view = TwitzSessionManager.getInstance().getTwitzMainViewForSession(sessionName);
		config = TwitzSessionManager.getInstance().getSettingsManagerForSession(sessionName);
		resource = twitz.TwitzApp.getContext().getResourceMap(twitz.twitter.TwitterManager.class);
		this.login();
		baseConfig = buildConfiguration();
		defaultTwitter = new TwitterFactory(baseConfig).getInstance();
//		this.twitter = new TwitterFactory(baseConfig).getInstance(config.getString("twitter_id"), config.getString("twitter_password"));
//		this.atwitter = new AsyncTwitterFactory(baseConfig, view).getInstance(config.getString("twitter_id"),config.getString("twitter_password"));
	}

	private Configuration buildConfiguration() {
		builder = new ConfigurationBuilder();
		builder.setUserAgent(resource.getString("USER_AGENT.TEXT"));
		builder.setSource(resource.getString("SOURCE.TEXT"));
		builder.setClientURL(resource.getString("CLIENT.URL"));
		builder.setClientVersion(resource.getString("CLIENT.VERSION"));
		File dir = twitz.TwitzApp.getConfigDirectory();
		File confile = new File(dir, "consumer.properties");
		//Config file overrides hardcoded consumer key/secret values
		if(confile.exists() && confile.isFile())
		{
			try
			{
				FileReader reader = new FileReader(confile);
				Properties cprop = new Properties();
				cprop.load(reader);
				consumerKey = cprop.getProperty("consumer.key", consumerKey);
				consumerSecret = cprop.getProperty("consumer.secret", consumerSecret);
			}
			catch (FileNotFoundException ex)
			{
				logger.error("Failed to load local consumer file "+ex.getLocalizedMessage());
			}
			catch(IOException ex)
			{
				logger.error("Failed to load local consumer file "+ex.getLocalizedMessage());
			}

		}
		builder.setOAuthConsumerKey(consumerKey);
		builder.setOAuthConsumerSecret(consumerSecret);
		if(!config.getBoolean(DBManager.SESSION_TWITTER_OAUTH))
			builder.setUseSSL(true);
		builder.setAsyncNumThreads(resource.getInteger("CLIENT.MAX.THREADS"));
		if(config.getBoolean("twitter_use_proxy")) {
			builder.setHttpProxyPort(config.getInteger("twitter_proxy_port"));
			builder.setHttpProxyHost(config.getString("twitter_proxy_host"));
			if(config.getString("twitter_proxy_password") != null && !config.getString("twitter_proxy_password").equals(""))
				builder.setHttpProxyPassword(config.getString("twitter_proxy_password"));
			if(config.getString("twitter_proxy_user") != null && !config.getString("twitter_proxy_user").equals(""))
				builder.setHttpProxyUser(config.getString("twitter_proxy_user"));
		}
		return builder.build();
	}
	
	/**
	 * Get a TwitterManager instance
	 * @param v The TwitzMainView using this manager
	 * @return The Singleton TwitterManager Object
	 */
	public synchronized static TwitterManager getInstance(String session) {
		if(instance == null)
			instance = new TwitterManager(session);
		return instance;
	}

	public final void login() {
		baseConfig = buildConfiguration();
		defaultTwitter = new TwitterFactory(baseConfig).getInstance();
		if(config.getBoolean(DBManager.SESSION_TWITTER_OAUTH))
		{
			twitter = new TwitterFactory(baseConfig).getOAuthAuthorizedInstance(config.getAccessToken());
			atwitter = new AsyncTwitterFactory(baseConfig, view).getOAuthAuthorizedInstance(config.getAccessToken());
		}
		else
		{
			twitter = new TwitterFactory(baseConfig).getInstance(config.getString("twitter_id"),config.getString("twitter_password"));
			atwitter = new AsyncTwitterFactory(baseConfig, view).getInstance(config.getString("twitter_id"),config.getString("twitter_password"));
		}
		//view.loadAllPanels();
	}

	public ResourceMap getResourceMap() {
		return resource;
	}
	
	public Twitter getTwitterInstance() {
		return twitter;
	}

	public AsyncTwitter getAsyncTwitterInstance() {
		return atwitter;
	}

	public Twitter getDefaultTwitterInstance()
	{
		return this.defaultTwitter;
	}

	public void setSessionName(String name)
	{
		String old = this.sessionName;
		this.sessionName = name;
		config = TwitzSessionManager.getInstance().getSettingsManagerForSession(sessionName);
		//firePropertyChange(SESSION_PROPERTY, old, name);
	}

	public String getSessionName()
	{
		return this.sessionName;
	}

	public void firePropertyChange(String property, Object oldValue, Object newValue)
	{
		pcs.firePropertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
	}

	public void addPropertyChangeListener(PropertyChangeListener e)
	{
		pcs.addPropertyChangeListener(e);
	}

	public void removePropertyChangeListener(PropertyChangeListener e)
	{
		pcs.removePropertyChangeListener(e);
	}

	private class EDTPropertyChangeSupport extends PropertyChangeSupport
	{
		public EDTPropertyChangeSupport(Object source)
		{
			super(source);
		}

		@Override
		public void firePropertyChange(final PropertyChangeEvent evt)
		{
			if(SwingUtilities.isEventDispatchThread())
			{
				super.firePropertyChange(evt);
			}
			else
			{
				SwingUtilities.invokeLater(new Runnable(){
					public void run()
					{
						firePropertyChange(evt);
					}
				});
			}
		}
	}
}
