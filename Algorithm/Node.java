package Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Node class representing a point in the grid with coordinates and cost attributes.
 * This class is used in pathfinding algorithms like A* to represent nodes in the search space.
 */
public class Node {
    int x, y; // Coordinates (if you're using a 2D grid)
    double g, h, f; // g, h, and f values for A*
    Node parent; // To store the path
    List<Node> neighbors; // Adjacent nodes

    /**
     * Constructor for Node.
     *
     * @param x The x-coordinate of the node.
     * @param y The y-coordinate of the node.
     */
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.neighbors = new ArrayList<>();
    }

    /**
     * returns a string representation of the node's coordinates.
     * This is useful for debugging and visualization purposes.
     * 
     * @return A string representation of the node's coordinates in the format "(x, y)".
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Checks if two nodes are equal based on their coordinates.
     * This is important for ensuring that nodes are correctly identified in the search algorithm.
     * Nodes are considered equal if they have the same x and y coordinates.
     * 
     * @param obj The object to compare with this node.
     * @return true if the object is a Node with the same coordinates, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Node node = (Node) obj;
        return x == node.x && y == node.y;
    }

    /**
     * Generates a hash code for the node based on its coordinates.
     * This is used in hash-based collections like HashSet to ensure that nodes can be uniquely identified.
     * 
     * @return The hash code of the node.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
