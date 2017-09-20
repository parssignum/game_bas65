
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javafx.geometry.Point2D;

/**
 * Generates the blocks and power-ups for each level.
 * 
 * This class is well designed because of its flexibility -- new levels can be
 * added to the game without changing the level factory code at all. As long as
 * the .properties file contains the necessary level data (namely, the number of
 * each block type and power-up type present in the level), the level factory
 * will assign the blocks and power-ups to random positions.
 * 
 * In addition to being flexible to new levels, the factory is flexible to new
 * types of blocks and power-ups. To add a new power-up or block, we need only
 * update and add the relevant lines in the .properties file, and the level
 * factory will happily place them for us.
 * 
 * @author Ben Schwennesen
 */
public class LevelFactory {

	/**
	 * The types of blocks present in the game, specified in the .properties file
	 */
	private final String[] BLOCK_TYPES = PropertiesGetter.getBlockTypes();
	/**
	 * The length (in pixels) of the width of the game's scene
	 */
	private final int SCENE_WIDTH = PropertiesGetter.getScreenWidth();
	/**
	 * The length (in pixels) of the all sides of all blocks (they're square)
	 */
	private final int BLOCK_SIZE = PropertiesGetter.getBlockSize();
	/**
	 * The number of elements in each row and each column of a square grid of blocks
	 * occupying a square with side length SCENE_WIDTH
	 */
	private final int GRID_DIMENSION = SCENE_WIDTH / BLOCK_SIZE;
	/**
	 * A list of active blocks' positions, used for placing power-ups underneath
	 * them
	 */
	private List<Point2D> blockPositions = new ArrayList<>();
	/** Set of active blocks */
	private Set<Block> activeBlocks = new HashSet<>();

	/**
	 * The types of power-ups present in the game, specified in the .properties file
	 */
	private final String[] POWER_UP_TYPES = PropertiesGetter.getPowerUpTypes();
	/** Set of active power-ups */
	private Set<PowerUp> activePowerUps = new HashSet<>();

	/**
	 * Stores a list of valid block positions within a block grid.
	 * 
	 * The positions of blocks as used in this class are relative to this grid, that
	 * is, they are indices in a two dimensional matrix (these are converted to
	 * absolute positions whenever a block is generated)
	 */
	private List<Point2D> validPositions = new ArrayList<>();
	/** Random number generator used to select the position of blocks */
	private Random positionSelector = new Random();
	/**
	 * String representing the current level, used to access values in .properties
	 * through the PropertiesGetter class
	 */
	private String levelString;

	/**
	 * Start up the level factory at level one
	 */
	public LevelFactory() {
		updateLevel(1);
	}

	/**
	 * Create the blocks and power-ups for a new level.
	 * 
	 * @param level
	 *            - the game level for which the factory needs to generate objects
	 */
	public void updateLevel(int level) {
		clearAll();
		levelString = String.valueOf(level);
		generateValidPositions();
		// since the power-ups are positioned behind blocks, blocks must be generated
		// first
		generateBlocks();
		generatePowerUps();
	}

	/**
	 * Generate a set of valid positions within the square matrix with dimensions
	 * GRID_DIMENSION. The Block class translates these relative positions to an
	 * absolute position within the game scene.
	 */
	private void generateValidPositions() {
		for (int i = 0; i < GRID_DIMENSION; i++) {
			for (int j = 0; j < GRID_DIMENSION; j++) {
				validPositions.add(new Point2D(i, j));
			}
		}
		blockPositions.addAll(validPositions);
	}

	/**
	 * Randomly generate the positions of blocks of each type within a level. The
	 * positions generated are relative to the grid of blocks described above.
	 */
	private void generateBlocks() {
		int size = validPositions.size();
		for (String blockType : BLOCK_TYPES) {
			int numOfThisType = PropertiesGetter.getNumberOfTypeInLevel(levelString, blockType);
			for (int i = 0; i < numOfThisType; i++) {
				addBlock(blockType, size--);
			}
		}
		// once all blocks are placed, remove remaining valid positions so that a
		// power-up can only be placed behind a block
		blockPositions.removeAll(validPositions);
	}

	/**
	 * Add a new block to the set of active blocks.
	 * 
	 * @param blockType
	 *            - the type of block to add
	 * @param randomPositionsRemaining
	 *            - the number of possible locations left at which to place the
	 *            block
	 */
	private void addBlock(String blockType, int randomPositionsRemaining) {
		Point2D randomPosition = validPositions.remove(positionSelector.nextInt(randomPositionsRemaining));
		Block addedBlock = new Block(PropertiesGetter.getColorOf("block", blockType),
				PropertiesGetter.getBlockHealth(blockType), (int) randomPosition.getX(), (int) randomPosition.getY(),
				(blockType.equals("rubber")));
		activeBlocks.add(addedBlock);
	}

	/**
	 * Generate the power-ups for the current level and place them randomly behind
	 * active blocks.
	 */
	private void generatePowerUps() {
		int size = blockPositions.size();
		for (String blockType : POWER_UP_TYPES) {
			int numOfThisType = PropertiesGetter.getNumberOfTypeInLevel(levelString, blockType);
			for (int i = 0; i < numOfThisType; i++) {
				addPowerUp(blockType, size--);
			}
		}
	}

	/**
	 * Add a new power-up behind an existing block.
	 * 
	 * @param type
	 *            - the type of power up to add
	 * @param blockPositionsLeft
	 *            - number of blocks behind which we can randomly place this
	 *            power-up
	 */
	private void addPowerUp(String type, int blockPositionsLeft) {
		Point2D randomPosition = blockPositions.remove(positionSelector.nextInt(blockPositionsLeft));
		PowerUp addedPowerUp = new PowerUp(type, PropertiesGetter.getColorOf("power-up", type),
				(int) randomPosition.getX(), (int) randomPosition.getY());
		activePowerUps.add(addedPowerUp);
	}

	/**
	 * Clear all blocks, power-ups, and generated positions when transitioning to a
	 * new level.
	 */
	private void clearAll() {
		activeBlocks.clear();
		activePowerUps.clear();
		validPositions.clear();
		blockPositions.clear();
	}

	/**
	 * Check if the player has destroyed all the blocks.
	 * 
	 * @return true if all the blocks have been destroyed and hence the game should
	 *         proceed to the next level
	 */
	public boolean outOfBlocks() {
		return activeBlocks.isEmpty();
	}

	/** 
	 * Get a set of the level's remaining blocks.
	 * 
	 * @return the set of active (non-destroyed) blocks 
	 */
	public Set<Block> getActiveBlocks() {
		return activeBlocks;
	}

	/** 
	 * Get a set of the level's unactivated power-ups.
	 * 
	 * @return the set of active (not yet collected) power-ups 
	 */
	public Set<PowerUp> getActivePowerUps() {
		return activePowerUps;
	}
}