package src.listaencadeada;

/**
 * Generic node for use in a linked list.
 */
public class No<T> {
    private T valor;
    private No<T> prox;

    
    public No(T valor){
        this.valor = valor;
        this.prox=null;
    }
    /**
     * @return the valor
     */
    public T getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(T valor) {
        this.valor = valor;
    }

    /**
     * @return the prox
     */
    public No<T> getProx() {
        return prox;
    }

    /**
     * @param prox the prox to set
     */
    public void setProx(No<T> prox) {
        this.prox = prox;
    }        
}