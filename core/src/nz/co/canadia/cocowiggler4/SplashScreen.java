package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
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
    private final Texture grassTexture;
    private final Texture titleTexture;
    private final Sprite grassSprite;
    private final Stage stage;
    private final Texture upTexture;
    private final Texture downTexture;
    private final BitmapFont font;
    private final Label.LabelStyle labelStyle;
    private final ImageTextButton.ImageTextButtonStyle buttonStyle;
    private final ImageTextButton backButton;
    private final Preferences settings;
    private final ScrollPane.ScrollPaneStyle scrollPaneStyle;
    private final BitmapFont creditsFont;
    private float musicVolume;
    private float soundVolume;
    private Label musicLevelLabel;
    private Label soundLevelLabel;
    private enum Menu {
        TITLE, SETTINGS, CREDITS
    }
    private Menu currentMenu;

    SplashScreen(final CocoWiggler game) {
        this.game = game;

        settings = Gdx.app.getPreferences("nz.co.canadia.cocowiggler4.settings");
        game.setSoundVolume(settings.getFloat("soundVolume", Constants.SOUND_VOLUME_DEFAULT));

        game.setMusicVolume(settings.getFloat("musicVolume", Constants.MUSIC_VOLUME_DEFAULT));
        if(Gdx.app.getType() != Application.ApplicationType.WebGL) {
            game.playMusicLooping();
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
        viewport = new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT, camera);

        grassTexture = new Texture("graphics/grass.png");
        grassTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion grassRegion = new TextureRegion(grassTexture, 0, 0, grassTexture.getWidth(),
                grassTexture.getHeight());
        grassSprite = new Sprite(grassRegion);
        grassSprite.setOrigin(0, 0);
        grassSprite.setPosition(0, 0);

        titleTexture = new Texture("graphics/title.png");
        titleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        upTexture = new Texture("graphics/button_up.png");
        downTexture = new Texture("graphics/button_down.png");

        font = new BitmapFont(Gdx.files.internal("fonts/Arial32.fnt"));
        creditsFont = new BitmapFont(Gdx.files.internal("fonts/Arial20.fnt"));

        labelStyle = new Label.LabelStyle(font, Constants.FONT_COLOR);

        NinePatchDrawable upPatchDrawable = new NinePatchDrawable(
                new NinePatch(upTexture, 3, 3, 3, 3));
        NinePatchDrawable downPatchDrawable = new NinePatchDrawable(
                new NinePatch(downTexture, 3, 3, 3, 3));

        buttonStyle = new ImageTextButton.ImageTextButtonStyle(
                upPatchDrawable, downPatchDrawable, upPatchDrawable, font);
        buttonStyle.fontColor = Constants.FONT_COLOR;

        scrollPaneStyle = new ScrollPane.ScrollPaneStyle(
                null, downPatchDrawable, upPatchDrawable,
                downPatchDrawable, upPatchDrawable);

        backButton = new ImageTextButton("Back", buttonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.flush();
                goBack();
            }
        });

        musicVolume = game.getMusicVolume();
        soundVolume = game.getSoundVolume();

        stage = new Stage(viewport);
        setCurrentMenu(Menu.TITLE);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void setCurrentMenu(Menu menu) {
        currentMenu = menu;
        switch (menu) {
            case TITLE:
                showTitleMenu();
                break;
            case SETTINGS:
                showSettingsMenu();
                break;
            case CREDITS:
                showCreditsMenu();
                break;
        }
    }

    private void goBack() {
        switch(currentMenu) {
            case TITLE:
                Gdx.app.exit();
                break;
            case SETTINGS:
                setCurrentMenu(Menu.TITLE);
                break;
            case CREDITS:
                setCurrentMenu(Menu.SETTINGS);
                break;
        }
    }

    private void play() {
        game.setScreen(new GameScreen(game));
        dispose();
    }

    private String printVolume(float volume) {
        return Integer.toString(MathUtils.round(volume * 10));
    }

    private void decreaseMusicVolume() {
        game.setMusicVolume(musicVolume - Constants.VOLUME_INCREMENT);
        musicVolume = game.getMusicVolume();
        musicLevelLabel.setText(printVolume(musicVolume));
        settings.putFloat("musicVolume", musicVolume);
    }

    private void increaseMusicVolume() {
        game.setMusicVolume(musicVolume + Constants.VOLUME_INCREMENT);
        musicVolume = game.getMusicVolume();
        musicLevelLabel.setText(printVolume(musicVolume));
        settings.putFloat("musicVolume", musicVolume);
    }

    private void decreaseSoundVolume() {
        game.setSoundVolume(soundVolume - Constants.VOLUME_INCREMENT);
        soundVolume = game.getSoundVolume();
        soundLevelLabel.setText(printVolume(soundVolume));
        settings.putFloat("soundVolume", soundVolume);
    }

    private void increaseSoundVolume() {
        game.setSoundVolume(soundVolume + Constants.VOLUME_INCREMENT);
        soundVolume = game.getSoundVolume();
        soundLevelLabel.setText(printVolume(soundVolume));
        settings.putFloat("soundVolume", soundVolume);
    }

    private void showTitleMenu() {
        // Title image
        Image titleImage = new Image(titleTexture);

        NinePatchDrawable patchDrawableUp = new NinePatchDrawable(
                new NinePatch(upTexture, 3, 3, 3, 3));
        NinePatchDrawable patchDrawableDown = new NinePatchDrawable(
                new NinePatch(downTexture, 3, 3, 3, 3));
        ImageTextButton.ImageTextButtonStyle style =
                new ImageTextButton.ImageTextButtonStyle(
                        patchDrawableUp, patchDrawableDown, patchDrawableUp, font);
        style.fontColor = Constants.FONT_COLOR;

        // Play button
        ImageTextButton playButton = new ImageTextButton(
                "Play", style);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                play();
            }
        });
        // Settings button
        ImageTextButton settingsButton = new ImageTextButton(
                "Settings", style);
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playMusicLooping();
                setCurrentMenu(Menu.SETTINGS);
            }
        });
        // Quit button
        ImageTextButton quitButton = new ImageTextButton(
                "Quit", style);
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goBack();
            }
        });

        // Button table
        Table buttonTable = new Table();
        buttonTable.pad(Constants.UI_PADDING);
        buttonTable.add(playButton).space(Constants.UI_PADDING)
                .prefSize(Constants.MENU_BUTTON_WIDTH, Constants.MENU_BUTTON_HEIGHT);
        buttonTable.add(settingsButton).space(Constants.UI_PADDING)
                .prefSize(Constants.MENU_BUTTON_WIDTH, Constants.MENU_BUTTON_HEIGHT);
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            buttonTable.add(quitButton).space(Constants.UI_PADDING)
                    .prefSize(Constants.MENU_BUTTON_WIDTH, Constants.MENU_BUTTON_HEIGHT);
        }

        Table table = new Table();
        table.setFillParent(true);
        table.pad(Constants.UI_PADDING);
        table.center();
        table.add(titleImage).space(Constants.UI_PADDING);
        table.row();
        table.add(buttonTable);

        stage.clear();
        stage.addActor(table);

        grassSprite.setAlpha(1);
    }

    private void showSettingsMenu() {
        Label settingsLabel = new Label("Settings", labelStyle);
        Label musicVolumeLabel = new Label("Music Volume:", labelStyle);
        Label soundVolumeLabel = new Label("Sound Volume:", labelStyle);
        musicLevelLabel = new Label(printVolume(musicVolume), labelStyle);
        musicLevelLabel.setAlignment(Align.right);
        soundLevelLabel = new Label(printVolume(soundVolume), labelStyle);
        soundLevelLabel.setAlignment(Align.right);

        ImageTextButton musicDownButton = new ImageTextButton("-", buttonStyle);
        musicDownButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                decreaseMusicVolume();
            }
        });
        ImageTextButton musicUpButton = new ImageTextButton("+", buttonStyle);
        musicUpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                increaseMusicVolume();
            }
        });
        ImageTextButton soundDownButton = new ImageTextButton("-", buttonStyle);
        soundDownButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                decreaseSoundVolume();
            }
        });
        ImageTextButton soundUpButton = new ImageTextButton("+", buttonStyle);
        soundUpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                increaseSoundVolume();
            }
        });

        ImageTextButton creditsButton = new ImageTextButton("Credits", buttonStyle);
        creditsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setCurrentMenu(Menu.CREDITS);
            }
        });

        Table settingsTable = new Table();
        settingsTable.setFillParent(true);
        settingsTable.pad(Constants.UI_PADDING);
        settingsTable.center();
        
        settingsTable.add(settingsLabel).left().space(Constants.UI_PADDING);
        settingsTable.row();
        settingsTable.add(musicVolumeLabel).left().space(Constants.UI_PADDING);
        settingsTable.add(musicDownButton)
                .space(Constants.UI_PADDING)
                .prefSize(Constants.MENU_BUTTON_HEIGHT);
        settingsTable.add(musicUpButton)
                .space(Constants.UI_PADDING)
                .prefSize(Constants.MENU_BUTTON_HEIGHT);
        settingsTable.add(musicLevelLabel).right()
                .space(Constants.UI_PADDING)
                .prefSize(Constants.MENU_BUTTON_HEIGHT / 2f);
        settingsTable.row();
        settingsTable.add(soundVolumeLabel).left().space(Constants.UI_PADDING);
        settingsTable.add(soundDownButton)
                .space(Constants.UI_PADDING)
                .prefSize(Constants.MENU_BUTTON_HEIGHT);
        settingsTable.add(soundUpButton)
                .space(Constants.UI_PADDING)
                .prefSize(Constants.MENU_BUTTON_HEIGHT);
        settingsTable.add(soundLevelLabel).right()
                .space(Constants.UI_PADDING)
                .prefSize(Constants.MENU_BUTTON_HEIGHT / 2f);
        settingsTable.row();
        settingsTable.add(creditsButton).center()
                .space(Constants.UI_PADDING)
                .prefSize(soundVolumeLabel.getPrefWidth(), Constants.MENU_BUTTON_HEIGHT);
        settingsTable.add(backButton).center()
                .colspan(3)
                .space(Constants.UI_PADDING)
                .prefSize(soundVolumeLabel.getPrefWidth(), Constants.MENU_BUTTON_HEIGHT);

        stage.clear();
        stage.addActor(settingsTable);

        grassSprite.setAlpha(0);
    }

    private void showCreditsMenu() {
        Label creditsHeaderLabel = new Label("Credits", labelStyle);

        FileHandle file = Gdx.files.internal("credits.txt");
        String creditsText = file.readString();
        Label.LabelStyle creditLabelStyle = new Label.LabelStyle(creditsFont, Constants.FONT_COLOR);
        Label creditsTextLabel = new Label(creditsText, creditLabelStyle);
        creditsTextLabel.setWrap(true);
        ScrollPane creditsPane = new ScrollPane(creditsTextLabel, scrollPaneStyle);
        creditsPane.setFadeScrollBars(false);

        Table table = new Table();
        table.setFillParent(true);
        table.pad(Constants.UI_PADDING);
        table.add(creditsHeaderLabel).space(Constants.UI_PADDING).center();
        table.row();
        table.add(creditsPane)
                .prefWidth(Constants.APP_WIDTH);
        table.row();
        table.add(backButton).space(Constants.UI_PADDING)
                .prefSize(Constants.MENU_BUTTON_WIDTH, Constants.MENU_BUTTON_HEIGHT);

        stage.clear();
        stage.addActor(table);
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

        stage.act(delta);

        game.batch.begin();
        grassSprite.draw(game.batch);
        game.batch.end();

        stage.draw();
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
        stage.dispose();
        grassTexture.dispose();
        titleTexture.dispose();
        upTexture.dispose();
        downTexture.dispose();
        font.dispose();
        creditsFont.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        // ESC or BACK quits the game
        if (keycode == Input.Keys.BACK
                | keycode == Input.Keys.ESCAPE) {
            goBack();
        }
        return true;
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
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
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
