/*
 * TwitzApp.java
 */

package twitz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import twitz.util.SettingsManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class TwitzApp extends SingleFrameApplication implements ActionListener, MouseListener {

	public static final String UPDATE = "Update";
	public static final String TWEET = "Tweet";
	public static final String TWEET_MINI = "TweetMini";

	private java.awt.Window window = null;
	SettingsManager config = SettingsManager.getInstance();
	Logger logger = Logger.getLogger(TwitzApp.class.getName());
	TwitzTrayIcon tray = null;
	private TwitzView view;
	private boolean hidden = false;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
		view = new TwitzView(this);
		tray = new TwitzTrayIcon(this, view);
        //show(new TwitzView(this, tray, config));
		show(view);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
		window = root;
		window.addWindowListener(new WindowListener() {

			public void windowIconified(WindowEvent e)
			{
				logger.log(Level.INFO, "Window Iconified");
				//tray.toggleWindowView("down");
				hide(view);
				hidden = true;
			}

			public void windowOpened(WindowEvent e)
			{
				if(config.getBoolean("minimize.startup"))
				{
					//e.getWindow().setVisible(false);
					hide(view);
					hidden = true;
//					if(window != null)
//						window.setVisible(false);
//					else
//						logger.log(Level.WARNING, "Window is null");
				}
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void windowClosing(WindowEvent e)
			{
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void windowClosed(WindowEvent e)
			{
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void windowDeiconified(WindowEvent e)
			{
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void windowActivated(WindowEvent e)
			{
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void windowDeactivated(WindowEvent e)
			{
				//throw new UnsupportedOperationException("Not supported yet.");
			}
		});
    }

	public java.awt.Window getMainWindow() {
		return window;
	}

    /**
     * A convenient static getter for the application instance.
     * @return the instance of TwitzApp
     */
    public static TwitzApp getApplication() {
        return Application.getInstance(TwitzApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(TwitzApp.class, args);
    }

	public void toggleWindowView(String action) {
		java.awt.Window win = getMainWindow();
		//Window win = window;
		if(action != null)
		{
			if(action.equalsIgnoreCase("front"))
			{
				win.toFront();
			}
			else if(action.equalsIgnoreCase("back"))
			{
				win.toBack();
			}
			else if(action.equalsIgnoreCase("up"))
			{
				show(view);
			}
			else if(action.equalsIgnoreCase("down"))
			{
				hide(view);
			}
			else if(action.equalsIgnoreCase("toggle"))
			{

				if (!hidden)
				{
					hide(view);
					hidden = true;
					//win.setVisible(false);
				}
				else if (!hidden && !win.isActive())
				{
					win.toFront();
				}
				else
				{
					show(view);
					hidden = false;
					getMainFrame().setState(java.awt.Frame.NORMAL);
//					win.setVisible(true);
//					getMainFrame().setState(java.awt.Frame.NORMAL);
//					win.toFront();
				}
			}
		}
		else
		{
			if (!hidden && win.isActive())
			{
				hide(view);
				hidden = true;
				//win.setVisible(false);
			}
			else if (!hidden && !win.isActive())
			{
				win.toFront();
			}
			else
			{
				show(view);
				hidden = false;
				//win.setVisible(true);
				getMainFrame().setState(java.awt.Frame.NORMAL);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.endsWith(TWEET_MINI)) {
			view.showMiniTweet();
		}
		else if(cmd.equals("Exit")) {
			exit(e);
		}
		else if(cmd.equals("About")) {
			view.showAboutBox();
		}
		else if(cmd.equals("PrefsDlg")) {
			//prefs = new PreferencesDialog(null, true/*, config*/);
			//prefs.setVisible(true);
			view.showPrefsBox();
		}
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void mouseClicked(MouseEvent e)
	{
		this.toggleWindowView("toggle");
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void mousePressed(MouseEvent e)
	{
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void mouseReleased(MouseEvent e)
	{
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void mouseEntered(MouseEvent e)
	{
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void mouseExited(MouseEvent e)
	{
		//throw new UnsupportedOperationException("Not supported yet.");
	}
}
