package nz.co.canadia.cocowiggler4.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import nz.co.canadia.cocowiggler4.CocoWiggler;
import nz.co.canadia.cocowiggler4.util.Constants;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Constants.APP_WIDTH, Constants.APP_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new CocoWiggler();
        }
}