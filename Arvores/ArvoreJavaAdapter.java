package Arvores;

import colecao.IColecao;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * Adapter para utilizar a árvore padrão do Java (TreeMap - Red-Black Tree)
 * como uma IColecao. O TreeMap é utilizado mapeando o próprio objeto como
 * chave e valor, permitindo o retorno direto do objeto no método pesquisar.
 * 
 * @param <T>
 */
public class ArvoreJavaAdapter<T> implements IColecao<T> {

    private TreeMap<T, T> arvore;

    public ArvoreJavaAdapter(Comparator<T> comparator) {
        this.arvore = new TreeMap<>(comparator);
    }

    @Override
    public void adicionar(T novoValor) {
        // Usa o objeto tanto como chave quanto como valor
        this.arvore.put(novoValor, novoValor);
    }

    @Override
    public T pesquisar(T valor) {
        // O TreeMap permite resgatar diretamente o valor armazenado usando a chave
        return this.arvore.get(valor);
    }

    @Override
    public boolean remover(T valor) {
        // Se a remoção retornar o objeto, significa que ele existia
        return this.arvore.remove(valor) != null;
    }

    @Override
    public int quantidadeNos() {
        return this.arvore.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T elemento : arvore.values()) {
            sb.append(elemento.toString()).append("\n");
        }
        return sb.toString();
    }
}
