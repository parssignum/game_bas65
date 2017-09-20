package game;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import actors.Ball;
import actors.Bullet;
import actors.Character;
import actors.Paddle;
import actors.Sprite;
import javafx.animation.Animation.Status;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class HostileBreakout extends GameEnvironment {

	/* game configuration constants */
	private final String TITLE = PropertiesGetter.getTitle();
	private final int SCENE_HEIGHT = PropertiesGetter.getScreenHeight();
	private final int SCENE_WIDTH = PropertiesGetter.getScreenWidth();
	private final int HUD_HEIGHT = PropertiesGetter.getHUDHeight();
	private final int KEY_INPUT_SPEED = PropertiesGetter.getKeyInputSpeed();
	/* paddle configuration constants */
	private final int PADDLE_DEFAULT_X = SCENE_WIDTH / 2;
	private final int PADDLE_DEFAULT_Y = SCENE_HEIGHT - HUD_HEIGHT;
	/* ball configuration constants */
	private final int BALL_Y_OFFSET = PropertiesGetter.getBallHorizontalOffset();
	private final int BALL_THROW_X_COMPONENT = PropertiesGetter.getHorizontalThrowSpeed();
	private final int BALL_DEFAULT_X = PADDLE_DEFAULT_X;
	private final int BALL_DEFAULT_Y = SCENE_HEIGHT - HUD_HEIGHT + BALL_Y_OFFSET;
	/* displayed text properties */
	private final TextAlignment CENTER = TextAlignment.CENTER;
	private final int TEXT_OFFSET = PropertiesGetter.getHUDTextOffset();
	private final int DISPLAYED_TEXT_FONT_SIZE = PropertiesGetter.getMenuTitleFontSize();
	private final String LIVES_INDICATOR_BASE = "LIVES REMAINING: ";
	private final String WON_STRING = "WON";
	private final String LOSS_STRING = "LOST";

	/** the paddle character controlled by the user */
	private Paddle paddle;
	/** the ball used to destroy most blocks */
	private Ball ball;
	/** the heads up display (HUD) base containing lives left, active power-ups */
	private Rectangle headUpDisplay;
	/** the text displayed in the HUD indicating the paddle's remaining lives */
	private Text livesIndicator;
	/** the paddle's movement speed */
	private int paddleMoveSpeed = KEY_INPUT_SPEED;
	/** the paddle's movement speed */
	private int ballThrowSpeedX = BALL_THROW_X_COMPONENT;
	/** the scene where levels are displayed */
	private Scene primaryScene;
	/** the game window */
	private Stage primaryStage;
	/** the level the user is currently playing */
	private int currentLevel;
	/**
	 * indicates if the ball should move when the paddle moves (i.e., if the ball is
	 * "caught")
	 */
	private boolean ballLockedToPaddle;
	/** a factory used to generate and manage the game's blocks and power-ups */
	private LevelFactory levelFactory;
	/** a manager to process interaction among sprites */	
	private SpriteManager spriteManager;
	/** host all game components (nodes) in a single group */
	private Group gameGroup;

	/**
	 * Initialize the game environment and set initial conditions
	 */
	public HostileBreakout() {
		super();
	}

	/**
	 * Setup the game and all components
	 * 
	 * @param primaryStage
	 *            - the main game window
	 */
	public void initialize(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle(TITLE);
		// setup game actors
		currentLevel = 1;
		ballLockedToPaddle = true;
		ball = new Ball(BALL_DEFAULT_X, BALL_DEFAULT_Y);
		paddle = new Paddle(PADDLE_DEFAULT_X, PADDLE_DEFAULT_Y, PropertiesGetter.getPaddleLives());
		// setup managers and factories
		levelFactory = new LevelFactory();
		spriteManager = new SpriteManager();
		spriteManager.getActiveSprites().add(ball);
		// create the HUD
		livesIndicator = new Text(LIVES_INDICATOR_BASE + paddle.getLives());
		livesIndicator.setTextAlignment(CENTER);
		initializeHeadUpDisplay();
		// initialize the scene root
		gameGroup = new Group(ball.getImage(), paddle.getImage(), headUpDisplay, livesIndicator);
		// create scene
		primaryScene = new Scene(gameGroup, SCENE_WIDTH, SCENE_HEIGHT);
		Image levelOneBackground = new Image(getClass().getClassLoader().
				getResourceAsStream(PropertiesGetter.getLevelBackgroundImage(currentLevel)));
		primaryScene.setFill(new ImagePattern(levelOneBackground));
		// use lambda to set key press behavior
		primaryScene.setOnKeyPressed(key -> handleKeyInput(key.getCode()));
		// make object factory for level one (it will add nodes to the game group)
		gameGroup.getChildren().addAll(levelFactory.getActivePowerUps());
		// add the power-ups before the blocks so that the blocks cover them
		gameGroup.getChildren().addAll(levelFactory.getActiveBlocks());
		primaryStage.setScene(primaryScene);
	}

	/**
	 * Set up the heads up display (HUD), showing remaining lives
	 */
	private void initializeHeadUpDisplay() {
		headUpDisplay = new Rectangle(SCENE_WIDTH, HUD_HEIGHT);
		headUpDisplay.setX(0);
		headUpDisplay.setY(SCENE_HEIGHT - HUD_HEIGHT);
		livesIndicator = new Text(LIVES_INDICATOR_BASE + paddle.getLives());
		livesIndicator.setX(0);
		livesIndicator.setY(SCENE_HEIGHT - TEXT_OFFSET);
		livesIndicator.setFont(Font.font("Ubuntu", DISPLAYED_TEXT_FONT_SIZE));
		livesIndicator.setFill(Color.WHITE);
		livesIndicator.setTextAlignment(CENTER);
		headUpDisplay.setFocusTraversable(false);
	}

	/**
	 * Handle key press events
	 */
	private void handleKeyInput(KeyCode code) {
		if (getGameLoop().getStatus() == Status.RUNNING) {
			if (code.isArrowKey()) {
				handleArrowPress(code);
			} else if (code == KeyCode.SPACE && ball.getVelocity().magnitude() == 0) {
				handleThrow();
			} else if (code.isDigitKey()) {
				handleDigitPress(code);
			} else if (code == KeyCode.R) {
				// reset positions
				ball.resetPosition();
				paddle.resetPosition();
				ballLockedToPaddle = true;
			} else if (code == KeyCode.N) {
				// nuke cheat (lose instantly)
				showEndGameScreen(LOSS_STRING);
			} else if (code == KeyCode.G) {
				// god mode
				ball.setInGodMode(!ball.isInGodMode());
				paddle.setMyHealthPoints(Integer.MAX_VALUE);
			} else if (code == KeyCode.S) {
				activatePowerUp("speed");
			} else if (code == KeyCode.M) {
				activatePowerUp("multiply");
			} else if (code == KeyCode.T) {
				activatePowerUp("slow");
			}
		}
	}

	/**
	 * Handle and press of the arrow keys, either moving the paddle or spawning a
	 * bullet
	 * 
	 * @param code
	 *            - a pressed arrow key
	 */
	private void handleArrowPress(KeyCode code) {
		if (code == KeyCode.RIGHT) {
			handleRightPress();
		} else if (code == KeyCode.LEFT) {
			handleLeftPress();
		} else if (code == KeyCode.UP) {
			// false below indicates that the bullet is from the paddle, not the boss
			addBullet((int) paddle.getCenterX(), BALL_DEFAULT_Y, false);
		}
	}

	/**
	 * Process level-shift cheat code
	 * 
	 * @param code
	 *            - a pressed digit key code
	 */
	private void handleDigitPress(KeyCode code) {
		int levelDesired = code.ordinal() - 24; // the ordinal code for '1' is 25, ..., '5' is 29
		if (levelDesired > 0 && levelDesired < 6) {
			transitionLevel(levelDesired);
		}
	}

	/**
	 * Throw the ball away from the paddle when space key is pressed. If the ball is
	 * on the paddle's left side, throw the ball toward the left, otherwise throw it
	 * toward the right.
	 */
	private void handleThrow() {
		if (ball.getCenterX() <= paddle.getCenterX()) {
			ball.beThrown(-ballThrowSpeedX);
		} else {
			ball.beThrown(ballThrowSpeedX);
		}
		ballLockedToPaddle = false;
	}

	/**
	 * Move the paddle (and ball if it's caught) leftward on left key press
	 */
	private void handleLeftPress() {
		if (paddle.getImage().getX() - paddleMoveSpeed > 0) {
			paddle.setX(paddle.getX() - paddleMoveSpeed);
			if (ballLockedToPaddle) {
				ball.setX(ball.getX() - paddleMoveSpeed);
			}
		} else {
			double distancePaddleMoves = -paddle.getX();
			paddle.setX(0);
			if (ballLockedToPaddle) {
				ball.setX(ball.getX() + distancePaddleMoves);
			}
		}
	}

	/**
	 * Move the paddle (and ball if it's caught) rightward on right key press
	 */
	private void handleRightPress() {
		double paddleWidth = paddle.getWidth();
		if (paddle.getX() + paddleMoveSpeed < SCENE_WIDTH - paddleWidth) {
			paddle.setX(paddle.getX() + paddleMoveSpeed);
			if (ballLockedToPaddle) {
				ball.setX(ball.getX() + paddleMoveSpeed);
			}
		} else {
			double distancePaddleMoves = SCENE_WIDTH - paddleWidth - paddle.getX();
			paddle.setX(SCENE_WIDTH - paddleWidth);
			if (ballLockedToPaddle) {
				ball.setX(ball.getX() + distancePaddleMoves);
			}
		}
	}

	/**
	 * Check for collisions and have the sprite factory handle them where necessary
	 */
	public void processCollisions() {
		if (ball.collidesWith(paddle)) {
			ball.setVelocity(new Point2D(0, 0));
			ballLockedToPaddle = true;
		}
		Set<PowerUp> powerUpsToActivate = checkCollisions();
		activatePowerUps(powerUpsToActivate);
	}
	
	/** 
	 * Check for collisions among blocks and sprites
	 * 
	 * @return a set of power-ups that were collided with (and so need to be activated)
	 */
	private Set<PowerUp> checkCollisions() {
		Set<PowerUp> powerUpsToActivate = new HashSet<>();
		for (Sprite sprite : spriteManager.getActiveSprites()) {
			for (Block block : levelFactory.getActiveBlocks()) {
				if (sprite.collidesWith(block)) {
					(block).takeDamageFrom(sprite);
					sprite.bounceOffBlock(block);
				}
			}
			for (PowerUp powerup : levelFactory.getActivePowerUps()) {
				if (sprite.collidesWith(powerup)) {
					powerUpsToActivate.add(powerup);
				}
			}
			if (sprite.collidesWith(paddle)) {
				collide(sprite, paddle);
			}
		}
		return powerUpsToActivate;
	}

	private void activatePowerUps(Set<PowerUp> powerUpsToActivate) {
		Set<PowerUp> activePowerUps = levelFactory.getActivePowerUps();
		for (PowerUp powerup : powerUpsToActivate) {
			activatePowerUp(powerup.getType());
			gameGroup.getChildren().remove(powerup);
			activePowerUps.remove(powerup);
		}
	}

	/**
	 * Activate the provided power-up
	 * 
	 * @param powerup
	 */
	private void activatePowerUp(String type) {
		if (type.equals("speed")) {
			if (paddleMoveSpeed == KEY_INPUT_SPEED) {
				paddleMoveSpeed *= PropertiesGetter.getSpeedMultiplier();
			} else {
				paddleMoveSpeed = KEY_INPUT_SPEED;
			}
		} else if (type.equals("multiply")) {
			activateMultiply();
		} else if (type.equals("slow")) {
			if (ball.getThrowSpeed() == PropertiesGetter.getBallSpeed()) {
				ball.setThrowSpeed(ball.getThrowSpeed() - PropertiesGetter.getSlowAmount());
			} else {
				ball.setThrowSpeed(PropertiesGetter.getBallSpeed());
			}
		}
	}

	/** activate the multiply power-up */
	private void activateMultiply() {
		Random random = new Random();
		Image auxiliaryBallImage = new Image(getClass().getClassLoader().getResourceAsStream("resources/auxiliary-ball.png"));
		for (int i = 0; i < PropertiesGetter.getMultiplyCount(); i++) {
			// have the new balls enter from the top left with random velocity vectors
			Ball newAuxiliaryBall = new Ball(auxiliaryBallImage, 0, 0);
			newAuxiliaryBall.setVelocity(new Point2D(random.nextInt(BALL_THROW_X_COMPONENT),
					random.nextInt(PropertiesGetter.getBallSpeed())));
			gameGroup.getChildren().add(newAuxiliaryBall.getImage());
			spriteManager.addLiveSprite(newAuxiliaryBall);
		}
	}

	/**
	 * Process a collision between sprites
	 * 
	 * @param s1
	 *            - the first sprite to collide
	 * @param s2
	 *            - the second sprite to collide
	 */
	public void collide(Sprite s1, Sprite s2) {
		// have the sprites deal damage to each other if necessary
		exchangeDamage(s1, s2);
		exchangeDamage(s2, s1);
		// send the sprites backwards along the direction they were traveling
		s1.setVelocity(new Point2D(-s1.getVelocity().getX(), -s1.getVelocity().getY()));
		s2.setVelocity(new Point2D(-s2.getVelocity().getX(), -s2.getVelocity().getY()));
	}
	
	/**
	 * Generate a new bullet sprite
	 * 
	 * @param gameGroup
	 *            - root of the game scene
	 * @param x
	 *            - x-coordinate of the new bullet
	 * @param y
	 *            - y-coordinate of the new bullet
	 * @param fromBoss
	 *            - indicates source of the bullet: true if fired by the boss, false
	 *            if fired by the sprite
	 */
	public void addBullet(int x, int y, boolean fromBoss) {
		Bullet bullet = new Bullet(x, y, fromBoss);
		gameGroup.getChildren().add(bullet.getImage());
		spriteManager.addLiveSprite(bullet);
	}

	/**
	 * Move on to the next level
	 */
	private void transitionLevel() {
		if (currentLevel + 1 > PropertiesGetter.getLastLevel()) {
			showEndGameScreen(WON_STRING);
		} else {
			transitionLevel(currentLevel + 1);
		}
	}

	/**
	 * Skip to a specific level, either by beating the last level or by
	 * entering the level-jump cheat-codes
	 * 
	 * @param desiredLevel
	 *            - the level the player is advancing to
	 */
	private void transitionLevel(int desiredLevel) {
		paddle.resetPosition();
		ball.resetPosition();
		currentLevel = desiredLevel;
		ballLockedToPaddle = true;
		ball.setThrowSpeed(PropertiesGetter.getBallSpeed());
		paddleMoveSpeed = KEY_INPUT_SPEED;
		clearAll();
		gameGroup.getChildren().add(ball.getImage());
		Image levelBackground = new Image(getClass().getClassLoader()
				.getResourceAsStream(PropertiesGetter.getLevelBackgroundImage(currentLevel)));
		primaryScene.setFill(new ImagePattern(levelBackground));
		levelFactory.updateLevel(currentLevel);
		gameGroup.getChildren().addAll(levelFactory.getActivePowerUps());
		gameGroup.getChildren().addAll(levelFactory.getActiveBlocks());
	}

	/**
	 * Show a simple screen once the game has ended. Allow the user to exit or start
	 * over.
	 * 
	 * @param endState
	 *            - indicates if the user won or lost
	 */
	private void showEndGameScreen(String endState) {
		primaryScene.setFill(Color.WHITE);
		StackPane root = new StackPane();
		Text endInstructions = new Text(
				"You " + endState + "!\nPress ENTER to play again.\nPress Q to exit.");
		endInstructions.setTextAlignment(CENTER);
		endInstructions.setFill(Color.BLACK);
		endInstructions.setFont(Font.font(DISPLAYED_TEXT_FONT_SIZE));
		root.getChildren().add(endInstructions);
		primaryScene.setRoot(root);
		primaryScene.setOnKeyPressed(e -> handleEndGame(e.getCode()));
		StackPane.setAlignment(endInstructions, Pos.CENTER);
	}

	/**
	 * Process the user's input at end game, either restarting or exiting the game
	 * 
	 * @param code
	 *            - pressed key
	 */
	private void handleEndGame(KeyCode code) {
		if (code == KeyCode.ENTER) {
			initialize(primaryStage);
		} else if (code == KeyCode.Q){
			primaryStage.close();
		}
	}

	@Override
	public void refreshSprites() {
		for (Sprite s : spriteManager.getActiveSprites()) {
			s.updatePosition(SECOND_DELAY);
			s.bounce();
		}
		if (paddle.isDead() || ball.getY() > 600) {
			paddle.resetHealth();
			int livesRemaining = paddle.getLives() - 1;
			paddle.setLives(livesRemaining);
			livesIndicator.setText(LIVES_INDICATOR_BASE + (livesRemaining > 0 ? livesRemaining : 0));
			if (livesRemaining <= 0) {
				showEndGameScreen(LOSS_STRING);
			} else {
				paddle.resetPosition();
				ball.resetPosition();
			}
		}
	}

	/**
	 * Remove all game elements once the game is over.
	 * 
	 * @param gameGroup
	 *            - root of the game scene
	 */
	public void clearAll() {
		gameGroup.getChildren().removeAll(levelFactory.getActiveBlocks());
		gameGroup.getChildren().removeAll(levelFactory.getActivePowerUps());
		spriteManager.getActiveSprites().forEach(sprite -> gameGroup.getChildren().remove(sprite.getImage()));
	}
	
	/**
	 * Have sprite one take damage from sprite two if sprite one is a character
	 * 
	 * @param victim
	 *            - the sprite taking damage
	 * @param aggressor
	 *            - the sprite giving damage
	 */
	public void exchangeDamage(Sprite victim, Sprite aggressor) {
		if (victim instanceof Character) {
			Character character = (Character) victim;
			character.takeDamageFrom(aggressor);
		}
	}

	/**
	 * Remove blocks which have been destroyed from the game root
	 * 
	 * @param gameGroup
	 *            - root of the game scene
	 */
	private void removeDeadBlocks() {
		Iterator<Block> blockIterator = levelFactory.getActiveBlocks().iterator();
		while (blockIterator.hasNext()) {
			Block block = blockIterator.next();
			if (block.isDead()) {
				gameGroup.getChildren().remove(block);
				blockIterator.remove();
			}
		}
	}
	
	@Override
	public void removeDeadNodes() {
		removeDeadBlocks();
		if (levelFactory.outOfBlocks() && getGameLoop().getStatus() == Status.RUNNING) {
			transitionLevel();
		}
	}
	
}