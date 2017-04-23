package com.arieldiax.codelab.fireblood.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.ConfirmBottomSheetDialog;
import com.arieldiax.codelab.fireblood.models.FormValidator;
import com.arieldiax.codelab.fireblood.utils.ConnectionUtils;
import com.arieldiax.codelab.fireblood.utils.FormUtils;
import com.arieldiax.codelab.fireblood.utils.ViewUtils;

import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    /**
     * Consent age of the Dominican Republic.
     */
    private static final int DOMINICAN_REPUBLIC_CONSENT_AGE = 18;

    /**
     * Edit text field for birthday.
     */
    private EditText mBirthdayEditText;

    /**
     * Edit text field for phone.
     */
    private EditText mPhoneEditText;

    /**
     * Spinner field for province.
     */
    private Spinner mProvinceSpinner;

    /**
     * Edit text field for hospital.
     */
    private EditText mHospitalEditText;

    /**
     * Spinner field for blood type.
     */
    private Spinner mBloodTypeSpinner;

    /**
     * Button field for sign up.
     */
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
     * Instance of the FormValidator class.
     */
    private FormValidator mFormValidator;

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
        mBirthdayEditText = (EditText) findViewById(R.id.birthday_edit_text);
        mPhoneEditText = (EditText) findViewById(R.id.phone_edit_text);
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
                mBirthdayEditText.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year, ++month, dayOfMonth));
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
        mSnackbar = Snackbar.make(findViewById(R.id.activity_sign_up), "", Snackbar.LENGTH_LONG);
        View.OnClickListener positiveButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mConfirmBottomSheetDialog.dismiss();
                finishAfterTransition();
            }
        };
        mConfirmBottomSheetDialog = new ConfirmBottomSheetDialog(this)
                .setTitle(R.string.title_cancel_registration)
                .setMessage(R.string.message_are_you_sure)
                .setPositiveButtonListener(positiveButtonListener)
        ;
        mFormValidator = new FormValidator(this);
    }

    /**
     * Initializes the form validator view bindings.
     */
    private void initValidators() {
        mFormValidator
                .addValidation(R.id.email_edit_text, R.string.validation_please_fill_the_field)
                .addValidation(R.id.password_edit_text, R.string.validation_please_fill_the_field)
                .addValidation(R.id.first_name_edit_text, R.string.validation_please_fill_the_field)
                .addValidation(R.id.last_name_edit_text, R.string.validation_please_fill_the_field)
                .addValidation(R.id.birthday_edit_text, R.string.validation_please_fill_the_field)
                .addValidation(R.id.gender_radio_group, R.string.validation_please_select_an_option)
                .addValidation(R.id.phone_edit_text, R.string.validation_please_fill_the_field)
                .addValidation(R.id.province_spinner, R.string.validation_please_select_an_option)
                .addValidation(R.id.hospital_edit_text, R.string.validation_please_fill_the_field)
                .addValidation(R.id.blood_type_spinner, R.string.validation_please_select_an_option)
        ;
    }

    /**
     * Initializes the event listener view bindings.
     */
    private void initListeners() {
        mBirthdayEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ViewUtils.hideKeyboard(SignUpActivity.this);
                mBirthdayDatePickerDialog.show();
            }
        });
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
        mPhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
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
                if (!mFormValidator.validate()) {
                    mSnackbar.setText(R.string.validation_validation_failed).show();
                    return;
                }
                mSnackbar.setText(R.string.validation_validation_passed).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                mConfirmBottomSheetDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        mConfirmBottomSheetDialog.show();
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
}
