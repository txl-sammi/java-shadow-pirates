import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Projectile implements Attackable{
    private final static double IMAGE_WIDTH = 64;
    private final static double IMAGE_LENGTH = 64;
    private final Image projectileImage;
    private final double speed;
    private final int damagePoints;
    private double x;
    private double y;
    private boolean disappeared = false;
    private final double direction;
    private int bottomEdge;
    private int topEdge;
    private int leftEdge;
    private int rightEdge;
    private final DrawOptions directionToDraw = new DrawOptions();

    public Projectile(Image projectile, double speed, int damage, double x, double y, double direction){
        this.projectileImage = projectile;
        this.speed = speed;
        this.damagePoints = damage;
        this.x = x;
        this.y = y;
        this.direction = direction;
        directionToDraw.setRotation(direction);
    }

    /**
     * Method that performs state update
     */
    public void update(Sailor sailor){
        if (!disappeared){
            double moveX = Math.cos(direction) * speed;
            double moveY = Math.sin(direction) * speed;

            move(moveX, moveY);
            checkOutOfBound();
            attack(sailor);
            projectileImage.draw(x,y, directionToDraw);
        }
    }

    /**
     * Method that determines if the projectile has collided with the sailor
     */
    @Override
    public void attack(Object sailor){
        Rectangle sailorBox = sailor.getBoundingBox();
        Point projectilePoint = new Point(x, y);
        if (sailorBox.intersects(projectilePoint)) {
            sailor.reduceHealthPoints(damagePoints);
            disappeared = true;
            System.out.println("Pirate inflicts " + damagePoints + " damage points on Sailor. Sailor's current health: " + sailor.getHealth() + "/" + sailor.getMaxHealth());

        }
    }

    /**
     * Method that moves the projectile to the given direction
     */
    private void move(double xMove, double yMove){
        x += xMove;
        y += yMove;
    }

    public boolean hasDisappeared(){
        return disappeared;
    }

    public void checkOutOfBound(){
        if ((y - IMAGE_LENGTH/2 < topEdge) || (y - IMAGE_LENGTH/2 > bottomEdge) || (x - IMAGE_WIDTH/2 < leftEdge) || (x > rightEdge)) {
            disappeared = true;
        }
    }

    public void setBound(int bottom, int top, int left, int right){
        this.bottomEdge = bottom;
        this.topEdge = top;
        this.leftEdge = left;
        this.rightEdge = right;
    }
}
