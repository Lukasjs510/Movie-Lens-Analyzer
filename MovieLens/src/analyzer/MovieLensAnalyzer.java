package analyzer;

import data.Movie;
import data.Reviewer;
import graph.Graph;
import graph.GraphAlgorithms;
import util.DataLoader;
import util.PriorityQueue;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class MovieLensAnalyzer {

	public static void main(String[] args){
		System.out.println("========== Welcome to MovieLens Analyzer ==========");
		// Your program should take two command-line arguments:
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genres

		//if(args.length != 2){
		//	System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
		//	System.exit(-1);
		//}

		String movieLocS = "movies.csv/";
		String ratingsLocS = "ratings.csv/";
		String dataDirS = "/MovieLens/src/ml-latest-small/";

		System.out.println("The files being analyzed are: \n" + ratingsLocS + "\n" + movieLocS);
		Graph graph = queryGraph(dataDirS, ratingsLocS, movieLocS);
		queryGraphActions(graph);

	}

	private static Graph queryGraph(String dataDirS, String ratingsLocS, String movieLocS){
		String dir = System.getProperty("user.dir");
		DataLoader data = new DataLoader();
		data.loadData(dir + dataDirS + movieLocS,  dir + dataDirS + ratingsLocS);
		Graph<Integer> graph = new Graph<>();
		Map<Integer,Movie> movies = data.getMovies();
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
		for (int i = 0; i < priorityQueue.size(); i++) {
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

		//Option 1 ~ 10 seconds
		if (option == 1) {
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
			//Option 2 ~ 10 seconds
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
			//Option 3 ~ 30 seconds
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
					"\n[Option 1] Print out statistics about the graph" +
					"\n[Option 2] Print node information" +
					"\n[Option 3] Display shortest path between two nodes" +
					"\n[Option 4] Quit" +
					"\nChose an option (1-4):");
			int option = scan.nextInt();
			if(option == 1){

			} else if (option == 2){

			} else if (option == 3){

			} else if (option == 4){
				break;
			} else {
				System.out.println("Invalid input.");
			}
		}
	}

}

