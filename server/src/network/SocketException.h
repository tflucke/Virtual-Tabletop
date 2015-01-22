/*
 * SocketException.hpp
 *
 *  Created on: Dec 11, 2014
 *      Author: tom
 */

#ifndef SOCKETEXCEPTION_H_
#define SOCKETEXCEPTION_H_

#include <string>			/* std::string */
#include <stdexcept>	/* std::runtime_error */

class SocketException : public std::runtime_error
{
private:
protected:
public:
  SocketException(const std::string& detail);
};

#endif /* SOCKETEXCEPTION_H_ */
