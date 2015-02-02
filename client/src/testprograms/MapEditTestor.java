package testprograms;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import name.tomflucke.components.AutoCompleteComboBox;
import name.tomflucke.dragNdrop.DragDropListener;
import name.tomflucke.layouts.TableLayout;
import name.tomflucke.virtualdesktop.DebugConstants;
import name.tomflucke.virtualdesktop.map.RPGMap;
import name.tomflucke.virtualdesktop.map.Tile;
import name.tomflucke.virtualdesktop.ui.MapDisplay;
import name.tomflucke.virtualdesktop.ui.MapEditor;
import name.tomflucke.virtualdesktop.ui.TileListRenderer;
import name.tomflucke.virtualdesktop.ui.layerlist.ColumnConstants;
import name.tomflucke.virtualdesktop.ui.layerlist.ColumnInfo;
import name.tomflucke.virtualdesktop.ui.layerlist.LayerTable;

public class MapEditTestor implements DebugConstants, ColumnConstants
{
	private static byte DEBUG_MODE = VERBOSE;
	private static RPGMap map;
	private static MapDisplay mapPanel;
	private static LayerTable layerList; 
	
	private static JFrame buildWindow()
	{
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(685, 510);
		window.getContentPane().setLayout(
		        new TableLayout(new double[][] {
		                { TableLayout.FILL, TableLayout.PREFERRED },
		                { TableLayout.PREFERRED, TableLayout.FILL,
		                        TableLayout.FILL } }));
		return window;
	}
	
	private static JMenuBar buildMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem saveButton = new JMenuItem("Save");
		fileMenu.add(saveButton);
		saveButton.addActionListener(new ActionListener()
		{
			@Override
            public void actionPerformed(ActionEvent ae)
            {
	            try
                {
	            	File f = new File("/home/tom/testMap.map");
	            	f.delete();
	                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
	                oos.writeObject(map);
	                oos.close();
                }
                catch (IOException e)
                {
	                e.printStackTrace();
                }
            }
		});
		JMenuItem loadButton = new JMenuItem("Load");
		fileMenu.add(loadButton);
		loadButton.addActionListener(new ActionListener()
		{
			@Override
            public void actionPerformed(ActionEvent ae)
            {
	            try
                {
	                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/home/tom/testMap.map"));
	                map = (RPGMap) ois.readObject();
	                ois.close();
	                mapPanel.setMap(map);
	                layerList.setMap(map);
                }
                catch (IOException | ClassNotFoundException e)
                {
	                e.printStackTrace();
                }
            }
		});
		return menuBar;
	}
	
	private static MapDisplay buildMapEditor(RPGMap map)
	{
		MapDisplay mapPanel = new MapEditor();
		mapPanel.setMap(map);
		return mapPanel;
	}
	
	private static JScrollPane buildMapPane(MapDisplay mapPanel)
	{
		JPanel mapContainer = new JPanel();
		mapContainer.add(mapPanel);
		JScrollPane mapScrollPane = new JScrollPane(mapContainer);
		return mapScrollPane;
	}
	
	private static JList<Tile> buildTileList(MapDisplay mapPanel)
	{
		JList<Tile> tileList = new JList<Tile>();
		tileList.setCellRenderer(new TileListRenderer());
		DragDropListener<Tile> tileDragger = new DragDropListener<Tile>()
		{
			@Override
			public Tile drag(Component comp, Point p)
			{
				return (Tile) ((JList<?>) comp).getSelectedValue();
			}
			
			@Override
			public void drop(Component comp, Point p, Tile data)
			{
				((MapEditor) comp).addLayer(p, data);
			}
			
			@Override
			public void endDrag()
			{
			}
		};
		tileDragger.register(DragDropListener.DRAG_ZONE, tileList);
		tileDragger.register(DragDropListener.DROP_ZONE, mapPanel);
		return tileList;
	}
	
	private static AutoCompleteComboBox buildTileGroupSelector(
	        JList<Tile> tileList)
	{
		AutoCompleteComboBox tileGroupSelector = new AutoCompleteComboBox(
		        Arrays.asList(Tile.getGroupNames()));
		tileGroupSelector.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent ie)
			{
				tileList.setListData(Tile.getGroup((String) ie.getItem()));
			}
			
		});
		tileList.setListData(Tile.getGroup((String) tileGroupSelector
		        .getSelectedItem()));
		return tileGroupSelector;
	}
	
	private static LayerTable buildLayerList(RPGMap map)
	{
		LayerTable layerList = new LayerTable(new ColumnInfo[] {
		        new ColumnInfo(40, HIDE_BUTTON), new ColumnInfo(75, PREVIEW),
		        new ColumnInfo(70, LAYER_NAME) });
		layerList.setMap(map);
		return layerList;
	}
	
	public static void main(final String... args)
	{
		DebugConstants.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Initializing main window.");
		JFrame window = buildWindow();
		window.setJMenuBar(buildMenuBar());
		
		map = new RPGMap(new Dimension(6, 6));
		map.addLayer(Tile.getTile("Grass"), new Point(1, 2),
		        new Dimension(1, 1));
		map.addLayer(Tile.getTile("Water"), new Point(1, 2),
		        new Dimension(2, 2));
		
		mapPanel = buildMapEditor(map);
		window.add(buildMapPane(mapPanel), "0, 0, 0, 2");
		
		JList<Tile> tileList = buildTileList(mapPanel);
		window.add(tileList, "1, 1");
		
		window.add(buildTileGroupSelector(tileList), "1, 0");
		
		layerList = buildLayerList(map);
		window.add(layerList, "1, 2");
		
		DebugConstants.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Displaying main window.");
		window.setVisible(true);
	}
}
