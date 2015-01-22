package testprograms;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import name.tomflucke.virtualdesktop.map.RPGMap;
import name.tomflucke.virtualdesktop.map.RPGMap.Layer;
import name.tomflucke.virtualdesktop.ui.MapDisplay;
import name.tomflucke.virtualdesktop.ui.layerlist.ColumnConstants;

public class NetworkPeerTestor implements ColumnConstants
{
	private static final int defaultPort = 18081;
	
	private static RPGMap map;
	private static MapDisplay mapPanel;
	private static Socket server;
	
	private static InetSocketAddress stringToINet(String address)
	{
		String[] ip = address.split(":");
		InetSocketAddress serverAddress = new InetSocketAddress(ip[0],
		        ip.length > 1 ? Integer.valueOf(ip[1]) : defaultPort);
		return serverAddress;
	}
	
	private static Socket connectToServer(InetSocketAddress serverAddress)
	        throws IOException
	{
		Socket server = new Socket();
		server.setReuseAddress(true);
		server.setTcpNoDelay(true);
		server.connect(serverAddress);
		return server;
	}
	
	private static JFrame buildWindow()
	{
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(685, 510);
		return window;
	}
	
	private static MapDisplay buildMapDisplay(RPGMap map)
	{
		MapDisplay mapPanel = new MapDisplay();
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
	public static void main(final String... args) throws IOException, ClassNotFoundException
	{

		String serverString = JOptionPane.showInputDialog("Server IP:",
		        "127.0.0.1");
		InetSocketAddress serverAddress = stringToINet(serverString);
		server = connectToServer(serverAddress);
		ObjectInputStream in = new ObjectInputStream(server.getInputStream());
		
		map = (RPGMap) in.readObject();
		
		JFrame window = buildWindow();
		
		mapPanel = buildMapDisplay(map);
		window.add(buildMapPane(mapPanel));
		
		window.setVisible(true);
		
		while (!server.isClosed())
		{
			int id = in.readInt();
			Object obj = in.readObject();
			if (id < 0)
			{
				mapPanel.revalidate();
			}
			else
			{
				Layer l = map.getLayerById(id);
				if (obj == null)
				{
					//Remove Layer
				}
				else if (obj instanceof Point)
				{
					l.setPosition((Point) obj); 
				}
				else if (obj instanceof Dimension)
				{
					l.setSize((Dimension) obj); 
				}
				else if (obj instanceof Boolean)
				{
					l.setVisible((Boolean) obj); 
				}
				else if (obj instanceof Integer)
				{
					l.setZPosition((Integer) obj); 
				}
			}
			mapPanel.repaint();
		}
	} 
}
