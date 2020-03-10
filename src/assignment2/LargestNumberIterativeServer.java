package assignment2;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import static assignment2.LargestNumberConcurrentServer.getIntegers;

public class LargestNumberIterativeServer {
        public static void main(String[] args) {
        // determine if port to listen on is specified by user else use default
        int portNumber = 4444; // default port number
        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]); // user specified port number
        }
        System.out.println("Largest Number Inerative Server Started");
        int clientIdNumber = 0;
        List<Integer> numList = new ArrayList();
        // create serverSocket to listen on	
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                // accept client connection
                Socket clientSocket = serverSocket.accept();
                try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
                    
                    System.out.println("Client Accepted");
                    clientIdNumber++; // give id number to connecting client
                    
                    numList = (ArrayList)in.readObject();
                    System.out.println("Client:" + clientIdNumber + "\nEntry For " + numList + " Added");

                    System.out.println("Unordered List: " + numList);
                    Collections.sort(numList);
                    System.out.println("Ordered List: " + numList);

                    ArrayList<Integer> result = new ArrayList();
                    result = getSubsequence(numList);
                    
                    int resultSize = 0;
                    resultSize = result.size();

                    try {
                        out.writeObject(resultSize + " Elements In Sub-Sequence\nSub-Sequence Is: " + result.toString());
                        out.flush();
                        out.close();
                        System.out.println("\nSerialization Successful... Check your Client output..\n");
                    } catch (Exception e) {
                        System.err.println("Write Error " + e.getMessage());
                    }
                } catch (IOException e) {
                    System.err.println("IOException:" + e.getMessage());
                }
            } // end while true
        } catch (Exception e) {
            System.err.println("Exception:" + e.getMessage());
        } // end catch
    } // end main

    public static ArrayList<Integer> getSubsequence(List<Integer> array) {
        return getIntegers(array);
    }
}
