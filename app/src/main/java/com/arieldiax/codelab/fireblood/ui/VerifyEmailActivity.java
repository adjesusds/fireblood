package com.arieldiax.codelab.fireblood.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.widgets.ConfirmBottomSheetDialog;
import com.arieldiax.codelab.fireblood.utils.ConnectionUtils;
import com.arieldiax.codelab.fireblood.utils.ViewUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmailActivity extends AppCompatActivity {

    /**
     * Views of the activity.
     */
    ScrollView mVerifyEmailScrollView;
    TextView mEmailTextView;
    Button mSendEmailButton;

    /**
     * Instance of the Snackbar class.
     */
    Snackbar mSnackbar;

    /**
     * Instance of the ConfirmBottomSheetDialog class.
     */
    ConfirmBottomSheetDialog mConfirmBottomSheetDialog;

    /**
     * Instance of the ProgressDialog class.
     */
    ProgressDialog mProgressDialog;

    /**
     * Instance of the FirebaseAuth class.
     */
    FirebaseAuth mFirebaseAuth;

    /**
     * Instance of the FirebaseUser class.
     */
    FirebaseUser mFirebaseUser;

    /**
     * Handler of the verification.
     */
    Handler mVerificationHandler;

    /**
     * Runnable of the verification.
     */
    Runnable mVerificationRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        initUi();
        init();
        initListeners();
    }

    /**
     * Initializes the user interface view bindings.
     */
    void initUi() {
        mVerifyEmailScrollView = (ScrollView) findViewById(R.id.verify_email_activity);
        mEmailTextView = (TextView) findViewById(R.id.email_text_view);
        mSendEmailButton = (Button) findViewById(R.id.send_email_button);
    }

    /**
     * Initializes the back end logic bindings.
     */
    void init() {
        mSnackbar = Snackbar.make(mVerifyEmailScrollView, "", Snackbar.LENGTH_LONG);
        View.OnClickListener positiveButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mConfirmBottomSheetDialog.dismiss();
                sendVerificationEmail();
            }
        };
        mConfirmBottomSheetDialog = new ConfirmBottomSheetDialog(this)
                .setTitle(R.string.title_send_verification_email)
                .setMessage(R.string.message_are_you_sure)
                .setPositiveButtonListener(positiveButtonListener)
        ;
        mProgressDialog = new ProgressDialog(this, R.style.AppProgressDialogTheme);
        mProgressDialog.setTitle(R.string.title_sending_verification_email);
        mProgressDialog.setMessage(getString(R.string.message_please_wait_a_few_seconds));
        mProgressDialog.setCancelable(false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            mEmailTextView.setText(mFirebaseUser.getEmail());
        }
        mVerificationHandler = new Handler();
        mVerificationRunnable = new Runnable() {

            @Override
            public void run() {
                try {
                    if (!ConnectionUtils.hasInternetConnection(VerifyEmailActivity.this)) {
                        if (!mSnackbar.isShown()) {
                            mSnackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                            mSnackbar.setText(R.string.message_please_check_your_internet_connection).show();
                        }
                    } else {
                        mSnackbar.dismiss();
                        mFirebaseUser
                                .reload()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            return;
                                        }
                                        if (mFirebaseUser.isEmailVerified()) {
                                            ViewUtils.startCustomActivity(VerifyEmailActivity.this, MainActivity.class, null, true);
                                        }
                                    }
                                })
                        ;
                    }
                } finally {
                    mVerificationHandler.postDelayed(mVerificationRunnable, DateUtils.SECOND_IN_MILLIS * 5);
                }
            }
        };
    }

    /**
     * Initializes the event listener view bindings.
     */
    void initListeners() {
        mSendEmailButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mConfirmBottomSheetDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVerificationRunnable.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVerificationHandler.removeCallbacks(mVerificationRunnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_verify_email, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.sign_out_menu_item:
                signOutUser();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Signs out the user.
     */
    void signOutUser() {
        mFirebaseAuth.signOut();
        ViewUtils.startCustomActivity(this, WelcomeActivity.class, null, true);
    }

    /**
     * Sends the verification email.
     */
    void sendVerificationEmail() {
        if (!ConnectionUtils.hasInternetConnection(this)) {
            mSnackbar.setDuration(Snackbar.LENGTH_LONG);
            mSnackbar.setText(R.string.message_please_check_your_internet_connection).show();
            return;
        }
        if (mFirebaseUser != null) {
            mProgressDialog.show();
            mFirebaseUser
                    .sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mProgressDialog.dismiss();
                            mSnackbar.setDuration(Snackbar.LENGTH_LONG);
                            if (!task.isSuccessful()) {
                                mSnackbar.setText(R.string.message_an_error_has_occurred).show();
                                return;
                            }
                            mSnackbar.setText(R.string.message_verification_email_sent).show();
                        }
                    })
            ;
        }
    }
}
