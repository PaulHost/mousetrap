package ph.hostev.paul;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import ph.hostev.paul.admob.AdManager;

public class AdMobManager implements AdManager {

    private static final int ADSHOW = 1, ADHIDE = 0;

    private final String TEST_DEVISE = "", UNIT_ID;

    AdView adView;
    RelativeLayout.LayoutParams adParams;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADSHOW:
                    adView.setVisibility(View.VISIBLE);
                    break;
                case ADHIDE:
                    adView.setVisibility(View.GONE);
            }
        }
    };

    public AdMobManager(String id) {
        this.UNIT_ID = id;
    }

    public void init(Context context) {

        MobileAds.initialize(context, "ca-app-pub-9662067415857988~9718214790");

        adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        adView = new AdView(context);
        adView.setAdUnitId(UNIT_ID);
        adView.setAdSize(AdSize.BANNER);
        adView.setBackgroundColor(Color.TRANSPARENT);

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
