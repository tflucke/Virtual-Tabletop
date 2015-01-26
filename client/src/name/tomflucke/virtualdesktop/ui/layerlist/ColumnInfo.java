package name.tomflucke.virtualdesktop.ui.layerlist;

import java.lang.reflect.Array;

import javax.swing.Icon;

/**
 * Contains a description of how a column in a table should render and
 * interface.
 * 
 * @author tom
 * @version 1.0.0
 */
public class ColumnInfo implements ColumnConstants
{
	/**
	 * Contains information for how this object should display.
	 */
	/*
	 * This could be either a String, Icon, or a two dimensional array of
	 * Strings or Icons. The zero-th index represents a false value and the
	 * first is true.
	 */
	private final Object display;
	/**
	 * The width of the column.
	 */
	public final int width;
	/**
	 * The type of column.
	 * 
	 * @see ColumnConstants
	 */
	public final byte type;
	
	/**
	 * Initializes with the given width, type, and display information.
	 * 
	 * @param width The width of the column.
	 * @param type The type of the column.
	 * @param displayFalse The string which will represent the column when given
	 *            a false value (if type permits).
	 * @param displayTrue The string which will represent the column when given
	 *            a true value (if type permits).
	 */
	public ColumnInfo(int width, byte type, String displayFalse,
	        String displayTrue)
	{
		this.width = width;
		this.type = type;
		this.display = new String[] { displayFalse, displayTrue };
	}
	
	/**
	 * Initializes with the given width, type, and display information.
	 * 
	 * @param width The width of the column.
	 * @param type The type of the column.
	 * @param displayFalse The icon which will represent the column when given a
	 *            false value (if type permits).
	 * @param displayTrue The icon which will represent the column when given a
	 *            true value (if type permits).
	 */
	public ColumnInfo(int width, byte type, Icon displayFalse, Icon displayTrue)
	{
		this.width = width;
		this.type = type;
		this.display = new Icon[] { displayFalse, displayTrue };
	}
	
	/**
	 * Initializes with the given width, type, and display information.
	 * 
	 * @param width The width of the column.
	 * @param type The type of the column.
	 * @param display The string which will represent the column (if type
	 *            permits).
	 */
	public ColumnInfo(int width, byte type, String display)
	{
		this.width = width;
		this.type = type;
		this.display = display;
	}
	
	/**
	 * Initializes with the given width, type, and display information.
	 * 
	 * @param width The width of the column.
	 * @param type The type of the column.
	 * @param display The icon which will represent the column (if type
	 *            permits).
	 */
	public ColumnInfo(int width, byte type, Icon display)
	{
		this.width = width;
		this.type = type;
		this.display = display;
	}
	
	/**
	 * Initializes with the given width and type.
	 * 
	 * A ColumnInfo initialized this way will return null for any methods
	 * concerning display.
	 * 
	 * @param width The width of the column.
	 * @param type The type of the column.
	 */
	public ColumnInfo(int width, byte type)
	{
		this.width = width;
		this.type = type;
		this.display = null;
	}
	
	/**
	 * Gets the object representing how the column should be displayed, given a
	 * certain value.
	 * 
	 * @param state The current value.
	 * @return An object which should be used to display the column, or null if
	 *         not initialized with display information.
	 */
	public Object getDisplay(boolean state)
	{
		if (display == null)
		{
			return null;
		}
		if (display.getClass().isArray())
		{
			return Array.get(display, state ? 1 : 0);
		}
		else
		{
			return display;
		}
	}
	
	/**
	 * Gets the class of the object which represents the display.
	 * 
	 * This will be either a String or an Icon.
	 * 
	 * @return The class of the display object, or null if not initialized with
	 *         display information.
	 */
	public Class<?> getDisplayType()
	{
		if (display == null)
		{
			return null;
		}
		Class<?> displayClass = display.getClass();
		return displayClass.isArray() ? displayClass.getComponentType()
		        : displayClass;
	}
}