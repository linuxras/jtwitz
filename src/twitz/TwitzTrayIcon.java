/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz;

import twitz.util.SettingsManager;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author mistik1
 */
public class TwitzTrayIcon {

	public static final String UPDATE = "Update";
	public static final String TWEET = "Tweet";
	public static final String TWEET_MINI = "TweetMini";
	private TrayIcon trayIcon = null;
	private TwitzPopup popup = null;
	private SettingsManager config = SettingsManager.getInstance();
	private static TwitzApp mainApp;// = new TwitzApp();
	org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(TwitzTrayIcon.class);
	//private TwitzViewMini mini = new TwitzViewMini();
	private TwitzView mainView; // = new TwitzView(mainApp);
	//private PreferencesDialog prefs;
	private Window window;
	Logger logger = Logger.getLogger(TwitzTrayIcon.class.getName());


	public TwitzTrayIcon(TwitzApp app, TwitzView view) {
		popup = new TwitzPopup(app);
		mainApp = app;
//		config = c;
		mainView = view;
		initComponents();
	}

	private void initComponents() {
		try
		{
			ImageIcon icon = resourceMap.getImageIcon("Systray.icon");
			Image image = icon.getImage();
			trayIcon = new TrayIcon(image,"Twitz", popup);
			trayIcon.setImageAutoSize(true);
            trayIcon.setActionCommand(UPDATE);
            trayIcon.addActionListener(mainApp);
            trayIcon.addMouseListener(mainApp);
            SystemTray.getSystemTray().add(trayIcon);

			MenuItem item = new MenuItem("Error");
			item.addActionListener(mainApp);
			item.setActionCommand(TWEET_MINI);
			item.setLabel(TWEET_MINI);
			popup.add(item);

			item = new MenuItem("About twitz");
			item.addActionListener(mainApp);
			item.setActionCommand("About");
			item.setLabel("About twitz");
			popup.add(item);

			item = new MenuItem("Preferences");
			item.addActionListener(mainApp);
			item.setActionCommand("PrefsDlg");
			item.setLabel("Preferences");
			popup.add(item);

			item = new MenuItem("Exit");
			item.setActionCommand("Exit");
			item.addActionListener(mainApp);
			popup.add(item);
		}
		catch(Exception e){
		}
	}

	public void toggleWindowView() {
		this.toggleWindowView("toggle");
	}

	public void toggleWindowView(String action) {
		Window win = mainApp.getMainWindow();
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
				if(!win.isShowing())
				{
					win.setVisible(true);
				}
			}
			else if(action.equalsIgnoreCase("down"))
			{
				win.setVisible(false);
			}
			else if(action.equalsIgnoreCase("toggle"))
			{
				if (win.isShowing() && win.isActive())
				{
					win.setVisible(false);
				}
				else if (win.isShowing() && !win.isActive())
				{
					win.toFront();
				}
				else
				{
					win.setVisible(true);
					mainApp.getMainFrame().setState(Frame.NORMAL);
					win.toFront();
				}
			}
		}
		else
		{
			if (win.isShowing() && win.isActive())
			{
				win.setVisible(false);
			}
			else if (win.isShowing() && !win.isActive())
			{
				win.toFront();
			}
			else
			{
				win.setVisible(true);
				mainApp.getMainFrame().setState(Frame.NORMAL);
			}
		}
	}

	public static Image getIcon() throws Exception {

        Image icon = null;

        ClassLoader loader = TwitzTrayIcon.class.getClassLoader();
        InputStream is = loader.getResourceAsStream("resources/clock.png");
        icon = ImageIO.read(is);
        is.close();

        return (icon);
    }

//	@SuppressWarnings("static-access")
//	public static void main(String[] args) {
//		//mainApp.launch(mainApp.getClass(), args);
//		//new TwitzTrayIcon();
//		launch(TwitzTrayIcon.class, args);
//	}

}
