package name.tomflucke.virtualdesktop.ui;

import java.awt.Component;

import name.tomflucke.virtualdesktop.map.Tile;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

public class TileListRenderer implements ListCellRenderer<Tile>, SwingConstants
{
	private boolean showIcons;
	private boolean showNames;
	private int marginSize;
	private int textHPosition;
	private int textVPosition;
	private int hPosition;
	private int vPosition;
	
	{
		marginSize = 6;
		showIcons = true;
		showNames = true;
		textHPosition = CENTER;
		textVPosition = BOTTOM;
		hPosition = CENTER;
		vPosition = CENTER;
	}
	
	public void setShowIcons(boolean icons)
	{
		showIcons = icons;
	}
	
	public boolean isShowingIcons()
	{
		return showIcons;
	}
	
	public void setShowNames(boolean names)
	{
		showNames = names;
	}
	
	public boolean isShowingNames()
	{
		return showNames;
	}
	
	public void setHorizontalTextAlignment(int alignment)
	{
		textHPosition = alignment;
	}
	
	public int getHorizontalTextAlignment()
	{
		return textHPosition;
	}
	
	public void setVerticalTextAlignment(int alignment)
	{
		textVPosition = alignment;
	}
	
	public int getVerticalTextAlignment()
	{
		return textVPosition;
	}
	
	public void setVerticalAlignment(int alignment)
	{
		vPosition = alignment;
	}
	
	public int getVerticalAlignment()
	{
		return vPosition;
	}
	
	public void setHorizontalAlignment(int alignment)
	{
		hPosition = alignment;
	}
	
	public int getHorizontalAlignment()
	{
		return hPosition;
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Tile> list,
	        Tile value, int index, boolean isSelected, boolean cellHasFocus)
	{
		JLabel result = new JLabel();
		result.setBorder(BorderFactory.createEmptyBorder(marginSize,
		        marginSize, marginSize, marginSize));
		if (showIcons)
		{
			result.setIcon(new ImageIcon(value.image));
		}
		if (showNames)
		{
			result.setText(value.name);
		}
		result.setHorizontalTextPosition(textHPosition);
		result.setVerticalTextPosition(textVPosition);
		result.setVerticalAlignment(vPosition);
		result.setHorizontalAlignment(hPosition);
		return result;
	}
}