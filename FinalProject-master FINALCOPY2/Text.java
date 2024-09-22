import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Displays different text based on the text number.
 * 
 * @author Hilary Ho 
 * @version January 2024
 */
public class Text extends Actor
{
    private int num; 
    private boolean shown;
    private int type; 

    private GreenfootImage text;
    private int story = 0; 
    private VillageConversation vc;

    Button next = new Button(6);
    private SuperDisplayLabel line1, line2, job;
    private Storybook storyPage; 
    /**
     * Creates a new textbox based on the number ID 
     * @param num   Number ID of the text
     */
    public Text(int num){
        this.num = num; 
        line1 = new SuperDisplayLabel();
        line2 = new SuperDisplayLabel();
        job = new SuperDisplayLabel();

        text = new GreenfootImage("convoclickbar.PNG"); 
        setImage(text);
    }

    /**
     * Another more specific type of textbox based on the type of text and whether or not it should be hidden
     * 
     * @param type  Type of text
     * @param show  Whether or not the text should be displayed
     */
    public Text(int type, boolean shown){
        this.type = type; 
        this.shown = shown; 

    }

    public void act()
    {
        if (num == 1) {
            mayorConvo();
        } else if (num == 2){
            potionConvo();
        } else if (num == 3){
            weaponConvo();
        } else if (num == 4){
            levelOneConvo();
        }else if (num == 5){
            levelTwoConvo();
        }else if (num == 6){
            levelThreeConvo();
        }else if (num == 7){
            levelFourConvo();
        }
    }

    private void mayorConvo(){
        //clickNext();
        //getWorld().addObject(line1, 600, 400);
        //line1.update("Click NEXT to continue...");

        if (Greenfoot.mouseClicked(this)){
            //getWorld().removeObject(line1);
            getWorld().addObject(line1, 600, 400);
            getWorld().addObject(line2, 600, 430);
            getWorld().addObject(job, 600, 355);

            job.update("MAYOR:");
            //getWorld().addObject(line1, 600, 400);

            setImage(text = new GreenfootImage("convobar.PNG"));
            if (story == 0){
                line1.update("Hello brave hero!");
                line2.update("Welcome to my house!");
                //story++;
            } else if (story == 1) {
                line1.update("I heard that you were the brave");
                line2.update("hero of this village who can help...");
            } else if (story == 2) {
                line1.update("I was with my daughter, Bobette");
                line2.update("when suddenly... she disappeared!");
                //story++;
            } else if (story == 3){
                line1.update("This isn't the first time!");
                line2.update("Three other children are missing!");
            } else if (story == 4) {
                line1.update("I suspect it's that horrid monster");
                line2.update("named HAROLD, hidden in the caves!");
                //story++;
            } else if (story == 5){
                line1.update("Legend has it that he is behind the");
                line2.update("all the kidnappings");
            } else if (story == 6){
                line1.update("I even heard that he has minions on");
                line2.update("each level of the cave defending him!");
            } else if (story == 7){
                line1.update("We need you to defeat this monster"); 
                line2.update("and save my daughter");
            } else if (story == 8){
                line1.update("... and the rest of the children too");
                line2.update("... i guess");
            } else if (story == 9){
                line1.update("We need your help to save them");
                line2.update("Will you be the hero of our village?");
            } else if (story == 10){
                //removeObjects(); 
            } 
            story++;
        }
    }

    private void potionConvo(){

        if (Greenfoot.mouseClicked(this)){
            //getWorld().removeObject(line1);
            getWorld().addObject(line1, 600, 400);
            getWorld().addObject(line2, 600, 430);
            getWorld().addObject(job, 600, 355);

            job.update("POTION MERCHANT:");
            //getWorld().addObject(line1, 600, 400);

            setImage(text = new GreenfootImage("convobar.PNG"));
            if (story == 0){
                line1.update("Welcome to my POTION shop!");
                line2.update("Do you want to buy something?");
                //story++;
            } else if (story == 1) {
                line1.update("-- click NEXT if YES --");
                line2.update("-- click EXIT SHOP if NO --");
            } else if (story == 2) {

            } else if (story == 3) {
                //Greenfoot.setWorld(new WeaponShop());
            }
            story++;
        }
    }

    private void weaponConvo(){
        if (Greenfoot.mouseClicked(this)){
            //getWorld().removeObject(line1);
            getWorld().addObject(line1, 600, 400);
            getWorld().addObject(line2, 600, 430);
            getWorld().addObject(job, 600, 355);

            job.update("WEAPON MERCHANT:");
            //getWorld().addObject(line1, 600, 400);

            setImage(text = new GreenfootImage("convobar.PNG"));
            if (story == 0){
                line1.update("Welcome to my WEAPON shop!");
                line2.update("Do you want to buy something?");
                //story++;
            } else if (story == 1) {
                line1.update("-- click NEXT if YES --");
                line2.update("-- click EXIT SHOP if NO --");
            } else if (story == 2) {

            } else if (story == 3) {
                //Greenfoot.setWorld(new WeaponShop());
            }
            story++;
        }
    }

    private void levelOneConvo(){
        getWorld().addObject(line1, 300, 100);
        getWorld().addObject(line2, 300, 130);
        getWorld().addObject(job, 300, 60);
        setImage(text = new GreenfootImage("convobar.PNG"));
        if (story == 0){
            job.update("Missing Kid #1");
            line1.update("Thank you for defeating the moster!");
            line2.update("Take these coins as gratitude!");
            //story++;
        } else if (story == 1) {

        }
        //story++;
    }

    private void levelTwoConvo(){
        getWorld().addObject(line1, 300, 100);
        getWorld().addObject(line2, 300, 130);
        getWorld().addObject(job, 300, 60);
        setImage(text = new GreenfootImage("convobar.PNG"));
        if (story == 0){
            job.update("Missing Kid #2");
            line1.update("You are so brave!");
            line2.update("Keep freeing more children!");
            //story++;
        } else if (story == 1) {

        }
        //story++;
    }

    private void levelThreeConvo(){
        getWorld().addObject(line1, 300, 100);
        getWorld().addObject(line2, 300, 130);
        getWorld().addObject(job, 300, 60);
        setImage(text = new GreenfootImage("convobar.PNG"));
        if (story == 0){
            job.update("Missing Kid #3");
            line1.update("Took you long enough...");
            line2.update("I think I saw the mayor's kid below!");
            //story++;
        } else if (story == 1) {

        }
        //story++;
    }

    private void levelFourConvo(){
        if(Greenfoot.mouseClicked(this)){
            getWorld().addObject(line1, 800, 100);
            getWorld().addObject(line2, 800, 130);
            getWorld().addObject(job, 800, 60);
            setImage(text = new GreenfootImage("convobar.PNG"));
            job.update("The Mayor's Kid: Bobette");
            if (story == 0){
                line1.update("Thank you hero for saving me!");
                line2.update("My father will be so greatful!");
                //story++;
            } else if (story == 1) {
                line1.update("Now that this horrid moster is gone");
                line2.update("Our village can live in peace");
            } else if (story == 2) {
                line1.update("Not only did you save me...");
                line2.update("But you saved countless others!");
            } else if (story == 3) {
                line1.update("Thank you brave hero");
                line2.update("Your courage will be forever remembered");
            } 
            story++;
        }
    }

    /**
     * Getter method that returns the current story number. 
     * @return int  Returns the current story number.
     */
    public int getStoryNum(){
        return story; 
    }

}