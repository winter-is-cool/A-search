package Algorithm;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create a 5x5 grid
        Graph graph = new Graph(5, 5);
        graph.blockNode(1, 0);
        graph.blockNode(1, 1);
        graph.blockNode(1, 2);
        graph.blockNode(1, 4);

        Node start = new Node(0, 0);
        Node goal = new Node(4, 4);

        graph.setStart(start);
        graph.setGoal(goal);

        AStar aStar = new AStar(graph, start, goal);
        List<Node> path = aStar.search();

        if (path == null) {
            System.out.println("No path exists.");
            printGrid(graph, path);
        } else {
            System.out.println("Path found!");
            printGrid(graph, path);
        }
    }

    // Updated printGrid method
    public static void printGrid(Graph graph, List<Node> path) {
        // Print the grid
        for (int y = 0; y < graph.getHeight(); y++) {
            for (int x = 0; x < graph.getWidth(); x++) {
                Node node = new Node(x, y);

                // Check if the node is the start, goal, or in the path
                if (node.equals(graph.getStart())) {
                    System.out.print("S "); // Start node
                } else if (node.equals(graph.getGoal())) {
                    System.out.print("G "); // Goal node
                } else if (path != null && path.contains(node)) {
                    System.out.print("P "); // Path node
                } else if (graph.isBlocked(x, y)) {
                    System.out.print("X "); // Blocked node
                } else {
                    System.out.print(". "); // Empty node
                }
            }
            System.out.println();
        }
    }
}
