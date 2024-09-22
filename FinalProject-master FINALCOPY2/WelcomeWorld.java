import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * STORYLINE: <br>
 * Oh no! The Mayor's child has been kidnapped?! Recently, a lot of children from the village has been disappearing, and locals suspect that an ancient frog
 * Harold has been taking them! No one has the guts to go into the dungeon to save the children though...excpect for YOU. Defeat the enemies in the dungeon
 * to discover the myths of Harold!!
 * <br>
* 
 * FEATURES: <br>
 * - Press "m" ---> show map
 * - automatic and randomized generation of world maps
 * - different enemy looks and minibosses in different world levels
 * - potion and weapon shop
 * - Harold the frog (mega boss)
 * - Jail cells that hold the children
 * - different weapons
 * <br>
 * 
 * GRAPHIC CREDITS: <br>
 * - Majority of visuals hand-drawn by Hilary <br>
 * - Village tileset, final boss, and attack objects taken from NinjaAdventure pack by pixel-boy on Itch.io: https://pixel-boy.itch.io/ninja-adventure-asset-pack <br>
 * - Dungeon world tileset and enemies from 0x72 on itch.io: https://0x72.itch.io/dungeontileset-ii <br>
 * 
 * SOUND CREDITS: <br>
 * - all taken from NinjaAdventure pack by pixel-boy on Itch.io: https://pixel-boy.itch.io/ninja-adventure-asset-pack <br>
 * 
 * 
 * KNOWN BUGS: <br>
 * - Might spawn inside of a wall 
 * -
 * <br>
 * 
 * /
 
/ * * <br> 
 * 
 * 
 * @author Hilary Ho and Jessica Biro
 * @version January 2024
 */
public class WelcomeWorld extends World
{
    //Buttons
    Button play = new Button(Button.PLAY, true); 
    Button load = new Button(Button.LOAD, true);
    Button back = new Button(Button.BACK, true);
    //loaded games
    private GreenfootImage bkg, loadScreen;
    private Textbox savedGame1, savedGame2, savedGame3;
    public static final String fileName1 = "savedGame1.txt";
    public static final String fileName2 = "savedGame2.txt";
    public static final String fileName3 = "savedGame3.txt";

    public static String currentFileName = "";
    private GreenfootSound bgm;
    private int LENGTH = 550;  
    /**
     * Constructor for objects of class WelcomeWorld.
     * 
     */
    public WelcomeWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1); 
        addObject(play, getWidth()/2, LENGTH);
        addObject(load, getWidth()/2, LENGTH + 100);
        bkg = new GreenfootImage("startscreen.PNG");
        loadScreen = new GreenfootImage("loadgamebkg.PNG");
        setBackground(bkg);

        bgm = new GreenfootSound("Adventure Begin.mp3");
        bgm.setVolume(60);
        //bgm.playLoop();
        
        //reset static variables
        Player.coinCount = 0;
        Player.weaponType = Weapon.SPEAR;
    }

    public void act(){
        if(Greenfoot.mouseClicked(load) || Greenfoot.mouseClicked(play)){
            removeObjects(getObjects(Actor.class));
            addObject(back, 1050, 720);
            setBackground(loadScreen);
            addSavedGames();
            addStats();
        }
        /*
        if(Greenfoot.mouseClicked(play)){
            bgm.stop();
            if(FileWriter.loadWorldStats(fileName1) == null){
                currentFileName = fileName1;
            } else if(FileWriter.loadWorldStats(fileName2) == null){
                currentFileName = fileName2;
            } else if(FileWriter.loadWorldStats(fileName3) == null){
                currentFileName = fileName3;
            } else{
                currentFileName = fileName1;
                FileWriter f = new FileWriter();
                try {
                    f.clearFile(fileName1);
                } catch (Exception e) {
                    System.out.println("Couldn't clear file");
                }
            }
            Greenfoot.setWorld(new CharacterSelection());
        } */
        if(Greenfoot.mouseClicked(back)){
            removeObjects(getObjects(Actor.class));
            setBackground(bkg);
            addButtons();
        }
        
        //load or start a new game in whichever game the user chose
        //start a new game is that save is currently empty 
        if(Greenfoot.mouseClicked(savedGame1)){
            currentFileName = fileName1;
            bgm.stop();
            if(FileWriter.loadWorldStats(fileName1) != null){
                Greenfoot.setWorld(new DungeonWorld(-1, fileName1, true));    
            } else {
                Greenfoot.setWorld(new CharacterSelection());
            }
        }
        if(Greenfoot.mouseClicked(savedGame2)){
            currentFileName = fileName2;
            bgm.stop();
            if(FileWriter.loadWorldStats(fileName2) != null){
                Greenfoot.setWorld(new DungeonWorld(-1, fileName2, true));    
            } else {
                Greenfoot.setWorld(new CharacterSelection());
            }
        }
        if(Greenfoot.mouseClicked(savedGame3)){
            currentFileName = fileName3;
            bgm.stop();
            if(FileWriter.loadWorldStats(fileName3) != null){
                Greenfoot.setWorld(new DungeonWorld(-1, fileName3, true));    
            } else {
                Greenfoot.setWorld(new CharacterSelection());
            }
        }
    }

    /**
     * Adds the play and load buttons to the world. 
     */
    private void addButtons(){
        addObject(play, getWidth()/2, LENGTH);
        addObject(load, getWidth()/2, LENGTH + 100);
    }

    /**
     * Adds the saved games images 
     */
    
    private void addSavedGames(){
        savedGame1 = new Textbox(new GreenfootImage("save1.PNG"));
        savedGame2 = new Textbox(new GreenfootImage("save2.PNG"));
        savedGame3 = new Textbox(new GreenfootImage("save3.PNG"));

        addObject(savedGame1, 600, 275);
        addObject(savedGame2, 600, 275 + 150);
        addObject(savedGame3, 600, 275 + 300);
    }

    /**
     * Loads all the statistics from the saved files to show which game the user wants to load. 
     */
    private void addStats(){
        int[] stats1, stats2, stats3;
        stats1 = FileWriter.loadWorldStats(fileName1);
        stats2 = FileWriter.loadWorldStats(fileName2);
        stats3 = FileWriter.loadWorldStats(fileName3);
        if(stats1 != null && stats1.length > 0){
            addObject(new PlayerBasic(stats1[0], true, 60, 110), savedGame1.getX() - 215, savedGame1.getY() - 10);
            addObject(new WeaponBasic(stats1[1]), savedGame1.getX() + 180, savedGame1.getY());            
            savedGame1.getImage().setFont(new Font("Marlboro", 30));
            savedGame1.getImage().setColor(HpBar.brown);
            savedGame1.getImage().drawString(stats1[2] + " ", 200, 140); //coins
            savedGame1.getImage().drawString(stats1[3] + " ", 280, 100); //world level
        } else {
            savedGame1.getImage().setFont(new Font("Marlboro", 30));
            savedGame1.getImage().setColor(HpBar.brown);
            
            savedGame1.getImage().drawString("N/A", 450, 100); // weapon
            savedGame1.getImage().drawString("N/A", 200, 140); //coins
            savedGame1.getImage().drawString("N/A", 280, 100); //world level
        }
        if(stats2 != null && stats2.length > 0){
            addObject(new PlayerBasic(stats2[0], true, 60, 110), savedGame2.getX() - 215, savedGame2.getY() - 10);
            addObject(new WeaponBasic(stats2[1]), savedGame2.getX() + 180, savedGame2.getY());            
            savedGame2.getImage().setFont(new Font("Marlboro", 30));
            savedGame2.getImage().setColor(HpBar.brown);
            savedGame2.getImage().drawString(stats2[2] + " ", 200, 140); //coins
            savedGame2.getImage().drawString(stats2[3] + " ", 280, 100); //world level
        } else {
            savedGame2.getImage().setFont(new Font("Marlboro", 30));
            savedGame2.getImage().setColor(HpBar.brown);
            
            savedGame2.getImage().drawString("N/A", 450, 100); // weapon
            savedGame2.getImage().drawString("N/A", 200, 140); //coins
            savedGame2.getImage().drawString("N/A", 280, 100); //world level
        }
        if(stats3 != null && stats3.length > 0){
            addObject(new PlayerBasic(stats3[0], true, 60, 110), savedGame3.getX() - 215, savedGame3.getY() - 10);
            addObject(new WeaponBasic(stats3[1]), savedGame3.getX() + 180, savedGame3.getY());            
            savedGame3.getImage().setFont(new Font("Marlboro", 30));
            savedGame3.getImage().setColor(HpBar.brown);
            savedGame3.getImage().drawString(stats1[2] + " ", 200, 140); //coins
            savedGame3.getImage().drawString(stats1[3] + " ", 280, 100); //world level
        } else{
            savedGame3.getImage().setFont(new Font("Marlboro", 30));
            savedGame3.getImage().setColor(HpBar.brown);
            
            savedGame3.getImage().drawString("N/A", 450, 100); // weapon
            savedGame3.getImage().drawString("N/A", 200, 140); //coins
            savedGame3.getImage().drawString("N/A", 280, 100); //world level
        }
    }

    /**
     * Starts and loops background music 
     */
    public void started () {
        if (bgm != null){
            bgm.playLoop();
        }
    }

    /**
     * Stops music after simulation has paused 
     */
    public void stopped () {
        if (bgm != null){
            bgm.pause();
        }
    }
}
