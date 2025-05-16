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

    // ...existing code...

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();

        // --- Randomization Controls ---
        JLabel blockedLabel = new JLabel("Blocked %:");
        JSlider blockedSlider = new JSlider(0, 80, 20); // 0-80%, default 20%
        blockedSlider.setMajorTickSpacing(20);
        blockedSlider.setMinorTickSpacing(5);
        blockedSlider.setPaintTicks(true);
        blockedSlider.setPaintLabels(true);

        JLabel teleportLabel = new JLabel("Teleport %:");
        JSlider teleportSlider = new JSlider(0, 30, 5); // 0-30%, default 5%
        teleportSlider.setMajorTickSpacing(10);
        teleportSlider.setMinorTickSpacing(2);
        teleportSlider.setPaintTicks(true);
        teleportSlider.setPaintLabels(true);

        JLabel widthLabel = new JLabel("Width:");
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(graph.getWidth(), 5, 50, 1));
        JLabel heightLabel = new JLabel("Height:");
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(graph.getHeight(), 5, 50, 1));

        JButton randomButton = new JButton("Generate Random Grid");
        randomButton.addActionListener(e -> {
            int width = (int) widthSpinner.getValue();
            int height = (int) heightSpinner.getValue();
            double blockedPercent = blockedSlider.getValue() / 100.0;
            double teleportPercent = teleportSlider.getValue() / 100.0;

            // Recreate the graph with new size if changed
            if (width != graph.getWidth() || height != graph.getHeight()) {
                graph = new Graph(width, height);
                gridPanel.setGraph(graph); // use setter
            }

            graph.generateRandomGrid(width, height, blockedPercent, teleportPercent);

            // Recalculate the path
            AStar aStar = new AStar(graph, graph.getStart(), graph.getGoal());
            path = aStar.search();

            gridPanel.setPath(path);
            gridPanel.repaint();

            // Resize the panel if grid size changed
            gridPanel.setPreferredSize(new Dimension(width * gridPanel.getCellSize(), height * gridPanel.getCellSize()));
            frame.pack();

            if (path == null) {
                JOptionPane.showMessageDialog(frame, "No path exists with the current configuration.");
            }
        });

        // --- Existing Controls ---
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> gridPanel.resetAnimation());

        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> gridPanel.togglePause());

        JButton stepForwardButton = new JButton("Step Forward");
        stepForwardButton.addActionListener(e -> gridPanel.stepForward());

        JButton stepBackwardButton = new JButton("Step Backward");
        stepBackwardButton.addActionListener(e -> gridPanel.stepBackward());

        JButton wrapAroundButton = new JButton("Toggle Wrap-Around");
        wrapAroundButton.addActionListener(e -> {
            graph.setWrapAroundEnabled(!graph.isWrapAroundEnabled());
            System.out.println("Wrap-Around is now " + (graph.isWrapAroundEnabled() ? "enabled" : "disabled"));

            AStar aStar = new AStar(graph, graph.getStart(), graph.getGoal());
            path = aStar.search();
            gridPanel.setPath(path);
            gridPanel.repaint();

            if (path == null) {
                JOptionPane.showMessageDialog(frame, "No path exists with the current configuration.");
            }
        });

        JButton teleportationButton = new JButton("Toggle Teleportation");
        teleportationButton.addActionListener(e -> {
            teleportationEnabled = !teleportationEnabled;
            System.out.println("Teleportation is now " + (teleportationEnabled ? "enabled" : "disabled"));

            if (!teleportationEnabled) {
                for (Node teleportNode : graph.getTeleportationNodes()) {
                    graph.blockNode(teleportNode.x, teleportNode.y);
                }
            } else {
                for (Node teleportNode : graph.getTeleportationNodes()) {
                    graph.unblockNode(teleportNode.x, teleportNode.y);
                }
            }

            AStar aStar = new AStar(graph, graph.getStart(), graph.getGoal());
            path = aStar.search();
            gridPanel.setPath(path);
            gridPanel.repaint();

            if (path == null) {
                JOptionPane.showMessageDialog(frame, "No path exists with the current configuration.");
            }
        });

        // --- Add controls to panel ---
        panel.add(blockedLabel);
        panel.add(blockedSlider);
        panel.add(teleportLabel);
        panel.add(teleportSlider);
        panel.add(widthLabel);
        panel.add(widthSpinner);
        panel.add(heightLabel);
        panel.add(heightSpinner);
        panel.add(randomButton);

        panel.add(restartButton);
        panel.add(pauseButton);
        panel.add(stepForwardButton);
        panel.add(stepBackwardButton);
        panel.add(wrapAroundButton);
        panel.add(teleportationButton);

        return panel;
    }
}
