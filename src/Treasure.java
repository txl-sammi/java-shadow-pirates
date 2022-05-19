public class Treasure extends Item{
    private final static int IMAGE_LENGTH = 40;
    private final static int IMAGE_WIDTH = 48;

    public Treasure(int x, int y){
        super(x,y);
        this.currentImage = TREASURE;
        this.imageLength = IMAGE_LENGTH;
        this.imageWidth = IMAGE_WIDTH;
    }

    /**
     * Method that sets boolean values to indicate that treasure has been gained
     */
    public void pickup(Sailor sailor){
        pickedUp = true;
        sailor.gotTreasure();
    }
}
