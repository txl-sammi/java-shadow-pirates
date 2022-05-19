public class Block extends Object{

    public Block(int startX, int startY){
        super(startX, startY);
        this.currentImage = BLOCK;
    }

    /**
     * Method that performs state update
     */
    public void update() {
        BLOCK.draw(x, y);
    }

}