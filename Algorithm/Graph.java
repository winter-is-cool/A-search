package Algorithm;

import java.util.*;

/**
 * Graph class representing a grid with nodes, blocked cells, teleportation
 * links, and wrap-around functionality.
 * This class provides methods to generate a random grid, manage nodes, and find
 * neighbors with teleportation and wrap-around capabilities.
 */
public class Graph {
    private int width, height;
    private boolean[][] blocked;
    private Node start, goal;
    private Map<Node, Node> teleportationLinks; // Teleportation nodes
    private boolean wrapAroundEnabled = false; // Wrap-around flag

    /**
     * Node class representing a point in the grid with coordinates and cost
     * attributes.
     */
    public Graph(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocked = new boolean[width][height];
        this.teleportationLinks = new HashMap<>();
    }

    /**
     * Generates a random grid with specified dimensions, blocked cells,
     * teleportation nodes, and a random seed.
     *
     * @param width           Width of the grid.
     * @param height          Height of the grid.
     * @param blockedPercent  Percentage of cells to be blocked.
     * @param teleportPercent Percentage of cells to be teleportation nodes.
     * @param seed            Random seed for reproducibility.
     */
    public void generateRandomGrid(int width, int height, double blockedPercent, double teleportPercent, long seed) {
        Random rand = new Random(seed);
        clearGrid();

        List<Node> teleportNodes = new ArrayList<>();

        // Set blocked and teleportation nodes
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double r = rand.nextDouble();
                if (r < blockedPercent) {
                    blockNode(x, y);
                } else if (r < blockedPercent + teleportPercent) {
                    Node tNode = new Node(x, y);
                    teleportNodes.add(tNode);
                }
            }
        }

        // Ensure even number of teleportation nodes
        if (teleportNodes.size() % 2 == 1 && !teleportNodes.isEmpty()) {
            teleportNodes.remove(rand.nextInt(teleportNodes.size()));
        }

        // Randomly pair up teleportation nodes
        Collections.shuffle(teleportNodes, rand);
        for (int i = 0; i + 1 < teleportNodes.size(); i += 2) {
            Node a = teleportNodes.get(i);
            Node b = teleportNodes.get(i + 1);
            addTeleportationLink(a, b);
            addTeleportationLink(b, a);
        }
        // If odd number, last node is left unlinked

        // Random start and goal (not blocked or teleport)
        Node start, goal;
        do {
            start = new Node(rand.nextInt(width), rand.nextInt(height));
        } while (isBlocked(start.x, start.y) || teleportNodes.contains(start));
        setStart(start);

        do {
            goal = new Node(rand.nextInt(width), rand.nextInt(height));
        } while (isBlocked(goal.x, goal.y) || teleportNodes.contains(goal) || goal.equals(start));
        setGoal(goal);
    }

    /**
     * Sets the start node for pathfinding.
     * 
     * @param start
     */
    public void setStart(Node start) {
        this.start = start;
    }

    /**
     * Sets the goal node for pathfinding.
     * 
     * @param goal
     */
    public void setGoal(Node goal) {
        this.goal = goal;
    }

    /**
     * Gets the start node.
     * 
     * @return The start node.
     */
    public Node getStart() {
        return start;
    }

    /**
     * Gets the goal node.
     * 
     * @return The goal node.
     */
    public Node getGoal() {
        return goal;
    }

    /**
     * Blocks a node at the specified coordinates.
     * 
     * @param x X-coordinate of the node to block.
     * @param y Y-coordinate of the node to block.
     */
    public void blockNode(int x, int y) {
        blocked[x][y] = true;
    }

    /**
     * Checks if a node at the specified coordinates is blocked.
     * 
     * @param x X-coordinate of the node.
     * @param y Y-coordinate of the node.
     * @return True if the node is blocked, false otherwise.
     */
    public boolean isBlocked(int x, int y) {
        return blocked[x][y];
    }

    /**
     * Enables or disables wrap-around functionality for the grid.
     * When enabled, nodes at the edges will wrap around to the opposite edge.
     * 
     * @param enabled True to enable wrap-around, false to disable.
     */
    public void setWrapAroundEnabled(boolean enabled) {
        this.wrapAroundEnabled = enabled;
    }

    /**
     * Checks if wrap-around functionality is enabled.
     * 
     * @return True if wrap-around is enabled, false otherwise.
     */
    public boolean isWrapAroundEnabled() {
        return wrapAroundEnabled;
    }

    /**
     * Adds a teleportation link between two nodes.
     * 
     * @param from The node from which the teleportation occurs.
     * @param to   The destination node for the teleportation.
     */
    public void addTeleportationLink(Node from, Node to) {
        teleportationLinks.put(from, to);
    }

    /**
     * Removes a teleportation link for a specific node.
     * 
     * @param node The node for which the teleportation link is removed.
     */
    public boolean isTeleportationNode(Node node) {
        return teleportationLinks.containsKey(node);
    }

    /**
     * Gets the teleportation destination for a specific node.
     * 
     * @param node The node for which to get the teleportation destination.
     * @return The destination node if it exists, null otherwise.
     */
    public Node getTeleportDestination(Node node) {
        return teleportationLinks.get(node);
    }

    /**
     * Gets the neighbors of a node, including wrap-around and teleportation links.
     * 
     * @param node The node for which to find neighbors.
     * @return A list of neighboring nodes.
     */
    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        // Standard directions (Up, Right, Down, Left)
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        for (int[] dir : directions) {
            int newX = node.x + dir[0];
            int newY = node.y + dir[1];

            if (wrapAroundEnabled) {
                // Wrap-around logic
                if (newX < 0)
                    newX = width - 1;
                if (newX >= width)
                    newX = 0;
                if (newY < 0)
                    newY = height - 1;
                if (newY >= height)
                    newY = 0;
            }

            if (isValid(newX, newY)) {
                neighbors.add(new Node(newX, newY));
            }
        }

        // **Check if the node has a teleportation destination**
        if (isTeleportationNode(node)) {
            neighbors.add(getTeleportDestination(node));
        }

        return neighbors;
    }

    /**
     * Checks if the given coordinates are valid (within bounds and not blocked).
     * 
     * @param x X-coordinate.
     * @param y Y-coordinate.
     * @return True if valid, false otherwise.
     */
    private boolean isValid(int x, int y) {
        // Ensure the indices are within bounds and the node is not blocked
        return (x >= 0 && x < width && y >= 0 && y < height) && !isBlocked(x, y);
    }

    /**
     * Gets the width of the grid.
     * 
     * @return The width of the grid.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the grid.
     * 
     * @return The height of the grid.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Unblocks a node at the specified coordinates.
     * 
     * @param x X-coordinate of the node to unblock.
     * @param y Y-coordinate of the node to unblock.
     */
    public void unblockNode(int x, int y) {
        blocked[x][y] = false;
    }

    /**
     * Gets a list of all teleportation nodes in the grid.
     * 
     * @return A list of nodes that have teleportation links.
     */
    public List<Node> getTeleportationNodes() {
        return new ArrayList<>(teleportationLinks.keySet());
    }

    /**
     * Clears the grid, removing all blocked nodes, teleportation links, and
     * resetting start and goal nodes.
     */
    public void clearGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                blocked[x][y] = false;
            }
        }
        teleportationLinks.clear();
        start = null;
        goal = null;
    }

    /**
     * Gets the cost of moving from one node to another.
     * This can be modified to account for different terrain costs or other factors.
     * 
     * @param from The starting node.
     * @param to   The destination node.
     * @return The cost of moving from 'from' to 'to'.
     */
    public double getCost(Node from, Node to) {
        return 1.0; // Uniform cost for all edges
    }

    /**
     * Gets the number of unblocked nodes (i.e., usable vertices).
     * 
     * @return The count of non-blocked vertices.
     */
    public int getVertexCount() {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!blocked[x][y]) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Gets the number of valid edges in the grid.
     * Each unblocked node can have up to 4 neighbors (up, down, left, right),
     * and optionally teleportation links.
     * 
     * @return The total number of edges in the graph.
     */
    public int getEdgeCount() {
        int edgeCount = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!blocked[x][y]) {
                    Node node = new Node(x, y);
                    List<Node> neighbors = getNeighbors(node);
                    edgeCount += neighbors.size();
                }
            }
        }
        return edgeCount;
    }

}
