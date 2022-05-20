import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Pirate extends Object{
    protected final static double MAX_SPEED = 0.7;
    protected final static double MIN_SPEED = 0.2;
    protected final static int MAX_DAMAGE = 10; //
    protected final static double PROJECTILE_SPEED = 0.4; //
    protected final static int MAX_HEALTH_POINTS = 45; //
    private final static int ORANGE_BOUNDARY = 65;
    private final static int RED_BOUNDARY = 35;
    protected final static double IMAGE_WIDTH = 40;
    protected final static double IMAGE_LENGTH = 58;
    protected final static int INVINCIBLE_DURATION = 1500;
    protected final static int SHOOT_COOLDOWN = 3000; //
    protected final static double ATTACK_RANGE = 100; //

    protected final static Colour GREEN = new Colour(0, 0.8, 0.2);
    protected final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    protected final static Colour RED = new Colour(1, 0, 0);
    protected final static List<String> DIRECTION_LIST = new ArrayList<>();
    protected final DrawOptions colour = new DrawOptions();
    protected Image projectile;
    protected Image left;
    protected Image right;
    protected Image leftHit;
    protected Image rightHit;
    protected int maxHealth;
    protected int maxDamage;
    protected double projectileSpeed;
    protected int shootCooldown;
    protected double attackRange;

    private double oldX;
    private double oldY;
    private int bottomEdge;
    private int topEdge;
    private int leftEdge;
    private int rightEdge;
    private boolean isInvincible = false;
    private boolean isLeft;
    private boolean onCooldown = false;
    private int lastHurtTime = (int) System.currentTimeMillis() - INVINCIBLE_DURATION;
    private int lastShootTime = (int) System.currentTimeMillis() - shootCooldown;
    private final double speed = MIN_SPEED + (Math.random() * (MAX_SPEED - MIN_SPEED));
    private String direction = getRandomDirection(DIRECTION_LIST);

    /**
     * Constructor for pirate
     * @param startX starting x coordinate
     * @param startY starting y coordinate
     */
    public Pirate(int startX, int startY){
        super(startX, startY);
        this.healthPoints = MAX_HEALTH_POINTS;
        this.currentImage = PIRATE_RIGHT;
        colour.setBlendColour(GREEN);
        this.left = PIRATE_LEFT;
        this.right = PIRATE_RIGHT;
        this.leftHit = PIRATE_HIT_LEFT;
        this.rightHit = PIRATE_HIT_RIGHT;
        this.maxHealth = MAX_HEALTH_POINTS;
        this.maxDamage = MAX_DAMAGE;
        this.projectileSpeed = PROJECTILE_SPEED;
        this.shootCooldown = SHOOT_COOLDOWN;
        this.attackRange = ATTACK_RANGE;
        this.projectile = PIRATE_PROJECTILE;
    }

    /**
     * Method that performs state update
     * @param blocks list of blocks used to check collisions
     */
    public void update(Block[] blocks){
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
            renderImage();

            // check for invincible and cooldown durations
            int now = (int) System.currentTimeMillis();
            isInvincible = !((lastHurtTime + INVINCIBLE_DURATION <= now));
            onCooldown = !((lastShootTime + shootCooldown <= now));


            checkCollisions(blocks);
            checkOutOfBound();
            currentImage.draw(x, y);
            renderHealthPoints();
        }
    }

    /**
     * Method that checks for collisions between pirate and blocks
     * @param blocks list of blocks
     */
    private void checkCollisions(Block[] blocks){
        // check collisions and print log
        Rectangle pirateBox = currentImage.getBoundingBoxAt(new Point(x, y));
        for (Block current : blocks) {
            if (current != null){
                Rectangle blockBox = current.getBoundingBox();
                if (pirateBox.intersects(blockBox)) {
                    changeDirection();
                    moveBack();
                    break;
                }
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
     * @param list list of possible directions
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
        }
    }

    /**
     * Method that moves the projectile to the given direction
     * @param xMove x direction
     * @param yMove y direction
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

    /**
     * Method that assigns the correct image to current image
     */
    private void renderImage(){
        if (isInvincible && isLeft){
            currentImage = leftHit;
        } else if (isInvincible && !isLeft){
            currentImage = rightHit;
        } else if (!isInvincible && isLeft) {
            currentImage = left;
        } else if (!isInvincible && !isLeft){
            currentImage = right;
        }
    }

    /**
     * Method that renders the current health of pirate as a percentage above them
     */
    private void renderHealthPoints(){
        double percentageHP = (healthPoints/maxHealth) * 100;
        if (percentageHP <= RED_BOUNDARY){
            colour.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            colour.setBlendColour(ORANGE);
        }
        FONT.drawString(Math.round(percentageHP) + "%", x - IMAGE_WIDTH/2, y-6 - IMAGE_LENGTH/2, colour);
    }

    /**
     * Method that reduces pirate health points by given value
     * @param damage value of how much damage to inflict
     */
    public void reduceHealthPoints(int damage) {
        if (!isInvincible && !isDead()){
            healthPoints = healthPoints - damage;
            lastHurtTime = (int) System.currentTimeMillis();
            if (this.getClass().getName().equals("Pirate")){
                System.out.println("Sailor inflicts " + damage + " damage points on Pirate. Pirate's current health: " + getHealth() + "/" + getMaxHealth());
            }
            else if (this.getClass().getName().equals("Blackbeard")){
                System.out.println("Sailor inflicts " + damage + " damage points on Blackbeard. Blackbeard's current health: " + getHealth() + "/" + getMaxHealth());
            }
        }
    }

    /**
     * Method that checks if the sailor is within the pirates attack range
     * @param sailor sailor to shoot
     */
    public boolean canShoot(Sailor sailor){
        Rectangle sailorBox = sailor.getBoundingBox();
        Rectangle pirateBox = new Rectangle(x - attackRange/2, y - attackRange/2, attackRange, attackRange);

        return pirateBox.intersects(sailorBox);
    }


    /**
     * Method that determines the direction the bullet with be rotated in radians
     * @param sailor sailor to shoot
     */
    public double directionFromSailor(Sailor sailor){
        double sailorX = sailor.getX();
        double sailorY = sailor.getY();
        double adjustment = 0;

        double differenceX = x - sailorX;
        double differenceY = y - sailorY;

        if (differenceX > 0){
            adjustment = Math.PI;
        }
        return Math.atan(differenceY/differenceX) - adjustment;
    }

    /**
     * Method that returns a projectile if the pirate's cooldown is over
     * @param sailor sailor to shoot
     * @return returns a projectile object
     */
    public Projectile shoot(Sailor sailor){
        if (!onCooldown){
            lastShootTime = (int) System.currentTimeMillis();
            return new Projectile(projectile, projectileSpeed, maxDamage, x, y, directionFromSailor(sailor));
        }
        return null;
    }

    /**
     * Method that checks if pirate has died
     * @returns whether health points is less than or equal to 0
     */
    public boolean isDead(){
        return healthPoints <= 0;
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
     * Method that gets pirate current health
     * @return returns pirate current health points
     */
    public int getHealth(){
        return (int) healthPoints;
    }

    /**
     * Method that gets pirates max health
     * @return returns pirates max health
     */
    public int getMaxHealth(){
        return maxHealth;
    }

}
