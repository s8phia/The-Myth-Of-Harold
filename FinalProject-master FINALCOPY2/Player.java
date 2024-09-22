import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * The Player class represents the main character in the game.
 * It is responsible for handling player movement, animation, health,
 * weapon interactions, and other player-related functionalities.
 * 
 * @author Sophia Wang and Jessica Biro
 * @version January 2024
 * 
 */
public class Player extends SuperSmoothMover
{   
    //player's speed
    private int originalSpeed, speed;
    private boolean isSlowed = false;

    //hit logic + hit image
    public static final int MAX_HP = 100;
    private int hp = MAX_HP;
    private boolean isHit = false;
    private int hitDuration = 30; 
    private int hitCounter = 0;
    private GreenfootImage hitImage;

    //animation logic
    private boolean canMove = true;
    private boolean facingRight = false;
    public boolean allowed;
    public boolean canShift = true;
    private int animIndex, animDelay, animCounter, frameCount;
    private int distX, distY;
    private int playerX, playerY;
    private GreenfootImage img[];
    private GreenfootImage image;
    protected int heroType;
    protected GreenfootImage[] animationFrames;
    protected GreenfootImage[] flippedAnimationFrames;
    protected GreenfootImage[] idleAnimationFrames;
    protected GreenfootImage[] flippedIdleAnimationFrames;

    //weapon
    public static int weaponType; 
    protected Weapon weapon; 
    public static int coinCount;

    private GreenfootSound walk;
    boolean moved = false; 
    public Player(int type) {
        super();

        heroType = type;

        distX = 0;
        distY = 0;

        //initialize animation variables
        originalSpeed = 8;
        speed = originalSpeed;
        frameCount = 4;

        animationFrames = new GreenfootImage[frameCount];
        flippedAnimationFrames = new GreenfootImage[frameCount];
        idleAnimationFrames = new GreenfootImage[3]; //frameCount??
        flippedIdleAnimationFrames = new GreenfootImage[3];
        animDelay = 4;
        animCounter = 0;

        //initialize images
        hitImage = new GreenfootImage("hero" + type + "hit" + ".png");
        hitImage.scale(35,60);

        for (int i = 0; i < frameCount; i++) {
            animationFrames[i] = new GreenfootImage("hero" + type + "run" + i + ".png");
            animationFrames[i].scale(35, 60);
        }

        for(int i = 0; i < frameCount; i++){
            flippedAnimationFrames[i] = new GreenfootImage("hero" + type + "run" + i + ".png");
            flippedAnimationFrames[i].mirrorHorizontally();
            flippedAnimationFrames[i].scale(35, 60);
        }

        for (int i = 0; i < 3; i++){
            idleAnimationFrames[i] = new GreenfootImage("hero" + type + "idle" + i + ".png");
            idleAnimationFrames[i].scale(35, 60);
        }

        for (int i = 0; i < 3; i++){
            flippedIdleAnimationFrames[i] = new GreenfootImage("hero" + type + "idle" + i + ".png");
            flippedIdleAnimationFrames[i].mirrorHorizontally();
            flippedIdleAnimationFrames[i].scale(35, 60);
        }

        setImage(animationFrames[0]);
        weapon = null; //starts without a weapon

        allowed = false;

        //sounds
        walk = new GreenfootSound("dirtrun.wav");
        walk.setVolume(60);
    }

    /**
     * Check collsions and movement. 
     */
    public void act()
    {
        checkKeys();
        //checkLadder();
        animate();
        checkWeaponPickup();
        checkWeaponLoad();
        checkFireballCollision();
        checkPlantballCollision();
        checkRockballCollision();
        checkGooCollision();
        checkDeath();
        playerX = getX();
        playerY = getY();
        if (isHit) {
            showHitImage();
        }
    }

    private void checkDeath(){
        if(hp <= 0){
            Greenfoot.setWorld(new PauseWorld(null, this, true));
        }
    }

    private void checkWeaponLoad(){
        if(this.weapon != null && this.weapon.getWorld() == null){
            getWorld().addObject(this.weapon, getX(), getY());
        }
    }

    /**
     * Gets the type of the currently equipped weapon.
     *
     * @return The weapon type identifier.
     */
    public int getWeaponType(){
        return weaponType;
    }

    /**
     * Gets the current X-coordinate of the player's position.
     *
     * @return The X-coordinate of the player.
     */
    public int getPlayerX() {
        return playerX;
    }

    /**
     * Gets the current Y-coordinate of the player's position.
     *
     * @return The Y-coordinate of the player.
     */
    public int getPlayerY() {
        return playerY;
    }

    private void checkKeys(){
        //dist from wall
        int n = 5;
        int[] dir = {0,0,0,0};
        int[] cords = DungeonWorld.worldToMap(getX() + DungeonWorld.CELL_SIZE/2, getY() + DungeonWorld.CELL_SIZE/2);
        if(canMove){
            if(Greenfoot.isKeyDown("d") && getX() + speed >= (getWorld().getWidth() - getWorld().getWidth() / n) 
            && allowed(cords[0], cords[1], new int[]{0,1,0,0})
            && DungeonWorld.WINDOW_X + DungeonWorld.WINDOW_WIDTH < DungeonWorld.WIDTH
            && canShift) {
                ((DungeonWorld)getWorld()).shiftWindow(0);
            } else if (Greenfoot.isKeyDown("d")){
                dir[1] = 1;
                facingRight = false;
                moveIfAllowed (getX() + speed, getY(), dir);
                //setLocation(getX() + speed, getY());
                distX = 1;
            }
            if(Greenfoot.isKeyDown("a") && getX() <= (getWorld().getWidth() / n) 
            && allowed(cords[0], cords[1], new int[]{1,0,0,0})
            && DungeonWorld.WINDOW_X > 0
            && canShift) {
                ((DungeonWorld)getWorld()).shiftWindow(1);
            } else if (Greenfoot.isKeyDown("a")) {
                dir[0] = 1;
                facingRight = true;
                moveIfAllowed(getX() - speed, getY(), dir);
                //setLocation(getX() - speed, getY());
                distX = -1;
            }
            if(Greenfoot.isKeyDown("w") && getY() <= (getWorld().getHeight() / n) 
            && allowed(cords[0], cords[1], new int[]{0,0,1,0})
            && DungeonWorld.WINDOW_Y > 0
            && canShift) {
                ((DungeonWorld)getWorld()).shiftWindow(2);
            } else if (Greenfoot.isKeyDown("w")) {
                dir[2] = 1;
                moveIfAllowed(getX(), getY() - speed, dir);
                //setLocation(getX(), getY() - speed);
                distY = -1;
            }
            if(Greenfoot.isKeyDown("s") && getY() >= (getWorld().getHeight() - getWorld().getHeight() / n) 
            && allowed(cords[0], cords[1], new int[]{0,0,0,1})
            && DungeonWorld.WINDOW_Y + DungeonWorld.WINDOW_HEIGHT < DungeonWorld.HEIGHT
            && canShift) {
                ((DungeonWorld)getWorld()).shiftWindow(3);
            } else if(Greenfoot.isKeyDown("s")) {
                dir[3] = 1;
                moveIfAllowed(getX(), getY() + speed, dir);
                //setLocation(getX(), getY() + speed);
                distY = 1;
            }
        }

    }

    /**
     * Sets the ability of the player to move.
     * 
     * @param can True to allow movement, false to restrict movement.
     */
    public void canMove(boolean can){
        canMove = can;
    }

    /**
     * Checks whether the player is allowed to move in the specified direction.
     *
     * @param x The X-coordinate of the player's position.
     * @param y The Y-coordinate of the player's position.
     * @param dir An array indicating the direction in which the player intends to move.
     * @return True if the player is allowed to move, false otherwise.
     * 
     * Author: - Jessica Biro
     */
    public boolean allowed(int x, int y, int[] dir){
        for(int i = 1; i <= Math.max(1,speed/DungeonWorld.CELL_SIZE + 1); i++)
        {
            if (dir[0] == 1 && (DungeonWorld.map[y][x-i] == DungeonWorld.WALL)
            || dir[1] == 1 && (DungeonWorld.map[y][x+i] == DungeonWorld.WALL)
            || dir[2] == 1 && (DungeonWorld.map[y-i][x] == DungeonWorld.WALL)
            || dir[3] == 1 && (DungeonWorld.map[y+i][x] == DungeonWorld.WALL))
            {
                allowed = false;
                return false;
            }
        }

        allowed = true;
        return true;
    }

    /**
     * Moves the player if allowed in the specified direction.
     *
     * @param x The new X-coordinate for the player.
     * @param y The new Y-coordinate for the player.
     * @param dir An array indicating the direction in which the player intends to move.
     * 
     * Author: - Jessica Biro
     */
    private void moveIfAllowed(int x, int y, int[] dir)
    {
        int[] cords = DungeonWorld.worldToMap(getX() + DungeonWorld.CELL_SIZE/2, getY() + DungeonWorld.CELL_SIZE/2);
        //int[] window = DungeonWorld.win
        boolean allowed = allowed(cords[0], cords[1], dir);
        //System.out.println(allowed);
        //System.out.println(cords[0] + " " + cords[1]);
        //boolean allowed = true;
        if (allowed){
            setLocation (x, y);
        }
    }

    /**
     * Locks or unlocks the player's movement based on the provided flag.
     *
     * @param lock True to lock movement, false to allow movement.
     */
    public void lockMovement(boolean lock){
        canMove = lock;
    }

    /**
     * Checks whether the player is allowed to move.
     * 
     * @return True if the player is allowed to move, false otherwise.
     */
    public boolean getMovement(){
        return canMove;
    }

    protected void animate()
    {
        if (Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("d")) {
            if (animCounter % animDelay == 0) {
                animIndex = (animIndex + 1) % animationFrames.length;
                setImage(animationFrames[animIndex]);
            }
            walk.play();
            animCounter++;
        }
        if(Greenfoot.isKeyDown("a")){
            if (animCounter % animDelay == 0) {
                animIndex = (animIndex + 1) % flippedAnimationFrames.length;
                setImage(flippedAnimationFrames[animIndex]);
            }
            walk.play();
            animCounter++;
        }
        else{
            if(facingRight){
                if (animCounter % animDelay == 0) {
                    animIndex = (animIndex + 1) % flippedIdleAnimationFrames.length;
                    setImage(flippedIdleAnimationFrames[animIndex]);
                }
                animCounter++;
            }
            else{
                if (animCounter % animDelay == 0) {
                    animIndex = (animIndex + 1) % idleAnimationFrames.length;
                    setImage(idleAnimationFrames[animIndex]);
                }
                animCounter++;
            }
        }
    }

    /**
     * Processes the player being hit by an attack, deducts health points, and displays a hit image.
     * 
     * @param damage The amount of damage inflicted on the player.
     */
    public void getHit(int damage) {
        if(!isHit){
            hp = hp - damage;
            isHit = true;
            setImage(hitImage);
        }
    }

    private void showHitImage() {
        hitCounter++;
        if (hitCounter >= hitDuration) {
            // Hit duration has passed, revert to normal animation frames
            isHit = false;
            hitCounter = 0;
            setImage(animationFrames[animIndex]);
        } else if (Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("d")) {
            // Player is moving, revert to normal animation frames
            isHit = false;
            hitCounter = 0;
            setImage(animationFrames[animIndex]);
        } else {
            // Hit duration hasn't passed and player is not moving, continue showing hit image
            setImage(hitImage);
        }
    }

    /**
     * Gets the X-coordinate of the cell that the player is currently in.
     * 
     * @return The X-coordinate of the cell.
     */
    private int getCellx(){
        return -1;
    }

    /**
     * Gets the Y-coordinate of the cell that the player is currently in.
     * 
     * @return The Y-coordinate of the cell.
     */
    private int getCelly(){

        return -1;
    }

    protected void checkWeaponPickup()
    {
        // Check if there is a Weapon object at the player's current location
        Weapon nearbyWeapon = (Weapon) getOneIntersectingObject(Weapon.class);

        if (nearbyWeapon != null && Greenfoot.isKeyDown("space"))
        {
            // Pick up the weapon
            pickUpWeapon(nearbyWeapon);
        }

        // Update the weapon's position relative to the player
        updateWeaponPosition();
    }

    private void pickUpWeapon(Weapon weapon)
    {
        // Attach the weapon to the player
        this.weapon = weapon;
        weaponType = weapon.getType();
    }

    /**
     * Sets the weapon type of the player if the player was loaded in.
     * @param type  The type of weapon the player has. 
     */
    public void setWeapon(int type)
    {
        // Attach the weapon to the player
        switch(type){
            case Weapon.STAFF:
                this.weapon = new Staff();
                break;
            case Weapon.SWORD:
                this.weapon = new Sword();
                break;
            case Weapon.GOLDSWORD:
                this.weapon = new GoldSword();
            case Weapon.REDSTAFF:
                this.weapon = new RedStaff();
            default:
                this.weapon = new Spear();
        }
        weaponType = type;
    }

    private void updateWeaponPosition() {
        if (weapon != null) {
            // Set the weapon's position relative to the player
            int xOffset;
            int yOffset = 0;

            if (facingRight) {
                xOffset = -18;

                // weapon.setRotation(-10); 
            } else {
                xOffset = 18;
                // weapon.setRotation(10); 
            }

            weapon.setLocation(getX() + xOffset, getY() + yOffset);
        }
    }

    /**
     * Returns if the player is facing right. 
     * @return boolean  Whether or not the play is facing right. 
     */
    public boolean isFacingRight() {
        return facingRight;
    }

    private void checkFireballCollision() {
        Fireball fireball = (Fireball) getOneIntersectingObject(Fireball.class);

        if (fireball != null) {
            getHit(1); 
            getWorld().removeObject(fireball); 
        }
    }

    private void checkPlantballCollision() {
        Plantball plantball = (Plantball) getOneIntersectingObject(Plantball.class);

        if (plantball != null) {
            getHit(2); 
            getWorld().removeObject(plantball); 
        }
    }

    private void checkRockballCollision() {
        Rockball rockball = (Rockball) getOneIntersectingObject(Rockball.class);

        if (rockball != null) {
            getHit(3); 
            getWorld().removeObject(rockball); 
        }
    }

    private void checkGooCollision() {
        Goo goo = (Goo) getOneIntersectingObject(Goo.class);

        if (goo != null) {
            // Apply slowdown effect
            if (!isSlowed) {
                speed =2; // Reduce the player's speed to half
                isSlowed = true;
            }
        } else {
            // Reset speed when not in contact with goo
            if (isSlowed) {
                speed = originalSpeed; // Restore the original speed
                isSlowed = false;
            }
        }
    }

    /**
     * Increments the player's coins by one.
     */
    public void incrementCoins() {
        coinCount++;
        System.out.println("Coins:" + coinCount);
    }

    /**
     * Decrements the player's coins by the specified amount.
     *
     * @param cost The amount by which to decrement the player's coins.
     */
    public void decrementCoins(int cost) {
        coinCount -= cost;
        System.out.println ("bought:" + coinCount);
    }

    /**
     * Set the players coin amount of player was loaded in. 
     * @param coins     How many coins to give the player. 
     */
    public void setCoins(int coins){
        coinCount = coins;
    }
    
    /**
     * Increments the player's hp by one.
     */
    public void incrementHp() {
        hp++;
    }

    /**
     * Sets the player's health points (HP) to the specified value.
     *
     * @param hp The new value for the player's HP.
     */
    public void setHP(int hp){
        this.hp = hp;
    }

    /**
     * Gets the current coin count of the player.
     *
     * @return The current coin count.
     */
    public int getCoinCount(){
        return coinCount;
    }

    /**
     * Increments the player's health points (HP) by the specified amount.
     *
     * @param addHp The amount by which to increment the player's HP.
     */
    public void incrementHp(int addHp) {
        hp += addHp;
    }

    /**
     * Gets the current health points (HP) of the player.
     *
     * @return The current HP of the player.
     */
    public int getPlayerHP(){
        return hp;
    }

    /**
     * Gets the hero type identifier of the player.
     *
     * @return The hero type identifier.
     */
    public int getHeroType(){
        return heroType;
    }

    /**
     * Gets the weapon currently equipped by the player.
     *
     * @return The weapon object currently equipped by the player.
     */
    public Weapon getWeapon() {
        return this.weapon;
    }
}
