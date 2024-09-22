import greenfoot.*;
/**
 * Energyball class represents a projectile fired by the player.
 * It extends the Attack class and includes functionalities
 * for movement, collision detection with enemies and bosses,
 * and animation.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class Energyball extends Attack {

    private static final int SPEED = 5;
    private boolean launched = false;
    private int animationCounter = 0;
    private boolean removed = false; 

    public Energyball() {
        GreenfootImage energyBallImage = new GreenfootImage("EB1.png");
        energyBallImage.scale(25, 25);
        setImage(energyBallImage);
    }

    private void animateEnergyball() {
        if (animationCounter % 3 == 0) {
            int currentImageIndex = animationCounter / 3 % 3; 
            GreenfootImage currentImage = new GreenfootImage("EB" + (currentImageIndex + 1) + ".png");
            currentImage.scale(25, 25);
            setImage(currentImage);
        }

        animationCounter++;
    }
    /**
     * Launches in the direction of the mouse click, and checks for collisions
     * with the boss and enemy and checks for out of bounds. 
     */
    public void act() {
        if (!launched) {
            launched = true;
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null) {
                int deltaX = mouse.getX() - getX();
                int deltaY = mouse.getY() - getY();
                double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
                setRotation((int) angle);
            }
        }

        move(SPEED);
        checkCollisionWithEnemy();
        checkCollisionWithBoss();
        checkOutOfBounds();
        animateEnergyball();

    }

    private void checkCollisionWithEnemy() {
        // Check for collisions with enemies
        Actor enemy = getOneIntersectingObject(Enemy.class);
        if (enemy != null) {
            // If collision with an enemy occurs, decrement its HP
            ((Enemy) enemy).takeDamage(1); 
            
            removed = true;

        }
    }
    
    private void checkCollisionWithBoss() {
        // Check for collisions with enemies
        Actor boss = getOneIntersectingObject(Boss.class);
        if (boss != null) {
            // If collision with an enemy occurs, decrement its HP
            ((Boss) boss).takeDamage(1); 
            removed = true;
        }
    }

    private void checkOutOfBounds() {
        // If the fireball is outside the world bounds, remove it
        if (getWorld() != null && !removed && isAtEdge()) {
            removed = true; // Mark as removed
        }

        // If marked as removed, remove the Energyball
        if (removed) {
            getWorld().removeObject(this);
        }
    }

}
