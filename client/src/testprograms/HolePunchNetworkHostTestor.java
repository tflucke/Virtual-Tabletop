package testprograms;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
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
import name.tomflucke.network.HolePuncher;
import name.tomflucke.virtualdesktop.DebugConstants;
import name.tomflucke.virtualdesktop.Debugger;
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

public class HolePunchNetworkHostTestor implements ColumnConstants,
        DebugConstants
{
	private static final InetSocketAddress serverAddress = new InetSocketAddress(
	        "63.246.2.164", 3345);
	// "127.0.0.1", 3345);
	
	private static final byte DEBUG_MODE = VERBOSE;
	private static RPGMap map;
	private static MapDisplay mapPanel;
	private static LayerTable layerList;
	private static ObjectOutputStream client;
	
	private static JFrame buildWindow()
	{
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(685, 510);
		window.getContentPane().setLayout(
		        new TableLayout(new double[][] {
		                { TableLayout.FILL, TableLayout.PREFERRED },
		                { TableLayout.PREFERRED, TableLayout.PREFERRED,
		                        TableLayout.FILL, TableLayout.FILL } }));
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
	
	private static Socket connectToPeer() throws IOException
	{
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Initalizing socket.");
		Socket server = new Socket();
		server.setReuseAddress(true);
		server.setTcpNoDelay(true);
		//server.bind(new InetSocketAddress("127.0.0.1", 16244));
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Connecting to server.");
		server.connect(serverAddress);
		int port = server.getLocalPort();
		System.out.println(port);
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Creating output stream.");
		PrintWriter out = new PrintWriter(server.getOutputStream());
		Debugger
		        .printIfDebug(DEBUG_MODE, VERBOSE, "Printing private IP.");
		out.println(InetAddress.getLocalHost().getHostAddress() + ":" + port);
		out.flush();
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Creating input stream.");
		BufferedReader in = new BufferedReader(new InputStreamReader(
		        server.getInputStream()));
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Reading remote private IP.");
		String[] ip_port = in.readLine().split(":");
		InetSocketAddress localPeerSocket = new InetSocketAddress(ip_port[0],
		        Integer.valueOf(ip_port[1]));
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Reading remote public IP.");
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Peer private IP: " + localPeerSocket);
		ip_port = in.readLine().split(":");
		InetSocketAddress remotePeerSocket = new InetSocketAddress(ip_port[0],
		        Integer.valueOf(ip_port[1]));
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Peer public IP: " + remotePeerSocket);
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Closing server connection.");
		server.close();
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Punching hole.");
		return HolePuncher.punchTCP(remotePeerSocket, localPeerSocket, port);
	}
	
	private static MapListener buildMapListener()
	{
		return new MapListener()
		{
			private void updateLayer(int id, Object obj)
			{
				try
				{
					client.writeInt(id);
					client.writeObject(obj);
				}
				catch (IOException e)
				{
					e.printStackTrace();
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
				updateLayer(changedLayer.getLayerId(),
				        changedLayer.getPosition());
			}
			
			@Override
			public void layerResized(Layer changedLayer)
			{
				updateLayer(changedLayer.getLayerId(), changedLayer.getSize());
			}
			
			@Override
			public void layerReordered(Layer changedLayer)
			{
				updateLayer(changedLayer.getLayerId(),
				        Integer.valueOf(changedLayer.getZPosition()));
			}
			
			@Override
			public void layerRenamed(Layer changedLayer)
			{
			}
			
			@Override
			public void layerVisibilitySet(Layer changedLayer)
			{
				updateLayer(changedLayer.getLayerId(),
				        Boolean.valueOf(changedLayer.isVisible()));
			}
			
		};
	}
	
	public static void main(final String... args) throws IOException
	{
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Creating Window.");
		JFrame window = buildWindow();
		window.setJMenuBar(buildMenuBar());
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Creating map.");
		map = new RPGMap(new Dimension(6, 6));
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Adding Grass tile.");
		map.addLayer(Tile.getTile("Grass"), new Point(1, 2),
		        new Dimension(1, 1));
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Adding Water tile.");
		map.addLayer(Tile.getTile("Water"), new Point(1, 2),
		        new Dimension(2, 2));
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Creating map display.");
		mapPanel = buildMapEditor(map);
		window.add(buildMapPane(mapPanel), "0, 0, 0, 3");
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Creating tile list.");
		JList<Tile> tileList = buildTileList(mapPanel);
		window.add(tileList, "1, 2");
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Creating tile group drop down list.");
		window.add(buildTileGroupSelector(tileList), "1, 1");
		
		Debugger
		        .printIfDebug(DEBUG_MODE, VERBOSE, "Creating layer list.");
		layerList = buildLayerList(map);
		window.add(layerList, "1, 3");
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Displaying window.");
		window.setVisible(true);
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Connecting to peer and creating output stream.");
		client = new ObjectOutputStream(connectToPeer().getOutputStream());
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Sending map info.");
		client.writeObject(map);
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Creating map listener.");
		map.addMapListener(buildMapListener());
	}
}
