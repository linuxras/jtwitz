/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz;

import twitz.util.SettingsManager;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author mistik1
 */
public class TwitzTrayIcon {

	private TrayIcon trayIcon = null;
	private TwitzPopup popup = null;
	private SettingsManager config = SettingsManager.getInstance();
	private static TwitzApp mainApp;
	org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(TwitzTrayIcon.class);
	private TwitzView mainView;
	Logger logger = Logger.getLogger(TwitzTrayIcon.class.getName());


	public TwitzTrayIcon(TwitzApp app, TwitzView view) throws Exception{
		popup = new TwitzPopup(app);
		mainApp = app;
		mainView = view;
		if(!initComponents()) {
			throw new IllegalStateException("System Tray is not supported on your platform");
		}
	}

	private boolean initComponents() {
		try
		{
			ImageIcon icon = resourceMap.getImageIcon("Systray.icon");
			Image image = icon.getImage();
			trayIcon = new TrayIcon(image,"Twitz", popup);
			trayIcon.setImageAutoSize(true);
            trayIcon.setActionCommand(TwitzApp.UPDATE);
            trayIcon.addActionListener(mainApp);
            trayIcon.addMouseListener(mainApp);
            SystemTray.getSystemTray().add(trayIcon);

			MenuItem item = new MenuItem("Error");
			item.addActionListener(mainApp);
			item.setActionCommand(TwitzApp.TWEET_MINI);
			item.setLabel(TwitzApp.TWEET_MINI);
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
			return false;
		}
		return true;
	}

	public static Image getIcon() throws Exception {

        Image icon = null;

        ClassLoader loader = TwitzTrayIcon.class.getClassLoader();
        InputStream is = loader.getResourceAsStream("resources/clock.png");
        icon = ImageIO.read(is);
        is.close();

        return (icon);
    }
}
