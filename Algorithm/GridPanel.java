package Algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridPanel extends JPanel {
    private Graph graph;
    private List<Node> path;
    private int cellSize = 50; // Size of each grid cell
    private int animationIndex = 0; // Index to track animation progress
    private boolean isPaused = true; // To track whether the animation is paused
    private Map<String, Image> images;
    private static final int MIN_CELL_SIZE = 5;
    private static final int MAX_CELL_SIZE = 40;
    private static final int PREFERRED_DRAW_SIZE = 800; // pixels

    public GridPanel(Graph graph, List<Node> path) {
        this.graph = graph;
        this.path = path;

        images = new HashMap<>();
        images.put("blocked", new ImageIcon("./wall1.png").getImage());
        images.put("default", new ImageIcon("./ground_dry1.png").getImage());
        images.put("goal", new ImageIcon("./chest.png").getImage());
        images.put("path", new ImageIcon("./path1.png").getImage());
        setPreferredSize(new Dimension(graph.getWidth() * cellSize, graph.getHeight() * cellSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = getCellSize(); // Get the dynamic cell size
        drawGrid(g, cellSize);
        drawStartAndGoal(g, cellSize);

        // Always draw the magenta circle, whether paused or not
        if (path != null && animationIndex < path.size()) {
            drawAnimation(g, cellSize);
        }
    }

    // Update drawGrid to accept cellSize
    private void drawGrid(Graphics g, int cellSize) {
        for (int y = 0; y < graph.getHeight(); y++) {
            for (int x = 0; x < graph.getWidth(); x++) {
                Node node = new Node(x, y);

                g.drawImage(images.get("default"), x * cellSize, y * cellSize, cellSize, cellSize, this);

                // Draw the path image if the node is part of the path
                if (path != null && path.contains(node)) {
                    g.drawImage(images.get("path"), x * cellSize, y * cellSize, cellSize, cellSize, this);
                }

                // Draw blocked nodes in red
                if (graph.isBlocked(x, y)) {
                    g.drawImage(images.get("blocked"), x * cellSize, y * cellSize, cellSize, cellSize, this);
                } else if (node.equals(graph.getGoal())) {
                    g.drawImage(images.get("goal"), x * cellSize, y * cellSize, cellSize, cellSize, this);
                }

                // Draw teleportation nodes with a blue circle
                if (graph.isTeleportationNode(node)) {
                    g.setColor(Color.orange);
                    int ovalMargin = Math.max(2, cellSize / 5);
                    g.fillOval(x * cellSize + ovalMargin, y * cellSize + ovalMargin, cellSize - 2 * ovalMargin,
                            cellSize - 2 * ovalMargin);
                }

                // Draw the grid lines
                g.setColor(Color.BLACK);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    // Update drawStartAndGoal to accept cellSize
    private void drawStartAndGoal(Graphics g, int cellSize) {
        g.setColor(Color.BLUE);
        int margin = Math.max(2, cellSize / 10);
        g.fillRect(graph.getStart().x * cellSize + margin, graph.getStart().y * cellSize + margin,
                cellSize - 2 * margin, cellSize - 2 * margin);
    }

    // Update drawAnimation to accept cellSize
    private void drawAnimation(Graphics g, int cellSize) {
        Node current = path.get(animationIndex);
        g.setColor(Color.MAGENTA);
        int margin = Math.max(2, cellSize / 10);
        g.fillOval(current.x * cellSize + margin, current.y * cellSize + margin, cellSize - 2 * margin,
                cellSize - 2 * margin);
    }

    // Increment animation index and trigger repaint
    public void animate() {
        if (animationIndex < path.size() && !isPaused) {
            animationIndex++;
            repaint(); // Trigger paintComponent to redraw the panel
        }
    }

    // Decrement animation index to step backward
    public void stepBackward() {
        if (animationIndex > 0) {
            animationIndex--;
            repaint();
        }
    }

    // Increment animation index to step forward
    public void stepForward() {
        if (animationIndex < path.size()) {
            animationIndex++;
            repaint();
        }
    }

    // Reset the animation to the first frame and trigger a repaint
    public void resetAnimation() {
        animationIndex = 0;
        isPaused = true; // Ensure that the animation isn't paused after reset
        repaint(); // Trigger repaint to reset the panel's view
    }

    // Toggle the animation pause state
    public void togglePause() {
        isPaused = !isPaused;

        // If the animation is resumed, continue from the current position
        if (!isPaused) {
            new Thread(() -> {
                while (animationIndex < path.size() && !isPaused) {
                    animate();
                    try {
                        Thread.sleep(100); // Adjust this value to control the animation speed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        repaint(); // Ensure repaint happens when pausing or resuming
    }

    // Return whether the animation is paused
    public boolean isPaused() {
        return isPaused;
    }

    public void setPath(List<Node> path) {
        this.path = path;
        this.animationIndex = 0; // Reset animation index
        repaint(); // Refresh the grid visualization
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        setPreferredSize(new Dimension(graph.getWidth() * cellSize, graph.getHeight() * cellSize));
        repaint();
    }

    public int getCellSize() {
        int cellWidth = Math.max(MIN_CELL_SIZE, PREFERRED_DRAW_SIZE / graph.getWidth());
        int cellHeight = Math.max(MIN_CELL_SIZE, PREFERRED_DRAW_SIZE / graph.getHeight());
        return Math.min(cellWidth, cellHeight);
    }

    @Override
    public Dimension getPreferredSize() {
        int cellSize = getCellSize();
        return new Dimension(graph.getWidth() * cellSize, graph.getHeight() * cellSize);
    }

}
