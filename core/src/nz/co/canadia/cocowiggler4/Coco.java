package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import nz.co.canadia.cocowiggler4.util.Constants;

/**
 * Coco is the star of the game, the whole reason we show up.
 */

class Coco {
    private Texture bitmap;
    private Sprite sprite;
    private float rot;
    private boolean pressingLeft;
    private boolean pressingRight;
    private boolean pressingUp;
    private boolean pressingDown;
    private boolean moving;
    private boolean facingRight;
    private Vector3 target;

    Coco (AssetManager manager, Array<Poo> poos) {
        // initialize variables
        pressingLeft = false;
        pressingRight = false;
        pressingUp = false;
        pressingDown = false;

        facingRight = false;

        moving = false;
        target = new Vector3();

        bitmap = manager.get("coco.png", Texture.class);
        TextureRegion region = new TextureRegion(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        sprite = new Sprite(region);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition(Constants.APP_WIDTH / 2 - bitmap.getWidth() / 2,
                Constants.APP_HEIGHT / 2 - bitmap.getHeight() / 2);

        spawnPoo(manager, poos);
    }

    void update(Camera camera) {
        // Movement controls

        // Keyboard movement
        pressingLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        pressingRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        pressingUp = Gdx.input.isKeyPressed(Input.Keys.UP);
        pressingDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (pressingLeft) {
            if (facingRight) {
                facingRight = false;
            }
            if (pressingUp) {
                sprite.setX(sprite.getX() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                sprite.setY(sprite.getY() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
            } else if (pressingDown) {
                sprite.setX(sprite.getX() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                sprite.setY(sprite.getY() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());

            } else {
                sprite.setX(sprite.getX() - Constants.SPEED * Gdx.graphics.getDeltaTime());
            }
        }
        if (pressingRight) {
            if (!facingRight) {
                facingRight = true;
            }
            if (pressingUp) {
                sprite.setX(sprite.getX() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                sprite.setY(sprite.getY() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
            } else if (pressingDown) {
                sprite.setX(sprite.getX() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                sprite.setY(sprite.getY() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());

            } else {
                sprite.setX(sprite.getX() + Constants.SPEED * Gdx.graphics.getDeltaTime());
            }
        }
        if (pressingUp & !pressingLeft & !pressingRight) {
            sprite.setY(sprite.getY() + Constants.SPEED * Gdx.graphics.getDeltaTime());
        }
        if (pressingDown & !pressingLeft & !pressingRight) {
            sprite.setY(sprite.getY() - Constants.SPEED * Gdx.graphics.getDeltaTime());
        }

        // Mouse/touch-screen movement
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            moving = true;
            camera.unproject(target.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            // stop target from clipping Coco outside
            if (target.x < sprite.getWidth() / 2) target.x = sprite.getWidth() / 2;
            if (target.x > Constants.APP_WIDTH - sprite.getWidth() / 2)
                target.x = Constants.APP_WIDTH - sprite.getWidth() / 2;
            if (target.y < sprite.getHeight() / 2) target.y = sprite.getHeight() / 2;
            if (target.y > Constants.APP_HEIGHT - sprite.getHeight() / 2)
                target.y = Constants.APP_HEIGHT - sprite.getHeight() / 2;


        }
        if (moving) {
            if (Math.abs(sprite.getX() - target.x + sprite.getWidth() / 2) < 5
                    & Math.abs(sprite.getY() - target.y + sprite.getHeight() / 2) < 5) {
                moving = false;
            } else {
                move();
            }

        }

        // don't let Coco escape
        if (sprite.getX() < 0) sprite.setX(0);
        if (sprite.getX() > Constants.APP_WIDTH - sprite.getWidth())
            sprite.setX(Constants.APP_WIDTH - sprite.getWidth());
        if (sprite.getY() < 0) sprite.setY(0);
        if (sprite.getY() > Constants.APP_HEIGHT - sprite.getHeight())
            sprite.setY(Constants.APP_HEIGHT - sprite.getHeight());

        // set flip
        sprite.setFlip(facingRight, false);
    }

    void draw(SpriteBatch batch) {
        rotate();
        sprite.draw(batch);
    }

    void dispose() {
        bitmap.dispose();
    }

    // This method is used to make Coco move towards a target select by touching clicking
    private void move() {
        // calculate distance along adjacent and opposite sides
        float pathX = target.x - sprite.getWidth() / 2 - sprite.getX();
        float pathY = target.y - sprite.getHeight() / 2 - sprite.getY();

        // flip Coco on direction change
        if (pathX < 0 && facingRight) {
            facingRight = false;
        }
        if (pathX > 0 && !facingRight) {
            facingRight = true;
        }

        // calculate distance along hypotenuse
        float distance = (float) Math.sqrt(pathX * pathX + pathY * pathY);
        // change is ratio between side an hypotenuse
        float changeX = pathX / distance;
        float changeY = pathY / distance;

        // move Coco
        sprite.setX(sprite.getX() + changeX * Constants.SPEED * Gdx.graphics.getDeltaTime());
        sprite.setY(sprite.getY() + changeY * Constants.SPEED * Gdx.graphics.getDeltaTime());
    }

    private void rotate() {
        final float degreesPerSecond = 10.0f;
        rot = (rot + Gdx.graphics.getDeltaTime() * degreesPerSecond) % 360;
        final float shakeAmplitudeInDegrees = 5.0f;
        float shake = MathUtils.sin(rot) * shakeAmplitudeInDegrees;
        sprite.setRotation(shake);
    }

    private void spawnPoo(AssetManager manager, Array<Poo> poos) {
        Poo poo = new Poo(manager);
        poos.add(poo);
    }

}
