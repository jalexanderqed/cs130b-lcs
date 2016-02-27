import java.util.ArrayList;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Main {
    final static int DIAGONAL = 1;
    final static int UP = 2;
    final static int LEFT = 3;

    public static void samePath(ArrayList<Tuple> a, ArrayList<Tuple> b){
        if(a.size() != b.size())
    }

    public static void copyVecTups( ArrayList<Tuple> old, ArrayList<Tuple> newVec) {
        for (Tuple t : old) {
            newVec.add(new Tuple(t));
        }
    }

    public static void copyVec( ArrayList<ArrayList<Tuple>> old, ArrayList<ArrayList<Tuple>> newVec) {
        for (ArrayList<Tuple> t : old) {
            ArrayList<Tuple> n = new ArrayList<Tuple>();
            copyVecTups(t, n);
            newVec.add(n);
        }
    }

    public static void copyVecWithNewTup( ArrayList<ArrayList<Tuple>> old, ArrayList<ArrayList<Tuple>> newVec, Tuple tup) {
        if (old.size() == 0 && newVec.size() == 0) {
            newVec.add(new ArrayList < Tuple >());
            newVec.get(0).add(tup);
        }
        for (int i = 0; i < old.size(); i++) {
            ArrayList<Tuple> n = new ArrayList<Tuple>();
            copyVecTups(old.get(i), n);
            n.add(tup);
            newVec.add(n);
        }
    }

    public static void analyzeAll(String first, String second) {
        System.out.println("Starting all on " + first + " " + second);

        int width = first.length();
        int height = second.length();
        
        ArrayList<ArrayList<ArrayList<ArrayList<Tuple>>>> vals = new ArrayList<ArrayList<ArrayList<ArrayList<Tuple>>>>();

        for(int i = 0; i <= width; i++){
            vals.add(new ArrayList<ArrayList<ArrayList<Tuple>>>());
            for(int j = 0; j <= height; j++){
                vals.get(i).add(new ArrayList<ArrayList<Tuple>>());
            }
        }

        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    copyVecWithNewTup(vals.get(i - 1).get(j - 1), vals.get(i).get(j), new Tuple(i - 1, j - 1));
                }

                int myLength = vals.get(i).get(j).size() == 0 ? 0 : vals.get(i).get(j).get(0).size();
                int upLength = vals.get(i).get(j - 1).size() == 0 ? 0 : vals.get(i).get(j - 1).get(0).size();
                int leftLength = vals.get(i - 1).get(j).size() == 0 ? 0 : vals.get(i - 1).get(j).get(0).size();

                if (myLength > 0) {
                    if (upLength == myLength) {
                        copyVecWithNewTup(vals.get(i).get(j - 1), vals.get(i).get(j), new Tuple(i - 1, j - 1));
                    }
                    if (leftLength == myLength) {
                        copyVecWithNewTup(vals.get(i - 1).get(j), vals.get(i - 1).get(j), new Tuple(i - 1, j - 1));
                    }
                } else {
                    if (upLength != 0 && upLength == leftLength) {
                        copyVecWithNewTup(vals.get(i).get(j - 1), vals.get(i).get(j), new Tuple(i - 1, j - 1));
                        copyVecWithNewTup(vals.get(i - 1).get(j), vals.get(i - 1).get(j), new Tuple(i - 1, j - 1));
                    } else if (upLength > leftLength) {
                        copyVecWithNewTup(vals.get(i).get(j - 1), vals.get(i).get(j), new Tuple(i - 1, j - 1));
                    } else if (leftLength != 0) {
                        copyVecWithNewTup(vals.get(i - 1).get(j), vals.get(i - 1).get(j), new Tuple(i - 1, j - 1));
                    }
                }
            }
        }

        ArrayList<ArrayList<Tuple>> result = vals.get(width).get(height);
        System.out.println(result.size());
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
        }
        catch(IOException e){}
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
    }
}