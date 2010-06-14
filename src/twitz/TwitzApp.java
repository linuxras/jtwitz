/*
 * TwitzApp.java
 */

package twitz;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
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
public class TwitzApp extends SingleFrameApplication implements ActionListener, MouseListener {

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

	private void buildSplash() {
//		Point center = getDesktopCenter();
		GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
//		javax.swing.ImageIcon icon = new javax.swing.ImageIcon(splash);
//		splashFrame.setAlwaysOnTop(true);
//		splashFrame.setUndecorated(true);
//		splashFrame.setLayout(gridbag);
//		splashFrame.setSize(icon.getIconWidth(), icon.getIconHeight());
//		javax.swing.JLabel img = new javax.swing.JLabel();
//		Rectangle bound = new Rectangle();
//		bound.setSize(icon.getIconWidth(), icon.getIconHeight());
//		bound.setLocation((center.x - (icon.getIconWidth() / 2)), (center.y - (icon.getIconHeight() / 2)));
//		splashFrame.setBounds(getDesktopCenter(splashFrame));
//		img.setIcon(icon);
//		splashFrame.add(img);
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

	@Override
	protected void configureTopLevel(JFrame top) {
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
	}

    @Override protected  Component createMainComponent() {
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
		System.out.println("Inside createMainComponent");
		return view;
    }

    /**
     * At startup create and show the main frame of the application.
     */
	@Override
	protected void startup() {
		getMainTopLevel().setJMenuBar(view.getMenuBar());
		view.init();
		view.fixTables();
		if(hidden)
			toggleWindowView("down");
		//setLAFFromSettings();
		System.out.println("Inside Startup");
	}

	@Override
	protected JMenuBar createJMenuBar() {
		System.out.println("Inside createJMenuBar");
        return null;
    }


	@Override
	protected void initialize(String[] args) {
		System.out.println("Inside initialize...");
		//setLAFFromSettings();
		String skin = config.getString("twitz.skin");
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
		//org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel
		System.out.println("Leaving initialize...");
	}

	@Override
	protected void ready() {
		System.out.println("Inside ready()");
		if(hidden)
			toggleWindowView("down");
	}

	public static void setLAFFromSettings() {
		Runnable doLAF = new Runnable() {
			public void run() {
				String skin = config.getString("twitz.skin");
				//org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel
				String currentSkin = "";
				LookAndFeel laf = UIManager.getLookAndFeel();

				currentSkin = laf.getName().replaceAll(" ", "");

				if(UIManager.getLookAndFeel() instanceof SubstanceLookAndFeel)
					currentSkin = SubstanceLookAndFeel.getCurrentSkin().getDisplayName().replaceAll(" ", "");
				
				System.out.println("Current Skin: "+ currentSkin);
				if (skin != null && !skin.equals("") && !skin.equals(currentSkin))
				{
					System.out.println("Setting LAF...");
					//SubstanceLookAndFeel.setSkin(skin);
					//SubstanceLookAndFeel.setSkin("org.pushingpixels.substance.api.skin.Substance"+skin+"LookAndFeel");
					//System.out.println(SubstanceLookAndFeel.getCurrentSkin().getDisplayName());
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
					view.fixTables();
					System.out.println("New Skin: "+UIManager.getLookAndFeel().getName().replaceAll(" ", ""));
//					SwingUtilities.updateComponentTreeUI(getMainFrame());
				}
			}
		};
		SwingUtilities.invokeLater(doLAF);
	}

	public java.awt.Window getMainWindow() {
		return window;
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
	}

	public void actionPerformed(ActionEvent e) {
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
