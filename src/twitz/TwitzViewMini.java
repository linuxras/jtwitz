/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TwitzViewMini.java
 *
 * Created on May 13, 2010, 3:24:37 PM
 */

package twitz;

import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.TwitterException;
import twitz.util.SettingsManager;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;

/**
 *
 * @author mistik1
 */
public class TwitzViewMini extends javax.swing.JFrame {

    /** Creates new form TwitzViewMini */
    public TwitzViewMini() {
		//tray = t;
		tm = twitz.twitter.TwitterManager.getInstance();
		errorDialog.setMaximumSize(new Dimension(300, 100));
        initComponents();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle frame = getBounds();
		Rectangle desk = ge.getMaximumWindowBounds();
		System.out.println("Widht of desktop: " + desk.toString());
		int x = (desk.width - frame.width);
		int y = (desk.height - frame.height) - 32;
		//System.out.println("X: " + x + " Y: " + y);
		setLocation(x, y);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTweet = new javax.swing.JTextField();
        btnTweet = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setAlwaysOnTop(true);
        setName("Form"); // NOI18N
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getResourceMap(TwitzViewMini.class);
        txtTweet.setText(resourceMap.getString("txtTweet.text")); // NOI18N
        txtTweet.setName("txtTweet"); // NOI18N
        txtTweet.setNextFocusableComponent(btnTweet);
        txtTweet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTweetKeyPressed(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(twitz.TwitzApp.class).getContext().getActionMap(TwitzViewMini.class, this);
        btnTweet.setAction(actionMap.get("sendTweetClicked")); // NOI18N
        btnTweet.setText(resourceMap.getString("btnTweet.text")); // NOI18N
        btnTweet.setToolTipText(resourceMap.getString("btnTweet.toolTipText")); // NOI18N
        btnTweet.setName("btnTweet"); // NOI18N

        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTweet, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTweet)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTweet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTweet))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void txtTweetKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTweetKeyPressed
	{//GEN-HEADEREND:event_txtTweetKeyPressed
		switch(evt.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				sendTweetClicked();
				break;
			case KeyEvent.VK_ESCAPE:
				dispose();
				break;

		}
	}//GEN-LAST:event_txtTweetKeyPressed

	private void formWindowActivated(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowActivated
	{//GEN-HEADEREND:event_formWindowActivated
		txtTweet.setText("");
		txtTweet.requestFocus();
	}//GEN-LAST:event_formWindowActivated

	@Action
	@SuppressWarnings("static-access")
	public void sendTweetClicked()
	{
		twitter4j.User u = null;
		try
		{
			u = tm.getTwitterInstance().verifyCredentials();
		}
		catch (TwitterException ex)
		{
			logger.log(Level.SEVERE, ex.getMessage());
			if(ex.isCausedByNetworkIssue()) {
				errorDialog.showMessageDialog(this, "Unable to reach "+ex.getMessage(), "Network Error", JOptionPane.ERROR_MESSAGE);
			}
			//logger.log(Level.SEVERE, ex.getStatusCode()+"");
		}

		try
		{
			tm.sendTweet(txtTweet.getText());
		}
		catch (TwitterException ex)
		{
			logger.log(Level.SEVERE, ex.getMessage());
			errorDialog.showMessageDialog(this, ex.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
		}
		catch (IllegalStateException ex)
		{
			logger.log(Level.SEVERE, ex.getMessage());
			errorDialog.showMessageDialog(this, ex.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
		}
		setVisible(false);
	}
	
    /**
    * @param args the command line arguments
    */
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new TwitzViewMini().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTweet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtTweet;
    // End of variables declaration//GEN-END:variables

	private SettingsManager config = SettingsManager.getInstance();
	//private TwitzTrayIcon tray = null;
	private twitz.twitter.TwitterManager tm;
	Logger logger = Logger.getLogger(TwitzViewMini.class.getName());
	private JOptionPane errorDialog = new JOptionPane();
	
}
