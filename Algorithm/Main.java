package Algorithm;

import java.util.List;

public class Main {
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
