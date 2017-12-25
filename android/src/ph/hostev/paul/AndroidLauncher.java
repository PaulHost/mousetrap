package ph.hostev.paul;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.MobileAds;

import ph.hostev.paul.admob.AdManager;

public class AndroidLauncher extends AndroidApplication {

    final AndroidLauncher context = this;
    static AdMobManager adMob = null;
    final GameScreen game;

    public AndroidLauncher() {
        adMob = new AdMobManager("ca-app-pub-9662067415857988/1441883921");
        game = new GameScreen(adMob);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        adMob.init(context);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useWakelock = true;

        View gameView = initializeForView(game, config);
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView);
        layout.addView(adMob.adView, adMob.adParams);
        setContentView(layout);
    }
}
