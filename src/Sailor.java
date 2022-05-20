import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Sailor extends Object implements Attackable{
    private final static double IMAGE_WIDTH = 40;
    private final static double IMAGE_LENGTH = 58;
    private final static int MOVE_SIZE = 1;
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

    private double oldX;
    private double oldY;
    private int bottomEdge;
    private int topEdge;
    private int leftEdge;
    private int rightEdge;
    private int maxHealth;
    private int damagePoints;
    private boolean isLeft = false;
    private boolean hasTreasure = false;
    private int lastAttackTime = (int) System.currentTimeMillis() - ATTACK_COOLDOWN;
    private boolean inAttackState = false;

    /**
     * Constructor for Sailor object
     * @param startX starting x coordinates
     * @param startY starting y coordinates
     */
    public Sailor(int startX, int startY){
        super(startX, startY);
        this.maxHealth = MAX_HEALTH_POINTS;
        this.healthPoints = MAX_HEALTH_POINTS;
        this.damagePoints = DAMAGE_POINTS;
        this.currentImage = SAILOR_RIGHT;
        COLOUR.setBlendColour(GREEN);
    }

    /**
     * Method that performs state update
     * @param input player input
     * @param blocks list of blocks
     * @param pirates list of pirates
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
            isLeft = true;
            setOldPoints();
            move(-MOVE_SIZE,0);
        }else if (input.isDown(Keys.RIGHT)){
            isLeft = false;
            setOldPoints();
            move(MOVE_SIZE,0);
        }

        setAttackImage();

        // check for attack key
        int now = (int) System.currentTimeMillis();
        if (input.isDown(Keys.S)){
            if (!(now - lastAttackTime <= ATTACK_COOLDOWN)){
                lastAttackTime = now;
            }
        }

        // check if in attack state
        inAttackState = !(lastAttackTime + ATTACK_DURATION <= now);
        if (inAttackState){ checkPirateCollisions(pirates);}

        currentImage.draw(x, y);
        checkCollisions(blocks);
        checkOutOfBound();
        renderHealthPoints();
    }

    /**
     * Method that checks for collisions between sailor and blocks
     * @param blocks list of blocks
     */
    private void checkCollisions(Block[] blocks){
        // check collisions and print log
        Rectangle sailorBox = currentImage.getBoundingBoxAt(new Point(x, y));
        for (Block current : blocks) {
            if (current != null){
                Rectangle blockBox = current.getBoundingBox();
                if (sailorBox.intersects(blockBox)) {
                    if (!current.isExploding() && current.isExplodable()){
                        current.exploding(this);
                    }
                    if (!current.hasDisappeared()){
                        moveBack();
                    }
                }
            }
        }
    }

    /**
     * Method that moves the sailor given the direction
     * @param xMove x direction
     * @param yMove y direction
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
        double percentageHP =  (healthPoints/maxHealth) * 100;
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
        return ((x >= WIN_X) && (y > WIN_Y));
    }

    /**
     * Method that checks if sailor has gone out-of-bound
     */
    public void checkOutOfBound(){
        if ((y - IMAGE_LENGTH/2 < topEdge) || (y - IMAGE_LENGTH/2 > bottomEdge) || (x - IMAGE_WIDTH/2 < leftEdge) || (x > rightEdge)) {
            moveBack();
        }
    }

    /**
     * Method that sets that the sailor has got the treasure
     */
    public void gotTreasure(){
        hasTreasure = true;
    }

    /**
     * Method that returns whether sailor has got the treasure
     */
    public boolean hasTreasure(){
        return hasTreasure;
    }

    /**
     * Method that boosts sailors max health with given value
     * @param boost integer value of how much to boost
     */
    public void boostMaxHealth(int boost){
        maxHealth = MAX_HEALTH_POINTS + boost;
        healthPoints = maxHealth;
    }

    /**
     * Method that increases sailors health with given value
     * @param boost integer value of how much to boost
     */
    public void increaseHealth(int boost){
        if (healthPoints + boost >= MAX_HEALTH_POINTS){
            healthPoints = MAX_HEALTH_POINTS;
        }
        else{
            healthPoints = healthPoints + boost;
        }
    }

    /**
     * Method that increases sailor damage with given value
     * @param boost integer value of how much to boost
     */
    public void increaseDamage(int boost){
        damagePoints = damagePoints + boost;
    }

    /**
     * Method that sets the correct attack image
     */
    public void setAttackImage(){
        if (inAttackState && isLeft){
            currentImage = SAILOR_HIT_LEFT;
        } else if(inAttackState && !isLeft){
            currentImage = SAILOR_HIT_RIGHT;
        } else if (!inAttackState && isLeft){
            currentImage = SAILOR_LEFT;
        } else if (!inAttackState && !isLeft){
            currentImage = SAILOR_RIGHT;
        }
    }

    /**
     * Method that checks if sailor has collided with any pirates
     * @param pirates list of pirates
     */
    public void checkPirateCollisions(Pirate[] pirates){
        Rectangle sailorBox = currentImage.getBoundingBoxAt(new Point(x, y));
        for (Pirate pirate : pirates){
            if (!(pirate == null)) {
                Rectangle pirateBox = pirate.getBoundingBox();
                if (sailorBox.intersects(pirateBox)) {
                    pirate.reduceHealthPoints(damagePoints);
                }
            }
        }
    }

    /**
     * Method use to attack given object with damage points
     * @param object objects to inflict damage to
     */
    @Override
    public void attack(Object object) {
        object.reduceHealthPoints(damagePoints);
    }

    /**
     * Method that reduces sailors health points by given value
     * @param damage value of how much damage to inflict
     */
    public void reduceHealthPoints(int damage) {
        healthPoints = healthPoints - damage;
    }

    /**
     * Method used to set game boundaries
     * @param bottom bottom boundary
     * @param top top boundary
     * @param left left boundary
     * @param right right boundary
     */
    public void setBound(int bottom, int top, int left, int right){
        this.bottomEdge = bottom;
        this.topEdge = top;
        this.leftEdge = left;
        this.rightEdge = right;
    }

    /**
     * Method used to get health points
     * @return returns sailor health points
     */
    public int getHealth(){
        return (int) healthPoints;
    }

    /**
     * Method used to get max health
     * @return returns sailor max health
     */
    public int getMaxHealth(){
        return maxHealth;
    }

    /**
     * Method used to get damage points
     * @return returns sailor damage points
     */
    public int getDamagePoints(){
        return damagePoints;
    }

}