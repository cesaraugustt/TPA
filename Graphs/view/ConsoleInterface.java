package view;

import algorithm.FordFulkerson;
import data.GraphLoader;
import domain.Edge;
import domain.Graph;
import domain.Vertex;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {

    private Graph<String> graph;
    private final Scanner scanner;

    public ConsoleInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option = -1;
        while (option != 6) {
            printMenu();
            System.out.print("Escolha uma opção (1-6): ");
            String input = scanner.nextLine().trim();
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite um número entre 1 e 6.");
                continue;
            }

            switch (option) {
                case 1:
                    handleLoadFile();
                    break;
                case 2:
                    handlePrintGraph();
                    break;
                case 3:
                    handleAddVertex();
                    break;
                case 4:
                    handleAddEdge();
                    break;
                case 5:
                    handleOptimizeFlow();
                    break;
                case 6:
                    System.out.println("Saindo do sistema. Distribuição de tráfego concluída.");
                    break;
                default:
                    System.out.println("Opção fora do intervalo (1-6).");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=======================================================");
        System.out.println("    ISP STREAMING - GERENCIADOR DE BANDA E TRÁFEGO     ");
        System.out.println("=======================================================");
        System.out.println("1. Carregar rede a partir de arquivo de topologia (.txt)");
        System.out.println("2. Visualizar topologia da rede atual (Capacidades)");
        System.out.println("3. Adicionar novo roteador (Vértice)");
        System.out.println("4. Adicionar novo link de transmissão (Aresta bidirecional)");
        System.out.println("5. Otimizar distribuição de tráfego (Fluxo Máximo)");
        System.out.println("6. Sair");
        System.out.println("=======================================================");
    }

    private void handleLoadFile() {
        System.out.print("Digite o caminho do arquivo de topologia (ou pressione Enter para 'entrada.txt'): ");
        String filepath = scanner.nextLine().trim();
        if (filepath.isEmpty()) {
            filepath = "entrada.txt";
        }
        
        java.io.File file = new java.io.File(filepath);
        if (!file.exists()) {
            System.out.print("Arquivo não encontrado. Deseja gerar uma nova rede de testes aleatória e salvá-la como '" + filepath + "'? (S/N): ");
            String ans = scanner.nextLine().trim().toLowerCase();
            if (ans.equals("s") || ans.equals("sim")) {
                System.out.print("Digite a quantidade de roteadores para a rede (mínimo 5): ");
                String nStr = scanner.nextLine().trim();
                int n = 5;
                try {
                    n = Math.max(5, Integer.parseInt(nStr));
                } catch (NumberFormatException e) {
                    System.out.println("Quantidade inválida. Gerando com padrão de 5 roteadores.");
                }
                try {
                    GraphLoader.generateRandomNetwork(filepath, n);
                    System.out.println("Sucesso: Arquivo de rede '" + filepath + "' gerado com sucesso.");
                } catch (IOException ex) {
                    System.out.println("Erro ao gerar o arquivo de rede: " + ex.getMessage());
                    return;
                }
            } else {
                System.out.println("Carregamento cancelado.");
                return;
            }
        }

        try {
            graph = GraphLoader.loadFromFile(filepath);
            System.out.println("Sucesso: Rede carregada com " + graph.getVertices().size() + " roteadores.");
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " + e.getMessage());
        }
    }

    private void handlePrintGraph() {
        if (graph == null) {
            System.out.println("Aviso: Nenhuma rede carregada. Carregue um arquivo primeiro (Opção 1).");
            return;
        }
        System.out.println("\n--- TOPOLOGIA DA REDE E CAPACIDADE DOS LINKS ---");
        graph.printGraph();
    }

    private void handleAddVertex() {
        if (graph == null) {
            System.out.println("Aviso: Nenhuma rede carregada. Inicializando um grafo em branco...");
            graph = new Graph<>();
        }
        System.out.print("Digite o nome do novo roteador/servidor: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Erro: O nome não pode ser vazio.");
            return;
        }
        graph.addVertex(name);
        System.out.println("Sucesso: Roteador '" + name + "' adicionado.");
    }

    private void handleAddEdge() {
        if (graph == null) {
            System.out.println("Aviso: Nenhuma rede carregada. Carregue um arquivo ou adicione vértices primeiro.");
            return;
        }
        System.out.print("Digite o nome do roteador de ORIGEM: ");
        String origin = scanner.nextLine().trim();
        System.out.print("Digite o nome do roteador de DESTINO: ");
        String destination = scanner.nextLine().trim();
        System.out.print("Digite a capacidade de banda (Mbps): ");
        String capStr = scanner.nextLine().trim();

        float capacity;
        try {
            capacity = Float.parseFloat(capStr);
            if (capacity <= 0) {
                System.out.println("Erro: A capacidade deve ser maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Capacidade inválida.");
            return;
        }

        // Verifica se os vértices existem
        if (!vertexExists(origin) || !vertexExists(destination)) {
            System.out.println("Erro: Um ou ambos os roteadores informados não existem na rede.");
            return;
        }

        // Adiciona arestas bidirecionais (ida e volta independentes com a mesma capacidade)
        graph.addEdge(origin, destination, capacity);
        graph.addEdge(destination, origin, capacity);
        System.out.println("Sucesso: Link de " + capacity + " Mbps estabelecido bidirecionalmente entre '" + origin + "' e '" + destination + "'.");
    }

    private void handleOptimizeFlow() {
        if (graph == null || graph.getVertices().isEmpty()) {
            System.out.println("Aviso: Nenhuma rede carregada ou sem roteadores.");
            return;
        }

        System.out.print("Digite o Servidor de Origem (Fonte): ");
        String source = scanner.nextLine().trim();
        System.out.print("Digite a Área de Clientes de Destino (Sumidouro): ");
        String sink = scanner.nextLine().trim();

        if (!vertexExists(source)) {
            System.out.println("Erro: Servidor '" + source + "' não encontrado.");
            return;
        }
        if (!vertexExists(sink)) {
            System.out.println("Erro: Clientes '" + sink + "' não encontrados.");
            return;
        }

        System.out.println("\nProcessando otimização usando algoritmo de Ford-Fulkerson...");
        FordFulkerson<String> solver = new FordFulkerson<>();
        FordFulkerson.FlowResult<String> result = solver.calculate(graph, source, sink);

        System.out.println("\n=======================================================");
        System.out.println("                 RESULTADO DA OTIMIZAÇÃO               ");
        System.out.println("=======================================================");
        System.out.printf("Vazão de Streaming Máxima Total: %.2f Mbps%n", result.getMaxFlow());
        System.out.println("\nDistribuição detalhada de tráfego por link físico:");

        Graph<String> residualGraph = result.getResidualGraph();
        boolean hasTraffic = false;

        for (Vertex<String> u : graph.getVertices()) {
            List<Edge<String>> originalEdges = graph.getAdjacencyList().get(u);
            if (originalEdges != null) {
                for (Edge<String> e : originalEdges) {
                    Edge<String> resEdge = findEdgeInGraph(
                            residualGraph,
                            e.getOrigin().getValue(),
                            e.getDestination().getValue()
                    );
                    float residualCap = (resEdge != null) ? resEdge.getCapacity() : 0.0f;
                    float allocatedFlow = e.getCapacity() - residualCap;

                    // Exibe o link se houver tráfego fluindo
                    if (allocatedFlow > 0.01f) {
                        System.out.printf("  [Link Fibra] %s -> %s: %.2f / %.2f Mbps%n",
                                e.getOrigin().getValue(),
                                e.getDestination().getValue(),
                                allocatedFlow,
                                e.getCapacity()
                        );
                        hasTraffic = true;
                    }
                }
            }
        }

        if (!hasTraffic && result.getMaxFlow() > 0) {
            System.out.println("  (Fluxo distribuído por rotas dinâmicas secundárias)");
        } else if (result.getMaxFlow() <= 0) {
            System.out.println("  Nenhuma rota disponível com banda livre entre a origem e o destino.");
        }
        System.out.println("=======================================================");
    }

    private boolean vertexExists(String value) {
        for (Vertex<String> v : graph.getVertices()) {
            if (v.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    private Edge<String> findEdgeInGraph(Graph<String> g, String origin, String destination) {
        for (Vertex<String> v : g.getVertices()) {
            if (v.getValue().equals(origin)) {
                List<Edge<String>> edges = g.getAdjacencyList().get(v);
                if (edges != null) {
                    for (Edge<String> e : edges) {
                        if (e.getDestination().getValue().equals(destination)) {
                            return e;
                        }
                    }
                }
            }
        }
        return null;
    }
}
