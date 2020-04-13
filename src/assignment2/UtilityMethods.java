package assignment2;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UtilityMethods {
    //A collection of utility methods to allow re-use between Iterative and Concurrent servers
    static int getResultSize(ObjectOutputStream objOutStream, ArrayList<Integer> result) {
        int resultSize = result.size();

        //try to write to the output object stream to return values to client
        try {
            objOutStream.writeObject("Element(s) in Subsequence: " + resultSize + "\nSub-Sequence Is: " + result.toString());
            objOutStream.flush();
            objOutStream.close();
            System.out.println("\nResult has been returned to the client, please see the client output.\n");
        } catch (Exception e) {
            System.err.println("Write Error " + e.getMessage());
        }
        return resultSize;
    }

    //Takes in subsequence and prints the unordered, ordered and actual subsequence
    static void printNumbersList(List<Integer> numbersList, ObjectOutputStream objOutStream, ArrayList<Integer> subsequence) {
        System.out.println("Total List " + numbersList);

        ArrayList<Integer> result;
        result = subsequence;

        int resultSize = getResultSize(objOutStream, result);
        System.out.println("Element(s) In Sub-Sequence: " + resultSize + "\nSub-Sequence Is: " + result.toString());
    }

    //Handles the creation of the subsequence
    static ArrayList<Integer> createSubSequence(List<Integer> array) {
        ArrayList<Integer> holder = new ArrayList<>();
        ArrayList<Integer> subsequence = new ArrayList<>();

        //Set the previousNum at -99999999 as it is unlikely to be used
        int previousNum = -999999999;
        boolean first = true;

        /*First iteration will set previous num to the first value it takes in,
        every iteration after should not change previous num at this point */
        for (Integer num : array) {
            if (first) {
                previousNum = num;
                holder.add(previousNum);
                first = false;
            } else {
                //If the number is 1 below the previous number, add it to the holder to build the subsequence
                if (previousNum == (num - 1)) {
                    holder.add(num);
                    previousNum = num;
                } else { //if it is not 1 below the previous number than holder is made new to create a new subsequence
                    holder = new ArrayList<>();
                    previousNum = num;
                    holder.add(num);
                }
            }
            //If the subsequence is smaller than the holder, then set the subsequence to the values stored in holder
            if (subsequence.size() < holder.size()) {
                subsequence = holder;
            }
        }
//        System.out.println(subsequence);
        return subsequence;
    }
}
