import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PS9WeightedGraph {
	
	public static char[][] matrix;
	public static int numRows;
	public static int numCols;
	
	public static void main(String[] args) {
		
		if (args.length != 3) {
            System.out.println("Invalid command syntax.");
            System.out.println("Syntax: java PS9WeightedGraphTest <inputfile> <source> <destination>");
            System.exit(1);
        }

        String inputFile = args[0];
        char sourceVertex = args[1].charAt(0);
        char destinationVertex = args[2].charAt(0);

        Graph graph = new Graph();

        try {
            buildGraph(graph, inputFile);

            System.out.println("===== Graph G Information =====");
            System.out.print("Vertices: ");
            for (Vertex vertex : graph.getVertices()) {
                System.out.print(vertex.getName() + ", ");
            }
            System.out.println();
            
            System.out.println("# of edges: " + graph.getEdgesCount());

            System.out.print("Unreachable Vertices: ");
            List<String> unreachableVertices = findUnreachableVertices(graph, graph.getVertexByChar(destinationVertex));
            if (unreachableVertices.isEmpty()) {
                System.out.println("<none>");
            } else {
                System.out.println(String.join(", ", unreachableVertices));
            }

            System.out.print("Graph Max-Tolls: ");
            List<Integer> maxTolls = findGraphMaxTolls(graph, sourceVertex);
            System.out.println(String.join(", ", maxTolls.stream().map(Object::toString).toArray(String[]::new)));

            int smallestMaxToll = findSmallestMaxToll(graph, sourceVertex, destinationVertex);
            System.out.println("Smallest Max-Toll: " + smallestMaxToll);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
		
	}
	
	public static void buildGraph(Graph G, String inputFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String source = parts[0];
                String destination = parts[1];
                int weight = Integer.parseInt(parts[2]);

                Vertex sourceVertex = G.getSourceVertex();
                Vertex destVertex = G.getDestinationVertex();

                if (sourceVertex == null) {
                    sourceVertex = new Vertex(source);
                    G.addVertex(sourceVertex);
                }

                if (destVertex == null) {
                    destVertex = new Vertex(destination);
                    G.addVertex(destVertex);
                }

                G.addEdge(sourceVertex, destVertex, weight);
            }
        }

        br.close();
    }
	
	
	public static void runDijkstra(Graph g, Vertex s) {
        System.out.printf("[Step 2]:  Running Dijkstra's algorithm.%n");

        for (Vertex u : g.getVertices()) {
            u.setD(Integer.MAX_VALUE);
            u.setPi(null);
        }

        s.setD(0);

        PriorityQueue<Vertex> minHeap = new PriorityQueue<>(Comparator.comparingInt(Vertex::getD));
        minHeap.add(s);

        while (!minHeap.isEmpty()) {
            Vertex u = minHeap.poll();

            for (Vertex v : g.getAdjacencyList(u)) {
                relax(u, v, g, minHeap);
            }
        }

        System.out.printf("[Step 2]:  Dijkstra's algorithm complete.%n");
    }

    private static void relax(Vertex u, Vertex v, Graph g, PriorityQueue<Vertex> minHeap) {
        int weightUV = g.getWeight(u, v);
        int distanceThroughU = u.getD() + weightUV;

        if (distanceThroughU < v.getD()) {
            minHeap.remove(v);
            v.setD(distanceThroughU);
            v.setPi(u);
            minHeap.add(v);
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
	
	
	
	
	class Graph {

		private HashSet<Vertex> vertices;
		private HashMap<Vertex, List<Vertex>> adjList;
		private HashMap<String, Vertex> vertexMap;

		private Vertex source;
		private Vertex destination;

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
		public void addEdge(Vertex src, Vertex dest, int weight) {
            List<Vertex> srcNeighbors = adjList.get(src);

            if (srcNeighbors == null) {
                srcNeighbors = new ArrayList<>();
                adjList.put(src, srcNeighbors);
            }

            srcNeighbors.add(dest);
            dest.setWeight(src, weight);  // Assuming you have a setWeight method in your Vertex class.
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
		 * This method will set the destination vertex
		 */
		public void setDestinationVertex(Vertex u) {
			this.source = u;
		}

		/**
		 * This method will return the destination vertex
		 */
		public Vertex getDestinationVertex() {
			return this.destination;
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
	
	
		public int getWeight(Vertex u, Vertex v) {
	        return u.getWeight(v);
	    }
	
	
	
	
	
	}

}






import java.util.HashMap;
import java.util.Map;

public class Vertex {

    private String name;
    private int d;  
    private Vertex pi; 
    private Map<Vertex, Integer> weights; 
    private VertexColor color;  

    public enum VertexColor {
        WHITE, GRAY, BLACK
    }

    public Vertex(String name) {
        this.name = name;
        this.weights = new HashMap<>();
        this.color = VertexColor.WHITE;  // Initialize color to WHITE
    }

    public int hashCode() {
        if (name != null) {
            return name.hashCode();
        } else {
            return -1;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public Vertex getPi() {
        return pi;
    }

    public void setPi(Vertex pi) {
        this.pi = pi;
    }

    public VertexColor getColor() {
        return color;
    }

    public void setColor(VertexColor color) {
        this.color = color;
    }

    public void setWeight(Vertex neighbor, int weight) {
        weights.put(neighbor, weight);
    }

    public int getWeight(Vertex neighbor) {
        return weights.getOrDefault(neighbor, Integer.MAX_VALUE);
    }
}

