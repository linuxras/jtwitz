/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TrendsPanel.java
 *
 * Created on Jun 25, 2010, 4:26:11 PM
 */

package twitz.ui;

import twitz.ui.dialogs.LocationListDialog;
import org.jdesktop.application.Action;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trend;
import twitter4j.Trends;
import twitz.ui.models.TrendsListModel;

/**
 *
 * @author mistik1
 */
public class TrendsPanel extends javax.swing.JPanel {

    /** Creates new form TrendsPanel */
    public TrendsPanel() {
		resourceMap = twitz.TwitzApp.getContext().getResourceMap(twitz.ui.TrendsPanel.class);
		actionMap = twitz.TwitzApp.getContext().getActionMap(twitz.ui.TrendsPanel.class, this);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")//{{{
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCurrent = new javax.swing.JButton();
        btnDay = new javax.swing.JButton();
        btnWeek = new javax.swing.JButton();
        trendsDate = new org.jdesktop.swingx.JXDatePicker();
        cmbLocations = new javax.swing.JComboBox();
        trendsGroup = new javax.swing.ButtonGroup();
        trendsToolbar = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        lblLocation = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnPickTrend = new javax.swing.JButton();
        trendsScrollPane = new javax.swing.JScrollPane();
        trendsList = new javax.swing.JList();
        pagingToolbar = new javax.swing.JToolBar();
        btnPrev = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnNext = new javax.swing.JButton();

        btnCurrent.setIcon(resourceMap.getIcon("btnCurrent.icon")); // NOI18N
        btnCurrent.setToolTipText(resourceMap.getString("btnCurrent.toolTipText")); // NOI18N
        btnCurrent.setActionCommand(resourceMap.getString("btnCurrent.actionCommand")); // NOI18N
        trendsGroup.add(btnCurrent);
        btnCurrent.setFocusable(false);
        btnCurrent.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnCurrent.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnCurrent.setName("btnCurrent"); // NOI18N
        btnCurrent.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        btnDay.setIcon(resourceMap.getIcon("btnDay.icon")); // NOI18N
        btnDay.setToolTipText(resourceMap.getString("btnDay.toolTipText")); // NOI18N
        btnDay.setActionCommand(resourceMap.getString("btnDay.actionCommand")); // NOI18N
        trendsGroup.add(btnDay);
        btnDay.setFocusable(false);
        btnDay.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnDay.setIconTextGap(1);
        btnDay.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnDay.setName("btnDay"); // NOI18N
        btnDay.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        btnWeek.setIcon(resourceMap.getIcon("btnWeek.icon")); // NOI18N
        btnWeek.setToolTipText(resourceMap.getString("btnWeek.toolTipText")); // NOI18N
        btnWeek.setActionCommand(resourceMap.getString("btnWeek.actionCommand")); // NOI18N
        trendsGroup.add(btnWeek);
        btnWeek.setFocusable(false);
        btnWeek.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnWeek.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnWeek.setName("btnWeek"); // NOI18N
        btnWeek.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        trendsDate.setToolTipText(resourceMap.getString("trendsDate.toolTipText")); // NOI18N
        trendsDate.setFormats("yyyy-MM-dd");
        trendsDate.setMinimumSize(new java.awt.Dimension(46, 23));
        trendsDate.setName("trendsDate"); // NOI18N

        cmbLocations.setEditable(true);
        cmbLocations.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbLocations.setEnabled(false);
        cmbLocations.setName("cmbLocations"); // NOI18N

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        trendsToolbar.setFloatable(false);
        trendsToolbar.setRollover(true);
        trendsToolbar.setName("trendsToolbar"); // NOI18N
        trendsToolbar.setPreferredSize(new java.awt.Dimension(100, 24));

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        trendsToolbar.add(jLabel1);

        lblLocation.setText(resourceMap.getString("lblLocation.text")); // NOI18N
        lblLocation.setName("lblLocation"); // NOI18N
        trendsToolbar.add(lblLocation);

        jSeparator1.setMaximumSize(new java.awt.Dimension(1000, 10));
        jSeparator1.setName("jSeparator1"); // NOI18N
        trendsToolbar.add(jSeparator1);

        btnPickTrend.setIcon(resourceMap.getIcon("btnPickTrend.icon")); // NOI18N
        btnPickTrend.setText(resourceMap.getString("btnPickTrend.text")); // NOI18N
        btnPickTrend.setToolTipText(resourceMap.getString("btnPickTrend.toolTipText")); // NOI18N
        btnPickTrend.setFocusable(false);
        btnPickTrend.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPickTrend.setIconTextGap(0);
        btnPickTrend.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnPickTrend.setName("btnPickTrend"); // NOI18N
        btnPickTrend.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPickTrend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showLocationBox(evt);
            }
        });
        trendsToolbar.add(btnPickTrend);

        add(trendsToolbar, java.awt.BorderLayout.NORTH);

        trendsScrollPane.setName("trendsScrollPane"); // NOI18N

        trendsList.setModel(new twitz.ui.models.TrendsListModel());
        trendsList.setCellRenderer(new twitz.ui.renderers.TrendsListRenderer());
        trendsList.setName("trendsList"); // NOI18N
        trendsScrollPane.setViewportView(trendsList);

        add(trendsScrollPane, java.awt.BorderLayout.CENTER);

        pagingToolbar.setFloatable(false);
        pagingToolbar.setRollover(true);
        pagingToolbar.setMaximumSize(new java.awt.Dimension(70, 22));
        pagingToolbar.setName("pagingToolbar"); // NOI18N
        pagingToolbar.setPreferredSize(new java.awt.Dimension(100, 22));

        btnPrev.setIcon(resourceMap.getIcon("btnPrev.icon")); // NOI18N
        btnPrev.setText(resourceMap.getString("btnPrev.text")); // NOI18N
        btnPrev.setFocusable(false);
        btnPrev.setName("btnPrev"); // NOI18N
        btnPrev.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pagingToolbar.add(btnPrev);

        jSeparator2.setMaximumSize(new java.awt.Dimension(1000, 10));
        jSeparator2.setName("jSeparator2"); // NOI18N
        pagingToolbar.add(jSeparator2);

        btnNext.setIcon(resourceMap.getIcon("btnNext.icon")); // NOI18N
        btnNext.setText(resourceMap.getString("btnNext.text")); // NOI18N
        btnNext.setFocusable(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnNext.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnNext.setName("btnNext"); // NOI18N
        btnNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pagingToolbar.add(btnNext);

        add(pagingToolbar, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents
//}}}

	/**
	 * Display popup dialog with the locations list. 
	 * This method is called internally
	 * @param A <code>java.awt.event.MouseEvent</code>
	 */
	private void showLocationBox(java.awt.event.MouseEvent evt)//GEN-FIRST:event_showLocationBox
	{//GEN-HEADEREND:event_showLocationBox
		if(locations == null) {
			buildLocationBox();
		}
		locations.popupBox(evt.getXOnScreen(), evt.getYOnScreen());
	}//GEN-LAST:event_showLocationBox

	private void buildLocationBox() {
		if(locations == null) {
			twitz.TwitzMainView view = twitz.TwitzMainView.getInstance();
			locations = new LocationListDialog(view.getMainFrame(), this);
			locations.addTwitzListener(view);
		}
	}

	public void setTrends(Trends trends) {
		Trend[] tr = trends.getTrends();
		TrendsListModel tlm = (TrendsListModel) trendsList.getModel();
		tlm.clear();
		for(Trend t: tr)
		{
			tlm.addTrend(t);
		}
	}

	/**
	 * This method is just a wrapper that calls a method of the same
	 * name in the LocationListDialog to update the country and city lists.
	 * @param locals A <code>ResponseList</code> of <code>Location</code> objects
	 */
	public void setLocations(ResponseList<Location> locals) {
		if(locations == null)
			buildLocationBox();
		locations.setLocations(locals);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCurrent;
    private javax.swing.JButton btnDay;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPickTrend;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnWeek;
    private javax.swing.JComboBox cmbLocations;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JToolBar pagingToolbar;
    private org.jdesktop.swingx.JXDatePicker trendsDate;
    private javax.swing.ButtonGroup trendsGroup;
    private javax.swing.JList trendsList;
    private javax.swing.JScrollPane trendsScrollPane;
    private javax.swing.JToolBar trendsToolbar;
    // End of variables declaration//GEN-END:variables

	javax.swing.ActionMap actionMap;// = twitz.TwitzApp.getContext().getActionMap(LocationListDialog.class, this);
	org.jdesktop.application.ResourceMap resourceMap;
	LocationListDialog locations;
}
