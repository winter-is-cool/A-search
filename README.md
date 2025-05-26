# A* Search Visualizer

This project was originally created for a school presentation to demonstrate how A* search and related algorithms work, especially in non-Euclidean spaces (like grids with wrap-around and teleportation). The application lets you dynamically measure and visualize differences between algorithms, making it easy to show their behavior live.

I'm releasing it because I think it's a fun little project that I probably overcomplicated—but I also think it looks neat and could be useful or interesting to others!

## Features

- **Algorithm Visualization:** Compare A*, Greedy Best-First Search, and Dijkstra's Algorithm.
- **Customizable Grid:** Adjust grid size, blocked cell percentage, and teleportation node percentage.
- **Manual Start/End Placement:** Toggle between setting the start and end node, then click on the grid to place them.
- **Teleportation and Wrap-Around:** Enable teleportation nodes and wrap-around edges for non-Euclidean effects.
- **Seed System:** Composite seed encodes all grid parameters and start/end positions, so you can reproduce and share any scenario.
- **Performance Metrics:** See pathfinding time (in milliseconds) and the number of nodes searched.
- **Copyable Seed:** Easily copy the current seed for sharing or later use.

## Usage

1. **Requirements:** Java 8 or newer (JDK).
2. **Run the Application:**  
   ```
   java -jar ASearchVisualizer.jar
   ```
3. **Controls:**
   - Adjust grid size, blocked %, and teleport % using the sliders and spinners.
   - Use the toggle button to switch between setting the start and end node, then click on the grid to place them.
   - Use the "Generate Random Grid" or "Generate from Seed" buttons to create new scenarios.
   - Copy and share the seed string to reproduce any grid configuration.

## Seed Format

The application uses a composite seed string to encode the entire grid configuration, including start and end positions.  
The format is:

```
width-height-blockedPercent-teleportPercent-seed-startX-startY-endX-endY
```

**Example:**
```
20-20-0.2-0.05-1234567890-0-0-19-19
```
- `width` and `height`: grid dimensions
- `blockedPercent`: fraction of blocked cells (e.g., 0.2 for 20%)
- `teleportPercent`: fraction of teleportation nodes (e.g., 0.05 for 5%)
- `seed`: random seed for grid generation
- `startX`, `startY`: coordinates of the start node
- `endX`, `endY`: coordinates of the goal node

You can copy, share, or manually edit this seed to reproduce or customize any scenario.

## Purpose

This application was developed for a school presentation to help visualize and understand the behavior of pathfinding algorithms in both standard and non-Euclidean grid environments.  
I’m sharing it in case others find it useful, interesting, or just want to play with pathfinding visualizations.

## Credits

All art and graphics used in this project were created by me.

## License

This project is for educational and demonstration use.

---