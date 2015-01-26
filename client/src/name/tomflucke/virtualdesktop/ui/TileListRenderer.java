package name.tomflucke.virtualdesktop.ui;

import java.awt.Component;

import name.tomflucke.virtualdesktop.map.Tile;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

/**
 * An object which renders a Tile object.
 * 
 * This is meant to be used with a JList for having a place from which to drag
 * new tiles.
 * 
 * @author tom
 * @version 1.0.0
 *
 */
public class TileListRenderer extends JLabel implements ListCellRenderer<Tile>,
        SwingConstants
{
	private static final long serialVersionUID = -7992322394942398165L;
	
	/**
	 * Determines whether or not a preview of the tile will display
	 */
	private boolean showIcons;
	/**
	 * Determines whether or not the name of the tile will display
	 */
	private boolean showNames;
	
	{
		int marginSize = 6;
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(marginSize, marginSize,
		        marginSize, marginSize));
	}
	
	{
		showIcons = true;
		showNames = true;
	}
	
	/**
	 * Sets whether or not a tile will display a preview or not.
	 * 
	 * @param icons true to display the tile preview, false otherwise
	 */
	public void setShowIcons(boolean icons)
	{
		showIcons = icons;
	}
	
	/**
	 * Determines whether or not a tile will display a preview or not.
	 * 
	 * @return true if displaying the tile preview, false otherwise
	 */
	public boolean isShowingIcons()
	{
		return showIcons;
	}
	
	/**
	 * Sets whether or not a tile will display the name or not.
	 * 
	 * @param names true to display the tile name, false otherwise
	 */
	public void setShowNames(boolean names)
	{
		showNames = names;
	}
	
	/**
	 * Determines whether or not a tile will display the name or not.
	 * 
	 * @return true if displaying the tile name, false otherwise
	 */
	public boolean isShowingNames()
	{
		return showNames;
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Tile> list,
	        Tile value, int index, boolean isSelected, boolean cellHasFocus)
	{
		if (showIcons)
		{
			setIcon(new ImageIcon(value.image));
		}
		if (showNames)
		{
			setText(value.name);
		}
		return this;
	}
}