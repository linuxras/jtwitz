/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import twitter4j.Status;
import twitz.ui.renderers.TweetsRenderer;

/**
 *
 * @author mistik1
 */
public class TweetsList extends JList {

	public TweetsList() {
		super();
		super.setCellRenderer(new TweetsRenderer());
		super.setModel(new DefaultListModel());
	}

	/*
	 * refuse all other renderers we manage ours internally
	 */
	@Override
	public void setCellRenderer(ListCellRenderer renderer) {
		return;
	}

	/*
	 * Override to refuse all other model this is managed internally
	 */
	@Override
	public void setModel(ListModel model) {
		return;
	}

	public void setSelectedValue(Status aUser, boolean scrollToStatus) {
        if(aUser == null)
            setSelectedIndex(-1);
        else if(!aUser.equals(getSelectedValue())) {
            int i,c;
            DefaultListModel dm = (DefaultListModel)getModel();
            for(i=0,c=dm.getSize();i<c;i++) {
                if(aUser.equals(dm.getElementAt(i))){
                    setSelectedIndex(i);
                    if(scrollToStatus)
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
	public Status getSelectedValue() {
        int i = getMinSelectionIndex();
        if (i == -1) {
			return null;
		}
		else {
			DefaultListModel dm = (DefaultListModel)getModel();
			return (Status)dm.getElementAt(i);
		}
    }

	@Override
	public Status[] getSelectedValues() {
        ListSelectionModel sm = getSelectionModel();
        DefaultListModel dm = (DefaultListModel) getModel();

        int iMin = sm.getMinSelectionIndex();
        int iMax = sm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return new Status[0];
        }

        Status[] rvTmp = new Status[1+ (iMax - iMin)];
        int n = 0;
        for(int i = iMin; i <= iMax; i++) {
            if (sm.isSelectedIndex(i)) {
                rvTmp[n++] = (Status)dm.getElementAt(i);
            }
        }
        Status[] rv = new Status[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        return rv;
    }
}
