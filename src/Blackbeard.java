public class Blackbeard extends Pirate{

    /**
     * Constructor for blackbeard
     * @param startX starting x coordinates
     * @param startY starting y coordinates
     */
    public Blackbeard(int startX, int startY){
        super(startX, startY);
        this.healthPoints = MAX_HEALTH_POINTS*2;
        this.currentImage = BLACKBEARD_RIGHT;
        colour.setBlendColour(GREEN);

        this.left = BLACKBEARD_LEFT;
        this.right = BLACKBEARD_RIGHT;
        this.leftHit = BLACKBEARD_HIT_LEFT;
        this.rightHit = BLACKBEARD_HIT_RIGHT;
        this.maxHealth = MAX_HEALTH_POINTS*2;
        this.maxDamage = MAX_DAMAGE*2;
        this.projectileSpeed = PROJECTILE_SPEED*2;
        this.shootCooldown = SHOOT_COOLDOWN/2;
        this.attackRange = ATTACK_RANGE*2;
        this.projectile = BLACKBEARD_PROJECTILE;
    }

}
