import java.util.ArrayList;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Main {

    // Used to indicate direction to proceed during basic LCS search
    final static int DIAGONAL = 1;
    final static int UP = 2;
    final static int LEFT = 3;

    /*
    Copies all entries from one Hashtable<String, Integer> into another without changing entries.
     */
    public static void copySet(Hashtable<String, Integer> old, Hashtable<String, Integer> newVec) {
        for (String str : old.keySet()) {
            newVec.put(str, old.get(str));
        }
    }

    /*
    Copies all entries from one Hashtable<String, Integer> into another and applies the stringified
    version of the provided Tuple to the end of each key.
    In practice, each of these strings represents a path to reach the current grid position and the
    corresponding integer is the length of that path. All lengths within the same Hashtable should be
    equal.
     */
    public static void copySetWithNewTup(Hashtable<String, Integer> old,
                                         Hashtable<String, Integer> newVec, Tuple tup) {
        if (old.size() == 0 && newVec.size() == 0) {
            newVec.put(tup.toString(), 1);
        }
        for (String str : old.keySet()) {
            String newStr = str + ", " + tup.toString();
            newVec.put(newStr, old.get(str) + 1);
        }
    }

    /*
    Called for the "-all" command line option.
    Works by iteratively constructing the set of maximum-length subsequence paths by which each
    node in an MxN matrix can be reached. These are stored as hashtables with the key (a String)
    representing the path in the format specified in the project assignment and the integer value
    indicating the length of the path.
    This hashtable structure is necessary to ensure that each stored path for a particular node
    is unique. Failure to fulfill this requirement massively decreases the efficiency of the
    algorithm and requires removing duplicates at the end. Since operations on a path are
    append-only, I found the String to be a reasonable, if somewhat inefficient, mode of storing
    a path. Additionally, it works easily with hashtables and prevents having to generate the node
    list at the end when printing.
    At each update step, the algorithm checks if the current characters in the first and second
    string are equal. If they are, all paths to the diagonal-up-left node are copied in with the
    current position appended to each of the paths.
    After this check, the algorithm checks the lengths of the paths of the paths to the nodes
    to the top and left. If either is equal (at this point) to the length of the path to the
    current node, the paths from that direction are copied in. If there is no path to the current
    node (i.e. the current characters differed), the node (left or top) with the greater length
    (or both if the lengths are equal) has its paths copied into the current node.
    At the end of the algorithm, the lower right node contains the set of all paths to that node,
    which are stored as the String keys of its hashtable.
     */
    public static void analyzeAll(String first, String second) {
        // System.out.println("Starting all on " + first + " " + second);
        // System.out.println(first.length());
        // System.out.println(second.length());

        int width = first.length();
        int height = second.length();

        ArrayList<ArrayList<Hashtable<String, Integer>>> vals = new ArrayList<ArrayList<Hashtable<String, Integer>>>();

        // Initialize all rows and elements in the table.
        for (int i = 0; i <= width; i++) {
            vals.add(new ArrayList<Hashtable<String, Integer>>());
            for (int j = 0; j <= height; j++) {
                vals.get(i).add(new Hashtable<String, Integer>());
            }
        }

        // Iterate through the matrix starting at the upper left, and update each node
        // with the possible paths to it.
        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {

                // If the current characters in the two strings are equal
                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    // Copy paths to the diagonal up-left node, appending the current node to each path
                    copySetWithNewTup(vals.get(i - 1).get(j - 1), vals.get(i).get(j), new Tuple(i, j));
                }

                // Initialized to the distance to the upper, left and current nodes according to their paths.
                int myLength, upLength, leftLength;

                if (vals.get(i).get(j).size() == 0) {
                    myLength = 0;
                } else {
                    myLength = vals.get(i).get(j).values().iterator()
                            .next().intValue();
                }

                if (vals.get(i).get(j - 1).size() == 0) {
                    upLength = 0;
                } else {
                    upLength = vals.get(i).get(j - 1).values().iterator()
                            .next().intValue();
                }

                if (vals.get(i - 1).get(j).size() == 0) {
                    leftLength = 0;
                } else {
                    leftLength = vals.get(i - 1).get(j).values().iterator()
                            .next().intValue();
                }

                // Copies paths from the upper node, left node, or both depending on the distances to them.
                if (myLength > 0) {
                    if (upLength == myLength) {
                        copySet(vals.get(i).get(j - 1), vals.get(i).get(j));
                    }
                    if (leftLength == myLength) {
                        copySet(vals.get(i - 1).get(j), vals.get(i).get(j));
                    }
                } else {
                    if (upLength != 0 && upLength == leftLength) {
                        copySet(vals.get(i).get(j - 1), vals.get(i).get(j));
                        copySet(vals.get(i - 1).get(j), vals.get(i).get(j));
                    } else if (upLength > leftLength) {
                        copySet(vals.get(i).get(j - 1), vals.get(i).get(j));
                    } else if (leftLength != 0) {
                        copySet(vals.get(i - 1).get(j), vals.get(i).get(j));
                    }
                }
                // System.out.println(i + ", " + j);
            }
        }

        Hashtable<String, Integer> result = vals.get(width).get(height);
        for(String str : result.keySet()) {
            System.out.println("(" + str + ")");
        }
    }

    /*
    Prints the longest common subsequence according to the directions stored
    in the "dirs" parameter. Only the first string of the two being compared
    is passed into the algorithm (this is all that is necessary).
     */
    public static void printLcs(String str, int[][] dirs, int x, int y) {
        if (x == 0 || y == 0) return;

        // If the current characters were found to be equivalent and
        // part of the LCS, print them and recurse on the diagonal-up-left
        // node.
        if (dirs[x][y] == DIAGONAL) {
            printLcs(str, dirs, x - 1, y - 1);
            System.out.print(str.charAt(x - 1));
        }
        // If the left node has a longer path, recurse on it
        else if (dirs[x][y] == LEFT) {
            printLcs(str, dirs, x - 1, y);
        }
        // Ditto for upper node
        else {
            printLcs(str, dirs, x, y - 1);
        }
    }

    /*
    Executes the basic LCS algorithm described in class, constructing both a record
    of the LCS distances to each node and directions for how to navigate through the
    LCS sequence.
     */
    public static int analyze(String first, String second, boolean print) {
        int width = first.length();
        int height = second.length();
        int[][] vals = new int[width + 1][height + 1];
        int[][] dirs = new int[width + 1][height + 1];

        for (int i = 0; i <= width; i++)
            vals[i][0] = 0;
        for (int i = 0; i <= height; i++)
            vals[0][i] = 0;

        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
                // If equal, move diagonally, increment current LCS length,
                // and record diagonal movement in the directions matrix.
                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    vals[i][j] = vals[i - 1][j - 1] + 1;
                    dirs[i][j] = DIAGONAL;
                }

                // Take the longer path from the left or top node, and indicate
                // that it's path should be followed when reconstructing the LCS.
                else if (vals[i - 1][j] >= vals[i][j - 1]) {
                    vals[i][j] = vals[i - 1][j];
                    dirs[i][j] = LEFT;
                } else {
                    vals[i][j] = vals[i][j - 1];
                    dirs[i][j] = UP;
                }
            }
        }

        // Print length of the LCS and invoke method to print
        // the LCS of the current two nodes.
        if (print) {
            System.out.print(vals[width][height] + " ");
            printLcs(first, dirs, width, height);
            System.out.println();
        }

        return vals[width][height];
    }

    public static void main(String[] args) {
        try {
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(System.in));
            String num = br.readLine();
            int numCases = Integer.parseInt(num); // Get number of cases to read.

            for (int i = 0; i < numCases; i++) {
                String line = br.readLine();
                String[] strs = line.split(" "); // Get two strings to compare.
                String first = strs[0];
                String second = strs[1];

                if (args.length > 0 && args[0].equals("-all")) {
                    analyzeAll(first, second); // Invoke algorithm to find all LCS paths.
                    System.out.println();
                } else {
                    analyze(first, second, true); // Invoke algorithm to find text of LCS.
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read from console");
        }
    }

    /*
    Helper class to short-term store a position in the matrix
    and convert it to a string per the format in the project
    description.
     */
    private static class Tuple {
        private int x;
        private int y;

        Tuple(int a, int b) {
            x = a;
            y = b;
        }

        public boolean equals(Object o) {
            Tuple other = (Tuple) o;
            return x == other.x && y == other.y;
        }

        public String toString() {
            return "<" + x + ", " + y + ">";
        }
    }
}