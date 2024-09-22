import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The EndScreen is shown when the final boss is defeated
 * 
 * @author Hilary Ho
 * @version January 2024
 */
public class EndWorld extends World
{
    private GreenfootImage bkg;
    private Button back;
    /**
     * Constructor for objects of class EndWorld.
     * Displays the end screen and a button to bring player to the WelcomeWorld
     * 
     */
    public EndWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1); 
        back = new Button(6, true);
        
        addObject(back, 600, 300);
        
        bkg = new GreenfootImage("winscreen.PNG");
        setBackground(bkg);
    }
    
    /**
     * If button is clicked, user is brought back to the WelcomeWorld
     */
    public void act(){
        if (Greenfoot.mouseClicked(back)){
            Greenfoot.setWorld(new WelcomeWorld());
        }
    }
}
