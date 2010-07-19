/*
 * TwitzApp.java
 */

package twitz;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import twitz.util.DBManager;
import twitz.util.SettingsManager;
import twitz.util.TwitzSessionManager;

/**
 * The main class of the application.
 */
public class TwitzApp extends SingleFrameApplication implements ActionListener, PropertyChangeListener{

	public static final String UPDATE = "Update";
	public static final String TWEET = "Tweet";
	public static final String TWEET_MINI = "TweetMini";

	private java.awt.Window window = null;
	private static DBManager DBM;// = DBManager.getInstance();
	private static SettingsManager config;// = SettingsManager.getInstance();
	private static TwitzSessionManager[] sessions;
	static Logger logger;
	static boolean logdebug = false;
	TwitzTrayIcon tray = null;
	private static TwitzMainView view;
	private boolean hidden;// = config.getBoolean("minimize.startup");
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

	public static void fixLocation(Component comp) {//{{{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle frame = comp.getBounds();
		Rectangle desktop = ge.getMaximumWindowBounds();
//		logger.debug("Width of desktop: " + desktop.toString());
//		logger.debug("Width of frame: " + frame.toString());
		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		//Check if we intersect with the Desktop rectangle before we process
		if(!desktop.contains(frame.x, frame.y, frame.width, frame.height)) {
			int x = frame.x;//(desktop.width - frame.width);
			int y = frame.y;//(desktop.height - frame.height) - 32;
			if((frame.x+frame.width) > desktop.width)
				left = true;
			if(desktop.x > frame.x) //Frame top should be less than desktop
				right = true;
			if(desktop.y > frame.y)
				down = true;
			if((frame.y+frame.height) > desktop.height) //Frame bottom should be less than desktop
				up = true;
			if(left) x = (desktop.width - frame.width);
			if(right) x = desktop.x;
			if(down) y = desktop.y;
			if(up) y = (desktop.height - frame.height) - 32;//-32 is to make up for most OS toolbars
			//System.out.println("X: " + x + " Y: " + y);
			comp.setLocation(x, y);
		}
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
				if(logdebug)
					logger.debug("Window Iconified");
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
		
		if(logdebug)
			logger.debug("Inside configureTopLevel....");
	}//}}}

    @Override
	protected  Component createMainComponent() {//{{{
		
		view = TwitzMainView.getInstance(this);
		try
		{
			tray = new TwitzTrayIcon(this, view);
		}
		catch (Exception ex)
		{
			logger.error("Fatal Error: ",ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Critical Error", JOptionPane.ERROR_MESSAGE);
			exit();
		}
		view.addPropertyChangeListener("POPUP", tray);
		if(splash != null) {
			renderSplashFrame(gap, "main components");
			splash.update();
		}
		if(logdebug)
			logger.debug("Inside createMainComponent");
		return view;
    }//}}}

    /**
     * At startup create and show the main frame of the application.
     */
	@Override
	protected void startup() {//{{{
		getMainTopLevel().setJMenuBar(view.getMenuBar());
		ComponentListener sizeListener = new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if(!view.isMiniMode())
					config.setProperty("twitz.last.height", getMainTopLevel().getHeight()+"");
			}
		};
		getMainTopLevel().addComponentListener(sizeListener);

		getMainTopLevel().addWindowFocusListener(new WindowFocusListener() {

			public void windowGainedFocus(WindowEvent e)
			{
				view.getTweetField().requestFocusInWindow();
			}

			public void windowLostFocus(WindowEvent e)
			{
			}
		});
		if(config.getBoolean("minimode")) //We closed in minimode
		{
			view.fullTwitz(); //Go full first so all the layout gets done before we shrink
			//wait for layout
			try
			{
				Thread.sleep(30);
			}catch(Exception ignore){}
			view.miniTwitz(true);
		}
		view.initTwitter();
		if(hidden)
			toggleWindowView("down");
		UIManager.addPropertyChangeListener(this);
		if(splash != null)
			splash.close();
		if(logdebug)
			logger.debug("Inside Startup");
		//logger.debug(getEnv());
	}//}}}

	@Override
	protected JMenuBar createJMenuBar() {
		System.out.println("Inside createJMenuBar");
        return null;
    }


	@Override
	protected void initialize(String[] args) {//{{{
		String s = SettingsManager.getConfigDirectory().getAbsolutePath();
		//
		System.out.println("This is the storage dir: "+s);
		System.setProperty("storage.dir", s);
		logger = Logger.getLogger(TwitzApp.class.getName());
		logdebug = logger.isDebugEnabled();
		if(logdebug)
			logger.debug("Inside initialize...");
		splash = SplashScreen.getSplashScreen();
		if(splash != null) {
			gap = splash.createGraphics();
			renderSplashFrame(gap, "initialization");
			splash.update();
		} else {
			if(logdebug)
				logger.debug("Splash is null");
		}
		config = SettingsManager.getInstance();
		hidden = config.getBoolean("minimize.startup");
		DBM = DBManager.getInstance();
		themer.addPropertyChangeListener(this);
		setLAFFromSettings(false, true);
		if(logdebug)
			logger.debug("Leaving initialize...");
	}//}}}

	private void loadAvailableSessions()
	{
		try
		{
			Vector<Map<String, Object>> sess = DBM.lookupSessions();
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage());
		}
	}

	private final static Logger getLogger() {
		return logger;
	}

	public static void setLAFFromSettings(boolean background)
	{
		setLAFFromSettings(background, false);
	}

	/**
	 * Set a new look and feel 
	 * @param background Should we run on the EDT or not.
	 * Defaults to true, the only time it should be false is when called 
	 * from the <code>initialize</code> method at startup.
	 */
	private static void setLAFFromSettings(boolean background, boolean... init) {//{{{
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
				logger.error(e.getMessage(), e);
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
			logger.error(ex.getMessage(),ex);
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
				//win.setVisible(true);
				win.setState(JFrame.NORMAL);
				hidden = false;
			}
			else if(action.equalsIgnoreCase("down"))
			{
				//win.setVisible(false);
				win.setState(JFrame.ICONIFIED);
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
					//win.setVisible(false);
					win.setState(java.awt.Frame.ICONIFIED);
					hidden = true;
				}
				else
				{
					//win.setVisible(true);
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
				//win.setVisible(false);
				win.setState(java.awt.Frame.ICONIFIED);
			}
			else if (!hidden && !win.isActive())
			{
				win.toFront();
			}
			else
			{
				//win.setVisible(true);
				hidden = false;
				win.setState(java.awt.Frame.NORMAL);
			}
		}
	}//}}}

	public static void fixTables() {
		view.fixTables();
	}
	
	public TwitzTrayIcon getTrayIcon()
	{
		return this.tray;
	}

	public void propertyChange(PropertyChangeEvent e) {//{{{
		if(e.getPropertyName().equals("lookAndFeelChange")) {
			setLAFFromSettings(true);
		}
		else if(e.getPropertyName().equals("skinChange")) {
			if(view != null)
				fixTables(); //update the view so all the tables have correct sizing
		}
		if(logdebug)
			logger.debug("PropertyChangeEvent recieved: "+e.getPropertyName());
	}//}}}

	public void actionPerformed(ActionEvent e) {//{{{
		String cmd = e.getActionCommand();
		if(cmd.endsWith(TWEET_MINI)) {
			toggleWindowView("up");
			view.miniTwitz(false);
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
	}//}}}

	public String getEnv() {
		StringBuffer buf = new StringBuffer();
		java.util.Properties p = null;
		try
		{
			p = System.getProperties();
		}
		catch(Exception e){
			e.printStackTrace();
			return "";
		}
		java.util.Enumeration en = p.propertyNames();

		while(en.hasMoreElements()) {
			String s = (String)en.nextElement();
			String v = p.getProperty(s);
			buf.append(s + " = <"+ v +">");
			buf.append("\n");
		}
		return buf.toString();
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

			currentSkin = laf.getName().replaceAll(" ", "").replace("Substance", "");

			//if(UIManager.getLookAndFeel() instanceof SubstanceLookAndFeel)
			//	currentSkin = SubstanceLookAndFeel.getCurrentSkin().getDisplayName().replaceAll(" ", "");
			
			if(logdebug)
				logger.debug("Current Skin: "+ currentSkin);
			if (skin != null && !skin.equals("") && !skin.equals(currentSkin))//{{{
			{
				if(logdebug)
					logger.debug("Setting LAF...");
				try
				{
					UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.Substance" + skin + "LookAndFeel");
				}
				catch (UnsupportedLookAndFeelException ex)
				{
					logger.error(ex.getMessage(), ex);
				}
				catch (Exception ex)
				{
					logger.error(ex.getMessage(), ex);
				}
				logger.info("Updating UI...");
				for (Window win : Window.getWindows())
				{
					SwingUtilities.updateComponentTreeUI(win);
				}
			//	UIManager.put("ScrollBar.width",3);
				String newSkin = UIManager.getLookAndFeel().getName().replaceAll(" ", "");
				//Notify all listeners that the skin change is complete
				this.pcs.firePropertyChange("skinChange", currentSkin, newSkin);
				if(logdebug)
					logger.debug("New Skin: "+newSkin.replace("Substance", ""));
			}//}}}
		}
	}//}}}
}
