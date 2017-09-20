

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Properties;

import javafx.scene.paint.Color;

/**
 * Static utility class for retrieving the game's properties.
 * 
 * @author Ben Schwennesen
 * 
 */
public final class PropertiesGetter {

	/** name of the file storing the properties for the breakout game variant */
	private static final String PROPERTIES_FILE = "src/breakout.properties";

	/**
	 * this class uses java.util.Properties as a base and extends its functionality
	 */
	private static final Properties GAME_CONFIG;

	/* keys the getter needs to remember */
	// paddle configuration keys
	private static final String PADDLE_HEALTH_KEY = "paddle-health";
	private static final String PADDLE_DAMAGE_KEY = "paddle-damage";
	private static final String PADDLE_LIVES_KEY = "paddle-lives";
	// boss configuration keys
	private static final String BOSS_HEALTH_KEY = "boss-health";
	// bullet configuration keys
	private static final String BULLET_DAMAGE_KEY = "bullet-damage";
	private static final String BULLET_SPEED_KEY = "bullet-speed";
	private static final String BULLET_SIZE_KEY = "bullet-size";
	// ball configuration keys
	private static final String BALL_SIZE_KEY = "ball-size";
	private static final String BALL_DAMAGE_KEY = "ball-damage";
	private static final String BALL_SPEED_KEY = "ball-speed";
	private static final String BALL_OFFSET_KEY = "ball-offset";
	private static final String BALL_THROW_VX_KEY = "ball-throw-horizontal-velocity";
	// block configuration keys
	private static final String BLOCK_TYPES_KEY = "block-types";
	private static final String BLOCK_HEALTH_KEY = "-block-health";
	private static final String BLOCK_SIZE_KEY = "block-size";
	private static final String COLOR_KEY = "-color-";
	// game environment configuration keys
	private static final String TITLE_KEY = "title";
	private static final String LAST_LEVEL_KEY = "last-level";
	private static final String KEY_PRESS_INPUT_SPEED_KEY = "key-input-speed";
	private static final String FRAMES_PER_SECOND_KEY = "frames-per-second";
	private static final String SCREEN_HEIGHT_KEY = "screen-height";
	private static final String SCREEN_WIDTH_KEY = "screen-width";
	private static final String HUD_HEIGHT_KEY = "hud-height";
	private static final String HUD_TEXT_OFFSET_KEY = "hud-text-offset";
	private static final String MENU_MARGIN_KEY = "menu-margin";
	private static final String MENU_TITLE_FONT_SIZE_KEY = "menu-title-font-size";
	private static final String MENU_BODY_FONT_SIZE_KEY = "menu-body-font-size";
	private static final String MENU_ITEM_HEIGHT_KEY = "menu-item-height";
	private static final String MENU_ITEM_WIDTH_KEY = "menu-item-width";
	private static final String MENU_ITEM_FONT_SIZE_KEY = "menu-item-font-size";
	private static final String MENU_ITEM_ROUNDEDNESS_KEY = "menu-item-roundness";
	private static final String MENU_ITEM_BORDER_WIDTH_KEY = "menu-item-border-width";
	private static final String MENU_ITEM_X_OFFSET_KEY = "menu-item-x-offset";
	// pop-up window configuration keys
	private static final String POP_UP_WIDTH_KEY = "pop-up-width";
	private static final String POP_UP_HEIGHT_KEY = "pop-up-height";
	// power-up configuration keys
	private static final String POWER_UP_TYPES_KEY = "power-up-types";
	private static final String POWER_UP_SIZE_KEY = "power-up-size";
	private static final String GROWTH_MULTIPLIER_KEY = "growth-multiplier";
	private static final String POWER_UP_OFFSET_KEY = "power-up-offset";
	private static final String SPEED_MULTIPLIER_KEY = "speed-multiplier";
	private static final String SLOW_AMOUNT_KEY = "slow-amount";
	private static final String MULTIPLY_COUNT_KEY = "multiply-count";
	private static final String STROKE_WIDTH_KEY = "stroke-width";

	/**
	 * Blank constructor to ensure no other class tries to create an instance of the
	 * utility class.
	 */
	private PropertiesGetter() {
		// do nothing
	}

	/** static block to initialize the static Properties member */
	static {
		GAME_CONFIG = new Properties();
		try {
			GAME_CONFIG.load(new FileInputStream(new File(PROPERTIES_FILE)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Augments java.util.Properties functionality by getting a property we know
	 * will be an integer
	 * 
	 * @param key
	 *            - the key used to index this
	 * @return value - the value of the key field in the properties file
	 */
	private static int getIntegerProperty(String key) {
		String value = GAME_CONFIG.getProperty(key);
		// if the key is not found, Properties will return null and we should return a
		// default value
		if (value == null) {
			return 0;
		}
		return Integer.parseInt(value);
	}

	/** @return the height of the screen */
	public static int getScreenHeight() {
		return getIntegerProperty(SCREEN_HEIGHT_KEY);
	}

	/** @return the width of the screen */
	public static int getScreenWidth() {
		return getIntegerProperty(SCREEN_WIDTH_KEY);
	}

	/** @return the paddle's health points */
	public static int getPaddleHealth() {
		return getIntegerProperty(PADDLE_HEALTH_KEY);
	}

	/** @return the paddle's health points */
	public static int getPaddleLives() {
		return getIntegerProperty(PADDLE_LIVES_KEY);
	}

	/** @return the boss's health points */
	public static int getBossHealth() {
		return getIntegerProperty(BOSS_HEALTH_KEY);
	}

	/** @return the damage dealt by bullets */
	public static int getBulletDamage() {
		return getIntegerProperty(BULLET_DAMAGE_KEY);
	}

	/** @return the size of the breakout ball */
	public static int getBallSize() {
		return getIntegerProperty(BALL_SIZE_KEY);
	}

	/** @return the default speed of the breakout ball */
	public static int getBallSpeed() {
		return getIntegerProperty(BALL_SPEED_KEY);
	}

	/** @return the damage dealt by the breakout ball */
	public static int getBallDamage() {
		return getIntegerProperty(BALL_DAMAGE_KEY);
	}

	/** @return the health points for a specific kind of block */
	public static int getBlockHealth(String blockType) {
		return getIntegerProperty(blockType + BLOCK_HEALTH_KEY);
	}

	/** @return the size of any block type */
	public static int getBlockSize() {
		return getIntegerProperty(BLOCK_SIZE_KEY);
	}

	/**
	 * @return the speed at which the paddle should move in response to left/right
	 *         key presses
	 */
	public static int getKeyInputSpeed() {
		return getIntegerProperty(KEY_PRESS_INPUT_SPEED_KEY);
	}

	/** @return game's frames per second */
	public static int getFramesPerSecond() {
		return getIntegerProperty(FRAMES_PER_SECOND_KEY);
	}

	/** @return the number of blocks or power-ups of type 'type' in level 'level' */
	public static int getNumberOfTypeInLevel(String level, String type) {
		return getIntegerProperty("num-" + type + "-level-" + level);
	}

	/** @return the height of the head up display */
	public static int getHUDHeight() {
		return getIntegerProperty(HUD_HEIGHT_KEY);
	}

	/** @return the vertical offset of text within the head up display */
	public static int getHUDTextOffset() {
		return getIntegerProperty(HUD_TEXT_OFFSET_KEY);
	}

	/** @return the horizontal offset of the ball when initially placing */
	public static int getBallHorizontalOffset() {
		return getIntegerProperty(BALL_OFFSET_KEY);
	}

	/** @return the horizontal component of velocity when the ball is thrown */
	public static int getHorizontalThrowSpeed() {
		// TODO Auto-generated method stub
		return getIntegerProperty(BALL_THROW_VX_KEY);
	}

	/** @return the margins of the main menu border */
	public static double getMenuMargin() {
		return getIntegerProperty(MENU_MARGIN_KEY);
	}

	/** @return the font size of the game's title in the main menu */
	public static int getMenuTitleFontSize() {
		return getIntegerProperty(MENU_TITLE_FONT_SIZE_KEY);
	}

	/** @return the font size of body text in the main menu */
	public static int getMenuBodyFontSize() {
		return getIntegerProperty(MENU_BODY_FONT_SIZE_KEY);
	}

	/** @return the speed bullets */
	public static int getBulletSpeed() {
		return getIntegerProperty(BULLET_SPEED_KEY);
	}

	/** @return the size of bullets */
	public static int getBulletSize() {
		return getIntegerProperty(BULLET_SIZE_KEY);
	}

	/** @return the damage the paddle can take */
	public static int getPaddleDamage() {
		return getIntegerProperty(PADDLE_DAMAGE_KEY);
	}

	/** @return the height of buttons in the menu */
	public static int getMenuItemHeight() {
		return getIntegerProperty(MENU_ITEM_HEIGHT_KEY);
	}

	/** @return the width of buttons in the menu */
	public static int getMenuItemWidth() {
		return getIntegerProperty(MENU_ITEM_WIDTH_KEY);
	}

	/** @return the size of text displayed in the menu */
	public static int getMenuItemFontSize() {
		return getIntegerProperty(MENU_ITEM_FONT_SIZE_KEY);
	}

	/** @return the degree to which menu buttons have their corners rounded */
	public static int getMenuItemRoundedness() {
		return getIntegerProperty(MENU_ITEM_ROUNDEDNESS_KEY);
	}

	/** @return the width of the border surrounding menu buttons */
	public static int getMenuItemBorderWidth() {
		return getIntegerProperty(MENU_ITEM_BORDER_WIDTH_KEY);
	}

	/** @return the offset of menu buttons from the left side of the scene */
	public static int getMenuItemXOffset() {
		return getIntegerProperty(MENU_ITEM_X_OFFSET_KEY);
	}

	/** @return the width of pop-up windows in the main menu */
	public static int getPopUpWidth() {
		return getIntegerProperty(POP_UP_WIDTH_KEY);
	}

	/** @return the height of pop-up windows in the main menu */
	public static int getPopUpHeight() {
		return getIntegerProperty(POP_UP_HEIGHT_KEY);
	}

	/** @return the size of the sides of power-ups (they're square) */
	public static int getPowerUpSize() {
		return getIntegerProperty(POWER_UP_SIZE_KEY);
	}

	/**
	 * @return the ratio by which the ball decreases in speed when the growth
	 *         power-up is activated
	 */
	public static int getSlowAmount() {
		return getIntegerProperty(SLOW_AMOUNT_KEY);
	}

	/** @return the offset of the power-ups when hidden behind blocks */
	public static int getPowerUpOffset() {
		return getIntegerProperty(POWER_UP_OFFSET_KEY);
	}

	/** @return the offset of the power-ups when hidden behind blocks */
	public static int getMultiplyCount() {
		return getIntegerProperty(MULTIPLY_COUNT_KEY);
	}

	/** @return the width of the border drawn around blocks and power-ups */
	public static double getStrokeWidth() {
		return getIntegerProperty(STROKE_WIDTH_KEY);
	}

	/**
	 * @return the last level in the game, so we can check for the end of the game
	 */
	public static int getLastLevel() {
		return getIntegerProperty(LAST_LEVEL_KEY);
	}

	/**
	 * @return the ratio by which the paddle increases in size when the growth
	 *         power-up is activated
	 */
	public static double getGrowthMultiplier() {
		return Double.valueOf(GAME_CONFIG.getProperty(GROWTH_MULTIPLIER_KEY));
	}

	/**
	 * @return the ratio by which the paddle increases in speed when the growth
	 *         power-up is activated
	 */
	public static double getSpeedMultiplier() {
		return Double.valueOf(GAME_CONFIG.getProperty(SPEED_MULTIPLIER_KEY));
	}

	/** @return the title of the game */
	public static String getTitle() {
		return GAME_CONFIG.getProperty(TITLE_KEY);
	}

	/** @return a string representing the image file for a level's background */
	public static String getLevelBackgroundImage(int level) {
		return GAME_CONFIG.getProperty("background-level-" + String.valueOf(level));
	}

	/** @return a Color used to fill a particular type of block or power-up */
	public static Color getColorOf(String blockOrPowerUp, String type) {
		String colorAsString = GAME_CONFIG.getProperty(blockOrPowerUp + COLOR_KEY + type);
		return Color.valueOf(colorAsString);
	}

	/**
	 * @return an array of Strings, with each element representing a type of block
	 *         in the game
	 */
	public static String[] getBlockTypes() {
		return GAME_CONFIG.getProperty(BLOCK_TYPES_KEY).split(",");
	}

	/**
	 * @return an array of Strings, with each element representing a type of
	 *         power-up in the game
	 */
	public static String[] getPowerUpTypes() {
		return GAME_CONFIG.getProperty(POWER_UP_TYPES_KEY).split(",");
	}
}