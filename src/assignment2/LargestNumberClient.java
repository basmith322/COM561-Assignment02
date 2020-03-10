package assignment2;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@SuppressWarnings("serial")
public class LargestNumberClient implements Serializable {
        public static void main(String[] args) {
        String hostName = "localhost"; // default host name
        int hostPort = 4444; // default host port
        // assign host machine name and port to connect to
        if (args.length != 0) {
            if (args[0] != null) {
                hostName = args[0]; // user specified machine
            }
            if (args[1] != null) {
                hostPort = Integer.parseInt(args[1]); // user specified port
            }
        }

        System.out.println("Connecting to Find-Largest-Number Server");
        // connect to server and extract input and output streams
        try (Socket serverSocket = new Socket(hostName, hostPort);
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream())) {

            // create client input stream for user input
            Scanner scanner = new Scanner(System.in);
            // Initialise The Number And ArrayList
            List<Integer> numList = new ArrayList;

            do {
                System.out.print("Type a single integer value: ");
                //if(!scanner.hasNext("q") && !scanner.hasNext("Q")){
                while (!scanner.hasNextInt()) {
                    System.out.print("Invalid input: Integer Required (Try again):");
                    scanner.next();
                }
                
                //number = scanner.nextInt();
                numList.add(scanner.nextInt());
                
                // send the int to the server
                //os.write(number);
               // os.flush();
            } while (!scanner.hasNext("q") && !scanner.hasNext("Q"));
            try {
                out.writeObject(numList);
                out.flush();
                System.out.println("\nSerialization Successful... Checkout your specified output file..\n");
            } catch (Exception e) {
                    e.printStackTrace();
            }
            String serverReply;
            //serverReply = is.readLine();
           // System.out.println(serverReply);
            
            serverReply = (String)in.readObject();
            System.out.println(serverReply);
            
            //os.close();
            //is.close();


        } catch (Exception e) {
            System.err.println("Client Exception:  " + e.getMessage());
        }
    } // end main

}
