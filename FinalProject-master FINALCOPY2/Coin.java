import greenfoot.*;
/**
 * Coin class represents a collectible coin in the game.
 * It extends the Asset class and includes functionality for animation and interaction with the player.
 * 
 * @author Sophia Wang
 * @version January 2024
 */
public class Coin extends Asset
{
    private GreenfootImage[] coinFrames;
    private int frameIndex = 0;
    private int actCount = 0;

    /**
     * Creates a new instance of a coin that can be picked up by the player. 
     */
    public Coin() {
        coinFrames = new GreenfootImage[4];

        for (int i = 0; i < 4; i++) {
            coinFrames[i] = new GreenfootImage("coin_anim_f" + i + ".png");
            coinFrames[i].scale(10, 10);
        }

        setImage(coinFrames[frameIndex]);
    }

    /**
     * Animate the coin and check for player interaction. 
     */
    public void act() {
        animateCoin();
        Player player = (Player) getOneIntersectingObject(Player.class);
        if (player != null) {
            player.incrementCoins();

            getWorld().removeObject(this);
        }
    }

    /**
     * Animates the coin. 
     */
    private void animateCoin() {
        if (actCount % 5 == 0) {
            frameIndex = (frameIndex + 1) % coinFrames.length;
            setImage(coinFrames[frameIndex]);
        }
        actCount++;
    }
}
