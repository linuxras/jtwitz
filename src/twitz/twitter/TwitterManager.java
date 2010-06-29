/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.twitter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdesktop.application.ResourceMap;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationFactory;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventType;
import twitz.util.SettingsManager;

/**
 *
 * @author mistik1
 */
public class TwitterManager extends DefaultTwitzEventModel/*implements TwitterListener*/ {

	SettingsManager config = SettingsManager.getInstance();
	private Twitter twitter;// = new TwitterFactory().getInstance();
	private static twitz.TwitzMainView view;
	private AsyncTwitter atwitter;
	private Logger logger = Logger.getLogger(TwitterManager.class.getName());
	private static TwitterManager instance;
	private ResourceMap resource;
	private ConfigurationBuilder builder;
	private Configuration baseConfig;

	@SuppressWarnings("static-access")
	private TwitterManager() {
		this.view = twitz.TwitzMainView.getInstance();
		resource = twitz.TwitzApp.getContext().getResourceMap(twitz.twitter.TwitterManager.class);
		baseConfig = buildConfiguration();
		this.twitter = new TwitterFactory(baseConfig).getInstance(config.getString("twitter.id"), config.getString("twitter.password"));
		this.atwitter = new AsyncTwitterFactory(baseConfig, view).getInstance(config.getString("twitter.id"),config.getString("twitter.password"));
	}

	private Configuration buildConfiguration() {
		builder = new ConfigurationBuilder();
		builder.setUserAgent(resource.getString("USER_AGENT.TEXT"));
		builder.setSource(resource.getString("SOURCE.TEXT"));
		builder.setClientURL(resource.getString("CLIENT.URL"));
		builder.setClientVersion(resource.getString("CLIENT.VERSION"));
		builder.setUseSSL(true);
		builder.setAsyncNumThreads(resource.getInteger("CLIENT.MAX.THREADS"));
		if(config.getBoolean("twitter.use_proxy")) {
			builder.setHttpProxyPort(config.getInteger("twitter.proxy_port"));
			builder.setHttpProxyHost(config.getString("twitter.proxy_host"));
			if(config.getString("twitter.proxy_password") != null && !config.getString("twitter.proxy_password").equals(""))
				builder.setHttpProxyPassword(config.getString("twitter.proxy_password"));
			if(config.getString("twitter.proxy_user") != null && !config.getString("twitter.proxy_user").equals(""))
				builder.setHttpProxyUser(config.getString("twitter.proxy_user"));
		}
		return builder.build();
	}
	
	/**
	 * Get a TwitterManager instance
	 * @param v The TwitzMainView using this manager
	 * @return The Singleton TwitterManager Object
	 */
	public synchronized static TwitterManager getInstance() {
		if(instance == null)
			instance = new TwitterManager();
		return instance;
	}

	/**
	 * This is a convenience method. You must NOT call this before the application has be
	 * fully initialized
	 * @return A TwitterManager singleton
	 */
//	public synchronized static TwitterManager getInstance() {
//		if(instance == null || view == null)
//			throw new IllegalStateException("Application has not been properly initialized");
//		return instance;
//	}

	public void setPassword(String pass) {
		config.setProperty("twitter.password", pass);
		//twitter = new TwitterFactory().getInstance(config.getString("twitter.id"), pass);
	}

	public void setUserId(int id) {
		config.setProperty("twitter.id", id+"");
		//twitter = new TwitterFactory().getInstance(config.getString("twitter.id"),config.getString("twitter.password"));
	}

	public void sendDirectMessage(String rec, String msg) {
		try {
			DirectMessage message = twitter.sendDirectMessage(rec, msg);
		}
		catch(TwitterException te) {
			logger.log(Level.SEVERE, te.getMessage());
			JOptionPane.showMessageDialog(null, te.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
		}
	}

	public List<DirectMessage> getDirectMessages() {
		List<DirectMessage> messages = null;
		try
		{
			messages = twitter.getDirectMessages();
		}
		catch (TwitterException ex)
		{
			logger.log(Level.SEVERE, ex.getMessage());
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
		}
		return messages;
	}

	public void sendTweet(String msg) throws TwitterException, IllegalStateException {
		Status stat = null;
//		try
//		{
			stat = twitter.updateStatus(msg);
//		}
//		catch (Exception ex)
//		{
//			logger.log(Level.SEVERE, ex.getMessage());
//			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
//		}
		if(stat != null) {
			//TODO Update pertinent list with this new data. I would like to fire an event here
			this.fireTwitzEvent(new TwitzEvent(stat, TwitzEventType.UPDATE_STATUS, new java.util.Date().getTime()));
		}
	}

	public void login() {
		baseConfig = buildConfiguration();
		twitter = new TwitterFactory(baseConfig).getInstance(config.getString("twitter.id"),config.getString("twitter.password"));
		atwitter = new AsyncTwitterFactory(baseConfig, view).getInstance(config.getString("twitter.id"),config.getString("twitter.password"));
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

}
