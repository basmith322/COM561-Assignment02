package assignment2;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UtilityMethods {
    static int getResultSize(ObjectOutputStream out, ArrayList<Integer> result) {
        int resultSize;
        resultSize = result.size();

        try {
            out.writeObject(resultSize + " Elements In Sub-Sequence\nSub-Sequence Is: " + result.toString());
            out.flush();
            out.close();
            System.out.println("\nResult has been returned to the client, please see the client output.\n");
        } catch (Exception e) {
            System.err.println("Write Error " + e.getMessage());
        }
        return resultSize;
    }

    static void printUL(List<Integer> numList, ObjectOutputStream out, ArrayList<Integer> subsequence) {
        System.out.println("Unordered List: " + numList);
        Collections.sort(numList);
        System.out.println("Ordered List: " + numList);

        ArrayList<Integer> result;
        result = subsequence;

        int resultSize = getResultSize(out, result);
        System.out.println(resultSize + " Element(s) In Sub-Sequence\nSub-Sequence Is: " + result.toString());
    }

    static ArrayList<Integer> getIntegers(List<Integer> array) {
        ArrayList<Integer> holder1 = new ArrayList<>();
        ArrayList<Integer> subsequence = new ArrayList<>();
        int previousNum = -999999999;

        for (Integer num : array) {
            if (previousNum == -999999999) {
                previousNum = num;
                holder1.add(num);
            } else {
                if (num == (previousNum + 1)) {
                    holder1.add(num);
                } else {
                    holder1 = new ArrayList<>();
                }
                previousNum = num;
            }
            if (subsequence.size() < holder1.size()) {
                subsequence = holder1;
            }
        }
        System.out.println(subsequence);
        return subsequence;
    }
}
