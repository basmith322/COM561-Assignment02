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

    public static void main(String[] args) {
        //if there is a port number specified from the client then use it, otherwise default port 4444 is used.
        int portNumber = 4444; // default port number
        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]); // user specified port number
        }

        int clientIdNumber = 0;
        System.out.println("Iterative Largest Consecutive Subsequence Server Started");
        List<Integer> numList = new ArrayList<>();
        // create serverSocket to listen on	
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            //Loop to keep listening for new connections until application finishes
            do {
                //Accept connection from the client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + clientSocket.getInetAddress() + " Accepted");
                clientIdNumber++; // give id number to connecting client

                //Create output and input streams that read from and write to the client socket
                try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

                    System.out.println("New Server Thread Running for Client" + clientSocket.getInetAddress());

                    //Try to store the values from the input stream in numList
                    try {
                        numList = (ArrayList<Integer>) in.readObject();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(LargestSubsequenceIterativeServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.print("Client:" + clientIdNumber + "\nEntry " + numList + " Added");

                    //Passes numList, outputStream and the getSubSequence Function results to print function to handle output
                    printNumbersList(numList, out, getSubsequence(numList));
                } catch (IOException e) {
                    System.err.println("IOException:" + e.getMessage());
                }
            } while (true);
        } catch (Exception e) {
            System.err.println("Exception:" + e.getMessage());
        }
    } // end main

    //Takes in an array and passes it to the createSubSequence function
    public static ArrayList<Integer> getSubsequence(List<Integer> array) {
        return createSubSequence(array);
    }
}
