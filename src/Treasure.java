public class Treasure extends Item{
    private final static int IMAGE_LENGTH = 40;
    private final static int IMAGE_WIDTH = 48;

    public Treasure(int x, int y){
        super(x,y);
        this.currentImage = TREASURE;
        this.imageLength = IMAGE_LENGTH;
        this.imageWidth = IMAGE_WIDTH;
    }
}
