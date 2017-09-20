

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Application class for launching the game. It will initially launch the main
 * menu and will await a user prompt before starting the game.
 * 
 * @author Ben Schwennesen
 */
public class BreakoutMain extends Application {

	/* main menu properties */
	private final int SCENE_HEIGHT = PropertiesGetter.getScreenHeight(); // 600
	private final int SCENE_WIDTH = PropertiesGetter.getScreenWidth(); // 400
	private final int POP_UP_WIDTH = PropertiesGetter.getPopUpWidth();
	private final int POP_UP_HEIGHT = PropertiesGetter.getPopUpHeight();
	private final int MENU_TITLE_FONT_SIZE = PropertiesGetter.getMenuTitleFontSize();
	private final double MAIN_MENU_MARGIN = PropertiesGetter.getMenuMargin();
	private final Color DUKE_BLUE = Color.rgb(0, 0, 156);

	/*
	 * the idea for this data structure came from:
	 * https://github.com/AlmasB/FXTutorials/blob/master/src/com/almasb/civ6menu/
	 * Civ6MenuApp.java
	 */
	/**
	 * A list of Pairs of menu item names and the action they perform when clicked
	 * on
	 */
	private final List<Pair<String, Runnable>> menuItems = Arrays.asList(
			new Pair<String, Runnable>("PLAY GAME", () -> initializeLevels()),
			new Pair<String, Runnable>("RULES", () -> displayRules()),
			new Pair<String, Runnable>("CHEAT CODES", () -> displayCheatCodes()),
			new Pair<String, Runnable>("STORY BLURB", () -> displayStory()),
			new Pair<String, Runnable>("EXIT", Platform::exit));

	/** a Pane which contains all menu items */
	private Pane menuRoot = new Pane();
	/**
	 * the game's primary stage, where the main menu scene and eventually levels are
	 * displayed
	 */
	private Stage primaryStage;

	/**
	 * Initialize the game
	 */
	@Override
	public void start(Stage primaryStage) {
		createMenu();
		Scene menuScene = new Scene(menuRoot, SCENE_WIDTH, SCENE_HEIGHT);
		menuScene.setFill(Color.SILVER);
		primaryStage.setScene(menuScene);
		this.primaryStage = primaryStage;
		primaryStage.setTitle(PropertiesGetter.getTitle());
		primaryStage.show();
	}

	/**
	 * Create the main menu
	 */
	public void createMenu() {
		Pane title = createTitle();
		VBox menuArea = createMenuArea();
		menuRoot.getChildren().addAll(title, menuArea);
	}

	/**
	 * Create and return a wrapper box for menu items
	 * 
	 * @return a VBox containing all menu items
	 */
	private VBox createMenuArea() {
		/*
		 * this method is based on one found here:
		 * https://github.com/AlmasB/FXTutorials/blob/master/src/com/almasb/civ6menu/
		 * Civ6MenuApp.java
		 */
		VBox menu = new VBox(-5);
		menu.setTranslateX(SCENE_WIDTH / 2 - MAIN_MENU_MARGIN);
		menu.setTranslateY(SCENE_HEIGHT / 3);
		int roundedness = PropertiesGetter.getMenuItemRoundedness();
		menuItems.forEach(menuItem -> {
			MenuItem item = new MenuItem(menuItem.getKey());
			item.setClick(menuItem.getValue());
			Rectangle base = new Rectangle(PropertiesGetter.getMenuItemWidth(), PropertiesGetter.getMenuItemHeight());
			base.setArcHeight(roundedness);
			base.setArcWidth(roundedness);
			item.setClip(base);
			menu.getChildren().add(item);
		});
		return menu;
	}

	/**
	 * Create an element for the game's title, to be displayed in the main menu
	 * 
	 * @return a Pane containing the game title
	 */
	private Pane createTitle() {
		Text title = new Text(PropertiesGetter.getTitle().toUpperCase());
		title.setFont(Font.font(MENU_TITLE_FONT_SIZE));
		title.setFill(DUKE_BLUE);
		Pane container = new Pane();
		container.getChildren().add(title);
		container.setTranslateX(SCENE_WIDTH / 2 - title.getLayoutBounds().getWidth() / 2);
		container.setTranslateY(SCENE_HEIGHT / 4);
		return container;
	}

	/**
	 * Create and display a pop up dialog box containing the game's rules
	 */
	private void displayRules() {
		String rules = null;
		try {
			rules = getFileAsString("game-rules.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		setUpDialogBox(rules);
	}

	/**
	 * Create and display a pop up dialog box containing the game's usable cheat
	 * codes
	 */
	private void displayCheatCodes() {
		String cheats = null;
		try {
			cheats = getFileAsString("cheat-codes.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		setUpDialogBox(cheats);
	}

	/**
	 * Create and display a pop up dialog box containing a (very) short
	 * stage-setting story
	 */
	private void displayStory() {
		String story = null;
		try {
			story = getFileAsString("story-blurb.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		setUpDialogBox(story);
	}

	/**
	 * Set up a dialog box containing information about the game
	 * 
	 * @param fileString
	 *            - the text to be displayed in the box, read in from a file
	 */
	private void setUpDialogBox(String fileString) {
		Stage rulesPopUp = new Stage();
		rulesPopUp.initOwner(primaryStage);
		VBox dialogBox = new VBox(50);
		dialogBox.getChildren().add(new Text(fileString));
		Scene popUpScene = new Scene(dialogBox, POP_UP_WIDTH, POP_UP_HEIGHT);
		rulesPopUp.setScene(popUpScene);
		rulesPopUp.show();
	}

	/**
	 * 
	 * Read an entire file into a single string
	 * 
	 * @param fileName
	 *            - the name of the file to read in
	 * @return a string representation of the file
	 * @throws IOException
	 */
	private String getFileAsString(String fileName) throws IOException {
		InputStream rulesAsInputStream = getClass().getClassLoader().getResourceAsStream(fileName);
		String fileAsString = readStream(rulesAsInputStream);
		rulesAsInputStream.close();
		return fileAsString;
	}

	/**
	 * Transform an InputStream for a file into a string
	 * 
	 * @param inputStream
	 *            - a stream containing file data
	 * @return a string representation of the file
	 */
	public String readStream(InputStream inputStream) {
		// this method was copied from:
		// https://stackoverflow.com/questions/3849692/whole-text-file-to-a-string-in-java
		StringBuilder sb = new StringBuilder(512);
		try {
			Reader r = new InputStreamReader(inputStream, "UTF-8");
			int c = 0;
			while ((c = r.read()) != -1) {
				sb.append((char) c);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

	/**
	 * Setup and display the game itself
	 */
	private void initializeLevels() {
		HostileBreakout myGame = new HostileBreakout();
		myGame.initialize(primaryStage);
		// display window
		primaryStage.show();
		// attach "game loop" to timeline to play it
		Timeline animation = myGame.getGameLoop();
		animation.play();
	}

	/**
	 * Launch the application
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
