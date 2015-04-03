/*
 * UDPTestServer.cpp
 *
 *  Created on: Apr, 02 2015
 *      Author: Tom Flucke
 */

#include <iostream>						/* std::cout, std::endl */
#include "network/UDPSocket.h"			/* ServerSocket */
#include "network/SocketException.h"	/* SocketException */
#include "debug/DebugMethods.hpp"		/* printIfDebug, VERBOSE*/

int
main (int argsLength, char** args)
{
	  debugMode = VERBOSE;
	  try
	  {
	    printIfDebug (VERBOSE, "Creating server socket.");
	    UDPSocket socket (3345);
	    //while (true)
	    //{
	    	UDPSocket::IPAddress ip1 = socket.read()->sender;
			printIfDebug (VERBOSE, "Received first connection.");
			printIfDebug (VERBOSE, "Con1: %s:%u.", ip1.ip, ip1.port);
			socket.print(ip1, "Hello World!");
//			UPDSocket::IPAddress ip2 = socket.read().sender;
//			printIfDebug (VERBOSE, "Received second connection.");
//			printIfDebug (VERBOSE, "Con2: %s:%u.", ip2.ip, ip2.port);
//	        printIfDebug (VERBOSE, "Sending Con2 private port to Con1.");
//
//	        printIfDebug (VERBOSE, "Sending Con1 private port to Con2.");
//
//	        printIfDebug (VERBOSE, "Sending Con2 public port to Con1.");
//	        socket.print(ip1, "%s:%d", ip2.ip, ip2.port);
//	        printIfDebug (VERBOSE, "Sending Con1 public port to Con2.");
//	        socket.print(ip2, "%s:%d", ip1.ip, ip1.port);
	    //}
	  }
	  catch (SocketException& se)
	  {
	    printIfDebug (FATAL, se.what ());
	  }
	  printIfDebug (VERBOSE, "Exiting.");
	  return 0;
}
