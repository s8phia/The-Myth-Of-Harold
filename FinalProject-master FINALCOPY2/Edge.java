/**
 * Helps with the pathfinding class. 
 * 
 * @author Jessica Biro
 * @version January 2024
 */
public class Edge  
{
    // instance variables - replace the example below with your own
    public Node fromNode, toNode;
    public int weight;

    /**
     * Constructor for an edge
     * @param n1    The starting node
     * @param n2    The ending node
     * @param w     The cost to travel the edge 
     */
    public Edge(Node n1, Node n2, int w)
    {
        fromNode = n1;
        toNode = n2;
        weight = w;
    }
}
