/*
 * DebugMethods.hpp
 *
 *  Created on: Feb 2, 2015
 *      Author: tom
 */

#ifndef SRC_DEBUG_DEBUGMETHODS_HPP_
#define SRC_DEBUG_DEBUGMETHODS_HPP_

#include <iostream>			/* std::cout, std::endl */

enum DebugLevel {OFF = 0, FATAL, ERROR, WARNING, VERBOSE};

DebugLevel debugMode = OFF;

void
printIfDebug (DebugLevel requiredLevel, std::string message)
{
  if (debugMode >= requiredLevel)
  {
    std::cout << message << std::endl;
  }
}

void
printIfDebug (DebugLevel debugMode, DebugLevel requiredLevel,
	      std::string message)
{
  if (debugMode >= requiredLevel)
  {
    std::cout << message << std::endl;
  }
}

#endif /* SRC_DEBUG_DEBUGMETHODS_HPP_ */
