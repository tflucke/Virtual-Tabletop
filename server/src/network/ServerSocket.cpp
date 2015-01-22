/*
 * ServerSocket.cpp
 *
 *  Created on: Dec 11, 2014
 *      Author: tom
 */

#include "ServerSocket.h"
#include <sys/socket.h>				/* socket, AF_INET */
#include <netinet/in.h>				/* INADDR_ANY */
#include "SocketException.h"	/* SocketException */

ServerSocket::ServerSocket (const unsigned int port,
														const unsigned int queueLimit) :
		sock (socket (AF_INET, SOCK_STREAM, 0))
{
	if (sock < 0)
	{
		throw SocketException ("Could not create socket!");
	}

	sockaddr_in socketInfo;
	socketInfo.sin_family = AF_INET;
	socketInfo.sin_addr.s_addr = INADDR_ANY;
	socketInfo.sin_port = htons (port);

	if (bind (sock, (sockaddr *) &socketInfo, sizeof(socketInfo)) < 0)
	{
		throw SocketException ("Could not bind socket.  Port may be in use.");
	}

	if (listen (sock, queueLimit) < 0)
	{
		throw SocketException ("Could not listen on socket.");
	}
}

ServerSocket::ServerSocket (const unsigned int port) :
		ServerSocket (port, MAX_QUEUE)
{
}

TCPSocket*
ServerSocket::accept () const
{
	sockaddr_in clientInfo;
	unsigned int clientInfoSize = sizeof(clientInfo);
	int sessionSocket = ::accept (sock, (sockaddr *) &clientInfo,
																&clientInfoSize);
	if (sessionSocket < 0)
	{
		//TODO:
		//Use errno for more details
		throw SocketException ("Could not accept client socket.");
	}
	else
	{
		return new TCPSocket (sessionSocket, clientInfo, clientInfoSize);
	}
}
