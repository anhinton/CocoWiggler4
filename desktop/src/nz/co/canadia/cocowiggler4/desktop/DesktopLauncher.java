package nz.co.canadia.cocowiggler4.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nz.co.canadia.cocowiggler4.CocoWiggler;
import nz.co.canadia.cocowiggler4.util.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Constants.GAME_NAME;
		config.width = Constants.APP_WIDTH;
		config.height = Constants.APP_HEIGHT;
		new LwjglApplication(new CocoWiggler(), config);
	}
}
