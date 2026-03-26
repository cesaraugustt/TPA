package src.gerador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Entry point do gerador de dados.
 *
 * Uso:
 *   java src.gerador.MainGerador                    → gera os 3 tamanhos padrão
 *   java src.gerador.MainGerador 50000 150000        → gera tamanhos customizados
 */
public class MainGerador {

    private static final int[] TAMANHOS_PADRAO = {100_000, 200_000, 400_000};
    private static final String DIRETORIO_SAIDA = "datasets";

    public static void main(String[] args) throws IOException {
        Files.createDirectories(Paths.get(DIRETORIO_SAIDA));

        int[] tamanhos = resolverTamanhos(args);

        for (int tamanho : tamanhos) {
            String arquivo = DIRETORIO_SAIDA + "/alunos_" + tamanho + ".txt";
            System.out.print("Gerando " + tamanho + " registros em '" + arquivo + "'... ");

            long inicio = System.currentTimeMillis();
            GeradorDados.gerar(tamanho, arquivo);
            long duracao = System.currentTimeMillis() - inicio;

            System.out.println("concluido em " + duracao + " ms.");
        }

        System.out.println("\nArquivos gerados em: " + Paths.get(DIRETORIO_SAIDA).toAbsolutePath());
    }

    /**
     * Interpreta os argumentos CLI como tamanhos. Usa padrões se nenhum for fornecido.
     */
    private static int[] resolverTamanhos(String[] args) {
        if (args.length == 0) return TAMANHOS_PADRAO;

        int[] tamanhos = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            try {
                tamanhos[i] = Integer.parseInt(args[i]);
                if (tamanhos[i] <= 0) throw new IllegalArgumentException();
            } catch (NumberFormatException e) {
                System.err.println("Argumento invalido: '" + args[i] + "'. Use inteiros positivos.");
                System.exit(1);
            } catch (IllegalArgumentException e) {
                System.err.println("Argumento invalido: '" + args[i] + "'. Use inteiros positivos.");
                System.exit(1);
            }
        }
        return tamanhos;
    }
}
