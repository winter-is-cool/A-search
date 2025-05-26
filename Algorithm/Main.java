package Algorithm;

import java.util.List;

/**
 * Entry point for the A* Search Visualizer application.
 * Initializes the grid and launches the GUI.
 * This class sets up a 5x5 grid, blocks certain nodes, adds teleportation links,
 * and runs the A* search algorithm to find a path from the start node to the goal node.
 * If a path is found, it visualizes the grid with the path highlighted.
 * @author winteriscool / winter_is_cool_
 * @version 0.1.0
 * @since 2025-05-27
 */
public class Main {

    /**
     * launches the A* Search Visualizer application.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Create a 5x5 grid
        Graph graph = new Graph(5, 5);
        graph.blockNode(1, 0);
        graph.blockNode(1, 1);
        graph.blockNode(1, 2);
        graph.blockNode(1, 3);

        graph.addTeleportationLink(new Node(4, 0), new Node(3, 3));
        graph.addTeleportationLink(new Node(3, 3), new Node(4, 0));
        
        Node start = new Node(0, 0);
        Node goal = new Node(3, 2);

        // Set start and goal in the graph
        graph.setStart(start);
        graph.setGoal(goal);

        AStar aStar = new AStar(graph, start, goal);
        List<Node> path = aStar.search();

        if (path == null) {
            System.out.println("No path exists.");
        } else {
            System.out.println("Path: " + path);
            // Visualize the grid with animation
            GridVisualizer visualizer = new GridVisualizer(graph, path);
            visualizer.visualize();
        }
    }
}
