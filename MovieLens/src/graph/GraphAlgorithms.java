package graph;
import util.PriorityQueue;
import java.util.Arrays;
import java.util.List;

public class GraphAlgorithms {

	public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source){
		PriorityQueue Q = new PriorityQueue();
        int[] dist = new int[graph.numVertices() + 1];
        Integer prev = null;
		for (int i = 0; i < dist.length; i++){
			dist[i] = Integer.MAX_VALUE;
		}
        dist[source] = 0;


		for (int v = 1; v <= graph.numVertices(); v ++){
            Q.push(v, dist[v]);
        }

        while (!Q.isEmpty()){
            int u = Q.topElement();

            Q.printHeap();
            Q.printMap();

            Q.pop();
            List<Integer> adjList = graph.getNeighbors(u);

            if (!adjList.isEmpty()) {
                for (int i = 0; i < adjList.size(); i++) {
                    int v = adjList.get(i);
                    int alt = dist[u] + 1;
                    if (alt < dist[v]) {
                        dist[v] = alt;
                        prev = u;
                        Q.changePriority(v, alt);
                    }
                }
            }
        }

        System.out.println(Arrays.toString(dist));
        return dist;
	}

	
}
