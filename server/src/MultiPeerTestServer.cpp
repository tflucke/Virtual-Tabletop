/*
 * server.cpp
 *
 *  Created on: Nov 17, 2014
 *      Author: Tom Flucke
 */

#include <iostream>			/* std::cout, std::endl */
#include <string>			/* std::string */
#include <sstream>			/* std::stringstream, std::endl */
#include <thread>			/* std::thread */
#include <deque>			/* std::deque */
#include "network/ServerSocket.h"	/* ServerSocket */
#include "network/SocketException.h"	/* SocketException */
#include "debug/DebugMethods.hpp"	/* printIfDebug, VERBOSE*/

enum State
{
  WAITING, PASSING_DATA, ABORTING
};

static State currentState;
static std::deque<TCPSocket*> peers;
static std::string hostPublicIP;
static std::string hostPrivateIP;

void
waitForClose (TCPSocket* host)
{
  printIfDebug (VERBOSE, "Reading host private IP.\n\r");
  hostPrivateIP = host->readLine ();
  printIfDebug (VERBOSE, "Reading host public IP.\n\r");
  std::stringstream stringBuilder;
  stringBuilder << host->getClientIP () << ":" << host->getClientIP ()
      << std::endl;
  hostPublicIP = stringBuilder.str ();
  std::string line;
  printIfDebug (VERBOSE, "Waiting for host input.\n\r");
  while ((line = host->readLine ()) != "close\n")
  {
    printIfDebug (VERBOSE, "Host says: %s", &line);
    if (line == "abort\n")
    {
      printIfDebug (WARNING, "Aborting Session.\n\r");
      delete host;
      return;
    }
  }
  printIfDebug (VERBOSE, "Ceasing peer search.\n\r");
  currentState = PASSING_DATA;
}

int
main (int argsLength, char** args)
{
  debugMode = VERBOSE;
  try
  {
    printIfDebug (VERBOSE, "Creating server socket.\n\r");
    ServerSocket ss (3345);
    printIfDebug (VERBOSE, "Waiting on host.\n\r");
    TCPSocket* host = ss.accept ();
    printIfDebug (VERBOSE,
		  "Received host connection.\n\rCreating manager thread.\n\r");
    new std::thread (waitForClose, host);
    currentState = WAITING;
    while (currentState == WAITING)
    {
      printIfDebug (VERBOSE, "Waiting for peer.\n\r");
      TCPSocket* peer = ss.accept ();
      printIfDebug (VERBOSE, "Received peer connection.\n\r");
      peers.push_back (peer);
    }
    printIfDebug (VERBOSE, "Finished waiting for peers.\n\r");
    if (currentState != ABORTING)
    {
      printIfDebug (VERBOSE, "Sending IP information for all connections.\n\r");
      for (TCPSocket* peer : peers)
      {
	printIfDebug (VERBOSE,
		      "Sending IP information about %s:%d to host.\n\r",
		      peer->getClientIP (), peer->getClientPort ());
	host->print (peer->readLine ());
	host->print (peer->getClientIP (), peer->getClientPort ());
	printIfDebug (VERBOSE,
		      "Sending IP information to %s:%d about host.\n\r",
		      peer->getClientIP (), peer->getClientPort ());
	host->print (hostPrivateIP);
	host->print (hostPublicIP);
	delete peer;
      }
      delete host;
    }
    else
    {
      printIfDebug (WARNING,
		    "Session aborted.\n\rClosing all connections.\n\r");
      for (TCPSocket* peer : peers)
      {
	printIfDebug (VERBOSE, "Closing connection to %s:%d.\n\r",
		      peer->getClientIP (), peer->getClientPort ());
	delete peer;
      }
    }
  }
  catch (SocketException& se)
  {
    printIfDebug (FATAL, se.what ());
  }
  printIfDebug (VERBOSE, "Exiting.\n\r");
  return 0;
}
