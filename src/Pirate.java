import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Pirate extends Level{
    private final static Image PIRATE_LEFT = new Image("res/pirate/pirateLeft.png");
    private final static Image PIRATE_RIGHT = new Image("res/pirate/pirateRight.png");
    private final static Image PIRATE_HIT_LEFT = new Image("res/pirate/pirateHitLeft.png");
    private final static Image PIRATE_HIT_RIGHT = new Image("res/pirate/pirateHitRight.png");
    private final static double MAX_SPEED = 0.7;
    private final static int MAX_DAMAGE = 10;
    private final static int MAX_HEALTH_POINTS = 45;
    private final static int FONT_SIZE = 15;
    private final static int IMAGE_WIDTH = 40;
    private final static int IMAGE_LENGTH = 58;

    private final static Font FONT = new Font("res/wheaton.otf", FONT_SIZE);
    private final static DrawOptions COLOUR = new DrawOptions();
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);
    private final static List<String> list = new ArrayList<>();

    private int x;
    private int y;
    private int oldX;
    private int oldY;
    private int bottomEdge;
    private int topEdge;
    private int leftEdge;
    private int rightEdge;
    private int healthPoints;
    private Image currentImage;
    private double speed = Math.random() * MAX_SPEED;
    private String direction = getRandomDirection(list);

    public Pirate(int startX, int startY){
        this.x = startX;
        this.y = startY;
        this.healthPoints = MAX_HEALTH_POINTS;
        this.currentImage = PIRATE_RIGHT;
        COLOUR.setBlendColour(GREEN);
    }

    public String getRandomDirection(List<String> list){
        list.add("left");
        list.add("right");
        list.add("up");
        list.add("down");

        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }


    public void update(Block[] blocks){
        System.out.println(direction);
        // store old coordinates every time the pirate moves
        if (Objects.equals(direction, "left")) {
            setOldPoints();
            move(-speed, 0);
            currentImage = PIRATE_LEFT;
        } else if (Objects.equals(direction, "right")) {
            setOldPoints();
            move(speed, 0);
            currentImage = PIRATE_RIGHT;
        } else if (Objects.equals(direction, "up")) {
            setOldPoints();
            move(0, -speed);
        } else if (Objects.equals(direction, "down")) {
            setOldPoints();
            move(0, speed);
        }

        checkCollisions(blocks);
        checkOutOfBound();
        currentImage.draw(x, y);
    }

    /**
     * Method that checks for collisions between sailor and blocks
     */
    private void checkCollisions(Block[] blocks){
        // check collisions and print log
        Rectangle pirateBox = currentImage.getBoundingBoxAt(new Point(x, y));
        for (Block current : blocks) {
            Rectangle blockBox = current.getBoundingBox();
            if (pirateBox.intersects(blockBox)) {
                if (Objects.equals(direction, "left")) {
                    direction = "right";
                } else if (Objects.equals(direction, "right")) {
                    direction = "left";
                } else if (Objects.equals(direction, "up")) {
                    direction = "down";
                } else if (Objects.equals(direction, "down")) {
                    direction = "up";
                }
                moveBack();
            }
        }
    }

    public void checkOutOfBound(){
        if ((y - IMAGE_LENGTH/2 < topEdge) || (y - IMAGE_LENGTH/2 > bottomEdge) || (x - IMAGE_WIDTH/2 < leftEdge) || (x > rightEdge)) {
            moveBack();
        };
    }

    public void setBound(int bottom, int top, int left, int right){
        this.bottomEdge = bottom;
        this.topEdge = top;
        this.leftEdge = left;
        this.rightEdge = right;
    }

    private void move(double xMove, double yMove){
        x += xMove;
        y += yMove;
    }

    /**
     * Method that moves the sailor back to its previous position
     */
    private void moveBack(){
        x = oldX;
        y = oldY;
    }

    /**
     * Method that stores the old coordinates of the sailor
     */
    private void setOldPoints(){
        oldX = x;
        oldY = y;
    }

}
