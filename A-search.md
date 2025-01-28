# A* Search in Non-Euclidean Pathfinding Problems

my algorithm is at https://github.com/winter-is-cool/A-search

## Overview
A* search is a widely used and powerful graph search algorithm. It combines aspects of **Dijkstra's Algorithm** (which finds the shortest path by exploring all possible paths) and **Greedy Best First Search** (which uses heuristics to prioritize paths that seem closer to the goal). In this paper, we explore the application of A* in non-Euclidean pathfinding problems, focusing on graphs with **teleportation nodes** and **wrap-around edges**.

Non-Euclidean graphs introduce unique challenges, such as irregular connections between nodes and unconventional spatial arrangements. However, A* is highly adaptable and works effectively in these environments without requiring modifications to the core algorithm. The key to its success lies in appropriately defining the graph representation and heuristics to ensure efficient pathfinding.

## What is A* Searching For?
A* is designed to find the shortest path between two points on a graph. The graph consists of:
- **Nodes**: Locations or points in the search space.
- **Edges**: Connections between nodes, each with an associated cost (or weight).

For non-Euclidean graphs, nodes may be linked through:
- **Teleportation Nodes**: Instant transitions between specific, non-adjacent nodes.
- **Wrap-Around Edges**: Edges that connect nodes at the boundaries of a graph (e.g., a toroidal grid where moving off the right edge leads to the left edge).

The algorithm naturally accommodates these irregularities as long as they are correctly represented in the graph structure.

---

## How Does A* Work?
A* operates as follows:

1. **Define the Search Space**:
   - Provide A* with the graph representation, which includes the non-Euclidean features (e.g., teleportation and wrap-around edges).

2. **Start Exploring**:
   - A* maintains two sets:
     - **Open Set**: Nodes that need to be evaluated.
     - **Closed Set**: Nodes that have already been evaluated.
   - Begin by adding the starting node to the open set.

3. **Prioritize Nodes**:
   - A* selects the node with the lowest total cost $f(n)$:
     $$
     f(n) = g(n) + h(n)
     $$
     - $g(n)$: The actual cost from the start node to $n$.
     - $h(n)$: The heuristic estimate from $n$ to the destination.

4. **Expand the Path**:
   - Examine neighbors of the selected node.
   - Update their scores and add them to the open set if they haven't been evaluated yet or if a shorter path to them is found.

5. **Handle Non-Euclidean Features Naturally**:
   - Teleportation nodes and wrap-around edges do not require changes to A*; they simply need to be included in the graph's adjacency list with the correct edge costs.

6. **Repeat Until the Goal is Found**:
   - Continue exploring nodes with the lowest $f(n)$ until reaching the destination or exhausting the search space.

7. **Reconstruct the Path**:
   - Once the goal is reached, backtrack using the stored parent relationships for each node to construct the final path.

---

## Heuristics in Non-Euclidean Graphs
Heuristics guide A* by estimating the remaining cost to the destination from a given node. The choice of heuristic significantly impacts the algorithm’s efficiency.

### Common Heuristics
1. **Manhattan Distance** (for grid-based systems with only vertical and horizontal movement):
   $$
   h(n) = |x_{\text{current}} - x_{\text{goal}}| + |y_{\text{current}} - y_{\text{goal}}|
   $$

2. **Euclidean Distance** (for systems with diagonal movement):
   $$
   h(n) = \sqrt{(x_{\text{current}} - x_{\text{goal}})^2 + (y_{\text{current}} - y_{\text{goal}})^2}
   $$

3. **Octile Distance** (for systems with diagonal and straight movement but different costs):
   $$
   h(n) = \min(|dx|, |dy|) \cdot \text{diagonal\_cost} + |dx - dy| \cdot \text{straight\_cost}
   $$

### Adapting Heuristics for Non-Euclidean Features
In non-Euclidean graphs, standard heuristics can still be applied without modification, as long as they account for:
- **Teleportation nodes**, which can be treated as additional edges with zero or predefined costs.
- **Wrap-around edges**, where distances are computed considering both direct and wrap-around paths, selecting the shortest.

---

## Performance Metrics
### Path Optimality
A* guarantees the shortest path if:
- The heuristic is **admissible** (never overestimates the cost).
- The heuristic is **consistent** (satisfies the triangle inequality).

### Search Efficiency
Search efficiency can be measured by:
- The number of nodes scanned (added to the closed set).
- The size of the open set at each step.

In non-Euclidean graphs, A* still performs optimally as long as the graph is correctly represented.

---

## Experimental Setup
To evaluate A*'s performance, we will:
1. Create test graphs with:
   - Teleportation nodes (e.g., connecting distant points).
   - Wrap-around edges (e.g., toroidal grids).

2. Use different heuristic functions:
   - Standard heuristics (Manhattan, Euclidean).
   - Modified heuristics tailored for non-Euclidean features.

3. Measure:
   - The length of the path found.
   - The number of nodes scanned.
   - Computation time.

---

## Preliminary Results
1. **Teleportation Nodes**:
   - A* performed optimally without requiring modifications, provided teleportation nodes were treated as standard edges.

2. **Wrap-Around Edges**:
   - Standard heuristics still functioned effectively when wrap-around connections were naturally included in the graph representation.

---

## Conclusion
A* is highly effective in non-Euclidean pathfinding problems without requiring changes to its algorithm. The key lies in defining the graph representation properly to accommodate teleportation nodes and wrap-around edges. While standard heuristics remain useful, slight adjustments in distance calculations can further enhance efficiency. Overall, A* seamlessly adapts to these challenges, maintaining its optimality and efficiency in complex, irregular search spaces.

---

## Resources
- [A* Algorithm on GeeksforGeeks](https://www.geeksforgeeks.org/a-search-algorithm/)
- [Pathfinding at Red Blob Games](https://www.redblobgames.com/pathfinding/a-star/introduction.html)
- [Amit’s A* Pages](https://www.redblobgames.com/pathfinding/a-star/introduction.html)
