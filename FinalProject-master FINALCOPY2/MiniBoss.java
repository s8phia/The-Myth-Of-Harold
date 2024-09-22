import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class representing the MiniBoss enemy in the game.
 * MiniBoss enemies have different attacks based on the world level.
 * They can throw plantballs, rockballs, or bite the player.
 * MiniBoss enemies have a larger size and more hit points compared to regular enemies.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class MiniBoss extends Enemy
{

    private int ballCooldown = 100; 
    private int ballCounter = Greenfoot.getRandomNumber(ballCooldown); // Randomize the counter
    private int ballDurationCounter = 0;
    /**
     * Constructor for MiniBoss class.
     * 
     * @param x The X-coordinate of the room in which the mini-boss is located.
     * @param y The Y-coordinate of the room in which the mini-boss is located.
     * @param worldLevel The current level of the world.
     */
    public MiniBoss(int x, int y, int worldLevel) {
        super(x, y);
        setAnimationImages(worldLevel);
        setEnemyImage();
        hp = 15;
    }

    public void act() {
        super.act(); 
        attack();
    }

    private void throwPlantball() {
        if (ballCounter <= 0) {
            Player player = getWorld().getObjects(Player.class).get(0);

            if (player != null) {
                Plantball plantball = new Plantball();
                getWorld().addObject(plantball, getX(), getY());

                // Calculate the angle towards the player
                int angle = (int) Math.toDegrees(Math.atan2(player.getY() - getY(), player.getX() - getX()));

                // Set the plantball rotation
                plantball.setRotation(angle);

                // Reset the counter to the cooldown value
                ballCounter = ballCooldown;
            }
        } else {
            // Decrease the counter until it reaches 0
            ballCounter--;
        }

    }

    private void throwRockball(){
        if (ballCounter <= 0) {
            Player player = getWorld().getObjects(Player.class).get(0);

            if (player != null) {
                Rockball rockball = new Rockball();
                getWorld().addObject(rockball, getX(), getY());

                // Calculate the angle towards the player
                int angle = (int) Math.toDegrees(Math.atan2(player.getY() - getY(), player.getX() - getX()));

                // Set the plantball rotation
                rockball.setRotation(angle);
                // Reset the counter to the cooldown value
                ballCounter = ballCooldown;
            }
        } else {
            // Decrease the counter until it reaches 0
            ballCounter--;
        }

    }

    private void bite() {
        Player player = (Player) getOneObjectAtOffset(0, 0, Player.class);

        if (player != null) {
            player.getHit(3); 
        }
    }

    /**
     * Sets the animation images for the MiniBoss based on the world level.
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
            creeperImages[i] = new GreenfootImage("boss" + lvlIndex + "0" +  "run" + i + ".png");
            creeperImages[i].scale(95, 100);
        }

        setAnimationImages(creeperImages);
    }

    private void setEnemyImage() {
        setImage(getAnimationImages()[0]);  // Set the default image
    }

    /**
     * Attack the player based on which mini boss instance. 
     */
    private void attack() {
        if(getWorld() instanceof DungeonWorld){
            DungeonWorld DungeonWorld = (DungeonWorld) getWorld();
            int worldLevel = DungeonWorld.worldLevel;

            if(worldLevel == 1){
                throwPlantball();
            }
            if(worldLevel == 2){
                throwRockball();
            }
            if(worldLevel == 3){
                bite();
            }
        }
    }

    /**
     * Checks if the MiniBoss is dead (hp <= 0).
     * 
     * @return True if the MiniBoss is dead, false otherwise.
     */
    public boolean isDead(){
        if (hp <= 0){
            return true;
        }
        return false;
    }
}
