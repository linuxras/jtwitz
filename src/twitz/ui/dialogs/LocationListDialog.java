/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LocationListDialog.java
 *
 * Created on Jun 27, 2010, 1:16:52 AM
 */

package twitz.ui.dialogs;

import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitz.TwitzMainView;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventModel;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;
import twitz.testing.LocationTest;
import twitz.ui.models.LocationListModel;
import twitz.util.SettingsManager;
import twitz.util.TwitzSessionManager;

/**
 *
 * @author Andrew Williams
 */
public class LocationListDialog extends JDialog implements TwitzEventModel {
	private SettingsManager config;

    /** Creates new form LocationListDialog */
    public LocationListDialog(Frame topLevel, JPanel caller, String session) {
		super(topLevel, true);
		setSessionName(session);
		this.topLevel = topLevel;
		this.caller = caller;
        initComponents();
		initDefaults();
    }

	private void initDefaults()
	{
		ListSelectionListener countryListener = new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e)
			{
				JList list = (JList)e.getSource();
				if(list.getSelectedIndex() != -1 && list.isFocusOwner()) {
					logger.debug("Country selection made, clearing cities");
					//citiesList.setSelectedIndex(-1);
					citiesList.clearSelection();
				}
			}
		};
		ListSelectionListener cityListener = new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e)
			{
				JList list = (JList)e.getSource();
				if(list.getSelectedIndex() != -1 && list.isFocusOwner()) {
					logger.debug("City selection made clearing countries");
					//countriesList.setSelectedIndex(-1);
					countriesList.clearSelection();
				}
			}
		};
		countriesList.setPrototypeCellValue("01234567891234567890");
		countriesList.addListSelectionListener(countryListener);
		citiesList.setPrototypeCellValue("01234567891234567890");
		citiesList.addListSelectionListener(cityListener);
		this.setUndecorated(true);
		this.addWindowFocusListener(new WindowFocusListener(){

			public void windowGainedFocus(WindowEvent e)
			{
				fixLocation();
			}

			public void windowLostFocus(WindowEvent e)
			{
			}
		});
		if(!view.isConnected())
			addSampleData();
		twitz.TwitzMainView.fixJScrollPaneBarsSize(cityScrollPane);
		twitz.TwitzMainView.fixJScrollPaneBarsSize(countryScrollPane);
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked") //{{{
    private void initComponents() {//GEN-BEGIN:initComponents

        countryPanel = new javax.swing.JPanel();
        lblCountry = new javax.swing.JLabel();
        countryScrollPane = new javax.swing.JScrollPane();
        countriesList = new javax.swing.JList();
        cityPanel = new javax.swing.JPanel();
        lblCity = new javax.swing.JLabel();
        cityScrollPane = new javax.swing.JScrollPane();
        citiesList = new javax.swing.JList();
        buttonPanel = new javax.swing.JPanel();
        btnDone = new javax.swing.JButton();
        btnWorld = new javax.swing.JButton();

        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        setFont(resourceMap.getFont("TrendsListPane.font")); // NOI18N
        setMinimumSize(new java.awt.Dimension(500, 300));
        setModal(true);
        setName("TrendsListPane"); // NOI18N
        setUndecorated(true);

        countryPanel.setName("countryPanel"); // NOI18N
        countryPanel.setLayout(new java.awt.BorderLayout(0, 3));

        lblCountry.setIcon(resourceMap.getIcon("lblCountry.icon")); // NOI18N
        lblCountry.setText(resourceMap.getString("lblCountry.text")); // NOI18N
        lblCountry.setName("lblCountry"); // NOI18N
        countryPanel.add(lblCountry, java.awt.BorderLayout.NORTH);

        countryScrollPane.setBorder(null);
        countryScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        countryScrollPane.setMinimumSize(new java.awt.Dimension(21, 100));
        countryScrollPane.setName("countryScrollPane"); // NOI18N
        countryScrollPane.setPreferredSize(new java.awt.Dimension(208, 100));

        countriesList.setModel(new twitz.ui.models.LocationListModel());
        countriesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        countriesList.setCellRenderer(new twitz.ui.renderers.LocationListRenderer(true));
        countriesList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        countriesList.setName("countriesList"); // NOI18N
        countriesList.setVisibleRowCount(4);
        countryScrollPane.setViewportView(countriesList);

        countryPanel.add(countryScrollPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(countryPanel, java.awt.BorderLayout.PAGE_START);

        cityPanel.setName("cityPanel"); // NOI18N
        cityPanel.setLayout(new java.awt.BorderLayout(0, 3));

        lblCity.setIcon(resourceMap.getIcon("lblCity.icon")); // NOI18N
        lblCity.setText(resourceMap.getString("lblCity.text")); // NOI18N
        lblCity.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblCity.setName("lblCity"); // NOI18N
        cityPanel.add(lblCity, java.awt.BorderLayout.NORTH);

        cityScrollPane.setBorder(null);
        cityScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        cityScrollPane.setName("cityScrollPane"); // NOI18N
        cityScrollPane.setPreferredSize(new java.awt.Dimension(315, 100));

        citiesList.setModel(new twitz.ui.models.LocationListModel());
        citiesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        citiesList.setCellRenderer(new twitz.ui.renderers.LocationListRenderer(false));
        citiesList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        citiesList.setName("citiesList"); // NOI18N
        citiesList.setVisibleRowCount(5);
        cityScrollPane.setViewportView(citiesList);

        cityPanel.add(cityScrollPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(cityPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setName("buttonPanel"); // NOI18N
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        
        btnDone.setAction(actionMap.get("closeBox")); // NOI18N
        btnDone.setIcon(resourceMap.getIcon("btnDone.icon")); // NOI18N
        btnDone.setText(resourceMap.getString("btnDone.text")); // NOI18N
        btnDone.setName("btnDone"); // NOI18N
        buttonPanel.add(btnDone);

        btnWorld.setAction(actionMap.get("doWorld")); // NOI18N
        btnWorld.setIcon(resourceMap.getIcon("btnWorld.icon")); // NOI18N
        btnWorld.setText(resourceMap.getString("btnWorld.text")); // NOI18N
        btnWorld.setToolTipText(resourceMap.getString("btnWorld.toolTipText")); // NOI18N
        btnWorld.setName("btnWorld"); // NOI18N
        buttonPanel.add(btnWorld);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }//GEN-END:initComponents
	//}}}
	

	private void addSampleData()
	{
		//LocationTest(String countryName, String countryCode,
		//	String placeName, int placeCode, int woeid)
		String country[] = {"Worldwide","United States", "Germany", "Italy", "United Kingdom", "Mexico", "Brazil", "Canada", "Ireland"};
		String cc[] = {"WW","US", "GE", "IT", "UK", "ME", "BR", "CA", "IR"};
		String city[] = { "Atlanta", "Baltimore", "Boston", "Chicago", "Dallas-Ft. Worth",
			"Houston", "London", "Los Angeles", "New York City", "Philadelphia",
			"San Antonio", "San Francisco", "Seattle", "Sao Paulo", "Washinton D.C."};
		String ccc[] = { cc[1], cc[1], cc[1], cc[1], cc[1], cc[1], cc[4], cc[1], cc[1], cc[1],
			cc[1], cc[1], cc[1], cc[6], cc[1]};
		String ccl[] = { country[1], country[1], country[1], country[1], country[1], country[1],
			country[4], country[1], country[1], country[1], country[1], country[1], country[1],
			country[6], country[1]};
		LocationListModel cm = new LocationListModel();
		for(int i=0; i<country.length; i++)
		{
			cm.addLocation(new LocationTest(country[i], cc[i], "", 0, (i+1)));
		}
		LocationListModel lm = new LocationListModel();
		for(int i=0; i<city.length; i++)
		{
			lm.addLocation(new LocationTest(ccl[i], ccc[i], city[i], i, (i+14)));
		}
		countriesList.setModel(cm);
		citiesList.setModel(lm);
	}

	public final void setSessionName(String name)
	{
		String old = this.sessionName;
		this.sessionName = name;
		config = TwitzSessionManager.getInstance().getSettingsManagerForSession(sessionName);
		view = TwitzSessionManager.getInstance().getTwitzMainViewForSession(sessionName);
		//firePropertyChange(SESSION_PROPERTY, old, name);
	}

	public String getSessionName()
	{
		return this.sessionName;
	}

	public boolean isLocation(Object o)
	{
		return (o instanceof Location);
	}

	public void setLocations(ResponseList locals) {
		LocationListModel countryModel = new LocationListModel();
		LocationListModel cityModel = new LocationListModel();
		countries.clear();
		cities.clear();
		//Twitter.com does not provide Worldwide as a Location 
		countries.add("Worldwide");
		for(Object o : locals)
		{
			if (isLocation(o))
			{
				Location l = (Location) o;
				if (!countries.contains(l.getCountryName()))
				{
					countryModel.addLocation(l);
					countries.add(l.getCountryName());
				}
				if (!cities.contains(l.getPlaceName()))
				{
					cityModel.addLocation(l);
					cities.add(l.getPlaceName());
				}
			}
		}
		countryModel.insertLocation(new LocationTest("Worldwide", "WW", "", 0, 1), 0);
		countriesList.setModel(countryModel);
		citiesList.setModel(cityModel);
	}

	@Action
	public void closeBox()
	{
		if(citiesList.getSelectedIndex() == -1 && countriesList.getSelectedIndex() == -1)
		{
			this.dispose();
			return;
		}
		try
		{
			Map map = new TreeMap();
			map.put("async", true);
			map.put("caller", caller);
			ArrayList args = new ArrayList();
			Location selected = null;
			if (countriesList.getSelectedIndex() != -1)
			{
				//Get the woeid from the location in this list. maybe need a custom model here
				int index = countriesList.getSelectedIndex();
				LocationListModel model = (LocationListModel) countriesList.getModel();
				//selected = (Location)countriesList.getSelectedValue();
				selected = model.getLocationAt(index);
				args.add(selected.getWoeid());
				firePropertyChange("locationsChanged", oldLocation, selected.getCountryName());
				oldLocation = selected.getCountryName();
			}
			if (citiesList.getSelectedIndex() != -1)
			{
				//Get the woeid from the location in this list. maybe need a custom model here
				int index = citiesList.getSelectedIndex();
				LocationListModel model = (LocationListModel) citiesList.getModel();
				//selected = (Location)citiesList.getSelectedValue();
				selected = model.getLocationAt(index);
				args.add(selected.getPlaceCode());
				firePropertyChange("locationsChanged", oldLocation, selected.getPlaceName());
				oldLocation = selected.getPlaceName();
			}
			if (selected != null)
			{

				map.put("arguments", args);
				TwitzEvent te = new TwitzEvent(this, TwitzEventType.LOCATION_TRENDS, new Date().getTime(), map);
				fireTwitzEvent(te);
			}
		}
		catch (Exception e)
		{
			logger.error(e.getLocalizedMessage());
		}
		//oldLocation = selected.getName();
		this.dispose();
	}

	@Action
	public void doWorld()
	{
		ListSelectionModel model = countriesList.getSelectionModel();
		model.clearSelection();
		//countriesList.setSelectedIndex(-1);
		model = citiesList.getSelectionModel();
		model.clearSelection();
		//citiesList.setSelectedIndex(-1);
		Map map = new TreeMap();
		map.put("async", true);
		map.put("caller", caller);
		ArrayList args = new ArrayList();
		args.add(1);
		map.put("arguments", args);
		firePropertyChange("locationsChanged", oldLocation, "Worldwide");
		oldLocation = "Worldwide";
		TwitzEvent te = new TwitzEvent(this, TwitzEventType.LOCATION_TRENDS, new Date().getTime(), map);
		fireTwitzEvent(te);
		this.dispose();
	}

	public void setDismssOnClick(boolean b) {
		this.dismissOnClick = b;
	}

	public boolean getDismissOnClick() {
		return this.dismissOnClick;
	}
	
	public void popupBox(int x, int y) {
		this.setLocation(x, y);
		this.setVisible(true);
		//fixLocation();
	}
	
	private void fixLocation()
	{
		twitz.TwitzApp.fixLocation(this);
	}

	public void addTwitzListener(TwitzListener o)
	{
		dtem.addTwitzListener(o);
	}

	public void removeTwitzListener(TwitzListener o)
	{
		dtem.removeTwitzListener(o);
	}

	public void fireTwitzEvent(TwitzEvent e)
	{
		dtem.fireTwitzEvent(e);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDone;
    private javax.swing.JButton btnWorld;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JList citiesList;
    private javax.swing.JPanel cityPanel;
    private javax.swing.JScrollPane cityScrollPane;
    private javax.swing.JList countriesList;
    private javax.swing.JPanel countryPanel;
    private javax.swing.JScrollPane countryScrollPane;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblCountry;
    // End of variables declaration//GEN-END:variables

	javax.swing.ActionMap actionMap = twitz.TwitzApp.getContext().getActionMap(LocationListDialog.class, this);
	org.jdesktop.application.ResourceMap resourceMap = twitz.TwitzApp.getContext().getResourceMap(LocationListDialog.class);
	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	Frame topLevel;
	JPanel caller;
	boolean dismissOnClick = true;
	private enum Direction { LEFT, RIGHT, UP, DOWN };
	final Logger logger = Logger.getLogger(this.getClass().getName());
	boolean logdebug = logger.isDebugEnabled();
	private String oldLocation = "WorldWide";
	private List<String> countries = Collections.synchronizedList(new ArrayList<String>());
	private List<String> cities = Collections.synchronizedList(new ArrayList<String>());
	public static final String SESSION_PROPERTY = "sessionName";
	private String sessionName;
	private TwitzMainView view;
}
