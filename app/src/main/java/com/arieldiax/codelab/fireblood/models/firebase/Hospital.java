package com.arieldiax.codelab.fireblood.models.firebase;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Hospital {

    /**
     * Path segments for hospitals.
     */
    public static final String PATH_SEGMENT_PROVINCE = ":province";
    public static final String PATH_SEGMENT_BLOOD_TYPE = ":bloodType";

    /**
     * Database path of the hospitals, per province, per blood type.
     */
    public static String sDatabasePathPerProvincePerBloodType = "/hospitalsPerProvincePerBloodType/" + PATH_SEGMENT_PROVINCE + "/" + PATH_SEGMENT_BLOOD_TYPE;

    /**
     * Fields of the hospital.
     */
    public String name;
    public double latitude;
    public double longitude;
    public int donorsCount;

    /**
     * Creates a new Hospital object.
     */
    public Hospital() {
        // Required empty public constructor (to allow instantiation).
    }

    @Exclude
    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }
}
