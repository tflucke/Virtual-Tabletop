package testprograms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import name.tomflucke.layouts.TableLayout;

public class NetworkLobbyTestor
{
	/*
	 * TODO: Remember what port was openned on the server
	 */
	private static final int defaultPort = 4335;
	private static JFrame window;
	private static Socket server;
	private static PrintWriter out;
	private static BufferedReader in;
	
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
		window.setLayout(new TableLayout(new double[][] {
		        { TableLayout.FILL, TableLayout.FILL },
		        { TableLayout.FILL, TableLayout.PREFERRED } }));
		return window;
	}
	
	private static JButton buildRefreshButton()
	{
		JButton result = new JButton("Refresh");
		result.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
			}
		});
		return result;
	}
	
	private static JButton buildCloseButton()
	{
		JButton result = new JButton("Close");
		result.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				try
				{
					out.println("close");
					out.flush();
					in.close();
					out.close();
					server.close();
					window.dispose();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		return result;
	}
	
	public static void main(String... args) throws IOException
	{
		String serverString = JOptionPane.showInputDialog("Server IP:",
		        "127.0.0.1");
		InetSocketAddress serverAddress = stringToINet(serverString);
		server = connectToServer(serverAddress);
		out = new PrintWriter(server.getOutputStream());
		in = new BufferedReader(new InputStreamReader(server.getInputStream()));
		window = buildWindow();
		window.add(buildCloseButton(), "1, 1");
		window.add(buildRefreshButton(), "0, 1");
		window.setVisible(true);
		window.pack();
	}
}
