import greenfoot.*;
/**
 * Poof class represents an animation effect displayed when an enemy is defeated.
 * It extends the Effect class and includes an animation of fading away.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class Poof extends Effect
{
    // Array to store the animation frames
    private GreenfootImage[] deathFrames;
    
    // Counter to keep track of the current frame
    private int frameCounter;
    
    // Total number of frames in the animation
    private static final int TOTAL_FRAMES = 3;
    
    // Delay between frame changes (in act cycles)
    private static final int FRAME_DELAY = 3;
    
    // Counter to keep track of the delay
    private int delayCounter;

    /**
     * Creates a new instance for a poof effect. 
     */
    public Poof()
    {
        // Initialize the array with your animation frames
        deathFrames = new GreenfootImage[TOTAL_FRAMES];
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            // Construct the file name based on the frame number
            String fileName = "FX052-" + i + ".png";
            deathFrames[i] = new GreenfootImage(fileName);
            deathFrames[i].scale(50, 50);
        }
        
        // Set the initial frame
        setImage(deathFrames[0]);
        
        // Initialize the frame counter and delay counter
        frameCounter = 0;
        delayCounter = 0;
    }

    public void act()
    {
        // Animate the enemy death
        animateDeath();
    }

    private void animateDeath()
    {
        // Check if it's time to change the frame
        if (delayCounter >= FRAME_DELAY) {
            // Change the image to the next frame
            setImage(deathFrames[frameCounter]);
            
            // Increment the frame counter
            frameCounter++;
            
            // Reset the delay counter
            delayCounter = 0;
        } else {
            // Increment the delay counter
            delayCounter++;
        }
        
        // Check if all frames have been displayed
        if (frameCounter >= TOTAL_FRAMES) {
            // If all frames are displayed, remove the object from the world
            getWorld().removeObject(this);
        }
    }
}
