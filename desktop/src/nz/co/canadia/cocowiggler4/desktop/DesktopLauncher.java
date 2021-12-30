package nz.co.canadia.cocowiggler4.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import nz.co.canadia.cocowiggler4.CocoWiggler;
import nz.co.canadia.cocowiggler4.util.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle(Constants.GAME_NAME);
		config.setWindowedMode(Constants.APP_WIDTH, Constants.APP_HEIGHT);
		config.setWindowIcon(
				"icon_128.png",
				"icon_32.png",
				"icon_16.png"
		);

		new Lwjgl3Application(new CocoWiggler(), config);
	}
}
