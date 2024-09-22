import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Hitboxes to determine when a player had arrived at a certain location. 
 * 
 * @author Hilary Ho
 * @version January 2024
 */
public class Hitbox extends Actor
{
    private GreenfootImage blank;
    private int hbNum; 
    /**
     * Creates a new instance of a hitbox. 
     * @param hbNum     The ID number of the hitbox. 
     */
    public Hitbox(int hbNum){
        this.hbNum = hbNum;
        blank = new GreenfootImage(50, 50);
        //blank.setColor(Color.BLACK);
        //blank.fill();
        setImage(blank);
    }
    
    /**
     * Getter method that returns the ID number of the current hitbox. 
     * @return int      The ID of the hitbox. 
     */
    public int getHbNum(){
        return hbNum; 
    }
}
