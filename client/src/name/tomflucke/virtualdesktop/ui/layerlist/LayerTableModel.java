package name.tomflucke.virtualdesktop.ui.layerlist;

import javax.swing.table.AbstractTableModel;

import name.tomflucke.dragNdrop.Reorderable;
import name.tomflucke.virtualdesktop.map.RPGMap;
import name.tomflucke.virtualdesktop.map.RPGMap.Layer;

/**
 * Serves as the middle-object between a JTable and an RPGMap.
 * 
 * Most calls to a method will translate into a query of the set RPGMap.
 * 
 * Each column is described as a ColumnInfo object.
 * 
 * @author tom
 * @version 1.0.0
 */
class LayerTableModel extends AbstractTableModel implements Reorderable,
        RPGMap.MapListener, ColumnConstants
{
	private static final long serialVersionUID = 6416082983398009739L;
	
	/**
	 * All of the descriptions of the columns and how they should behave.
	 */
	private final ColumnInfo[] columns;
	/**
	 * The map to which queries will be passed.
	 */
	private RPGMap currentMap;
	
	/**
	 * Initializes the LayerTableModel
	 * 
	 * @param columns Descriptions of how each column should be rendered.
	 */
	LayerTableModel(ColumnInfo... columns)
	{
		this.columns = columns;
	}
	
	/**
	 * Sets the map which will be queried.
	 * 
	 * @param newMap The map to be queried.
	 */
	void setMap(RPGMap newMap)
	{
		if (currentMap != null)
		{
			currentMap.removeMapeListener(this);
		}
		currentMap = newMap;
		newMap.addMapListener(this);
		fireTableDataChanged();
	}
	
	@Override
	public void layerAdded(Layer newLayer)
	{
		fireTableDataChanged();
	}
	
	@Override
	public void layerRemoved(Layer removedLayer)
	{
		fireTableDataChanged();
	}
	
	@Override
	public void layerMoved(Layer changedLayer)
	{
		fireTableDataChanged();
	}
	
	@Override
	public void layerResized(Layer changedLayer)
	{
		fireTableDataChanged();
	}
	
	@Override
	public void layerReordered(Layer changedLayer)
	{
		fireTableDataChanged();
	}
	
	@Override
	public void layerRenamed(Layer changedLayer)
	{
		fireTableDataChanged();
	}
	
	@Override
	public void layerVisibilitySet(Layer changedLayer)
	{
		fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount()
	{
		return columns.length;
	}
	
	@Override
	public int getRowCount()
	{
		return currentMap != null ? currentMap.getLayers().length : 0;
	}
	
	@Override
	public Object getValueAt(int row, int col)
	{
		if (currentMap == null || col >= columns.length
		        || row >= currentMap.getLayers().length)
		{
			return null;
		}
		Layer l = currentMap.getLayers()[row];
		switch (columns[col].type)
		{
			case PREVIEW:
				return l;
			case LAYER_NAME:
				return l.getName();
			case HIDE_BUTTON:
				return l.isVisible();
		}
		return null;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col)
	{
		if (currentMap == null || col >= columns.length
		        || row >= currentMap.getLayers().length)
		{
			return;
		}
		Layer l = currentMap.getLayers()[row];
		switch (columns[col].type)
		{
			case LAYER_NAME:
				l.setName((String) value);
				break;
			case HIDE_BUTTON:
				l.setVisible((boolean) value);
				break;
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return columns[columnIndex].type == HIDE_BUTTON;
	}
	
	/**
	 * Changes the order of the layers.
	 * 
	 * @param fromIndex The index of the layer to move.
	 * @param toIndex The layer to which the layer will move.
	 */
	@Override
	public void reorder(int fromIndex, int toIndex)
	{
		currentMap.getLayerByZIndex(fromIndex).setZPosition(toIndex);
	}
}