package name.tomflucke.virtualdesktop.ui.layerlist;

import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import name.tomflucke.components.table.BooleanButtonRenderer;
import name.tomflucke.components.table.ButtonRenderer;
import name.tomflucke.dragNdrop.TableRowTransferHandler;
import name.tomflucke.virtualdesktop.map.RPGMap;

/**
 * A component which displays the layers in a map as a series of rows.
 * 
 * While this class could have instead been replaced by a normal JTable and
 * public methods, this allows for everything to be pre-configured upon object
 * creation.
 * 
 * @author tom
 * @version 1.0.0
 * @see LayerTableModel
 */
public class LayerTable extends JTable implements ColumnConstants
{
	private static final long serialVersionUID = -8604416026115943743L;
	
	private final ButtonRenderer hideButtonRenderer;
	
	{
		Object visible = new ImageIcon(
		        LayerTable.class.getResource("/config/images/visible.png"));
		Object invisible = new ImageIcon(
		        LayerTable.class.getResource("/config/images/invisible.png"));
		
		hideButtonRenderer = new BooleanButtonRenderer(visible, invisible);
	}
	
	{
		setShowVerticalLines(false);
		setRowHeight(78);
	}
	
	{
		setDropMode(DropMode.INSERT_ROWS);
		setTransferHandler(new TableRowTransferHandler(this));
		setDragEnabled(true);
	}
	
	/**
	 * Initializes the table.
	 * 
	 * Each column is described as a ColumnInfo object.
	 * 
	 * @param columns The details as to how and what each column displays.
	 * @see ColumnInfo
	 * @see LayerTableModel
	 */
	public LayerTable(ColumnInfo... columns)
	{
		setModel(new LayerTableModel(columns));
		TableColumnModel cModel = getColumnModel();
		for (int colIndex = 0; colIndex < columns.length; colIndex++)
		{
			TableColumn col = cModel.getColumn(colIndex);
			if (columns[colIndex].width > 0)
			{
				col.setPreferredWidth(columns[colIndex].width);
			}
			switch (columns[colIndex].type)
			{
				case PREVIEW:
					col.setCellRenderer(new LayerRenderer());
					break;
				case HIDE_BUTTON:
					col.setCellRenderer(hideButtonRenderer);
					col.setCellEditor(hideButtonRenderer);
					break;
			}
		}
	}
	
	/**
	 * A wrapper method which sets the map for the TableModel.
	 * 
	 * @param newMap The map whose layers will be displayed.
	 * @see LayerTableModel
	 */
	public void setMap(RPGMap newMap)
	{
		((LayerTableModel) getModel()).setMap(newMap);
	}
}