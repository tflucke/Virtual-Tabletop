/*
 * server.cpp
 *
 *  Created on: Nov 17, 2014
 *      Author: Tom Flucke
 */

#include <iostream>			/* std::cout, std::endl */
#include "network/ServerSocket.h"	/* ServerSocket */
#include "network/SocketException.h"	/* SocketException */
#include "debug/DebugMethods.hpp"	/* printIfDebug, VERBOSE*/

int
main (int argsLength, char** args)
{
  debugMode = VERBOSE;
  try
  {
    printIfDebug (VERBOSE, "Creating server socket.");
    ServerSocket ss (3345);
    TCPSocket* tcps1 = ss.accept ();
    printIfDebug (VERBOSE, "Received first connection.");
    TCPSocket* tcps2 = ss.accept ();
    printIfDebug (VERBOSE, "Received second connection.");
    printIfDebug (VERBOSE, "Sending Con2 private IP to Con1.");
    tcps1->print (tcps2->readLine ());
    printIfDebug (VERBOSE, "Sending Con1 private IP to Con2.");
    tcps2->print (tcps1->readLine ());
    printIfDebug (VERBOSE, "Sending Con2 public IP to Con1.");
    tcps1->print ("%s:%d\n", tcps2->getClientIP (), tcps2->getClientPort ());
    printIfDebug (VERBOSE, "Sending Con1 public IP to Con2.");
    tcps2->print ("%s:%d\n", tcps1->getClientIP (), tcps1->getClientPort ());
  }
  catch (SocketException& se)
  {
    printIfDebug (FATAL, se.what ());
  }
  printIfDebug (VERBOSE, "Exiting.");
  return 0;
}
