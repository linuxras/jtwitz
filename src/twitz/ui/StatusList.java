/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import twitter4j.Status;
import twitter4j.User;
import twitz.TwitzMainView;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventModel;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;
import twitz.ui.models.StatusListModel;
import twitz.ui.renderers.StatusListRenderer;

/**
 *
 * @author mistik1
 */
public class StatusList extends JList implements MouseListener, ActionListener, TwitzEventModel{

	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private StatusListModel model = new StatusListModel();;

	public StatusList() {
		this(new StatusListModel());
	}

	public StatusList(ListModel model) {
		super(model);
		super.setCellRenderer(new StatusListRenderer());
	}

	/**
	 * refuse all other renderers we manage ours internally
	 */
	@Override
	public void setCellRenderer(ListCellRenderer renderer) {
		return;
	}

	/**
	 * Override to refuse all other model this is managed internally
	 */
	@Override
	public void setModel(ListModel model) {
		if(model instanceof StatusListModel)
		{
			super.setModel(model);
		}
	}

	@Override
	public StatusListModel getModel() {
		return (StatusListModel)super.getModel();
	}

	public void setSelectedValue(Status aUser, boolean scrollToStatus) {
        if(aUser == null)
            setSelectedIndex(-1);
        else if(!aUser.equals(getSelectedValue())) {
            int i,c;
            StatusListModel dm = (StatusListModel)getModel();
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
			StatusListModel dm = (StatusListModel)getModel();
			return (Status)dm.getElementAt(i);
		}
    }

	@Override
	public Status[] getSelectedValues() {
        ListSelectionModel sm = getSelectionModel();
        StatusListModel dm = (StatusListModel) getModel();

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

	//TwitzEventModel
	public void addTwitzListener(TwitzListener o) {
		dtem.addTwitzListener(o);
	}

	public void removeTwitzListener(TwitzListener o) {
		dtem.removeTwitzListener(o);
	}

	public void fireTwitzEvent(TwitzEvent e) {
		dtem.fireTwitzEvent(e);
	}

	//MouseListener
	public void mouseClicked(MouseEvent e) {//{{{
		if (e.getButton() == MouseEvent.BUTTON3)
		{
//			if(!isFocusOwner())
//				requestFocusInWindow();
			java.awt.Point p = e.getPoint();
			if (e.getSource() instanceof StatusList)
			{
				StatusList list = (StatusList) e.getSource();
				int index = list.locationToIndex(p);
				if (index != -1)
				{ //Show menu only if list is not empty
					if(list.getSelectedIndex() == -1)
						list.setSelectedIndex(index);
					//Make the caller this panel as we can add the selected list to the panel
					//that  will make the action listener of the menu items this panel as well
					TwitzMainView.getInstance().getActionsMenu(this).show(list, p.x, p.y);
				}

			}
		}
	}//}}}

	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e)	{ }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }

	public void actionPerformed(ActionEvent e) {
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("caller", this);
		map.put("async", true);
		Status[] selections = getSelectedValues();
		map.put("selections", selections);
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.valueOf(e.getActionCommand()), new java.util.Date().getTime(), map));
	}
}
