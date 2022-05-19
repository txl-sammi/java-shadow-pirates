import bagel.*;

public class Projectile {
    private Image projectileImage;
    private double speed;
    private int damagePoints;
    private double x;
    private double y;
    private boolean disappeared = false;

    private DrawOptions directionToDraw = new DrawOptions();

    public Projectile(Image projectile, double speed, int damage, double x, double y, double direction){
        this.projectileImage = projectile;
        this.speed = speed;
        this.damagePoints = damage;
        this.x = x;
        this.y = y;
        directionToDraw.setRotation(direction);
    }

    public void update(){
        projectileImage.draw(x,y, directionToDraw);
        
    }

    private void move(double xMove, double yMove){
        x += xMove;
        y += yMove;
    }

    public boolean hasDisappeared(){
        return disappeared;
    }
}
