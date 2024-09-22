import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.FileNotFoundException;

/**
 * Write a description of class PauseWorld here.
 * 
 * @author Jessica Biro 
 * @version January 2024
 */
public class PauseWorld extends World
{
    private GreenfootImage background;
    private GreenfootImage[] instructions; 
    private int instructionsIndex = 0;
    private Button save, play, help, exit, back, next, respawn;
    public static final int XOFFSET = 600;
    public static final int YOFFSET = 350;
    public static final int XNEXT = 1100;
    public static final int YNEXT = 700;

    private World dungeon;
    private Player player;

    private boolean died; 
    /**
     * Pauseworld to allow the player to get help, save the game, exit or if the player
     * died to respawn.
     * @param w     The dungeon world
     * @param p     The player
     * @param died  Whether or not the player died
     * 
     */
    public PauseWorld(World w, Player p, boolean died)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1); 
        this.died = died;
        //if died spawn the respawn button
        if(died){
            respawn = new Button(Button.RESPAWN, true);
            addObject(respawn, getWidth()/2, getHeight()/2 + 200);
            background = new GreenfootImage("deathscreen.PNG");
        } else{
            background = new GreenfootImage("menu.PNG");
            background.scale(1200, 800);
            setBackground(background);
            instructions = new GreenfootImage[2];
            instructions[0] = new GreenfootImage("NEWESTinstructions2.PNG");
            instructions[1] = new GreenfootImage("NEWESTinstructions.PNG");

            back = new Button(Button.BACK, true);
            initButtons();
            addButtonsToWorld();
        }
        dungeon = w;
        player = p;
    }

    public void act(){
        if(died){
            //if died, save the game set HP to max and respawn in the village 
            if(Greenfoot.mouseClicked(respawn)){
                player.setHP(player.MAX_HP);
                player.setWeapon(Weapon.SPEAR); //start with a spear again
                FileWriter f = new FileWriter();
                try { 
                    f.saveWorldToFile(WelcomeWorld.currentFileName, player); //save the world with the player having max hp but a spear 
                } catch (Exception e) {
                    System.out.println("Couldn't save world");
                }
                Greenfoot.setWorld(new VillageConversation(player.getHeroType(), new VillageWorld(player.getHeroType()), true));
            }
        } else{
            if(Greenfoot.mouseClicked(help)){
                setBackground(instructions[instructionsIndex]);
                removeObjects(getObjects(Button.class));
                addObject(back, XNEXT, YNEXT);
                addObject(next, XNEXT, YNEXT - 100);
            }
            if(Greenfoot.mouseClicked(next)){
                instructionsIndex++;
                if(instructionsIndex > 1){
                    instructionsIndex = 0;
                }
                setBackground(instructions[instructionsIndex]);
            }
            if(Greenfoot.mouseClicked(save)){
                if(Greenfoot.mouseClicked(save)){
                    FileWriter f = new FileWriter();
                    try { 
                        f.saveWorldToFile(WelcomeWorld.currentFileName, player);
                    } catch (Exception e) {
                        System.out.println("Couldn't save world");
                    }
                }
            }
            if(Greenfoot.mouseClicked(play)){
                Greenfoot.setWorld(dungeon);
            }
            if(Greenfoot.mouseClicked(back)){
                removeObject(back);
                removeObject(next);
                setBackground(background);
                addButtonsToWorld();
            }
            if(Greenfoot.mouseClicked(exit)){
                FileWriter f = new FileWriter();
                try { 
                    f.saveWorldToFile(WelcomeWorld.currentFileName, player); //save the world with the player having max hp but a spear 
                } catch (Exception e) {
                    System.out.println("Couldn't save world");
                }
                DungeonWorld.bgm.stop();
                Greenfoot.setWorld(new VillageConversation(player.getHeroType(), new VillageWorld(player.getHeroType()), true));
            }
        }
    }

    private void addButtonsToWorld(){
        addObject(play, XOFFSET, YOFFSET);
        addObject(save, XOFFSET, YOFFSET + 100);
        addObject(help, XOFFSET, YOFFSET + 200);
        addObject(exit, XOFFSET, YOFFSET + 300);
    }

    private void initButtons(){
        save = new Button(Button.SAVE, true);
        play = new Button(Button.PLAY, true);
        help = new Button(Button.HELP, true);
        exit = new Button(Button.EXIT, true);
        next = new Button(0);
    }
}
