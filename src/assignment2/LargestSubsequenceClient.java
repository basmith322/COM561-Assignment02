package assignment2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LargestSubsequenceClient implements Serializable {
    public static void main(String[] args) {
        String hostAddress = "localhost"; //default host address
        int hostPort = 4444; //default host port

        // If there are arguments passed in then assign them where necessary
        if (args.length != 0) {

            //If there is an argument at array position 0, this must be host address so assign arg to host address
            if (args[0] != null) {
                hostAddress = args[0]; //user specified address
            }
            //If there is an argument at array position 1, this must be host port so assign arg to host port
            if (args[1] != null) {
                hostPort = Integer.parseInt(args[1]); //user specified port
            }
        }

        System.out.println("Connecting to Server. Address: " + hostAddress + " | Port: " + hostPort);

        //Try connecting to server and extract input and output streams
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

            //Set a boolean value used to finish input and send to output
            boolean finish = false;
            do {
                System.out.print("Enter a number: ");

                //If the next value in the scanner is q or Q, then finish.
                if (scanner.hasNext("q") || scanner.hasNext("Q")) {
                    finish = true;
                } else {
                    /* While the scanner does not have a new integer and finish is not true,
                    then if next value is q or Q, finish. Else ask user for valid input */
                    while (!scanner.hasNextInt() && !finish) {
                        if (scanner.hasNext("q") || scanner.hasNext("Q")) {
                            finish = true;
                        } else {
                            System.out.print("Invalid input: Input must a valid Integer. Please try again:");
                            scanner.next();
                        }
                    }
                    //If there is a next integer, add it to the number list and print the current list
                    if (scanner.hasNextInt()) {
                        int newNumber = scanner.nextInt();
                        numList.add(newNumber);
                        System.out.println(numList);
                    }
                }
            } while (!finish);

            //Try to write the numList to the output stream to send to the server
            try {
                outputStream.writeObject(numList);
                outputStream.flush();
                System.out.println("\nValues have been sent to server, please see the server output.\n");
            } catch (Exception e) {
                System.out.println("\nError sending to server, please see the error message below\n");
                e.printStackTrace();
            }

            //Store the value returned from the server using the input stream and print it
            String serverReply = (String) inputStream.readObject();
            System.out.println(serverReply);

        } catch (Exception e) {
            System.err.println("Client Exception:  " + e.getMessage());
        }
    }
} // end main