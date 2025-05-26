package Algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GridPanel is a JPanel that visualizes a grid-based graph and animates a path through it.
 * It allows interaction to set start and goal nodes, and provides methods to animate the path.
 */
public class GridPanel extends JPanel {
    private Graph graph; // The graph to visualize
    private List<Node> path; // The path to animate through the grid
    private int cellSize = 50; // Size of each grid cell
    private int animationIndex = 0; // Index to track animation progress
    private boolean isPaused = true; // To track whether the animation is paused
    private Map<String, Image> images; // Map to hold images for different node types
    private static final int MIN_CELL_SIZE = 5; // pixels
    private static final int PREFERRED_DRAW_SIZE = 800; // pixels
    private boolean settingStart = true; // true: set start, false: set end
    private GridVisualizer visualizer; // Reference to the visualizer for interaction

    /**
     * Constructor for GridPanel.
     *
     * @param graph The graph to visualize.
     * @param path  The path to animate.
     * @param visualizer The visualizer that manages the grid and path updates.
     */
    public GridPanel(Graph graph, List<Node> path, GridVisualizer visualizer) {
        this.graph = graph;
        this.path = path;
        this.visualizer = visualizer;

        images = new HashMap<>();
        images.put("blocked", new ImageIcon("./resources/wall1.png").getImage());
        images.put("default", new ImageIcon("./resources/ground_dry1.png").getImage());
        images.put("goal", new ImageIcon("./resources/chest.png").getImage());
        images.put("path", new ImageIcon("./resources/path1.png").getImage());
        setPreferredSize(new Dimension(graph.getWidth() * cellSize, graph.getHeight() * cellSize));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int cellSize = getCellSize();
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;

                // Check if the click is within the grid bounds
                if (x < 0 || x >= graph.getWidth() || y < 0 || y >= graph.getHeight())
                    return;
                if (graph.isBlocked(x, y))
                    return; // Don't allow start/end on blocked

                // Toggle the setting for start and goal
                if (settingStart) {
                    graph.setStart(new Node(x, y));
                } else {
                    graph.setGoal(new Node(x, y));
                }

                repaint();
                if (visualizer != null) {
                    visualizer.recalculateAndDisplayPath();
                    visualizer.updateSeedFieldWithCurrentState();
                }
            }
        });
    }

    /**
     * Paints the grid and the path animation.
     * @param g The Graphics object to draw on.
     * This method dynamically calculates the cell size based on the grid dimensions
     */
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

    /**
     * Draws the grid, including the path and blocked nodes.
     *
     * @param g        The Graphics object to draw on.
     * @param cellSize The size of each cell in the grid.
     */
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

    /**
     * Draws the start and goal nodes on the grid.
     *
     * @param g        The Graphics object to draw on.
     * @param cellSize The size of each cell in the grid.
     */
    private void drawStartAndGoal(Graphics g, int cellSize) {
        g.setColor(Color.BLUE);
        int margin = Math.max(2, cellSize / 10);
        g.fillRect(graph.getStart().x * cellSize + margin, graph.getStart().y * cellSize + margin,
                cellSize - 2 * margin, cellSize - 2 * margin);
    }

    /**
     * Draws the current animation frame as a magenta circle.
     *
     * @param g        The Graphics object to draw on.
     * @param cellSize The size of each cell in the grid.
     */
    private void drawAnimation(Graphics g, int cellSize) {
        Node current = path.get(animationIndex);
        g.setColor(Color.MAGENTA);
        int margin = Math.max(2, cellSize / 10);
        g.fillOval(current.x * cellSize + margin, current.y * cellSize + margin, cellSize - 2 * margin,
                cellSize - 2 * margin);
    }

    /**
     * Animates the path by incrementing the animation index and repainting the panel.
     * This method should be called periodically to update the animation.
     */
    public void animate() {
        if (animationIndex < path.size() && !isPaused) {
            animationIndex++;
            repaint(); // Trigger paintComponent to redraw the panel
        }
    }

    /**
     * Steps backward in the animation by decrementing the animation index.
     * This allows the user to go back to the previous frame in the animation.
     */
    public void stepBackward() {
        if (animationIndex > 0) {
            animationIndex--;
            repaint();
        }
    }

    /**
     * Steps forward in the animation by incrementing the animation index.
     * This allows the user to go to the next frame in the animation.
     */
    public void stepForward() {
        if (animationIndex < path.size()) {
            animationIndex++;
            repaint();
        }
    }

    /**
     * Resets the animation to the beginning.
     * This method sets the animation index to 0 and repaints the panel.
     */
    public void resetAnimation() {
        animationIndex = 0;
        isPaused = true; // Ensure that the animation isn't paused after reset
        repaint();
    }

    /**
     * Toggles the pause state of the animation.
     * If paused, it resumes the animation; if running, it pauses it.
     */
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

    /**
     * Checks if the animation is currently paused.
     *
     * @return true if the animation is paused, false otherwise.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Sets the path to animate through the grid.
     * This method updates the path and resets the animation index.
     *
     * @param path The new path to animate.
     */
    public void setPath(List<Node> path) {
        this.path = path;
        this.animationIndex = 0; // Reset animation index
        repaint(); // Refresh the grid visualization
    }

    /**
     * Sets the graph for this GridPanel.
     * This method updates the graph and adjusts the preferred size of the panel.
     *
     * @param graph The new graph to visualize.
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
        setPreferredSize(new Dimension(graph.getWidth() * cellSize, graph.getHeight() * cellSize));
        repaint();
    }

    /**
     * Gets the size of each cell in the grid.
     * This method calculates the cell size based on the grid dimensions and ensures it is not smaller than a minimum size.
     *
     * @return The size of each cell in pixels.
     */
    public int getCellSize() {
        int cellWidth = Math.max(MIN_CELL_SIZE, PREFERRED_DRAW_SIZE / graph.getWidth());
        int cellHeight = Math.max(MIN_CELL_SIZE, PREFERRED_DRAW_SIZE / graph.getHeight());
        return Math.min(cellWidth, cellHeight);
    }

    /**
     * Gets the preferred size of the panel based on the graph dimensions and cell size.
     * 
     * @return The preferred size of the panel as a Dimension object.
     */
    @Override
    public Dimension getPreferredSize() {
        int cellSize = getCellSize();
        return new Dimension(graph.getWidth() * cellSize, graph.getHeight() * cellSize);
    }

    /**
     * Sets whether the next click will set the start node or the goal node.
     * 
     * @param settingStart true to set the start node, false to set the goal node.
     */
    public void setSettingStart(boolean settingStart) {
        this.settingStart = settingStart;
    }

}
