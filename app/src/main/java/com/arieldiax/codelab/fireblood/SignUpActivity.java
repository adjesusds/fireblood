package com.arieldiax.codelab.fireblood;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    /**
     * Edit text field for birthday.
     */
    private EditText mBirthdayEditText;

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
     * Date picker dialog for birthday.
     */
    private DatePickerDialog mBirthdayDatePickerDialog;

    /**
     * Instance of the Snackbar class.
     */
    private Snackbar mSnackbar;

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
        initListeners();
    }

    /**
     * Initializes the user interface view bindings.
     */
    private void initUi() {
        mBirthdayEditText = (EditText) findViewById(R.id.birthday_edit_text);
        mProvinceSpinner = (Spinner) findViewById(R.id.province_spinner);
        mHospitalEditText = (EditText) findViewById(R.id.hospital_edit_text);
        mBloodTypeSpinner = (Spinner) findViewById(R.id.blood_type_spinner);
    }

    /**
     * Initializes the back end logic bindings.
     */
    private void init() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - 18;
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mBirthdayDatePickerDialog = new DatePickerDialog(this, R.style.AppDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mBirthdayEditText.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year, ++month, dayOfMonth));
            }
        }, year, month, dayOfMonth);
        ArrayAdapter<CharSequence> provinceArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_values_provinces, android.R.layout.simple_spinner_item);
        provinceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvinceSpinner.setAdapter(provinceArrayAdapter);
        ArrayAdapter<CharSequence> bloodTypeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_values_blood_types, android.R.layout.simple_spinner_item);
        bloodTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBloodTypeSpinner.setAdapter(bloodTypeArrayAdapter);
        mSnackbar = Snackbar.make(findViewById(R.id.activity_sign_up), "", Snackbar.LENGTH_LONG);
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
        mProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mHospitalEditText.setText("");
                mHospitalEditText.setEnabled(!FormUtils.hasEmptyValue(mProvinceSpinner));
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
                if (!ConnectionUtils.hasInternetConnection(SignUpActivity.this)) {
                    mSnackbar.setText(R.string.message_please_check_your_internet_connection).show();
                    return;
                }
                Intent pickPlaceIntent = new Intent(SignUpActivity.this, PlacePickerActivity.class);
                pickPlaceIntent.putExtra("province_name", FormUtils.getSpinnerValue(mProvinceSpinner));
                startActivityForResult(pickPlaceIntent, 0);
            }
        });
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
