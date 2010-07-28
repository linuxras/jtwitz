/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RelationshipDialog.java
 *
 * Created on Jul 27, 2010, 12:13:49 PM
 */

package twitz.ui.dialogs;

import javax.swing.table.DefaultTableModel;
import org.jdesktop.application.Action;
import twitter4j.Relationship;

/**
 *
 * @author mistik1
 */
public class RelationshipDialog extends javax.swing.JDialog {

	javax.swing.ActionMap actionMap = twitz.TwitzApp.getContext().getActionMap(RelationshipDialog.class, this);
	org.jdesktop.application.ResourceMap resourceMap = twitz.TwitzApp.getContext().getResourceMap(RelationshipDialog.class);

    /** Creates new form RelationshipDialog */
    public RelationshipDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
		twitz.TwitzMainView.fixJScrollPaneBarsSize(jScrollPane1);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        tablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        relationshipTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        actionPanel = new javax.swing.JPanel();
        actionToolBar = new javax.swing.JToolBar();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnOk = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("RelationshipDialog"); // NOI18N
        setUndecorated(true);

        tablePanel.setName("tablePanel"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        relationshipTable.setModel(new RelationshipTableModel());
        relationshipTable.setFillsViewportHeight(true);
        relationshipTable.setName("relationshipTable"); // NOI18N
        jScrollPane1.setViewportView(relationshipTable);

        javax.swing.GroupLayout tablePanelLayout = new javax.swing.GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
        );
        tablePanelLayout.setVerticalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
        );

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        actionPanel.setName("actionPanel"); // NOI18N

        actionToolBar.setFloatable(false);
        actionToolBar.setRollover(true);
        actionToolBar.setName("actionToolBar"); // NOI18N

        jSeparator1.setMaximumSize(new java.awt.Dimension(1000, 10));
        jSeparator1.setName("jSeparator1"); // NOI18N
        actionToolBar.add(jSeparator1);

        
        btnOk.setAction(actionMap.get("okAction")); // NOI18N
        btnOk.setIcon(resourceMap.getIcon("btnOk.icon")); // NOI18N
        btnOk.setText(resourceMap.getString("btnOk.text")); // NOI18N
        btnOk.setFocusable(false);
        btnOk.setName("btnOk"); // NOI18N
        btnOk.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        actionToolBar.add(btnOk);

        jSeparator2.setMaximumSize(new java.awt.Dimension(1000, 10));
        jSeparator2.setName("jSeparator2"); // NOI18N
        jSeparator2.setPreferredSize(new java.awt.Dimension(10, 1000));
        actionToolBar.add(jSeparator2);

        javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.setHorizontalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(actionToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
        );
        actionPanelLayout.setVerticalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(actionToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tablePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                    .addComponent(actionPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RelationshipDialog dialog = new RelationshipDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

	@Action
	public void okAction()
	{
		dispose();
	}

	public void setRelationship(Relationship relation)
	{
		relationshipTable.setValueAt(relation.getSourceUserScreenName(), 0, 1);
		relationshipTable.setValueAt(relation.getTargetUserScreenName(), 0,  2);
		relationshipTable.setValueAt(relation.isSourceFollowingTarget(), 1 , 1);
		relationshipTable.setValueAt(relation.isTargetFollowingSource(), 1, 2);
		relationshipTable.setValueAt(relation.isSourceFollowedByTarget(), 2, 1);
		relationshipTable.setValueAt(relation.isTargetFollowedBySource(), 2, 2);
		relationshipTable.setValueAt(relation.isSourceBlockingTarget(), 3, 2);
		relationshipTable.setValueAt(relation.isSourceNotificationsEnabled(), 4, 1);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JToolBar actionToolBar;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTable relationshipTable;
    private javax.swing.JPanel tablePanel;
    // End of variables declaration//GEN-END:variables

	class RelationshipTableModel extends DefaultTableModel
	{

		Class[] types = new Class[]
		{
			java.lang.Object.class, java.lang.String.class, java.lang.Object.class
		};
		String[] header = new String[]
		{
			"Legend", "Source User", "Target User"
		};
		boolean[] canEdit = new boolean[]
		{
			false, false, false
		};
		Object[][] model = new Object[][]
		{
			{
				"ScreenName", null, null
			},
			{
				"Following", null, null
			},
			{
				"Followed By", null, null
			},
			{
				"Blocked By", null, null
			},
			{
				"Notifications", null, null
			}
		};

		public RelationshipTableModel()
		{
			//super(model, header);
			super();
			super.setDataVector(model, header);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			return canEdit[columnIndex];
		}

		@Override
		public Class getColumnClass(int columnIndex)
		{
			return types[columnIndex];
		}

	}

}
