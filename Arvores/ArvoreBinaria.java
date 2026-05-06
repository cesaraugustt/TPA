package Arvores;

import java.util.Comparator;

public class ArvoreBinaria<T> extends ArvoreBinariaBase<T> {

    protected NoArvore<T> raiz;
    protected int qtdNos;

    public ArvoreBinaria(Comparator<T> comparador) {
        super(comparador);
        this.raiz = null;
        this.qtdNos = 0;
    }

    @Override
    public int altura() {
        return alturaRecursivo(raiz);
    }

    private int alturaRecursivo(NoArvore<T> atual) {
        if (atual == null) {
            return -1;
        } else {
            int alturaEsquerda = alturaRecursivo(atual.getEsquerdo());
            int alturaDireita = alturaRecursivo(atual.getDireito());
            if (alturaEsquerda > alturaDireita) {
                return alturaEsquerda + 1;
            } else {
                return alturaDireita + 1;
            }
        }
    }

    @Override
    public String caminharEmNivel() {
        if (raiz == null) {
            return "";
        }
        
        StringBuilder resultado = new StringBuilder();
        java.util.Queue<NoArvore<T>> fila = new java.util.LinkedList<>();
        fila.add(raiz);
        
        while (!fila.isEmpty()) {
            NoArvore<T> atual = fila.poll();
            resultado.append(atual.getValor().toString());
            
            if (atual.getEsquerdo() != null) {
                fila.add(atual.getEsquerdo());
            }
            if (atual.getDireito() != null) {
                fila.add(atual.getDireito());
            }
            
            if (!fila.isEmpty()) {
                resultado.append("\n");
            }
        }
        return resultado.toString();
    }

    @Override
    public String caminharEmOrdem() {
        StringBuilder resultado = new StringBuilder();
        caminharEmOrdemRecursivo(raiz, resultado);
        
        if (resultado.length() > 0) {
            resultado.setLength(resultado.length() - 1); // Remove a última quebra de linha
        }
        
        return resultado.toString();
    }

    private void caminharEmOrdemRecursivo(NoArvore<T> atual, StringBuilder resultado) {
        if (atual != null) {
            caminharEmOrdemRecursivo(atual.getEsquerdo(), resultado);
            resultado.append(atual.getValor().toString()).append("\n");
            caminharEmOrdemRecursivo(atual.getDireito(), resultado);
        }
    }

    @Override
    public int quantidadeNos() {
        return this.qtdNos;
    }

    @Override
    public void adicionar(T novoValor) {
        raiz = adicionarRecursivo(raiz, novoValor);
    }

    protected NoArvore<T> adicionarRecursivo (NoArvore<T> atual, T valor) {
        if (atual == null) {
            this.qtdNos++;
            return new NoArvore<>(valor);
        }

        int comparacao = this.comparador.compare(valor, atual.getValor());

        if (comparacao < 0) {
            NoArvore<T> esquerdo = atual.getEsquerdo();
            atual.setEsquerdo(adicionarRecursivo(esquerdo, valor));
        } else if (comparacao > 0) {
            NoArvore<T> direito = atual.getDireito();
            atual.setDireito(adicionarRecursivo(direito, valor));
        }
        return atual;
    }

    @Override
    public T pesquisar(T valor) {
        NoArvore<T> resultado = pesquisarRecursivo(raiz, valor);
        if (resultado == null) {
            return null;
        }
        return resultado.getValor();
    }

    private NoArvore<T> pesquisarRecursivo(NoArvore<T> atual, T valor) {
        if (atual == null) {
            return null;
        }
        
        int comparacao = this.comparador.compare(valor, atual.getValor());
        
        if (comparacao == 0) {
            return atual;
        } else if (comparacao < 0) {
            NoArvore<T> esquerdo = atual.getEsquerdo();
            return pesquisarRecursivo(esquerdo, valor);
        } else {
            NoArvore<T> direito = atual.getDireito();
            return pesquisarRecursivo(direito, valor);
        }
    }

    @Override
    public boolean remover(T valor) {
        int tamanhoAntes = this.qtdNos;
        raiz = removerRecursivo(raiz, valor);
        return this.qtdNos < tamanhoAntes;
    }

    private NoArvore<T> removerRecursivo(NoArvore<T> atual, T valor) {
        if (atual == null) {
            return null;
        }

        int comparacao = this.comparador.compare(valor, atual.getValor());

        if (comparacao < 0) {
            atual.setEsquerdo(removerRecursivo(atual.getEsquerdo(), valor));
        } else if (comparacao > 0) {
            atual.setDireito(removerRecursivo(atual.getDireito(), valor));
        } else {
            NoArvore<T> esquerdo = atual.getEsquerdo();
            NoArvore<T> direito = atual.getDireito();
            
            // Caso 1: 0 ou 1 filho
            if (esquerdo == null) {
                this.qtdNos--;
                return direito;
            } else if (direito == null) {
                this.qtdNos--;
                return esquerdo;
            } else {
                // Caso 2: 2 filhos
                NoArvore<T> sucessor = encontrarMinimo(direito);
                T valorSucessor = sucessor.getValor();
                
                atual.setValor(valorSucessor);
                atual.setDireito(removerRecursivo(direito, valorSucessor));
            }
        }
        return atual;
    }

    private NoArvore<T> encontrarMinimo(NoArvore<T> atual) {
        while (atual.getEsquerdo() != null) {
            atual = atual.getEsquerdo();
        }
        return atual;
    }
}
