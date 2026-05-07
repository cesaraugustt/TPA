package Arvores.benchmark;

import Arvores.ArvoreBinaria;
import Arvores.ArvoreAVL;
import colecao.IColecao;
import dominio.Aluno;
import dominio.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BenchmarkArvore {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Arvores.benchmark.BenchmarkArvore <caminho_arquivo> [--avl]");
            System.exit(1);
        }
        
        String caminhoArquivo = args[0];
        boolean useAvl = (args.length > 1 && args[1].equals("--avl"));

        System.out.println("Aquecendo JIT...");
        aquecer(useAvl);
        System.out.println("Aquecimento concluido. Iniciando medicoes...");
        executarBenchmark(caminhoArquivo, useAvl);
    }
    
    private static void aquecer(boolean useAvl) {
        // Rotina de aquecimento rápida com carga simulada
        IColecao<Aluno> arvore = useAvl ? new ArvoreAVL<>(Aluno.obterComparadorPorMatricula()) : new ArvoreBinaria<>(Aluno.obterComparadorPorMatricula());
        int N = 1000;
        for (int i = 1; i <= N; i++) {
            arvore.adicionar(new Aluno(i, "Aluno", 5));
        }
        arvore.pesquisar(Aluno.porMatricula(N));
        arvore.pesquisar(Aluno.porMatricula(N + 1));
        arvore.remover(Aluno.porMatricula(N));
        arvore.quantidadeNos();
    }

    private static void executarBenchmark(String caminhoArquivo, boolean useAvl) {
        IColecao<Aluno> arvore = useAvl ? new ArvoreAVL<>(Aluno.obterComparadorPorMatricula()) : new ArvoreBinaria<>(Aluno.obterComparadorPorMatricula());
        String implementacaoNome = useAvl ? "ArvoreAVL" : "ArvoreBinaria";
        
        // ── Fase 1: Carga ────────────────────────────────────────────────────
        int totalRegistros = 0;
        int maxId = 0; // Guardar o maior ID para fazer a pesquisa da folha mais distante

        long inicioCarga = System.nanoTime();
        
        // Thread para imprimir ponto a cada 5 segundos sem pesar no benchmark
        Thread loadingThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(5000);
                    System.out.print(".");
                    System.out.flush(); // Garante que o ponto apareça na hora
                }
            } catch (InterruptedException e) {
                // A thread foi encerrada quando a carga acabou
            }
        });
        loadingThread.setDaemon(true);
        loadingThread.start();

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] partes = linha.split(";");
                int matricula = Integer.parseInt(partes[0]);
                String nome = partes[1];
                int nota = Integer.parseInt(partes[2]);

                Aluno aluno = new Aluno(matricula, nome, nota);
                arvore.adicionar(aluno);
                totalRegistros++;
                if (matricula > maxId) {
                    maxId = matricula;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao ler o arquivo '" + caminhoArquivo + "': " + e.getMessage());
            return;
        } finally {
            loadingThread.interrupt(); // Encerra a thread dos pontinhos
            System.out.println(); // Quebra de linha para não misturar com o relatório
        }
        long tempoCarga = System.nanoTime() - inicioCarga;

        if (totalRegistros == 0) {
            System.err.println("Arquivo vazio ou sem registros validos: " + caminhoArquivo);
            return;
        }

        // ── Fase 2: Operações individuais ───────────────────────────────────

        // 2.1 Busca da folha mais distante (pesquisar)
        // Em árvores degeneradas (ordenadas crescentemente), o maior ID é o último nó e o mais distante.
        // Em árvores perfeitamente balanceadas geradas em pré-ordem, o maior ID também é alocado na máxima profundidade (extremo direito).
        Aluno folhaMaisDistante = Aluno.porMatricula(maxId);
        long t0 = System.nanoTime();
        arvore.pesquisar(folhaMaisDistante);
        long tempoBuscaExistente = System.nanoTime() - t0;

        // 2.2 Busca de elemento inexistente (que seria filho da folha mais distante)
        Aluno inexistente = Aluno.porMatricula(maxId + 1);
        t0 = System.nanoTime();
        arvore.pesquisar(inexistente);
        long tempoBuscaInexistente = System.nanoTime() - t0;

        // 2.3 Remover a folha mais distante
        t0 = System.nanoTime();
        arvore.remover(folhaMaisDistante);
        long tempoRemocao = System.nanoTime() - t0;

        // 2.4 Contagem de nós
        t0 = System.nanoTime();
        int quantidade = arvore.quantidadeNos();
        long tempoContagem = System.nanoTime() - t0;

        // ── Saída ────────────────────────────────────────────────────────────
        imprimir(implementacaoNome, caminhoArquivo, totalRegistros, tempoCarga,
                 tempoBuscaExistente, tempoBuscaInexistente,
                 tempoRemocao, quantidade, tempoContagem);
    }

    private static void imprimir(String implementacao, String arquivo, int registros, long carga,
                                  long buscaExiste, long buscaInexiste,
                                  long remocao, int quantidade, long contagem) {
        String sep = "=".repeat(62);
        String div = "-".repeat(62);

        System.out.println(sep);
        System.out.printf(" BENCHMARK — %s%n", implementacao);
        System.out.printf(" Arquivo   : %s%n", arquivo);
        System.out.println(sep);
        System.out.printf(" %-30s: %,12d registros%n", "Registros lidos", registros);
        System.out.printf(" %-30s: %s%n",              "Tempo de carga", Utils.formatarTempo(carga));
        System.out.println(div);
        System.out.printf(" %-30s: %s%n", "Pesquisar (folha + distante)", Utils.formatarTempo(buscaExiste));
        System.out.printf(" %-30s: %s%n", "Pesquisar (inexistente)",      Utils.formatarTempo(buscaInexiste));
        System.out.printf(" %-30s: %s%n", "Remover (folha + distante)",   Utils.formatarTempo(remocao));
        System.out.printf(" %-30s: %,12d nos  — %s%n",
                "Quantidade de nos", quantidade, Utils.formatarTempo(contagem));
        System.out.println(sep);
        System.out.println();
    }


}
