import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An image of a miniature map of the currently generated larger map. To help the player guide themselves throughout the world. 
 * 
 * @author Jessica Biro
 * @version January 2024
 */
public class MiniMap extends Actor
{
    private GreenfootImage miniMap;
    private Player p;
    private int[][] map;
    private int cellSize = 2; //how large each cell will be displayed on the minimap 
    
    /**
     * Creates an instance of a mini map that shows the entire world map and the players current coordinates. 
     * 
     * @param map   The map that the mini map is based off of. 
     * @param p     The player from the world. 
     */
    public MiniMap(int[][] map, Player p){
        displayMiniMap(map);
        this.p = p;
        this.map = map;
    }

    /**
     * Constantly updates the player's location and the map image. 
     */
    public void act()
    {
        updatePlayer();
        updateMiniMap();
    }

    /**
     * Creates the mini map image by looping through the larger map and drawing it cell by cell. 
     * @param map   The map of which the mini map is based off of. 
     */
    private void displayMiniMap(int[][] map){
        // minimap
        miniMap = new GreenfootImage(600, 400);
        miniMap.setColor(greenfoot.Color.PINK);
        
        int size = 1;
        //loop through the map and create the image of the map. 
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    if(map[i][j] == DungeonWorld.WALL){
                        miniMap.setColor(greenfoot.Color.WHITE); //walls are in white 
                    } else if(map[i][j] == DungeonWorld.LADDER){
                        miniMap.setColor(greenfoot.Color.RED); //the ladder for the next levels
                        size = 1;
                    } else if(map[i][j] == DungeonWorld.HOLE){
                        miniMap.setColor(greenfoot.Color.YELLOW); //the hole for the boss world 
                        size = 1;
                    }else {
                        miniMap.setColor(greenfoot.Color.PINK); //pink by default
                        size = 1;
                    }
                    miniMap.fillRect(j * cellSize, i * cellSize, cellSize * size, cellSize * size);
                }
            }
        }
        setImage(miniMap);
    }

    /**
     * Updates the players current location on the mini map using the players current location. 
     */
    private void updatePlayer(){
        //update location
        int[] playerCords = DungeonWorld.worldToMap(p.getX(), p.getY()); //change the players x and y into cell x and y. 
        miniMap.setColor(greenfoot.Color.GREEN);
        miniMap.fillRect(playerCords[0] * cellSize, playerCords[1] * cellSize, cellSize, cellSize); //draw the players location in green. 
    }
    /**
     * Updates the mini maps image. 
     */
    private void updateMiniMap(){
        //update the image with the player as a circle
        setImage(miniMap);
    }

    /**
     * Resets the map to the originally made minimap so that the players markings don't remain when the map isn't displayed. 
     */
    public void reset(){
        displayMiniMap(map);
    }
}
