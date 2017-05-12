package com.arieldiax.codelab.fireblood.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.utils.ViewUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    /**
     * Views of the activity.
     */
    ImageView mAppLogoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUi();
        updateUi();
    }

    /**
     * Initializes the user interface view bindings.
     */
    void initUi() {
        mAppLogoImageView = (ImageView) findViewById(R.id.app_logo_image_view);
    }

    /**
     * Updates the user interface view bindings.
     */
    void updateUi() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                boolean isUserSignedIn = firebaseUser != null;
                Pair<View, String> activityPair = (isUserSignedIn)
                        ? null
                        : Pair.create((View) mAppLogoImageView, getString(R.string.transition_app_logo_image_view));
                Class activityClass = (isUserSignedIn)
                        ?
                        (firebaseUser.isEmailVerified())
                                ? MainActivity.class
                                : VerifyEmailActivity.class
                        : WelcomeActivity.class;
                ViewUtils.startCustomActivity(SplashActivity.this, activityClass, activityPair, true);
            }
        }, DateUtils.SECOND_IN_MILLIS);
    }
}
