import greenfoot.*;
/**
 * WeaponShop class represents a shop where the player can purchase weapons.
 * It extends the World class and includes functionality for displaying weapons, shop icons, and buy buttons.
 * Players can purchase weapons if they have enough coins and can exit the shop.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class WeaponShop extends World
{
    private VillageConversation villageConversation;
    private Player player;
    private Button weapon1, weapon2, weapon3, weapon4, weapon5, exit;
    private GreenfootImage bkg;
    private Weapon selectedWeapon;
    private Weapon playerBasicSelectedWeapon;
    private SuperDisplayLabel label, coinLabel; 

    /**
     * Constructor for objects of class WeaponShop.
     * Initializes the background, player, and conversation objects.
     * Displays weapons, shop icons, buy buttons, and an exit button.
     * 
     * @param player The player object associated with the shop.
     * @param villageConversation The conversation object for the village.
     */
    public WeaponShop(Player player, VillageConversation villageConversation)
    {
        super(1200, 800, 1);

        bkg = new GreenfootImage("shelf.PNG");
        setBackground(bkg);
        this.player = player;
        this.villageConversation = villageConversation;

        //display weapons
        GoldSword goldSword = new GoldSword();
        goldSword.getImage().scale(76, 120); 
        addObject(goldSword, 195, 457);

        RedStaff redStaff = new RedStaff();
        redStaff.getImage().scale(56, 120); 
        addObject(redStaff, 392, 457);

        Spear spear = new Spear();
        spear.getImage().scale(30, 120); 
        addObject(spear, 588, 457);

        Staff staff = new Staff();
        staff.getImage().scale(56, 120); 
        addObject(staff, 782, 457);

        Sword sword = new Sword();
        sword.getImage().scale(76, 120); 
        addObject(sword, 971, 457); 

        //add shop icons to corresponding weapon location 
        addObject(new ShopIcon(3), 195, 457); 
        addObject(new ShopIcon(4), 392, 457);
        addObject(new ShopIcon(0), 588, 457);
        addObject(new ShopIcon(1), 782, 457);
        addObject(new ShopIcon(2), 971, 457);

        //add buy buttons rarrr
        weapon1 = new Button(7);
        addObject(weapon1, 195, 600);
        weapon2 = new Button(7);
        addObject(weapon2, 392, 600);
        weapon3 = new Button(7);
        addObject(weapon3, 588, 600);
        weapon4 = new Button(7);
        addObject(weapon4, 782, 600);
        weapon5 = new Button(7);
        addObject(weapon5, 971, 600);

        //exit button
        exit = new Button(3);
        addObject(exit, 600, 725);

        label = new SuperDisplayLabel();
        coinLabel = new SuperDisplayLabel();
        addObject(coinLabel, 800, 131);
        coinLabel.update("Coins: " + Player.coinCount);
    }

    public void act() {
        buy();
        exit();
    }

    public void buy() {
        if(Greenfoot.mouseClicked(weapon1)){
            purchaseWeapon(new GoldSword());
        }
        if(Greenfoot.mouseClicked(weapon2)){
            purchaseWeapon(new RedStaff());
        }
        if(Greenfoot.mouseClicked(weapon3)){
            purchaseWeapon(new Spear());
        }
        if(Greenfoot.mouseClicked(weapon4)){
            purchaseWeapon(new Staff());
        }
        if(Greenfoot.mouseClicked(weapon5)){
            purchaseWeapon(new Sword());
        }
    }

    /**
     * Handles the purchase of a weapon by the player.
     * Checks if the player has enough coins to make the purchase.
     * If the purchase is successful, updates the player's weapon and displays a success message.
     * If the player doesn't have enough coins, displays a message indicating insufficient funds.
     * 
     * @param weapon The weapon object to be purchased.
     */
    private void purchaseWeapon(Weapon weapon){
        if(Player.coinCount >= weapon.getCost()){
            Player.coinCount -= weapon.getCost();
            coinLabel.update("Coins: " + Player.coinCount);
            int selectedWeapon = weapon.getType();
            Player.weaponType = selectedWeapon;
            //save the weapon and coin numbers
            FileWriter f = new FileWriter();
            try { 
                f.saveWorldToFile(WelcomeWorld.currentFileName, player); //save the world with the player having max hp but a spear 
            } catch (Exception e) {
                System.out.println("Couldn't save world");
            }

            addObject(label, 588, 131); 
            label.update("Added to backpack. See in cave.");
        } else{
            addObject(label, 588, 131); 
            label.update("TOO POOR!");
        }
    }

    private void exit() {
        if(Greenfoot.mouseClicked(exit)){
            Greenfoot.setWorld(villageConversation);
        }
    }

}
