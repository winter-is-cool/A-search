package Algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GridVisualizer {
    private Graph graph;
    private List<Node> path;
    private JFrame frame;
    private GridPanel gridPanel;
    private boolean teleportationEnabled = true; // Toggle for teleportation nodes

    public GridVisualizer(Graph graph, List<Node> path) {
        this.graph = graph;
        this.path = path;
    }

    public void visualize() {
        frame = new JFrame("A* Pathfinding Visualization");
        gridPanel = new GridPanel(graph, path);

        // Set up the main panel and frame
        JPanel controlPanel = createControlPanel();
        frame.setLayout(new BorderLayout());
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Set up the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }

    // Create control panel with buttons
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();

        // Restart Button
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> gridPanel.resetAnimation());

        // Pause Button
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> gridPanel.togglePause());

        // Step Forward Button
        JButton stepForwardButton = new JButton("Step Forward");
        stepForwardButton.addActionListener(e -> gridPanel.stepForward());

        // Step Backward Button
        JButton stepBackwardButton = new JButton("Step Backward");
        stepBackwardButton.addActionListener(e -> gridPanel.stepBackward());

        // Wrap-Around Toggle Button
        JButton wrapAroundButton = new JButton("Toggle Wrap-Around");
        wrapAroundButton.addActionListener(e -> {
            graph.setWrapAroundEnabled(!graph.isWrapAroundEnabled());
            System.out.println("Wrap-Around is now " + (graph.isWrapAroundEnabled() ? "enabled" : "disabled"));

            // Recalculate the path
            AStar aStar = new AStar(graph, graph.getStart(), graph.getGoal());
            path = aStar.search();

            // Update the grid panel with the new path
            gridPanel.setPath(path);
            gridPanel.repaint();

            // Notify if no path exists
            if (path == null) {
                JOptionPane.showMessageDialog(frame, "No path exists with the current configuration.");
            }
        });

        // Teleportation Toggle Button
        JButton teleportationButton = new JButton("Toggle Teleportation");
        teleportationButton.addActionListener(e -> {
            teleportationEnabled = !teleportationEnabled;
            System.out.println("Teleportation is now " + (teleportationEnabled ? "enabled" : "disabled"));

            // Update teleportation nodes to behave as blocked nodes if disabled
            if (!teleportationEnabled) {
                for (Node teleportNode : graph.getTeleportationNodes()) {
                    graph.blockNode(teleportNode.x, teleportNode.y);
                }
            } else {
                // Unblock teleportation nodes when re-enabled
                for (Node teleportNode : graph.getTeleportationNodes()) {
                    graph.unblockNode(teleportNode.x, teleportNode.y);
                }
            }

            // Recalculate the path
            AStar aStar = new AStar(graph, graph.getStart(), graph.getGoal());
            path = aStar.search();

            // Update the grid panel with the new path
            gridPanel.setPath(path);
            gridPanel.repaint();

            // Notify if no path exists
            if (path == null) {
                JOptionPane.showMessageDialog(frame, "No path exists with the current configuration.");
            }
        });

        // Add buttons to control panel
        panel.add(restartButton);
        panel.add(pauseButton);
        panel.add(stepForwardButton);
        panel.add(stepBackwardButton);
        panel.add(wrapAroundButton);
        panel.add(teleportationButton);

        return panel;
    }
}
