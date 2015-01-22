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
import name.tomflucke.virtualdesktop.map.RPGMap;
import name.tomflucke.virtualdesktop.map.RPGMap.Layer;
import name.tomflucke.virtualdesktop.ui.MapDisplay;
import name.tomflucke.virtualdesktop.ui.layerlist.ColumnConstants;

public class HolePunchNetworkPeerTestor implements ColumnConstants {
	private static final InetSocketAddress serverAddress = new InetSocketAddress(
			"63.246.2.164", 18081);

	private static RPGMap map;
	private static MapDisplay mapPanel;
	private static Socket server;

	private static Socket connectToPeer() throws IOException {
		Socket server = new Socket();
		server.setReuseAddress(true);
		server.setTcpNoDelay(true);
		server.connect(serverAddress);
		int port = server.getLocalPort();
		PrintWriter out = new PrintWriter(server.getOutputStream());
		out.println(InetAddress.getLocalHost().getHostAddress() + ":" + port);
		out.flush();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				server.getInputStream()));
		String[] ip_port = in.readLine().split(":");
		InetSocketAddress localPeerSocket = new InetSocketAddress(ip_port[0],
				Integer.valueOf(ip_port[1]));
		ip_port = in.readLine().split(":");
		InetSocketAddress remotePeerSocket = new InetSocketAddress(ip_port[0],
				Integer.valueOf(ip_port[1]));
		server.close();
		return HolePuncher.punchTCP(remotePeerSocket, localPeerSocket, port);
	}

	private static JFrame buildWindow() {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(685, 510);
		return window;
	}

	private static MapDisplay buildMapDisplay(RPGMap map) {
		MapDisplay mapPanel = new MapDisplay();
		mapPanel.setMap(map);
		return mapPanel;
	}

	private static JScrollPane buildMapPane(MapDisplay mapPanel) {
		JPanel mapContainer = new JPanel();
		mapContainer.add(mapPanel);
		JScrollPane mapScrollPane = new JScrollPane(mapContainer);
		return mapScrollPane;
	}

	public static void main(final String... args) throws IOException,
			ClassNotFoundException {
		server = connectToPeer();
		ObjectInputStream in = new ObjectInputStream(server.getInputStream());

		map = (RPGMap) in.readObject();

		JFrame window = buildWindow();

		mapPanel = buildMapDisplay(map);
		window.add(buildMapPane(mapPanel));

		window.setVisible(true);

		while (!server.isClosed()) {
			int id = in.readInt();
			Object obj = in.readObject();
			if (id < 0) {
				mapPanel.revalidate();
			} else {
				Layer l = map.getLayerById(id);
				if (obj == null) {
					// Remove Layer
				} else if (obj instanceof Point) {
					l.setPosition((Point) obj);
				} else if (obj instanceof Dimension) {
					l.setSize((Dimension) obj);
				} else if (obj instanceof Boolean) {
					l.setVisible((Boolean) obj);
				} else if (obj instanceof Integer) {
					l.setZPosition((Integer) obj);
				}
			}
			mapPanel.repaint();
		}
	}
}
