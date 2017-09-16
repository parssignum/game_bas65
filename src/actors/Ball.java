package actors;
import game.PropertiesGetter;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Ball extends Sprite {

	/** image displayed for the ball */
	private final Image BALL_IMAGE = new Image(getClass().getClassLoader().getResourceAsStream("resources/ball.png"));
	/**
	 * the width of the game's scene, representing the location of the right "wall"
	 */
	private final int SCENE_WIDTH = PropertiesGetter.getScreenWidth();
	/**
	 * the width of the game's scene, representing the location of the bottom "wall"
	 */
	private final int SCENE_HEIGHT = PropertiesGetter.getScreenHeight() - PropertiesGetter.getHUDHeight();

	/** speed at which the ball is thrown when released by the paddle */
	private int throwSpeed;
	/** the default absolute horizontal position of the ball */
	private double defaultX;
	/** the default absolute vertical position of the ball */
	private double defaultY;
	/** if true, the ball should bounce off all walls */
	private boolean inGodMode;

	/**
	 * Construct a ball at a specified location
	 * 
	 * @param x
	 *            - base horizontal position before accounting for the ball's size
	 * @param y
	 *            - base vertical position before accounting for the ball's size
	 */
	public Ball(int x, int y) {
		super();
		setInGodMode(false);
		setThrowSpeed(PropertiesGetter.getBallSpeed());
		setImage(BALL_IMAGE);
		getImage().setPreserveRatio(true);
		getImage().setFitHeight(PropertiesGetter.getBallSize());
		// set default X and Y coordinates (pass location above the paddle)
		defaultX = x - getBounds().getWidth() / 2;
		defaultY = y + getBounds().getHeight() / 2;
		setX(defaultX);
		setY(defaultY);
		setVelocity(new Point2D(0, 0));
		setDamageDealt(PropertiesGetter.getBallDamage());
	}
	
	public Ball(Image image, int x, int y) {
		this(x, y);
		setImage(image);
		getImage().setPreserveRatio(true);
		getImage().setFitHeight(PropertiesGetter.getBallSize());
	}

	/**
	 * Bounces the ball off all walls expect the bottom, except when in god mode. In
	 * god mode, bounces ball off all walls.
	 */
	public void bounce() {
		if (getImage().getX() < 0 || getImage().getX() > SCENE_WIDTH - getWidth()) {
			setVelocity(new Point2D(-getVelocity().getX(), getVelocity().getY()));
		}
		if (getImage().getY() < 0 || (isInGodMode() && getImage().getY() > SCENE_HEIGHT - getHeight())) {
			setVelocity(new Point2D(getVelocity().getX(), -getVelocity().getY()));
		}
	}

	/**
	 * The ball should be thrown with constant vertical velocity component, varying
	 * horizontal velocity component
	 * 
	 * @param xVelocity
	 *            - the horizontal component of velocity, determined by key press at
	 *            release
	 */
	public void beThrown(int xVelocity) {
		getImage().setY(defaultY);
		setVelocity(new Point2D(xVelocity, -getThrowSpeed()));
	}

	/**
	 * Decrease the speed of the ball when encountering the "slow time" power-up
	 * 
	 * @param speedFactor
	 */
	public void slow(int speedFactor) {
		setThrowSpeed(getThrowSpeed() * speedFactor);
	}

	/**
	 * Reset the ball to its default position within the scene. This should be used
	 * when a paddle life is lost.
	 */
	public void resetPosition() {
		setX(defaultX);
		setY(defaultY);
	}

	public boolean isInGodMode() {
		return inGodMode;
	}

	public void setInGodMode(boolean inGodMode) {
		this.inGodMode = inGodMode;
	}

	public int getThrowSpeed() {
		return throwSpeed;
	}

	public void setThrowSpeed(int throwSpeed) {
		this.throwSpeed = throwSpeed;
	}

}