public class Item extends Object{

    public Item(int x, int y){
        super(x, y);
    }

    public void update() {
        currentImage.draw(x, y);
    }
}
