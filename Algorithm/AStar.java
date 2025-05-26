package Algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * A* Search Algorithm implementation for pathfinding in a grid.
 * This class uses a priority queue to explore nodes based on their estimated cost (f = g + h).
 */
public class AStar {
    private Graph graph;
    private Node start, goal;
    private int nodesSearched = 0; // Add this field

    /**
     * Constructor for AStar algorithm.
     *
     * @param graph The graph to search in.
     * @param start The starting node.
     * @param goal  The goal node.
     */
    public AStar(Graph graph, Node start, Node goal) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
    }

    /**
     * Executes the A* search algorithm to find the shortest path from start to goal.
     *
     * @return A list of nodes representing the path from start to goal, or null if no path exists.
     */
    public List<Node> search() {
        nodesSearched = 0; // Reset counter at the start of each search
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));
        Set<Node> closedSet = new HashSet<>();

        start.g = 0;
        start.h = calculateHeuristic(start, goal);
        start.f = start.g + start.h;
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            nodesSearched++; // Increment for each node processed

            // Goal reached
            if (current.equals(goal)) {
                System.out.println("Path found!");
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (Node neighbor : graph.getNeighbors(current)) {
                if (closedSet.contains(neighbor)) {
                    continue; // Skip already explored nodes
                }

                double tentativeG = current.g + calculateCost(current, neighbor);
                if (!openSet.contains(neighbor) || tentativeG < neighbor.g) {
                    neighbor.parent = current;
                    neighbor.g = tentativeG;
                    neighbor.h = calculateHeuristic(neighbor, goal);
                    neighbor.f = neighbor.g + neighbor.h;

                    if (openSet.contains(neighbor)) {
                        openSet.remove(neighbor); // Update priority
                    }
                    openSet.add(neighbor);
                }
            }
        }

        System.out.println("No path found.");
        return null;
    }

    /**
     * Returns the number of nodes searched during the last search.
     *
     * @return The number of nodes searched.
     */
    public int getNodesSearched() {
        return nodesSearched;
    }

    /**
     * Calculates the heuristic cost between two nodes.
     * This project uses Manhattan distance, but can be modified for other heuristics.
     *
     * @param a The first node.
     * @param b The second node.
     * @return The heuristic cost.
     */
    protected double calculateHeuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Manhattan distance
    }

    /**
     * Calculates the cost to move from node a to node b.
     * This can be modified to account for different terrain costs or other factors.
     *
     * @param a The starting node.
     * @param b The destination node.
     * @return The cost of moving from a to b.
     */
    protected double calculateCost(Node a, Node b) {
        return 1.0; // Assume cost is constant for simplicity (can be modified)
    }

    /**
     * Reconstructs the path from the goal node back to the start node.
     *
     * @param current The current node (goal).
     * @return A list of nodes representing the path from start to goal.
     */
    private List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();

        // Trace back the parent links to reconstruct the path
        while (current != null) {
            path.add(0, current); // Add at the beginning to avoid reversing later
            current = current.parent;
        }

        return path;
    }

}
