public class Potion extends Item{
    private final static int HEALTH_INCREASE = 25;

    public Potion(int x, int y){
        super(x,y);
        this.currentImage = POTION;
        this.iconImage = POTION_ICON;
    }

    /**
     * Method that allows sailor to increase health when item is picked up
     */
    public void pickup(Sailor sailor){
        sailor.increaseHealth(HEALTH_INCREASE);
        pickedUp = true;
        currentIcons++;
        iconNumber = currentIcons;
        System.out.println(iconNumber);
    }
}