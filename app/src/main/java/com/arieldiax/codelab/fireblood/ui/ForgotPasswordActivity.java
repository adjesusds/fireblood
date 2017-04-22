package com.arieldiax.codelab.fireblood.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    /**
     * Text view field for sign in.
     */
    private TextView mSignInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initUi();
        initListeners();
    }

    /**
     * Initializes the user interface view bindings.
     */
    private void initUi() {
        mSignInTextView = (TextView) findViewById(R.id.sign_in_text_view);
    }

    /**
     * Initializes the event listener view bindings.
     */
    private void initListeners() {
        mSignInTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finishAfterTransition();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
