package ph.hostev.paul;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import ph.hostev.paul.admob.IAdManager;

public class AdManager implements IAdManager {

    private final int ADSHOW = 1, ADHIDE = 0;

    private final String TEST_DEVISE = "", APP_ID;

    public AdView adView = null;
    public RelativeLayout.LayoutParams adParams = null;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADSHOW:
                    adView.setVisibility(View.VISIBLE);
                    break;
                case ADHIDE:
                    adView.setVisibility(View.INVISIBLE);
            }
        }
    };

    public AdManager(String id) {
        this.APP_ID = id;
    }

    public void init(Context context) {
        adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(APP_ID);

        AdRequest.Builder requestBuilder = new AdRequest.Builder();
        requestBuilder.addTestDevice(TEST_DEVISE);

        adView.loadAd(requestBuilder.build());
    }

    @Override
    public void show() {
        handler.sendEmptyMessage(ADSHOW);
    }

    @Override
    public void hide() {
        handler.sendEmptyMessage(ADHIDE);
    }
}
