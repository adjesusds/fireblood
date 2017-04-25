package com.arieldiax.codelab.fireblood.models.pojos;

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
     */
    public Place() {
    }

    /**
     * Sets the name of the place.
     *
     * @param name Name of the place.
     * @return The instance of the Place class.
     */
    public Place setName(String name) {
        mName = name;
        return this;
    }

    /**
     * Sets the latitude of the place.
     *
     * @param latitude Latitude of the place.
     * @return The instance of the Place class.
     */
    public Place setLatitude(double latitude) {
        mLatitude = latitude;
        return this;
    }

    /**
     * Sets the longitude of the place.
     *
     * @param longitude Longitude of the place.
     * @return The instance of the Place class.
     */
    public Place setLongitude(double longitude) {
        mLongitude = longitude;
        return this;
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
     * Gets the location of the place.
     *
     * @return The location of the place.
     */
    public LatLng getLocation() {
        return new LatLng(mLatitude, mLongitude);
    }
}
