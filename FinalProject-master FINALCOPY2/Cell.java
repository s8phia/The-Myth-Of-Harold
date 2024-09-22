import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Creates a new cell from the map of the world
 * 
 * @author Jessica Biro
 * @version January 2024
 */
public class Cell extends Actor
{
    int x, y; 
    public static final int cellSize = 16; 
    private GreenfootImage img;
    private int diameter;
    private String id;
    private Color c;

    //booleans for which type of cell 
    private boolean isSpike = false;
    private boolean spiky = false; 
    private boolean isDoor = false;
    private boolean openDoor = false;
    private boolean isLadder = false;
    public static boolean canContinue; 
    private boolean isHole = false;
    private boolean shouldFog = true;

    //spike animations
    private GreenfootImage[] spikes = new GreenfootImage[4];
    private int animIndex, animDelay, animCounter;
    //fogging values
    private static final int VIEW_RANGE = 200 + 15;
    private static final int MIN_TRANSPARENCY = 255 - VIEW_RANGE;
    private int curTransparency;

    /**
     * Simplest constructor for a cell with only a specific color 
     * 
     * @param Color The color of the standardized cell 
     */
    public Cell(Color color){
        GreenfootImage cell = new GreenfootImage(cellSize,cellSize);
        cell.setColor(color); 
        cell.fill();
        setImage(cell);
    }

    /**
     * Constructor for a cell with a specific diameter and color 
     * 
     * @param diameter The diameter of the cell
     * @param id       The type of cell that it is
     * @param Color    The color of the cell 
     */
    public Cell(int diameter, int id, Color c) {
        this.diameter = diameter;
        this.id = Integer.toString(id);
        switch(id) {
            case 1:
                c = Color.BLACK;
                break;
            case 2:
                c = Color.YELLOW;
                break;
            default:
                c = Color.GREEN;
        }
        this.c = c;
        setImg();
    }

    /**
     * Constructor for a cell with a specific image
     * 
     * @param type The type of cell that the instance currently is 
     */
    public Cell (int type){
        switch(type){
            case DungeonWorld.WALL:
                img = new GreenfootImage("wall_mid.png");
                break;
            case DungeonWorld.FLOOR:
            case DungeonWorld.ENEMY:
            case DungeonWorld.DOOR:
            case DungeonWorld.PLAYER:
                if(type == DungeonWorld.DOOR){
                    isDoor = true;
                }
                if(Greenfoot.getRandomNumber(10)== 0){
                    if(Greenfoot.getRandomNumber(10) == 0){
                        img = new GreenfootImage("floor_spikes_anim_f0.png");
                        initSpikes();
                        isSpike = true;
                    } else{
                        img = new GreenfootImage("floor_" + (Greenfoot.getRandomNumber(7) + 1)+".png");
                    }
                } else {
                    img = new GreenfootImage("floor_1.png");
                }
                break;
            case DungeonWorld.LADDER:
                //img = new GreenfootImage("floor_ladder.png");
                if(canContinue){
                    img = new GreenfootImage("floor_ladder.png");
                } else{
                    img = new GreenfootImage("jail1.PNG");
                    img.scale(100,100);
                }
                DungeonWorld.CurrentLadder = this; 
                isLadder = true;
                break;
            case DungeonWorld.HOLE:
                img = new GreenfootImage("hole.png");
                isHole = true;
                break;
            default:
                img = new GreenfootImage(DungeonWorld.CELL_SIZE, DungeonWorld.CELL_SIZE);
                img.setColor(DungeonWorld.VOID_COLOR);
                img.fill();
        }
        //set the image transparency to the minimum to start
        img.setTransparency(MIN_TRANSPARENCY);
        setImage(img);
        //initialize the counters for spikes 
        animIndex = 0;
        animDelay = 20;
        animCounter = animDelay;
    }

    /**
     * Act - do whatever the Cell wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if(isSpike){
            animSpikes();
        } 

        //if this cell is a ladder and the mini boss was defeated
        //change floors and continue to the next level 
        if(isLadder && Greenfoot.mouseClicked(this) && canContinue){
            System.out.println("Climbed down ladder");
            DungeonWorld.changeFloors = true;
            canContinue = false;
        }

        //if this cell if a hole, once clicked you can continue
        //to the next level
        if(isHole && Greenfoot.mouseClicked(this)){
            System.out.println("Fell down hole");
            DungeonWorld.changeBoss = true;       
        }
        //different transparency for the boss fight, want to see the entire screen 
        if(DungeonWorld.bossFight){
            getImage().setTransparency(150);   
        } else if(isLadder || isHole){ //if it's a ladder or a hole set transparency to max so that you can spot the ladder 
            getImage().setTransparency(255);
        }
        //otherwise you should always fog the cells 
        else {
            fog(); 
        }
    }

    /**
     * Sets the transparency of the cells based on the current distance from the player 
     */
    private void fog() {
        ArrayList<Player> p = (ArrayList<Player>)getWorld().getObjects(Player.class);
        int dist = (int)(calculateDistanceBetween(this, p.get(0))/1.5);
        if(dist < VIEW_RANGE){
            getImage().setTransparency(255-dist);
            //curTransparency = 255-dist;
        } else {
            getImage().setTransparency(MIN_TRANSPARENCY);
            //curTransparency = MIN_TRANSPARENCY;
        }
    }

    /**
     * Helper method to calculate the distance between two actors using pythagorean theorem 
     */
    private double calculateDistanceBetween(Actor a, Actor b){
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    /**
     * Initializes the spike images 
     */
    private void initSpikes(){
        for(int i = 0; i < spikes.length; i++){
            spikes[i] = new GreenfootImage("floor_spikes_anim_f" + i + ".png");
        }
    }

    /**
     * Animates the spikes
     */
    private void animSpikes(){
        if(animCounter == 0){
            animCounter = animDelay;
            animIndex++;
            if(animIndex == spikes.length){
                animIndex = 0;
                spiky = true;
            }
        } else{
            animCounter--; 
        }
        setImage(spikes[animIndex]);
    }

    /**
     * Getter method for the isLadder boolean.
     * 
     * @return boolean Whether or not the current cell is a ladder 
     */
    public boolean isLadder(){
        return isLadder;
    }

    /**
     * Sets the image of the cell to a set standard of color and size 
     */
    private void setImg(){
        GreenfootImage image = new GreenfootImage(diameter, diameter);
        image.setColor(c);
        image.fillRect(0, 0, diameter, diameter);
        image.setColor(Color.BLACK);
        image.drawString(id, diameter / 3, diameter / 2);
        setImage(image);
    }

    /**
     * If the cell is currently a ladder, it'll be a temporary jail cell depending on the current floor, 
     * changes the image of the ladder and adds an actual jail cell on top once the mini boss has been defeated. 
     */
    public void openJailCell(){
        setImage(new GreenfootImage("floor_ladder.png"));
        getWorld().addObject(new Jail(DungeonWorld.worldLevel), getX(), getY());
        canContinue = true; 
    }
}
