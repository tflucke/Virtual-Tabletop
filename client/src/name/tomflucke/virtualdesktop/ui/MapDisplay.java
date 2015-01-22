package name.tomflucke.virtualdesktop.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import name.tomflucke.virtualdesktop.map.RPGMap;
import name.tomflucke.virtualdesktop.map.RPGMap.Layer;
import name.tomflucke.virtualdesktop.map.Tile;

public class MapDisplay extends JPanel implements RPGMap.MapListener
{
	private static final long serialVersionUID = -1417051982711902331L;
	
	private static class TileComponent extends JComponent
	{
		private static final long serialVersionUID = 4694958303904544079L;
		
		private final Tile tile;
		
		public TileComponent(Tile t)
		{
			tile = t;
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			g.drawImage(tile.image, 0, 0, getWidth(), getHeight(), null);
		}
	}
	
	protected Map<Integer, JComponent> layers;
	protected RPGMap currentMap;
	protected int dpi;
	
	{
		layers = new HashMap<>();
		dpi = 96;
		setLayout(null);
	}
	
	protected int getLayerId(JComponent comp)
	{
		for (Map.Entry<Integer, JComponent> entry : layers.entrySet())
		{
			if (entry.getValue() == comp)
			{
				return entry.getKey();
			}
		}
		return -1;
	}
	
	public void setMap(RPGMap newMap)
	{
		if (currentMap != null)
		{
			currentMap.removeMapeListener(this);
			removeAll();
			layers.clear();
			dpi = 96;
		}
		Dimension s = newMap.getSize();
		setPreferredSize(new Dimension(s.width*dpi, s.height*dpi));
		currentMap = newMap;
		currentMap.addMapListener(this);
		Layer[] layers = currentMap.getLayers();
		for (int i = layers.length - 1; i >= 0; i--)
		{
			layerAdded(layers[i]);
		}
	}

	@Override
    public void layerAdded(Layer newLayer)
    {
		JComponent comp = new TileComponent(newLayer.getTile());
		add(comp);
		layers.put(newLayer.getLayerId(), comp);
		Point p = newLayer.getPosition();
		Dimension d = newLayer.getSize();
		int x = p.x*dpi, y = p.y*dpi, w = d.width*dpi, h = d.height*dpi;
		comp.setBounds(new Rectangle(x, y, w, h));
		setComponentZOrder(comp, 0);
		revalidate();
		repaint();
    }

	@Override
    public void layerRemoved(Layer removedLayer)
    {
		JComponent comp = layers.get(removedLayer.getLayerId());
		remove(comp);
		layers.remove(removedLayer.getLayerId());
		revalidate();
		repaint();
    }

	@Override
    public void layerMoved(Layer changedLayer)
    {
		Point p = changedLayer.getPosition();
		int x = p.x*dpi, y = p.y*dpi;
		layers.get(changedLayer.getLayerId()).setLocation(new Point(x, y));
    }

	@Override
    public void layerResized(Layer changedLayer)
    {
		Dimension d = changedLayer.getSize();
		int w = d.width*dpi, h = d.height*dpi;
		layers.get(changedLayer.getLayerId()).setSize(new Dimension(w, h));
    }

	@Override
    public void layerReordered(Layer changedLayer)
    {
		setComponentZOrder(layers.get(changedLayer.getLayerId()), changedLayer.getZPosition());
		repaint();
    }

	@Override
    public void layerRenamed(Layer changedLayer) {}

	@Override
    public void layerVisibilitySet(Layer changedLayer)
    {
		layers.get(changedLayer.getLayerId()).setVisible(changedLayer.isVisible());
    }
	
}
