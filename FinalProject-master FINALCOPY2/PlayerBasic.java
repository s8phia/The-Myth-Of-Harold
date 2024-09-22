import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * A basic version of the player class that allows the player to move around the village. 
 * 
 * @author Hilary Ho, Jessica Biro
 * @version January 2024
 */
public class PlayerBasic extends Player
{
    private int speed = 2;
    private boolean canMove = true;
    private boolean isInVillage;
    private int type;

    private int animIndex, animDelay, animCounter;
    private int distX, distY;
    private GreenfootImage img[];

    private GreenfootImage image;
    //protected int heroType;

    private int xPlayer, yPlayer;

    private Hitbox hitbox = new Hitbox(5); 
    private Hitbox hb, hb1, hb2, hb3;
    private int hbNum;
    //private int convo;

    private World w;
    private VillageConversation vc;  
    private boolean enforceBorders = false;
    private boolean moving; 
    private boolean amHero; 

    private Text text1, text2, text3; 
    public boolean musicInVillage; 

    /**
     * The village hero
     * @param type          The hero type of the player
     * @param w             The world
     * @param isInVillage   If the hero is inside the village
     */
    public PlayerBasic(int type, World w, boolean isInVillage) { //village hero
        //GreenfootImage image = new GreenfootImage(diameter, diameter);
        //image.setColor(Color.PINK);
        //image.fillOval(0, 0, diameter, diameter);
        //image.setColor(Color.BLACK);
        //setImage(image);
        super(type);
        this.type = type; 
        this.w = w;
        this.isInVillage = isInVillage; 

        moving = true;
        //musicInVillage = true;
        //isInVillage = true;
        distX = 0;
        distY = 0;

        image = new GreenfootImage("hero" + type + "idle0.png");
        image.scale(45,75);
        setImage(image); 
    }

    /**
     * The storybook hero, doesn't move just for show
     * @param type      The hero type of the player
     * @param amHero    Whether or not its the storybook hero
     */
    public PlayerBasic (int type, boolean amHero){ //storybook hero
        super(type); 
        this.type = type;
        this.amHero = amHero; 

        moving = false;
        //musicInVillage = true;
        //isInVillage = false;
        image = new GreenfootImage("hero" + type + "idle0.png");
        image.scale(150,220);
        setImage(image);  
    }

    /**
     * The stats hero, doesn't move just for show
     * @param type      The hero type of the player
     * @param amHero    Whether or not its the stats hero
     * @param scaleX    The X of the scaled image
     * @param scaleY    The Y of the scaled image
     */
    public PlayerBasic (int type, boolean amHero, int scaleX, int scaleY){ //stats hero
        super(type); 
        this.type = type;
        this.amHero = amHero; 

        moving = false; 
        //isInVillage = false;
        image = new GreenfootImage("hero" + type + "idle0.png");
        image.scale(scaleX,scaleY);
        setImage(image);  
    }

    /**
     * The merchants and the mayor in the village 
     * @param type      The type of merchant
     */
    public PlayerBasic(int type){ //merhchants
        super(type); 
        this.type = 0;
        //vc = new VillageConversation(type, convo, w,this ); 
        //this.convo = convo;
        //hb = new Hitbox(hbNum);
        //this.hbNum = hbNum;

        text1 = new Text(1); 
        text2 = new Text(2); 
        text3 = new Text(3); 

        moving = false;
        //isInVillage = true;

        image = new GreenfootImage ("mayor.PNG");
        image.scale(45, 75);
        setImage(image); 
    }

    /**
     * Animate and check hit boxes to change worlds
     */
    public void act()
    {
        if(moving){ //hero movement
            checkKeys();
            checkHitBox();
            //super.act();
            animate();
            checkWeaponPickup();

        } else if (!moving && !isInVillage && !amHero){ //checks to see village merchants
            checkPlayer(); 
        } 

        //playerLocation();
        //animate();
    }

    public void enforceBorders(boolean enforce){
        enforceBorders = enforce;
    }

    private void checkHitBox(){
        //musicInVillage = false;
        ArrayList<Hitbox> hit = (ArrayList<Hitbox>)getIntersectingObjects(Hitbox.class);
        if(hit != null){
            for(Hitbox h: hit){
                enforceBorders = false;
                if(h.getHbNum() == 1){
                    Greenfoot.setWorld(new VillageConversation(type, 1, w, this)); //mayor house 
                } 
                else if(h.getHbNum() == 2) {
                    Greenfoot.setWorld(new VillageConversation(type, 2, w, this)); //potions
                } 
                else if(h.getHbNum() == 3){
                    Greenfoot.setWorld(new VillageConversation(type, 3, w, this)); //weapons
                }
                else if(h.getHbNum() == 4){
                    //musicInVillage = false;
                    if(FileWriter.loadWorldStats(WelcomeWorld.currentFileName) != null){
                        Greenfoot.setWorld(new DungeonWorld(-1, WelcomeWorld.currentFileName, true)); 
                    } else {
                        Greenfoot.setWorld(new DungeonWorld(type, null, false)); //cave
                    }
                    VillageWorld.bgm.stop();
                } 
                else if (h.getHbNum() == 5){
                    Greenfoot.setWorld(new VillageConversation(type, 4, w, this)); //hero house
                } 
                else if (h.getHbNum() == 6){
                    Greenfoot.setWorld(new VillageWorld(type)); //hero house to village
                    this.enforceBorders(true);
                    musicInVillage = false;

                    System.out.println(musicInVillage + "yeehaw");
                }
            }
        }
    }

    public boolean allowed(int x, int y, int[] dir){
        //if the player is out of the world he's not allowed to go 
        if(x >= 1200 || y >= 800 || x<0 || y<0)
        {
            return false;
        }
        //needed to enter the speed because the borders could be overruled if the player moved quickly through them
        //checks based on the direction by adding the speed to see if it's possible to go

        for(int i = 0; i <= speed; i++)
        {
            if (dir[0] == 1 && VillageWorld.villageBoundaries[x+i][y] == 1 
            || dir[1] == 1 && VillageWorld.villageBoundaries[x-i][y] == 1 
            || dir[2] == 1 && VillageWorld.villageBoundaries[x][y+i] == 1 
            || dir[3] == 1 && VillageWorld.villageBoundaries[x][y-i] == 1)
            {
                return false;
            }
        }
        return VillageWorld.villageBoundaries[x][y] == 0;

    }

    private void moveIfAllowed(int x, int y, int[] dir)
    {
        if(enforceBorders){
            if(allowed(x,y,dir)){
                setLocation (x, y); 
            }
        } else{
            setLocation (x, y); 
        }
    }

    private void checkKeys(){
        //dist from wall
        musicInVillage = true;
        int speed = 6;
        int[] dir = {0,0,0,0};
        if (Greenfoot.isKeyDown("d")){
            dir[1] = 1;
            moveIfAllowed(getX() + speed, getY(), dir);
            distX = 1;
        }
        if (Greenfoot.isKeyDown("a")) {
            dir[0] = 1;
            moveIfAllowed(getX() - speed, getY(), dir);
            distX = -1;
        }
        if (Greenfoot.isKeyDown("w")) {
            dir[2] = 1;
            moveIfAllowed(getX(), getY() - speed, dir);
            distY = -1;
        }
        if(Greenfoot.isKeyDown("s")) {
            dir[3] = 1; 
            moveIfAllowed(getX(), getY() + speed, dir);
            distY = 1;
        }
    }

    /**
     * Set the movement of the player
     * @param lock  Can the player move or not 
     */
    public void lockMovement(boolean lock){
        canMove = lock;
    }

    /**
     * Get the movement of the player
     * @return boolean  Can the player move or not 
     */
    public boolean getMovement(){
        return canMove;
    }

    public boolean checkPlayer(){
        ArrayList<PlayerBasic> cp = (ArrayList<PlayerBasic>) getIntersectingObjects(PlayerBasic.class);
        for (PlayerBasic p : cp){
            return true;
        }
        return false;
    }

    public boolean getMusicInVillage(){
        return musicInVillage;
    }
}