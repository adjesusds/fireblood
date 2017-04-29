package com.arieldiax.codelab.fireblood.models.firebase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {

    /**
     * Child node of a user.
     */
    public static final String CHILD_NODE = "users";

    /**
     * Properties of a user.
     */
    public static final String PROPERTY_EMAIL = "email";
    public static final String PROPERTY_USERNAME = "username";
    public static final String PROPERTY_FIRST_NAME = "firstName";
    public static final String PROPERTY_LAST_NAME = "lastName";
    public static final String PROPERTY_PHONE = "phone";
    public static final String PROPERTY_GENDER = "gender";
    public static final String PROPERTY_BIRTHDAY = "birthday";
    public static final String PROPERTY_PROVINCE = "province";
    public static final String PROPERTY_HOSPITAL = "hospital";
    public static final String PROPERTY_HOSPITAL_NAME = "name";
    public static final String PROPERTY_HOSPITAL_LATITUDE = "latitude";
    public static final String PROPERTY_HOSPITAL_LONGITUDE = "longitude";
    public static final String PROPERTY_BLOOD_TYPE = "bloodType";
    public static final String PROPERTY_IS_DONOR = "isDonor";

    /**
     * Values of a gender.
     */
    public static final String VALUE_GENDER_FEMALE = "female";
    public static final String VALUE_GENDER_MALE = "male";

    /**
     * Fields of the user.
     */
    public String email;
    public String username;
    public String firstName;
    public String lastName;
    public String phone;
    public String gender;
    public long birthday;
    public String province;
    public Map<String, Object> hospital;
    public String bloodType;
    public boolean isDonor;

    /**
     * Creates a new User object.
     */
    public User() {
        // Required empty public constructor (to allow instantiation).
    }

    /**
     * Creates a new User object.
     *
     * @param email     Email of the user.
     * @param username  Username of the user.
     * @param firstName First name of the user.
     * @param lastName  Last name of the user.
     * @param phone     Phone of the user.
     * @param gender    Gender of the user.
     * @param birthday  Birthday of the user.
     * @param province  Province of the user.
     * @param hospital  Hospital of the user.
     * @param bloodType Blood type of the user.
     * @param isDonor   Whether or not the user is a donor.
     */
    public User(String email, String username, String firstName, String lastName, String phone, String gender, long birthday, String province, HashMap<String, Object> hospital, String bloodType, boolean isDonor) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.gender = gender;
        this.birthday = birthday;
        this.province = province;
        this.hospital = hospital;
        this.bloodType = bloodType;
        this.isDonor = isDonor;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> user = new HashMap<>();
        user.put(PROPERTY_EMAIL, email);
        user.put(PROPERTY_USERNAME, username);
        user.put(PROPERTY_FIRST_NAME, firstName);
        user.put(PROPERTY_LAST_NAME, lastName);
        user.put(PROPERTY_PHONE, phone);
        user.put(PROPERTY_GENDER, gender);
        user.put(PROPERTY_BIRTHDAY, birthday);
        user.put(PROPERTY_PROVINCE, province);
        user.put(PROPERTY_HOSPITAL, hospital);
        user.put(PROPERTY_BLOOD_TYPE, bloodType);
        user.put(PROPERTY_IS_DONOR, isDonor);
        return user;
    }
}
