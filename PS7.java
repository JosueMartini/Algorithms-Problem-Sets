import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PS7 {

	public static char[][] matrix;
	public static int numRows;
	public static int numCols;

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

		if (algorithm.equals("dfs")) {
			runDFS(G, G.getSourceVertex(), G.getVertexByCoordinates(9, 7));
		} else {
			runBFS(G, G.getSourceVertex());
		}

		System.out.printf("[Step 2]:  Complete.%n%n");

		/********************************************
		 * Print Output
		 ********************************************/
		System.out.printf("[Step 3]:  Building graph output.%n");

		// Build the output as indicated in the problem set.
		BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));

		List<String> shortestPathCoordinates = printPath(G.getSourceVertex(), G.getVertexByCoordinates(9, 7), args[0]);

		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				char cell = matrix[i][j];

				String currentCoordinate = i + "_" + j;
				if (shortestPathCoordinates.contains(currentCoordinate)) {
					bw.write('X');
				} else if (cell == 'S') {
					bw.write('S');
				} else if (cell == 'M') {
					bw.write('M');
				} else {
					bw.write(cell);
				}
			}
			bw.newLine();
		}

		bw.close();

		System.out.printf("[Step 3]:  Complete.%n");

	}

	public static void buildGraph(Graph G, String inputFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line;

		List<String> lines = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		numRows = lines.size();
		numCols = lines.get(0).length();
		matrix = new char[numRows][numCols];

		for (int i = 0; i < numRows; i++) {
			matrix[i] = lines.get(i).toCharArray();
		}

		br.close();

		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				char cell = matrix[i][j];

				if (cell == ' ' || cell == 'S' || cell == 'M') {
					Vertex curr = G.getVertexByCoordinates(i, j);
					if (curr == null) {
						curr = new Vertex(i + "_" + j);
						G.addVertex(curr);
						G.getVertexMap().put(curr.getName(), curr);
					}

					if (cell == 'S') {
						G.setSourceVertex(curr);
						curr.setName("S");
					} else if (cell == 'M') {
						curr.setName("M");
					}

					// set left neighbor
					if (j > 0 && (matrix[i][j - 1] == ' ' || matrix[i][j - 1] == 'M' || matrix[i][j - 1] == 'S')) {
						Vertex leftNeigh = G.getVertexByCoordinates(i, j - 1);
						if (leftNeigh == null) {
							leftNeigh = new Vertex(i + "_" + (j - 1));
							G.addVertex(leftNeigh);
							G.getVertexMap().put(leftNeigh.getName(), leftNeigh);
						}
						G.addEdge(curr, leftNeigh);
					}
					// set right neighbor
					if (j < numCols - 1
							&& (matrix[i][j + 1] == ' ' || matrix[i][j + 1] == 'M' || matrix[i][j + 1] == 'S')) {
						Vertex rightNeigh = G.getVertexByCoordinates(i, j + 1);
						if (rightNeigh == null) {
							rightNeigh = new Vertex(i + "_" + (j + 1));
							G.addVertex(rightNeigh);
							G.getVertexMap().put(rightNeigh.getName(), rightNeigh);
						}
						G.addEdge(curr, rightNeigh);
					}
					// set top neighbor
					if (i > 0 && (matrix[i - 1][j] == ' ' || matrix[i - 1][j] == 'M' || matrix[i - 1][j] == 'S')) {
						Vertex topNeigh = G.getVertexByCoordinates(i - 1, j);
						if (topNeigh == null) {
							topNeigh = new Vertex((i - 1) + "_" + j);
							G.addVertex(topNeigh);
							G.getVertexMap().put(topNeigh.getName(), topNeigh);
						}
						G.addEdge(curr, topNeigh);
					}
					// set bottom neighbor
					if (i < numRows - 1
							&& (matrix[i + 1][j] == ' ' || matrix[i + 1][j] == 'M' || matrix[i + 1][j] == 'S')) {
						Vertex bottomNeigh = G.getVertexByCoordinates(i + 1, j);
						if (bottomNeigh == null) {
							bottomNeigh = new Vertex((i + 1) + "_" + j);
							G.addVertex(bottomNeigh);
							G.getVertexMap().put(bottomNeigh.getName(), bottomNeigh);
						}
						G.addEdge(curr, bottomNeigh);
					}
				}
			}
		}
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
			Vertex u = queue.remove();

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

	private static List<String> printPath(Vertex source, Vertex target, String algorithm) {
		List<String> coordinates = new ArrayList<>();
		Vertex current = target;

		while (current != null) {
			coordinates.add(0, current.getName());
			current = current.getPi();
		}

		return coordinates;
	}

	public static void runDFS(Graph g, Vertex s, Vertex destination) {
		System.out.printf("[Step 2]:  Running DFS algorithm.%n");

		int time = 0;
		for (Vertex u : g.getVertices()) {
			u.setColor(Vertex.VertexColor.WHITE);
			u.setPi(null);
		}

		time = dfsVisit(g, s, destination, time);
	}

	public static int dfsVisit(Graph G, Vertex u, Vertex destination, int time) {
		time++;
		u.setD(time);
		u.setColor(Vertex.VertexColor.GRAY);

		for (Vertex v : G.getAdjacencyList(u)) {
			if (v.getColor() == Vertex.VertexColor.WHITE) {
				v.setPi(u);
				if (v.equals(destination)) {
					time++;
					v.setColor(Vertex.VertexColor.BLACK);
					return time;
				}
				time = dfsVisit(G, v, destination, time);
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
	private HashMap<String, Vertex> vertexMap;

	private Vertex source;

	public Graph() {
		this.vertices = new HashSet<Vertex>();
		this.adjList = new HashMap<>();
		this.vertexMap = new HashMap<>();

	}

	/**
	 * This method will add a vertex to the graph.
	 */
	public void addVertex(Vertex u) {
		vertices.add(u);
		adjList.put(u, new ArrayList<>());
		vertexMap.put(u.getName(), u);
	}

	/**
	 * This method will remove a vertex from the graph.
	 */
	public void removeVertex(Vertex u) {
		vertices.remove(u);
		List<Vertex> neighbors = adjList.remove(u);

		for (Vertex neighbor : neighbors) {
			adjList.get(neighbor).remove(u);
		}
	}

	/**
	 * This method will add an edge to vertex src to destination vertex dest.
	 * 
	 */
	public void addEdge(Vertex src, Vertex dest) {
		src.setColor(Vertex.VertexColor.WHITE);
		dest.setColor(Vertex.VertexColor.WHITE);
		List<Vertex> srcNeighbors = adjList.get(src);

		if (srcNeighbors == null) {
			srcNeighbors = new ArrayList<>();
			adjList.put(src, srcNeighbors);
		}

		srcNeighbors.add(dest);
	}

	/**
	 * This method will remove the edge from vertex src to destination vertex dest.
	 */
	public void removeEdge(Vertex src, Vertex dest) {
		List<Vertex> srcNeighbors = adjList.get(src);
		if (srcNeighbors != null) {
			srcNeighbors.remove(dest);

			if (srcNeighbors.isEmpty()) {
				adjList.remove(src);
			}
		}
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
		List<Vertex> neighbors = adjList.get(u);

		if (neighbors != null) {
			return new HashSet<>(neighbors);
		} else {
			return new HashSet<>();
		}

	}

	public HashSet<Vertex> getVertices() {
		return this.vertices;
	}

	public Vertex getVertexByCoordinates(int row, int col) {
		String vertexName = row + "_" + col;
		return vertexMap.get(vertexName);
	}

	public HashMap<String, Vertex> getVertexMap() {
		return this.vertexMap;
	}

}
