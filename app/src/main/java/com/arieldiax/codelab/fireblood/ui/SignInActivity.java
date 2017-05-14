package com.arieldiax.codelab.fireblood.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    /**
     * Views of the activity.
     */
    ScrollView mSignInScrollView;
    ImageView mAppLogoImageView;
    EditText mEmailOrUsernameEditText;
    EditText mPasswordEditText;
    TextView mForgotYourPasswordTextView;
    Button mSignInButton;

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
        setContentView(R.layout.activity_sign_in);
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
        mSignInScrollView = (ScrollView) findViewById(R.id.sign_in_activity);
        mAppLogoImageView = (ImageView) findViewById(R.id.app_logo_image_view);
        mEmailOrUsernameEditText = (EditText) findViewById(R.id.email_or_username_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mForgotYourPasswordTextView = (TextView) findViewById(R.id.forgot_your_password_text_view);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
    }

    /**
     * Initializes the back end logic bindings.
     */
    void init() {
        mSnackbar = Snackbar.make(mSignInScrollView, "", Snackbar.LENGTH_LONG);
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
                .addValidation(R.id.password_edit_text, Validation.REGEX_NOT_EMPTY, R.string.validation_please_complete_the_field)
                .addValidation(R.id.password_edit_text, Validation.REGEX_PASSWORD, R.string.validation_please_enter_a_valid_password)
        ;
    }

    /**
     * Initializes the event listener view bindings.
     */
    void initListeners() {
        Pair<View, String> activityPair = Pair.create((View) mAppLogoImageView, getString(R.string.transition_app_logo_image_view));
        mForgotYourPasswordTextView.setOnClickListener(ViewUtils.getStartCustomActivityOnClickListener(this, ForgotPasswordActivity.class, activityPair, false));
        mSignInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ViewUtils.hideKeyboard(SignInActivity.this);
                if (!mFormValidator.validate()) {
                    mSignInScrollView.fullScroll(View.FOCUS_UP);
                    mSnackbar.setText(R.string.validation_validation_failed).show();
                    return;
                }
                if (!ConnectionUtils.hasInternetConnection(SignInActivity.this)) {
                    mSnackbar.setText(R.string.message_please_check_your_internet_connection).show();
                    return;
                }
                attemptToSignInUser();
            }
        });
    }

    /**
     * Updates the user interface view bindings.
     */
    void updateUi() {
        mProgressDialog.setTitle(R.string.title_signing_in);
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
     * Attempts to sign in the user.
     */
    void attemptToSignInUser() {
        mSignInScrollView.fullScroll(View.FOCUS_UP);
        mProgressDialog.show();
        String emailOrUsername = FormUtils.getViewValue(this, mEmailOrUsernameEditText);
        if (emailOrUsername.matches(Validation.REGEX_EMAIL)) {
            signInUser(emailOrUsername);
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
                            FormUtils.setViewError(SignInActivity.this, mEmailOrUsernameEditText, getString(R.string.validation_the_username_is_not_registered));
                            mProgressDialog.dismiss();
                            mSnackbar.setText(R.string.validation_validation_failed).show();
                            return;
                        }
                        signInUser(user.email);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                })
        ;
    }

    /**
     * Signs in the user.
     *
     * @param email Email of the user.
     */
    void signInUser(String email) {
        mFirebaseAuth
                .signInWithEmailAndPassword(email, FormUtils.getViewValue(this, mPasswordEditText))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            mSnackbar.setText(R.string.message_the_credentials_did_not_match_our_records).show();
                            return;
                        }
                        Class activityClass = (task.getResult().getUser().isEmailVerified())
                                ? MainActivity.class
                                : VerifyEmailActivity.class;
                        ViewUtils.startCustomActivity(SignInActivity.this, activityClass, null, true);
                    }
                })
        ;
    }
}
