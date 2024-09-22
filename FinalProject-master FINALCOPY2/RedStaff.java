import greenfoot.*;
/**
 * RedStaff class represents a specific type of weapon in the game - a red staff.
 * It extends the Weapon class and includes additional features such as swing animation and attack functionality.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class RedStaff extends Weapon
{
    private GreenfootSound attack;

    public RedStaff()
    {
        GreenfootImage spearImage = new GreenfootImage("weapon_red_magic_staff.png"); 
        spearImage.scale(15, 50);        
        setImage(spearImage);

        attack = new GreenfootSound("Fireball.wav");
        attack.setVolume(30);

        cost = 60;
    }

    /**
     * Animates the sword and attacks the player. 
     */
    public void act()
    {
        super.act();
        if(!(getWorld() instanceof WeaponShop)){
            checkForAttack();
        }
        performSwingAnimation();
        attack();
    }

    /**
     * Checks if the staff is attacking. 
     */
    public void checkForAttack() {
        if(Greenfoot.mouseClicked(null) && !isSwinging){
            int equippedWeaponType = ((Player) getWorld().getObjects(Player.class).get(0)).getWeaponType();
            if(getType() == equippedWeaponType){
                if(equippedWeaponType == REDSTAFF){
                    isSwinging = true;
                    swingCounter = 0;
                    boolean facingRight = ((Player) getWorld().getObjects(Player.class).get(0)).isFacingRight();

                    // Adjust the X-coordinate based on the facing direction
                    int xOffset = facingRight ? -20 : 20;

                    RedEnergyball energyball = new RedEnergyball();

                    // Set the initial position of the energy ball
                    int energyballX = getX() + xOffset;
                    int energyballY = getY();
                    getWorld().addObject(energyball, energyballX, energyballY);
                    attack.play();
                }
            }

        }
    }

    private void performSwingAnimation() {
        if (isSwinging) {
            // Obtain the facingRight value from the Player class
            boolean facingRight = ((Player) getWorld().getObjects(Player.class).get(0)).isFacingRight();

            // Rotate the sword during the swing, flip if facing left
            int rotation = facingRight ? -swingCounter * 10 : swingCounter * 10;
            setRotation(rotation);

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
                enemy.takeDamage(5); 
            }
        }
    }

    /**
     * Get the type of the weapon.
     * 
     * @return The type of the weapon (REDSTAFF).
     */
    public int getType(){
        return Weapon.REDSTAFF;
    }

}
