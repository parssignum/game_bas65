
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
/**
 * An on-screen object capable of moving positions. Represented by an image.
 * Includes all characters (including the paddle), all balls, and bullets.
 * 
 * @author Ben Schwennesen
 *
 */
public abstract class Sprite {

	/** Image representing the sprite */
	private ImageView myImage;
	/** Velocity vector of the sprite */
	private Point2D myVelocity;
	/** Damage dealt - the amount of damage this sprite deals to others */
	private int myDamage;

	/**
	 * Empty constructor to allow classes to set image without passing
	 */
	public Sprite() {
	}

	/**
	 * Construct a bouncing, moving sprite
	 * 
	 * @param image
	 *            - graphical representation
	 */
	public Sprite(Image image) {
		setImage(image);
	}

	/**
	 * Return the bounds of this sprite for collision checking
	 * 
	 * @return Bounds - the boundary of the sprite
	 */
	public Bounds getBounds() {
		return getImage().getBoundsInLocal();
	}

	/**
	 * Check for collisions among sprites
	 * 
	 * @param other
	 *            : the other sprite aside from 'this' that we're checking for a
	 *            collision with
	 * @return true if the sprites overlap
	 */
	public boolean collidesWith(Object other) {
		if (other != null) {
			if (other instanceof Sprite) {
				Sprite s = (Sprite) other;
				return !s.equals(this) && this.getBounds().intersects(s.getBounds());
			} else if (other instanceof Rectangle) {
				Rectangle rect = (Rectangle) other;
				return this.getBounds().intersects(rect.getBoundsInLocal());
			}
		}
		return false;
	}

	/**
	 * Move the sprite along its velocity vector
	 * 
	 * @param timeStep
	 *            : dt, as in physics
	 */
	public void updatePosition(double timeStep) {
		getImage().setX(getImage().getX() + getVelocity().getX() * timeStep);
		getImage().setY(getImage().getY() + getVelocity().getY() * timeStep);
	}

	/**
	 * Reverse the path of travel of the sprite in the horizontal direction when
	 * hitting a block
	 */
	public void reverseCourseX() {
		setVelocity(new Point2D(-getVelocity().getX(), getVelocity().getY()));
	}

	/**
	 * Reverse the path of travel the sprite in the vertical direction when hitting
	 * a block
	 */
	public void reverseCourseY() {
		setVelocity(new Point2D(getVelocity().getX(), -getVelocity().getY()));
	}

	/**
	 * Bounce off all walls and corners when encountered, if this sprite should do
	 * so
	 */
	public abstract void bounce();

	@Override
	public int hashCode() {
		int hash = getImage().hashCode() * 17;
		hash = (hash + getVelocity().hashCode()) * 17;
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		return this == o || (o != null && o instanceof Sprite && getImage().equals(((Sprite) o).getImage()));
	}

	public ImageView getImage() {
		return myImage;
	}

	public void setImage(Image myImage) {
		this.myImage = new ImageView(myImage);
	}

	public double getX() {
		return myImage.getX();
	}

	public void setX(double x) {
		myImage.setX(x);
	}

	public double getY() {
		return myImage.getY();
	}

	public void setY(double y) {
		myImage.setY(y);
	}

	public double getHeight() {
		return getBounds().getHeight();
	}

	public double getWidth() {
		return getBounds().getWidth();
	}

	public Point2D getVelocity() {
		return myVelocity;
	}

	public void setVelocity(Point2D myVelocity) {
		this.myVelocity = myVelocity;
	}

	public int getDamageDealt() {
		return myDamage;
	}

	public void setDamageDealt(int myDamage) {
		this.myDamage = myDamage;
	}

	public double getCenterX() {
		return getX() + getWidth() / 2;
	}

	public double getCenterY() {
		return getY() + getHeight() / 2;
	}

	/**
	 * Bounce the sprite off a block in the correct direction. If the vertical
	 * distance between the centers of the block and sprite is greater than the
	 * horizontal distance between the centers, then the sprite needs to bounce in
	 * the vertical direction. Otherwise, it needs to bounce in the vertical
	 * direction.
	 * 
	 * @param block
	 *            - the block this sprite is currently intersecting
	 */
	public void bounceOffBlock(Block block) {
		double horizontalDistance = Math.abs(block.getCenterX() - getCenterX());
		double verticalDistance = Math.abs(block.getCenterY() - getCenterY());
		if (horizontalDistance < verticalDistance) {
			reverseCourseY();
		} else {
			reverseCourseX();
		}
	}
}