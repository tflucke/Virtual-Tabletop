/*
 * ServerSocket.h
 *
 *  Created on: Dec 11, 2014
 *      Author: tom
 */

#ifndef SERVERSOCKET_H_
#define SERVERSOCKET_H_

#include "TCPSocket.h"	/* TCPSocket */

class ServerSocket
{
private:
  static const unsigned short MAX_QUEUE = 5;
protected:
  const int sock;
public:
  ServerSocket (const unsigned int port, const unsigned int queueLimit);
  ServerSocket (const unsigned int port);
  TCPSocket*
  accept () const;
};

#endif /* SERVERSOCKET_H_ */
