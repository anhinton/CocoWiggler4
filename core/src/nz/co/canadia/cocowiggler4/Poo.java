package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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
//
//        switch(MathUtils.random(1, 9)) {
//            case 1:
//                bitmap = manager.get("graphics/poo01.png", Texture.class);
//                break;
//            case 2:
//                bitmap = manager.get("graphics/poo02.png", Texture.class);
//                break;
//            case 3:
//                bitmap = manager.get("graphics/poo03.png", Texture.class);
//                break;
//            case 4:
//                bitmap = manager.get("graphics/poo04.png", Texture.class);
//                break;
//            case 5:
//                bitmap = manager.get("graphics/poo05.png", Texture.class);
//                break;
//            case 6:
//                bitmap = manager.get("graphics/poo06.png", Texture.class);
//                break;
//            case 7:
//                bitmap = manager.get("graphics/poo07.png", Texture.class);
//                break;
//            case 8:
//                bitmap = manager.get("graphics/poo08.png", Texture.class);
//                break;
//            case 9:
//                bitmap = manager.get("graphics/poo09.png", Texture.class);
//                break;
//        }
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
