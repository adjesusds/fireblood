package com.arieldiax.codelab.fireblood.ui.navigation.profile;

import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.ui.navigation.main.MainActivity;
import com.arieldiax.codelab.fireblood.utils.AnimationUtils;

public class ProfileActivity extends MainActivity {

    /**
     * Views of the activity.
     */
    ImageView mUserPhotoImageView;
    TextView mUserFullNameTextView;
    TextView mUserProvinceTextView;
    TextView mUserCountryTextView;
    TextView mUserAgeTextView;
    TextView mUserGenderTextView;
    TextView mUserBloodTypeTextView;
    TextView mUserIsDonorTextView;

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
        mUserPhotoImageView = (ImageView) findViewById(R.id.user_photo_image_view);
        mUserFullNameTextView = (TextView) findViewById(R.id.user_full_name_text_view);
        mUserProvinceTextView = (TextView) findViewById(R.id.user_province_text_view);
        mUserCountryTextView = (TextView) findViewById(R.id.user_country_text_view);
        mUserAgeTextView = (TextView) findViewById(R.id.user_age_text_view);
        mUserGenderTextView = (TextView) findViewById(R.id.user_gender_text_view);
        mUserBloodTypeTextView = (TextView) findViewById(R.id.user_blood_type_text_view);
        mUserIsDonorTextView = (TextView) findViewById(R.id.user_is_donor_text_view);
    }

    @Override
    protected void init() {
        super.init();
        mClassCanonicalName = getClass().getCanonicalName();
    }

    @Override
    protected void updateUi() {
        super.updateUi();
        mMainBottomNavigationView.setSelectedItemId(R.id.profile_navigation_item);
        mUserPhotoImageView.setClipToOutline(true);
        Animation blinkAnimation = AnimationUtils.getBlinkAnimation();
        mUserFullNameTextView.startAnimation(blinkAnimation);
        mUserProvinceTextView.startAnimation(blinkAnimation);
        mUserCountryTextView.startAnimation(blinkAnimation);
        mUserAgeTextView.startAnimation(blinkAnimation);
        mUserGenderTextView.startAnimation(blinkAnimation);
        mUserBloodTypeTextView.startAnimation(blinkAnimation);
        mUserIsDonorTextView.startAnimation(blinkAnimation);
    }
}
