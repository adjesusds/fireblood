package com.arieldiax.codelab.fireblood.ui.navigation.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.ArrayMap;
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
import com.arieldiax.codelab.fireblood.models.firebase.Hospital;
import com.arieldiax.codelab.fireblood.models.firebase.User;
import com.arieldiax.codelab.fireblood.ui.navigation.main.MainActivity;
import com.arieldiax.codelab.fireblood.utils.FormUtils;
import com.arieldiax.codelab.fireblood.utils.MapUtils;
import com.arieldiax.codelab.fireblood.utils.ViewUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
    Button mSearchForHospitalsButton;

    /**
     * Instance of the User class.
     */
    User mUser;

    /**
     * Instance of the GoogleMap class.
     */
    GoogleMap mGoogleMap;

    /**
     * Padding of the map.
     */
    int mMapPaddingLeft;
    int mMapPaddingTop;
    int mMapPaddingRight;
    int mMapPaddingBottom;

    /**
     * Camera update of the map.
     */
    CameraUpdate mMapCameraUpdate;

    /**
     * Duration of the animations.
     */
    long animationsDuration;

    /**
     * Whether or not the search for hospitals has finished.
     */
    boolean mHasFinishedSearchForHospitals;

    /**
     * Database path of the hospitals.
     */
    String mHospitalsDatabasePath;

    /**
     * Child event listener of the hospitals.
     */
    ChildEventListener mHospitalsChildEventListener;

    /**
     * Map of Marker instances.
     */
    ArrayMap<String, Marker> mMarkers;

    /**
     * Instance of the LatLngBounds.Builder class.
     */
    LatLngBounds.Builder mLatLngBoundsBuilder;

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
        mSearchForHospitalsButton = (Button) findViewById(R.id.search_for_hospitals_button);
    }

    @Override
    protected void init() {
        super.init();
        mClassCanonicalName = getClass().getCanonicalName();
        mUser = null;
        mGoogleMap = null;
        mMapPaddingLeft = 0;
        mMapPaddingTop = 0;
        mMapPaddingRight = 0;
        mMapPaddingBottom = 0;
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        int displayHeight = getResources().getDisplayMetrics().heightPixels;
        mMapCameraUpdate = CameraUpdateFactory.newLatLngBounds(MapUtils.sDominicanRepublicGeographicalBoundaries, displayWidth, displayHeight, 0);
        animationsDuration = DateUtils.SECOND_IN_MILLIS / 3;
        mHasFinishedSearchForHospitals = true;
        mHospitalsDatabasePath = "";
        mHospitalsChildEventListener = null;
        mMarkers = new ArrayMap<>();
        mLatLngBoundsBuilder = new LatLngBounds.Builder();
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
                        if (
                                transitionType != LayoutTransition.CHANGE_APPEARING &&
                                        transitionType != LayoutTransition.CHANGE_DISAPPEARING
                                ) {
                            return;
                        }
                        if (mHasFinishedSearchForHospitals) {
                            mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
                            mGoogleMap.clear();
                            mDatabaseReference
                                    .child(mHospitalsDatabasePath)
                                    .removeEventListener(mHospitalsChildEventListener)
                            ;
                        }
                        ValueAnimator translateAnimation = (mHasFinishedSearchForHospitals)
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
                                if (!mHasFinishedSearchForHospitals) {
                                    mToast.setText(R.string.message_waiting_for_hospitals);
                                    mToast.show();
                                }
                                mGoogleMap.animateCamera(mMapCameraUpdate, new GoogleMap.CancelableCallback() {

                                    @Override
                                    public void onFinish() {
                                        if (!mHasFinishedSearchForHospitals) {
                                            mDatabaseReference
                                                    .child(mHospitalsDatabasePath)
                                                    .addChildEventListener(mHospitalsChildEventListener)
                                            ;
                                        }
                                    }

                                    @Override
                                    public void onCancel() {
                                    }
                                });
                            }
                        });
                        translateAnimation.start();
                        int translationY = (!mHasFinishedSearchForHospitals)
                                ? mMapPaddingBottom - mMapPaddingLeft - mSearchForHospitalsButton.getPaddingTop() / 2
                                : 0;
                        mSearchForHospitalsButton
                                .animate()
                                .translationY(translationY)
                                .setDuration(animationsDuration)
                        ;
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
                mHospitalsDatabasePath = "";
                mMarkers.clear();
                mLatLngBoundsBuilder = new LatLngBounds.Builder();
                toggleSearchForHospitalsState();
            }
        });
        mSearchForHospitalsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mHospitalsDatabasePath = Hospital
                        .sDatabasePath
                        .replace(Hospital.PATH_SEGMENT_PROVINCE, FormUtils.getViewValue(SearchActivity.this, mProvinceSpinner))
                        .replace(Hospital.PATH_SEGMENT_BLOOD_TYPE, FormUtils.getViewValue(SearchActivity.this, mBloodTypeSpinner));
                toggleSearchForHospitalsState();
            }
        });
        mDatabaseReference
                .child(User.DATABASE_PATH)
                .child(mFirebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUser = dataSnapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                })
        ;
        mHospitalsChildEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(
                    DataSnapshot dataSnapshot,
                    String string
            ) {
                Hospital hospital = dataSnapshot.getValue(Hospital.class);
                mToast.cancel();
                mMapProgressBar.setVisibility(View.GONE);
                placeMarker(hospital);
            }

            @Override
            public void onChildChanged(
                    DataSnapshot dataSnapshot,
                    String string
            ) {
                Hospital hospital = dataSnapshot.getValue(Hospital.class);
                placeMarker(hospital);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(
                    DataSnapshot dataSnapshot,
                    String string
            ) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
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
        if (
                !mHasFinishedSearchForHospitals &&
                        !mMarkers.isEmpty()
                ) {
            animateMarkers();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mHasFinishedSearchForHospitals) {
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
                mGoogleMap.moveCamera(mMapCameraUpdate);
            }
        }, DateUtils.SECOND_IN_MILLIS / 2);
    }

    /**
     * Toggles the search for hospitals state.
     */
    void toggleSearchForHospitalsState() {
        mHasFinishedSearchForHospitals = !mHasFinishedSearchForHospitals;
        int visibility = (mHasFinishedSearchForHospitals)
                ? View.GONE
                : View.VISIBLE;
        boolean enabled = mHasFinishedSearchForHospitals;
        mBackImageView.setVisibility(visibility);
        mProvinceSpinner.setEnabled(enabled);
        mBloodTypeSpinner.setEnabled(enabled);
        mMapProgressBar.setVisibility(visibility);
        mSearchForHospitalsButton.setClickable(enabled);
    }

    /**
     * Places the marker.
     *
     * @param hospital Instance of the Hospital class.
     */
    void placeMarker(Hospital hospital) {
        if (
                mUser.isDonor &&
                        mUser.province.equals(FormUtils.getViewValue(this, mProvinceSpinner)) &&
                        mUser.bloodType.equals(FormUtils.getViewValue(this, mBloodTypeSpinner)) &&
                        mUser.hospital.getLocation().equals(hospital.getLocation())
                ) {
            hospital.donorsCount--;
        }
        LatLng hospitalLocation = hospital.getLocation();
        Marker marker = null;
        String markerKey = hospitalLocation.toString();
        if (mMarkers.containsKey(markerKey)) {
            marker = mMarkers.get(markerKey);
        } else {
            marker = mGoogleMap.addMarker(new MarkerOptions().position(hospitalLocation));
            mMarkers.put(markerKey, marker);
            mLatLngBoundsBuilder.include(hospitalLocation);
        }
        BitmapDescriptor markerIcon = (hospital.donorsCount > 0)
                ? BitmapDescriptorFactory.fromBitmap(ViewUtils.convertDrawableIntoBitmap(getDrawable(R.drawable.ic_whatshot_red_24dp)))
                : BitmapDescriptorFactory.fromBitmap(ViewUtils.convertDrawableIntoBitmap(getDrawable(R.drawable.ic_whatshot_black_24dp)));
        String markerTitle = hospital.name;
        String markerSnippet = getString(R.string.hospital_donors_snippet, hospital.donorsCount);
        marker.setIcon(markerIcon);
        marker.setTitle(markerTitle);
        marker.setSnippet(markerSnippet);
        if (marker.isInfoWindowShown()) {
            marker.showInfoWindow();
        }
        animateMarkers();
    }

    /**
     * Animates the markers.
     */
    void animateMarkers() {
        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBoundsBuilder.build(), 0), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            }

            @Override
            public void onCancel() {
            }
        });
    }
}
