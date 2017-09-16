package game;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuItem extends Pane {

	/* menu item property constants */
	private final int HEIGHT = PropertiesGetter.getMenuItemHeight();
	private final int WIDTH = PropertiesGetter.getMenuItemWidth();
	private final int FONT_SIZE = PropertiesGetter.getMenuItemFontSize();
	private final int ROUNDEDNESS = PropertiesGetter.getMenuItemRoundedness();
	private final int BORDER_WIDTH = PropertiesGetter.getMenuItemBorderWidth();
	private final int X_OFFSET = PropertiesGetter.getMenuItemXOffset();
	private final Color DUKE_BLUE = Color.rgb(0, 0, 156);

	/** the text displayed over this menu item (its name) */
    private Text itemText;

	/**
	 * Make an item (button) for placement in the main menu
	 * 
	 * @param itemName
	 *            - the name of this item (e.g., "PLAY GAME")
	 */
	public MenuItem(String itemName) {
		// make a base/bounding box
		Rectangle base = new Rectangle(WIDTH, HEIGHT);
		base.setArcHeight(ROUNDEDNESS);
		base.setArcWidth(ROUNDEDNESS);
		base.setStroke(Color.BLACK);
		base.setStrokeWidth(BORDER_WIDTH);
		base.setFill(DUKE_BLUE);
		// make and format the item text
		itemText = new Text(itemName);
		itemText.setTranslateX(X_OFFSET);
		itemText.setTranslateY(HEIGHT - FONT_SIZE);
		itemText.setFont(Font.font(FONT_SIZE));
		itemText.setFill(Color.WHITE);
		// add to the pane
		getChildren().addAll(base, itemText);

	}

	/**
	 * Set the item's behavior upon clicking
	 * 
	 * @param action
	 *            - action to be performed on when clicking the item
	 */
	public void setClick(Runnable action) {
		setOnMouseClicked(e -> action.run());
	}
}