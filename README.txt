My implementation of the basic LCS algorithm follows the algorithm given in class and in the lab description.

My implementation of the all-LCS algorithm relies on building a table of possible paths to each node, which is expanded in a manner similar to that in the basic algorithm. A more full description is given in the comments in Main.java.

Main.java contains all code for the project.
findLCS is an executable that compiles the java code and runs the Main class with whatever command line arguments are passed to it.
Makefile does not have any function except removing all Java class files when the "clean" target is invoked. This is just in case you still wanted that functionality for Java projects.