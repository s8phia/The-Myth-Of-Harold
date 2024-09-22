import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * The dungeon world, where the  magic happens. 
 * 
 * @author Jessica Biro
 * @version January 2024
 */
public class DungeonWorld extends World
{
    // Background
    private GreenfootImage background;
    public static GreenfootSound bgm;
    public static int type;
    private Button pause;
    public static final int PAUSEX = 1130;
    public static final int PAUSEY = 750;

    //Torches
    public static ArrayList<Cell> torches;

    // Minimap
    private MiniMap miniMap;
    public static final int MINIMAP_X = 1100;
    public static final int MINIMAP_Y = 725;

    public static Color VOID_COLOR = new Color(50, 38, 79);
    //World size
    public static int WIDTH;
    public static int HEIGHT;
    //Rooms 
    public static int ROOM_MIN_SIZE;
    public static int ROOM_MAX_SIZE;
    public static final int MAX_ROOMS = 10;
    public static final int MIN_ROOMS = 4;
    public static final int CELL_SIZE = 25;
    public static int[][] map;
    public static int eX, eY;
    private static PathFinder pathFinder;
    
    public static ArrayList<Room> rooms;
    public static int CURR_ROOM;
    public static int BOSS_ROOM;

    // 2d array coordinate types
    public static final int HOLE = 7;
    public static final int LADDER = 6;
    public static final int DOOR = 5;
    public static final int ENEMY = 4;
    public static final int PLAYER = 3;
    public static final int FLOOR = 2;
    public static final int WALL = 1;
    public static final int VOID = 0;
    

    //player
    public Player player;
    public static int playerCoins = -1;
    public static int playerHp = -1;
    public static int playerWeaponType = -1;
    public static int playerX = -1;
    public static int playerY = -1;

    //health and stat bars
    public static HpBar hpBar;
    public HpBar bossHpBar;
    public static final int HPBARX = 230;
    public static final int HPBARY = 740;

    public static final int BOSSHPBARX = 955;
    public static final int BOSSHPBARY = 50;

    public static final int SUPERSTATBARX = 250;
    public static final int SUPERSTATBARY = 755;

    // Window
    public static int WINDOW_X = 0;
    public static int WINDOW_Y = 0;
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;
    private static int SHIFT = 10;

    //Enemies
    public static ArrayList<ArrayList<Enemy>> allEnemies;
    private static ArrayList<Cell> visibleEnemies = new ArrayList<Cell>();;
    private static int enemyCounter;
    private static final int ENEMY_SPEED = 5; //how fast move towards location
    private static ArrayList<PathFinder> pathFinders; //one per room    

    //world level
    public static int worldLevel;

    public static boolean changeFloors = false;

    //Final boss
    public static boolean changeBoss = false;
    public static final int MAX_WAVES = 10;
    public static boolean bossFight = false;
    public static boolean bossShift = false;
    private Boss boss = new Boss(); 
    
    //Mini boss
    public static boolean miniBossDefeated = false; 

    public static Cell CurrentLadder = null; 
    /**
     * Constructor for objects of class DungeonWorld.
     * 
     * @param type The type of player
     */
    public DungeonWorld(int type, String fileName, boolean loadWorld)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1); 
        this.type = type;

        //World size
        WIDTH = getWidth() / CELL_SIZE * 4;
        HEIGHT = getHeight() / CELL_SIZE * 4;
        WINDOW_WIDTH = WIDTH / 4;
        WINDOW_HEIGHT = HEIGHT / 4;
        ROOM_MIN_SIZE = WIDTH / 10;
        ROOM_MAX_SIZE = WIDTH / 4; //og is 4

        bossFight = false; //not currently fighting the boss 

        pause = new Button(Button.PAUSE, true);
        addObject(pause, PAUSEX, PAUSEY);

        //load variables from file 
        if(loadWorld){
            FileWriter.loadWorldFromFile(fileName);
            player = new Player(this.type);
            hpBar = new HpBar(player);

            player.setCoins(playerCoins);
            //player.setWeapon(playerWeaponType);
            player.setHP(playerHp);
            addObject(player, playerX, playerY);
            setRoomMaps();
        } else{
            worldLevel = 1;
            player.coinCount = 0;
            playerWeaponType = Weapon.SPEAR;
            //generate world
            map = generateRooms();
            //map = generateBossWorld();
            fixPathing();
            
            //player
            player = new Player(type);
            hpBar = new HpBar(player);

            WINDOW_X = rooms.get(0).x1;
            WINDOW_Y = rooms.get(0).y1;

            int windowRoomXCell = rooms.get(0).x1 - WINDOW_X;
            int windowRoomYCell = rooms.get(0).y1 - WINDOW_Y;
            int[] worldP = mapToWorld(windowRoomXCell + (rooms.get(0).x2 - rooms.get(0).x1) / 2, windowRoomYCell + (rooms.get(0).y2 - rooms.get(0).y1) / 2);
            addObject(player, worldP[0], worldP[1]); 

            // spawn
            CURR_ROOM = 0;
        }
        boundWindow();
        // background color
        background = new GreenfootImage(getWidth(), getHeight());
        background.setColor(VOID_COLOR);
        background.fill();
        setBackground(background);

        //add health bars
        addObject(hpBar, HPBARX, HPBARY);
        //initialize path finders
        pathFinders = new ArrayList<PathFinder>();
        setPathFinders();
        setRoomNums();

        //minimap
        miniMap = new MiniMap(map, player);

        // enemies
        allEnemies = new ArrayList<ArrayList<Enemy>>();
        for(int i = 0; i < rooms.size(); i++){
            allEnemies.add(new ArrayList<Enemy>());
        }
        visibleEnemies = new ArrayList<Cell>();
        displayWindow(map);
        //background music 
        bgm = new GreenfootSound("Dungeon.mp3");
        bgm.setVolume(60);
        bgm.playLoop();
        //only add a ladder if the world level is not 5 when loaded 
        if(!loadWorld && worldLevel != 5){
            setLadder(LADDER);
        }
        
        setPaintOrder(SuperDisplayLabel.class, Text.class, Jail.class, SuperStatBar.class, HpBar.class, Effect.class, MiniMap.class, Button.class, Asset.class, Weapon.class, Attack.class, Boss.class, Enemy.class, Player.class, Goo.class, Cell.class);
        //automatically set the players weapon type to either a spear or whatever weapon it was loaded as
        player.setWeapon(playerWeaponType); 
        miniBossDefeated = false;
    }
    

    /**
     * Constantly check keys and update rooms and stats, if it's the boss world it'll lock the scrolling once the boss appears on the screen.
     * Otherwise keep enemy pathfinding and add enemies and mini bosses to rooms as usual. 
     */
    public void act(){
        checkMiniBoss();
        displayMiniMap();
        updateCurrentRoom();
        checkPause();
        hpBar.updateLevelAndCoins();

        //if its the boss world 
        if(worldLevel > 4){
            if(CURR_ROOM == 1){
                bossShift = true;
                bossFight = true;
                player.canShift = false;
            }
            if(bossShift){
                player.canMove(false);
                if(DungeonWorld.WINDOW_Y > 0){
                    bossShift(true);
                }
                else if(DungeonWorld.WINDOW_X > 0){
                    bossShift(false);
                } else {
                    bossShift = false;
                    player.canMove(true);
                    addObject(boss, getWidth() /2, getHeight()/ 4);
                    if(!(getObjects(Jail.class).size() > 0)){
                        addObject(new Jail(true), 120, 90); 
                    }
                    if(getObjects(HpBar.class).size() == 1){
                        bossHpBar = new HpBar(boss);
                        addObject(bossHpBar, BOSSHPBARX, BOSSHPBARY);
                    }
                }
            }
        } 
        //otherwise allow the enemies to path find and add mini bosses to room 
        else {
            pathFindEnemies();
            addMiniBossToRoom();
            //check if need to change floors to the next floor or the boss world 
            if(changeFloors){ //if clickd on ladder
                changeFloors();
                changeFloors = false;
                miniBossDefeated = false; 
            }
            if(changeBoss){ //if clicked on hole
                changeToBossLvl();
                changeBoss = false;
                miniBossDefeated = false; 
            }
        }
    }

    /**
     * Did the player pause the game
     */
    private void checkPause(){
        if(Greenfoot.mouseClicked(pause)){
            Greenfoot.setWorld(new PauseWorld(this, player, false));
        }
    }
    
    /**
     * Is the mini boss defeated, can the player continue to the next level
     */
    private void checkMiniBoss(){
        if(miniBossDefeated && worldLevel < 4){
            CurrentLadder.openJailCell();
            miniBossDefeated = false; 
            player.canShift = false; 
        }
    }

    /**
     * Enemies pathfind on a counter to avoid too much lag
     */
    private void pathFindEnemies(){
        //enemy pathfinding per room
        boolean updateEnemies = enemyCounter % ENEMY_SPEED == 0;
        updateEnemyPaths(false, updateEnemies, allEnemies.get(CURR_ROOM));
        enemyCounter++;
    }

    /**
     * Only add one mini boss per level, added based on the ladder location and floor level. 
     */
    private void addMiniBossToRoom(){
        if(!(rooms.get(CURR_ROOM).hasEnemies())){
            addEnemiesToRoom(10, CURR_ROOM, allEnemies.get(CURR_ROOM));
            if(CURR_ROOM == BOSS_ROOM){
                int roomWidth = rooms.get(CURR_ROOM).x2 - rooms.get(CURR_ROOM).x1;
                int roomHeight= rooms.get(CURR_ROOM).y2 - rooms.get(CURR_ROOM).y1;
                int x,y;
                boolean locationFound = false;
                while(!locationFound){
                    x = Greenfoot.getRandomNumber(roomWidth);
                    y = Greenfoot.getRandomNumber(roomHeight);
                    int[] mapCoords = roomToMap(x,y);
                    if(map[mapCoords[1]][mapCoords[0]] == FLOOR) {
                        locationFound = true;
                        map[mapCoords[1]][mapCoords[0]] = ENEMY;
                        int rand = rand(3);
                        Enemy e;
                        e = new MiniBoss(x, y, worldLevel);
                        
                        allEnemies.get(CURR_ROOM).add(e);
                        e.setCell(new Cell(CELL_SIZE, ENEMY, Color.GREEN), mapCoords[0], mapCoords[1]);
                    }
                }
            }
        }
    }

    /**
     * Shift the screen in the boss world to fit and lock the scrolling. 
     */
    private void bossShift(boolean up){
        ArrayList<Cell> cells = (ArrayList<Cell>)getObjects(Cell.class);
        ArrayList<Enemy> enemies = (ArrayList<Enemy>)getObjects(Enemy.class);
        ArrayList<Player> p = (ArrayList<Player>)getObjects(Player.class);
        //all objects that should be moved
        ArrayList<Actor> actors = new ArrayList<Actor>();
        actors.addAll(cells);
        actors.addAll(enemies);
        actors.addAll(p);
        if(up){
            for (Actor o : actors) { // Delete bottom-most column of balls and move the rest down
                if (o.getY() == (WINDOW_HEIGHT-1) * CELL_SIZE + CELL_SIZE / 2) {
                    removeObject(o);
                } else {
                    o.setLocation(o.getX(), o.getY()+CELL_SIZE);
                }
            }

            WINDOW_Y --;
            for(int i = 0; i < WINDOW_WIDTH; i++) { // Add top most row from map
                if (map[WINDOW_Y][WINDOW_X + i] > 0) {
                    addObject(new Cell(map[WINDOW_Y][WINDOW_X + i]), i * CELL_SIZE + CELL_SIZE/2, CELL_SIZE/2);
                }
            } 
        } else{

            for (Actor o : actors) { // Delete right-most column of balls and move the rest right
                if (o.getX() == (WINDOW_WIDTH-1) * CELL_SIZE + CELL_SIZE / 2) {
                    removeObject(o);
                } else {
                    o.setLocation(o.getX()+CELL_SIZE, o.getY());
                }
            }

            WINDOW_X --;
            for(int i = 0; i < WINDOW_HEIGHT; i++) { // Add left most column from map
                if (map[WINDOW_Y + i][WINDOW_X] > 0) {
                    addObject(new Cell(map[WINDOW_Y + i][WINDOW_X]), CELL_SIZE/2, i * CELL_SIZE + CELL_SIZE/2);
                }
            } 
        }
        player.canMove(true);
    }

    /**
     * Change the level to the boss world, generate the boss world and clear all the enemies and maps. 
     */
    private void changeToBossLvl(){
        //remove everything
        ArrayList<Actor> actors = (ArrayList<Actor>)getObjects(Actor.class);
        for(Actor a: actors){
            removeObject(a);
        }
        rooms.clear();
        map = generateBossWorld();
        fixPathing();
        pathFinders.clear();
        setPathFinders();
        setRoomNums();

        pause = new Button(Button.PAUSE, true);
        addObject(pause, PAUSEX, PAUSEY);

        //minimap
        miniMap = new MiniMap(map, player);

        // spawn
        CURR_ROOM = 0;

        WINDOW_X = rooms.get(0).x1;
        WINDOW_Y = rooms.get(0).y1;
        boundWindow();

        //set player location in first room
        int windowRoomXCell = rooms.get(0).x1 - WINDOW_X;
        int windowRoomYCell = rooms.get(0).y1 - WINDOW_Y;
        int[] worldP = mapToWorld(windowRoomXCell + (rooms.get(0).x2 - rooms.get(0).x1) / 2, windowRoomYCell + (rooms.get(0).y2 - rooms.get(0).y1) / 2);
        addObject(player, worldP[0], worldP[1]); 
        addObject(hpBar, HPBARX, HPBARY);

        // enemies
        allEnemies.clear();
        for(int i = 0; i < MAX_WAVES; i++){
            allEnemies.add(new ArrayList<Enemy>());
        }
        visibleEnemies.clear();
        displayWindow(map);
        worldLevel++;
    }

    /**
     * Change floors, initialize all maps and enemies again. 
     */
    private void changeFloors(){
        //remove everything
        ArrayList<Actor> actors = (ArrayList<Actor>)getObjects(Actor.class);
        for(Actor a: actors){
            if (a.getWorld() != null){
                removeObject(a);
            }
            
        }

        //generate 
        rooms.clear();
        map = generateRooms();
        fixPathing();
        pathFinders.clear();
        setPathFinders();
        setRoomNums();

        pause = new Button(Button.PAUSE, true);
        addObject(pause, PAUSEX, PAUSEY);

        //minimap
        miniMap = new MiniMap(map, player);

        // spawn
        CURR_ROOM = 0;

        WINDOW_X = rooms.get(0).x1;
        WINDOW_Y = rooms.get(0).y1;
        boundWindow();

        int windowRoomXCell = rooms.get(0).x1 - WINDOW_X;
        int windowRoomYCell = rooms.get(0).y1 - WINDOW_Y;
        int[] worldP = mapToWorld(windowRoomXCell + (rooms.get(0).x2 - rooms.get(0).x1) / 2, windowRoomYCell + (rooms.get(0).y2 - rooms.get(0).y1) / 2);
        addObject(player, worldP[0], worldP[1]);   
        addObject(hpBar, HPBARX, HPBARY);

        // enemies
        allEnemies.clear();
        for(int i = 0; i < rooms.size(); i++){
            allEnemies.add(new ArrayList<Enemy>());
        }
        visibleEnemies.clear();
        displayWindow(map);
        if(worldLevel < 3){
            setLadder(LADDER);
        } else{
            setLadder(HOLE);
        }
        worldLevel++;
    }

    /**
     * Calculates the distance between two points on a cartesian plane using pythagorean theorem.
     * @param x1    The X-coordinate of the first point 
     * @param x2    The X-coordinate of the second point 
     * @param y1    The Y-coordinate of the first point 
     * @param y2    The Y-coordinate of the second point 
     * @return double The distance between the two points.
     */
    public double calculateDistanceBetween(int x1, int x2, int y1, int y2){
        return Math.sqrt(Math.pow(x1 -x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * Sets the ladder on each floor the furthest away from the spawn location of the player.
     */
    private void setLadder(int type){
        Room furthestRoom = rooms.get(0);
        double furthestDist = calculateDistanceBetween(player.getX(), furthestRoom.getCenterX(), player.getY(), furthestRoom.getCenterY());
        for(Room r: rooms){
            double tempDist = calculateDistanceBetween(player.getX(), r.getCenterX(), player.getY(), r.getCenterY());
            if(furthestDist < tempDist){
                furthestRoom = r;
                furthestDist = tempDist; 
            }
        }
        BOSS_ROOM = furthestRoom.getRoomNum();
        int roomWidth = furthestRoom.x2 - furthestRoom.x1;
        int roomHeight= furthestRoom.y2 - furthestRoom.y1;

        int x, y;
        boolean locationFound = false;
        while(!locationFound) {
            x = Greenfoot.getRandomNumber(roomWidth);
            y = Greenfoot.getRandomNumber(roomHeight);
            int[] mapCoords = roomToMap(x,y, furthestRoom.getRoomNum());
            if(map[mapCoords[1]][mapCoords[0]] == FLOOR) {
                map[mapCoords[1]][mapCoords[0]] = type;
                locationFound = true;
            }
        }
        locationFound = false;
    }

    /**
     * Set each room their ID number after initialized. 
     */
    private void setRoomNums(){
        for(int i = 0; i < rooms.size(); i++){
            rooms.get(i).setRoomNum(i);
        }
    }

    /**
     * Set pathfinders for each rooms after rooms generated. 
     */
    private void setPathFinders(){
        for(int i = 0; i < rooms.size(); i++){
            int[][] copy = Arrays.stream(rooms.get(i).roomMap).map(int[]::clone).toArray(int[][]::new);
            pathFinders.add(new PathFinder(copy));
        }
    }

    /**
     * Update the current room based on the player location. For pathfinding reasons. 
     */
    private void updateCurrentRoom(){
        int[] coords = worldToMap(player.getX(), player.getY());
        for(Room r: rooms){
            if(coords[0] > r.x1 && coords[0] < r.x2 && coords[1] > r.y1 && coords[1] < r.y2){
                CURR_ROOM = r.getRoomNum();
                return;
            }
        }

    }

    /**
     * Make sure not to call this if there are no locations in the room (FLOOR) available.
     * Add enemies to a specific room, and populates that rooms arraylist of enemies.
     * 
     * @param n         The amount of enemies in that should be added to the room
     * @param roomNum   The room number that the enemies should be added to
     * @param enemies   The arraylist of enemies that should be updated with the enemies in the specified room
     */
    private void addEnemiesToRoom(int n, int roomNum, ArrayList<Enemy> enemies) {
        // Pick a random room floor and spawn enemy there
        //int roomWidth = rooms.get(CURR_ROOM).x2 - rooms.get(CURR_ROOM).x1;
        //int roomHeight= rooms.get(CURR_ROOM).y2 - rooms.get(CURR_ROOM).y1;

        int roomWidth = rooms.get(roomNum).x2 - rooms.get(roomNum).x1;
        int roomHeight= rooms.get(roomNum).y2 - rooms.get(roomNum).y1;

        int x,y;
        boolean locationFound = false;
        for(int i = 0; i < n; i++) {
            while(!locationFound) {
                x = Greenfoot.getRandomNumber(roomWidth);
                y = Greenfoot.getRandomNumber(roomHeight);
                int[] mapCoords = roomToMap(x,y);
                if(map[mapCoords[1]][mapCoords[0]] == FLOOR) {
                    locationFound = true;
                    map[mapCoords[1]][mapCoords[0]] = ENEMY;
                    int rand = rand(3);
                    Enemy e; 
                    if(rand == 0){
                        e = new Slime(x, y, worldLevel);
                    } else if (rand == 1){
                        e = new Flyer(x, y, worldLevel, player);
                    } else{
                        e = new Creeper(x, y, worldLevel);
                    }
                    enemies.add(e);
                    e.setCell(new Cell(CELL_SIZE, ENEMY, Color.GREEN), mapCoords[0], mapCoords[1]);

                    // Initialize the pathfinder to what is currently visible in the window
                    //int[][] copy = Arrays.stream(rooms.get(CURR_ROOM).roomMap).map(int[]::clone).toArray(int[][]::new);

                }
            }
            locationFound = false;
        }

        updateRoomMap();//update the room map 
        updateVisibleEnemies(enemies);
    }

    // So that the pathfinding is updated
    public static void updateRoomMap() {
        int[][] roomMap = new int[rooms.get(CURR_ROOM).y2 - rooms.get(CURR_ROOM).y1][rooms.get(CURR_ROOM).x2 - rooms.get(CURR_ROOM).x1];
        for (int y = rooms.get(CURR_ROOM).y1; y < rooms.get(CURR_ROOM).y2; y++) {
            for (int x = rooms.get(CURR_ROOM).x1; x < rooms.get(CURR_ROOM).x2; x++) {
                roomMap[y - rooms.get(CURR_ROOM).y1][x - rooms.get(CURR_ROOM).x1] = map[y][x]; // update from map
            }
        }
        rooms.get(CURR_ROOM).setMap(roomMap);
        rooms.get(CURR_ROOM).setHasEnemies(true);
    }

    /**
     * Calculates the distance between two actors x and y coordinates 
     * @param a     The first actor
     * @param b     The second actor
     * @return double The distance between the two actors
     */
    public double calculateDistanceBetween(Actor a, Actor b){
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    /**
     * Update the enemy paths as the player moves.
     * 
     * @param showPath      Should the enemy path calculation be shown
     * @param updateEnemy   Should the enemy be updated
     * @param enemies       ArrayList of enemies in the current room
     */ 
    private void updateEnemyPaths(boolean showPath, boolean updateEnemy, ArrayList<Enemy> enemies) {
        for(Enemy e : enemies) {
            ArrayList<Node> path = new ArrayList<Node>(); 
            // Get player x,y
            int[] mapCoords = DungeonWorld.worldToMap(player.getX(), player.getY()); 
            int[] roomCoords = DungeonWorld.mapToRoom(mapCoords[0], mapCoords[1]);
            //System.out.println(roomCoords[0] + " " + roomCoords[1] + " " + e.roomX + " " + e.roomY);

            if(!e.actualPath.isEmpty() && updateEnemy) { // Update the enemy in the map 2d array and also visually
                Node n = e.actualPath.get(0);
                int[] mapCoordsN = DungeonWorld.roomToMap(n.x, n.y); 
                int[] mapCoordsE = DungeonWorld.roomToMap(e.roomX, e.roomY);

                boolean success = moveIfPossible(map, mapCoordsE[0], mapCoordsE[1], mapCoordsN[0], mapCoordsN[1]);
                if(success) {
                    e.roomX = n.x;
                    e.roomY = n.y;

                    //maybe fix this
                    if(e.getWorld() != null) {
                        int[] windowCoords = DungeonWorld.windowToMap(e.cellX, e.cellY);
                        int[] worldCoords = DungeonWorld.mapToWorld(windowCoords[0], windowCoords[1]);

                        if(e.getClass().getName().equals("Creeper")){
                            if(calculateDistanceBetween(e, player) > 100.0){
                                e.setLocation(worldCoords[0], worldCoords[1]);
                            }
                        } else {
                            e.setLocation(worldCoords[0], worldCoords[1]);
                        }
                    }

                    e.cellX = mapCoordsN[0];
                    e.cellY = mapCoordsN[1];

                    updateVisibleEnemies(enemies); 
                    //updateRoomMap(); // may not be necessary unless you want moving obstacles
                    e.actualPath.remove(0);
                }
            }

            int[] randPlayerCoords = getRandomPosition(rooms.get(DungeonWorld.CURR_ROOM).roomMap, roomCoords[0], roomCoords[1]);
            path = (ArrayList<Node>)pathFinders.get(CURR_ROOM).calculatePath(DungeonWorld.rooms.get(DungeonWorld.CURR_ROOM).roomMap, e.roomX, e.roomY, randPlayerCoords[0], randPlayerCoords[1]);

            if(path == null) {
                //return; 
            } else {
                e.actualPath = path;
                if(showPath) {
                    //Delete previous path
                    for(Cell c : e.path) {
                        removeObject(c);
                    }
                    e.path.clear();
                    // draws on the screen, does not save path to any map or room
                    for(Node n : path) {
                        //System.out.println("Room: " + n.x + " " + n.y);
                        int[] mapCoordsN = DungeonWorld.roomToMap(n.x, n.y);
                        //System.out.println("Map: " + mapCoordsN[0] + " " + mapCoordsN[1]);
                        int[] windowCoordsN = DungeonWorld.windowToMap(mapCoordsN[0], mapCoordsN[1]);
                        int[] worldCoordsN = DungeonWorld.mapToWorld(windowCoordsN[0], windowCoordsN[1]);
                        //System.out.println("Adding path node at: " + worldCoordsN[0] + " " + worldCoordsN[1]);
                        if(!DungeonWorld.inWorld(worldCoordsN[0], worldCoordsN[1])) {
                            continue;
                        }
                        Cell c = new Cell(DungeonWorld.CELL_SIZE, 0, Color.BLUE);
                        addObject(c, worldCoordsN[0], worldCoordsN[1]);
                        e.path.add(c);
                    }
                }
            }
        } 
    }

    /**
     * Updates the location of enemies that are supposed to be on the screen 
     * (the scrolling forces the enemies to "move" off screen in their respective rooms)
     * 
     * @param enemies   The ArrayList of enemies that should be updated on screen
     */
    private void updateVisibleEnemies(ArrayList<Enemy> enemies) {
        for(Enemy e : enemies) {
            int[] windowCoords = DungeonWorld.windowToMap(e.cellX, e.cellY);
            int[] worldCoords = DungeonWorld.mapToWorld(windowCoords[0], windowCoords[1]);
            if(inWorld(worldCoords[0], worldCoords[1]) && e.getWorld() == null) {
                //addObject(e.cell, worldCoords[0], worldCoords[1]);
                addObject(e, worldCoords[0], worldCoords[1]);
            }
        }
    }

    /**
     * Enemy tries to move to nearest floor if possible, avoiding other enemies
     * @param map       The map of the room that the enemies should be moving in
     * @param currentX  The current X of the enemy
     * @param currentY  The current Y of the enemy
     * @param targetX   The "target" X (the player x)
     * @param targetY   The "target" Y (the player y)
     * 
     * @return boolean  Can the enemy move?
     */
    private boolean moveIfPossible(int[][] map, int currentX, int currentY, int targetX, int targetY) {
        // Check if the target position is a floor
        if (map[targetY][targetX] == 2) {
            // Swap the current position with the target position
            int temp = map[currentY][currentX];
            map[currentY][currentX] = map[targetY][targetX];
            map[targetY][targetX] = temp;
            return true; // Move successful
        } else {
            // If the target position is not a floor, try surrounding cells in the same direction
            int dx = targetX - currentX;
            int dy = targetY - currentY;

            // Check if the direction is valid (not diagonal)
            if (dx != 0 && dy != 0) {
                return false; // Cannot move diagonally
            }

            // Check the next cell in the same direction
            int nextX = currentX + dx;
            int nextY = currentY + dy;

            // Check if the next cell is within the map bounds
            if (nextX >= 0 && nextX < map[0].length && nextY >= 0 && nextY < map.length) {
                // Check if the next cell is a floor
                if (map[nextY][nextX] == 2) {
                    // Swap the current position with the next position
                    int temp = map[currentY][currentX];
                    map[currentY][currentX] = map[nextY][nextX];
                    map[nextY][nextX] = temp;
                    return true; // Move successful
                }
            }
        }
        return false; // Unable to move
    }

    private static int[] getRandomPosition(int[][] map, int x, int y) {
        int offsetX = (rand(3) - 1); // Random offset in the range [-1, 1] for x-coordinate
        int offsetY = (rand(3) - 1); // Random offset in the range [-1, 1] for y-coordinate

        int newX = x + offsetX;
        int newY = y + offsetY;

        // Ensure the new position is within the bounds of the map
        if (newX >= 0 && newX < map[0].length && newY >= 0 && newY < map.length) {
            return new int[]{newX, newY};
        } else {
            // If the new position is outside the map bounds, return the player's current position
            return new int[]{x,y};
        }
    }


    /**
     * Takes in world x, y pixel coordinates and converts to indices in 
     * the map 2d array.
     * The current window location is taken into account.
     * 
     * @param x The x coordinate in pixels on the window
     * @param y The y coordinate in pixels on the window
     */
    public static int[] worldToMap(int x, int y) {
        // Need to reverse the display math {x,y} * CELL_SIZE + CELL_SIZE/2
        return new int[] {WINDOW_X + (x - CELL_SIZE/2) / CELL_SIZE, WINDOW_Y + (y - CELL_SIZE/2) / CELL_SIZE};
    }

    /**
     * Takes in [x][y] indices and converts to x,y world coordinates (pixel values)
     * Need to sanitize the return coordinates to make sure they are within the window bounds before displaying them
     * ONLY USE WITH WINDOW COORDINATES
     * INPUT: window cells
     * OUTPUT: pixels
     * 
     * @param x The x coordinate in cells on the map
     * @param y The y coordinate in cells on the map
     */
    public static int[] mapToWorld(int x, int y) {
        return new int[] {x * CELL_SIZE + CELL_SIZE/2, y * CELL_SIZE + CELL_SIZE/2};
    }

    /**
     * Takes in the window's [x][y] cell coordinates and 
     * converts to [x][y] cell coordinates in the map.
     * Returns indices instead of pixel coords.
     * 
     * @param x The x coordinate of the window in cells
     * @param y The y coordinate of the window in cells
     */
    public static int[] windowToMap(int x, int y) {
        return new int[] {x - WINDOW_X, y - WINDOW_Y};
    }
    
    /**
     * Takes in the world maps [x][y] cell coordinates
     * and converts them into cell coordinates on the room map
     * 
     * @param x     The x coordinate of the map
     * @param y     The y coordinate of the map
     */
    public static int[] mapToRoom(int x, int y) {
        return new int[] {x - rooms.get(CURR_ROOM).x1, y - rooms.get(CURR_ROOM).y1};
    }

    /**
     * Takes in the room maps [x][y] cell coordinates
     * and converts them into cell coordinates on the world map
     * 
     * @param x     The x coordinate of the room
     * @param y     The y coordinate of the room
     */
    public static int[] roomToMap(int x, int y) {
        return new int[] {x + rooms.get(CURR_ROOM).x1, y + rooms.get(CURR_ROOM).y1};
    }

    /**
     * Takes in the room maps [x][y] cell coordinates
     * and converts them into cell coordinates on the world map
     * but only using a given room number
     * 
     * @param x         The x coordinate of the room
     * @param y         The y coordinate of the room
     * @param roomNum   The number of the room in which the coordinates are being converted from
     */
    public static int[] roomToMap(int x, int y, int roomNum) {
        return new int[] {x + rooms.get(roomNum).x1, y + rooms.get(roomNum).y1};
    }

    /**
     * Sets the end
     * @param x     The x coordinate of the end
     * @param y     The y coordinate of the end
     */
    public void setEnd(int x, int y) {
        //System.out.println("End set: " + x + " " + y);
        eX = x / CELL_SIZE;
        eY = y / CELL_SIZE;
    }

    /**
     * Returns whether the world coordinates are valid
     * @param x     X-coordinate
     * @param y     Y-coordinate
     */
    public static boolean inWorld(int x, int y) {
        int[] worldCoords = mapToWorld(WINDOW_WIDTH, WINDOW_HEIGHT);
        return x > 0 && x < worldCoords[0] && y > 0 && y < worldCoords[1];
    }

    /**
     * Getter method for a reference to the current player
     * @return Player       The current player. 
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Getter method for a reference to the current pathfinder
     * @return PathFinder       The current pathfinder. 
     */
    public PathFinder getPathFinder() {
        return pathFinder;
    }

    /**
     * Fixes the pathing for the world generation by adding walls around
     * the floors that are touching the void
     */
    private void fixPathing(){
        for(int i = 0; i < WIDTH; i++){
            for(int j = 0; j < HEIGHT; j++){
                //coordinates flipped bc y is how many arrays
                if(map[j][i] == FLOOR){
                    //below
                    if(map[j + 1][i] == VOID){
                        map[j + 1][i] = WALL;
                    }
                    //above
                    if(map[j - 1][i] == VOID){
                        map[j - 1][i] = WALL;
                    }
                    //right
                    if(map[j][i + 1] == VOID){
                        map[j][i + 1] = WALL;
                    }
                    //left
                    if(map[j][i - 1] == VOID){
                        map[j][i - 1] = WALL;
                    }
                    //}
                }
            }
        }
    }

    /**
     * Constantly checks if the user presses 'm' and shows the mini map of the world.
     */
    private void displayMiniMap(){
        if(Greenfoot.isKeyDown("m")){
            addObject(miniMap, MINIMAP_X, MINIMAP_Y);
        } else{
            removeObject(miniMap);
            miniMap.reset();
        }
    }

    /**
     * Shifts the actual window and all the actors inside in the given direction.
     * 0 - right
     * 1 - left
     * 2 - up
     * 3 - down
     * 
     * @param dir The direction in which the window should shift
     */
    public void shiftWindow(int dir) {
        ArrayList<Cell> cells = (ArrayList<Cell>)getObjects(Cell.class);
        ArrayList<Enemy> enemies = (ArrayList<Enemy>)getObjects(Enemy.class);
        ArrayList<Coin> coins = (ArrayList<Coin>)getObjects(Coin.class);
        ArrayList<Goo> goo = (ArrayList<Goo>)getObjects(Goo.class);
        //all objects that should be moved
        ArrayList<Actor> actors = new ArrayList<Actor>();
        actors.addAll(cells);
        actors.addAll(enemies);
        actors.addAll(coins);
        actors.addAll(goo);

        if(dir == 0){ //&& WINDOW_X + WINDOW_WIDTH < WIDTH
            for (Actor o : actors) { // Delete left-most column of balls and move the rest left
                if (o.getX() <= CELL_SIZE / 2) {
                    removeObject(o);
                } else {
                    o.setLocation(o.getX()-CELL_SIZE, o.getY());
                }
            }

            WINDOW_X ++;
            for(int i = 0; i < WINDOW_HEIGHT; i++) { // Add right most column from map
                if (map[WINDOW_Y + i][WINDOW_X + WINDOW_WIDTH - 1] > 0) {
                    addObject(new Cell(map[WINDOW_Y + i][WINDOW_X + WINDOW_WIDTH-1]), (WINDOW_WIDTH-1) * CELL_SIZE + CELL_SIZE/2, i * CELL_SIZE + CELL_SIZE/2);
                }
            }
        }
        if(dir == 1){ // && WINDOW_X > 0
            for (Actor o : actors) { // Delete right-most column of balls and move the rest right
                if (o.getX() == (WINDOW_WIDTH-1) * CELL_SIZE + CELL_SIZE / 2) {
                    removeObject(o);
                } else {
                    o.setLocation(o.getX()+CELL_SIZE, o.getY());
                }
            }

            WINDOW_X --;
            for(int i = 0; i < WINDOW_HEIGHT; i++) { // Add left most column from map
                if (map[WINDOW_Y + i][WINDOW_X] > 0) {
                    addObject(new Cell(map[WINDOW_Y + i][WINDOW_X]), CELL_SIZE/2, i * CELL_SIZE + CELL_SIZE/2);
                }
            } 
        }
        if(dir == 2){ //&& WINDOW_Y > 0
            for (Actor o : actors) { // Delete bottom-most column of balls and move the rest down
                if (o.getY() == (WINDOW_HEIGHT-1) * CELL_SIZE + CELL_SIZE / 2) {
                    removeObject(o);
                } else {
                    o.setLocation(o.getX(), o.getY()+CELL_SIZE);
                }
            }

            WINDOW_Y --;
            for(int i = 0; i < WINDOW_WIDTH; i++) { // Add top most row from map
                if (map[WINDOW_Y][WINDOW_X + i] > 0) {
                    addObject(new Cell(map[WINDOW_Y][WINDOW_X + i]), i * CELL_SIZE + CELL_SIZE/2, CELL_SIZE/2);
                }
            } 
        }
        if(dir == 3){ //&& WINDOW_Y + WINDOW_HEIGHT < HEIGHT
            for (Actor o : actors) { // Delete top-most column of balls and move the rest up
                if (o.getY() == CELL_SIZE / 2) {
                    removeObject(o);
                } else {
                    o.setLocation(o.getX(), o.getY()-CELL_SIZE);
                }
            }
            WINDOW_Y ++;
            for(int i = 0; i < WINDOW_WIDTH; i++) { // Add bottom most row from map
                if (map[WINDOW_Y + WINDOW_HEIGHT - 1][WINDOW_X + i] > 0) {
                    addObject(new Cell(map[WINDOW_Y + WINDOW_HEIGHT - 1][WINDOW_X + i]), i * CELL_SIZE + CELL_SIZE/2, (WINDOW_HEIGHT - 1) * CELL_SIZE + CELL_SIZE/2);
                }
            }
        }
    }

    private void boundWindow() {
        if(WINDOW_X + WINDOW_WIDTH > WIDTH) {
            WINDOW_X -= WINDOW_X + WINDOW_WIDTH - WIDTH;
        }
        if(WINDOW_Y + WINDOW_HEIGHT > HEIGHT) {
            WINDOW_Y -= WINDOW_Y + WINDOW_HEIGHT - HEIGHT;
        }
    }

    private void displayWindow(int[][] map) {
        ArrayList<Cell> remove = (ArrayList<Cell>)getObjects(Cell.class);
        for (Object objects : remove)
            removeObject((Actor)objects);
        for(int i = 0; i < WINDOW_HEIGHT; i++) {
            for(int j = 0; j < WINDOW_WIDTH; j++) {
                //System.out.print(map[WINDOW_Y + i][WINDOW_X + j]);
                if (map[WINDOW_Y + i][WINDOW_X + j] > 0) {
                    addObject(new Cell(map[WINDOW_Y + i][WINDOW_X + j]), j * CELL_SIZE + CELL_SIZE/2, i * CELL_SIZE + CELL_SIZE/2);
                }
            }
            //System.out.println();
        }
    }

    /**
     * Helper method to get a random integer
     * @param num The maximum integer possible
     */
    private static int rand(int num){
        return Greenfoot.getRandomNumber(num);
    }

    /**
     * Generate the final boss world. (Two rooms) 
     */
    private int[][] generateBossWorld(){
        int[][] grid = new int[HEIGHT][WIDTH];
        rooms = new ArrayList<>();
        int radius = HEIGHT/6;
        int centerX = WIDTH/2;
        int centerY = HEIGHT/6;
        int rX = WINDOW_WIDTH / 2 -1;
        int rY = WINDOW_HEIGHT / 2;

        int r1W = 15;
        int r1H = 15;
        Room r1 = new Room (rX - r1W/2, HEIGHT -20, r1W, r1H);
        rooms.add(r1);
        int[][] roomMap1 = new int[r1.y2 - r1.y1][r1.x2 - r1.x1];
        for (int y = r1.y1; y < r1.y2; y++) {
            for (int x = r1.x1; x < r1.x2; x++) {
                grid[y][x] = FLOOR; // Floor
                roomMap1[y - r1.y1][x - r1.x1] = FLOOR;
            }
        }
        r1.setMap(roomMap1);

        Room r2 = new Room (0, 0, rX * 2, rY * 2);
        rooms.add(r2);
        //int[][] roomMap2 = new int[r2.y2 - r2.y1][r2.x2 - r2.x1];
        int[][] roomMap2 = new int[rY*2][rX*2];
        System.out.println(WIDTH + " " + HEIGHT);
        for(int y = r2.y1; y < r2.y2; y++){
            for(int x = r2.x1; x < r2.x2; x++){
                //double dist = calculateDistanceBetween(y, centerY, x, centerX);
                double dist = Math.pow(x - rX, 2) / (rX * rX) + Math.pow(y - rY, 2) / (rY * rY);

                if(dist < 1){
                    grid[y][x] = FLOOR;
                    roomMap2[y - r2.y1][x - r2.x1] = FLOOR;
                }
            }
        }
        r2.setMap(roomMap2);

        // Connect rooms with corridors (no diagonal movement)
        for (int i = 1; i < rooms.size(); i++) {
            Room prevRoom = rooms.get(i - 1);
            Room currRoom = rooms.get(i);

            int startX = prevRoom.getCenterX();
            int startY = prevRoom.getCenterY();
            int endX = currRoom.getCenterX();
            int endY = currRoom.getCenterY();

            while (startX != endX) {
                int type = FLOOR;
                if(grid[startY][startX] == WALL){
                    type = DOOR;
                }
                grid[startY][startX] = type; // Corridor
                grid[startY-1][startX] = type;
                grid[startY+1][startX] = type;

                grid[startY-2][startX] = type;
                grid[startY+2][startX] = type;

                startX += (startX < endX) ? 1 : -1;
            }
            while (startY != endY) {
                int type = FLOOR;
                if(grid[startY][startX] == WALL){
                    type = DOOR;
                }

                grid[startY][startX] = type; // Corridor
                grid[startY][startX-1] = type;
                grid[startY][startX+1] = type;

                grid[startY][startX-2] = type;
                grid[startY][startX+2] = type;
                startY += (startY < endY) ? 1 : -1;
            }
        }

        return grid;
    }

    /**
     * Autogenerates a 2D map of the rooms
     */
    private int[][] generateRooms() {
        int[][] grid = new int[HEIGHT][WIDTH];

        rooms = new ArrayList<>();

        // Generate rooms
        while(rooms.size() < MIN_ROOMS){
            rooms.clear();
            for (int i = 0; i < MAX_ROOMS; i++) {
                int roomWidth = rand(ROOM_MAX_SIZE - ROOM_MIN_SIZE) + ROOM_MIN_SIZE;
                int roomHeight = rand(ROOM_MAX_SIZE - ROOM_MIN_SIZE) + ROOM_MIN_SIZE;
                int x = rand(WIDTH - roomWidth - 1) + 1;
                int y = rand(HEIGHT - roomHeight - 1) + 1;

                Room newRoom = new Room(x, y, roomWidth, roomHeight);
                boolean isOverlap = false;
                for (Room room : rooms) {
                    if (newRoom.intersects(room)) {
                        isOverlap = true;
                        break;
                    }
                }
                if (!isOverlap) {
                    rooms.add(newRoom);
                }
            }
        }

        // Fill rooms with floors
        for (Room room : rooms) {
            //System.out.println("Room at: " + room.x1 + " " + room.y1);
            int[][] roomMap = new int[room.y2 - room.y1][room.x2 - room.x1];
            for (int y = room.y1; y < room.y2; y++) {
                for (int x = room.x1; x < room.x2; x++) {
                    grid[y][x] = FLOOR; // Floor
                    roomMap[y - room.y1][x - room.x1] = FLOOR;
                    room.setMap(roomMap);
                }
            }
        }

        // Connect rooms with corridors (no diagonal movement)
        for (int i = 1; i < rooms.size(); i++) {
            Room prevRoom = rooms.get(i - 1);
            Room currRoom = rooms.get(i);

            int startX = prevRoom.getCenterX();
            int startY = prevRoom.getCenterY();
            int endX = currRoom.getCenterX();
            int endY = currRoom.getCenterY();

            while (startX != endX) {
                int type = FLOOR;
                if(grid[startY][startX] == WALL){
                    type = DOOR;
                }
                grid[startY][startX] = type; // Corridor
                grid[startY-1][startX] = type;
                grid[startY+1][startX] = type;

                grid[startY-2][startX] = type;
                grid[startY+2][startX] = type;

                startX += (startX < endX) ? 1 : -1;
            }
            while (startY != endY) {
                int type = FLOOR;
                if(grid[startY][startX] == WALL){
                    type = DOOR;
                }

                grid[startY][startX] = type; // Corridor
                grid[startY][startX-1] = type;
                grid[startY][startX+1] = type;

                grid[startY][startX-2] = type;
                grid[startY][startX+2] = type;
                startY += (startY < endY) ? 1 : -1;
            }
        }

        // Fill rooms with obstacles
        for (Room room : rooms) {
            int width = room.x2 - room.x1;
            int height = room.y2 - room.y1;

            for(int i = 0; i < rand(4) + 1; i++){
                int a = rand(width / 2) + room.x1 + width/4; //x
                int b = rand(height / 2) + room.y1 + height/4; //y

                int boxWidth = rand(2) + 2;
                int boxHeight = rand(2) + 2;
                if(rand(1) == 0){
                    boxWidth  += rand(4) + 1;
                } else {
                    boxHeight += rand(4) + 1;
                }
                for (int y = b; y < b + boxHeight; y++) {
                    for (int x = a; x < a + boxWidth; x++) {
                        try{
                            grid[y][x] = WALL; // Wall
                            room.roomMap[y - room.y1][x - room.x1] = WALL;
                        } catch(Exception e){
                            System.out.println("Out out bounds");
                        }
                    }
                }
            }
        }

        return grid;
    }

    /**
     * Sets individual room maps for every room on the arraylist, used for enemy pathfinding. 
     */
    private void setRoomMaps(){
        for (Room room : rooms) {
            int[][] roomMap = new int[room.y2 - room.y1][room.x2 - room.x1];
            for (int y = room.y1; y < room.y2; y++) {
                for (int x = room.x1; x < room.x2; x++) {
                    roomMap[y - room.y1][x - room.x1] = FLOOR;
                    room.setMap(roomMap);
                }
            }
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