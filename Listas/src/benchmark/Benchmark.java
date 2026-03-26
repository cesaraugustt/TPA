package src.benchmark;

import src.colecao.IColecao;
import src.dominio.Aluno;
import src.listaencadeada.ListaEncadeada;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Mede o tempo de operações básicas (inserção, busca, remoção, contagem)
 * sobre uma ListaEncadeada carregada a partir de um arquivo de dataset.
 *
 * Uso:
 *   java src.benchmark.Benchmark datasets/alunos_100000.txt   → um arquivo
 *   java src.benchmark.Benchmark                              → todos os padrões
 */
public class Benchmark {

    private static final String[] ARQUIVOS_PADRAO = {
        "datasets/alunos_100000.txt",
        "datasets/alunos_200000.txt",
        "datasets/alunos_400000.txt"
    };

    public static void main(String[] args) {
        String[] arquivos = args.length > 0 ? args : ARQUIVOS_PADRAO;

        // Aquecimento: executa uma passagem silenciosa sobre o menor arquivo
        // para que o JIT compile os hot paths antes das medições reais.
        System.out.println("Aquecendo JIT...");
        aquecer(arquivos[0]);
        System.out.println("Aquecimento concluido. Iniciando medições...\n");

        for (String arquivo : arquivos) {
            executar(arquivo);
        }
    }

    /**
     * Executa o fluxo completo (carga + operações) de forma silenciosa.
     * Garante que o JIT otimize os hot paths antes das medições reais.
     */
    private static void aquecer(String caminhoArquivo) {
        try {
            IColecao<Aluno> lista = new ListaEncadeada<>();
            Aluno ultimo = null;

            try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    linha = linha.trim();
                    if (linha.isEmpty()) continue;
                    String[] p = linha.split(";");
                    ultimo = new Aluno(Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]));
                    lista.adicionar(ultimo);
                }
            }

            if (ultimo != null) {
                lista.pesquisar(ultimo);
                lista.pesquisar(Aluno.porMatricula(-1));
                lista.remover(ultimo);
                lista.quantidadeNos();
            }
        } catch (IOException | NumberFormatException ignored) {
            // Falha no aquecimento não interrompe o benchmark
        }
    }

    /**
     * Executa e mede o benchmark completo para um único arquivo.
     */
    private static void executar(String caminhoArquivo) {
        IColecao<Aluno> lista = new ListaEncadeada<>();

        // ── Fase 1: Carga ────────────────────────────────────────────────────
        Aluno ultimoAluno = null;
        int totalRegistros = 0;

        long inicioCarga = System.nanoTime();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] partes = linha.split(";");
                int matricula   = Integer.parseInt(partes[0]);
                String nome     = partes[1];
                int nota        = Integer.parseInt(partes[2]);

                ultimoAluno = new Aluno(matricula, nome, nota);
                lista.adicionar(ultimoAluno);
                totalRegistros++;
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo '" + caminhoArquivo + "': " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            System.err.println("Erro ao processar valor numerico: " + e.getMessage());
            return;
        }
        long tempoCarga = System.nanoTime() - inicioCarga;

        if (ultimoAluno == null) {
            System.err.println("Arquivo vazio ou sem registros validos: " + caminhoArquivo);
            return;
        }

        // ── Fase 2: Operações individuais ───────────────────────────────────

        // Busca de elemento existente (último → pior caso sequencial)
        long t0 = System.nanoTime();
        lista.pesquisar(ultimoAluno);
        long tempoBuscaExistente = System.nanoTime() - t0;

        // Busca de elemento inexistente (percorre lista inteira)
        Aluno inexistente = Aluno.porMatricula(-1);
        t0 = System.nanoTime();
        lista.pesquisar(inexistente);
        long tempoBuscaInexistente = System.nanoTime() - t0;

        // Remoção do último elemento
        t0 = System.nanoTime();
        lista.remover(ultimoAluno);
        long tempoRemocao = System.nanoTime() - t0;

        // Contagem de nós
        t0 = System.nanoTime();
        int quantidade = lista.quantidadeNos();
        long tempoContagem = System.nanoTime() - t0;

        // ── Saída ────────────────────────────────────────────────────────────
        imprimir(caminhoArquivo, totalRegistros, tempoCarga,
                 tempoBuscaExistente, tempoBuscaInexistente,
                 tempoRemocao, quantidade, tempoContagem);
    }

    /**
     * Formata e imprime os resultados do benchmark.
     */
    private static void imprimir(String arquivo, int registros, long carga,
                                  long buscaExiste, long buscaInexiste,
                                  long remocao, int quantidade, long contagem) {
        String sep = "=".repeat(62);
        String div = "-".repeat(62);

        System.out.println(sep);
        System.out.printf(" BENCHMARK — %s%n", arquivo);
        System.out.println(sep);
        System.out.printf(" %-30s: %,12d registros%n", "Registros lidos", registros);
        System.out.printf(" %-30s: %s%n",              "Tempo de carga", formatarTempo(carga));
        System.out.println(div);
        System.out.printf(" %-30s: %s%n", "Pesquisar (existente)",   formatarTempo(buscaExiste));
        System.out.printf(" %-30s: %s%n", "Pesquisar (inexistente)", formatarTempo(buscaInexiste));
        System.out.printf(" %-30s: %s%n", "Remover (ultimo)",        formatarTempo(remocao));
        System.out.printf(" %-30s: %,12d nos  — %s%n",
                "Quantidade de nos", quantidade, formatarTempo(contagem));
        System.out.println(sep);
        System.out.println();
    }

    /**
     * Converte nanosegundos para uma string legível com ms e ns.
     */
    private static String formatarTempo(long ns) {
        if (ns < 1_000_000L) {
            return String.format("%,12d ns", ns);
        }
        return String.format("%,9.2f ms  (%,d ns)", ns / 1_000_000.0, ns);
    }
}
