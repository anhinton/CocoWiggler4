package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import nz.co.canadia.cocowiggler4.util.Constants;

public class CocoWiggler extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture grassImage;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Coco coco;
    private Sprite grass;

    @Override
    public void create() {

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        viewport = new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT, camera);

        batch = new SpriteBatch();

        coco = new Coco();

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

        coco.update(camera);

        batch.begin();

        // draw grass
        grass.draw(batch);

        // draw coco
        coco.draw(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        coco.dispose();
        grassImage.dispose();
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
    }
}
