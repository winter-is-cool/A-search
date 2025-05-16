package Algorithm;

import java.util.*;

public class Graph {
    private int width, height;
    private boolean[][] blocked;
    private Node start, goal;
    private Map<Node, Node> teleportationLinks; // Teleportation nodes
    private boolean wrapAroundEnabled = false; // Wrap-around flag

    public Graph(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocked = new boolean[width][height];
        this.teleportationLinks = new HashMap<>();
    }

    // Set start and goal nodes
    public void setStart(Node start) {
        this.start = start;
    }

    public void setGoal(Node goal) {
        this.goal = goal;
    }

    // Get start and goal nodes
    public Node getStart() {
        return start;
    }

    public Node getGoal() {
        return goal;
    }

    public void blockNode(int x, int y) {
        blocked[x][y] = true;
    }

    public boolean isBlocked(int x, int y) {
        return blocked[x][y];
    }

    // Enable or disable wrap-around
    public void setWrapAroundEnabled(boolean enabled) {
        this.wrapAroundEnabled = enabled;
    }

    public boolean isWrapAroundEnabled() {
        return wrapAroundEnabled;
    }
    
    // **ADD TELEPORTATION NODES**
    public void addTeleportationLink(Node from, Node to) {
        teleportationLinks.put(from, to);
    }

    public boolean isTeleportationNode(Node node) {
        return teleportationLinks.containsKey(node);
    }

    public Node getTeleportDestination(Node node) {
        return teleportationLinks.get(node);
    }

    // **MODIFY NEIGHBORS TO INCLUDE TELEPORTS AND WRAP-AROUND**
    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        // Standard directions (Up, Right, Down, Left)
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        for (int[] dir : directions) {
            int newX = node.x + dir[0];
            int newY = node.y + dir[1];

            if (wrapAroundEnabled) {
                // Wrap-around logic
                if (newX < 0) newX = width - 1;
                if (newX >= width) newX = 0;
                if (newY < 0) newY = height - 1;
                if (newY >= height) newY = 0;
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

    private boolean isValid(int x, int y) {
        // Ensure the indices are within bounds and the node is not blocked
        return (x >= 0 && x < width && y >= 0 && y < height) && !isBlocked(x, y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void unblockNode(int x, int y) {
        blocked[x][y] = false;
    }
    
    public List<Node> getTeleportationNodes() {
        return new ArrayList<>(teleportationLinks.keySet());
    }
}
