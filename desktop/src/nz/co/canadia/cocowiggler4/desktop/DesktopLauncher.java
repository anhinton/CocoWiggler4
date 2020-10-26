package nz.co.canadia.cocowiggler4.desktop;

import com.badlogic.gdx.Files;
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
		config.addIcon("icon_128.png", Files.FileType.Local);
		config.addIcon("icon_32.png", Files.FileType.Local);
		config.addIcon("icon_16.png", Files.FileType.Local);
		new LwjglApplication(new CocoWiggler(), config);
	}
}
