import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Storybook here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Storybook extends SuperSmoothMover
{
    private GreenfootImage book;
    private boolean rPage = false; 
    private boolean lPage = false;
    private boolean flip = false;
    private boolean showCharacter = true; 
    private boolean showStats = true;
    private int rHideIndex = 6;
    private int lHideIndex = 3;
    
    private int rHideStatsIndex = 2; 
    private int lHideStatsIndex = 5; 
    

    //private int animIndex, animDelay, animCounter;
    private int distX, distY;
    
    private int animationIndex, animationDelay, animationCounter;
    
    private GreenfootImage rightImg[], leftImg[];
    /**
     * Act - do whatever the Storybook wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public Storybook(){
        
        //animate();
        rightImg = new GreenfootImage[7];
        leftImg = new GreenfootImage[7];
        for(int i = 0; i < rightImg.length; i++){
            rightImg[i] = new GreenfootImage("rflip" + i + ".PNG");
            leftImg[i] = new GreenfootImage("lflip" + i + ".PNG");
        }
        animationIndex = 0;
        animationDelay = 8;
        animationCounter = animationDelay;
        setImage(rightImg[0]);

    }

    public void act()
    {
        
        if (rPage == true){
            rightAnim();
        }
        
        if (lPage == true){
            leftAnim(); 
        }
  
    }

    public void pageFlipRight(boolean flipped){
        if (flipped){
            System.out.println("flipped page to the right");
            rPage = true;
            flip = flipped;
             
        }
    }

    public void pageFlipLeft(boolean flipped){
        if (flipped){
            System.out.println("flipped page to the right");
            lPage = true;
            flip = flipped;  
        } 
    }

    private void rightAnim(){
       
        if (animationCounter == 0){ // counter reaches 0 means ready for next frame
            animationCounter = animationDelay; // reset counter to max 
            animationIndex++; // this will be used to set the image to the next frame

            // If the image index has passed the last image, go back to zero
            if (animationIndex == rightImg.length){
                animationIndex = 0;
                rPage = false;
                flip = false;
                showCharacter = true;
                showStats = true; 
            }
            //change 5 later and make veriable 
            if (animationIndex == rHideIndex){
                showCharacter = false; 
            }
            
            if (animationIndex == rHideIndex + 1){
                showCharacter = true; 
                
            }
            
            if (animationIndex == rHideStatsIndex){
                showStats = false; 
                //System.out.println("not showing");
            }
            
            if (animationIndex == rHideStatsIndex + 1){
                showStats = true; 
                //System.out.println("showing");
                //flip = false;
            }
            
            // Apply new image to this Actor
            setImage (rightImg[animationIndex]);
        } else {// not ready to animate yet, still waiting
            // so just decrement the counter          
            animationCounter--;
        }
         
    }
    
    private void leftAnim(){
        if (animationCounter == 0){ // counter reaches 0 means ready for next frame
            animationCounter = animationDelay; // reset counter to max 
            animationIndex++; // this will be used to set the image to the next frame

            // If the image index has passed the last image, go back to zero
            if (animationIndex == leftImg.length){
                animationIndex = 0;
                lPage = false;
                flip = false;
                showStats = true; 
                //showCharacter = true;  
            } 
            
            //change 5 later and make veriable 
            if (animationIndex == lHideIndex){
                showCharacter = false; 
            }
            if (animationIndex == lHideIndex + 1){
                showCharacter = true;
                flip = false;
            }
            
            if (animationIndex == lHideStatsIndex){
                showStats = false;
                System.out.println("hiding");
                //flip = false;
            }
            if (animationIndex == lHideStatsIndex + 1){
                showStats = true;
                System.out.println("showing");
                //flip = true;
            }
            
            // Apply new image to this Actor
            setImage (leftImg[animationIndex]);
        } else {// not ready to animate yet, still waiting
            // so just decrement the counter          
            animationCounter--;
        }
        
    }
    
    public boolean getFlip(){
        return flip;
    }
    
    public boolean getShowCharacter(){
        return showCharacter; 
    }
    
    public boolean getShowStats(){
        return showStats; 
    }
    
}
