all:
	g++ -g -std=c++11 -o findLCS main.cpp
clean:
	rm -f *.o findLCS
