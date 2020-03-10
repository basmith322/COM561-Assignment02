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

import static assignment2.UtilityMethods.getIntegers;
import static assignment2.UtilityMethods.printUL;

public class LargestNumberConcurrentServer extends Thread {
    final int clientIdNumber;
    // This server acts as a multi-threaded server.
    // A number of clients can connect at once.
    private final Socket clientSocket; // client socket for each thread connected
    List<Integer> numList = new ArrayList<>();

    // Constructor
    LargestNumberConcurrentServer(Socket clientSocket, int clientIdNumber) {
        this.clientSocket = clientSocket;
        this.clientIdNumber = clientIdNumber;
    } // end constructor

    public static void main(String[] args) {
        // determine if port to listen on is specified by user else use default
        int portNumber = 4444; // default port number

        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]); // user specified port number
        }
        int clientIdNumber = 0;
        System.out.println("Concurrent Largest Consecutive Subsequence Server Started");
        // create serverSocket to listen on
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + clientSocket.getInetAddress() + " Accepted");
                clientIdNumber++; // give id number to connecting client
                // spawn a new thread to handle new client
                LargestNumberConcurrentServer concurrentServerThread = new LargestNumberConcurrentServer(clientSocket, clientIdNumber);
                System.out.println("Starting new thread");
                concurrentServerThread.start();
            } // end while true
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        } // end catch
    }

    public static ArrayList<Integer> getSubsequence(List<Integer> array) {
        return getIntegers(array);
    }

    // run method for thread
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            System.out.println("New Server Thread Running for Client" + clientSocket.getInetAddress());

            try {
                numList = (ArrayList<Integer>) (in.readObject());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LargestNumberConcurrentServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Client:" + clientIdNumber + "\nEntry " + numList + " Added");

            printUL(numList, out, getSubsequence(numList));

        } catch (IOException e) {
            System.out.println("IOException:" + e.getMessage());
        }
    } // end run
}
