package com.arieldiax.codelab.fireblood.ui;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.utils.MapUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MapStyleOptions;

public class SearchActivity extends MainActivity implements OnMapReadyCallback {

    /**
     * Instance of the GoogleMap class.
     */
    GoogleMap mGoogleMap;

    /**
     * Instance of the MapFragment class.
     */
    MapFragment mMapFragment;

    /**
     * Camera update of the Dominican Republic.
     */
    CameraUpdate mDominicanRepublicCameraUpdate;

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
    protected void init() {
        super.init();
        mClassCanonicalName = getClass().getCanonicalName();
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        int displayHeight = getResources().getDisplayMetrics().heightPixels;
        mDominicanRepublicCameraUpdate = CameraUpdateFactory.newLatLngBounds(MapUtils.sDominicanRepublicGeographicalBoundaries, displayWidth, displayHeight, 0);
    }

    @Override
    protected void updateUi() {
        super.updateUi();
        mMainBottomNavigationView.setSelectedItemId(R.id.search_navigation_item);
        mMapFragment.getMapAsync(this);
    }

    @Override
    protected void onNavigationItemReselectedListener() {
        super.onNavigationItemReselectedListener();
        mGoogleMap.animateCamera(mDominicanRepublicCameraUpdate);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.google_maps_style));
        mGoogleMap.moveCamera(mDominicanRepublicCameraUpdate);
        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
    }
}
