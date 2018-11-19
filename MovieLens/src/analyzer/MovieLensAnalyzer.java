package analyzer;

import data.Movie;
import data.Reviewer;
import graph.Graph;
import graph.GraphAlgorithms;
import util.DataLoader;
import util.PriorityQueue;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Map;

public class MovieLensAnalyzer {
	
	public static void main(String[] args){

		// Your program should take two command-line arguments: 
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genres
		
		//if(args.length != 2){
		//	System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
		//	System.exit(-1);
		//}
		int option = 1;
		String movieLocS = "movies.csv/";
		String ratingsLocS = "ratings.csv/";
		String dataDirS = "/MovieLens/src/ml-latest-small/";
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
		else {
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
		}

		System.out.println(graph);
		int[][] shortestPaths = GraphAlgorithms.floydWarshall(graph);
		System.out.println(graph.numVertices());
		System.out.println(graph.numEdges());
		System.out.println("Hi there");
	}
}
