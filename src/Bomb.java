public class Bomb extends Block{
    private final static int MAX_DAMAGE = 10;

    public Bomb(int startX, int startY){
        super(startX, startY);
        this.currentImage = BOMB;
    }

    /**
     * Method that performs state update
     */
    public void update() {
        BOMB.draw(x, y);
    }

    public void explode() {
        currentImage = EXPLOSION;
    }

}