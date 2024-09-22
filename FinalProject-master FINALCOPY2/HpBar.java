import greenfoot.*;
import java.util.ArrayList;
/**
 * The stats bar for the player and the boss. Displays the current world level, coins and hp for the player. 
 * For the boss displays only the current hp. 
 * 
 * @author Jessica Biro 
 * @version January 2024
 */
public class HpBar extends Effect {
    //SuperStatBar variables
    private SuperStatBar hpBar;
    public static final Color RIGHT_BAR_COLOR = new Color(119, 158, 203);
    public static final Color LEFT_BAR_COLOR = new Color(180, 211, 178);
    private int maxHP;
    private int currHP;
    
    //owners of the stat bar
    private Player player;
    private Boss boss;
    
    public static Color brown = new Color(117, 13, 16);

    private GreenfootImage img; //current stats image
    private int type; //type of hero to display

    private boolean isBoss = false;
    /**
     * Creates a new HpBar for the player using the player's current hp. 
     * @param player    The player that the HpBar is attached to. 
     */
    public HpBar(Player player) {
        this.player = player;
        type = player.getHeroType();
        //set image and hp
        img = new GreenfootImage("hero" + (type) + "stats.PNG");
        img.scale(450, 230);
        setImage(img);
        maxHP = player.MAX_HP;
        currHP = maxHP;

        hpBar = new SuperStatBar(maxHP, currHP, this, 170, 20, 15, Color.GREEN, Color.RED, false, Color.BLACK, 1);
    }

    /**
     * Creates a new HpBar for the boss using the boss's current hp. 
     * @param boss    The boss that the HpBar is attached to. 
     */
    public HpBar(Boss boss){
        this.boss = boss;
        isBoss = true;
        //set image and hp
        img = new GreenfootImage("boss4stats.PNG");
        img.scale(500,256);
        setImage(img);
        maxHP = boss.MAX_HP;
        currHP = maxHP;

        hpBar = new SuperStatBar(maxHP, currHP, this, 170, 20, 5, Color.GREEN, Color.RED, false, Color.BLACK, 1);
    }

    /**
     * Once added to world, add a superstatbar to the hpBar and update the current hp. 
     */
    public void addedToWorld(){
        getWorld().addObject(hpBar, DungeonWorld.HPBARX, DungeonWorld.HPBARY);
        hpBar.update(currHP);
    }

    /**
     * Update the stat bar based on whether or not it is the boss. 
     */
    public void act() {
        if(isBoss){
            updateBoss();
        } else{
            updatePlayer();
        }
    }

    /**
     * Update the boss' current HP from the boss in the world. 
     */
    private void updateBoss(){
        // Update the health bar with the player's current HP
        ArrayList<Boss> boss = (ArrayList<Boss>)getWorld().getObjects(Boss.class);
        if (boss.size() > 0) {
            currHP = boss.get(0).getHP();
            hpBar.update(currHP);
        }

        if(hpBar.getWorld() == null){
            getWorld().addObject(hpBar, DungeonWorld.SUPERSTATBARX, DungeonWorld.SUPERSTATBARX);
        }
    }

    /**
     * Update the boss' current HP from the boss in the world. 
     */
    private void updatePlayer(){
        // Update the health bar with the player's current HP
        Player p = (Player) getWorld().getObjects(Player.class).get(0);
        if (p != null) {
            currHP = player.getPlayerHP();
            hpBar.update(currHP);
        }

        if(hpBar.getWorld() == null){
            getWorld().addObject(hpBar, DungeonWorld.SUPERSTATBARX, DungeonWorld.SUPERSTATBARX);
        }
    }
    
    /**
     * Update the statbars current world level and coins everytime it increments. 
     * Draws directly on the stat bar, resets image everytime the method is called. 
     */
    public void updateLevelAndCoins(){
        setImage(new GreenfootImage("hero" + (type) + "stats.PNG"));
        getImage().scale(450, 230);
        getImage().setFont(new Font("Marlboro", 25));
        getImage().setColor(brown);
        getImage().drawString(DungeonWorld.worldLevel + " ", 150, 110);
        getImage().drawString(getWorld().getObjects(Player.class).get(0).getCoinCount() + " ", 230, 110);
    }
}
