package com.arieldiax.codelab.fireblood.ui;

import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.pojos.Place;
import com.arieldiax.codelab.fireblood.models.widgets.ConfirmBottomSheetDialog;
import com.arieldiax.codelab.fireblood.services.PlaceAsyncTaskLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class PlacePickerActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LoaderManager.LoaderCallbacks<List<Place>> {

    /**
     * Geographical boundaries of the Dominican Republic.
     */
    private static final LatLngBounds DOMINICAN_REPUBLIC_GEOGRAPHICAL_BOUNDARIES = new LatLngBounds(new LatLng(17.361100, -72.007510), new LatLng(19.978699, -68.252600));

    /**
     * Boundaries padding for Google Maps.
     */
    private static final int GOOGLE_MAPS_BOUNDARIES_PADDING = 200;

    /**
     * Views of the activity.
     */
    ProgressBar mPlacePickerProgressBar;

    /**
     * Instance of the GoogleMap class.
     */
    GoogleMap mGoogleMap;

    /**
     * Instance of the MapFragment class.
     */
    MapFragment mMapFragment;

    /**
     * Instance of the LoaderManager class.
     */
    LoaderManager mLoaderManager;

    /**
     * Instance of the Marker class.
     */
    Marker mMarker;

    /**
     * Instance of the ConfirmBottomSheetDialog class.
     */
    ConfirmBottomSheetDialog mConfirmBottomSheetDialog;

    /**
     * Name of the province.
     */
    String mProvinceName;

    /**
     * Width of the display.
     */
    int mDisplayWidth;

    /**
     * Height of the display.
     */
    int mDisplayHeight;

    /**
     * Whether or not the markers have been added.
     */
    boolean mMarkersHaveBeenAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        initUi();
        init();
        updateUi();
    }

    /**
     * Initializes the user interface view bindings.
     */
    void initUi() {
        mPlacePickerProgressBar = (ProgressBar) findViewById(R.id.place_picker_progress_bar);
    }

    /**
     * Initializes the back end logic bindings.
     */
    void init() {
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.place_picker_fragment);
        mLoaderManager = getLoaderManager();
        mConfirmBottomSheetDialog = new ConfirmBottomSheetDialog(this);
        mProvinceName = getIntent().getExtras().getString("province_name");
        mDisplayWidth = getResources().getDisplayMetrics().widthPixels;
        mDisplayHeight = getResources().getDisplayMetrics().heightPixels;
        mMarkersHaveBeenAdded = false;
    }

    /**
     * Updates the user interface view bindings.
     */
    void updateUi() {
        mMapFragment.getMapAsync(this);
        View.OnClickListener positiveButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("hospital_name", mMarker.getTitle());
                resultIntent.putExtra("hospital_latitude", mMarker.getPosition().latitude);
                resultIntent.putExtra("hospital_longitude", mMarker.getPosition().longitude);
                setResult(RESULT_OK, resultIntent);
                mConfirmBottomSheetDialog.dismiss();
                finish();
            }
        };
        DialogInterface.OnDismissListener negativeButtonListener = new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                mMarker.hideInfoWindow();
                mMarker = null;
                setGoogleMapGestures();
            }
        };
        mConfirmBottomSheetDialog
                .setTitle(R.string.title_select_hospital)
                .setMessage(R.string.message_are_you_sure)
                .setPositiveButtonListener(positiveButtonListener)
                .setNegativeButtonListener(negativeButtonListener)
        ;
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("message_resource_id", R.string.message_action_canceled);
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.google_maps_style));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(DOMINICAN_REPUBLIC_GEOGRAPHICAL_BOUNDARIES, mDisplayWidth, mDisplayHeight, 0));
        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
        mLoaderManager.initLoader(0, null, this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMarker = marker;
        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mConfirmBottomSheetDialog.show();
                mMarker.showInfoWindow();
            }
        }, DateUtils.SECOND_IN_MILLIS / 2);
        return false;
    }

    @Override
    public Loader<List<Place>> onCreateLoader(
            int id,
            Bundle args
    ) {
        return new PlaceAsyncTaskLoader(this, getString(R.string.configuration_google_places_search_query, mProvinceName));
    }

    @Override
    public void onLoadFinished(
            Loader<List<Place>> loader,
            final List<Place> places
    ) {
        if (places == null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("message_resource_id", R.string.message_please_check_your_internet_connection);
            setResult(RESULT_CANCELED, resultIntent);
            finish();
            return;
        }
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        for (Place place : places) {
            latLngBoundsBuilder.include(place.getLocation());
        }
        mPlacePickerProgressBar.setVisibility(View.GONE);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), mDisplayWidth, mDisplayHeight, GOOGLE_MAPS_BOUNDARIES_PADDING), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                Handler handler = new Handler();
                int delay = 0;
                if (!mMarkersHaveBeenAdded) {
                    for (final Place place : places) {
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                mGoogleMap.addMarker(new MarkerOptions().position(place.getLocation()).title(place.getName()));
                            }
                        }, delay);
                        delay += DateUtils.SECOND_IN_MILLIS / 10;
                    }
                    mGoogleMap.setOnMarkerClickListener(PlacePickerActivity.this);
                    mMarkersHaveBeenAdded = true;
                }
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        setGoogleMapGestures();
                    }
                }, delay);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<Place>> loader) {
    }

    /**
     * Sets the Google map gestures.
     */
    void setGoogleMapGestures() {
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
    }
}
