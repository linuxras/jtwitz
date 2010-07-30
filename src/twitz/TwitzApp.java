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
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.Serializable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
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
import twitz.ui.TwitzDesktopFrame;
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
	private static TwitzDesktopFrame mainFrame;
	private TwitzSessionManager session;
	private boolean hidden;// = config.getBoolean("minimize_startup");
	private ResourceMap resources = null;
	//Image splash = getIcon("resources/splash.png");
	//JFrame mainFrame;
	private static final LAFUpdater themer = new LAFUpdater();
	private SplashScreen splash; //= SplashScreen.getSplashScreen();
	private Graphics2D gap = null; //splash.createGraphics();
	public static enum OS {
		WINDOWS,
		OSX,
		UNIX
	};
	//END variable declarations
	
	//SingleFrameApplication overrides
	@Override
	protected void initialize(String[] args) {//{{{
		String s = getConfigDirectory().getAbsolutePath();
		System.setProperty("storage.dir", s);
		//System.out.println("Storage dir found: "+s);
		logger = Logger.getLogger(TwitzApp.class.getName());
		logdebug = logger.isDebugEnabled();
		if(logdebug)
		{
			logger.debug("Inside initialize...");
			logger.debug("This is the storage dir: "+s);
		}
		UIManager.put("ScrollBar.width",8);
		DBM = DBManager.getInstance();
		session = TwitzSessionManager.getInstance(this);
		splash = SplashScreen.getSplashScreen();
		if(splash != null) {
			try
			{
				gap = splash.createGraphics();
				renderSplashFrame(gap, "initialization");
				splash.update();
			}
			catch(Exception e){logger.warn(e.getLocalizedMessage(), e);}
		} else {
			if(logdebug)
				logger.debug("Splash is null");
		}
		config = TwitzSessionManager.getInstance().getSettingsManagerForSession("Default");
		hidden = config.getBoolean("minimize_startup");
		
		themer.addPropertyChangeListener(this);
		setLAFFromSettings(false, true);
		if(logdebug)
			logger.debug("Leaving initialize...");
	}//}}}

	@Override
	protected JMenuBar createJMenuBar() {
		logger.debug("Inside createJMenuBar");
		if(splash != null) {
			try
			{
				renderSplashFrame(gap, "menu bar");
				splash.update();
			}
			catch(Exception e){
				logger.warn(e.getLocalizedMessage());
			}
		}
        return null;
    }

	@Override
	protected JFrame createTopLevel()
	{
		logger.debug("Inside createTopLevel");
		TwitzDesktopFrame top = TwitzDesktopFrame.getInstance(this);
		//top.setUndecorated(config.getBoolean("twitz_undecorated"));
		configureRootPane(top.getRootPane());
		mainFrame = top;
		configureTopLevel(top);
		return top;
	}

	@Override
	protected  Component createMainComponent() {//{{{
		if(logdebug)
			logger.debug("Inside createMainComponent");

		if(splash != null) {
			try
			{
				renderSplashFrame(gap, "default sessions");
				splash.update();
			}
			catch(Exception e){
				logger.warn(e.getLocalizedMessage());
			}
		}
		session.loadAvailableSessions();
		view = session.getDefaultSession();
		//view = new TwitzMainView(this, "Default");

		if(logdebug)
			logger.debug("Leaving createMainComponent");
		return view;
    }//}}}

	@Override
	protected void configureTopLevel(JFrame top) {//{{{
		if(logdebug)
			logger.debug("Inside configureTopLevel....");
		if(splash != null) {
			try
			{
				renderSplashFrame(gap, "top level window");
				splash.update();
			}
			catch(Exception e)
			{
				logger.warn(e.getLocalizedMessage());
			}
		}

		//top.setUndecorated(config.getBoolean("twitz_undecorated"));
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
		//mainFrame = top;//top.setVisible(true);
		
		if(logdebug)
			logger.debug("Leaving configureTopLevel....");
	}//}}}

    /**
     * At startup create and show the main frame of the application.
     */
	@Override
	protected void startup() {//{{{
		if(logdebug)
			logger.debug("Inside Startup");
		if(splash != null) {
			try
			{
				renderSplashFrame(gap, "final startup");
				splash.update();
			}
			catch(Exception e){
				logger.warn(e.getLocalizedMessage());
			}
		}
		//getMainTopLevel().setJMenuBar(view.getMenuBar());
//		ComponentListener sizeListener = new ComponentAdapter() {
//			@Override
//			public void componentResized(ComponentEvent e) {
//				if(!view.isMiniMode())
//					config.setProperty("twitz_last_height", getMainTopLevel().getHeight()+"");
//			}
//		};
//		getMainTopLevel().addComponentListener(sizeListener);

//		getMainTopLevel().addWindowFocusListener(new WindowFocusListener() {
//
//			public void windowGainedFocus(WindowEvent e)
//			{
//				view.getTweetField().requestFocusInWindow();
//			}
//
//			public void windowLostFocus(WindowEvent e)
//			{
//			}
//		});
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
		
		//if(hidden)
		//	toggleWindowView("down");
		UIManager.addPropertyChangeListener(this);
		try
		{
			if(splash != null)
			{
				splash.close();
			}
		}
		catch(Exception e){
			logger.warn(e.getLocalizedMessage());
		}
		try
		{
			tray = new TwitzTrayIcon(this);
		}
		catch (Exception ex)
		{
			logger.error("Fatal Error: ",ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Critical Error", JOptionPane.ERROR_MESSAGE);
			exit();
		}
		//session.addTwitzMainView("Default", view);
		mainFrame.addView(view);
		try
		{
			view.setMaximum(true);
		}
		catch (PropertyVetoException ex)
		{
			logger.error(ex.getLocalizedMessage());
		}
		for(TwitzMainView v : session.getAutoLoadingSessions())
		{
			mainFrame.addView(v);
			//v.initTwitter();
		}
		//view.addPropertyChangeListener("POPUP", tray);
		mainFrame.setVisible(true);
		mainFrame.initSessions();
		try
		{
			getContext().getSessionStorage().restore(mainFrame, "session.xml");
		}
		catch (IOException ex)
		{
			logger.error(ex.getLocalizedMessage());
		}
		mainFrame.fixFrameSizes();
		//view.initTwitter();
		if(logdebug)
			logger.debug("Leaving Startup");
		//logger.debug(getEnv());
	}//}}}

	@Override
	protected void shutdown()
	{
		try
		{
			getContext().getSessionStorage().save(mainFrame, "session.xml");
		}
		catch (IOException ex)
		{
			logger.error(ex.getLocalizedMessage());
		}
	}
	//END overrides

	//Static methods
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

	/**
	 * This method uses privilaged  access to calculate the current operating system
	 * @return An Enum representing the OS
	 */
	private static OS getOS() {//{{{
		PrivilegedAction<String> doOSLookup = new PrivilegedAction<String>() {
			public String run() {
				return System.getProperty("user.home");
			}
		};

		//I'n a linux guy i'll default to UNIX
		OS rv = OS.UNIX;
		//Do the lookup
		String os = AccessController.doPrivileged(doOSLookup);
        if (os != null) {
            if (os.toLowerCase().startsWith("mac os x")) {
                rv = OS.OSX;
            } else if (os.contains("Windows")) {
                rv = OS.WINDOWS;
            }
        }
		return rv;
	}//}}}

	static void renderSplashFrame(Graphics2D g, String comp) {
        //final String[] comps = {"foo", "bar", "baz"};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120,140,200,40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("Loading "+comp+"...", 120, 150);
    }

	private static Logger getLogger() {
		return logger;
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

	/**
	 * <strong>Warning:</strong> Although this method is static it must NOT be
	 * called before this class has been initialized as it depends on the
	 * {@link TwitzDesktopFrame} to be created. It is safe to call this after
	 * {@link #createTopLevel()} is called.
	 * This method will resize JInternalFrame so they fit into this Twitz desktop
	 * @param comp The component to resize.
	 */
	public static void fixIFrameLocation(Component comp) {//{{{
//		java.awt.Dimension dim = mainFrame.getDesktop().getMaximumSize();
//		dim.
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle frame = comp.getBounds();
		Rectangle desktop = mainFrame.getDesktop().getBounds();//ge.getMaximumWindowBounds();
//		logger.debug("Width of desktop: " + desktop.toString());
//		logger.debug("Width of frame: " + frame.toString());
		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		boolean resizeH = false;
		if(frame.height > desktop.height)
		{
			comp.setSize(comp.getWidth(), (desktop.height));
			frame = comp.getBounds();
		}
		if(frame.width > desktop.width)
		{
			comp.setSize(desktop.width, comp.getHeight());
			frame = comp.getBounds();
		}
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
			if(up) y = (desktop.height - frame.height);// - 49;//-32 is to make up for most OS toolbars
			//System.out.println("X: " + x + " Y: " + y);
			comp.setLocation(x, y);
		}
	}//}}}


	public static void setLAFFromSettings(boolean background)
	{
		setLAFFromSettings(background, false);
	}

	//public
	/**
	 * This method is borrowed from org.jdesktop.application.LocalStorage
	 * @return
	 */
	public static File getConfigDirectory() {//{{{
		ResourceMap resource = null;
		String appId = null;//resource.getString("Application.id");
		String vendorId = null;//resource.getString("Application.venderId");
		try
		{
			resource = getContext().getResourceMap(TwitzApp.class);
			appId = resource.getString("Application.id");
			vendorId = resource.getString("Application.venderId");
		}
		catch(Exception e)
		{
			java.util.logging.Logger.getLogger(TwitzApp.class.getName()).log(Level.WARNING, "Error while looking up resource");
		}
		if(appId == null) {
			appId = "Twitz";//TwitzApp.getContext().getApplicationClass().getSimpleName();
		}
		if (vendorId == null)
		{
			vendorId = "Andrew Williams";
		}

		File directory = null;
		String userHome = null;
		try
		{
			userHome = System.getProperty("user.home");
		}
		catch (SecurityException ignore)
		{
		}
		if (userHome != null)
		{
			OS osId = getOS();
			if (osId == OS.WINDOWS)
			{
				File appDataDir = null;
				try
				{
					String appDataEV = System.getenv("APPDATA");
					if ((appDataEV != null) && (appDataEV.length() > 0))
					{
						appDataDir = new File(appDataEV);
					}
				}
				catch (SecurityException ignore)
				{
				}
				if ((appDataDir != null) && appDataDir.isDirectory())
				{
					// ${APPDATA}\{vendorId}\${applicationId}
					String path = vendorId + "\\" + appId + "\\";
					directory = new File(appDataDir, path);
				}
				else
				{
					// ${userHome}\Application Data\${vendorId}\${applicationId}
					String path = "Application Data\\" + vendorId + "\\" + appId + "\\";
					directory = new File(userHome, path);
				}
			}
			else if (osId == OS.OSX)
			{
				// ${userHome}/Library/Application Support/${applicationId}
				String path = "Library/Application Support/" + appId + "/";
				directory = new File(userHome, path);
			}
			else
			{
				// ${userHome}/.${applicationId}/
				String path = "." + appId + "/";
				directory = new File(userHome, path);
			}
		}
		return directory;
	}//}}}

	public static synchronized URL verifyImage(URL img)
	{
		URL rv = null;

		ResourceMap res = getContext().getResourceMap(TwitzMainView.class);
		String resourcesDir = res.getResourcesDir();
		String filename = resourcesDir + res.getString("NO_PICTURE_DEFAULT");
		//filename = resourceMap.getResourcesDir() + resourceMap.getString("icon.comment");
		URL altImg = res.getClassLoader().getResource(filename);

		//URL img = u.getProfileImageURL();

		URLConnection fileCheck = null;
		if(img != null)
		{
			rv = img;
			try
			{
				fileCheck = img.openConnection();
				int size = fileCheck.getContentLength();
				if (size <= 0)
				{
					rv = altImg;
					//System.out.println("wwhhhhhhhhhhhhhhhhhooooooooooooooooooooooooooooooooooooooooooo");
				}
			}
			catch (IOException ioe)
			{
				//System.out.println("wwooooooooooooooooooooooooooooooooooooooooooo");
				rv = altImg;
			}
		}
		else
		{
			rv = altImg;
		}
		return rv;
	}

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(TwitzApp.class, args);
    }
	//END static methods

	private void buildSplash() {
	}

	private Point getDesktopCenter() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
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
	
	public TwitzTrayIcon getTrayIcon()
	{
		return this.tray;
	}

	public JFrame getMainFrame()
	{
		return this.mainFrame;
	}

	public void propertyChange(PropertyChangeEvent e) {//{{{
		if(e.getPropertyName().equals("lookAndFeelChange")) {
			setLAFFromSettings(true);
		}
		else if(e.getPropertyName().equals("skinChange")) {
			//
		}
		//if(logdebug)
		//	logger.debug("PropertyChangeEvent recieved: "+e.getPropertyName());
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
			mainFrame.showAboutBox();
		}
		else if(cmd.equals("PrefsDlg")) {
			mainFrame.showPrefsBox();
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
			SettingsManager sm = TwitzSessionManager.getInstance().getSettingsManagerForSession("Default");
			String skin = sm.getString("twitz_skin");
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
