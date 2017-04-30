package nz.co.canadia.cocowiggler4.util;

/**
 * Some things should be constant
 */

public class Constants {
    public static final String GAME_NAME = "Coco Wiggler 4";

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;

    public static final float SPEED = 200;
    // ANGLE_SPEED is the change in x/y when moving both vertically and horizontally
    public static final float ANGLE_SPEED = (float) Math.sqrt(SPEED * SPEED / 2);
}
