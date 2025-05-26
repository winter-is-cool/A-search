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

## Purpose

This application was developed for a school presentation to help visualize and understand the behavior of pathfinding algorithms in both standard and non-Euclidean grid environments.  
I’m sharing it in case others find it useful, interesting, or just want to play with pathfinding visualizations.

## Credits

All art and graphics used in this project were created by me.

## License

This project is for educational and demonstration use.

---