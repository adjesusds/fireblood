package com.arieldiax.codelab.fireblood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);
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
        mSignUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ViewUtils.startCustomActivity(MainActivity.this, SignUpActivity.class, false);
            }
        });
        mSignInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ViewUtils.startCustomActivity(MainActivity.this, SignInActivity.class, false);
            }
        });
    }
}
