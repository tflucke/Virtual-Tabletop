/*
 * Game.h
 *
 *  Created on: Jan 15, 2015
 *      Author: tom
 */

#ifndef SRC_CLIENTS_GAME_H_
#define SRC_CLIENTS_GAME_H_

#include <string>		/* std::string */
#include <list>			/* std::list */
#include "Client.h"	/* Client */

class Game
{
private:
	static std::list<Game*> gameList;
	const Client* gm;
	std::string name;
public:
	static std::list<Game*>*
	getGameList();
	Game (Client* gm, std::string name);
	~Game ();
	std::string
	getName() const;
};

#endif /* SRC_CLIENTS_GAME_H_ */
