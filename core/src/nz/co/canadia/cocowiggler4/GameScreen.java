package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import nz.co.canadia.cocowiggler4.util.Constants;

class GameScreen implements InputProcessor, Screen {
    private final CocoWiggler game;

    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Coco coco;
    private final Background background;
    private final Array<Texture> pooBitmaps = new Array<>();
    private final Array<Poo> poos;
    private final boolean debug;
    private final Stage stage;
    private final Texture upTexture;
    private final Texture downTexture;
    private final Label producedLabel;
    private final Label consumedLabel;

    GameScreen(final CocoWiggler game) {
        this.game = game;

        game.playMusicLooping();

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

        poos = new Array<>();

        coco = new Coco(game.getSoundVolume());

        background = new Background();

        Label.LabelStyle scoreLabelStyle = new Label.LabelStyle(font, Constants.FONT_COLOR);
        producedLabel = new Label("", scoreLabelStyle);
        consumedLabel = new Label("", scoreLabelStyle);

        upTexture = new Texture("graphics/button_up.png");
        NinePatchDrawable upPatchDrawable = new NinePatchDrawable(
                new NinePatch(upTexture, 3, 3, 3, 3));
        downTexture = new Texture("graphics/button_down.png");
        NinePatchDrawable downPatchDrawable = new NinePatchDrawable(
                new NinePatch(downTexture, 3, 3, 3, 3));
        ImageTextButton.ImageTextButtonStyle style =
                new ImageTextButton.ImageTextButtonStyle(
                        upPatchDrawable, downPatchDrawable, upPatchDrawable, font);
        style.fontColor = Constants.FONT_COLOR;
        ImageTextButton menuButton = new ImageTextButton(
                "Menu", style);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goBack();
            }
        });

        stage = new Stage(viewport);

        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.pad(Constants.UI_PADDING);
        table.add(producedLabel).left().expandX().prefWidth(Constants.APP_WIDTH / 4f);
        table.add(menuButton).center().prefSize(Constants.MENU_BUTTON_WIDTH, Constants.MENU_BUTTON_HEIGHT);
        table.add(consumedLabel).right().expandX().prefWidth(Constants.APP_WIDTH / 4f);

        stage.addActor(table);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);

        // DEBUG GRAPHICS
        debug = Constants.GRAPHICS_DEBUG;
    }

    private void updateScores() {
        producedLabel.setText("Produced: " + coco.getPooCount());
        consumedLabel.setText("Consumed: " + coco.getEatenCount());
    }

    private void goBack() {
        game.setScreen(new SplashScreen(game));
        dispose();
    }

    @Override
    public void render(float delta) {
        // clear screen
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // update Coco
        coco.update(pooBitmaps, poos);

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

        // end sprite batch
        game.batch.end();

        // update scores
        updateScores();

        // draw UI
        stage.draw();

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
        upTexture.dispose();
        downTexture.dispose();
        stage.dispose();
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

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            // go back to splash screen on back key/Escape
            case Input.Keys.BACK:
            case Input.Keys.ESCAPE:
                goBack();
                break;
            // movement buttons
            case Input.Keys.LEFT:
                coco.setMovingLeft(true);
                break;
            case Input.Keys.RIGHT:
                coco.setMovingRight(true);
                break;
            case Input.Keys.UP:
                coco.setMovingUp(true);
                break;
            case Input.Keys.DOWN:
                coco.setMovingDown(true);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            // movement buttons
            case Input.Keys.LEFT:
                coco.setMovingLeft(false);
                break;
            case Input.Keys.RIGHT:
                coco.setMovingRight(false);
                break;
            case Input.Keys.UP:
                coco.setMovingUp(false);
                break;
            case Input.Keys.DOWN:
                coco.setMovingDown(false);
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Mouse/touch-screen movement
        Vector3 target = camera.unproject(
                new Vector3(screenX, screenY, 0),
                viewport.getScreenX(),
                viewport.getScreenY(),
                viewport.getScreenWidth(),
                viewport.getScreenHeight());
        coco.setTarget(target);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Mouse/touch-screen movement
        Vector3 target = camera.unproject(
                new Vector3(screenX, screenY, 0),
                viewport.getScreenX(),
                viewport.getScreenY(),
                viewport.getScreenWidth(),
                viewport.getScreenHeight());
        coco.setTarget(target);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
