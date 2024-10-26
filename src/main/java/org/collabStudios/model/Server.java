package org.collabStudios.model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4242);

        //wait for client to connect
        Socket socket = serverSocket.accept();

        //open output stream to client
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush(); //flush send header
        //open input stream
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        



    }

}
