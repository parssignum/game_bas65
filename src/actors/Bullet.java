package actors;
import game.PropertiesGetter;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Bullet extends Sprite {
	/* base properties of the bullet */
	private final int BULLET_SPEED = PropertiesGetter.getBulletSpeed();
	private final int BULLET_DAMAGE = PropertiesGetter.getBulletDamage();
	private final int BULLET_SIZE = PropertiesGetter.getBulletSize();
	/** image displayed for a bullet */
	private final Image BULLET_IMAGE = new Image(
			getClass().getClassLoader().getResourceAsStream("resources/bullet.png"));
	/**
	 * indicates if this bullet was from the boss or the paddle this is necessary
	 * because the paddle should not take damage from its own bullets
	 **/
	private boolean fromBoss;

	/**
	 * Build a bullet being fired by the boss or the paddle.
	 * 
	 * @param image
	 * @param x
	 *            - absolute x-coordinate
	 * @param y
	 *            - absolute y-coordinate
	 * @param fromBoss
	 *            - indicates the source of the block (boss or paddle)
	 */
	public Bullet(int x, int y, boolean fromBoss) {
		super();
		setImage(BULLET_IMAGE);
		this.fromBoss = fromBoss;
		if (fromBoss) {
			setVelocity(new Point2D(0, BULLET_SPEED));
		} else {
			setVelocity(new Point2D(0, -BULLET_SPEED));
		}
		getImage().setPreserveRatio(true);
		getImage().setFitHeight(BULLET_SIZE);
		setDamageDealt(BULLET_DAMAGE);
		setX(x);
		setY(y);
	}

	/**
	 * Bullets should not bounce off of walls. They fall of any part of the screen.
	 */
	@Override
	public void bounce() {
		return;
	}

	/***
	 * Check if this bullet came from the boss.
	 * 
	 * @return a boolean: true if the bullet came from the boss, false if it came
	 *         from the paddle
	 */
	public boolean isFromBoss() {
		return fromBoss;
	}

}