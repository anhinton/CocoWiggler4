package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import nz.co.canadia.cocowiggler4.util.Constants;

/**
 * Coco makes a lot of these, and eats about half of them.
 */

class Poo {
    private Texture bitmap;
    private Sprite sprite;
    private Circle boundingCircle;
    private boolean edible;
    private boolean eaten;

    Poo(Array<Texture> pooBitmaps, float x, float y) {
        bitmap = pooBitmaps.random();
        TextureRegion region = new TextureRegion(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        sprite = new Sprite(region);
        sprite.setOriginCenter();
        sprite.setRotation(MathUtils.random(0, 359));
        sprite.setPosition(x, y);
        edible = MathUtils.randomBoolean();
        eaten = false;
        boundingCircle = new Circle(x + sprite.getOriginX(), y + sprite.getOriginY(),
                Math.max(sprite.getWidth(), sprite.getHeight()) / 2);
    }

    boolean isEdible() {
        return edible;
    }

    void eatPoo() {
        edible = false;
        eaten = true;
    }

    boolean isEaten() {
        return eaten;
    }

    Circle getBoundingCircle() {
        return boundingCircle;
    }

    void draw(SpriteBatch batch) {
        if (!eaten) {
            sprite.draw(batch);
        }
    }

    void drawBounds(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(boundingCircle.x, boundingCircle.y, boundingCircle.radius);
    }

    void dispose() {
        bitmap.dispose();
    }
}
