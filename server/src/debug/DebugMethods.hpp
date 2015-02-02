/*
 * DebugMethods.hpp
 *
 *  Created on: Feb 2, 2015
 *      Author: tom
 */

#ifndef SRC_DEBUG_DEBUGMETHODS_HPP_
#define SRC_DEBUG_DEBUGMETHODS_HPP_

#include <cstdio>		/* printf */
#include <string>		/* std::string */
#include <stdarg.h> 		/* va_list, va_start, va_end */

enum DebugLevel
{
  OFF = 0, FATAL, ERROR, WARNING, VERBOSE
};

DebugLevel debugMode = OFF;

void
printIfDebug (DebugLevel requiredLevel, std::string format, ...)
{
  if (debugMode >= requiredLevel)
  {
    va_list args;
    va_start(args, format);
    printf (format.c_str (), args);
    va_end(args);
  }
}

void
printIfDebug (DebugLevel debugMode, DebugLevel requiredLevel,
	      std::string format, ...)
{
  if (debugMode >= requiredLevel)
  {
    va_list args;
    va_start(args, format);
    printf (format.c_str (), args);
    va_end(args);
  }
}

#endif /* SRC_DEBUG_DEBUGMETHODS_HPP_ */
