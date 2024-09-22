import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Button here.
 * 
 * @author Hilary Ho, Jessica Biro
 * @version January 2024
 */
public class Button extends Actor
{
    //Button types
    public static final int SAVE = 0;
    public static final int LOAD = 1;
    public static final int PLAY = 2;
    public static final int HELP = 3;
    public static final int EXIT = 4;
    public static final int PAUSE = 5;
    public static final int BACK = 6;
    public static final int RESPAWN = 7;
    //hover on or off of the same button 
    private GreenfootImage on, off; 
    private boolean onButton; 

    private GreenfootImage[] upDownButtons; 
    private int buttonType; 
    //sounds
    private GreenfootSound[] click; 
    private int clickSI; 
    public Button(int type){
        buttonType = type; 
        //initialize on and off images 
        if(buttonType < 2){
            on = new GreenfootImage("darkArrow" + buttonType + ".PNG");
            off = new GreenfootImage("lightArrow" + buttonType + ".PNG"); 
            //right left up down (0, 1, 2, 3) 
        } else if (buttonType == 2){
            on = new GreenfootImage("select1.PNG"); 
            off = new GreenfootImage("select0.PNG");
        }
        else if (buttonType == 4){
            on = new GreenfootImage("start0.PNG"); 
            off = new GreenfootImage("start0.PNG"); 
        } else if (buttonType == 5){
            on = new GreenfootImage("skip0.PNG");
            off = new GreenfootImage("skip1.PNG");
        } else if (buttonType == 7){
            on = new GreenfootImage("buy0.PNG");
            off = new GreenfootImage("buy1.png");
        }
        else{
            on = new GreenfootImage("arrow0.png");
            off = new GreenfootImage("arrow1.png"); 
        }
        setImage(off); 
        onButton = false; 
        //initialize sounds 
        initSounds();
    }

    public Button(int type, boolean isMenuButton){
        //initialize menu button images for on and off 
        switch(type){
            case SAVE:
                on = new GreenfootImage("saveButton0.PNG");
                off = new GreenfootImage("saveButton1.PNG");
                break;
            case LOAD:
                on = new GreenfootImage("load0.PNG");
                off = new GreenfootImage("load1.PNG");
                break;
            case PLAY:
                on = new GreenfootImage("play0.PNG");
                off = new GreenfootImage("play1.PNG");
                break;
            case HELP:
                on = new GreenfootImage("help0.PNG");
                off = new GreenfootImage("help1.PNG");
                break;
            case EXIT:
                on = new GreenfootImage("exit0.PNG");
                off = new GreenfootImage("exit1.PNG");
                break;
            case PAUSE:
                on = new GreenfootImage("pause0.PNG");
                off = new GreenfootImage("pause1.PNG");
                break;
            case BACK:
                on = new GreenfootImage("back0.PNG");
                off = new GreenfootImage("back1.PNG");
                break;
            case RESPAWN:
                on = new GreenfootImage("respawn0.PNG");
                off = new GreenfootImage("respawn1.PNG");
            default:

        }
        //initializeSounds
        initSounds();
    }

    /**
     * Check if the mouse if hovering the button, if the button is clicked, play the sound. 
     */
    public void act(){
        buttonHover(); 
        if (Greenfoot.mouseClicked(this)){
            sounds();
        }
    }
    
    /**
     * Initialize all the sounds. 
     */
    private void initSounds(){
        //initialize sounds 
        click = new GreenfootSound[20];
        for (int i = 0; i < click.length; i++){
            click[i] = new GreenfootSound("click.wav");
            click[i].setVolume(85);
        }
        clickSI = 0;
    }

    private void buttonHover(){
        if(Greenfoot.mouseMoved(this)){
            onButton = true; 
        }
        else if(Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this)){
            onButton = false; 
            //only false if mouse movement is currently null and the mouse isn't on the button 
        }
        //changes between two different images when on and off the button to show that it's "active" and that you can press it
        if(onButton){
            setImage(on); 
        }
        if(!onButton){
            setImage(off); 
        }
    }
    
    private void sounds(){
        click[clickSI].play();
        clickSI++;
        if(clickSI == click.length){
            clickSI = 0; 
        }
    }
}
