import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class representing the Creeper enemy in the game.
 * Creeper enemies can throw fireballs at the player.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class Creeper extends Enemy
{

    private int fireballCooldown = 300; 
    private int fireballCounter = Greenfoot.getRandomNumber(fireballCooldown); // Randomize the counter
    private int fireballDurationCounter = 0;
    /**
     * Constructor for Creeper class.
     * 
     * @param x          The X-coordinate of the room in which the creeper is located.
     * @param y          The Y-coordinate of the room in which the creeper is located.
     * @param worldLevel The current level of the world.
     */  
    public Creeper(int x, int y, int worldLevel) {
        super(x, y);
        setAnimationImages(worldLevel);
        setEnemyImage();
            }

    /**
     * Animate and throw fireballs. 
     */
    public void act() {
        super.act();  
        throwFireball();
    }

    /**
     * Throws fireballs angled towards the players x and y. 
     */
    private void throwFireball() {
        if (fireballCounter <= 0) {
            Player player = getWorld().getObjects(Player.class).get(0);

            if (player != null) {
                Fireball fireball = new Fireball();
                getWorld().addObject(fireball, getX(), getY());

                // Calculate the angle towards the player
                int angle = (int) Math.toDegrees(Math.atan2(player.getY() - getY(), player.getX() - getX()));

                // Set the fireball rotation
                fireball.setRotation(angle);

                // Reset the counter to the cooldown value
                fireballCounter = fireballCooldown;
            }
        } else {
            // Decrease the counter until it reaches 0
            fireballCounter--;
        }
    }
    /**
     * Sets the animation images for the Creeper based on the world level.
     * 
     * @param worldLevel The current level of the world.
     */
    private void setAnimationImages(int worldLevel) {
        int numFrames = 4;  

        GreenfootImage[] creeperImages = new GreenfootImage[numFrames];

        int lvlIndex = worldLevel;
        if(worldLevel > 3){
            lvlIndex = Greenfoot.getRandomNumber(3) + 1;
        }
        for (int i = 0; i < numFrames; i++) {
            creeperImages[i] = new GreenfootImage("creeper" + lvlIndex + "0" +  "run" + i + ".png");
            creeperImages[i].scale(45, 50);
        }

        setAnimationImages(creeperImages);
    }

    private void setEnemyImage() {
        setImage(getAnimationImages()[0]);  // Set the default image
    }
}
