package com.arieldiax.codelab.fireblood.models.firebase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Donor {

    /**
     * Path segments for donors.
     */
    public static final String PATH_SEGMENT_PROVINCE = ":province";
    public static final String PATH_SEGMENT_BLOOD_TYPE = ":bloodType";
    public static final String PATH_SEGMENT_LOCATION = ":location";
    private static final String PATH_SEGMENT_DONORS_COUNT = "donorsCount";

    /**
     * Database path of the donors count, per province, per blood type.
     */
    public static String sDatabasePathCountPerProvincePerBloodType = "/donorsPerProvincePerBloodType/" + PATH_SEGMENT_PROVINCE + "/" + PATH_SEGMENT_BLOOD_TYPE + "/" + PATH_SEGMENT_DONORS_COUNT;

    /**
     * Database path of the donors, per province, per blood type, per hospital.
     */
    public static String sDatabasePathPerProvincePerBloodTypePerHospital = "/donorsPerProvincePerBloodTypePerHospital/" + PATH_SEGMENT_PROVINCE + "/" + PATH_SEGMENT_BLOOD_TYPE + "/" + PATH_SEGMENT_LOCATION;

    /**
     * Fields of the donor.
     */
    public String username;
    public String photoUrl;
    public String firstName;
    public String lastName;
    public String fullName;
    public long createdAt;

    /**
     * Creates a new Donor object.
     */
    public Donor() {
        // Required empty public constructor (to allow instantiation).
    }
}
