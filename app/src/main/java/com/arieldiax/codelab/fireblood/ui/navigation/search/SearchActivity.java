package com.arieldiax.codelab.fireblood.ui.navigation.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.ui.navigation.main.MainActivity;
import com.arieldiax.codelab.fireblood.utils.MapUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends MainActivity implements OnMapReadyCallback {

    /**
     * Views of the activity.
     */
    MapFragment mMapFragment;
    LinearLayout mTopContainerLinearLayout;
    LinearLayout mCardContainerLinearLayout;
    ImageView mBackImageView;
    Spinner mProvinceSpinner;
    Spinner mBloodTypeSpinner;
    View mStartContainerView;
    ProgressBar mMapProgressBar;
    View mEndContainerView;
    LinearLayout mBottomContainerLinearLayout;
    Button mSearchForDonorsButton;

    /**
     * Instance of the GoogleMap class.
     */
    GoogleMap mGoogleMap;

    /**
     * Camera update of the Dominican Republic.
     */
    CameraUpdate mDominicanRepublicCameraUpdate;

    /**
     * Whether or not the search for blood has finished.
     */
    boolean mHasFinishedSearchBlood;

    /**
     * Padding of the map.
     */
    int mMapPaddingLeft;
    int mMapPaddingTop;
    int mMapPaddingRight;
    int mMapPaddingBottom;

    /**
     * Duration of the animations.
     */
    long animationsDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.fragment_search, (FrameLayout) findViewById(R.id.main_frame_layout));
        initUi();
        init();
        initListeners();
        updateUi();
    }

    @Override
    protected void initUi() {
        super.initUi();
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mTopContainerLinearLayout = (LinearLayout) findViewById(R.id.top_container_linear_layout);
        mCardContainerLinearLayout = (LinearLayout) findViewById(R.id.card_container_linear_layout);
        mBackImageView = (ImageView) findViewById(R.id.back_image_view);
        mProvinceSpinner = (Spinner) findViewById(R.id.province_spinner);
        mBloodTypeSpinner = (Spinner) findViewById(R.id.blood_type_spinner);
        mStartContainerView = findViewById(R.id.start_container_view);
        mMapProgressBar = (ProgressBar) findViewById(R.id.map_progress_bar);
        mEndContainerView = findViewById(R.id.end_container_view);
        mBottomContainerLinearLayout = (LinearLayout) findViewById(R.id.bottom_container_linear_layout);
        mSearchForDonorsButton = (Button) findViewById(R.id.search_for_donors_button);
    }

    @Override
    protected void init() {
        super.init();
        mClassCanonicalName = getClass().getCanonicalName();
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        int displayHeight = getResources().getDisplayMetrics().heightPixels;
        mDominicanRepublicCameraUpdate = CameraUpdateFactory.newLatLngBounds(MapUtils.sDominicanRepublicGeographicalBoundaries, displayWidth, displayHeight, 0);
        mHasFinishedSearchBlood = true;
        animationsDuration = DateUtils.SECOND_IN_MILLIS / 3;
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        mCardContainerLinearLayout
                .getLayoutTransition()
                .addTransitionListener(new LayoutTransition.TransitionListener() {

                    @Override
                    public void startTransition(
                            LayoutTransition transition,
                            ViewGroup container,
                            View view,
                            int transitionType
                    ) {
                        switch (transitionType) {
                            case LayoutTransition.CHANGE_APPEARING:
                            case LayoutTransition.CHANGE_DISAPPEARING:
                                int translationY = (transitionType == LayoutTransition.CHANGE_APPEARING)
                                        ? mMapPaddingBottom - mMapPaddingLeft - mSearchForDonorsButton.getPaddingTop() / 2
                                        : 0;
                                mSearchForDonorsButton
                                        .animate()
                                        .translationY(translationY)
                                        .setDuration(animationsDuration)
                                ;
                                ValueAnimator translateAnimation = (mHasFinishedSearchBlood)
                                        ? ValueAnimator.ofInt(mMapPaddingLeft, mMapPaddingBottom)
                                        : ValueAnimator.ofInt(mMapPaddingBottom, mMapPaddingLeft);
                                translateAnimation.setDuration(animationsDuration);
                                translateAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        mGoogleMap.setPadding(mMapPaddingLeft, mMapPaddingTop, mMapPaddingRight, Integer.parseInt(animation.getAnimatedValue().toString()));
                                    }
                                });
                                translateAnimation.addListener(new AnimatorListenerAdapter() {

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mGoogleMap.animateCamera(mDominicanRepublicCameraUpdate);
                                    }
                                });
                                translateAnimation.start();
                                break;
                        }
                    }

                    @Override
                    public void endTransition(
                            LayoutTransition transition,
                            ViewGroup container,
                            View view,
                            int transitionType
                    ) {
                    }
                })
        ;
        mBackImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toggleActivitySearchBloodState();
            }
        });
        mSearchForDonorsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toggleActivitySearchBloodState();
            }
        });
    }

    @Override
    protected void updateUi() {
        super.updateUi();
        mMainBottomNavigationView.setSelectedItemId(R.id.search_navigation_item);
        mMapFragment.getMapAsync(this);
        String[] provinces = getResources().getStringArray(R.array.array_provinces);
        ArrayAdapter<CharSequence> provinceArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<CharSequence>(Arrays.asList(provinces)));
        provinceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceArrayAdapter.remove(getString(R.string.placeholder_select_an_option));
        provinceArrayAdapter.notifyDataSetChanged();
        mProvinceSpinner.setAdapter(provinceArrayAdapter);
        String[] bloodTypes = getResources().getStringArray(R.array.array_blood_types);
        ArrayAdapter<CharSequence> bloodTypeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<CharSequence>(Arrays.asList(bloodTypes)));
        bloodTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeArrayAdapter.remove(getString(R.string.placeholder_select_an_option));
        bloodTypeArrayAdapter.notifyDataSetChanged();
        mBloodTypeSpinner.setAdapter(bloodTypeArrayAdapter);
    }

    @Override
    protected void onNavigationItemReselectedListener() {
        super.onNavigationItemReselectedListener();
        if (!mHasFinishedSearchBlood) {
            mBackImageView.performClick();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mHasFinishedSearchBlood) {
            mBackImageView.performClick();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.google_maps_style));
        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mMapPaddingLeft = mStartContainerView.getWidth();
                mMapPaddingTop = mTopContainerLinearLayout.getHeight();
                mMapPaddingRight = mEndContainerView.getWidth();
                mMapPaddingBottom = mBottomContainerLinearLayout.getHeight();
                mGoogleMap.setPadding(mMapPaddingLeft, mMapPaddingTop, mMapPaddingRight, mMapPaddingBottom);
                mGoogleMap.moveCamera(mDominicanRepublicCameraUpdate);
            }
        }, DateUtils.SECOND_IN_MILLIS / 2);
    }

    /**
     * Toggles the activity search for blood state.
     */
    void toggleActivitySearchBloodState() {
        mHasFinishedSearchBlood = !mHasFinishedSearchBlood;
        int visibility = (mHasFinishedSearchBlood)
                ? View.GONE
                : View.VISIBLE;
        boolean enabled = mHasFinishedSearchBlood;
        mBackImageView.setVisibility(visibility);
        mProvinceSpinner.setEnabled(enabled);
        mBloodTypeSpinner.setEnabled(enabled);
        mMapProgressBar.setVisibility(visibility);
        mSearchForDonorsButton.setClickable(enabled);
    }
}
