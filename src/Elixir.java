public class Elixir extends Item{
    private final static int MAX_HEALTH_BOOST = 35;

    /** Constructor for elixir item which stores its location
     * @param x the x coordinates for elixir
     * @param y the y coordinates for elixir
     */
    public Elixir(int x, int y){
        super(x,y);
        this.currentImage = ELIXIR;
        this.iconImage = ELIXIR_ICON;
    }

    /** This method is used to indicate that sailor had picked up elixir
     * @param sailor the sailor who picked up the item
     */
    public void pickup(Sailor sailor){
        pickedUp = true;
        currentIcons++;
        iconNumber = currentIcons;
        sailor.boostMaxHealth(MAX_HEALTH_BOOST);
        System.out.println("Sailor finds Elixir. Sailor's current health: " + sailor.getHealth() + "/" + sailor.getMaxHealth());
    }
}
