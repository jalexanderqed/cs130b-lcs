#include <iostream>
#include <string>
#include <cstring>

using namespace std;

const int DIAGONAL = 1;
const int UP = 2;
const int LEFT = 3;

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

void analyze(string first, string second) {
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

    /* for (int j = 0; j <= height; j++) {
        for (int i = 0; i <= width; i++) {
            cout << (vals[i][j] < 10 ? "" : "") << vals[i][j] << " ";
        }
        cout << endl;
    }*/

    printLcs(first, dirs, width, height);
    cout << endl;
}

int main(int arc, char **argv) {
    string first;
    getline(cin, first);
    int numCases = stoi(first);

    for (int i = 0; i < numCases; i++) {
        string line;
        getline(cin, line);
        int div = line.find(' ');
        string first = line.substr(0, div);
        string second = line.substr(div + 1);

        analyze(first, second);
    }

    return 0;
}