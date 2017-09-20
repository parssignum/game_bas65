
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents the blocks displayed in the game
 * 
 * @author Ben Schwennesen
 */
public class Block extends Rectangle {

	/** the size of blocks in pixels */
	private final int BLOCK_SIZE = PropertiesGetter.getBlockSize();
	/** Row index within the grid space where we place blocks */
	private final int x;
	/** Column index within the grid space where we place blocks */
	private final int y;
	/** Total damage that must be done to kill this block */
	private int healthPoints;
	/** Damage taken thus far */
	private int damageTaken;
	/** If true, this block can only be damaged by bullets, not the ball */
	private boolean isRubber;

	/**
	 * Build a block to be destroyed by the user.
	 * 
	 * @param color
	 *            - desired block color
	 * @param size
	 *            - of a side (blocks are all square)
	 * @param health
	 *            - health points required to kill the block
	 * @param x
	 *            - horizontal index within a part of the screen divided into a grid
	 *            of blocks
	 * @param y
	 *            - vertical index within a part of the screen divided into a grid
	 *            of blocks
	 * @param isRubber
	 *            - indicates if this block is of the special rubber type
	 */
	public Block(Color color, int health, int x, int y, boolean isRubber) {
		super(); // do the regular Rectangle initialization
		// set position within the "grid" part of the screen where we place blocks
		this.x = x;
		this.y = y;
		// set absolute position within the scene
		setX(x * BLOCK_SIZE);
		setY(y * BLOCK_SIZE);
		// set the type (indicates whether or not the ball can damage this block; if not
		// must use bullets)
		this.isRubber = isRubber;
		// set health points, explicitly initialize damage taken to zero
		healthPoints = health;
		damageTaken = 0;
		// set size
		setWidth(BLOCK_SIZE);
		setHeight(BLOCK_SIZE);
		// give the rectangle a border
		setStroke(Color.BLACK);
		setStrokeWidth(PropertiesGetter.getStrokeWidth());
		// fill the rectangle with the specified color
		setFill(color);
	}

	/**
	 * Deal damage to the block from a sprite
	 * 
	 * @param sprite
	 *            - a sprite which has collided with the block
	 */
	public void takeDamageFrom(Sprite sprite) {
		if (!isRubber || sprite instanceof Bullet) {
			damageTaken += sprite.getDamageDealt();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Block other = (Block) o;
		return this == o || (healthPoints == other.healthPoints && x == other.x && y == other.y);
	}

	@Override
	public int hashCode() {
		int hash = x;
		hash = 37 * hash + y;
		hash = 37 * hash + healthPoints;
		return hash;
	}

	/**
	 * Check if the block is dead
	 * 
	 * @return a boolean indicating if the block has been destroyed
	 */
	public boolean isDead() {
		return damageTaken >= healthPoints;
	}

	/**
	 * @return the x-coordinate of the block's center
	 */
	public double getCenterX() {
		return getX() + getWidth() / 2;
	}

	/**
	 * @return the y-coordinate of the block's center
	 */
	public double getCenterY() {
		return getY() + getHeight() / 2;
	}
}