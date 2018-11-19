package graph;

import java.util.List;

public class GraphAlgorithms {

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
