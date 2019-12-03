# CS112
Listed below are the projects I completed for CS112(Data Structures) at Rutgers along with a short description of each project.
## BigInteger
* A program that implements a BigInteger, a data type that can store numbers larger than the `int` data type is able to.
* Will parse signed numerical values and is able to perform basic operations such as addition, subtraction, and multiplication given two numbers.
* This implementation uses linked list nodes to store digits of given numbers.

## Expression
* A program that parses an infix expression that may contain integer constants, variables, arrays, any operator, and parenthesized subexpressions.
* My implementation uses Dijkstra's Shunting Yard Algorithm to construct a binary expression tree from infix. 

## Trie
* Implements the Trie data structure to store an array of words through the use of linked lists.
* Also contains a method that efficiently searches for words that start with a given prefix.

## Friends
* Implements some useful algorithms for graphs that represent friendships. 
* Contains a method that computes the shortest chain between two people through mutual friends. Uses a breadth-first search algorithm to find the shortest path.
* Implements a method that identifies cliques in the graph given a category, in the case of the friendship graph, "universities" is the category used to identify certain people in the graph. Uses a depth-first search algorithm to find cliques.
* Contains a method that identifies "connectors", or cut vertices/articulation points, in the graph. Implements Tarjan's algorithm which uses depth-first search.

