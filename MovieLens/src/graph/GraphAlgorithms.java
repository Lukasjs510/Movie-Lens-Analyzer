package graph;
import util.PriorityQueue;
import java.util.Arrays;
import java.util.List;

public class GraphAlgorithms {

	public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source){
		PriorityQueue Q = new PriorityQueue();
        int[] dist = new int[graph.numVertices() + 1];
        int[] prev = new int[graph.numVertices()];

		for (int i = 1; i < dist.length; i++){
			dist[i] = Integer.MAX_VALUE - 1;
		}
        dist[source] = 0;


		for (int v = 1; v <= graph.numVertices(); v ++){
            Q.push(dist[v], v);
        }

        while (!Q.isEmpty()){
            int u = Q.topElement();
            Q.pop();

            List<Integer> adjList = graph.getNeighbors(u);
            int size = adjList.size();
            for (int i = 0; i < size; i++) {
                int v = adjList.get(i);
                int alt = dist[u] + 1;
                if (alt < dist[v]) {
                    dist[v] = alt;
                    prev[v] = u;
                    Q.changePriority(v, alt);
                }
            }
        }

        System.out.println(Arrays.toString(dist));
        System.out.println(Arrays.toString(prev));
        return dist;
	}

	
}
