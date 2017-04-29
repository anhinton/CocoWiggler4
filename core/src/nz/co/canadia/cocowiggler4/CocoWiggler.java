package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import nz.co.canadia.cocowiggler4.util.Constants;

public class CocoWiggler extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture cocoImage;
    private Texture grassImage;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Sprite coco;
    private Sprite grass;
    private float rot;
    private boolean pressingLeft;
    private boolean pressingRight;
    private boolean pressingUp;
    private boolean pressingDown;
    private boolean seekingTarget;
    private boolean facingRight;
    private Vector3 targetXYZ;
    private float pathX;
    private float pathY;
    private float distance;
    private float changeX;
    private float changeY;

    @Override
    public void create() {
        pressingLeft = false;
        pressingRight = false;
        pressingUp = false;
        pressingDown = false;
        facingRight = false;

        seekingTarget = false;
        targetXYZ = new Vector3();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        viewport = new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT, camera);

        batch = new SpriteBatch();

        cocoImage = new Texture(Gdx.files.internal("coco.png"));
        cocoImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion cocoRegion = new TextureRegion(cocoImage, 0, 0, cocoImage.getWidth(),
                cocoImage.getHeight());
        coco = new Sprite(cocoRegion);
        coco.setOrigin(coco.getWidth() / 2, coco.getHeight() / 2);
        coco.setPosition(Constants.APP_WIDTH / 2 - cocoImage.getWidth() / 2,
                Constants.APP_HEIGHT / 2 - cocoImage.getHeight() / 2);

        grassImage = new Texture(Gdx.files.internal("grass.png"));
        grassImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion grassRegion = new TextureRegion(grassImage, 0, 0, grassImage.getWidth(),
                grassImage.getHeight());
        grass = new Sprite(grassRegion);
        grass.setOrigin(0, 0);
        grass.setPosition(0, 0);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.246f, 0.574f, 0.102f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // don't let Coco go off-screen
        if (coco.getX() < 0) {
            coco.setX(0);
        }
        if (coco.getX() + coco.getWidth() > Constants.APP_WIDTH) {
            coco.setX(Constants.APP_WIDTH - coco.getWidth());
        }
        if (coco.getY() < 0) {
            coco.setY(0);
        }
        if (coco.getY() + coco.getHeight() > Constants.APP_HEIGHT) {
            coco.setY(Constants.APP_HEIGHT - coco.getHeight());
        }

        batch.begin();

        // draw grass
        grass.draw(batch);

        // draw coco
        final float degreesPerSecond = 10.0f;
        rot = (rot + Gdx.graphics.getDeltaTime() * degreesPerSecond) % 360;
        final float shakeAmplitudeInDegrees = 5.0f;
        float shake = MathUtils.sin(rot) * shakeAmplitudeInDegrees;
        coco.setRotation(shake);
        coco.draw(batch);

        batch.end();

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
                coco.setX(coco.getX() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                coco.setY(coco.getY() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
            } else if (pressingDown) {
                coco.setX(coco.getX() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                coco.setY(coco.getY() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());

            } else {
                coco.setX(coco.getX() - Constants.SPEED * Gdx.graphics.getDeltaTime());
            }
        }
        if (pressingRight) {
            if (!facingRight) {
                facingRight = true;
            }
            if (pressingUp) {
                coco.setX(coco.getX() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                coco.setY(coco.getY() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
            } else if (pressingDown) {
                coco.setX(coco.getX() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                coco.setY(coco.getY() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());

            } else {
                coco.setX(coco.getX() + Constants.SPEED * Gdx.graphics.getDeltaTime());
            }
        }
        if (pressingUp & !pressingLeft & !pressingRight) {
            coco.setY(coco.getY() + Constants.SPEED * Gdx.graphics.getDeltaTime());
        }
        if (pressingDown & !pressingLeft & !pressingRight) {
            coco.setY(coco.getY() - Constants.SPEED * Gdx.graphics.getDeltaTime());
        }

        // Mouse/touch-screen movement
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            seekingTarget = true;
            camera.unproject(targetXYZ.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (targetXYZ.x < coco.getWidth() / 2) {
                targetXYZ.x = coco.getWidth() / 2;
            }
            if (targetXYZ.x > Constants.APP_WIDTH - coco.getWidth() / 2) {
                targetXYZ.x = Constants.APP_WIDTH - coco.getWidth() / 2;
            }
            if (targetXYZ.y < coco.getHeight() / 2) {
                targetXYZ.y = coco.getHeight() / 2;
            }
            if (targetXYZ.y > Constants.APP_HEIGHT - coco.getHeight() / 2) {
                targetXYZ.y = Constants.APP_HEIGHT - coco.getHeight() / 2;
            }

            pathX = targetXYZ.x - coco.getWidth() / 2 - coco.getX();
            pathY = targetXYZ.y - coco.getHeight() / 2 - coco.getY();

            distance = (float) Math.sqrt(pathX * pathX + pathY * pathY);
            changeX = pathX / distance;
            changeY = pathY / distance;
        }
        if (seekingTarget) {
            if (Math.abs(coco.getX() - targetXYZ.x + coco.getWidth() / 2) < 5
                    & Math.abs(coco.getY() - targetXYZ.y + coco.getHeight() / 2) < 5) {
                seekingTarget = false;
            } else {

                coco.setX(coco.getX() + changeX * Constants.SPEED * Gdx.graphics.getDeltaTime());
                coco.setY(coco.getY() + changeY * Constants.SPEED * Gdx.graphics.getDeltaTime());
            }

        }

        // set flip
        coco.setFlip(facingRight, false);
    }

    @Override
    public void dispose() {
        batch.dispose();
        cocoImage.dispose();
        grassImage.dispose();
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
    }
}
