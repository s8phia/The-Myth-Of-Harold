import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The players current statbar. 
 * 
 * @author Hilary Ho
 * @version January 2024
 */
public class PlayerStats extends Actor
{
    private GreenfootImage image;
    private int type;
    /**
     * Creates a new image of a statbar using the hero type. 
     * 
     * @param type      The type of hero to put on the statbar
     */
    public PlayerStats(int type){
        this.type = type; 
        image = new GreenfootImage("s" + type + ".PNG");
        image.scale(450, 450);
        setImage(image);
    }
}
