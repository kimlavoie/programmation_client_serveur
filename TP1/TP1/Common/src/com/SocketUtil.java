package com;

import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

/**
  * Helper class to help send and receive bytes from a socket
  */
public class SocketUtil{
	/**
	  * Send the number of bytes in a 4 bytes int, then send the content
	  */
    public static void writeToSocket(byte[] content, Socket socket) throws Exception{
	    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
	    byte[] length = ByteBuffer.allocate(4).putInt(content.length).array();			//First, we prepend the length
		output.write(length, 0, 4);
		output.write(content, 0, content.length);											//Then we write de datas
	}
	
	/**
	  * Read the number of bytes in a 4 bytes int, then read the content
	  */
	public static byte[] readFromSocket(Socket socket) throws Exception{
	    DataInputStream input = new DataInputStream(socket.getInputStream());
	    byte[] lengthBytes = new byte[4];												//First, we read the length
	    input.read(lengthBytes, 0, 4);
		int length = ByteBuffer.wrap(lengthBytes).getInt();
	       byte[] bytesRead = new byte[length];											//Then we read the correct amount of datas
	    input.read(bytesRead, 0, length);
		return bytesRead;
	}
}