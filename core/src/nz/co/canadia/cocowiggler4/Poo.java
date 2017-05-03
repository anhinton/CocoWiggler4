package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import nz.co.canadia.cocowiggler4.util.Constants;

/**
 * Coco makes a lot of these, and eats about half of them.
 */

class Poo {
    private Texture bitmap;
    private Sprite sprite;

    Poo(AssetManager manager) {
        switch(MathUtils.random(1, 9)) {
            case 1:
                bitmap = manager.get("poo01.png", Texture.class);
                break;
            case 2:
                bitmap = manager.get("poo02.png", Texture.class);
                break;
            case 3:
                bitmap = manager.get("poo03.png", Texture.class);
                break;
            case 4:
                bitmap = manager.get("poo04.png", Texture.class);
                break;
            case 5:
                bitmap = manager.get("poo05.png", Texture.class);
                break;
            case 6:
                bitmap = manager.get("poo06.png", Texture.class);
                break;
            case 7:
                bitmap = manager.get("poo07.png", Texture.class);
                break;
            case 8:
                bitmap = manager.get("poo08.png", Texture.class);
                break;
            case 9:
                bitmap = manager.get("poo09.png", Texture.class);
                break;
        }
        TextureRegion region = new TextureRegion(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        sprite = new Sprite(region);
        sprite.setOrigin(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        int pooX = MathUtils.random(0, Constants.APP_WIDTH - bitmap.getWidth());
        int pooY = MathUtils.random(0, Constants.APP_HEIGHT - bitmap.getHeight());
        sprite.setPosition(pooX, pooY);
        sprite.setRotation(MathUtils.random(0, 359));
    }

    void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    void dispose() {
        bitmap.dispose();
    }
}
