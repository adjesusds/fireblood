package com.arieldiax.codelab.fireblood;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
     * Spinner field for gender.
     */
    private Spinner mGenderSpinner;

    /**
     * Spinner field for blood type.
     */
    private Spinner mBloodTypeSpinner;

    /**
     * Date picker dialog for birthday.
     */
    private DatePickerDialog mBirthdayDatePickerDialog;

    /**
     * Array adapter for gender.
     */
    private ArrayAdapter<CharSequence> mGenderArrayAdapter;

    /**
     * Array adapter for blood type.
     */
    private ArrayAdapter<CharSequence> mBloodTypeArrayAdapter;

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
        mGenderSpinner = (Spinner) findViewById(R.id.gender_spinner);
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
        mBirthdayDatePickerDialog = new DatePickerDialog(SignUpActivity.this, R.style.AppAlertDialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mBirthdayEditText.setText(year + "-" + String.format(Locale.getDefault(), "%02d", ++month) + "-" + String.format(Locale.getDefault(), "%02d", dayOfMonth));
            }
        }, year, month, dayOfMonth);
        mGenderArrayAdapter = ArrayAdapter.createFromResource(SignUpActivity.this, R.array.array_genders, android.R.layout.simple_spinner_item);
        mGenderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(mGenderArrayAdapter);
        mBloodTypeArrayAdapter = ArrayAdapter.createFromResource(SignUpActivity.this, R.array.array_blood_types, android.R.layout.simple_spinner_item);
        mBloodTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBloodTypeSpinner.setAdapter(mBloodTypeArrayAdapter);
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
    }
}
