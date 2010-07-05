/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.twitter;

import java.util.List;
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
import twitz.util.SettingsManager;

/**
 *
 * @author mistik1
 */
public class TwitterManager extends DefaultTwitzEventModel {

	SettingsManager config = SettingsManager.getInstance();
	private Twitter twitter;
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

	public void login() {
		baseConfig = buildConfiguration();
		twitter = new TwitterFactory(baseConfig).getInstance(config.getString("twitter.id"),config.getString("twitter.password"));
		atwitter = new AsyncTwitterFactory(baseConfig, view).getInstance(config.getString("twitter.id"),config.getString("twitter.password"));
		view.loadAllPanels();
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
