public class Elixir extends Item{
    private final static int MAX_HEALTH_BOOST = 35;

    public Elixir(int x, int y){
        super(x,y);
        this.currentImage = ELIXIR;
        this.iconImage = ELIXIR_ICON;
    }

    /**
     * Method that allows sailor to boost their max health when item is picked up
     */
    public void pickup(Sailor sailor){
        pickedUp = true;
        currentIcons++;
        iconNumber = currentIcons;
        sailor.boostMaxHealth(MAX_HEALTH_BOOST);
        System.out.println(iconNumber);

    }
}
