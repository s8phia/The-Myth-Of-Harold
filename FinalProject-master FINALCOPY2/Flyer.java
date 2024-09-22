import greenfoot.*;
/**
 * Class representing the Flyer enemy in the game.
 * Flyer enemies can fly towards the player and attack.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class Flyer extends Enemy {
    private int speed = 4;
    private int minDistance = 50;  // Minimum distance to maintain from other flyers
    private Player player;
    private int attackSpeed = 8;
    private boolean isAttacking = false;

    private int attackDistance = 100;  // Distance at which the flyer will attack the player
    /**
     * Constructor for Flyer class.
     * 
     * @param x          The X-coordinate of the room in which the flyer is located.
     * @param y          The Y-coordinate of the room in which the flyer is located.
     * @param worldLevel The current level of the world.
     * @param player     The player object in the world.
     */
    public Flyer(int x, int y, int worldLevel, Player player) {
        super(x, y);
        this.player = player;
        setAnimationImages(worldLevel);
        setEnemyImage();
    }

    /**
     * If is near the player, attack otherwise fly towards the player. 
     */
    public void act() {
        super.act();
        if (isNearPlayer()) {
            attackPlayer();
        } else {
            speed = 4;
            flyToPlayer();
        }
    }

    /**
     * Sets the animation images for the Flyer based on the world level.
     * 
     * @param worldLevel The current level of the world.
     */
    private void setAnimationImages(int worldLevel) {
        int numFrames = 4;

        GreenfootImage[] flyerImages = new GreenfootImage[numFrames];

        int lvlIndex = worldLevel;
        if (worldLevel > 3) {
            lvlIndex = Greenfoot.getRandomNumber(3) + 1;
        }
        for (int i = 0; i < numFrames; i++) {
            flyerImages[i] = new GreenfootImage("flyer" + lvlIndex + "0" + "run" + i + ".png");
            flyerImages[i].scale(35, 40);
        }

        setAnimationImages(flyerImages);
    }

    private void setEnemyImage() {
        setImage(getAnimationImages()[0]);  // Set the default image
    }

    /**
     * Fly towards the players current x and y coordinates 
     */
    private void flyToPlayer() {
        int xTarget = player.getPlayerX();
        int yTarget = player.getPlayerY();

        turnTowards(xTarget, yTarget);
        move(speed);
    }

    /**
     * Don't overlap with other flyers. 
     */
    private void avoidBunching() {
        java.util.List<Flyer> flyers = getObjectsInRange(minDistance, Flyer.class);
        for (Flyer otherFlyer : flyers) {
            if (otherFlyer != this) {
                // Calculate the distance between this Flyer and the other Flyer
                double distance = Math.hypot(getX() - otherFlyer.getX(), getY() - otherFlyer.getY());

                // If the distance is less than the minimum distance, adjust the position
                if (distance < minDistance) {
                    int newX = getX() + (int) ((minDistance - distance) * Math.cos(getRotation()));
                    int newY = getY() + (int) ((minDistance - distance) * Math.sin(getRotation()));
                    setLocation(newX, newY);
                }
            }
        }
    }

    /**
     * Checks if the Flyer is near the player based on the attack distance.
     * 
     * @return True if the Flyer is near the player, false otherwise.
     */
    private boolean isNearPlayer() {
        int xTarget = player.getPlayerX();
        int yTarget = player.getPlayerY();
        double distance = Math.hypot(getX() - xTarget, getY() - yTarget);
        return distance < attackDistance;
    }

    private void stopFlying() {
        speed = 0;
        isAttacking = false;
    }

    private void attackPlayer() {
        if (!isAttacking) {
            isAttacking = true;
            int originalSpeed = speed;
            speed = 8;  
            // Move towards the player quickly
            move(speed);
            // Bounce away from the player
            speed = -originalSpeed; 

            checkPlayerCollision();
        } else {
            // Continue bouncing back
            move(speed);
        }
    }

    private void checkPlayerCollision() {
        Player player = (Player) getOneIntersectingObject(Player.class);
        if (player != null) {
            player.getHit(1);
        }
    }
}
