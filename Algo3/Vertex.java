
/*
 * Name: Seah Hao Jian
 * GUID: 2228156S
 * 16 November 2015
 * Title: Algorithmics I (H) Assessed Assignment
 * This is my own work which is referred from the Assessed Coursework warmup
 * Lab exercise on moodle  
 */
import java.util.LinkedList;

/**
 * class to represent a vertex with its associated adjacent vertices represented
 * as AdjListNode objects
 */
public class Vertex implements Comparable<Vertex> {

	private LinkedList<AdjListNode> adjList; // the adjacency list
	private int index; // the index of this vertex in the graph
	private int weight;
	int predecessor; // index of predecessor vertex in a traversal

	// constructor
	public Vertex(int n) {
		adjList = new LinkedList<AdjListNode>();
		index = n;
		weight = Integer.MAX_VALUE;
	}

	// overloading constructor
	public Vertex(Vertex v) {
		adjList = v.getAdjList();
		index = v.getIndex();
	}

	public LinkedList<AdjListNode> getAdjList() {
		return adjList;
	}

	public void setWeight(int n) {
		weight = n;
	}

	public int getWeight() {
		return weight;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int n) {
		index = n;
	}

	public int getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(int n) {
		predecessor = n;
	}

	public void addToAdjList(int n, int w) {
		adjList.addLast(new AdjListNode(n, w));
	}

	public int vertexDegree() {
		return adjList.size();
	}

	@Override
	public int compareTo(Vertex other) {
		// over write the comparator so that it will compare against this class
		// weight otherwise the priority queue cannot compare
		return Integer.compare(this.getWeight(), other.getWeight());
	}
}
