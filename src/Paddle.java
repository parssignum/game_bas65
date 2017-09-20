
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Paddle extends Character {

	/** the image representing the paddle */
	private final Image PADDLE_BASE_IMAGE = new Image(getClass().getClassLoader().getResourceAsStream("images/human.png"));
	/** default horizontal position */
	private double defaultX;
	/** default vertical position */
	private double defaultY;

	/** remaining lives for the paddle */
	private int paddleLives;

	/**
	 * Build a paddle to be controlled by the player
	 * 
	 * @param midScene
	 *            - the middle of the game scene with respect to the x-axis
	 * @param bottomScene
	 *            - the bottom of the game scene
	 * @param lives
	 *            - the number of lives this paddle has
	 */
	public Paddle(int midSceneX, int bottomScene, int lives) {
		super(PropertiesGetter.getPaddleHealth(), PropertiesGetter.getPaddleDamage());
		paddleLives = lives;
		setImage(PADDLE_BASE_IMAGE);
		// store default positons so we can easily reset the paddle after losing a life
		defaultX = midSceneX - getBounds().getWidth() / 2;
		setX(defaultX);
		defaultY = bottomScene - getBounds().getHeight();
		setY(defaultY);
		// the paddle moves based on key presses as opposed to a velocity vector
		setVelocity(new Point2D(0, 0));
	}

	/**
	 * The paddle should bounce off the right and left walls only
	 * 
	 * @param sceneWidth
	 *            - boundary on the right
	 * @param sceneHeight
	 *            - boundary on the top
	 */
	public void bounce() {
		int screenWidth = PropertiesGetter.getScreenWidth();
		if (getImage().getX() < 0 || getImage().getX() > screenWidth - getImage().getBoundsInLocal().getWidth()) {
			setVelocity(new Point2D(-getVelocity().getX(), getVelocity().getY()));
		}
	}

	/**
	 * The paddle only takes damage from bullets fired from the boss
	 * 
	 * @param sprite
	 *            - the sprite we might be taking damage from, depending on its type
	 */
	public void takeDamageFrom(Sprite sprite) {
		if (sprite instanceof Bullet) {
			takeDamage(sprite.getDamageDealt());
		}
	}

	/**
	 * Reset the paddle to its default position.
	 */
	public void resetPosition() {
		setX(defaultX);
		setY(defaultY);
	}

	/** @return the paddle's remaining lives */
	public int getLives() {
		return paddleLives;
	}

	/**
	 * Update the paddle's lives after it loses all health or fails to catch the
	 * ball.
	 * 
	 * @param lives
	 *            - the updated number of lives
	 */
	public void setLives(int lives) {
		paddleLives = lives;
	}
}