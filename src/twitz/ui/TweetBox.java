/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TweetBox.java
 *
 * Created on Jul 13, 2010, 11:24:55 PM
 */

package twitz.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.Icon;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import twitter4j.Status;
import twitter4j.User;
import twitz.TwitzMainView;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventModel;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;

/**
 *
 * @author mistik1
 */
public class TweetBox extends javax.swing.JPanel implements ActionListener, TwitzEventModel {


    /** Creates new form TweetBox */
    public TweetBox() {
        initComponents();
		initDefaults();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnTweet = new javax.swing.JButton();
        btnMini = new javax.swing.JButton();
        lblChars = new javax.swing.JLabel();
        txtTweet = new javax.swing.JTextField();

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setName("TweetBox"); // NOI18N

        btnTweet.setText(resourceMap.getString("btnTweet.text")); // NOI18N
        btnTweet.setToolTipText(resourceMap.getString("btnTweet.toolTipText")); // NOI18N
        btnTweet.setName("btnTweet"); // NOI18N

        btnMini.setAction(actionMap.get("showMiniMode")); // NOI18N
        btnMini.setIcon(resourceMap.getIcon("btnMini.icon")); // NOI18N
        btnMini.setText(resourceMap.getString("btnMini.text")); // NOI18N
        btnMini.setToolTipText(resourceMap.getString("btnMini.toolTipText")); // NOI18N
        btnMini.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnMini.setName("btnMini"); // NOI18N

        lblChars.setFont(resourceMap.getFont("lblChars.font")); // NOI18N
        lblChars.setForeground(resourceMap.getColor("lblChars.foreground")); // NOI18N
        lblChars.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblChars.setText(resourceMap.getString("lblChars.text")); // NOI18N
        lblChars.setName("lblChars"); // NOI18N

        txtTweet.setName("txtTweet"); // NOI18N
        txtTweet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTweetKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTweetKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblChars, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTweet, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTweet)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMini)
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTweet)
                    .addComponent(btnMini)
                    .addComponent(txtTweet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblChars, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void txtTweetKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTweetKeyTyped
	{//GEN-HEADEREND:event_txtTweetKeyTyped
		keyTyped(evt);
}//GEN-LAST:event_txtTweetKeyTyped

	private void txtTweetKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTweetKeyReleased
	{//GEN-HEADEREND:event_txtTweetKeyReleased
		keyReleased(evt);
	}//GEN-LAST:event_txtTweetKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMini;
    private javax.swing.JButton btnTweet;
    private javax.swing.JLabel lblChars;
    private javax.swing.JTextField txtTweet;
    // End of variables declaration//GEN-END:variables
	
	//TwitzMainView view = TwitzMainView.getInstance();
	org.jdesktop.application.ResourceMap resourceMap = twitz.TwitzApp.getContext().getResourceMap(TweetBox.class);
	javax.swing.ActionMap actionMap = twitz.TwitzApp.getContext().getActionMap(TweetBox.class, this);
	Logger logger = Logger.getLogger(this.getClass().getName());
	boolean logdebug = logger.isDebugEnabled();
	DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private Status replyToStatus;

	private void initDefaults()
	{
		btnTweet.setActionCommand("UPDATE_STATUS");
		btnTweet.addActionListener(this);
	}

	@Action
	public void showMiniMode()
	{
		TwitzMainView.getInstance().showMiniMode();
	}

	@Action
	private void keyTyped(java.awt.event.KeyEvent evt) {//{{{
		switch(evt.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				//sendAsyncTweet();
				btnTweet.doClick();
				//sendTweetClicked().execute();
				break;
			case KeyEvent.VK_M:
				if(evt.isControlDown())
					TwitzMainView.getInstance().showMiniMode();
				break;
			case KeyEvent.VK_ESCAPE:
				TwitzMainView.getInstance().getMainApp().toggleWindowView("down");
				break;
		//	default:
		//		int c = txtTweet.getDocument().getLength();
		//		lblChars.setText((140 - c)+"");
		//		if((c > 0) && (c < 141)) {
		//			btnTweet.setEnabled(true);
		//			lblChars.setForeground(getResourceMap().getColor("lblChars.foreground"));
		//		}
		//		else if(c > 140) {
		//			lblChars.setForeground(Color.RED);
		//			btnTweet.setEnabled(false);
		//		}
		//		else
		//		{
		//			btnTweet.setEnabled(false);
		//		}
		}
	}//}}}

	@Action
	private void keyReleased(java.awt.event.KeyEvent evt) {//{{{
		int c = txtTweet.getDocument().getLength();
		lblChars.setText((140 - c)+"");
		if((c > 0) && (c < 141)) {
			btnTweet.setEnabled(true);
			lblChars.setForeground(TwitzMainView.getResourceMap().getColor("lblChars.foreground"));
		}
		else if(c > 140) {
			lblChars.setForeground(Color.RED);
			btnTweet.setEnabled(false);
		}
		else
		{
			btnTweet.setEnabled(false);
		}
	}//}}}

	public void setMiniIcon(Icon val)
	{
		btnMini.setIcon(val);
	}
	
	public void setButtonEnabled(boolean value)
	{
		this.btnTweet.setEnabled(value);
	}

	public void clearTweetText()
	{
		txtTweet.setText("");
	}

	public void setTweetEnabled(boolean value)
	{
		txtTweet.setEnabled(value);
	}

	public void setReplyToStatus(Status s)
	{
		this.replyToStatus = s;
	}

	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		if("UPDATE_STATUS".equals(cmd))
		{
			Map map = Collections.synchronizedMap(new TreeMap());
			map.put("caller", e.getSource());
			map.put("async", true);
			User[] selections = new User[4];//getContactsList().getSelectedValues();
			ArrayList args = new ArrayList();
			args.add(txtTweet.getText());
			map.put("arguments", args);
			logger.debug("Got action to perform");
			fireTwitzEvent(new TwitzEvent(this, TwitzEventType.valueOf(cmd),
						new java.util.Date().getTime(), map));
		}
	}

	public void fireTwitzEvent(TwitzEvent e)
	{
		dtem.fireTwitzEvent(e);
	}

	public void addTwitzListener(TwitzListener o)
	{
		dtem.addTwitzListener(o);
	}

	public void removeTwitzListener(TwitzListener o)
	{
		dtem.removeTwitzListener(o);
	}
}