package com.arieldiax.codelab.fireblood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    /**
     * Text view field for forgot your password.
     */
    private TextView mForgotYourPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initUi();
        initListeners();
    }

    /**
     * Initializes the user interface view bindings.
     */
    private void initUi() {
        mForgotYourPasswordTextView = (TextView) findViewById(R.id.forgot_your_password_text_view);
    }

    /**
     * Initializes the event listener view bindings.
     */
    private void initListeners() {
        mForgotYourPasswordTextView.setOnClickListener(ViewUtils.getStartCustomActivityOnClickListener(this, ForgotPasswordActivity.class, false));
    }
}
