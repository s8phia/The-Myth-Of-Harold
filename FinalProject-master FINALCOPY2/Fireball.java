import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Fireball class represents a projectile fired by the player or the boss.
 * It extends the Attack class and includes functionalities for movement,
 * animation, and removal based on different constructors.
 * 
 * @author Sophia Wang and Jessica Biro
 * @version January 2024
 */
public class Fireball extends Attack
{
    private int speed; 
    private int shootAtX;
    private int shootAtY;
    private int life;

    private static final int SPEED = 5;
    private boolean launched = false;
    private int animationCounter = 0;
    private List<GreenfootImage> animationImages = new ArrayList<>();
    private boolean creeperFireball = false; //if it's the fireball that enemys throw
    /**
     * Fireballs thrown by creepers. Shoots at the player. 
     */
    public Fireball() {
        init();
        life = 200;
        creeperFireball = true;
    }

    /**
     * Constructor for shooting fireballs at a specific coordinate.
     * Used for boss targeting player.
     * 
     * @param x The x-coordinate to shoot towards.
     * @param y The y-coordinate to shoot towards.
     */
    public Fireball(int x, int y){
        init();

        shootAtX = x;
        shootAtY = y;

        // despawns when expired
        life = 200;

    }

    /**
     * Constructor for shooting fireballs at a specified degree rotation.
     * Used for the boss' circle attack.
     * 
     * @param degrees The rotation angle in degrees.
     */
    public Fireball(int degrees){
        // despawns at edge ~ 500
        life = 200;
        init();
        turn(degrees); 
    }
    
    /**
     * Animates the fireball.
     */
    private void animateFireball() {
        // Change the image every few acts to create animation
        if (animationCounter % 3 == 0) {
            int currentImageIndex = animationCounter / 3 % animationImages.size();
            setImage(animationImages.get(currentImageIndex));
        }

        animationCounter++;
    }

    /**
     * Initializes all the variables to avoid repetition of the speed and resizing of the fireballs
     */
    private void init(){
        // Load multiple images for animation
        for (int i = 1; i <= 4; i++) {
            GreenfootImage fireballImage = new GreenfootImage("FB500-" + i + ".png");
            fireballImage.scale(40, 40);
            animationImages.add(fireballImage);
        }
        // Set the initial image
        setImage(animationImages.get(0));
        //-1 by default unless the constructor changes it 
        shootAtX = -1;
        shootAtY = -1;
    }

    /**
     * Fireball goes where it has to based on its constructor. 
     * Has a lifespawn of 100 acts and gets removed once it surpasses and it hasn't had contact with the player or the edge. 
     */
    public void act()
    {
        //if the shoot at x and y aren't empty (if they used the first constructor) turn towards coordinates
        if (shootAtX != -1 && shootAtY != -1)
        {
            turnTowards(shootAtX, shootAtY); 
        }
        if(creeperFireball){
            if (!launched) {
                moveTowardsPlayer();
                launched = true; // Set the state to launched once
            }
        }
        //move until removed & take away life every act     
        life--;
        move(SPEED); // Move the fireball continuously
        checkOutOfBounds();
        animateFireball();

        if(life <= 0){
            if(this.getWorld() != null){
                getWorld().removeObject(this);
            }
        }
    }

    /**
     * Moves the fireball towards the players current location. 
     */
    private void moveTowardsPlayer() {
        Player player = getWorld().getObjects(Player.class).get(0);
        if (player != null) {
            int angle = (int) Math.toDegrees(Math.atan2(player.getY() - getY(), player.getX() - getX()));
            setRotation(angle);
        }
    }

    /**
     * Checks for the fireball to be out of bounds of the world to remove it. 
     */
    private void checkOutOfBounds() {
        // If the fireball reaches the end of the world, remove it
        if (getX() <= 0 || getX() >= getWorld().getWidth() - 1 || getY() <= 0 || getY() >= getWorld().getHeight() - 1 || this.isTouching(Player.class)) {
            getWorld().removeObject(this);
        }
    }
}
