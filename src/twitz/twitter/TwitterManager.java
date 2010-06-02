/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.twitter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.util.SettingsManager;

/**
 *
 * @author mistik1
 */
public class TwitterManager extends DefaultTwitzEventModel {

	SettingsManager config = SettingsManager.getInstance();
	private Twitter twitter = new TwitterFactory().getInstance();
	private Logger logger = Logger.getLogger(TwitterManager.class.getName());
	private JComponent parent;
	private static TwitterManager instance;

	private TwitterManager(/*JComponent parent, SettingsManager c*/) {
		//config = c;
		//this.parent = parent;
	}

	public synchronized static TwitterManager getInstance() {
		if(instance == null)
			instance = new TwitterManager();
		return instance;
	}

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
			this.fireTwitzEvent(new TwitzEvent(stat, twitter, TwitzEvent.UPDATE, new java.util.Date().getTime()));
		}
	}

	public void login() {
		twitter = new TwitterFactory().getInstance(config.getString("twitter.id"),config.getString("twitter.password"));
	}

	public Twitter getTwitterInstance() {
		return twitter;
	}



}
