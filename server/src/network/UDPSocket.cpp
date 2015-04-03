/*
 * UDPSocket.cpp
 *
 *  Created on: Apr 2, 2015
 *      Author: tom
 */

#include "UDPSocket.h"
#include <arpa/inet.h>		/*  */
#include <sys/socket.h>		/* socket */
#include "SocketException.h"
#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <string.h>			/* memset */
#include <stdarg.h> 		/* va_list, va_start, va_end */

UDPSocket::UDPSocket(const int port) : sessionSocket(socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP))
{
	if (sessionSocket == -1)
	{
		throw new SocketException("Could not create socket.");
	}
	struct sockaddr_in si_me;
	memset((char *) &si_me, 0, sizeof(si_me));
	si_me.sin_family = AF_INET;
	si_me.sin_port = htons(port);
	si_me.sin_addr.s_addr = htonl(INADDR_ANY);
	if (bind(sessionSocket, (struct sockaddr*) &si_me, sizeof(si_me)) == -1)
	{
		throw new SocketException("Could not bind socket.");
	}

	bufferMaxSize = 255;
}

void
UDPSocket::setBufferSize (unsigned int newSize)
{
	bufferMaxSize = newSize;
}

UDPSocket::Packet*
UDPSocket::read () const
{
	Packet* res = new Packet();
	res->message = new char[bufferMaxSize];
	struct sockaddr_in si_other;
	unsigned int slen = sizeof(si_other);
    memset((char *) &si_other, 0, sizeof(si_other));
	int newChars;
	if ((newChars = recvfrom(sessionSocket, res->message, bufferMaxSize, 0, (struct sockaddr*) &si_other, &slen)) == -1)
	{
		throw std::runtime_error ("Stream is closed.");
	}
	res->sender.ip = inet_ntoa(si_other.sin_addr);
	res->sender.port = ntohs(si_other.sin_port);
	return res;
}

int
UDPSocket::print (const IPAddress addr, const char * format, ...) const
{
	struct sockaddr_in si_other;
	int slen = sizeof(si_other);
	memset((char *) &si_other, 0, sizeof(si_other));
	si_other.sin_family = AF_INET;
	si_other.sin_port = htons(addr.port);
	if (inet_aton(addr.ip, &si_other.sin_addr) == 0)
	{
		throw std::runtime_error ("Could not resolve ip address.");
	}
	va_list args;
	va_start(args, format);
	char buf[bufferMaxSize];
	sprintf(buf, format, args);
	va_end(args);
	int result;
	if ((result = sendto(sessionSocket, buf, bufferMaxSize, 0, (struct sockaddr*) &si_other, slen)) == -1)
	{
		throw std::runtime_error ("Failed to send packet.");
	}
	return result;
}
int
UDPSocket::print (const IPAddress addr, const std::string format, ...) const
{
	struct sockaddr_in si_other;
	int slen = sizeof(si_other);
	memset((char *) &si_other, 0, sizeof(si_other));
	si_other.sin_family = AF_INET;
	si_other.sin_port = htons(addr.port);
	if (inet_aton(addr.ip, &si_other.sin_addr) == 0)
	{
		throw std::runtime_error ("Could not resolve ip address.");
	}
	va_list args;
	va_start(args, format);
	char buf[bufferMaxSize];
	sprintf(buf, format.c_str (), args);
	va_end(args);
	int result;
	if ((result = sendto(sessionSocket, buf, bufferMaxSize, 0, (struct sockaddr*) &si_other, slen)) == -1)
	{
		throw std::runtime_error ("Failed to send packet.");
	}
	return result;
}

UDPSocket::~UDPSocket()
{
	close(sessionSocket);
}

