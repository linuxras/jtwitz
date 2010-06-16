/*
 * TwitzApp.java
 */

package twitz;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import twitz.util.SettingsManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

/**
 * The main class of the application.
 */
public class TwitzApp extends SingleFrameApplication implements ActionListener, MouseListener, PropertyChangeListener{

	public static final String UPDATE = "Update";
	public static final String TWEET = "Tweet";
	public static final String TWEET_MINI = "TweetMini";

	private java.awt.Window window = null;
	private static SettingsManager config = SettingsManager.getInstance();
	Logger logger = Logger.getLogger(TwitzApp.class.getName());
	TwitzTrayIcon tray = null;
	private static TwitzMainView view;
	private boolean hidden = config.getBoolean("minimize.startup");
	private ResourceMap resources = null;
	//Image splash = getIcon("resources/splash.png");
	//JFrame splashFrame = new JFrame();
	private static final LAFUpdater themer = new LAFUpdater();
	private SplashScreen splash; //= SplashScreen.getSplashScreen();
	private Graphics2D gap = null; //splash.createGraphics();

	static void renderSplashFrame(Graphics2D g, String comp) {
        //final String[] comps = {"foo", "bar", "baz"};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120,140,200,40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("Loading "+comp+"...", 120, 150);
    }
	
	private void buildSplash() {
	}

	private Point getDesktopCenter() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
	}

	public static Rectangle getDesktopCenter(java.awt.Component comp) {//{{{
		Rectangle rv = new Rectangle();
		Point c = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int w = comp.getWidth();
		int h = comp.getHeight();
		rv.setSize(w, h);
		rv.setLocation((c.x - (w / 2)), (c.y - (h / 2)));
		return rv;
	}//}}}

	@Override
	protected void configureTopLevel(JFrame top) {//{{{
		if(splash != null) {
			renderSplashFrame(gap, "main components");
			splash.update();
		}

		top.setUndecorated(config.getBoolean("twitz.undecorated"));
		resources = getContext().getResourceMap(TwitzApp.class);
		Point c = getDesktopCenter();
		Rectangle bound = new Rectangle();
		bound.setSize(600, 400);
		bound.setLocation((c.x - 210), (c.y - 150));
		top.setBounds(bound);
		configureWindow(top, "mainView");
		top.setIconImage(resources.getImageIcon("Application.icon").getImage());
		WindowListener wl = new WindowAdapter() {

			@Override
			public void windowIconified(WindowEvent e)
			{
				logger.log(Level.INFO, "Window Iconified");
				toggleWindowView("down");
			}

			@Override
			public void windowClosing(WindowEvent e) {
				exit(e);
			}

		};
		top.addWindowListener(wl);
		top.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		top.setTitle(getContext().getResourceMap().getString("Application.title"));
		top.setVisible(true);
		System.out.println("Inside configureTopLevel....");
	}//}}}

    @Override
	protected  Component createMainComponent() {//{{{
		view = new TwitzMainView(this);
		try
		{
			tray = new TwitzTrayIcon(this, view);
		}
		catch (Exception ex)
		{
			Logger.getLogger(TwitzApp.class.getName()).log(Level.SEVERE, null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Critical Error", JOptionPane.ERROR_MESSAGE);
			exit();
		}
		view.addPropertyChangeListener("POPUP", tray);
		if(splash != null) {
			renderSplashFrame(gap, "main components");
			splash.update();
		}
		System.out.println("Inside createMainComponent");
		return view;
    }//}}}

    /**
     * At startup create and show the main frame of the application.
     */
	@Override
	protected void startup() {//{{{
		getMainTopLevel().setJMenuBar(view.getMenuBar());
		view.init();
		if(hidden)
			toggleWindowView("down");
		UIManager.addPropertyChangeListener(this);
		if(splash != null)
			splash.close();
		System.out.println("Inside Startup");
	}//}}}

	@Override
	protected JMenuBar createJMenuBar() {
		System.out.println("Inside createJMenuBar");
        return null;
    }


	@Override
	protected void initialize(String[] args) {//{{{
		System.out.println("Inside initialize...");
		splash = SplashScreen.getSplashScreen();
		if(splash != null) {
			gap = splash.createGraphics();
			renderSplashFrame(gap, "initialization");
			splash.update();
		} else {
			System.out.println("Splash is null");
		}
		themer.addPropertyChangeListener(this);
		setLAFFromSettings(false, true);
		System.out.println("Leaving initialize...");
	}//}}}

	@Override
	protected void ready() {
		System.out.println("Inside ready()");
		if(hidden)
			toggleWindowView("down");
	}
	
	/**
	 * Set a new look and feel 
	 * @param background Should we run on the EDT or not.
	 * Defaults to true, the only time it should be false is when called 
	 * from the <code>initialize</code> method at startup.
	 */
	public static void setLAFFromSettings(boolean background, boolean... init) {//{{{
		if(init.length > 0 && init[0])
		{
			themer.run();
			return;
		}

		if(background) {
			SwingUtilities.invokeLater(themer);
		} else {
			try
			{
				SwingUtilities.invokeAndWait(themer);
			}
			catch(Exception e) 
			{
				Logger.getLogger(TwitzApp.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}//}}}

	public java.awt.Window getMainWindow() {
		return window;
	}


    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(TwitzApp.class, args);
    }

	public Image getIcon(String path) {//{{{

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
    }//}}}

	public void toggleWindowView(String action) {//{{{
		JFrame win = getMainTopLevel();
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
				win.setVisible(true);
				hidden = false;
			}
			else if(action.equalsIgnoreCase("down"))
			{
				win.setVisible(false);
				hidden = true;
			}
			else if(action.equalsIgnoreCase("toggle"))
			{

				if (!hidden && !win.isActive())
				{
					win.toFront();
				}
				else if (!hidden)
				{
					win.setVisible(false);
					win.setState(java.awt.Frame.NORMAL);
					hidden = true;
				}
				else
				{
					win.setVisible(true);
					hidden = false;
					win.setState(java.awt.Frame.NORMAL);

				}
			}
		}
		else
		{
			if (!hidden && win.isActive())
			{
				hidden = true;
				win.setVisible(false);
			}
			else if (!hidden && !win.isActive())
			{
				win.toFront();
			}
			else
			{
				win.setVisible(true);
				hidden = false;
				win.setState(java.awt.Frame.NORMAL);
			}
		}
	}//}}}

	public static void fixTables() {
		view.fixTables();
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		if(e.getPropertyName().equals("lookAndFeelChange")) {
			setLAFFromSettings(true, true);
		}
		else if(e.getPropertyName().equals("skinChange")) {
			if(view != null)
				fixTables(); //update the view so all the tables have correct sizing
		}
		logger.log(Level.INFO, "PropertyChangeEvent recieved: "+e.getPropertyName());
	}

	public void actionPerformed(ActionEvent e) {//{{{
		String cmd = e.getActionCommand();
		tray.hideGlassPane();
		if(cmd.endsWith(TWEET_MINI)) {
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
			view.showPrefsBox();
		}
		//throw new UnsupportedOperationException("Not supported yet.");
	}//}}}

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

	private static class LAFUpdater implements Runnable {//{{{

		private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

		public void addPropertyChangeListener( PropertyChangeListener listener )
		{
		    this.pcs.addPropertyChangeListener( listener );
		}
		
		public void removePropertyChangeListener( PropertyChangeListener listener )
		{
		    this.pcs.removePropertyChangeListener( listener );
		}


		public void run() {
			String skin = config.getString("twitz.skin");
			//org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel
			String currentSkin = "";
			LookAndFeel laf = UIManager.getLookAndFeel();

			currentSkin = laf.getName().replaceAll(" ", "");

			//if(UIManager.getLookAndFeel() instanceof SubstanceLookAndFeel)
			//	currentSkin = SubstanceLookAndFeel.getCurrentSkin().getDisplayName().replaceAll(" ", "");
			
			System.out.println("Current Skin: "+ currentSkin);
			if (skin != null && !skin.equals("") && !skin.equals(currentSkin))//{{{
			{
				System.out.println("Setting LAF...");
				try
				{
					UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.Substance" + skin + "LookAndFeel");
				}
				catch (UnsupportedLookAndFeelException ex)
				{
					Logger.getLogger(TwitzApp.class.getName()).log(Level.SEVERE, null, ex);
				}
				catch (Exception ex)
				{
					Logger.getLogger(TwitzApp.class.getName()).log(Level.SEVERE, null, ex);
				}
				System.out.println("Updating UI...");
				for (Window win : Window.getWindows())
				{
					SwingUtilities.updateComponentTreeUI(win);
				}
				String newSkin = UIManager.getLookAndFeel().getName().replaceAll(" ", "");
				//Notify all listeners that the skin change is complete
				this.pcs.firePropertyChange("skinChange", currentSkin, newSkin);
				System.out.println("New Skin: "+newSkin);
			}//}}}
		}
	}//}}}
}
