
public abstract class Character extends Sprite {

	/**
	 * The amount of damage the character can take. Sprite is alive while this is
	 * greater than damage taken.
	 */
	private int myHealthPoints;
	/** Tracks the damage this character has taken */
	private int damageTaken;

	/** 
	 * Construct a sprite with some additional properties: health
	 * @param health
	 * @param damage
	 */
	public Character(int health, int damage) {
		super();
		setMyHealthPoints(health);
		setDamageDealt(damage);
	}

	/**
	 * Take damage from other sprites
	 */
	public abstract void takeDamageFrom(Sprite sprite);

	/**
	 * Heal from power-up
	 * 
	 * @param pointsToAdd
	 *            - the number of health points we add
	 */
	public void heal(int pointsToAdd) {
		setMyHealthPoints(getMyHealthPoints() + pointsToAdd);
	}

	/**
	 * Take damage from bullets
	 * 
	 * @param damage
	 *            - the amount of damage to take
	 */
	public void takeDamage(int damage) {
		damageTaken += damage;
	}

	/**
	 * Check to see if this sprite is dead or destroyed
	 * 
	 * @return boolean : whether the sprite has lost all health or not
	 */
	public boolean isDead() {
		return damageTaken > getMyHealthPoints();
	}

	public void resetHealth() {
		damageTaken = 0;
	}

	public int getMyHealthPoints() {
		return myHealthPoints;
	}

	public void setMyHealthPoints(int myHealthPoints) {
		this.myHealthPoints = myHealthPoints;
	}

}