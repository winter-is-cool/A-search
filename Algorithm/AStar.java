package Algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class AStar {
    private Graph graph;
    private Node start, goal;
    private int nodesSearched = 0; // Add this field

    public AStar(Graph graph, Node start, Node goal) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
    }

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

    public int getNodesSearched() {
        return nodesSearched;
    }

    // Calculate heuristic (e.g., Euclidean or Manhattan)
    private double calculateHeuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Example for Manhattan
    }

    // Calculate cost (e.g., distance between nodes)
    private double calculateCost(Node a, Node b) {
        return 1.0; // Assume cost is constant for simplicity (can be modified)
    }

    // Reconstruct the path by backtracking through parent nodes
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
