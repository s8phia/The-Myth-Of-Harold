import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The base class for different types of weapons in the game.
 * Weapons can be swung by the player, and each weapon has a specific type and cost.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public abstract class Weapon extends Actor
{

    protected boolean isSwinging = false;
    protected int swingCounter = 0;

    public static final int SPEAR = 0;
    public static final int STAFF = 1;
    public static final int SWORD = 2;
    public static final int GOLDSWORD = 3;
    public static final int REDSTAFF = 4;

    protected ShopIcon shopIcon;
    protected int cost;
    public void act()
    {
        if(!(getWorld() instanceof WeaponShop)){
            checkForSwing();
        }
    }

    /**
     * Checks if the weapon is currently swinging. 
     */
    public void checkForSwing() {
        if (Greenfoot.mouseClicked(null) && !isSwinging) {
            // Get the type of the currently equipped weapon from the Player class
            int equippedWeaponType = ((Player) getWorld().getObjects(Player.class).get(0)).getWeaponType();

            // Check if the currently equipped weapon type matches the type of this weapon
            if (getType() == equippedWeaponType) {
                if(equippedWeaponType == SWORD || equippedWeaponType == GOLDSWORD || equippedWeaponType == SPEAR){
                    isSwinging = true;
                    swingCounter = 0;

                    // Obtain the facingRight value from the Player class
                    boolean facingRight = ((Player) getWorld().getObjects(Player.class).get(0)).isFacingRight();

                    // Adjust the X-coordinate based on the facing direction
                    int xOffset = facingRight ? -20 : 20;

                    // Slash slash = new Slash();
                    // getWorld().addObject(slash, getX() + xOffset, getY());
                }
            }
        }
    }

    protected abstract int getType();

    /**
     * Get the cost of the weapon.
     * 
     * @return The cost of the weapon.
     */
    protected int getCost() {
        return cost;
    }
}
