/*
 * SocketException.cpp
 *
 *  Created on: Dec 11, 2014
 *      Author: tom
 */

#include "SocketException.h"

SocketException::SocketException(const std::string& d) : std::runtime_error(d)
{
}
