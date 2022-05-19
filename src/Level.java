public abstract class Level {
    private int bottomEdge;
    private int topEdge;
    private int leftEdge;
    private int rightEdge;

    public boolean checkOutOfBound(double x, double y){
        if ((y < topEdge) || (y > bottomEdge) || (x < leftEdge) || (x > rightEdge)) {
            return true;
        }
        return false;
    }
}
