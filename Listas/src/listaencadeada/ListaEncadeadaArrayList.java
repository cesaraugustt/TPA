package src.listaencadeada;

import src.colecao.IColecao;

import java.util.ArrayList;

public class ListaEncadeadaArrayList<T> implements IColecao<T> {

    private final ArrayList<T> lista;

    public ListaEncadeadaArrayList() {
        this.lista = new ArrayList<>();
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
