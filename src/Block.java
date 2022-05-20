public class Block extends Object{
    private final static int MAX_DAMAGE = 10;
    protected final static int EXPLODING_DURATION = 3000;
    protected boolean explodable;
    protected boolean isExploding = false;
    protected boolean disappeared;
    protected int lastExplodeTime = (int) System.currentTimeMillis() - EXPLODING_DURATION;

    /**
     * Constructor for block
     * @param startX starting x coordinates
     * @param startY starting y coordinates
     */
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
     * @param sailor sailor to collide with
     */
    public void exploding(Sailor sailor){
        sailor.reduceHealthPoints(MAX_DAMAGE);
        System.out.println("Bomb inflicts " + MAX_DAMAGE + " damage points on Sailor. Sailor's current health: " + sailor.getHealth() + "/" + sailor.getMaxHealth());
        lastExplodeTime = (int) System.currentTimeMillis();
        isExploding = true;
    }

    /**
     * Method that gets whether the block is currently exploding
     * @return returns isExploding
     */
    public boolean isExploding(){
        return isExploding;
    }

    /**
     * Method that gets whether the block is explodable
     * @return returns explodable
     */
    public boolean isExplodable(){
        return explodable;
    }

    /**
     * method that gets whether the block has disappeared
     * @return returns disappeared
     */
    public boolean hasDisappeared(){
        return !disappeared;
    }

}