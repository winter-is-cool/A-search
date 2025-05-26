package Algorithm;

public class AlgorithmFactory {
    public static AStar createAlgorithm(String algorithm, Graph graph, Node start, Node goal) {
        switch (algorithm) {
            case "A* Search":
                return new AStar(graph, start, goal); // Default A* behavior
            case "Greedy Best-First Search":
                return new AStar(graph, start, goal) {
                    @Override
                    protected double calculateHeuristic(Node a, Node b) {
                        return super.calculateHeuristic(a, b); // Use only heuristic
                    }

                    @Override
                    protected double calculateCost(Node a, Node b) {
                        return 0; // Ignore cost-so-far
                    }
                };
            case "Dijkstra's Algorithm":
                return new AStar(graph, start, goal) {
                    @Override
                    protected double calculateHeuristic(Node a, Node b) {
                        return 0; // Disable heuristic
                    }
                };
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }
}