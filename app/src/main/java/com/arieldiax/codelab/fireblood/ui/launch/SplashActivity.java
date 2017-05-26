package com.arieldiax.codelab.fireblood.ui.launch;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.ui.navigation.search.SearchActivity;
import com.arieldiax.codelab.fireblood.ui.verification.VerifyEmailActivity;
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
                Class activityClass = (isUserSignedIn)
                        ?
                        (firebaseUser.isEmailVerified())
                                ? SearchActivity.class
                                : VerifyEmailActivity.class
                        : WelcomeActivity.class;
                Pair<View, String> activityPair1 = (isUserSignedIn)
                        ? null
                        : Pair.create((View) mAppLogoImageView, mAppLogoImageView.getTransitionName());
                ViewUtils.startCustomActivity(SplashActivity.this, activityClass, activityPair1, null, true);
            }
        }, DateUtils.SECOND_IN_MILLIS);
    }
}
