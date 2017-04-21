package com.arieldiax.codelab.fireblood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * Initializes the back end logic bindings.
     */
    private void init() {
        ViewUtils.startCustomActivity(this, WelcomeActivity.class, null, true);
    }
}
