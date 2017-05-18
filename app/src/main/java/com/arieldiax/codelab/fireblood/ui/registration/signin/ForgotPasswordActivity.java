package com.arieldiax.codelab.fireblood.ui.registration.signin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.firebase.User;
import com.arieldiax.codelab.fireblood.models.validations.FormValidator;
import com.arieldiax.codelab.fireblood.models.validations.Validation;
import com.arieldiax.codelab.fireblood.utils.ConnectionUtils;
import com.arieldiax.codelab.fireblood.utils.FirebaseUtils;
import com.arieldiax.codelab.fireblood.utils.FormUtils;
import com.arieldiax.codelab.fireblood.utils.ViewUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity {

    /**
     * Views of the activity.
     */
    ScrollView mForgotPasswordScrollView;
    EditText mEmailOrUsernameEditText;
    TextView mSignInTextView;
    Button mSendEmailButton;

    /**
     * Instance of the Snackbar class.
     */
    Snackbar mSnackbar;

    /**
     * Instance of the ProgressDialog class.
     */
    ProgressDialog mProgressDialog;

    /**
     * Instance of the FormValidator class.
     */
    FormValidator mFormValidator;

    /**
     * Instance of the DatabaseReference class.
     */
    DatabaseReference mDatabaseReference;

    /**
     * Instance of the FirebaseAuth class.
     */
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initUi();
        init();
        initValidators();
        initListeners();
        updateUi();
    }

    /**
     * Initializes the user interface view bindings.
     */
    void initUi() {
        mForgotPasswordScrollView = (ScrollView) findViewById(R.id.forgot_password_activity);
        mEmailOrUsernameEditText = (EditText) findViewById(R.id.email_or_username_edit_text);
        mSignInTextView = (TextView) findViewById(R.id.sign_in_text_view);
        mSendEmailButton = (Button) findViewById(R.id.send_email_button);
    }

    /**
     * Initializes the back end logic bindings.
     */
    void init() {
        mSnackbar = Snackbar.make(mForgotPasswordScrollView, "", Snackbar.LENGTH_LONG);
        mProgressDialog = new ProgressDialog(this, R.style.AppProgressDialogTheme);
        mFormValidator = new FormValidator(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Initializes the form validator view bindings.
     */
    void initValidators() {
        mFormValidator
                .addValidation(R.id.email_or_username_edit_text, Validation.REGEX_NOT_EMPTY, R.string.validation_please_complete_the_field)
                .addValidation(R.id.email_or_username_edit_text, Validation.REGEX_EMAIL_OR_USERNAME, R.string.validation_please_enter_a_valid_email_or_username)
        ;
    }

    /**
     * Initializes the event listener view bindings.
     */
    void initListeners() {
        mSignInTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finishAfterTransition();
            }
        });
        mSendEmailButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ViewUtils.hideKeyboard(ForgotPasswordActivity.this);
                if (!mFormValidator.validate()) {
                    mForgotPasswordScrollView.fullScroll(View.FOCUS_UP);
                    mSnackbar.setText(R.string.validation_validation_failed).show();
                    return;
                }
                if (!ConnectionUtils.hasInternetConnection(ForgotPasswordActivity.this)) {
                    mSnackbar.setText(R.string.message_please_check_your_internet_connection).show();
                    return;
                }
                attemptToSendRecoveryEmail();
            }
        });
    }

    /**
     * Updates the user interface view bindings.
     */
    void updateUi() {
        mProgressDialog.setTitle(R.string.title_sending_recovery_email);
        mProgressDialog.setMessage(getString(R.string.message_please_wait_a_few_seconds));
        mProgressDialog.setCancelable(false);
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

    /**
     * Attempts to send the recovery email.
     */
    void attemptToSendRecoveryEmail() {
        mForgotPasswordScrollView.fullScroll(View.FOCUS_UP);
        mProgressDialog.show();
        String emailOrUsername = FormUtils.getViewValue(this, mEmailOrUsernameEditText);
        if (emailOrUsername.matches(Validation.REGEX_EMAIL)) {
            sendRecoveryEmail(emailOrUsername);
            return;
        }
        mDatabaseReference
                .child(User.CHILD_NODE)
                .orderByChild(User.PROPERTY_USERNAME)
                .equalTo(emailOrUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = FirebaseUtils.getDataSnapshotChild(dataSnapshot).getValue(User.class);
                        if (user == null) {
                            FormUtils.setViewError(ForgotPasswordActivity.this, mEmailOrUsernameEditText, getString(R.string.validation_the_username_is_not_registered));
                            mProgressDialog.dismiss();
                            mSnackbar.setText(R.string.validation_validation_failed).show();
                            return;
                        }
                        sendRecoveryEmail(user.email);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                })
        ;
    }

    /**
     * Sends the recovery email.
     *
     * @param email Email of the user.
     */
    void sendRecoveryEmail(String email) {
        mFirebaseAuth
                .sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mProgressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            FormUtils.setViewError(ForgotPasswordActivity.this, mEmailOrUsernameEditText, getString(R.string.validation_the_email_is_not_registered));
                            mSnackbar.setText(R.string.validation_validation_failed).show();
                            return;
                        }
                        mSnackbar.setText(R.string.message_recovery_email_sent).show();
                    }
                })
        ;
    }
}
