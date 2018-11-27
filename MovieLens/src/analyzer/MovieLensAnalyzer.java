package analyzer;

import data.Movie;
import data.Reviewer;
import graph.Graph;
import graph.GraphAlgorithms;
import util.DataLoader;
import util.PriorityQueue;
import java.util.*;

public class MovieLensAnalyzer {
	private static Map<Integer,Movie> movies;

	public static void main(String[] args){
		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}
		System.out.println("========== Welcome to MovieLens Analyzer ==========");
        String movieLocS = args[0];
		String ratingsLocS = args[1];
		String dataDirS = "/src/ml-latest-small/";

		System.out.println("The files being analyzed are: \n" + ratingsLocS + "\n" + movieLocS);
		Graph graph = queryGraph(dataDirS, ratingsLocS, movieLocS);
		queryGraphActions(graph);
	}

	private static Graph queryGraph(String dataDirS, String ratingsLocS, String movieLocS){
		String dir = System.getProperty("user.dir");
		DataLoader data = new DataLoader();
		data.loadData(dir + dataDirS + movieLocS,  dir + dataDirS + ratingsLocS);
		Graph<Integer> graph = new Graph<>();
		movies = data.getMovies();
		Map<Integer,Reviewer> reviewers = data.getReviewers();
		//Fill graph
		for (int i = 1; i < movies.size() + 1; i++) {
			graph.addVertex(movies.get(i).getMovieId());
		}
		//Set edges
		PriorityQueue priorityQueue = new PriorityQueue();
		ArrayList<Reviewer> userWeight = new ArrayList<>();
		for (Integer i : reviewers.keySet()) {
			Reviewer r = reviewers.get(i);
			priorityQueue.push(r.numRated(),r.getReviewerId());
		}
		while (!priorityQueue.isEmpty()) {
			int p = priorityQueue.topElement();
			userWeight.add(reviewers.get(p));
			priorityQueue.pop();
		}

		Scanner scan = new Scanner(System.in);
		System.out.println("There are 3 choices for defining adjacency: " +
				"\n[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies" +
				"\n[Option 2] u and v are adjacent if the same 12 users watched both movies (regardless of rating)" +
				"\n[Option 3] u is adjacent to v if at least 33.0% of the users users that rated u gave the same rating to v" +
				"\nChose an option to build a graph (1-3):");
		int option = scan.nextInt();

		System.out.print("Creating graph...");

		//Option 1
		if (option == 1) {
			for (Integer a : movies.keySet()) {
				for (Integer b : movies.keySet()) {
					Movie u = movies.get(a);
					Movie v = movies.get(b);
					if (v.getMovieId() != u.getMovieId()) {
						int common = 0;
						int ind = 0;
						int set = 12;
						while (common < set && ind < userWeight.size()) {
							Reviewer r = userWeight.get(ind);
							if (r.ratedMovie(u.getMovieId()) && r.ratedMovie(v.getMovieId())) {
								if (r.getMovieRating(u.getMovieId()) == r.getMovieRating(v.getMovieId())) {
									common++;
								}
							}
							ind++;
						}
						if (common == set) {
							graph.addEdge(u.getMovieId(), v.getMovieId());
						}
					}
				}
			}
		}
		else if (option == 2) {
			//Option 2
			for (Integer a : movies.keySet()) {
				for (Integer b : movies.keySet()) {
					Movie u = movies.get(a);
					Movie v = movies.get(b);
					if (v.getMovieId() != u.getMovieId()) {
						int common = 0;
						int ind = 0;
						int set = 12;
						while (common < set && ind < userWeight.size() - 1) {
							Reviewer r = userWeight.get(ind);
							if (r.ratedMovie(u.getMovieId()) && r.ratedMovie(v.getMovieId())) {
								common++;
							}
							ind++;
						}
						if (common == set) {
							graph.addEdge(u.getMovieId(), v.getMovieId());
						}
					}
				}
			}
		}
		else if (option == 3) {
			//Option 3
			for (Integer a : movies.keySet()) {
				for (Integer b : movies.keySet()) {
					Movie u = movies.get(a);
					Movie v = movies.get(b);
					if (v.getMovieId() != u.getMovieId()) {
						int common = 0;
						int ind = 0;
						int pool = 0;
						while (ind < userWeight.size() - 1) {
							Reviewer r = userWeight.get(ind);
							if (r.ratedMovie(u.getMovieId()) && r.ratedMovie(v.getMovieId())) {
								pool++;
								if (r.getMovieRating(u.getMovieId()) == r.getMovieRating(v.getMovieId())) {
									common++;
								}
							}
							ind++;
						}
						if (pool > 0) {
							if (common/pool >= 0.33) {
								graph.addEdge(u.getMovieId(), v.getMovieId());
							}
						}
					}
				}
			}
		} else {
			System.out.println("Invalid input.");
		}
		System.out.println("the graph has been created.");
		return graph;
	}

	private static void queryGraphActions(Graph graph){
		Scanner scan = new Scanner(System.in);
		while(true){
			System.out.println("\nGraph Actions: " +
					"\n[Option 0] Search" +
					"\n[Option 1] Print out statistics about the graph" +
					"\n[Option 2] Print node information" +
					"\n[Option 3] Display shortest path between two nodes" +
					"\n[Option 4] Quit" +
					"\n[Option 5] Reccomendations" +
					"\n[Option 6] Explore Movies" +
					"\nChose an option (1-6):");
			int option = scan.nextInt();
			String po = "";
			if (option == 0) {
				GraphAlgorithms.search(movies);
			}
			else if(option == 1){
				po = GraphAlgorithms.graphInfo(graph);
				System.out.println(po);
			} else if (option == 2){
				System.out.println("Enter movie ID(1-1000): ");
				int movieID = scan.nextInt();
				GraphAlgorithms.nodeInfo(movies, graph, movieID);
			} else if (option == 3){
				System.out.println("Enter starting node: ");
				int startingNode = scan.nextInt();
				System.out.println("Enter ending node: ");
				int endingNode = scan.nextInt();
				po = GraphAlgorithms.dispShortestPath(movies, graph, startingNode, endingNode);
				System.out.println(po);
			} else if (option == 4){
				System.out.println("To the Heights!");
				break;
			} else if (option == 5){
				System.out.println("Enter a comma delinated list of movies you enjoyed (1-1000): ");
				ArrayList<Integer> liked = new ArrayList<>();
				String list = scan.next();
				String[] List = list.split(",");
				for (String s : List) {
					liked.add(Integer.parseInt(s));
				}
				ArrayList<Integer> listOfRec = GraphAlgorithms.reccomendations(movies,graph,liked);
				if (listOfRec.size() > 10) {
					int size;
					if (listOfRec.size() < 25) {
						size = 10;
						System.out.println("Our reccomendations for you include: ");
						GraphAlgorithms.display(listOfRec,size, movies);
					}
					else if (listOfRec.size() > 25 && listOfRec.size() < 50) {
						System.out.println("How many would you like displayed at a time? (10, 25): ");
						size = scan.nextInt();
						System.out.println("Our reccomendations for you include: ");
						GraphAlgorithms.display(listOfRec,size, movies);
					}
					else {
						System.out.println("How many would you like displayed at a time? (10, 25, 50): ");
						size = scan.nextInt();
						System.out.println("Our reccomendations for you include: ");
						GraphAlgorithms.display(listOfRec,size, movies);
					}
				}
				else {
					System.out.println("Our reccomendations for you include: ");
					String str = "";
					for (int i : listOfRec) {
						str += movies.get(i);
					}
					System.out.println(str);
				}
			}
			else if (option == 6) {
				System.out.println("How many would you like displayed at a time? (10, 25, 50): ");
				int size = scan.nextInt();
				GraphAlgorithms.scroll(movies, size);
			}
			else {
				System.out.println("Invalid input.");
			}
		}
	}
}
