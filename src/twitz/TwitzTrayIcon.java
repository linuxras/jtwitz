/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz;

import javax.swing.event.PopupMenuEvent;
import twitz.util.SettingsManager;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import org.apache.log4j.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author mistik1
 */
public class TwitzTrayIcon extends MouseAdapter implements PropertyChangeListener
{

	private TrayIcon trayIcon = null;
	private TwitzPopup popup = null;
	private SettingsManager config = SettingsManager.getInstance();
	private static TwitzApp mainApp;
	org.jdesktop.application.ResourceMap resourceMap = TwitzApp.getContext().getResourceMap(TwitzTrayIcon.class);
	org.jdesktop.application.ResourceMap viewResource = TwitzApp.getContext().getResourceMap(TwitzMainView.class);
	private Logger logger = Logger.getLogger(TwitzTrayIcon.class.getName());
	private boolean menuVisible = false;


	public TwitzTrayIcon(TwitzApp app) throws Exception
	{
		popup = new TwitzPopup(app);
		mainApp = app;
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
			popup.add(item);

			item = new JMenuItem("About twitz");
			item.addActionListener(mainApp);
			item.setActionCommand("About");
			item.setIcon(viewResource.getIcon("icon.help"));
			popup.add(item);

			item = new JMenuItem("Preferences");
			item.addActionListener(mainApp);
			item.setActionCommand("PrefsDlg");
			item.setIcon(viewResource.getIcon("icon.wrench"));
			popup.add(item);

			item = new JMenuItem("Exit");
			item.setActionCommand("Exit");
			item.addActionListener(mainApp);
			item.setIcon(viewResource.getIcon("icon.door_out"));
			popup.add(item);

			popup.addPopupMenuListener(new PopupMenuListener(){
				public void popupMenuCanceled(PopupMenuEvent e) {menuVisible = false;}
				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
				public void popupMenuWillBecomeVisible(PopupMenuEvent e) {menuVisible = true;}
			});
		}
		catch(Exception e){
			return false;
		}
		return true;
	}

	public boolean isMenuShowing()
	{
		return popup.isVisible();
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if(evt.getPropertyName().equalsIgnoreCase("POPUP"))
		{
			String[] s2 = (String[])evt.getNewValue();
			if(s2.length >= 3) {
				int msgType = Integer.parseInt(s2[2]);
				switch (msgType)
				{
					case 1:
						trayIcon.displayMessage(s2[0], s2[1], TrayIcon.MessageType.ERROR);
						break;
					case 2:
						trayIcon.displayMessage(s2[0], s2[1], TrayIcon.MessageType.INFO);
						break;
					case 3:
						trayIcon.displayMessage(s2[0], s2[1], TrayIcon.MessageType.NONE);
						break;
					case 4:
						trayIcon.displayMessage(s2[0], s2[1], TrayIcon.MessageType.WARNING);
						break;
				}
			}
		}
	}

	public void mousePressed(MouseEvent e)
	{
		//if (e.isPopupTrigger()) {
		if(e.getButton() == MouseEvent.BUTTON3) 
		{
			//show menu
			java.awt.Point p = e.getPoint();

			popup.setInvoker(popup);
			popup.setLocation(p.x, p.y - 100);
			menuVisible = true;
			popup.setVisible(true);

		}
		else
		{
			logger.debug("Got click is popup showing: "+ isMenuShowing());
			if(menuVisible)
			{
				//Just set the variable false here nothing else
				//since clicking has already made the popup go away
				menuVisible = false;
			}
			else
			{
				mainApp.toggleWindowView("toggle");
			}
		}
	}
}
