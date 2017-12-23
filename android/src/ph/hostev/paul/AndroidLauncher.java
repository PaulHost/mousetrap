package ph.hostev.paul;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

    private final AndroidLauncher context = this;
    private final AdManager adMob;
    private final GameScreen game;

    public AndroidLauncher() {
        adMob = new AdManager("ca-app-pub-9662067415857988~9718214790");
        game = new GameScreen(adMob);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adMob.init(context);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useWakelock = true;

        View gameView = initializeForView(game, config);
//		initialize(new GameScreen(), config);
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView);
        layout.addView(adMob.adView, adMob.adParams);
        setContentView(layout);
    }
}
