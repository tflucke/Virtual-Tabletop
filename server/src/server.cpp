/*
 * server.cpp
 *
 *  Created on: Nov 17, 2014
 *      Author: Tom Flucke
 */

#include <iostream>			/* std::cout, std::endl */
#include "network/ServerSocket.h"	/* ServerSocket */
#include "network/SocketException.h"	/* SocketException */
//#include "clients/Client.h"		/* Client */

int
main (int argsLength, char** args)
{
  try
  {
    ServerSocket ss (18081);
    TCPSocket* tcps1 = ss.accept ();
    TCPSocket* tcps2 = ss.accept ();
    tcps1->print(tcps2->readLine());
    tcps2->print(tcps1->readLine());
    tcps1->print ("%s:%d\n", tcps2->getClientIP (), tcps2->getClientPort ());
    tcps2->print ("%s:%d\n", tcps1->getClientIP (), tcps1->getClientPort ());
  }
  catch (SocketException& se)
  {
    std::cout << se.what () << std::endl;
  }
  return 0;
}
//
//int
//main (int argsLength, char** args)
//{
//	try
//	{
//		ServerSocket ss (4335);
//		//		TCPSocket tcps1 = ss.accept ();
//		//		TCPSocket tcps2 = ss.accept ();
//		//		tcps1.print ("%s:%d\r\n", tcps2.getClientIP (), tcps2.getClientPort ());
//		//		tcps2.print ("%s:%d\r\n", tcps1.getClientIP (), tcps1.getClientPort ());
//	}
//	catch (SocketException& se)
//	{
//		std::cout << se.what () << std::endl;
//	}
//	return 0;
//}
