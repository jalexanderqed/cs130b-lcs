all:
	g++ -g -std=c++0x -o findLCS main.cpp
clean:
	rm -f *.o findLCS
