import bagel.Font;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Object {
    protected final static Image BLOCK = new Image("res/block.png");
    protected final static Image BOMB = new Image("res/bomb.png");
    protected final static Image EXPLOSION = new Image("res/explosion.png");
    protected final static Image TREASURE = new Image("res/treasure.png");

    protected final static Image SAILOR_LEFT = new Image("res/sailor/sailorLeft.png");
    protected final static Image SAILOR_RIGHT = new Image("res/sailor/sailorRight.png");
    protected final static Image SAILOR_HIT_LEFT = new Image("res/sailor/sailorHitLeft.png");
    protected final static Image SAILOR_HIT_RIGHT = new Image("res/sailor/sailorHitRight.png");

    protected final static Image PIRATE_LEFT = new Image("res/pirate/pirateLeft.png");
    protected final static Image PIRATE_RIGHT = new Image("res/pirate/pirateRight.png");
    protected final static Image PIRATE_HIT_LEFT = new Image("res/pirate/pirateHitLeft.png");
    protected final static Image PIRATE_HIT_RIGHT = new Image("res/pirate/pirateHitRight.png");
    protected final static Image PIRATE_PROJECTILE = new Image("res/pirate/pirateProjectile.png");

    protected final static Image BLACKBEARD_LEFT = new Image("res/blackbeard/blackbeardLeft.png");
    protected final static Image BLACKBEARD_RIGHT = new Image("res/blackbeard/blackbeardRight.png");
    protected final static Image BLACKBEARD_HIT_LEFT = new Image("res/blackbeard/blackbeardHitLeft.png");
    protected final static Image BLACKBEARD_HIT_RIGHT = new Image("res/blackbeard/blackbeardHitRight.png");
    protected final static Image BLACKBEARD_PROJECTILE = new Image("res/blackbeard/blackbeardProjectile.png");

    protected final static Image ELIXIR = new Image("res/items/elixir.png");
    protected final static Image ELIXIR_ICON = new Image("res/items/elixirIcon.png");
    protected final static Image POTION = new Image("res/items/potion.png");
    protected final static Image POTION_ICON = new Image("res/items/potionIcon.png");
    protected final static Image SWORD = new Image("res/items/sword.png");
    protected final static Image SWORD_ICON = new Image("res/items/swordIcon.png");

    private final static int FONT_SIZE = 15;
    protected final static Font FONT = new Font("res/wheaton.otf", FONT_SIZE);

    protected double x;
    protected double y;
    protected double healthPoints;
    protected double maxHealth;
    protected Image currentImage;

    public Object(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void reduceHealthPoints(int damage) {
        healthPoints = healthPoints - damage;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public Rectangle getBoundingBox(){
        return currentImage.getBoundingBoxAt(new Point(x, y));
    }

    public int getHealth(){
        return (int) healthPoints;
    }

    public int getMaxHealth(){
        return (int) maxHealth;
    }
}
