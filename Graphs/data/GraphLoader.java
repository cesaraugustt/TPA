package data;

import domain.Graph;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class GraphLoader {

    public static void generateRandomNetwork(String filepath, int n) throws IOException {
        Random rand = new Random();
        char[] vogais = {'a', 'e', 'i', 'o','u','A','E','I','O','U'};
        
        try (PrintWriter gravarArq = new PrintWriter(new FileWriter(filepath))) {
            gravarArq.println(n);
            
            // Gerando nomes de roteadores
            for(int i = 1; i <= n; i++){
                StringBuilder palavra = new StringBuilder();
                palavra.append((char) ('A' + rand.nextInt(26)));
                for(int cont = 1; cont < 6; cont++) {
                    char anterior = palavra.charAt(cont - 1);
                    boolean ehVogal = false;
                    for (char l : vogais) {
                        if (l == anterior) { ehVogal = true; break; }
                    }
                    if (ehVogal) {
                        palavra.append((char) ('a' + rand.nextInt(26)));
                    } else {
                        palavra.append(vogais[rand.nextInt(5)]);
                    }
                }
                gravarArq.printf("%d,%s%n", i, palavra.toString());
            }
            
            // Gerando distâncias/capacidades
            int tamVetDist = (n * n - n) / 2;
            double[] distancias = new double[tamVetDist];
            int idx = 0;
            for(int l = 0; l < n; l++){
                for(int c = l + 1; c < n; c++){
                    double r = rand.nextDouble();
                    if (c - l == 1) {
                        distancias[idx] = r * 100 + 10;
                    } else {
                        distancias[idx] = (r < 0.3) ? 0.0 : r * 100;
                    }
                    idx++;
                }
            }

            // Escrevendo a matriz
            for(int l = 0; l < n; l++){
                int c;
                for(c = 0; c < n - 1; c++){
                    if(l == c)
                        gravarArq.printf("%.2f,", 0.0);
                    else if (l < c)
                        gravarArq.printf("%.2f,", distancias[l * n - (l * l + l) / 2 + c - l - 1]);
                    else
                        gravarArq.printf("%.2f,", distancias[c * n - (c * c + c) / 2 + l - c - 1]);
                }
                if(l == (n - 1))
                    gravarArq.printf("%.2f%n", 0.0);
                else
                    gravarArq.printf("%.2f%n", distancias[l * n - (l * l + l) / 2 + c - l - 1]);
            }
        }
    }

    public static Graph<String> loadFromFile(String filepath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("O arquivo está vazio.");
            }

            int n;
            try {
                n = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                throw new IOException("A primeira linha deve conter o número de vértices (inteiro).", e);
            }

            String[] idToName = new String[n];
            for (int i = 0; i < n; i++) {
                String vertexLine = reader.readLine();
                if (vertexLine == null) {
                    throw new IOException("Esperado mapeamento de vértice na linha " + (i + 2));
                }
                String[] parts = vertexLine.split(",", 2);
                if (parts.length < 2) {
                    throw new IOException("Formato de vértice inválido na linha " + (i + 2) + ". Esperado 'ID,Nome'");
                }
                int id;
                try {
                    id = Integer.parseInt(parts[0].trim());
                } catch (NumberFormatException e) {
                    throw new IOException("ID de vértice inválido na linha " + (i + 2), e);
                }
                String name = parts[1].trim();
                idToName[id - 1] = name;
            }

            Graph<String> graph = new Graph<>();
            for (int i = 0; i < n; i++) {
                graph.addVertex(idToName[i]);
            }

            for (int l = 0; l < n; l++) {
                String matrixLine = reader.readLine();
                if (matrixLine == null) {
                    throw new IOException("Matriz de adjacência incompleta. Linha " + (l + 1) + " da matriz ausente.");
                }
                String[] tokens = matrixLine.split(",");
                
                // Determina se o arquivo usa separador decimal PT-BR (vírgula) ou US (ponto)
                // Se usar vírgula, cada número da forma "X,YY" vira dois tokens. O total de tokens será 2 * N.
                boolean isPtBrLocale = tokens.length >= 2 * n;
                
                for (int c = 0; c < n; c++) {
                    float capacity;
                    try {
                        if (isPtBrLocale) {
                            String numberStr = tokens[2 * c].trim() + "." + tokens[2 * c + 1].trim();
                            capacity = Float.parseFloat(numberStr);
                        } else {
                            if (tokens.length < n) {
                                throw new IOException("Linha " + (l + 1) + " da matriz de adjacência contém menos elementos que o esperado (" + tokens.length + "/" + n + ").");
                            }
                            capacity = Float.parseFloat(tokens[c].trim());
                        }
                    } catch (NumberFormatException e) {
                        throw new IOException("Valor inválido na célula (" + l + ", " + c + ") da matriz.", e);
                    }
                    if (capacity > 0.0f) {
                        // Adiciona aresta direcionada do vértice l para o vértice c
                        graph.addEdge(idToName[l], idToName[c], capacity);
                    }
                }
            }

            return graph;
        }
    }
}
