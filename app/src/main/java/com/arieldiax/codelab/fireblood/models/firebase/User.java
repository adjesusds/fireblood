package com.arieldiax.codelab.fireblood.models.firebase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class User {

    /**
     * Child path of a user.
     */
    public static final String CHILD_PATH = "/users";

    /**
     * Path segments for user profile photo.
     */
    public static final String PATH_SEGMENT_USER_UID = ":userUid";
    public static final String PATH_SEGMENT_UNIX_TIME = ":unixTime";

    /**
     * Storage path of a user profile photo.
     */
    public static String sStoragePathProfilePhoto = "/images/" + CHILD_PATH + "/" + PATH_SEGMENT_USER_UID + "/profilePhotos/" + PATH_SEGMENT_UNIX_TIME + "/original";

    /**
     * Properties of a user.
     */
    public static final String PROPERTY_EMAIL = "email";
    public static final String PROPERTY_USERNAME = "username";
    public static final String PROPERTY_PHOTO_URL = "photoUrl";
    public static final String PROPERTY_FIRST_NAME = "firstName";
    public static final String PROPERTY_LAST_NAME = "lastName";
    public static final String PROPERTY_FULL_NAME = "fullName";
    public static final String PROPERTY_PHONE = "phone";
    public static final String PROPERTY_GENDER = "gender";
    public static final String PROPERTY_BIRTHDAY = "birthday";
    public static final String PROPERTY_COUNTRY = "country";
    public static final String PROPERTY_PROVINCE = "province";
    public static final String PROPERTY_HOSPITAL = "hospital";
    public static final String PROPERTY_BLOOD_TYPE = "bloodType";
    public static final String PROPERTY_IS_DONOR = "isDonor";
    public static final String PROPERTY_CREATED_AT = "createdAt";
    public static final String PROPERTY_UPDATED_AT = "updatedAt";
    public static final String PROPERTY_DELETED_AT = "deletedAt";

    /**
     * Values of a gender.
     */
    public static final String VALUE_GENDER_FEMALE = "female";
    public static final String VALUE_GENDER_MALE = "male";

    /**
     * Values of a country.
     */
    public static final String VALUE_COUNTRY_REPUBLICA_DOMINICANA = "República Dominicana";

    /**
     * Fields of the user.
     */
    public String email;
    public String username;
    public String photoUrl;
    public String firstName;
    public String lastName;
    public String fullName;
    public long phone;
    public String gender;
    public long birthday;
    public String country;
    public String province;
    public Hospital hospital;
    public String bloodType;
    public boolean isDonor;
    public long createdAt;
    public long updatedAt;
    public long deletedAt;

    /**
     * Creates a new User object.
     */
    public User() {
        // Required empty public constructor (to allow instantiation).
    }

    @Exclude
    public static User fromMap(HashMap<String, Object> userMap) {
        User user = new User();
        user.email = userMap.get(PROPERTY_EMAIL).toString();
        user.username = userMap.get(PROPERTY_USERNAME).toString();
        user.photoUrl = userMap.get(PROPERTY_PHOTO_URL).toString();
        user.firstName = userMap.get(PROPERTY_FIRST_NAME).toString();
        user.lastName = userMap.get(PROPERTY_LAST_NAME).toString();
        user.fullName = user.firstName + " " + user.lastName;
        user.phone = Long.valueOf(userMap.get(PROPERTY_PHONE).toString());
        user.gender = userMap.get(PROPERTY_GENDER).toString();
        user.birthday = Long.valueOf(userMap.get(PROPERTY_BIRTHDAY).toString());
        user.country = VALUE_COUNTRY_REPUBLICA_DOMINICANA;
        user.province = userMap.get(PROPERTY_PROVINCE).toString();
        user.hospital = Hospital.fromMap((HashMap<String, Object>) userMap.get(PROPERTY_HOSPITAL));
        user.bloodType = userMap.get(PROPERTY_BLOOD_TYPE).toString();
        user.isDonor = Boolean.valueOf(userMap.get(PROPERTY_IS_DONOR).toString());
        user.createdAt = Long.valueOf(userMap.get(PROPERTY_CREATED_AT).toString());
        user.updatedAt = Long.valueOf(userMap.get(PROPERTY_UPDATED_AT).toString());
        user.deletedAt = Long.valueOf(userMap.get(PROPERTY_DELETED_AT).toString());
        return user;
    }

    @Exclude
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put(PROPERTY_EMAIL, email);
        userMap.put(PROPERTY_USERNAME, username);
        userMap.put(PROPERTY_PHOTO_URL, photoUrl);
        userMap.put(PROPERTY_FIRST_NAME, firstName);
        userMap.put(PROPERTY_LAST_NAME, lastName);
        userMap.put(PROPERTY_FULL_NAME, fullName);
        userMap.put(PROPERTY_PHONE, phone);
        userMap.put(PROPERTY_GENDER, gender);
        userMap.put(PROPERTY_BIRTHDAY, birthday);
        userMap.put(PROPERTY_COUNTRY, country);
        userMap.put(PROPERTY_PROVINCE, province);
        userMap.put(PROPERTY_HOSPITAL, hospital.toMap());
        userMap.put(PROPERTY_BLOOD_TYPE, bloodType);
        userMap.put(PROPERTY_IS_DONOR, isDonor);
        userMap.put(PROPERTY_CREATED_AT, createdAt);
        userMap.put(PROPERTY_UPDATED_AT, updatedAt);
        userMap.put(PROPERTY_DELETED_AT, deletedAt);
        return userMap;
    }

    @IgnoreExtraProperties
    public static final class Hospital {

        /**
         * Properties of a hospital.
         */
        public static final String PROPERTY_NAME = "name";
        public static final String PROPERTY_LATITUDE = "latitude";
        public static final String PROPERTY_LONGITUDE = "longitude";

        /**
         * Fields of the hospital.
         */
        public String name;
        public double latitude;
        public double longitude;

        /**
         * Creates a new Hospital object.
         */
        public Hospital() {
            // Required empty public constructor (to allow instantiation).
        }

        @Exclude
        public static Hospital fromMap(HashMap<String, Object> hospitalMap) {
            Hospital hospital = new Hospital();
            hospital.name = hospitalMap.get(PROPERTY_NAME).toString();
            hospital.latitude = Double.valueOf(hospitalMap.get(PROPERTY_LATITUDE).toString());
            hospital.longitude = Double.valueOf(hospitalMap.get(PROPERTY_LONGITUDE).toString());
            return hospital;
        }

        @Exclude
        public HashMap<String, Object> toMap() {
            HashMap<String, Object> hospitalMap = new HashMap<>();
            hospitalMap.put(PROPERTY_NAME, name);
            hospitalMap.put(PROPERTY_LATITUDE, latitude);
            hospitalMap.put(PROPERTY_LONGITUDE, longitude);
            return hospitalMap;
        }
    }
}
