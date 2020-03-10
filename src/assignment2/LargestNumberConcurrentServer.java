/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LargestNumberConcurrentServer extends Thread {
    // This server acts as a multi-threaded server. 
    // A number of clients can connect at once.
    private final Socket clientSocket; // client socket for each thread connected
    int clientIdNumber;
    List<Integer> numList = new ArrayList();
    
    // Constructor
    LargestNumberConcurrentServer(Socket clientSocket, int clientIdNumber) {
        this.clientSocket = clientSocket;
        this.clientIdNumber = clientIdNumber;
    } // end constructor

    // run method for thread
    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
            
            System.out.println("New Server Thread Running for Client");

            try {
                numList = (ArrayList)(in.readObject());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LargestNumberConcurrentServer.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            System.out.println(resultSize + " Elements In Sub-Sequence\nSub-Sequence Is: " + result.toString());

        } catch (IOException e) {
            System.out.println("IOException:" + e.getMessage());
        }
    } // end run

    public static ArrayList<Integer> getSubsequence(List<Integer> array) {
        return getIntegers(array);
    }

    static ArrayList<Integer> getIntegers(List<Integer> array) {
        ArrayList<Integer> holder1 = new ArrayList<>();
        ArrayList<Integer> subsequence = new ArrayList<>();
        int previousNum = -999999999;

        for(Integer num : array) {
            if(previousNum == -999999999){
                previousNum = num;
                holder1.add(num);
            } else {
                if(num == (previousNum+1)){
                    holder1.add(num);
                } else {
                    holder1 = new ArrayList<Integer>();
                }
                previousNum = num;
            }
            if(subsequence.size() < holder1.size()){
                subsequence = holder1;
            }
        }
        System.out.println(subsequence);
        return subsequence;
    }

    public static void main(String[] args) {
        // determine if port to listen on is specified by user else use default
        int portNumber = 4444; // default port number

        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]); // user specified port number
        }
        int clientIdNumber = 0;
        System.out.println("Largest Number Concurrent Server Started");
        // create serverSocket to listen on	
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Accepted from " + clientSocket.getInetAddress());
                clientIdNumber++; // give id number to connecting client
                // spawn a new thread to handle new client
                LargestNumberConcurrentServer concurrentServerThread = new LargestNumberConcurrentServer(clientSocket, clientIdNumber);
                System.out.println("About to start new thread");
                concurrentServerThread.start();
            } // end while true
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        } // end catch
    }
}
