import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class representing the Slime enemy in the game.
 * Slime enemies can leave goo on the ground as they move.
 * Goo has a certain duration before it disappears.
 * Slime enemies attack the player when in close proximity.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class Slime extends Enemy
{

    private int hp = 1;
    private static final int GOO_INTERVAL = 1000; // Adjust the interval as needed
    private static final int GOO_DURATION = 300; // Adjust the duration (act cycles) as needed
    private int gooCounter = Greenfoot.getRandomNumber(GOO_INTERVAL); // Initial random counter
    private Goo gooInstance; // Track the Goo instance
    private int gooDurationCounter = 0; // Counter for tracking Goo duration
    /**
     * Constructor for Slime class.
     * 
     * @param x The X-coordinate of the room in which the slime is located.
     * @param y The Y-coordinate of the room in which the slime is located.
     * @param worldLevel The current level of the world.
     */
    public Slime(int x, int y, int worldLevel) {
        super(x, y);
        setAnimationImages(worldLevel);
        setEnemyImage();
    }

    /**
     * Constantly leave clue and only attack the player every 50 acts if intersecting.
     * Once dead, remove all the good left on the ground. 
     */
    public void act() {
        super.act();  
        leaveGoo();

        if(Greenfoot.getRandomNumber(50) == 0){
            attackPlayer();
        }
        if (hp <= 0) {
            removeGooOnDeath();
        }
    }

    /**
     * Sets the animation images for the Slime based on the world level.
     * 
     * @param worldLevel The current level of the world.
     */
    private void setAnimationImages(int worldLevel) {
        int numFrames = 4;  

        GreenfootImage[] slimerImages = new GreenfootImage[numFrames];

        int lvlIndex = worldLevel;
        if(worldLevel > 3){
            lvlIndex = Greenfoot.getRandomNumber(3) + 1;
        }
        for (int i = 0; i < numFrames; i++) {
            slimerImages[i] = new GreenfootImage("slime" + lvlIndex + "0" + i + ".png");
            slimerImages[i].scale(35, 40);

        }

        setAnimationImages(slimerImages);
    }

    private void setEnemyImage() {
        setImage(getAnimationImages()[0]);  // Set the default image
    }

    private void leaveGoo() {
        gooCounter--;

        if (gooCounter <= 0) {
            if (gooInstance != null) {
                getWorld().removeObject(gooInstance); // Remove existing Goo
            }

            gooInstance = new Goo();
            getWorld().addObject(gooInstance, getX(), getY()); // Add new Goo
            gooCounter = Greenfoot.getRandomNumber(GOO_INTERVAL); // Randomize the counter
            gooDurationCounter = 0; // Reset the Goo duration counter
        }

        // Check if it's time to remove the Goo
        if (gooInstance != null) {
            gooDurationCounter++;
            if (gooDurationCounter >= GOO_DURATION) {
                getWorld().removeObject(gooInstance); // Remove Goo after a certain duration
                gooInstance = null; // Reset Goo instance
            }
        }
    }

    private void removeGooOnDeath() {
        if (gooInstance != null) {
            getWorld().removeObject(gooInstance); // Remove Goo when slime dies
            gooInstance = null; // Reset Goo instance
        }
    }

    private void attackPlayer() {
        Player player = (Player) getOneIntersectingObject(Player.class);
        if (player != null) {
            // Player is intersecting with the slime
            player.getHit(1); // Call the getHit method to decrement player's HP
        }
    }
}
