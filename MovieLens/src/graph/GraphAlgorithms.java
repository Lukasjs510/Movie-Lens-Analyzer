package graph;

import java.util.PriorityQueue;

public class GraphAlgorithms {
	public dijkstrasAlgorithm(Graph<Integer> graph, int source){
		PriorityQueue Q = new PriorityQueue<Movie>();

		int[] dist = new int[graph.numEdges()];

		for (int i = 0; i < dist.length; i++){
			dist[i] = Integer.MAX_VALUE;
		}

		Movie prev = new Movie();
		prev = null;

		dist[source] = 0;

		
		
	}
	
}
