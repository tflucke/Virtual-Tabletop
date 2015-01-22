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

public class LayerList extends JTable implements ColumnConstants
{
	private static final long serialVersionUID = -8604416026115943743L;
	
	private final ButtonRenderer hideButtonRenderer;

	{
		Object visible = new ImageIcon(
		        LayerList.class.getResource("/config/images/visible.png"));
		Object invisible = new ImageIcon(
		        LayerList.class.getResource("/config/images/invisible.png"));
		
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
	
	public LayerList(ColumnInfo... columns)
	{
		setModel(new LayerTableModel(columns));
		TableColumnModel cModel = getColumnModel();
		for (int i = 0; i < columns.length; i++)
		{
			TableColumn col = cModel.getColumn(i);
			if (columns[i].width > 0)
			{
				col.setPreferredWidth(columns[i].width);
				switch (columns[i].type)
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
	}
	
	public void setMap(RPGMap newMap)
	{
		((LayerTableModel) getModel()).setMap(newMap);
	}
}