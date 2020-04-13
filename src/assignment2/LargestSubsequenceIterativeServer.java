package assignment2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static assignment2.UtilityMethods.createSubSequence;
import static assignment2.UtilityMethods.printNumbersList;

public class LargestSubsequenceIterativeServer {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // determine if port to listen on is specified by user else use default
        int portNumber = 4444; // default port number
        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]); // user specified port number
        }
        System.out.println("Iterative Largest Consecutive Subsequence Server Started");
        List<Integer> numList = new ArrayList<>();
        // create serverSocket to listen on	
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            // end while true
            int clientIdNumber = 0;
            while (true) {
                // accept client connection
                Socket clientSocket = serverSocket.accept();
                try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

                    System.out.println("Client " + clientSocket.getInetAddress() + " Accepted");
                    clientIdNumber++; // give id number to connecting client

                    try {
                        numList = (ArrayList<Integer>) in.readObject();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(LargestSubsequenceIterativeServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.print("Client:" + clientIdNumber + "\nEntry " + numList + " Added");

                    printNumbersList(numList, out, getSubsequence(numList));
                } catch (IOException e) {
                    System.err.println("IOException:" + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Exception:" + e.getMessage());
        } // end catch
    } // end main

    public static ArrayList<Integer> getSubsequence(List<Integer> array) {
        return createSubSequence(array);
    }


}
