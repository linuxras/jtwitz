/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui;

import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JPanel;
import twitter4j.User;


/**
 *
 * @author mistik1
 */
public class UserListMainPanel extends JPanel {
	javax.swing.GroupLayout layout;
	ParallelGroup vgroup;
	SequentialGroup hgroup;
	
	public UserListMainPanel()
	{
		super();
		initLayout();
	}

	@Override
	public GroupLayout getLayout() {
		return layout;
	}

	/**
	 * This method is overridden and currently does nothing.
	 * All layout is handled internally and cannot be changed externally.
	 * If you want to modify the layout use <code>getLayout</code> to get
	 * the internal layout and add setting to that.
	 * @param layout
	 */
	@Override
	public void setLayout(java.awt.LayoutManager layout) {
		return;
	}

	private void initLayout() {//{{{
		layout = new javax.swing.GroupLayout(this);

        super.setLayout(layout);
		layout.setHonorsVisibility(true);
		//layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);

        ParallelGroup pg = layout.createParallelGroup(Alignment.LEADING);
        SequentialGroup sg = layout.createSequentialGroup();
		pg = pg.addGroup(sg);
		//add components to this one.
        vgroup = layout.createParallelGroup(Alignment.LEADING);
		sg.addGroup(vgroup);
        layout.setHorizontalGroup(pg);

		//Add out components to this one.
		hgroup = layout.createSequentialGroup();
		ParallelGroup pg1 = layout.createParallelGroup(Alignment.LEADING)
				.addGroup(hgroup);
		layout.setVerticalGroup(pg1);
	}//}}}

	public void addPanel(Component comp) {

//      vgroup.addComponent(comp, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
//		hgroup.addComponent(comp, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);

		vgroup.addComponent(comp, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE);
		hgroup.addComponent(comp, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
	}

	public UserListPanel addUserList(User[] users, String listName) {
		UserListPanel panel = new UserListPanel();
		panel.addUser(users);
		panel.setTitle(listName);
		addPanel(panel);
		return panel;
	}

	public UserListPanel addUserList(String listName) {
		UserListPanel panel = new UserListPanel();
		panel.setTitle(listName);
		addPanel(panel);
		return panel;
	}
}
