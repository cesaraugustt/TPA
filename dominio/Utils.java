package dominio;

public class Utils {
    
    /**
     * Converte nanosegundos para uma string legível com ms e ns.
     * Formatação padronizada para todos os benchmarks do projeto.
     */
    public static String formatarTempo(long ns) {
        return String.format("%,12.4f ms  (%,15d ns)", ns / 1_000_000.0, ns);
    }
}
