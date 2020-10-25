package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import nz.co.canadia.cocowiggler4.util.Constants;

/**
 * Show the title before we get started
 */

class SplashScreen implements InputProcessor, Screen {

    private final CocoWiggler game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Texture bitmap;
    private final Sprite sprite;

    SplashScreen(final CocoWiggler game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        viewport = new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT, camera);

        bitmap = new Texture("graphics/title.png");
        bitmap.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion grassRegion = new TextureRegion(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        sprite = new Sprite(grassRegion);
        sprite.setOrigin(0, 0);
        sprite.setPosition(0, 0);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        sprite.draw(game.batch);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void dispose() {
        bitmap.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        // ESC or BACK quits the game
        if (keycode == Input.Keys.BACK
                | keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // any other input means start the game
        game.setScreen(new GameScreen(game));
        dispose();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
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
