package Algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GridPanel extends JPanel {
    private Graph graph;
    private List<Node> path;
    private int cellSize = 50;  // Size of each grid cell
    private int animationIndex = 0;  // Index to track animation progress
    private boolean isPaused = true; // To track whether the animation is paused

    public GridPanel(Graph graph, List<Node> path) {
        this.graph = graph;
        this.path = path;
        setPreferredSize(new Dimension(graph.getWidth() * cellSize, graph.getHeight() * cellSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawPath(g);
        drawStartAndGoal(g);

        // Always draw the magenta circle, whether paused or not
        if (path != null && animationIndex < path.size()) {
            drawAnimation(g);
        }
    }

    // Draw the grid with blocked nodes and path
    private void drawGrid(Graphics g) {
        for (int y = 0; y < graph.getHeight(); y++) {
            for (int x = 0; x < graph.getWidth(); x++) {
                Node node = new Node(x, y);

                // Draw blocked nodes in red
                if (graph.isBlocked(x, y)) {
                    g.setColor(Color.RED);
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
                // Draw the grid lines
                g.setColor(Color.BLACK);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    // Draw the path nodes
    private void drawPath(Graphics g) {
        if (path != null) {
            g.setColor(Color.GREEN);
            for (Node node : path) {
                g.fillRect(node.x * cellSize + 5, node.y * cellSize + 5, cellSize - 10, cellSize - 10);
            }
        }
    }

    // Draw the start (S) and goal (G) nodes
    private void drawStartAndGoal(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(graph.getStart().x * cellSize + 5, graph.getStart().y * cellSize + 5, cellSize - 10, cellSize - 10);

        g.setColor(Color.ORANGE);
        g.fillRect(graph.getGoal().x * cellSize + 5, graph.getGoal().y * cellSize + 5, cellSize - 10, cellSize - 10);
    }

    // Draw the animated object (path) moving along
    private void drawAnimation(Graphics g) {
        Node current = path.get(animationIndex);
        g.setColor(Color.MAGENTA);
        g.fillOval(current.x * cellSize + 5, current.y * cellSize + 5, cellSize - 10, cellSize - 10);
    }

    // Increment animation index and trigger repaint
    public void animate() {
        if (animationIndex < path.size() && !isPaused) {
            animationIndex++;
            repaint();  // Trigger paintComponent to redraw the panel
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
        isPaused = false; // Ensure that the animation isn't paused after reset
        repaint();  // Trigger repaint to reset the panel's view
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

        repaint();  // Ensure repaint happens when pausing or resuming
    }

    // Return whether the animation is paused
    public boolean isPaused() {
        return isPaused;
    }
}
