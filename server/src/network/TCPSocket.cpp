/*
 * TCPSocket.cpp
 *
 *  Created on: Dec 11, 2014
 *      Author: tom
 */

#include "TCPSocket.h"
#include <unistd.h> 					/* close, read */
#include <arpa/inet.h>				/* inet_ntoa */
#include <stdio.h> 						/* fprintf, fgets */
#include <stdarg.h> 					/* va_list, va_start, va_end */
#include <stdexcept>					/* std::runtime_error */
#include <errno.h>						/* errno */
#include "SocketException.h"	/* SocketException */

TCPSocket::TCPSocket (const int socketDescriptor, const sockaddr_in info,
		      const unsigned int infoSize) :
    sessionSocket (socketDescriptor), clientInfo (info), clientInfoSize (
	infoSize)
{
  inputStream = fdopen (socketDescriptor, "r");
  outputStream = fdopen (socketDescriptor, "w");
  bufferSize = 255;
}
TCPSocket::~TCPSocket ()
{
  fflush (outputStream);
  fclose (outputStream);
  fclose (inputStream);
  close (sessionSocket);
}
void
TCPSocket::setBufferSize (unsigned int newSize)
{
  bufferSize = newSize;
}
char*
TCPSocket::getClientIP () const
{
  return inet_ntoa (clientInfo.sin_addr);
}
int
TCPSocket::getClientPort () const
{
  return clientInfo.sin_port;
}

int
TCPSocket::print (const char * format, ...) const
{
  va_list args;
  va_start(args, format);
  int result = vfprintf (outputStream, format, args);
  va_end(args);
  fflush (outputStream);
  return result;
}
int
TCPSocket::print (const std::string format, ...) const
{
  va_list args;
  va_start(args, format);
  int result = print (format.c_str (), args);
  va_end(args);
  return result;
}

char
TCPSocket::read () const
{
  char ch = '\0';
  int lastRead = -1;

  while (lastRead < 0)
  {
    lastRead = ::read (sessionSocket, &ch, 1);

    if (lastRead < 0 && errno != EINTR)
    {
        throw SocketException ("Read error");
    }
    else if (lastRead == 0)
    {
      throw std::runtime_error ("Stream is closed.");
    }
  }

  return ch;
}

std::string
TCPSocket::readLine () const
{
  char buffer[bufferSize];
  if (fgets (buffer, bufferSize, inputStream) != NULL)
  {
    return std::string (buffer);
  }
  else
  {
    throw std::runtime_error ("Stream is closed.");
  }
}
