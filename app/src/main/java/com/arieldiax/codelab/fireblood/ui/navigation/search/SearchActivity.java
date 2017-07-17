package com.arieldiax.codelab.fireblood.ui.navigation.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.firebase.Donor;
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
    TextView mDonorsCountTextView;
    View mStartContainerView;
    ProgressBar mMapProgressBar;
    View mEndContainerView;
    RelativeLayout mBottomContainerRelativeLayout;
    Button mSearchForDonorsButton;
    Button mRequestBloodButton;

    /**
     * Instance of the User class.
     */
    User mUser;

    /**
     * Instance of the GoogleMap class.
     */
    GoogleMap mGoogleMap;

    /**
     * Camera update of the map.
     */
    CameraUpdate mMapCameraUpdate;

    /**
     * Duration of the animations.
     */
    long mAnimationsDuration;

    /**
     * Whether or not the search for hospitals, per province, per blood type has finished.
     */
    boolean mHasFinishedSearchForHospitalsPerProvincePerBloodType;

    /**
     * Count down timer of the toast.
     */
    CountDownTimer mToastCountDownTimer;

    /**
     * Whether or not the count down timer of the toast should be restored.
     */
    boolean mShouldRestoreToastCountDownTimer;

    /**
     * Database path of the donors count, per province, per blood type.
     */
    String mDonorsCountPerProvincePerBloodTypeDatabasePath;

    /**
     * Database path of the hospitals, per province, per blood type.
     */
    String mHospitalsPerProvincePerBloodTypeDatabasePath;

    /**
     * Value event listener of the user.
     */
    ValueEventListener mUserValueEventListener;

    /**
     * Value event listener of the donors count, per province, per blood type.
     */
    ValueEventListener mDonorsCountPerProvincePerBloodTypeValueEventListener;

    /**
     * Child event listener of the hospitals, per province, per blood type.
     */
    ChildEventListener mHospitalsPerProvincePerBloodTypeChildEventListener;

    /**
     * Map of Marker instances.
     */
    SparseArray<Marker> mMarkers;

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
        mDonorsCountTextView = (TextView) findViewById(R.id.donors_count_text_view);
        mStartContainerView = findViewById(R.id.start_container_view);
        mMapProgressBar = (ProgressBar) findViewById(R.id.map_progress_bar);
        mEndContainerView = findViewById(R.id.end_container_view);
        mBottomContainerRelativeLayout = (RelativeLayout) findViewById(R.id.bottom_container_relative_layout);
        mSearchForDonorsButton = (Button) findViewById(R.id.search_for_donors_button);
        mRequestBloodButton = (Button) findViewById(R.id.request_blood_button);
    }

    @Override
    protected void init() {
        super.init();
        mClassCanonicalName = getClass().getCanonicalName();
        mUser = null;
        mGoogleMap = null;
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        int displayHeight = getResources().getDisplayMetrics().heightPixels;
        mMapCameraUpdate = CameraUpdateFactory.newLatLngBounds(MapUtils.sDominicanRepublicGeographicalBoundaries, displayWidth, displayHeight, 0);
        mAnimationsDuration = DateUtils.SECOND_IN_MILLIS / 3;
        mHasFinishedSearchForHospitalsPerProvincePerBloodType = true;
        long toastOnScreenDuration = DateUtils.SECOND_IN_MILLIS * 3 + DateUtils.SECOND_IN_MILLIS / 2;
        mToastCountDownTimer = new CountDownTimer(toastOnScreenDuration + DateUtils.SECOND_IN_MILLIS, toastOnScreenDuration) {

            @Override
            public void onTick(long millisUntilFinished) {
                mToast.show();
            }

            @Override
            public void onFinish() {
                mToastCountDownTimer.start();
            }
        };
        mShouldRestoreToastCountDownTimer = false;
        mDonorsCountPerProvincePerBloodTypeDatabasePath = "";
        mHospitalsPerProvincePerBloodTypeDatabasePath = "";
        mUserValueEventListener = null;
        mDonorsCountPerProvincePerBloodTypeValueEventListener = null;
        mHospitalsPerProvincePerBloodTypeChildEventListener = null;
        mMarkers = new SparseArray<>();
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
                        int translationY = ViewUtils.convertDpIntoPx(SearchActivity.this, 60.00f);
                        if (!mHasFinishedSearchForHospitalsPerProvincePerBloodType) {
                            mSearchForDonorsButton
                                    .animate()
                                    .setListener(null)
                                    .translationY(translationY)
                                    .setDuration(mAnimationsDuration)
                                    .setListener(new AnimatorListenerAdapter() {

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            mToast.setText(R.string.message_waiting_for_donors);
                                            mToastCountDownTimer.start();
                                            mShouldRestoreToastCountDownTimer = true;
                                            mGoogleMap.setPadding(mStartContainerView.getWidth(), mTopContainerLinearLayout.getHeight(), mEndContainerView.getWidth(), mBottomContainerRelativeLayout.getHeight());
                                            mGoogleMap.animateCamera(mMapCameraUpdate, new GoogleMap.CancelableCallback() {

                                                @Override
                                                public void onFinish() {
                                                    mDatabaseReference
                                                            .child(mDonorsCountPerProvincePerBloodTypeDatabasePath)
                                                            .addValueEventListener(mDonorsCountPerProvincePerBloodTypeValueEventListener)
                                                    ;
                                                    mDatabaseReference
                                                            .child(mHospitalsPerProvincePerBloodTypeDatabasePath)
                                                            .addChildEventListener(mHospitalsPerProvincePerBloodTypeChildEventListener)
                                                    ;
                                                }

                                                @Override
                                                public void onCancel() {
                                                }
                                            });
                                        }
                                    })
                            ;
                        } else {
                            mToast.cancel();
                            mToastCountDownTimer.cancel();
                            mShouldRestoreToastCountDownTimer = false;
                            mRequestBloodButton
                                    .animate()
                                    .setListener(null)
                                    .translationY(translationY)
                                    .setDuration(mAnimationsDuration)
                                    .setListener(new AnimatorListenerAdapter() {

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            mDatabaseReference
                                                    .child(mDonorsCountPerProvincePerBloodTypeDatabasePath)
                                                    .removeEventListener(mDonorsCountPerProvincePerBloodTypeValueEventListener)
                                            ;
                                            mDatabaseReference
                                                    .child(mHospitalsPerProvincePerBloodTypeDatabasePath)
                                                    .removeEventListener(mHospitalsPerProvincePerBloodTypeChildEventListener)
                                            ;
                                            mDonorsCountTextView.setText(getString(R.string.donor_information_donors, 0));
                                            mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
                                            mGoogleMap.clear();
                                            mGoogleMap.animateCamera(mMapCameraUpdate, new GoogleMap.CancelableCallback() {

                                                @Override
                                                public void onFinish() {
                                                    mSearchForDonorsButton
                                                            .animate()
                                                            .setListener(null)
                                                            .translationY(0)
                                                            .setDuration(mAnimationsDuration)
                                                            .setListener(new AnimatorListenerAdapter() {

                                                                @Override
                                                                public void onAnimationEnd(Animator animation) {
                                                                    super.onAnimationEnd(animation);
                                                                    mGoogleMap.setPadding(mStartContainerView.getWidth(), mTopContainerLinearLayout.getHeight(), mEndContainerView.getWidth(), mBottomContainerRelativeLayout.getHeight());
                                                                    mGoogleMap.animateCamera(mMapCameraUpdate);
                                                                }
                                                            })
                                                    ;
                                                }

                                                @Override
                                                public void onCancel() {
                                                }
                                            });
                                        }
                                    })
                            ;
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
                mHospitalsPerProvincePerBloodTypeDatabasePath = "";
                mMarkers.clear();
                mLatLngBoundsBuilder = new LatLngBounds.Builder();
                toggleActivityInteractionsState();
            }
        });
        mSearchForDonorsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String province = FormUtils.getViewValue(SearchActivity.this, mProvinceSpinner);
                String bloodType = FormUtils.getViewValue(SearchActivity.this, mBloodTypeSpinner);
                mDonorsCountPerProvincePerBloodTypeDatabasePath = Donor
                        .sDatabasePathCountPerProvincePerBloodType
                        .replace(Donor.PATH_SEGMENT_PROVINCE, province)
                        .replace(Donor.PATH_SEGMENT_BLOOD_TYPE, bloodType);
                mHospitalsPerProvincePerBloodTypeDatabasePath = Hospital
                        .sDatabasePathPerProvincePerBloodType
                        .replace(Hospital.PATH_SEGMENT_PROVINCE, province)
                        .replace(Hospital.PATH_SEGMENT_BLOOD_TYPE, bloodType);
                toggleActivityInteractionsState();
            }
        });
        mRequestBloodButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mRequestBloodButton.setClickable(false);
                RequestBloodDialogFragment requestBloodDialogFragment = new RequestBloodDialogFragment();
                Bundle arguments = new Bundle();
                arguments.putString(RequestBloodDialogFragment.PROP_IN_PROVINCE, FormUtils.getViewValue(SearchActivity.this, mProvinceSpinner));
                arguments.putString(RequestBloodDialogFragment.PROP_IN_BLOOD_TYPE, FormUtils.getViewValue(SearchActivity.this, mBloodTypeSpinner));
                requestBloodDialogFragment.setArguments(arguments);
                requestBloodDialogFragment.show(getSupportFragmentManager(), requestBloodDialogFragment.getTag());
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mRequestBloodButton.setClickable(true);
                    }
                }, mAnimationsDuration);
            }
        });
        mUserValueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabaseReference
                .child(User.DATABASE_PATH)
                .child(mFirebaseUser.getUid())
                .addValueEventListener(mUserValueEventListener)
        ;
        mDonorsCountPerProvincePerBloodTypeValueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object donorsCountObject = dataSnapshot.getValue();
                if (donorsCountObject == null) {
                    return;
                }
                int donorsCountValue = Integer.parseInt(donorsCountObject.toString());
                String donorsCountText = (
                        !mUser.isDonor ||
                                !mUser.province.equals(FormUtils.getViewValue(SearchActivity.this, mProvinceSpinner)) ||
                                !mUser.bloodType.equals(FormUtils.getViewValue(SearchActivity.this, mBloodTypeSpinner))
                )
                        ? getString(R.string.donor_information_donors, donorsCountValue)
                        : getString(R.string.donor_information_donors_including_you, donorsCountValue);
                mDonorsCountTextView.setText(donorsCountText);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mHospitalsPerProvincePerBloodTypeChildEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(
                    DataSnapshot dataSnapshot,
                    String string
            ) {
                Hospital hospital = dataSnapshot.getValue(Hospital.class);
                mToast.cancel();
                mToastCountDownTimer.cancel();
                mShouldRestoreToastCountDownTimer = false;
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
        mDonorsCountTextView.setText(getString(R.string.donor_information_donors, 0));
    }

    @Override
    protected void onNavigationItemReselectedListener() {
        super.onNavigationItemReselectedListener();
        if (
                !mHasFinishedSearchForHospitalsPerProvincePerBloodType &&
                        mMarkers.size() > 0
                ) {
            animateMarkers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mShouldRestoreToastCountDownTimer) {
            mToastCountDownTimer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mShouldRestoreToastCountDownTimer) {
            mToastCountDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseReference
                .child(User.DATABASE_PATH)
                .child(mFirebaseUser.getUid())
                .removeEventListener(mUserValueEventListener)
        ;
        mDatabaseReference
                .child(mDonorsCountPerProvincePerBloodTypeDatabasePath)
                .removeEventListener(mDonorsCountPerProvincePerBloodTypeValueEventListener)
        ;
        mDatabaseReference
                .child(mHospitalsPerProvincePerBloodTypeDatabasePath)
                .removeEventListener(mHospitalsPerProvincePerBloodTypeChildEventListener)
        ;
    }

    @Override
    public void onBackPressed() {
        if (!mHasFinishedSearchForHospitalsPerProvincePerBloodType) {
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
                mGoogleMap.setPadding(mStartContainerView.getWidth(), mTopContainerLinearLayout.getHeight(), mEndContainerView.getWidth(), mBottomContainerRelativeLayout.getHeight());
                mGoogleMap.moveCamera(mMapCameraUpdate);
            }
        }, DateUtils.SECOND_IN_MILLIS / 2);
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                DonorsBottomSheetDialogFragment donorsBottomSheetDialogFragment = new DonorsBottomSheetDialogFragment();
                Bundle arguments = new Bundle();
                arguments.putString(DonorsBottomSheetDialogFragment.PROP_IN_HOSPITAL_NAME, marker.getTitle());
                arguments.putString(DonorsBottomSheetDialogFragment.PROP_IN_PROVINCE, FormUtils.getViewValue(SearchActivity.this, mProvinceSpinner));
                arguments.putString(DonorsBottomSheetDialogFragment.PROP_IN_BLOOD_TYPE, FormUtils.getViewValue(SearchActivity.this, mBloodTypeSpinner));
                arguments.putString(DonorsBottomSheetDialogFragment.PROP_IN_LOCATION, marker.getPosition().latitude + "_" + marker.getPosition().longitude);
                donorsBottomSheetDialogFragment.setArguments(arguments);
                donorsBottomSheetDialogFragment.show(getSupportFragmentManager(), donorsBottomSheetDialogFragment.getTag());
            }
        });
    }

    /**
     * Toggles the activity interactions state.
     */
    void toggleActivityInteractionsState() {
        mHasFinishedSearchForHospitalsPerProvincePerBloodType = !mHasFinishedSearchForHospitalsPerProvincePerBloodType;
        int visibility = (mHasFinishedSearchForHospitalsPerProvincePerBloodType)
                ? View.GONE
                : View.VISIBLE;
        boolean enabled = mHasFinishedSearchForHospitalsPerProvincePerBloodType;
        mBackImageView.setVisibility(visibility);
        mProvinceSpinner.setEnabled(enabled);
        mBloodTypeSpinner.setEnabled(enabled);
        mDonorsCountTextView.setVisibility(visibility);
        mMapProgressBar.setVisibility(visibility);
        mSearchForDonorsButton.setClickable(enabled);
        mRequestBloodButton.setClickable(!enabled);
    }

    /**
     * Places the marker.
     *
     * @param hospital Instance of the Hospital class.
     */
    void placeMarker(Hospital hospital) {
        LatLng hospitalLocation = hospital.getLocation();
        Marker marker = null;
        int markerKey = hospitalLocation.hashCode();
        if (mMarkers.indexOfKey(markerKey) > -1) {
            marker = mMarkers.get(markerKey);
        } else {
            marker = mGoogleMap.addMarker(new MarkerOptions().position(hospitalLocation));
            mMarkers.put(markerKey, marker);
            mLatLngBoundsBuilder.include(hospitalLocation);
        }
        BitmapDescriptor markerIcon = (hospital.donorsCount > 0)
                ? BitmapDescriptorFactory.fromBitmap(ViewUtils.convertDrawableIntoBitmap(getDrawable(R.drawable.ic_place_red_24dp)))
                : BitmapDescriptorFactory.fromBitmap(ViewUtils.convertDrawableIntoBitmap(getDrawable(R.drawable.ic_place_black_24dp)));
        String markerTitle = hospital.name;
        String markerSnippet = (
                !mUser.isDonor ||
                        !mUser.province.equals(FormUtils.getViewValue(this, mProvinceSpinner)) ||
                        !mUser.bloodType.equals(FormUtils.getViewValue(this, mBloodTypeSpinner)) ||
                        !mUser.hospital.getLocation().equals(hospitalLocation)
        )
                ? getString(R.string.donor_information_donors, hospital.donorsCount)
                : getString(R.string.donor_information_donors_including_you, hospital.donorsCount);
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
                if (mRequestBloodButton.getTranslationY() > 0) {
                    mRequestBloodButton
                            .animate()
                            .setListener(null)
                            .translationY(0)
                            .setDuration(mAnimationsDuration)
                    ;
                }
            }

            @Override
            public void onCancel() {
            }
        });
    }
}
