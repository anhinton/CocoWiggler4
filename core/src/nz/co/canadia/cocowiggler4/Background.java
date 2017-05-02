package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This is just the background image. Doesn't do much, but it knows it loves you.
 */

class Background {
    private Texture bitmap;
    private Sprite sprite;

    Background(AssetManager manager) {
        bitmap = manager.get("grass.png", Texture.class);
        TextureRegion grassRegion = new TextureRegion(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        sprite = new Sprite(grassRegion);
        sprite.setOrigin(0, 0);
        sprite.setPosition(0, 0);
    }

    void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    void dispose() {
        bitmap.dispose();
    }
}
