package com.arieldiax.codelab.fireblood;

import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
     * Name of the province.
     */
    private String mProvinceName;

    /**
     * Width of the display.
     */
    private int mDisplayWidth;

    /**
     * Height of the display.
     */
    private int mDisplayHeight;

    /**
     * Instance of the GoogleMap class.
     */
    private GoogleMap mGoogleMap;

    /**
     * Instance of the LoaderManager class.
     */
    private LoaderManager mLoaderManager;

    /**
     * Instance of the Marker class.
     */
    private Marker mMarker;

    /**
     * Instance of the BottomSheetDialog class.
     */
    private BottomSheetDialog mBottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_place_picker);
        init();
    }

    /**
     * Initializes the back end logic bindings.
     */
    private void init() {
        mProvinceName = getIntent().getExtras().getString("province_name");
        mDisplayWidth = getResources().getDisplayMetrics().widthPixels;
        mDisplayHeight = getResources().getDisplayMetrics().heightPixels;
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_place_picker);
        supportMapFragment.getMapAsync(this);
        mLoaderManager = getLoaderManager();
        View.OnClickListener positiveButtonOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("hospital_name", mMarker.getTitle());
                resultIntent.putExtra("hospital_latitude", mMarker.getPosition().latitude);
                resultIntent.putExtra("hospital_longitude", mMarker.getPosition().longitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        };
        DialogInterface.OnDismissListener negativeButtonOnClickListener = new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                mMarker.hideInfoWindow();
                mMarker = null;
                setGoogleMapGestures();
            }
        };
        mBottomSheetDialog = ViewUtils.buildBottomSheetDialog(this, R.string.title_select_hospital, R.string.message_are_you_sure, positiveButtonOnClickListener, negativeButtonOnClickListener);
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
        Handler handler = new Handler();
        int delay = 500;
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mBottomSheetDialog.show();
                mMarker.showInfoWindow();
            }
        }, delay);
        return false;
    }

    @Override
    public Loader<List<Place>> onCreateLoader(int id, Bundle args) {
        return new PlaceAsyncTaskLoader(this, getString(R.string.configuration_google_places_search_query, mProvinceName));
    }

    @Override
    public void onLoadFinished(Loader<List<Place>> loader, final List<Place> places) {
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        for (Place place : places) {
            latLngBoundsBuilder.include(place.getLocation());
        }
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), mDisplayWidth, mDisplayHeight, GOOGLE_MAPS_BOUNDARIES_PADDING), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                Handler handler = new Handler();
                int delay = 0;
                for (final Place place : places) {
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mGoogleMap.addMarker(new MarkerOptions().position(place.getLocation()).title(place.getName()));
                        }
                    }, delay);
                    delay += 100;
                }
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mGoogleMap.setOnMarkerClickListener(PlacePickerActivity.this);
                        setGoogleMapGestures();
                    }
                }, delay);
            }

            @Override
            public void onCancel() {
                mGoogleMap.stopAnimation();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<Place>> loader) {
    }

    /**
     * Sets the Google map gestures.
     */
    private void setGoogleMapGestures() {
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
    }
}
