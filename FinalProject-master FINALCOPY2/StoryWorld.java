import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Cutscene of the story. 
 * 
 * @author Hilary Ho
 * @version January 2024
 */
public class StoryWorld extends World
{
    Button skip = new Button(5);
    Button next = new Button(6);
    
    private GreenfootImage story;
    private int storyFrame = 0; 
    private GreenfootImage bkg; 
    
    private boolean finished = false; 
    private int animationIndex, animationDelay, animationCounter;
    private GreenfootImage storyImg[];
    
    private int type;
    private World w; 
    private PlayerBasic p; 
    
    public GreenfootSound bgm;
    /**
     * Constructor for objects of class StoryWorld.
     * @param type  The player type. 
     * 
     */
    public StoryWorld(int type)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1); 
        this.type = type; 
        
        bkg = new GreenfootImage("image30.PNG");
        setBackground(bkg); 
        
        addObject(skip, 1095, 75); 
        //addObject(next, 1000, 500);
        storyImg = new GreenfootImage[36];
        for(int i = 0; i < storyImg.length; i++){
            storyImg[i] = new GreenfootImage("image" + i + ".PNG");
            //storyImg.scale(1200,800);
        }
        animationIndex = 0;
        animationDelay = 22;
        animationCounter = animationDelay;
        setBackground(storyImg[0]);
        
        bgm = new GreenfootSound("Story.mp3");
        bgm.setVolume(30);
        bgm.playLoop();
    }
    /**
     * Show animation, if user pressues skip or the animation is finished, change the world. 
     */
    public void act(){
        if (Greenfoot.mouseClicked(skip)){
           Greenfoot.setWorld(new VillageConversation(type, w, true));
           bgm.stop();
           //Greenfoot.setWorld(new VillageConversation(type, 5, w, p));
        }
        
        if (!finished){
            animate();
        }
        
    }
    
    private void animate(){
        if (animationCounter == 0){ // counter reaches 0 means ready for next frame
            animationCounter = animationDelay; // reset counter to max 
            animationIndex++; // this will be used to set the image to the next frame

            // If the image index has passed the last image, go back to zero
            if (animationIndex == storyImg.length){
                animationIndex = 0;
                finished = true;
                Greenfoot.setWorld(new VillageConversation(type, w, true));
                bgm.stop();
            }
            // Apply new image to this Actor
            setBackground(storyImg[animationIndex]);
        } else {// not ready to animate yet, still waiting
            // so just decrement the counter          
            animationCounter--;
            finished = false; 
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
