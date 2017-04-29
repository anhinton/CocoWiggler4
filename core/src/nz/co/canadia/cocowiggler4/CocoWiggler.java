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
    private boolean isPressingLeft;
    private boolean isPressingRight;
    private boolean isPressingUp;
    private boolean isPressingDown;

    @Override
    public void create() {
        isPressingLeft = false;
        isPressingRight = false;
        isPressingUp = false;
        isPressingDown = false;

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

        isPressingLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        isPressingRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        isPressingUp = Gdx.input.isKeyPressed(Input.Keys.UP);
        isPressingDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (isPressingLeft) {
            if (isPressingUp) {
                coco.setX(coco.getX() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                coco.setY(coco.getY() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
            } else if (isPressingDown) {
                coco.setX(coco.getX() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                coco.setY(coco.getY() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());

            } else {
                coco.setX(coco.getX() - Constants.SPEED * Gdx.graphics.getDeltaTime());
            }
        }
        if (isPressingRight) {
            if (isPressingUp) {
                coco.setX(coco.getX() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                coco.setY(coco.getY() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
            } else if (isPressingDown) {
                coco.setX(coco.getX() + Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());
                coco.setY(coco.getY() - Constants.ANGLE_SPEED * Gdx.graphics.getDeltaTime());

            } else {
                coco.setX(coco.getX() + Constants.SPEED * Gdx.graphics.getDeltaTime());
            }
        }
        if (isPressingUp & !isPressingLeft & !isPressingRight) {
            coco.setY(coco.getY() + Constants.SPEED * Gdx.graphics.getDeltaTime());
        }
        if (isPressingDown & !isPressingLeft & !isPressingRight) {
            coco.setY(coco.getY() - Constants.SPEED * Gdx.graphics.getDeltaTime());
        }
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
