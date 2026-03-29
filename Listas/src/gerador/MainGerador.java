package src.gerador;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Entry point do gerador de dados.
 *
 * Uso:
 *   java src.gerador.MainGerador                    → gera os 3 tamanhos padrão
 *   java src.gerador.MainGerador 50000 150000        → gera tamanhos customizados
 */
public class MainGerador {

    public static void main(String[] args) throws IOException {
        int[] tamanhos = resolverTamanhos(args);

        for (int tamanho : tamanhos) {
            String arquivo = GeradorDados.caminhoPadrao(tamanho);
            GeradorDados.gerarComLog(tamanho, arquivo);
        }

        System.out.println("\nArquivos gerados em: " + Paths.get(GeradorDados.DIRETORIO).toAbsolutePath());
    }

    /**
     * Interpreta os argumentos CLI como tamanhos. Usa padrões se nenhum for fornecido.
     */
    private static int[] resolverTamanhos(String[] args) {
        if (args.length == 0) return GeradorDados.TAMANHOS_PADRAO;

        int[] tamanhos = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            try {
                tamanhos[i] = Integer.parseInt(args[i]);
                if (tamanhos[i] <= 0) throw new IllegalArgumentException();
            } catch (IllegalArgumentException e) {
                System.err.println("Argumento invalido: '" + args[i] + "'. Use inteiros positivos.");
                System.exit(1);
            }
        }
        return tamanhos;
    }
}
