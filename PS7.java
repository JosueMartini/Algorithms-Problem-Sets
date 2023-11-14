import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PS7 {

	public static void main(String[] args) throws IOException {

		String studentName = "Josue Martinez";

		System.out.println("*******************************************************");
		System.out.printf("*  Problem Set 7:  Graph Algorithms                   *%n");
		System.out.printf("*  Student Name:   %-30s     *%n", studentName);
		System.out.println("*******************************************************\n");

		if (args.length != 3) {
			System.out.println("Invalid command syntax.");
			System.out.println("Syntax:   java PS7 <bfs/dfs> <inputfile> <outputfile>");
			System.exit(1);
		}

		args = new String[] { "abc", "def", "abc" };

		String algorithm = args[0];
		String infile = args[1];
		String outfile = args[2];

		Graph G = new Graph();

		/********************************************
		 * Constructing the graph
		 ********************************************/

		System.out.printf("[Step 1]:  Constructing the graph for input file:  %s %n", infile);

		PS7.buildGraph(G, infile);

		System.out.printf("[Step 1]:  Complete.  There are %d vertices. %n%n", G.getVertices().size());

		/********************************************
		 * Run algorithm
		 ********************************************/
		System.out.printf("[Step 2]:  Calling graph algorithm.%n");

		if (algorithm == "dfs") {
//			runDFS(G, v);
		} else {
//			runBFS(G, v);
		}

		System.out.printf("[Step 2]:  Complete.%n%n");

		/********************************************
		 * Print Output
		 ********************************************/
		System.out.printf("[Step 3]:  Building graph output.%n");

		// Build the output as indicated in the problem set.

		System.out.printf("[Step 3]:  Complete.%n");

	}

	public static void buildGraph(Graph G, String inputFile) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line;
		int numRows = 0;
		int row = 0;

		while ((line = br.readLine()) != null) {
			numRows++;
			for (int i = 0; i < line.length(); i++) {
				char cell = line.charAt(i);

				Vertex curr = new Vertex(row + "_" + i);
				G.addVertex(curr);

				if (cell == 'S') {
					G.setSourceVertex(curr);
				} else if (cell == 'M') {
					G.addVertex(curr);
				} else if (cell == 'x' || cell == ' ') {
					curr.setName(Character.toString(cell));
				}

				// set left neighbor
				if (i > 0 && line.charAt(i - 1) != ' ') {
					Vertex leftNeigh = new Vertex(row + "_" + (i - 1));
					G.addVertex(leftNeigh);
					G.addEdge(curr, leftNeigh);
				}
				// set right neighbor
				if (i < line.length() - 1 && line.charAt(i + 1) != ' ') {
					Vertex rightNeigh = new Vertex(row + "_" + (i + 1));
					G.addVertex(rightNeigh);
					G.addEdge(curr, rightNeigh);
				}
				// set top neighbor
				if (row > 0 && line.charAt(i) != ' ') {
					Vertex topNeigh = new Vertex((row - 1) + "_" + i);
					G.addVertex(topNeigh);
					G.addEdge(curr, topNeigh);
				}
				// set bottom neighbor
				if (row < numRows - 1 && line.charAt(i) != ' ') {
					Vertex bottomNeigh = new Vertex((row + 1) + "_" + i);
					G.addVertex(bottomNeigh);
					G.addEdge(curr, bottomNeigh);
				}

			}

			row++;

		}

		br.close();

	}

	public static void runBFS(Graph g, Vertex s) {

		System.out.printf("[Step 2]:  Running BFS algorithm.%n");

		for (Vertex u : g.getVertices()) {
			u.setColor(Vertex.VertexColor.WHITE);
			u.setD(Integer.MAX_VALUE);
			u.setPi(null);
		}

		s.setColor(Vertex.VertexColor.GRAY);
		s.setD(0);
		s.setPi(null);

		Queue<Vertex> queue = new LinkedList<>();
		queue.add(s);

		while (!queue.isEmpty()) {
			Vertex u = queue.poll();
			for (Vertex v : g.getAdjacencyList(u)) {
				if (v.getColor() == Vertex.VertexColor.WHITE) {
					v.setColor(Vertex.VertexColor.GRAY);
					v.setD(u.getD() + 1);
					v.setPi(u);
					queue.add(v);
				}
			}
			u.setColor(Vertex.VertexColor.BLACK);
		}

	}

	public static void runDFS(Graph g, Vertex s) {

		System.out.printf("[Step 2]:  Running DFS algorithm.%n");

		int time = 0;
		for (Vertex u : g.getVertices()) {
			u.setColor(Vertex.VertexColor.WHITE);
			u.setPi(null);
		}

		for (Vertex u : g.getVertices()) {
			if (u.getColor() == Vertex.VertexColor.WHITE) {
				time = dfsVisit(g, u, time);
			}
		}

	}

	public static int dfsVisit(Graph G, Vertex u, int time) {
		time++;
		u.setD(time);
		u.setColor(Vertex.VertexColor.GRAY);

		for (Vertex v : G.getAdjacencyList(u)) {
			if (v.getColor() == Vertex.VertexColor.WHITE) {
				v.setPi(u);
				time = dfsVisit(G, v, time);
			}
		}

		time++;
		u.setColor(Vertex.VertexColor.BLACK);

		return time;
	}

}

class Graph {

	private HashSet<Vertex> vertices;
	private HashMap<Vertex, List<Vertex>> adjList;

	private Vertex source;

	public Graph() {
		this.vertices = new HashSet<Vertex>();
		this.adjList = new HashMap<>();
	}

	/**
	 * This method will add a vertex to the graph.
	 */
	public void addVertex(Vertex u) {
		vertices.add(u);
		adjList.put(u, null);

	}

	/**
	 * This method will remove a vertex from the graph.
	 */

	public void removeVertex(Vertex u) {
		vertices.remove(u);
		adjList.remove(u);
	}

	/**
	 * This method will add an edge to vertex src to destination vertex dest.
	 * 
	 */
	public void addEdge(Vertex src, Vertex dest) {

	}

	/**
	 * This method will remove the edge from vertex src to destination vertex dest.
	 */

	public void removeEdge(Vertex src, Vertex dest) {

	}

	/**
	 * This method will set the source vertex
	 */
	public void setSourceVertex(Vertex u) {
		this.source = u;
	}

	/**
	 * This method will return the source vertex
	 */
	public Vertex getSourceVertex() {
		return this.source;
	}

	/**
	 * This method will return the adjacency list for vertex u.
	 */
	public HashSet<Vertex> getAdjacencyList(Vertex u) {

		return null;
	}

	public HashSet<Vertex> getVertices() {
		return this.vertices;
	}

}
