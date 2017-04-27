package com.arieldiax.codelab.fireblood.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.validations.FormValidator;
import com.arieldiax.codelab.fireblood.models.validations.Validation;
import com.arieldiax.codelab.fireblood.models.widgets.ConfirmBottomSheetDialog;
import com.arieldiax.codelab.fireblood.utils.ConnectionUtils;
import com.arieldiax.codelab.fireblood.utils.FormUtils;
import com.arieldiax.codelab.fireblood.utils.ViewUtils;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    /**
     * Consent age of the Dominican Republic.
     */
    private static final int DOMINICAN_REPUBLIC_CONSENT_AGE = 18;

    /**
     * Views of the activity.
     */
    private ScrollView mSignUpScrollView;
    private EditText mPhoneEditText;
    private EditText mBirthdayEditText;
    private Spinner mProvinceSpinner;
    private EditText mHospitalEditText;
    private Spinner mBloodTypeSpinner;
    private Button mSignUpButton;

    /**
     * Date picker dialog for birthday.
     */
    private DatePickerDialog mBirthdayDatePickerDialog;

    /**
     * Instance of the Snackbar class.
     */
    private Snackbar mSnackbar;

    /**
     * Instance of the ConfirmBottomSheetDialog class.
     */
    private ConfirmBottomSheetDialog mConfirmBottomSheetDialog;

    /**
     * Instance of the ProgressDialog class.
     */
    private ProgressDialog mProgressDialog;

    /**
     * Instance of the FormValidator class.
     */
    private FormValidator mFormValidator;

    /**
     * Hash of the form validator.
     */
    private String mFormValidatorHash;

    /**
     * Latitude of the hospital.
     */
    private double mHospitalLatitude;

    /**
     * Longitude of the hospital.
     */
    private double mHospitalLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUi();
        init();
        initValidators();
        initListeners();
    }

    /**
     * Initializes the user interface view bindings.
     */
    private void initUi() {
        mSignUpScrollView = (ScrollView) findViewById(R.id.sign_up_activity);
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
    private void init() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - DOMINICAN_REPUBLIC_CONSENT_AGE;
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = 23;
        int minute = 59;
        int second = 59;
        mBirthdayDatePickerDialog = new DatePickerDialog(this, R.style.AppDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mBirthdayEditText.setText(getString(R.string.profile_label_birthday_format, year, ++month, dayOfMonth));
            }
        }, year, month, dayOfMonth);
        calendar.set(year, month, dayOfMonth, hourOfDay, minute, second);
        mBirthdayDatePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        ArrayAdapter<CharSequence> provinceArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_values_provinces, android.R.layout.simple_spinner_item);
        provinceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvinceSpinner.setAdapter(provinceArrayAdapter);
        ArrayAdapter<CharSequence> bloodTypeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_values_blood_types, android.R.layout.simple_spinner_item);
        bloodTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBloodTypeSpinner.setAdapter(bloodTypeArrayAdapter);
        mSnackbar = Snackbar.make(mSignUpScrollView, "", Snackbar.LENGTH_LONG);
        View.OnClickListener positiveButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mConfirmBottomSheetDialog.dismiss();
                finishAfterTransition();
            }
        };
        mConfirmBottomSheetDialog = new ConfirmBottomSheetDialog(this)
                .setTitle(R.string.title_cancel_sign_up)
                .setMessage(R.string.message_are_you_sure)
                .setPositiveButtonListener(positiveButtonListener)
        ;
        mProgressDialog = new ProgressDialog(this, R.style.AppProgressDialogTheme);
        mProgressDialog.setTitle(R.string.title_start_sign_up);
        mProgressDialog.setMessage(getString(R.string.message_please_wait_a_few_seconds));
        mProgressDialog.setCancelable(false);
        mFormValidator = new FormValidator(this);
    }

    /**
     * Initializes the form validator view bindings.
     */
    private void initValidators() {
        mFormValidator
                .addValidation(R.id.email_edit_text, R.string.validation_please_complete_the_field)
                .addValidation(R.id.email_edit_text, Validation.REGEX_EMAIL, R.string.validation_please_enter_a_valid_email)
                .addValidation(R.id.username_edit_text, R.string.validation_please_complete_the_field)
                .addValidation(R.id.username_edit_text, Validation.REGEX_USERNAME, R.string.validation_please_enter_a_valid_username)
                .addValidation(R.id.password_edit_text, R.string.validation_please_complete_the_field)
                .addValidation(R.id.password_edit_text, Validation.REGEX_PASSWORD, R.string.validation_please_enter_a_valid_password)
                .addValidation(R.id.first_name_edit_text, R.string.validation_please_complete_the_field)
                .addValidation(R.id.last_name_edit_text, R.string.validation_please_complete_the_field)
                .addValidation(R.id.phone_edit_text, R.string.validation_please_complete_the_field)
                .addValidation(R.id.phone_edit_text, Validation.REGEX_PHONE, R.string.validation_please_enter_a_valid_phone)
                .addValidation(R.id.gender_radio_group, R.string.validation_please_select_an_option)
                .addValidation(R.id.birthday_edit_text, R.string.validation_please_complete_the_field)
                .addValidation(R.id.province_spinner, R.string.validation_please_select_an_option)
                .addValidation(R.id.hospital_edit_text, R.string.validation_please_complete_the_field)
                .addValidation(R.id.blood_type_spinner, R.string.validation_please_select_an_option)
                .addValidation(R.id.is_donor_switch, R.string.validation_please_select_an_option)
        ;
        mFormValidatorHash = mFormValidator.hash();
    }

    /**
     * Initializes the event listener view bindings.
     */
    private void initListeners() {
        mPhoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mPhoneEditText.setHint(R.string.profile_label_phone_hint);
                } else {
                    mPhoneEditText.setHint("");
                }
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                pickPlaceIntent.putExtra("province_name", FormUtils.getViewValue(SignUpActivity.this, mProvinceSpinner));
                startActivityForResult(pickPlaceIntent, 0);
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
                mProgressDialog.show();
            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();
        switch (resultCode) {
            case RESULT_OK:
                mHospitalEditText.setText(extras.getString("hospital_name"));
                mHospitalLatitude = extras.getDouble("hospital_latitude");
                mHospitalLongitude = extras.getDouble("hospital_longitude");
                break;
            case RESULT_CANCELED:
                mSnackbar.setText(extras.getInt("message_resource_id")).show();
                break;
        }
    }

    /**
     * Attempts to finish the activity.
     */
    private void attemptToFinishActivity() {
        if (!mFormValidatorHash.equals(mFormValidator.hash())) {
            mConfirmBottomSheetDialog.show();
        } else {
            finishAfterTransition();
        }
    }
}
