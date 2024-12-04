package Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {
    int x, y; // Coordinates (if you're using a 2D grid)
    double g, h, f; // g, h, and f values for A*
    Node parent; // To store the path
    List<Node> neighbors; // Adjacent nodes

    // Constructor
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.neighbors = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}