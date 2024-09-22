import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * ShopIcon class represents an icon displayed in the shop for selecting different weapons or items.
 * It extends the Effect class and includes functionality for handling mouse hover events.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class ShopIcon extends Effect
{
    private GreenfootImage defaultIcon;
    private GreenfootImage currentIcon;

    private boolean onIcon = false;
    /**
     * Constructs a ShopIcon object based on the specified weapon type.
     * 
     * @param weaponType The type of weapon or item for the shop icon.
     */
    public ShopIcon(int weaponType) {
        switch (weaponType) {
            case 0: // SPEAR
                defaultIcon = new GreenfootImage("spear.PNG");
                break;
            case 1: // STAFF
                defaultIcon = new GreenfootImage("greenstaff.PNG"); 
                break;
            case 2: // SWORD
                defaultIcon = new GreenfootImage("diamondsword.PNG"); 
                break;
            case 3: // GOLDSWORD
                defaultIcon = new GreenfootImage("goldsword.PNG");
                break;
            case 4: // REDSTAFF
                defaultIcon = new GreenfootImage("redstaff.PNG");
                break;
            case 5: //HEARTPOTION1 (not a weapon, but just go with it)
                defaultIcon = new GreenfootImage("potion1.PNG");
                break;
            case 6: //HEARTPOTION2
                defaultIcon = new GreenfootImage("potion2.PNG");
                break;
            case 7: //HEARTPOTION3
                defaultIcon = new GreenfootImage("potion3.PNG");

                break;

            default:
                defaultIcon = new GreenfootImage("spear.PNG");
                break;
        }

        currentIcon = defaultIcon;
        currentIcon.scale(210, 210);

        // Set the initial image
        setImage(currentIcon);
    }

    /**
     * Checks if the users mouse if hovering over the icon 
     * to display the stats. 
     */
    public void act()
    {
        iconHover();
    }

    private void iconHover() {
        if(Greenfoot.mouseMoved(this)){
            onIcon = true; 
        }
        else if(Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this)){
            onIcon = false; 
        }

        if(onIcon){
            setImage(currentIcon);
            getImage().setTransparency(255);
        }
        else{
            getImage().setTransparency(0);
        }
    }
}
