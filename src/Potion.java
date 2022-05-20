public class Potion extends Item{
    private final static int HEALTH_INCREASE = 25;

    /** Constructor for potion item which stores its location
     * @param x the x coordinates for potion
     * @param y the y coordinates for potion
     */
    public Potion(int x, int y){
        super(x,y);
        this.currentImage = POTION;
        this.iconImage = POTION_ICON;
    }

    /** This method is used to indicate that sailor had picked up potion
     * @param sailor the sailor who picked up the item
     */
    public void pickup(Sailor sailor){
        sailor.increaseHealth(HEALTH_INCREASE);
        pickedUp = true;
        currentIcons++;
        iconNumber = currentIcons;
        System.out.println("Sailor finds Potion. Sailor's current health: " + sailor.getHealth() + "/" + sailor.getMaxHealth());
    }
}