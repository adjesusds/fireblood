package com.arieldiax.codelab.fireblood.ui.navigation.profile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.firebase.User;
import com.arieldiax.codelab.fireblood.ui.navigation.main.MainActivity;
import com.arieldiax.codelab.fireblood.utils.AnimationUtils;
import com.arieldiax.codelab.fireblood.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends MainActivity {

    /**
     * Properties of the activity.
     */
    public static final String PROP_IN_USER_UID = "user_uid";

    /**
     * Views of the activity.
     */
    ScrollView mProfileScrollView;
    ImageView mUserPhotoImageView;
    FrameLayout mEditProfileFrameLayout;
    ImageView mEditProfileImageView;
    TextView mUserFullNameTextView;
    TextView mUserProvinceTextView;
    TextView mUserCountryTextView;
    TextView mUserAgeTextView;
    TextView mUserGenderTextView;
    TextView mUserBloodTypeTextView;
    TextView mUserIsDonorTextView;
    TextView mUserHospitalTextView;
    CardView mContactMeCardView;
    TextView mAnEmailTextView;
    TextView mACallTextView;
    TextView mAMessageTextView;

    /**
     * Unique ID of the user.
     */
    String mUserUid;

    /**
     * Instance of the User class.
     */
    User mUser;

    /**
     * Instance of the RequestOptions class.
     */
    RequestOptions mRequestOptions;

    /**
     * On click listener of the user fields.
     */
    View.OnClickListener mUserFieldsOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.fragment_profile, (FrameLayout) findViewById(R.id.main_frame_layout));
        initUi();
        init();
        initListeners();
        updateUi();
    }

    @Override
    protected void initUi() {
        super.initUi();
        mProfileScrollView = (ScrollView) findViewById(R.id.profile_fragment);
        mUserPhotoImageView = (ImageView) findViewById(R.id.user_photo_image_view);
        mEditProfileFrameLayout = (FrameLayout) findViewById(R.id.edit_profile_frame_layout);
        mEditProfileImageView = (ImageView) findViewById(R.id.edit_profile_image_view);
        mUserFullNameTextView = (TextView) findViewById(R.id.user_full_name_text_view);
        mUserProvinceTextView = (TextView) findViewById(R.id.user_province_text_view);
        mUserCountryTextView = (TextView) findViewById(R.id.user_country_text_view);
        mUserAgeTextView = (TextView) findViewById(R.id.user_age_text_view);
        mUserGenderTextView = (TextView) findViewById(R.id.user_gender_text_view);
        mUserBloodTypeTextView = (TextView) findViewById(R.id.user_blood_type_text_view);
        mUserIsDonorTextView = (TextView) findViewById(R.id.user_is_donor_text_view);
        mUserHospitalTextView = (TextView) findViewById(R.id.user_hospital_text_view);
        mContactMeCardView = (CardView) findViewById(R.id.contact_me_card_view);
        mAnEmailTextView = (TextView) findViewById(R.id.an_email_text_view);
        mACallTextView = (TextView) findViewById(R.id.a_call_text_view);
        mAMessageTextView = (TextView) findViewById(R.id.a_message_text_view);
    }

    @Override
    protected void init() {
        super.init();
        mClassCanonicalName = getClass().getCanonicalName();
        mUserUid = getIntent().getStringExtra(PROP_IN_USER_UID);
        mUser = null;
        mRequestOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .circleCrop()
        ;
        mUserFieldsOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mUser == null) {
                    mToast.setText(R.string.message_the_data_has_not_loaded_yet);
                    mToast.show();
                    return;
                }
                Intent activityIntent = null;
                Pair<View, String> activityPair1 = null;
                Pair<View, String> activityPair2 = null;
                ActivityOptions activityOptions = null;
                Intent externalIntent = null;
                switch (view.getId()) {
                    case R.id.user_photo_image_view:
                        activityIntent = new Intent(ProfileActivity.this, ViewPhotoActivity.class);
                        activityIntent.putExtra(ViewPhotoActivity.PROP_IN_USER_PHOTO_URL, mUser.photoUrl);
                        activityPair1 = Pair.create((View) mUserPhotoImageView, mUserPhotoImageView.getTransitionName());
                        activityOptions = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this, activityPair1);
                        startActivity(activityIntent, activityOptions.toBundle());
                        return;
                    case R.id.edit_profile_frame_layout:
                        activityIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                        activityIntent.putExtra(EditProfileActivity.PROP_IN_USER_UID, mFirebaseUser.getUid());
                        activityIntent.putExtra(EditProfileActivity.PROP_IN_USER_MAP, mUser.toMap());
                        activityPair1 = Pair.create((View) mUserPhotoImageView, mUserPhotoImageView.getTransitionName());
                        activityPair2 = Pair.create((View) mEditProfileImageView, mEditProfileImageView.getTransitionName());
                        activityOptions = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this, activityPair1, activityPair2);
                        startActivity(activityIntent, activityOptions.toBundle());
                        return;
                    case R.id.user_hospital_text_view:
                        externalIntent = new Intent(Intent.ACTION_VIEW);
                        externalIntent.setData(Uri.parse("geo:" + mUser.hospital.latitude + "," + mUser.hospital.longitude + "?q=" + Uri.encode(mUser.hospital.name)));
                        break;
                    case R.id.an_email_text_view:
                        externalIntent = new Intent(Intent.ACTION_SENDTO);
                        externalIntent.setData(Uri.parse("mailto:"));
                        externalIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mUser.email});
                        break;
                    case R.id.a_call_text_view:
                        externalIntent = new Intent(Intent.ACTION_DIAL);
                        externalIntent.setData(Uri.parse("tel:" + mUser.phone));
                        break;
                    case R.id.a_message_text_view:
                        externalIntent = new Intent(Intent.ACTION_SENDTO);
                        externalIntent.setData(Uri.parse("smsto:" + mUser.phone));
                        break;
                }
                if (externalIntent != null) {
                    if (externalIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(externalIntent);
                    } else {
                        mToast.setText(R.string.message_no_application_was_found);
                        mToast.show();
                    }
                }
            }
        };
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        mUserPhotoImageView.setOnClickListener(mUserFieldsOnClickListener);
        mEditProfileFrameLayout.setOnClickListener(mUserFieldsOnClickListener);
        mUserHospitalTextView.setOnClickListener(mUserFieldsOnClickListener);
        mAnEmailTextView.setOnClickListener(mUserFieldsOnClickListener);
        mACallTextView.setOnClickListener(mUserFieldsOnClickListener);
        mAMessageTextView.setOnClickListener(mUserFieldsOnClickListener);
    }

    @Override
    protected void updateUi() {
        super.updateUi();
        if (!mUserUid.isEmpty()) {
            mMainBottomNavigationView.setVisibility(View.GONE);
        } else {
            mMainBottomNavigationView.setSelectedItemId(R.id.profile_navigation_item);
            mEditProfileFrameLayout.setVisibility(View.VISIBLE);
            mContactMeCardView.setVisibility(View.VISIBLE);
        }
        mUserPhotoImageView.setClipToOutline(true);
        startFieldsAnimation();
        mDatabaseReference
                .child(User.DATABASE_PATH)
                .child((!mUserUid.isEmpty()) ? mUserUid : mFirebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUser = dataSnapshot.getValue(User.class);
                        updateUserFields();
                        stopFieldsAnimation();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                })
        ;
    }

    @Override
    protected void onNavigationItemReselectedListener() {
        super.onNavigationItemReselectedListener();
        mProfileScrollView.fullScroll(View.FOCUS_UP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return (
                mUserUid.isEmpty() &&
                        super.onCreateOptionsMenu(menu)
        );
    }

    @Override
    public void onBackPressed() {
        if (!mUserUid.isEmpty()) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Starts the animation of the fields.
     */
    void startFieldsAnimation() {
        Animation blinkAnimation = AnimationUtils.getBlinkAnimation();
        mUserFullNameTextView.startAnimation(blinkAnimation);
        mUserProvinceTextView.startAnimation(blinkAnimation);
        mUserCountryTextView.startAnimation(blinkAnimation);
        mUserAgeTextView.startAnimation(blinkAnimation);
        mUserGenderTextView.startAnimation(blinkAnimation);
        mUserBloodTypeTextView.startAnimation(blinkAnimation);
        mUserIsDonorTextView.startAnimation(blinkAnimation);
    }

    /**
     * Updates the user fields.
     */
    void updateUserFields() {
        setTitle(mUser.username);
        if (!mUser.photoUrl.isEmpty()) {
            Glide
                    .with(this)
                    .load(mUser.photoUrl)
                    .apply(mRequestOptions)
                    .into(mUserPhotoImageView)
            ;
            mUserPhotoImageView.setOnClickListener(mUserFieldsOnClickListener);
        } else {
            mUserPhotoImageView.setImageResource(R.mipmap.ic_launcher);
            mUserPhotoImageView.setOnClickListener(null);
        }
        mUserFullNameTextView.setText(mUser.fullName);
        mUserProvinceTextView.setText(mUser.province);
        mUserCountryTextView.setText(mUser.country);
        mUserAgeTextView.setText(getString(R.string.profile_information_user_age, Utils.calculateUserAge(mUser.birthday)));
        int genderResourceId = (mUser.gender.equals(User.VALUE_GENDER_FEMALE))
                ? R.string.profile_information_user_gender_female
                : R.string.profile_information_user_gender_male;
        mUserGenderTextView.setText(genderResourceId);
        mUserBloodTypeTextView.setText(getString(R.string.profile_information_user_blood_type, mUser.bloodType));
        int isDonorResourceId = (!mUser.isDonor)
                ? R.string.profile_information_user_is_donor_false
                : R.string.profile_information_user_is_donor_true;
        mUserIsDonorTextView.setText(isDonorResourceId);
    }

    /**
     * Stops the animation of the fields.
     */
    void stopFieldsAnimation() {
        Drawable whiteBackground = getDrawable(android.R.color.white);
        mUserFullNameTextView.clearAnimation();
        mUserFullNameTextView.setBackground(whiteBackground);
        mUserProvinceTextView.clearAnimation();
        mUserProvinceTextView.setBackground(whiteBackground);
        mUserCountryTextView.clearAnimation();
        mUserCountryTextView.setBackground(whiteBackground);
        mUserAgeTextView.clearAnimation();
        mUserAgeTextView.setBackground(whiteBackground);
        mUserGenderTextView.clearAnimation();
        mUserGenderTextView.setBackground(whiteBackground);
        mUserBloodTypeTextView.clearAnimation();
        mUserBloodTypeTextView.setBackground(whiteBackground);
        mUserIsDonorTextView.clearAnimation();
        mUserIsDonorTextView.setBackground(whiteBackground);
    }
}
