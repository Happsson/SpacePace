package nu.geeks.spacepace.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nu.geeks.spacepace.SpacePace;

public class  DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1050;
        config.height = 540;
		new LwjglApplication(new SpacePace(), config);
	}
}
