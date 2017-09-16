package game;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PowerUp extends Rectangle {

	/** the size of each side of the power-up */
	private final int SIDE_SIZE = PropertiesGetter.getPowerUpSize();
	/** the size of block sides, used to place underneath blocks */
	private final int BLOCK_SIZE = PropertiesGetter.getBlockSize();
	/** the offset of the power-ups when hidden behind blocks */
	private final int OFFSET = PropertiesGetter.getPowerUpOffset();
	/** the kind of powerup this is */
	private final String type;

	/**
	 * Generate a sprite with specified color and coordinates
	 * 
	 * @param color
	 * @param x
	 * @param y
	 */
	public PowerUp(String type, Color color, int x, int y) {
		super();
		this.type = type;
		setFill(color);
		setHeight(SIDE_SIZE);
		setWidth(SIDE_SIZE);
		setX(x * BLOCK_SIZE + OFFSET);
		setY(y * BLOCK_SIZE + OFFSET);
		setStroke(Color.BLACK);
		setStrokeWidth(PropertiesGetter.getStrokeWidth());
	}

	public String getType() {
		return type;
	}

}