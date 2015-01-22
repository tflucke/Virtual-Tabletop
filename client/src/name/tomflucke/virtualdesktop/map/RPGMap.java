package name.tomflucke.virtualdesktop.map;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Contains all the information necessary to store and draw a map.
 * Note, all dimensions and points are measured in "Map Units", not pixels.
 * A single map unit does not necessarily correspond to any amount of pixels although, it will usually be about 98px or 1 inch.
 * The reason "Map Units" are used instead of some other unit is because this allows for variability in the display, such as a zoom feature or a hexagonal grid.
 * Additionally, this makes computation easier, since instead of calculating whether or not two layers overlap anywhere over 98px, you only have to check for if they share that one locus.
 * 
 * @author tom
 * @version 1.0.1
 *
 */
public class RPGMap implements Serializable
{
	private static final long serialVersionUID = -8654411772344018235L;
	
	/**
	 * An interface which the map can use to notify other object when something changes.
	 * 
	 * @author tom
	 *
	 */
	public static interface MapListener
	{
		public void layerAdded(Layer newLayer);
		public void layerRemoved(Layer removedLayer);
		public void layerMoved(Layer changedLayer);
		public void layerResized(Layer changedLayer);
		public void layerReordered(Layer changedLayer);
		public void layerRenamed(Layer changedLayer);
		public void layerVisibilitySet(Layer changedLayer);
	}
	
	/**
	 * The next unique id to assign to a new layer.
	 */
	private int nextLayerId;
	/**
	 * All of the objects listening to changes on this map.
	 */
	private transient Collection<MapListener> mapListeners;
	/**
	 * A list of the current layers associated with this map.
	 */
	private final List<Layer> layers;
	/**
	 * The current size of the map
	 * All sizes are measured in "Map Units".
	 */
	private Dimension size;

	{
		nextLayerId = 0;
		mapListeners = new HashSet<>();
		layers = new ArrayList<>();
	}
	
	/**
	 * Creates a new map with the given size.
	 * All sizes are measured in "Map Units".
	 * 
	 * @param size The default size for the new map.
	 */
	public RPGMap(Dimension size)
	{
		this.size = size;
	}

	/**
	 * Restores the object information and reinitializes listener collections.
	 * 
	 * @param inputStream The source of information
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    private void readObject(ObjectInputStream inputStream)
            throws IOException, ClassNotFoundException
    {
    	inputStream.defaultReadObject();
		mapListeners = new HashSet<>();
    } 
    
	/**
	 * Creates a new layer associated with this map.
	 * All sizes are measured in "Map Units".
	 * 
	 * @param tile The tile which the new layer will display
	 * @param pos The position of the new layer
	 * @param size The size of the new layer
	 */
	public void addLayer(Tile tile, Point pos, Dimension size)
	{
		Layer newLayer = new Layer(tile, pos, size);
		for (MapListener ml : mapListeners)
		{
			ml.layerAdded(newLayer);
		}
	}

	/**
	 * Gets all the layers associated with this map.
	 * 
	 * @return An array of all the layers in this map
	 */
	public Layer[] getLayers()
	{
		return layers.toArray(new Layer[0]);
	}

	/**
	 * Finds and returns a layer whose id matches the given 
	 * 
	 * @param layerId The id to compare against
	 * @return The layer with a matching id, or null if none exists.
	 */
	public Layer getLayerById(int layerId)
	{
		for (Layer l : layers)
		{
			if (l.layerId == layerId)
			{
				return l;
			}
		}
		return null;
	}

	/**
	 * Finds and returns the z-th layer
	 * 
	 * @param zIndex The distance from the top of the layer stack
	 * @return The layer that is the given distance from the top of the stack
	 */
	public Layer getLayerByZIndex(int zIndex)
	{
		return layers.get(zIndex);
	}
	
	/**
	 * Gets the current size of the map. 
	 * All sizes are measured in "Map Units".
	 * 
	 * @return The size of the map
	 */
	public Dimension getSize()
	{
		return size;
	}
	
	/**
	 * Adds a new MapListener.
	 * MapListeners, once added, will be notified of all events that happen in this map.
	 * 
	 * @param listener The listener to be added
	 * @return true if the listener was successfully added, false otherwise
	 */
	public boolean addMapListener(MapListener listener)
	{
		return mapListeners.add(listener);
//		for (MapDisplayListener mdl : listeners)
//		{
//			mdl.registered(map);
//		}
	}

	/**
	 * Removes a MapListener.
	 * MapListeners, once removed, won't be notified of any events that happen in this map.
	 * 
	 * @param listener The listener to be removed
	 * @return true if the listener was successfully removed, false otherwise
	 */
	public boolean removeMapeListener(MapListener listener)
	{
		return mapListeners.remove(listener);
//		for (MapDisplayListener mdl : listeners)
//		{
//			mdl.unregistered(map);
//		}
	}
	
	/**
	 * Contains all the information necessary to draw a given tile.
	 * 
	 * @author tom
	 *
	 */
	public class Layer implements Serializable
	{
		private static final long serialVersionUID = 6345023334421867820L;
		
		/**
		 * Distinguishes this layer from any other layers.
		 */
		private final int layerId;
		/**
		 * The name of this layer.
		 * Primarily used for identifying the layer in the editor.  Servers little functional purpose.
		 */
		private String name;
		/**
		 * Represents the current distance from the top of the map display.
		 * This member is updated such that layers.get(currentIndex) == this is always true.
		 */
		private int currentIndex;
		/**
		 * The tile which is painted on the layer.
		 */
		private transient Tile tile;
		/**
		 * The name of the tile to be painted.
		 * This is used to recover tile information after serializing the layer.
		 */
		/* 
		 * TODO:
		 * Remove this field and print the tile name in the writeObject method,
		 * and recover it in the readObject method.
		 */
		private final String tileName;
		/**
		 * The position of the layer.
		 * All sizes are measured in "Map Units".
		 */
		private Point position;
		/**
		 * The size of the layer.
		 * All sizes are measured in "Map Units".
		 */
		private Dimension size;
		/**
		 * The current visibility of the layer.
		 */
		private boolean visible;
		
		{
			layerId = nextLayerId++;
			name = "Layer " + nextLayerId;
			visible = true;
			currentIndex = 0;
			synchronized (layers)
			{
				layers.add(0, this);
				for (int i = 1; i < layers.size(); i++)
				{
					layers.get(i).currentIndex++;
				}	
			}
		}
		
		/**
		 * Creates a new Layer which matches the given descriptions.
		 * All sizes are measured in "Map Units".
		 * 
		 * @param t The tile which is painted on this layer
		 * @param p The initial position of this layer
		 * @param d The initial size of this layer
		 */
		protected Layer(Tile t, Point p, Dimension d)
		{
			tile = t;
			tileName = t.name;
			position = p;
			size = d;
		}

		/**
		 * Restores the object information and recollects the Tile.
		 * Tiles are not saved during serialization to prevent sync errors and save data.
		 * 
		 * @param inputStream The source of information
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
	    private void readObject(ObjectInputStream inputStream)
	            throws IOException, ClassNotFoundException
	    {
	    	inputStream.defaultReadObject();
	    	tile = Tile.getTile(tileName);
	    	if (layers != null)
	    	{
				synchronized (layers)
				{
					layers.add(currentIndex, this);
					for (int i = currentIndex+1; i < layers.size(); i++)
					{
						layers.get(i).currentIndex++;
					}	
				}
				for (MapListener ml : mapListeners)
				{
					ml.layerAdded(this);
				}
	    	}
	    }
	    
		/**
		 * Changes the current position and size of this layer.
		 * All sizes are measured in "Map Units"
		 * 
		 * @param bounds The new position and size of this layer
		 */
		public void setPosition(Point pos)
		{
			position = pos;
	    	for (MapListener ml : mapListeners)
	    	{
	    		ml.layerMoved(this);
	    	}
		}

		/**
		 * Changes the current position and size of this layer.
		 * All sizes are measured in "Map Units"
		 * 
		 * @param bounds The new position and size of this layer
		 */
		public void setPosition(Rectangle bounds)
		{
			position = bounds.getLocation();
			size = bounds.getSize();
	    	for (MapListener ml : mapListeners)
	    	{
	    		ml.layerMoved(this);
	    		ml.layerResized(this);
	    	}
		}

		/**
		 * Changes the current size of this layer.
		 * All sizes are measured in "Map Units"
		 * 
		 * @param size The new size of this layer
		 */
		public void setSize(Dimension size)
		{
			this.size = size;
	    	for (MapListener ml : mapListeners)
	    	{
	    		ml.layerResized(this);
	    	}
		}

		/**
		 * Sets the name for this layer.
		 * 
		 * @param newName The new name
		 */
		public void setName(String newName)
		{
			name = newName;
	    	for (MapListener ml : mapListeners)
	    	{
	    		ml.layerRenamed(this);
	    	}
		}

		/**
		 * Sets the distance from the top of the layer stack.
		 * For example, with layers A, B, & C, setting C to 0 would reorder the layers C, A, & B.
		 * 
		 * @param index Distance from the top
		 */
		public synchronized void setZPosition(int index)
		{
			if (index > currentIndex)
			{
				for (int i = currentIndex+1; i <= index; i++)
				{
					layers.get(i).currentIndex--;
				}
			}
			else
			{
				for (int i = index; i < currentIndex; i++)
				{
					layers.get(i).currentIndex++;
				}
			}
			currentIndex = index;
			layers.remove(this);
			layers.add(index, this);
	    	for (MapListener ml : mapListeners)
	    	{
	    		ml.layerReordered(this);
	    	}
		}

		/**
		 * Sets the visibility
		 * 
		 * @param flag true if the layer will become visible, false otherwise
		 */
		public void setVisible(boolean flag)
		{
			visible = flag;
	    	for (MapListener ml : mapListeners)
	    	{
	    		ml.layerVisibilitySet(this);
	    	}
		}

		/**
		 * Returns the id of this layer.
		 * 
		 * @return The id of the layer
		 */
		public int getLayerId()
		{
			return layerId;
		}

		/**
		 * Returns the current name of the layer.
		 * 
		 * @return The name of this layer
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * Returns this layer's position in the vertical position of this layer.
		 * For example, a layer with a z-position of 0 will display on top of all the other layers.
		 * 
		 * @return The number of layers between this and the top of the map
		 */
		public int getZPosition()
		{
			return currentIndex;
		}

		/**
		 * Returns the tile which the layer paints.
		 * 
		 * @return The tile used for displaying this layer
		 */
		public Tile getTile()
		{
			return tile;
		}

		/**
		 * Returns the current size of the layer.
		 * All sizes are measured in "Map Units"
		 * 
		 * @return The dimensions of the object
		 */
		public Point getPosition()
		{
			return position;
		}

		/**
		 * Returns the current size of the layer.
		 * All sizes are measured in "Map Units"
		 * 
		 * @return The dimensions of the object
		 */
		public Dimension getSize()
		{
			return size;
		}

		/**
		 * Returns whether or not the layer is currently visible. 
		 * 
		 * @return true if the layer is currently visible, false otherwise
		 */
		public boolean isVisible()
		{
			return visible;
		}
		
		/**
		 * Gets the map with which this layer is associated.
		 * 
		 * @return The map which contains this layer
		 */
		public RPGMap getMap()
		{
			return RPGMap.this;
		}
	}	
}
