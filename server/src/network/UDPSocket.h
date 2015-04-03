/*
 * UDPSocket.h
 *
 *  Created on: Apr 2, 2015
 *      Author: tom
 */

#ifndef UDPSOCKET_H_
#define UDPSOCKET_H_

#include <string>	/* std::string */
#include <stdint.h>	/* uint16_t */

class UDPSocket
{
private:
	unsigned int bufferMaxSize;
protected:
	const int sessionSocket;
public:
	struct IPAddress
	{
		char* ip;
		uint16_t port;
	};
	struct Packet
	{
		char* message;
		IPAddress sender;
	};
	UDPSocket (const int port);
	~UDPSocket ();
	int
	print (IPAddress addr, const char * format, ...) const;
	int
	print (IPAddress addr, const std::string format, ...) const;
	Packet*
	read () const;
	void
	setBufferSize (const unsigned int newSize);
};

#endif /* UDPSOCKET_H_ */
