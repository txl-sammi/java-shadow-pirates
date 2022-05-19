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
    private final static double MIN_SPEED = 0.2;
    private final static int MAX_DAMAGE = 10;
    private final static double PROJECTILE_SPEED = 0.4;
    private final static int MAX_HEALTH_POINTS = 45;
    private final static int ORANGE_BOUNDARY = 65;
    private final static int RED_BOUNDARY = 35;
    private final static int FONT_SIZE = 15;
    private final static int IMAGE_WIDTH = 40;
    private final static int IMAGE_LENGTH = 58;
    private final static int INVINCIBLE_DURATION = 1500;
    private final static int ATTACK_RANGE = 100;

    private final static Font FONT = new Font("res/wheaton.otf", FONT_SIZE);

    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);
    private final static List<String> DIRECTION_LIST = new ArrayList<>();
    private List<Object> projectileStats = new ArrayList<Object>();

    private DrawOptions colour = new DrawOptions();
    private double x;
    private double y;
    private double oldX;
    private double oldY;
    private int bottomEdge;
    private int topEdge;
    private int leftEdge;
    private int rightEdge;
    private int healthPoints;
    private int currentHealth = healthPoints;
    private boolean isInvincible = false;
    private boolean isLeft = false;

    private int lastHurtTime = (int) System.currentTimeMillis() - INVINCIBLE_DURATION;
    private boolean inAttackState = false;
    private Image currentImage;
    private double speed = MIN_SPEED + (Math.random() * (MAX_SPEED - MIN_SPEED));
    private String direction = getRandomDirection(DIRECTION_LIST);

    public Pirate(int startX, int startY){
        this.x = startX;
        this.y = startY;
        this.healthPoints = MAX_HEALTH_POINTS;
        this.currentImage = PIRATE_RIGHT;
        colour.setBlendColour(GREEN);
    }

    public void update(Sailor sailor, Block[] blocks){
        // store old coordinates every time the pirate moves

        if(!(isDead())) {
            if (Objects.equals(direction, "left")) {
                setOldPoints();
                isLeft = true;
                move(-speed, 0);
            } else if (Objects.equals(direction, "right")) {
                setOldPoints();
                isLeft = false;
                move(speed, 0);
            } else if (Objects.equals(direction, "up")) {
                setOldPoints();
                move(0, -speed);
            } else if (Objects.equals(direction, "down")) {
                setOldPoints();
                move(0, speed);
            }
            renderInvincible();

            int now = (int) System.currentTimeMillis();
            isInvincible = !((lastHurtTime + INVINCIBLE_DURATION <= now));

            checkCollisions(blocks);
            checkOutOfBound();
            currentImage.draw(x, y);
            renderHealthPoints();

        }
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
                changeDirection();
                moveBack();
                break;
            }
        }
    }

    /**
     * Method that changes the direction of the pirates movement
     */
    public void changeDirection(){
        if (Objects.equals(direction, "left")) {
            direction = "right";
        } else if (Objects.equals(direction, "right")) {
            direction = "left";
        } else if (Objects.equals(direction, "up")) {
            direction = "down";
        } else if (Objects.equals(direction, "down")) {
            direction = "up";
        }
    }

    /**
     * Method that returns a random direction
     */
    public String getRandomDirection(List<String> list){
        list.add("left");
        list.add("right");
        list.add("up");
        list.add("down");

        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    /**
     * Method that checks if the pirate is out of bound
     */
    public void checkOutOfBound(){
        if ((y - IMAGE_LENGTH/2 < topEdge) || (y - IMAGE_LENGTH/2 > bottomEdge) || (x - IMAGE_WIDTH/2 < leftEdge) || (x > rightEdge)) {
            moveBack();
            changeDirection();
        };
    }

    /**
     * Method that moves the pirate
     */
    private void move(double xMove, double yMove){
        x += xMove;
        y += yMove;
    }

    /**
     * Method that moves the pirate back to its previous position
     */
    private void moveBack(){
        x = oldX;
        y = oldY;
    }

    /**
     * Method that stores the old coordinates of the pirate
     */
    private void setOldPoints(){
        oldX = x;
        oldY = y;
    }

    private void renderInvincible(){
        if (isInvincible && isLeft){
            currentImage = PIRATE_HIT_LEFT;
        } else if (isInvincible && !isLeft){
            currentImage = PIRATE_HIT_RIGHT;
        } else if (!isInvincible && isLeft) {
            currentImage = PIRATE_LEFT;
        } else if (!isInvincible && !isLeft){
            currentImage = PIRATE_RIGHT;
        }
    }

    /**
     * Method that renders the current health of pirate as a percentage above them
     */
    private void renderHealthPoints(){
        double percentageHP = ((double) healthPoints/MAX_HEALTH_POINTS) * 100;
        if (percentageHP <= RED_BOUNDARY){
            colour.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            colour.setBlendColour(ORANGE);
        }
        FONT.drawString(Math.round(percentageHP) + "%", x - IMAGE_WIDTH/2, y-6 - IMAGE_LENGTH/2, colour);
    }

    /**
     * Method that reduces pirates health points if they are not invincible
     */
    public void reduceHealthPoints(int damage) {
        if (!isInvincible){
            healthPoints = healthPoints - damage;
            int now = (int) System.currentTimeMillis();
            lastHurtTime = now;
        }
    }

    public boolean canShoot(Sailor sailor){
        Rectangle sailorBox = sailor.getRectangle();
        Rectangle pirateBox = new Rectangle(x - ATTACK_RANGE/2, y - ATTACK_RANGE/2, ATTACK_RANGE, ATTACK_RANGE);
        Drawing drawing = new Drawing();
        drawing.drawRectangle(x - ATTACK_RANGE/2, y - ATTACK_RANGE/2, ATTACK_RANGE, ATTACK_RANGE, Colour.RED);

        if(pirateBox.intersects(sailorBox)){
            System.out.println("shoot now");
            return true;
        }
        return false;
    }

    public double directionFromSailor(Sailor sailor){
        int sailorX = sailor.getX();
        int sailorY = sailor.getY();

        double differenceX = x - sailorX;
        double differenceY = y - sailorY;
        return Math.atan(differenceY/differenceX);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getSpeed(){
        return speed;
    }

    public int getDamage(){
        return MAX_DAMAGE;
    }

    public Rectangle getBoundingBox(){
        return currentImage.getBoundingBoxAt(new Point(x, y));
    }

    public boolean isDead(){
        return healthPoints <= 0;
    }

    public void setBound(int bottom, int top, int left, int right){
        this.bottomEdge = bottom;
        this.topEdge = top;
        this.leftEdge = left;
        this.rightEdge = right;
    }
}
