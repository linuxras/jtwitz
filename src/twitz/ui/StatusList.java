/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.MouseAdapter;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import twitter4j.Status;
import twitter4j.User;
import org.apache.log4j.Logger;
import twitz.TwitzMainView;
import twitz.events.DefaultTwitzEventModel;
import twitz.events.TwitzEvent;
import twitz.events.TwitzEventModel;
import twitz.events.TwitzEventType;
import twitz.events.TwitzListener;
import twitz.ui.dialogs.StatusPopupPanel;
import twitz.ui.models.StatusListModel;
import twitz.ui.renderers.StatusListRenderer;
import twitz.util.ListHotSpot;
import twitz.util.TwitzSessionManager;

/**
 *
 * @author Andrew Williams
 */
public class StatusList extends JList implements ActionListener, TwitzEventModel{

	private DefaultTwitzEventModel dtem = new DefaultTwitzEventModel();
	private StatusListModel model = new StatusListModel();;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private boolean logdebug = logger.isDebugEnabled();
	private Map<String, Rectangle> hotspots = Collections.synchronizedMap(new TreeMap<String, Rectangle>());
	private Vector<ListHotSpot> hotzone = new Vector<ListHotSpot>();
	public static final String SESSION_PROPERTY = "sessionName";
	private String sessionName;
	private TwitzMainView view;
	private HierarchyBoundsListener sizeListener;
	/*
	 * Used to calculate the a real size change because it seems the LAF
	 * does a small 1 pixel change when it does mouseover
	 */
	private Dimension lastSize = new Dimension(0,0);

	public StatusList(String session) {
		this(new StatusListModel(), session);
	}

	public StatusList(ListModel model, String session) {
		super(model);
		setSessionName(session);
		super.setCellRenderer(new StatusListRenderer(session));
		initDefaults();
	}

	private void initDefaults() //{{{
	{
		MouseListener clickListener = new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				StatusList source = (StatusList) e.getSource();
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					java.awt.Point p = e.getPoint();
					int index = source.locationToIndex(p);
					if (index != -1)
					{ //Show menu only if list is not empty
						if (source.getSelectedIndex() == -1)
						{
							source.setSelectedIndex(index);
						}
						//Make the caller this panel as we can add the selected list to the panel
						//that  will make the action listener of the menu items this panel as well
						//showMenu(source, p);
					}
				}
				else
				{

					int selection = source.getSelectedIndex();
					//System.out.println("Inside click event");
					checkActionSpot(e);
				//	if (source.isActionSpot(e))
				//	{
				//		StatusPopupPanel spp = new StatusPopupPanel();
				//		spp.configureBox(source, source.getSelectedValue(), selection);
				//		spp.popupBox(e.getXOnScreen(), e.getYOnScreen());
				//	}
				}
			}
		};
		addMouseListener(clickListener);
		sizeListener = new HierarchyBoundsListener(){

			public void ancestorMoved(HierarchyEvent e)
			{
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void ancestorResized(HierarchyEvent e)
			{
				java.awt.Component p = getParent();
				java.awt.Component c = e.getChanged();
				if((c != null && p != null) && c.equals(p) && isRealResize(p.getSize()))
				{
					StatusListModel mod = getModel();
					if(!mod.isEditing() && !mod.isEmpty())
					{
						//logger.debug("qqqqqqqqqqqqqqqqqqqqqqqqqqq "+c.getName()+" "+p.toString()+" "+p.getParent().getName()+" qqqqqpppppppppppppppppppppppppp");
						Vector<Status> stat = mod.getDataVector();
						mod.setDataVector(stat);
					}
				}
				//throw new UnsupportedOperationException("Not supported yet.");
			}
		};
		addHierarchyBoundsListener(sizeListener);
	} //}}}

	private boolean isRealResize(Dimension size)
	{
		boolean rv = false;
		int owp = (lastSize.width+1);
		int owm = (lastSize.width-1);
		int ohp = (lastSize.height+1);
		int ohm = (lastSize.height-1);
		if(size.width > owp || size.width < owm)
		{
			rv = true;
		}
		else if(size.height > ohp || size.height < ohm)
		{
			rv = true;
		}
		lastSize = size;
		return rv;
	}

	private void showMenu(StatusList list, Point p)
	{
		view.getActionsMenu(this).show(list, p.x, p.y);
	}

	public final void setSessionName(String name)
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

	public void addHotSpot(ListHotSpot spot)
	{
		hotzone.add(spot);
	}

	public Vector<ListHotSpot> getHotSpot()
	{
		return hotzone;
	}

	public void addHotSpot(String property, Rectangle rect) {
		if(rect != null && property != null)
		{
			hotspots.put(property, rect);
		}
	}
	
	public Map<String, Rectangle> getHotSpots()
	{
		return hotspots;
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

	public void setSelectedValue(Status aUser, boolean scrollToStatus) //{{{
	{
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
    } //}}}

	@Override
	public Status getSelectedValue() //{{{
	{
        int i = getMinSelectionIndex();
        if (i == -1) {
			return null;
		}
		else {
			StatusListModel dm = (StatusListModel)getModel();
			return (Status)dm.getElementAt(i);
		}
    } //}}}

	@Override
	public Status[] getSelectedValues() //{{{
	{
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
    } //}}}

	public void checkActionSpot(MouseEvent e)//{{{
	{
		int selection = this.getSelectedIndex();
		if(selection == -1)
			return;
		for(ListHotSpot spot : hotzone)
		{
			spot.checkActionSpot(e, this);
		}
	}//}}}

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

	public void actionPerformed(ActionEvent e) {
		Map map = Collections.synchronizedMap(new TreeMap());
		map.put("caller", this);
		map.put("async", true);
		Status[] selections = getSelectedValues();
		map.put("selections", selections);
		fireTwitzEvent(new TwitzEvent(this, TwitzEventType.valueOf(e.getActionCommand()), new java.util.Date().getTime(), map));
	}
}
