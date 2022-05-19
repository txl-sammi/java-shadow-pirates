public class Sword extends Item{
    private final static int DAMAGE_INCREASE = 15;

    public Sword(int x, int y){
        super(x,y);
        this.currentImage = SWORD;
        this.iconImage = SWORD_ICON;
    }

    /**
     * Method that allows sailor to increase their damage if item has been picked up
     */
    public void pickup(Sailor sailor){
        sailor.increaseDamage(DAMAGE_INCREASE);
        pickedUp = true;
        currentIcons++;
        iconNumber = currentIcons;
        System.out.println(iconNumber);
    }
}