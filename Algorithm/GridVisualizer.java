package Algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * GridVisualizer is a GUI class that visualizes the A* pathfinding algorithm on
 * a grid.
 * It allows users to interactively generate grids, set start and goal nodes,
 * and visualize
 * the pathfinding process with various algorithms.
 */
public class GridVisualizer {
    private Graph graph; // The graph representing the grid
    private List<Node> path; // The path found by the algorithm
    private JFrame frame; // Main window for the visualization
    private GridPanel gridPanel; // Panel that displays the grid and path
    private boolean teleportationEnabled = true; // Toggle for teleportation nodes
    private JLabel timeLabel = new JLabel("Time: 0 ms"); // Label to display the time taken for pathfinding
    private JLabel nodesLabel = new JLabel("Nodes searched: 0"); // Label to display the number of nodes searched
    private JComboBox<String> algorithmDropdown; // Dropdown to select the pathfinding algorithm
    private JTextField seedField; // Text field to display and input the seed for random grid generation
    private JSplitPane splitPane; // Split pane to hold the grid panel and control panel
    private JLabel pathLengthLabel = new JLabel("Path Length: 0"); // Label to display the length of the path

    /**
     * Constructor for GridVisualizer.
     *
     * @param graph The graph representing the grid.
     * @param path  The path found by the algorithm.
     */
    public GridVisualizer(Graph graph, List<Node> path) {
        this.graph = graph;
        this.path = path;
    }

    /**
     * Starts the visualization by creating the main window and control panel.
     */
    public void visualize() {
        frame = new JFrame("A* Pathfinding Visualization");
        gridPanel = new GridPanel(graph, path, this);

        JPanel controlPanel = createControlPanel();
        // Add a left border to the control panel for a dividing line
        controlPanel.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY));

        // Use JSplitPane to place grid and panel side by side
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gridPanel, controlPanel);
        splitPane.setDividerSize(4); // Thin divider
        splitPane.setDividerLocation(gridPanel.getPreferredSize().width + 10); // Adjust as needed
        splitPane.setResizeWeight(1.0); // Grid takes more space

        frame.setLayout(new BorderLayout());
        frame.add(splitPane, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }

    /**
     * Recalculates the path using the selected algorithm and updates the display.
     * This method is called when the user changes the algorithm or generates a new
     * grid.
     */
    void recalculateAndDisplayPath() {
        String algorithm = (String) algorithmDropdown.getSelectedItem();
        long startTime = System.nanoTime(); // Use nanoTime for better precision
        AStar aStar = AlgorithmFactory.createAlgorithm(algorithm, graph, graph.getStart(), graph.getGoal());
        path = aStar.search();
        long endTime = System.nanoTime();
        double elapsed = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds with decimals
        int nodesSearched = aStar.getNodesSearched();

        timeLabel.setText(String.format("Time: %.3f ms", elapsed)); // Show 3 decimals
        nodesLabel.setText("Nodes searched: " + nodesSearched);

        if (path != null) {
            pathLengthLabel.setText("Path length: " + path.size());
        } else {
            pathLengthLabel.setText("Path length: 0");
        }

        gridPanel = new GridPanel(graph, path, this);
        splitPane.setLeftComponent(gridPanel); // Replace the old gridPanel in the UI
        splitPane.setDividerLocation(gridPanel.getPreferredSize().width + 10);
        splitPane.revalidate();
        splitPane.repaint();

        if (path == null) {
            JOptionPane.showMessageDialog(frame, "No path exists with the current configuration.");
        }
    }

    /**
     * Updates the seed field with the current state of the grid, including width,
     * height,
     * blocked percentage, teleport percentage, and the current seed.
     * 
     * Composite seed format:
     * width-height-blockedPercent-teleportPercent-seed-startX-startY-endX-endY
     * Example: "10-10-0.2-0.05-123456789-0-0-9-9"
     */
    public void updateSeedFieldWithCurrentState() {
        int width = graph.getWidth();
        int height = graph.getHeight();
        double blockedPercent = 0.2, teleportPercent = 0.05;
        long seed = System.currentTimeMillis();
        try {
            String[] parts = seedField.getText().split("-");
            if (parts.length >= 5) {
                blockedPercent = Double.parseDouble(parts[2]);
                teleportPercent = Double.parseDouble(parts[3]);
                seed = Long.parseLong(parts[4]);
            }
        } catch (Exception ignored) {
        }
        Node start = graph.getStart();
        Node goal = graph.getGoal();
        if (start == null || goal == null)
            return;
        String compositeSeed = width + "-" + height + "-" + blockedPercent + "-" + teleportPercent + "-" + seed
                + "-" + start.x + "-" + start.y + "-" + goal.x + "-" + goal.y; // formatting the seed string
        seedField.setText(compositeSeed);
    }

    /**
     * Creates the control panel with various controls for the grid visualization.
     *
     * @return The JPanel containing the control elements.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Add this line for vertical layout
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Optional: padding

        // --- Randomization Controls ---
        JLabel blockedLabel = new JLabel("Blocked %:");
        JSlider blockedSlider = new JSlider(0, 80, 20);
        blockedSlider.setMajorTickSpacing(20);
        blockedSlider.setMinorTickSpacing(5);
        blockedSlider.setPaintTicks(true);
        blockedSlider.setPaintLabels(true);

        JLabel teleportLabel = new JLabel("Teleport %:");
        JSlider teleportSlider = new JSlider(0, 30, 5);
        teleportSlider.setMajorTickSpacing(10);
        teleportSlider.setMinorTickSpacing(2);
        teleportSlider.setPaintTicks(true);
        teleportSlider.setPaintLabels(true);

        JLabel widthLabel = new JLabel("Width:");
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(graph.getWidth(), 5, 100, 1));
        widthSpinner.setMaximumSize(widthSpinner.getPreferredSize()); // Prevent vertical stretching

        JLabel heightLabel = new JLabel("Height:");
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(graph.getHeight(), 5, 100, 1));
        heightSpinner.setMaximumSize(heightSpinner.getPreferredSize()); // Prevent vertical stretching

        JButton randomButton = new JButton("Generate Random Grid");
        randomButton.addActionListener(e -> {
            int width = (int) widthSpinner.getValue();
            int height = (int) heightSpinner.getValue();
            double blockedPercent = blockedSlider.getValue() / 100.0;
            double teleportPercent = teleportSlider.getValue() / 100.0;
            long seed = System.currentTimeMillis();

            if (width != graph.getWidth() || height != graph.getHeight()) {
                graph = new Graph(width, height);
                gridPanel = new GridPanel(graph, path, this);
                splitPane.setLeftComponent(gridPanel); // Replace the old gridPanel in the UI
                splitPane.setDividerLocation(gridPanel.getPreferredSize().width + 10);
                splitPane.revalidate();
                splitPane.repaint();
            }

            graph.generateRandomGrid(width, height, blockedPercent, teleportPercent, seed);

            // Ensure start and goal are set and not null
            if (graph.getStart() == null) {
                graph.setStart(new Node(0, 0));
            }
            if (graph.getGoal() == null) {
                graph.setGoal(new Node(width - 1, height - 1));
            }

            String compositeSeed = width + "-" + height + "-" + blockedPercent + "-" + teleportPercent + "-" + seed
                    + "-" + graph.getStart().x + "-" + graph.getStart().y
                    + "-" + graph.getGoal().x + "-" + graph.getGoal().y; // formatting the seed string
            seedField.setText(compositeSeed);

            gridPanel
                    .setPreferredSize(new Dimension(width * gridPanel.getCellSize(), height * gridPanel.getCellSize()));
            frame.pack();
            recalculateAndDisplayPath();
        });

        // --- Existing Controls ---
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> gridPanel.resetAnimation());

        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> gridPanel.togglePause());

        JButton stepForwardButton = new JButton(
                "Step Forward");
        stepForwardButton.addActionListener(e -> gridPanel.stepForward());

        JButton stepBackwardButton = new JButton(
                "Step Backward");
        stepBackwardButton.addActionListener(e -> gridPanel.stepBackward());

        JButton wrapAroundButton = new JButton("Toggle Wrap-Around");
        wrapAroundButton.addActionListener(e -> {
            graph.setWrapAroundEnabled(!graph.isWrapAroundEnabled());
            System.out.println("Wrap-Around is now " + (graph.isWrapAroundEnabled() ? "enabled" : "disabled"));
            recalculateAndDisplayPath();
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
            recalculateAndDisplayPath();
        });

        JLabel algorithmLabel = new JLabel("Algorithm:");
        String[] algorithms = { "A* Search", "Greedy Best-First Search", "Dijkstra's Algorithm" };
        algorithmDropdown = new JComboBox<>(algorithms);
        algorithmDropdown.setMaximumSize(algorithmDropdown.getPreferredSize());
        algorithmDropdown.addActionListener(e -> {
            recalculateAndDisplayPath();
        });

        JLabel seedLabel = new JLabel("Seed:");
        seedField = new JTextField(10);
        seedField.setMaximumSize(seedField.getPreferredSize());

        JButton copySeedButton = new JButton("Copy");
        copySeedButton.addActionListener(e -> {
            String seedText = seedField.getText();
            if (!seedText.isEmpty()) {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new java.awt.datatransfer.StringSelection(seedText), null);
            }
        });

        JButton generateSeedButton = new JButton("Generate from Seed");
        generateSeedButton.addActionListener(e -> {
            String seedText = seedField.getText().trim();
            try {
                /**
                 * Composite seed format:
                 * width-height-blocked-teleport-seed-startX-startY-endX-endY
                 * Example: "10-10-0.2-0.05-123456789-0-0-9-9"
                 */
                String[] parts = seedText.split("-");
                if (parts.length != 9)
                    throw new Exception();
                int width = Integer.parseInt(parts[0]);
                int height = Integer.parseInt(parts[1]);
                double blockedPercent = Double.parseDouble(parts[2]);
                double teleportPercent = Double.parseDouble(parts[3]);
                long seed = Long.parseLong(parts[4]);
                int startX = Math.max(0, Math.min(width - 1, Integer.parseInt(parts[5])));
                int startY = Math.max(0, Math.min(height - 1, Integer.parseInt(parts[6])));
                int endX = Math.max(0, Math.min(width - 1, Integer.parseInt(parts[7])));
                int endY = Math.max(0, Math.min(height - 1, Integer.parseInt(parts[8])));

                // Update controls to match the seed
                widthSpinner.setValue(width);
                heightSpinner.setValue(height);
                blockedSlider.setValue((int) (blockedPercent * 100));
                teleportSlider.setValue((int) (teleportPercent * 100));

                graph = new Graph(width, height);
                gridPanel = new GridPanel(graph, path, this);
                splitPane.setLeftComponent(gridPanel); // Replace the old gridPanel in the UI
                splitPane.setDividerLocation(gridPanel.getPreferredSize().width + 10);
                splitPane.revalidate();
                splitPane.repaint();
                graph.generateRandomGrid(width, height, blockedPercent, teleportPercent, seed);

                // Set start and goal, clamped to grid bounds
                graph.setStart(new Node(startX, startY));
                graph.setGoal(new Node(endX, endY));

                gridPanel.setPreferredSize(
                        new Dimension(width * gridPanel.getCellSize(), height * gridPanel.getCellSize()));
                frame.pack();
                recalculateAndDisplayPath();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid seed format. Use: width-height-blocked-teleport-seed-startX-startY-endX-endY");
            }
        });

        JToggleButton setStartToggle = new JToggleButton("Set Start (Blue)");
        setStartToggle.setSelected(true); // Default to setting start

        setStartToggle.addActionListener(e -> {
            gridPanel.setSettingStart(setStartToggle.isSelected());
            setStartToggle.setText(setStartToggle.isSelected() ? "Set Start (Blue)" : "Set End (Goal)");
        });

        // --- Add controls to panel (vertically) ---
        panel.add(timeLabel);
        panel.add(nodesLabel);
        panel.add(pathLengthLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(blockedLabel);
        panel.add(blockedSlider);
        panel.add(Box.createVerticalStrut(10));
        panel.add(teleportLabel);
        panel.add(teleportSlider);
        panel.add(Box.createVerticalStrut(10));
        panel.add(setStartToggle);
        panel.add(Box.createVerticalStrut(10));
        panel.add(widthLabel);
        panel.add(widthSpinner);
        panel.add(heightLabel);
        panel.add(heightSpinner);
        panel.add(Box.createVerticalStrut(10));
        panel.add(seedLabel);
        panel.add(seedField);
        panel.add(copySeedButton);
        panel.add(generateSeedButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(algorithmLabel);
        panel.add(algorithmDropdown);
        panel.add(Box.createVerticalStrut(10));
        panel.add(randomButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(restartButton);
        panel.add(pauseButton);
        panel.add(stepForwardButton);
        panel.add(stepBackwardButton);
        panel.add(wrapAroundButton);
        panel.add(teleportationButton);

        return panel;
    }
}
