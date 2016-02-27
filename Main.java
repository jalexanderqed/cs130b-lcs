import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Iterator;

public class Main {
    final static int DIAGONAL = 1;
    final static int UP = 2;
    final static int LEFT = 3;

    public static void copySet(Hashtable<String, Integer> old, Hashtable<String, Integer> newVec) {
        for (String str : old.keySet()) {
            newVec.put(str, old.get(str));
        }
    }

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

    public static void analyzeAll(String first, String second) {
        // System.out.println("Starting all on " + first + " " + second);
        // System.out.println(first.length());
        // System.out.println(second.length());

        int width = first.length();
        int height = second.length();

        ArrayList<ArrayList<Hashtable<String, Integer>>> vals = new ArrayList<ArrayList<Hashtable<String, Integer>>>();

        for (int i = 0; i <= width; i++) {
            vals.add(new ArrayList<Hashtable<String, Integer>>());
            for (int j = 0; j <= height; j++) {
                vals.get(i).add(new Hashtable<String, Integer>());
            }
        }

        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    copySetWithNewTup(vals.get(i - 1).get(j - 1), vals.get(i).get(j), new Tuple(i, j));
                }

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

    public static void printLcs(String str, int[][] dirs, int x, int y) {
        if (x == 0 || y == 0) return;
        if (dirs[x][y] == DIAGONAL) {
            printLcs(str, dirs, x - 1, y - 1);
            System.out.print(str.charAt(x - 1));
        } else if (dirs[x][y] == LEFT) {
            printLcs(str, dirs, x - 1, y);
        } else {
            printLcs(str, dirs, x, y - 1);
        }
    }

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
                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    vals[i][j] = vals[i - 1][j - 1] + 1;
                    dirs[i][j] = DIAGONAL;
                } else if (vals[i - 1][j] >= vals[i][j - 1]) {
                    vals[i][j] = vals[i - 1][j];
                    dirs[i][j] = LEFT;
                } else {
                    vals[i][j] = vals[i][j - 1];
                    dirs[i][j] = UP;
                }
            }
        }

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
            int numCases = Integer.parseInt(num);

            for (int i = 0; i < numCases; i++) {
                String line = br.readLine();
                String[] strs = line.split(" ");
                String first = strs[0];
                String second = strs[1];

                if (args.length > 0 && args[0].equals("-all")) {
                    analyzeAll(first, second);
                    System.out.println();
                } else {
                    analyze(first, second, true);
                }
            }
        } catch (IOException e) {
        }
    }

    private static class Tuple {
        private int x;
        private int y;

        Tuple(int a, int b) {
            x = a;
            y = b;
        }

        Tuple(Tuple t) {
            x = t.x;
            y = t.y;
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