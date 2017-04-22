package com.arieldiax.codelab.fireblood;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.arieldiax.codelab.fireblood.ui.WelcomeActivity;
import com.arieldiax.codelab.fireblood.utils.ViewUtils;

public class SplashActivity extends AppCompatActivity {

    /**
     * Image view field for app logo.
     */
    private ImageView mAppLogoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUi();
        init();
    }

    /**
     * Initializes the user interface view bindings.
     */
    private void initUi() {
        mAppLogoImageView = (ImageView) findViewById(R.id.app_logo_image_view);
    }

    /**
     * Initializes the back end logic bindings.
     */
    private void init() {
        Handler handler = new Handler();
        int delay = 1000;
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Pair<View, String> activityPair = Pair.create((View) mAppLogoImageView, getString(R.string.transition_app_logo_image_view));
                ViewUtils.startCustomActivity(SplashActivity.this, WelcomeActivity.class, activityPair, true);
            }
        }, delay);
    }
}
