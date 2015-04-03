package testprograms;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UDPHolePunchClient
{
	public static void main(String... args) throws IOException
	{
//		new UDPHolePunchClient(new InetSocketAddress("63.246.2.164", 3345));
		new UDPHolePunchClient(new InetSocketAddress("127.0.0.1", 3345));
	}
	
	public UDPHolePunchClient(InetSocketAddress serverAddr) throws IOException
	{
		DatagramSocket socket = new DatagramSocket();
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr.getAddress(), serverAddr.getPort());
        socket.send(packet);
        System.out.println("Packet Sent");
        
            // get response
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        System.out.println("Packet recived");
 
        // display response
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Message from the outside: " + received);
     
        socket.close();
	}
}
