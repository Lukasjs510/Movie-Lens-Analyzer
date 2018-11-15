package graph;
import data.Movie;
import java.util.PriorityQueue;
import java.util.Set;

public class GraphAlgorithms {

	public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source){
		PriorityQueue Q = new PriorityQueue<Movie>();
        int[] dist = new int[graph.numEdges()];
        Movie prev = new Movie();
        prev = null;
		for (int i = 0; i < dist.length; i++){
			dist[i] = Integer.MAX_VALUE;
		}
        dist[source] = 0;

		for (int v = 0; v < graph.numVertices(); i ++){
            Q.push(v, dist[v]);
            System.out.println(v);
        }




        return int[] hold = new int[10];
	}

	
}
