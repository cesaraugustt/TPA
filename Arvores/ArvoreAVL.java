package Arvores;

import java.util.Comparator;

public class ArvoreAVL<T> extends ArvoreBinaria<T> {

    public ArvoreAVL(Comparator<T> comparador) {
        super(comparador);
    }

    private int altura(NoArvore<T> no) {
        if (no == null) {
            return -1;
        }
        return no.getAltura();
    }

    private void atualizarAltura(NoArvore<T> no) {
        if (no != null) {
            int alturaEsquerda = altura(no.getEsquerdo());
            int alturaDireita = altura(no.getDireito());
            
            if (alturaEsquerda > alturaDireita) {
                no.setAltura(alturaEsquerda + 1);
            } else {
                no.setAltura(alturaDireita + 1);
            }
        }
    }

    private int fatorBalanceamento(NoArvore<T> no) {
        if (no == null) {
            return 0;
        }
        return altura(no.getDireito()) - altura(no.getEsquerdo());
    }

    private NoArvore<T> rotacaoEsquerda(NoArvore<T> no) {
        NoArvore<T> novaRaiz = no.getDireito();
        no.setDireito(novaRaiz.getEsquerdo());
        novaRaiz.setEsquerdo(no);

        atualizarAltura(no);
        atualizarAltura(novaRaiz);

        return novaRaiz;
    }

    private NoArvore<T> rotacaoDireita(NoArvore<T> no) {
        NoArvore<T> novaRaiz = no.getEsquerdo();
        no.setEsquerdo(novaRaiz.getDireito());
        novaRaiz.setDireito(no);

        atualizarAltura(no);
        atualizarAltura(novaRaiz);

        return novaRaiz;
    }

    private NoArvore<T> rotacaoEsquerdaDireita(NoArvore<T> no) {
        no.setEsquerdo(rotacaoEsquerda(no.getEsquerdo()));
        return rotacaoDireita(no);
    }

    private NoArvore<T> rotacaoDireitaEsquerda(NoArvore<T> no) {
        no.setDireito(rotacaoDireita(no.getDireito()));
        return rotacaoEsquerda(no);
    }

    private NoArvore<T> balancear(NoArvore<T> no) {
        atualizarAltura(no);
        int balanceamento = fatorBalanceamento(no);

        // Desbalanceada para a direita
        if (balanceamento > 1) {
            // Verifica se a subárvore direita está desbalanceada para a esquerda
            if (fatorBalanceamento(no.getDireito()) < 0) {
                return rotacaoDireitaEsquerda(no);
            }
            // Rotação simples à esquerda
            return rotacaoEsquerda(no);
        }

        // Desbalanceada para a esquerda
        if (balanceamento < -1) {
            // Verifica se a subárvore esquerda está desbalanceada para a direita
            if (fatorBalanceamento(no.getEsquerdo()) > 0) {
                return rotacaoEsquerdaDireita(no);
            }
            // Rotação simples à direita
            return rotacaoDireita(no);
        }

        return no; // Já está balanceada
    }

    @Override
    protected NoArvore<T> adicionarRecursivo(NoArvore<T> atual, T valor) {
        atual = super.adicionarRecursivo(atual, valor);
        return balancear(atual);
    }
}
