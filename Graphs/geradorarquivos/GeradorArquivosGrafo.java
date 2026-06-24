package geradorarquivos;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeradorArquivosGrafo {
    
    final char vogais[] = {'a', 'e', 'i', 'o','u','A','E','I','O','U'};
    final Random rand = new Random();
    
    private boolean ehVogal (char c){
        for (char l: vogais){
            if (l==c) return true;
        }
        return false;
    }
    
    private char geraVogal(boolean min){
        if (min)
            return vogais[rand.nextInt(5)];
        else
            return vogais[5+rand.nextInt(5)];       
    }

    private char geraLetra(boolean min){
        if (min)
            return (char) ('a'+rand.nextInt(26));
        else
            return (char) ('A'+rand.nextInt(26));
    }

    
    private String geraPalavra(int tam){
        int cont;
        String palavra = "";

        palavra+= geraLetra(false);
        for(cont=1;cont<tam;cont++){
            if (ehVogal(palavra.charAt(cont-1)))
                palavra+=geraLetra(true);
            else
                palavra+=geraVogal(true);        
        }
        return palavra;
    }    
    
     private void geraArquivo(int n){
        int i;
        String nome;
        FileWriter arq;
        try {
            arq = new FileWriter("entrada.txt");
            PrintWriter gravarArq = new PrintWriter(arq);
            //Gerando linha com quantidade de cidades
            gravarArq.println(n);
            //Gerando linhas com códigos e nomes de cidades
            for(i=1;i<=n;i++){
                nome = geraPalavra(3+rand.nextInt(10));
                gravarArq.printf("%d,%s%n",i, nome);
            }
            int tamVetDist = (n*n-n)/2;
            double distancias[] = new double[tamVetDist];
            int l,c;
            i=0;
            for(l=0;l<n;l++){
                for(c=l+1;c<n;c++){
                    distancias[i]=geraDistancia(l,c);
                    i++;
                }
            }

            for(l=0;l<n;l++){
                for(c=0;c<n-1;c++){
                    if(l==c)
                        gravarArq.printf("%.2f,",0.0);
                    else if (l<c)
                        gravarArq.printf("%.2f,",distancias[l*n-(l*l+l)/2+c-l-1]);
                    else
                        gravarArq.printf("%.2f,",distancias[c*n-(c*c+c)/2+l-c-1]);
                }
                if(l==(n-1))
                    gravarArq.printf("%.2f%n",0.0);
                else
                    gravarArq.printf("%.2f%n",distancias[l*n-(l*l+l)/2+c-l-1]);
            }
            arq.close();
        } catch (IOException ex) {
            Logger.getLogger(GeradorArquivosGrafo.class.getName()).log(Level.SEVERE, null, ex);
        }
     }        

    double geraDistancia(int linha, int coluna){
        double r=rand.nextDouble();
        if (coluna-linha==1){
            return r*100+10;
        }
        else{
            if (r<0.3)
                return 0.0;
            else
                return r * 100;
        }
    }
     
    public static void main(String[] args) throws IOException {
        GeradorArquivosGrafo g = new GeradorArquivosGrafo();
        Locale.setDefault(Locale.US);
        
        int TAM = 5;
        long tempoInicial = System. currentTimeMillis();        
        g.geraArquivo(TAM);
        long tempoFinal = System. currentTimeMillis();
        
        System.out.println("Tempo Total de geração do arquivo em ms: " + (tempoFinal - tempoInicial));
    }
}
