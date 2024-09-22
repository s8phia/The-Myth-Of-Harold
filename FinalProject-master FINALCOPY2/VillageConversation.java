import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * VillageConversation brings user into the different conversation in each building 
 * 
 * @author Hilary Ho, Jessica Biro, Sophia Wang 
 * @version January 2024
 */
public class VillageConversation extends World
{
    private int type; //player type 
    private int convo;
    private boolean isHouse;
    private GreenfootImage bkg; 
    
    private World VillageWorld;
    private PlayerBasic p;
    private Hitbox hitbox;

    public static int WIDTH = 1200;
    public static int HEIGHT = 800;

    private int[][] boundaries; 

    //player
    private PlayerBasic player2;
    private PlayerBasic bob;
    private Player player;

    private Text text1, text2, text3; 
    private SuperDisplayLabel label; 

    //weapon
    private Weapon playerWeapon;
    private World WeaponWorld;
    
    private boolean enteredShop = false;
    private boolean enteredPotionShop = false;
    /**
     * Constructor for objects of class VillageConversation
     * Displays the respective conversations accoriding to the building entered
     * 
     * @param type  The hero chosen 
     * @param convo The conversation chosen 
     * @param w     The world 
     * @param p     PlayerBasic in world
     */
    public VillageConversation(int type, int convo, World w, PlayerBasic p)
    {    
        super(1200, 800, 1);

        this.type = type;
        this.convo = convo; 
        VillageWorld = w;
        this.p = p;
        p.enforceBorders(false); //disables borders 

        //respective conversations initialized
        text1 = new Text(1); 
        text2 = new Text(2); 
        text3 = new Text(3);

        player2 = new PlayerBasic(type, w, false); 
        bob = new PlayerBasic(type); 
        
        label = new SuperDisplayLabel();

        if (convo == 4){ //house backgrounds
            addObject(new Hitbox(6), 600, 730);
            bkg = new GreenfootImage("bedroombkg.PNG");
            setBackground(bkg);
        }

        setPaintOrder(SuperDisplayLabel.class, Border.class, Hitbox.class); 
    }
    /**
     * The house conversation from the StoryWold
     * 
     * @param type    The hero chosen 
     * @param w       The world 
     * @param isHouse Is hero in house?
     */
    public VillageConversation(int type, World w, boolean isHouse)
    {
        super(1200, 800, 1);

        this.type = type; 
        this.isHouse = isHouse; 

        addObject(new Hitbox(6), 600, 730);
        player2 = new PlayerBasic(type, w, false); 

        house();
    }

    /**
     * Changes the background based on the conversation. 
     */
    public void act(){
        if (convo == 1){
            mayor();
        } else if (convo == 2){
            potion();
        } else if (convo == 3){
            weapon();
        } else if (convo == 4){
            house();
        }
    }
    
    /**
     * Conversation with the mayor 
     */
    private void mayor(){
        bkg = new GreenfootImage("mayorhouse.PNG");
        setBackground(bkg);

        addObject(player2, 600, 600);
        addObject(bob, 600, 300);
        if (bob.checkPlayer()){
            addObject(text1, 600, 400);
        }
        if (text1.getStoryNum() == 11){
            otherExit();
        }

    }
    
    /**
     * Conversation with the potion merchant 
     */
    private void potion(){
        bkg = new GreenfootImage("potion.PNG");
        setBackground(bkg);

        addObject(player2, 600, 600);
        player2.getImage().scale(50,80);
        addObject(bob, 600, 300);
        if (bob.checkPlayer()){
            addObject(text2, 600, 400);
        }
        if (text2.getStoryNum() == 3 && !enteredPotionShop){
            Greenfoot.setWorld(new PotionShop(p, this));
            enteredPotionShop = true;
        }
        exit();
    }

    /**
     * Conversation with the weapon merchant 
     */
    private void weapon(){
        bkg = new GreenfootImage("weapon.PNG");
        setBackground(bkg);

        addObject(player2, 600, 600);
        player2.getImage().scale(50,80);

        addObject(bob, 600, 300);
        if (bob.checkPlayer()){
            addObject(text3, 600, 400);
        }
        if(text3.getStoryNum() == 3 && !enteredShop){ 
            Greenfoot.setWorld(new WeaponShop(p, this));
            enteredShop = true;

        }

        if (playerWeapon != null) { //adds bought weapon
            addObject(playerWeapon, 500, 700);
        }

        exit();
    }
    
    /**
     * Conversation in the house
     */
    private void house(){
        bkg = new GreenfootImage ("bedroombkg.PNG"); 
        setBackground(bkg);

        addObject(player2, 600, 300); 
        player2.getImage().scale(50,80);

        exit();
    }

    private void otherExit(){
        removeObject(player2);

        p.setLocation(p.getX(), p.getY() + 25);
        p.enforceBorders(true);
        Greenfoot.setWorld(VillageWorld);  
    }

    private void exit(){
        if (Greenfoot.mouseClicked(this)){
            removeObject(player2);

            p.setLocation(p.getX(), p.getY() + 25);
            p.enforceBorders(true);
            Greenfoot.setWorld(VillageWorld);  

        }
    }
    /**
     * Returns the conversation number 
     * 
     * @return int The conversation 
     */
    public int getConvoNum(){
        return convo; 
    }
    
    /**
     * Equips player with weapon.
     * @param weapon    The weapon that the player equips. 
     */
    public void setPlayerWeapon(Weapon weapon) {
        playerWeapon = weapon;
    }
}