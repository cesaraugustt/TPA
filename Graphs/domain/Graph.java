package domain;

import java.util.*;

public class Graph<T> {

    private List<Vertex<T>> vertices;
    private Map<Vertex<T>, List<Edge<T>>> adjacencyList;

    public Graph() {
        vertices = new ArrayList<>();
        adjacencyList = new HashMap<>();
    }

    // Construtor de cópia profunda para evitar mutação do grafo original
    public Graph(Graph<T> other) {
        this.vertices = new ArrayList<>();
        this.adjacencyList = new HashMap<>();

        // Clona todos os vértices
        for (Vertex<T> vertex : other.getVertices()) {
            this.addVertex(vertex.getValue());
        }

        // Clona todas as arestas
        for (Vertex<T> vertex : other.getVertices()) {
            List<Edge<T>> edges = other.getAdjacencyList().get(vertex);
            if (edges != null) {
                for (Edge<T> edge : edges) {
                    this.addEdge(
                            edge.getOrigin().getValue(),
                            edge.getDestination().getValue(),
                            edge.getCapacity()
                    );
                }
            }
        }
    }

    public void addVertex(T value) {
        for (Vertex<T> vertex : vertices) {
            if (vertex.getValue().equals(value)) {
                return;
            }
        }

        Vertex<T> vertex = new Vertex<>(value);

        vertices.add(vertex);
        adjacencyList.put(vertex, new ArrayList<>());
    }

    public void addEdge(T origin, T destination, float capacity) {
        Vertex<T> originVertex = null;
        Vertex<T> destinationVertex = null;

        for (Vertex<T> vertex : vertices) {
            if (vertex.getValue().equals(origin)) {
                originVertex = vertex;
            }

            if (vertex.getValue().equals(destination)) {
                destinationVertex = vertex;
            }
        }

        if (originVertex != null && destinationVertex != null) {
            adjacencyList.get(originVertex)
                    .add(new Edge<>(
                            originVertex,
                            destinationVertex,
                            capacity
                    ));
        }
    }

    public void printGraph() {
        for (Vertex<T> vertex : vertices) {
            System.out.print(vertex + " -> ");

            for (Edge<T> edge : adjacencyList.get(vertex)) {
                System.out.print(
                        edge.getDestination()
                                + "("
                                + edge.getCapacity()
                                + ") "
                );
            }

            System.out.println();
        }
    }

    public void bfs(T startValue) {
        Vertex<T> start = null;

        for (Vertex<T> vertex : vertices) {
            if (vertex.getValue().equals(startValue)) {
                start = vertex;
            }
        }

        if (start == null) return;

        Queue<Vertex<T>> queue = new LinkedList<>();
        List<Vertex<T>> visited = new ArrayList<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Vertex<T> current = queue.poll();

            System.out.println(current);

            for (Edge<T> edge : adjacencyList.get(current)) {
                Vertex<T> neighbor = edge.getDestination();

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
    }

    public List<Vertex<T>> getVertices() {
        return vertices;
    }

    public Map<Vertex<T>, List<Edge<T>>> getAdjacencyList() {
        return adjacencyList;
    }
}
