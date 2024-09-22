import greenfoot.*;
/**
 * Represents the Potion Shop in the game world.
 * Players can purchase health potions from this shop to restore their health.
 * Provides different options with varying amounts of health restoration.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class PotionShop extends World
{
    private VillageConversation villageConversation;
    private Player player;
    private Button heart1, heart2, heart3, exit;
    private GreenfootImage bkg;
    private GreenfootImage potion1, potion2, potion3;
    private Weapon selectedWeapon;
    private SuperDisplayLabel label; 

    /**
     * Constructor for objects of class PotionShop.
     * Initializes the background, player, and conversation objects.
     * Displays potions, shop icons, buy buttons, and an exit button.
     * 
     * @param player The player object associated with the shop.
     * @param villageConversation The conversation object for the village.
     */
    public PotionShop(Player player, VillageConversation villageConversation)
    {

        super(1200, 800, 1);
        this.player = player;
        this.villageConversation = villageConversation;
        bkg = new GreenfootImage("shelf.PNG");
        setBackground(bkg);

        potion1 = new GreenfootImage("potionbottle1.PNG");
        potion1.scale(100,100);
        potion2 = new GreenfootImage("potionbottle2.PNG");
        potion2.scale(115,115);
        potion3 = new   GreenfootImage("flask_big_red.png");
        potion3.scale(125,125);

        //display Potion
        addObject(new Actor() {
                { setImage(potion1); }
            }, 280, 450);

        addObject(new Actor() {
                { setImage(potion2); }
            }, 580, 450);

        addObject(new Actor() {
                { setImage(potion3); }
            }, 880, 450);

        heart1 = new Button(7);
        addObject(heart1, 275, 600);

        heart2 = new Button(7);
        addObject(heart2, 588, 600);

        heart3 = new Button (7);
        addObject(heart3, 888, 600);

        addObject(new ShopIcon(5), 280, 450); 
        addObject(new ShopIcon(6), 580, 450);
        addObject(new ShopIcon(7), 880, 450);

        exit = new Button(3);
        addObject(exit, 600, 725);

        label = new SuperDisplayLabel();

    }

    public void act() {
        buy();
        exit();
    }

    public void buy() {
        if(Greenfoot.mouseClicked(heart1)){
            if(player.getCoinCount() >= 5) {
                player.incrementHp(2);
                player.decrementCoins(5);
                addObject(label, 588, 131); 
                label.update("ADDED 2 HP!");
            }
            else {
                addObject(label, 588, 131); 
                label.update("TOO POOR!");

            }
        }
        if(Greenfoot.mouseClicked(heart2)){
            if(player.getCoinCount() >= 10) {
                player.incrementHp(10);
                player.decrementCoins(10);
                addObject(label, 588, 131); 
                label.update("ADDED 10 HP!");
            }
            else {
                addObject(label, 588, 131); 
                label.update("TOO POOR!");

            }
        }
        if(Greenfoot.mouseClicked(heart3)){
            if(player.getCoinCount() >= 30) {
                player.incrementHp(25);
                player.decrementCoins(30);
                addObject(label, 588, 131); 
                label.update("ADDED 25 HP!");
            }
            else{
                addObject(label, 588, 131); 
                label.update("TOO POOR!");

            }
        }

    }

    private void exit() {
        if(Greenfoot.mouseClicked(exit)){
            Greenfoot.setWorld(villageConversation);
        }
    }
}
