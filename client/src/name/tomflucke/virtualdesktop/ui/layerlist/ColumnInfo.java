package name.tomflucke.virtualdesktop.ui.layerlist;

import java.lang.reflect.Array;

import javax.swing.Icon;

public class ColumnInfo implements ColumnConstants
{
	private final Object display;
	public final int width;
	public final byte type;
	
	public ColumnInfo(int width, byte type, String displayFalse,
	        String displayTrue)
	{
		this.width = width;
		this.type = type;
		this.display = new String[] { displayFalse, displayTrue };
	}
	
	public ColumnInfo(int width, byte type, Icon displayFalse,
			Icon displayTrue)
	{
		this.width = width;
		this.type = type;
		this.display = new Icon[] { displayFalse, displayTrue };
	}
	
	public ColumnInfo(int width, byte type, String display)
	{
		this.width = width;
		this.type = type;
		this.display = display;
	}
	
	public ColumnInfo(int width, byte type, Icon display)
	{
		this.width = width;
		this.type = type;
		this.display = display;
	}
	
	public ColumnInfo(int width, byte type)
	{
		this.width = width;
		this.type = type;
		this.display = null;
	}
	
	public Object getDisplay(boolean state)
	{
		if (display.getClass().isArray())
		{
			return Array.get(display, state ? 1 : 0);
		}
		else
		{
			return display;
		}
	}

	public Class<?> getDisplayType()
    {
		Class<?> displayClass = display.getClass();
		return displayClass.isArray() ? displayClass.getComponentType() : displayClass;
    }
}