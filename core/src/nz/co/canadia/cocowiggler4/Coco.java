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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

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
    private float changeX;
    private float changeY;
    private Vector2 headCentre;
    private Vector3 target;
    private long lastPooTime;
    private long pooDelay;

    Coco () {
        // initialize variables
        pressingLeft = false;
        pressingRight = false;
        pressingUp = false;
        pressingDown = false;

        facingRight = false;
        headCentre = new Vector2();

        moving = false;
        target = new Vector3();
        changeX = 0;
        changeY = 0;

        bitmap = new Texture("graphics/coco.png");
        bitmap.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion region = new TextureRegion(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        sprite = new Sprite(region);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition(Constants.APP_WIDTH / 2 - bitmap.getWidth() / 2,
                Constants.APP_HEIGHT / 2 - bitmap.getHeight() / 2);

        lastPooTime = TimeUtils.nanoTime();
        pooDelay = (long) MathUtils.randomTriangular(Constants.POO_TIME_MIN,
                Constants.POO_TIME_MAX);
    }

    private void calculateHeadCentre() {
        if (facingRight) {
            headCentre.x = sprite.getWidth() * 5 / 6;
            headCentre.y = sprite.getHeight() / 5;
        } else {
            headCentre.x = sprite.getWidth() / 6;
            headCentre.y = sprite.getHeight() / 5;
        }
    }

    void update(Camera camera, Array<Texture> pooBitmaps, Array<Poo> poos) {
        if (TimeUtils.nanoTime() - lastPooTime > pooDelay) {
            spawnPoo(pooBitmaps, poos);
        }

        // Movement controls

        // Keyboard movement
        pressingLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        pressingRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        pressingUp = Gdx.input.isKeyPressed(Input.Keys.UP);
        pressingDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        // cancel opposites
        if (pressingLeft && pressingRight) {
            pressingLeft = false;
            pressingRight = false;
        }
        if (pressingUp && pressingDown) {
            pressingUp = false;
            pressingDown = false;
        }

        // Mouse/touch-screen movement
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            moving = true;
            camera.unproject(target.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        }

        // perform movement
        calculateHeadCentre();
        move();

        // don't let Coco escape
        if (sprite.getX() < 0) sprite.setX(0);
        if (sprite.getX() > Constants.APP_WIDTH - sprite.getWidth())
            sprite.setX(Constants.APP_WIDTH - sprite.getWidth());
        if (sprite.getY() < 0) sprite.setY(0);
        if (sprite.getY() > Constants.APP_HEIGHT - sprite.getHeight())
            sprite.setY(Constants.APP_HEIGHT - sprite.getHeight());

        // stop target from clipping Coco outside
        if (target.x < headCentre.x) target.x = headCentre.x;
        if (target.x > Constants.APP_WIDTH - sprite.getWidth() + headCentre.x)
            target.x = Constants.APP_WIDTH - sprite.getWidth() + headCentre.x;
        if (target.y < headCentre.y) target.y = headCentre.y;
        if (target.y > Constants.APP_HEIGHT - sprite.getHeight() + headCentre.y)
            target.y = Constants.APP_HEIGHT - sprite.getHeight() + headCentre.y;

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
        changeX = 0;
        changeY = 0;

        if (moving) {
            if (Math.abs(sprite.getX() + headCentre.x - target.x) < 2
                    && Math.abs(sprite.getY() + headCentre.y - target.y) < 2) {
                moving = false;
            } else {
                // calculate distance along adjacent and opposite sides
                float pathX = target.x - sprite.getX() - headCentre.x;
                float pathY = target.y - sprite.getY() - headCentre.y;

                // flip Coco on direction change
                if (pathX < -sprite.getWidth() * Constants.FLIP_THRESHOLD && facingRight) {
                    facingRight = false;
                }
                if (pathX > sprite.getWidth() * Constants.FLIP_THRESHOLD && !facingRight) {
                    facingRight = true;
                }

                // calculate distance along hypotenuse
                float distance = (float) Math.sqrt(pathX * pathX + pathY * pathY);
                // change is ratio between side an hypotenuse
                changeX = pathX / distance * Constants.SPEED;
                changeY = pathY / distance * Constants.SPEED;
            }
        }

        if (pressingLeft) {
            if (facingRight) {
                facingRight = false;
            }
            if (pressingUp) {
                changeX = -Constants.ANGLE_SPEED;
                changeY = Constants.ANGLE_SPEED;
            } else if (pressingDown) {
                changeX = -Constants.ANGLE_SPEED;
                changeY = -Constants.ANGLE_SPEED;

            } else {
                changeX = -Constants.SPEED;
            }
        }
        if (pressingRight) {
            if (!facingRight) {
                facingRight = true;
            }
            if (pressingUp) {
                changeX = Constants.ANGLE_SPEED;
                changeY = Constants.ANGLE_SPEED;
            } else if (pressingDown) {
                changeX = Constants.ANGLE_SPEED;
                changeY = -Constants.ANGLE_SPEED;

            } else {
                changeX = Constants.SPEED;
            }
        }
        if (pressingUp & !pressingLeft & !pressingRight) {
            changeY =  Constants.SPEED;
        }
        if (pressingDown & !pressingLeft & !pressingRight) {
            changeY = -Constants.SPEED;
        }

        // clamp speed
        if (Math.abs(changeX) > Constants.SPEED) {
            changeX = MathUtils.clamp(changeX, -Constants.SPEED, Constants.SPEED);
        }
        if (Math.abs(changeY) > Constants.SPEED) {
            changeY = MathUtils.clamp(changeY, -Constants.SPEED, Constants.SPEED);
        }

        // move Coco
        sprite.setX(sprite.getX() + changeX * Gdx.graphics.getDeltaTime());
        sprite.setY(sprite.getY() + changeY * Gdx.graphics.getDeltaTime());

    }

    private void rotate() {
        final float degreesPerSecond = 10.0f;
        rot = (rot + Gdx.graphics.getDeltaTime() * degreesPerSecond) % 360;
        final float shakeAmplitudeInDegrees = 5.0f;
        float shake = MathUtils.sin(rot) * shakeAmplitudeInDegrees;
        sprite.setRotation(shake);
    }

    private void spawnPoo(Array<Texture> pooBitmaps, Array<Poo> poos) {
        float pooX;
        if (facingRight) {
            pooX = sprite.getX() + sprite.getWidth() / 9;
        } else {
            pooX = sprite.getX() + sprite.getWidth() * 4 / 5;
        }
        float pooY = sprite.getY() + sprite.getHeight() / 5;

        Poo poo = new Poo(pooBitmaps, pooX, pooY);
        poos.add(poo);
        lastPooTime = TimeUtils.nanoTime();
        pooDelay = (long) MathUtils.randomTriangular(Constants.POO_TIME_MIN,
                Constants.POO_TIME_MAX);
    }

}
