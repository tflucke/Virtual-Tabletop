package name.tomflucke.virtualdesktop.ui.layerlist;

import javax.swing.table.AbstractTableModel;

import name.tomflucke.dragNdrop.Reorderable;
import name.tomflucke.virtualdesktop.map.RPGMap;
import name.tomflucke.virtualdesktop.map.RPGMap.Layer;

class LayerTableModel extends AbstractTableModel implements Reorderable,
        RPGMap.MapListener, ColumnConstants
{
	private static final long serialVersionUID = 6416082983398009739L;
	
	private final ColumnInfo[] columns;
	private RPGMap currentMap;
	
	LayerTableModel(ColumnInfo... columns)
	{
		this.columns = columns;
	}
	
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
	
	@Override
	public void reorder(int fromIndex, int toIndex)
	{
		currentMap.getLayerByZIndex(fromIndex).setZPosition(toIndex);
	}
}