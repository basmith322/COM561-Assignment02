package assignment2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LargestNumberClient implements Serializable {
    public static void main(String[] args) {
        String hostAddress = "localhost"; //default host address
        int hostPort = 4444; //default host port
        // assign host machine name and port to connect to
        if (args.length != 0) {
            if (args[0] != null) {
                hostAddress = args[0]; //user specified address
            }
            if (args[1] != null) {
                hostPort = Integer.parseInt(args[1]); //user specified port
            }
        }

        System.out.println("Connecting to Server. Address: " + hostAddress + " | Port: " + hostPort);
        // connect to server and extract input and output streams
        try (Socket serverSocket = new Socket(hostAddress, hostPort);
             ObjectOutputStream outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(serverSocket.getInputStream())) {
            System.out.println("Connection Successful!");
            System.out.println("Welcome to the Largest Consecutive Subsequence application. " +
                    "\nEnter a number and press the return key to continue (Type q or Q and press enter when finished)");

            // create client input stream for user input
            Scanner scanner = new Scanner(System.in);
            // Initialise The Number And ArrayList
            List<Integer> numList = new ArrayList<>();

            boolean exit = true;
            do {
                System.out.print("Enter a number: ");
                if (scanner.hasNext("q") || scanner.hasNext("Q")) {
                    exit = false;
                } else {
                    while (!scanner.hasNextInt() && exit) {
                        if (scanner.hasNext("q") || scanner.hasNext("Q")) {
                            exit = false;
                        } else {
                            System.out.print("Invalid input: Input must a valid Integer. Please try again:");
                            scanner.next();
                        }
                    }
                    if (scanner.hasNextInt()) {
                        int newNumber = scanner.nextInt();
                        numList.add(newNumber);
                        System.out.println(numList);
                    }
                }
            } while (exit);
            try {
                outputStream.writeObject(numList);
                outputStream.flush();
                System.out.println("\nValues have been sent to server, please see the server output.\n");
            } catch (Exception e) {
                System.out.println("\nError sending to server, please see the error message below\n");
                e.printStackTrace();
            }
            String serverReply;

            serverReply = (String) inputStream.readObject();
            System.out.println(serverReply);

        } catch (
                Exception e) {
            System.err.println("Client Exception:  " + e.getMessage());
        }
    }
} // end main