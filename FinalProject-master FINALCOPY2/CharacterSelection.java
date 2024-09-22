import greenfoot.*; 

/**
 * Brings user to choose their hero for the gameplay 
 * 
 * @author Hilary Ho, Jessica Biro
 * @version January 2024
 */
public class CharacterSelection extends World {
    private int currentHeroType = 1;
    private int numberOfHeroes = 10;

    //public static final int WIDTH = 900;
    public static final int LENGTH = 570;

    private GreenfootImage bkg; 
    Storybook story = new Storybook(); 

    //buttons
    Button right = new Button(0);
    Button left = new Button(1); 
    Button select = new Button(2); 
    
    private GreenfootSound bgm;
    
    /**
     * Constructor for objects of class CharacterSelection
     * Initializes the background images, music, character selections, and storybook 
     */
    public CharacterSelection() {
        super(1200, 800, 1);

        showHero(currentHeroType);
        // Add next and back button
        addObject(right, 950, LENGTH);
        addObject(left, 250, LENGTH);
        addObject(select, 380, 450);
        addObject(story, getWidth()/2, getHeight()/2); 
        bkg = new GreenfootImage ("storybkg.PNG");
        setBackground(bkg);
        setPaintOrder(PlayerStats.class,PlayerBasic.class, Button.class, Storybook.class);
        
        bgm = new GreenfootSound("Peaceful.mp3");
        bgm.setVolume(30);
        bgm.play();
    }

    /**
     * When respective buttons are clicked, they can either select hero, or look at other hero options
     */
    public void act(){

        if (Greenfoot.mouseClicked(select)){ //selects hero
            System.out.println("selected!"); 
            Greenfoot.setWorld(new StoryWorld(currentHeroType)); 
            bgm.stop();
        }

        if (Greenfoot.mouseClicked(right)){ //flips forward to next hero 
            story.pageFlipRight(true);
            System.out.println("right");

            if (currentHeroType == 10){ //loops back to the beginning
                currentHeroType = 0; 
            }
            currentHeroType++; 
            System.out.println(currentHeroType);
        }

        if (Greenfoot.mouseClicked(left)){ //flips back to last hero 
            story.pageFlipLeft(true);
            System.out.println("left"); 
            if (currentHeroType == 1){ //loops back to the end
                currentHeroType = 11; 
            }
            currentHeroType--; 
            System.out.println(currentHeroType); 
        }

        if(!(story.getFlip()) && !(getObjects(PlayerBasic.class).size() > 0)){ //displays hero when page is displayed
            showHero(currentHeroType);
        } else if (!story.getShowCharacter()){ //hides hero when page is flipped
            hideHero(); 
        } 
        if(story.getShowStats() && !(getObjects(PlayerStats.class).size() > 0)){ //displays hero stats when page is displayed
            showHeroStats(currentHeroType);
            
        } else if (!(story.getShowStats())){ //hides hero when page is flipped
            hideStats();
            System.out.println(story.getShowStats());
        }

    }

    private void showHero(int heroType){
        // Add the selected hero
        addObject(new PlayerBasic(heroType, true), 390, 280);

    }

    private void hideHero(){
        // Clear previous heroes
        removeObjects(getObjects(PlayerBasic.class));

    }

    private void showHeroStats(int heroType){
        //adds and displays hero stats 
        addObject(new PlayerStats(heroType), 830, 330); 

    }

    private void hideStats(){
        //clears and hides hero stats 
        removeObjects(getObjects(PlayerStats.class)); 
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