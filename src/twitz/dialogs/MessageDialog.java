/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MessageDialog.java
 *
 * Created on Jun 2, 2010, 12:34:18 AM
 */

package twitz.dialogs;

import javax.swing.Icon;
import org.jdesktop.application.Action;

/**
 *
 * @author mistik1
 */
public class MessageDialog extends javax.swing.JDialog {

    /** Creates new form MessageDialog */
    public MessageDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
		//this.setBounds(twitz.TwitzApp.getDesktopCenter(this));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        messagePane = new javax.swing.JEditorPane();
        imageLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(MessageDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(520, 314));
        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getActionMap(MessageDialog.class, this);
        btnClose.setAction(actionMap.get("closeDialog")); // NOI18N
        btnClose.setIcon(resourceMap.getIcon("btnClose.icon")); // NOI18N
        btnClose.setText(resourceMap.getString("btnClose.text")); // NOI18N
        btnClose.setName("btnClose"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        messagePane.setBackground(resourceMap.getColor("messagePane.background")); // NOI18N
        messagePane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, resourceMap.getColor("messagePane.border.highlightOuterColor"), resourceMap.getColor("messagePane.border.highlightInnerColor"), resourceMap.getColor("messagePane.border.shadowOuterColor"), resourceMap.getColor("messagePane.border.shadowInnerColor"))); // NOI18N
        messagePane.setEditable(false);
        messagePane.setForeground(resourceMap.getColor("messagePane.foreground")); // NOI18N
        messagePane.setName("messagePane"); // NOI18N
        jScrollPane1.setViewportView(messagePane);

        imageLabel.setIcon(resourceMap.getIcon("imageLabel.icon")); // NOI18N
        imageLabel.setText(resourceMap.getString("imageLabel.text")); // NOI18N
        imageLabel.setName("imageLabel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(imageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(btnClose)
                        .addGap(212, 212, 212))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(imageLabel)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MessageDialog dialog = new MessageDialog(new javax.swing.JFrame(), true);
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
	public void closeDialog()
	{
		dispose();
	}

	public void setMessage(String msg) {
		messagePane.setText(msg);
	}

	public String getMessage() {
		return messagePane.getText();
	}

	public String getSelected() {
		return messagePane.getSelectedText();
	}

	public void setIcon(Icon icon) {
		imageLabel.setIcon(icon);
	}

	public Icon getIcon() {
		return imageLabel.getIcon();
	}

	public void setContentType(String t) {
		messagePane.setContentType(t);
	}

	public String getContentType() {
		return messagePane.getContentType();
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JEditorPane messagePane;
    // End of variables declaration//GEN-END:variables

}
