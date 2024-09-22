import greenfoot.*;
/**
 * Spear class represents a specific type of weapon in the game - a spear.
 * It extends the Weapon class and includes additional features such as swing animation and attack functionality.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class Spear extends Weapon
{
    private GreenfootSound attack;
    /**
     * Constructor for Spear class.
     * Initializes the sword image, sets its cost, and creates a sound effect for the attack.
     */
    public Spear()
    {
        GreenfootImage spearImage = new GreenfootImage("weapon_spear.png"); 
        spearImage.scale(15, 50);        
        setImage(spearImage);
        attack = new GreenfootSound("slash.mp3");
        attack.setVolume(50);

        cost = 0;
    }

    /**
     * Animates the spear and attacks the player. 
     */
    public void act()
    {
        super.act();
        performSwingAnimation();
        attack();
    }

    private void performSwingAnimation() {
        if (isSwinging) {
            // Obtain the facingRight value from the Player class
            boolean facingRight = ((Player) getWorld().getObjects(Player.class).get(0)).isFacingRight();

            // Rotate the sword during the swing, flip if facing left
            int rotation = facingRight ? -swingCounter * 10 : swingCounter * 10;
            setRotation(rotation);
            attack.play();

            // Increment the swing counter
            swingCounter++;
            

            // Check if the swing animation is complete
            if (swingCounter >= 10) { 
                isSwinging = false;
                setRotation(0); // Reset the rotation
                swingCounter = 0; // Reset the counter for the next swing
            }
        }
    }

    private void attack() {
        if (isSwinging) {
            // Obtain the facingRight value from the Player class
            boolean facingRight = ((Player) getWorld().getObjects(Player.class).get(0)).isFacingRight();

            // Adjust the X-coordinate based on the facing direction
            int xOffset = facingRight ? -20 : 20;

            // Check if there is an Enemy object at the sword's current location
            Enemy enemy = (Enemy) getOneObjectAtOffset(xOffset, 0, Enemy.class);

            if (enemy != null) {
                // Attack the enemy and decrement its HP
                enemy.takeDamage(1); 
            }

            // Check if there is an boss object at the sword's current location
            Boss boss = (Boss) getOneObjectAtOffset(xOffset, 0, Boss.class);
            if (boss != null) {
                // Attack the boss and decrement its HP
                boss.takeDamage(1); 
            }
        }
    }
    /**
     * Get the type of the weapon.
     * 
     * @return The type of the weapon (SPEAR).
     */
    public int getType(){
        return Weapon.SPEAR;
    }

}
