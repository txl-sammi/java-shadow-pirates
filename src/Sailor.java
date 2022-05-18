import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Timer;

public class Sailor{
    private final static Image SAILOR_LEFT = new Image("res/sailor/sailorLeft.png");
    private final static Image SAILOR_RIGHT = new Image("res/sailor/sailorRight.png");
    private final static Image SAILOR_HIT_LEFT = new Image("res/sailor/sailorHitLeft.png");
    private final static Image SAILOR_HIT_RIGHT = new Image("res/sailor/sailorHitRight.png");
    private final static int IMAGE_WIDTH = 40;
    private final static int IMAGE_LENGTH = 58;
    private final static int MOVE_SIZE = 2;
    private final static int MAX_HEALTH_POINTS = 100;
    private final static int DAMAGE_POINTS = 15;

    private final static int WIN_X = 990;
    private final static int WIN_Y = 630;
    private final static int ATTACK_COOLDOWN = 2000;
    private final static int ATTACK_DURATION = 1000;

    private final static int HEALTH_X = 10;
    private final static int HEALTH_Y = 25;
    private final static int ORANGE_BOUNDARY = 65;
    private final static int RED_BOUNDARY = 35;
    private final static int FONT_SIZE = 30;
    private final static Font FONT = new Font("res/wheaton.otf", FONT_SIZE);
    private final static DrawOptions COLOUR = new DrawOptions();
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);


    private int healthPoints;
    private int oldX;
    private int oldY;
    private int x;
    private int y;
    private int bottomEdge;
    private int topEdge;
    private int leftEdge;
    private int rightEdge;
    private Image currentImage;
    private int lastAttackTime = (int) System.currentTimeMillis();
    private boolean inAttackState = false;

    public Sailor(int startX, int startY){
        this.x = startX;
        this.y = startY;
        this.healthPoints = MAX_HEALTH_POINTS;
        this.currentImage = SAILOR_RIGHT;
        COLOUR.setBlendColour(GREEN);
    }

    /**
     * Method that performs state update
     */
    public void update(Input input, Block[] blocks, Pirate[] pirates){
        // store old coordinates every time the sailor moves
        if (input.isDown(Keys.UP)){
            setOldPoints();
            move(0, -MOVE_SIZE);
        }else if (input.isDown(Keys.DOWN)){
            setOldPoints();
            move(0, MOVE_SIZE);
        }else if (input.isDown(Keys.LEFT)){
            setOldPoints();
            move(-MOVE_SIZE,0);
            if (inAttackState){
                currentImage = SAILOR_HIT_LEFT;
            } else { currentImage = SAILOR_LEFT; }
        }else if (input.isDown(Keys.RIGHT)){
            setOldPoints();
            move(MOVE_SIZE,0);
            if (inAttackState){
                currentImage = SAILOR_HIT_RIGHT;
            } else { currentImage = SAILOR_RIGHT; }
        }

        // check for attack key
        int now = (int) System.currentTimeMillis();
        if (input.isDown(Keys.S)){
            if (!(now - lastAttackTime <= ATTACK_COOLDOWN)){
                lastAttackTime = now;
            }
        }

        inAttackState = !(lastAttackTime + ATTACK_DURATION <= now);
        if (inAttackState){ attack(pirates); }
        checkCollisions(blocks);
        checkOutOfBound();
        currentImage.draw(x, y);
        renderHealthPoints();
    }

    /**
     * Method that checks for collisions between sailor and blocks
     */
    private void checkCollisions(Block[] blocks){
        // check collisions and print log
        Rectangle sailorBox = currentImage.getBoundingBoxAt(new Point(x, y));
        for (Block current : blocks) {
            Rectangle blockBox = current.getBoundingBox();
            if (sailorBox.intersects(blockBox)) {
                moveBack();
            }
        }
    }

    /**
     * Method that moves the sailor given the direction
     */
    private void move(int xMove, int yMove){
        x += xMove;
        y += yMove;
    }

    /**
     * Method that stores the old coordinates of the sailor
     */
    private void setOldPoints(){
        oldX = x;
        oldY = y;
    }

    /**
     * Method that moves the sailor back to its previous position
     */
    private void moveBack(){
        x = oldX;
        y = oldY;
    }

    /**
     * Method that renders the current health as a percentage on screen
     */
    private void renderHealthPoints(){
        double percentageHP = ((double) healthPoints/MAX_HEALTH_POINTS) * 100;
        if (percentageHP <= RED_BOUNDARY){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            COLOUR.setBlendColour(ORANGE);
        }
        FONT.drawString(Math.round(percentageHP) + "%", HEALTH_X, HEALTH_Y, COLOUR);
    }

    /**
     * Method that checks if sailor's health is <= 0
     */
    public boolean isDead(){
        return healthPoints <= 0;
    }

    /**
     * Method that checks if sailor has reached the ladder
     */
    public boolean hasWon(){
        return (x >= WIN_X) && (y > WIN_Y);
    }

    /**
     * Method that checks if sailor has gone out-of-bound
     */
    public void checkOutOfBound(){
        if ((y - IMAGE_LENGTH/2 < topEdge) || (y - IMAGE_LENGTH/2 > bottomEdge) || (x - IMAGE_WIDTH/2 < leftEdge) || (x > rightEdge)) {
            moveBack();
        };
    }

    public void attack(Pirate[] pirates){
        Rectangle sailorBox = currentImage.getBoundingBoxAt(new Point(x, y));
        for (Pirate pirate : pirates){
            if (!(pirate == null)) {
                Rectangle pirateBox = pirate.getBoundingBox();
                if (sailorBox.intersects(pirateBox)) {
                    pirate.reduceHealthPoints(DAMAGE_POINTS);
                }
            }
        }
    }

    public void setBound(int bottom, int top, int left, int right){
        this.bottomEdge = bottom;
        this.topEdge = top;
        this.leftEdge = left;
        this.rightEdge = right;
    }
}