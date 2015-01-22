/*
 * Client.cpp
 *
 *  Created on: Jan 14, 2015
 *      Author: tom
 */

#include "Client.h"
#include <thread>			/* std::thread */
#include <iostream>		/* std::thread */
#include <sstream>		/* std::stringstream */
#include <list>				/* std::list */
#include "Game.h"			/* Game */

unsigned int Client::nextId = 0;
unsigned int Client::connectionCount = 0;

Client::Client (TCPSocket* tcps) : id(nextId++), socket(tcps)
{
	connectionCount++;
	active = false;
	if (initalValidation())
	{
		active = true;
		new std::thread(&Client::handleInput, this);
	}
	else
	{
		connectionCount--;
	}
}
Client::~Client ()
{
	close();
}

void
Client::close()
{
	if (active)
	{
		active = false;
		connectionCount--;
		delete socket;
	}
}

void
Client::handleInput()
{
	std::string line;
	while (active && (line = socket->readLine()) != "close\n")
	{
		socket->print(processCommand(line));
	}
	close();
}

bool
Client::initalValidation()
{
	return true;
}

std::string
Client::processCommand(std::string command)
{
	std::stringstream result;
	if (command == "count cons\n")
	{
		result << connectionCount;
	}
	else if (command == "list games\n")
	{
		std::list<Game*>* gameList = Game::getGameList();
		for (Game* game : *gameList)
		{
			result << game->getName() << std::endl;
		}
	}
	result << std::endl;
	return result.str();
}

bool
Client::isActive() const
{
	return active;
}
