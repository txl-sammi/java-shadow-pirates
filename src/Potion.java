public class Potion extends Item{
    private final static int HEALTH_INCREASE = 25;

    public Potion(int x, int y){
        super(x,y);
        this.currentImage = POTION;
        this.iconImage = POTION_ICON;
    }
    public void pickup(Sailor sailor){
        sailor.increaseHealth(HEALTH_INCREASE);
        pickedUp = true;
        CURRENT_ICONS++;
        iconNumber = CURRENT_ICONS;
        System.out.println(iconNumber);
    }
}