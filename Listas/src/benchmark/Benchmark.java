package src.benchmark;

import src.colecao.IColecao;
import src.dominio.Aluno;
import src.gerador.GeradorDados;
import src.listaencadeada.ListaEncadeada;
import src.listaencadeada.ListaEncadeadaArrayList;
import src.listaencadeada.ListaEncadeadaLinkedList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * Mede o tempo de operações básicas (inserção, busca, remoção, contagem)
 * sobre três implementações de IColecao carregadas a partir de arquivos de dataset:
 *
 *   1. ListaEncadeada       — implementação customizada
 *   2. ListaEncadeadaArrayList  — wrapper de java.util.ArrayList
 *   3. ListaEncadeadaLinkedList — wrapper de java.util.LinkedList
 *
 * Uso via CLI:
 *   java src.benchmark.Benchmark                              → todos os arquivos padrão
 *   java src.benchmark.Benchmark datasets/alunos_100000.txt  → arquivo específico
 */
public class Benchmark {

    /** As três implementações que serão comparadas. */
    @SuppressWarnings("unchecked")
    private static final Supplier<IColecao<Aluno>>[] IMPLEMENTACOES = new Supplier[]{
            ListaEncadeada::new,
            ListaEncadeadaArrayList::new,
            ListaEncadeadaLinkedList::new
    };

    private static final String[] NOMES_IMPLEMENTACOES = {
        "ListaEncadeada (customizada)",
        "ArrayList (java.util)",
        "LinkedList (java.util)"
    };

    public static void main(String[] args) {
        String[] arquivos = args.length > 0 ? args : arquivosPadrao();
        executar(arquivos);
    }

    /** Monta o array de caminhos padrão a partir das constantes centralizadas em GeradorDados. */
    private static String[] arquivosPadrao() {
        String[] caminhos = new String[GeradorDados.TAMANHOS_PADRAO.length];
        for (int i = 0; i < GeradorDados.TAMANHOS_PADRAO.length; i++) {
            caminhos[i] = GeradorDados.caminhoPadrao(GeradorDados.TAMANHOS_PADRAO[i]);
        }
        return caminhos;
    }

    /**
     * Ponto de entrada programático: executa o benchmark sobre os arquivos fornecidos.
     * Pode ser chamado diretamente pelo menu principal.
     *
     * Para cada arquivo, executa as três implementações em sequência.
     * O aquecimento usa sempre a ListaEncadeada customizada.
     */
    public static void executar(String[] arquivos) {
        System.out.println("Aquecendo JIT...");
        aquecer(arquivos[0]);
        System.out.println("Aquecimento concluido. Iniciando medicoes...\n");

        for (String arquivo : arquivos) {
            System.out.println("*".repeat(62));
            System.out.printf("* Arquivo: %s%n", arquivo);
            System.out.println("*".repeat(62) + "\n");

            for (int i = 0; i < IMPLEMENTACOES.length; i++) {
                executarArquivo(arquivo, IMPLEMENTACOES[i], NOMES_IMPLEMENTACOES[i]);
            }
        }
    }

    /**
     * Executa o fluxo completo (carga + operações) de forma silenciosa para aquecimento do JIT.
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
     * Executa e mede o benchmark completo para um único arquivo e uma implementação específica.
     * O Supplier cria uma nova instância limpa da implementação a cada chamada.
     */
    private static void executarArquivo(String caminhoArquivo,
                                        Supplier<IColecao<Aluno>> fornecedor,
                                        String nomeImplementacao) {
        IColecao<Aluno> lista = fornecedor.get();

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
        imprimir(nomeImplementacao, totalRegistros, tempoCarga,
                 tempoBuscaExistente, tempoBuscaInexistente,
                 tempoRemocao, quantidade, tempoContagem);
    }

    /**
     * Formata e imprime os resultados do benchmark para uma implementação.
     */
    private static void imprimir(String implementacao, int registros, long carga,
                                  long buscaExiste, long buscaInexiste,
                                  long remocao, int quantidade, long contagem) {
        String sep = "=".repeat(62);
        String div = "-".repeat(62);

        System.out.println(sep);
        System.out.printf(" BENCHMARK — %s%n", implementacao);
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
