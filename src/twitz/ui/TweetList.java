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
import twitter4j.Tweet;
import twitter4j.User;
import twitz.TwitzMainView;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventModel;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;
import twitz.ui.models.TweetListModel;
import twitz.ui.renderers.TweetListRenderer;
import twitz.util.TwitzSessionManager;

/**
 *
 * @author Andrew Williams
 */
public class TweetList extends JList implements MouseListener, ActionListener, TwitzEventModel{

	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private TweetListModel model = new TweetListModel();
	public static final String SESSION_PROPERTY = "sessionName";
	private String sessionName;
	private TwitzMainView view;

	public TweetList() {
		this(new TweetListModel());
	}

	public TweetList(ListModel model) {
		super(model);
		super.setCellRenderer(new TweetListRenderer());
	}

	public void setSessionName(String name)
	{
		String old = this.sessionName;
		this.sessionName = name;
		//config = TwitzSessionManager.getInstance().getSettingsManagerForSession(sessionName);
		view = TwitzSessionManager.getInstance().getTwitMainViewForSession(sessionName);
		//firePropertyChange(SESSION_PROPERTY, old, name);
	}

	public String getSessionName()
	{
		return this.sessionName;
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
		if(model instanceof TweetListModel)
		{
			super.setModel(model);
		}
	}

	@Override
	public TweetListModel getModel() {
		return (TweetListModel)super.getModel();
	}

	public void setSelectedValue(Tweet aUser, boolean scrollToTweet) {
        if(aUser == null)
            setSelectedIndex(-1);
        else if(!aUser.equals(getSelectedValue())) {
            int i,c;
            TweetListModel dm = (TweetListModel)getModel();
            for(i=0,c=dm.getSize();i<c;i++) {
                if(aUser.equals(dm.getElementAt(i))){
                    setSelectedIndex(i);
                    if(scrollToTweet)
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
	public Tweet getSelectedValue() {
        int i = getMinSelectionIndex();
        if (i == -1) {
			return null;
		}
		else {
			TweetListModel dm = (TweetListModel)getModel();
			return (Tweet)dm.getElementAt(i);
		}
    }

	@Override
	public Tweet[] getSelectedValues() {
        ListSelectionModel sm = getSelectionModel();
        TweetListModel dm = (TweetListModel) getModel();

        int iMin = sm.getMinSelectionIndex();
        int iMax = sm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return new Tweet[0];
        }

        Tweet[] rvTmp = new Tweet[1+ (iMax - iMin)];
        int n = 0;
        for(int i = iMin; i <= iMax; i++) {
            if (sm.isSelectedIndex(i)) {
                rvTmp[n++] = (Tweet)dm.getElementAt(i);
            }
        }
        Tweet[] rv = new Tweet[n];
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
			if (e.getSource() instanceof TweetList)
			{
				TweetList list = (TweetList) e.getSource();
				int index = list.locationToIndex(p);
				if (index != -1)
				{ //Show menu only if list is not empty
					if(list.getSelectedIndex() == -1)
						list.setSelectedIndex(index);
					//Make the caller this panel as we can add the selected list to the panel
					//that  will make the action listener of the menu items this panel as well
					view.getActionsMenu(this).show(list, p.x, p.y);
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
		Tweet[] selections = getSelectedValues();
		map.put("selections", selections);
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.valueOf(e.getActionCommand()), new java.util.Date().getTime(), map));
	}
}
