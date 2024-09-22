import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Creates a new instance of the final boss <br>
 * 
 * Credits: <br>
 *   - Pathway logic: Mr. Cohen
 * 
 * @author Jessica Biro
 * @version January 2024
 */
public class Boss extends SuperSmoothMover
{
    //HP
    private int hp; 
    public static final int MAX_HP = 200;

    //Speed
    private final int MAX_SPEED = 20;
    //Pathway variables
    private Coordinate currentDestination;
    private ArrayList<Coordinate> destinations = new ArrayList<Coordinate>();

    //Images and animations
    private GreenfootImage[] idle; 
    private GreenfootImage[] charge; 
    private GreenfootImage[] jumpR; 
    private GreenfootImage[] jumpL;
    private GreenfootImage[] attack; 
    private GreenfootImage[] hit;
    private int atkframe, idleframe, jumpframe, chrgframe, hitframe;

    private int idleDel, idleSpeed; 
    private int chargeDel, chargeSpeed;
    private int jumpDel, jumpSpeed;
    private int attackDel, attackSpeed;

    //Booleans of current status 
    private boolean isCharging, isAttacking, isIdle, isJumping;
    //Act counters
    private int act; 
    private int circleFireballPhaseStart = 120; 
    private int shootAtPlayerPhaseStart = 30;
    private int jumpPhaseStart = 500;
    
    //if the boss is dead or not 
    private boolean bossDead = false; 
    private GreenfootImage tinyBoss; 
    public Boss(){
        enableStaticRotation(); //dont rotate the image of the boss 
        hp = MAX_HP; //set hp to the max when initialized
        //initialize all the images and animation variables
        initImgs();
        setImage(idle[0]);

        idleSpeed = 8; 
        idleDel = idleSpeed; 
        idleframe = 0;

        chargeSpeed = 15;
        chargeDel = chargeSpeed;
        chrgframe = 0;

        jumpSpeed = 8;
        jumpDel = jumpSpeed;
        jumpframe = 0;

        attackSpeed = 8;
        attackDel = attackSpeed;
        atkframe = 0;
        //tiny boss image for when defeated 
        tinyBoss = new GreenfootImage("boss_idle0.png");
        tinyBoss.scale(50, 50);
    }

    /**
     * Animations and attacks for the boss. If the boss is "dead", don't animate or attack. 
     */
    public void act()
    {
        if(bossDead){
            setImage(tinyBoss);
        } else {
            //Animations for different states
            if(isCharging){
                charge();
                chargeDel = Math.max (chargeDel - 1, 0);
            } else if (isJumping){
                jump();
                jumpDel = Math.max (jumpDel - 1, 0);
            } else if (isAttacking){
                attack();
                attackDel = Math.max (attackDel - 1, 0);
            }
            else {
                idle(); 
                idleDel = Math.max (idleDel - 1, 0);
            }
            act++;//act counter
            moveTowardsDestination();
            //once a certain phase has started, start the animation for it and attack 
            if(act % jumpPhaseStart == 0){
                isCharging = true;
            } else if (!isJumping){
                if(act % circleFireballPhaseStart == 0){
                    isAttacking = true; 
                    shootFireballs();
                }
                if(act % shootAtPlayerPhaseStart == 0){
                    isAttacking = true;
                    shootFireballsAtPlayer();
                }
            }

            // If I don't have a destination, and there are destinations
            // in my list, set my current destination to the next one in my list
            if (currentDestination == null && destinations.size() > 0){
                currentDestination = getNextDestination();
            } 
        }
    }
    
    /**
     * Initializes all of the image arrays for animations.
     */
    private void initImgs(){
        idle = new GreenfootImage[5]; 
        for(int i = 0; i < idle.length; i++){
            idle[i] = new GreenfootImage("boss_idle" + i + ".png"); 
            idle[i].scale(200,200); 
        }
        charge = new GreenfootImage[7]; 
        for(int i = 0; i < charge.length; i++){
            charge[i] = new GreenfootImage("boss_charge" + i + ".png"); 
            charge[i].scale(200,200); 
        }
        jumpR = new GreenfootImage[6]; 
        for(int i = 0; i < jumpR.length; i++){
            jumpR[i] = new GreenfootImage("boss_jump" + i + ".png"); 
            jumpR[i].scale(200,200); 
            jumpR[i].mirrorHorizontally();
        }
        jumpL = new GreenfootImage[6]; 
        for(int i = 0; i < jumpL.length; i++){
            jumpL[i] = new GreenfootImage("boss_jump" + i + ".png"); 
            jumpL[i].scale(200,200); 
        }
        attack = new GreenfootImage[3]; 
        for(int i = 0; i < attack.length; i++){
            attack[i] = new GreenfootImage("boss_attack" + i + ".png"); 
            attack[i].scale(200,200); 
        }
        hit = new GreenfootImage[3]; 
        for(int i = 0; i < attack.length; i++){
            hit[i] = new GreenfootImage("boss_hit" + i + ".png"); 
            hit[i].scale(200,200); 
        }
    }
    
    /**
     * Getter method that returns whether or not the boss is currently dead. 
     * @return boolean Is the boss currently dead. 
     */
    public boolean isDead(){
        return bossDead;
    }

    /**
     * Animates the boss idling 
     */
    private void idle(){
        if(idleDel == 0){
            idleframe++; 
            if(idleframe >= idle.length){
                idleframe = 0; 
            }
            idleDel = idleSpeed; 
        }
        setImage (idle[idleframe]); 
    }

    /**
     * Animates the boss charging its attack
     */
    private void charge(){
        if(chargeDel == 0){
            chrgframe++; 
            if(chrgframe >= charge.length){
                isCharging = false;
                jumpOnPlayer();
                isJumping = true;
                chrgframe = 0;
            }
            chargeDel = chargeSpeed; 
        }
        setImage (charge[chrgframe]);
    }

    /**
     * Animates the boss jumping
     */
    private void jump(){
        if(jumpDel == 0){
            jumpframe++; 
            if(jumpframe >= jumpR.length){
                ArrayList<Player> p = (ArrayList<Player>)getWorld().getObjects(Player.class);
                if(p.size() > 0 && this.isTouching(Player.class)){
                    p.get(0).getHit(15);
                }
                isJumping = false;
                jumpframe = 0;
            }
            jumpDel = jumpSpeed; 
        }
        setImage (jumpR[jumpframe]);
    }

    /**
     * Animates the boss attacking 
     */
    private void attack(){
        if(attackDel == 0){
            atkframe++; 
            if(atkframe >= attack.length){
                isAttacking = false;
                atkframe = 0;
            }
            attackDel = attackSpeed; 
        }
        setImage (attack[atkframe]);
    }

    /**
     * If the boss is currently attacking, shoot fireballs in a circle. 
     */
    private void shootFireballs()
    {
        //if currently shooting add webs (that move on their own) all around the boss starting at his core 
        if (isAttacking){
            for(int d=0; d<360; d+=10){
                getWorld().addObject(new Fireball(d), getX(), getY());
            }
        }
    }

    /**
     * Shoot fireballs directly at the player by getting the players current x and y coordinates. 
     */
    private void shootFireballsAtPlayer()
    {
        //if currently shooting get the players coordinates from their public static and target them 
        ArrayList<Player> p = (ArrayList<Player>)getWorld().getObjects(Player.class);
        if (p.size() > 0){
            getWorld().addObject(new Fireball(p.get(0).getX(), p.get(0).getY()), getX(), getY());
        }
    }

    /**
     * Jump on the player by getting the players current x and y coordinates. 
     */
    private void jumpOnPlayer(){
        ArrayList<Player> p = (ArrayList<Player>)getWorld().getObjects(Player.class);
        if (p.size() > 0){
            addDestination(new Coordinate(p.get(0).getX(), p.get(0).getY()));
        }
    }

    /**
     * Getter method that gets the final boss' current HP
     * @return int The boss' current HP
     */
    public int getHP(){
        return hp;
    }

    /**
     * Method that allows the boss to take damage based on how strong the weapon is.
     * Sets bossDead to true once the hp is below 0. 
     * @param damage How much damage the boss should take
     */
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            // Remove the boss from the world
            DungeonWorld.allEnemies.get(DungeonWorld.CURR_ROOM).remove(this);
            Poof deathEffect = new Poof();
            getWorld().addObject(deathEffect, getX(), getY());
            // Remove the actor from the world
            //getWorld().removeObject(this);
            bossDead = true; 
        }
    }

    /**
     * Mr Cohen's moveTowardsDestination method
     * Makes the actor follow a path of coordinates 
     */
    private void moveTowardsDestination() {
        if (currentDestination != null) {
            // Use method to figure out the exact distance between self and destination
            double distanceToDestination = getDistance(new Coordinate(getX(), getY()), currentDestination);

            // If I'm so close to my destination that I'm about to overshoot it, set my
            // location to the exact destination location instead of using calculated movement
            if (distanceToDestination < MAX_SPEED) {
                setLocation(currentDestination.getX(), currentDestination.getY());
                destinations.remove(currentDestination);
                currentDestination = null;
                return; //end method once current destination is set to null 
            } else {
                // Turn towards and move towards my destination
                turnTowards(currentDestination.getX(), currentDestination.getY());
                move(MAX_SPEED);
            }
        }
    }

    /**
     * Accessor to allow the World to get the Player's path to redraw it
     */
    public ArrayList<Coordinate> getPath(){
        return destinations;
    }

    /**
     * Mutator to allow the World to add a destination to the Player's path
     * (which is triggered by a mouse click in the World)
     * 
     * @param c     The Coordinate to add to the player's destinations
     */
    public void addDestination (Coordinate c){
        destinations.add(c);
    }

    /**
     * Helper method to get the next destination in the List. In a separate method
     * in case we want to add other things to happen when getting next destination
     */
    private Coordinate getNextDestination () {
        return destinations.get(0);
    }

    /**
     * Static method that gets the distance between the x,y coordinates of two Actors
     * using Pythagorean Theorum.
     *
     * @param a     First Actor
     * @param b     Second Actor
     * @return distance The distance from the center of a to the center of b.
     */
    public static double getDistance (Coordinate a, Coordinate b)
    {
        double distance;
        double xLength = a.getX() - b.getX();
        double yLength = a.getY() - b.getY();
        distance = Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
        return distance;
    }

}
