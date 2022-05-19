public class Block extends Object{
    private final static int MAX_DAMAGE = 10;
    protected final static int EXPLODING_DURATION = 3000;
    protected boolean explodable = false;
    protected boolean isExploding = false;
    protected int lastExplodeTime = (int) System.currentTimeMillis() - EXPLODING_DURATION;

    public Block(int startX, int startY){
        super(startX, startY);
        this.currentImage = BLOCK;
        this.explodable = false;
    }

    /**
     * Method that performs state update
     */
    public void update() {
        BLOCK.draw(x, y);
    }

    /**
     * Method that sets the block to be exploding
     */
    public void exploding(Sailor sailor){
        sailor.reduceHealthPoints(MAX_DAMAGE);
        System.out.println("Bomb inflicts " + MAX_DAMAGE + " damage points on Sailor. Sailor's current health: " + sailor.getHealth() + "/" + sailor.getMaxHealth());
        lastExplodeTime = (int) System.currentTimeMillis();
        isExploding = true;
    }

    public boolean isExploding(){
        return isExploding;
    }

    public boolean isExplodable(){
        return explodable;
    }



}