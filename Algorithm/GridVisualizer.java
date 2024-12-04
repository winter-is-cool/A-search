package Algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GridVisualizer {
    private Graph graph;
    private List<Node> path;
    private JFrame frame;
    private GridPanel gridPanel;

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

        // In your GUI setup code
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> {
            gridPanel.togglePause(); // This toggles between pausing and resuming the animation
        });

        // Step Forward Button
        JButton stepForwardButton = new JButton("Step Forward");
        stepForwardButton.addActionListener(e -> gridPanel.stepForward());

        // Step Backward Button
        JButton stepBackwardButton = new JButton("Step Backward");
        stepBackwardButton.addActionListener(e -> gridPanel.stepBackward());

        // Add buttons to control panel
        panel.add(restartButton);
        panel.add(pauseButton);
        panel.add(stepForwardButton);
        panel.add(stepBackwardButton);

        return panel;
    }
}
