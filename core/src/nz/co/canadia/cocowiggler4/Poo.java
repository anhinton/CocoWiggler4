package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Coco makes a lot of these, and eats about half of them.
 */

class Poo {
    private Texture bitmap;
    private Sprite sprite;

    Poo(AssetManager manager) {
        bitmap = manager.get("poo01.png", Texture.class);
        TextureRegion region = new TextureRegion(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        sprite = new Sprite(region);
        sprite.setOrigin(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        sprite.setPosition(50, 50);
    }

    void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    void dispose() {
        bitmap.dispose();
    }
}
