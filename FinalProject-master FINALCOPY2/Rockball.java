import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Rockball class represents a projectile launched by the MiniBoss enemy.
 * It extends the Attack class and includes animations for its movement.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class Rockball extends Attack {
    private static final int SPEED = 5;
    private boolean launched = false;
    private int animationCounter = 0;
    private List<GreenfootImage> animationImages = new ArrayList<>();

    public Rockball() {
        // Load multiple images for animation
        for (int i = 1; i <= 3; i++) {
            GreenfootImage rockballImage = new GreenfootImage("RB" + i + ".png");
            rockballImage.scale(40, 40);
            animationImages.add(rockballImage);
        }

        // Set the initial image
        setImage(animationImages.get(0));
    }

    private void animateRockball() {
        // Change the image every few acts to create animation
        if (animationCounter % 3 == 0) {
            int currentImageIndex = animationCounter / 3 % animationImages.size();
            setImage(animationImages.get(currentImageIndex));
        }

        animationCounter++;
    }

    /**
     * Move towards the player and check out of bounds. 
     */
    public void act() {
        if (!launched) {
            moveTowardsPlayer();
            launched = true; // Set the state to launched once
        }
        move(SPEED); // Move the fireball continuously
        checkOutOfBounds();
        animateRockball();
    }

    private void moveTowardsPlayer() {
        Player player = getWorld().getObjects(Player.class).get(0);
        if (player != null) {
            int angle = (int) Math.toDegrees(Math.atan2(player.getY() - getY(), player.getX() - getX()));
            setRotation(angle);
        }
    }

    private void checkOutOfBounds() {
        // If the fireball reaches the end of the world, remove it
        if (getX() <= 0 || getX() >= getWorld().getWidth() - 1 || getY() <= 0 || getY() >= getWorld().getHeight() - 1) {
            getWorld().removeObject(this);
        }
    }
}
