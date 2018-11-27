package graph;

import util.PriorityQueue;

import java.util.*;

import data.Movie;

public class GraphAlgorithms {
    public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source){
        PriorityQueue Q = new PriorityQueue();
        int[] dist = new int[graph.numVertices() + 1];
        int[] prev = new int[graph.numVertices() + 1];

        for (int i = 1; i < dist.length; i++){
            dist[i] = Integer.MAX_VALUE - 1;
        }
        dist[source] = 0;


        for (int v = 1; v <= graph.numVertices(); v++){
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

        return prev;
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

    public static String graphInfo(Graph g) {
        String str = "";
        double numVerts = g.numVertices();
        str += "|V| = " + numVerts + " vertices \n";
        double numEdge = g.numEdges();
        str += "|E| = " + numEdge + " edges \n";
        double density = (numEdge / (numVerts * numVerts - 1));
        str += "Density = " + density + "\n";
        int maxDeg = 0;
        int maxNode = 0;
        for (int i = 1; i < numVerts + 1; i++) {
            int degree = g.degree(i);
            if (degree > maxDeg) {
                maxDeg = degree;
                maxNode = i;
            }
        }
        str += "Max. degree = " + maxDeg + "(node " + maxNode + ") \n";
        int[][] shortestPaths = floydWarshall(g);
        int maxPath = 0;
        int a = 0;
        int b = 0;
        double numPaths = 0;
        double totalWeight = 0;
        for (int i = 0; i < shortestPaths.length; i++) {
            for (int j = 0; j < shortestPaths[i].length; j++) {
                int path = shortestPaths[i][j];
                if (path < 1000000) {
                    if (path > maxPath) {
                        maxPath = path;
                        a = i + 1;
                        b = j + 1;
                    }
                    numPaths++;
                    totalWeight += path;
                }
            }
        }
        str += "Diameter = " + maxPath + "(from " + a + " to " + b + ") \n";
        double avgPathLength = totalWeight/numPaths;
        str += "Avg. path length = " + avgPathLength + "\n";
        return str;
    }

    public static String dispShortestPath(Map<Integer,Movie> movies, Graph g, int a, int b) {
        int[] prev = dijkstrasAlgorithm(g,a);
        int end = b;
        ArrayList<Integer> path = new ArrayList<>();
        path.add(end);
        while (prev[end] != 0) {
            end = prev[end];
            path.add(end);
        }
        String str = "";
        Movie m = movies.get(path.get(0));
        for (int i = 1; i < path.size(); i++) {
            Movie m2 = movies.get(path.get(i));
            str += m.getTitle() + " ===> " + m2.getTitle() + "\n";
            m = m2;
        }
        return str;
    }
	
    public static void nodeInfo(Map<Integer,Movie> movies, Graph graph, int index){
        List<Integer> adjIndex = graph.getNeighbors(index);
        Movie movie = movies.get(index);
        System.out.println(movie.toString());
        System.out.println("Neighbors: ");
        for (int i = 1; i <= adjIndex.size(); i++){
            Movie m = movies.get(adjIndex.get(i-1));
            String name = m.getTitle();
            System.out.println("    " + name);
        }
    }

    public static void scroll(int index, Map<Integer,Movie> movies){
        int ind = index;
        Movie movie = movies.get(ind);
        System.out.println(movie.toString());
        while(true) {
            System.out.println("\nEnter \"next\" and \"prev\" to browse movies or \"q\" to exit: ");
            if (com.equals("Next") || com.equals("Next ") || com.equals("next") || com.equals("next ")) {
                ind ++;
                Movie next = movies.get(ind);
                System.out.println(next.toString());
            } else if (com.equals("Prev") || com.equals("Prev ") || com.equals("prev") || com.equals("prev ")){
                ind --;
                Movie prev = movies.get(ind);
                System.out.println(prev.toString());
            } else if (com.equals("q")){
                System.out.println("Scroll ya later!");
                break;
            } else {
                System.out.println("Invalid Input");
            }
        }
    }
}
