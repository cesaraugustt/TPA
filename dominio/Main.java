package dominio;

import src.benchmark.Benchmark;
import colecao.IColecao;
import src.gerador.GeradorDados;
import Arvores.ArvoreBinaria;
import Arvores.ArvoreJavaAdapter;
import Arvores.geradorArquivos.GeradorArquivosBalanceados;
import Arvores.geradorArquivos.GeradorArquivosOrdenados;
import Arvores.benchmark.BenchmarkArvore;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Escolha a Estrutura de Dados ===");
        System.out.println("1. Arvore Binaria Customizada");
        System.out.println("2. Arvore Padrao do Java (TreeMap)");
        IColecao<Aluno> colecao = null;
        
        while (colecao == null) {
            System.out.print("Opcao: ");
            int escolha = lerInteiro(scanner);
            
            if (escolha == 1) {
                colecao = new ArvoreBinaria<>(Aluno.obterComparadorPorMatricula());
                System.out.println("\nIniciando com Arvore Binaria Customizada.\n");
            } else if (escolha == 2) {
                colecao = new ArvoreJavaAdapter<>(Aluno.obterComparadorPorMatricula());
                System.out.println("\nIniciando com Arvore Padrao do Java (TreeMap).\n");
            } else if (escolha != -1) {
                System.out.println("\nOpcao invalida! Digite 1 ou 2.");
            }
        }

        boolean executando = true;

        while (executando) {
            System.out.println("\n=== Sistema de Gerenciamento de Alunos ===");
            System.out.println("1. Adicionar Aluno");
            System.out.println("2. Remover Aluno");
            System.out.println("3. Pesquisar Aluno");
            System.out.println("4. Imprimir todos os Alunos");
            System.out.println("5. Gerar Dados e Executar Benchmark (Listas)");
            System.out.println("6. Gerar Dados e Executar Benchmark (Arvore Binaria)");
            System.out.println("7. Gerar Dados e Executar Benchmark (Arvore AVL)");
            System.out.println("8. Sair");
            System.out.print("Escolha uma opcao: ");

            int opcao = lerInteiro(scanner);
            if (opcao == -1) continue;

            switch (opcao) {
                case 1:
                    adicionarAluno(scanner, colecao);
                    break;
                case 2:
                    removerAluno(scanner, colecao);
                    break;
                case 3:
                    pesquisarAluno(scanner, colecao);
                    break;
                case 4:
                    imprimirColecao(colecao);
                    break;
                case 5:
                    executarBenchmark(scanner);
                    break;
                case 6:
                    processarBenchmarkArvore(scanner, false);
                    break;
                case 7:
                    processarBenchmarkArvore(scanner, true);
                    break;
                case 8:
                    System.out.println("\nSaindo do sistema...");
                    executando = false;
                    break;
                default:
                    System.out.println("\nOpcao incorreta! Insira um numero de 1 a 8.");
            }
        }

        scanner.close();
    }

    /**
     * Lê os dados do aluno inseridos pelo usuário e adiciona um novo Aluno à coleção.
     */
    private static void adicionarAluno(Scanner scanner, IColecao<Aluno> colecao) {
        System.out.print("Matricula: ");
        int matricula = lerInteiro(scanner);
        if (matricula < 0) {
            System.out.println("\nOperacao cancelada.");
            return;
        }

        if (colecao.pesquisar(Aluno.porMatricula(matricula)) != null) {
            System.out.println("\nMatricula " + matricula + " ja cadastrada. Operacao cancelada.");
            return;
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) {
            System.out.println("\nNome nao pode ser vazio. Operacao cancelada.");
            return;
        }

        System.out.print("Nota (0-10): ");
        int nota = lerInteiro(scanner);
        if (nota < 0 || nota > 10) {
            System.out.println("\nNota invalida. Operacao cancelada.");
            return;
        }

        colecao.adicionar(new Aluno(matricula, nome, nota));
        System.out.println("\nAluno adicionado com sucesso!");
    }

    /**
     * Lê a matrícula de um aluno e remove o registro correspondente da coleção.
     * A matrícula é utilizada como chave pelo equals().
     */
    private static void removerAluno(Scanner scanner, IColecao<Aluno> colecao) {
        if (colecao.quantidadeNos() == 0) {
            System.out.println("\nA colecao esta vazia.");
            return;
        }

        System.out.print("Matricula do aluno a remover: ");
        int matricula = lerInteiro(scanner);
        if (matricula < 0) {
            System.out.println("\nOperacao cancelada.");
            return;
        }

        // Apenas a matrícula é necessária, pois equals() compara somente esse campo.
        boolean removido = colecao.remover(Aluno.porMatricula(matricula));
        if (removido) {
            System.out.println("\nAluno removido com sucesso!");
        } else {
            System.out.println("\nAluno nao encontrado.");
        }
    }

    /**
     * Lê a matrícula de um aluno e pesquisa o registro correspondente na coleção.
     */
    private static void pesquisarAluno(Scanner scanner, IColecao<Aluno> colecao) {
        if (colecao.quantidadeNos() == 0) {
            System.out.println("\nA colecao esta vazia.");
            return;
        }

        System.out.print("Matricula do aluno a pesquisar: ");
        int matricula = lerInteiro(scanner);
        if (matricula < 0) {
            System.out.println("\nOperacao cancelada.");
            return;
        }

        Aluno resultado = colecao.pesquisar(Aluno.porMatricula(matricula));
        if (resultado != null) {
            System.out.println("\nAluno encontrado: " + resultado);
        } else {
            System.out.println("\nAluno nao encontrado.");
        }
    }

    /**
     * Imprime todos os alunos armazenados na coleção.
     */
    private static void imprimirColecao(IColecao<Aluno> colecao) {
        if (colecao.quantidadeNos() == 0) {
            System.out.println("\nA colecao esta vazia.");
            return;
        }
        System.out.println("\n--- Colecao de Alunos (" + colecao.quantidadeNos() + " aluno(s)) ---");
        System.out.println(colecao);
    }

    /**
     * Lê uma linha do stdin e a interpreta como um inteiro não-negativo.
     * Retorna -1 se a entrada for inválida.
     */
    private static int lerInteiro(Scanner scanner) {
        String linha = scanner.nextLine().trim();
        try {
            int valor = Integer.parseInt(linha);
            if (valor < 0) {
                System.out.println("\nValor nao pode ser negativo.");
                return -1;
            }
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("\nEntrada invalida! Esperado um numero inteiro.");
            return -1;
        }
    }

    /**
     * Orienta o usuário na geração de datasets e executa o benchmark integrado.
     * Os tamanhos padrão são definidos centralmente em GeradorDados.TAMANHOS_PADRAO.
     */
    private static void executarBenchmark(Scanner scanner) {
        System.out.println("\n=== Gerador de Dados + Benchmark ===");
        System.out.println("Tamanhos padrao: 400.000 | 800.000 | 1.600.000 registros");
        System.out.print("Usar tamanhos padrao? (s/n): ");
        String resposta = scanner.nextLine().trim().toLowerCase();

        int[] tamanhos;
        if (resposta.equals("s") || resposta.equals("sim")) {
            tamanhos = GeradorDados.TAMANHOS_PADRAO;
        } else {
            System.out.print("Informe os tamanhos separados por espaco (ex: 50000 150000): ");
            String[] partes = scanner.nextLine().trim().split("\\s+");
            tamanhos = new int[partes.length];
            for (int i = 0; i < partes.length; i++) {
                try {
                    tamanhos[i] = Integer.parseInt(partes[i]);
                    if (tamanhos[i] <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    System.out.println("\nTamanho invalido: '" + partes[i] + "'. Operacao cancelada.");
                    return;
                }
            }
        }

        // ── Fase 1: Geração dos datasets ─────────────────────────────────────
        System.out.println("\n--- Gerando datasets ---");
        try {
            String[] arquivos = new String[tamanhos.length];
            for (int i = 0; i < tamanhos.length; i++) {
                arquivos[i] = GeradorDados.caminhoPadrao(tamanhos[i]);
                GeradorDados.gerarComLog(tamanhos[i], arquivos[i]);
            }

            // ── Fase 2: Benchmark ─────────────────────────────────────────────
            System.out.println("\n--- Executando Benchmark ---");
            Benchmark.executar(arquivos);

        } catch (IOException e) {
            System.err.println("\nErro ao gerar datasets: " + e.getMessage());
        }
    }

    /**
     * Orienta o usuário na escolha do tamanho e executa a geração de arquivos
     * e os testes de benchmark para a Arvore Binaria ou Arvore AVL.
     */
    private static void processarBenchmarkArvore(Scanner scanner, boolean useAvl) {
        String nomeArvore = useAvl ? "Arvore AVL" : "Arvore Binaria";
        System.out.println("\n=== Benchmark de " + nomeArvore + " ===");
        System.out.println("Selecione o tamanho do dataset:");
        for (int i = 0; i < GeradorDados.TAMANHOS_ARVORE.length; i++) {
            System.out.printf("%d. %,d registros%n", (i + 1), GeradorDados.TAMANHOS_ARVORE[i]);
        }
        System.out.print("Escolha uma opcao: ");

        int opcao = lerInteiro(scanner);
        int tamanho = 0;
        
        if (opcao >= 1 && opcao <= GeradorDados.TAMANHOS_ARVORE.length) {
            tamanho = GeradorDados.TAMANHOS_ARVORE[opcao - 1];
        } else {
            System.out.println("\nOpcao invalida. Operacao cancelada.");
            return;
        }

        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("datasets"));
        } catch (IOException e) {
            System.err.println("\nErro ao criar diretorio datasets: " + e.getMessage());
            return;
        }

        String arquivoBalanceado = "datasets/alunos_balanceados_" + tamanho + ".txt";
        String arquivoOrdenado = "datasets/alunos_ordenados_" + tamanho + ".txt";

        System.out.println("\n--- Gerando datasets (" + tamanho + " registros) ---");
        
        System.out.println("Gerando arquivo balanceado...");
        GeradorArquivosBalanceados.main(new String[]{String.valueOf(tamanho), arquivoBalanceado});
        
        System.out.println("Gerando arquivo ordenado (degenerado)...");
        GeradorArquivosOrdenados.main(new String[]{String.valueOf(tamanho), arquivoOrdenado});

        String[] argsBalanceado = useAvl ? new String[]{arquivoBalanceado, "--avl"} : new String[]{arquivoBalanceado};
        String[] argsOrdenado = useAvl ? new String[]{arquivoOrdenado, "--avl"} : new String[]{arquivoOrdenado};

        System.out.println("\n--- Executando Benchmark (" + nomeArvore + " Balanceada) ---");
        BenchmarkArvore.main(argsBalanceado);

        System.out.println("\n--- Executando Benchmark (" + nomeArvore + " Degenerada) ---");
        BenchmarkArvore.main(argsOrdenado);
    }
}