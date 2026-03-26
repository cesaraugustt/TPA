package src.gerador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Gera arquivos TXT com registros de alunos aleatórios.
 * Formato de cada linha: matricula;nome;nota
 */
public class GeradorDados {

    private static final String[] NOMES = {
        "Ana", "Bruno", "Carlos", "Diana", "Eduardo",
        "Fernanda", "Gabriel", "Helena", "Igor", "Julia",
        "Kevin", "Laura", "Marcos", "Natalia", "Otavio",
        "Patricia", "Rafael", "Sabrina", "Thiago", "Vanessa"
    };

    private static final String[] SOBRENOMES = {
        "Silva", "Santos", "Costa", "Oliveira", "Souza",
        "Lima", "Pereira", "Carvalho", "Ferreira", "Rodrigues",
        "Almeida", "Nascimento", "Araujo", "Gomes", "Martins",
        "Ribeiro", "Rocha", "Mendes", "Barbosa", "Freitas"
    };

    /**
     * Gera um arquivo com registros de alunos.
     *
     * @param quantidade    número de registros a gerar
     * @param caminhoArquivo caminho completo do arquivo de saída
     */
    public static void gerar(int quantidade, String caminhoArquivo) throws IOException {
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (int i = 1; i <= quantidade; i++) {
                // Matrícula sequencial garante unicidade
                int matricula = i;

                String nome = NOMES[random.nextInt(NOMES.length)]
                            + " " + SOBRENOMES[random.nextInt(SOBRENOMES.length)];

                int nota = random.nextInt(11); // [0, 10]

                writer.write(matricula + ";" + nome + ";" + nota);
                writer.newLine();
            }
        }
    }
}
