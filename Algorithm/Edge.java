package Algorithm;
public class Edge {
    Node start, end;
    double cost;  // The cost of traveling this edge

    // Constructor
    public Edge(Node start, Node end, double cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }
}
