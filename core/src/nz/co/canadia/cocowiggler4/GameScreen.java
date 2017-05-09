package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import nz.co.canadia.cocowiggler4.util.Constants;

class GameScreen implements Screen {
    private final CocoWiggler game;

    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Coco coco;
    private Background background;
    private Array<Texture> pooBitmaps;
    private Array<Poo> poos;
    private boolean debug;

    GameScreen(final CocoWiggler gam) {
        this.game = gam;

        pooBitmaps = new Array<Texture>();
        pooBitmaps.add(new Texture("graphics/poo01.png"));
        pooBitmaps.add(new Texture("graphics/poo02.png"));
        pooBitmaps.add(new Texture("graphics/poo03.png"));
        pooBitmaps.add(new Texture("graphics/poo04.png"));
        pooBitmaps.add(new Texture("graphics/poo05.png"));
        pooBitmaps.add(new Texture("graphics/poo06.png"));
        pooBitmaps.add(new Texture("graphics/poo07.png"));
        pooBitmaps.add(new Texture("graphics/poo08.png"));
        pooBitmaps.add(new Texture("graphics/poo09.png"));
        for (Texture bitmap: pooBitmaps) {
            bitmap.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        viewport = new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT, camera);

        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont(Gdx.files.internal("fonts/Arial32.fnt"));

        poos = new Array<Poo>();

        coco = new Coco();

        background = new Background();

        // DEBUG GRAPHICS
        debug = Constants.GRAPHICS_DEBUG;
    }

    @Override
    public void render(float delta) {
        // clear screen
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // go back to splash screen on back key/Escape
        Gdx.input.setCatchBackKey(true);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new SplashScreen(game));
            dispose();
        }

        // update camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // update Coco
        coco.update(camera, pooBitmaps, poos);

        // flush eaten poos
        for (int i = 0; i < poos.size; i++) {
            if (poos.get(i).isEaten()) {
                poos.removeIndex(i);
            }
        }

        // start sprite batch
        game.batch.begin();

        // draw grass
        background.draw(game.batch);

        // draw poos
        for (Poo poo: poos) {
            poo.draw(game.batch);
        }

        // draw coco
        coco.draw(game.batch);

        // do text
        font.setColor(Constants.FONT_COLOR);
        font.draw(game.batch, "Produced: " + coco.getPooCount(), 10, Constants.APP_HEIGHT - 10, 0, Align.left,
                false);
        font.draw(game.batch, "Consumed: " + coco.getEatenCount(), Constants.APP_WIDTH - 10, Constants.APP_HEIGHT - 10,
                0, Align.right, false);

        // end sprite batch
        game.batch.end();

        // GRAPHICS DEBUGGING
        if (debug) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            coco.drawBounds(shapeRenderer);
            for (Poo poo : poos) {
                poo.drawBounds(shapeRenderer);
            }
            shapeRenderer.end();
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        coco.dispose();
        font.dispose();
        for (Texture bitmap: pooBitmaps) {
            bitmap.dispose();
        }
        for (Poo poo: poos) {
            poo.dispose();
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
