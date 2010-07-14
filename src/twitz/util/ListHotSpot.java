package twitz.util;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JList;

public class ListHotSpot implements Serializable
{
	public enum Direction {
		LEFT_TO_RIGHT,
		RIGHT_TO_LEFT
	};

	private URL icon;
	private URL disabledIcon;
	private Rectangle dim;
	private String key;
	private String tip;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport( this );
	private boolean enabled =  true;

	private Direction direction = Direction.RIGHT_TO_LEFT;

	public ListHotSpot(String name)
	{
		this(name, Direction.RIGHT_TO_LEFT, new Rectangle(45, 25, 20, 20), "");
	}

	//public ListHotSpot(String name, ImageIcon icon)
	//{
	//	this();
	//}

	public ListHotSpot(String name, Direction dir, Rectangle defaults, String tooltip)
	{
		this.direction = dir;
		this.key = name;
		this.dim = defaults;
		this.tip = tooltip;
		//this.icon = icon;
	}
	
	public void setEnabled(boolean val)
	{
		boolean old = this.enabled;
		this.enabled = val;
		this.pcs.firePropertyChange("enabled", old, val);
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setIcon(URL i)
	{
		URL old = this.icon;
		this.icon = i;
		this.pcs.firePropertyChange("icon", old, this.icon);
		
	}

	public URL getIcon()
	{
		return this.icon;
	}

	public void setDisabledIcon(URL i)
	{
		URL old = this.disabledIcon;
		this.disabledIcon = i;
		this.pcs.firePropertyChange("disabledIcon", old, i);
		
	}

	public URL getDisabledIcon()
	{
		return this.disabledIcon;
	}

	public void setDirection(Direction dir)
	{
		Direction old = this.direction;
		this.direction = dir;
		this.pcs.firePropertyChange("direction", old, this.direction);
	}

	public Direction getDirection()
	{
		return this.direction;
	}

	public void setName(String val)
	{
		String old = this.key;
		this.key = val;
		this.pcs.firePropertyChange("name", old, val);
	}

	public String getName()
	{
		return this.key;
	}

	public void setTooltipText(String tooltip)
	{
		String old = this.tip;
		this.tip = tooltip;
		this.pcs.firePropertyChange("tooltip", old, tooltip);
	}

	public String getTooltipText()
	{
		return this.tip;
	}

	public void checkActionSpot(MouseEvent e, JList list)//{{{
	{
		final java.awt.Point loc = e.getPoint();
		int pos = -1;
		int ypos = -1;
		Rectangle box = null;
		int selection = list.getSelectedIndex();

		if(selection != -1)
		{
			Rectangle rect = list.getCellBounds(selection, selection);
//				System.out.println("Index: " + selection + " \nCell Size:" + rect);
			switch(direction)
			{
				case LEFT_TO_RIGHT:
					pos = (rect.x + dim.x);
					ypos = (rect.y + dim.y);
					box = new Rectangle(rect.x + pos, ypos, dim.width, dim.height);
					//addHotspot("Actions", new Rectangle(45, 25, 20, 20));
//					System.out.println("pos: " + pos + " ypos: " + ypos + "\nBox: " + box + "\nPoint: " + loc);
					if (box.contains(loc))
					{
//						System.out.println("Got the click spot");
						this.pcs.firePropertyChange(key, null, e); //We send the MouseEvent along
					}
				break;
				case RIGHT_TO_LEFT:
					pos = (rect.width - dim.x);
					ypos = (rect.height - dim.y);
					box = new Rectangle(rect.x + pos, rect.y + ypos, dim.width, dim.height);
					//addHotspot("Actions", new Rectangle(45, 25, 20, 20));
//					System.out.println("pos: " + pos + " ypos: " + ypos + "\nBox: " + box + "\nPoint: " + loc);
					if (box.contains(loc))
					{
//						System.out.println("Got the click spot");
						this.pcs.firePropertyChange(key, null, e); //We send the MouseEvent along
					}
				break;
			}
		}
	}//}}}
	
	public boolean isActionSpot(MouseEvent e, int selection, JList list)//{{{
	{
		boolean rv = false;
		final java.awt.Point loc = e.getPoint();
		int pos = -1;
		int ypos = -1;
		Rectangle box = null;

		Rectangle rect = list.getCellBounds(selection, selection);
//		System.out.println("Index: " + selection + " \nCell Size:" + rect);
		switch(direction)
		{
			case LEFT_TO_RIGHT:
				pos = (rect.x + dim.x);
				ypos = (rect.y + dim.y);
				box = new Rectangle(pos, dim.y, dim.width, dim.height);
				//addHotspot("Actions", new Rectangle(45, 25, 20, 20));
//				System.out.println("pos: " + pos + " ypos: " + ypos + "\nBox: " + box + "\nPoint: " + loc);
				if (box.contains(loc))
				{
//					System.out.println("Got the spot");
					rv = true;
				}
			break;
			case RIGHT_TO_LEFT:
				pos = (rect.width - dim.x);
				ypos = (rect.height - dim.y);
		//		Rectangle box = new Rectangle(rect.x + pos, rect.height - dim.y, dim.width, dim.height);
				box = new Rectangle(rect.x + pos, ypos, dim.width, dim.height);
				//addHotspot("Actions", new Rectangle(45, 25, 20, 20));
//				System.out.println("pos: " + pos + " ypos: " + ypos + "\nBox: " + box + "\nPoint: " + loc);
				if (box.contains(loc))
				{
//					System.out.println("Got the spot");
					rv = true;
				}
			break;
		}
		return rv;
	}//}}}

	public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        this.pcs.addPropertyChangeListener( listener );
    }

    public void removePropertyChangeListener( PropertyChangeListener listener )
    {
        this.pcs.removePropertyChangeListener( listener );
    }

}
