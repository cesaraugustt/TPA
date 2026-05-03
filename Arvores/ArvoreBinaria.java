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
        // TODO: Implementar
        throw new UnsupportedOperationException("Método altura não implementado.");
    }

    @Override
    public String caminharEmNivel() {
        // TODO: Implementar
        throw new UnsupportedOperationException("Método caminharEmNivel não implementado.");
    }

    @Override
    public String caminharEmOrdem() {
        // TODO: Implementar
        throw new UnsupportedOperationException("Método caminharEmOrdem não implementado.");
    }

    @Override
    public int quantidadeNos() {
        return this.qtdNos;
    }

    @Override
    public void adicionar(T novoValor) {
        raiz = adicionarRecursivo(raiz, novoValor);
    }

    private NoArvore<T> adicionarRecursivo (NoArvore<T> atual, T valor) {
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
