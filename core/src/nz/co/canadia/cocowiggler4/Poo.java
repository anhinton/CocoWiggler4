package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import nz.co.canadia.cocowiggler4.util.Constants;

/**
 * Coco makes a lot of these, and eats about half of them.
 */

class Poo {
    private Texture bitmap;
    private Sprite sprite;

    Poo(Array<Texture> pooBitmaps, float x, float y) {
        bitmap = pooBitmaps.random();
        TextureRegion region = new TextureRegion(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        sprite = new Sprite(region);
        sprite.setOrigin(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        sprite.setPosition(x, y);
        sprite.setRotation(MathUtils.random(0, 359));
    }

    void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    void dispose() {
        bitmap.dispose();
    }
}
