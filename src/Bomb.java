public class Bomb extends Block{
    private boolean disappeared = false;

    /**
     * Constructor for bomb
     * @param startX starting x coordinate
     * @param startY starting y coordinates
     */
    public Bomb(int startX, int startY){
        super(startX, startY);
        this.currentImage = BOMB;
        this.explodable = true;
    }

    /**
     * Method that performs state update
     */
    public void update() {
        if (!disappeared){
            currentImage.draw(x, y);

            if (isExploding){
                currentImage = EXPLOSION;
                int now = (int) System.currentTimeMillis();

                disappeared = !((lastExplodeTime + EXPLODING_DURATION >= now));
            }
        }

    }
}