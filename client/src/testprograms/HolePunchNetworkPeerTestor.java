package testprograms;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import name.tomflucke.network.HolePuncher;
import name.tomflucke.virtualdesktop.DebugConstants;
import name.tomflucke.virtualdesktop.Debugger;
import name.tomflucke.virtualdesktop.map.RPGMap;
import name.tomflucke.virtualdesktop.map.RPGMap.Layer;
import name.tomflucke.virtualdesktop.ui.MapDisplay;
import name.tomflucke.virtualdesktop.ui.layerlist.ColumnConstants;

public class HolePunchNetworkPeerTestor implements ColumnConstants,
        DebugConstants
{
	private static final InetSocketAddress serverAddress = new InetSocketAddress(
			"63.246.2.164", 3345);
	//		"127.0.0.1", 3345);
	
	private static final byte DEBUG_MODE = VERBOSE;
	private static RPGMap map;
	private static MapDisplay mapPanel;
	private static Socket server;
	
	private static Socket connectToPeer() throws IOException
	{
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Initalizing socket.");
		Socket server = new Socket();
		server.setReuseAddress(true);
		server.setTcpNoDelay(true);
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Connecting to server.");
		server.connect(serverAddress);
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Creating output stream.");
		int port = server.getLocalPort();
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
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Peer private IP: " + localPeerSocket);
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Reading remote public IP.");
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
	
	public static void main(final String... args) throws IOException,
	        ClassNotFoundException
	{
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Connecting to Host.");
		server = connectToPeer();
		Debugger
		        .printIfDebug(DEBUG_MODE, VERBOSE, "Saving input stream.");
		ObjectInputStream in = new ObjectInputStream(server.getInputStream());
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Reading map info.");
		map = (RPGMap) in.readObject();
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Creating Window.");
		JFrame window = buildWindow();
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
		        "Creating map display.");
		mapPanel = buildMapDisplay(map);
		window.add(buildMapPane(mapPanel));
		
		Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Displaying window.");
		window.setVisible(true);
		
		while (!server.isClosed())
		{
			Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
			        "Connection still open.");
			
			Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Read layer id.");
			int id = in.readInt();
			Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
			        "Read layer object.");
			Object obj = in.readObject();
			if (id < 0)
			{
				Debugger.printIfDebug(DEBUG_MODE, VERBOSE,
				        "Layer Added.\nRepainting.");
				mapPanel.revalidate();
			}
			else
			{
				Layer l = map.getLayerById(id);
				if (obj == null)
				{
					Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Layer "
					        + id + " deleted.");
					// Remove Layer
				}
				else if (obj instanceof Point)
				{
					Point pos = (Point) obj;
					Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Layer "
					        + id + " moved to " + pos + ".");
					l.setPosition(pos);
				}
				else if (obj instanceof Dimension)
				{
					Dimension size = (Dimension) obj;
					Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Layer "
					        + id + " resized to +" + size + ".");
					l.setSize(size);
				}
				else if (obj instanceof Boolean)
				{
					boolean visible = (Boolean) obj;
					Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Layer "
					        + id + " visibility set to " + visible + ".");
					l.setVisible(visible);
				}
				else if (obj instanceof Integer)
				{
					int zIndex = (Integer) obj;
					Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Layer "
					        + id + " reordered to " + zIndex + ".");
					l.setZPosition(zIndex);
				}
			}
			Debugger.printIfDebug(DEBUG_MODE, VERBOSE, "Repainting.");
			mapPanel.repaint();
		}
	}
}
