package de.dennisguse.opentracks.data.models;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.JsonObject;

import java.time.Duration;
import java.time.Instant;

import de.dennisguse.opentracks.data.adapters.Gson_DurationTypeAdapter;
import de.dennisguse.opentracks.data.adapters.Gson_InstantTypeAdapter;
import de.dennisguse.opentracks.data.interfaces.JSONSerializable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.time.Duration;
import java.time.Instant;

import de.dennisguse.opentracks.data.adapters.Gson_DurationTypeAdapter;
import de.dennisguse.opentracks.data.adapters.Gson_InstantTypeAdapter;
import de.dennisguse.opentracks.data.interfaces.JSONSerializable;

public class UserModel {

    // Attributes of User class

    private String nickname; //User key that identifies user
    private String country;
    private long dateOfBirth;
    private String gender;
    private int height;
    private int weight;
    private boolean socialAllow; //Bool flag for leaderboard appearance permission
    private String profilePicURL; //To be replaced by firestore path - https://

    // Default Constructor

    public UserModel() {

        this.nickname = "";
        this.country = "";
        this.dateOfBirth = 0;
        this.gender = "";
        this.height = 0;
        this.weight = 0;
        this.socialAllow = false;
        this.profilePicURL = "";
    }

    // Constructor

    /**
     * Constructs a new Car object with the given make, model, and year.
     *
     * @param nickname The nickname / username.
     * @param country The country of user / not based on tracking.
     * @param dateOfBirth The DOB.
     * @param gender User gender.
     * @param height User height unit default.
     * @param weight User weight unit default.
     * @param socialAllow Flags permission to share on leaderboard
     * @param profilePicURL Stores path to blob.
     */
    private static final String SHARED_PREFS_NAME = "UserPrefs";
    private static final String PREF_SOCIAL_ALLOW = "socialAllow";
    private transient Context context;

    // Default Constructor

    public UserModel() {

        this.nickname = "";
        this.country = "";
        this.dateOfBirth = 0;
        this.gender = "";
        this.height = 0;
        this.weight = 0;
        this.socialAllow = false;
        this.profilePicURL = "";
    }

    // Constructor

    /**
     * Constructs a new Car object with the given make, model, and year.
     *
     * @param nickname The nickname / username.
     * @param country The country of user / not based on tracking.
     * @param dateOfBirth The DOB.
     * @param gender User gender.
     * @param height User height unit default.
     * @param weight User weight unit default.
     * @param socialAllow Flags permission to share on leaderboard
     * @param profilePicURL Stores path to blob.
     */
    public UserModel(String nickname, String country, long dateOfBirth, String gender, int height, int weight, boolean socialAllow, String profilePicURL) {

        this.nickname = nickname;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.socialAllow = socialAllow;
        this.profilePicURL = profilePicURL;
    }

    // Methods

    public UserModel(String nickname, String country, long dateOfBirth, String gender, int height, int weight) {

        this.nickname = nickname;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }


    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public void saveSharingPreference(boolean socialAllow) {
        this.socialAllow = socialAllow;
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_SOCIAL_ALLOW, socialAllow);
        editor.apply();
    }

    // Method to get the user's sharing preference
    public boolean getSharingPreference() {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        // Default value is false if not set
        return prefs.getBoolean(PREF_SOCIAL_ALLOW, false);
    }

    public UserModel(String nickname, String country, long dateOfBirth, String gender, int height, int weight) {

        this.nickname = nickname;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }

    // Methods

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isSocialAllow() {
        return socialAllow;
    }
    public void setSocialAllow(boolean socialAllow) {
        this.socialAllow = socialAllow;
    }

    public String getPictureURL() {
        return profilePicURL;
    }
    public void setPictureURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public JsonObject toJSON() {
        // Create a GsonBuilder and configure it to serialize special floating point values
        Gson gson = new GsonBuilder()
                .serializeSpecialFloatingPointValues()
                .registerTypeAdapter(Instant.class, new Gson_InstantTypeAdapter())
                .registerTypeAdapter(Duration.class, new Gson_DurationTypeAdapter())
                .create();

        // Convert the object to JsonObject using Gson
        String jsonString = gson.toJson(this);
        return gson.fromJson(jsonString, JsonObject.class);
    }

}
