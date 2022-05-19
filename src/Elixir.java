public class Elixir extends Item{
    private final static int MAX_HEALTH_BOOST = 35;

    public Elixir(int x, int y){
        super(x,y);
        this.currentImage = ELIXIR;
        this.iconImage = ELIXIR_ICON;
    }

    public void pickup(Sailor sailor){
        pickedUp = true;
        CURRENT_ICONS++;
        iconNumber = CURRENT_ICONS;
        sailor.boostMaxHealth(MAX_HEALTH_BOOST);
        System.out.println(iconNumber);

    }
}
