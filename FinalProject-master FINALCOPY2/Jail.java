import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Displays the jail cell and trapped kids in game world 
 * 
 * @author Hilary Ho, Jessica Biro
 * @version January 2024
 */
public class Jail extends Actor
{
    private GreenfootImage img;
    private int worldLevel;
    private MiniBoss mb;
    private Text text4, text5, text6, text7;
    private SuperDisplayLabel sdl;

    private boolean mayorsDaughter = false; 

    /**
     * Constructor for all jail cell images and text based on game level.
     * 
     * @param worldLevel The world level
     */
    public Jail(int worldLevel){
        this.worldLevel = worldLevel;
        sdl = new SuperDisplayLabel();
        text4 = new Text(4);
        text5 = new Text(5);
        text6 = new Text(6); 
        text7 = new Text(7);

        mb = new MiniBoss(300, 200, worldLevel);
        switch(worldLevel){
            case 1:
                img = new GreenfootImage("jail1.PNG");
                break;
            case 2:
                img = new GreenfootImage("jail2.PNG");
                break;
            case 3:
                img = new GreenfootImage("jail3.PNG");
                break;
            default:
                img = new GreenfootImage("jail4.PNG");
        }
        img.scale(100, 100);
        setImage(img); 

        //img = new GreenfootImage("emptyjail.PNG");
        //setImage(img);
    }

    public Jail(boolean mayorsDaughter){
        sdl = new SuperDisplayLabel();
        img = new GreenfootImage("jail4.PNG");
        img.scale(100,100);
        setImage(img);
        text4 = new Text(4);
        text5 = new Text(5);
        text6 = new Text(6); 
        text7 = new Text(7);
        this.mayorsDaughter = mayorsDaughter;         
    }

    /**
     * Has a different child in jail based on the level, on the final boss the mayors daughter awaits and once the final boss is defeated the game ends. 
     */
    public void act()
    {
        if(mayorsDaughter){
            ArrayList<Boss> boss = (ArrayList<Boss>)getWorld().getObjects(Boss.class);
            if(boss.size() > 0 && boss.get(0).isDead()){ //boss is dead 
                levelFour();
            }
            if(Greenfoot.mouseClicked(text7) && text7.getStoryNum() == 4){
                getWorld().removeObjects(getWorld().getObjects(SuperDisplayLabel.class));
                getWorld().removeObject(text7);

                //the mayors daughter was saved!! 
                Greenfoot.setWorld(new EndWorld());
                getWorld().removeObject(this); 
            }
        } else {
            if (worldLevel == 1){
                levelOne();
                if(Greenfoot.mouseClicked(text4)){
                    ArrayList<Player> p = (ArrayList<Player>)getWorld().getObjects(Player.class);
                    if(p != null){
                        p.get(0).canShift = true; 
                    }
                    //getWorld().removeObject(sdl);
                    getWorld().removeObjects(getWorld().getObjects(SuperDisplayLabel.class));
                    getWorld().removeObject(text4);
                    getWorld().removeObject(this); 
                }
            } else if (worldLevel == 2){
                levelTwo();
                if(Greenfoot.mouseClicked(text5)){
                    ArrayList<Player> p = (ArrayList<Player>)getWorld().getObjects(Player.class);
                    if(p != null){
                        p.get(0).canShift = true; 
                    }
                    getWorld().removeObjects(getWorld().getObjects(SuperDisplayLabel.class));
                    getWorld().removeObject(text5);
                    getWorld().removeObject(this); 
                }
            } else if(worldLevel == 3){
                levelThree();
                if(Greenfoot.mouseClicked(text6)){
                    ArrayList<Player> p = (ArrayList<Player>)getWorld().getObjects(Player.class);
                    if(p != null){
                        p.get(0).canShift = true; 
                    }
                    getWorld().removeObjects(getWorld().getObjects(SuperDisplayLabel.class));
                    getWorld().removeObject(text6);
                    getWorld().removeObject(this); 
                }
            } 
        }
    }

    private boolean levelOne(){
        img = new GreenfootImage("free1.PNG");
        img.scale(100, 100); 
        setImage(img);

        getWorld().addObject(text4, 300, 100);
        if(Greenfoot.mouseClicked(this)){

            return true;
        }

        return false;
    }

    private boolean levelTwo(){
        img = new GreenfootImage("free2.PNG");
        img.scale(100, 100); 
        setImage(img);

        getWorld().addObject(text5, 300, 100);
        if(Greenfoot.mouseClicked(this)){

            return true;
        }

        return false;
    }

    private boolean levelThree(){
        img = new GreenfootImage("free3.PNG");
        img.scale(100, 100); 
        setImage(img);

        getWorld().addObject(text6, 300, 100);
        if(Greenfoot.mouseClicked(this)){

            return true;
        }

        return false;
    }

    private boolean levelFour(){
        img = new GreenfootImage("free4.PNG");
        img.scale(100, 100); 
        setImage(img);

        getWorld().addObject(text7, 800, 100);
        if(Greenfoot.mouseClicked(this)){
            return true;
        }

        return false;
    }
}
