package name.tomflucke.virtualdesktop.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import name.tomflucke.components.TiledImageComponent;
import name.tomflucke.dragNdrop.CollectiveComponentMover;
import name.tomflucke.dragNdrop.ComponentResizer;
import name.tomflucke.virtualdesktop.map.RPGMap;
import name.tomflucke.virtualdesktop.map.RPGMap.Layer;
import name.tomflucke.virtualdesktop.map.Tile;

/*
 * TODO: Background layer
 */
public class MapEditor extends MapDisplay
{
	private static final long serialVersionUID = 6438969590769838254L;
	
	private final CollectiveComponentMover dragger;
	private final ComponentResizer resizer;
	
	private Color highlightColor;
	private int borderSize;
	private TiledImageComponent background;
	
	{
		dragger = new CollectiveComponentMover();
		dragger.setDragInsets(new Insets(5, 5, 5, 5));
		resizer = new ComponentResizer();
		Dimension snap = new Dimension(dpi, dpi);
		dragger.setSnapSize(snap);
		resizer.setSnapSize(snap);
		
		ChangeListener cl = new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent ce)
			{
				JComponent comp = (JComponent) ce.getSource();
				Rectangle bounds = comp.getBounds();
				bounds.x = bounds.x / dpi;
				bounds.y = bounds.y / dpi;
				bounds.width = bounds.width / dpi;
				bounds.height = bounds.height / dpi;
				currentMap.getLayerById(getLayerId(comp)).setPosition(bounds);
			}
		};
		dragger.addChangeListener(cl);
		resizer.addChangeListener(cl);
	}
	
	{
		highlightColor = new Color(0, 0, 255, 255 / 4);
		borderSize = 4;
	}
	
	{
		try
		{
			background = new TiledImageComponent(ImageIO.read(MapEditor.class
			        .getResource("/config/images/background.png")));
			background.setSize(getWidth(), getHeight());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addLayer(Point p, Tile t)
	{
		currentMap.addLayer(t, new Point(p.x / dpi, p.y / dpi), new Dimension(
		        1, 1));
	}
	
	public void selectLayer(int layerId)
	{
		dragger.clearSelected();
		dragger.select(layers.get(layerId));
	}
	
	public void setClickSelectEnabled(boolean flag)
	{
		dragger.setSelectKeys(flag ? MouseEvent.BUTTON1 : 0);
	}
	
	@Override
	public void setMap(RPGMap newMap)
	{
		super.setMap(newMap);
		add(background);
	}
	
	@Override
	public void layerAdded(Layer newLayer)
	{
		super.layerAdded(newLayer);
		JComponent comp = layers.get(newLayer.getLayerId());
		dragger.registerComponent(comp);
		resizer.registerComponent(comp);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		/*
		 * TODO: Move to on resize
		 */
		if (currentMap != null)
		{
			background.setSize(getWidth(), getHeight());
			setComponentZOrder(background, getComponentCount() - 1);
		}
		
		for (Component comp : dragger.getSelected())
		{
			g.setColor(highlightColor);
			Rectangle rect = comp.getBounds();
			rect.add(rect.x - borderSize, rect.y - borderSize);
			rect.setSize(rect.width + borderSize, rect.height + borderSize);
			((Graphics2D) g).setStroke(new BasicStroke(borderSize));
			((Graphics2D) g).draw(rect);
		}
		// if (highlightRectangle != null)
		// {
		// Graphics2D g2d = (Graphics2D) g;
		// g2d.setColor(highlightColor);
		// g2d.fill(highlightRectangle);
		// }
	}
}
