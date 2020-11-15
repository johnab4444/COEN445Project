package com.coen445.FinalProject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

//todo: in client make sure that socket is unique. if not unique, chose another random socket until a free one is found
//todo: while also making sure that they're above the reserved sockets [(thus do rand() % (max socket - amount of reserved sockets)] + amount of reserved sockets

public class ClientHandlerClass extends Thread{
    private Server server;

    public ClientHandlerClass(Server server){
        this.server = server;
    }

    @Override
    public void run() {
        super.run();
        String received = "";
        String toReturn = "";
        System.out.println("Request on port: " + server.getPort());

        loop: while(true){
            if(!server.getRegistered()){
                try {
                    toReturn = "TOREGISTER";
                    server.sendObject(toReturn);
                    server.setRegistered(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                //spit the received message. Each part of the frame is separated by a space. Thus
                //the type of message will be the first element.
                try {
                    received = server.readObject().toString();
                } catch (IOException e) {
                    //in the event a client randomly disconnects, it will throw and end of file exception.
                    //When this happens, we're going to catch it, print the log that says a user disconnected, and then move on
                    //if(e.equals(EOFException.class)){
                    if(e instanceof EOFException || e instanceof SocketException){
                        System.out.println("A user disconnected while server waiting to receive a message");
                        break;
                    }
                    else
                        e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String[] messageSegments = received.split(" ");
                switch (messageSegments[0].toUpperCase()) {
                    case "REGISTER":
                        try {
                            System.out.println("Registered new user");
                            server.sendObject("REGISTERED");
                            server.setRegistered(true);
                        } catch (IOException e) {
                            if(e instanceof EOFException || e instanceof SocketException){
                                System.out.println("A user disconnected while server trying to send a message");
                                break loop;
                            }
                            else
                            e.printStackTrace();
                        }
                        // TODO: 2020-11-08 Add new user to the database (Use semaphore n shit)

                        //now that we have the data from the user we need to save it.

                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + messageSegments[0].toUpperCase());
                }
            }
        }
        System.out.println("Session Terminated");
    }
}