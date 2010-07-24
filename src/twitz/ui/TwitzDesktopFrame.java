/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TwitzDesktopFrame.java
 *
 * Created on Jul 18, 2010, 4:10:40 PM
 */

package twitz.ui;

import java.awt.IllegalComponentStateException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import twitz.TwitzApp;
import twitz.TwitzMainView;
import twitz.ui.dialogs.MessageDialog;
import twitz.ui.dialogs.PreferencesDialog;
import twitz.ui.dialogs.TwitzAboutBox;
import twitz.util.DBManager;
import twitz.util.SettingsManager;
import twitz.util.TwitzSessionManager;

/**
 *
 * @author mistik1
 */
public class TwitzDesktopFrame extends javax.swing.JFrame implements ActionListener, 
		InternalFrameListener, PropertyChangeListener
{

	public static final String DESKTOP_PROPERTY = "desktopChanged";
	public static final String VIEWS_ADDED_PROPERTY = "viewAdded";
	public static final String VIEWS_REMOVED_PROPERTY = "viewRemoved";
	public static final String SESSION_ADDED_PROPERY = "sessionAdded";
	public static final String SESSION_REMOVED_PROPERTY = "sessionRemoved";

	private TwitzApp mainApp;
	private JDialog aboutBox;
	private PreferencesDialog prefs;
	private static TwitzDesktopFrame instance;
	org.jdesktop.application.ResourceMap resourceMap = TwitzApp.getContext().getResourceMap(TwitzDesktopFrame.class);
	javax.swing.ActionMap actionMap = TwitzApp.getContext().getActionMap(TwitzDesktopFrame.class, this);
	private Map<String, TwitzMainView> views = Collections.synchronizedMap(new TreeMap<String, TwitzMainView>());
	private Logger logger = Logger.getLogger(TwitzDesktopFrame.class.getName());
	private Map<String, JToggleButton> taskBarButtons = Collections.synchronizedMap(new TreeMap<String, JToggleButton>());
	private Map<String, JMenuItem> profileMenus = Collections.synchronizedMap(new TreeMap<String, JMenuItem>());
	private TwitzSessionManager sessions = TwitzSessionManager.getInstance();

    /** Creates new form TwitzDesktopFrame */
    private TwitzDesktopFrame(TwitzApp app) {
		this.mainApp = app;
        initComponents();
		//this.setContentPane(desktop);
		this.desktop.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);
		initDefaults();
    }

	public static TwitzDesktopFrame getInstance(TwitzApp app)
	{
		if(instance == null)
		{
			instance = new TwitzDesktopFrame(app);
		}
		return instance;
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        taskBarGroup = new javax.swing.ButtonGroup();
        desktop = new javax.swing.JDesktopPane();
        taskBar = new javax.swing.JToolBar();
        btnDefault = new javax.swing.JToggleButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        prefsMenuItem1 = new javax.swing.JMenuItem();
        profilesMenu = new javax.swing.JMenu();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem1 = new javax.swing.JMenuItem();
        logsMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setName("Form"); // NOI18N

        desktop.setName("desktop"); // NOI18N

        taskBar.setFloatable(false);
        taskBar.setRollover(true);
        taskBar.setName("taskBar"); // NOI18N

        taskBarGroup.add(btnDefault);
        
        btnDefault.setIcon(resourceMap.getIcon("btnDefault.icon")); // NOI18N
        btnDefault.setText(resourceMap.getString("btnDefault.text")); // NOI18N
        btnDefault.setFocusable(false);
        btnDefault.setIconTextGap(2);
        btnDefault.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnDefault.setName("btnDefault"); // NOI18N
        btnDefault.setPreferredSize(new java.awt.Dimension(76, 22));
        taskBar.add(btnDefault);

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        
        exitItem.setAction(actionMap.get("quit")); // NOI18N
        exitItem.setIcon(resourceMap.getIcon("exitItem.icon")); // NOI18N
        exitItem.setName("exitItem"); // NOI18N
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
        editMenu.setName("editMenu"); // NOI18N

        prefsMenuItem1.setAction(actionMap.get("showPrefsBox")); // NOI18N
        prefsMenuItem1.setIcon(resourceMap.getIcon("showPrefsBox.Action.icon")); // NOI18N
        prefsMenuItem1.setText(resourceMap.getString("prefsMenuItem1.text")); // NOI18N
        prefsMenuItem1.setName("prefsMenuItem1"); // NOI18N
        editMenu.add(prefsMenuItem1);

        menuBar.add(editMenu);

        profilesMenu.setText(resourceMap.getString("profilesMenu.text")); // NOI18N
        profilesMenu.setName("profilesMenu"); // NOI18N
        menuBar.add(profilesMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem1.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem1.setIcon(resourceMap.getIcon("aboutMenuItem1.icon")); // NOI18N
        aboutMenuItem1.setText(resourceMap.getString("aboutMenuItem1.text")); // NOI18N
        aboutMenuItem1.setName("aboutMenuItem1"); // NOI18N
        helpMenu.add(aboutMenuItem1);

        logsMenuItem1.setAction(actionMap.get("viewHTMLLog")); // NOI18N
        logsMenuItem1.setIcon(resourceMap.getIcon("logsMenuItem1.icon")); // NOI18N
        logsMenuItem1.setText(resourceMap.getString("logsMenuItem1.text")); // NOI18N
        logsMenuItem1.setName("logsMenuItem1"); // NOI18N
        helpMenu.add(logsMenuItem1);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(taskBar, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addComponent(taskBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }//GEN-END:initComponents

	private void initDefaults()
	{
		//setContentPane(desktop);
		//getContentPane().add(taskBar, BorderLayout.SOUTH);
		btnDefault.setActionCommand("Default");
		btnDefault.addActionListener(this);
		taskBarButtons.put("Default", btnDefault);
		setPreferredSize(new java.awt.Dimension(640, 480));
		sessions.addPropertyChangeListener(TwitzSessionManager.ADDED_PROPERTY, this);
		sessions.addPropertyChangeListener(TwitzSessionManager.LOADED_PROPERTY, this);
		//buildProfilesMenu();
		//throw new UnsupportedOperationException("Not yet implemented");
	}
	
	@Action
    public void showAboutBox() {//{{{
        if (aboutBox == null) {
            //JFrame mainFrame = TwitzApp.getApplication().getMainFrame();
            aboutBox = new TwitzAboutBox(this);
            aboutBox.setLocationRelativeTo(this);
        }
        //TwitzApp.getApplication().show(aboutBox);
		aboutBox.setVisible(true);
    }//}}}

	@Action
	public void showPrefsBox() {//{{{
		if(prefs == null) {
			//JFrame mainFrame = TwitzApp.getApplication().getMainFrame();
			prefs = new PreferencesDialog(this, true, mainApp);
			prefs.addPropertyChangeListener(mainApp);
			prefs.setLocationRelativeTo(this);
			prefs.setSessionName("Default");
			prefs.setSingleSessionMode(false);
		}
		prefs.setVisible(true);
	}//}}}

	@Action
	public void viewHTMLLog()//{{{
	{
		ResourceMap res = twitz.TwitzApp.getContext().getResourceMap(twitz.TwitzMainView.class);
		MessageDialog msg = new MessageDialog(this, false);
		msg.setContentType("text/html");
		try
		{
			URI spec = twitz.TwitzApp.getConfigDirectory().toURI();
			String s = spec.toString()+"/logs/index.html";
			URL path = new URL(s);
			msg.setMessage(path);
			msg.setResizable(true);
			msg.setTitle(res.getString("LOG_WINDOW_TITLE.TEXT"));
			msg.setSize(640, 480);
			msg.setVisible(true);
			//InputStream is = this.getClass().getResourceAsStream(path);
		}
		catch (IOException ex)
		{
			logger.error(ex.getMessage(), ex);
		}
		//InputStream is = this.getClass().getResourceAsStream(path);
	}//}}}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnDefault;
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem logsMenuItem1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem prefsMenuItem1;
    private javax.swing.JMenu profilesMenu;
    private javax.swing.JToolBar taskBar;
    private javax.swing.ButtonGroup taskBarGroup;
    // End of variables declaration//GEN-END:variables


	public void setDesktop(JDesktopPane desk)
	{
		JDesktopPane old = desktop;
		if(desk != null)
		{
			this.desktop = desk;
			this.desktop.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);
			firePropertyChange(DESKTOP_PROPERTY, old, desk);
		}
	}

	public JDesktopPane getDesktop()
	{
		return this.desktop;
	}

	private void buildProfilesMenu()
	{
		ResourceMap res = TwitzApp.getContext().getResourceMap(TwitzMainView.class);
		Map<String, TwitzMainView> vmap = TwitzSessionManager.getInstance().getSessions();
		Set<String> set = vmap.keySet();
		Iterator<String> iter = set.iterator();
		profilesMenu.removeAll();
		profileMenus.clear();
		//logger.debug(vmap.size()+" '''''''''''''''''''''''''''''''''''''''''''''");
		while(iter.hasNext())
		{
			String tname = iter.next();
			if(tname.equals("Default"))
				continue;
			TwitzMainView v = vmap.get(tname);
			boolean selected = views.containsKey(tname);

			JMenuItem item = new JMenuItem(tname);
			item.setIcon(res.getIcon("icon.user_comment"));
			item.setEnabled(true);
			item.setActionCommand(tname);
			item.addActionListener(this);
			profilesMenu.add(item);
			profileMenus.put(tname, item);
		}
	}

	public void initSessions()
	{
		Set<String> set = views.keySet();
		Iterator<String> iter = set.iterator();
		while(iter.hasNext())
		{
			String sname = iter.next();
			TwitzMainView v = views.get(sname);
			if(v != null)
			{
				SettingsManager sm = TwitzSessionManager.getInstance().getSettingsManagerForSession(sname);
				if(sm.getBoolean(DBManager.SESSION_AUTOLOAD))
				{
					v.initTwitter();
				}
			}
		}
	}

	/**
	 * Adds a new view to the desktop and registers it with our internal Map
	 * @param sessionName Name of the session managing this view
	 * @param view The {@link twitz.TwitzMainView} to add
	 * @throws IllegalComponentStateException if the is already a session be the given {@code sessionName}
	 */
	public void addView(twitz.TwitzMainView view)
	{
		if(view != null)
		{
			ResourceMap res = TwitzApp.getContext().getResourceMap(TwitzMainView.class);
			String sessionName = view.getSessionName();
			if(!views.containsKey(sessionName))
			{
				views.put(sessionName, view);
//
				view.setVisible(true);
				view.setSize(500, 600);
				view.addInternalFrameListener(this);
				try
				{
					getDesktop().add(view, javax.swing.JDesktopPane.DEFAULT_LAYER);
				}
				catch(Exception ex)
				{
					System.out.println(ex.getLocalizedMessage());
				}
				try {
					view.setSelected(true);
				} catch (java.beans.PropertyVetoException e) {}
				if(!sessionName.equals("Default")) //default is created stat
				{
					JToggleButton btnD = new JToggleButton();

					btnD.setIcon(resourceMap.getIcon("btnDefault.icon")); // NOI18N
					btnD.setText(sessionName); // NOI18N
					btnD.setFocusable(false);
					btnD.setIconTextGap(2);
					btnD.setMargin(new java.awt.Insets(2, 2, 2, 2));
					btnD.setName(sessionName.replaceAll(" ", "_").replaceAll("\\W", "")); // NOI18N
					btnD.setPreferredSize(new java.awt.Dimension(0, 22));
					btnD.setActionCommand(sessionName);
					btnD.addActionListener(this);
					btnD.setSelected(true);
					taskBarGroup.add(btnD);
					taskBar.add(btnD);
					taskBarButtons.put(sessionName, btnD);

					twitz.TwitzApp.fixIFrameLocation(view);
//					JMenuItem item = new JMenuItem(sessionName);
//					item.setIcon(res.getIcon("icon.user_comment"));
//					item.setEnabled(false);
//					item.setActionCommand(sessionName);
//					item.addActionListener(this);
//					profilesMenu.add(item);
//					profileMenus.put(sessionName, item);
				}
				
//				firePropertyChange(VIEWS_ADDED_PROPERTY, null, view);
			}
			else
				throw new IllegalComponentStateException("A session by that name already exists");//TODO needs I18N
		}
	}

	public void removeView(String sessionName)
	{
		twitz.TwitzMainView v = null;
		if(views.containsKey(sessionName))
		{
			v = views.remove(sessionName);
			if(taskBarButtons.containsKey(sessionName))
				taskBarButtons.remove(sessionName);
			firePropertyChange(VIEWS_REMOVED_PROPERTY, null, v);
		}
	}

	public twitz.TwitzMainView getView(String sessionName)
	{
		twitz.TwitzMainView view = null;
		if(views.containsKey(sessionName))
			view = views.get(sessionName);
		return view;
	}

	public Map<String, twitz.TwitzMainView> getViews()
	{
		return this.views;
	}

	public void fixFrameSizes()
	{
		Set<String> set = views.keySet();
		Iterator<String> iter = set.iterator();
		while(iter.hasNext())
		{
			TwitzMainView v = views.get(iter.next());
			mainApp.fixIFrameLocation(v);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JToggleButton)
		{
			JToggleButton btn = (JToggleButton)e.getSource();
			if(btn.isSelected())
			{
				System.out.println("ActionCommand "+e.getActionCommand());
				if(views.containsKey(e.getActionCommand()))
				{
					logger.debug("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
					TwitzMainView v = views.get(e.getActionCommand());
					if(v.isSelected() && !v.isIcon())
					{
						try
						{
							v.setIcon(true);
						}
						catch (PropertyVetoException ex)
						{
							logger.warn(ex.getLocalizedMessage());
						}
					}
					else if(v.isIcon())
					{
						try
						{
							v.setIcon(false);
						}
						catch (PropertyVetoException ex)
						{
							logger.warn(ex.getLocalizedMessage());
						}
					}
					else
					{
						try
						{
							v.toFront();
							v.setSelected(true);
						}
						catch (PropertyVetoException ex)
						{
							logger.warn(ex.getLocalizedMessage());
						}
					}
				}
			}
		}
		else if(e.getSource() instanceof JMenuItem)
		{
			JMenuItem item = (JMenuItem)e.getSource();
			String sname = e.getActionCommand();
			if(sname != null && views.containsKey(sname))
			{
				TwitzMainView v = views.get(sname);
				if(v.isClosed())
				{
					//v.setClosed(false);
					//add(v);
					logger.debug("lllllllllllllllllllllllllllllllllllllllllllllllllllllllll");
					getDesktop().add(v, javax.swing.JDesktopPane.DEFAULT_LAYER);
					v.setVisible(true);
					try
					{
						v.setSelected(true);
					}
					catch (PropertyVetoException ex)
					{
						logger.error(ex.getLocalizedMessage());
					}
					item.setEnabled(false);
					JToggleButton btn = taskBarButtons.get(sname);
					if (btn != null)
					{
						logger.debug("Enabling JButton " + sname + " in taskBar");
						//taskBar.remove(btn);
						//taskBar.revalidate();
						btn.setVisible(true);
					}
				}
			}
			else
			{
				addView(TwitzSessionManager.getInstance().getTwitMainViewForSession(e.getActionCommand()));
				item.setEnabled(false);
			}
		}
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void internalFrameClosing(InternalFrameEvent e) {
        //displayMessage("Internal frame closing", e);
		TwitzMainView f = (TwitzMainView)e.getInternalFrame();
		if(f != null)
		{
			String sname = f.getSessionName();
			JMenuItem item = profileMenus.get(sname);
			if(item != null)
			{
				logger.debug("Disabling JMenuItem "+sname);
				item.setEnabled(true);
			}
			JToggleButton btn = taskBarButtons.get(sname);
			if(btn != null)
			{
				logger.debug("Removing JButton "+sname+" from taskBar");
				//taskBar.remove(btn);
				//taskBar.revalidate();
				btn.setVisible(false);
			}
		}

    }

    public void internalFrameClosed(InternalFrameEvent e) {
        displayMessage("Internal frame closed", e);
    }

    public void internalFrameOpened(InternalFrameEvent e) {
        TwitzMainView f = (TwitzMainView)e.getInternalFrame();
		if(f != null)
		{
			String sname = f.getSessionName();
			JMenuItem item = profileMenus.get(sname);
			if(item != null)
			{
				item.setEnabled(false);
			}
			JToggleButton btn = taskBarButtons.get(sname);
			if(btn != null)
			{
				int c = taskBar.getComponentIndex(btn);
				//toolbar is zero indexed and the first would be 0
				//so if its less there is no such component
				if(c < 0)
					taskBar.add(btn);
			}
		}
		//displayMessage("Internal frame opened", e);
    }

    public void internalFrameIconified(InternalFrameEvent e) {
        displayMessage("Internal frame iconified", e);
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
        displayMessage("Internal frame deiconified", e);
    }

    public void internalFrameActivated(InternalFrameEvent e) {
		if (e.getInternalFrame() instanceof TwitzMainView)
		{
			TwitzMainView v = (TwitzMainView) e.getInternalFrame();
			if (views.containsValue(v))
			{
				JToggleButton btn = taskBarButtons.get(v.getSessionName());
				if(btn != null)
					btn.setSelected(true);
			}
		}
        //displayMessage("Internal frame activated", e);
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
		if (e.getInternalFrame() instanceof TwitzMainView)
		{
			TwitzMainView v = (TwitzMainView) e.getInternalFrame();
			if (views.containsValue(v))
			{
				JToggleButton btn = taskBarButtons.get(v.getSessionName());
				btn.setSelected(false);
			}
		}
        //displayMessage("Internal frame deactivated", e);
    }

    //Add some text to the text area.
    void displayMessage(String prefix, InternalFrameEvent e) {
        String s = prefix;// + ": " + e.getSource();
        logger.debug(s);
    }

	public void propertyChange(PropertyChangeEvent evt)
	{
		if(evt.getPropertyName().equals(TwitzSessionManager.ADDED_PROPERTY))
		{
			TwitzMainView v = (TwitzMainView)evt.getNewValue();
			SettingsManager sm = sessions.getSettingsManagerForSession(v.getSessionName());
			if(sm.getBoolean(DBManager.SESSION_AUTOLOAD))
			{
				addView(v);
			}
			buildProfilesMenu();
		}
		else if(evt.getPropertyName().equals(TwitzSessionManager.LOADED_PROPERTY))
		{
			buildProfilesMenu();
		}
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	
}
