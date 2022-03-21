
/*
 * Name: Seah Hao Jian
 * GUID: 2228156S
 * 16 November 2015
 * Title: Algorithmics I (H) Assessed Assignment
 * This is my own work as defined in the Academic Ethics agreement I
 *	have signed.
 */
import java.util.ArrayDeque;
import java.util.Deque;

/*
 * This class is for representing a Path as we traverse the graph 
*/

public class Path {
	
	private int weight;
	private Deque<Integer> route;
	
	public Path() {
		weight = -1;
		route = new ArrayDeque<Integer>();
	}
	
	public void setWeight (int w) {
		weight = w;
	}
	
	public int getWeight () {
		return weight;
	}
	
	public void setRoute(Deque<Integer> a) {
		route.clear();
		route.addAll(a);
	}
	public Deque<Integer> getRoute() {
		return route;
	}
	
	public void addToRoute(int r) {
		route.addLast(r);
	}
	
	public void removeLastRoutePosition() {
		if (route.size() > 0) {
		route.removeLast();
	}
}
}
