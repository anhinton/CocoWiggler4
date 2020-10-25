package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
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
    private final Texture bitmap;
    private final Sprite sprite;
    private final Sound chomp;
    private final Sound pop;
    private final float soundVolume;
    private float rot;
    private boolean movingLeft;
    private boolean movingRight;
    private boolean movingUp;
    private boolean movingDown;
    private boolean moving;
    private boolean facingRight;
    private float changeX;
    private float changeY;
    private final Vector2 headCentre;
    private Vector3 target;
    private long lastPooTime;
    private long pooDelay;
    private int pooCount;
    private int eatenCount;

    Coco(float soundVolume) {
        this.soundVolume = soundVolume;

        // initialize variables
        movingLeft = false;
        movingRight = false;
        movingUp = false;
        movingDown = false;

        facingRight = false;
        headCentre = new Vector2();

        moving = false;
        target = new Vector3();
        changeX = 0;
        changeY = 0;

        chomp = Gdx.audio.newSound(Gdx.files.internal("sounds/chomp.mp3"));
        pop = Gdx.audio.newSound(Gdx.files.internal("sounds/pop.mp3"));

        bitmap = new Texture("graphics/coco.png");
        bitmap.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion region = new TextureRegion(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        sprite = new Sprite(region);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition(Constants.APP_WIDTH / 2f - bitmap.getWidth() / 2f,
                Constants.APP_HEIGHT / 2f - bitmap.getHeight() / 2f);

        calculateHeadCentre();

        lastPooTime = TimeUtils.nanoTime();
        pooDelay = (long) MathUtils.randomTriangular(Constants.POO_TIME_MIN,
                Constants.POO_TIME_MAX);

        pooCount = 0;
        eatenCount = 0;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void setTarget(Vector3 target) {
        this.target = target;
        moving = true;
    }

    private void calculateHeadCentre() {
        if (facingRight) {
            headCentre.x = sprite.getWidth() * 5 / 6;
        } else {
            headCentre.x = sprite.getWidth() / 6;
        }
        headCentre.y = sprite.getHeight() / 5;
    }

    private Circle getMouthBoundingCircle() {
        return(new Circle(sprite.getX() + headCentre.x, sprite.getY() + headCentre.y,
                sprite.getWidth() / 10));
    }

    void update(Array<Texture> pooBitmaps, Array<Poo> poos) {
        // where is my head at
        calculateHeadCentre();

        // poo if you need to
        if (TimeUtils.nanoTime() - lastPooTime > pooDelay) {
            spawnPoo(pooBitmaps, poos);
            pooCount++;
        }

        // eat poo if you can
        for (Poo poo: poos) {
            if (poo.getBoundingCircle().overlaps(getMouthBoundingCircle())){
                if (poo.isEdible()) {
                    poo.eatPoo();
                    chomp.play(soundVolume);
                    eatenCount++;
                }
            }
        }

        // perform movement
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

    void drawBounds(ShapeRenderer shapeRenderer) {
        Circle bound = getMouthBoundingCircle();
        shapeRenderer.circle(bound.x, bound.y, bound.radius);
    }

    void dispose() {
        bitmap.dispose();
        chomp.dispose();
        pop.dispose();
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

        if (movingLeft && movingRight) {
            movingLeft = false;
            movingRight = false;
        }
        if (movingUp && movingDown) {
            movingUp = false;
            movingDown = false;
        }

        if (movingLeft) {
            if (facingRight) {
                facingRight = false;
            }
            if (movingUp) {
                changeX = -Constants.ANGLE_SPEED;
                changeY = Constants.ANGLE_SPEED;
            } else if (movingDown) {
                changeX = -Constants.ANGLE_SPEED;
                changeY = -Constants.ANGLE_SPEED;
            } else {
                changeX = -Constants.SPEED;
            }
        }
        if (movingRight) {
            if (!facingRight) {
                facingRight = true;
            }
            if (movingUp) {
                changeX = Constants.ANGLE_SPEED;
                changeY = Constants.ANGLE_SPEED;
            } else if (movingDown) {
                changeX = Constants.ANGLE_SPEED;
                changeY = -Constants.ANGLE_SPEED;
            } else {
                changeX = Constants.SPEED;
            }
        }
        if (movingUp & !movingLeft & !movingRight) {
            changeY =  Constants.SPEED;
        }
        if (movingDown & !movingLeft & !movingRight) {
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

        // create a new poo
        Poo poo = new Poo(pooBitmaps, pooX, pooY);
        poos.add(poo);

        // poo time calculations
        lastPooTime = TimeUtils.nanoTime();
        pooDelay = (long) MathUtils.randomTriangular(Constants.POO_TIME_MIN,
                Constants.POO_TIME_MAX);

        // make a poo sound
        pop.play(soundVolume);
    }

    int getPooCount() {
        return pooCount;
    }

    int getEatenCount() {
        return eatenCount;
    }
}
