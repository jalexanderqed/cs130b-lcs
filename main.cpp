#include <iostream>
#include <string>

using namespace std;

int main() {
    string first;
    getline(cin, first);
    int numCases = stoi(first);

    cout << "Num cases: " << numCases << endl;

    return 0;
}