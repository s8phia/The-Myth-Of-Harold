import java.io.PrintWriter;  
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

/**
 * Saves and loads given files of the world.
 * 
 * @author Jessica Biro
 * @version January 2023
 */
public class FileWriter 
{
    //world width and height in cells to read and load the map 
    public static final int height = 800 / DungeonWorld.CELL_SIZE * 4;
    public static final int width = 1200 / DungeonWorld.CELL_SIZE * 4;
    
    /**
     * Clears the given file.
     * @param filePath  The file that you want to clear. 
     */
    public void clearFile(String filePath) throws Exception{
        PrintWriter writer = new PrintWriter(filePath, "UTF-8");
        try {
            writer.println();
            writer.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Saves the world to the given file.
     * @param filePath  The file that you want to save to
     * @param p         The player in the world that's being saved 
     */
    public void saveWorldToFile(String filePath, Player p) throws Exception {
        PrintWriter writer = new PrintWriter(filePath, "UTF-8");
        int[][] map = DungeonWorld.map;
        try {
            //draw the map
            for (int[] row : map) {
                for (int value : row) {
                    writer.print(value + " ");
                }
                writer.println();
            }
            //rooms
            writer.println(DungeonWorld.rooms.size()); // room size
            //input the constructors for rooms
            for(int i = 0; i < DungeonWorld.rooms.size(); i++){
                writer.print(DungeonWorld.rooms.get(i).x1 + " ");
                writer.print(DungeonWorld.rooms.get(i).y1 + " ");
                writer.print(DungeonWorld.rooms.get(i).x2 + " ");
                writer.print(DungeonWorld.rooms.get(i).y2 + " ");
                writer.println();
            }
            //hero type
            writer.println(DungeonWorld.type);
            //weapon type
            writer.println(p.getWeaponType());
            //coins
            writer.println(p.getCoinCount());
            //hp
            writer.println(p.getPlayerHP());
            //worldLevel
            writer.println(DungeonWorld.worldLevel);
            //current room
            writer.println(DungeonWorld.CURR_ROOM);
            //windowX
            writer.println(DungeonWorld.WINDOW_X);
            //windowY
            writer.println(DungeonWorld.WINDOW_Y);
            //player x (in pixels)
            writer.println(p.getX());
            //player y (in pixels)
            writer.println(p.getY());
            
            writer.flush();
            System.out.println("Saved to file: " + filePath + " " + map.length + " " + map[0].length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Loads the world from given file and sets the static variables in the world 
     * @param filePath  The file that you want to load. 
     */
    public static void loadWorldFromFile(String filePath){
        //what to load
        int[][] grid = null;
        int totalRooms;
        ArrayList<Room> worldRooms = new ArrayList<Room>();
        int heroType, weaponType, coins, hp, worldLvl, CurrentRoom, windowX, windowY, pX, pY;
        
        File f = new File(filePath);
        Scanner scanner;
        
        try {
            int rowCount = height;
            int colCount = width;
            System.out.println("Row: " + rowCount + " Col: " + colCount);
            
            scanner = new Scanner(f);
            // Create the 2D array with the correct dimensions
            grid = new int[rowCount][colCount];
            System.out.println(rowCount + " " + colCount);

            // Populate the array with values from the file
            int row = 0;
            for(row = 0; row < height; row++){
                String[] values = scanner.nextLine().split(" ");
                for (int col = 0; col < values.length; col++) {
                    grid[row][col] = Integer.parseInt(values[col]);
                }
            }
            totalRooms = Integer.parseInt(scanner.nextLine());
            for(int i = 0; i < totalRooms; i++){
                String[] values = scanner.nextLine().split(" ");
                worldRooms.add(new Room(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]) - Integer.parseInt(values[0]), Integer.parseInt(values[3]) - Integer.parseInt(values[1])));
            }
            
            heroType = Integer.parseInt(scanner.nextLine());
            weaponType = Integer.parseInt(scanner.nextLine());
            coins = Integer.parseInt(scanner.nextLine());
            hp = Integer.parseInt(scanner.nextLine());
            worldLvl = Integer.parseInt(scanner.nextLine());
            CurrentRoom = Integer.parseInt(scanner.nextLine());
            windowX = Integer.parseInt(scanner.nextLine());
            windowY = Integer.parseInt(scanner.nextLine());
            pX = Integer.parseInt(scanner.nextLine());
            pY = Integer.parseInt(scanner.nextLine());
            
            //since void, set everything as static in the world
            
            //2D array of map
            DungeonWorld.map = grid;
            //Arraylist of rooms
            DungeonWorld.rooms = worldRooms;
            
            DungeonWorld.type = heroType;
            DungeonWorld.playerWeaponType = weaponType;
            DungeonWorld.playerCoins = coins;
            DungeonWorld.playerHp = hp;
            DungeonWorld.worldLevel = worldLvl;
            DungeonWorld.CURR_ROOM = CurrentRoom;
            DungeonWorld.WINDOW_X = windowX;
            DungeonWorld.WINDOW_Y = windowY;
            DungeonWorld.playerX = pX;
            DungeonWorld.playerY = pY;
                        
            System.out.println("World loaded from file: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Load only the hero type, weapon type, number of coins and world level from given file. Used to display world stats when loading. 
     * Returns the follow stats: <br>
     *      0: Hero type <br>
     *      1: Weapon type <br>
     *      2: Coin amount <br>
     *      3: World Level <br>
     * @param filePath  The world file of which you want to load the stats. 
     * @return int[]    Returns in array of all the stats
     */
    public static int[] loadWorldStats(String filePath){
        int heroType, weaponType, coins, worldLvl;
        int[] stats = new int[4];
        File f = new File(filePath);
        Scanner scanner;
        
        try {
            int rowCount = height;
            int colCount = width;
            scanner = new Scanner(f);
            // skip over the map and rooms loading
            int row = 0;
            for(row = 0; row < height; row++){
                String[] values = scanner.nextLine().split(" ");
            }
            int totalRooms = Integer.parseInt(scanner.nextLine());
            for(int i = 0; i < totalRooms; i++){
                String[] values = scanner.nextLine().split(" ");
            }
            //populate the array of stats
            stats[0] = Integer.parseInt(scanner.nextLine()); //hero type
            stats[1] = Integer.parseInt(scanner.nextLine()); //weapon type
            stats[2] = Integer.parseInt(scanner.nextLine()); //coins
            int hp = Integer.parseInt(scanner.nextLine());
            stats[3] = Integer.parseInt(scanner.nextLine()); //world level
            System.out.println("World loaded from file: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return stats;
    }
    
}
