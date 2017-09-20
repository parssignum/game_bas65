package game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * This file is used to generically specify necessary aspects of the game loop
 * for a game created using JavaFX
 * 
 * @author Ben Schwennesen
 */

public abstract class GameEnvironment {

	/** Implement the classical game loop using a Timeline */
	private Timeline gameLoop;
	/* base game properties */
	private final int FRAMES_PER_SECOND = PropertiesGetter.getFramesPerSecond();
	protected final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	protected final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

	/**
	 * Constructor called by extending classes. Set the title and initialize the
	 * game loop.
	 * 
	 * @param title
	 *            - application window header
	 */
	public GameEnvironment() {
		initializeGameLoop();
	}

	/**
	 * Perform initialization phase of the game loop
	 */
	private void initializeGameLoop() {
		Duration singleFrame = Duration.millis(MILLISECOND_DELAY);
		KeyFrame frame = new KeyFrame(singleFrame, e -> step(SECOND_DELAY));
		gameLoop = new Timeline();
		gameLoop.setCycleCount(Timeline.INDEFINITE);
		gameLoop.getKeyFrames().add(frame);
	}

	/**
	 * Handle heavy lifting of the game loop
	 */
	private void step(double secondDelay) {
		// refresh positions of sprites
		refreshSprites();
		// if we have any collisions, handle them
		processCollisions();
		// if a sprite or block has lost all health, remove it
		removeDeadNodes();
	}

	/**
	 * Update the information of each sprite
	 */
	public abstract void refreshSprites();

	/**
	 * Have factories/managers clear dead/destroyed nodes
	 */
	public abstract void removeDeadNodes();

	/**
	 * Check for collisions and have a manager handle them where necessary
	 */
	public abstract void processCollisions();

	/**
	 * @return the game loop for an implemented game
	 */
	public Timeline getGameLoop() {
		return gameLoop;
	}

}