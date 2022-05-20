public class Treasure extends Item{
    private final static int IMAGE_LENGTH = 40;
    private final static int IMAGE_WIDTH = 48;

    /** Constructor for treasure item which stores its location
     * @param x the x coordinates for treasure
     * @param y the y coordinates for treasure
     */
    public Treasure(int x, int y){
        super(x,y);
        this.currentImage = TREASURE;
        this.imageLength = IMAGE_LENGTH;
        this.imageWidth = IMAGE_WIDTH;
    }

    /** This method is used to indicate that sailor had picked up treasure
     * @param sailor the sailor who picked up the item
     */
    public void pickup(Sailor sailor){
        pickedUp = true;
        sailor.gotTreasure();
    }
}
