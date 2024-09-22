import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class SuperDisplayLabel extends Actor //diplays the label for game 
{

    private static final double HEIGHT_RATIO = 2.0;

    //private static final Color DEFAULT_BACKGROUND_COLOR = new Color (217, 199, 177);
    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 0, 0); 
    private static final Color DEFAULT_TEXT_COLOR = new Color(101, 67, 33);

    private static final Font DEFAULT_FONT = new Font ("Courier", true, false, 20);

    private GreenfootImage image;
    private Color backColor;
    private Color foreColor;
    private Font textFont;
    private String text;
    private String lastOutput;
    private String[] labels;
    //private String[] stringValues;
    private int[] intValues;
    private int centeredX;
    private int bottomY, height;

    
    public SuperDisplayLabel(Color backColor, Color foreColor, Font font, int height){

        //System.out.println(bottomY);
        // Declare colour objects for use within this class (red and white)
        this.backColor = backColor;
        this.foreColor = foreColor;
        // Initialize the font - chose Courier because it's evenly spaced
        textFont = font;
        this.height = height;
        text = "";

    }

    public SuperDisplayLabel(Color backColor, Color foreColor, Font font){
        this (backColor, foreColor, font, (int)(font.getSize() * HEIGHT_RATIO));
    }

    public SuperDisplayLabel (Font font)
    {
        this (DEFAULT_BACKGROUND_COLOR, DEFAULT_TEXT_COLOR , font);      
    }

    public SuperDisplayLabel (){
        this (DEFAULT_FONT);
    }

    public void setLabels (String[] labels){
        this.labels = labels;
    }

    public void addedToWorld (World w){
        //image = new GreenfootImage (w.getWidth()/2, height);
        image = new GreenfootImage (450, height); 

        bottomY = image.getHeight() - (int)((image.getHeight() - textFont.getSize())/1.8);

        // Set the colour to red and fill the background of this rectangle
        image.setColor(backColor);
        image.fill();
        image.setFont(textFont);
        // Assign the image we just created to be the image representing THIS actor
        this.setImage (image);
        // Prepare the font for use within the code
        //setLocation (w.getWidth() / 2, w.getHeight() - getImage().getHeight() / 2);
        //setLocation (445, 655);
    }

    public void update (String[] labels, int[] newValues)
    {
        if (labels.length != newValues.length){
            System.out.println("ERROR - Both arrays must be the same size");
        }
        this.labels = labels;
        this.intValues = newValues;
        // Arrays should be the same size, but just in case, only loop to the lower length to avoid crashing
        int loops = Math.min (labels.length, newValues.length);
        String text = "";
        for (int i = 0; i < loops; i++){
            text += labels[i] + " " + newValues[i];
            if (i < loops - 1){
                text += " "; // extra space, but not for the very last loop
            }

        }
        update (text, true);
    }

    public void update (int[] newValues)
    {
        update (labels, newValues);
    }

    public void update () {
        update (labels, intValues);
    }

    public void update (String output){
        update (output, true);
    }

    public void update(String output, boolean recenter) {
        // Create a new GreenfootImage to clear the old content
        image = new GreenfootImage(450, height);
        image.setColor(backColor);
        image.fill();

        // Write text over the solid background
        image.setColor(foreColor);
        image.setFont(textFont);

        // Draw the text onto the image
        if (recenter) {
            // Smart piece of code that centers text
            centeredX = image.getWidth() / 2 - getStringWidth(textFont, output) / 2;
        }

        image.drawString(output, centeredX, bottomY);
        setImage(image);  // Update the actor's image
        lastOutput = output;
    }

    public static void drawCenteredText (GreenfootImage canvas, String text, int middleX, int bottomY){
        canvas.drawString (text, middleX - (getStringWidth(canvas.getFont(), text)/2), bottomY);
    }

    public static void drawCenteredText (GreenfootImage canvas, String text, int bottomY){
        canvas.drawString (text, canvas.getWidth()/2 - (getStringWidth(canvas.getFont(), text)/2), bottomY);
    }

    public static int getStringWidth (Font font, String text){
        // Dividing font size by 1.2 should work for even the widest fonts, as fonts are
        // taller than wide. For example, a 24 point font is usually 24 points tall 
        // height varies by character but even a w or m should be less than 20 wide
        // 24 / 1.2 = 20
        int maxWidth = (int)(text.length() * (font.getSize()/1.20));//1000; 
        int fontSize = font.getSize();
        int marginOfError = fontSize / 6; // how many pixels can be skipped scanning vertically for pixels?
        int checkX;

        GreenfootImage temp = new GreenfootImage (maxWidth, fontSize);
        temp.setFont(font);
        temp.drawString (text, 0, fontSize);

        //int testValue = 1000;
        boolean running = true;

        checkX = maxWidth - 1;
        while(running){
            boolean found = false;
            for (int i = fontSize - 1; i >= 0 && !found; i-=marginOfError){

                if (temp.getColorAt(checkX, i).getAlpha() != 0){
                    // This lets me only look at every other pixel on the first run - check back one to the right
                    // when I find a pixel to see if I passed the first pixel or not. This should almost half the 
                    // total calls to getColorAt().
                    if (temp.getColorAt(checkX + 1, i).getAlpha() != 0){
                        checkX++;
                        if (temp.getColorAt(checkX + 1, i).getAlpha() != 0){
                            checkX++;
                        }
                    }
                    found = true;
                }
            }
            if (found){
                return checkX;
            }
            checkX-=3; // shift 3 pixels at a time in my search - above code will make sure I don't miss anything
            if (checkX <= marginOfError)
                running = false;
        }
        return 0;

    }

    public void remove(){

    }
}