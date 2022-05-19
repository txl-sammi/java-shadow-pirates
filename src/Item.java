import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Item extends Object implements Pickupable{
    private final static int ICON_X = 25;
    private final static int ICON_Y = 60;
    private final static int ICON_ADJUSTMENT = 40;
    private final static int IMAGE_LENGTH = 40;
    private final static int IMAGE_WIDTH = 32;
    protected static int currentIcons = 0;
    protected boolean pickedUp = false;
    protected int iconNumber = 0;
    protected int imageWidth;
    protected int imageLength;
    protected Image iconImage;

    public Item(int x, int y){
        super(x, y);
        this.imageLength = IMAGE_LENGTH;
        this.imageWidth = IMAGE_WIDTH;
    }

    /**
     * Method that performs state update
     */
    public void update(Sailor sailor) {
        if (!pickedUp){
            currentImage.drawFromTopLeft(x, y);
            checkCollision(sailor);
        }else {
            showIcon();
        }
    }

    @Override
    public void pickup(Sailor sailor) {
        pickedUp = true;
        currentIcons++;
        iconNumber = currentIcons;
    }

    /**
     * Method that checks if item has collided with sailor and picks up item if so
     */
    public void checkCollision(Sailor sailor) {
        Rectangle itemBox = currentImage.getBoundingBoxAt(new Point(x, y));

        Rectangle sailorBox = sailor.getBoundingBox();
        if (sailorBox.intersects(itemBox)) {
            pickup(sailor);
        }
    }

    /**
     * Method that renders the icons of picked up items
     */
    public void showIcon(){
        if (iconNumber == 1){
            iconImage.draw(ICON_X, ICON_Y);
        } else if (iconNumber == 2){
            iconImage.draw(ICON_X, ICON_Y + ICON_ADJUSTMENT);
        } else if (iconNumber == 3){
            iconImage.draw(ICON_X, ICON_Y + 2*ICON_ADJUSTMENT);
        }
    }
}
