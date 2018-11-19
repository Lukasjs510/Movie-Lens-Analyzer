package graph;

import util.PriorityQueue;
import java.util.Arrays;
import java.util.List;
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

	public static int[][] floydWarshall(Graph<Integer> graph) {
	    int n = graph.numVertices();
        int[][] A = new int[n][n];
        for (int i = 1; i < n; i++) {
            List<Integer> neigbors = graph.getNeighbors(i);
            for (int j = 1; j < n; j++) {
                if (!neigbors.isEmpty()) {
                    if (i == j) {
                        A[i-1][j-1] = 0;
                    } else if (neigbors.contains(j)) {
                        A[i-1][j-1] = 1;
                    }
                    else {
                        A[i-1][j-1] = 1000000;
                    }
                }
                else {
                    A[i-1][j-1] = 1000000;
                }
            }
        }
        int[][] last = A;
        int[][] B = new int[n][n];
        for (int k = 1; k < n; k++) {
            B = new int[n][n];
            for (int i = 1; i < n; i++) {
                for (int j = 1; j < n; j++) {
                    B[i-1][j-1] = Math.min(last[i-1][j-1], last[i-1][k-1] + last[k-1][j-1]);
                }
            }
            last = B;
        }
        return B;
    }
}
