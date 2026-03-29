package src.listaencadeada;

import src.colecao.IColecao;

import java.util.LinkedList;

public class ListaEncadeadaLinkedList<T> implements IColecao<T> {

    private final LinkedList<T> lista;

    public ListaEncadeadaLinkedList() {
        this.lista = new LinkedList<>();
    }

    @Override
    public void adicionar(T novoValor) {
        lista.add(novoValor);
    }

    @Override
    public T pesquisar(T valor) {
        int indice = lista.indexOf(valor);
        return indice >= 0 ? lista.get(indice) : null;
    }

    @Override
    public boolean remover(T valor) {
        return lista.remove(valor);
    }

    @Override
    public int quantidadeNos() {
        return lista.size();
    }
}
