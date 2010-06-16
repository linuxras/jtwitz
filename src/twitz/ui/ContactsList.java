/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import twitter4j.User;
import twitz.ui.models.ContactsListModel;

/**
 *
 * @author mistik1
 */
public class ContactsList extends JList{

	private ContactsListModel model;

	public ContactsList() {
		super(new ContactsListModel());
	}

	public ContactsList(ContactsListModel model) {
		super(model);
	}

	public void setSelectedValue(User aUser, boolean scrollToUser) {
        if(aUser == null)
            setSelectedIndex(-1);
        else if(!aUser.equals(getSelectedValue())) {
            int i,c;
            ContactsListModel dm = (ContactsListModel)getModel();
            for(i=0,c=dm.getSize();i<c;i++) {
                if(aUser.equals(dm.getElementAt(i))){
                    setSelectedIndex(i);
                    if(scrollToUser)
                        ensureIndexIsVisible(i);
                    //repaint();
                    return;
                }
			}
            setSelectedIndex(-1);
        }
        //repaint();
    }

	@Override
	public User getSelectedValue() {
        int i = getMinSelectionIndex();
        if (i == -1) {
			return null;
		}
		else {
			ContactsListModel dm = (ContactsListModel)getModel();
			return dm.getElementAt(i);
		}
    }

	@Override
	public User[] getSelectedValues() {
        ListSelectionModel sm = getSelectionModel();
        ContactsListModel dm = (ContactsListModel) getModel();

        int iMin = sm.getMinSelectionIndex();
        int iMax = sm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return new User[0];
        }

        User[] rvTmp = new User[1+ (iMax - iMin)];
        int n = 0;
        for(int i = iMin; i <= iMax; i++) {
            if (sm.isSelectedIndex(i)) {
                rvTmp[n++] = dm.getElementAt(i);
            }
        }
        User[] rv = new User[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        return rv;
    }


}
