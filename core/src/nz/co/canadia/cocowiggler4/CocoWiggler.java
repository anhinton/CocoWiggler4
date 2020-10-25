package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;


public class CocoWiggler extends Game {

    SpriteBatch batch;
    private Music music;
    private float musicVolume;
    private float soundVolume;

    public void create() {
        batch = new SpriteBatch();

        musicVolume = 0;
        soundVolume = 0;

        // play music
        music = Gdx.audio.newMusic(Gdx.files.internal("music/soundtrack.mp3"));
        setMusicVolume(0.8f);
        music.setLooping(true);
        music.play();

        // show the intro screen
        this.setScreen(new SplashScreen(this));
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        music.dispose();
        batch.dispose();
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setMusicVolume(float musicVolume) {
        musicVolume = MathUtils.clamp(musicVolume, 0, 1);
        if (musicVolume < MathUtils.FLOAT_ROUNDING_ERROR) {
            musicVolume = 0;
        }
        if (musicVolume > (1 - MathUtils.FLOAT_ROUNDING_ERROR)) {
            musicVolume = 1;
        }
        this.musicVolume = musicVolume;
        music.setVolume(musicVolume);
    }

    public void setSoundVolume(float soundVolume) {
        soundVolume = MathUtils.clamp(soundVolume, 0, 1);
        if (soundVolume < MathUtils.FLOAT_ROUNDING_ERROR) {
            soundVolume = 0;
        }
        if (soundVolume > (1 - MathUtils.FLOAT_ROUNDING_ERROR)) {
            soundVolume = 1;
        }
        this.soundVolume = soundVolume;
    }
}
