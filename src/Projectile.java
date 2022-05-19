import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Projectile{
    private final static int IMAGE_WIDTH = 64;
    private final static int IMAGE_LENGTH = 64;
    private Image projectileImage;
    private double speed;
    private int damagePoints;
    private double x;
    private double y;
    private boolean disappeared = false;
    private double moveX;
    private double moveY;
    private double direction;
    private int bottomEdge;
    private int topEdge;
    private int leftEdge;
    private int rightEdge;
    private DrawOptions directionToDraw = new DrawOptions();

    public Projectile(Image projectile, double speed, int damage, double x, double y, double direction){
        this.projectileImage = projectile;
        this.speed = speed;
        this.damagePoints = damage;
        this.x = x;
        this.y = y;
        this.direction = direction;
        directionToDraw.setRotation(direction);
    }

    public void update(Sailor sailor){
        if (!disappeared){
            // between 0 and pi/2
            moveX = Math.cos(direction)*speed;
            moveY = Math.sin(direction)*speed;

            move(moveX, moveY);
            checkOutOfBound();
            checkCollision(sailor);
            projectileImage.draw(x,y, directionToDraw);
        }
    }

    public void checkCollision(Sailor sailor){
        Rectangle sailorBox = sailor.getBoundingBox();
        Point projectilePoint = new Point(x, y);
        if (sailorBox.intersects(projectilePoint)) {
            sailor.reduceHealthPoints(damagePoints);
            disappeared = true;
        }
    }

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
