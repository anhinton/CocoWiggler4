package nz.co.canadia.cocowiggler4.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Some things should be constant
 */

public class Constants {
    public static final String GAME_NAME = "Coco Wiggler 4";

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;

    public static final int UI_PADDING = 10;

    public static final int MENU_BUTTON_WIDTH = 150;
    public static final int MENU_BUTTON_HEIGHT = 75;

    public static final float MUSIC_VOLUME_DEFAULT = 0.8f;
    public static final float SOUND_VOLUME_DEFAULT = 1;
    public static final float VOLUME_INCREMENT = 0.1f;

    public static final Color BACKGROUND_COLOR = new Color(63 / 255f, 146 / 255f, 26 / 255f, 1);
    public static final Color FONT_COLOR = new Color(0, 0, 0, 1);

    public static final float SPEED = 200;
    // ANGLE_SPEED is the change in x/y when moving both vertically and horizontally
    public static final float ANGLE_SPEED = (float) Math.sqrt(SPEED * SPEED / 2);

    public static final long POO_TIME_MIN = TimeUtils.millisToNanos(1000);
    public static final long POO_TIME_MAX = TimeUtils.millisToNanos(5000);

    public static final float FLIP_THRESHOLD = 4f / 7;

    public static final boolean GRAPHICS_DEBUG = false;
}
