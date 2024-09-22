import java.util.*;
import greenfoot.*;
/**
 * Pathfinder. 
 * 
 * Credits: <br>
 *     - Pathfinding logic: https://en.wikipedia.org/wiki/A*_search_algorithm#
 * 
 * @author Jessica Biro
 * @version January 2024
 */
public class PathFinder  
{
    // instance variables - replace the example below with your own
    private int x;
    List<Node> nodes = new ArrayList<Node>();
    public int[][] map;
    private static final int START_ID = 100;
    private int currID = START_ID;
    private PriorityQueue<Node> minHeap;
    List<Node> path;

    /**
     * Constructor for a pathfinding given a specific map.
     * @param map   The map base for the pathfinder. 
     */
    public PathFinder(int[][] map)
    {
        this.map = map;
        
        // Convert the map to Nodes with Edges
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                if(map[i][j] >= DungeonWorld.FLOOR) {
                    Node node = getNode(j, i);
                    if(j > 0 && map[i][j-1] >= DungeonWorld.FLOOR) { // LEFT
                        node.addEdge(getNode(j-1, i), 1);
                    } 
                    if(j < map[0].length-1 && map[i][j+1] >= DungeonWorld.FLOOR) { // RIGHT
                        node.addEdge(getNode(j+1, i), 1);
                    }
                    if(i > 0 && map[i-1][j] >= DungeonWorld.FLOOR) { // UP
                        node.addEdge(getNode(j, i-1), 1);
                    }
                    if(i < map.length-1 && map[i+1][j] >= DungeonWorld.FLOOR) { // DOWN
                        node.addEdge(getNode(j, i+1), 1);
                    }
                }
            }
        }
    }

    /**
     * Returns the node with the locations at i and j on the map.
     * @return Node  The node in the map.
     * @param i      The integer location on the map
     * @param j      The integer location on the map 
     */
    public Node getNode(int i, int j) {
        Node node = null;
        if(map[j][i] >= START_ID) {
            for(Node n : nodes) {
                if(n.id == map[j][i]) {
                    return n;
                }
            }
        } else {
            node = new Node(currID, Integer.MAX_VALUE, null, i, j);
            map[j][i] = currID; // mark this location as loaded
            nodes.add(node); // add a node if not already in the map 
            currID++;
        }
        return node;
    }

    /**
     * Helper class, compares the distance between two nodes. 
     */
    class NodeComparator implements Comparator<Node>{
        public int compare(Node n1, Node n2) {
            if (n1.dist < n2.dist)
                return 1;
            else if (n1.dist > n2.dist)
                return -1;
            return 0; //distance is equal 
        }
    }

    /**
     * Gets the node at the inputed x and y coordinates on the map.
     * @param x         The y coordinate of the node. 
     * @param y         The x coordinate of the node. 
     * @return Node     The node at the given position.
     */
    public Node getNodeAtPosition(int x, int y){
        for(Node n : nodes) {
            if(n.x == x && n.y == y) {
                return n;
            }
        }
        return null; //if nothing was found return null 
    }

    /**
     * Returns the updates map.
     * 
     * @return int[][]  The updated map of the pathfinder. 
     */
    public int[][] getUpdatedMap() {
        return map;
    }

    // startX, startY, endX, endY are room coordinates
    /**
     * Calculates the path between nodes from the starting x and y to the ending x and y in the map. 
     * @param map       The map of nodes
     * @param startX    The starting x coordinate in the map 
     * @param startY    The starting y coordinate in the map 
     * @param endX      The ending x coordinate in the map 
     * @param endY      The ending y coordinate in the map 
     */
    public List<Node> calculatePath(int[][] map, int startX, int startY, int endX, int endY) {
        path = new ArrayList<>();
        Node source = getNodeAtPosition(startX, startY);
        Node dest = getNodeAtPosition(endX, endY);
        if (source == null || dest == null || source == dest) {
            return null;
        }

        for (Node n : nodes) {
            n.dist = Integer.MAX_VALUE;
            n.prev = null;
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.fScore));
        Set<Node> closedSet = new HashSet<>();
        source.dist = 0;
        source.fScore = heuristic(startX, startY, endX, endY);
        openSet.add(source);

        int iterations = 0;
        int maxIterations = 400; // Adjust as needed
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current == dest) {
                // Reconstruct the path and return it
                Node temp = dest;
                while (temp.prev != null) {
                    path.add(temp);
                    temp = temp.prev;
                }
                Collections.reverse(path);
                return path;
            }
            closedSet.add(current);
            for (Edge edge : current.getEdges()) {
                Node neighbor = edge.toNode;
                if (closedSet.contains(neighbor)) {
                    continue; // Ignore the neighbor which is already evaluated.
                }
                int tentativeGScore = current.dist + edge.weight;
                if (tentativeGScore < neighbor.dist) {
                    neighbor.prev = current;
                    neighbor.dist = tentativeGScore;
                    neighbor.fScore = neighbor.dist + heuristic(neighbor.x, neighbor.y, endX, endY);
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
            iterations++;
            if (iterations >= maxIterations) {
                // Reached maximum iterations for this batch, return null to indicate that the computation is not complete
                return null;
            }
        }
        // If the loop completes without finding the destination, return an empty path
        return path;
    }

    private int heuristic(int x1, int y1, int x2, int y2) {
        // Using Manhattan distance heuristic
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

}
