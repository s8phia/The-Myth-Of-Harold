import java.util.*;
/**
 * Helps with pathfinding class. 
 * 
 * @author Jessica Biro 
 * @version January 2024
 */
public class Node  
{
    public int id, dist, x, y, fScore;
    public List<Edge> adjList = new ArrayList<Edge>();
    public Node prev;
    public boolean visited;

    /**
     * Constructor for objects of class Node
     * @pararm id   Unique ID to compare nodes. 
     * @param dist  The current distance from the start to this instance of node. 
     * @param prev  The previous node in the path. 
     * @param x     The x coordinate in the map
     * @param y     The y coordinate in the map 
     */
    public Node(int id, int dist, Node prev, int x, int y)
    {
        this.id = id;
        this.dist = dist;
        this.prev = prev;
        this.x = x;
        this.y = y;
        visited = false;
    }

    /**
     * Building and edge between current node and a given node. 
     * 
     * @param n     The node that the edge is being built between 
     * @param w     The cost to travel the edge
     */
    public void addEdge(Node n, int w) {
        adjList.add(new Edge(this, n, w));
    }
    
    /**
     * Returns a list of all the edges from the node. 
     * 
     * @return List<Edge> A list of all the edges from the current node. 
     */
    public List<Edge> getEdges() {
        return adjList;
    }
}
