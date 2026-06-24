import java.util.*;

public class FordFulkerson<T> {

    private Edge<T> findEdge(
            Graph<T> graph,
            Vertex<T> origin,
            Vertex<T> destination
    ) {
        for (Edge<T> edge :
                graph.getAdjacencyList().get(origin)) {

            if (edge.getDestination() == destination) {
                return edge;
            }
        }

        return null;
    }

    private boolean dfsPath(
            Graph<T> graph,
            Vertex<T> current,
            Vertex<T> sink,
            List<Edge<T>> path,
            List<Vertex<T>> visited
    ) {
        if (current == sink) {
            return true;
        }

        visited.add(current);

        for (Edge<T> edge :
                graph.getAdjacencyList().get(current)) {

            if (edge.getCapacity() <= 0) {
                continue;
            }

            Vertex<T> next =
                    edge.getDestination();

            if (!visited.contains(next)) {

                path.add(edge);

                if (dfsPath(
                        graph,
                        next,
                        sink,
                        path,
                        visited
                )) {
                    return true;
                }

                path.remove(path.size() - 1);
            }
        }

        return false;
    }

    private Vertex<T> findVertex(
            Graph<T> graph,
            T value
    ) {
        for (Vertex<T> vertex :
                graph.getVertices()) {

            if (vertex.getValue().equals(value)) {
                return vertex;
            }
        }

        return null;
    }

    public float calculate(
            Graph<T> graph,
            T sourceValue,
            T sinkValue
    ) {

        Vertex<T> source =
                findVertex(graph, sourceValue);

        Vertex<T> sink =
                findVertex(graph, sinkValue);

        float maxFlow = 0;

        while (true) {

            List<Edge<T>> path =
                    new ArrayList<>();

            List<Vertex<T>> visited =
                    new ArrayList<>();

            boolean found =
                    dfsPath(
                            graph,
                            source,
                            sink,
                            path,
                            visited
                    );

            if (!found) {
                break;
            }

            float bottleneck =
                    Float.MAX_VALUE;

            for (Edge<T> edge : path) {
                bottleneck =
                        Math.min(
                                bottleneck,
                                edge.getCapacity()
                        );
            }

            for (Edge<T> edge : path) {

                edge.setCapacity(
                        edge.getCapacity()
                                - bottleneck
                );

                Edge<T> reverse =
                        findEdge(
                                graph,
                                edge.getDestination(),
                                edge.getOrigin()
                        );

                if (reverse == null) {

                    graph.getAdjacencyList()
                            .get(
                                    edge.getDestination()
                            )
                            .add(
                                    new Edge<>(
                                            edge.getDestination(),
                                            edge.getOrigin(),
                                            bottleneck
                                    )
                            );

                } else {

                    reverse.setCapacity(
                            reverse.getCapacity()
                                    + bottleneck
                    );
                }
            }

            maxFlow += bottleneck;
        }

        return maxFlow;
    }
}