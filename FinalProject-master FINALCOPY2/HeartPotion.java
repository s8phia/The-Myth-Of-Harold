import greenfoot.*;
/**
 * HeartPotion class represents a health potion in the game which can spawn when enemies die.
 * It extends the Asset class and includes functionality for player interaction to increase health.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class HeartPotion extends Asset
{
    private GreenfootImage potionImage;

    /**
     * Creates a new instance of a heart potion that can be picked up by the player. 
     */
    public HeartPotion() {
        potionImage = new GreenfootImage("flask_red.png");
        potionImage.scale(25,25);
        setImage(potionImage);
    }

    /**
     * Check for player interactions. 
     */
    public void act() {
        Player player = (Player) getOneIntersectingObject(Player.class);
        if (player != null) {
            player.incrementHp();
            // Remove the coin from the world
            getWorld().removeObject(this);
        }
    }

}
