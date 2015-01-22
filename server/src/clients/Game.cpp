/*
 * Game.cpp
 *
 *  Created on: Jan 15, 2015
 *      Author: tom
 */

#include "Game.h"
#include "Client.h"	/* Client */

std::list<Game*> Game::gameList;

std::list<Game*>*
Game::getGameList()
{
	return &gameList;
}

Game::Game (Client* gm, std::string name) : gm(gm)
{
	this->name = name;
	gameList.push_back(this);
}

Game::~Game ()
{
	gameList.remove(this);
}

std::string
Game::getName() const
{
	return name;
}
