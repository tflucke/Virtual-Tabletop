/*
 * Client.h
 *
 *  Created on: Jan 14, 2015
 *      Author: tom
 */

#ifndef SRC_CLIENT_H_
#define SRC_CLIENT_H_

#include <string>									/* std::string */
#include "../network/TCPSocket.h"	/* TCPSocket */

class Client
{
	private:
		static unsigned int nextId;
		static unsigned int connectionCount;
		unsigned const int id;
		bool active;
		void
		handleInput();
		void
		close();
	protected:
		const TCPSocket* socket;
		bool
		initalValidation();
		std::string
		processCommand(std::string command);
	public:
		Client (TCPSocket* tcps);
		~Client ();
		bool
		isActive() const;
};

#endif /* SRC_CLIENT_H_ */
