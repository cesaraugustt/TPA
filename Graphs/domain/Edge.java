package domain;

public class Edge<T> {

    private Vertex<T> origin;
    private Vertex<T> destination;
    private float capacity;

    public Edge(
            Vertex<T> origin,
            Vertex<T> destination,
            float capacity
    ) {
        this.origin = origin;
        this.destination = destination;
        this.capacity = capacity;
    }

    public Vertex<T> getOrigin() {
        return origin;
    }

    public Vertex<T> getDestination() {
        return destination;
    }

    public float getCapacity() {
        return capacity;
    }

    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }
}
