/*
 * TwitzApp.java
 */

package twitz;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import twitz.util.SettingsManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
	private boolean hidden = config.getBoolean("minimize.startup");
	Image splash = getIcon("resources/splash.png");
	JFrame splashFrame = new JFrame();

	private void buildSplash() {
//		Point center = getDesktopCenter();
		GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
		javax.swing.ImageIcon icon = new javax.swing.ImageIcon(splash);
		splashFrame.setAlwaysOnTop(true);
		splashFrame.setUndecorated(true);
		splashFrame.setLayout(gridbag);
		splashFrame.setSize(icon.getIconWidth(), icon.getIconHeight());
		javax.swing.JLabel img = new javax.swing.JLabel();
//		Rectangle bound = new Rectangle();
//		bound.setSize(icon.getIconWidth(), icon.getIconHeight());
//		bound.setLocation((center.x - (icon.getIconWidth() / 2)), (center.y - (icon.getIconHeight() / 2)));
		splashFrame.setBounds(getDesktopCenter(splashFrame));
		img.setIcon(icon);
		splashFrame.add(img);
		//return splashFrame;
	}

	private Point getDesktopCenter() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
	}

	public static Rectangle getDesktopCenter(java.awt.Component comp) {
		Rectangle rv = new Rectangle();
		Point c = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int w = comp.getWidth();
		int h = comp.getHeight();
		rv.setSize(w, h);
		rv.setLocation((c.x - (w / 2)), (c.y - (h / 2)));
		return rv;
	}

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
		//buildSplash();
		//splashFrame.setVisible(true);
		Point c = getDesktopCenter();
		Rectangle bound = new Rectangle();
		bound.setSize(420, 300);
		bound.setLocation((c.x - 210), (c.y - 150));
		view = new TwitzView(this);
		view.getFrame().setBounds(bound);
		view.getFrame().setUndecorated(config.getBoolean("twitz.undecorated"));
		try
		{
			tray = new TwitzTrayIcon(this, view);
		}
		catch (Exception ex)
		{
			Logger.getLogger(TwitzApp.class.getName()).log(Level.SEVERE, null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Critical Error", JOptionPane.ERROR_MESSAGE);
		}
		view.addPropertyChangeListener("POPUP", tray);
		System.out.println("Inside Startup");
        //show(new TwitzView(this, tray, config));
		show(view);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
		System.out.println("Inside configureWindow");
		window = root;
		window.setIconImage(getIcon("resources/clock.png"));
		window.addWindowListener(new WindowListener() {

			public void windowIconified(WindowEvent e)
			{
				logger.log(Level.INFO, "Window Iconified");
				toggleWindowView("down");
			}

			public void windowOpened(WindowEvent e)
			{
//				if(config.getBoolean("minimize.startup"))
//				{
//					toggleWindowView("down");
//				}
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

	@Override
	protected void ready() {
		System.out.println("Inside ready()");
		if(hidden)
			toggleWindowView("down");
		//splashFrame.setVisible(false);
		GraphicsEnvironment gc = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] dv = gc.getScreenDevices();
		for(GraphicsDevice d : dv) {
			System.out.println(d.getIDstring());
			GraphicsConfiguration g = d.getDefaultConfiguration();
			System.out.println(g.toString());
		}
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

	public Image getIcon(String path) {

        Image icon = null;

        //ClassLoader loader = TwitzApp.class.getClassLoader();
        //InputStream is = loader.getResourceAsStream(path);
		InputStream is = this.getClass().getResourceAsStream(path);
		try
		{
			icon = ImageIO.read(is);
			is.close();
		}
		catch(IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		}

        return (icon);
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
				hidden = false;
			}
			else if(action.equalsIgnoreCase("down"))
			{
				hide(view);
				hidden = true;
			}
			else if(action.equalsIgnoreCase("toggle"))
			{

				if (!hidden && !win.isActive())
				{
					//hide(view);
					win.toFront();
					//win.setVisible(false);
				}
				else if (!hidden)
				{
					hide(view);
					getMainFrame().setState(java.awt.Frame.NORMAL);
					hidden = true;
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
		tray.hideGlassPane();
		if(cmd.endsWith(TWEET_MINI)) {
			//view.showMiniTweet();
			view.miniTwitz();
			toggleWindowView("up");
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
