import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A basic weapon class to display the weapons in the loading screen. 
 * 
 * @author Jessica Biro
 * @version January 2024
 */
public class WeaponBasic extends Weapon
{
    private int type;
    private GreenfootImage weapon;
    public WeaponBasic(int type){
        this.type = type;
        //change the image of the basic weapon based on which type was given
        switch(type){
            case Weapon.STAFF:
                weapon = new GreenfootImage("weapon_green_magic_staff.png"); 
                break;
            case Weapon.SWORD:
                weapon = new GreenfootImage("weapon_regular_sword.png"); 
                break;
            case Weapon.GOLDSWORD:
                weapon = new GreenfootImage("weapon_golden_sword.png"); 
            case Weapon.REDSTAFF:
                weapon = new GreenfootImage("weapon_red_magic_staff.png"); 
            default:
                weapon = new GreenfootImage("weapon_spear.png"); 
        }
        //scale and set the image 
        weapon.scale(30, 66);
        setImage(weapon);
    }
    /**
     * Getter method to get the current type of the basic weapon.
     * @return int The type of weapon. 
     */
    public int getType(){
        return type;
    }
}
