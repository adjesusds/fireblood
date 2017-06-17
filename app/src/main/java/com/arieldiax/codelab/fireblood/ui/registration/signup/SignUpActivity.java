package com.arieldiax.codelab.fireblood.ui.registration.signup;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.firebase.User;
import com.arieldiax.codelab.fireblood.models.validations.FormValidator;
import com.arieldiax.codelab.fireblood.models.validations.Validation;
import com.arieldiax.codelab.fireblood.models.widgets.ConfirmBottomSheetDialog;
import com.arieldiax.codelab.fireblood.ui.verification.VerifyEmailActivity;
import com.arieldiax.codelab.fireblood.utils.ConnectionUtils;
import com.arieldiax.codelab.fireblood.utils.FirebaseUtils;
import com.arieldiax.codelab.fireblood.utils.FormUtils;
import com.arieldiax.codelab.fireblood.utils.Utils;
import com.arieldiax.codelab.fireblood.utils.ViewUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    /**
     * Request codes of the activity.
     */
    private static final int REQUEST_CODE_PICK_PLACE = 0;

    /**
     * Consent age of the Dominican Republic.
     */
    private static final int DOMINICAN_REPUBLIC_CONSENT_AGE = 18;

    /**
     * Views of the activity.
     */
    ScrollView mSignUpScrollView;
    ImageView mAppLogoImageView;
    EditText mEmailEditText;
    EditText mUsernameEditText;
    EditText mPasswordEditText;
    EditText mPhoneEditText;
    EditText mBirthdayEditText;
    Spinner mProvinceSpinner;
    EditText mHospitalEditText;
    Spinner mBloodTypeSpinner;
    Button mSignUpButton;

    /**
     * Date picker dialog for birthday.
     */
    DatePickerDialog mBirthdayDatePickerDialog;

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
     * Instance of the FormValidator class.
     */
    FormValidator mFormValidator;

    /**
     * Latitude of the hospital.
     */
    double mHospitalLatitude;

    /**
     * Longitude of the hospital.
     */
    double mHospitalLongitude;

    /**
     * Instance of the DatabaseReference class.
     */
    DatabaseReference mDatabaseReference;

    /**
     * Instance of the FirebaseAuth class.
     */
    FirebaseAuth mFirebaseAuth;

    /**
     * Remaining custom validations of the form.
     */
    int mFormRemainingCustomValidations;

    /**
     * Whether or not the form has passed the custom validations.
     */
    boolean mHasPassedCustomValidations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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
        mSignUpScrollView = (ScrollView) findViewById(R.id.sign_up_activity);
        mAppLogoImageView = (ImageView) findViewById(R.id.app_logo_image_view);
        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mUsernameEditText = (EditText) findViewById(R.id.username_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mPhoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        mBirthdayEditText = (EditText) findViewById(R.id.birthday_edit_text);
        mProvinceSpinner = (Spinner) findViewById(R.id.province_spinner);
        mHospitalEditText = (EditText) findViewById(R.id.hospital_edit_text);
        mBloodTypeSpinner = (Spinner) findViewById(R.id.blood_type_spinner);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
    }

    /**
     * Initializes the back end logic bindings.
     */
    void init() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - DOMINICAN_REPUBLIC_CONSENT_AGE;
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = 23;
        int minute = 59;
        int second = 59;
        mBirthdayDatePickerDialog = new DatePickerDialog(this, R.style.AppDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(
                    DatePicker view,
                    int year,
                    int month,
                    int dayOfMonth
            ) {
                mBirthdayEditText.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year, ++month, dayOfMonth));
            }
        }, year, month, dayOfMonth);
        calendar.set(year, month, dayOfMonth, hourOfDay, minute, second);
        mBirthdayDatePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        mSnackbar = Snackbar.make(mSignUpScrollView, "", Snackbar.LENGTH_LONG);
        mConfirmBottomSheetDialog = new ConfirmBottomSheetDialog(this);
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
                .addValidation(R.id.email_edit_text, User.PROPERTY_EMAIL, Validation.REGEX_NOT_EMPTY, R.string.validation_please_complete_the_field)
                .addValidation(R.id.email_edit_text, User.PROPERTY_EMAIL, Validation.REGEX_EMAIL, R.string.validation_please_enter_a_valid_email)
                .addValidation(R.id.username_edit_text, User.PROPERTY_USERNAME, Validation.REGEX_NOT_EMPTY, R.string.validation_please_complete_the_field)
                .addValidation(R.id.username_edit_text, User.PROPERTY_USERNAME, Validation.REGEX_USERNAME, R.string.validation_please_enter_a_valid_username)
                .addValidation(R.id.password_edit_text, null, Validation.REGEX_NOT_EMPTY, R.string.validation_please_complete_the_field)
                .addValidation(R.id.password_edit_text, null, Validation.REGEX_PASSWORD, R.string.validation_please_enter_a_valid_password)
                .addValidation(R.id.first_name_edit_text, User.PROPERTY_FIRST_NAME, Validation.REGEX_NOT_EMPTY, R.string.validation_please_complete_the_field)
                .addValidation(R.id.last_name_edit_text, User.PROPERTY_LAST_NAME, Validation.REGEX_NOT_EMPTY, R.string.validation_please_complete_the_field)
                .addValidation(R.id.phone_edit_text, User.PROPERTY_PHONE, Validation.REGEX_NOT_EMPTY, R.string.validation_please_complete_the_field)
                .addValidation(R.id.phone_edit_text, User.PROPERTY_PHONE, Validation.REGEX_PHONE, R.string.validation_please_enter_a_valid_phone)
                .addValidation(R.id.gender_radio_group, User.PROPERTY_GENDER, Validation.REGEX_NOT_EMPTY, R.string.validation_please_select_an_option)
                .addValidation(R.id.birthday_edit_text, User.PROPERTY_BIRTHDAY, Validation.REGEX_NOT_EMPTY, R.string.validation_please_complete_the_field)
                .addValidation(R.id.province_spinner, User.PROPERTY_PROVINCE, Validation.REGEX_NOT_EMPTY, R.string.validation_please_select_an_option)
                .addValidation(R.id.hospital_edit_text, User.PROPERTY_HOSPITAL, Validation.REGEX_NOT_EMPTY, R.string.validation_please_complete_the_field)
                .addValidation(R.id.blood_type_spinner, User.PROPERTY_BLOOD_TYPE, Validation.REGEX_NOT_EMPTY, R.string.validation_please_select_an_option)
                .addValidation(R.id.is_donor_switch, User.PROPERTY_IS_DONOR, Validation.REGEX_NOT_EMPTY, R.string.validation_please_select_an_option)
                .updateHash()
        ;
    }

    /**
     * Initializes the event listener view bindings.
     */
    void initListeners() {
        mPhoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(
                    View view,
                    boolean hasFocus
            ) {
                String phoneHint = (hasFocus) ? getString(R.string.profile_label_phone_hint) : "";
                mPhoneEditText.setHint(phoneHint);
            }
        });
        mBirthdayEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ViewUtils.hideKeyboard(SignUpActivity.this);
                mBirthdayDatePickerDialog.show();
            }
        });
        mProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id
            ) {
                mHospitalEditText.setText("");
                mHospitalLatitude = 0.00;
                mHospitalLongitude = 0.00;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mHospitalEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ViewUtils.hideKeyboard(SignUpActivity.this);
                if (FormUtils.hasEmptyValue(SignUpActivity.this, mProvinceSpinner)) {
                    mSnackbar.setText(R.string.message_please_select_a_province_first).show();
                    return;
                }
                if (!ConnectionUtils.hasInternetConnection(SignUpActivity.this)) {
                    mSnackbar.setText(R.string.message_please_check_your_internet_connection).show();
                    return;
                }
                Intent pickPlaceIntent = new Intent(SignUpActivity.this, PlacePickerActivity.class);
                pickPlaceIntent.putExtra(PlacePickerActivity.PROP_IN_PROVINCE_NAME, FormUtils.getViewValue(SignUpActivity.this, mProvinceSpinner));
                startActivityForResult(pickPlaceIntent, REQUEST_CODE_PICK_PLACE);
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ViewUtils.hideKeyboard(SignUpActivity.this);
                if (!mFormValidator.validate()) {
                    mSignUpScrollView.fullScroll(View.FOCUS_UP);
                    mSnackbar.setText(R.string.validation_validation_failed).show();
                    return;
                }
                if (!ConnectionUtils.hasInternetConnection(SignUpActivity.this)) {
                    mSnackbar.setText(R.string.message_please_check_your_internet_connection).show();
                    return;
                }
                attemptToSignUpUser();
            }
        });
    }

    /**
     * Updates the user interface view bindings.
     */
    void updateUi() {
        ArrayAdapter<CharSequence> provinceArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_provinces, android.R.layout.simple_spinner_item);
        provinceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvinceSpinner.setAdapter(provinceArrayAdapter);
        ArrayAdapter<CharSequence> bloodTypeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_blood_types, android.R.layout.simple_spinner_item);
        bloodTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBloodTypeSpinner.setAdapter(bloodTypeArrayAdapter);
        View.OnClickListener positiveButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mConfirmBottomSheetDialog.dismiss();
                finishAfterTransition();
            }
        };
        mConfirmBottomSheetDialog
                .setTitle(R.string.title_cancel_sign_up)
                .setMessage(R.string.message_are_you_sure)
                .setPositiveButtonListener(positiveButtonListener)
        ;
        mProgressDialog.setTitle(R.string.title_signing_up);
        mProgressDialog.setMessage(getString(R.string.message_please_wait_a_few_seconds));
        mProgressDialog.setCancelable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                attemptToFinishActivity();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        attemptToFinishActivity();
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_PLACE:
                Bundle extras = data.getExtras();
                switch (resultCode) {
                    case RESULT_OK:
                        mHospitalEditText.setText(extras.getString(PlacePickerActivity.PROP_OUT_HOSPITAL_NAME));
                        mHospitalLatitude = extras.getDouble(PlacePickerActivity.PROP_OUT_HOSPITAL_LATITUDE);
                        mHospitalLongitude = extras.getDouble(PlacePickerActivity.PROP_OUT_HOSPITAL_LONGITUDE);
                        break;
                    case RESULT_CANCELED:
                        mSnackbar.setText(R.string.message_action_canceled).show();
                        break;
                }
                break;
        }
    }

    /**
     * Attempts to finish the activity.
     */
    void attemptToFinishActivity() {
        if (mFormValidator.hasChanged()) {
            mConfirmBottomSheetDialog.show();
        } else {
            finishAfterTransition();
        }
    }

    /**
     * Attempts to sign up the user.
     */
    void attemptToSignUpUser() {
        mSignUpScrollView.fullScroll(View.FOCUS_UP);
        mProgressDialog.show();
        mFormRemainingCustomValidations = 2;
        mHasPassedCustomValidations = true;
        mDatabaseReference
                .child(User.CHILD_PATH)
                .orderByChild(User.PROPERTY_EMAIL)
                .equalTo(FormUtils.getViewValue(this, mEmailEditText))
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = FirebaseUtils.getDataSnapshotChild(dataSnapshot).getValue(User.class);
                        if (user != null) {
                            FormUtils.setViewError(SignUpActivity.this, mEmailEditText, getString(R.string.validation_the_email_is_in_use));
                            mHasPassedCustomValidations = false;
                        }
                        mFormRemainingCustomValidations--;
                        signUpUser();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                })
        ;
        mDatabaseReference
                .child(User.CHILD_PATH)
                .orderByChild(User.PROPERTY_USERNAME)
                .equalTo(FormUtils.getViewValue(this, mUsernameEditText))
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = FirebaseUtils.getDataSnapshotChild(dataSnapshot).getValue(User.class);
                        if (user != null) {
                            FormUtils.setViewError(SignUpActivity.this, mUsernameEditText, getString(R.string.validation_the_username_is_in_use));
                            mHasPassedCustomValidations = false;
                        }
                        mFormRemainingCustomValidations--;
                        signUpUser();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                })
        ;
    }

    /**
     * Signs up the user.
     */
    void signUpUser() {
        if (mFormRemainingCustomValidations > 0) {
            return;
        }
        if (!mHasPassedCustomValidations) {
            mProgressDialog.dismiss();
            mSnackbar.setText(R.string.validation_validation_failed).show();
            return;
        }
        mFirebaseAuth
                .createUserWithEmailAndPassword(FormUtils.getViewValue(this, mEmailEditText), FormUtils.getViewValue(this, mPasswordEditText))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            mSnackbar.setText(R.string.message_an_error_has_occurred).show();
                            return;
                        }
                        createUser(task.getResult().getUser().getUid());
                    }
                })
        ;
    }

    /**
     * Creates the user.
     *
     * @param userUid Unique ID of the user.
     */
    void createUser(String userUid) {
        mDatabaseReference
                .child(User.CHILD_PATH)
                .child(userUid)
                .setValue(getUser())
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                firebaseUser.delete();
                            }
                            mProgressDialog.dismiss();
                            mSnackbar.setText(R.string.message_an_error_has_occurred).show();
                            return;
                        }
                        sendVerificationEmail();
                    }
                })
        ;
    }

    /**
     * Sends the verification email.
     */
    void sendVerificationEmail() {
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser
                    .sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mProgressDialog.dismiss();
                            ViewUtils.startCustomActivity(SignUpActivity.this, VerifyEmailActivity.class, null, null, true);
                        }
                    })
            ;
        }
    }

    /**
     * Gets the user.
     */
    User getUser() {
        HashMap<String, Object> userMap = mFormValidator.serialize();
        String userPhotoUrl = "";
        String userGender = (userMap.get(User.PROPERTY_GENDER).toString().equals(getString(R.string.profile_label_gender_female)))
                ? User.VALUE_GENDER_FEMALE
                : User.VALUE_GENDER_MALE;
        long userBirthday = Utils.unixTime(userMap.get(User.PROPERTY_BIRTHDAY).toString(), "yyyy-MM-dd");
        String hospitalName = userMap.get(User.PROPERTY_HOSPITAL).toString();
        double hospitalLatitude = mHospitalLatitude;
        double hospitalLongitude = mHospitalLongitude;
        HashMap<String, Object> hospitalMap = new HashMap<>();
        boolean isDonor = !userMap.get(User.PROPERTY_IS_DONOR).toString().isEmpty();
        long userCreatedAt = System.currentTimeMillis();
        long userUpdatedAt = 0;
        long userDeletedAt = 0;
        userMap.put(User.PROPERTY_PHOTO_URL, userPhotoUrl);
        userMap.put(User.PROPERTY_GENDER, userGender);
        userMap.put(User.PROPERTY_BIRTHDAY, userBirthday);
        hospitalMap.put(User.Hospital.PROPERTY_NAME, hospitalName);
        hospitalMap.put(User.Hospital.PROPERTY_LATITUDE, hospitalLatitude);
        hospitalMap.put(User.Hospital.PROPERTY_LONGITUDE, hospitalLongitude);
        userMap.put(User.PROPERTY_HOSPITAL, hospitalMap);
        userMap.put(User.PROPERTY_IS_DONOR, isDonor);
        userMap.put(User.PROPERTY_CREATED_AT, userCreatedAt);
        userMap.put(User.PROPERTY_UPDATED_AT, userUpdatedAt);
        userMap.put(User.PROPERTY_DELETED_AT, userDeletedAt);
        return User.fromMap(userMap);
    }
}
