public class Sword extends Item{
    private final static int DAMAGE_INCREASE = 15;

    /** Constructor for sword item which stores its location
     * @param x the x coordinates for sword
     * @param y the y coordinates for sword
     */
    public Sword(int x, int y){
        super(x,y);
        this.currentImage = SWORD;
        this.iconImage = SWORD_ICON;
    }

    /** This method is used to indicate that sailor had picked up sword
     * @param sailor the sailor who picked up the item
     */
    public void pickup(Sailor sailor){
        sailor.increaseDamage(DAMAGE_INCREASE);
        pickedUp = true;
        currentIcons++;
        iconNumber = currentIcons;
        System.out.println("Sailor finds Sword. Sailor's damage points increased to: " + sailor.getDamagePoints());
    }
}