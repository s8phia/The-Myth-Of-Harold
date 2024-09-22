import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Goo class represents a sticky substance left behind by the Slime enemy.
 * It extends the Attack class and includes a specific image for the goo.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class Goo extends Attack
{
    GreenfootImage gooImage;
    /**
     * Creates a new instance of "goo" that is placed on top of cells. 
     */
    public Goo() {
        gooImage = new GreenfootImage("slime2.png");
        gooImage.scale(25,25);
        setImage(gooImage);
    }
}
