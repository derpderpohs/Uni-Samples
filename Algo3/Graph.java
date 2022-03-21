
/*
 * Name: Seah Hao Jian
 * GUID: 2228156S
 * 16 November 2015
 * Title: Algorithmics I (H) Assessed Assignment
 * This is my own work which is referred from the Assessed Coursework warmup
 * Lab exercise on moodle.
 * The method djikstra() and getPath() is referred from 
 * http://en.literateprograms.org/index.php?title=Special%3aDownloadCode/Dijkstra%27s_algorithm_%28Java%29&oldid=15444
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * class to represent the graph which holds the vertices and the algorithm
 * implementation
 */
public class Graph {

	private Vertex[] vertices; // the (array of) vertices
	private int numVertices = 0; // number of vertices

	public Graph(Vertex[] vList) {
		numVertices = vList.length;
		vertices = vList;
	}

	public int size() {
		return numVertices;
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

	/**
	 * This method holds the Dijkstra algorithm implementation
	 */
	public Vertex[] dijkstra(int srcIndex, int destIndex) {

		PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>(); // this is
																	// for stuff
																	// not
																	// already
																	// relaxed

		for (Vertex v : vertices) {
			if (v.getIndex() == srcIndex) {
				// if currentnode index is the same index

				v.setPredecessor(-1);
				// set predecessor index as -1 to indicate it does not exist ie
				// src node
				v.setWeight(0);
				// weight of first node is always 0
				queue.add(v);
				// add into the set that has done relaxation
			}
		}

		while (!queue.isEmpty()) {
			// for each vertex in that has been relaxed
			// check adjancency, if node is in list then ignore
			Vertex v = queue.poll();
			// will get the first result in the list ie the src node

			LinkedList<AdjListNode> adjnodes = v.getAdjList();

			for (AdjListNode n : adjnodes) {
				Vertex node = vertices[n.getVertexNumber()];

				int newWeight = v.getWeight() + n.getWeight();
				// get the new weight of the vertex from the current node
				if (node.getWeight() > newWeight) {
					// remove adjnode if it exists in queue since we need update
					// it.
					queue.remove(node);
					// if the newweight is more we set the new weight to the
					// node
					node.setWeight(newWeight);
					// changing values also mean that a better path is found,
					// update predecessor.
					node.setPredecessor(v.getIndex());
					// add the new/newly updated node into the queue, as we want
					// to check and see
					// if it contains a better path with its updated value.
					queue.add(node);
				}
			}

			if (v.getIndex() == destIndex) {
				// once we read the destination, we dont want to check for path
				// anymore
				// so return back since best path is already computed.
				return vertices;
			}
		}
		// if there are no vertices then we cannot compute so return null
		return null;

	}

	public String getPath(int dest) {

		Vertex v = vertices[dest];
		ArrayList<Integer> bestpath = new ArrayList<>();
		String route = "";

		for (;;) {
			bestpath.add(v.getIndex());
			if (v.getPredecessor() == -1) {
				break;
			}
			v = vertices[v.getPredecessor()];
		}

		Collections.reverse(bestpath);

		for (int n : bestpath) {
			route += n + " ";
		}
		return route;

	}

}
