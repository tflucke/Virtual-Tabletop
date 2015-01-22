/*
 * TCPSocket.h
 *
 *  Created on: Dec 11, 2014
 *      Author: tom
 */

#ifndef NETWORK_TCPSOCKET_H_
#define NETWORK_TCPSOCKET_H_

#include <iostream>			/* FILE */
#include <netinet/in.h>	/* sockaddr_in */
#include <string>				/* std:string */

class TCPSocket
{
private:
	FILE* inputStream;
	FILE* outputStream;
	unsigned int bufferSize;
protected:
	const int sessionSocket;
	const sockaddr_in clientInfo;
	const unsigned int clientInfoSize;
public:
	TCPSocket (const int sessionSocket, const sockaddr_in clientInfo,
							const unsigned int clientInfoSize);
	~TCPSocket ();
	int
	print (const char * format, ...) const;
	int
	print (const std::string format, ...) const;
	std::string
	readLine () const;
	char
	read () const;
	void
	setBufferSize (const unsigned int newSize);
	char*
	getClientIP () const;
	int
	getClientPort () const;
};

#endif /* NETWORK_TCPSOCKET_H_ */
