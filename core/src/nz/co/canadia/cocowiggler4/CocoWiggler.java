package nz.co.canadia.cocowiggler4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class CocoWiggler extends Game {

    SpriteBatch batch;

    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new SplashScreen(this));
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        batch.dispose();
    }

}
