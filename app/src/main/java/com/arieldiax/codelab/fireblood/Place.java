package com.arieldiax.codelab.fireblood;

import com.google.android.gms.maps.model.LatLng;

public class Place {

    /**
     * Name of the place.
     */
    private String mName;

    /**
     * Latitude of the place.
     */
    private double mLatitude;

    /**
     * Longitude of the place.
     */
    private double mLongitude;

    /**
     * Creates a new Place object.
     *
     * @param name      Name of the place.
     * @param latitude  Latitude of the place.
     * @param longitude Longitude of the place.
     */
    public Place(String name, double latitude, double longitude) {
        mName = name;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    /**
     * Gets the name of the place.
     *
     * @return The name of the place.
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets the latitude of the place.
     *
     * @return The latitude of the place.
     */
    public double getLatitude() {
        return mLatitude;
    }

    /**
     * Gets the longitude of the place.
     *
     * @return The longitude of the place.
     */
    public double getLongitude() {
        return mLongitude;
    }

    /**
     * Gets the location of the place.
     *
     * @return The location of the place.
     */
    public LatLng getLocation() {
        return new LatLng(mLatitude, mLongitude);
    }
}
