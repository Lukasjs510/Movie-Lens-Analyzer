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

    /*
	A strong reccomendation might be a movie that not only appears on multiple shortest paths, but also as a neighbor
	to multiple liked movies. Displays highest occuring nodes first
	*/
    public static ArrayList<Integer> reccomendations(Map<Integer,Movie> movies, Graph g, ArrayList<Integer> A) {
        ArrayList<Integer> res;
        if (A.size() > 2) {
            ArrayList<int[]> noWay = new ArrayList<>();
            ArrayList<List<Integer>> neighbors = new ArrayList<>();
            for (int a : A) {
                noWay.add(GraphAlgorithms.dijkstrasAlgorithm(g, a));
                neighbors.add(g.getNeighbors(a));
            }
            int[] one = multiIntersection(noWay, movies.size());
            int[] two = howdyNeighbor(neighbors, movies.size());
            ArrayList<int[]> fin = new ArrayList<>();
            fin.add(one);
            fin.add(two);
            int[] hold = multiIntersection(fin, movies.size());
            ArrayList<Integer> rec = new ArrayList<>();
            for (int i = 0; i < hold.length; i++) {
                if (hold[i] > 1) {
                    rec.add(i + 1);
                }
            }
            res = rec;
        }
        else {
            int[] prev = GraphAlgorithms.dijkstrasAlgorithm(g, A.get(0));
            int end = A.get(1);
            ArrayList<Integer> path = new ArrayList<>();
            path.add(end);
            while (prev[end] != 0) {
                end = prev[end];
                path.add(end);
            }
            res = path;
        }
        return res;
    }

    public static int[] multiIntersection(ArrayList<int[]> A, int size) {
        int[] m = new int[size + 1];
        for (int[] a : A) {
            for (int i = 0; i < a.length - 1; i++) {
                if (a[i] > 0) {
                    m[i] = m[i] + 1;
                }
            }
        }
        return m;
    }

    public static int[] howdyNeighbor(ArrayList<List<Integer>> A, int size) {
        int[] m = new int[size + 1];
        for (List<Integer> a : A) {
            for (int t : a) {
                m[t] = m[t] + 1;
            }
        }
        return m;
    }

    public static void display(ArrayList<Integer> list, int size, Map<Integer, Movie> movies) {
        Scanner scan = new Scanner(System.in);
        boolean displaying = true;
        int range = 0;
        int top = size;
        while (displaying) {
            String str = "";
            for (int i = range; i < top; i++) {
                str += movies.get(list.get(i));
            }
            System.out.println(str);
            boolean deciding = true;
            while (deciding) {
                System.out.println("Displaying selections " + range + " through " + top + " of " +
                        list.size() + " total reccomendations");
                System.out.println("Previous, Next, or Quit: ");
                String line = scan.nextLine();
                line.toLowerCase();
                if (line.equals("previous") || line.equals("prev") || line.equals("p")) {
                    if (range == 0) {
                        System.out.println("No prior suggestions");
                    }
                    else if (range - size < 0){
                        int over = range - size;
                        over = Math.abs(over);
                        range = range - size + over;
                        top = top - size + over;
                        deciding = false;
                    }
                    else {
                        range = range - size;
                        top = top - size;
                        deciding = false;
                    }
                } else if (line.equals("next") || line.equals("n")) {
                    if (top == list.size()) {
                        System.out.println("No further Suggestions");
                    }
                    else if (top + size > list.size()){
                        int over = (top + size) - list.size();
                        range = range + size - over;
                        top = top + size - over;
                        deciding = false;
                    }
                    else {
                        range = range + size;
                        top = top + size;
                        deciding = false;
                    }
                } else if (line.equals("quit") || line.equals("q")) {
                    deciding = false;
                    displaying = false;
                } else {
                    System.out.println("Unrecognized command");
                }
            }
        }
    }

    public static void scroll(Map<Integer, Movie> movies, int size) {
        Scanner scan = new Scanner(System.in);
        boolean displaying = true;
        int range = 1;
        int top = size + 1;
        while (displaying) {
            String str = "";
            for (int i = range; i < top; i++) {
                str += movies.get(i);
            }
            System.out.println(str);
            boolean deciding = true;
            while (deciding) {
                System.out.println("Displaying movies " + range + " through " + (top - 1) + " of " +
                        movies.size() + " total Movies");
                System.out.println("Previous, Next, or Quit: ");
                String line = scan.nextLine();
                line.toLowerCase();
                if (line.equals("previous") || line.equals("prev") || line.equals("p")) {
                    if (range == 1) {
                        System.out.println("No prior movies");
                    }
                    else if (range - size < 1){
                        int over = range - size;
                        over = Math.abs(over);
                        range = range - size + over;
                        top = top - size + over;
                        deciding = false;
                    }
                    else {
                        range = range - size;
                        top = top - size;
                        deciding = false;
                    }
                } else if (line.equals("next") || line.equals("n")) {
                    if (top == movies.size()) {
                        System.out.println("No further Suggestions");
                    }
                    else if (top + size > movies.size()){
                        int over = (top + size) - movies.size();
                        range = range + size - over;
                        top = top + size - over;
                        deciding = false;
                    }
                    else {
                        range = range + size;
                        top = top + size;
                        deciding = false;
                    }
                } else if (line.equals("quit") || line.equals("q")) {
                    deciding = false;
                    displaying = false;
                } else {
                    System.out.println("Unrecognized command");
                }
            }
        }
    }

    public static void search(Map<Integer, Movie> movies) {
        Scanner scan = new Scanner(System.in);
        boolean searching = true;
        while (searching) {
            System.out.println("Would you like to search based on Title or Genre? (Q or quit to end searh)");
            String resp = scan.nextLine();
            resp = resp.toLowerCase();
            if (resp.equals("t") || resp.equals("title")) {
                ArrayList<Integer> found = new ArrayList<Integer>();
                System.out.println("Enter search term:");
                String look = scan.nextLine();
                look = look.toLowerCase();
                for (Integer i : movies.keySet()) {
                    Movie m = movies.get(i);
                    if (m.getTitle().toLowerCase().contains(look)) {
                        found.add(i);
                    }
                }
                if (found.size() >= 10) {
                    display(found, 10, movies);
                }
                else {
                    String res = "";
                    for (int i : found) {
                        res += movies.get(i);
                    }
                    System.out.println(res);
                }
            } else if (resp.equals("g") || resp.equals("genre")) {
                String found;
                found = "";
                System.out.println("Enter search term:");
                String look = scan.nextLine();
                look = look.toLowerCase();
                for (Integer i : movies.keySet()) {
                    Movie m = movies.get(i);
                    Set<String> S = m.getGenres();
                    for (String s : S) {
                        String g = s.toLowerCase();
                        if (g.contains(look)) {
                            found += m;
                        }
                    }
                }
                System.out.println("\n" + found);
            }
            else if (resp.equals("q") || resp.equals("quit")) {
                searching = false;
            }
            else {
                System.out.println("Unrecognized Command");
            }
        }
    }

    /*
     * This is a quick Sort implementation that keeps two arrays indexed values the same while actively sorting only the integer array.
     * @param first, left bound for starting sort comparisons
     * @param last, right bound for starting sort comparisons
     * @param names, the list of all server names
     * @param sizes, the list of all server sizes
     */
    private static void dualQuickSort(int first, int last, ArrayList<Integer> names, int[] sizes) {
        int left = first, right = last;
        int base = sizes[((last-first)/2) + first];

        while(left <= right){
            while(sizes[left] < base) {
                left ++;
            }
            while(sizes[right] > base){
                right --;
            }

            if (left <= right) {
                swap(left, right, names, sizes);
                left ++;
                right --;
            }
        }
        if (first < right) {
            dualQuickSort(first, right, names, sizes);
        }
        if (left < last) {
            dualQuickSort(left, last, names, sizes);
        }
    }

    /*
     * This method is used to swap indices and values for the dualQuickSort(int, int, String[], int[]).
     * @param index1, first index to be swapped
     * @param index2, second index to be  swapped
     * @param names, the list of all server names
     * @param sizes, the list of all server sizes
     */
    private static void swap(int index1, int index2, ArrayList<Integer> names, int[] sizes) {
        int theSwapShop = sizes[index1];
        int theSwapShopString = names.get(index1);

        sizes[index1] = sizes[index2];
        names.set(index1, names.get(index2));

        sizes[index2] = theSwapShop;
        names.set(index2, theSwapShopString);
    }
}
