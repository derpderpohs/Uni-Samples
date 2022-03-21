
/*
 * Name: Seah Hao Jian
 * GUID: 2228156S
 * 16 November 2015
 * Title: Algorithmics I (H) Assessed Assignment
 * This code is referred from the Assessed Coursework warmup
 * Lab exercise on moodle  
 */
import java.util.LinkedList;


/**
 class to represent a  vertex in a graph
*/
public class Vertex {
   
    private LinkedList<AdjListNode> adjList ; // the adjacency list 
    private int index; // the index of this vertex in the graph
    private int weight;
	boolean visited; // whether vertex has been visited in a traversal

     
    /**
	 creates a new instance of Vertex
	 */
    public Vertex(int n) {
    	adjList = new LinkedList<AdjListNode>();
    	index = n;
    	visited = false;
    	weight = Integer.MAX_VALUE;
    }
    
    public Vertex(Vertex v){
    	adjList = v.getAdjList();
    	index = v.getIndex();
    	visited = v.getVisited();
    }
     
    
    public LinkedList<AdjListNode> getAdjList(){
        return adjList;
    }
    
    
    public void setWeight(int n) {
    	weight = n;
    }
    
    public int getWeight() {
    	return weight;
    }
    
    public int getIndex(){
    	return index;
    }
    
    public void setIndex(int n){
    	index = n;
    }
    
    public boolean getVisited(){
    	return visited;
    }
    
    public void setVisited(boolean b){
    	visited = b;
    }   
    
    public void addToAdjList(int n, int w){
        adjList.addLast(new AdjListNode(n, w));
    }
    
    public int vertexDegree(){
        return adjList.size();
    }

}
