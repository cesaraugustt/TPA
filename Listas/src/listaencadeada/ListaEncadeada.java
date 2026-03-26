package src.listaencadeada;

import src.colecao.IColecao;
import java.util.Objects;

public class ListaEncadeada<T> implements IColecao<T> {

    private int qtd;
    private No<T> primeiro, ultimo;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        No<T> atual = this.primeiro;
        while (atual != null) {
            sb.append(atual.getValor());
            if (atual.getProx() != null) sb.append("\n");
            atual = atual.getProx();
        }
        return sb.toString();
    }

    public ListaEncadeada() { this.primeiro = this.ultimo = null; }

    public int quantidadeNos() {
        return this.qtd;
    }

    public void adicionar(T novoValor) {
        No<T> novoUltimo = new No<>(novoValor);

        if (this.primeiro == null) {
            this.primeiro = this.ultimo = novoUltimo;
        } else {
            this.ultimo.setProx(novoUltimo);
            this.ultimo = novoUltimo;
        }
        this.qtd++;
    }

    public T pesquisar(T valor) {
        No<T> noAtual = this.primeiro;

        while (noAtual != null) {
            if (Objects.equals(noAtual.getValor(), valor)) {
                return noAtual.getValor();
            }
            noAtual = noAtual.getProx();
        }
        return null;
    }

    public boolean remover(T valor) {
        No<T> noAtual    = this.primeiro;
        No<T> noAnterior = null;

        while (noAtual != null) {
            if (Objects.equals(noAtual.getValor(), valor)) {
                if (noAnterior == null) {
                    // remoção do primeiro nó
                    this.primeiro = noAtual.getProx();
                    if (this.primeiro == null) { this.ultimo = null; }
                } else if (noAtual.getProx() != null) {
                    // remoção de nó do meio
                    noAnterior.setProx(noAtual.getProx());
                } else {
                    // remoção do último nó
                    this.ultimo = noAnterior;
                    noAnterior.setProx(null);
                }
                noAtual.setProx(null); // limpa referência do nó removido
                this.qtd--;
                return true;
            }

            noAnterior = noAtual;
            noAtual    = noAtual.getProx();
        }
        return false;
    }
}