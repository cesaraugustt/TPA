package src;

import colecao.IColecao;

public class ListaEncadeada<T> implements IColecao<T> {
    int qtd;
    Node<T> first, last;

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
    
    public ListaEncadeada() { this.first = this.last = null; }

    public int quantidadeNos() {
        return this.qtd;
    }

    public void adicionar(T novoValor) {
        Node<T> novo_ultimo  = new Node<T>();
        novo_ultimo.value = novoValor;
        
        if (this.first == null) { this.first = this.last = novo_ultimo; }
        else {
            this.last.next    = novo_ultimo;
            this.last         = novo_ultimo;
            this.last.next    = null;
        }
        this.qtd += 1;
    }

    public T pesquisar(T valor) {
        Node<T> node_atual = this.first;

        while (node_atual != null) {
            if (node_atual.value.equals(valor)) { return valor; }
            
            node_atual = node_atual.next;
        }
        return null;
    }

    public boolean remover(T v) {
        Node<T> node_atual    = this.first;
        Node<T> node_anterior = node_atual;

        while (node_atual != null) {
            if (node_atual.value.equals(v)) {
                if (node_anterior == node_atual) {
                    this.first = node_atual.next;
                    if (this.first == null) { this.last = null; }
                }
                else if (node_atual.next != null) {
                    node_anterior.next = node_atual.next;
                    node_atual         = null;
                }
                else {
                    this.last          = node_anterior;
                    node_anterior.next = null;
                    node_atual         = null;
                }
                this.qtd--;
                return true; 
            }

            node_anterior = node_atual;
            node_atual    = node_atual.next;
        }
        return false;
    }
}