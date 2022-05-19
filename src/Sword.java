public class Sword extends Item{
    private final static int DAMAGE_INCREASE = 15;

    public Sword(int x, int y){
        super(x,y);
        this.currentImage = SWORD;
        this.iconImage = SWORD_ICON;
    }

    public void pickup(Sailor sailor){
        sailor.increaseDamage(DAMAGE_INCREASE);
        pickedUp = true;
        CURRENT_ICONS++;
        iconNumber = CURRENT_ICONS;
        System.out.println(iconNumber);
    }
}