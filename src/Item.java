import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Item extends Object implements Pickupable{
    private final static int ICON_X = 25;
    private final static int ICON_Y = 60;
    private final static int ICON_ADJUSTMENT = 40;
    private final static int IMAGE_LENGTH = 40;
    private final static int IMAGE_WIDTH = 32;
    private final static int FIRST = 1;
    private final static int SECOND = 2;
    private final static int THIRD = 3;
    protected static int currentIcons = 0;
    protected boolean pickedUp = false;
    protected int iconNumber = 0;
    protected int imageWidth;
    protected int imageLength;
    protected Image iconImage;

    /**
     * Constructor for item
     * @param x x coordinate
     * @param y y coordinate
     */
    public Item(int x, int y){
        super(x, y);
        this.imageLength = IMAGE_LENGTH;
        this.imageWidth = IMAGE_WIDTH;
    }

    /**
     * Method that performs state update
     * @param sailor sailor object
     */
    public void update(Sailor sailor) {
        if (!pickedUp){
            currentImage.drawFromTopLeft(x, y);
            checkCollision(sailor);
        }else {
            showIcon();
        }
    }

    /** This method is used to indicate that sailor had picked up item
     * @param sailor the sailor who picked up the item
     */
    @Override
    public void pickup(Sailor sailor) {
        pickedUp = true;
        currentIcons++;
        iconNumber = currentIcons;
    }

    /**
     * Method that checks if item has collided with sailor and picks up item if so
     * @param sailor sailor to collide with
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
        if (iconNumber == FIRST){
            iconImage.draw(ICON_X, ICON_Y);
        } else if (iconNumber == SECOND){
            iconImage.draw(ICON_X, ICON_Y + ICON_ADJUSTMENT);
        } else if (iconNumber == THIRD){
            iconImage.draw(ICON_X, ICON_Y + 2*ICON_ADJUSTMENT);
        }
    }
}
