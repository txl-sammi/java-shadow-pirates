public abstract class Level {
    private int bottomEdge;
    private int topEdge;
    private int leftEdge;
    private int rightEdge;

    public void setBound(int bottom, int top, int left, int right){
        this.bottomEdge = bottom;
        this.topEdge = top;
        this.leftEdge = left;
        this.rightEdge = right;
    }
}
