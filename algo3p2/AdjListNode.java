/*
 * Name: Seah Hao Jian
 * GUID: 2228156S
 * 16 November 2015
 * Title: Algorithmics I (H) Assessed Assignment
 * This code is referred from the Assessed Coursework warmup
 * Lab exercise on moodle  
 */


/**
 class to represent an entry in the adjacency list of a vertex
 in a graph
*/
public class AdjListNode {

	private int vertexNumber;
	private int weight;

	
	public AdjListNode(int n, int w){
		vertexNumber = n;
		weight = w;
	}
	
	public int getVertexNumber(){
		return vertexNumber;
	}
	
	public void setVertexNumber(int n){
		vertexNumber = n;
	}
	
	public void setWeight(int w) {
		weight = w;
	}
	
	public int getWeight() {
		return weight;
	}
}
