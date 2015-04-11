/*
 * UDPTestServer.cpp
 *
 *  Created on: Apr, 02 2015
 *      Author: Tom Flucke
 */

#include <boost/asio.hpp>
#include <iostream>

int
main (int argsLength, char** args)
{
	try
	{
		boost::asio::io_service io;
		//udp_server server(io);
		io.run();
		return 0;
	}
	catch (std::exception& e)
	{
		std::cerr << e.what() << std::endl;
		return 1;
	}
}
//int
//main (int argsLength, char** args)
//{
//	  debugMode = VERBOSE;
//	  try
//	  {
//	    printIfDebug (VERBOSE, "Creating server socket.");
//	    UDPSocket socket (3345);
//	    //while (true)
//	    //{
//	    	UDPSocket::IPAddress* ip1 = socket.read()->sender;
//			printIfDebug (VERBOSE, "Received first connection.");
//			//printIfDebug (VERBOSE, "Con1: %s:%d.", ip1->getIpString(), ip1->getPort());
//			printf("Con1: %s:%d.\n", ip1->getIpString(), ip1->getPort());
//			socket.print(ip1, "%s:%d", ip1->getIpString(), ip1->getPort());
////			UPDSocket::IPAddress ip2 = socket.read().sender;
////			printIfDebug (VERBOSE, "Received second connection.");
////			printIfDebug (VERBOSE, "Con2: %s:%u.", ip2.ip, ip2.port);
////	        printIfDebug (VERBOSE, "Sending Con2 private port to Con1.");
////
////	        printIfDebug (VERBOSE, "Sending Con1 private port to Con2.");
////
////	        printIfDebug (VERBOSE, "Sending Con2 public port to Con1.");
////	        socket.print(ip1, "%s:%d", ip2.ip, ip2.port);
////	        printIfDebug (VERBOSE, "Sending Con1 public port to Con2.");
////	        socket.print(ip2, "%s:%d", ip1.ip, ip1.port);
//	    //}
//	  }
//	  catch (SocketException& se)
//	  {
//	    printIfDebug (FATAL, se.what ());
//	  }
//	  printIfDebug (VERBOSE, "Exiting.");
//	  return 0;
//}
