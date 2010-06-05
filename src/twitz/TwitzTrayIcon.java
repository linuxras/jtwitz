/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz;

import java.awt.event.FocusEvent;
import javax.swing.event.PopupMenuEvent;
import twitz.util.SettingsManager;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author mistik1
 */
public class TwitzTrayIcon implements MouseListener{

	private TrayIcon trayIcon = null;
	private TwitzPopup popup = null;
	private SettingsManager config = SettingsManager.getInstance();
	private static TwitzApp mainApp;
	org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(TwitzTrayIcon.class);
	org.jdesktop.application.ResourceMap viewResource = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(TwitzView.class);
	private TwitzView mainView;
	private javax.swing.JFrame f = new javax.swing.JFrame();
	private java.awt.Component menuHack = f.getGlassPane();
	Logger logger = Logger.getLogger(TwitzTrayIcon.class.getName());


	public TwitzTrayIcon(TwitzApp app, TwitzView view) throws Exception{
		popup = new TwitzPopup(app);
		popup.addPopupMenuListener(new PopupMenuListener() {

			public void popupMenuWillBecomeVisible(PopupMenuEvent e)
			{
				//System.out.println("popupMenuWillBecomeVisible");
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
			{
				//System.out.println("popupMenuWillBecomeInvisible");
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void popupMenuCanceled(PopupMenuEvent e)
			{
				hideGlassPane();
				//System.out.println("Popup menu Canceled");
				//throw new UnsupportedOperationException("Not supported yet.");
			}
		});
		menuHack.setSize(1, 1);
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
			trayIcon = new TrayIcon(image,"Twitz");
			trayIcon.setImageAutoSize(true);
            trayIcon.setActionCommand(TwitzApp.UPDATE);
            trayIcon.addActionListener(mainApp);
            trayIcon.addMouseListener(this);
            SystemTray.getSystemTray().add(trayIcon);

			JMenuItem item = new JMenuItem(TwitzApp.TWEET_MINI);
			item.addActionListener(mainApp);
			item.setActionCommand(TwitzApp.TWEET_MINI);
			item.setIcon(viewResource.getIcon("icon.comment"));
//			item.setLabel(TwitzApp.TWEET_MINI);
			popup.add(item);

			item = new JMenuItem("About twitz");
			item.addActionListener(mainApp);
			item.setActionCommand("About");
			item.setIcon(viewResource.getIcon("icon.help"));
//			item.setLabel("About twitz");
			popup.add(item);

			item = new JMenuItem("Preferences");
			item.addActionListener(mainApp);
			item.setActionCommand("PrefsDlg");
			item.setIcon(viewResource.getIcon("icon.wrench"));
//			item.setLabel("Preferences");
			popup.add(item);

			item = new JMenuItem("Exit");
			item.setActionCommand("Exit");
			item.addActionListener(mainApp);
			item.setIcon(viewResource.getIcon("icon.door_out"));
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

	public void hideGlassPane() {
		menuHack.setVisible(false);
	}

	public void mouseClicked(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON3) {
			//show menu
			java.awt.Point p = e.getPoint();

			menuHack.setLocation(p.x, (p.y - 100));
			menuHack.setVisible(true);
			popup.setInvoker(menuHack);
			popup.setLocation(p.x, p.y - 100);
			popup.setVisible(true);

		}
		else {
			if(menuHack.isVisible()) {
				menuHack.setVisible(false);
			}
			else {
				mainApp.toggleWindowView("toggle");
			}
		}
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
