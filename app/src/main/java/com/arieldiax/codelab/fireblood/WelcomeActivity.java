package com.arieldiax.codelab.fireblood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    /**
     * Button field for sign up.
     */
    private Button mSignUpButton;

    /**
     * Button field for sign in.
     */
    private Button mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initUi();
        initListeners();
    }

    /**
     * Initializes the user interface view bindings.
     */
    private void initUi() {
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
    }

    /**
     * Initializes the event listener view bindings.
     */
    private void initListeners() {
        mSignUpButton.setOnClickListener(ViewUtils.getStartCustomActivityOnClickListener(this, SignUpActivity.class, false));
        mSignInButton.setOnClickListener(ViewUtils.getStartCustomActivityOnClickListener(this, SignInActivity.class, false));
    }
}
