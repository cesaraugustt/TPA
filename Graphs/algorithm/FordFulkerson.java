package algorithm;

import domain.Edge;
import domain.Graph;
import domain.Vertex;
import java.util.ArrayList;
import java.util.List;

public class FordFulkerson<T> {

    public static class FlowResult<T> {
        private final float maxFlow;
        private final Graph<T> residualGraph;

        public FlowResult(float maxFlow, Graph<T> residualGraph) {
            this.maxFlow = maxFlow;
            this.residualGraph = residualGraph;
        }

        public float getMaxFlow() {
            return maxFlow;
        }

        public Graph<T> getResidualGraph() {
            return residualGraph;
        }
    }

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

    public FlowResult<T> calculate(
            Graph<T> graph,
            T sourceValue,
            T sinkValue
    ) {
        // Clona o grafo original para trabalhar sobre uma cópia (grafo residual)
        Graph<T> residualGraph = new Graph<>(graph);

        Vertex<T> source =
                findVertex(residualGraph, sourceValue);

        Vertex<T> sink =
                findVertex(residualGraph, sinkValue);

        if (source == null || sink == null) {
            return new FlowResult<>(0, residualGraph);
        }

        float maxFlow = 0;

        while (true) {

            List<Edge<T>> path =
                    new ArrayList<>();

            List<Vertex<T>> visited =
                    new ArrayList<>();

            boolean found =
                    dfsPath(
                            residualGraph,
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
                                residualGraph,
                                edge.getDestination(),
                                edge.getOrigin()
                        );

                if (reverse == null) {

                    residualGraph.getAdjacencyList()
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

        return new FlowResult<>(maxFlow, residualGraph);
    }
}
