package assignment2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static assignment2.UtilityMethods.createSubSequence;
import static assignment2.UtilityMethods.printNumbersList;

public class LargestSubsequenceConcurrentServer extends Thread {
    final int clientIdNumber;

    //This server is multithreaded
    private final Socket clientSocket; //Create a client socket for each thread connected
    List<Integer> numList = new ArrayList<>();

    // Constructor to allow clientSocket and clientID from the client connection
    LargestSubsequenceConcurrentServer(Socket clientSocket, int clientIdNumber) {
        this.clientSocket = clientSocket;
        this.clientIdNumber = clientIdNumber;
    } // end constructor

    public static void main(String[] args) {
        //if there is a port number specified from the client then use it, otherwise default port 4444 is used.
        int portNumber = 4444; // default port number
        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]); // user specified port number
        }

        int clientIdNumber = 0;
        System.out.println("Concurrent Largest Consecutive Subsequence Server Started");
        // create serverSocket to listen on
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            //Loop to keep listening for new connections until application finishes
            while (true) {
                //Accept connection from the client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + clientSocket.getInetAddress() + " Accepted");
                clientIdNumber++; // give ID number to connecting client

                //Create a new thread for each new client
                LargestSubsequenceConcurrentServer concurrentServerThread = new LargestSubsequenceConcurrentServer(clientSocket, clientIdNumber);
                System.out.println("Starting new thread");
                concurrentServerThread.start();
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        } // end catch
    }

    //Takes in an array and sorts it prior to passing it to the createSubSequence function
    public static ArrayList<Integer> getSubsequence(List<Integer> array) {
        Collections.sort(array);
        return createSubSequence(array);
    }

    // run method for threads
    @Override
    public void run() {
        //Create output and input streams that read from and write to the client socket
        try (ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream())) {

            System.out.println("New Server Thread Running for Client" + clientSocket.getInetAddress());

            //Try to store the values from the input stream in numList
            try {
                numList = (ArrayList<Integer>) inStream.readObject();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LargestSubsequenceConcurrentServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("Client: " + clientIdNumber + "\nEntry " + numList + " Added");
            //Passes numList, outputStream and the getSubSequence Function results to print function to handle output
            printNumbersList(numList, outStream, getSubsequence(numList));

        } catch (IOException e) {
            System.out.println("IOException:" + e.getMessage());
        }
    } // end run
}
