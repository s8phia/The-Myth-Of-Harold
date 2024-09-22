import greenfoot.*;
/**
 * Staff class represents a specific type of weapon in the game - a magic staff.
 * It extends the Weapon class and includes additional features such as attack functionality using energy balls.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class Staff extends Weapon
{
    private GreenfootSound attack;
    /**
     * Constructor for Staff class.
     * Initializes the sword image, sets its cost, and creates a sound effect for the attack.
     */
    public Staff()
    {
        GreenfootImage spearImage = new GreenfootImage("weapon_green_magic_staff.png"); 
        spearImage.scale(15, 50);        
        setImage(spearImage);

        attack = new GreenfootSound("Fireball.wav");
        attack.setVolume(30);

        cost = 40;
    }

    /**
     * Animates the staff and attacks the player. 
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
     * Checks for the staff's attack. 
     */
    public void checkForAttack() {
        if(Greenfoot.mouseClicked(null) && !isSwinging){
            int equippedWeaponType = ((Player) getWorld().getObjects(Player.class).get(0)).getWeaponType();
            if(getType() == equippedWeaponType){
                if(equippedWeaponType == STAFF){
                    isSwinging = true;
                    swingCounter = 0;
                    boolean facingRight = ((Player) getWorld().getObjects(Player.class).get(0)).isFacingRight();

                    // Adjust the X-coordinate based on the facing direction
                    int xOffset = facingRight ? -20 : 20;

                    Energyball energyball = new Energyball();

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
                enemy.takeDamage(1); 
            }
        }
    }

    /**
     * Get the type of the weapon.
     * 
     * @return The type of the weapon (STAFF).
     */
    public int getType(){
        return Weapon.STAFF;
    }
}
