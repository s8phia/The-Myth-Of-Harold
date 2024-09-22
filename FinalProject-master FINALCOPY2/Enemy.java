import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Abstract class representing an enemy in the game.
 * Enemies have a view range, health points (HP), and can take damage.
 * They are animated and can drop coins or heart potions upon defeat.
 * 
 * @author Sophia Wang and Jessica Biro
 * @version January 2024
 */
public abstract class Enemy extends SuperSmoothMover
{
    private static final int VIEW_RANGE = 200 + 15;
    private static final int MIN_TRANSPARENCY = 255 - VIEW_RANGE;

    private int curTransparency;
    int roomX, roomY, counter;
    List<Cell> path;
    List<Node> actualPath;
    Cell cell;
    int cellX, cellY; // map coordinates

    GreenfootImage img = new GreenfootImage("creeper10idle0.png");

    // Image animation 
    private GreenfootImage[] animationImages;

    private int imageIndex;
    private int animationDelay = 6;
    private int currentDelay = 0;

    private int frameCounter = 0;
    private int currentFrame = 0;

    private int directionX = 1;

    protected int hp = 1;

    /**
     * Constructor for Enemy class.
     * 
     * @param x The X-coordinate of the room in which the enemy is located.
     * @param y The Y-coordinate of the room in which the enemy is located.
     */
    public Enemy(int x, int y){
        roomX = x;
        roomY = y;
        counter = 0;
        path = new ArrayList<Cell>();
        actualPath = new ArrayList<Node>();
    }

    /**
     * Animate the enemy and set the fog transparency. 
     */
    public void act() {
        if(DungeonWorld.bossFight){
            getImage().setTransparency(150);   
        } else {
            fog(); 
        }
        animate();
    }   

    /**
     * Set transparency based on distance from the current player x and y. 
     */
    private void fog() {
        ArrayList<Player> p = (ArrayList<Player>)getWorld().getObjects(Player.class);
        if(p.size() > 0){
            int dist = (int)(calculateDistanceBetween(this, p.get(0))/1.5);
            if(dist < VIEW_RANGE){
                getImage().setTransparency(255-dist);
            } else {
                getImage().setTransparency(MIN_TRANSPARENCY);
            }
        }
    }

    /**
     * Calculates the distance between two actors.
     * 
     * @param a The first actor.
     * @param b The second actor.
     * @return The distance between the two actors.
     */
    private double calculateDistanceBetween(Actor a, Actor b){
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    /**
     * Sets the current cell and map coordinates of the enemy.
     * 
     * @param cell The current cell the enemy occupies.
     * @param x The X-coordinate in the map.
     * @param y The Y-coordinate in the map.
     */
    public void setCell(Cell cell, int x, int y) {
        this.cell = cell;
        cellX = x;
        cellY = y;
    }

    /**
     * Animates the enemies by iterating through the array of images. 
     */
    public void animate() {
        if (currentDelay == 0) {
            setImage(animationImages[imageIndex]);

            // Increment the image index, and loop back to 0 when it reaches the end
            imageIndex = (imageIndex + 1) % animationImages.length;

            currentDelay = animationDelay;
        } else {
            currentDelay--;
        }
    }
    /**
     * Sets the animation images for the enemy.
     * 
     * @param images The array of animation images.
     */
    public void setAnimationImages(GreenfootImage[] images) {
        animationImages = images;
        setImage(animationImages[0]);  
    }
    /**
     * Gets the animation images of the enemy.
     * 
     * @return The array of animation images.
     */
    public GreenfootImage[] getAnimationImages() {
        return animationImages;
    }

    /**
     * Inflicts damage on the enemy, deducting health points.
     * If the health points reach zero or below, the enemy is removed from the world.
     * Coins or a heart potion may be dropped upon defeat.
     * 
     * @param damage The amount of damage to be inflicted.
     */
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            if(this instanceof MiniBoss){
                DungeonWorld.miniBossDefeated = true; 
            }

            // Remove the enemy from the world
            DungeonWorld.allEnemies.get(DungeonWorld.CURR_ROOM).remove(this);

            Poof deathEffect = new Poof();
            getWorld().addObject(deathEffect, getX(), getY());

            if(Greenfoot.getRandomNumber(2) == 0){
                int numCoins = Greenfoot.getRandomNumber(3) + 1;
                for (int i=0; i < numCoins; i ++){
                    Coin coin = new Coin();
                    getWorld().addObject(coin, getX(), getY());
                }
            } else {
                HeartPotion heart = new HeartPotion();
                getWorld().addObject(heart, getX(), getY());
            }

            // Remove the actor from the world
            getWorld().removeObject(this);
        }
    }

}
