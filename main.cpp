#include <iostream>
#include <string>
#include <cstring>
#include <vector>

using namespace std;

const int DIAGONAL = 1;
const int UP = 2;
const int LEFT = 3;

struct Tuple {
    int x;
    int y;

    Tuple(int a, int b) {
        x = a;
        y = b;
    }

    Tuple(const Tuple &t) {
        x = t.x;
        y = t.y;
    }

    Tuple& operator=(const Tuple &t){
        x = t.x;
        y = t.y;
        return *this;
    }
};

void copyVec(const vector<Tuple>& old, vector<Tuple>& newVec) {
    cout << "In first copy" << endl;
    for (Tuple t : old) {
        newVec.push_back(Tuple(t));
    }
}

void copyVec(const vector <vector<Tuple>>& old, vector <vector<Tuple>>& newVec) {
    cout << "In second copy" << endl;
    for (vector<Tuple> t : old) {
        vector<Tuple> n;
        copyVec(t, n);
        newVec.push_back(n);
    }
}

void copyVecWithNewTup(const vector <vector<Tuple>>& old, vector <vector<Tuple>>& newVec, Tuple tup) {
    cout << "In copy with tup" << endl;
    if(old.size() == 0 && newVec.size() == 0){
        newVec.push_back(vector<Tuple>());
        newVec[0].push_back(tup);
    }
    for (vector<Tuple> t : old) {
        vector<Tuple> n;
        copyVec(t, n);
        n.push_back(tup);
        newVec.push_back(n);
    }
}

void analyzeAll(string first, string second) {
    int width = first.length();
    int height = second.length();

    vector <vector<Tuple>> **vals = new vector <vector<Tuple>> *[width + 1];

    for (int i = 0; i <= width; i++) {
        vals[i] = new vector<vector<Tuple>>[height + 1];
    }

    for (int i = 1; i <= width; i++) {
        for (int j = 1; j <= height; j++) {
            cout << "in loop" << endl;

            if (first[i - 1] == second[j - 1]) {
                copyVecWithNewTup(vals[i - 1][j - 1], vals[i][j], Tuple(i - 1, j - 1));
            }

            int myLength = vals[i][j].size() == 0 ? 0 : vals[i][j][0].size();
            int upLength = vals[i][j - 1].size() == 0 ? 0 : vals[i][j - 1][0].size();
            int leftLength = vals[i - 1][j].size() == 0 ? 0 : vals[i - 1][j][0].size();

            /*
            cout << "myLength: " << myLength << endl;
            cout << "upLength: " << upLength << endl;
            cout << "leftLength: " << leftLength << endl;
*/

            if(myLength > 0) {
                if (upLength == myLength) {
                    copyVecWithNewTup(vals[i][j - 1], vals[i][j], Tuple(i - 1, j - 1));
                }
                if (leftLength == myLength) {
                    copyVecWithNewTup(vals[i - 1][j], vals[i - 1][j], Tuple(i - 1, j - 1));
                }
            }
            else{
                if(upLength != 0 && upLength == leftLength){
                    copyVecWithNewTup(vals[i][j - 1], vals[i][j], Tuple(i - 1, j - 1));
                    copyVecWithNewTup(vals[i - 1][j], vals[i - 1][j], Tuple(i - 1, j - 1));
                }
                else if(upLength > leftLength){
                    copyVecWithNewTup(vals[i][j - 1], vals[i][j], Tuple(i - 1, j - 1));
                }
                else if(leftLength != 0){
                    copyVecWithNewTup(vals[i - 1][j], vals[i - 1][j], Tuple(i - 1, j - 1));
                }
            }
        }
    }

    vector <vector<Tuple>> result = vals[width][height];
    cout << result.size();
}

void printLcs(string str, int **dirs, int x, int y) {
    if (x == 0 || y == 0) return;
    if (dirs[x][y] == DIAGONAL) {
        printLcs(str, dirs, x - 1, y - 1);
        cout << str[x - 1];
    }
    else if (dirs[x][y] == LEFT) {
        printLcs(str, dirs, x - 1, y);
    }
    else {
        printLcs(str, dirs, x, y - 1);
    }
}

int analyze(string first, string second, bool print) {
    int width = first.length();
    int height = second.length();
    int **vals = new int *[width + 1];
    int **dirs = new int *[width + 1];

    for (int i = 0; i <= width; i++) {
        vals[i] = new int[height + 1];
        dirs[i] = new int[height + 1];
        vals[i][0] = 0;
    }
    for (int i = 0; i <= height; i++)
        vals[0][i] = 0;

    for (int i = 1; i <= width; i++) {
        for (int j = 1; j <= height; j++) {
            if (first[i - 1] == second[j - 1]) {
                vals[i][j] = vals[i - 1][j - 1] + 1;
                dirs[i][j] = DIAGONAL;
            }
            else if (vals[i - 1][j] >= vals[i][j - 1]) {
                vals[i][j] = vals[i - 1][j];
                dirs[i][j] = LEFT;
            }
            else {
                vals[i][j] = vals[i][j - 1];
                dirs[i][j] = UP;
            }
        }
    }

    /*
    for (int j = 0; j <= height; j++) {
        for (int i = 0; i <= width; i++) {
            cout << (vals[i][j] < 10 ? "" : "") << vals[i][j] << " ";
        }
        cout << endl;
    }
     */

    if (print) {
        cout << vals[width][height] << " ";
        printLcs(first, dirs, width, height);
        cout << endl;
    }

    return vals[width][height];
}

int main(int argc, char **argv) {
    string first;
    getline(cin, first);
    int numCases = stoi(first);

    for (int i = 0; i < numCases; i++) {
        string line;
        getline(cin, line);
        int div = line.find(' ');
        string first = line.substr(0, div);
        string second = line.substr(div + 1);
        if (argc > 1 && strcmp(argv[1], "-all") == 0) {
            analyzeAll(first, second);
            cout << endl;
        }
        else {
            analyze(first, second, true);
        }
    }

    return 0;
}