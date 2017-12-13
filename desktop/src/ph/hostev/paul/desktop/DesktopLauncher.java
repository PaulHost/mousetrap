package ph.hostev.paul.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ph.hostev.paul.MousetrapStarter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "MouseTrap";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new MousetrapStarter(), config);
	}
}
