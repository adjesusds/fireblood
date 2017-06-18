package com.arieldiax.codelab.fireblood.ui.navigation.profile;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.firebase.User;
import com.arieldiax.codelab.fireblood.models.validations.FormValidator;
import com.arieldiax.codelab.fireblood.models.validations.Validation;
import com.arieldiax.codelab.fireblood.models.widgets.ConfirmBottomSheetDialog;
import com.arieldiax.codelab.fireblood.models.widgets.PhotoBottomSheetDialog;
import com.arieldiax.codelab.fireblood.ui.registration.signup.PlacePickerActivity;
import com.arieldiax.codelab.fireblood.utils.ConnectionUtils;
import com.arieldiax.codelab.fireblood.utils.FormUtils;
import com.arieldiax.codelab.fireblood.utils.Utils;
import com.arieldiax.codelab.fireblood.utils.ViewUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    /**
     * Properties of the activity.
     */
    public static final String PROP_IN_USER_UID = "user_uid";
    public static final String PROP_IN_USER_MAP = "user_map";

    /**
     * Request codes of the activity.
     */
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_PICK_PLACE = 1;

    /**
     * Consent age of the Dominican Republic.
     */
    private static final int DOMINICAN_REPUBLIC_CONSENT_AGE = 18;

    /**
     * Views of the activity.
     */
    ScrollView mEditProfileScrollView;
    RelativeLayout mPhotoRelativeLayout;
    ImageView mPhotoImageView;
    ImageView mEditProfileImageView;
    ProgressBar mPhotoProgressBar;
    EditText mPhoneEditText;
    EditText mBirthdayEditText;
    Spinner mProvinceSpinner;
    EditText mHospitalEditText;
    Spinner mBloodTypeSpinner;

    /**
     * Unique ID of the user.
     */
    String mUserUid;

    /**
     * Instance of the User class.
     */
    User mUser;

    /**
     * Date picker dialog for birthday.
     */
    DatePickerDialog mBirthdayDatePickerDialog;

    /**
     * Instance of the Toast class.
     */
    Toast mToast;

    /**
     * Instance of the Snackbar class.
     */
    Snackbar mSnackbar;

    /**
     * Instance of the ConfirmBottomSheetDialog class.
     */
    ConfirmBottomSheetDialog mConfirmBottomSheetDialog;

    /**
     * Instance of the PhotoBottomSheetDialog class.
     */
    PhotoBottomSheetDialog mPhotoBottomSheetDialog;

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
     * Instance of the StorageReference class.
     */
    StorageReference mStorageReference;

    /**
     * Whether or not the form has processed the profile photo.
     */
    boolean mHasProcessedProfilePhoto;

    /**
     * Whether or not the form has loaded the field entries.
     */
    boolean mHasLoadedFieldEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
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
        mEditProfileScrollView = (ScrollView) findViewById(R.id.edit_profile_activity);
        mPhotoRelativeLayout = (RelativeLayout) findViewById(R.id.photo_relative_layout);
        mPhotoImageView = (ImageView) findViewById(R.id.photo_image_view);
        mEditProfileImageView = (ImageView) findViewById(R.id.edit_profile_image_view);
        mPhotoProgressBar = (ProgressBar) findViewById(R.id.photo_progress_bar);
        mPhoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        mBirthdayEditText = (EditText) findViewById(R.id.birthday_edit_text);
        mProvinceSpinner = (Spinner) findViewById(R.id.province_spinner);
        mHospitalEditText = (EditText) findViewById(R.id.hospital_edit_text);
        mBloodTypeSpinner = (Spinner) findViewById(R.id.blood_type_spinner);
    }

    /**
     * Initializes the back end logic bindings.
     */
    void init() {
        mUserUid = getIntent().getStringExtra(PROP_IN_USER_UID);
        mUser = User.fromMap((HashMap<String, Object>) getIntent().getSerializableExtra(PROP_IN_USER_MAP));
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - DOMINICAN_REPUBLIC_CONSENT_AGE;
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = 23;
        int minute = 59;
        int second = 59;
        calendar.setTimeInMillis(mUser.birthday);
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
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(year, month, dayOfMonth, hourOfDay, minute, second);
        mBirthdayDatePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        mSnackbar = Snackbar.make(mEditProfileScrollView, "", Snackbar.LENGTH_LONG);
        mConfirmBottomSheetDialog = new ConfirmBottomSheetDialog(this);
        mPhotoBottomSheetDialog = new PhotoBottomSheetDialog(this);
        mProgressDialog = new ProgressDialog(this, R.style.AppProgressDialogTheme);
        mFormValidator = new FormValidator(this);
        mHospitalLatitude = mUser.hospital.latitude;
        mHospitalLongitude = mUser.hospital.longitude;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mHasProcessedProfilePhoto = true;
        mHasLoadedFieldEntries = false;
    }

    /**
     * Initializes the form validator view bindings.
     */
    void initValidators() {
        mFormValidator
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
        ;
    }

    /**
     * Initializes the event listener view bindings.
     */
    void initListeners() {
        mPhotoRelativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!mHasProcessedProfilePhoto) {
                    mToast.setText(R.string.message_the_photo_has_not_processed_yet);
                    mToast.show();
                    return;
                }
                mPhotoBottomSheetDialog
                        .setRemovePhotoButtonState(!mUser.photoUrl.isEmpty())
                        .show()
                ;
            }
        });
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
                ViewUtils.hideKeyboard(EditProfileActivity.this);
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
                if (!mHasLoadedFieldEntries) {
                    return;
                }
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
                ViewUtils.hideKeyboard(EditProfileActivity.this);
                if (!mHasProcessedProfilePhoto) {
                    mToast.setText(R.string.message_the_photo_has_not_processed_yet);
                    mToast.show();
                    return;
                }
                if (FormUtils.hasEmptyValue(EditProfileActivity.this, mProvinceSpinner)) {
                    mSnackbar.setText(R.string.message_please_select_a_province_first).show();
                    return;
                }
                if (!ConnectionUtils.hasInternetConnection(EditProfileActivity.this)) {
                    mSnackbar.setText(R.string.message_please_check_your_internet_connection).show();
                    return;
                }
                Intent pickPlaceIntent = new Intent(EditProfileActivity.this, PlacePickerActivity.class);
                pickPlaceIntent.putExtra(PlacePickerActivity.PROP_IN_PROVINCE_NAME, FormUtils.getViewValue(EditProfileActivity.this, mProvinceSpinner));
                startActivityForResult(pickPlaceIntent, REQUEST_CODE_PICK_PLACE);
            }
        });
    }

    /**
     * Updates the user interface view bindings.
     */
    void updateUi() {
        mPhotoImageView.setClipToOutline(true);
        mEditProfileImageView.setClipToOutline(true);
        if (!mUser.photoUrl.isEmpty()) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions
                    .placeholder(R.mipmap.ic_launcher)
                    .circleCrop()
            ;
            Glide
                    .with(this)
                    .load(mUser.photoUrl)
                    .apply(requestOptions)
                    .into(mPhotoImageView)
            ;
        }
        ArrayAdapter<CharSequence> provinceArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_provinces, android.R.layout.simple_spinner_item);
        provinceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvinceSpinner.setAdapter(provinceArrayAdapter);
        ArrayAdapter<CharSequence> bloodTypeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_blood_types, android.R.layout.simple_spinner_item);
        bloodTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBloodTypeSpinner.setAdapter(bloodTypeArrayAdapter);
        ((TextView) mSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        View.OnClickListener positiveButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mConfirmBottomSheetDialog.dismiss();
                finishAfterTransition();
            }
        };
        mConfirmBottomSheetDialog
                .setTitle(R.string.title_cancel_edit_profile)
                .setMessage(R.string.message_are_you_sure)
                .setPositiveButtonListener(positiveButtonListener)
        ;
        View.OnClickListener galleryButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mPhotoBottomSheetDialog.dismiss();
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(pickImageIntent, REQUEST_CODE_PICK_IMAGE);
            }
        };
        View.OnClickListener removePhotoButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mPhotoBottomSheetDialog.dismiss();
                mPhotoProgressBar.setVisibility(View.VISIBLE);
                mHasProcessedProfilePhoto = false;
                updateUserProfilePhoto("");
            }
        };
        mPhotoBottomSheetDialog
                .setGalleryButtonListener(galleryButtonListener)
                .setRemovePhotoButtonListener(removePhotoButtonListener)
        ;
        mProgressDialog.setTitle(R.string.title_editing_profile);
        mProgressDialog.setMessage(getString(R.string.message_please_wait_a_few_seconds));
        mFormValidator.populate(getUserMap());
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mHasLoadedFieldEntries = true;
            }
        }, DateUtils.SECOND_IN_MILLIS / 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                attemptToFinishActivity();
                return true;
            case R.id.save_menu_item:
                ViewUtils.hideKeyboard(EditProfileActivity.this);
                if (!mHasProcessedProfilePhoto) {
                    mToast.setText(R.string.message_the_photo_has_not_processed_yet);
                    mToast.show();
                    return true;
                }
                if (!mFormValidator.hasChanged()) {
                    mSnackbar.setText(R.string.validation_no_data_has_changed).show();
                    return true;
                }
                if (!mFormValidator.validate()) {
                    mEditProfileScrollView.fullScroll(View.FOCUS_UP);
                    mSnackbar.setText(R.string.validation_validation_failed).show();
                    return true;
                }
                if (!ConnectionUtils.hasInternetConnection(EditProfileActivity.this)) {
                    mSnackbar.setText(R.string.message_please_check_your_internet_connection).show();
                    return true;
                }
                attemptToSaveUser();
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
            case REQUEST_CODE_PICK_IMAGE:
                switch (resultCode) {
                    case RESULT_OK:
                        mPhotoProgressBar.setVisibility(View.VISIBLE);
                        mHasProcessedProfilePhoto = false;
                        mStorageReference
                                .child(User.sStoragePathProfilePhoto.replace(User.PATH_SEGMENT_USER_UID, mUserUid).replace(User.PATH_SEGMENT_UNIX_TIME, String.valueOf(System.currentTimeMillis())))
                                .putFile(data.getData())
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            mPhotoProgressBar.setVisibility(View.GONE);
                                            mHasProcessedProfilePhoto = true;
                                            mSnackbar.setText(R.string.message_an_error_has_occurred).show();
                                            return;
                                        }
                                        @SuppressWarnings("VisibleForTests") String photoUrl = task.getResult().getDownloadUrl().toString();
                                        updateUserProfilePhoto(photoUrl);
                                    }
                                })
                        ;
                        break;
                    case RESULT_CANCELED:
                        mSnackbar.setText(R.string.message_action_canceled).show();
                        break;
                }
                break;
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
     * Gets the map of the user.
     *
     * @return The map of the user.
     */
    HashMap<String, Object> getUserMap() {
        HashMap<String, Object> userMap = mUser.toMap();
        int userGender = (mUser.gender.equals(User.VALUE_GENDER_FEMALE))
                ? 0
                : 1;
        String userBirthday = Utils.formatDate(mUser.birthday, "yyyy-MM-dd");
        String userHospital = mUser.hospital.name;
        userMap.put(User.PROPERTY_GENDER, userGender);
        userMap.put(User.PROPERTY_BIRTHDAY, userBirthday);
        userMap.put(User.PROPERTY_HOSPITAL, userHospital);
        return userMap;
    }

    /**
     * Attempts to finish the activity.
     */
    void attemptToFinishActivity() {
        if (!mHasProcessedProfilePhoto) {
            mToast.setText(R.string.message_the_photo_has_not_processed_yet);
            mToast.show();
            return;
        }
        if (mFormValidator.hasChanged()) {
            mConfirmBottomSheetDialog.show();
        } else {
            finishAfterTransition();
        }
    }

    /**
     * Updates the user profile photo.
     *
     * @param photoUrl URL of the photo.
     */
    void updateUserProfilePhoto(String photoUrl) {
        HashMap<String, Object> userMap = new HashMap<>();
        final String userPhotoUrl = photoUrl;
        long userUpdatedAt = System.currentTimeMillis();
        userMap.put(User.PROPERTY_PHOTO_URL, userPhotoUrl);
        userMap.put(User.PROPERTY_UPDATED_AT, userUpdatedAt);
        mDatabaseReference
                .child(User.CHILD_PATH)
                .child(mUserUid)
                .updateChildren(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mPhotoProgressBar.setVisibility(View.GONE);
                        mHasProcessedProfilePhoto = true;
                        if (!task.isSuccessful()) {
                            mSnackbar.setText(R.string.message_an_error_has_occurred).show();
                            return;
                        }
                        mSnackbar.setText(R.string.message_profile_photo_successfully_edited).show();
                        mUser.photoUrl = userPhotoUrl;
                        if (!mUser.photoUrl.isEmpty()) {
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions
                                    .placeholder(R.mipmap.ic_launcher)
                                    .circleCrop()
                            ;
                            Glide
                                    .with(EditProfileActivity.this)
                                    .load(mUser.photoUrl)
                                    .apply(requestOptions)
                                    .into(mPhotoImageView)
                            ;
                        } else {
                            mPhotoImageView.setImageResource(R.mipmap.ic_launcher);
                        }
                    }
                })
        ;
    }

    /**
     * Attempts to save the user.
     */
    void attemptToSaveUser() {
        mEditProfileScrollView.fullScroll(View.FOCUS_UP);
        mProgressDialog.show();
        updateUser();
    }

    /**
     * Updates the user.
     */
    void updateUser() {
        mDatabaseReference
                .child(User.CHILD_PATH)
                .child(mUserUid)
                .setValue(getUser())
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mProgressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            mSnackbar.setText(R.string.message_an_error_has_occurred).show();
                            return;
                        }
                        mSnackbar.setText(R.string.message_profile_successfully_edited).show();
                        mFormValidator.updateHash();
                    }
                })
        ;
    }

    /**
     * Gets the user.
     *
     * @return The instance of the User class.
     */
    User getUser() {
        HashMap<String, Object> userMap = mFormValidator.serialize();
        String userEmail = mUser.email;
        String userUsername = mUser.username;
        String userPhotoUrl = mUser.photoUrl;
        String userGender = (userMap.get(User.PROPERTY_GENDER).toString().equals(getString(R.string.profile_label_gender_female)))
                ? User.VALUE_GENDER_FEMALE
                : User.VALUE_GENDER_MALE;
        long userBirthday = Utils.unixTime(userMap.get(User.PROPERTY_BIRTHDAY).toString(), "yyyy-MM-dd");
        String hospitalName = userMap.get(User.PROPERTY_HOSPITAL).toString();
        double hospitalLatitude = mHospitalLatitude;
        double hospitalLongitude = mHospitalLongitude;
        HashMap<String, Object> hospitalMap = new HashMap<>();
        boolean isDonor = !userMap.get(User.PROPERTY_IS_DONOR).toString().isEmpty();
        long userCreatedAt = mUser.createdAt;
        long userUpdatedAt = System.currentTimeMillis();
        long userDeletedAt = mUser.deletedAt;
        userMap.put(User.PROPERTY_EMAIL, userEmail);
        userMap.put(User.PROPERTY_USERNAME, userUsername);
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
