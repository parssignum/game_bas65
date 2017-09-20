
import java.util.HashSet;
import java.util.Set;

public class SpriteManager {
	
	/** set of living sprites */
	private Set<Sprite> activeSprites = new HashSet<>();
	/** set of sprites which have been killed */
	private Set<Sprite> deadSprites = new HashSet<>();
	
	public SpriteManager() {
		
	}
	/**
	 * Collide a block with a sprite (bullet or boss, in this case)
	 * 
	 * @param block
	 * @param sprite
	 */
	public void collide(Block block, Sprite sprite) {
		block.takeDamageFrom(sprite);
	}

	public void addLiveSprite (Sprite toAdd) {
		activeSprites.add(toAdd);
	}
	
	
	public Set<Sprite> getActiveSprites() {
		return activeSprites;
	}

	public Set<Sprite> getDeadSprites() {
		return deadSprites;
	}
	
}