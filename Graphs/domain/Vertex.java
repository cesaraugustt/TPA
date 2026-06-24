package domain;

public class Vertex<T> {

    private T value;

    public Vertex(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
