import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Creates an individual room of given size and x and y coordinates in the world. To keep track of the world map. 
 * 
 * @author Jessica Biro
 * @version January 2024
 */
public class Room extends Actor
{
    public int x1, y1, x2, y2; // top left xy, bottom right xy
    public int[][] roomMap; //the map of the room for enemy pathfinding 

    private int roomNum = -1; //current room number is -1 until changed
    private boolean hasEnemies = false; 
    /**
     * Creates an instance of a room of given width and height. Specific x and y coordinates in the world map.
     * @param x         The x coordinate in cells in the world map
     * @param y         The y coordinate in cells in the world map
     * @param width     The width of the room in cells
     * @param height    The height of the room in cells 
     */
    Room(int x, int y, int width, int height) {
        this.x1 = x;
        this.y1 = y;
        this.x2 = x + width;
        this.y2 = y + height;
    }

    /**
     * Setter method to set the room number of the current room.
     * @param rn    The room number of the room
     */
    public void setRoomNum(int rn){
        roomNum = rn;
    }
    
    /**
     * Setter method to set if the current room has enemies or not.
     * @param has   Whether or not the room has enemies. 
     */
    public void setHasEnemies(boolean has){
        hasEnemies = has;
    }
    
    /**
     * Getter method to see if the room currently has enemies. 
     * @return boolean  Whether or not the room has enemies. 
     */
    public boolean hasEnemies(){
        return hasEnemies;
    }
    
    /**
     * Getter method to get the current rooms number.
     * @return int  The room's number. 
     */
    public int getRoomNum(){
        return roomNum;
    }
    
    /**
     * Getter method to get the center X of the room in cells.
     * @return int  The x coordinate of the center of the room in cells 
     */
    public int getCenterX() {
        return (x1 + x2) / 2;
    }

    /**
     * Getter method to get the center Y of the room in cells.
     * @return int  The y coordinate of the center of the room in cells 
     */
    public int getCenterY() {
        return (y1 + y2) / 2;
    }

    /**
     * Method that checks whether or not the current room is overlapping with another room. 
     * @param other     The room that the current room is checking overlap with.
     * @return boolean  Whether or not the two rooms are overlapping. 
     */
    public boolean intersects(Room other) {
        return x1 <= other.x2 && x2 >= other.x1 && y1 <= other.y2 && y2 >= other.y1;
    }

    /**
     * Setter method to set the room map of the current room. 
     * @param map   The map of the room. 
     */
    public void setMap(int[][] map) {
        roomMap = map;
    }
}
