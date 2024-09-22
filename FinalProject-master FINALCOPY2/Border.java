import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Borders around the world where the player cannot move. 
 * 
 * @author Jessica Biro
 * @version June 2023
 */
public class Border extends Actor
{
    /**
     * Takes in a 2D array of coordinates and makes them into borders where the player cannot move. 
     * @param VillageWorld  The 2D array of the world used to make the borders. 
     * 
     */
    public Border(int[][] VillageWorld)
    {
        GreenfootImage img = new GreenfootImage(VillageWorld.length,VillageWorld[0].length);
        //comment the next line if you want to see the borders on the world 
        img.setColor(new Color(0, 0, 0, 0)); 
        for(int x=0; x<VillageWorld.length; x++)
        {
            for(int y=0; y<VillageWorld[0].length; y++)
            {
                /*all of the blocked pixels are shown in the world info
                 * if any of the blocked pixels in the array are ones instead of zeros they become borders 
                 * they're then drawn onto the world 
                 */
                if(VillageWorld[x][y] == 1)
                {
                    img.drawRect(x,y,5,5);

                }
            }
        }
        setImage(img);
    }
}
