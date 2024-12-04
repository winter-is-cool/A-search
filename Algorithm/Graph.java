package Algorithm;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private int width, height;
    private boolean[][] blocked;
    private Node start, goal; // Add start and goal nodes

    public Graph(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocked = new boolean[width][height];
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

    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // Up, Right, Down, Left
        for (int[] dir : directions) {
            int newX = node.x + dir[0];
            int newY = node.y + dir[1];
            if (isValid(newX, newY)) {
                neighbors.add(new Node(newX, newY));
            }
        }
        return neighbors;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height && !isBlocked(x, y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
