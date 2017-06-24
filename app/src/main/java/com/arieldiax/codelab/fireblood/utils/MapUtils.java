package com.arieldiax.codelab.fireblood.utils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public final class MapUtils {

    /**
     * Geographical boundaries of the Dominican Republic.
     */
    public static final LatLngBounds sDominicanRepublicGeographicalBoundaries = new LatLngBounds(new LatLng(17.361100, -72.007510), new LatLng(19.978699, -68.252600));

    /**
     * Creates a new MapUtils object (no, it won't).
     */
    private MapUtils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Sets the Google map gestures.
     *
     * @param googleMap Instance of the GoogleMap class.
     */
    public static void setGoogleMapGestures(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
    }
}
