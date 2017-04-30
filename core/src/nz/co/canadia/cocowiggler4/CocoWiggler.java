package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import nz.co.canadia.cocowiggler4.util.Constants;

public class CocoWiggler extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Coco coco;
    private Background background;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        viewport = new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT, camera);

        batch = new SpriteBatch();

        coco = new Coco();

        background = new Background();
    }

    @Override
    public void render() {
        // clear screen
        Gdx.gl.glClearColor(0.246f, 0.574f, 0.102f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // update Coco
        coco.update(camera);

        // start sprite batch
        batch.begin();

        // draw grass
        background.draw(batch);

        // draw coco
        coco.draw(batch);

        // end sprite batch
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
        coco.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
