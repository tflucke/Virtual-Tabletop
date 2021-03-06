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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import name.tomflucke.virtualdesktop.map.RPGMap;
import name.tomflucke.virtualdesktop.map.RPGMap.Layer;
import name.tomflucke.virtualdesktop.map.Tile;
import name.tomflucke.virtualdesktop.map.RPGMap.MapListener;
import name.tomflucke.virtualdesktop.ui.MapDisplay;
import name.tomflucke.virtualdesktop.ui.MapEditor;
import name.tomflucke.virtualdesktop.ui.TileListRenderer;
import name.tomflucke.virtualdesktop.ui.layerlist.ColumnConstants;
import name.tomflucke.virtualdesktop.ui.layerlist.ColumnInfo;
import name.tomflucke.virtualdesktop.ui.layerlist.LayerTable;

public class NetworkHostTestor implements ColumnConstants
{
	private static RPGMap map;
	private static MapDisplay mapPanel;
	private static LayerTable layerList;
	private static List<ObjectOutputStream> clients;
	
	private static JFrame buildWindow()
	{
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(685, 510);
		window.getContentPane().setLayout(new TableLayout(new double[][]
			{
				{ TableLayout.FILL, TableLayout.PREFERRED },
		        { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.FILL }
			}
		));
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
					ObjectOutputStream oos = new ObjectOutputStream(
					        new FileOutputStream(f));
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
					ObjectInputStream ois = new ObjectInputStream(
					        new FileInputStream("/home/tom/testMap.map"));
					map = (RPGMap) ois.readObject();
					ois.close();
					mapPanel.setMap(map);
					layerList.setMap(map);
					System.out.println(map.getLayers().length);
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
	        final JList<Tile> tileList)
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
	
	private static MapListener buildMapListener()
	{
		return new MapListener()
		{
			private void updateLayer(int id, Object obj)
			{
				for (ObjectOutputStream out : clients)
				{
                    try
                    {
		                out.writeInt(id);
		                out.writeObject(obj);
                    }
                    catch (IOException e)
                    {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
                    }
				}
			}
			
			@Override
            public void layerAdded(Layer newLayer)
            {
	            updateLayer(-1, newLayer);
            }

			@Override
            public void layerRemoved(Layer removedLayer)
            {
	            updateLayer(removedLayer.getLayerId(), null);
            }

			@Override
            public void layerMoved(Layer changedLayer)
            {
	            updateLayer(changedLayer.getLayerId(), changedLayer.getPosition());
            }

			@Override
            public void layerResized(Layer changedLayer)
            {
	            updateLayer(changedLayer.getLayerId(), changedLayer.getSize());
            }

			@Override
            public void layerReordered(Layer changedLayer)
            {
	            updateLayer(changedLayer.getLayerId(), Integer.valueOf(changedLayer.getZPosition()));
            }

			@Override
            public void layerRenamed(Layer changedLayer)
            {}

			@Override
            public void layerVisibilitySet(Layer changedLayer)
            {
	            updateLayer(changedLayer.getLayerId(), Boolean.valueOf(changedLayer.isVisible()));
            }
			
		};
	}
	
	public static void main(final String... args)
	{
		JFrame window = buildWindow();
		window.setJMenuBar(buildMenuBar());
		
		map = new RPGMap(new Dimension(6, 6));
		map.addLayer(Tile.getTile("Grass"), new Point(1, 2),
		        new Dimension(1, 1));
		map.addLayer(Tile.getTile("Water"), new Point(1, 2),
		        new Dimension(2, 2));
		
		mapPanel = buildMapEditor(map);
		window.add(buildMapPane(mapPanel), "0, 0, 0, 3");
		
		JList<Tile> tileList = buildTileList(mapPanel);
		window.add(tileList, "1, 2");
		
		window.add(buildTileGroupSelector(tileList), "1, 1");
		
		layerList = buildLayerList(map);
		window.add(layerList, "1, 3");
		
		window.setVisible(true);
		new ServerThread().start();
		
		map.addMapListener(buildMapListener());
	}
	
	private static class ServerThread extends Thread
	{
		private ServerSocket serverSocket;
		
		{
			try
            {
	            serverSocket = new ServerSocket(18081);
            }
            catch (IOException e)
            {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
			clients = new ArrayList<>();
		}
		
		@Override
		public void run()
		{
			while (!isInterrupted())
			{
				try
                {
	                Socket client = serverSocket.accept();
	                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
	                clients.add(out);
	                out.writeObject(map);
	                out.flush();
                }
                catch (IOException e)
                {
	                e.printStackTrace();
                }
			}
			try
            {
	            serverSocket.close();
            }
            catch (IOException e)
            {
	            e.printStackTrace();
            }
		}
	}
}
