package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import nz.co.canadia.cocowiggler4.util.Constants;

public class CocoWiggler extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Coco coco;
    private Background background;
    private AssetManager manager;
    private Array<Poo> poos;

    @Override
    public void create() {
        manager = new AssetManager();
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        manager.load("coco.png", Texture.class, param);
        manager.load("grass.png", Texture.class, param);
        manager.load("poo01.png", Texture.class, param);
        manager.load("poo02.png", Texture.class, param);
        manager.load("poo03.png", Texture.class, param);
        manager.load("poo04.png", Texture.class, param);
        manager.load("poo05.png", Texture.class, param);
        manager.load("poo06.png", Texture.class, param);
        manager.load("poo07.png", Texture.class, param);
        manager.load("poo08.png", Texture.class, param);
        manager.load("poo09.png", Texture.class, param);
        manager.finishLoading();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        viewport = new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT, camera);

        batch = new SpriteBatch();

        poos = new Array<Poo>();

        coco = new Coco(manager);

        background = new Background(manager);
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
        coco.update(camera, manager, poos);

        // start sprite batch
        batch.begin();

        // draw grass
        background.draw(batch);

        // draw poos
        for (Poo poo: poos) {
            poo.draw(batch);
        }

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
        manager.dispose();
        for (Poo poo: poos) {
            poo.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
