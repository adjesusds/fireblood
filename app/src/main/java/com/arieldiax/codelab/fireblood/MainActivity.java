package com.arieldiax.codelab.fireblood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /**
     * @var mSignUpButton Button field for sign up.
     */
    private Button mSignUpButton;

    /**
     * @var mSignInButton Button field for sign in.
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
                Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
        mSignInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(signInIntent);
            }
        });
    }
}
