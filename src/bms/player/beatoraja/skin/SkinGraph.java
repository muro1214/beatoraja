package bms.player.beatoraja.skin;

import bms.player.beatoraja.MainState;
import bms.player.beatoraja.skin.Skin.SkinObjectRenderer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * グラフイメージ
 */
public class SkinGraph extends SkinObject {

	/**
	 * イメージ
	 */
	private SkinSource source;
	/**
	 * グラフの参照値(将来廃止予定)
	 */
	private final int id;
	/**
	 * グラフ値参照先
	 */	
	private final FloatProperty ref;	
	/**
	 * グラフの伸びる向き(1:下, それ以外:右)
	 */
	private int direction = 1;

	private final TextureRegion current = new TextureRegion();

	public SkinGraph(int imageid, int id) {
		setImageID(imageid);
		ref = SkinPropertyMapper.getFloatProperty(id);
		this.id = id;
	}

	public SkinGraph(int imageid, int id, int min, int max) {
		setImageID(imageid);
		ref = new RateProperty(id, min, max);
		this.id = id;
	}

	public SkinGraph(TextureRegion[] image, int timer, int cycle, int id) {
		source = new SkinSourceImage(image, timer, cycle);
		ref = SkinPropertyMapper.getFloatProperty(id);
		this.id = id;
	}

	public SkinGraph(TextureRegion[] image, int timer, int cycle, int id, int min, int max) {
		source = new SkinSourceImage(image, timer, cycle);
		ref = new RateProperty(id, min, max);
		this.id = id;
	}

	public void draw(SkinObjectRenderer sprite, long time, MainState state) {
		if (getImageID() != -1) {
			Rectangle r = this.getDestination(time, state);
			TextureRegion image = state.getImage(getImageID());
			if (r != null && image != null) {
				float value = ref != null ? ref.get(state) : (id != -1 ? state.getSliderValue(id) : 0);
				if (direction == 1) {
					current.setRegion(image, 0,
							image.getRegionY() + image.getRegionHeight() - (int) (image.getRegionHeight() * value),
							image.getRegionWidth(), (int) (image.getRegionHeight() * value));
					draw(sprite, current, r.x, r.y, r.width, r.height * value, state);
				} else {
					current.setRegion(image, 0, image.getRegionY(), (int) (image.getRegionWidth() * value),
							image.getRegionHeight());
					draw(sprite, current, r.x, r.y, r.width * value, r.height, state);
				}
			}
		} else if (source != null) {
			Rectangle r = this.getDestination(time, state);
			if (r != null) {
				float value = ref != null ? ref.get(state) : (id != -1 ? state.getSliderValue(id) : 0);
				TextureRegion image = source.getImage(time, state);
				if (direction == 1) {
					current.setRegion(image, 0, image.getRegionHeight() - (int) (image.getRegionHeight() * value),
							image.getRegionWidth(), (int) (image.getRegionHeight() * value));
					draw(sprite, current, r.x, r.y, r.width, r.height * value, state);
				} else {
					current.setRegion(image, 0, 0, (int) (image.getRegionWidth() * value), image.getRegionHeight());
					draw(sprite, current, r.x, r.y, r.width * value, r.height, state);
				}
			}
		}
	}

	public void dispose() {
		if (source != null) {
			source.dispose();
			source = null;
		}
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
