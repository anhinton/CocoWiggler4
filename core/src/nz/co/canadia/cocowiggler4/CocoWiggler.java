package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class CocoWiggler extends Game {

    SpriteBatch batch;
    private Music music;

    public void create() {
        batch = new SpriteBatch();

        // play music
        music = Gdx.audio.newMusic(Gdx.files.internal("music/soundtrack.mp3"));
        music.setVolume(0.8f);
        music.setLooping(true);
//        music.play();

        // show the intro screen
//        this.setScreen(new SplashScreen(this));
        this.setScreen(new GameScreen(this));
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        music.dispose();
        batch.dispose();
    }

}
