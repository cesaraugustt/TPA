public class Main {

    public static void main(String[] args) {

        Graph<Integer> graph = new Graph<>();

        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addVertex(6);
        graph.addVertex(7);

        graph.addEdge(1, 2, 7);
        graph.addEdge(1, 3, 9);
        graph.addEdge(1, 4, 5);

        graph.addEdge(2, 5, 3);

        graph.addEdge(3, 5, 3);
        graph.addEdge(3, 6, 2);

        graph.addEdge(4, 6, 4);

        graph.addEdge(5, 7, 8);
        graph.addEdge(6, 7, 9);

        System.out.println("=== GRAFO ===");
        graph.printGraph();

        FordFulkerson<Integer> fordFulkerson =
                new FordFulkerson<>();

        float maxFlow =
                fordFulkerson.calculate(
                        graph,
                        1,
                        7
                );

        System.out.println();
        System.out.println(
                "Fluxo Máximo: " + maxFlow
        );
    }
}