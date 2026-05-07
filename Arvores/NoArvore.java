package Arvores;

public class NoArvore<T> {
    private T valor;
    private NoArvore<T> esquerdo;
    private NoArvore<T> direito;
    private int altura;

    public NoArvore(T valor) {
        this.valor = valor;
        this.esquerdo = null;
        this.direito = null;
        this.altura = 0;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    public NoArvore<T> getEsquerdo() {
        return esquerdo;
    }

    public void setEsquerdo(NoArvore<T> esquerdo) {
        this.esquerdo = esquerdo;
    }

    public NoArvore<T> getDireito() {
        return direito;
    }

    public void setDireito(NoArvore<T> direito) {
        this.direito = direito;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }
}
