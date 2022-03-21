
/*
 * Name: Seah Hao Jian
 * GUID: 2228156S
 * 16 November 2015
 * Title: Algorithmics I (H) Assessed Assignment
 * This is my own work which is referred from the Assessed Coursework warmup
 * Lab exercise on moodle with the implementation of 
 * tryPath() which is referred from the pseudocode in 
 * the assignment specifications.
 */


/**
 class to represent an undirected graph using adjacency lists
 */
public class Graph {

	private Vertex[] vertices; // the array of vertices
	private Path bestPath, currentPath; //Path objects representing current path traversing and the logged Best Path
	
	/**
	 creates a new instance of Graph with n vertices
	*/
	public Graph(Vertex[] vList) {
		vertices = vList;
		bestPath = new Path();
		currentPath = new Path();
	}

	public Vertex getVertex(int i) {
		return vertices[i];
	}

	public void setVertex(int i) {
		vertices[i] = new Vertex(i);
	}
	
	public void setVertexList(Vertex[] vList) {
		vertices = vList;
	}
	
	//this method is called when we want to do start our backtrack algorithm
	public void addPath(int src) {	
		//add the source into the path obj representing the current path we are tracking
		currentPath.addToRoute(src);
		bestPath.setWeight(Integer.MAX_VALUE);
	}
	
	
	//this method holds the main backtrack algorithm logic.
	public void tryPath(int src, int dest) {
		 Vertex source = vertices[src];
		 source.setVisited(true);
		 //add current src vertex into path
		for (AdjListNode n : source.getAdjList()) {
			
			int currWeight = currentPath.getWeight() == -1 ? 0 : currentPath.getWeight();
			Vertex current = vertices[n.getVertexNumber()];
			
			if (!current.getVisited() && currWeight + n.getWeight() < bestPath.getWeight()) {
				//set the node that has been visited to be true
				current.setVisited(true);
				//add the adjnode into the current path
				currentPath.addToRoute(n.getVertexNumber());
				//update the currentweight
				currentPath.setWeight(currWeight + n.getWeight());
				
					if (current.getIndex() == dest) {												
							//update the lowest weight
							bestPath.setWeight(currentPath.getWeight());
							bestPath.setRoute(currentPath.getRoute());						
					} else {	
					tryPath(current.getIndex(), dest);
				}
					//after analyzing all nodes for this particular branch, backtrack and remove itself to update currentpath
					currentPath.removeLastRoutePosition();
					//then set itself to not visited so it can be used from another node in another path.
					current.setVisited(false);
					//reset the weight
					currentPath.setWeight(currentPath.getWeight() - n.getWeight());				
			} 
		}		
	}

	
	public String getPath(int dest) {
		String path = "";
		for (int i : bestPath.getRoute()) {
			path += i + " ";
		}
		return path;
		
	}

	public int getBestPathWeight () {
		return bestPath.getWeight();
	}
}
