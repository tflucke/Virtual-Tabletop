package name.tomflucke.virtualdesktop.ui.layerlist;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import name.tomflucke.components.ImageComponent;
import name.tomflucke.virtualdesktop.map.RPGMap.Layer;

class LayerRenderer implements TableCellRenderer
{
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
	        boolean isSelected, boolean hasFocus, int row, int column)
	{
		Layer l = (Layer) table.getValueAt(row, column);
		
		Dimension prefSize = l.getMap().getSize();
		Point loc = l.getPosition();
		Dimension d = l.getSize();
		
		Rectangle cellSize = table.getCellRect(row, column, false);
		double scale = Math.min(cellSize.width, cellSize.height)
		        / Math.max(prefSize.width, prefSize.height);
		int w = (int) (prefSize.width * scale), h = (int) (prefSize.height * scale);
		BufferedImage result = new BufferedImage(w, h,
		        BufferedImage.TYPE_INT_ARGB);
		int x = (int) (loc.x * scale), y = (int) (loc.y * scale), tW = (int) (d.width * scale), tH = (int) (d.height * scale);
		Graphics2D g2d = result.createGraphics();
		g2d.fillRect(0, 0, w, h);
		g2d.drawImage(l.getTile().image, x, y, tW, tH, null);
		g2d.dispose();
		return new ImageComponent(result);
	}
}