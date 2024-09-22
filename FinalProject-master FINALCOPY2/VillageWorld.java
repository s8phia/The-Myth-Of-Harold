import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * VillageWorld allows the hero to walk around and buy weapons, buy potions, and talk to the mayor
 * 
 * @author Hilary Ho, Jessica Biro, Sophia Wang 
 * @version January 2024
 */
public class VillageWorld extends World
{
    private int type; 
    private PlayerBasic player; 
    Button next = new Button(6);
    
    public static int WIDTH = 1200;
    public static int HEIGHT = 800;
    public static final int CELL_SIZE = 40;

    private GreenfootImage bkg; 
    public static int [][] villageBoundaries;
    
    private World w;
    private VillageConversation vc;
    public static GreenfootSound bgm;
    private boolean played; 
    /**
     * Constructor for objects of class VillageWorld.
     * Brings the hero into the VillageWorld
     * 
     * @param type The hero type chosen 
     * 
     */
    public VillageWorld(int type)
    {    
        super(1200, 800, 1); 
        this.type = type; 

        villageBoundaries = borders();
        addObject(new Border(villageBoundaries), getWidth()/2, getHeight()/2); 
        
        player = new PlayerBasic(type,this, true); 
        player.enforceBorders(true); //enforce world borders 
        addObject(player, 625, 250);
        
        //allows hero to go to specific conversations 
        addObject(new Hitbox(1), 148, 180); 
        addObject(new Hitbox(2), 130, 455);
        addObject(new Hitbox(3), 920, 470);
        addObject(new Hitbox(4), 1090, 145);
        addObject(new Hitbox(5), 620, 180);

        vc = new VillageConversation(type, w, true);
        
        setPaintOrder(Button.class, Hitbox.class, Border.class); 

        bkg = new GreenfootImage("village.PNG");
        setBackground(bkg);
        if (bgm != null){
            bgm.stop();
            bgm = null;
        }
        bgm = new GreenfootSound("Village.mp3");
        bgm.setVolume(30); 
        bgm.playLoop();
    }
    
    /**
     * Sets the VillageWorld boundaries 
     * 
     * @return int[][] Returns the coordinates of the borders 
     */
    public int[][] borders(){
        int[][] villageBoundaries = new int [WIDTH][HEIGHT]; 

        //draws horizontal borders 
        for (int x = 55; x < 110; x++){
            villageBoundaries[x][235] = 1; 
        }
        
        for (int x = 180; x < 595; x++){
            villageBoundaries[x][235] = 1; 
        }
        
        for (int x = 640; x < 1032; x++){
            villageBoundaries[x][235] = 1; 
        }

        for (int x = 55; x < 230; x++){
            villageBoundaries[x][325] = 1; 
        }

        for (int x = 325; x < 1150; x++){
            villageBoundaries[x][325] = 1; 
        }

        for (int x = 55; x < 95; x++){
            villageBoundaries[x][525] = 1; 
        }

        for (int x = 160; x < 230; x++){
            villageBoundaries[x][525] = 1; 
        }

        for (int x = 325; x < 888; x++){
            villageBoundaries[x][535] = 1; 
        }

        for (int x = 945; x < 1150; x++){
            villageBoundaries[x][535] = 1; 
        }

        for (int x = 55; x < 990; x++){
            villageBoundaries[x][670] = 1; 
        }

        for (int x = 1084; x < 1150; x++){
            villageBoundaries[x][670] = 1; 
        }

        //draws vertical borders 
        for (int y = 235; y < 670; y++){
            villageBoundaries[55][y] = 1; 
        }

        for (int y = 190; y < 235; y++){
            villageBoundaries[1032][y] = 1; 
        }

        for (int y = 190; y < 670; y++){
            villageBoundaries[1150][y] = 1; 
        }

        for (int y = 325; y < 525; y++){
            villageBoundaries[230][y] = 1; 
        }

        for (int y = 330; y < 535; y++){
            villageBoundaries[325][y] = 1; 
        }

        for (int y = 670; y < 790; y++){
            villageBoundaries[990][y] = 1; 
        }

        for (int y = 670; y < 790; y++){
            villageBoundaries[1084][y] = 1; 
        }
        return villageBoundaries; 
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